package com.github.js;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.RelativeLayout;

import com.github.js.jsinterface.JsApi;
import com.github.jsbridge.JsBridgeWebView;
import com.github.jsbridge.JsWebViewClient;

/**
 * Created by zlove on 2018/1/29.
 */

public class WebViewActivity extends AppCompatActivity {

    private static final String TAG = WebViewActivity.class.getSimpleName();

    private RelativeLayout mContainer;
    private JsBridgeWebView webView;
    private JsWebViewClient webViewClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_web);
        mContainer = (RelativeLayout) findViewById(R.id.container);
        initWebView();
        initData();
    }

    private void initWebView() {
        try {
            webView = new JsBridgeWebView(this);
        } catch (Exception e) {
            Log.e(TAG, "Create WebView Failed:" + e.getMessage());
        }

        if (webView != null) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mContainer.addView(webView, params);
            webView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    private void initData() {
        if (webView == null) {
            return;
        }
        WebSettings settings = webView.getSettings();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            settings.setLoadsImagesAutomatically(true);
        } else {
            settings.setLoadsImagesAutomatically(false);
        }
        webViewClient = new JsWebViewClient();
        removeUnSafeInterfaceForWebView();
        webView.setJavascriptInterface(new JsApi(this));
        webView.setWebViewClient(webViewClient);
        webView.loadUrl("file:///android_asset/js_android_interactor.html");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != webView) {
            webView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != webView) {
            webView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != webView) {
            if (mContainer != null) {
                mContainer.removeView(webView);
            }
            webView.setWebViewClient(null);
            webView.destroy();
            webView = null;
        }
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webViewClient.setBackPressed(true);
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private void removeUnSafeInterfaceForWebView() {
        try {
            webView.removeJavascriptInterface("searchBoxJavaBridge_");
            webView.removeJavascriptInterface("accessibilityTraversal");
            webView.removeJavascriptInterface("accessibility");
            webView.getSettings().setSavePassword(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
