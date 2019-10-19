package com.mcc.fshopper.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mcc.fshopper.R;
import com.mcc.fshopper.data.constant.AppConstants;
import com.mcc.fshopper.listener.OnItemClickListener;
import com.mcc.fshopper.model.ProductModel;
import com.mcc.fshopper.utils.AppUtility;
import com.mcc.fshopper.utils.ListTypeShow;

import java.util.ArrayList;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<ProductModel> dataList;
    private int displaySize = 0;
    private ListTypeShow listTypeShow;

    // Listener
    private OnItemClickListener mListener;

    public SearchListAdapter(Context context, ArrayList<ProductModel> dataList, ListTypeShow listTypeShow) {
        this.mContext = context;
        this.dataList = dataList;
        this.listTypeShow = listTypeShow;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivCategory;
        private TextView tvPrice, tvProductName, tvLikeCounter;

        public ViewHolder(final View itemView, int viewType) {
            super(itemView);

            ivCategory = (ImageView) itemView.findViewById(R.id.ivProductImage);
            tvProductName = (TextView) itemView.findViewById(R.id.tvProductName);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            tvLikeCounter = (TextView) itemView.findViewById(R.id.tvLikeCounter);

            // listener
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

        if (listTypeShow == ListTypeShow.GRID) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_grid, parent, false);
            return new ViewHolder(view, viewType);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_list, parent, false);
            return new ViewHolder(view, viewType);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final ProductModel product = dataList.get(position);

        if (product.getTitle() != null) {
            holder.tvProductName.setText(AppUtility.showHtml(product.getTitle()));
        }
        if (product.getPrice() != null) {
            holder.tvPrice.setText(String.valueOf(product.getPrice()));
        }
        else {
            holder.tvPrice.setVisibility(View.INVISIBLE);
        }
        if (product.getLikeCount() != 0) {
            holder.tvLikeCounter.setText(String.valueOf(product.getLikeCount()));
        }

        if (product.getImageList().size() > 0) {
            Glide.with(mContext)
                    .load(product.getImageList().get(0))
                    .placeholder(R.color.imgPlaceholder)
                    .centerCrop()
                    .into(holder.ivCategory);
        }

    }

    @Override
    public int getItemCount() {
        if(displaySize == AppConstants.VALUE_ZERO || displaySize > dataList.size()) {
            return dataList.size();
        }
        else {
            return displaySize;
        }
    }

    @Override
    public int getItemViewType(int position) {
            return 0;
    }

    public void setItemClickListener(OnItemClickListener mListener) {
        if (mListener != null) {
            this.mListener = mListener;
        }
    }

    public void setDisplayCount(int numberOfEntries) {
        displaySize = numberOfEntries;
        notifyDataSetChanged();

    }
}
