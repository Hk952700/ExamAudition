package com.huaxia.exam.activity.answer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.huaxia.exam.R;
import com.huaxia.exam.adapter.MultiSelectedMultiOptionRecyclerViewAdapter;
import com.huaxia.exam.adapter.MultiSelectedMultiQuestionRecyclerViewAdapter;
import com.huaxia.exam.base.BaseActivity;
import com.huaxia.exam.bean.AnswerNineSelectedFiveOptionBean;
import com.huaxia.exam.bean.AnswerResultDataBean;
import com.huaxia.exam.bean.UploadGradeDataBean;
import com.huaxia.exam.utils.AnswerConstants;
import com.huaxia.exam.utils.SharedPreUtils;

import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * 2019年4月12日 10:45:48
 * jiao hao kang
 * 九选五 填空题(九宫格)
 */
public class NineSelectedFiveActivity extends BaseActivity implements View.OnClickListener {
    private ArrayList<AnswerNineSelectedFiveOptionBean> bufferArrayList = new ArrayList<>();
    private ArrayList<AnswerNineSelectedFiveOptionBean> optionArrayList = new ArrayList<>();
    private TextView mCountDownText;
    private RecyclerView mQuestionRecyclerview;
    private ImageView mQuestionRemoveImageview;
    private RecyclerView mOptionRecyclerview;
    private MultiSelectedMultiOptionRecyclerViewAdapter mOptionRecyclerViewAdapter;
    private MultiSelectedMultiQuestionRecyclerViewAdapter mQuestionRecyclerViewAdapter;
    private Button mConfirmButton;
    private String mOptions01;
    private String mAnswer;
    private AnswerResultDataBean answer;

    private long date;
    private long submitTime;

    private RelativeLayout mWebsocket_status;
    private SimpleDraweeView mTitle2Back;
    private TextView mTitle_name;
    private TextView mTitle_candidate_number;
    private TextView mTitle_school;
    private TextView mCount_num;
    private AlertDialog alertDialog;
    private ImageView alertDialogAnswerImageview;
    private TextView alertDialogAnswerRightAnswer;
    private TextView alertDialogAnswerUserAnswer;
    private TextView alertDialogAnswerRightAnswerText;
    private TextView alertDialogAnswerUserAnswerText;
    private Button alertDialogAnswerButton;

    @Override
    public int setContentView() {
        return R.layout.activity_nine_selected_five;
    }

    @Override
    public Context setContext() {
        return NineSelectedFiveActivity.this;
    }

    public void init() {
        //右上角WebSocket状态方框
        mWebsocket_status = (RelativeLayout) findViewById(R.id.nine_selected_five_websocket_status);


        //倒计时textview
        mCountDownText = (TextView) findViewById(R.id.nine_selected_five_count_down);
        //答案的recyclerview
        mQuestionRecyclerview = (RecyclerView) findViewById(R.id.nine_selected_five_question_recyclerview);
        //移除选中的选项
        mQuestionRemoveImageview = (ImageView) findViewById(R.id.nine_selected_five_question_remove_imageview);
        //显示选项的recyclerview
        mOptionRecyclerview = (RecyclerView) findViewById(R.id.nine_selected_five_option_recyclerview);
        mConfirmButton = (Button) findViewById(R.id.nine_selected_five_confirm_button);//确认图片


        mConfirmButton.setOnClickListener(this);
        mQuestionRemoveImageview.setOnClickListener(this);


        String user_name = SharedPreUtils.getString(this, "user_name");
        String user_numberplate = SharedPreUtils.getString(this, "user_numberplate");
        String user_school = SharedPreUtils.getString(this, "user_school");

        mTitle_name = (TextView) findViewById(R.id.nine_selected_five_title_name);
        mTitle_candidate_number = (TextView) findViewById(R.id.nine_selected_five_title_candidate_number);
        mTitle_school = (TextView) findViewById(R.id.nine_selected_five_title_school);


        mCount_num = (TextView) findViewById(R.id.nine_selected_five_count_num);

        mTitle_name.setText("姓名:" + user_name);
        mTitle_candidate_number.setText("桌号:" + user_numberplate);
        mTitle_school.setText("学校:" + user_school);


        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        answer = (AnswerResultDataBean) intent.getParcelableExtra("answer");

        if (answer != null) {

            mCount_num.setText(answer.getTp_senum() + "/15");

            mOptions01 = answer.getTp_subject();
            mAnswer = answer.getTp_answer();
            if (!TextUtils.isEmpty(mOptions01)) {
                char[] chars = mOptions01.toCharArray();

                for (int i = 0; i < chars.length; i++) {
                    AnswerNineSelectedFiveOptionBean answerNineSelectedFiveOptionBean = new AnswerNineSelectedFiveOptionBean();
                    answerNineSelectedFiveOptionBean.setValue(String.valueOf(chars[i]));
                    answerNineSelectedFiveOptionBean.setIndex(i);
                    optionArrayList.add(answerNineSelectedFiveOptionBean);
                }


            }
        }
        nineSelectedFive(bufferArrayList, optionArrayList);
        startCountDown(mCountDownText, 30);
        mCountDownText.setOnClickListener(this);
    }


