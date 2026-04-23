package com.voiling.nlp;

import com.voiling.translit.KanaConverter;

/**
 * MVP tokenizer for local development.
 * This version only normalizes kana and keeps kanji unchanged.
 * Replace with Kuromoji-based implementation in Android integration phase.
 */
public class SimpleKanaTokenizerManager implements TokenizerManager {
    @Override
    public String toKanaReading(String japaneseText) {
        if (japaneseText == null || japaneseText.isBlank()) {
            return "";
        }
        return KanaConverter.katakanaToHiragana(japaneseText.trim());
    }
}
