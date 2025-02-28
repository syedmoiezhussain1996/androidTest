package com.example.androidtest;


import android.content.Intent;
import android.os.Bundle;
import  com.example.androidtest.R;
import androidx.appcompat.app.AppCompatActivity;



public class AdminPolicyComplianceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy_compliance);

        Intent intent = getIntent();

        setResult(RESULT_OK, intent);
        finish();
    }
}