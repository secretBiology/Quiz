package com.secretbiology.minimalquiz.common;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.secretbiology.minimalquiz.R;

import java.util.List;


public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.Holder> {

    private List<Answer> answerList;
    private OnAnswerClick answerClick;

    public AnswerAdapter(List<Answer> answerList, OnAnswerClick answerClick) {
        this.answerList = answerList;
        this.answerClick = answerClick;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.answer, parent, false));
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        Answer answer = answerList.get(position);
        Context context = holder.itemView.getContext();

        holder.text.setText((Html.fromHtml(answer.getText())));

        if (answer.showAnswer()) {
            if (answer.isSelected()) {
                holder.text.setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_red_light));
            }
            if (answer.isCorrect()) {
                holder.text.setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_green_light));
            }
        } else {
            holder.text.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answerClick.onAnswerClicked(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return answerList.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        TextView text;
        ConstraintLayout layout;

        Holder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.ans_text);
            layout = itemView.findViewById(R.id.ans_layout);
        }
    }

    public interface OnAnswerClick {

        void onAnswerClicked(int position);
    }
}
