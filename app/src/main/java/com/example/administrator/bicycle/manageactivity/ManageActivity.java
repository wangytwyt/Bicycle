package com.example.administrator.bicycle.manageactivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.administrator.bicycle.R;

public class ManageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_manage);
    }
}
