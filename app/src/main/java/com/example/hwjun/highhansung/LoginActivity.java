package com.example.hwjun.highhansung;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class LoginActivity extends AppCompatActivity {
    private EditText id;
    private EditText password;
    private Button login;
    private Button signup;
    private FirebaseRemoteConfig firebaseRemoteConfig;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        //firebaseAuth.signOut();

        //상태바 색깔 바꾸기
        String splash_background= firebaseRemoteConfig.getString(getString(R.string.rc_color));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor(splash_background));
        }

        id = findViewById(R.id.loginActivity_edittext_id);
        password = findViewById(R.id.loginActivity_edittext_password);

        //id, password 디폴트 설정
        id.setText("hwjun@hansung.ac.kr");
        password.setText("123456");

        //버튼 획득
        login = findViewById(R.id.loginActivity_button_login);
        signup = findViewById(R.id.loginActivity_button_signup);
        //버튼 background 색깔 지정
        login.setBackgroundColor(Color.parseColor(splash_background));
        signup.setBackgroundColor(Color.parseColor(splash_background));

        //로그인 버튼 클릭 이벤트
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //아이디와 패스워드 중 하나라도 입력하지 않으면
                if(id.getText().toString()==null||password.getText().toString()==null) {
                    Toast.makeText(LoginActivity.this, "아이디와 패스워드를 입력하세요.",Toast.LENGTH_LONG).show();
                    return;
                }
                loginEvent();
            }
        });

        //회원가입 버튼 클릭 이벤트
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //회원가입 액티비티 호출
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        //로그인 인터페이스 리스너(로그인 되었는지 확인해주는 리스너)
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {
                    //로그인 상태 => MainActivity 호출
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    //로그아웃 상태
                }
            }
        };
    }

    private void loginEvent() {
        //로그인 처리
        firebaseAuth.signInWithEmailAndPassword(id.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()) {
                            //login 실패한 부분
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            password.setText("");
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
}
