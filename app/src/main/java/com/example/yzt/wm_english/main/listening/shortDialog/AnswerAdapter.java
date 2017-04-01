package com.example.yzt.wm_english.main.listening.shortDialog;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yzt.wm_english.R;

import java.util.List;

/**
 * Created by YZT on 2017/3/14.
 */

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.ViewHolder> {
    private List<Answer> mAnswersList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View answerView;
        EditText answerinput;
        TextView num;
        TextView answer;
        ImageView judgeIcon;
        public ViewHolder(View view){
            super(view);
            answerView = view;
            num = (TextView) view.findViewById(R.id.answer_num);
            answerinput = (EditText) view.findViewById(R.id.answer_input);
            answer = (TextView) view.findViewById(R.id.answer);
            judgeIcon = (ImageView) view.findViewById(R.id.judge);
        }
    }

    public AnswerAdapter(List<Answer> answerList) {
        mAnswersList = answerList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answeritem, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        Log.d("num", holder.getAdapterPosition()+" ");
        holder.answerinput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                holder.judgeIcon.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals(mAnswersList.get(holder.getAdapterPosition()).answer)){
                    change(holder);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final String answer = mAnswersList.get(position).answer;
        holder.num.setText("("+(position+1)+")");
        if (mAnswersList.get(position).flag) {
            holder.answer.setVisibility(View.VISIBLE);
        } else {
            holder.answer.setVisibility(View.INVISIBLE);
        }
        holder.answer.setText(answer);


    }
    public void change(ViewHolder holder) {
        holder.judgeIcon.setImageResource(R.drawable.right_icon);
    }
    @Override
    public int getItemCount() {
        return mAnswersList.size();
    }
}
