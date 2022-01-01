package com.hoanmy.kleanco;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hoanmy.kleanco.commons.Constants;
import com.hoanmy.kleanco.models.Login;
import com.hoanmy.kleanco.utils.Utils;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;

public class ManagerActivity extends BaseActivity {
    Login login;

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
        login = Paper.book().read("login");
        Utils.loginActivity(this, login);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        ButterKnife.bind(this);

        FirebaseMessaging.getInstance().subscribeToTopic(Constants.KEY_SUBSCRIBE_ADMIN);
        FirebaseMessaging.getInstance().subscribeToTopic(Constants.KEY_SUBSCRIBE_ALL)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.msg_subscribe_failed);
                        }
                        Log.d(Utils.TAG, msg);
                    }
                });

    }
}
