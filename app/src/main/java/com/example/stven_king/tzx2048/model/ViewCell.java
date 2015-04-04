package com.example.stven_king.tzx2048.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.stven_king.tzx2048.R;

/**
 * Created by STVEN_KING on 2015/1/15.
 */
public class ViewCell extends View{

    private static final int DRAW_TIME = 5;

    public ViewCellListener mViewCellListener;

    public void setViewCellListener(ViewCellListener listener){
        mViewCellListener = listener;
    };
    //回掉接口
    public interface ViewCellListener{
        public void onFinished();
    }

    private RectF r = new RectF();
    //纪录当前View的数字：0表示没有数字
    private int number = 0;
    private Canvas canvas =new Canvas();
    private Paint paint = new Paint();

    //当前所处的位置
    private int[] location = new  int[2] ;

    /**View绘制的XY坐标**/
    private int mPosX =0;
    private int mPosY =0;

    /**View绘制的宽高**/
    private int mWidth =0;
    private int mHeight =0;

    private enum DrawKinds {
        no,qidao,jiaohuan,chuizi,fanhui
    }

    private DrawKinds drawKinds;

    private int chuiziHeight = 0;
    private int qidaoHeight = 0;

    private boolean isDrawProp;

    public ViewCell(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public ViewCell(Context context,AttributeSet attr) {
        // TODO Auto-generated constructor stub
        super(context,attr);
    }

    public ViewCell(Context context,int iwidth,int iheight){
        super(context);
        setLayoutParams(new LinearLayout.LayoutParams(iwidth, iheight));
        mHeight=iheight;
        mWidth=iwidth;
        r.set(0, 0, this.getWidth(), this.getHeight());//矩形的坐标设置为制定的值
        r.inset(10, 10);//插图的矩形(dx,dy)。
        drawKinds = DrawKinds.no;
        isDrawProp = false;
    }

    public void setNumber(int number){
        onDrawNum(number);
        this.number=number;
    }

    public int getNumber(){
        return number;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(!isDrawProp){
            super.onDraw(canvas);
            this.canvas=canvas;
            onDrawNum(number);
        } else {
            float heightScale;
            float widthScale;
            Matrix matrix;
            switch (drawKinds){
                case qidao:
                    Bitmap qidao = getBitmap(number/2);
                    // 计算缩放因子
                    heightScale =  mHeight / ((float) qidao.getHeight());
                    widthScale = mWidth / ((float) qidao.getWidth());
                    // 新建立矩阵
                    matrix = new Matrix();
                    matrix.postScale(heightScale, widthScale);
                    Bitmap qidao1 = Bitmap.createBitmap(qidao,0,0,qidao.getWidth(),qidao.getHeight(),matrix,true);
                    Bitmap qidao2 = Bitmap.createBitmap(qidao1,0,mHeight-qidaoHeight,mWidth,qidaoHeight);
                    canvas.drawBitmap(qidao2,0,0,null);
                    if((qidaoHeight-=DRAW_TIME) <= DRAW_TIME){
                        isDrawProp = false;
                        setNumber(number);
                        if(mViewCellListener != null){
                            mViewCellListener.onFinished();
                        }
                    }
                    break;
                case jiaohuan:
                    break;
                case chuizi:
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.a);
                    // 计算缩放因子
                    heightScale =  mHeight / ((float) bitmap.getHeight());
                    widthScale = mWidth / ((float) bitmap.getWidth());
                    // 新建立矩阵
                    matrix = new Matrix();
                    matrix.postScale(heightScale, widthScale);
                    Bitmap bitmap1 = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
                    Bitmap bitmap2 = Bitmap.createBitmap(bitmap1,0,0,mWidth,chuiziHeight);
                    canvas.drawBitmap(bitmap2,0,0,null);
                    if((chuiziHeight+=DRAW_TIME) > mHeight-DRAW_TIME){
                        isDrawProp = false;
                        setNumber(0);
                        if(mViewCellListener != null){
                            mViewCellListener.onFinished();
                        }
                    }

                    break;
                case fanhui:
                    //TODO 目前操作没有实现
                    break;
            }
            invalidate();
        }
    }

