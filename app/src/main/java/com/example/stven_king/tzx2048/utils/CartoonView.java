package com.example.stven_king.tzx2048.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import com.example.stven_king.tzx2048.R;

/**
 * Created by STVEN_KING on 2015/1/15.
 */
public class CartoonView extends View{

    private static int WINDOW_SHADES_LENGTH = 80;
    private static int STEP_LENGTH = 1;

    Bitmap bp204,b8;

    float x,y,x1,y1,y2,y3,xx,yy;
    long t1,t2;

    //每个黑色条的宽度
    private int width_n=WINDOW_SHADES_LENGTH;
    private int height_n=WINDOW_SHADES_LENGTH;

    //黑色条数
    //private static int number=5;
    private int number_w=10;
    private int number_h=15;

    //显示屏幕的宽高
    private int width=0;
    private int height=0;

    public CartoonView(Context context) {
        super(context);
    }

    /**
     * TODO初始化View
     * */
    public CartoonView(Context context, int iwidth, int iheight){
        super(context);
        height=iheight;
        width=iwidth;
        bp204= BitmapFactory.decodeResource(getResources(), R.drawable.p204);
        b8=BitmapFactory.decodeResource(getResources(), R.drawable.p8);
        x=-300;
        y=height/2-100;
        x1=0;
        y1=-100;
        y2=0;
        y3=height;
        //width_n = width/2/number;
        number_w = width/width_n;
        number_h = height/height_n;
        xx=yy=0;
        setBackgroundResource(R.drawable.qishi);
        t1=System.currentTimeMillis();
    }

    public CartoonView(Context context, AttributeSet attrs){
        super(context,attrs);
    }

    public CartoonView(Context context, AttributeSet attrs, int defStyle){
        super(context,attrs,defStyle);
    }

    private WindowManager getSystemService(String windowService) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     *
     * TODO进行动画
     * */
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        Paint p=new Paint();
        p.setColor(Color.BLACK);
        canvas.drawBitmap(bp204, x, y, p);
        canvas.drawBitmap(b8, x1, y1, p);
        t2=System.currentTimeMillis();
        if(x<width/2-130)
            x+=3;
        if(y1<height/2-100)
            y1+=3;
        if(x1<width/2+20)
            x1+=3;
        if(t2-t1>3500) {
            p.setColor(Color.BLACK);
			/*//交叉相合动画
			for(int i=0;i<number;i++){
				canvas.drawRect(i*2*width_n, 0, i*2*width_n+width_n,y2, p);
				canvas.drawRect(i*2*width_n+width_n, y3, (i+1)*2*width_n,height, p);
			}
			y3-=12;
			y2+=12;*/

            //百叶窗合并动画
            for(int i=0;i<number_w;i++){
                canvas.drawRect(i*width_n, 0, i*width_n+xx,height, p);
            }
            for(int i=0;i<number_h;i++){
                canvas.drawRect(0, i*height_n, width,i*height_n+yy, p);
            }
            xx+=STEP_LENGTH;
            yy+=STEP_LENGTH;

        }
        invalidate();
    }
}
