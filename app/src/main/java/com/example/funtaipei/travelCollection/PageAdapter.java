package com.example.funtaipei.travelCollection;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.funtaipei.PlaceFavoriteFragment;
import com.example.funtaipei.place.FavoritePlaceFragment;

import java.util.zip.Inflater;

public class PageAdapter extends FragmentPagerAdapter {

    private int numofTabs;
    public PageAdapter(FragmentManager fm, int numofTabs){
        super(fm);
        this.numofTabs = numofTabs;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new TravelCollectionFragment();
            case 1:
                return new FavoritePlaceFragment();
                default:
                    return null;
        }
    }

    @Override
    public int getCount() {
        return numofTabs;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
