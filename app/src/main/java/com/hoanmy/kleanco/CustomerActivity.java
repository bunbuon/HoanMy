package com.hoanmy.kleanco;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ctrlplusz.anytextview.AnyTextView;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.hoanmy.kleanco.api.RequestApi;
import com.hoanmy.kleanco.commons.Constants;
import com.hoanmy.kleanco.models.Login;
import com.hoanmy.kleanco.services.NotificationForegroundService;
import com.zing.zalo.zalosdk.oauth.ZaloOpenAPICallback;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class CustomerActivity extends AppCompatActivity implements ZaloOpenAPICallback {
    private String name_manage;
    @BindView(R.id.txt_name_manage)
    AnyTextView txtNameManager;


    private Login loginData;

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
//        startService();
        Intent intent = new Intent(this, NotificationManageActivity.class);
        startActivity(intent);
//        OpenAPIService.getInstance().sendMsgToFriend(this, "sDGw492sLrFSspyKdBXSLTVHC1YSf0i8eBeMBh6g8m6DnmWxnViW3CJ8Fq7QqH5PujifVD_cNcoLopDTkSa8I_RgFMsxwmfnX_K3Oehz8tgNt0fCX_8eG96GF7gSeGn5lv8lTRoBB6Q4gaLLoxbcGjspH5AKWq4UhEaY0FZa0H78nZ4HnAGADFEM6Ihk_Hefu_KEPTJbFI3hoZKGpTWX1lx691pCq0vGzk0T2CRhS2JnrKWOpE5eLTZsSLgMsX91tBOfSv2v7csVj0bwaBzgMBQLQL-DjbK7RK5vMrfrag5HLm", "huoli1997", "aloaloalo", "", this::onResult);

    }
    @OnClick(R.id.btn_manage_job)
    void onClickManage(){

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

        }
    }

    private void getDetailProject(String token) {
        RequestApi.getInstance().getDetailProject(token).retry(Constants.NUMBER_RETRY_IF_CALL_API_FAIL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonElement>() {
                    @Override
                    public void call(JsonElement jsonElement) {
                        Login login = new Gson().fromJson(jsonElement.getAsJsonObject().get("data"), new TypeToken<Login>() {
                        }.getType());


                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
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

    public void startService() {
        Intent serviceIntent = new Intent(this, NotificationForegroundService.class);
        serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    public void stopService() {
        Intent serviceIntent = new Intent(this, NotificationForegroundService.class);
        stopService(serviceIntent);
    }

    @Override
    public void onResult(JSONObject jsonObject) {
        Log.d("TAG", "onResult: ");
    }
}