    public void setIsDraw(boolean isDrawProp) {
        this.isDrawProp = isDrawProp;
    }

    public boolean getIsDraw(){ return isDrawProp; }

    /**
     *
     * 设置边框(白色背景)
     * */
    private void onDrawBorder(Canvas canvas) {
        paint.setShader(null);//设置或清除着色器对象。
        paint.setStrokeWidth(16);//该方法用于设置画笔的空心线宽。该方法在矩形、圆形等图形上有明显的效果。
        paint.setColor(Color.GRAY);
        //paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawLine(0, 0, 0, this.getHeight(), paint);
        canvas.drawLine(0, 0, this.getWidth(), 0, paint);
        canvas.drawLine(this.getWidth(), 0, this.getWidth(), this.getHeight(), paint);
        canvas.drawLine(0, this.getHeight(), this.getWidth(), this.getHeight(), paint);
    }

    /**
     * 判断是否点中图片按钮
     * @param x
     * @param y
     */
    public boolean IsClick(int x, int y) {
        boolean isClick = false;
        getLocationOnScreen(location);
        mPosX=location[0];
        mPosY=location[1];
        if (x >= mPosX && x <= mPosX + mWidth && y >= mPosY&& y <= mPosY + mHeight) {
            isClick = true;
        }
        return isClick;
    }

    /**
     *
     * 画背景和数字
     * */
    public void onDrawNum(int number){

        String bgcolor;
        switch(number){
            case 0:
                bgcolor="#CCC0B3";
                this.setBackgroundResource(R.drawable.a);
                break;
            case 2:
                bgcolor="#EEE4DA";
                this.setBackgroundResource(R.drawable.b);
                break;
            case 4:
                bgcolor="#EDE0C8";
                this.setBackgroundResource(R.drawable.c);
                break;
            case 8:
                bgcolor="#F2B179";
                this.setBackgroundResource(R.drawable.d);
                break;
            case 16:
                bgcolor="#F49563";
                this.setBackgroundResource(R.drawable.e);
                break;
            case 32:
                bgcolor="#F5794D";
                this.setBackgroundResource(R.drawable.f);
                break;
            case 64:
                bgcolor="#F55D37";
                this.setBackgroundResource(R.drawable.g);
                break;
            case 128:
                bgcolor="#EEE863";
                this.setBackgroundResource(R.drawable.h);
                break;
            case 256:
                bgcolor="#EDB04D";
                this.setBackgroundResource(R.drawable.j);
                break;
            case 512:
                bgcolor="#ECB04D";
                this.setBackgroundResource(R.drawable.k);
                break;
            case 1024:
                bgcolor="#EB9437";
                this.setBackgroundResource(R.drawable.l);
                break;
            case 2048:
                bgcolor="#EA7821";
                this.setBackgroundResource(R.drawable.m);
                break;
            default:
                bgcolor="#EA7821";
                //this.setBackgroundResource(R.drawable.a);
                break;
        }

    }

    /**
     * 对方块使用锤子
     * */
    public void UseQiDao(){
        Log.v("ViewCell","UserQidao");
        setNumber(number * 2);
        drawKinds = DrawKinds.qidao;
        isDrawProp = true;
        qidaoHeight = mHeight - 4;
        invalidate();

    }

    /**
     * 对方块使用锤子
     * */
    public void UseChuiZi(){
        Log.v("ViewCall", "UserChuiZi");
//        Canvas canvas;
//        Paint p=new Paint();
//        p.setColor(Color.rgb(239, 228, 176));
        drawKinds = DrawKinds.chuizi;
        isDrawProp = true;
        chuiziHeight = 4;
        invalidate();

    }

    private Bitmap getBitmap(int number){
        Bitmap bitmap;
        switch (number){
            case 2:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.b);
                break;
            case 4:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.c);
                break;
            case 8:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.d);
                break;
            case 16:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.e);
                break;
            case 32:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.f);
                break;
            case 64:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.g);
                break;
            case 128:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.h);
                break;
            case 256:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.j);
                break;
            case 512:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.k);
                break;
            case 1024:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.l);
                break;
            case 2048:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.m);
                break;
            default:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.a);

        }
        return bitmap;
    }
}
