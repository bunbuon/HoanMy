package com.hoanmy.kleanco.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;

import com.ctrlplusz.anytextview.AnyTextView;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.hoanmy.kleanco.CustomerActivity;
import com.hoanmy.kleanco.EmployeeAtivity;
import com.hoanmy.kleanco.EmployeeListActivity;
import com.hoanmy.kleanco.LoginActivity;
import com.hoanmy.kleanco.ManagerActivity;
import com.hoanmy.kleanco.R;
import com.hoanmy.kleanco.api.RequestApi;
import com.hoanmy.kleanco.commons.Action;
import com.hoanmy.kleanco.commons.Constants;
import com.hoanmy.kleanco.commons.IOnBackPressed;
import com.hoanmy.kleanco.models.Login;
import com.hoanmy.kleanco.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import io.paperdb.Paper;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class LoginFragment extends BaseFragment implements IOnBackPressed {
    @BindView(R.id.status_login)
    AnyTextView txtStatus;

    @BindView(R.id.edt_account)
    AppCompatEditText edtAccount;
    @BindView(R.id.edt_password)
    AppCompatEditText edtPassword;

    @OnClick(R.id.image_back)
    void onBack() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.tv_loss_password)
    void clickLossPass() {
        EventBus.getDefault().post(Action.LOSS_PASSWORD);
    }

    @OnClick(R.id.tv_create_new_account)
    void clickCreateAccount() {
        EventBus.getDefault().post(Action.REGISTER);
    }

    @OnClick(R.id.btn_login)
    void onClickLogin() {
        String userName = edtAccount.getText().toString();
        String pass = edtPassword.getText().toString();

        postDataUser(userName, pass);
    }

    private void postDataUser(String userName, String passWord) {
        JSONObject paramObject = new JSONObject();
        JsonObject gsonObject = new JsonObject();
        try {
            paramObject.put("username", userName);
            paramObject.put("password", passWord);
            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(paramObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestApi.getInstance().postLogin(gsonObject).retry(Constants.NUMBER_RETRY_IF_CALL_API_FAIL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonElement>() {
                    @Override
                    public void call(JsonElement jsonElement) {
                        Login login = new Gson().fromJson(jsonElement.getAsJsonObject().get("data"), new TypeToken<Login>() {
                        }.getType());
                        if (login == null) {
                            txtStatus.setVisibility(View.VISIBLE);
                        } else {
                            txtStatus.setVisibility(View.GONE);
                            setRole(login);
                        }


                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });

    }

    private void setRole(Login login) {
        Paper.book().write("login", login);
        String role = login.getRole();
        if (role.equals("admin")) {
            Paper.book().write(getString(R.string.status_login), "admin");
            Utils.nextHome(getActivity());
        } else if (role.equals("customer")) {
            Paper.book().write(getString(R.string.status_login), "customer");
            Utils.nextCustomer(getActivity());
        } else if (role.equals("employee")) {
            Paper.book().write(getString(R.string.status_login), "employee");
            Utils.nextEmployee(getActivity());
        } else {
            txtStatus.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onCreateView(Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.login_fragment;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }


}
