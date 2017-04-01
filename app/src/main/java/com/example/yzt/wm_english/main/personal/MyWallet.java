package com.example.yzt.wm_english.main.personal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yzt.wm_english.R;
import com.example.yzt.wm_english.Units.HttpUtil;
import com.example.yzt.wm_english.Units.ToastUtils;
import com.example.yzt.wm_english.main.listening.shortDialog.ShortDialog;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class MyWallet extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MyWallet";
    private Button recharge;
    private LinearLayout purchased;
    private LinearLayout credit;
    private LinearLayout vip;
    private TextView points;
    private Point point;
    public static void actionStart(Context context, String resUrl) {
        Intent intent = new Intent(context, MyWallet.class);
        intent.putExtra("resUrl", resUrl);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mywallet_layout);
        initToolbar();
        init();
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        int id = pref.getInt("id", 0);
        new DownLoadTask().execute("http://lincloud.me:8080/app/wallet?userId="+id);
    }
    public void init() {
        recharge = (Button) findViewById(R.id.recharge);
        recharge.setOnClickListener(MyWallet.this);
        purchased = (LinearLayout) findViewById(R.id.purchased);
        purchased.setOnClickListener(MyWallet.this);
        credit = (LinearLayout) findViewById(R.id.credit);
        credit.setOnClickListener(MyWallet.this);
        vip = (LinearLayout) findViewById(R.id.Vip);
        vip.setOnClickListener(MyWallet.this);
        points = (TextView) findViewById(R.id.my_points);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recharge:
                ToastUtils.showToast(v.getContext(), "功能暂未开放");
                break;
            case R.id.purchased:
                ShortDialog.actionStart(v.getContext(), "http://lincloud.me:8080/app/mycourses", "已购买课程");
                break;
            case R.id.credit:
                ToastUtils.showToast(v.getContext(), "功能暂未开放");
                break;
            case R.id.Vip:
                ToastUtils.showToast(v.getContext(), "功能暂未开放");
        }
    }

    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("我的钱包");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.home_icon);
        }
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }
    class DownLoadTask extends AsyncTask<String, Integer, Integer> {
//        ProgressDialog dialog = new ProgressDialog(MyWallet.this);
        @Override
        protected void onPreExecute() {
//            dialog.show();
//            dialog.setMessage("Loading...");
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
                        point = gson.fromJson(jsonData, Point.class);
                        Log.d(TAG, point.points+"");
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
                    points.setText(point.points+"");
//                    dialog.dismiss();
                    break;
                default:
            }
        }

    }
}
