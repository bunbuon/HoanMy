package com.hoanmy.kleanco.adapters;

import android.app.Activity;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.ctrlplusz.anytextview.AnyTextView;
import com.hoanmy.kleanco.R;
import com.hoanmy.kleanco.models.ManagementItem;
import com.hoanmy.kleanco.models.Notification;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Notification> itemList;
    private static Activity mActivity;

    public NotificationAdapter(Activity activity, List<Notification> items) {
        mActivity = activity;
        this.itemList = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_notification, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).showViews(this, itemList, position);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.logo)
        AppCompatImageView imgLogo;
        @BindView(R.id.txt_title_noti)
        AnyTextView txtTile;
        @BindView(R.id.txt_date_noti)
        AnyTextView txtTimeAgo;
        @BindView(R.id.txt_des_noti)
        AnyTextView txtDes;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void showViews(final RecyclerView.Adapter adapter, final List<Notification> notifications, final int position) {

        }


    }
}
