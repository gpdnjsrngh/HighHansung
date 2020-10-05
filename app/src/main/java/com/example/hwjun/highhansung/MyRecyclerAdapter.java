package com.example.hwjun.highhansung;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hwjun.highhansung.model.CardItem;

import org.w3c.dom.Text;

import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {
    private final List<CardItem> mDataList;
    private MyRecyclerViewClickListener mListener;

    //객체 생성 시 데이터 전달 받음
    public MyRecyclerAdapter(List<CardItem> dataList) {
        mDataList=dataList;
    }

    //최초 뷰 홀더 생성, 레이아웃 설정
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card, parent, false);
        return new ViewHolder(v);
    }

    //뷰 홀더에 데이터 설정
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CardItem item = mDataList.get(position);

        holder.num.setText(item.getNum());
        holder.name.setText(item.getName());
        holder.major.setText(item.getMajor());
        holder.gender.setText(item.getGender());

        if(mListener!=null) {
            final int pos = position;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   mListener.onItemClicked(pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView num;
        TextView name;
        TextView major;
        TextView gender;

        public ViewHolder(View itemView) {
            super(itemView);

            num=itemView.findViewById(R.id.tv_num);
            name=itemView.findViewById(R.id.tv_name);
            major=itemView.findViewById(R.id.tv_major);
            gender=itemView.findViewById(R.id.tv_gender);
        }
    }

    public void setOnClickeListener(MyRecyclerViewClickListener listener) {
        mListener = listener;
    }
    public interface MyRecyclerViewClickListener {
        void onItemClicked(int position); //아이템 전체 부분 클릭
    }
}
