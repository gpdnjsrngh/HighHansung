package com.example.hwjun.highhansung.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.hwjun.highhansung.ProjectInfoFragment;
import com.example.hwjun.highhansung.R;

import java.util.ArrayList;

public class ProjectAdapter extends ArrayAdapter<Project> implements View.OnClickListener {
    private ArrayList<Project> projectList;
    Context mContext;

    //View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtHost;
        TextView txtRegisterDate;
    }

    public ProjectAdapter(ArrayList<Project> data, Context context) {
        super(context, R.layout.project_list_item, data);
        this.projectList = data;
        this.mContext = context;
    }

    @Override
    public void onClick(View v) {
        int pos = (Integer)v.getTag();
        Object object = getItem(pos);
        Project project = (Project)object;

        switch (v.getId())
        {
            case R.id.project_list_name:
                /*Snackbar.make(v, "Release data"+project.getName()
                        , Snackbar.LENGTH_LONG).setAction("No action", null).show();*/

                break;

        }
    }
    private int lastPos = -1;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //Get the data item for this position
        Project project = getItem(position);
        //Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; //view loockup cache stored in tag

        final View result;

        if(convertView==null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.project_list_item, parent,false);
            viewHolder.txtName = (TextView)convertView.findViewById(R.id.project_list_name);
            viewHolder.txtHost = (TextView)convertView.findViewById(R.id.project_list_host);
            viewHolder.txtRegisterDate = (TextView)convertView.findViewById(R.id.project_list_registerDate);

            result = convertView;
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder)convertView.getTag();
            result=convertView;
        }

        viewHolder.txtName.setText(project.getName());
        viewHolder.txtHost.setText(project.getHost());
        viewHolder.txtRegisterDate.setText(project.getRegisterDate());
        return convertView;

    }
}
