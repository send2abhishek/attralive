package com.attra.attralive.activity;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.attra.attralive.R;
import com.attra.attralive.fragment.DatePickerFragment;

public class UserDetailsActivity extends AppCompatActivity {
    Spinner designation,bu,location;
    Button continueBtn;
    private static final String[] designList = {"Software Engineer","Project Lead", "Project Lead", "Project manager"};
    private static final String[] buList = {"Synchrony","Practice", "APAC", "Corporate"};
    private static final String[] locationList = {"Bangalore","Hydrabad", "Pune", "Melburn"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        designation = findViewById(R.id.sp_userDesignation);
        bu = findViewById(R.id.sp_userBU);
        location = findViewById(R.id.sp_userWorkLocation);
        continueBtn = findViewById(R.id.btn_continue);

        ArrayAdapter<String>designationAdapter = new ArrayAdapter<String>(UserDetailsActivity.this,
                android.R.layout.simple_spinner_item,designList);

        designationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        designation.setAdapter(designationAdapter);

        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(UserDetailsActivity.this,
                android.R.layout.simple_spinner_item,locationList);

        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        location.setAdapter(locationAdapter);

        ArrayAdapter<String> userBuAdapter = new ArrayAdapter<>(UserDetailsActivity.this,
                android.R.layout.simple_spinner_item,buList);

        userBuAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bu.setAdapter(userBuAdapter);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),DashboardActivity.class);
                startActivity(intent);
            }
        });




    }
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }



}
