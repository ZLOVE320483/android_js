package com.github.jsbridge;

/**
 * Created by zlove on 2018/1/28.
 */

public interface CompletionHandler {
    void complete(String retValue);
    void complete();
    void setProgressData(String value);
}
