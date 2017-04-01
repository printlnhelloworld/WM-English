package com.example.yzt.wm_english.main;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yzt.wm_english.R;
import com.example.yzt.wm_english.main.listening.ListeningFragment;
import com.example.yzt.wm_english.main.personal.PersonalFragment;
import com.example.yzt.wm_english.main.research.SearchFragment;
import com.example.yzt.wm_english.main.talking.TalkingFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    //定义四个Fragment对象
    private ListeningFragment fg1;
    private TalkingFragment fg2;
    private SearchFragment fg3;
    private PersonalFragment fg4;
    // 帧布局对象，用来存放Fragment对象
    private FrameLayout frameLayout;
    // 定义每个选项中的相关控件
    private RelativeLayout firstLayout;
    private RelativeLayout secondLayout;
    private RelativeLayout thirdLayout;
    private RelativeLayout fourthLayout;
    private ImageView firstImage;
    private ImageView secondImage;
    private ImageView thirdImage;
    private ImageView fourthImage;
    private TextView firstText;
    private TextView secondText;
    private TextView thirdText;
    private TextView fourthText;
    // 定义几个颜色
    private int white = 0xFFFFFFFF;
    private int gray = 0xFF7597B3;
    private int dark = 0xff000000;
    // 定义FragmentManager对象管理器
    private FragmentManager fragmentManager = getSupportFragmentManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        initView();
        setChoiceItem(0);
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
//            progressDialog.hide();
        }
    }
    private void initView() {
// 初始化底部导航栏的控件
        firstImage = (ImageView) findViewById(R.id.first_image);
        secondImage = (ImageView) findViewById(R.id.second_image);
        thirdImage = (ImageView) findViewById(R.id.third_image);
        fourthImage = (ImageView) findViewById(R.id.fourth_image);
        firstText = (TextView) findViewById(R.id.first_text);
        secondText = (TextView) findViewById(R.id.second_text);
        thirdText = (TextView) findViewById(R.id.third_text);
        fourthText = (TextView) findViewById(R.id.fourth_text);
        firstLayout = (RelativeLayout) findViewById(R.id.first_layout);
        secondLayout = (RelativeLayout) findViewById(R.id.second_layout);
        thirdLayout = (RelativeLayout) findViewById(R.id.third_layout);
        fourthLayout = (RelativeLayout) findViewById(R.id.fourth_layout);
        firstLayout.setOnClickListener(MainActivity.this);
        secondLayout.setOnClickListener(MainActivity.this);
        thirdLayout.setOnClickListener(MainActivity.this);
        fourthLayout.setOnClickListener(MainActivity.this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.first_layout:
                setChoiceItem(0);
                break;
            case R.id.second_layout:
                setChoiceItem(1);
                break;
            case R.id.third_layout:
                setChoiceItem(2);
                break;
            case R.id.fourth_layout:
                setChoiceItem(3);
                break;
            default:
                break;
        }
    }
    /**
     * 设置点击选项卡的事件处理
     *
     * @param index 选项卡的标号：0, 1, 2, 3
     */
    private void setChoiceItem(int index) {
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        int id = pref.getInt("id", 0);
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        clearChoice(); // 清空, 重置选项, 隐藏所有Fragment
        hideFragments(fragmentTransaction);
        switch (index) {
            case 0:
// firstImage.setImageResource(R.drawable.XXXX); 需要的话自行修改
                firstText.setTextColor(Color.rgb(0, 227, 129));
                firstImage.setImageResource(R.drawable.listening2_icon);
// 如果fg1为空，则创建一个并添加到界面上
                if (fg1 == null) {
                    fg1 = new ListeningFragment();
                    fg1.setArguments(bundle);
                    fragmentTransaction.add(R.id.content, fg1);
                } else {
// 如果不为空，则直接将它显示出来
                    fragmentTransaction.show(fg1);
                }
                break;
            case 1:
// secondImage.setImageResource(R.drawable.XXXX);
                secondText.setTextColor(Color.rgb(0, 227, 129));
                secondImage.setImageResource(R.drawable.talking2_icon);
                if (fg2 == null) {
                    fg2 = new TalkingFragment();
                    fg2.setArguments(bundle);
                    fragmentTransaction.add(R.id.content, fg2);
                } else {
                    fragmentTransaction.show(fg2);
                }
                break;
            case 2:
// thirdImage.setImageResource(R.drawable.XXXX);
                thirdText.setTextColor(Color.rgb(0, 227, 129));
                thirdImage.setImageResource(R.drawable.search2_icon);
                if (fg3 == null) {
                    fg3 = new SearchFragment();
                    fg3.setArguments(bundle);
                    fragmentTransaction.add(R.id.content, fg3);
                } else {
                    fragmentTransaction.show(fg3);
                }
                break;
            case 3:
// fourthImage.setImageResource(R.drawable.XXXX);
                fourthText.setTextColor(Color.rgb(0, 227, 129));
                fourthImage.setImageResource(R.drawable.my2_icon);
                if (fg4 == null) {
                    fg4 = new PersonalFragment();
                    fg4.setArguments(bundle);
                    fragmentTransaction.add(R.id.content, fg4);
                } else {
                    fragmentTransaction.show(fg4);
                }
                break;
        }
        fragmentTransaction.commit(); // 提交
    }
    /**
     * 当选中其中一个选项卡时，其他选项卡重置为默认
     */
    private void clearChoice() {
// firstImage.setImageResource(R.drawable.XXX);
        firstText.setTextColor(Color.rgb(132, 132, 132));
        firstImage.setImageResource(R.drawable.listening_icon);
// secondImage.setImageResource(R.drawable.XXX);
        secondText.setTextColor(Color.rgb(132, 132, 132));
        secondImage.setImageResource(R.drawable.talking_icon);
// thirdImage.setImageResource(R.drawable.XXX);
        thirdText.setTextColor(Color.rgb(132, 132, 132));
        thirdImage.setImageResource(R.drawable.search_icon);
// fourthImage.setImageResource(R.drawable.XXX);
        fourthText.setTextColor(Color.rgb(132, 132, 132));
        fourthImage.setImageResource(R.drawable.my_icon);
    }
    /**
     * 隐藏Fragment
     *
     * @param fragmentTransaction
     */
    private void hideFragments(FragmentTransaction fragmentTransaction) {
        if (fg1 != null) {
            fragmentTransaction.hide(fg1);
        }
        if (fg2 != null) {
            fragmentTransaction.hide(fg2);
        }
        if (fg3 != null) {
            fragmentTransaction.hide(fg3);
        }
        if (fg4 != null) {
            fragmentTransaction.hide(fg4);
        }
    }


}
