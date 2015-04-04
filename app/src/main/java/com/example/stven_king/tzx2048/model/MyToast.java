package com.example.stven_king.tzx2048.model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.stven_king.tzx2048.R;

/**
 * Created by Administrator on 2015/2/9.
 */
public class MyToast {
    private final Handler handler = new Handler();
    //显示时间
    private int duration = DEFAULT_DURATION;
    private int gravity = Gravity.CENTER;

    //当前显示、下个显示
    private View view;
    private View nextview;

    private WindowManager windowManager;
    private static Context context;
    private final WindowManager.LayoutParams params = new WindowManager.LayoutParams();

    //提示类型：失败、成功
    public static final int TYPE_SUCCESS = 1;
    public static final int TYPE_FAIL = 2;

    //显示时间
    public static final int DEFAULT_DURATION = 2000;


    public MyToast(Context context){
        init(context);
    }
    private void init(Context context){
        if (context == null){
            return ;
        }
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
        params.windowAnimations = android.R.style.Animation_Toast;
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.setTitle("Toast");
        windowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
    }
    public static MyToast maketext(Context context,CharSequence text,int type){
        MyToast.context = context;
        return maketext(context,text,DEFAULT_DURATION,type);
    }
    public static MyToast maketext(final Context context,CharSequence text,int duration, int type){
        MyToast.context = context;
        MyToast showdoor = new MyToast(context);
        if (context == null){
            return showdoor;
        }
        //获取屏幕信息
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        //padding==屏幕分辨率*10
        int padding = (int) (displayMetrics.density * 10);
        LinearLayout linearLayout =new LinearLayout(context);
        //设置整个textview居中
        linearLayout.setGravity(Gravity.CENTER);
        //设置弹出Toast的时候界面为透明
        linearLayout.setBackgroundColor(Color.parseColor("#66000000"));
//        linearLayout.setPadding(20,10,20,10);
        TextView showview = new TextView(context);
        showview.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.common_custom_toast_bg));
        //设置textView的最小宽度为屏幕分辨率*200
        showview.setMinWidth((int) (displayMetrics.density * 200));
        //设置textview中的字居中显示
        showview.setGravity(Gravity.CENTER);
        //设置textView的最大宽度为屏幕的宽减去屏幕分辨率*100
        showview.setMaxWidth((int) (displayMetrics.widthPixels - displayMetrics.density * 100));
        //设置TextView的内边距
        showview.setPadding(padding,padding*2,padding,padding*2);
        //设置TextView的字体颜色
        showview.setTextColor(context.getResources().getColor(R.color.common_custom_toast_text_color));
        //设置textView的字体大小
        showview.setTextSize(16);
        if (type == TYPE_SUCCESS){
            showview.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.common_toast_success, 0, 0);
            showview.setCompoundDrawablePadding(20);
        } else if (type == TYPE_FAIL){
            showview.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.common_toast_failture, 0, 0);
            showview.setCompoundDrawablePadding(20);
        }
        //设置自定义的字体
        showview.setTypeface(Typeface.createFromAsset(context.getAssets(), "font/chinese.ttf"));
        showview.setTextSize(20.0f);
        showview.setText(text);
        linearLayout.addView(showview);
        showdoor.nextview = linearLayout;
        showdoor.duration = duration;
        return showdoor;
    }
    public void show(){

        handler.post(mShow);
        if (duration > 0){
            handler.postDelayed(mHide,duration);
        }
    }

    private void showToast(){
        if (windowManager == null){
            return;
        }
        if (view != nextview){
            hideToast();
            view = nextview;
            params.gravity = gravity;
            if (view.getParent() != null){
                windowManager.removeView(view);
            }
            windowManager.addView(view,params);
        }
    }
    private void hideToast(){
        if (windowManager == null){
            return ;
        }
        if (view != null){
            if (view.getParent() != null){
                windowManager.removeView(view);
            }
            view = null;
        }
    }
    public void hide() {

        handler.post(mHide);
    }

    private final Runnable mShow = new Runnable() {

        public void run() {

            showToast();
        }
    };

    private final Runnable mHide = new Runnable() {

        public void run() {

            hideToast();
        }
    };

}
