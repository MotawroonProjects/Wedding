

package com.apps.wedding.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.wedding.R;
import com.apps.wedding.databinding.OrderRowBinding;
import com.apps.wedding.databinding.PreviousOrderRowBinding;
import com.apps.wedding.model.ResevisionModel;
import com.apps.wedding.uis.activity_home.fragments_home_navigaion.FragmentCurrentReservation;
import com.apps.wedding.uis.activity_home.fragments_home_navigaion.FragmentPreviousReservation;

import java.util.List;

public class PreviousReservionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ResevisionModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;


    public PreviousReservionAdapter(Context context, Fragment fragment) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment = fragment;
    }

    @androidx.annotation.NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {


        PreviousOrderRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.previous_order_row, parent, false);
        return new MyHolder(binding);


    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        ResevisionModel model = list.get(position);
        double total = model.getPrice() + model.getExtra_item_price();
        myHolder.binding.setTotal(total + "");
        myHolder.binding.llShow.setOnClickListener(v -> {
            if (fragment instanceof FragmentPreviousReservation) {
                FragmentPreviousReservation fragmentCurrentReservation = (FragmentPreviousReservation) fragment;
                fragmentCurrentReservation.createSheetDialog(list.get(myHolder.getAdapterPosition()));

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
        public PreviousOrderRowBinding binding;

        public MyHolder(PreviousOrderRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public void updateList(List<ResevisionModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

}
