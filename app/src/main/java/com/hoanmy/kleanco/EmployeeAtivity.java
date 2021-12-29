package com.hoanmy.kleanco;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ctrlplusz.anytextview.AnyTextView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.hoanmy.kleanco.api.RequestApi;
import com.hoanmy.kleanco.commons.Action;
import com.hoanmy.kleanco.commons.Constants;
import com.hoanmy.kleanco.fragments.LoginFragment;
import com.hoanmy.kleanco.fragments.LossPassFragment;
import com.hoanmy.kleanco.fragments.RegisterFragment;
import com.hoanmy.kleanco.models.Login;
import com.hoanmy.kleanco.models.ProjectsForID;
import com.hoanmy.kleanco.models.TaskProject;
import com.hoanmy.kleanco.services.NotificationForegroundService;
import com.hoanmy.kleanco.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class EmployeeAtivity extends BaseActivity {
    private Login loginData;
    private long timeEndJob;
    @BindView(R.id.txt_name_staff)
    AnyTextView txtNameStaff;
    @BindView(R.id.txt_num_staff)
    AnyTextView txtNumStaff;
    @BindView(R.id.txt_name_job)
    AnyTextView txtNameJob;
    @BindView(R.id.txt_location)
    AnyTextView txtLocation;
    @BindView(R.id.txt_floor)
    AnyTextView txtFloor;
    @BindView(R.id.txt_time_duration)
    AnyTextView txtTimeJob;
    @BindView(R.id.btn_extra_job)
    AppCompatButton btnExtra;
    @BindView(R.id.viewLoading)
    RelativeLayout viewLoading;
    private ProjectsForID projectsForID;
    private Timer T;
    private int statusButton;
    private boolean isExtra = false;
    private int statusActive;

    @OnClick(R.id.btn_pause)
    void onClickPause() {
        if (statusActive == 3) {
            Toast.makeText(getApplicationContext(), "Bạn phải kết thúc công việc phát sinh trước.", Toast.LENGTH_SHORT).show();
        } else {
            statusButton = 1;
            showDialogConfirm(getString(R.string.text_pause), getString(R.string.content_pause));
        }
    }

    @OnClick(R.id.btn_continue)
    void onClickResume() {
        if (statusActive == 3) {
            Toast.makeText(getApplicationContext(), "Bạn phải kết thúc công việc phát sinh trước.", Toast.LENGTH_SHORT).show();
        } else {
            statusButton = 2;
            showDialogConfirm(getString(R.string.text_resume), getString(R.string.content_resume));
        }
    }

    @OnClick(R.id.btn_done)
    void onClickDone() {
        if (statusActive == 3) {
            Toast.makeText(getApplicationContext(), "Bạn phải kết thúc công việc phát sinh trước.", Toast.LENGTH_SHORT).show();
        } else {
            statusButton = 5;
            showDialogConfirm(getString(R.string.text_done), getString(R.string.content_done));
        }
    }


    @OnClick(R.id.btn_extra_job)
    void onClickExtra() {
        if (!isExtra) {
            statusButton = 3;
            isExtra = true;
            showDialogConfirm(getString(R.string.text_extra), getString(R.string.content_extra));
        } else {
            statusButton = 4;
            isExtra = false;
            showDialogConfirm(getString(R.string.text_extra), getString(R.string.text_end_extra));
        }

    }

    @OnClick(R.id.img_notification)
    void onClickNotification() {
        Intent intent = new Intent(this, NotificationManageActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.img_logout)
    void onClickLogout() {
        Utils.loginActivity(this);
    }

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);
        ButterKnife.bind(this);
        statusActive = 0;

        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getApplicationContext(), getString(R.string.refresh_list_staff), Toast.LENGTH_SHORT).show();
                getProjects(loginData.getToken());
            }
        });
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        viewLoading.setVisibility(View.VISIBLE);
        Paper.book().delete("PAUSE");
        Paper.book().delete("DONE");
        loginData = Paper.book().read("login");
        txtNameStaff.setText(loginData.getName());
        txtNumStaff.setText(loginData.getUsername());
        getProjects(loginData.getToken());
    }

    public void startService(String jobName, long timeEnd) {
        Intent serviceIntent = new Intent(this, NotificationForegroundService.class);
        serviceIntent.setAction(Action.STARTFOREGROUND_ACTION + "");
        ContextCompat.startForegroundService(this, serviceIntent);

    }

    public void stopService() {
        Intent serviceIntent = new Intent(this, NotificationForegroundService.class);
        serviceIntent.setAction(Action.STOPFOREGROUND_ACTION + "");
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void getProjects(String token) {
        String fDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        RequestApi.getInstance().getProjectForId(token, "1,2", fDate, 500).retry(Constants.NUMBER_RETRY_IF_CALL_API_FAIL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonElement>() {
                    @Override
                    public void call(JsonElement jsonElement) {
                        projectsForID = new Gson().fromJson(jsonElement.getAsJsonObject().get("data"), new TypeToken<ProjectsForID>() {
                        }.getType());
                        checkJobNext();

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    private void setDataJob(ProjectsForID dataJob) {
        TaskProject dataJobNow = dataJob.getTaskProjects().get(0);
        timeEndJob = dataJobNow.getTime_end_timestamp();
        txtNameJob.setText(dataJobNow.getName());
        txtFloor.setText(dataJobNow.getFloor());
        txtLocation.setText(loginData.getProjectDetail().getAddress());
        countOutTime();
        Paper.book().write("DataService", dataJobNow);
        startService(dataJobNow.getName(), timeEndJob);

    }


    public void countOutTime() {
        viewLoading.setVisibility(View.GONE);
        if (T != null) {
            T.cancel();
            T.purge();
            T = null;
        }
        Log.d(Utils.TAG, "countOutTime--------------------: ");

        T = new Timer();
        T.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Log.d(Utils.TAG, "countOutTime--------------------: 1");
                        if (statusActive == 1 || statusActive == 3 || statusActive == 5 || Paper.book().read("PAUSE") != null)
                            return;
                        long timeCurrent = System.currentTimeMillis() / 1000;
                        Log.d(Utils.TAG, "run--------------------: " + timeCurrent);
                        long countTime = 0;
                        if (timeCurrent <= timeEndJob) {
                            countTime = (timeEndJob - timeCurrent);
                            txtTimeJob.setTextColor(getResources().getColor(R.color.white));
                        } else {
                            txtTimeJob.setTextColor(getResources().getColor(R.color.red));
                            countTime = (timeCurrent - timeEndJob);
                        }
                        txtTimeJob.setText(
                                String.format("%02d:%02d:%02d",
                                        TimeUnit.SECONDS.toHours(countTime) % 60,
                                        TimeUnit.SECONDS.toMinutes(countTime) % 60,
                                        TimeUnit.SECONDS.toSeconds(countTime) % 60));

                    }
                });
            }
        }, 1000, 1000);
    }

    private void showDialogConfirm(String title, String content) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(title)
                .setMessage(content)
                .setPositiveButton(R.string.text_dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (statusButton == 1) {
                            Paper.book().write("PAUSE", "PAUSE");
                            txtTimeJob.setText(getString(R.string.text_pause));
                            postDataStatus(1);
                            statusActive = 1;
                            if (T != null) {
                                T.cancel();
                                T.purge();
                                T = null;
                            }

                        } else if (statusButton == 2) {
                            statusActive = 2;
                            countOutTime();
                            Paper.book().delete("PAUSE");
                            postDataStatus(2);
                        } else if (statusButton == 3) {
                            btnExtra.setText(getString(R.string.text_end_extra));
                            btnExtra.setBackground(getResources().getDrawable(R.drawable.border_radius_button_extra));
                            postDataStatus(3);
                            Paper.book().write("PAUSE", "PAUSE");
                            txtTimeJob.setText(getString(R.string.text_pause));

                            statusActive = 3;
                            if (T != null) {
                                T.cancel();
                                T.purge();
                                T = null;
                            }


                        } else if (statusButton == 4) {
                            statusActive = 4;
                            btnExtra.setText(getString(R.string.input_job_extra));
                            btnExtra.setBackground(getResources().getDrawable(R.drawable.border_radius_button));
                            postDataStatus(4);
                        } else if (statusButton == 5) {
                            txtTimeJob.setText(getString(R.string.text_done));
                            if (T != null) {
                                T.cancel();
                                T.purge();
                                T = null;
                            }
                            stopService();
                            postDataDoneJob();
                            viewLoading.setVisibility(View.VISIBLE);
                        }

                    }
                })
                .setNegativeButton(R.string.text_dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }


    private void checkJobNext() {

        swipeRefreshLayout.setRefreshing(false);
        if (projectsForID.getTaskProjects().size() >= 1) {
            setDataJob(projectsForID);
        } else {
            swipeRefreshLayout.setEnabled(true);
            viewLoading.setVisibility(View.GONE);
            Toast.makeText(this, getString(R.string.notification_task_process), Toast.LENGTH_LONG).show();
        }
    }

    @Subscribe
    public void changeFragmentEvents(Action action) {
        if (action == Action.PAUSE) {
            if (statusActive == 3) {
                Toast.makeText(getApplicationContext(), "Bạn phải kết thúc công việc phát sinh trước.", Toast.LENGTH_SHORT).show();
            } else {
                Paper.book().write("PAUSE", "PAUSE");
                statusActive = 1;
                postDataStatus(1);
                if (T != null) {
                    T.cancel();
                    T.purge();
                    T = null;
                }

                txtTimeJob.setText(getString(R.string.text_pause));
            }
        } else if (action == Action.RESUME) {
            if (statusActive == 3) {
                Toast.makeText(getApplicationContext(), "Bạn phải kết thúc công việc phát sinh trước.", Toast.LENGTH_SHORT).show();
            } else {
                Paper.book().delete("PAUSE");
                statusActive = 2;
                countOutTime();
                postDataStatus(2);
            }
        } else if (action == Action.DONE) {
            if (statusActive == 3) {
                Toast.makeText(getApplicationContext(), "Bạn phải kết thúc công việc phát sinh trước.", Toast.LENGTH_SHORT).show();
            } else {
                txtTimeJob.setText(getString(R.string.text_done));
                if (T != null) {
                    T.cancel();
                    T.purge();
                    T = null;
                }
                stopService();
                postDataDoneJob();
            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void postDataDoneJob() {
        JSONObject paramObject = new JSONObject();
        JsonObject gsonObject = new JsonObject();
        try {
            paramObject.put("task_id_done", projectsForID.getTaskProjects().get(0).get_id());
            if (projectsForID.getTaskProjects().size() > 1) {
                paramObject.put("task_id_next", projectsForID.getTaskProjects().get(1).get_id());
            } else {
                paramObject.put("task_id_next", "");
            }
            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(paramObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestApi.getInstance().postDoneJob(loginData.getToken(), gsonObject).retry(Constants.NUMBER_RETRY_IF_CALL_API_FAIL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonElement>() {
                    @Override
                    public void call(JsonElement jsonElement) {
                        getProjects(loginData.getToken());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    // status 1: pause start / 2: pause end / 3: extra start / 4:extra end / 5: done

    private void postDataStatus(int status) {
        JSONObject paramObject = new JSONObject();
        JsonObject gsonObject = new JsonObject();
        try {
            paramObject.put("task_id", projectsForID.getTaskProjects().get(0).get_id());
            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(paramObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (status == 1) {
            RequestApi.getInstance().postPauseJobStart(loginData.getToken(), gsonObject).retry(Constants.NUMBER_RETRY_IF_CALL_API_FAIL)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<JsonElement>() {
                        @Override
                        public void call(JsonElement jsonElement) {

                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    });
        } else if (status == 2) {
            RequestApi.getInstance().postPauseJobEnd(loginData.getToken(), gsonObject).retry(Constants.NUMBER_RETRY_IF_CALL_API_FAIL)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<JsonElement>() {
                        @Override
                        public void call(JsonElement jsonElement) {

                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    });
        } else if (status == 3) {
            RequestApi.getInstance().postExtraJobStart(loginData.getToken(), gsonObject).retry(Constants.NUMBER_RETRY_IF_CALL_API_FAIL)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<JsonElement>() {
                        @Override
                        public void call(JsonElement jsonElement) {

                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    });
        } else {
            RequestApi.getInstance().postExtraJobEnd(loginData.getToken(), gsonObject).retry(Constants.NUMBER_RETRY_IF_CALL_API_FAIL)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<JsonElement>() {
                        @Override
                        public void call(JsonElement jsonElement) {
                            viewLoading.setVisibility(View.VISIBLE);
                            Paper.book().delete("PAUSE");
                            stopService();
                            getProjects(loginData.getToken());
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
