package com.hoanmy.kleanco;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hoanmy.kleanco.adapters.ManagementAdapter;
import com.hoanmy.kleanco.models.ManagementItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ManagementActivity extends BaseActivity {
    private LinearLayoutManager linearLayoutManager;
    @BindView(R.id.rcv_management)
    public RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.edt_search)
    EditText edtSearch;
    private ManagementAdapter managementAdapter;
    private List<ManagementItem> itemList;

    @OnClick(R.id.img_search)
    void onClickSearch() {
        if (edtSearch.getVisibility() == View.GONE) {
            edtSearch.setVisibility(View.VISIBLE);
        } else {
            edtSearch.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        managementAdapter = new ManagementAdapter(this, itemList);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(managementAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
