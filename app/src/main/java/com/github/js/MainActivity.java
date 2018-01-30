package com.github.js;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.github.js.jsinterface.AndroidToJs;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();

        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        // 通过addJavascriptInterface()将Java对象映射到JS对象
        //参数1：Javascript对象名
        //参数2：Java对象名
        webView.addJavascriptInterface(new AndroidToJs(), "jsCallback"); // AndroidToJs类对象映射到js的jsCallback对象

        webView.setWebViewClient(webViewClient);
        webView.setWebChromeClient(webChromeClient);

        // 加载JS代码
        // 格式规定为:file:///android_asset/文件名.html
        webView.loadUrl("file:///android_asset/js_call_android.html");
    }

    private WebViewClient webViewClient = new WebViewClient() {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Uri uri = Uri.parse(url);
            if (("scheme").equals(uri.getScheme())) {
                Toast.makeText(MainActivity.this, url, Toast.LENGTH_SHORT).show();
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

    };

    private WebChromeClient webChromeClient = new WebChromeClient() {

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            Log.d(TAG, "onJsAlert---url---" + url);
            Log.d(TAG, "onJsAlert---message---" + message);
            return super.onJsAlert(view, url, message, result);
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            Log.d(TAG, "onJsConfirm---url---" + url);
            Log.d(TAG, "onJsConfirm---message---" + message);
            return super.onJsConfirm(view, url, message, result);
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            Log.d(TAG, "onJsPrompt---url---" + url);
            Log.d(TAG, "onJsPrompt---message---" + message);
            Uri uri = Uri.parse(message);
            if (("scheme").equals(uri.getScheme())) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                result.confirm("这里是Android返回给前端Js的结果");
                return true;
            }
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }
    };
}
