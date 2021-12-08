package com.hoanmy.kleanco.fragments;

import android.os.Bundle;


import com.hoanmy.kleanco.R;
import com.hoanmy.kleanco.commons.IOnBackPressed;

public class LossPassFragment extends BaseFragment implements IOnBackPressed {
    @Override
    protected void onCreateView(Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.loss_password_fragment;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
