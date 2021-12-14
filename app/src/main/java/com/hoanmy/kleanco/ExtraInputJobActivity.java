package com.hoanmy.kleanco;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.appcompat.widget.Toolbar;

import com.ctrlplusz.anytextview.AnyTextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExtraInputJobActivity extends BaseActivity {
    @BindView(R.id.spinner_employee)
    Spinner spinnerEmployee;

    @BindView(R.id.spinner_job)
    Spinner spinnerJob;

    @BindView(R.id.spinner_project)
    Spinner spinnerProject;
    @BindView(R.id.spinner_frequency)
    Spinner spinnerFrequency;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.value_time_start)
    AnyTextView valueTime;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;
    private int mYear, mMonth, mDay, mHour, mMinute;

    @OnClick(R.id.layout_select_time_start)
    void OnClickTime() {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, mTimeSetListener, mHour, mMinute, false);

        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                c.set(Calendar.MINUTE, mMinute);
                SimpleDateFormat mSDF = new SimpleDateFormat("hh:mm a");
                String time = mSDF.format(c.getTime());
                valueTime.setText(time);
            }
        };
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
        ArrayAdapter<CharSequence> projectAdapter = ArrayAdapter
                .createFromResource(this, R.array.project_arr,
                        android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> employeeAdapter = ArrayAdapter
                .createFromResource(this, R.array.employee_arr,
                        android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> jobAdapter = ArrayAdapter
                .createFromResource(this, R.array.job_arr,
                        android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> frequencyAdapter = ArrayAdapter
                .createFromResource(this, R.array.frequency_arr,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        projectAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        employeeAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        jobAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        frequencyAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinnerProject.setAdapter(projectAdapter);
        spinnerEmployee.setAdapter(employeeAdapter);
        spinnerJob.setAdapter(jobAdapter);
        spinnerFrequency.setAdapter(frequencyAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
