package com.example.yzt.wm_english.main.listening.shortDialog;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yzt.wm_english.R;

import java.util.List;

/**
 * Created by YZT on 2017/3/14.
 */

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.ViewHolder> {

    private List<String> mAnswerList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View answerView;
        EditText answerinput;
        TextView num;
        public ViewHolder(View view){
            super(view);
            answerView = view;
            num = (TextView) view.findViewById(R.id.answer_num);
            answerinput = (EditText) view.findViewById(R.id.answer_input);
        }
    }

    public AnswerAdapter(List<String> answerList) {
        mAnswerList = answerList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answeritem, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        Log.d("num", holder.getAdapterPosition()+" ");
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.num.setText("("+(position+1)+")");
    }

    @Override
    public int getItemCount() {
        return mAnswerList.size();
    }
}
