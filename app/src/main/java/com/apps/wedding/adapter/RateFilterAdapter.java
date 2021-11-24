
package com.apps.wedding.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.wedding.R;
import com.apps.wedding.databinding.FilterRateRowBinding;
import com.apps.wedding.databinding.WeddingHallRowBinding;
import com.apps.wedding.model.FilterRateModel;
import com.apps.wedding.model.WeddingHallModel;
import com.apps.wedding.uis.activity_home.fragments_home_navigaion.FragmentHome;

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

    public void updateData(List<FilterRateModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

}
