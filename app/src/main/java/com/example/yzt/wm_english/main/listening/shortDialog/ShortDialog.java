package com.example.yzt.wm_english.main.listening.shortDialog;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.yzt.wm_english.R;
import com.example.yzt.wm_english.Units.HttpUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class ShortDialog extends AppCompatActivity {

    private static final String TAG = "ShortDialog";
    private List<Auditions.Audition> auditionList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_dialog);
        Log.d(TAG, "onCreate: ");
        new DownLoadTask().execute();
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
            HttpUtil.get("http://lincloud.me:8080/app/audition/small", new okhttp3.Callback() {

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d(TAG, "onResponse: ");
                    //成功获取返回值;
                    try {
//                        int id = (int)getArguments().get("id");
                        String jsonData = response.body().string();
                        Log.d(TAG, jsonData);
                        Gson gson = new Gson();
                        Auditions res = gson.fromJson(jsonData, Auditions.class);
                        auditionList = res.audition;
                        Log.d(TAG, "httpSuccess"+res.audition.get(0).getId());
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
