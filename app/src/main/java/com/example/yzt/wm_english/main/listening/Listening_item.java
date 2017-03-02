package com.example.yzt.wm_english.main.listening;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import com.example.yzt.wm_english.DownloadListener;
import com.example.yzt.wm_english.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Listening_item extends AppCompatActivity {
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, Listening_item.class);
//        intent.putExtra("news_title", newsTitle);
//        intent.putExtra("news_content", newsContent);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening_item);
    }

    class DownloadTask extends AsyncTask<String, Integer, Integer> {

        public static final int TYPE_SUCCESS = 0;
        public static final int TYPE_FAILED = 1;
        public static final int TYPE_PAUSED = 2;
        public static final int TYPE_CANCELED = 3;

        private DownloadListener listener;

        private boolean isCanceled = false;

        private boolean isPaused = false;

        private int lastProgress;

        public DownloadTask(DownloadListener listener) {
            this.listener = listener;
        }
        //任务开始前调用,进行界面的初始化操作,如显示进度对话框
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        //存放在子线程中运行的代码,不可以进行UI操作,如需进行UI操作,可调用publishProgress()
        @Override
        protected Integer doInBackground(String ... params) {
            InputStream is = null;
            RandomAccessFile savedFile = null;
            File file = null;
            try {
                long downloadedLength = 0;//记录已下载的文件长度
                String downloadUrl = params[0];
                String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
                String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                file = new File(directory + fileName);
                if (file.exists()) {
                    downloadedLength = file.length();
                }
                long contentLength = getContentLength(downloadUrl);
                if (contentLength == 0) {
                    return TYPE_FAILED;
                } else if (contentLength == downloadedLength) {
                    //已下载字节和文件总字节相等,说明已下载完成
                    return TYPE_SUCCESS;
                }
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        //断点下载,指定从那个字节开始下载
                        .addHeader("RANGE", "bytes=" + downloadedLength + "-")
                        .url(downloadUrl)
                        .build();
                Response response = client.newCall(request).execute();
                if (response != null) {
                    is = response.body().byteStream();
                    savedFile = new RandomAccessFile(file, "rw");
                    savedFile.seek(downloadedLength);//跳过已下载的字节
                    byte[] b = new byte[1024];
                    int total = 0;
                    int len;
                    while ((len = is.read(b)) != -1) {
                        if (isCanceled) {
                            return TYPE_CANCELED;
                        } else if (isPaused) {
                            return TYPE_PAUSED;
                        } else {
                            total += len;
                            savedFile.write(b, 0, len);
                            //计算已下载的百分比
                            int progress = (int) ((total + downloadedLength) * 100 / contentLength);
                            publishProgress(progress);
                        }
                    }
                    response.body().close();
                    return TYPE_SUCCESS;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                    if (savedFile != null) {
                        savedFile.close();
                    }
                    if (isCanceled && file != null) {
                        file.delete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return TYPE_FAILED;
        }
        //进行UI操作,数据由publishProgress()传递
        @Override
        protected void onProgressUpdate(Integer... values) {
            int progress = values[0];
            if (progress > lastProgress) {
                listener.onProgress(progress);
                lastProgress = progress;
            }
        }
        //后台任务执行完毕时调用,参数由后台任务返回的数据,可更新UI,如关闭对话框,提醒任务执行结果等
        @Override
        protected void onPostExecute(Integer status) {
            switch (status) {
                case TYPE_SUCCESS:
                    listener.onSuccess();
                    break;
                case TYPE_FAILED:
                    listener.onFailed();
                    break;
                case TYPE_PAUSED:
                    listener.onPaused();
                    break;
                case TYPE_CANCELED:
                    listener.onCanceled();
                    break;
                default:
                    break;
            }
        }

        public void pauseDownload() {
            isPaused = true;
        }

        public void cancelDownload() {
            isCanceled = true;
        }

        public long getContentLength(String downloadUrl) throws IOException {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(downloadUrl)
                    .build();
            Response response = client.newCall(request).execute();
            if (response != null && response.isSuccessful()) {
                long contentLength = response.body().contentLength();
                response.close();
                return contentLength;
            }
            return 0;
        }
    }
}
