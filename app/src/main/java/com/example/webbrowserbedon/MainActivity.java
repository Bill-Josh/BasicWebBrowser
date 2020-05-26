package com.example.webbrowserbedon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;

import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText txtURL;
    private Button bntGo;
    private WebView wbView;
    private String url;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtURL = findViewById(R.id.etxtURL);
        bntGo = findViewById(R.id.btnGo);
        wbView = findViewById(R.id.wbView);
        progressBar = findViewById(R.id.progressbar);
        swipeRefreshLayout = findViewById(R.id.webViewReload);

        bntGo.setOnClickListener(this);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                wbView.reload();
            }
        });


        //Visualizar el WebView
        wbView.setWebViewClient(new Browser_Home(swipeRefreshLayout));
        wbView.setWebChromeClient(new ChromeClient());
        WebSettings webSettings = wbView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
        loadWebSite();

        //Ajustar en pantalla completa

        wbView.getSettings().getLoadWithOverviewMode();
        wbView.getSettings().setUseWideViewPort(true);

        //InstanceTEST
        if(savedInstanceState == null) {
            wbView.post(new Runnable() {
                @Override
                public void run() {
                    loadWebSite();
                }
            });
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        wbView.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        wbView.restoreState(savedInstanceState);
    }

    //Loads the Default website.
    private void loadWebSite() {
        ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            progressBar.setVisibility(View.VISIBLE);
            wbView.loadUrl("https://www.youtube.com"); //Cargar URL Predeterminada
        } else{
            wbView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        url = txtURL.getText().toString();
        wbView.loadUrl("http://"+url);
    }

    private class Browser_Home extends WebViewClient {
        Browser_Home(){}

        SwipeRefreshLayout swipeRefreshLayout;

        public Browser_Home(SwipeRefreshLayout swipeRefreshLayout) {
            this.swipeRefreshLayout = swipeRefreshLayout;
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error){
            super.onReceivedError(view, request, error);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progressBar.setVisibility(View.VISIBLE);
            super.onPageStarted(view, url, favicon);
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            setTitle(view.getTitle());
            progressBar.setVisibility(View.GONE);
            super.onPageFinished(view, url);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    //Impedir que el botón Atrás cierre la aplicación
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        wbView = findViewById(R.id.wbView);
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (wbView.canGoBack()) {
                        wbView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
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

        public void onHideCustomView(){
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
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout)getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.
                    LayoutParams(-1, -1));
            getWindow().getDecorView().setSystemUiVisibility(3846 | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }
}