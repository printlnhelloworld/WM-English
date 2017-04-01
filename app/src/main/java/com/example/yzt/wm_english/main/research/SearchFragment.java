package com.example.yzt.wm_english.main.research;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yzt.wm_english.R;
import com.example.yzt.wm_english.Units.HttpUtil;
import com.example.yzt.wm_english.main.listening.shortDialog.AuditionAdapter;
import com.example.yzt.wm_english.main.listening.shortDialog.Auditions;
import com.example.yzt.wm_english.main.listening.shortDialog.ShortDialogItem;
import com.example.yzt.wm_english.main.listening.video.Video;
import com.example.yzt.wm_english.main.talking.Dialouge;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by YZT on 2017/2/7.
 */

public class SearchFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "SearchFragment";
    List<Auditions.Audition> auditionList = new ArrayList<>();
    List<Auditions.Audition> mauditionList = new ArrayList<>();
    SearchView searchView;
    TextView label1;
    TextView label2;
    TextView label3;
    TextView searchFlag;
    LinearLayout label;
    AuditionAdapter adapter;
    RecyclerView recyclerView;
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_layout, container, false);
        init(view);
        new DownLoadTask().execute("http://lincloud.me:8080/app/search/index");
        return view;
    }

    public void init(View view) {
        searchView = (SearchView) view.findViewById(R.id.search);
        searchView.setIconifiedByDefault(false);
        label = (LinearLayout) view.findViewById(R.id.label);
        label1 = (TextView) view.findViewById(R.id.label1);
        label2 = (TextView) view.findViewById(R.id.label2);
        label3 = (TextView) view.findViewById(R.id.label3);
        label1.setOnClickListener(SearchFragment.this);
        label2.setOnClickListener(SearchFragment.this);
        label3.setOnClickListener(SearchFragment.this);
        searchFlag = (TextView) view.findViewById(R.id.search_flag);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)){
//                    mListView.setFilterText(newText);
                    label.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    new SearchDownTask().execute("http://lincloud.me:8080/app/search/show?q="+newText);
                }else{
//                    mListView.clearTextFilter();
                    mauditionList.clear();
                    adapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.GONE);
                    label.setVisibility(View.VISIBLE);


                }
                return false;
            }
        });
    }

    public void initData() {
        label1.setText(auditionList.get(0).getTitle());
        label2.setText(auditionList.get(1).getTitle());
        label3.setText(auditionList.get(2).getTitle());
        label.setVisibility(View.VISIBLE);
        recyclerView = (RecyclerView) view.findViewById(R.id.audition_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.label1:
                if (auditionList.get(0).type.equals("small")) {
                    ShortDialogItem.actionStart(v.getContext(), auditionList.get(0).getResUrl());
                } else if (auditionList.get(0).type.equals("dialouge")){
                    Dialouge.actionStart(v.getContext(), auditionList.get(0).getResUrl());
                } else if (auditionList.get(0).type.equals("movie")) {
                    Video.actionStart(v.getContext(), auditionList.get(0).getResUrl());
                }
                break;
            case R.id.label2:
                if (auditionList.get(1).type.equals("small")) {
                    ShortDialogItem.actionStart(v.getContext(), auditionList.get(1).getResUrl());
                } else if (auditionList.get(1).type.equals("dialouge")){
                    Dialouge.actionStart(v.getContext(), auditionList.get(1).getResUrl());
                }else if (auditionList.get(1).type.equals("movie")) {
                    Video.actionStart(v.getContext(), auditionList.get(1).getResUrl());
                }
                break;
            case R.id.label3:
                if (auditionList.get(2).type.equals("small")) {
                    ShortDialogItem.actionStart(v.getContext(), auditionList.get(2).getResUrl());
                } else if (auditionList.get(2).type.equals("dialouge")){
                    Dialouge.actionStart(v.getContext(), auditionList.get(2).getResUrl());
                }else if (auditionList.get(2).type.equals("movie")) {
                    Video.actionStart(v.getContext(), auditionList.get(2).getResUrl());
                }
                break;
            default:
                break;
        }
    }

    class DownLoadTask extends AsyncTask<String, Integer, Integer> {
        ProgressDialog dialog = new ProgressDialog(getContext());
        @Override
        protected void onPreExecute() {
            dialog.show();
            Log.d(TAG, "onPreExecute: ");
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
                        Log.d(TAG, jsonData);
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
                    initData();
                    dialog.dismiss();
                    break;
                default:
            }
        }

        @Override
        protected void onPostExecute(Integer integer) {

        }
    }
    class SearchDownTask extends AsyncTask<String, Integer, Integer> {
//        ProgressDialog dialog = new ProgressDialog(getContext());
//        @Override
//        protected void onPreExecute() {
//            dialog.show();
//            dialog.setMessage("Loading...");
//        }

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
                        Log.d(TAG, jsonData);
                        Gson gson = new Gson();
                        Auditions res = gson.fromJson(jsonData, Auditions.class);
                        mauditionList.clear();
                        mauditionList.addAll(res.audition);
//                        Message message = new Message();
//                        message.what = UPDATE_TEXT1;
//                        handler.sendMessage(message);
                        if (res.total == 0) {
                            publishProgress(2);
                        } else {
                            publishProgress(3);
                        }
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
                    adapter = new AuditionAdapter(mauditionList);
                    recyclerView.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();
                    break;
                case 2:
                    searchFlag.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    searchFlag.setVisibility(View.GONE);
                default:
                    break;
            }
        }

        @Override
        protected void onPostExecute(Integer integer) {

        }
    }
}
