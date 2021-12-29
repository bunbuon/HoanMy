package com.hoanmy.kleanco.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ctrlplusz.anytextview.AnyTextView;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.hoanmy.kleanco.R;
import com.hoanmy.kleanco.adapters.TaskProcessAdapter;
import com.hoanmy.kleanco.api.RequestApi;
import com.hoanmy.kleanco.commons.Action;
import com.hoanmy.kleanco.commons.Constants;
import com.hoanmy.kleanco.commons.RemoveItemViewListener;
import com.hoanmy.kleanco.models.Login;
import com.hoanmy.kleanco.models.ProjectsForID;
import com.hoanmy.kleanco.models.TaskProject;
import com.hoanmy.kleanco.utils.Utils;

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

public class FragmentTaskProcess extends BaseFragmentSearch implements RemoveItemViewListener {
    private LinearLayoutManager linearLayoutManager;
    @BindView(R.id.rcv_list)
    public RecyclerView recyclerView;
    @BindView(R.id.txt_notification)
    AnyTextView txtNotification;
    @BindView(R.id.loadingBar)
    ProgressBar progressBar;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeRefreshLayout;
    private TaskProcessAdapter taskProcessAdapter;
    private List<TaskProject> itemList = new ArrayList<>();
    public static List<TaskProject> itemListDone = new ArrayList<>();

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        Login loginData = Paper.book().read("login");
        linearLayoutManager = new LinearLayoutManager(getContext());

        taskProcessAdapter = new TaskProcessAdapter(getActivity(), itemList, this::onChangeData);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(taskProcessAdapter);
        getDetailProject(loginData.getToken());
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getContext(), getString(R.string.refresh_list_staff), Toast.LENGTH_SHORT).show();
                getDetailProject(loginData.getToken());
            }
        });
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.recyclerview;
    }

    @Override
    public void beginSearch(String query) {
        taskProcessAdapter.getFilter().filter(query);
    }

    private void getDetailProject(String token) {
        String fDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        RequestApi.getInstance().getProjectForId(token, "", fDate, 500).retry(Constants.NUMBER_RETRY_IF_CALL_API_FAIL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonElement>() {
                    @Override
                    public void call(JsonElement jsonElement) {
                        ProjectsForID projectsForID = new Gson().fromJson(jsonElement.getAsJsonObject().get("data"), new TypeToken<ProjectsForID>() {
                        }.getType());
                        initDataList(projectsForID.getTaskProjects());


                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    private void initDataList(List<TaskProject> taskProjects) {
        itemList.clear();
        itemListDone.clear();
        List<TaskProject> _itemList = new ArrayList<>();
        List<TaskProject> _itemListDone = new ArrayList<>();
        for (int i = 0; i < taskProjects.size(); i++) {
            if (taskProjects.get(i).getFeedback_status() == 0) {
                _itemList.add(taskProjects.get(i));
            } else if (taskProjects.get(i).getFeedback_status() == 1) {
                _itemListDone.add(taskProjects.get(i));
            }
        }
        itemListDone.addAll(_itemListDone);
        EventBus.getDefault().post(Action.REQUEST_API_TASK_DONE);

        itemList.clear();
        itemList.addAll(_itemList);
        swipeRefreshLayout.setRefreshing(false);
        progressBar.setVisibility(View.GONE);
        if (itemList.size() > 0) {
            txtNotification.setVisibility(View.GONE);
            taskProcessAdapter.notifyDataSetChanged();
        } else {
            txtNotification.setVisibility(View.VISIBLE);
            txtNotification.setText(R.string.notification_task_process);
        }
    }

    @Subscribe
    public void changeDataTaskDone(Action action) {
//        if (action == Action.PUSH_CHECK_LIST) {
//            txtNotification.setVisibility(View.VISIBLE);
//            txtNotification.setText(R.string.notification_task_process);
//        }
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
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onChangeData(TaskProject taskProject) {
        itemListDone.add(taskProject);
        EventBus.getDefault().post(Action.REQUEST_API_TASK_DONE);
        itemList.remove(taskProject);
        taskProcessAdapter.notifyDataSetChanged();

        if (itemList.size() == 0) {
            txtNotification.setVisibility(View.VISIBLE);
            txtNotification.setText(R.string.notification_task_process);
        }
    }
}
