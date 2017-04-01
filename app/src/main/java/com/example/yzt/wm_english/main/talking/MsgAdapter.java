package com.example.yzt.wm_english.main.talking;

import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yzt.wm_english.R;

import java.util.List;

/**
 * Created by YZT on 2017/3/27.
 */

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder>  {

    private List<Msg> mMsgList;

    private MediaPlayer mMediaPlayer = new MediaPlayer();

    static class ViewHolder extends RecyclerView.ViewHolder {


        LinearLayout leftLayout;

        LinearLayout rightLayout;

        TextView leftMsg;

        TextView rightMsg;

        public ViewHolder(View view) {
            super(view);
            leftLayout = (LinearLayout) view.findViewById(R.id.left_layout);
            rightLayout = (LinearLayout) view.findViewById(R.id.right_layout);
            leftMsg = (TextView) view.findViewById(R.id.left_msg);
            rightMsg = (TextView) view.findViewById(R.id.right_msg);
        }
    }

    public MsgAdapter(List<Msg> msgList) {
        mMsgList = msgList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    private void playMusic(String name) {
        try {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(name);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Msg msg = mMsgList.get(position);
        if (msg.getType() == Msg.TYPE_RECEIVED) {
            //如果是收到的消息,就显示在左边,将右边的消息布局隐藏
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftMsg.setText(msg.getContent());
        } else if (msg.getType() == Msg.TYPE_SENT) {
            holder.rightLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playMusic(msg.getContent()) ;
                }
            });
            //如果是收到的消息,就显示在右边,将左边的消息布局隐藏
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.VISIBLE);
//            holder.rightMsg.setText(msg.getContent());
            holder.rightMsg.setText("");

        }
    }

    @Override
    public int getItemCount() {
        return mMsgList.size();
    }
}
