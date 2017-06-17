package com.example.administrator.bicycle.Personal;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.bicycle.Post.Juhe;
import com.example.administrator.bicycle.R;
import com.example.administrator.bicycle.util.ContentValuse;

import org.json.JSONArray;
import org.json.JSONObject;

public class CertificationActivity extends AppCompatActivity {
    private EditText ed_Certification;
    private String cardID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_certification);
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
        tvtitle.setText("实名认证");


        ed_Certification = (EditText) findViewById(R.id.ed_Certification);

        findViewById(R.id.but_certification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardID = ed_Certification.getText().toString().trim();
                if (cardID != null) {
                    new CertificationAsty(cardID).execute();
                }

            }
        });
    }

    class CertificationAsty extends AsyncTask<String, String, String> {
        private String id;

        public CertificationAsty(String id) {
            this.id = id;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject object = new JSONObject(s);
                if (object.getInt("error_code") == 0) {
                    System.out.println(object.get("result"));
                    Toast.makeText(CertificationActivity.this,"验证成功",Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent();
                    intent.putExtra(ContentValuse.Realname,true);
                    setResult(1,intent);
                    CertificationActivity.this.finish();
                } else {
                    Toast.makeText(CertificationActivity.this,"验证失败"+object.get("error_code") + ":" + object.get("reason"),Toast.LENGTH_SHORT).show();
                    System.out.println(object.get("error_code") + ":" + object.get("reason"));
                }
            } catch (Exception e) {

            }

        }

        @Override
        protected String doInBackground(String... params) {
            return Juhe.getRequest1(id);
        }
    }

}
