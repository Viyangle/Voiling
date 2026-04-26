package com.voiling.model;

import java.util.List;

public class PronunciationResult {
    private final String originalText;
    private final String kanaLine;
    private final String cnPhoneticLine;
    private final List<TokenPronunciation> tokens;

    public PronunciationResult(String originalText, String kanaLine, String cnPhoneticLine, List<TokenPronunciation> tokens) {
        this.originalText = originalText;
        this.kanaLine = kanaLine;
        this.cnPhoneticLine = cnPhoneticLine;
        this.tokens = tokens;
    }

    public String getOriginalText() {
        return originalText;
    }

    public String getKanaLine() {
        return kanaLine;
    }

    public String getCnPhoneticLine() {
        return cnPhoneticLine;
    }

    public List<TokenPronunciation> getTokens() {
        return tokens;
    }
}
