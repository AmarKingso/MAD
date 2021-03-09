package com.amarso.italk.controller.activity;

import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.amarso.italk.R;
import com.amarso.italk.controller.fragment.ContactFragment;
import com.amarso.italk.controller.fragment.SettingFragment;


public class MainActivity extends FragmentActivity {

    private RadioGroup rg;
    private ContactFragment contactFragment;
    private SettingFragment settingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactFragment = new ContactFragment();
        settingFragment = new SettingFragment();

        rg = findViewById(R.id.main_rg);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Fragment fragment = null;
                switch (checkedId){
                    case R.id.main_contact_rb:
                        fragment = contactFragment;
                        break;
                    case R.id.main_setting_rb:
                        fragment = settingFragment;
                        break;
                }

                //fragment切换
                switchFragment(fragment);
            }
        });

        rg.check(R.id.main_contact_rb);
    }

    private void switchFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_fl, fragment).commit();
    }
}
