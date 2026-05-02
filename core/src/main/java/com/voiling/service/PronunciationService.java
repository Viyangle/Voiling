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
    private static final String[][] HANZI_SYLLABLE_MAP = new String[][]{
            {"xiu", "\u4fee"}, {"xia", "\u590f"}, {"xio", "\u4f11"},
            {"qiu", "\u79cb"}, {"qia", "\u6070"}, {"qio", "\u4e18"},
            {"jiu", "\u4e45"}, {"jia", "\u52a0"}, {"jio", "\u7126"},
            {"ang", "\u6602"}, {"eng", "\u4ea8"}, {"ong", "\u7fc1"},
            {"an", "\u5b89"}, {"en", "\u6069"}, {"ai", "\u7231"}, {"ao", "\u5965"},
            {"kou", "\u53e3"}, {"gou", "\u8d2d"}, {"sou", "\u641c"}, {"hou", "\u4faf"}, {"lou", "\u697c"},
            {"rou", "\u8089"}, {"mou", "\u67d0"}, {"nou", "\u8bfa"}, {"you", "\u54df"}, {"zou", "\u90b9"},
            {"dou", "\u90fd"}, {"tou", "\u5077"},
            {"liu", "\u6d41"}, {"lia", "\u4e3d"}, {"lio", "\u6599"},
            {"niu", "\u725b"}, {"nia", "\u5a18"}, {"nio", "\u9e1f"},
            {"miu", "\u8c2c"}, {"mia", "\u5999"}, {"mio", "\u55b5"},
            {"biu", "\u6807"}, {"bia", "\u98d9"}, {"bio", "\u8198"},
            {"piu", "\u98d8"}, {"pia", "\u7784"}, {"pio", "\u98d8"},
            {"zei", "\u8d3c"}, {"dei", "\u5f97"}, {"tei", "\u7279"}, {"hei", "\u563f"},
            {"xi", "\u897f"}, {"qi", "\u4e03"}, {"ci", "\u6b21"}, {"ji", "\u673a"},
            {"shi", "\u897f"}, {"chi", "\u4e03"}, {"tsu", "\u6b21"},
            {"ou", "\u6b27"}, {"ei", "\u8bf6"},
            {"ka", "\u5361"}, {"ki", "\u57fa"}, {"ku", "\u9177"}, {"ke", "\u514b"}, {"ko", "\u53e3"},
            {"ga", "\u560e"}, {"gi", "\u5409"}, {"gu", "\u8c37"}, {"ge", "\u54e5"}, {"go", "\u8d2d"},
            {"sa", "\u8428"}, {"si", "\u897f"}, {"su", "\u82cf"}, {"se", "\u745f"}, {"so", "\u7d22"},
            {"za", "\u6742"}, {"zi", "\u5b50"}, {"zu", "\u65cf"}, {"ze", "\u6cfd"}, {"zo", "\u5b97"},
            {"da", "\u8fbe"}, {"di", "\u8fea"}, {"du", "\u90fd"}, {"de", "\u5fb7"}, {"do", "\u591a"},
            {"ta", "\u4ed6"}, {"ti", "\u63d0"}, {"tu", "\u56fe"}, {"te", "\u7279"}, {"to", "\u6258"},
            {"na", "\u90a3"}, {"ni", "\u5c3c"}, {"nu", "\u5974"}, {"ne", "\u5185"}, {"no", "\u8bfa"},
            {"ha", "\u54c8"}, {"hi", "\u559c"}, {"hu", "\u547c"}, {"he", "\u559d"}, {"ho", "\u970d"},
            {"ma", "\u5417"}, {"mi", "\u7c73"}, {"mu", "\u6728"}, {"me", "\u7f8e"}, {"mo", "\u9ed8"},
            {"ya", "\u5440"}, {"yu", "\u4f18"}, {"yo", "\u54df"},
            {"la", "\u5566"}, {"li", "\u91cc"}, {"lu", "\u8def"}, {"le", "\u4e50"}, {"lo", "\u54e6"},
            {"wa", "\u54c7"}, {"wo", "\u6211"},
            {"ja", "\u5609"}, {"ju", "\u5c45"}, {"jo", "\u4e54"},
            {"fa", "\u6cd5"}, {"fi", "\u83f2"}, {"fu", "\u798f"}, {"fe", "\u98de"}, {"fo", "\u4f5b"},
            {"a", "\u554a"}, {"i", "\u8863"}, {"u", "\u4e4c"}, {"e", "\u989d"}, {"o", "\u54e6"},
            {"n", "\u6069"}, {"m", "\u59c6"}
    };

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
            return new PronunciationResult("", "", "", "", new ArrayList<>());
        }

        List<TokenPronunciation> tokens = new ArrayList<>();
        StringBuilder kanaLine = new StringBuilder();
        StringBuilder phoneticLine = new StringBuilder();
        StringBuilder fastPhoneticLine = new StringBuilder();
        String[] lines = text.split("\\R", -1);

        for (int lineIndex = 0; lineIndex < lines.length; lineIndex++) {
            if (lineIndex > 0) {
                kanaLine.append('\n');
                phoneticLine.append('\n');
                fastPhoneticLine.append('\n');
            }

            List<TokenReading> tokenReadings = tokenizerManager.tokenizeWithReading(lines[lineIndex]);
            for (int i = 0; i < tokenReadings.size(); i++) {
                TokenReading token = tokenReadings.get(i);
                String surface = token.getSurface();
                String hiraReading = applySingingReading(surface, KanaConverter.katakanaToHiragana(token.getReading()));
                String ruleBased = cnPhoneticEngine.toCnPhonetic(hiraReading);
                String override = userDictRepository.findBySurface(surface);
                String finalPhonetic = override == null ? ruleBased : override;
                String fastPhonetic;
                if (isLatinSurface(surface)) {
                    fastPhonetic = surface;
                } else if (override == null) {
                    fastPhonetic = toFastSingAlong(finalPhonetic);
                } else {
                    fastPhonetic = finalPhonetic;
                }

                tokens.add(new TokenPronunciation(surface, hiraReading, finalPhonetic));

                if (i > 0) {
                    kanaLine.append(' ');
                    phoneticLine.append(' ');
                    fastPhoneticLine.append(' ');
                }
                kanaLine.append(hiraReading);
                phoneticLine.append(finalPhonetic);
                fastPhoneticLine.append(fastPhonetic);
            }
        }

        return new PronunciationResult(
                text,
                kanaLine.toString(),
                phoneticLine.toString(),
                fastPhoneticLine.toString(),
                tokens
        );
    }

    private String applySingingReading(String surface, String hiraReading) {
        if (surface == null || hiraReading == null) {
            return hiraReading == null ? "" : hiraReading;
        }
        if ("は".equals(surface) && "は".equals(hiraReading)) {
            return "わ";
        }
        if ("へ".equals(surface) && "へ".equals(hiraReading)) {
            return "え";
        }
        if ("を".equals(surface) && "を".equals(hiraReading)) {
            return "お";
        }
        return hiraReading;
    }

    private String toFastSingAlong(String phonetic) {
        if (phonetic == null || phonetic.isBlank()) {
            return "";
        }
        String fast = phonetic.toLowerCase();
        // Romaji -> Mandarin-pinyin-like sing-along hints
        fast = fast.replace("shi", "xi")
                .replace("chi", "qi")
                .replace("tsu", "ci")
                .replace("rya", "lia")
                .replace("ryu", "liu")
                .replace("ryo", "lio")
                .replace("kya", "qia")
                .replace("kyu", "qiu")
                .replace("kyo", "qio")
                .replace("gya", "jia")
                .replace("gyu", "jiu")
                .replace("gyo", "jio")
                .replace("nya", "nia")
                .replace("nyu", "niu")
                .replace("nyo", "nio")
                .replace("hya", "xia")
                .replace("hyu", "xiu")
                .replace("hyo", "xio")
                .replace("mya", "mia")
                .replace("myu", "miu")
                .replace("myo", "mio")
                .replace("bya", "bia")
                .replace("byu", "biu")
                .replace("byo", "bio")
                .replace("pya", "pia")
                .replace("pyu", "piu")
                .replace("pyo", "pio")
                .replace("ji", "ji")
                .replace("fu", "fu")
                .replace("ja", "jia")
                .replace("ju", "ju")
                .replace("jo", "jio")
                .replace("sha", "xia")
                .replace("shu", "xiu")
                .replace("sho", "xio")
                .replace("cha", "qia")
                .replace("chu", "qiu")
                .replace("cho", "qio")
                .replace("ra", "la")
                .replace("ri", "li")
                .replace("ru", "lu")
                .replace("re", "lei")
                .replace("ro", "lo")
                .replace("za", "za")
                .replace("zu", "zu")
                .replace("ze", "zei")
                .replace("zo", "zou")
                .replace("da", "da")
                .replace("de", "dei")
                .replace("do", "dou")
                .replace("ta", "ta")
                .replace("te", "tei")
                .replace("to", "tou")
                .replace("he", "hei")
                .replace("ho", "hou");
        // Mild voicing tendency for sing-along speed: t -> d.
        fast = fast.replace("t", "d");
        fast = fast.replaceAll("([bcdfghjklmpqrstvwxyz])\\1+", "$1");
        fast = fast.replaceAll("([aeiou])\\1+", "$1");
        fast = fast.replaceAll("desu\\b", "des");
        fast = fast.replaceAll("masu\\b", "mas");
        fast = fast.replaceAll("e\\b", "ei");
        // Japanese "o" is often rounded toward "ou" in singing mouth shape.
        fast = fast.replaceAll("([bcdfghjklmnpqrstvwxyz])o(?!u)", "$1ou");
        fast = fast.replaceAll("o\\b", "ou");
        fast = fast.replace("wa", "ua")
                .replace("yu", "you")
                .replace("yo", "you");
        return toHanziApprox(fast);
    }

    private boolean isLatinSurface(String text) {
        return text != null && text.matches(".*[A-Za-z].*");
    }

    private String toHanziApprox(String text) {
        StringBuilder out = new StringBuilder();
        int i = 0;
        while (i < text.length()) {
            char c = text.charAt(i);
            if (!Character.isLetter(c)) {
                out.append(c);
                i++;
                continue;
            }

            String matched = null;
            String mapped = null;
            for (String[] pair : HANZI_SYLLABLE_MAP) {
                String syllable = pair[0];
                if (text.regionMatches(true, i, syllable, 0, syllable.length())) {
                    matched = syllable;
                    mapped = pair[1];
                    break;
                }
            }

            if (matched != null) {
                out.append(mapped);
                i += matched.length();
            } else {
                out.append(letterFallback(c));
                i++;
            }
        }
        return out.toString();
    }

    private String letterFallback(char c) {
        return switch (Character.toLowerCase(c)) {
            case 'a' -> "\u554a";
            case 'b' -> "\u5e03";
            case 'c' -> "\u6b21";
            case 'd' -> "\u5fb7";
            case 'e' -> "\u8bf6";
            case 'f' -> "\u5f17";
            case 'g' -> "\u683c";
            case 'h' -> "\u559d";
            case 'i' -> "\u8863";
            case 'j' -> "\u9e21";
            case 'k' -> "\u514b";
            case 'l' -> "\u52d2";
            case 'm' -> "\u59c6";
            case 'n' -> "\u6069";
            case 'o' -> "\u6b27";
            case 'p' -> "\u666e";
            case 'q' -> "\u4e03";
            case 'r' -> "\u65e5";
            case 's' -> "\u4e1d";
            case 't' -> "\u7279";
            case 'u' -> "\u4e4c";
            case 'v' -> "\u7ef4";
            case 'w' -> "\u543e";
            case 'x' -> "\u897f";
            case 'y' -> "\u4f0a";
            case 'z' -> "\u5b50";
            default -> String.valueOf(c);
        };
    }
}
