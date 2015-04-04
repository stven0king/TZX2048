package com.example.stven_king.tzx2048.dialog.effects;

import com.example.stven_king.tzx2048.dialog.Effectstype;

import java.util.Random;

/**
 * Created by Administrator on 2015/2/12.
 */
public class EffectsInfo {
    private static EffectsInfo effectsInfo;
    private Effectstype effect;
    public EffectsInfo(){

    }
    public static EffectsInfo getInstance(){
        if (effectsInfo == null){
            effectsInfo = new EffectsInfo();
        }
        return effectsInfo;
    }

    public Effectstype getEffect(){
        int pos = new Random().nextInt(13);
        switch (pos){
            case 0:effect=Effectstype.Fadein;break;
            case 1:effect=Effectstype.Slideright;break;
            case 2:effect=Effectstype.Slideleft;break;
            case 3:effect=Effectstype.Slidetop;break;
            case 4:effect=Effectstype.SlideBottom;break;
            case 5:effect=Effectstype.Newspager;break;
            case 6:effect=Effectstype.Fall;break;
            case 7:effect=Effectstype.Sidefill;break;
            case 8:effect=Effectstype.Fliph;break;
            case 9:effect=Effectstype.Flipv;break;
            case 10:effect=Effectstype.RotateBottom;break;
            case 11:effect=Effectstype.RotateLeft;break;
            case 12:effect=Effectstype.Slit;break;
            case 13:effect=Effectstype.Shake;break;
        }
        return  effect;
    }

    public void setEffect(Effectstype effect){
        this.effect = effect;
    }
}
