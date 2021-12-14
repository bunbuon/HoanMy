package com.hoanmy.kleanco.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hoanmy.kleanco.R;
import com.hoanmy.kleanco.models.Employee;
import com.hoanmy.kleanco.models.Project;

import java.util.List;

import butterknife.ButterKnife;

public class EmployeeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Employee> itemList;
    private static Activity mActivity;

    public EmployeeAdapter(Activity activity, List<Employee> items) {
        mActivity = activity;
        this.itemList = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_employee, parent, false);
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



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void showViews(final RecyclerView.Adapter adapter, final List<Employee> notifications, final int position) {

        }


    }
}
