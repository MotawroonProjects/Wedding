
package com.e_co.wedding.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.e_co.wedding.R;
import com.e_co.wedding.databinding.MapWeddingHallRowBinding;
import com.e_co.wedding.databinding.WeddingHallRowBinding;
import com.e_co.wedding.model.WeddingHallModel;
import com.e_co.wedding.uis.activity_home.fragments_home_navigaion.FragmentNearby;

import java.util.List;

public class MapWeddingHallAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<WeddingHallModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;


    public MapWeddingHallAdapter(Context context, Fragment fragment) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment = fragment;
    }

    @androidx.annotation.NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {


        MapWeddingHallRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.map_wedding_hall_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.itemView.setOnClickListener(v -> {
            if (fragment instanceof FragmentNearby) {
                FragmentNearby fragmentNearby = (FragmentNearby) fragment;
                fragmentNearby.setItemWeddingDetails(list.get(holder.getLayoutPosition()).getId());
            }
        });

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
        public MapWeddingHallRowBinding binding;

        public MyHolder(MapWeddingHallRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public void updateList(List<WeddingHallModel> list) {
        if (list!=null){
            this.list = list;
        }else {
            if (this.list!=null){
                this.list.clear();
            }
        }
        notifyDataSetChanged();

    }

}
