package com.example.yzt.wm_english.main.listening.shortDialog;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yzt.wm_english.R;
import com.example.yzt.wm_english.Units.ToastUtils;

import java.util.List;


/**
 * Created by YZT on 2017/3/14.
 */

public class AuditionAdapter extends RecyclerView.Adapter<AuditionAdapter.ViewHolder> {

    private List<Auditions.Audition> mAuditionList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View auditionView;
        ImageView auditionImage;
        TextView auditionTitle;
        public ViewHolder(View view) {
            super(view);
            auditionView = view;
            auditionImage = (ImageView) view.findViewById(R.id.audition_image);
            auditionTitle = (TextView) view.findViewById(R.id.audition_title);
        }
    }

    public AuditionAdapter(List<Auditions.Audition> auditionList) {
        mAuditionList = auditionList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.auditionitem, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.auditionView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Auditions.Audition audition = mAuditionList.get(position);

                ShortDialogItem.actionStart(v.getContext(),audition.resUrl);
                ToastUtils.showToast(v.getContext(), "resUrl = "+audition.resUrl);
            }

        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Auditions.Audition audition = mAuditionList.get(position);
        holder.auditionTitle.setText(audition.getTitle());
        Glide.with(holder.auditionView.getContext()).load(audition.getImgPath()).into(holder.auditionImage);
    }

    @Override
    public int getItemCount() {
        return mAuditionList.size();
    }
}
