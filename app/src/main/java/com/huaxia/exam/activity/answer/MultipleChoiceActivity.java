package com.huaxia.exam.activity.answer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.huaxia.exam.adapter.MultipleChoiceRecyclerViewAdapter;
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
 * 多选Activity
 */
public class MultipleChoiceActivity extends BaseActivity implements View.OnClickListener {

    private TextView mCountDown;
    private TextView mQuestion;
    private RecyclerView mOptionRecyclerview;
    private Button mConfirmButton;
    private ArrayList<SingleChoiceItemBean> optionsArray = new ArrayList<>();
    private AnswerResultDataBean data;

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
    private String[] split;


    @Override
    public int setContentView() {
        return R.layout.activity_multiple_choice;
    }

    @Override
    public Context setContext() {
        return MultipleChoiceActivity.this;
    }

    @Override
    public void init() {
        //右上角WebSocket状态方框
        mWebsocket_status = (RelativeLayout) findViewById(R.id.multiple_choice_websocket_status);


        mCountDown = (TextView) findViewById(R.id.multiple_choice_count_down);//倒计时显示textview
        mQuestion = (TextView) findViewById(R.id.multiple_choice_question);//问题展示
        mOptionRecyclerview = (RecyclerView) findViewById(R.id.multiple_choice_option_recyclerview);//选项展示
        mConfirmButton = (Button) findViewById(R.id.multiple_choice_confirm_button);//确认图片

        mTitle_name = (TextView) findViewById(R.id.multiple_choice_title_name);
        mTitle_candidate_number = (TextView) findViewById(R.id.multiple_choice_title_candidate_number);
        mTitle_school = (TextView) findViewById(R.id.multiple_choice_title_school);
        String user_name = SharedPreUtils.getString(this, "user_name");
        String user_numberplate = SharedPreUtils.getString(this, "user_numberplate");
        String user_school = SharedPreUtils.getString(this, "user_school");

        mCount_num = (TextView) findViewById(R.id.multiple_choice_count_num);


        mTitle_name.setText(user_name);
        mTitle_candidate_number.setText(user_numberplate);
        mTitle_school.setText(user_school);


        initDataAndRecycler();
        startCountDown(mCountDown, 20);
        mConfirmButton.setOnClickListener(this);
    }


    private void initDataAndRecycler() {
        Intent intent = getIntent();
        data = (AnswerResultDataBean) intent.getParcelableExtra("answer");

        if (data != null) {

            if (data.getTp_subject().length() <= 13) {
                mQuestion.setTextSize(this.getResources().getDimension(R.dimen.sp_36));
            } else if (data.getTp_subject().length() <= 18 && data.getTp_subject().length() > 13) {
                mQuestion.setTextSize(this.getResources().getDimension(R.dimen.sp_28));
            } else {
                mQuestion.setTextSize(this.getResources().getDimension(R.dimen.sp_18));
            }

            mCount_num.setText(data.getTp_senum() + "/15");

            mQuestion.setText(data.getTp_subject().trim());
            //选项
            ArrayList<SingleChoiceItemBean> multipleChoiceItemBeans = new ArrayList<>();
            split = data.getTp_options().split("/");
            for (int i = 0; i < split.length; i++) {
                char[] chars = split[i].toCharArray();
                StringBuffer stringBuffer = new StringBuffer();
                for (int j = 0; j < chars.length; j++) {
                    if (j > 0) {
                        stringBuffer.append(chars[j]);
                    }
                }

                multipleChoiceItemBeans.add(new SingleChoiceItemBean(String.valueOf(chars[0]), stringBuffer.toString(), false, false));
            }

            multipleChoice(multipleChoiceItemBeans);
        }


    }

    /**
     * 2019年5月6日 18:05:45
     * jiao hao kang
     *
     * @param multipleChoiceItemBeans
     */
    private void multipleChoice(ArrayList<SingleChoiceItemBean> multipleChoiceItemBeans) {

        MultipleChoiceRecyclerViewAdapter multipleChoiceRecyclerViewAdapter = new MultipleChoiceRecyclerViewAdapter(MultipleChoiceActivity.this);
        mOptionRecyclerview.setLayoutManager(new LinearLayoutManager(this, VERTICAL, false));
        mOptionRecyclerview.setAdapter(multipleChoiceRecyclerViewAdapter);
        multipleChoiceRecyclerViewAdapter.setmList(multipleChoiceItemBeans);
        multipleChoiceRecyclerViewAdapter.setOnItemClickListener(new MultipleChoiceRecyclerViewAdapter.onItemClickListener() {
            @Override
            public void onItemClick(ArrayList<SingleChoiceItemBean> items) {
                optionsArray = items;
            }
        });


    }


