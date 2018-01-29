# android_js
## 前言
    作为开发人员都知道，客户端的版本更新对于用户来说代价是很大的。为了满足客户端能够快速更新迭代的要求，许多app都内嵌入了H5，比如很多电商平台，淘宝、京东、
    聚划算等等。这类技术的关键就是在于Android客户与Web前端之间的交互。很多大型项目的接口为了防止Spammer的侵入，都是要求只能由客户端发起请求的。所以本项目
    就封装了一个module，实现客户端接收前端的调用，然后由客户端发起Http请求的功能。
>
    开始介绍项目之前，先来快速回顾一下Android客户端与Web前端之间交互的几种方式。
## 1. Android调用JS方法
#### 1.1 通过WebView的loadUrl()
        android客户端代码：
>
     private void initWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // 设置与Js交互的权限
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); // 设置允许JS弹窗

        webView.loadUrl("file:///android_asset/javascript.html");
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(SimpleWebViewActivity.this);
                b.setTitle("Alert");
                b.setMessage(message);
                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                b.setCancelable(false);
                b.create().show();
                return true;
            }
        });
    }
    
    private void setListener() {
        btnLoadUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.post(new Runnable() {
                    @Override
                    public void run() {
                        // 此处的callJS方法名与JS中的function方法名必须要一致
                        webView.loadUrl("javascript:callJS()");
                    }
                });
            }
        });
    }
        javascript前端代码：
 >
      <!DOCTYPE html>
        <html>
        <head>
            <meta charset="utf-8">
            <title>Carson_Ho</title>
            // JS代码
            <script>
                // Android需要调用的方法
               function callJS() {
                  alert("Android调用了JS的callJS方法");
               }

            </script>
        </head>
        </html>
>
    运行结果如图
![运行结果](https://github.com/ZLOVE320483/android_js/blob/master/img/device-2018-01-29-104644.png)
#### 1.2 通过WebView的evaluateJavascript()
        android客户端代码：
>
     btnEvaluateJavascript.setOnClickListener(new View.OnClickListener() {

            @TargetApi(19)
            @Override
            public void onClick(final View v) {
                webView.evaluateJavascript("javascript:callJS()", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        //此处为 js 返回的结果
                        Log.d(TAG, "value---" + value);
                    }
                });
            }
        });
>
    运行结果跟上图是一样的。
>
    两种交互方式的比较
>
| 调用方式 | 优点 | 缺点 | 使用场景 |
| :-: | :-: | :-: | :-: |
| 使用loadUrl() | 方便简洁 | 效率低；获取返回值麻烦 | 不需要使用返回值，对性能要求较低 |
| 使用evaluateJavascript() | 效率高 | 向下兼容性差（仅Android 4.4以上可用） | Android 4.4及以上 |
#### 1.3 使用建议
>
    // Android版本变量
    final int version = Build.VERSION.SDK_INT;
    // 因为该方法在 Android 4.4 版本才可使用，所以使用时需进行版本判断
    if (version < 18) {
        webView.loadUrl("javascript:callJS()");
    } else {
        webView.evaluateJavascript（"javascript:callJS()", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                //此处为 js 返回的结果
            }
        });
    }
