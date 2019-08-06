package com.huaxia.exam.activity.answer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
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
import com.huaxia.exam.adapter.SingleChoiceRecyclerViewAdapter;
import com.huaxia.exam.base.BaseActivity;
import com.huaxia.exam.bean.AnswerResultDataBean;
import com.huaxia.exam.bean.SingleChoiceItemBean;
import com.huaxia.exam.bean.UploadGradeDataBean;
import com.huaxia.exam.utils.AnswerConstants;
import com.huaxia.exam.utils.SharedPreUtils;

import java.util.ArrayList;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * 2019年4月12日 10:22:14
 * jiao  hao kang
 * 单选题 activity
 */
public class SingleChoiceActivity extends BaseActivity implements View.OnClickListener {
    private RecyclerView mOptionRecyclerview;
    private TextView mQuestion;
    private TextView mCountDownText;
    private Button mConfirmButton;
    private SingleChoiceItemBean mUserAnswer;
    private AnswerResultDataBean data;

    private long date;
    private long submitTime;

    private RelativeLayout mWebsocket_status;
    private SimpleDraweeView mTitle2Back;
    private TextView mTitle_name;
    private TextView mTitle_school;
    private TextView mTitle_candidate_number;
    private TextView mCount_num;
    private AlertDialog alertDialog;
    private String[] split;
    private TextView alertDialogAnswerRightAnswer;
    private TextView alertDialogAnswerUserAnswer;
    private ImageView alertDialogAnswerImageview;
    private TextView alertDialogAnswerRightAnswerText;
    private TextView alertDialogAnswerUserAnswerText;
    private Button alertDialogAnswerButton;

    @Override
    public int setContentView() {
        return R.layout.activity_single_choice;
    }

    @Override
    public Context setContext() {
        return SingleChoiceActivity.this;
    }


    @Override
    public void init() {
        //右上角WebSocket状态方框
        mWebsocket_status = (RelativeLayout) findViewById(R.id.single_choice_websocket_status);


        mCountDownText = (TextView) findViewById(R.id.single_choice_count_down);
        mQuestion = (TextView) findViewById(R.id.single_choice_question);
        mOptionRecyclerview = (RecyclerView) findViewById(R.id.single_choice_option_recyclerview);

        mConfirmButton = (Button) findViewById(R.id.single_choice_confirm_button);//确认图片


        String user_name = SharedPreUtils.getString(this, "user_name");
        String user_numberplate = SharedPreUtils.getString(this, "user_numberplate");
        String user_school = SharedPreUtils.getString(this, "user_school");

        mTitle_name = (TextView) findViewById(R.id.single_choice_title_name);
        mTitle_candidate_number = (TextView) findViewById(R.id.single_choice_title_candidate_number);
        mTitle_school = (TextView) findViewById(R.id.single_choice_title_school);


        mCount_num = (TextView) findViewById(R.id.single_choice_count_num);

        mTitle_name.setText("姓名:　" + user_name);
        mTitle_candidate_number.setText("桌号:　" + user_numberplate);
        mTitle_school.setText("学校:　" + user_school);


        initDataAndRecycler();
        startCountDown(mCountDownText, 20);
        mConfirmButton.setOnClickListener(this);

    }


