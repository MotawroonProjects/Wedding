
package com.apps.wedding.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.wedding.R;
import com.apps.wedding.databinding.WeddingHallRowBinding;
import com.apps.wedding.model.DepartmentModel;
import com.apps.wedding.model.WeddingHallModel;

import java.util.List;

public class WeddingHallAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<WeddingHallModel> list;
    private Context context;
    private LayoutInflater inflater;
    private AppCompatActivity appCompatActivity;


    public WeddingHallAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        appCompatActivity = (AppCompatActivity) context;
    }

    @androidx.annotation.NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {


        WeddingHallRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.wedding_hall_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;


    }

    @Override
    public int getItemCount() {
        if (list!=null){
         return list.size();
        }else {
            return 8;
        }
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public WeddingHallRowBinding binding;

        public MyHolder(WeddingHallRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public void updateList(List<WeddingHallModel> list){
        this.list=list;
        notifyDataSetChanged();
    }

}
