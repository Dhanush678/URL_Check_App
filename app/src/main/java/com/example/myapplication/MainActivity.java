package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import android.content.res.AssetManager;


public class MainActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button start = findViewById(R.id.start);
        progressBar = findViewById(R.id.progressBar);
        final EditText URL = findViewById(R.id.URL);

        start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                startWaitingProcess();

                String editTextData = URL.getText().toString();
                boolean stringFound = isStringInAssetFile(MainActivity.this, "text.txt",editTextData);
                if (stringFound) {
                    Toast.makeText(MainActivity.this, "Malicious URL detected!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),malicious.class));
                    finish();
                } else {
                    startWaitingProcess();
                    checkMaliciousURL(editTextData);

                }


            }
        });
    }

    public static boolean isStringInAssetFile(Context context, String fileName, String searchString) {
        try {
            InputStream inputStream = context.getAssets().open(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(searchString)) {
                    reader.close();
                    return true;
                }
            }

            reader.close();
            inputStreamReader.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
    private void startWaitingProcess() {
        progressBar.setVisibility(View.VISIBLE);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.INVISIBLE);
            }
        }, 13000);
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
                                    startActivity(new Intent(getApplicationContext(),malicious.class));

                                } else {

                                    Toast.makeText(MainActivity.this, "URL is safe.", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(),NON_malicious.class));
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

