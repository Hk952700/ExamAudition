package com.huaxia.exam.adapter.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huaxia.exam.R;

/**
 * @Author:JiaoHaoKang
 * @E-mail:hkwaner@163.com
 * @Date:2019/6/27
 * @Description:
 */
public class AnswerRecoredViewHolder_0 extends RecyclerView.ViewHolder {


    public final RelativeLayout list_question_num_rl;
    public final RelativeLayout list_correct_answer_rl;
    public final RelativeLayout list_user_answer_rl;
    public final RelativeLayout list_result_rl;
    public final TextView list_question_num_tv;
    public final TextView list_correct_answer_tv;
    public final TextView list_user_answer_tv;
    public final TextView list_result_tv;
    public final ImageView list_result_iv;

    public AnswerRecoredViewHolder_0(@NonNull View itemView) {
        super(itemView);
        list_question_num_rl = (RelativeLayout) itemView.findViewById(R.id.item_answer_record_score_list_question_num_rl);
        list_correct_answer_rl = (RelativeLayout) itemView.findViewById(R.id.item_answer_record_score_list_correct_answer_rl);
        list_user_answer_rl = (RelativeLayout) itemView.findViewById(R.id.item_answer_record_score_list_user_answer_rl);
        list_result_rl = (RelativeLayout) itemView.findViewById(R.id.item_answer_record_score_list_result_rl);
        list_question_num_tv = (TextView) itemView.findViewById(R.id.item_answer_record_score_list_question_num_tv);
        list_correct_answer_tv = (TextView) itemView.findViewById(R.id.item_answer_record_score_list_correct_answer_tv);
        list_user_answer_tv = (TextView) itemView.findViewById(R.id.item_answer_record_score_list_user_answer_tv);
        list_result_tv = (TextView) itemView.findViewById(R.id.item_answer_record_score_list_result_tv);
        list_result_iv = (ImageView) itemView.findViewById(R.id.item_answer_record_score_list_result_iv);

    }
}
