package com.voiling.nlp;

import java.util.ArrayList;
import java.util.List;

public class SimpleKanaTokenizerManager implements TokenizerManager {
    @Override
    public List<TokenReading> tokenizeWithReading(String text) {
        String input = text == null ? "" : text.trim();
        List<TokenReading> result = new ArrayList<>();
        if (input.isEmpty()) {
            return result;
        }
        result.add(new TokenReading(input, input));
        return result;
    }
}
