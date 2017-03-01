package com.example.yzt.wm_english.main.listening;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.yzt.wm_english.R;

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
}
