package com.secretbiology.quiz;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Rohit Suratekar on 29-11-17 for Quiz.
 * All code is released under MIT License.
 */

public class AnswersAdapter extends RecyclerView.Adapter<AnswersAdapter.Holder> {

    private List<Answer> answers;
    private OnAnswerClick onAnswerClick;


    public AnswersAdapter(List<Answer> answers, OnAnswerClick onAnswerClick) {
        this.answers = answers;
        this.onAnswerClick = onAnswerClick;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.answers, parent, false));
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        Answer ans = answers.get(position);
        Context context = holder.itemView.getContext();
        holder.layout.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
        holder.text.setText(Html.fromHtml(ans.getText()));
        holder.letter.setText(context.getString(R.string.ans_bracket, position + 1));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAnswerClick.answer(holder.getAdapterPosition());
            }
        });
        if (ans.isSelected()) {
            if (ans.isCorrect()) {
                holder.layout.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
            } else {
                holder.layout.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
            }
        }
    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView letter, text;
        ConstraintLayout layout;

        public Holder(View itemView) {
            super(itemView);
            letter = itemView.findViewById(R.id.ans_letter);
            text = itemView.findViewById(R.id.ans_text);
            layout = itemView.findViewById(R.id.ans_layout);
        }
    }

    public interface OnAnswerClick {
        void answer(int position);
    }

}
