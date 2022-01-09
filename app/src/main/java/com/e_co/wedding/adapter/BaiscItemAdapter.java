
package com.e_co.wedding.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.e_co.wedding.R;
import com.e_co.wedding.databinding.BasicItemRowBinding;
import com.e_co.wedding.databinding.WeddingHallRowBinding;
import com.e_co.wedding.model.WeddingHallModel;

import java.util.List;

public class BaiscItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<WeddingHallModel.ServiceMainItem> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;


    public BaiscItemAdapter(Context context, Fragment fragment) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment = fragment;
    }

    @androidx.annotation.NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {


        BasicItemRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.basic_item_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        if (position == list.size()-1) {
            myHolder.binding.view.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public BasicItemRowBinding binding;

        public MyHolder(BasicItemRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
    public void updateList(List<WeddingHallModel.ServiceMainItem> list){
        this.list=list;
        notifyDataSetChanged();
    }

}
