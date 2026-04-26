package com.voiling.service;

import com.voiling.data.UserDictRepository;
import com.voiling.model.PronunciationResult;
import com.voiling.model.TokenPronunciation;
import com.voiling.nlp.TokenReading;
import com.voiling.nlp.TokenizerManager;
import com.voiling.translit.CnPhoneticEngine;
import com.voiling.translit.KanaConverter;
import com.voiling.util.TextNormalizer;

import java.util.ArrayList;
import java.util.List;

public class PronunciationService {
    private final TokenizerManager tokenizerManager;
    private final CnPhoneticEngine cnPhoneticEngine;
    private final UserDictRepository userDictRepository;
    private final TextNormalizer textNormalizer;

    public PronunciationService(
            TokenizerManager tokenizerManager,
            CnPhoneticEngine cnPhoneticEngine,
            UserDictRepository userDictRepository,
            TextNormalizer textNormalizer
    ) {
        this.tokenizerManager = tokenizerManager;
        this.cnPhoneticEngine = cnPhoneticEngine;
        this.userDictRepository = userDictRepository;
        this.textNormalizer = textNormalizer;
    }

    public PronunciationResult convert(String originalText) {
        String text = textNormalizer.normalize(originalText);
        if (text.isEmpty()) {
            return new PronunciationResult("", "", "", new ArrayList<>());
        }

        List<TokenReading> tokenReadings = tokenizerManager.tokenizeWithReading(text);
        List<TokenPronunciation> tokens = new ArrayList<>(tokenReadings.size());
        StringBuilder kanaLine = new StringBuilder();
        StringBuilder phoneticLine = new StringBuilder();

        for (int i = 0; i < tokenReadings.size(); i++) {
            TokenReading token = tokenReadings.get(i);
            String surface = token.getSurface();
            String hiraReading = KanaConverter.katakanaToHiragana(token.getReading());
            String ruleBased = cnPhoneticEngine.toCnPhonetic(hiraReading);
            String override = userDictRepository.findBySurface(surface);
            String finalPhonetic = override == null ? ruleBased : override;

            tokens.add(new TokenPronunciation(surface, hiraReading, finalPhonetic));

            if (i > 0) {
                kanaLine.append(' ');
                phoneticLine.append(' ');
            }
            kanaLine.append(hiraReading);
            phoneticLine.append(finalPhonetic);
        }

        return new PronunciationResult(text, kanaLine.toString(), phoneticLine.toString(), tokens);
    }
}
