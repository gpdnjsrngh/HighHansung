package com.example.hwjun.highhansung;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.hwjun.highhansung.model.Project;
import com.example.hwjun.highhansung.model.ProjectAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;


/**
 * A simple {@link Fragment} subclass.
 */
public class OneFragment extends android.support.v4.app.Fragment {
    ArrayList<Project> projectList;
    ListView listView;
    private static ProjectAdapter adapter;

    //파싱할 홈페이지의 URL 주소
    private String htmlPageUrl = "https://www.hansung.ac.kr/web/www/cmty_01_01?p_p_id=EXT_BBS&p_p_lifecycle=0&p_p_state=normal&p_p_mode=view&p_p_col_id=column-1&p_p_col_pos=1&p_p_col_count=3&_EXT_BBS_struts_action=%2Fext%2Fbbs%2Fview&_EXT_BBS_sCategory=&_EXT_BBS_sKeyType=title&_EXT_BBS_sKeyword=%EB%B9%84%EA%B5%90%EA%B3%BC&_EXT_BBS_curPage=1";
    Elements rows;
    int cnt = 0;

    String project_id = "id_error";
    Project project; //html 파싱해서 생성하는 project
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_one, container, false);

        System.out.println("프래그1의" + (cnt + 1) + "번째 파싱");
        OneFragment.JsoupAsyncTask jsoupAsyncTask = new OneFragment.JsoupAsyncTask();
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

                System.out.println("-------------------------------------------------------------");
                for (Elements es : projectDatas) {
                    project = new Project();

                    for (int j = 0; j < 6; j++) {
                        System.out.println(j + "번째 element: " + es.get(j).text());

                        switch (j) {
                            case 0:
                                project_id = "b" + es.get(j).text().trim();
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
            System.out.println("여기 호출 언제됨?");

            adapter = new ProjectAdapter(projectList, getContext());

            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Project project = projectList.get(position);

                    //프래그먼트 교체 안됌ㅠ
                    //((MainActivity)MainActivity.mainContext).myPagerAdapter.changeFrag();

                    /*Fragment productDetailFragment = new ProjectInfoFragment();
                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.viewpager, productDetailFragment).commit();*/

                    Intent intent = new Intent(getContext(), ProjectInfoActivity.class);
                    intent.putExtra("projectLink", project.getLink());
                    intent.putExtra("projectId", project.getProject_id());
                    startActivity(intent);
                }
            });

        }
    }

}
