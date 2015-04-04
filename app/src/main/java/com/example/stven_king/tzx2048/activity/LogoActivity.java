package com.example.stven_king.tzx2048.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.WindowManager;

import com.example.stven_king.tzx2048.utils.CartoonView;

/**
 * Created by STVEN_KING on 2015/1/15.
 * TODO开机cg动画
 */
public class LogoActivity extends Activity{
    private static final int MESSAGE = 333;
    private static final int TIME = 5000;
    private CartoonView myView;

    //显示屏幕的宽高
    private int width;
    private int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display dp = wm.getDefaultDisplay();

        //获取屏幕的宽和高
        width = dp.getWidth();
        height = dp.getHeight();

        myView=new CartoonView(this,width,height);
        setContentView(myView);
        myhHandler.sendEmptyMessageDelayed(MESSAGE, TIME);
    }

    /**
     * TODO处理时间句柄
     * */
    Handler myhHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what) {
                case MESSAGE:
                    Intent intent = new Intent();
                    intent.setClass(LogoActivity.this, WelcomeActivity.class);

                    startActivity(intent);
                    finish();
                    onDestroy();
                    break;
                default:
                    break;
            }
        }
    };
}