    @Override
    public void onCountDownFinish(long date, long submitTime) {
        super.onCountDownFinish(date, submitTime);
        this.date = date;
        this.submitTime = submitTime;

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

        UploadGradeDataBean uploadGradeDataBean = new UploadGradeDataBean();

        if (optionsArray != null && optionsArray.size() > 0) {
            StringBuffer stringBuffer = new StringBuffer();
            StringBuffer stringBuffer1 = new StringBuffer();
            for (int i = 0; i < optionsArray.size(); i++) {
                stringBuffer.append(optionsArray.get(i).getOption().trim());
                stringBuffer1.append(optionsArray.get(i).getOption() + ":" + optionsArray.get(i).getContext().trim() + ",");
            }
            String substring = stringBuffer1.substring(0, stringBuffer1.length() - 1);


            if (isRight(stringBuffer.toString(), data.getTp_answer())) {
                uploadGradeDataBean.setTrRight("0");//对错
                alertDialogAnswerRightAnswerText.setBackgroundResource(R.drawable.shape_dialog_answer_result_yes);
                alertDialogAnswerUserAnswerText.setBackgroundResource(R.drawable.shape_dialog_answer_result_yes);
                alertDialogAnswerImageview.setBackgroundResource(R.drawable.alert_dialog_answer_yes);
                uploadGradeDataBean.setTrMark(data.getTp_score() + "");//分数
            } else {
                alertDialogAnswerRightAnswerText.setBackgroundResource(R.drawable.shape_dialog_answer_result_no);
                alertDialogAnswerUserAnswerText.setBackgroundResource(R.drawable.shape_dialog_answer_result_no);
                alertDialogAnswerImageview.setBackgroundResource(R.drawable.alert_dialog_answer_no);
                uploadGradeDataBean.setTrRight("1");//对错
                uploadGradeDataBean.setTrMark("0");//分数
            }
            uploadGradeDataBean.setTrAnswer(stringBuffer.toString());//学生答案
            alertDialogAnswerUserAnswer.setText(substring);
        } else {
            uploadGradeDataBean.setTrAnswer("未作答");//学生答案
            alertDialogAnswerUserAnswer.setText("未作答");
            uploadGradeDataBean.setTrRight("1");//对错
            alertDialogAnswerRightAnswerText.setBackgroundResource(R.drawable.shape_dialog_answer_result_no);
            alertDialogAnswerUserAnswerText.setBackgroundResource(R.drawable.shape_dialog_answer_result_no);
            alertDialogAnswerImageview.setBackgroundResource(R.drawable.alert_dialog_answer_no);
            uploadGradeDataBean.setTrMark("0");//分数
        }

        if (data != null) {
            uploadGradeDataBean.setTrQuestion(data.getTp_subject());
            uploadGradeDataBean.setTrClass(data.getTp_class() + "");//班级
            uploadGradeDataBean.setTrTime(date + "");//耗时
            uploadGradeDataBean.setTrPapernum(data.getTp_senum() + "");//题号
            uploadGradeDataBean.setTrRightAnswer(data.getTp_answer());

            char[] chars = data.getTp_answer().trim().toCharArray();
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < split.length; i++) {
                for (int j = 0; j < chars.length; j++) {
                    if (split[i].trim().substring(0, 1).equals(String.valueOf(chars[j]))) {
                        stringBuffer.append(String.valueOf(chars[j]) + ":" + split[i].trim().substring(1, split[i].trim().length()) + ",");
                        continue;
                    }
                }
            }

            String substring = stringBuffer.substring(0, stringBuffer.length() - 1);
            alertDialogAnswerRightAnswer.setText(substring);

            uploadGradeDataBean.setTrType(data.getTp_type() + "");
            UploadGrade(uploadGradeDataBean);

            if (data.getTp_senum() == AnswerConstants.ANSWER_QUESTION_SUM) {
                alertDialogAnswerButton.setVisibility(View.VISIBLE);
            }
        }
        mConfirmButton.setVisibility(View.GONE);
    }


    /**
     * 2019年5月6日 18:38:42
     * jiao hao kang
     *
     * @param userAnswer 选手答案
     * @param answer     正确答案
     * @return 是否正确
     */

    private boolean isRight(String userAnswer, String answer) {

        char[] chars0 = userAnswer.toCharArray();//选手答案
        char[] chars1 = answer.trim().toCharArray();//正确答案

        if (chars0.length != chars1.length) {
            return false;
        } else {
            for (int i = 0; i < chars1.length; i++) {
                boolean flag = false;
                for (int j = 0; j < chars0.length; j++) {
                    if (String.valueOf(chars0[j]).equals(String.valueOf(chars1[i]))) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    return false;
                }

            }
        }

        return true;
    }


    @Override
    public void websocketStatusChange(int color) {
        if (mWebsocket_status != null) {
            mWebsocket_status.setBackgroundResource(color);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.multiple_choice_confirm_button:
                if (optionsArray != null && optionsArray.size() > 0) {
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
