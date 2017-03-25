package com.example.yzt.wm_english.main.listening.shortDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.yzt.wm_english.R;
import com.example.yzt.wm_english.Units.HttpUtil;
import com.example.yzt.wm_english.Units.ToastUtils;
import com.example.yzt.wm_english.main.listening.Player;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class ShortDialogItem extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "ShortDialogItem";
    private Player player;

    private SeekBar skbProgress;
    private DialogRes dialogRes;

    private TextView question;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private List<String> answersList;
    private ArrayList<Answer> manswersList = new ArrayList<>();
    private Button checkOut;
    private Button nextQues;
    private AnswerAdapter adapter;
    public static void actionStart(Context context, String resUrl) {
        Intent intent = new Intent(context, ShortDialogItem.class);
        intent.putExtra("resUrl", resUrl);
//        intent.putExtra("news_content", newsContent);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening_item);
        initShortDialog();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.home_icon);
        }
        Intent intent = getIntent();
        String resUrl = intent.getStringExtra("resUrl");
        new DownLoadTask().execute(resUrl);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.check_answer:
                if (checkOut.getText().equals("查看答案")) {
                    checkOut.setText("隐藏答案");
                    for (int i = 0; i < manswersList.size(); i++) {
                        manswersList.get(i).flag = true;
                    }
                } else {
                    checkOut.setText("查看答案");
                    for (int i = 0; i < manswersList.size(); i++) {
                        manswersList.get(i).flag = false;
                    }
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.next_ques:
                actionStart(ShortDialogItem.this,dialogRes.nextResUrl);
                ToastUtils.showToast(ShortDialogItem.this,"nextResUrl = " + dialogRes.nextResUrl);
                finish();
                break;
            default:
                break;
        }
    }

    public void initShortDialog() {
        question = (TextView) findViewById(R.id.ques_subject);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        skbProgress = (SeekBar) this.findViewById(R.id.skbProgress);
        skbProgress.setOnSeekBarChangeListener(new SeekBarChangeEvent());
        checkOut = (Button) findViewById(R.id.check_answer);
        checkOut.setOnClickListener(this);
        nextQues = (Button) findViewById(R.id.next_ques);
        nextQues.setOnClickListener(this);
        player = new Player(skbProgress);
    }
    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.horn:
                String url = dialogRes.auditionUrl;
                player.playUrl(url);
                break;
            case R.id.write:
                break;
            case android.R.id.home:
                finish();
            default:
                break;
        }
        return true;
    }

    class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {
        int progress;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            // 原本是(progress/seekBar.getMax())*player.mediaPlayer.getDuration()
            this.progress = progress * player.mediaPlayer.getDuration()
                    / seekBar.getMax();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // seekTo()的参数是相对与影片时间的数字，而不是与seekBar.getMax()相对的数字
            player.mediaPlayer.seekTo(progress);
        }
    }

    class DownLoadTask extends AsyncTask<String, Integer, Integer> {
        ProgressDialog dialog = new ProgressDialog(ShortDialogItem.this);
        @Override
        protected void onPreExecute() {
            dialog.show();
            dialog.setMessage("Loading...");
        }

        @Override
        protected Integer doInBackground(String... params) {
            HttpUtil.get(params[0], new okhttp3.Callback() {

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //成功获取返回值;
                    try {
//                        int id = (int)getArguments().get("id");
                        String jsonData = response.body().string();
                        Log.d(TAG, jsonData);
                        Gson gson = new Gson();
                        dialogRes = gson.fromJson(jsonData, DialogRes.class);
                        answersList = dialogRes.answer;
                        for (int i = 0; i < answersList.size(); i++) {
                            manswersList.add(new Answer(answersList.get(i)));
                        }
                        Log.d(TAG, answersList.get(0));
//                        Message message = new Message();
//                        message.what = UPDATE_TEXT1;
//                        handler.sendMessage(message);
                        publishProgress(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }
            });
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            switch (values[0]) {
                case 1:
                    Log.d(TAG, "下载后");
                    question.setText(dialogRes.getQuestion());

                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.answer_view);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(ShortDialogItem.this);
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(layoutManager);
                    adapter = new AnswerAdapter(manswersList);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    Log.d(TAG, "next");
                    player.playUrl(dialogRes.auditionUrl);
                    break;
                default:
            }
        }

        @Override
        protected void onPostExecute(Integer integer) {
            dialog.dismiss();
        }
    }
    //重写回车事件
    @Override
    public void onBackPressed() {
        //TODO something
        super.onBackPressed();
        player.stop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.stop();
    }
    //    class DownloadTask extends AsyncTask<String, Integer, Integer> {
