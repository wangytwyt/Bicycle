package com.example.administrator.bicycle.zxing.camera.open;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.bicycle.Kaisuo.KaisuoActivity;
import com.example.administrator.bicycle.R;

public class InputActivity extends AppCompatActivity {
    EditText mingpai;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        mingpai = (EditText) findViewById(R.id.mingpai);
        ImageView go = (ImageView) findViewById(R.id.go);

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = mingpai.getText().toString();

                if (data != null && !data.equals("")) {
                    Intent intent = new Intent(InputActivity.this, KaisuoActivity.class);
                    intent.putExtra("data", data);
                    startActivity(intent);
                } else {
                    Toast.makeText(InputActivity.this, "请输入正确的铭牌号", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
