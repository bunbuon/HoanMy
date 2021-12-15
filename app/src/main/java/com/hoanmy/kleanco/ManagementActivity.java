package com.hoanmy.kleanco;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.hoanmy.kleanco.adapters.ManagementAdapter;
import com.hoanmy.kleanco.api.RequestApi;
import com.hoanmy.kleanco.commons.Constants;
import com.hoanmy.kleanco.models.Login;
import com.hoanmy.kleanco.models.ManagementItem;
import com.hoanmy.kleanco.models.ProjectsForID;
import com.hoanmy.kleanco.models.TaskProject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ManagementActivity extends BaseActivity {
    private LinearLayoutManager linearLayoutManager;
    @BindView(R.id.rcv_management)
    public RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    SearchView searchView;
    private ManagementAdapter managementAdapter;
    private List<TaskProject> itemList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);
        ButterKnife.bind(this);
        Login loginData = Paper.book().read("login");
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(loginData.getProject_id());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        managementAdapter = new ManagementAdapter(this, itemList);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(managementAdapter);
        getDetailProject(loginData.getToken());
    }

    private void getDetailProject(String token) {
        RequestApi.getInstance().getProjectForId(token).retry(Constants.NUMBER_RETRY_IF_CALL_API_FAIL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonElement>() {
                    @Override
                    public void call(JsonElement jsonElement) {
                        ProjectsForID projectsForID = new Gson().fromJson(jsonElement.getAsJsonObject().get("data"), new TypeToken<ProjectsForID>() {
                        }.getType());
                        itemList.addAll(projectsForID.getTaskProjects());
                        managementAdapter.notifyDataSetChanged();

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                managementAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                managementAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

}
