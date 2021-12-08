package com.hoanmy.kleanco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ctrlplusz.anytextview.AnyTextView;
import com.zing.zalo.zalosdk.oauth.FeedData;
import com.zing.zalo.zalosdk.oauth.OpenAPIService;
import com.zing.zalo.zalosdk.oauth.ZaloOpenAPICallback;
import com.zing.zalo.zalosdk.oauth.ZaloPluginCallback;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements ZaloOpenAPICallback {
    private String name_manage;
    @BindView(R.id.txt_name_manage)
    AnyTextView txtNameManager;


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

        OpenAPIService.getInstance().sendMsgToFriend(this, "sDGw492sLrFSspyKdBXSLTVHC1YSf0i8eBeMBh6g8m6DnmWxnViW3CJ8Fq7QqH5PujifVD_cNcoLopDTkSa8I_RgFMsxwmfnX_K3Oehz8tgNt0fCX_8eG96GF7gSeGn5lv8lTRoBB6Q4gaLLoxbcGjspH5AKWq4UhEaY0FZa0H78nZ4HnAGADFEM6Ihk_Hefu_KEPTJbFI3hoZKGpTWX1lx691pCq0vGzk0T2CRhS2JnrKWOpE5eLTZsSLgMsX91tBOfSv2v7csVj0bwaBzgMBQLQL-DjbK7RK5vMrfrag5HLm", "huoli1997", "aloaloalo", "", this::onResult);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        name_manage = getIntent().getStringExtra("NAME");
        txtNameManager.setText("Xin chào " + name_manage);
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

    @Override
    public void onResult(JSONObject jsonObject) {
        Log.d("TAG", "onResult: ");
    }
}