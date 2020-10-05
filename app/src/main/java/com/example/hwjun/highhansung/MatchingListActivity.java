package com.example.hwjun.highhansung;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.hwjun.highhansung.model.CardItem;
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
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.ArrayList;
import java.util.List;

public class MatchingListActivity extends AppCompatActivity
        implements MyRecyclerAdapter.MyRecyclerViewClickListener {
    private DatabaseReference mDatabase;
    ArrayList<UserModel> userModels = new ArrayList<>();
    List<CardItem> dataList = new ArrayList<>();
    RecyclerView rcv;
    private FirebaseUser currentUser;
    private UserModel cum = null;
    ArrayList<ProjectOfInterest> projectOfInterests = new ArrayList<>();
    private FirebaseRemoteConfig firebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_list);
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        //상태바 색깔 바꾸기
        String splash_background = firebaseRemoteConfig.getString(getString(R.string.rc_color));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor(splash_background));
        }

        currentUser = ((MainActivity) MainActivity.mainContext).currentUser;
        rcv = findViewById(R.id.recycler_view);

        //레이아웃 매니저로 LinearLayoutManager 설정
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        rcv.setLayoutManager(lm);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        onReadDB();

    }

    public void onReadDB() {
        //모든 사용자 객체 가져오기
        mDatabase.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    UserModel userModel = postSnapshot.getValue(UserModel.class);

                    userModels.add(userModel);

                }


                if (userModels.size() > 0) {
                    Log.d("ml", "사용자 리스트 사이즈 0 아니야");
                    setUI();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void onReadDB2(String uid) {
        //현재 사용자의 projects 가져오기
        mDatabase.child("users").child(uid).child("projects").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    ProjectOfInterest projectOfInterest = postSnapshot.getValue(ProjectOfInterest.class);

                    projectOfInterests.add(projectOfInterest);
                }
                if (projectOfInterests.size() > 0) {
                    Log.d("ml", projectOfInterests.get(0).getMajor());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void setUI() {
        //자신은 지우기
        for (UserModel userModel : userModels) {
            if (userModel.getUID().equals(currentUser.getUid())) {
                cum = userModel;
                userModels.remove(userModel);
                break;
            }
        }

        String s = "1";
        int i = 1;
        for (UserModel userModel : userModels) {
            dataList.add(new CardItem(s, userModel.getUserName(), userModel.getUserMajor(), userModel.getUserGender()));
            i++;
            s = String.valueOf(i);
        }

        MyRecyclerAdapter adapter = new MyRecyclerAdapter(dataList);
        adapter.setOnClickeListener(MatchingListActivity.this);
        rcv.setAdapter(adapter);

    }

    @Override
    public void onItemClicked(int position) {
        Intent popupIntent = new Intent(MatchingListActivity.this, ProfilePopupActivity.class);
        popupIntent.putExtra("introMsg", (userModels.get(position)).getIntroMsg());
        popupIntent.putExtra("clickName", (userModels.get(position)).getUserName());
        popupIntent.putExtra("currentUserName", cum.getUserName());
        startActivity(popupIntent);
    }
}
