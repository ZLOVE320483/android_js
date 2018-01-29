package com.github.js;

import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * Created by zlove on 2018/1/29.
 */

public class AndroidToJs {

    // 定义JS需要调用的方法
    // 被JS调用的方法必须加入@JavascriptInterface注解
    @JavascriptInterface
    public void hello(String msg) {
        Toast.makeText(App.getInstance(), "JS调用了Android的hello方法", Toast.LENGTH_SHORT).show();
    }
}
