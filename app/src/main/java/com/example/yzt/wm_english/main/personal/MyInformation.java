package com.example.yzt.wm_english.main.personal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.yzt.wm_english.R;

public class MyInformation extends AppCompatActivity {
    public static void actionStart(Context context,String resUrl) {
        Intent intent = new Intent(context, MyInformation.class);
        intent.putExtra("resUrl", resUrl);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myinformation_layout);
    }
}
