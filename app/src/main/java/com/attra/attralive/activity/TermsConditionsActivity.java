package com.attra.attralive.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.attra.attralive.R;


public class TermsConditionsActivity extends AppCompatActivity {

    private Button okButton;
    TextView termsConditionTextview;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);

        /*termsConditionTextview = (TextView) findViewById(R.id.termsConditionDataId1);

        termsConditionTextview.setText(R.string.termscontent);

        okButton = (Button) findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                    startActivity(intent);
            }
        });*/
    }
}
