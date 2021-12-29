package com.hoanmy.kleanco;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.ctrlplusz.anytextview.AnyTextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hoanmy.kleanco.models.Login;
import com.hoanmy.kleanco.utils.Utils;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;

public class CustomerActivity extends AppCompatActivity {
    private String name_manage;
    @BindView(R.id.txt_name_manage)
    AnyTextView txtNameManager;
    @BindView(R.id.txt_time_job)
    AnyTextView txtTimeJob;
    @BindView(R.id.txt_mail)
    AnyTextView txtMail;
    @BindView(R.id.txt_phone_number)
    AnyTextView txtPhoneNumber;


    private Login loginData;

    @OnClick(R.id.img_logout)
    void onClickLogout() {
        Utils.loginActivity(this);
    }

    @OnClick(R.id.image_hotline)
    void OnClickHotLine() {
        if (Build.VERSION.SDK_INT < 23) {
            phoneCall();
        } else {

            if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                phoneCall();
            } else {
                final String[] PERMISSIONS_STORAGE = {Manifest.permission.CALL_PHONE};
                //Asking request Permissions
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 9);
            }
        }
    }

    @OnClick(R.id.image_zalo)
    void onClickZalo() {
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.zing.zalo");
        if (intent != null) {
            // We found the activity now start the activity
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            // Bring user to the market or let them choose an app?
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + "com.package.name"));
            startActivity(intent);
        }
    }

    @OnClick(R.id.btn_manage_job)
    void onClickManage() {

        Intent intent = new Intent(this, ManagementActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        ButterKnife.bind(this);
        loginData = Paper.book().read("login");
        if (loginData != null) {
            txtNameManager.setText("Xin chào " + loginData.getName());
            txtMail.setText(loginData.getProjectDetail().getEmail());
            txtTimeJob.setText(loginData.getProjectDetail().getHour_start() + " - " + loginData.getProjectDetail().getHour_end());
            txtPhoneNumber.setText(loginData.getProjectDetail().getPhone());
        }

        FirebaseMessaging.getInstance().subscribeToTopic(loginData.get_id())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.msg_subscribe_failed);
                        }
                        Log.d(Utils.TAG, msg);
                        Toast.makeText(CustomerActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean permissionGranted = false;
        switch (requestCode) {
            case 9:
                permissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (permissionGranted) {
            phoneCall();
        } else {
            Toast.makeText(getApplicationContext(), "You don't assign permission.", Toast.LENGTH_SHORT).show();
        }
    }

    private void phoneCall() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:0337095709"));
            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            getApplicationContext().startActivity(callIntent);
        } else {
            Toast.makeText(getApplicationContext(), "You don't assign permission.", Toast.LENGTH_SHORT).show();
        }
    }


}