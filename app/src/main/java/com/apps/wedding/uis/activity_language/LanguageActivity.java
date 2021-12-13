package com.apps.wedding.uis.activity_language;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.databinding.DataBindingUtil;

import com.apps.wedding.R;
import com.apps.wedding.databinding.ActivityLanguageBinding;
import com.apps.wedding.uis.activity_base.BaseActivity;


public class LanguageActivity extends BaseActivity {
    private ActivityLanguageBinding binding;
    private String lang = "";
    private String selectedLang;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_language);
        initView();
    }

    private void initView() {
        lang = getLang();
        selectedLang = lang;
        if (lang.equals("ar")) {
            binding.flAr.setBackgroundResource(R.drawable.small_stroke_primary);
            binding.flEn.setBackgroundResource(0);

        } else {
            binding.flAr.setBackgroundResource(0);
            binding.flEn.setBackgroundResource(R.drawable.small_stroke_primary);

        }

        binding.cardAr.setOnClickListener(view -> {
            selectedLang = "ar";

            if (!selectedLang.equals(lang)) {
                binding.btnNext.setVisibility(View.VISIBLE);

            } else {
                binding.btnNext.setVisibility(View.INVISIBLE);

            }
            binding.flAr.setBackgroundResource(R.drawable.small_stroke_primary);
            binding.flEn.setBackgroundResource(0);

        });

        binding.cardEn.setOnClickListener(view -> {
            selectedLang = "en";
            if (!selectedLang.equals(lang)) {
                binding.btnNext.setVisibility(View.VISIBLE);

            } else {
                binding.btnNext.setVisibility(View.INVISIBLE);

            }
            binding.flAr.setBackgroundResource(0);
            binding.flEn.setBackgroundResource(R.drawable.small_stroke_primary);
        });

        binding.btnNext.setOnClickListener(view -> {

            Intent intent = getIntent();
            intent.putExtra("lang", selectedLang);
            setResult(RESULT_OK, intent);
            finish();
        });
    }


}