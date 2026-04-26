package com.voiling.util;

import com.ibm.icu.text.Normalizer2;

public class TextNormalizer {
    private final Normalizer2 normalizer = Normalizer2.getNFKCInstance();

    public String normalize(String text) {
        if (text == null) {
            return "";
        }
        return normalizer.normalize(text).trim();
    }
}
