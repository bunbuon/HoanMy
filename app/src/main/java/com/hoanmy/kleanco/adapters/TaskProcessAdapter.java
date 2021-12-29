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
import com.hoanmy.kleanco.commons.RemoveItemViewListener;
import com.hoanmy.kleanco.models.Login;
import com.hoanmy.kleanco.models.TaskProject;
import com.hoanmy.kleanco.services.NotificationForegroundService;
import com.hoanmy.kleanco.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
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
    private static int positionItem;
    private boolean isClick = false;


    private RemoveItemViewListener removeItemViewListener;

    public TaskProcessAdapter(Activity activity, List<TaskProject> items, RemoveItemViewListener _removeItemViewListener) {
        mActivity = activity;
        this.itemList = items;
        this.itemListSearch = items;
        this.removeItemViewListener = _removeItemViewListener;
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
            viewFeedback.setVisibility(View.GONE);
            positionItem = position;
            timeCurrent = System.currentTimeMillis() / 1000;
            isClick = false;
            if (itemListSearch.get(position).getStatus() == 1) {
                viewStatusColor.setBackgroundColor(Color.parseColor("#FFFFFF"));
            } else if (itemListSearch.get(position).getStatus() == 2) {
                viewStatusColor.setBackgroundColor(Color.parseColor("#FF03DAC5"));
            }
            long timeEndTimestamp = itemListSearch.get(position).getTime_end_timestamp();

            txtNameCustomer.setText(itemListSearch.get(position).getUser().getName());
            txtDes.setText(itemListSearch.get(position).getName());
            txtTimeCounter.setText(itemListSearch.get(position).getTime_period() + "p:00s");
            txtTime.setText(itemListSearch.get(position).getTime_start() + " - " + itemListSearch.get(position).getTime_end() + " / " + itemListSearch.get(position).getDate_str());

            if (itemListSearch.get(position).getTime_end_real_ts() > 0) {
                itemListSearch.get(position).setTimeStartFeedback(itemListSearch.get(position).getTime_end_real_ts() + 120);
            } else {
                itemListSearch.get(position).setTimeStartFeedback(timeEndTimestamp + 120);
            }
            if (itemListSearch.get(position).getTimeStartFeedback() > timeCurrent && timeCurrent > timeEndTimestamp) {
                itemListSearch.get(position).setDone(true);
            }
            if (timeCurrent >= itemListSearch.get(position).getTime_start_timestamp() && timeEndTimestamp > timeCurrent
                    && itemListSearch.get(position).getStatus() != 4 && itemListSearch.get(position).getStatus() != 3) {
                mTimer = new Timer();
                mTimer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        mHandler.post(new Runnable() {

                            @Override
                            public void run() {

                                long countTime = 0;
                                long timeCurrent = System.currentTimeMillis() / 1000;
                                if (itemListSearch.size() > 0) {
                                    if (timeCurrent <= timeEndTimestamp) {
                                        countTime = (timeEndTimestamp - timeCurrent);
                                    } else if (itemListSearch.get(position).getTimeStartFeedback() > timeCurrent) {
                                        countTime = itemListSearch.get(position).getTimeStartFeedback() - timeCurrent;
                                        itemListSearch.get(position).setDone(true);
                                        viewFeedback.setVisibility(View.VISIBLE);
                                    } else if (itemListSearch.get(position).getTimeStartFeedback() < timeCurrent) {
                                        viewFeedback.setVisibility(View.GONE);
                                        postDataFeedback(itemListSearch.get(positionItem), 1, edtFeedback.getText().toString(), positionItem, itemView);
                                        mTimer.cancel();
                                    }

                                    txtTimeCounter.setText(String.format("%02d:%02d:%02d",
                                            TimeUnit.SECONDS.toHours(countTime) % 60,
                                            TimeUnit.SECONDS.toMinutes(countTime) % 60,
                                            TimeUnit.SECONDS.toSeconds(countTime) % 60));
                                }
                            }

                        });
                    }
                }, 1000, NOTIFY_INTERVAL);
            } else if (itemListSearch.get(position).isDone() || itemListSearch.get(position).getStatus() == 4 || itemListSearch.get(position).getStatus() == 3) {
                viewFeedback.setVisibility(View.VISIBLE);
                mTimer2 = new Timer();
                mTimer2.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        mHandler2.post(new Runnable() {

                            @Override
                            public void run() {
                                long countTime = 0;
                                long timeCurrent = System.currentTimeMillis() / 1000;
                                if (itemListSearch.size() > 0) {
                                    Log.d(Utils.TAG, "runmHandlerPos:----------- " + position);
                                    if (position >= itemListSearch.size())
                                        return;
                                    if (itemListSearch.get(position).getTimeStartFeedback() > timeCurrent) {
                                        countTime = itemListSearch.get(position).getTimeStartFeedback() - timeCurrent;
                                        viewFeedback.setVisibility(View.VISIBLE);
                                    } else if (itemListSearch.get(position).getTimeStartFeedback() < timeCurrent) {
                                        viewFeedback.setVisibility(View.GONE);
                                        postDataFeedback(itemListSearch.get(positionItem), 1, edtFeedback.getText().toString(), positionItem, itemView);
                                        mTimer2.cancel();
                                    }

                                    txtTimeCounter.setText(String.format("%02d:%02d:%02d",
                                            TimeUnit.SECONDS.toHours(countTime) % 60,
                                            TimeUnit.SECONDS.toMinutes(countTime) % 60,
                                            TimeUnit.SECONDS.toSeconds(countTime) % 60));
                                }
                            }

                        });
                    }
                }, 1000, NOTIFY_INTERVAL);
            }
            btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isClick = true;
                    postDataFeedback(itemListSearch.get(position), 1, edtFeedback.getText().toString(), position, itemView);
                }
            });
            btnNotCompleted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isClick = true;
                    postDataFeedback(itemListSearch.get(position), 0, edtFeedback.getText().toString(), position, itemView);
                }
            });
        }

        private Handler mHandler = new Handler();
        private Handler mHandler2 = new Handler();
        public Timer mTimer = null;
        public Timer mTimer2 = null;
        public final long NOTIFY_INTERVAL = 1000;

        private void cancelTimer(Timer mTimer) {
            if (mTimer != null) {
                mTimer.cancel();
                mTimer.purge();
            }
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
                            isClick = false;
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

            itemView.setVisibility(View.GONE);
            removeItemViewListener.onChangeData(taskProject);

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
