package com.example.funtaipei.group;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.funtaipei.R;
import com.example.funtaipei.travelCollection.FavoritePlaceFragment;
import com.example.funtaipei.travelCollection.TravelCollectionFragment;

import java.util.List;

public class MyGroupPagerAdapter extends FragmentStatePagerAdapter {
    private static final int[] TAB_TITLES = new int[] {R.string.textdoingGroup, R.string.textEndGroup};
    private Context mContext;

    public MyGroupPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;


    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new DoingGroupFragment();
            case 1:
                return new EndGroupFragment();
            default:
                return null;
        }
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }


    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }
}

