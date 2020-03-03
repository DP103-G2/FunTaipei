package com.example.funtaipei.group;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.funtaipei.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;


public class MygroupFragment extends Fragment {

    private Activity activity;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public MyGroupPagerAdapter pagerAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mygroup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity.setTitle("我的團體");
        pagerAdapter = new MyGroupPagerAdapter(activity,getChildFragmentManager());
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPaper);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);




    }
    //    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//
//        tabLayout =  findViewById(R.id.tabLayout);
//        doing =  findViewById(R.id.DoingGroup);
//        end =  findViewById(R.id.EndGroup);
//        master =  findViewById(R.id.Master);
//        viewPager = findViewById(R.id.viewPaper);
//        pagerAdapter = new MyGroupPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
//        viewPager.setAdapter(pagerAdapter);
//
//
//    }
}
