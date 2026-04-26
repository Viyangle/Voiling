package com.voiling.nlp;

import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;

import java.util.ArrayList;
import java.util.List;

public class KuromojiTokenizerManager implements TokenizerManager {
    private volatile Tokenizer tokenizer;

    @Override
    public List<TokenReading> tokenizeWithReading(String text) {
        String input = text == null ? "" : text.trim();
        if (input.isEmpty()) {
            return new ArrayList<>();
        }

        List<TokenReading> result = new ArrayList<>();
        for (Token token : getTokenizer().tokenize(input)) {
            String surface = token.getSurface();
            if (surface == null || surface.isBlank()) {
                continue;
            }
            String reading = token.getReading();
            if (reading == null || "*".equals(reading)) {
                reading = surface;
            }
            result.add(new TokenReading(surface, reading));
        }
        return result;
    }

    private Tokenizer getTokenizer() {
        if (tokenizer == null) {
            synchronized (this) {
                if (tokenizer == null) {
                    tokenizer = new Tokenizer();
                }
            }
        }
        return tokenizer;
    }
}
