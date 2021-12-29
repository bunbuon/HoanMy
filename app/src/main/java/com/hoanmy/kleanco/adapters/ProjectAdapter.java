package com.hoanmy.kleanco.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ctrlplusz.anytextview.AnyTextView;
import com.hoanmy.kleanco.EmployeeListActivity;
import com.hoanmy.kleanco.R;
import com.hoanmy.kleanco.models.ProjectDetail;
import com.hoanmy.kleanco.models.TaskProject;
import com.hoanmy.kleanco.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProjectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private static List<ProjectDetail> itemList;
    private static List<ProjectDetail> itemListSearch;
    private static Activity mActivity;

    public ProjectAdapter(Activity activity, List<ProjectDetail> items) {
        mActivity = activity;
        this.itemList = items;
        this.itemListSearch = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_project, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).showViews(this, itemListSearch, position);
    }

    @Override
    public int getItemCount() {
        return itemListSearch.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.root_view_project)
        LinearLayout rootView;
        @BindView(R.id.txt_name_project)
        AnyTextView tvNameProject;
        @BindView(R.id.txt_location)
        AnyTextView tvLocation;
        @BindView(R.id.txt_phone_number)
        AnyTextView tvPhoneNumber;
        @BindView(R.id.txt_employee_number)
        AnyTextView tvEmployeeNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void showViews(final RecyclerView.Adapter adapter, final List<ProjectDetail> projectDetails, final int position) {
            ProjectDetail projectDetail = projectDetails.get(position);
            tvNameProject.setText(projectDetail.getName());
            tvLocation.setText(projectDetail.getAddress());
            tvPhoneNumber.setText(projectDetail.getPhone());
            tvEmployeeNumber.setText(projectDetail.getNumber_of_staff() + " nhân viên");

            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, EmployeeListActivity.class);
                    intent.putExtra("project_id", projectDetail.get_id());
                    mActivity.startActivity(intent);
                }
            });
        }


    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    itemListSearch = itemList;
                } else {
                    List<ProjectDetail> filteredList = new ArrayList<>();
                    for (ProjectDetail row : itemList) {
                        String nameStaff = Utils.convertString(row.getName());
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getName().contains(charSequence) || nameStaff.toLowerCase().contains(charString.toLowerCase()) || nameStaff.contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    itemListSearch = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = itemListSearch;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                itemListSearch = (ArrayList<ProjectDetail>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
