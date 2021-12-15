package com.hoanmy.kleanco.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hoanmy.kleanco.EmployeeListActivity;
import com.hoanmy.kleanco.R;
import com.hoanmy.kleanco.models.ProjectDetail;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProjectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ProjectDetail> itemList;
    private static Activity mActivity;

    public ProjectAdapter(Activity activity, List<ProjectDetail> items) {
        mActivity = activity;
        this.itemList = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_project, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).showViews(this, itemList, position);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.root_view_project)
        LinearLayout rootView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void showViews(final RecyclerView.Adapter adapter, final List<ProjectDetail> notifications, final int position) {
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, EmployeeListActivity.class);
                    mActivity.startActivity(intent);
                }
            });
        }


    }
}
