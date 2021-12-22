package com.hoanmy.kleanco;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;

import com.ctrlplusz.anytextview.AnyTextView;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.hoanmy.kleanco.api.RequestApi;
import com.hoanmy.kleanco.commons.Constants;
import com.hoanmy.kleanco.models.Login;
import com.hoanmy.kleanco.models.ProjectsForID;
import com.hoanmy.kleanco.models.TaskProject;
import com.hoanmy.kleanco.services.NotificationForegroundService;
import com.hoanmy.kleanco.utils.Utils;

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
    private long timeTest = 1640201282;
    private long countTime = 1;
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

    @OnClick(R.id.img_notification)
    void onClickNotification() {
        Intent intent = new Intent(this, NotificationManageActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.img_logout)
    void onClickLogout() {
        Utils.loginActivity(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);
        ButterKnife.bind(this);
        loginData = Paper.book().read("login");
        txtNameStaff.setText(loginData.getName());
        txtNumStaff.setText(loginData.getUsername());
        getProjects(loginData.getToken());
    }

    public void startService(String jobName) {
        Intent serviceIntent = new Intent(this, NotificationForegroundService.class);
        serviceIntent.putExtra("jobName", jobName);
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    public void stopService() {
        Intent serviceIntent = new Intent(this, NotificationForegroundService.class);
        stopService(serviceIntent);
    }

    private void getProjects(String token) {
        RequestApi.getInstance().getProjectForId(token, "1").retry(Constants.NUMBER_RETRY_IF_CALL_API_FAIL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonElement>() {
                    @Override
                    public void call(JsonElement jsonElement) {
                        ProjectsForID projectsForID = new Gson().fromJson(jsonElement.getAsJsonObject().get("data"), new TypeToken<ProjectsForID>() {
                        }.getType());
                        setDataJob(projectsForID);

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    private void setDataJob(ProjectsForID dataJob) {
        long timeCurrent = System.currentTimeMillis() / 1000;
        TaskProject dataJobNow = dataJob.getTaskProjects().get(0);
        txtNameJob.setText(dataJobNow.getName());
        txtFloor.setText(dataJobNow.getFloor());
        txtLocation.setText(loginData.getProjectDetail().getAddress());
        initCountDownTimer((timeTest - timeCurrent) * 1000);
        startService(dataJobNow.getName());
    }


    public void initCountDownTimer(long time) {
        new CountDownTimer(time, 1000) {

            public void onTick(long millisUntilFinished) {
                txtTimeJob.setText(
                        String.format("%02d:%02d:%02d",
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 60,
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60));
            }

            public void onFinish() {
                Log.d("TAG", "showViews:onFinish ");
                countOutTime();
            }
        }.start();
    }

    public void countOutTime() {
        long secondsEnd = TimeUnit.MILLISECONDS.toSeconds(timeTest);
        txtTimeJob.setTextColor(getResources().getColor(R.color.red));
        Timer T = new Timer();
        T.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        countTime++;
                        txtTimeJob.setText(
                                String.format("%02d:%02d:%02d",
                                        TimeUnit.SECONDS.toHours(countTime+secondsEnd) % 60,
                                        TimeUnit.SECONDS.toMinutes(countTime+secondsEnd) % 60,
                                        TimeUnit.SECONDS.toSeconds(countTime+secondsEnd) % 60));
//                        txtTimeJob.setText("count=" + countTime);
                    }
                });
            }
        }, 1000, 1000);
    }
}
