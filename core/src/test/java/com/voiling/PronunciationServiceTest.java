package com.voiling;

import com.voiling.data.InMemoryUserDictRepository;
import com.voiling.model.PronunciationResult;
import com.voiling.nlp.SimpleKanaTokenizerManager;
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
    }
}
