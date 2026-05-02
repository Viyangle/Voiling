package com.voiling;

import com.voiling.data.InMemoryUserDictRepository;
import com.voiling.model.PronunciationResult;
import com.voiling.nlp.TokenReading;
import com.voiling.nlp.SimpleKanaTokenizerManager;
import com.voiling.nlp.TokenizerManager;
import com.voiling.service.PronunciationService;
import com.voiling.translit.CnPhoneticEngine;
import com.voiling.util.TextNormalizer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PronunciationServiceTest {
    @Test
    void convertsKanaToCnPhonetic() {
        PronunciationService service = new PronunciationService(
                new SimpleKanaTokenizerManager(),
                new CnPhoneticEngine(),
                new InMemoryUserDictRepository(),
                new TextNormalizer()
        );

        PronunciationResult result = service.convert("\u3053\u3093\u306b\u3061\u306f");
        assertEquals("\u3053\u3093\u306b\u3061\u306f", result.getKanaLine());
        assertEquals("konnichiha", result.getCnPhoneticLine());
        assertEquals("\u53e3\u6069\u5c3c\u4e03\u54c8", result.getFastCnPhoneticLine());
    }

    @Test
    void userDictionaryOverridesRuleEngine() {
        InMemoryUserDictRepository repository = new InMemoryUserDictRepository();
        repository.saveOrUpdate("\u3053\u3093\u306b\u3061\u306f", "custom");

        PronunciationService service = new PronunciationService(
                new SimpleKanaTokenizerManager(),
                new CnPhoneticEngine(),
                repository,
                new TextNormalizer()
        );

        PronunciationResult result = service.convert("\u3053\u3093\u306b\u3061\u306f");
        assertEquals("custom", result.getCnPhoneticLine());
        assertEquals("custom", result.getFastCnPhoneticLine());
    }

    @Test
    void fastModeSimplifiesDensePhoneticForTempo() {
        PronunciationService service = new PronunciationService(
                new SimpleKanaTokenizerManager(),
                new CnPhoneticEngine(),
                new InMemoryUserDictRepository(),
                new TextNormalizer()
        );

        PronunciationResult result = service.convert("\u307e\u3063\u3059\u3050\u3059\u3059\u3080");
        assertEquals("massugususumu", result.getCnPhoneticLine());
        assertEquals("\u5417\u82cf\u8c37\u82cf\u82cf\u6728", result.getFastCnPhoneticLine());
    }

    @Test
    void particleReadingIsAdjustedForSinging() {
        TokenizerManager tokenizer = text -> java.util.List.of(
                new TokenReading("\u79c1", "\u30ef\u30bf\u30b7"),
                new TokenReading("\u306f", "\u30cf")
        );

        PronunciationService service = new PronunciationService(
                tokenizer,
                new CnPhoneticEngine(),
                new InMemoryUserDictRepository(),
                new TextNormalizer()
        );

        PronunciationResult result = service.convert("\u79c1\u306f");
        assertEquals("\u308f\u305f\u3057 \u308f", result.getKanaLine());
        assertEquals("watashi wa", result.getCnPhoneticLine());
    }

    @Test
    void englishTokenIsPreservedInFastMode() {
        TokenizerManager tokenizer = text -> java.util.List.of(new TokenReading("Hello", "Hello"));
        PronunciationService service = new PronunciationService(
                tokenizer,
                new CnPhoneticEngine(),
                new InMemoryUserDictRepository(),
                new TextNormalizer()
        );

        PronunciationResult result = service.convert("Hello");
        assertEquals("Hello", result.getFastCnPhoneticLine());
    }
}
