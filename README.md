# Voiling (Maven Skeleton)

This is a local-only Maven starter project for an offline Japanese pronunciation helper.

## Current scope
- Local text conversion pipeline (no backend)
- Kana normalization (`Katakana -> Hiragana`)
- Hiragana to Chinese-style phonetic approximation (rule-based MVP)
- User dictionary override interface

## Run
```bash
mvn test
mvn -q exec:java -Dexec.mainClass=com.voiling.App -Dexec.args="きみのなまえは"
```

## Suggested next step for Android
- Keep this module as pronunciation core
- Create Android app module and call `PronunciationService`
- Replace `SimpleKanaTokenizerManager` with Kuromoji-based tokenizer in-app
