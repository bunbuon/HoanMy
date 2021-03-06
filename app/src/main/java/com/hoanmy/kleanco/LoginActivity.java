package com.hoanmy.kleanco;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hoanmy.kleanco.commons.Action;
import com.hoanmy.kleanco.commons.IOnBackPressed;
import com.hoanmy.kleanco.fragments.LoginFragment;
import com.hoanmy.kleanco.fragments.LossPassFragment;
import com.hoanmy.kleanco.fragments.RegisterFragment;
import com.hoanmy.kleanco.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.security.MessageDigest;

import io.paperdb.Paper;

public class LoginActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Paper.book().read("STATUS_LOGIN") != null) {
            if (Paper.book().read("STATUS_LOGIN").equals("admin".trim())) {
                Utils.nextHome(this);
            } else if (Paper.book().read("STATUS_LOGIN").equals("customer".trim())) {
                Utils.nextCustomer(this);
            } else if (Paper.book().read("STATUS_LOGIN").equals("staff".trim())) {
                Utils.nextEmployee(this);
            }
        } else
            addFragment(new LoginFragment());

    }


    private void addFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, fragment, null);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Subscribe
    public void changeFragmentEvents(Action action) {
        if (action == Action.HAVE_ACCOUNT)
            addFragment(new LoginFragment());
        else if (action == Action.REGISTER)
            addFragment(new RegisterFragment());
        else if (action == Action.LOSS_PASSWORD)
            addFragment(new LossPassFragment());
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            super.onBackPressed();
        } else
            finish();

    }
}
