package com.mcc.fshopper.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mcc.fshopper.R;
import com.mcc.fshopper.model.Comment;

import java.util.ArrayList;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> {

    private ArrayList<Comment> dataList;

    public CommentListAdapter(ArrayList<Comment> dataList) {
        this.dataList = dataList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvComments;

        public ViewHolder(final View itemView, int viewType) {
            super(itemView);

            tvComments = (TextView) itemView.findViewById(R.id.tvComments);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
            return new ViewHolder(view,viewType);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Comment comment = dataList.get(position);

        holder.tvComments.setText(comment.getMessage());

    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }

}
