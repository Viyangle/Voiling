package com.voiling;

import com.voiling.data.InMemoryUserDictionaryRepository;
import com.voiling.model.PronunciationResult;
import com.voiling.nlp.SimpleKanaTokenizerManager;
import com.voiling.service.PronunciationService;
import com.voiling.translit.CnPhoneticEngine;

public class App {
    public static void main(String[] args) {
        PronunciationService service = new PronunciationService(
                new SimpleKanaTokenizerManager(),
                new CnPhoneticEngine(),
                new InMemoryUserDictionaryRepository()
        );

        String text = args.length > 0 ? args[0] : "きみのなまえは";
        PronunciationResult result = service.convert(text);

        System.out.println("Original : " + result.originalText());
        System.out.println("Kana     : " + result.kanaReading());
        System.out.println("CN Sound : " + result.cnPhonetic());
    }
}
