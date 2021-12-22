package com.hoanmy.kleanco.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hoanmy.kleanco.receivers.NetworkChangeReceiver;
import com.hoanmy.kleanco.utils.NetworkStatusUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragmentSearch extends Fragment {
    private Unbinder unbinder;

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(NetworkChangeReceiver.ACTION_NETWORK_STATE_CHANGED)) {
                if (NetworkStatusUtil.isNetworkAvaiable(getContext())) onNetworkRestore();
                else onNetworkLost();
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getExtras();
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().registerReceiver(br, new IntentFilter(NetworkChangeReceiver.ACTION_NETWORK_STATE_CHANGED));
    }

    @Override
    public void onPause() {
        super.onPause();

        getActivity().unregisterReceiver(br);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(getLayoutId(), container, false);

        unbinder = ButterKnife.bind(this, root);

        onCreateView(savedInstanceState);

        return root;
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    protected void getExtras() {
    }

    protected abstract void onCreateView(Bundle savedInstanceState);

    protected abstract int getLayoutId();

    protected void onNetworkRestore() {
    }

    protected void onNetworkLost() {
    }

    public abstract void beginSearch(String query);
}
