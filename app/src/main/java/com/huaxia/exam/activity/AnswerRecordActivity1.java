package com.huaxia.exam.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huaxia.exam.R;
import com.huaxia.exam.adapter.AnswerRecodeAdapter_0;
import com.huaxia.exam.base.BaseActivity;
import com.huaxia.exam.bean.HttpParametersBean;
import com.huaxia.exam.bean.UserRecoderBean;
import com.huaxia.exam.bean.UserStandingListBean;
import com.huaxia.exam.service.ExamHpptService;
import com.huaxia.exam.utils.AnswerConstants;
import com.huaxia.exam.utils.ScreenCaptureUtil;
import com.huaxia.exam.utils.SharedPreUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Author:JiaoHaoKang
 * @E-mail:hkwaner@163.com
 * @Date:2019/6/27
 * @Description:
 */
public class AnswerRecordActivity1 extends BaseActivity implements View.OnClickListener {

    private RelativeLayout mWebsocket_status;
    private Button mBtn_answer_record_1_retry;
    private RecyclerView list_rev;
    private AnswerRecodeAdapter_0 answerRecodeAdapter_0;
    private Button answer_record_btn;
    private SimpleDateFormat format = new java.text.SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");
    private TextView time_tv;
    private ImageView answer_record_score_hundreds;
    private ImageView answer_record_score_tens;
    private ImageView answer_record_score_ones;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    dismissProgressDialog();
                    getUserRecored();
                    break;
                default:
                    break;

            }
        }
    };
    private TextView user_name_tv;

    @Override
    public int setContentView() {
        return R.layout.activity_answer_record_1;
    }

    @Override
    public Context setContext() {
        return AnswerRecordActivity1.this;
    }

    @Override
    public void init() {
        //右上角WebSocket状态方框
        mWebsocket_status = (RelativeLayout) findViewById(R.id.answer_record_1_websocket_status);

        //重试按钮
        mBtn_answer_record_1_retry = (Button) findViewById(R.id.btn_answer_record_1_retry);
        mBtn_answer_record_1_retry.setOnClickListener(this);
        answer_record_btn = (Button) findViewById(R.id.answer_record_btn);
        time_tv = (TextView) findViewById(R.id.answer_record_score_time_tv);
        list_rev = (RecyclerView) findViewById(R.id.answer_record_score_list_rev);
        answer_record_score_hundreds = (ImageView) findViewById(R.id.answer_record_score_hundreds);
        answer_record_score_tens = (ImageView) findViewById(R.id.answer_record_score_tens);
        answer_record_score_ones = (ImageView) findViewById(R.id.answer_record_score_ones);
        user_name_tv = (TextView) findViewById(R.id.answer_record_score_user_name_tv);
        answer_record_btn.setOnClickListener(this);
        showProgressDialogCancelable(this, "请稍等...");
        handler.sendEmptyMessageDelayed(0, 2000);
    }

    /**
     * 2019年5月17日 15:59:34
     * jiao hao kang
     * 获取用户答题列表
     *
     * @return
     */
    private void getUserRecored() {
        HashMap<String, String> map = new HashMap<>();
        map.put("trClass", SharedPreUtils.getString(this, "user_grade"));  //班级
        map.put("trScene", SharedPreUtils.getString(this, "user_sceneid"));  //场次
        map.put("trUsernum", SharedPreUtils.getString(this, "user_numberplate"));//桌牌号

        user_name_tv.setText("姓名:" + SharedPreUtils.getString(this, "user_name"));
        Log.i("jtest", "getUserRecored: 请求成绩列表前获取sp  trClass=" + SharedPreUtils.getString(this, "user_grade") + "trUsernum=" + SharedPreUtils.getString(this, "user_numberplate") + "   trScene=" + SharedPreUtils.getString(this, "user_sceneid"));

        Intent intent = new Intent(this, ExamHpptService.class);
        HttpParametersBean httpParametersBean = new HttpParametersBean();
        httpParametersBean.setUrl(AnswerConstants.GET_RECORDE);
        httpParametersBean.setMap(map);
        httpParametersBean.setType(1);
        httpParametersBean.setName("AnswerRecordActivity_GetRecorde");
        //obtain.obj = httpParametersBean;
        intent.putExtra("http_msg", httpParametersBean);
        showProgressDialogCancelable(this, "正在获取考试记录请稍等...");
        startService(intent);


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
            case R.id.btn_answer_record_1_retry:
                getUserRecored();
                break;

            case R.id.answer_record_btn:
                //准备清空Activity栈
                Intent intent = new Intent(AnswerRecordActivity1.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                //发送广播到服务 通知服务 关闭WebSocket连接
                Message obtain = Message.obtain();
                obtain.what = 9;
                sendMssage(obtain);


                String ip = SharedPreUtils.getString(AnswerRecordActivity1.this, "ip");
                //修改用户登录状态
                //setUserLandingState(false);


                //清空当前用户的SP存值
                SharedPreUtils.cleardata(AnswerRecordActivity1.this);
                SharedPreUtils.put(AnswerRecordActivity1.this, "ip", ip);


                //截图并保存到指定文件  通知相册更新
                ScreenCaptureUtil screenCaptureUtil = new ScreenCaptureUtil();
                Bitmap bitmap = screenCaptureUtil.captureScreen(AnswerRecordActivity1.this);
                screenCaptureUtil.saveImageToGallery(AnswerRecordActivity1.this, bitmap);


                //跳转Activity并清空清空Activity栈栈
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private int flagNum = 0;

    @Override
    public void onHttpServiceResultListener(HttpParametersBean httpParametersBean) {
        if (httpParametersBean != null) {
            if (httpParametersBean.getName().equals("WaitActivity_SetUserStatus")) {
                if (httpParametersBean.getStatus() == 0) {
                    Log.i("jtest", "ConfirmActivity:success: 用户状态更更改为未登录");

                } else if (httpParametersBean.getStatus() == 1) {
                    Log.i("jtest", "ConfirmActivity:success: 用户登录状态更改失败");
                }
            } else if (httpParametersBean.getName().equals("AnswerRecordActivity_GetRecorde")) {
                if (httpParametersBean.getStatus() == 0) {
                    i("jhk", httpParametersBean.getResultValue());
                    Log.i("jtest", "AnswerRecordActivity:success: 获取用户成绩列表" + httpParametersBean.getResultValue());
                    if (!TextUtils.isEmpty(httpParametersBean.getResultValue())) {
                        List<UserRecoderBean.TestrecordesBean> testrecordes = new Gson().fromJson(httpParametersBean.getResultValue(), UserRecoderBean.class).getTestrecordes();
                        if (testrecordes.size() > 0) {
                            //获取成绩列表 赋值recyclerview 数据
                            initScoreInfo();
                            initRecyclerView(testrecordes);
                            //右边的成绩信息的初复赋值
                            // AnswerNum = testrecordes.size();
                        }
                    }

                } else if (httpParametersBean.getStatus() == 1) {
                    mBtn_answer_record_1_retry.setVisibility(View.VISIBLE);
                    dismissProgressDialog();
                    Log.i("jtest", "AnswerRecordActivity:fail: 获取用户成绩列表" + httpParametersBean.getResultValue());
                    Toast.makeText(AnswerRecordActivity1.this, "获取成绩列表失败,请联系管理员!", Toast.LENGTH_SHORT).show();
                    long timetamp = System.currentTimeMillis();
                    String time = format.format(new Date());
                    File mFile4 = new File(getExternalCacheDir().getAbsolutePath() + "/crash/getUserRecored/" + time + "_" + timetamp + "_log.txt");
                    addTxtToFileBuffered(mFile4, "失败信息=" + httpParametersBean.getResultValue(), "data");
                    initScoreInfo();
                }
            } else if (httpParametersBean.getName().equals("AnswerRecordActivity_GetUserStandingList")) {
                if (httpParametersBean.getStatus() == 0) {
                    Log.i("jtest", "AnswerRecordActivity:success: 获取用户成绩结果" + httpParametersBean.getResultValue());

                    if (!TextUtils.isEmpty(httpParametersBean.getResultValue())) {
                        UserStandingListBean userStandingListBean = new Gson().fromJson(httpParametersBean.getResultValue(), UserStandingListBean.class);
                        List<UserStandingListBean.RankingListBean> rankingList = userStandingListBean.getRankingList();
                        for (int i = 0; i < rankingList.size(); i++) {
                            if (String.valueOf(rankingList.get(i).getTrUsernum()).equals(SharedPreUtils.getString(AnswerRecordActivity1.this, "user_numberplate"))) {
                                int timesum = rankingList.get(i).getTimesum();//时间
                                int marksum = rankingList.get(i).getMarksum();//成绩
                                setScore(marksum);
                                time_tv.setText("总共用时:" + stampToDate(timesum) + "");
                                // mtvAccuracy.setText("正确率:" + accuracyNum + "/" + "20");

                            }
                        }

                    }
                } else if (httpParametersBean.getStatus() == 1) {

                    mBtn_answer_record_1_retry.setVisibility(View.VISIBLE);
                    Log.i("jtest", "AnswerRecordActivity:fail: 获取用户成绩结果" + httpParametersBean.getResultValue());
                    Toast.makeText(AnswerRecordActivity1.this, "获取成绩结果失败,请联系管理员!", Toast.LENGTH_SHORT).show();
                    long timetamp = System.currentTimeMillis();
                    String time = format.format(new Date());
                    File mFile5 = new File(getExternalCacheDir().getAbsolutePath() + "/crash/initScoreInfo/" + time + "_" + timetamp + "_log.txt");
                    addTxtToFileBuffered(mFile5, "失败信息=" + httpParametersBean.getResultValue(), "data");
                }
                dismissProgressDialog();
            }
        }

    }

    private void setScore(int marksum) {
        String s1 = String.valueOf(marksum);
        char[] chars1 = s1.toCharArray();
     /*   ArrayList<Character> chars2 = new ArrayList<>();
        for (int i = 0; i < chars1.length; i++) {
            chars2.add(chars1[i]);
        }*/
        Log.i("hhh", "setScore: " + marksum + "  --" + chars1.toString());


        for (int i = 0; i < chars1.length; i++) {
            switch (i) {
                case 0:
                    setScore2(chars1[i], answer_record_score_hundreds);
                    Log.i("hhh", "setScore: " + 0 + "==" + chars1[i]);
                    break;
                case 1:
                    Log.i("hhh", "setScore: " + 1 + "==" + chars1[i]);
                    setScore2(chars1[i], answer_record_score_tens);
                    break;
                case 2:
                    Log.i("hhh", "setScore: " + 2 + "==" + chars1[i]);
                    setScore2(chars1[i], answer_record_score_ones);
                    break;
                default:
                    break;
            }
        }
    }

    private void setScore2(char s, ImageView answer_record_score_img) {
        switch (s) {
            case '0':
                answer_record_score_img.setImageResource(R.drawable.num_0);
                break;
            case '1':
                answer_record_score_img.setImageResource(R.drawable.num_1);
                break;
            case '2':
                answer_record_score_img.setImageResource(R.drawable.num_2);
                break;
            case '3':
                answer_record_score_img.setImageResource(R.drawable.num_3);
                break;
            case '4':
                answer_record_score_img.setImageResource(R.drawable.num_4);
                break;
            case '5':
                answer_record_score_img.setImageResource(R.drawable.num_5);
                break;
            case '6':
                answer_record_score_img.setImageResource(R.drawable.num_6);
                break;
            case '7':
                answer_record_score_img.setImageResource(R.drawable.num_7);
                break;
            case '8':
                answer_record_score_img.setImageResource(R.drawable.num_8);
                break;
            case '9':
                answer_record_score_img.setImageResource(R.drawable.num_9);
                break;
            default:
                break;
        }
    }


    private void initScoreInfo() {
        Intent intent = new Intent(this, ExamHpptService.class);
        HttpParametersBean httpParametersBean = new HttpParametersBean();
        httpParametersBean.setUrl(AnswerConstants.GET_USER_STANDING_LIST);
        httpParametersBean.setMap(null);
        httpParametersBean.setType(1);
        httpParametersBean.setName("AnswerRecordActivity_GetUserStandingList");
        //obtain.obj = httpParametersBean;
        intent.putExtra("http_msg", httpParametersBean);
        startService(intent);
    }

    private void initRecyclerView(List<UserRecoderBean.TestrecordesBean> testrecordes) {
        answerRecodeAdapter_0 = new AnswerRecodeAdapter_0(AnswerRecordActivity1.this);
        list_rev.setAdapter(answerRecodeAdapter_0);
        list_rev.setLayoutManager(new LinearLayoutManager(AnswerRecordActivity1.this));
        answerRecodeAdapter_0.setmList(testrecordes);
    }

    /**
     * 使用BufferedWriter进行文本内容的追加
     *
     * @param file
     * @param content
     */
    private void addTxtToFileBuffered(File file, String content, String logMsg) {
        //在文本文本中追加内容

        if (!file.exists()) {
            try {
                if (file.getParentFile() != null && !file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        BufferedWriter out = null;
        try {
            //FileOutputStream(file, true),第二个参数为true是追加内容，false是覆盖
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
            out.newLine();//换行
            out.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Log.i("jtest", "addTxtToFileBuffered: " + logMsg + "写入完成!");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }

    }


    public static void i(String tag, String msg) {  //信息太长,分段打印
        //因为String的length是字符数量不是字节数量所以为了防止中文字符过多，
        //  把4*1024的MAX字节打印长度改为2001字符数
        int max_str_length = 2001 - tag.length();
        //大于4000时
        while (msg.length() > max_str_length) {
            Log.i(tag, msg.substring(0, max_str_length));
            msg = msg.substring(max_str_length);
        }
        //剩余部分
        Log.i(tag, msg);
    }
}
