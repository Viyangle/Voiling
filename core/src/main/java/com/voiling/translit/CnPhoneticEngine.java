package com.voiling.translit;

import java.util.HashMap;
import java.util.Map;

public class CnPhoneticEngine {
    private static final Map<String, String> DIGRAPH_MAP = new HashMap<>();
    private static final Map<Character, String> KANA_MAP = new HashMap<>();

    static {
        DIGRAPH_MAP.put("きゃ", "kya");
        DIGRAPH_MAP.put("きゅ", "kyu");
        DIGRAPH_MAP.put("きょ", "kyo");
        DIGRAPH_MAP.put("しゃ", "sha");
        DIGRAPH_MAP.put("しゅ", "shu");
        DIGRAPH_MAP.put("しょ", "sho");
        DIGRAPH_MAP.put("ちゃ", "cha");
        DIGRAPH_MAP.put("ちゅ", "chu");
        DIGRAPH_MAP.put("ちょ", "cho");
        DIGRAPH_MAP.put("にゃ", "nya");
        DIGRAPH_MAP.put("にゅ", "nyu");
        DIGRAPH_MAP.put("にょ", "nyo");
        DIGRAPH_MAP.put("ひゃ", "hya");
        DIGRAPH_MAP.put("ひゅ", "hyu");
        DIGRAPH_MAP.put("ひょ", "hyo");
        DIGRAPH_MAP.put("みゃ", "mya");
        DIGRAPH_MAP.put("みゅ", "myu");
        DIGRAPH_MAP.put("みょ", "myo");
        DIGRAPH_MAP.put("りゃ", "rya");
        DIGRAPH_MAP.put("りゅ", "ryu");
        DIGRAPH_MAP.put("りょ", "ryo");
        DIGRAPH_MAP.put("ぎゃ", "gya");
        DIGRAPH_MAP.put("ぎゅ", "gyu");
        DIGRAPH_MAP.put("ぎょ", "gyo");
        DIGRAPH_MAP.put("じゃ", "ja");
        DIGRAPH_MAP.put("じゅ", "ju");
        DIGRAPH_MAP.put("じょ", "jo");
        DIGRAPH_MAP.put("びゃ", "bya");
        DIGRAPH_MAP.put("びゅ", "byu");
        DIGRAPH_MAP.put("びょ", "byo");
        DIGRAPH_MAP.put("ぴゃ", "pya");
        DIGRAPH_MAP.put("ぴゅ", "pyu");
        DIGRAPH_MAP.put("ぴょ", "pyo");

        putKana('あ', "a"); putKana('い', "i"); putKana('う', "u"); putKana('え', "e"); putKana('お', "o");
        putKana('か', "ka"); putKana('き', "ki"); putKana('く', "ku"); putKana('け', "ke"); putKana('こ', "ko");
        putKana('さ', "sa"); putKana('し', "shi"); putKana('す', "su"); putKana('せ', "se"); putKana('そ', "so");
        putKana('た', "ta"); putKana('ち', "chi"); putKana('つ', "tsu"); putKana('て', "te"); putKana('と', "to");
        putKana('な', "na"); putKana('に', "ni"); putKana('ぬ', "nu"); putKana('ね', "ne"); putKana('の', "no");
        putKana('は', "ha"); putKana('ひ', "hi"); putKana('ふ', "fu"); putKana('へ', "he"); putKana('ほ', "ho");
        putKana('ま', "ma"); putKana('み', "mi"); putKana('む', "mu"); putKana('め', "me"); putKana('も', "mo");
        putKana('や', "ya"); putKana('ゆ', "yu"); putKana('よ', "yo");
        putKana('ら', "ra"); putKana('り', "ri"); putKana('る', "ru"); putKana('れ', "re"); putKana('ろ', "ro");
        putKana('わ', "wa"); putKana('を', "o"); putKana('ん', "n");
        putKana('が', "ga"); putKana('ぎ', "gi"); putKana('ぐ', "gu"); putKana('げ', "ge"); putKana('ご', "go");
        putKana('ざ', "za"); putKana('じ', "ji"); putKana('ず', "zu"); putKana('ぜ', "ze"); putKana('ぞ', "zo");
        putKana('だ', "da"); putKana('ぢ', "ji"); putKana('づ', "zu"); putKana('で', "de"); putKana('ど', "do");
        putKana('ば', "ba"); putKana('び', "bi"); putKana('ぶ', "bu"); putKana('べ', "be"); putKana('ぼ', "bo");
        putKana('ぱ', "pa"); putKana('ぴ', "pi"); putKana('ぷ', "pu"); putKana('ぺ', "pe"); putKana('ぽ', "po");
        putKana('ゔ', "vu");
        putKana('ぁ', "a"); putKana('ぃ', "i"); putKana('ぅ', "u"); putKana('ぇ', "e"); putKana('ぉ', "o");
    }

    public String toCnPhonetic(String hiragana) {
        if (hiragana == null || hiragana.isBlank()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < hiragana.length()) {
            char current = hiragana.charAt(i);

            if (current == 'っ') {
                String next = peekSyllable(hiragana, i + 1);
                String cons = leadingConsonant(next);
                sb.append(cons.isEmpty() ? "tsu" : cons);
                i++;
                continue;
            }

            if (current == 'ー') {
                char vowel = trailingVowel(sb.toString());
                if (vowel != '\0') {
                    sb.append(vowel);
                }
                i++;
                continue;
            }

            if (current == 'ん') {
                String next = peekSyllable(hiragana, i + 1);
                if (next.startsWith("b") || next.startsWith("m") || next.startsWith("p")) {
                    sb.append('m');
                } else {
                    sb.append('n');
                }
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
            String digraph = text.substring(index, index + 2);
            String mapped = DIGRAPH_MAP.get(digraph);
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
        return c == 'ゃ' || c == 'ゅ' || c == 'ょ';
    }

    private static boolean isVowel(char c) {
        return c == 'a' || c == 'i' || c == 'u' || c == 'e' || c == 'o';
    }

    private static void putKana(char key, String value) {
        KANA_MAP.put(key, value);
    }
}
