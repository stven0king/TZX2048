package com.example.stven_king.tzx2048.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.stven_king.tzx2048.R;
import com.example.stven_king.tzx2048.dialog.effects.BaseEffects;

public class NiftyDialogBuilder extends Dialog implements DialogInterface {
    
    private final String defTextColor="#FFFFFFFF";
    
    private final String defDividerColor="#11000000";
    
    private final String defMsgColor="#FFFFFFFF";
    
    private final String defDialogColor="#FFE74C3C";


    private static Context tmpContext;
    
    
    private Effectstype type=null;
    
    private LinearLayout mLinearLayoutView;
    
    private RelativeLayout mRelativeLayoutView;
    
    private LinearLayout mLinearLayoutMsgView;
    
    private LinearLayout mLinearLayoutTopView;
    
    private FrameLayout mFrameLayoutCustomView;
    
    private View mDialogView;
    
    private View mDivider;
    
    private TextView mTitle;
    
    private TextView mMessage;
    
    private ImageView mIcon;
    
    private Button ok_botton;
    
    private Button cancel_botton;
    
    private int mDuration = -1;
    
    private static  int mOrientation=1;
    
    private boolean isCancelable=true;
    
    private static NiftyDialogBuilder instance;
    
    public NiftyDialogBuilder(Context context) {
        super(context,R.style.Transparent);
        init(context);
        
    }
    public NiftyDialogBuilder(Context context,int theme) {
        super(context, theme);
        init(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.width  = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes((WindowManager.LayoutParams) params);
        
    }
    
    public static NiftyDialogBuilder getInstance(Context context) {

        if (instance == null || !tmpContext.equals(context)) {
            synchronized (NiftyDialogBuilder.class) {
                if (instance == null || !tmpContext.equals(context)) {
                    instance = new NiftyDialogBuilder(context,R.style.dialog_untran);
                }
            }
        }
        tmpContext = context;
        return instance;

    }
    
    private void init(Context context) {
        
        mDialogView = View.inflate(context, R.layout.dialog_layout, null);
        
        mLinearLayoutView=(LinearLayout)mDialogView.findViewById(R.id.parentPanel);
        mRelativeLayoutView=(RelativeLayout)mDialogView.findViewById(R.id.main);
        mLinearLayoutTopView=(LinearLayout)mDialogView.findViewById(R.id.topPanel);
        mLinearLayoutMsgView=(LinearLayout)mDialogView.findViewById(R.id.contentPanel);
        mFrameLayoutCustomView=(FrameLayout)mDialogView.findViewById(R.id.customPanel);
        
        mTitle = (TextView) mDialogView.findViewById(R.id.alertTitle);
        mMessage = (TextView) mDialogView.findViewById(R.id.message);
        mIcon = (ImageView) mDialogView.findViewById(R.id.icon);
        mDivider = mDialogView.findViewById(R.id.titleDivider);
        ok_botton=(Button)mDialogView.findViewById(R.id.ok_botton);
        cancel_botton=(Button)mDialogView.findViewById(R.id.cancel_botton);
        
        setContentView(mDialogView);
        
        this.setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                
                mLinearLayoutView.setVisibility(View.VISIBLE);
                if(type==null){
                    type=Effectstype.Slidetop;
                }

                start(type);
                
                
            }
        });
        mRelativeLayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCancelable)dismiss();
            }
        });
    }
    
    public void toDefault(){
        mTitle.setTextColor(Color.parseColor(defTextColor));
        mDivider.setBackgroundColor(Color.parseColor(defDividerColor));
        mMessage.setTextColor(Color.parseColor(defMsgColor));
        mLinearLayoutView.setBackgroundColor(Color.parseColor(defDialogColor));
    }
    
    public NiftyDialogBuilder withDividerColor(String colorString) {
        mDivider.setBackgroundColor(Color.parseColor(colorString));
        return this;
    }
    public NiftyDialogBuilder withDividerColor(int color) {
        mDivider.setBackgroundColor(color);
        return this;
    }
    
    
    public NiftyDialogBuilder withTitle(CharSequence title) {
        toggleView(mLinearLayoutTopView,title);
        mTitle.setText(title);
        return this;
    }
    
    public NiftyDialogBuilder withTitleColor(String colorString) {
        mTitle.setTextColor(Color.parseColor(colorString));
        return this;
    }
    
    public NiftyDialogBuilder withTitleColor(int color) {
        mTitle.setTextColor(color);
        return this;
    }
    
    public NiftyDialogBuilder withMessage(int textResId) {
        toggleView(mLinearLayoutMsgView,textResId);
        mMessage.setText(textResId);
        return this;
    }
    
    public NiftyDialogBuilder withMessage(CharSequence msg) {
        toggleView(mLinearLayoutMsgView,msg);
        mMessage.setText(msg);
        return this;
    }
    public NiftyDialogBuilder withMessageColor(String colorString) {
        mMessage.setTextColor(Color.parseColor(colorString));
        return this;
    }
    public NiftyDialogBuilder withMessageColor(int color) {
        mMessage.setTextColor(color);
        return this;
    }
    
    public NiftyDialogBuilder withDialogColor(String colorString) {
        mLinearLayoutView.getBackground().setColorFilter(ColorUtils.getColorFilter(Color.parseColor(colorString)));
        return this;
    }
    
    public NiftyDialogBuilder withDialogColor(int color) {
        mLinearLayoutView.getBackground().setColorFilter(ColorUtils.getColorFilter(color));
        return this;
    }
    
    public NiftyDialogBuilder withIcon(int drawableResId) {
        mIcon.setImageResource(drawableResId);
        return this;
    }
    
    public NiftyDialogBuilder withIcon(Drawable icon) {
        mIcon.setImageDrawable(icon);
        return this;
    }
    
    public NiftyDialogBuilder withDuration(int duration) {
        this.mDuration=duration;
        return this;
    }
    
    public NiftyDialogBuilder withEffect(Effectstype type) {
        this.type=type;
        return this;
    }
    
    public NiftyDialogBuilder withButtonDrawable(int resid) {
        ok_botton.setBackgroundResource(resid);
        cancel_botton.setBackgroundResource(resid);
        return this;
    }
    public NiftyDialogBuilder withOk_ButtonText(CharSequence text) {
        ok_botton.setVisibility(View.VISIBLE);
        ok_botton.setText(text);
        
        return this;
    }
    public NiftyDialogBuilder withCancel_ButtonText(CharSequence text) {
        cancel_botton.setVisibility(View.VISIBLE);
        cancel_botton.setText(text);
        return this;
    }
    public NiftyDialogBuilder setOk_BottonClick(View.OnClickListener click) {
        ok_botton.setOnClickListener(click);
        return this;
    }
    
    public NiftyDialogBuilder setCancel_BottonClick(View.OnClickListener click) {
        cancel_botton.setOnClickListener(click);
        return this;
    }
    
    
    public NiftyDialogBuilder setCustomView(int resId, Context context) {
        View customView = View.inflate(context, resId, null);
        if (mFrameLayoutCustomView.getChildCount()>0){
            mFrameLayoutCustomView.removeAllViews();
        }
        mFrameLayoutCustomView.addView(customView);
        return this;
    }
    
    public NiftyDialogBuilder setCustomView(View view, Context context) {
        if (mFrameLayoutCustomView.getChildCount()>0){
            mFrameLayoutCustomView.removeAllViews();
        }
        mFrameLayoutCustomView.addView(view);
        
        return this;
    }
    public NiftyDialogBuilder isCancelableOnTouchOutside(boolean cancelable) {
        this.isCancelable=cancelable;
        this.setCanceledOnTouchOutside(cancelable);
        return this;
    }
    
    public NiftyDialogBuilder isCancelable(boolean cancelable) {
        this.isCancelable=cancelable;
        this.setCancelable(cancelable);
        return this;
    }
    
    private void toggleView(View view,Object obj){
        if (obj==null){
            view.setVisibility(View.GONE);
        }else {
            view.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void show() {
        super.show();
    }
    
    private void start(Effectstype type){
        BaseEffects animator = type.getAnimator();
        if(mDuration != -1){
            animator.setDuration(Math.abs(mDuration));
        }
        animator.start(mRelativeLayoutView);
    }
    
    @Override
    public void dismiss() {
        super.dismiss();
        ok_botton.setVisibility(View.GONE);
        cancel_botton.setVisibility(View.GONE);
    }
}
