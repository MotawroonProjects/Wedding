
package com.e_co.wedding.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.e_co.wedding.R;
import com.e_co.wedding.databinding.FilterRateRowBinding;
import com.e_co.wedding.model.FilterRateModel;
import com.e_co.wedding.uis.activity_home.fragments_home_navigaion.FragmentHome;
import com.e_co.wedding.uis.activity_home.fragments_home_navigaion.FragmentNearby;

import java.util.List;

public class RateFilterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<FilterRateModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;
    private int currentPos = 0;
    private int oldPos = 0;


    public RateFilterAdapter(Context context, Fragment fragment) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment = fragment;
    }

    @androidx.annotation.NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {


        FilterRateRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.filter_rate_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        FilterRateModel model = list.get(position);
        if (model.isSelected()){
            myHolder.binding.icon.setColorFilter(ContextCompat.getColor(context,R.color.color1));
        }else {
            myHolder.binding.icon.setColorFilter(ContextCompat.getColor(context,R.color.black));

        }
        myHolder.binding.setModel(model);
        myHolder.itemView.setOnClickListener(v -> {
            currentPos = myHolder.getAdapterPosition();
            if (oldPos != -1) {
                FilterRateModel old = list.get(oldPos);
                if (old.isSelected()) {
                    old.setSelected(false);
                    list.set(oldPos, old);
                    notifyItemChanged(oldPos);
                }

            }
            FilterRateModel currentModel = list.get(currentPos);
            if (!currentModel.isSelected()) {
                currentModel.setSelected(true);
                list.set(currentPos, currentModel);
                notifyItemChanged(currentPos);
                oldPos = currentPos;
            }

            if (fragment instanceof FragmentHome){
                FragmentHome fragmentHome = (FragmentHome) fragment;
                fragmentHome.updateFilterRate(model);
            }
            else if(fragment instanceof FragmentNearby){
                FragmentNearby fragmentNearby=(FragmentNearby) fragment;
                fragmentNearby.updateFilterRate(model);
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
        public FilterRateRowBinding binding;

        public MyHolder(FilterRateRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public void updateData(List<FilterRateModel> list, String rate) {
        this.list = list;
        if (list!=null){
            int pos = pos(rate);
            if (pos!=-1){
                currentPos = pos(rate);
                oldPos = currentPos;
                FilterRateModel model = this.list.get(currentPos);
                model.setSelected(true);
                this.list.set(currentPos,model);
            }

        }

        notifyDataSetChanged();
    }

    private int pos(String rate){
        int pos = -1;
        if (list!=null){
            for (int index=0;index<list.size();index++){
                if (list.get(index).getTitle().equals(rate)){
                    pos = index;
                    return pos;
                }
            }
        }

        return pos;
    }

}
