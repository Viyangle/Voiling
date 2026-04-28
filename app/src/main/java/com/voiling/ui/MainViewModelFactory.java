package com.voiling.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.voiling.data.UserDictRepository;
import com.voiling.service.PronunciationService;

public class MainViewModelFactory implements ViewModelProvider.Factory {
    private final PronunciationService pronunciationService;
    private final UserDictRepository userDictRepository;

    public MainViewModelFactory(PronunciationService pronunciationService, UserDictRepository userDictRepository) {
        this.pronunciationService = pronunciationService;
        this.userDictRepository = userDictRepository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(pronunciationService, userDictRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
