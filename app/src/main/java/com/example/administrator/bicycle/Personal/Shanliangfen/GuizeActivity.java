package com.example.administrator.bicycle.Personal.Shanliangfen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.administrator.bicycle.R;

public class GuizeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_guize);

        View title = findViewById(R.id.title);
        LinearLayout finish1 = (LinearLayout) title.findViewById(R.id.finish1);
        finish1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
