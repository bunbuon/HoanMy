package com.hoanmy.kleanco.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ctrlplusz.anytextview.AnyTextView;
import com.hoanmy.kleanco.R;
import com.hoanmy.kleanco.models.ManagementItem;
import com.hoanmy.kleanco.models.TaskProject;
import com.hoanmy.kleanco.utils.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskDoneAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    static List<TaskProject> itemList;
    static List<TaskProject> itemListSearch;
    private static Activity mActivity;
    private static TaskProject taskProject;

    public TaskDoneAdapter(Activity activity, List<TaskProject> items) {
        mActivity = activity;
        this.itemList = items;
        this.itemListSearch = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_management, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).setIsRecyclable(false);
        ((ViewHolder) holder).showViews(this, itemListSearch, position);
    }

    @Override
    public int getItemCount() {
        return itemListSearch.size();
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

        @BindView(R.id.color_status)
        RelativeLayout viewStatusColor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @SuppressLint("ResourceAsColor")
        private void showViews(final RecyclerView.Adapter adapter, final List<TaskProject> taskProjects, final int position) {
            taskProject = taskProjects.get(position);
            if (taskProject.getStatus() == 3) {
                txtTimeCounter.setText("Quá thời gian");
                viewStatusColor.setBackgroundColor(Color.parseColor("#FD6602"));
            } else if (taskProject.getStatus() == 4) {

                txtTimeCounter.setText("Hoàn thành");
                viewStatusColor.setBackgroundColor(Color.parseColor("#999999"));
            } else if (taskProject.getStatus() == 5) {

                txtTimeCounter.setText("Chưa hoàn thành");
                viewStatusColor.setBackgroundColor(Color.parseColor("#FF0000"));
            }

            txtNameCustomer.setText(taskProject.getUser().getName());
            txtDes.setText(taskProject.getName());
            txtTime.setText(taskProject.getTime_start() + " - " + taskProject.getTime_end() + " / " + taskProject.getDate_str());

        }


    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    itemListSearch = itemList;
                } else {
                    List<TaskProject> filteredList = new ArrayList<>();
                    for (TaskProject row : itemList) {
                        String nameStaff = Utils.convertString(row.getUser().getName());
                        if (row.getUser().getName().toLowerCase().contains(charString.toLowerCase()) || row.getUser().getName().contains(charSequence) || nameStaff.toLowerCase().contains(charString.toLowerCase()) || nameStaff.contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    itemListSearch = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = itemListSearch;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                itemListSearch = (ArrayList<TaskProject>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
