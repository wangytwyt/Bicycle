package com.example.administrator.bicycle.manageactivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.bicycle.R;

public class BatchIssueActivity extends AppCompatActivity {
private   TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_batch_issue);

        init();
    }

    private void init() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        TextView tvtitle = (TextView) findViewById(R.id.tv_title);
        tvtitle.setText("发放车辆");

        text  = (TextView) findViewById(R.id.tv_text);
//        text.setMovementMethod(new ScrollingMovementMethod());
//
//         findViewById(R.id.tv_cleaner).setOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View v) {
//                 text.setText("");
//             }
//         });
    }
}