//
//        public static final int TYPE_SUCCESS = 0;
//        public static final int TYPE_FAILED = 1;
//        public static final int TYPE_PAUSED = 2;
//        public static final int TYPE_CANCELED = 3;
//
//        private DownloadListener listener;
//
//        private boolean isCanceled = false;
//
//        private boolean isPaused = false;
//
//        private int lastProgress;
//
//        public DownloadTask(DownloadListener listener) {
//            this.listener = listener;
//        }
//        //任务开始前调用,进行界面的初始化操作,如显示进度对话框
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//        //存放在子线程中运行的代码,不可以进行UI操作,如需进行UI操作,可调用publishProgress()
//        @Override
//        protected Integer doInBackground(String ... params) {
//            InputStream is = null;
//            RandomAccessFile savedFile = null;
//            File file = null;
//            try {
//                long downloadedLength = 0;//记录已下载的文件长度
//                String downloadUrl = params[0];
//                String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
//                String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
//                file = new File(directory + fileName);
//                if (file.exists()) {
//                    downloadedLength = file.length();
//                }
//                long contentLength = getContentLength(downloadUrl);
//                if (contentLength == 0) {
//                    return TYPE_FAILED;
//                } else if (contentLength == downloadedLength) {
//                    //已下载字节和文件总字节相等,说明已下载完成
//                    return TYPE_SUCCESS;
//                }
//                OkHttpClient client = new OkHttpClient();
//                Request request = new Request.Builder()
//                        //断点下载,指定从那个字节开始下载
//                        .addHeader("RANGE", "bytes=" + downloadedLength + "-")
//                        .url(downloadUrl)
//                        .build();
//                Response response = client.newCall(request).execute();
//                if (response != null) {
//                    is = response.body().byteStream();
//                    savedFile = new RandomAccessFile(file, "rw");
//                    savedFile.seek(downloadedLength);//跳过已下载的字节
//                    byte[] b = new byte[1024];
//                    int total = 0;
//                    int len;
//                    while ((len = is.read(b)) != -1) {
//                        if (isCanceled) {
//                            return TYPE_CANCELED;
//                        } else if (isPaused) {
//                            return TYPE_PAUSED;
//                        } else {
//                            total += len;
//                            savedFile.write(b, 0, len);
//                            //计算已下载的百分比
//                            int progress = (int) ((total + downloadedLength) * 100 / contentLength);
//                            publishProgress(progress);
//                        }
//                    }
//                    response.body().close();
//                    return TYPE_SUCCESS;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                try {
//                    if (is != null) {
//                        is.close();
//                    }
//                    if (savedFile != null) {
//                        savedFile.close();
//                    }
//                    if (isCanceled && file != null) {
//                        file.delete();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            return TYPE_FAILED;
//        }
//        //进行UI操作,数据由publishProgress()传递
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//            int progress = values[0];
//            if (progress > lastProgress) {
//                listener.onProgress(progress);
//                lastProgress = progress;
//            }
//        }
//        //后台任务执行完毕时调用,参数由后台任务返回的数据,可更新UI,如关闭对话框,提醒任务执行结果等
//        @Override
//        protected void onPostExecute(Integer status) {
//            switch (status) {
//                case TYPE_SUCCESS:
//                    listener.onSuccess();
//                    break;
//                case TYPE_FAILED:
//                    listener.onFailed();
//                    break;
//                case TYPE_PAUSED:
//                    listener.onPaused();
//                    break;
//                case TYPE_CANCELED:
//                    listener.onCanceled();
//                    break;
//                default:
//                    break;
//            }
//        }
//
//        public void pauseDownload() {
//            isPaused = true;
//        }
//
//        public void cancelDownload() {
//            isCanceled = true;
//        }
//
//        public long getContentLength(String downloadUrl) throws IOException {
//            OkHttpClient client = new OkHttpClient();
//            Request request = new Request.Builder()
//                    .url(downloadUrl)
//                    .build();
//            Response response = client.newCall(request).execute();
//            if (response != null && response.isSuccessful()) {
//                long contentLength = response.body().contentLength();
//                response.close();
//                return contentLength;
//            }
//            return 0;
//        }
//    }
}
