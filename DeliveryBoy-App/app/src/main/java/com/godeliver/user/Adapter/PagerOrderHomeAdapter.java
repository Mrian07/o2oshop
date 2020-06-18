package com.godeliver.user.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.godeliver.user.Fragment.My_Past_Order;
import com.godeliver.user.Fragment.My_Pending_Order;
import com.godeliver.user.Fragment.NextOrder;
import com.godeliver.user.Fragment.TodayOrder;


/*
public class PagerOrderHomeAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerOrderHomeAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                TodayOrder tab1 = new TodayOrder();
                return tab1;
            case 1:
                NextOrder tab2 = new NextOrder();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}*/
