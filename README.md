# Voiling Android v1 (Offline)

This repository is now a Gradle multi-module project:
- `app`: Android application (Java + XML, MVVM, Room, Kuromoji)
- `core`: JVM demo and unit-testable pronunciation core

## v1 scope implemented
- Input Japanese lyrics in-app
- Offline tokenize + reading extraction with Kuromoji (IPADIC)
- Katakana to Hiragana conversion
- Rule-based phonetic conversion (`っ`, `ゃゅょ`, `ー`, `ん`)
- Show three lines: original + kana + phonetic
- Long-press token to edit phonetic override
- Save override into local Room database and re-apply on next conversion

## Module highlights
- `app/src/main/java/com/voiling/nlp/KuromojiTokenizerManager.java`
- `app/src/main/java/com/voiling/translit/CnPhoneticEngine.java`
- `app/src/main/java/com/voiling/data/local/AppDatabase.java`
- `app/src/main/java/com/voiling/service/PronunciationService.java`
- `app/src/main/java/com/voiling/ui/MainActivity.java`
- `app/src/main/java/com/voiling/ui/MainViewModel.java`
- `core/src/main/java/com/voiling/App.java`
- `core/src/test/java/com/voiling/PronunciationServiceTest.java`

## Notes
- Current output style is `拼音风格` phonetic line (for sing-along approximation).
- No backend is used.
- The old root-level Maven layout has been migrated into the `core` Gradle module.

## Gradle
- Use `.\gradlew.bat :core:test` to run core tests on Windows.
- Use `.\gradlew.bat :core:run --args="こんにちは"` to run the JVM demo.
- The Android `app` module requires a valid local Android SDK path in `local.properties` or `ANDROID_HOME`.
