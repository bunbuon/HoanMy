package com.hoanmy.kleanco;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;

import com.ctrlplusz.anytextview.AnyEditTextView;
import com.ctrlplusz.anytextview.AnyTextView;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.hoanmy.kleanco.api.RequestApi;
import com.hoanmy.kleanco.commons.Constants;
import com.hoanmy.kleanco.models.Employee;
import com.hoanmy.kleanco.models.Login;
import com.hoanmy.kleanco.models.ProjectDetail;
import com.hoanmy.kleanco.models.Projects;
import com.hoanmy.kleanco.models.Staffs;
import com.hoanmy.kleanco.searchable.SimpleArrayListAdapter;
import com.hoanmy.kleanco.searchable.SimpleListAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.IStatusListener;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.OnItemSelectedListener;
import io.paperdb.Paper;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ExtraInputJobActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private int lastSelectedHour = -1;
    private int lastSelectedMinute = -1;

    @BindView(R.id.tvText_time)
    AnyTextView editTextTime;
    @BindView(R.id.editText_time)
    AnyEditTextView edtTimeMinute;
    @BindView(R.id.loadingBar)
    ProgressBar progressBar;

    @OnClick(R.id.tvText_time)
    void onClickShowDialogTime() {
        if (this.lastSelectedHour == -1) {
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            this.lastSelectedHour = c.get(Calendar.HOUR_OF_DAY);
            this.lastSelectedMinute = c.get(Calendar.MINUTE);
        }
        final boolean is24HView = true;
        final boolean isSpinnerMode = false;

        // Time Set Listener.
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                editTextTime.setText(hourOfDay + ":" + minute);
                lastSelectedHour = hourOfDay;
                lastSelectedMinute = minute;
            }
        };

        // Create TimePickerDialog:
        TimePickerDialog timePickerDialog = null;

        // TimePicker in Spinner Mode:
        if (isSpinnerMode) {
            timePickerDialog = new TimePickerDialog(this,
                    android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                    timeSetListener, lastSelectedHour, lastSelectedMinute, is24HView);
        }
        // TimePicker in Clock Mode (Default):
        else {
            timePickerDialog = new TimePickerDialog(this,
                    timeSetListener, lastSelectedHour, lastSelectedMinute, is24HView);
        }

        // Show
        timePickerDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.extra_job_layout);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.input_job_extra);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        progressBar.setVisibility(View.VISIBLE);
        getProjectList();
        mSimpleListAdapter = new SimpleListAdapter(this, mStringsProject);
        mSimpleArrayListAdapter = new SimpleArrayListAdapter(this, mStringsEmployee);

        mSearchableSpinner1.setAdapter(mSimpleListAdapter);
        mSearchableSpinner1.setOnItemSelectedListener(mOnItemSelectedListener);
        mSearchableSpinner1.setStatusListener(new IStatusListener() {
            @Override
            public void spinnerIsOpening() {
            }

            @Override
            public void spinnerIsClosing() {

            }
        });
        mSearchableSpinner2.setAdapter(mSimpleArrayListAdapter);
//        mSearchableSpinner1.setOnItemSelectedListener(mOnItemSelectedListener);
        mSearchableSpinner2.setStatusListener(new IStatusListener() {
            @Override
            public void spinnerIsOpening() {
            }

            @Override
            public void spinnerIsClosing() {

            }
        });


    }


    @BindView(R.id.ssProjects)
    SearchableSpinner mSearchableSpinner1;

    @BindView(R.id.ssEmployees)
    SearchableSpinner mSearchableSpinner2;
    private SimpleListAdapter mSimpleListAdapter;
    private SimpleArrayListAdapter mSimpleArrayListAdapter;
    private final ArrayList<String> mStringsProject = new ArrayList<>();
    private final ArrayList<String> mStringsEmployee = new ArrayList<>();
    private OnItemSelectedListener mOnItemSelectedListener = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(View view, int position, long id) {

            progressBar.setVisibility(View.VISIBLE);
            getEmployeeList(itemListProjects.get(position - 1).get_id());
        }

        @Override
        public void onNothingSelected() {
            Toast.makeText(getApplicationContext(), "Nothing Selected", Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (!mSearchableSpinner1.isInsideSearchEditText(event)) {
            mSearchableSpinner1.hideEdit();
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private List<Employee> itemListEmployees = new ArrayList<>();
    private List<ProjectDetail> itemListProjects = new ArrayList<>();

    private void getProjectList() {
        Login loginData = Paper.book().read("login");
        RequestApi.getInstance().getProjects(loginData.getToken()).retry(Constants.NUMBER_RETRY_IF_CALL_API_FAIL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonElement>() {
                    @Override
                    public void call(JsonElement jsonElement) {
                        Projects data = new Gson().fromJson(jsonElement.getAsJsonObject().get("data"), new TypeToken<Projects>() {
                        }.getType());
                        itemListProjects.clear();
                        itemListProjects.addAll(data.getProjects());
                        getNameProject();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    private void getNameProject() {
        for (int i = 0; i < itemListProjects.size(); i++) {
            mStringsProject.add(itemListProjects.get(i).getName());
        }
        mSimpleListAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
    }

    private void getNameEmployee() {
        for (int i = 0; i < itemListEmployees.size(); i++) {
            mStringsEmployee.add(itemListEmployees.get(i).getName());
        }
        mSimpleArrayListAdapter.notifyDataSetChanged();

        progressBar.setVisibility(View.GONE);

    }

    private void getEmployeeList(String projectID) {
        Login loginData = Paper.book().read("login");
        RequestApi.getInstance().getEmployees(loginData.getToken(), projectID).retry(Constants.NUMBER_RETRY_IF_CALL_API_FAIL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonElement>() {
                    @Override
                    public void call(JsonElement jsonElement) {
                        Staffs data = new Gson().fromJson(jsonElement.getAsJsonObject().get("data"), new TypeToken<Staffs>() {
                        }.getType());
                        itemListEmployees.clear();
                        itemListEmployees.addAll(data.getStaffs());
                        getNameEmployee();

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

}
