package com.hoanmy.kleanco;

import android.content.Intent;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ManagerActivity extends BaseActivity {
    @OnClick(R.id.btn_list_project)
    void onClickListProject() {
        Intent intent = new Intent(this, ProjectActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_input)
    void onClickInput() {
        Intent intent = new Intent(this, ExtraInputJobActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.layout_notification)
    void onClickNotification() {
        Intent intent = new Intent(this, NotificationManageActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        ButterKnife.bind(this);
    }
}
