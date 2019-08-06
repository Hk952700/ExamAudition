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

public class TwelveSelectedSevenActivity extends BaseActivity implements View.OnClickListener {
    private ArrayList<AnswerNineSelectedFiveOptionBean> bufferArrayList = new ArrayList<>();
    private ArrayList<AnswerNineSelectedFiveOptionBean> optionArrayList = new ArrayList<>();
    private TextView mCountDownText;
    private RecyclerView mQuestionRecyclerview;
    private RecyclerView mOptionRecyclerview;
    private ImageView mQuestionRemoveImageview;
    private MultiSelectedMultiOptionRecyclerViewAdapter mOptionRecyclerViewAdapter;
    private MultiSelectedMultiQuestionRecyclerViewAdapter mQuestionRecyclerViewAdapter;
    private Button mConfirmButton;
    private String mOptions1;
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
        return R.layout.activity_twelve_selected_seven;
    }

    @Override
    public Context setContext() {
        return TwelveSelectedSevenActivity.this;
    }


    @Override
    public void init() {
        //右上角WebSocket状态方框
        mWebsocket_status = (RelativeLayout) findViewById(R.id.twelve_selected_seven_websocket_status);


        //初始化控件
        mCountDownText = (TextView) findViewById(R.id.twelve_selected_seven_count_down);//倒计时(textview)
        mQuestionRecyclerview = (RecyclerView) findViewById(R.id.twelve_selected_seven_question_recyclerview);//选手选中的答案的recyclerview
        mQuestionRemoveImageview = (ImageView) findViewById(R.id.twelve_selected_seven_question_remove_imageview);//选中的答案的删除按钮(imageview)
        mOptionRecyclerview = (RecyclerView) findViewById(R.id.twelve_selected_seven_option_recyclerview);//选项的recyclerview


        mQuestionRemoveImageview.setOnClickListener(this);

        mConfirmButton = (Button) findViewById(R.id.twelve_selected_seven_confirm_button);//确认图片

        String user_name = SharedPreUtils.getString(this, "user_name");
        String user_numberplate = SharedPreUtils.getString(this, "user_numberplate");
        String user_school = SharedPreUtils.getString(this, "user_school");

        mTitle_name = (TextView) findViewById(R.id.twelve_selected_seven_title_name);
        mTitle_candidate_number = (TextView) findViewById(R.id.twelve_selected_seven_title_candidate_number);
        mTitle_school = (TextView) findViewById(R.id.twelve_selected_seven_title_school);


        mCount_num = (TextView) findViewById(R.id.twelve_selected_seven_count_num);

        mTitle_name.setText("姓名:" + user_name);
        mTitle_candidate_number.setText("桌号:" + user_numberplate);
        mTitle_school.setText("学校:" + user_school);


        initData();
        startCountDown(mCountDownText, 30);
        mConfirmButton.setOnClickListener(this);
    }


    private void initData() {
        Intent intent01 = getIntent();
        answer = (AnswerResultDataBean) intent01.getParcelableExtra("answer");

        if (answer != null) {
            mCount_num.setText(answer.getTp_senum() + "/15");
            mOptions1 = answer.getTp_subject();
            mAnswer = answer.getTp_answer();

            mOptions1 = answer.getTp_subject();

        }

        if (!TextUtils.isEmpty(mOptions1)) {
            char[] chars = mOptions1.toCharArray();

            for (int i = 0; i < chars.length; i++) {
                AnswerNineSelectedFiveOptionBean answerNineSelectedFiveOptionBean = new AnswerNineSelectedFiveOptionBean();
                answerNineSelectedFiveOptionBean.setValue(String.valueOf(chars[i]));
                answerNineSelectedFiveOptionBean.setIndex(i);
                optionArrayList.add(answerNineSelectedFiveOptionBean);
            }


        }
        initRecyclerView(optionArrayList, bufferArrayList);
    }


    private void initRecyclerView(final ArrayList<AnswerNineSelectedFiveOptionBean> optionArrayList, final ArrayList<AnswerNineSelectedFiveOptionBean> bufferArrayList) {

        mOptionRecyclerViewAdapter = new MultiSelectedMultiOptionRecyclerViewAdapter(TwelveSelectedSevenActivity.this, 7);
        mOptionRecyclerview.setLayoutManager(new GridLayoutManager(TwelveSelectedSevenActivity.this, 4));

        mQuestionRecyclerview.setLayoutManager(new LinearLayoutManager(TwelveSelectedSevenActivity.this, LinearLayoutManager.HORIZONTAL, false));
        mQuestionRecyclerViewAdapter = new MultiSelectedMultiQuestionRecyclerViewAdapter(TwelveSelectedSevenActivity.this, 7);
        mQuestionRecyclerview.setAdapter(mQuestionRecyclerViewAdapter);

        mOptionRecyclerview.setAdapter(mOptionRecyclerViewAdapter);
        mOptionRecyclerViewAdapter.setmList(optionArrayList);

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
    public void onCountDownFinish(long date, long submitTime) {
        super.onCountDownFinish(date, submitTime);
        this.submitTime = submitTime;
        this.date = date;
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


        if (answer != null) {
            uploadGradeDataBean.setTrQuestion(answer.getTp_subject());
            alertDialogAnswerRightAnswer.setText(mAnswer);
            //mTime.setText("本题用时:" + stampToDate(date));
            uploadGradeDataBean.setTrClass(answer.getTp_class() + "");//班级
            uploadGradeDataBean.setTrTime(date + "");//耗时
            uploadGradeDataBean.setTrPapernum(answer.getTp_senum() + "");//题号
            uploadGradeDataBean.setTrRightAnswer(mAnswer);
            uploadGradeDataBean.setTrType(answer.getTp_type() + "");
            UploadGrade(uploadGradeDataBean);
            if (answer.getTp_senum() == AnswerConstants.ANSWER_QUESTION_SUM) {
                alertDialogAnswerButton.setVisibility(View.VISIBLE);
            }
        }


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.twelve_selected_seven_question_remove_imageview://点击删除按钮(imageview)
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
            case R.id.twelve_selected_seven_confirm_button: //点击确认按钮(textview)
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
