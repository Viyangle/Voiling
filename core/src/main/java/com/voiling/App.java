package com.voiling;

import com.voiling.data.InMemoryUserDictRepository;
import com.voiling.model.PronunciationResult;
import com.voiling.nlp.SimpleKanaTokenizerManager;
import com.voiling.service.PronunciationService;
import com.voiling.translit.CnPhoneticEngine;
import com.voiling.util.TextNormalizer;

public class App {
    public static void main(String[] args) {
        PronunciationService service = new PronunciationService(
                new SimpleKanaTokenizerManager(),
                new CnPhoneticEngine(),
                new InMemoryUserDictRepository(),
                new TextNormalizer()
        );

        String text = args.length > 0 ? args[0] : "\u3053\u3093\u306b\u3061\u306f";
        PronunciationResult result = service.convert(text);

        System.out.println("Original : " + result.getOriginalText());
        System.out.println("Kana     : " + result.getKanaLine());
        System.out.println("CN Sound : " + result.getCnPhoneticLine());
        System.out.println("FAST     : " + result.getFastCnPhoneticLine());
    }
}
