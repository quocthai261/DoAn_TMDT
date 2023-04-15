package com.example.tryagain;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private final ArrayList<Comment> list;

    public CommentAdapter(ArrayList<Comment> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = list.get(position);
        holder.tv_Name.setText(comment.getName());
        holder.tv_Cmt.setText(comment.getComment());
        holder.tv_date.setText(comment.getDate());
        Glide.with(holder.imv_profile.getContext())
                .load(comment.getProfileImage())
                .override(56, 56)
                .into(holder.imv_profile);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_Name, tv_Cmt, tv_date;
        private final ImageView imv_profile;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.tv_Date);
            tv_Name = itemView.findViewById(R.id.tv_Name);
            tv_Cmt = itemView.findViewById(R.id.tv_Comment);
            imv_profile = itemView.findViewById(R.id.imv_Profile);
        }
    }
}
