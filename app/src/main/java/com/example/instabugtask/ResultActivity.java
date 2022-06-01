package com.example.instabugtask;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        LinearLayout linear_headers_parent_host = findViewById(R.id.linear_headers_parent_host);
        TextView tv_Rcode = findViewById(R.id.tv_Rcode);
        TextView tv_error = findViewById(R.id.tv_error);
        ImageView iv_add_header = findViewById(R.id.iv_add_header);
        TextView tv_response_body = findViewById(R.id.tv_response_body);


        iv_add_header.setOnClickListener(v -> {

            linear_headers_parent_host.addView(
                    LayoutInflater.from(getApplicationContext()).inflate(R.layout.key_value, null, false)
            );
        });
        tv_Rcode.setText("200");
        tv_error.setText("No Error");

    }
}
