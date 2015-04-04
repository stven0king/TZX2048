package com.example.stven_king.tzx2048.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.stven_king.tzx2048.R;
import com.example.stven_king.tzx2048.utils.SignInRecord;

/**
 * Created by STVEN_KING on 2015/1/15.
 * TODO欢迎页面
 */
public class WelcomeActivity extends Activity {
    private static final int MESSAGE = 111;
    private static final int TIME = 5000;
    private ImageButton enter;
    private ImageView m2048;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("WelcomeActivity","onCreate");
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.welcome);
        //检查是否需要签到
        check();
        enter = (ImageButton) findViewById(R.id.enter);
        m2048 = (ImageView) findViewById(R.id.m2048);
        //3秒之后执行
        myhHandler.sendEmptyMessageDelayed(MESSAGE, TIME);
        //点击按钮进入主Activity
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                enter.setClickable(false);
                Intent intent = new Intent();
                if (SignInRecord.getInstance(WelcomeActivity.this).getIsorNo_Record()){
                    intent.setClass(WelcomeActivity.this, MainActivity.class);
                } else {
                    intent.setClass(WelcomeActivity.this, RegistrationActivity.class);
                }
                startActivity(intent);
                finish();
                onDestroy();
            }
        });
    }

    /**
     * 处理页面跳转到主Activity
     * */
    public Handler myhHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what) {
                case MESSAGE:
                    Intent intent = new Intent();
                    if (SignInRecord.getInstance(WelcomeActivity.this).getIsorNo_Record()){
                        intent.setClass(WelcomeActivity.this, MainActivity.class);
                    } else {
                        intent.setClass(WelcomeActivity.this, RegistrationActivity.class);
                    }
                    startActivity(intent);
                    finish();
                    onDestroy();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 检查当天是否签到
     * */
    public void check(){
        Long record_s[] = new Long[7];
        SignInRecord signInRecord = SignInRecord.getInstance(this);
        record_s[0]=signInRecord.getOneDay();
        record_s[1]=signInRecord.getTwoDay();
        record_s[2]=signInRecord.getThreeDay();
        record_s[3]=signInRecord.getFourDay();
        record_s[4]=signInRecord.getFiveDay();
        record_s[5]=signInRecord.getSixDay();
        record_s[6]=signInRecord.getSevenDay();
        if (record_s[0]==0){
            signInRecord.setIsorNo_Record(false);
            return;
        }
        for(int i=0;i<7;i++){
            if (record_s[i]==0){
                if (RegistrationActivity.isSame(record_s[i-1])){
                    //同一天登陆
                    signInRecord.setIsorNo_Record(true);
                    //第二天登陆
                } else if (RegistrationActivity.isNext(record_s[i-1])){
                    signInRecord.setIsorNo_Record(false);
                } else {
                    //不是两天连续登陆
                    signInRecord.setIsorNo_Record(false);
                    signInRecord.setOneDay((long) 0);
                    signInRecord.setTwoDay((long) 0);
                    signInRecord.setThreeDay((long) 0);
                    signInRecord.setFourDay((long) 0);
                    signInRecord.setFiveDay((long) 0);
                    signInRecord.setSixDay((long) 0);
                    signInRecord.setSevenDay((long) 0);

                }

                break;
            }
        }
    }

    /**
     * TODO动画处理
     * */
    public void AnimationWelcome(){
        AnimationSet set = new AnimationSet(true);//定义一个动画
        RotateAnimation rotate = new RotateAnimation(0, 360, //旋转角度
                Animation.RELATIVE_TO_PARENT, 0.5f, //X半轴为半个屏幕的宽度
                Animation.RELATIVE_TO_PARENT, 0.0f);	//Y半轴从原点计算
        rotate.setDuration(3000);	//3秒完成动画
        set.addAnimation(rotate);	//增加动画
        this.m2048.startAnimation(set); 	//启动动画
    }
}
