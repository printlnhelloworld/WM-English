package com.example.yzt.wm_english.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yzt.wm_english.R;
import com.example.yzt.wm_english.main.listening.video.Video;

import java.util.List;

/**
 * Created by YZT on 2017/3/8.
 */

public class ResourceAdapter extends RecyclerView.Adapter<ResourceAdapter.ViewHolder> {

    private List<Mainres.Resource> mResourceList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View resourceView;
        ImageView resourceImage;
        TextView resourceTitle;
        TextView resourceElaborate;
        TextView resourceWordNum;

        public ViewHolder(View view) {
            super(view);
            resourceView = view;
            resourceImage = (ImageView) view.findViewById(R.id.resource_image);
            resourceTitle = (TextView) view.findViewById(R.id.resource_title);
            resourceElaborate = (TextView) view.findViewById(R.id.resource_elaborate);
            resourceWordNum = (TextView) view.findViewById(R.id.resource_wordNum);
        }
    }
    public ResourceAdapter(List<Mainres.Resource> resourceList) {
        mResourceList = resourceList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.resourceitem_layout, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.resourceView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Mainres.Resource resource = mResourceList.get(position);
                Video.actionStart(v.getContext(), resource.resUrl);
            }

        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Mainres.Resource resource = mResourceList.get(position);
        holder.resourceTitle.setText(resource.getTitle());
        Glide.with(holder.resourceView.getContext()).load(resource.getImgUrl()).into(holder.resourceImage);
        holder.resourceWordNum.setText(resource.getWordNum()+"字");
        holder.resourceElaborate.setText(resource.getElaborate());
    }

    @Override
    public int getItemCount() {
        return mResourceList.size();
    }
}
