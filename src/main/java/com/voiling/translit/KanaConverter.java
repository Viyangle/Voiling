package com.voiling.translit;

public final class KanaConverter {
    private KanaConverter() {
    }

    public static String katakanaToHiragana(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder(text.length());
        for (char c : text.toCharArray()) {
            if (c >= '\u30A1' && c <= '\u30F6') {
                builder.append((char) (c - 0x60));
            } else {
                builder.append(c);
            }
        }
        return builder.toString();
    }
}
