package com.example.hwjun.highhansung;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hwjun.highhansung.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class SignupActivity extends AppCompatActivity {
    private EditText email;
    private EditText name;
    private EditText password;
    private RadioGroup gender;
    private AppCompatSpinner major;
    private EditText introMsg;
    private Button signup;
    private String splash_background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //상태바 색깔 원격에서 가져오기
        FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        splash_background= firebaseRemoteConfig.getString(getString(R.string.rc_color));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor(splash_background));
        }

        //UI 획득
        email = findViewById(R.id.signupActivity_edittext_email);
        name = findViewById(R.id.signupActivity_edittext_name);
        password = findViewById(R.id.signupActivity_edittext_password);
        gender = findViewById(R.id.signupActivity_rg_gender);
        major = findViewById(R.id.signupActivity_sp_major);
        introMsg = findViewById(R.id.signupActivity_edittext_introMsg);

        signup = findViewById(R.id.signupActivity_btn_signup);
        signup.setBackgroundColor(Color.parseColor(splash_background));

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString()==null||name.getText().toString()==null
                        ||password.getText().toString()==null||password.getText().toString().length()<6
                        ||findViewById(gender.getCheckedRadioButtonId())==null||introMsg.getText().toString()==null) {
                    Toast.makeText(SignupActivity.this, "다시 입력하세요.",Toast.LENGTH_LONG).show();
                    return;
                }

                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            AppCompatRadioButton radioButton = findViewById(gender.getCheckedRadioButtonId());
                            //회원가입이 완료되면 이곳으로 옴
                            String uid = task.getResult().getUser().getUid();

                            UserModel userModel = new UserModel();
                            userModel.setUserName(name.getText().toString());
                            userModel.setUserEmail(email.getText().toString());
                            userModel.setUID(uid);
                            userModel.setUserGender(radioButton.getText().toString());
                            userModel.setUserMajor(major.getSelectedItem().toString());
                            userModel.setIntroMsg(introMsg.getText().toString());

                            FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(userModel);
                            Toast.makeText(SignupActivity.this, "회원가입이 완료되었습니다.",Toast.LENGTH_LONG).show();
                            //로그인 액티비티 호출
                            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                            firebaseAuth.signOut();
                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    });
            }
        });
    }
}
