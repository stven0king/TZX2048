package com.example.stven_king.tzx2048.bean;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2015/1/22.
 */
public class User {
    private SharedPreferences sp;
    private static User user;
    public User(Context context){
        sp = context.getSharedPreferences(MyInfomations.USER_INFO, context.MODE_PRIVATE);
    }

    public static User getInstance(Context context){
        if (user == null){
            user = new User(context);
        }
        return user;
    }

    public void setQiDao(boolean b){
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(MyInfomations.QIDAO,b);
        editor.commit();
    }

    public boolean getQiDao(){
        return sp.getBoolean(MyInfomations.QIDAO,false);
    }

    public void setJaoHuan(boolean b){
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(MyInfomations.JIAOHUAN,b);
        editor.commit();
    }

    public boolean getJaoHuan(){
        return sp.getBoolean(MyInfomations.JIAOHUAN,false);
    }

    public void setChuiZi(boolean b){
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(MyInfomations.CHUIZI,b);
        editor.commit();
    }

    public boolean getChuiZi(){
        return sp.getBoolean(MyInfomations.CHUIZI,false);
    }

    public void setFanHui(boolean b){
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(MyInfomations.FANHUI,b);
        editor.commit();
    }

    public boolean getFanHui(){
        return sp.getBoolean(MyInfomations.FANHUI,false);
    }
}
