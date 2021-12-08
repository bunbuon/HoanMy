package com.hoanmy.kleanco.fragments;

import android.content.Intent;
import android.os.Bundle;


import androidx.appcompat.widget.AppCompatEditText;

import com.hoanmy.kleanco.MainActivity;
import com.hoanmy.kleanco.R;
import com.hoanmy.kleanco.commons.Action;
import com.hoanmy.kleanco.commons.IOnBackPressed;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginFragment extends BaseFragment implements IOnBackPressed {
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
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra("NAME", edtAccount.getText().toString());
        getContext().startActivity(intent);
        getActivity().finish();

    }

    @BindView(R.id.edt_account)
    AppCompatEditText edtAccount;
    @BindView(R.id.edt_password)
    AppCompatEditText edtPassword;

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
