package com.example.yzt.wm_english.main.listening;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.example.yzt.wm_english.HttpUtil;
import com.example.yzt.wm_english.R;
import com.example.yzt.wm_english.ToastUtils;
import com.example.yzt.wm_english.main.Mainres;
import com.example.yzt.wm_english.main.ResourceAdapter;
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
    private ImageView shortDialogue1;
    private ImageView shortDialogue2;
    private ImageView shortDialogue3;
    private ImageView shortDialogue4;
    private List<Mainres.Image> bannerPictureURLs = new ArrayList<>();
    private List<Mainres.Resource> resourceList = new ArrayList<>();

    private View view;
    public static final int UPDATE_TEXT1 = 1;
    public static final int UPDATE_TEXT2 = 2;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TEXT1:
                    Log.d(TAG, "下载后");
                    initPhotoCarousel(view);
                    initShortDialogue(view);
                    RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    recyclerView.setLayoutManager(layoutManager);
                    ResourceAdapter adapter = new ResourceAdapter(resourceList);
                    recyclerView.setAdapter(adapter);
                    break;
                case UPDATE_TEXT2:
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.listening_layout, container, false);
        HttpUtil.get("http://lincloud.me:8080/app/index", new okhttp3.Callback() {

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //成功获取返回值;
                try {
                    int id = (int)getArguments().get("id");
                    String jsonData = response.body().string();
                    Log.d(TAG, jsonData);
                    Gson gson = new Gson();
                    Mainres res = gson.fromJson(jsonData, Mainres.class);
                    bannerPictureURLs = res.images;
                    resourceList = res.resources;
                    Log.d(TAG, "httpSuccess");
                    Message message = new Message();
                    message.what = UPDATE_TEXT1;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
        Log.d(TAG, "加载前");
//        ProgressDialog progressDialog = new ProgressDialog(activity);
//        progressDialog.setMessage("Loading...");
//        progressDialog.setCancelable(false);


        //图片轮播加载
        //短对话加载

        return view;
    }


    private void initShortDialogue(View view) {
        shortDialogue1 = (ImageView) view.findViewById(R.id.short_dialog1);
        shortDialogue2 = (ImageView) view.findViewById(R.id.short_dialog2);
        shortDialogue3 = (ImageView) view.findViewById(R.id.short_dialog3);
        shortDialogue4 = (ImageView) view.findViewById(R.id.short_dialog4);

        shortDialogue1.setImageResource(R.drawable.img1);
        shortDialogue2.setImageResource(R.drawable.img2);
        shortDialogue3.setImageResource(R.drawable.img3);
        shortDialogue4.setImageResource(R.drawable.img4);
    }
    private void initPhotoCarousel(View view) {

        //图片轮播
        mRollViewPager = (RollPagerView) view.findViewById(R.id.roll_view_pager);

        //设置切换时间
        mRollViewPager.setPlayDelay(3000);
        //设置透明度
        mRollViewPager.setAnimationDurtion(500);
        //设置适配器

        mRollViewPager.setAdapter(new TestLoopAdapter(mRollViewPager));

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
        public View getView(ViewGroup container, int position) {
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
            Log.d(TAG, "getView: ");
//            view.setImageResource(imgs[position]);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    ToastUtils.showToast(getContext(),  "点击了第" + picNo + "张图片");

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
