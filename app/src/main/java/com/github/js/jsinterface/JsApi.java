package com.github.js.jsinterface;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.github.jsbridge.CompletionHandler;
import com.github.jsbridge.JSInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * Created by zlove on 2018/1/29.
 */

public class JsApi {
    private WeakReference<Context> mContextReference;

    public JsApi(Context context) {
        mContextReference = new WeakReference<>(context);
    }

    @JSInterface
    public void closePage(JSONObject jsonObject) throws JSONException {
        final Context context = mContextReference.get();
        if (context != null && context instanceof Activity) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((Activity) context).finish();
                }
            });
        }
    }

    @JSInterface
    public void testAsync(JSONObject jsonObject, CompletionHandler handler) throws JSONException {
        Toast.makeText(mContextReference.get(), jsonObject.toString(), Toast.LENGTH_SHORT).show();
        JSONObject object = new JSONObject();
        object.put("result", "success");
        handler.complete(object.toString());
    }
}
