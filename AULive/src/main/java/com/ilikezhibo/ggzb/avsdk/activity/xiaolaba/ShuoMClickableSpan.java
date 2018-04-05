package com.ilikezhibo.ggzb.avsdk.activity.xiaolaba;

import android.content.Context;
import android.content.Intent;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by r on 16/10/17.
 */
public class ShuoMClickableSpan extends ClickableSpan {
    String string;
    Context context;

    int color;

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    Intent intent;
    public void setColor(int color) {
        this.color = color;
    }
    public ShuoMClickableSpan(String str, Context context){
        super();
        this.string = str;
        this.context = context;
    }

    public ShuoMClickableSpan(String str, int color, Context context, Intent intent){
        super();
        this.string = str;
        this.color = color;
        this.context = context;
        this.intent = intent;
    }


    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(color);
    }





    @Override
    public void onClick(View widget) {
        if(intent == null) {
            return;
        }
        context.startActivity(intent);
    }
}
