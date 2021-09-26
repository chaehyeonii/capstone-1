package com.example.myapplication;

import android.app.Activity;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;

public class BadgeActivity extends Activity {

    ImageView badge1, badge2, badge3;
    Button buttonClick;
    private int count=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badge);
        buttonClick=findViewById(R.id.buttonClick);


        badge1=findViewById(R.id.badge1);
        badge1.setImageResource(R.drawable.gugu);

        badge2=findViewById(R.id.badge2);
        badge2.setImageResource(R.drawable.haha);

        badge3=findViewById(R.id.badge3);
        badge3.setImageResource(R.drawable.hoho);

        //----컬러 필터를 흑백으로 변경------
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter=new ColorMatrixColorFilter(matrix);
        badge1.setColorFilter(filter);
        badge2.setColorFilter(filter);
        badge3.setColorFilter(filter);

    }

    public void click(View v){
        count++;

        if(count==1){
            badge1.setColorFilter(null);
        }
        else if(count==2){
            badge2.setColorFilter(null);
        }
        else if(count==3){
            badge3.setColorFilter(null);
        }
    }
}