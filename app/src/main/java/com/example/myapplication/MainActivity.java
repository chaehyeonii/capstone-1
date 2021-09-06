package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button_chatting=findViewById(R.id.button_chatting); //채팅 버튼 가져오기
        button_chatting.setOnClickListener(new View.OnClickListener(){  //채팅 버튼 눌렀을 때 화면 전환
            @Override
            public void onClick(View v){
                Intent intent_chatting = new Intent(getApplicationContext(),ChattingActivity.class);
                startActivity(intent_chatting);
            }
        });

        Button button_declare=findViewById(R.id.button_declare); //신고 버튼 가져오기
        button_declare.setOnClickListener(new View.OnClickListener(){ //신고 버튼 클릭 시 화면 전환
            @Override
            public void onClick(View v){
                Intent intent_declare = new Intent(getApplicationContext(),DeclareActivity.class);
                startActivity(intent_declare);
            }
        });

        Button button_home=findViewById(R.id.button_home); //홈 버튼 가져오기
        button_home.setOnClickListener(new View.OnClickListener(){ //홈 버튼 클릭 시 화면 전환
            @Override
            public void onClick(View v){
                Intent intent_home = new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(intent_home);
            }
        });
    }
}