    private void initDataAndRecycler() {
        Intent intent = getIntent();
        data = (AnswerResultDataBean) intent.getParcelableExtra("answer");

        if (data != null) {
            mCount_num.setText(data.getTp_senum() + "/15");

            if (data.getTp_subject().length() <= 13) {
                mQuestion.setTextSize(this.getResources().getDimension(R.dimen.sp_36));
            } else if (data.getTp_subject().length() <= 18 && data.getTp_subject().length() > 13) {
                mQuestion.setTextSize(this.getResources().getDimension(R.dimen.sp_28));
            } else {
                mQuestion.setTextSize(this.getResources().getDimension(R.dimen.sp_18));
            }

            mQuestion.setText(data.getTp_subject().trim());
            ArrayList<SingleChoiceItemBean> singleChoiceItemBeans = new ArrayList<>();
            split = data.getTp_options().split("/");
            for (int i = 0; i < split.length; i++) {
                char[] chars = split[i].toCharArray();
                StringBuffer stringBuffer = new StringBuffer();
                for (int j = 0; j < chars.length; j++) {
                    if (j > 0) {
                        stringBuffer.append(chars[j]);
                    }
                }

                singleChoiceItemBeans.add(new SingleChoiceItemBean(String.valueOf(chars[0]), stringBuffer.toString(), false, false));
            }
            singleChoice(singleChoiceItemBeans);
        }

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


        if (mUserAnswer != null) {
            alertDialogAnswerUserAnswer.setText(mUserAnswer.getOption() + ":" + mUserAnswer.getContext());//我的答案
            if (data.getTp_answer().equals(mUserAnswer.getOption())) {
                uploadGradeDataBean.setTrRight("0");//对错
                alertDialogAnswerRightAnswerText.setBackgroundResource(R.drawable.shape_dialog_answer_result_yes);
                alertDialogAnswerUserAnswerText.setBackgroundResource(R.drawable.shape_dialog_answer_result_yes);
                alertDialogAnswerImageview.setBackgroundResource(R.drawable.alert_dialog_answer_yes);
                uploadGradeDataBean.setTrMark(data.getTp_score() + "");//分数
            } else {
                // mOverImage.setImageResource(R.drawable.answer_no);
                uploadGradeDataBean.setTrRight("1");//对错
                alertDialogAnswerRightAnswerText.setBackgroundResource(R.drawable.shape_dialog_answer_result_no);
                alertDialogAnswerUserAnswerText.setBackgroundResource(R.drawable.shape_dialog_answer_result_no);
                alertDialogAnswerImageview.setBackgroundResource(R.drawable.alert_dialog_answer_no);
                uploadGradeDataBean.setTrMark("0");//分数
            }

            uploadGradeDataBean.setTrAnswer(mUserAnswer.getOption());//学生答案


        } else {
            alertDialogAnswerRightAnswerText.setBackgroundResource(R.drawable.shape_dialog_answer_result_no);
            alertDialogAnswerUserAnswerText.setBackgroundResource(R.drawable.shape_dialog_answer_result_no);
            alertDialogAnswerImageview.setBackgroundResource(R.drawable.alert_dialog_answer_no);
            alertDialogAnswerUserAnswer.setText("未作答");
            uploadGradeDataBean.setTrAnswer("未作答");//学生答案
            uploadGradeDataBean.setTrMark("0");//分数
            uploadGradeDataBean.setTrRight("1");//对错
        }
        if (data != null) {
            uploadGradeDataBean.setTrClass(data.getTp_class() + "");//班级
            uploadGradeDataBean.setTrTime(date + "");//耗时
            uploadGradeDataBean.setTrQuestion(data.getTp_subject());
            uploadGradeDataBean.setTrPapernum(data.getTp_senum() + "");//题号。

            String tp_answer = data.getTp_answer();

            StringBuffer stringBuffer = new StringBuffer();

            for (int i = 0; i < split.length; i++) {
                char[] chars = split[i].toCharArray();
                if (String.valueOf(chars[0]).trim().equals(tp_answer.trim())) {

                    stringBuffer.append(String.valueOf(chars[0]) + ":" + split[i].substring(1, split[i].length()).trim());
                }
            }


            alertDialogAnswerRightAnswer.setText(stringBuffer);//正确答案

            uploadGradeDataBean.setTrRightAnswer(data.getTp_answer());
            uploadGradeDataBean.setTrType(data.getTp_type() + "");
            UploadGrade(uploadGradeDataBean);
            if (data.getTp_senum() == AnswerConstants.ANSWER_QUESTION_SUM) {
                alertDialogAnswerButton.setVisibility(View.VISIBLE);
            }
        }
        mConfirmButton.setVisibility(View.GONE);

    }


    /**
     * 2019年4月1日 11:41:27
     * jiao hao kang
     * 单选题
     */
    private void singleChoice(ArrayList<SingleChoiceItemBean> singleDatas) {

        mOptionRecyclerview.setLayoutManager(new LinearLayoutManager(this, VERTICAL, false));
        SingleChoiceRecyclerViewAdapter answerSingleChoiceRecyclerViewAdapter = new SingleChoiceRecyclerViewAdapter(this);
        mOptionRecyclerview.setAdapter(answerSingleChoiceRecyclerViewAdapter);
        answerSingleChoiceRecyclerViewAdapter.setmList(singleDatas);
        answerSingleChoiceRecyclerViewAdapter.setOnItemClickListener(new SingleChoiceRecyclerViewAdapter.onItemClickListener() {
            @Override
            public void onItemClick(SingleChoiceItemBean item) {
                mUserAnswer = item;
            }
        });


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.single_choice_confirm_button:
                if (mUserAnswer != null && !TextUtils.isEmpty(mUserAnswer.getOption())) {
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
