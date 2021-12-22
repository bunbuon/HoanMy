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
import com.hoanmy.kleanco.adapters.TaskProcessAdapter;
import com.hoanmy.kleanco.api.RequestApi;
import com.hoanmy.kleanco.commons.Constants;
import com.hoanmy.kleanco.models.Login;
import com.hoanmy.kleanco.models.ProjectsForID;
import com.hoanmy.kleanco.models.TaskProject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.paperdb.Paper;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class FragmentTaskProcess extends BaseFragmentSearch {
    private LinearLayoutManager linearLayoutManager;
    @BindView(R.id.rcv_list)
    public RecyclerView recyclerView;

    @BindView(R.id.loadingBar)
    ProgressBar progressBar;
    private TaskProcessAdapter taskProcessAdapter;
    private List<TaskProject> itemList = new ArrayList<>();

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        Login loginData = Paper.book().read("login");
        linearLayoutManager = new LinearLayoutManager(getContext());

        taskProcessAdapter = new TaskProcessAdapter(getActivity(), itemList);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(taskProcessAdapter);
        getDetailProject(loginData.getToken());
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
        RequestApi.getInstance().getProjectForId(token, "1").retry(Constants.NUMBER_RETRY_IF_CALL_API_FAIL)
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
                        taskProcessAdapter.notifyDataSetChanged();

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

}
