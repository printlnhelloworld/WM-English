package com.example.yzt.wm_english.main.personal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.yzt.wm_english.R;
import com.example.yzt.wm_english.Units.ToastUtils;
import com.example.yzt.wm_english.main.listening.shortDialog.ShortDialog;

public class MyWallet extends AppCompatActivity implements View.OnClickListener{
    private Button recharge;
    private LinearLayout purchased;
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
    }
    public void init() {
        recharge = (Button) findViewById(R.id.recharge);
        recharge.setOnClickListener(MyWallet.this);
        purchased = (LinearLayout) findViewById(R.id.purchased);
        purchased.setOnClickListener(MyWallet.this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recharge:
                ToastUtils.showToast(v.getContext(), "充值功能暂未开放");
                break;
            case R.id.purchased:
                ShortDialog.actionStart(v.getContext(), "http://lincloud.me:8080/app/mycourses", "已购买课程");
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
}
