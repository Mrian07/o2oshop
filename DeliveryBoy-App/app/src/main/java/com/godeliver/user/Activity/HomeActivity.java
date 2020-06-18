package com.godeliver.user.Activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.godeliver.user.R;

public class HomeActivity extends AppCompatActivity {

    TabLayout tabLayout1;
     ViewPager viewPager1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

    }
}
