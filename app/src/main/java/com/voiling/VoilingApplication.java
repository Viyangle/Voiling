package com.voiling;

import android.app.Application;

import com.voiling.data.UserDictRepository;
import com.voiling.data.UserDictRepositoryImpl;
import com.voiling.data.local.AppDatabase;
import com.voiling.nlp.KuromojiTokenizerManager;
import com.voiling.nlp.TokenizerManager;
import com.voiling.service.PronunciationService;
import com.voiling.translit.CnPhoneticEngine;
import com.voiling.util.TextNormalizer;

public class VoilingApplication extends Application {
    private PronunciationService pronunciationService;
    private UserDictRepository userDictRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        AppDatabase db = AppDatabase.build(this);
        userDictRepository = new UserDictRepositoryImpl(db.userDictDao());
        TokenizerManager tokenizerManager = new KuromojiTokenizerManager();
        pronunciationService = new PronunciationService(
                tokenizerManager,
                new CnPhoneticEngine(),
                userDictRepository,
                new TextNormalizer()
        );
    }

    public PronunciationService getPronunciationService() {
        return pronunciationService;
    }

    public UserDictRepository getUserDictRepository() {
        return userDictRepository;
    }
}
