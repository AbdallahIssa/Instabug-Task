package com.example.instabugtask;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @SuppressLint({"InflateParams", "NonConstantResourceId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        RadioGroup radio_group = findViewById(R.id.radio_group);
        RadioButton rb_post = findViewById(R.id.rb_post);
        RadioButton rb_get = findViewById(R.id.rb_get);
        LinearLayout linear_headers_parent_host = findViewById(R.id.linear_headers_parent_host);
        ImageView iv_remove_item = findViewById(R.id.iv_remove_item);
        ImageView iv_add_header = findViewById(R.id.iv_add_header);
        LinearLayout linear_request_body = findViewById(R.id.linear_request_body);


        Button btn_send = findViewById(R.id.btn_send);


        /*iv_remove_item.setOnClickListener(v -> {
            //todo : manage how to remove the last pressed
            linear_headers_parent_host.removeViewAt(0);
        });*/


        iv_add_header.setOnClickListener(v ->
                linear_headers_parent_host.addView(
                        LayoutInflater.from(getApplicationContext()).inflate(R.layout.key_value, null, false)
                ));
        rb_get.setOnClickListener(v1 ->
                linear_request_body.setVisibility(View.INVISIBLE));

        rb_post.setOnClickListener(v12 ->
                linear_request_body.setVisibility(View.VISIBLE));


        btn_send.setOnClickListener(v -> {
            if (Network.isNetworkAvailable(getApplication())) {
                int id = radio_group.getCheckedRadioButtonId();
                switch (id) {
                    case R.id.rb_get:
                        get();
                        break;
                    case R.id.rb_post:
                        post();
                        break;
                }

                startActivity(
                        new Intent(MainActivity.this, ResultActivity.class)
                );
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(true);
                builder.setTitle("NO INTERNET");
                builder.setMessage("Your device is offline, can't connect");
                builder.setNegativeButton("OK", (dialog, which) -> dialog.cancel());
                builder.show();
                Toast.makeText(getApplicationContext(), "Not Connected to the Internet!", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void post() {

        //private static final String TAG = "MyHttpRequestTask";
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {

            EditText ex_url = findViewById(R.id.ex_url);
            EditText et_request_body = findViewById(R.id.et_request_body);

            try {
                URL url = new URL(ex_url.getText().toString());
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                // setting the Request Method Type
                httpURLConnection.setRequestMethod("POST");
                LinearLayout linear_headers_parent_host = findViewById(R.id.linear_headers_parent_host);
                httpURLConnection.setRequestProperty(((EditText) (((LinearLayout) linear_headers_parent_host.getChildAt(0)).getChildAt(0))).getText().toString(),
                        ((EditText) (((LinearLayout) linear_headers_parent_host.getChildAt(0)).getChildAt(1))).getText().toString()
                );

                try {
                    //to tell the connection object that we will be writing some data on the server and then will fetch the output result
                    httpURLConnection.setDoOutput(true);
                    // this is used for just in case we don't know about the data size associated with our request
                    httpURLConnection.setChunkedStreamingMode(0);

                    // to write tha data in our request
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                    outputStreamWriter.write(Arrays.toString(et_request_body.getText().toString().getBytes()));
                    outputStreamWriter.flush();
                    outputStreamWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    // this is done so that there are no open connections left when this task is going to complete
                    httpURLConnection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        });

    }


    public void get() {

        EditText ex_url = findViewById(R.id.ex_url);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {

            try {
                URL obj = new URL(ex_url.getText().toString());
                HttpURLConnection httpURLConnection = (HttpURLConnection) obj.openConnection();
                try {
                    httpURLConnection.setRequestMethod("GET");
                    if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(
                                httpURLConnection.getInputStream()));
                        String inputLine;
                        StringBuilder response = new StringBuilder();

                        while ((inputLine = reader.readLine()) != null) {
                            response.append(inputLine);
                        }
                        reader.close();

                        Log.d(TAG, response.toString());
                    } else {
                        Log.d(TAG, "GET request not worked");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

}
