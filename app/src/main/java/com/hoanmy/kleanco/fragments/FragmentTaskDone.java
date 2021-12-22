package com.hoanmy.kleanco.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
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
    private TaskDoneAdapter taskDoneAdapter;
    private List<TaskProject> itemList = new ArrayList<>();
    private Login loginData;

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        loginData = Paper.book().read("login");
        linearLayoutManager = new LinearLayoutManager(getContext());

        taskDoneAdapter = new TaskDoneAdapter(getActivity(), itemList);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(taskDoneAdapter);
        getDetailProject(loginData.getToken());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.recyclerview;
    }

    @Override
    public void beginSearch(String query) {
        taskDoneAdapter.getFilter().filter(query);
    }

    private void getDetailProject(String token) {
        RequestApi.getInstance().getProjectForId(token, "4,5").retry(Constants.NUMBER_RETRY_IF_CALL_API_FAIL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonElement>() {
                    @Override
                    public void call(JsonElement jsonElement) {
                        ProjectsForID projectsForID = new Gson().fromJson(jsonElement.getAsJsonObject().get("data"), new TypeToken<ProjectsForID>() {
                        }.getType());
                        progressBar.setVisibility(View.GONE);
                        itemList.clear();
                        itemList.addAll(projectsForID.getTaskProjects());
                        taskDoneAdapter.notifyDataSetChanged();

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    @Subscribe
    public void changeDataTaskDone(Action action) {
        if (action == Action.REQUEST_API_TASK_DONE) {
            progressBar.setVisibility(View.VISIBLE);
            getDetailProject(loginData.getToken());
        }
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
}
