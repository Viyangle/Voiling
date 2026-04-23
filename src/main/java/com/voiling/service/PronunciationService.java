package com.voiling.service;

import com.voiling.data.UserDictionaryRepository;
import com.voiling.model.PronunciationResult;
import com.voiling.nlp.TokenizerManager;
import com.voiling.translit.CnPhoneticEngine;

public class PronunciationService {
    private final TokenizerManager tokenizerManager;
    private final CnPhoneticEngine cnPhoneticEngine;
    private final UserDictionaryRepository userDictionaryRepository;

    public PronunciationService(
            TokenizerManager tokenizerManager,
            CnPhoneticEngine cnPhoneticEngine,
            UserDictionaryRepository userDictionaryRepository
    ) {
        this.tokenizerManager = tokenizerManager;
        this.cnPhoneticEngine = cnPhoneticEngine;
        this.userDictionaryRepository = userDictionaryRepository;
    }

    public PronunciationResult convert(String originalText) {
        String text = originalText == null ? "" : originalText.trim();
        if (text.isEmpty()) {
            return new PronunciationResult("", "", "");
        }
        String kanaReading = tokenizerManager.toKanaReading(text);
        String cnPhonetic = userDictionaryRepository.findByOriginal(text)
                .orElseGet(() -> cnPhoneticEngine.toCnPhonetic(kanaReading));
        return new PronunciationResult(text, kanaReading, cnPhonetic);
    }
}
