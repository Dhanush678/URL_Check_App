package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button start = findViewById(R.id.start);
        final EditText URL = findViewById(R.id.URL);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editTextData = URL.getText().toString();
                checkMaliciousURL(editTextData);
            }
        });
    }

    private void checkMaliciousURL(String url) {
        String apiKey = "XtT5ih91I4xxA6Z67Desd6oBacO25451";
        OkHttpClient client = new OkHttpClient();
        String apiUrl = "https://ipqualityscore.com/api/json/url/" + apiKey + "/" + url;
        Request request = new Request.Builder()
                .url(apiUrl)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle network errors
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseBody = response.body().string();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonResponse = new JSONObject(responseBody);

                                boolean isMalicious = jsonResponse.optBoolean("is_malicious");

                                int riskScore = jsonResponse.optInt("risk_score");

                                if (isMalicious) {

                                    Toast.makeText(MainActivity.this, "Malicious URL detected!", Toast.LENGTH_SHORT).show();
                                } else {

                                    Toast.makeText(MainActivity.this, "URL is safe.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();

                            }
                        }
                    });
                }

                }
            });
        }

    }

