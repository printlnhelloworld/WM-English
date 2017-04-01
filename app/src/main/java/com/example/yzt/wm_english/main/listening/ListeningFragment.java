package com.example.yzt.wm_english.main.listening;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.yzt.wm_english.R;
import com.example.yzt.wm_english.Units.HttpUtil;
import com.example.yzt.wm_english.main.Mainres;
import com.example.yzt.wm_english.main.ResourceAdapter;
import com.example.yzt.wm_english.main.listening.shortDialog.ShortDialog;
import com.example.yzt.wm_english.main.listening.video.Video;
import com.google.gson.Gson;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by YZT on 2017/2/7.
 */

public class ListeningFragment extends Fragment {
    private static final String TAG = "ListeningFragment";
    private RollPagerView mRollViewPager;
    private ImageView shortDialogue;
    private ImageView longDialogue;
    private ImageView newsDialogue;
    private List<Mainres.Image> bannerPictureURLs = new ArrayList<>();
    private List<Mainres.Resource> resourceList = new ArrayList<>();
    Mainres res;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.listening_layout, container, false);
        initPhotoCarousel(view);
        new DownLoadTask().execute();

        return view;
    }

    class DownLoadTask extends AsyncTask<String, Integer, Integer> {
        ProgressDialog dialog = new ProgressDialog(getContext());
        @Override
        protected void onPreExecute() {
            Log.d(TAG, "onPreExecute: ");
            dialog.show();
            dialog.setMessage("Loading...");
        }

        @Override
        protected Integer doInBackground(String... params) {
            HttpUtil.get("http://lincloud.me:8080/app/index", new okhttp3.Callback() {

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //成功获取返回值;
                    try {
//                        int id = (int)getArguments().get("id");
                        String jsonData = response.body().string();
                        Log.d(TAG, jsonData);
                        Gson gson = new Gson();
                        res = gson.fromJson(jsonData, Mainres.class);
                        bannerPictureURLs = res.images;
                        resourceList = res.resources;
                        Log.d(TAG, "httpSuccess"+res.resources);
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
                    mRollViewPager.setAdapter(new TestLoopAdapter(mRollViewPager));
                    mRollViewPager.getViewPager().getAdapter().notifyDataSetChanged();// 更新banner图片
                    initShortDialogue(view);
                    RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(layoutManager);
                    ResourceAdapter adapter = new ResourceAdapter(resourceList);
                    recyclerView.setAdapter(adapter);
                    dialog.dismiss();
                    break;
                default:
            }
        }

        @Override
        protected void onPostExecute(Integer integer) {
        }
    }

    private void initShortDialogue(View view) {
        shortDialogue = (ImageView) view.findViewById(R.id.short_dialog);
        longDialogue = (ImageView) view.findViewById(R.id.long_dialog);
        newsDialogue = (ImageView) view.findViewById(R.id.news_dialog);

        Glide.with(ListeningFragment.this).load(res.images.get(0).imgUrl).into(shortDialogue);
        Glide.with(ListeningFragment.this).load(res.images.get(1).imgUrl).into(longDialogue);
        Glide.with(ListeningFragment.this).load(res.images.get(2).imgUrl).into(newsDialogue);

        shortDialogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShortDialog.actionStart(v.getContext(), "http://lincloud.me:8080/app/audition/small", "短对话");
            }
        });

        longDialogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShortDialog.actionStart(v.getContext(), "http://lincloud.me:8080/app/audition/", "长对话");
            }
        });

        newsDialogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShortDialog.actionStart(v.getContext(), "http://lincloud.me:8080/app/audition/", "新闻训练");
            }
        });
    }
    private void initPhotoCarousel(View view) {

        //图片轮播
        mRollViewPager = (RollPagerView) view.findViewById(R.id.roll_view_pager);

        //设置切换时间
        mRollViewPager.setPlayDelay(3000);
        //设置透明度
        mRollViewPager.setAnimationDurtion(500);
        //设置适配器
        //设置指示器（顺序依次）
        //自定义指示器图片
        //设置圆点指示器颜色
        //设置文字指示器
        //隐藏指示器
        //mRollViewPager.setHintView(new IconHintView(this, R.drawable.point_focus, R.drawable.point_normal));
        mRollViewPager.setHintView(new ColorPointHintView(getActivity(), Color.YELLOW, Color.WHITE));
        //mRollViewPager.setHintView(new TextHintView(this));
        //mRollViewPager.setHintView(null);

    }

    private class TestLoopAdapter extends LoopPagerAdapter {




        private int count = bannerPictureURLs.size(); // banner上图片的数量

        public TestLoopAdapter(RollPagerView viewPager) {
            super(viewPager);
        }


        @Override
        public View getView(ViewGroup container, final int position) {
            final int picNo = position + 1;
            ImageView view = new ImageView(container.getContext());
            Glide.with(ListeningFragment.this)
                    .load(bannerPictureURLs.get(position).imgUrl)
                    .into(view);
//            Picasso.with(container.getContext())
//                    .load(bannerPictureURLs.get(position).imgUrl)
//                    .placeholder(R.drawable.wrong_name)
//                    .error(R.drawable.wrong_name).
//                    into(view);  // 加载网络图片
//            view.setImageResource(imgs[position]);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Video.actionStart(v.getContext(), res.images.get(position).resUrl);

                }
            });
            return view;
        }


        @Override
        public int getRealCount() {
            return count;
        }
    }

}
