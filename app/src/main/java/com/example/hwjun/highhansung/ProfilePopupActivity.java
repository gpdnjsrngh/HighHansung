package com.example.hwjun.highhansung;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hwjun.highhansung.model.ProjectOfInterest;
import com.example.hwjun.highhansung.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfilePopupActivity extends Activity {

    //데이터베이스
    private DatabaseReference databaseReference;
    String projectId;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_profile_popup);


        Intent intent = getIntent();
        final String currentUserName = intent.getExtras().getString("currentUserName");

        //UI 객체 생성
        TextView tv_introMsg = findViewById(R.id.tv_introMsg);
        TextView tv_clickName = findViewById(R.id.tv_clickName);

        tv_introMsg.setText(intent.getExtras().getString("introMsg"));
        tv_clickName.setText(intent.getExtras().getString("clickName"));

        Button cancelBtn = findViewById(R.id.activity_profile_popup_cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("result", "Cancel");

                //액티비티(팝업) 닫기
                finish();
            }
        });

        Button registerBtn = findViewById(R.id.activity_profile_popup_register_btn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProfilePopupActivity.this, ChatActivity.class);
                intent.putExtra("currentUserName", currentUserName);
                startActivity(intent);
                //액티비티(팝업) 닫기
                finish();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭 시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}
