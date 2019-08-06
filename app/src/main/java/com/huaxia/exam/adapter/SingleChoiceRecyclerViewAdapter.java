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
import java.util.List;

/**
 * 2019年3月28日 09:40:20
 * jiao hao kang
 * 单选题展示的recyclerview 的 adapter
 */
public class SingleChoiceRecyclerViewAdapter extends RecyclerView.Adapter<AnswerSingleChoice> {
    private Context mContext;
    private List<SingleChoiceItemBean> mList = new ArrayList<>();

    public SingleChoiceRecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setmList(ArrayList<SingleChoiceItemBean> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AnswerSingleChoice onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new AnswerSingleChoice(View.inflate(mContext, R.layout.answer_option_item_layout1, null));
    }

    @Override
    public void onBindViewHolder(@NonNull final AnswerSingleChoice viewHolder, final int i) {
        viewHolder.mSingle_choice_context_text.setText(mList.get(i).getContext());
        viewHolder.mSingle_choice_option_text.setText(mList.get(i).getOption());
        if (mList.get(i).isChecked()) {
            switch (mList.get(i).getOption().trim()) {
                case "A":
                    viewHolder.mSingle_choice_image.setImageResource(R.drawable.circle_bg_a_1);
                    break;
                case "B":
                    viewHolder.mSingle_choice_image.setImageResource(R.drawable.circle_bg_b_1);
                    break;
                case "C":
                    viewHolder.mSingle_choice_image.setImageResource(R.drawable.circle_bg_c_1);
                    break;
                case "D":
                    viewHolder.mSingle_choice_image.setImageResource(R.drawable.circle_bg_d_1);
                    break;
                default:
                    break;
            }

            viewHolder.mRlv_single_item_option_context.setBackgroundResource(R.drawable.single_item_context_bg_1);
        } else {
            viewHolder.mRlv_single_item_option_context.setBackgroundResource(R.drawable.single_item_context_bg_0);
            switch (mList.get(i).getOption().trim()) {
                case "A":
                    viewHolder.mSingle_choice_image.setImageResource(R.drawable.circle_bg_a_0);
                    break;
                case "B":
                    viewHolder.mSingle_choice_image.setImageResource(R.drawable.circle_bg_b_0);
                    break;
                case "C":
                    viewHolder.mSingle_choice_image.setImageResource(R.drawable.circle_bg_c_0);
                    break;
                case "D":
                    viewHolder.mSingle_choice_image.setImageResource(R.drawable.circle_bg_d_0);
                    break;
                default:
                    break;
            }

        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 单选的点击事件
                for (int j = 0; j < mList.size(); j++) {
                    if (i == j) {
                        mList.get(j).setChecked(true);
                    } else {
                        mList.get(j).setChecked(false);
                    }
                    onItemClickListener.onItemClick(mList.get(i));
                    notifyDataSetChanged();
                }


            }
        });
        if (i == mList.size() - 1) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.mSingle_item_option_rl.getLayoutParams();
            layoutParams.bottomMargin = 0;
            viewHolder.mSingle_item_option_rl.setLayoutParams(layoutParams);
        } else {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.mSingle_item_option_rl.getLayoutParams();
            layoutParams.bottomMargin = (int) mContext.getResources().getDimension(R.dimen.dp_10);
            viewHolder.mSingle_item_option_rl.setLayoutParams(layoutParams);
        }

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    private onItemClickListener onItemClickListener;

    public void setOnItemClickListener(SingleChoiceRecyclerViewAdapter.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface onItemClickListener {
        void onItemClick(SingleChoiceItemBean item);
    }
}
