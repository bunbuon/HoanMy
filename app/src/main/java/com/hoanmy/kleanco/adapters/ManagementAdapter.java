package com.hoanmy.kleanco.adapters;

import android.app.Activity;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ctrlplusz.anytextview.AnyTextView;
import com.hoanmy.kleanco.R;
import com.hoanmy.kleanco.models.ManagementItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ManagementAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<ManagementItem> itemList;
    private static Activity mActivity;

    public ManagementAdapter(Activity activity, List<ManagementItem> items) {
        mActivity = activity;
        this.itemList = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_management, parent, false);
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
        @BindView(R.id.txt_name)
        AnyTextView txtNameCustomer;
        @BindView(R.id.txt_duration)
        AnyTextView txtTimeCounter;
        @BindView(R.id.txt_time)
        AnyTextView txtTime;
        @BindView(R.id.txt_description)
        AnyTextView txtDes;
        @BindView(R.id.view_feedback)
        RelativeLayout viewFeedback;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void showViews(final RecyclerView.Adapter adapter, final List<ManagementItem> rootMatchInfos, final int position) {
            if (position == 0) {
                txtTimeCounter.setText("00:00");
                viewFeedback.setVisibility(View.VISIBLE);
            }
        }

        private void counterTime(long starttime) {
            Handler h2 = new Handler();
            Runnable run = new Runnable() {

                @Override
                public void run() {
                    long millis = System.currentTimeMillis() - starttime;
                    int seconds = (int) (millis / 1000);
                    int minutes = seconds / 60;
                    seconds = seconds % 60;

                    txtTimeCounter.setText(String.format("%d:%02d", minutes, seconds));

                    h2.postDelayed(this, 500);
                }
            };
        }
    }
}
