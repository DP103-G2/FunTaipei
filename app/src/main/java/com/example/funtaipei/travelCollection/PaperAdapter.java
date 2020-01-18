package com.example.funtaipei.travelCollection;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PaperAdapter extends FragmentPagerAdapter {

    private int numoftabs;

    public PaperAdapter(FragmentManager fm, int numoftabs){
        super(fm);
        this.numoftabs = numoftabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                 return new Fragment();
            case 1:
                return new Fragment();
                default:
                   return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
