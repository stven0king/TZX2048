package com.example.stven_king.tzx2048.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.stven_king.tzx2048.bean.MyInfomations;

/**
 * Created by STVEN_KING on 2015/1/15.
 * @author STVEN_KING
 * TODO 储存最高纪录，
 */
public class BestScode {

    private SharedPreferences sp;
    private static BestScode bestScode;

    public BestScode(Context context){
        sp = context.getSharedPreferences(MyInfomations.BEST_SCODE, context.MODE_PRIVATE);
    }

    public static BestScode getInstance(Context context){
        if (bestScode == null){
            bestScode = new BestScode(context);
        }
        return bestScode;
    }

    public int getBestScode(){
        int bestscode = sp.getInt("bestscode", 0);
        return bestscode;
    }
    public void setBestScode(int bestScode){
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("bestscode", bestScode);
        editor.commit();
    }

    public int getGolds(){
        int golds = sp.getInt("golds",0);
        return golds;
    }

//    public void setGolds(int golds){
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putInt("golds",golds);
//        editor.commit();
//    }

    public void addGolds(int golds){
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("golds",getGolds() + golds);
        editor.commit();
    }

    public void cutGolds(int golds){
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("golds",getGolds() - golds);
        editor.commit();
    }

}
