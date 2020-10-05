package com.example.hwjun.highhansung;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hwjun.highhansung.model.Project;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProjectInfoActivity extends AppCompatActivity {
    TextView tv_projectName;
    TextView tv_projectLink;
    ImageView iv_projectImg;

    private String htmlPageUrl;
    private String imgUri;

    int cnt=0;
    Elements html_projectName;
    Elements p_rows;

    Bitmap bitmap;
    String projectId;

    private FirebaseRemoteConfig firebaseRemoteConfig;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_info);
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        //상태바 색깔 바꾸기
        String splash_background= firebaseRemoteConfig.getString(getString(R.string.rc_color));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor(splash_background));
        }

        tv_projectName = findViewById(R.id.activity_project_info_name);
        tv_projectLink = findViewById(R.id.activity_project_info_link);
        iv_projectImg = findViewById(R.id.activity_project_info_img);

        Button btn_registerIntrestingProject=findViewById(R.id.activity_project_info_regiser_btn);
        Button btn_goMatchingList=findViewById(R.id.activity_project_info_matching_btn);
        btn_registerIntrestingProject.setBackgroundColor(Color.parseColor(splash_background));
        btn_goMatchingList.setBackgroundColor(Color.parseColor(splash_background));

        Intent intent = getIntent();
        if(intent.getExtras().getString("from")!=null)
            if((intent.getExtras().getString("from")).equals("ProjectOfInterestActivity")) {
                btn_registerIntrestingProject.setVisibility(View.GONE);
                btn_goMatchingList.setVisibility(View.VISIBLE);
            }

        String projectLink = intent.getExtras().getString("projectLink");
        projectId = intent.getExtras().getString("projectId");
        htmlPageUrl=projectLink;

        tv_projectLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(htmlPageUrl));
                browserIntent.setPackage("com.android.chrome");
                startActivity(browserIntent);
            }
        });

        btn_registerIntrestingProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent popupIntent = new Intent(ProjectInfoActivity.this, PopupActivity.class);

                popupIntent.putExtra("projectId",projectId);
                startActivity(popupIntent);
            }
        });

        btn_goMatchingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mlIntent = new Intent(ProjectInfoActivity.this, MatchingListActivity.class);
                //putExtra로 각 프로젝트에 따른 매칭리스트 출력
                startActivity(mlIntent);
            }
        });

        System.out.println("프래그1의" + (cnt++) + "번째 파싱");
        ProjectInfoActivity.JsoupAsyncTask jsoupAsyncTask = new ProjectInfoActivity.JsoupAsyncTask();
        jsoupAsyncTask.execute();
        cnt++;
    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                Document doc = Jsoup.connect(htmlPageUrl).get();

                //bbs-view라는 이름을 가진 table의 thead의 tr의 th가져오기(클릭된 프로젝트 제목 전체)
                html_projectName = doc.select("table.bbs-view thead tr th");

                p_rows = doc.select("div.bbs-view div.core p");

                for (Element e : p_rows) {
                    if(!(p_rows.get(p_rows.indexOf(e)).getElementsByTag("img").isEmpty())) {
                        imgUri = p_rows.get(p_rows.indexOf(e)).getElementsByTag("img").attr("src").toString();
                        System.out.println("이미지path: "+imgUri);
                    }
                }

                System.out.println("-------------------------------------------------------------");
                if(imgUri!=null) {
                    URL url = new URL("https://www.hansung.ac.kr" + imgUri);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);//서버로부터 응답 수신
                    conn.connect();

                    InputStream is = conn.getInputStream();//inputstream값 가져오기
                    bitmap = BitmapFactory.decodeStream(is);//bitmap으로 변환
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            tv_projectName.setText(html_projectName.text().trim());
            iv_projectImg.setImageBitmap(bitmap);
        }
    }
}
