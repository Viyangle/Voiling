package com.voiling.translit;

import java.util.HashMap;
import java.util.Map;

public class CnPhoneticEngine {
    private static final Map<String, String> DIGRAPH_MAP = new HashMap<>();
    private static final Map<Character, String> KANA_MAP = new HashMap<>();

    static {
        DIGRAPH_MAP.put("\u304d\u3083", "kya"); DIGRAPH_MAP.put("\u304d\u3085", "kyu"); DIGRAPH_MAP.put("\u304d\u3087", "kyo");
        DIGRAPH_MAP.put("\u3057\u3083", "sha"); DIGRAPH_MAP.put("\u3057\u3085", "shu"); DIGRAPH_MAP.put("\u3057\u3087", "sho");
        DIGRAPH_MAP.put("\u3061\u3083", "cha"); DIGRAPH_MAP.put("\u3061\u3085", "chu"); DIGRAPH_MAP.put("\u3061\u3087", "cho");
        DIGRAPH_MAP.put("\u306b\u3083", "nya"); DIGRAPH_MAP.put("\u306b\u3085", "nyu"); DIGRAPH_MAP.put("\u306b\u3087", "nyo");
        DIGRAPH_MAP.put("\u3072\u3083", "hya"); DIGRAPH_MAP.put("\u3072\u3085", "hyu"); DIGRAPH_MAP.put("\u3072\u3087", "hyo");
        DIGRAPH_MAP.put("\u307f\u3083", "mya"); DIGRAPH_MAP.put("\u307f\u3085", "myu"); DIGRAPH_MAP.put("\u307f\u3087", "myo");
        DIGRAPH_MAP.put("\u308a\u3083", "rya"); DIGRAPH_MAP.put("\u308a\u3085", "ryu"); DIGRAPH_MAP.put("\u308a\u3087", "ryo");
        DIGRAPH_MAP.put("\u304e\u3083", "gya"); DIGRAPH_MAP.put("\u304e\u3085", "gyu"); DIGRAPH_MAP.put("\u304e\u3087", "gyo");
        DIGRAPH_MAP.put("\u3058\u3083", "ja");  DIGRAPH_MAP.put("\u3058\u3085", "ju");  DIGRAPH_MAP.put("\u3058\u3087", "jo");
        DIGRAPH_MAP.put("\u3073\u3083", "bya"); DIGRAPH_MAP.put("\u3073\u3085", "byu"); DIGRAPH_MAP.put("\u3073\u3087", "byo");
        DIGRAPH_MAP.put("\u3074\u3083", "pya"); DIGRAPH_MAP.put("\u3074\u3085", "pyu"); DIGRAPH_MAP.put("\u3074\u3087", "pyo");
    }

    static {
        put('\u3042', "a");  put('\u3044', "i");   put('\u3046', "u");  put('\u3048', "e");  put('\u304a', "o");
        put('\u304b', "ka"); put('\u304d', "ki");  put('\u304f', "ku"); put('\u3051', "ke"); put('\u3053', "ko");
        put('\u3055', "sa"); put('\u3057', "shi"); put('\u3059', "su"); put('\u305b', "se"); put('\u305d', "so");
        put('\u305f', "ta"); put('\u3061', "chi"); put('\u3064', "tsu");put('\u3066', "te"); put('\u3068', "to");
        put('\u306a', "na"); put('\u306b', "ni");  put('\u306c', "nu"); put('\u306d', "ne"); put('\u306e', "no");
        put('\u306f', "ha"); put('\u3072', "hi");  put('\u3075', "fu"); put('\u3078', "he"); put('\u307b', "ho");
        put('\u307e', "ma"); put('\u307f', "mi");  put('\u3080', "mu"); put('\u3081', "me"); put('\u3082', "mo");
        put('\u3084', "ya"); put('\u3086', "yu");  put('\u3088', "yo");
        put('\u3089', "ra"); put('\u308a', "ri");  put('\u308b', "ru"); put('\u308c', "re"); put('\u308d', "ro");
        put('\u308f', "wa"); put('\u3092', "o");   put('\u3093', "n");
        put('\u304c', "ga"); put('\u304e', "gi");  put('\u3050', "gu"); put('\u3052', "ge"); put('\u3054', "go");
        put('\u3056', "za"); put('\u3058', "ji");  put('\u305a', "zu"); put('\u305c', "ze"); put('\u305e', "zo");
        put('\u3060', "da"); put('\u3062', "ji");  put('\u3065', "zu"); put('\u3067', "de"); put('\u3069', "do");
        put('\u3070', "ba"); put('\u3073', "bi");  put('\u3076', "bu"); put('\u3079', "be"); put('\u307c', "bo");
        put('\u3071', "pa"); put('\u3074', "pi");  put('\u3077', "pu"); put('\u307a', "pe"); put('\u307d', "po");
        put('\u3094', "vu");
        put('\u3041', "a");  put('\u3043', "i");   put('\u3045', "u");  put('\u3047', "e");  put('\u3049', "o");
    }

    public String toCnPhonetic(String hiragana) {
        if (hiragana == null || hiragana.isBlank()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < hiragana.length()) {
            char current = hiragana.charAt(i);

            if (current == '\u3063') {
                String next = peekSyllable(hiragana, i + 1);
                String cons = leadingConsonant(next);
                sb.append(cons.isEmpty() ? "tsu" : cons);
                i++;
                continue;
            }

            if (current == '\u30fc') {
                char vowel = trailingVowel(sb.toString());
                if (vowel != '\0') {
                    sb.append(vowel);
                }
                i++;
                continue;
            }

            if (current == '\u3093') {
                String next = peekSyllable(hiragana, i + 1);
                sb.append(next.startsWith("b") || next.startsWith("m") || next.startsWith("p") ? "m" : "n");
                i++;
                continue;
            }

            if (i + 1 < hiragana.length() && isSmallY(hiragana.charAt(i + 1))) {
                String digraph = hiragana.substring(i, i + 2);
                String mapped = DIGRAPH_MAP.get(digraph);
                if (mapped != null) {
                    sb.append(mapped);
                    i += 2;
                    continue;
                }
            }

            sb.append(KANA_MAP.getOrDefault(current, String.valueOf(current)));
            i++;
        }
        return sb.toString();
    }

    private static String peekSyllable(String text, int index) {
        if (index >= text.length()) {
            return "";
        }
        if (index + 1 < text.length() && isSmallY(text.charAt(index + 1))) {
            String mapped = DIGRAPH_MAP.get(text.substring(index, index + 2));
            if (mapped != null) {
                return mapped;
            }
        }
        return KANA_MAP.getOrDefault(text.charAt(index), "");
    }

    private static String leadingConsonant(String syllable) {
        if (syllable == null || syllable.isEmpty()) {
            return "";
        }
        char c = syllable.charAt(0);
        return isVowel(c) ? "" : String.valueOf(c);
    }

    private static char trailingVowel(String text) {
        for (int i = text.length() - 1; i >= 0; i--) {
            char c = text.charAt(i);
            if (isVowel(c)) {
                return c;
            }
        }
        return '\0';
    }

    private static boolean isSmallY(char c) {
        return c == '\u3083' || c == '\u3085' || c == '\u3087';
    }

    private static boolean isVowel(char c) {
        return c == 'a' || c == 'i' || c == 'u' || c == 'e' || c == 'o';
    }

    private static void put(char kana, String value) {
        KANA_MAP.put(kana, value);
    }
}
