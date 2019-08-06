package com.huaxia.exam.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.huaxia.exam.R;
import com.huaxia.exam.adapter.viewholder.AnswerSingleChoice;
import com.huaxia.exam.bean.SingleChoiceItemBean;

import java.util.ArrayList;

/**
 * 2019年4月18日 09:41:44
 * jiao hao kang
 * 多选题recyclerview adapter
 */
public class MultipleChoiceRecyclerViewAdapter extends RecyclerView.Adapter<AnswerSingleChoice> {
    private Context context;
    private ArrayList<SingleChoiceItemBean> mList = new ArrayList<>();

    public MultipleChoiceRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public void setmList(ArrayList<SingleChoiceItemBean> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AnswerSingleChoice onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new AnswerSingleChoice(View.inflate(context, R.layout.answer_option_item_layout1, null));
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerSingleChoice answerSingleChoice, final int i) {
        if (mList.get(i).isChecked()) {

            answerSingleChoice.mRlv_single_item_option_context.setBackgroundResource(R.drawable.single_item_context_bg_1);
            switch (mList.get(i).getOption().trim()) {
                case "A":
                    answerSingleChoice.mSingle_choice_image.setImageResource(R.drawable.circle_bg_a_1);
                    break;
                case "B":
                    answerSingleChoice.mSingle_choice_image.setImageResource(R.drawable.circle_bg_b_1);
                    break;
                case "C":
                    answerSingleChoice.mSingle_choice_image.setImageResource(R.drawable.circle_bg_c_1);
                    break;
                case "D":
                    answerSingleChoice.mSingle_choice_image.setImageResource(R.drawable.circle_bg_d_1);
                    break;
                default:
                    break;
            }
        } else {
            answerSingleChoice.mRlv_single_item_option_context.setBackgroundResource(R.drawable.single_item_context_bg_0);
            switch (mList.get(i).getOption().trim()) {
                case "A":
                    answerSingleChoice.mSingle_choice_image.setImageResource(R.drawable.circle_bg_a_0);
                    break;
                case "B":
                    answerSingleChoice.mSingle_choice_image.setImageResource(R.drawable.circle_bg_b_0);
                    break;
                case "C":
                    answerSingleChoice.mSingle_choice_image.setImageResource(R.drawable.circle_bg_c_0);
                    break;
                case "D":
                    answerSingleChoice.mSingle_choice_image.setImageResource(R.drawable.circle_bg_d_0);
                    break;
                default:
                    break;
            }

        }

        answerSingleChoice.mSingle_choice_option_text.setText(mList.get(i).getOption());
        answerSingleChoice.mSingle_choice_context_text.setText(mList.get(i).getContext());


        answerSingleChoice.mRlv_single_item_option_context.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mList.get(i).isChecked()) {
                    mList.get(i).setChecked(false);
                } else {
                    mList.get(i).setChecked(true);
                }
                notifyItemChanged(i);
                ArrayList<SingleChoiceItemBean> selectItemBeans = new ArrayList<>();
                for (int j = 0; j < mList.size(); j++) {
                    if (mList.get(j).isChecked()) {
                        selectItemBeans.add(mList.get(j));
                    }
                }
                onItemClickListener.onItemClick(selectItemBeans);


            }
        });

        if (i == mList.size() - 1) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) answerSingleChoice.mSingle_item_option_rl.getLayoutParams();
            layoutParams.bottomMargin = 0;
            answerSingleChoice.mSingle_item_option_rl.setLayoutParams(layoutParams);
        } else {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) answerSingleChoice.mSingle_item_option_rl.getLayoutParams();
            layoutParams.bottomMargin = (int) context.getResources().getDimension(R.dimen.dp_10);
            answerSingleChoice.mSingle_item_option_rl.setLayoutParams(layoutParams);
        }


    }


    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    private onItemClickListener onItemClickListener;

    public void setOnItemClickListener(onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface onItemClickListener {
        void onItemClick(ArrayList<SingleChoiceItemBean> items);
    }

}
