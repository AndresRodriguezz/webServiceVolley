package com.example.practicawebservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class WebViewTwitterActivity extends AppCompatActivity {
    private WebView webView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_twitter);


        webView = (WebView)findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://twitter.com/anditititop");


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent miIntentent = new Intent(this,MainActivity.class);
    }
}
