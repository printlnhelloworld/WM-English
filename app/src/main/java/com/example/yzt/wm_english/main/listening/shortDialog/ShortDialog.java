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
import android.view.MenuItem;

import com.example.yzt.wm_english.R;
import com.example.yzt.wm_english.Units.HttpUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class ShortDialog extends AppCompatActivity {
    public static void actionStart(Context context, String resUrl, String title) {
        Intent intent = new Intent(context, ShortDialog.class);
        intent.putExtra("resUrl", resUrl);
        intent.putExtra("title", title);
//        intent.putExtra("news_content", newsContent);
        context.startActivity(intent);
    }
    private static final String TAG = "ShortDialog";
    private List<Auditions.Audition> auditionList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_dialog);
        Intent intent = getIntent();
        String resUrl = intent.getStringExtra("resUrl");
        String title = intent.getStringExtra("title");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.home_icon);
        }
        Log.d(TAG, "onCreate: ");

        new DownLoadTask().execute(resUrl);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                break;
        }
        return true;
    }
    class DownLoadTask extends AsyncTask<String, Integer, Integer> {
        ProgressDialog dialog = new ProgressDialog(ShortDialog.this);
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
                    Log.d(TAG, "onResponse: ");
                    //成功获取返回值;
                    try {
//                        int id = (int)getArguments().get("id");
                        String jsonData = response.body().string();
                        Gson gson = new Gson();
                        Auditions res = gson.fromJson(jsonData, Auditions.class);
                        auditionList = res.audition;
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
                    Log.d(TAG, "onFailure: ");
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
                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.audition_view);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(ShortDialog.this);
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(layoutManager);
                    AuditionAdapter adapter = new AuditionAdapter(auditionList);
                    recyclerView.setAdapter(adapter);
                    break;
                default:
            }
        }

        @Override
        protected void onPostExecute(Integer integer) {
            dialog.dismiss();
        }
    }
}
