package com.hoanmy.kleanco;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;

import com.hoanmy.kleanco.services.NotificationForegroundService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EmployeeAtivity extends BaseActivity {
    @OnClick(R.id.layout_notification)
    void onClickNotification() {
        Intent intent = new Intent(this, NotificationManageActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);
        ButterKnife.bind(this);
        startService();
    }

    public void startService() {
        Intent serviceIntent = new Intent(this, NotificationForegroundService.class);
        serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    public void stopService() {
        Intent serviceIntent = new Intent(this, NotificationForegroundService.class);
        stopService(serviceIntent);
    }
}
