package com.voiling;

import com.voiling.translit.KanaConverter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KanaConverterTest {
    @Test
    void convertsKatakanaToHiragana() {
        assertEquals("\u304b\u305f\u304b\u306a", KanaConverter.katakanaToHiragana("\u30ab\u30bf\u30ab\u30ca"));
    }
}
