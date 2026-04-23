package com.voiling.translit;

import java.util.HashMap;
import java.util.Map;

public class CnPhoneticEngine {
    private static final Map<String, String> DIGRAPH_MAP = new HashMap<>();
    private static final Map<Character, String> KANA_MAP = new HashMap<>();

    static {
        DIGRAPH_MAP.put("きゃ", "恰");
        DIGRAPH_MAP.put("きゅ", "Q");
        DIGRAPH_MAP.put("きょ", "Q哦");
        DIGRAPH_MAP.put("しゃ", "夏");
        DIGRAPH_MAP.put("しゅ", "修");
        DIGRAPH_MAP.put("しょ", "修哦");
        DIGRAPH_MAP.put("ちゃ", "恰");
        DIGRAPH_MAP.put("ちゅ", "秋");
        DIGRAPH_MAP.put("ちょ", "秋哦");
        DIGRAPH_MAP.put("にゃ", "尼亚");
        DIGRAPH_MAP.put("にゅ", "牛");
        DIGRAPH_MAP.put("にょ", "尼哦");
        DIGRAPH_MAP.put("ひゃ", "嘿亚");
        DIGRAPH_MAP.put("ひゅ", "休");
        DIGRAPH_MAP.put("ひょ", "嘿哦");
        DIGRAPH_MAP.put("みゃ", "米亚");
        DIGRAPH_MAP.put("みゅ", "谬");
        DIGRAPH_MAP.put("みょ", "米哦");
        DIGRAPH_MAP.put("りゃ", "里亚");
        DIGRAPH_MAP.put("りゅ", "流");
        DIGRAPH_MAP.put("りょ", "里哦");
        DIGRAPH_MAP.put("ぎゃ", "给亚");
        DIGRAPH_MAP.put("ぎゅ", "纠");
        DIGRAPH_MAP.put("ぎょ", "给哦");
        DIGRAPH_MAP.put("じゃ", "贾");
        DIGRAPH_MAP.put("じゅ", "朱");
        DIGRAPH_MAP.put("じょ", "朱哦");
        DIGRAPH_MAP.put("びゃ", "比亚");
        DIGRAPH_MAP.put("びゅ", "比优");
        DIGRAPH_MAP.put("びょ", "比哦");
        DIGRAPH_MAP.put("ぴゃ", "皮亚");
        DIGRAPH_MAP.put("ぴゅ", "皮优");
        DIGRAPH_MAP.put("ぴょ", "皮哦");

        KANA_MAP.put('あ', "啊");
        KANA_MAP.put('い', "衣");
        KANA_MAP.put('う', "乌");
        KANA_MAP.put('え', "诶");
        KANA_MAP.put('お', "哦");
        KANA_MAP.put('か', "卡");
        KANA_MAP.put('き', "ki");
        KANA_MAP.put('く', "库");
        KANA_MAP.put('け', "开");
        KANA_MAP.put('こ', "扣");
        KANA_MAP.put('さ', "撒");
        KANA_MAP.put('し', "西");
        KANA_MAP.put('す', "苏");
        KANA_MAP.put('せ', "塞");
        KANA_MAP.put('そ', "搜");
        KANA_MAP.put('た', "塔");
        KANA_MAP.put('ち', "七");
        KANA_MAP.put('つ', "次");
        KANA_MAP.put('て', "忒");
        KANA_MAP.put('と', "托");
        KANA_MAP.put('な', "那");
        KANA_MAP.put('に', "尼");
        KANA_MAP.put('ぬ', "奴");
        KANA_MAP.put('ね', "内");
        KANA_MAP.put('の', "诺");
        KANA_MAP.put('は', "哈");
        KANA_MAP.put('ひ', "嘿");
        KANA_MAP.put('ふ', "呼");
        KANA_MAP.put('へ', "黑");
        KANA_MAP.put('ほ', "后");
        KANA_MAP.put('ま', "妈");
        KANA_MAP.put('み', "米");
        KANA_MAP.put('む', "木");
        KANA_MAP.put('め', "美");
        KANA_MAP.put('も', "磨");
        KANA_MAP.put('や', "呀");
        KANA_MAP.put('ゆ', "优");
        KANA_MAP.put('よ', "哟");
        KANA_MAP.put('ら', "啦");
        KANA_MAP.put('り', "里");
        KANA_MAP.put('る', "鲁");
        KANA_MAP.put('れ', "类");
        KANA_MAP.put('ろ', "罗");
        KANA_MAP.put('わ', "哇");
        KANA_MAP.put('を', "哦");
        KANA_MAP.put('ん', "嗯");
        KANA_MAP.put('が', "噶");
        KANA_MAP.put('ぎ', "给");
        KANA_MAP.put('ぐ', "古");
        KANA_MAP.put('げ', "给");
        KANA_MAP.put('ご', "够");
        KANA_MAP.put('ざ', "杂");
        KANA_MAP.put('じ', "吉");
        KANA_MAP.put('ず', "祖");
        KANA_MAP.put('ぜ', "贼");
        KANA_MAP.put('ぞ', "走");
        KANA_MAP.put('だ', "达");
        KANA_MAP.put('ぢ', "吉");
        KANA_MAP.put('づ', "祖");
        KANA_MAP.put('で', "得");
        KANA_MAP.put('ど', "多");
        KANA_MAP.put('ば', "吧");
        KANA_MAP.put('び', "比");
        KANA_MAP.put('ぶ', "部");
        KANA_MAP.put('べ', "北");
        KANA_MAP.put('ぼ', "波");
        KANA_MAP.put('ぱ', "啪");
        KANA_MAP.put('ぴ', "皮");
        KANA_MAP.put('ぷ', "普");
        KANA_MAP.put('ぺ', "佩");
        KANA_MAP.put('ぽ', "颇");
        KANA_MAP.put('ー', "—");
    }

    public String toCnPhonetic(String hiragana) {
        if (hiragana == null || hiragana.isBlank()) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        int i = 0;
        while (i < hiragana.length()) {
            char current = hiragana.charAt(i);

            if (current == 'っ') {
                if (i + 1 < hiragana.length()) {
                    String next = String.valueOf(hiragana.charAt(i + 1));
                    String mapped = KANA_MAP.getOrDefault(next.charAt(0), next);
                    result.append(mapped);
                }
                i++;
                continue;
            }

            if (i + 1 < hiragana.length()) {
                String digraph = hiragana.substring(i, i + 2);
                String mappedDigraph = DIGRAPH_MAP.get(digraph);
                if (mappedDigraph != null) {
                    result.append(mappedDigraph);
                    i += 2;
                    continue;
                }
            }

            result.append(KANA_MAP.getOrDefault(current, String.valueOf(current)));
            i++;
        }
        return result.toString();
    }
}
