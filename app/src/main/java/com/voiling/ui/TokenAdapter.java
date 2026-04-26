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

public class TokenAdapter extends RecyclerView.Adapter<TokenAdapter.TokenViewHolder> {
    public interface OnTokenLongClickListener {
        void onLongClick(TokenPronunciation token);
    }

    private final List<TokenPronunciation> items = new ArrayList<>();
    private final OnTokenLongClickListener listener;

    public TokenAdapter(OnTokenLongClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<TokenPronunciation> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TokenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_token, parent, false);
        return new TokenViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TokenViewHolder holder, int position) {
        TokenPronunciation item = items.get(position);
        holder.surfaceText.setText(item.getSurface());
        holder.readingText.setText(item.getReading());
        holder.phoneticText.setText(item.getPhonetic());
        holder.itemView.setOnLongClickListener(v -> {
            listener.onLongClick(item);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
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
}
