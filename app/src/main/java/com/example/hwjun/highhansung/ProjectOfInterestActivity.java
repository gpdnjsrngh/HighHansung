package com.example.hwjun.highhansung;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.hwjun.highhansung.model.Project;
import com.example.hwjun.highhansung.model.ProjectAdapter;
import com.example.hwjun.highhansung.model.ProjectOfInterest;
import com.example.hwjun.highhansung.model.UserModel;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.ArrayList;
import java.util.Iterator;

public class ProjectOfInterestActivity extends AppCompatActivity {
    String TAG = this.getClass().getSimpleName();
    ArrayList<Project> projectList;
    ListView listView;
    private static ProjectAdapter adapter;
    ArrayList<String> projectIdList;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference1;
    ProjectOfInterest projectOfInterest;
    Iterable<DataSnapshot> iterable;
    Iterator<DataSnapshot> iterator;
    DataSnapshot ds;
    private FirebaseRemoteConfig firebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_of_interest);

        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        //상태바 색깔 바꾸기
        String splash_background = firebaseRemoteConfig.getString(getString(R.string.rc_color));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor(splash_background));
        }

        listView = (ListView) findViewById(R.id.main_project_list);
        projectList = new ArrayList<>();
        projectIdList = new ArrayList<>();

        //...DB작업
        //DB의 userModel에 저장
        FirebaseUser user = ((MainActivity) MainActivity.mainContext).currentUser;

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").child(user.getUid()).child("projects").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                iterable = dataSnapshot.getChildren();
                iterator = iterable.iterator();
                while (iterator.hasNext()) {
                    ds = iterator.next();
                    projectOfInterest = ds.getValue(ProjectOfInterest.class);
                    projectIdList.add(projectOfInterest.getProjectId()); //user가 갖고있는 프로젝트 아이디 추가
                }

                if (projectIdList.size() != 0) {
                    Log.d(TAG, "hyewon: " + "0아니야");
                    setUI();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    Project userProject;

    public void setUI() {

        if (projectIdList.size() != 0) {
            Log.d(TAG, "hyewon: " + "0아니라고");
        }
        for (String projectId : projectIdList) {
            databaseReference1 = FirebaseDatabase.getInstance().getReference();
            databaseReference1.child("projects").child(projectId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userProject = dataSnapshot.getValue(Project.class);
                    projectList.add(userProject);
                    Log.d(TAG, "hyewon: " + projectList.get(projectList.indexOf(userProject)).getProject_id());
                    setUI2();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public void setUI2() {
        adapter = new ProjectAdapter(projectList, this);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Project project = projectList.get(position);
                Snackbar.make(view, project.getName() + "\n" + project.getHost() + "\n" + project.getRegisterDate()
                        , Snackbar.LENGTH_LONG).setAction("No action", null).show();

                Intent intent = new Intent(ProjectOfInterestActivity.this, ProjectInfoActivity.class);
                intent.putExtra("projectLink", project.getLink());
                intent.putExtra("projectId", project.getProject_id());
                intent.putExtra("from", "ProjectOfInterestActivity");
                startActivity(intent);
            }
        });
    }

}
