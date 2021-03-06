
package com.e_co.wedding.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.e_co.wedding.R;
import com.e_co.wedding.databinding.CategoryRowBinding;
import com.e_co.wedding.databinding.WeddingHallRowBinding;
import com.e_co.wedding.model.DepartmentModel;
import com.e_co.wedding.uis.activity_home.fragments_home_navigaion.FragmentHome;

import java.util.List;

public class WeddingHallDepartmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DepartmentModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;
    private int currentPos = 0;
    private int oldPos = currentPos;

    public WeddingHallDepartmentAdapter(Context context,Fragment fragment) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment = fragment;
    }

    @androidx.annotation.NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {


        CategoryRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.category_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.itemView.setOnClickListener(v -> {
            currentPos = holder.getAdapterPosition();
            DepartmentModel model = list.get(currentPos);
            if (!model.isSelected()) {

                DepartmentModel oldModel = list.get(oldPos);
                oldModel.setSelected(false);
                list.set(oldPos, oldModel);
                notifyItemChanged(oldPos);


                model.setSelected(true);
                list.set(currentPos, model);
                notifyItemChanged(currentPos);

                if (fragment instanceof FragmentHome){
                    FragmentHome fragmentHome = (FragmentHome) fragment;
                    fragmentHome.setItemDepartment(model);
                }

            }


            oldPos = currentPos;

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
        public CategoryRowBinding binding;

        public MyHolder(CategoryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public void updateList(List<DepartmentModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
