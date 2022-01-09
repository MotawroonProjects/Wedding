package com.e_co.wedding.uis.activity_login;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.e_co.wedding.R;
import com.e_co.wedding.databinding.ActivityLoginBinding;
import com.e_co.wedding.model.LoginModel;
import com.e_co.wedding.uis.activity_base.BaseActivity;
import com.e_co.wedding.uis.activity_verification_code.VerificationCodeActivity;

public class LoginActivity extends BaseActivity {
    private ActivityLoginBinding binding;
    private String phone_code = "";
    private String phone = "";
    private LoginModel model;
    private ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        initView();
    }


    private void initView() {
        model = new LoginModel();
        binding.setModel(model);
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            }
        });

        binding.edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().startsWith("0")){
                    binding.edtPhone.setText("");
                }
            }
        });
        binding.btnLogin.setOnClickListener(v -> {
            if (model.isDataValid(this)) {
                navigateToVerificationCodeActivity();
            }
        });
    }

    private void navigateToVerificationCodeActivity() {
        Intent intent = new Intent(this, VerificationCodeActivity.class);
        intent.putExtra("phone_code", model.getPhone_code());
        intent.putExtra("phone", model.getPhone());
        launcher.launch(intent);
    }
}