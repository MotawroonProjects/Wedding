
package com.apps.wedding.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.wedding.R;
import com.apps.wedding.databinding.AdditionalItemsRowBinding;
import com.apps.wedding.databinding.BasicItemRowBinding;
import com.apps.wedding.model.WeddingHallModel;
import com.apps.wedding.uis.activity_home.fragments.FragmentReservisionConfirmation;

import java.util.List;

public class AdditionalItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<WeddingHallModel.ServiceExtraItem> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;


    public AdditionalItemAdapter(Context context, Fragment fragment) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment = fragment;
    }

    @androidx.annotation.NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {


        AdditionalItemsRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.additional_items_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        if (position == list.size()) {
            myHolder.binding.view.setVisibility(View.GONE);
        }
        myHolder.binding.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (fragment instanceof FragmentReservisionConfirmation) {
                    FragmentReservisionConfirmation fragmentReservisionConfirmation = (FragmentReservisionConfirmation) fragment;
                    fragmentReservisionConfirmation.seletItem(list.get(holder.getLayoutPosition()).getId());
                }
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
        public AdditionalItemsRowBinding binding;

        public MyHolder(AdditionalItemsRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public void updateList(List<WeddingHallModel.ServiceExtraItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }

}
