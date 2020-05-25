package com.example.webbrowserbedon;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

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

        //Visualizar el WebView
        wbView.setWebViewClient(new Browser_Home());
        wbView.setWebChromeClient(new ChromeClient());
        WebSettings webSettings = wbView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);

        //Ajustar en pantalla completa
        wbView.getSettings().getLoadWithOverviewMode();
        wbView.getSettings().setUseWideViewPort(true);

        loadWebSite();

    }

    private void loadWebSite() {
        wbView.loadUrl("http://www.google.com"); //Cargar URL Predeterminada
    }

    @Override
    public void onClick(View v) {
        url = txtURL.getText().toString();
        wbView.loadUrl("http://"+url);
    }

    private class Browser_Home extends WebViewClient {
        Browser_Home(){}

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }

    private class ChromeClient extends WebChromeClient {
        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        protected FrameLayout mFullscreenContainer;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        ChromeClient(){}

        public Bitmap getDefaultVideoPoster() {
            if (mCustomView ==null){
                return null;
            }
            return BitmapFactory.decodeResource(getApplicationContext()
                    .getResources(), 2130837573);
        }

        public void onHideCostumView(){
            ((FrameLayout)getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, WebChromeClient.
                CustomViewCallback paramCustomViewCallback){
            if (this.mCustomView != null){
                onHideCostumView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout)getWindow().getDecorView()).
                    addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            getWindow().getDecorView().
                    setSystemUiVisibility(3846 | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }
}