    @Override
    public void onCountDownFinish(long date, long submitTime) {
        super.onCountDownFinish(date, submitTime);
        this.date = date;
        this.submitTime = submitTime;
        UploadGradeDataBean uploadGradeDataBean = new UploadGradeDataBean();
        alertDialog = new AlertDialog.Builder(this, R.style.Translucent_NoTitle).create();
        alertDialog.setCancelable(false);

        alertDialog.show();
        android.view.WindowManager.LayoutParams p = alertDialog.getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = (int) (WRAP_CONTENT);   //高度自适应
        p.width = (int) (WRAP_CONTENT);    //宽度自适应
        alertDialog.getWindow().setAttributes(p);
        alertDialog.setContentView(R.layout.alertdialog_amswer_results);


        alertDialogAnswerImageview = (ImageView) alertDialog.findViewById(R.id.alertdialog_answer_imageview);
        alertDialogAnswerRightAnswer = (TextView) alertDialog.findViewById(R.id.alertdialog_answer_right_answer);
        alertDialogAnswerUserAnswer = (TextView) alertDialog.findViewById(R.id.alertdialog_answer_user_answer);
        alertDialogAnswerRightAnswerText = (TextView) alertDialog.findViewById(R.id.alertdialog_answer_right_answer_text);
        alertDialogAnswerUserAnswerText = (TextView) alertDialog.findViewById(R.id.alertdialog_answer_user_answer_text);
        alertDialogAnswerButton = (Button) alertDialog.findViewById(R.id.alertdialog_answer_button);
        alertDialogAnswerButton.setOnClickListener(this);

        if (bufferArrayList.size() != 0) {
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < bufferArrayList.size(); i++) {
                stringBuffer.append(bufferArrayList.get(i).getValue());
            }
            String s = stringBuffer.toString();


            if (s.equals(mAnswer)) {
                uploadGradeDataBean.setTrRight("0");//对错
                alertDialogAnswerRightAnswerText.setBackgroundResource(R.drawable.shape_dialog_answer_result_yes);
                alertDialogAnswerUserAnswerText.setBackgroundResource(R.drawable.shape_dialog_answer_result_yes);
                alertDialogAnswerImageview.setBackgroundResource(R.drawable.alert_dialog_answer_yes);
                uploadGradeDataBean.setTrMark(answer.getTp_score() + "");//分数
            } else {
                uploadGradeDataBean.setTrRight("1");//对错
                alertDialogAnswerRightAnswerText.setBackgroundResource(R.drawable.shape_dialog_answer_result_no);
                alertDialogAnswerUserAnswerText.setBackgroundResource(R.drawable.shape_dialog_answer_result_no);
                alertDialogAnswerImageview.setBackgroundResource(R.drawable.alert_dialog_answer_no);

                uploadGradeDataBean.setTrMark("0");//分数
            }

            alertDialogAnswerRightAnswer.setText(mAnswer);
            //mTime.setText("本题用时:" + stampToDate(date));
            alertDialogAnswerUserAnswer.setText(s);
            uploadGradeDataBean.setTrAnswer(s);//学生答案

        } else {
            alertDialogAnswerRightAnswerText.setBackgroundResource(R.drawable.shape_dialog_answer_result_no);
            alertDialogAnswerUserAnswerText.setBackgroundResource(R.drawable.shape_dialog_answer_result_no);
            alertDialogAnswerImageview.setBackgroundResource(R.drawable.alert_dialog_answer_no);
            uploadGradeDataBean.setTrAnswer("未作答");//学生答案
            alertDialogAnswerUserAnswer.setText("未作答");
            uploadGradeDataBean.setTrMark("0");//分数
            uploadGradeDataBean.setTrRight("1");//对错
        }

