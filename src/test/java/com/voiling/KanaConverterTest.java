package com.voiling;

import com.voiling.translit.KanaConverter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KanaConverterTest {
    @Test
    void convertsKatakanaToHiragana() {
        assertEquals("かたかな", KanaConverter.katakanaToHiragana("カタカナ"));
    }
}
