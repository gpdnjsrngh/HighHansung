package com.example.hwjun.highhansung;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hwjun.highhansung.model.Project;
import com.example.hwjun.highhansung.model.ProjectAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


/**
 * A simple {@link Fragment} subclass.
 */
public class TwoFragment extends Fragment {
    ArrayList<Project> projectList;
    ListView listView;
    private static ProjectAdapter adapter;

    //파싱할 홈페이지의 URL 주소
    private String htmlPageUrl = "https://www.hansung.ac.kr/web/www/cmty_01_01?p_p_id=EXT_BBS&p_p_lifecycle=0&p_p_state=normal&p_p_mode=view&p_p_col_id=column-1&p_p_col_pos=1&p_p_col_count=3&_EXT_BBS_struts_action=%2Fext%2Fbbs%2Fview&_EXT_BBS_sCategory=&_EXT_BBS_sKeyType=title&_EXT_BBS_sKeyword=%EA%B3%B5%EB%AA%A8%EC%A0%84&_EXT_BBS_curPage=1";
    String page_1 = "1";
    String page_2 = "2";
    String page_3 = "3";
    Elements rows;
    int cnt = 0;
    String project_id = "id_error";
    Project project; //html 파싱해서 생성하는 project
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_two, container, false);

        SSLConnect ssl = new SSLConnect();
        ssl.postHttps(htmlPageUrl, 1000, 1000);

        System.out.println("프래그2의" + (cnt + 1) + "번째 파싱");
        TwoFragment.JsoupAsyncTask jsoupAsyncTask = new TwoFragment.JsoupAsyncTask();
        jsoupAsyncTask.execute();
        cnt++;

        listView = (ListView) layout.findViewById(R.id.main_project_list);
        projectList = new ArrayList<>();

        return layout;

    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                int t = 0;

                Document doc = Jsoup.connect(htmlPageUrl).get();
                //bbs-list라는 이름을 가진 table의 tbody의 tr 가져오기('행'들을 전부 가져옴)
                rows = doc.select("table.bbs-list tbody tr");

                //행 데이터 각각 projectDats에 저장
                Vector<Elements> projectDatas = new Vector<>();
                for (int i = 0; i < 15; i++) {
                    projectDatas.add(rows.get(i).getElementsByTag("td"));
                }

                //첫번째 항목의 링크 뽑기
                System.out.println("링크 뽑아줘 " + projectDatas.get(0).get(1).getElementsByTag("a").attr("href").toString());    //.html().toString());
                //Elements hostElements = titles.select("td");
                System.out.println("-------------------------------------------------------------");
                for (Elements es : projectDatas) {
                    project = new Project();

                    for (int j = 0; j < 6; j++) {
                        System.out.println(j + "번째 element: " + es.get(j).text());
                        //htmlContentInStringFormat += es.get(i).text().trim() + "\n";

                        switch (j) {
                            case 0:
                                project_id = "g" + es.get(j).text().trim();
                                project.setProject_id(project_id);
                                break;
                            case 1:
                                project.setName(es.get(j).text());
                                break;
                            case 3:
                                project.setHost(es.get(j).text());
                                break;
                            case 4:
                                project.setRegisterDate(es.get(j).text());
                                break;
                            default:
                                break;
                        }
                    }

                    String project_link = es.get(1).getElementsByTag("a").attr("href").toString();
                    project.setLink(project_link);
                    System.out.println(projectDatas.indexOf(es) + "번째 link: " + project_link);
                    projectList.add(project);

                    //DB의 projects에 project 객체 저장
                    databaseReference.child("projects").child(project_id).setValue(project);

                }
                t++;

                System.out.println("-------------------------------------------------------------");


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //textView에 가져온 데이터 출력
            /* textViewHtmlDocument.setText(htmlContentInStringFormat);*/
            System.out.println("여기 호출 언제됨?");

            adapter = new ProjectAdapter(projectList, getContext());

            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Project project = projectList.get(position);

                    Intent intent = new Intent(getContext(), ProjectInfoActivity.class);
                    intent.putExtra("projectLink", project.getLink());
                    intent.putExtra("projectId", project.getProject_id());
                    startActivity(intent);

                }
            });
        }
    }


}
