package com.hoanmy.kleanco.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.RecyclerView;

import com.ctrlplusz.anytextview.AnyTextView;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hoanmy.kleanco.R;
import com.hoanmy.kleanco.api.RequestApi;
import com.hoanmy.kleanco.commons.Action;
import com.hoanmy.kleanco.commons.Constants;
import com.hoanmy.kleanco.models.Login;
import com.hoanmy.kleanco.models.TaskProject;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.paperdb.Paper;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class TaskProcessAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    static List<TaskProject> itemList;
    static List<TaskProject> itemListSearch;
    private static Activity mActivity;
    static long timeCurrent;
    private static TaskProject taskProject;

    public TaskProcessAdapter(Activity activity, List<TaskProject> items) {
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

    class ViewHolder extends RecyclerView.ViewHolder {
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
        @BindView(R.id.btn_done)
        AppCompatButton btnDone;
        @BindView(R.id.btn_not_completed)
        AppCompatButton btnNotCompleted;
        @BindView(R.id.edt_feedback)
        AppCompatEditText edtFeedback;
        @BindView(R.id.color_status)
        RelativeLayout viewStatusColor;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @SuppressLint("ResourceAsColor")
        private void showViews(final RecyclerView.Adapter adapter, final List<TaskProject> taskProjects, final int position) {
            taskProject = taskProjects.get(position);
            if (taskProject.getStatus() == 1){
                viewStatusColor.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }else  if (taskProject.getStatus() == 2){
                viewStatusColor.setBackgroundColor(Color.parseColor("#FF03DAC5"));
            }
            long timeTest = 1640023770;
            txtNameCustomer.setText(taskProject.getUser().getName());
            txtDes.setText(taskProject.getName());
            txtTimeCounter.setText(taskProject.getTime_period() + "p:00s");
            txtTime.setText(taskProject.getTime_start() + " - " + taskProject.getTime_end() + " / " + taskProject.getDate_str());
            if (taskProject.getTimeStartFeedback() <= 0) {
                taskProject.setTimeStartFeedback(timeTest + 120);
            }
            timeCurrent = System.currentTimeMillis() / 1000;
            if (timeCurrent >= taskProject.getTime_start_timestamp() && !taskProject.isDone()) {
                initCountDownTimer((timeTest - timeCurrent) * 1000);
            } else if (taskProject.isDone() && taskProject.getTimeStartFeedback() > timeCurrent) {
                viewFeedback.setVisibility(View.VISIBLE);
                initCountDownTimerFeedback((taskProject.getTimeStartFeedback() - timeCurrent) * 1000);
            }
            btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postDataFeedback(taskProject, 1, edtFeedback.getText().toString(), position, itemView);
                }
            });
            btnNotCompleted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postDataFeedback(taskProject, 0, edtFeedback.getText().toString(), position, itemView);
                }
            });
        }

        public void initCountDownTimer(long time) {
            new CountDownTimer(time, 1000) {

                public void onTick(long millisUntilFinished) {
                    txtTimeCounter.setText("time jobs: " +
                            String.format("%02d:%02d:%02d",
                                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 60,
                                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60));
                }

                public void onFinish() {
                    Log.d("TAG", "showViews:onFinish ");
                    viewFeedback.setVisibility(View.VISIBLE);
                    taskProject.setDone(true);
                    timeCurrent = System.currentTimeMillis() / 1000;
                    initCountDownTimerFeedback((taskProject.getTimeStartFeedback() - timeCurrent) * 1000);
                }
            }.start();
        }

        public void initCountDownTimerFeedback(long timeFeeback) {
            new CountDownTimer(timeFeeback, 1000) {

                public void onTick(long millisUntilFinished) {
                    txtTimeCounter.setText("time feedback: " +
                            String.format("%02d:%02d:%02d",
                                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 60,
                                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60));
                }

                public void onFinish() {
                    txtTimeCounter.setText("done!");
                }
            }.start();
        }

        private void postDataFeedback(TaskProject project, int status, String feedback, int pos, View itemView) {
            Login loginData = Paper.book().read("login");
            JSONObject paramObject = new JSONObject();
            JsonObject gsonObject = new JsonObject();
            try {
                paramObject.put("task_id", project.get_id());
                paramObject.put("feedback_text", feedback);
                paramObject.put("status", status);
                JsonParser jsonParser = new JsonParser();
                gsonObject = (JsonObject) jsonParser.parse(paramObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestApi.getInstance().postFeedback(loginData.getToken(), gsonObject).retry(Constants.NUMBER_RETRY_IF_CALL_API_FAIL)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<JsonElement>() {
                        @Override
                        public void call(JsonElement jsonElement) {
                            removeItem(pos, project, itemView);

                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    });
        }
        private void removeItem(int position, TaskProject taskProject, View itemView) {
            EventBus.getDefault().post(Action.REQUEST_API_TASK_DONE);
            itemList.remove(taskProject);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, itemList.size());
            itemView.setVisibility(View.GONE);
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
                        if (row.getUser().getName().toLowerCase().contains(charString.toLowerCase()) || row.getUser().getName().contains(charSequence)) {
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
