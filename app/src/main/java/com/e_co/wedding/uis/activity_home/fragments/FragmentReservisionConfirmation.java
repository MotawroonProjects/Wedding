package com.e_co.wedding.uis.activity_home.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.SavedStateHandle;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.e_co.wedding.R;
import com.e_co.wedding.adapter.AdditionalItemAdapter;
import com.e_co.wedding.adapter.BaiscItemAdapter;
import com.e_co.wedding.databinding.FragmentReservationConfirmationBinding;
import com.e_co.wedding.model.RequestServiceModel;
import com.e_co.wedding.model.SingleWeddingHallDataModel;
import com.e_co.wedding.model.UserModel;
import com.e_co.wedding.model.WeddingHallModel;
import com.e_co.wedding.preferences.Preferences;
import com.e_co.wedding.uis.activity_base.BaseFragment;
import com.e_co.wedding.uis.activity_home.HomeActivity;

import java.util.ArrayList;
import java.util.List;

public class FragmentReservisionConfirmation extends BaseFragment {
    private FragmentReservationConfirmationBinding binding;
    private HomeActivity activity;
    private BaiscItemAdapter baiscItemAdapter;
    private AdditionalItemAdapter additionalItemAdapter;
    private UserModel userModel;
    private Preferences preferences;
    private boolean login;
    private SingleWeddingHallDataModel singleWeddingHallDataModel;
    private List<WeddingHallModel.ServiceMainItem> serviceMainItemList;
    private List<WeddingHallModel.ServiceExtraItem> serviceExtraItemList;
    private List<String> ids;
    private RequestServiceModel requestServiceModel;
    private WeddingHallModel.OfferModel offerModel = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            singleWeddingHallDataModel = (SingleWeddingHallDataModel) bundle.getSerializable("data");
            offerModel = (WeddingHallModel.OfferModel) bundle.getSerializable("data2");
        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reservation_confirmation, container, false);
        return binding.getRoot();

    }

    private void initView() {
        ids = new ArrayList<>();
        requestServiceModel = new RequestServiceModel();
        serviceExtraItemList = new ArrayList<>();
        serviceMainItemList = new ArrayList<>();
        binding.setModel(singleWeddingHallDataModel.getData());
        binding.setOfferModel(offerModel);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);


        binding.recViewBaiscItem.setLayoutManager(new LinearLayoutManager(activity));
        baiscItemAdapter = new BaiscItemAdapter(activity, this);
        binding.recViewBaiscItem.setAdapter(baiscItemAdapter);
        binding.recViewAdditionalItem.setLayoutManager(new LinearLayoutManager(activity));



        additionalItemAdapter = new AdditionalItemAdapter(activity, this);

        binding.recViewAdditionalItem.setAdapter(additionalItemAdapter);
        if (singleWeddingHallDataModel.getData().getService_main_items() != null) {
            serviceMainItemList.addAll(singleWeddingHallDataModel.getData().getService_main_items());
            baiscItemAdapter.updateList(serviceMainItemList);

        }
        if (singleWeddingHallDataModel.getData().getService_extra_items() != null) {
            serviceExtraItemList.addAll(singleWeddingHallDataModel.getData().getService_extra_items());
            additionalItemAdapter.updateList(serviceExtraItemList);

        }
        binding.btnBook.setOnClickListener(view -> {
            requestServiceModel.setIds(ids);
            requestServiceModel.setWeddingHallModel(singleWeddingHallDataModel.getData());
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", requestServiceModel);
            if (offerModel!=null){
                bundle.putSerializable("data2", offerModel.getId());

            }else {
                bundle.putSerializable("data2", null);

            }

            Navigation.findNavController(binding.getRoot()).navigate(R.id.chooseDayFragment, bundle);
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        NavBackStackEntry currentBackStackEntry = Navigation.findNavController(binding.getRoot()).getCurrentBackStackEntry();
        if (currentBackStackEntry != null) {
            SavedStateHandle savedStateHandle = currentBackStackEntry.getSavedStateHandle();
            if (savedStateHandle.contains("data")) {
                login = savedStateHandle.get("data");
                if (login) {
                    userModel = preferences.getUserData(activity);
                    Log.e("dldll", userModel.getData().getName());
                }
            }
        }


    }

    public void selectItem(String id) {
        if (!ids.contains(id)) {
            ids.add(id);

        } else {
            ids.remove(ids.indexOf(id));
        }
    }
}