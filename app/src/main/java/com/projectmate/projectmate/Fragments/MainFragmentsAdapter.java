package com.projectmate.projectmate.Fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainFragmentsAdapter extends FragmentPagerAdapter{
    public MainFragmentsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return new MyProjectFragment();
            case 2:
                return new ChatsFragment();
            case 3:
                return new ActivitiesFragment();
            case 4:
                return new ProfileFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 5;
    }
}
