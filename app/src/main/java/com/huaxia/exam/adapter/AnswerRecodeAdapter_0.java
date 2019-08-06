package com.huaxia.exam.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.huaxia.exam.R;
import com.huaxia.exam.adapter.viewholder.AnswerRecoredViewHolder_0;
import com.huaxia.exam.bean.UserRecoderBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:JiaoHaoKang
 * @E-mail:hkwaner@163.com
 * @Date:2019/6/27
 * @Description:
 */
public class AnswerRecodeAdapter_0 extends RecyclerView.Adapter<AnswerRecoredViewHolder_0> {
    private Context mContext;
    private List<UserRecoderBean.TestrecordesBean> mList = new ArrayList<UserRecoderBean.TestrecordesBean>();

    public AnswerRecodeAdapter_0(Context mContext) {
        this.mContext = mContext;
    }

    public void setmList(List<UserRecoderBean.TestrecordesBean> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public AnswerRecoredViewHolder_0 onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = viewGroup.inflate(mContext, R.layout.item_answer_record_score_list, null);
        return new AnswerRecoredViewHolder_0(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerRecoredViewHolder_0 answerRecoredViewHolder_0, int i) {
        if (i == 0) {//第一条
            answerRecoredViewHolder_0.list_question_num_rl.setBackgroundResource(R.drawable.shape_item_answer_record_score_listt_bg_0);
            answerRecoredViewHolder_0.list_correct_answer_rl.setBackgroundResource(R.drawable.shape_item_answer_record_score_listt_bg_0);
            answerRecoredViewHolder_0.list_user_answer_rl.setBackgroundResource(R.drawable.shape_item_answer_record_score_listt_bg_0);
            answerRecoredViewHolder_0.list_result_rl.setBackgroundResource(R.drawable.shape_item_answer_record_score_listt_bg_0);
            answerRecoredViewHolder_0.list_question_num_tv.setText("题号");
            answerRecoredViewHolder_0.list_correct_answer_tv.setText("我的答案");
            answerRecoredViewHolder_0.list_user_answer_tv.setText("正确答案");
            answerRecoredViewHolder_0.list_result_tv.setText("结果");
            answerRecoredViewHolder_0.list_result_iv.setVisibility(View.GONE);

        } else {
            answerRecoredViewHolder_0.list_question_num_tv.setText(String.valueOf(mList.get(i - 1).getTrPapernum()));
            answerRecoredViewHolder_0.list_correct_answer_tv.setText(mList.get(i - 1).getTrAnswer());
            answerRecoredViewHolder_0.list_user_answer_tv.setText(mList.get(i - 1).getTrRightAnswer());
            answerRecoredViewHolder_0.list_question_num_tv.setTextColor(Color.parseColor("#000000"));
            answerRecoredViewHolder_0.list_user_answer_tv.setTextColor(Color.parseColor("#000000"));
            answerRecoredViewHolder_0.list_correct_answer_tv.setTextColor(Color.parseColor("#000000"));
            answerRecoredViewHolder_0.list_result_tv.setVisibility(View.GONE);
            if (mList.get(i - 1).getTrRight() == 0) {
                answerRecoredViewHolder_0.list_result_iv.setImageResource(R.drawable.answer_record_yes);
            } else if (mList.get(i - 1).getTrRight() == 1) {
                answerRecoredViewHolder_0.list_result_iv.setImageResource(R.drawable.answer_record_no);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList.size() + 1;
    }
}
