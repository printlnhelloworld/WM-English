package com.example.yzt.wm_english.main.listening;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.yzt.wm_english.HttpUtil;
import com.example.yzt.wm_english.R;
import com.example.yzt.wm_english.login.Status;
import com.example.yzt.wm_english.main.MainActivity;
import com.google.gson.Gson;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import java.io.IOException;

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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.listening_layout, container, false);
        //图片轮播加载
        initPhotoCarousel(view);
        //短对话加载
        initShortDialogue(view);
        return view;
    }

    class DownloadTask extends AsyncTask<Void, Integer, Boolean> {
        //任务开始前调用,进行界面的初始化操作,如显示进度对话框
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        //存放在子线程中运行的代码,不可以进行UI操作,如需进行UI操作,可调用publishProgress()
        @Override
        protected Boolean doInBackground(Void... params) {
//            int t = 1;
//            publishProgress(t);
            return null;
        }
        //进行UI操作,数据由publishProgress()传递
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
        //后台任务执行完毕时调用,参数由后台任务返回的数据,可更新UI,如关闭对话框,提醒任务执行结果等
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
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
        HttpUtil.get("http://www.badu.com", new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //成功获取返回值
                Log.d(TAG, response.body().string());
                Gson gson = new Gson();
                Status resJson = gson.fromJson(response.body().string(), Status.class);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                //传送失败

                e.printStackTrace();
            }
        });
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

        final MainActivity activity = (MainActivity) getActivity();

        private int[] imgs = {
                R.drawable.img1,
                R.drawable.img2,
                R.drawable.img3,
                R.drawable.img4,
        };

        private int count = imgs.length; // banner上图片的数量

        public TestLoopAdapter(RollPagerView viewPager) {
            super(viewPager);
        }


        @Override
        public View getView(ViewGroup container, int position) {
            final int picNo = position + 1;
            ImageView view = new ImageView(container.getContext());
//            Picasso.with(activity).load(bannerPictureURLs.get(position)).into(view);  // 加载网络图片
            view.setImageResource(imgs[position]);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    activity.showToast( "点击了第" + picNo + "张图片");

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
