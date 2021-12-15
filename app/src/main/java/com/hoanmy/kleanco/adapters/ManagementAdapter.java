package com.hoanmy.kleanco.adapters;

import android.app.Activity;
import android.os.CountDownTimer;
import android.os.Handler;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ManagementAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    static List<TaskProject> itemList;
    static List<TaskProject> itemListSearch;
    private static Activity mActivity;

    public ManagementAdapter(Activity activity, List<TaskProject> items) {
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
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


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void showViews(final RecyclerView.Adapter adapter, final List<TaskProject> taskProjects, final int position) {
            txtDes.setText(taskProjects.get(position).getName());
            txtTimeCounter.setText(taskProjects.get(position).getTime_period() + "p:00s");
            txtTime.setText(taskProjects.get(position).getTime_start() + " - " + taskProjects.get(position).getTime_end());
        }

        public void initCountDownTimer(int time) {
            new CountDownTimer(time, 1000) {

                public void onTick(long millisUntilFinished) {
                    txtTimeCounter.setText("time jobs: " +
                            String.format("%02d:%02d:%02d",
                                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 60,
                                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60));
                }

                public void onFinish() {
                    viewFeedback.setVisibility(View.VISIBLE);
                    initCountDownTimerFeedback();
                }
            }.start();
        }

        public void initCountDownTimerFeedback() {
            new CountDownTimer(120000, 1000) {

                public void onTick(long millisUntilFinished) {
                    txtTimeCounter.setText("time feedback: " +
                            String.format("%02d:%02d",
                                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60));
                }

                public void onFinish() {
                    txtTimeCounter.setText("done!");
                    viewFeedback.setVisibility(View.VISIBLE);
                }
            }.start();
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

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getName().contains(charSequence)) {
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
