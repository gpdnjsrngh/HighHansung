package com.example.hwjun.highhansung;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hwjun.highhansung.model.Project;
import com.example.hwjun.highhansung.model.ProjectOfInterest;
import com.example.hwjun.highhansung.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;



public class PopupActivity extends Activity {
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
        setContentView(R.layout.activity_popup);

        //UI 객체 생성
        Button cancelBtn = findViewById(R.id.activity_popup_cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("result", "Cancel");
                //액티비티(팝업) 닫기
                finish();
            }
        });

        final RadioGroup rg_gender = findViewById(R.id.rg_gender);

        Button registerBtn = findViewById(R.id.activity_popup_register_btn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(1000);

                MediaPlayer player = MediaPlayer.create(PopupActivity.this, R.raw.fallbackring);
                player.start();

                ProjectOfInterest projectOfInterest = new ProjectOfInterest();

                //성별 값 가져오기
                int rb_gener_id = rg_gender.getCheckedRadioButtonId();
                RadioButton rb_gender = findViewById(rb_gener_id);

                projectOfInterest.setGender(rb_gender.getText().toString());
                //전공 값 가져오기
                final Spinner sp_major = findViewById(R.id.sp_major);
                String major = sp_major.getSelectedItem().toString();
                projectOfInterest.setMajor(major);

                //가까운 팀원 불린 값 가져오기
                CheckBox cb_near = findViewById(R.id.cb_near);
                if(cb_near.isChecked()) {
                    projectOfInterest.setWantNear(true);
                }
                else
                    projectOfInterest.setWantNear(false);

                //DB에 관심 프로젝트 저장 (UserModel의 관심프로젝트리스트에 project_id삽입)
                Intent intent = getIntent();
                String projectLink = intent.getExtras().getString("projectLink");
                projectId = intent.getExtras().getString("projectId");

                //프로젝트 아이디 값도 저장
                projectOfInterest.setProjectId(projectId);
                Toast.makeText(PopupActivity.this, "관심프로젝트 등록이 완료되었습니다.", Toast.LENGTH_SHORT).show();

                //FirebaseDatabase.getInstance().getReference().child("projects").child(id).setValue(userModel);

                UserModel currentUserModel = ((MainActivity)MainActivity.mainContext).currentUserModel;
                currentUserModel.addProjectIdsOfInterest(projectId);
                Log.d("PopupActivity", currentUserModel.getUserName()+" : "+currentUserModel.getProjectIdsOfInterest().get(0));
                //...DB작업
                //DB의 userModel에 저장
                FirebaseUser user = ((MainActivity)MainActivity.mainContext).currentUser;
                databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("users").child(user.getUid()).child("projectIds").child(projectId).setValue(projectId);
                databaseReference.child("users").child(user.getUid()).child("projects").child(projectId).setValue(projectOfInterest);
                //DB-Projects-projectId-user-원하는 팀원 특징
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
