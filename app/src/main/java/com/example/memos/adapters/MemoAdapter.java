package com.example.memos.adapters;

import android.app.Application;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memos.Dao.MemoInterface;
import com.example.memos.MainActivity;
import com.example.memos.Models.Note;
import com.example.memos.R;
import com.example.memos.memo_content;

import java.util.ArrayList;
import java.util.List;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.MemoViewHolder> {

    private ArrayList<Note> mNotes;
    private OnNoteListener mListener;

    public class MemoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title, timestamp;

        private MemoViewHolder(@NonNull final View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.timestamp = (TextView) itemView.findViewById(R.id.timestamp);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onMemoClick(getAdapterPosition());
        }
    }

    public MemoAdapter(ArrayList<Note> notes, OnNoteListener listener) {
        this.mNotes = notes;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public MemoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_memo, parent, false);
        return new MemoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemoViewHolder holder, int position) {
        holder.title.setText(mNotes.get(position).getTitle());
        holder.timestamp.setText(mNotes.get(position).getTimestamp());
    }

    public void watchMemoChanges(ArrayList<Note> notes){
        this.mNotes = notes;
        notifyDataSetChanged();
    }



    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public interface OnNoteListener{
       void onMemoClick(int position);
    }
}