package com.voiling.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.voiling.data.UserDictRepository;
import com.voiling.model.PronunciationResult;
import com.voiling.model.TokenPronunciation;
import com.voiling.service.PronunciationService;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainViewModel extends ViewModel {
    private final PronunciationService pronunciationService;
    private final UserDictRepository userDictRepository;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final MutableLiveData<PronunciationResult> resultLiveData = new MutableLiveData<>();
    private String latestInput = "";

    public MainViewModel(PronunciationService pronunciationService, UserDictRepository userDictRepository) {
        this.pronunciationService = pronunciationService;
        this.userDictRepository = userDictRepository;
        this.resultLiveData.setValue(new PronunciationResult("", "", "", "", new ArrayList<>()));
    }

    public LiveData<PronunciationResult> getResultLiveData() {
        return resultLiveData;
    }

    public void convert(String input) {
        latestInput = input == null ? "" : input;
        executorService.execute(() -> {
            PronunciationResult result = pronunciationService.convert(latestInput);
            resultLiveData.postValue(result);
        });
    }

    public void saveOverride(TokenPronunciation token, String newPhonetic) {
        if (token == null || newPhonetic == null || newPhonetic.isBlank()) {
            return;
        }
        executorService.execute(() -> {
            userDictRepository.saveOrUpdate(token.getSurface(), newPhonetic.trim());
            PronunciationResult refreshed = pronunciationService.convert(latestInput);
            resultLiveData.postValue(refreshed);
        });
    }

    @Override
    protected void onCleared() {
        executorService.shutdown();
    }
}
