package com.voiling.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.voiling.R;
import com.voiling.model.TokenPronunciation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class TokenAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_GROUP = 1;
    private static final int VIEW_TYPE_TOKEN = 2;
    private static final int BREATH_MORA_LIMIT = 10;
    private static final Pattern SPLIT_PUNCT = Pattern.compile("^[\\u3001\\u3002\\uFF01\\uFF1F,.!?…]$");

    public interface OnTokenLongClickListener {
        void onLongClick(TokenPronunciation token);
    }

    private final List<RowItem> items = new ArrayList<>();
    private final OnTokenLongClickListener listener;

    public TokenAdapter(OnTokenLongClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<TokenPronunciation> newItems) {
        items.clear();
        List<List<TokenPronunciation>> groups = toBreathGroups(newItems);
        for (int i = 0; i < groups.size(); i++) {
            items.add(RowItem.group("Breath " + (i + 1)));
            for (TokenPronunciation token : groups.get(i)) {
                items.add(RowItem.token(token));
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_GROUP) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_token_group, parent, false);
            return new GroupViewHolder(view);
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_token, parent, false);
        return new TokenViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RowItem row = items.get(position);
        if (row.isGroup()) {
            ((GroupViewHolder) holder).groupTitleText.setText(row.groupTitle);
            return;
        }
        TokenPronunciation item = row.token;
        TokenViewHolder tokenHolder = (TokenViewHolder) holder;
        if (item == null) {
            return;
        }
        tokenHolder.surfaceText.setText(item.getSurface());
        tokenHolder.readingText.setText(item.getReading());
        tokenHolder.phoneticText.setText(item.getPhonetic());
        tokenHolder.itemView.setOnLongClickListener(v -> {
            listener.onLongClick(item);
            return true;
        });
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).isGroup() ? VIEW_TYPE_GROUP : VIEW_TYPE_TOKEN;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private List<List<TokenPronunciation>> toBreathGroups(List<TokenPronunciation> tokens) {
        List<List<TokenPronunciation>> groups = new ArrayList<>();
        if (tokens == null || tokens.isEmpty()) {
            return groups;
        }
        List<TokenPronunciation> current = new ArrayList<>();
        int moraCount = 0;
        for (TokenPronunciation token : tokens) {
            if (token == null) {
                continue;
            }
            current.add(token);
            String surface = token.getSurface();
            moraCount += estimateMora(token.getReading());

            boolean forceSplit = SPLIT_PUNCT.matcher(surface == null ? "" : surface).matches();
            boolean lengthSplit = moraCount >= BREATH_MORA_LIMIT;
            if (forceSplit || lengthSplit) {
                groups.add(current);
                current = new ArrayList<>();
                moraCount = 0;
            }
        }
        if (!current.isEmpty()) {
            groups.add(current);
        }
        return groups;
    }

    private int estimateMora(String reading) {
        if (reading == null || reading.isBlank()) {
            return 0;
        }
        int count = 0;
        for (int i = 0; i < reading.length(); i++) {
            char c = reading.charAt(i);
            if (!Character.isWhitespace(c)) {
                count++;
            }
        }
        return count;
    }

    static class GroupViewHolder extends RecyclerView.ViewHolder {
        TextView groupTitleText;

        GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            groupTitleText = itemView.findViewById(R.id.groupTitleText);
        }
    }

    static class TokenViewHolder extends RecyclerView.ViewHolder {
        TextView surfaceText;
        TextView readingText;
        TextView phoneticText;

        TokenViewHolder(@NonNull View itemView) {
            super(itemView);
            surfaceText = itemView.findViewById(R.id.surfaceText);
            readingText = itemView.findViewById(R.id.readingText);
            phoneticText = itemView.findViewById(R.id.phoneticText);
        }
    }

    static class RowItem {
        private final String groupTitle;
        private final TokenPronunciation token;

        private RowItem(String groupTitle, TokenPronunciation token) {
            this.groupTitle = groupTitle;
            this.token = token;
        }

        static RowItem group(String title) {
            return new RowItem(title, null);
        }

        static RowItem token(TokenPronunciation token) {
            return new RowItem(null, token);
        }

        boolean isGroup() {
            return groupTitle != null;
        }
    }
}
