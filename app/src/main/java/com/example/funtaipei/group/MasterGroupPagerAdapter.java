package com.example.funtaipei.group;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.funtaipei.R;

public class MasterGroupPagerAdapter extends FragmentStatePagerAdapter {
    private static final int[] TAB_TITLES = new int[] {R.string.joining, R.string.textGroupFinish};
    private Context mContext;

    public MasterGroupPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;


    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new MasterFragment();
            case 1:
                return new MasterFinishFragment();
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



