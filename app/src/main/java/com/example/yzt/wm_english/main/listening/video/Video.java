package com.example.yzt.wm_english.main.listening.video;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.example.yzt.wm_english.R;
import com.example.yzt.wm_english.Units.HttpUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import okhttp3.Call;
import okhttp3.Response;

public class Video extends AppCompatActivity {
    JCVideoPlayerStandard jcVideoPlayerStandard;
    LinearLayoutManager layoutManager;
    RecyclerView recyclerView;

    private static final String TAG = "Video";
    private VideoRes res;
    public static void actionStart(Context context,String resUrl) {
        Intent intent = new Intent(context, Video.class);
        intent.putExtra("resUrl", resUrl);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        init();
        Intent intent = getIntent();
        String resUrl = intent.getStringExtra("resUrl");
        new DownLoadTask().execute(resUrl);
    }

    public void init() {
        jcVideoPlayerStandard = (JCVideoPlayerStandard) findViewById(R.id.videoPlayer);
        recyclerView = (RecyclerView) findViewById(R.id.comment);
        layoutManager = new LinearLayoutManager(Video.this);
    }
    class DownLoadTask extends AsyncTask<String, Integer, Integer> {

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
                        res = gson.fromJson(jsonData, VideoRes.class);
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
                    jcVideoPlayerStandard.setUp(res.resUrl, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, res.title);
                    Glide.with(Video.this).load(res.cover).into(jcVideoPlayerStandard.thumbImageView);
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(layoutManager);
                    VideoRes videoRes = new VideoRes();
                    videoRes.comments = new ArrayList<>();
                    CommentAdapter adapter = new CommentAdapter(videoRes.comments);
                    recyclerView.setAdapter(adapter);
            }
            super.onProgressUpdate(values);
        }
    }
    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }
}
