package com.voiling.ui;

import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.voiling.R;
import com.voiling.VoilingApplication;
import com.voiling.databinding.ActivityMainBinding;
import com.voiling.model.TokenPronunciation;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    private TokenAdapter tokenAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        VoilingApplication app = (VoilingApplication) getApplication();
        MainViewModelFactory factory = new MainViewModelFactory(
                app.getPronunciationService(),
                app.getUserDictRepository()
        );
        viewModel = new ViewModelProvider(this, factory).get(MainViewModel.class);

        tokenAdapter = new TokenAdapter(this::showEditDialog);
        binding.tokenRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.tokenRecyclerView.setAdapter(tokenAdapter);
        binding.tokenRecyclerView.setNestedScrollingEnabled(false);

        binding.convertButton.setOnClickListener(v -> {
            binding.inputEditText.clearFocus();
            hideKeyboard();
            viewModel.convert(String.valueOf(binding.inputEditText.getText()));
            binding.getRoot().post(() -> binding.getRoot().smoothScrollTo(0, binding.originalLabel.getTop()));
        });

        viewModel.getResultLiveData().observe(this, result -> {
            binding.originalText.setText(result.getOriginalText());
            binding.kanaText.setText(result.getKanaLine());
            binding.cnText.setText(result.getCnPhoneticLine());
            tokenAdapter.submitList(result.getTokens());
        });
    }

    private void showEditDialog(TokenPronunciation token) {
        EditText input = new EditText(this);
        input.setText(token.getPhonetic());
        int padding = (int) (16 * getResources().getDisplayMetrics().density);
        input.setPadding(padding, padding, padding, padding);

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialog_title) + "：" + token.getSurface())
                .setView(input)
                .setPositiveButton(R.string.dialog_positive, (dialog, which) ->
                        viewModel.saveOverride(token, String.valueOf(input.getText())))
                .setNegativeButton(R.string.dialog_negative, null)
                .show();
    }

    private void hideKeyboard() {
        InputMethodManager imm = getSystemService(InputMethodManager.class);
        if (imm != null) {
            imm.hideSoftInputFromWindow(binding.inputEditText.getWindowToken(), 0);
        }
    }

}
