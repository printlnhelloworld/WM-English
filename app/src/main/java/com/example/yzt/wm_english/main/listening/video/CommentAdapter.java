package com.example.yzt.wm_english.main.listening.video;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yzt.wm_english.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by YZT on 2017/3/16.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    List<VideoRes.comment> mCommentList;
    boolean flag = false;
    static class ViewHolder extends RecyclerView.ViewHolder{
        View CommentView;
        CircleImageView userIcon;
        TextView userName;
        TextView commentTime;
        TextView commentSubject;
        TextView likesNum;
        ImageView likesImg;
        public ViewHolder(View view) {
            super(view);
            CommentView = view;
            userIcon = (CircleImageView) view.findViewById(R.id.user_icon);
            userName = (TextView) view.findViewById(R.id.user_name);
            commentTime = (TextView) view.findViewById(R.id.comment_time);
            commentSubject = (TextView) view.findViewById(R.id.comment_subject);
            likesNum = (TextView) view.findViewById(R.id.comment_likesNum);
            likesImg = (ImageView) view.findViewById(R.id.comment_likes);
        }
    }

    public CommentAdapter(List<VideoRes.comment> commentList) {
        mCommentList = commentList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final VideoRes.comment comment = mCommentList.get(position);
        Glide.with(holder.CommentView.getContext()).load(comment.userIcon).into(holder.userIcon);
        holder.userName.setText(comment.userName);
        holder.likesNum.setText(comment.praiseNum+"");
        holder.commentSubject.setText(comment.comment);
        holder.commentTime.setText(comment.commentTime);
        holder.likesImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    holder.likesImg.setImageResource(R.drawable.praise);
                    holder.likesNum.setText(comment.praiseNum+"");
                    holder.likesNum.setTextColor(Color.rgb(132,132,132));
                    flag = false;
                } else {
                    holder.likesImg.setImageResource(R.drawable.praised);
                    holder.likesNum.setText(comment.praiseNum+1+"");
                    holder.likesNum.setTextColor(Color.rgb(220,76,83));
                    flag = true;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCommentList.size();
    }
}
