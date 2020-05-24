package com.example.webbrowserbedon;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText txtURL;
    private Button bntGo;
    private WebView wbView;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtURL = findViewById(R.id.etxtURL);
        bntGo = findViewById(R.id.btnGo);
        wbView = findViewById(R.id.wbView);

        bntGo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        url = txtURL.getText().toString();

        //Visualizar el WebView
        wbView.setWebViewClient(new WebViewClient());

        //Leer y abrir Javascript
        //wbView.getSettings().setJavaScriptEnabled(true);

        //Ajustar en pantalla completa
        wbView.getSettings().getLoadWithOverviewMode();
        wbView.getSettings().setUseWideViewPort(true);

        //Cargar URL
        wbView.loadUrl("http:www."+url);
    }


}