        alertDialogAnswerRightAnswer.setText(mAnswer);
        // mTime.setText("本题用时:" + stampToDate(date));
        if (answer != null) {
            uploadGradeDataBean.setTrClass(answer.getTp_class() + "");//班级
            uploadGradeDataBean.setTrTime(date + "");//耗时
            uploadGradeDataBean.setTrQuestion(answer.getTp_subject());
            uploadGradeDataBean.setTrPapernum(answer.getTp_senum() + "");//题号
            uploadGradeDataBean.setTrRightAnswer(answer.getTp_answer());
            uploadGradeDataBean.setTrType(answer.getTp_type() + "");

            UploadGrade(uploadGradeDataBean);
            if (answer.getTp_senum() == AnswerConstants.ANSWER_QUESTION_SUM) {
                alertDialogAnswerButton.setVisibility(View.VISIBLE);
            }
        }

    }


    /**
     * 2019年4月1日 10:17:54
     * jiao hao kang
     * 九选五(九宫格)
     */
    private void nineSelectedFive(final ArrayList<AnswerNineSelectedFiveOptionBean> bufferArrayList, final ArrayList<AnswerNineSelectedFiveOptionBean> optionArrayList) {

        //设置两个recyclertview的布局管理器  初始化dapter 配置dapter setList
        mOptionRecyclerview.setLayoutManager(new GridLayoutManager(NineSelectedFiveActivity.this, 3));
        mQuestionRecyclerview.setLayoutManager(new LinearLayoutManager(NineSelectedFiveActivity.this, LinearLayoutManager.HORIZONTAL, false));
        mOptionRecyclerViewAdapter = new MultiSelectedMultiOptionRecyclerViewAdapter(NineSelectedFiveActivity.this, 5);
        mQuestionRecyclerViewAdapter = new MultiSelectedMultiQuestionRecyclerViewAdapter(NineSelectedFiveActivity.this, 5);
        mOptionRecyclerview.setAdapter(mOptionRecyclerViewAdapter);
        mQuestionRecyclerview.setAdapter(mQuestionRecyclerViewAdapter);
        mOptionRecyclerViewAdapter.setmList(optionArrayList);//下面选项的
        mQuestionRecyclerViewAdapter.setmList(bufferArrayList);//上面填空的
        //上面填空的背景

        //选中回调
        mOptionRecyclerViewAdapter.setOnOptionItemselected(new MultiSelectedMultiOptionRecyclerViewAdapter.onOptionItemselected() {

            @Override
            public void optionItemselected(AnswerNineSelectedFiveOptionBean selectedItem) {
                bufferArrayList.add(selectedItem);
                mQuestionRecyclerViewAdapter.setmList(bufferArrayList);
                mOptionRecyclerViewAdapter.setSelectedItemCount(mOptionRecyclerViewAdapter.getSelectedItemCount() + 1);
            }

        });


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nine_selected_five_question_remove_imageview:
                if (mOptionRecyclerViewAdapter.getSelectedItemCount() > 0 && bufferArrayList.size() > 0) {
                    for (int i = 0; i < optionArrayList.size(); i++) {
                        if (bufferArrayList.get(bufferArrayList.size() - 1).getIndex() == optionArrayList.get(i).getIndex()) {
                            optionArrayList.get(i).setChecked(false);
                            mOptionRecyclerViewAdapter.setmList(optionArrayList);//下面选项的
                            mOptionRecyclerViewAdapter.setSelectedItemCount(mOptionRecyclerViewAdapter.getSelectedItemCount() - 1);
                            //循环遍历 移除  刷新两个适配器
                            bufferArrayList.remove(bufferArrayList.size() - 1);
                            mQuestionRecyclerViewAdapter.setmList(bufferArrayList);//上面填空的
                            break;
                        }
                    }


                }


                break;

            case R.id.nine_selected_five_confirm_button:
                if (bufferArrayList.size() != 0) {
                    confirm();
                } else {
                    Toast.makeText(this, "请选择答案!", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.alertdialog_answer_button:
                getAnswerRecord();
                break;
            default:
                break;
        }
    }

    @Override
    public void websocketStatusChange(int color) {

        if (mWebsocket_status != null) {

            mWebsocket_status.setBackgroundResource(color);


        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (alertDialog != null) {
            if (alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
            alertDialog = null;
        }
    }
}
