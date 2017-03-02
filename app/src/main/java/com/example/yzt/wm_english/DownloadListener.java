package com.example.yzt.wm_english;

/**
 * Created by YZT on 2017/3/1.
 */

public interface DownloadListener {
    void onProgress(int progress);

    void onSuccess();

    void onFailed();

    void onPaused();

    void onCanceled();

}
