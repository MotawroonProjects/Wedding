package com.apps.wedding.uis.activity_home.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.wedding.R;
import com.apps.wedding.adapter.WeddingHallAdapter;
import com.apps.wedding.databinding.FragmentProfileBinding;
import com.apps.wedding.databinding.FragmentSearchBinding;
import com.apps.wedding.model.WeddingHallModel;
import com.apps.wedding.mvvm.FragmentNearMvvm;
import com.apps.wedding.mvvm.FragmentProfileMvvm;
import com.apps.wedding.mvvm.FragmentSearchMvvm;
import com.apps.wedding.share.Common;
import com.apps.wedding.uis.activity_base.BaseFragment;
import com.apps.wedding.uis.activity_home.HomeActivity;
import com.apps.wedding.uis.activity_home.fragments_home_navigaion.FragmentProfile;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class FragmentSearch extends BaseFragment {
    private static final String TAG = FragmentSearch.class.getName();
    private HomeActivity activity;
    private FragmentSearchBinding binding;
    private FragmentSearchMvvm fragmentSearchMvvm;
    private WeddingHallAdapter adapter;
    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }

    private void initView() {
        fragmentSearchMvvm = ViewModelProviders.of(this).get(FragmentSearchMvvm.class);

        fragmentSearchMvvm.getIsLoading().observe(activity, isLoading -> {
            binding.swipeRefresh.setRefreshing(isLoading);
            if (isLoading) {
                adapter.updateList(null);
                binding.tvNoSearchData.setVisibility(View.GONE);
            }
        });
        fragmentSearchMvvm.getWeddingHall().observe(activity, list -> {
            adapter.updateList(list);
            if (list.size() > 0) {
                binding.tvNoSearchData.setVisibility(View.GONE);
            } else {
                binding.tvNoSearchData.setVisibility(View.VISIBLE);

            }
        });

        adapter = new WeddingHallAdapter(activity, this);
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recView.setAdapter(adapter);
        fragmentSearchMvvm.getWeddingHallData(null);

        binding.swipeRefresh.setOnRefreshListener(() -> fragmentSearchMvvm.getWeddingHallData(binding.edtSearch.getText().toString()));

        Observable.create(emitter -> {
            binding.edtSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    emitter.onNext(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        })
                .debounce(2, TimeUnit.SECONDS)
                .distinctUntilChanged()
                .subscribe(query -> {
                    fragmentSearchMvvm.getWeddingHallData(query.toString());

                });
    }


    public void setItemWeddingDetails(String id) {
        Bundle bundle = new Bundle();
        bundle.putString("data", id);
        Navigation.findNavController(binding.getRoot()).navigate(R.id.serviceDetailsFragment, bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Common.CloseKeyBoard(activity, binding.edtSearch);
        disposable.clear();
    }
}