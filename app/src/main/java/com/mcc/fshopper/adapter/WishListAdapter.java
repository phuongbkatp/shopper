package com.mcc.fshopper.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mcc.fshopper.data.constant.AppConstants;
import com.mcc.fshopper.R;
import com.mcc.fshopper.listener.OnItemClickListener;
import com.mcc.fshopper.model.WishItem;

import java.util.ArrayList;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<WishItem> dataList;

    // Listener
    public OnItemClickListener mListener;

    public WishListAdapter(Context context, ArrayList<WishItem> dataList) {
        this.mContext = context;
        this.dataList = dataList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivProductImage, ivRemoveWish;
        private TextView tvPrice, tvProductName, tvLikeCounter;

        public ViewHolder(final View itemView, int viewType) {
            super(itemView);

            ivProductImage = (ImageView) itemView.findViewById(R.id.ivProductImage);
            ivRemoveWish = (ImageView) itemView.findViewById(R.id.ivRemoveWish);
            tvProductName = (TextView) itemView.findViewById(R.id.tvProductName);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            tvLikeCounter = (TextView) itemView.findViewById(R.id.tvLikeCounter);

            ivRemoveWish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemListener(view, getLayoutPosition());
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemListener(view, getLayoutPosition());
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wish_list, parent, false);

        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final WishItem wishItem = dataList.get(position);

        holder.tvProductName.setText(wishItem.name);
        holder.tvLikeCounter.setText(String.valueOf(wishItem.likeCount));

        if (wishItem.price != null) {
            holder.tvPrice.setText(String.valueOf(wishItem.price));
        }
        else {
            holder.tvPrice.setVisibility(View.INVISIBLE);
        }


        if (!wishItem.images.isEmpty()) {
            Glide.with(mContext)
                    .load(wishItem.images)
                    .placeholder(R.color.imgPlaceholder)
                    .centerCrop()
                    .into(holder.ivProductImage);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return AppConstants.VALUE_ZERO;
    }

    public void setItemClickListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }
}
