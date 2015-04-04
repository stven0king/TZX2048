package com.example.stven_king.tzx2048.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.stven_king.tzx2048.bean.MyInfomations;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by STVEN_KING on 2015/1/19.
 */
public class SignInRecord {
    private SharedPreferences sp;
    private static SignInRecord sign_in_record;

    public SignInRecord(Context context){
        sp = context.getSharedPreferences(MyInfomations.SIGN_IN_RECORD, context.MODE_PRIVATE);
    }

    public static SignInRecord getInstance(Context context){
        if (sign_in_record == null){
            sign_in_record = new SignInRecord(context);
        }
        return sign_in_record;
    }

    public void setOneDay(Long b){
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(MyInfomations.ONE, b);
        editor.commit();
    }

    public Long getOneDay(){
        return sp.getLong(MyInfomations.ONE,0);
    }

    public void setTwoDay(Long b){
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(MyInfomations.TWO, b);
        editor.commit();
    }

    public Long getTwoDay(){
        return sp.getLong(MyInfomations.TWO,0);
    }

    public void setThreeDay(Long b){
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong("three", b);
        editor.commit();
    }

    public Long getThreeDay(){
        return sp.getLong(MyInfomations.THREE,0);
    }

    public void setFourDay(Long b){
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(MyInfomations.FOUR, b);
        editor.commit();
    }

    public Long getFourDay(){
        return sp.getLong(MyInfomations.FOUR,0);
    }

    public void setFiveDay(Long b){
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(MyInfomations.FIVE, b);
        editor.commit();
    }

    public Long getFiveDay(){
        return sp.getLong(MyInfomations.FIVE,0);
    }

    public void setSixDay(Long b){
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(MyInfomations.SIX, b);
        editor.commit();
    }

    public Long getSixDay(){
        return sp.getLong(MyInfomations.SIX,0);
    }

    public void setSevenDay(Long b){
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(MyInfomations.SEVEN, b);
        editor.commit();
    }

    public Long getSevenDay(){
        return sp.getLong(MyInfomations.SEVEN,0);
    }

    /**
     *  判断当天是否签到
     * */
    public boolean getIsorNo_Record(){
        return sp.getBoolean(MyInfomations.SIGN_IS_OR_NOT,false);
    }

    public void setIsorNo_Record(boolean isorno){
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(MyInfomations.SIGN_IS_OR_NOT, isorno);
        editor.commit();
    }
}
