package com.github.jsbridge;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.http.SslError;
import android.support.v7.app.AlertDialog;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by zlove on 2018/1/30.
 */

public class JsWebViewClient extends WebViewClient {
    
    private boolean isBackPressed = false;
    private OnPageFinishedCallback mCallback;

    public interface OnPageFinishedCallback {
        void onPageFinished(String url);
        void onPageError(int errorCode, String url);
        void onGetTitle(String title);
    }

    public void setCallback(OnPageFinishedCallback callback) {
        this.mCallback = callback;
    }

    public void setBackPressed(boolean backPressed) {
        isBackPressed = backPressed;
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        saySslAlert(view == null ? null : view.getContext(), handler, error);
        dispatchPageFinishedEvent(null);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        dispatchGetTitleEvent(view.getTitle());
        dispatchPageFinishedEvent(url);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        if (mCallback != null) {
            mCallback.onPageError(errorCode,failingUrl);
        }
        dispatchPageFinishedEvent(failingUrl);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (isBackPressed) {
            return false;
        } else {
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    private void saySslAlert(Context context, final SslErrorHandler handler, SslError error) {
        if (context == null || (context instanceof Activity) && ((Activity) context).isFinishing()) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog alertDialog = builder.create();
        String message = context.getResources().getString(R.string.ssl_certificate_error);
        switch (error.getPrimaryError()) {
            case SslError.SSL_UNTRUSTED:
                message = context.getResources().getString(R.string.ssl_untrusted);
                break;
            case SslError.SSL_EXPIRED:
                message = context.getResources().getString(R.string.ssl_expired);
                break;
            case SslError.SSL_IDMISMATCH:
                message = context.getResources().getString(R.string.ssl_mismatch);
                break;
            case SslError.SSL_NOTYETVALID:
                message = context.getResources().getString(R.string.ssl_valid);
                break;
        }

        alertDialog.setTitle(context.getResources().getString(R.string.ssl_error_title));
        alertDialog.setMessage(message);
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (handler != null) {
                    handler.cancel();
                }
            }
        });
        try {
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dispatchPageFinishedEvent(String url) {
        if (mCallback != null) {
            mCallback.onPageFinished(url);
        }
    }

    private void dispatchGetTitleEvent(String title) {
        if (mCallback != null) {
            mCallback.onGetTitle(title);
        }
    }

}
