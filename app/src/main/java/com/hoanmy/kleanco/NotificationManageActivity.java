package com.hoanmy.kleanco;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hoanmy.kleanco.adapters.NotificationAdapter;
import com.hoanmy.kleanco.models.Notification;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationManageActivity extends BaseActivity {

    private LinearLayoutManager linearLayoutManager;
    @BindView(R.id.rcv_list)
    public RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private NotificationAdapter notificationAdapter;
    private List<Notification> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_layout);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.notification);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        notificationAdapter = new NotificationAdapter(this, itemList);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(notificationAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
