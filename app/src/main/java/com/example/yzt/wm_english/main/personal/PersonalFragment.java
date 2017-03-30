package com.example.yzt.wm_english.main.personal;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.yzt.wm_english.R;
import com.example.yzt.wm_english.Units.HttpUtil;
import com.example.yzt.wm_english.main.listening.shortDialog.ShortDialog;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by YZT on 2017/2/7.
 */

public class PersonalFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "PersonalFragment";
    LinearLayout myInfo;
    LinearLayout myWallet;
    LinearLayout myStore;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.personal_layout, container, false);
        init(view);
        return view;
    }

    public void init(View view) {
        myInfo = (LinearLayout) view.findViewById(R.id.my_info);
        myInfo.setOnClickListener(PersonalFragment.this);
        myWallet = (LinearLayout) view.findViewById(R.id.my_wallet);
        myWallet.setOnClickListener(PersonalFragment.this);
        myStore = (LinearLayout) view.findViewById(R.id.my_store);
        myStore.setOnClickListener(PersonalFragment.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_info:
                MyInformation.actionStart(v.getContext(),"");
                break;
            case R.id.my_wallet:
                MyWallet.actionStart(v.getContext(),"http://lincloud.me:8080/app/wallet");
                break;
            case R.id.my_store:
                ShortDialog.actionStart(v.getContext(),"http://lincloud.me:8080/app/mycollection","我的收藏");
                break;
            default:
                break;
        }
    }

    class DownLoadTask extends AsyncTask<String, Integer, Integer> {
        @Override
        protected Integer doInBackground(String... params) {
            HttpUtil.get("http://lincloud.me:8080/app/audition/small", new okhttp3.Callback() {

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //成功获取返回值;
                    try {
//                        int id = (int)getArguments().get("id");
                        String jsonData = response.body().string();
                        Log.d(TAG, jsonData);
                        Gson gson = new Gson();
//                        Auditions res = gson.fromJson(jsonData, Auditions.class);
//                        auditionList = res.audition;
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
    }
}
