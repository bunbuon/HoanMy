package com.hoanmy.kleanco;

import android.content.Intent;
import android.os.Bundle;

import com.hoanmy.kleanco.utils.Utils;

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
        setContentView(R.layout.activity_manager);
        ButterKnife.bind(this);
    }
}
