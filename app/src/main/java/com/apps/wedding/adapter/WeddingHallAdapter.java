
package com.apps.wedding.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.wedding.R;
import com.apps.wedding.databinding.WeddingHallRowBinding;
import com.apps.wedding.model.WeddingHallModel;
import com.apps.wedding.uis.activity_home.fragments_home_navigaion.FragmentHome;

import java.util.List;

public class WeddingHallAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<WeddingHallModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;


    public WeddingHallAdapter(Context context,Fragment fragment) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment = fragment;
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
        myHolder.binding.setModel(list.get(position));
        myHolder.itemView.setOnClickListener(v -> {
            WeddingHallModel weddingHallModel = list.get(holder.getAdapterPosition());
            if (fragment instanceof FragmentHome){
                FragmentHome fragmentHome = (FragmentHome) fragment;
                fragmentHome.setItemWeddingDetails(weddingHallModel.getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        if (list!=null){
         return list.size();
        }else {
            return 0;
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
