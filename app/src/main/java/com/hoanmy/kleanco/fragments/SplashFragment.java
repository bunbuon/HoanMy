package com.hoanmy.kleanco.fragments;

import android.os.Bundle;

import com.hoanmy.kleanco.R;
import com.hoanmy.kleanco.commons.IOnBackPressed;

public class SplashFragment extends BaseFragment implements IOnBackPressed {


    @Override
    protected void onCreateView(Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.splash_fragment;
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }
}
