package com.hoanmy.kleanco.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ctrlplusz.anytextview.AnyTextView;
import com.hoanmy.kleanco.R;
import com.hoanmy.kleanco.models.Employee;
import com.hoanmy.kleanco.models.ProjectDetail;
import com.hoanmy.kleanco.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class EmployeeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private static List<Employee> itemList;
    private static List<Employee> itemListSearch;
    private static Activity mActivity;

    public EmployeeAdapter(Activity activity, List<Employee> items) {
        mActivity = activity;
        this.itemList = items;
        this.itemListSearch = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_employee, parent, false);
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
        @BindView(R.id.profile_image)
        CircleImageView imgAvt;
        @BindView(R.id.txt_name_employee)
        AnyTextView tvName;
        @BindView(R.id.txt_location)
        AnyTextView tvAdress;
        @BindView(R.id.txt_phone_number)
        AnyTextView tvPhoneNumber;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void showViews(final RecyclerView.Adapter adapter, final List<Employee> employees, final int position) {
            Employee employee = employees.get(position);
            Glide.with(mActivity).load(employee.getAvatar()).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(imgAvt);
            tvName.setText(employee.getName());
            tvAdress.setText(employee.getAddress());
            tvPhoneNumber.setText(employee.getPhone());

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
                    List<Employee> filteredList = new ArrayList<>();
                    for (Employee row : itemList) {
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
                itemListSearch = (ArrayList<Employee>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
