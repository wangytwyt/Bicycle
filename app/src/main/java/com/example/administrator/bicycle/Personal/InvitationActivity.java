package com.example.administrator.bicycle.Personal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.administrator.bicycle.R;

public class InvitationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);
    }
}
