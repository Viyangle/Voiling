package com.voiling.nlp;

import java.util.List;

public interface TokenizerManager {
    List<TokenReading> tokenizeWithReading(String text);
}
