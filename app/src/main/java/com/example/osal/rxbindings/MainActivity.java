package com.example.osal.rxbindings;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.osal.rxbindings.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    public RxViewModel vm = new RxViewModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setVm(vm);
    }
}
