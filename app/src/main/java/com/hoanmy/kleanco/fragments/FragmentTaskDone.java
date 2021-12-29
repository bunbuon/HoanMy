package com.hoanmy.kleanco.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ctrlplusz.anytextview.AnyTextView;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.hoanmy.kleanco.R;
import com.hoanmy.kleanco.adapters.TaskDoneAdapter;
import com.hoanmy.kleanco.api.RequestApi;
import com.hoanmy.kleanco.commons.Action;
import com.hoanmy.kleanco.commons.Constants;
import com.hoanmy.kleanco.models.Login;
import com.hoanmy.kleanco.models.ProjectsForID;
import com.hoanmy.kleanco.models.TaskProject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import io.paperdb.Paper;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class FragmentTaskDone extends BaseFragmentSearch {
    private LinearLayoutManager linearLayoutManager;
    @BindView(R.id.rcv_list)
    public RecyclerView recyclerView;
    @BindView(R.id.loadingBar)
    ProgressBar progressBar;

    @BindView(R.id.txt_notification)
    AnyTextView txtNotification;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeRefreshLayout;
    private TaskDoneAdapter taskDoneAdapter;
    private List<TaskProject> itemList = new ArrayList<>();
    private Login loginData;

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        loginData = Paper.book().read("login");
        linearLayoutManager = new LinearLayoutManager(getContext());
        swipeRefreshLayout.setEnabled(false);
        taskDoneAdapter = new TaskDoneAdapter(getActivity(), itemList);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(taskDoneAdapter);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.recyclerview;
    }

    @Override
    public void beginSearch(String query) {
        taskDoneAdapter.getFilter().filter(query);
    }


    @Subscribe
    public void changeDataTaskDone(Action action) {
        if (action == Action.REQUEST_API_TASK_DONE) {
            if (taskDoneAdapter != null) {
                itemList.clear();
                itemList.addAll(FragmentTaskProcess.itemListDone);
                taskDoneAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
