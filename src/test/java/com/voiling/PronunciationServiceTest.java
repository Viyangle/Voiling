package com.voiling;

import com.voiling.data.InMemoryUserDictionaryRepository;
import com.voiling.model.PronunciationResult;
import com.voiling.nlp.SimpleKanaTokenizerManager;
import com.voiling.service.PronunciationService;
import com.voiling.translit.CnPhoneticEngine;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PronunciationServiceTest {
    @Test
    void convertsKanaToCnPhonetic() {
        PronunciationService service = new PronunciationService(
                new SimpleKanaTokenizerManager(),
                new CnPhoneticEngine(),
                new InMemoryUserDictionaryRepository()
        );

        PronunciationResult result = service.convert("きみ");
        assertEquals("きみ", result.kanaReading());
        assertEquals("ki米", result.cnPhonetic());
    }

    @Test
    void userDictionaryOverridesRuleEngine() {
        InMemoryUserDictionaryRepository repository = new InMemoryUserDictionaryRepository();
        repository.saveOrUpdate("きみ", "自定义拟音");

        PronunciationService service = new PronunciationService(
                new SimpleKanaTokenizerManager(),
                new CnPhoneticEngine(),
                repository
        );

        PronunciationResult result = service.convert("きみ");
        assertEquals("自定义拟音", result.cnPhonetic());
    }
}
