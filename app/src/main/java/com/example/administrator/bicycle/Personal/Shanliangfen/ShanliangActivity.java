package com.example.administrator.bicycle.Personal.Shanliangfen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.administrator.bicycle.R;

public class ShanliangActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout guizejiedu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_shanliang);

        guizejiedu = (LinearLayout) findViewById(R.id.line_guizejiedu);
        guizejiedu.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.line_guizejiedu:
                startActivity(new Intent(ShanliangActivity.this,GuizeActivity.class));
                break;
        }
    }
}
