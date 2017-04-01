package com.example.yzt.wm_english.main.talking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yzt.wm_english.R;
import com.example.yzt.wm_english.Units.AudioRecoderUtils;
import com.example.yzt.wm_english.Units.HttpUtil;
import com.example.yzt.wm_english.Units.PopupWindowFactory;
import com.example.yzt.wm_english.Units.TimeUtils;
import com.example.yzt.wm_english.Units.ToastUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class Dialouge extends AppCompatActivity {

    private List<Msg> msgList = new ArrayList<>();

    private EditText inputText;

    private Button send;

    private RecyclerView msgRecyclerView;

    private MsgAdapter adapter;

    private MediaPlayer mediaPlayer = new MediaPlayer();

    private Button mButton;
    private ImageView mImageView;
    private TextView mTextView;
    private TextView mTextView2;
    private AudioRecoderUtils mAudioRecoderUtils;
    private DialougeItem dialougeItem;
    private List<String> messageList;
    private int num = 1;
    private float DownX;
    private float DownY;
    private float MoveX;
    private float MoveY;
    private boolean flag = false;
    private static final String TAG = "Dialouge";
    public static void actionStart(Context context, String resUrl) {
        Intent intent = new Intent(context, Dialouge.class);
        intent.putExtra("resUrl", resUrl);
//        intent.putExtra("news_content", newsContent);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialouge_layout);
        initToolbar();
        initRecord();
        initMsgs();

        inputText = (EditText) findViewById(R.id.input_text);
        send = (Button) findViewById(R.id.send);
        msgRecyclerView = (RecyclerView) findViewById(R.id.msg_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);

        send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                String content = inputText.getText().toString();
                if (!"".equals(content)) {
                    Msg msg = new Msg(content, Msg.TYPE_SENT);
                    msgList.add(msg);
                    adapter.notifyItemChanged(msgList.size() - 1);//有新消息时，刷新ListView中的显示
                    msgRecyclerView.scrollToPosition(msgList.size() - 1);//将ListView定位到最后一行
                    inputText.setText("");//清空输入框内容
                }
            }
        });
    }

    public void initRecord() {
        final LinearLayout rl = (LinearLayout) findViewById(R.id.activity_main);

        mButton = (Button) findViewById(R.id.button);

        //PopupWindow的布局文件
        final View view = View.inflate(this, R.layout.layout_microphone, null);

        final PopupWindowFactory mPop = new PopupWindowFactory(this,view);

        //PopupWindow布局文件里面的控件
        mImageView = (ImageView) view.findViewById(R.id.iv_recording_icon);
        mTextView = (TextView) view.findViewById(R.id.tv_recording_time);
        mTextView2 = (TextView) view.findViewById(R.id.tv_recording_send);
        mAudioRecoderUtils = new AudioRecoderUtils();

        //录音回调
        mAudioRecoderUtils.setOnAudioStatusUpdateListener(new AudioRecoderUtils.OnAudioStatusUpdateListener() {

            //录音中....db为声音分贝，time为录音时长
            @Override
            public void onUpdate(double db, long time) {
                mImageView.getDrawable().setLevel((int) (3000 + 6000 * db / 100));
                mTextView.setText(TimeUtils.long2String(time));
            }

            //录音结束，filePath为保存路径
            @Override
            public void onStop(String filePath) {

                Msg msg = new Msg(filePath, Msg.TYPE_SENT);
                msgList.add(msg);
                adapter.notifyItemChanged(msgList.size() - 1);//有新消息时，刷新ListView中的显示
                msgRecyclerView.scrollToPosition(msgList.size() - 1);//将ListView定位到最后一行

                inputText.setText("");//清空输入框内容
                            if (num < messageList.size()) {
                                Msg msg2 = new Msg(messageList.get(num), Msg.TYPE_RECEIVED);
                                msgList.add(msg2);
                                adapter.notifyItemChanged(msgList.size() - 1);//有新消息时，刷新ListView中的显示
                                msgRecyclerView.scrollToPosition(msgList.size() - 1);//将ListView定位到最后一行
                                num++;
            }
                mTextView.setText(TimeUtils.long2String(0));
                try {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                    }
                    mediaPlayer.reset();
//                    File file = new File(Environment.getExternalStorageDirectory(), "music,mp3");
                    mediaPlayer.setDataSource(filePath);
                    mediaPlayer.prepare();
                    mediaPlayer.start();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //Button的touch监听
        mButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()){

                    case MotionEvent.ACTION_DOWN:

                        mPop.showAtLocation(rl, Gravity.CENTER,0,0);

                        mButton.setText("松开保存");
                        mAudioRecoderUtils.startRecord();

                        DownX = Math.abs(event.getX());
                        DownY = Math.abs(event.getY());
                        break;

                    case MotionEvent.ACTION_UP:

                                //结束录音（保存录音文件）
//                        mAudioRecoderUtils.cancelRecord();    //取消录音（不保存录音文件）
                        mPop.dismiss();
                        mButton.setText("按住说话");

                        if (MoveY > 100) {
                            mAudioRecoderUtils.cancelRecord();
                        } else {
                            mAudioRecoderUtils.stopRecord();
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        MoveX = Math.abs(event.getX() - DownX);
                        MoveY = Math.abs(event.getY() - DownY);
                        if (MoveY > 100) {
                            mButton.setText("取消录音");
                            mTextView2.setText("取消发送");
                        } else {
                            mButton.setText("松开保存");
                            mTextView2.setText("手指上滑,取消发送");
                        }
                }

                return true;
            }
        });
    }
    public void initMsgs() {
        Intent intent = getIntent();
        String resUrl = intent.getStringExtra("resUrl");
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        int id = pref.getInt("id", 0);
        new DownLoadTask().execute(resUrl+"?userId="+id);
    }

    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("情景对话");
        toolbar.setBackgroundColor(Color.rgb(255,255,255));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.home_icon);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar2, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (flag) {
            menu.findItem(R.id.store).setIcon(R.drawable.store3_icon);
        } else {
            menu.findItem(R.id.store).setIcon(R.drawable.store_icon);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.store:
                new CollectTask().execute(dialougeItem.colRes);
                break;
            case android.R.id.home:
                finish();
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    class CollectTask extends AsyncTask<String, Integer, Integer> {
        @Override
        protected Integer doInBackground(String... params) {
            HttpUtil.get(params[0], new okhttp3.Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String jsonData = response.body().string();
                        Gson gson = new Gson();
                        com.example.yzt.wm_english.login.Status status = gson.fromJson(jsonData, com.example.yzt.wm_english.login.Status.class);
                        publishProgress(status.getStatus());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {

                }
            });
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            switch (values[0]) {
                case 0:
                    ToastUtils.showToast(getApplicationContext(), "收藏失败" );
                case 1:
                    if (flag) {
                        flag = false;
                        ToastUtils.showToast(getApplicationContext(), "取消收藏成功" );
                        invalidateOptionsMenu();
                    } else {
                        flag = true;
                        ToastUtils.showToast(getApplicationContext(), "收藏成功" );
                        invalidateOptionsMenu();
                    }
                    invalidateOptionsMenu();
                    break;
                default:
                    break;
            }
        }
    }
    class DownLoadTask extends AsyncTask<String, Integer, Integer> {
        ProgressDialog dialog = new ProgressDialog(Dialouge.this);
        @Override
        protected void onPreExecute() {
            dialog.show();
            dialog.setMessage("Loading...");
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
                        dialougeItem = gson.fromJson(jsonData, DialougeItem.class);
                        messageList = new ArrayList<String>();
                        messageList = dialougeItem.question;
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
                    if (dialougeItem.collection == 1) {
                        flag = true;
                        invalidateOptionsMenu();
                    }
                    Msg msg = new Msg(messageList.get(0), Msg.TYPE_RECEIVED);
                    msgList.add(msg);
                    adapter.notifyItemChanged(msgList.size() - 1);//有新消息时，刷新ListView中的显示
                    msgRecyclerView.scrollToPosition(msgList.size() - 1);//将ListView定位到最后一行
                    dialog.dismiss();
                    break;
                default:
            }
        }

    }
}
