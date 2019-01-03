package com.attra.attralive.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.attra.attralive.R;

public class DisplayUserProfileDetails extends AppCompatActivity {

    TextView username, designation,DOB,gender,BU,phone,email, submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_user_profile_details);

        username = findViewById(R.id.et_entername);
        designation=findViewById(R.id.et_designation);
        DOB = findViewById(R.id.tv_userDob);
        gender = findViewById(R.id.gendertype);
        BU = findViewById(R.id.BUType);
        phone = findViewById(R.id.et_mobilenumber);
        email= findViewById(R.id.emailid);
        submit = findViewById(R.id.btn_submitDetails);


    }
}
