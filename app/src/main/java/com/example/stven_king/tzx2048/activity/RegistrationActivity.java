package com.example.stven_king.tzx2048.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.stven_king.tzx2048.R;
import com.example.stven_king.tzx2048.utils.SignInRecord;
import com.example.stven_king.tzx2048.utils.BestScode;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by STVEN_KING on 2015/1/16.
 * TODO签到获取金币页面
 */
public class RegistrationActivity extends Activity implements View.OnClickListener{
    private static final int MESSAGE = 222;
    private static final int TIEM = 1000;
    private ImageButton[] imageButton = new ImageButton[7];
    private TextView golds_sum;
    private SignInRecord record;
    private BestScode bestScode;
    private Long record_s[] = new Long[7];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        golds_sum = (TextView) findViewById(R.id.golds_sum);

        golds_sum.setTypeface(Typeface.createFromAsset(getAssets(), "font/Non_mainstream_digital_fonts.ttf"));
//        golds_sum.setTypeface(Typeface.createFromAsset(getAssets(),"font/Bank_card_number_font.ttf"));
        imageButton[0] = (ImageButton) findViewById(R.id.ibtn1);
        imageButton[1] = (ImageButton) findViewById(R.id.ibtn2);
        imageButton[2] = (ImageButton) findViewById(R.id.ibtn3);
        imageButton[3] = (ImageButton) findViewById(R.id.ibtn4);
        imageButton[4] = (ImageButton) findViewById(R.id.ibtn5);
        imageButton[5] = (ImageButton) findViewById(R.id.ibtn6);
        imageButton[6] = (ImageButton) findViewById(R.id.ibtn7);

        record = SignInRecord.getInstance(this);
        bestScode = BestScode.getInstance(this);

        //数据初始化
        for (int i=0;i<7;i++) {
            record_s[i] = Long.valueOf(0);
        }

        //获取签到记录
        if(record.getOneDay()!=0){
            imageButton[0].setBackgroundResource(R.drawable.g1);
            record_s[0]=record.getOneDay();
        }
        if(record.getTwoDay()!=0){
            imageButton[1].setBackgroundResource(R.drawable.g2);
            record_s[1]=record.getTwoDay();
        }
        if(record.getThreeDay()!=0){
            imageButton[2].setBackgroundResource(R.drawable.g3);
            record_s[2]=record.getThreeDay();
        }
        if(record.getFourDay()!=0){
            imageButton[3].setBackgroundResource(R.drawable.g4);
            record_s[3]=record.getFourDay();
        }
        if(record.getFiveDay()!=0){
            imageButton[4].setBackgroundResource(R.drawable.g5);
            record_s[4]=record.getFiveDay();
        }
        if(record.getSixDay()!=0){
            imageButton[5].setBackgroundResource(R.drawable.g6);
            record_s[5]=record.getSixDay();
        }
        if(record.getSevenDay()!=0){
            imageButton[6].setBackgroundResource(R.drawable.g7);
            record_s[6]=record.getSevenDay();
        }

        imageButton[0].setClickable(false);
        imageButton[1].setClickable(false);
        imageButton[2].setClickable(false);
        imageButton[3].setClickable(false);
        imageButton[4].setClickable(false);
        imageButton[5].setClickable(false);
        imageButton[6].setClickable(false);

