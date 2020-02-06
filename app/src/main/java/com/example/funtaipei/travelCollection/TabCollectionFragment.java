package com.example.funtaipei.travelCollection;


import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.funtaipei.R;
import com.google.android.material.tabs.TabLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabCollectionFragment extends Fragment {

        private TabLayout tabLayout;
        private ViewPager viewPager;
        private ViewPaperAdapter adapter;
        private View v;
        private Activity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        activity.setTitle("我的最愛");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_tab_collection_fragment, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new ViewPaperAdapter(activity, getChildFragmentManager());
        tabLayout = v.findViewById(R.id.tabLayout);
        viewPager = v.findViewById(R.id.viewPaper);

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);





    }

    @Override
    public void onStop() {
        super.onStop();
    }

    //    private void initPaper(){
//        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
//            @NonNull
//            @Override
//            public Fragment getItem(int position) {
//                Fragment fragment = new Fragment();
//                if (fragment != null){
//                    switch (position){
//                        case 0:
//                            fragment = new TravelCollectionFragment();
//                            break;
//                        case 1:
//                            fragment = new FavoritePlaceFragment();
//                            break;
//                    }
//                }
//                return fragment;
//            }
//
//            @Override
//            public int getCount() {
//                return 2;
//            }
//        });
//
//        tabLayout.setupWithViewPager(viewPager);
//        viewPager.setCurrentItem(0);
////        tabLayout.getTabAt(0).setCustomView(getTabView(0));
////        tabLayout.getTabAt(1).setCustomView(getTabView(1));
//        initTab();
//    }
//
//    private void initTab(){
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
//
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                View view = tab.getCustomView();
//                ImageView imageView = view.findViewById(R.id.travelCollectionImage);
//                TextView textView = view.findViewById(R.id.Group_Name);
//                textView.setTextColor(Color.parseColor("#000000"));
//
//
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//                View view = tab.getCustomView();
//                ImageView imageView = view.findViewById(R.id.travelCollectionImage);
//                TextView textView = view.findViewById(R.id.Group_Name);
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//    }
//
//    private void getTabView(){
//        View view = LayoutInflater.from(activity).inflate(R.layout.travelcollection_item, null);
//        TextView textView = view.findViewById(R.id.Group_Name);
//        ImageView imageView = view.findViewById(R.id.travelCollectionImage);
//    }

}
