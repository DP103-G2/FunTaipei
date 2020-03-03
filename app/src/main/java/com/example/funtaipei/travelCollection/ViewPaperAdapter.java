package com.example.funtaipei.travelCollection;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.funtaipei.R;
import com.example.funtaipei.travelCollection.FavoritePlaceFragment;

import java.util.ArrayList;
import java.util.List;

public class ViewPaperAdapter extends FragmentStatePagerAdapter {

   private static final int[] TAB_TITLES = new int[] {R.string.tab1, R.string.tab2};

    private Context mContext;




    public ViewPaperAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
       switch (position){
           case 0:
               return TravelCollectionFragment.newInstance();
           case 1:
               return FavoritePlaceFragment.newInstance();
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

        return 2;
    }
}