        //是否刷新签到
        if (record_s[6] !=0 || record_s[0] == 0){
            refresh();
        } else {
            for (int i = 1; i < 7; i++) {
                if (record_s[i] == 0){
                    if (isNext(record_s[i-1])) {
                        imageButton[i].setClickable(true);
                    } else {
                        //imageButton[0].setClickable(true);
                        //if (i >= 1){
                        //    imageButton[0].setBackgroundResource(R.drawable.g10);
                        //}
                        //if (i >= 2){
                        //    imageButton[1].setBackgroundResource(R.drawable.g20);
                        //}
                        //if (i >= 3){
                        //    imageButton[2].setBackgroundResource(R.drawable.g30);
                        //}
                        //if (i >= 4){
                        //    imageButton[3].setBackgroundResource(R.drawable.g40);
                        //}
                        //if (i >= 5){
                        //    imageButton[4].setBackgroundResource(R.drawable.g50);
                        //}
                        //if (i >= 6){
                        //    imageButton[5].setBackgroundResource(R.drawable.g60);
                        //}
                        if (!isSame(record_s[i-1])){
                            refresh();
                        }
                    }
                    break;
                }
            }
        }
        golds_sum.setText(String.valueOf(bestScode.getGolds()));
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
                    SignInRecord.getInstance(RegistrationActivity.this).setIsorNo_Record(true);
                    intent.setClass(RegistrationActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    onDestroy();
                    break;
                default:
                    break;
            }
        }
    };



    @Override
    public void onClick(View v) {
        Log.v("RegistrationActivity","onClick");
        switch (v.getId()){
            case R.id.ibtn1:
                imageButton[0].setBackgroundResource(R.drawable.g1);
                imageButton[0].setClickable(false);
                bestScode.addGolds(10);
                record.setOneDay(System.currentTimeMillis());
                break;
            case R.id.ibtn2:
                imageButton[1].setBackgroundResource(R.drawable.g2);
                imageButton[1].setClickable(false);
                bestScode.addGolds(20);
                record.setTwoDay(System.currentTimeMillis());
                break;
            case R.id.ibtn3:
                imageButton[2].setBackgroundResource(R.drawable.g3);
                imageButton[2].setClickable(false);
                bestScode.addGolds(30);
                record.setThreeDay(System.currentTimeMillis());
                break;
            case R.id.ibtn4:
                imageButton[3].setBackgroundResource(R.drawable.g4);
                imageButton[3].setClickable(false);
                bestScode.addGolds(40);
                record.setFourDay(System.currentTimeMillis());
                break;
            case R.id.ibtn5:
                imageButton[4].setBackgroundResource(R.drawable.g5);
                imageButton[4].setClickable(false);
                bestScode.addGolds(50);
                record.setFiveDay(System.currentTimeMillis());
                break;
            case R.id.ibtn6:
                imageButton[5].setBackgroundResource(R.drawable.g6);
                imageButton[5].setClickable(false);
                bestScode.addGolds(60);
                record.setSixDay(System.currentTimeMillis());
                break;
            case R.id.ibtn7:
                imageButton[6].setBackgroundResource(R.drawable.g7);
                imageButton[6].setClickable(false);
                bestScode.addGolds(70);
                record.setSevenDay(System.currentTimeMillis());
        }
        golds_sum.setText(String.valueOf(bestScode.getGolds()));
        myhHandler.sendEmptyMessageDelayed(MESSAGE, TIEM);
    }
    /**
     * 刷新签到
     * */
    public void refresh(){
        imageButton[0].setClickable(true);
        record.setOneDay((long) 0);
        record.setTwoDay((long) 0);
        record.setThreeDay((long) 0);
        record.setFourDay((long) 0);
        record.setFiveDay((long) 0);
        record.setSixDay((long) 0);
        record.setSevenDay((long) 0);
        imageButton[0].setBackgroundResource(R.drawable.g10);
        imageButton[1].setBackgroundResource(R.drawable.g20);
        imageButton[2].setBackgroundResource(R.drawable.g30);
        imageButton[3].setBackgroundResource(R.drawable.g40);
        imageButton[4].setBackgroundResource(R.drawable.g50);
        imageButton[5].setBackgroundResource(R.drawable.g60);
        imageButton[6].setBackgroundResource(R.drawable.g70);
    }

    /**
     * TODO测当前登录时间是否为给定参数时间的第二天
     * */
    public static boolean isNext(Long n){
        long time = System.currentTimeMillis();
        String nowDate= String.valueOf(time);
        String lastDate = String.valueOf(n);
        Date date = new Date(Long.parseLong(nowDate));
        Date lastdate = new Date(Long.parseLong(lastDate));
        if((date.getDate() - lastdate.getDate() == 1) &&
                date.getYear() == lastdate.getYear() &&
                date.getMonth() == lastdate.getMonth()){
            return true;
        }
        return false;
    }

    /**
     * TODO测当前登录时间是否为给定参数时间的同一天
     * */
    public static boolean isSame(Long n){
        long time = System.currentTimeMillis();
        String nowDate= String.valueOf(time);
        String lastDate = String.valueOf(n);
        Date date = new Date(Long.parseLong(nowDate));
        Date lastdate = new Date(Long.parseLong(lastDate));
        if(date.getDate() == lastdate.getDate() &&
                date.getYear() == lastdate.getYear() &&
                date.getMonth() == lastdate.getMonth()){
            return true;
        }
        return false;
    }
}
