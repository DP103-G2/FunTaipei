package com.example.funtaipei.member;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Icon;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.funtaipei.Common;
import com.example.funtaipei.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


public class ListviewFragment extends Fragment {
    private Activity activity;
    private ListView listView;
    private ListAdapter memberAdapter, guestAdapter;
    //private int[] images = {R.drawable.personcontact,R.drawable.logout};
    //private String[] titles = {"會員資料","我的團體","登出"};
    private String[] memberTitle, guestTitle;
    private int[] memberIcon, guestIcon;
    private int[] memberAction, guestAction;
    //List<Map<String, Object>>data = new ArrayList<>();
    private List<Map<String, Object>> memberList, guestList;
    private SharedPreferences preferences;
    private int Mb_no;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        preferences =
                activity.getSharedPreferences(Common.PREFERENCES_MEMBER, Context.MODE_PRIVATE);
        Mb_no = preferences.getInt("mb_no", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_listview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initListMap();
        activity.setTitle("會員專區");
        listView = view.findViewById(R.id.listView);
        if (Mb_no == 0) {
            guestAdapter = new SimpleAdapter(activity, guestList, R.layout.member_list,
                    new String[]{"icon", "title"}, new int[]{R.id.ivListImage, R.id.tvList});
            listView.setAdapter(guestAdapter);
            listView.setOnItemClickListener((new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    Navigation.findNavController(v).navigate(guestAction[position]);
                }
            }));
        } else {
            memberAdapter = new SimpleAdapter(activity, memberList, R.layout.member_list,
                    new String[]{"icon", "title"}, new int[]{R.id.ivListImage, R.id.tvList});
            listView.setAdapter(memberAdapter);
            listView.setOnItemClickListener((new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    NavController navController = Navigation.findNavController(v);
                    if (position != 2) {
                        navController.navigate(memberAction[position]);
                    } else {
                        preferences.edit().putInt("mb_no", 0).commit();
                        navController.popBackStack(R.id.placeListFragment, false);
                    }
                }
            }));
        }
    }

    private void initListMap() {
        memberList = new ArrayList<>();
        memberIcon = new int[]{R.drawable.personcontact, R.drawable.group, R.drawable.login};
        memberTitle = new String[]{getString(R.string.textPersoncontact), getString(R.string.textGroup), getString(R.string.textLogout)};
        memberAction = new int[]{R.id.action_listviewFragment_to_memberDetailFragment, R.id.action_listviewFragment_to_mygroupFragment};
        guestList = new ArrayList<>();
        guestIcon = new int[]{R.drawable.login};
        guestTitle = new String[]{getString(R.string.textLogin)};
        guestAction = new int[]{R.id.action_listviewFragment_to_loginFragment};
        for (int i = 0; i < memberIcon.length; i++) {
            Map<String, Object> memberItem = new HashMap<>();
            memberItem.put("icon", memberIcon[i]);
            memberItem.put("title", memberTitle[i]);
            memberList.add(memberItem);
        }
        for (int i = 0; i < guestIcon.length; i++) {
            Map<String, Object> guestItem = new HashMap<>();
            guestItem.put("icon", guestIcon[i]);
            guestItem.put("title", guestTitle[i]);
            guestList.add(guestItem);
        }

    }
}
//        memberAdaoter = new SimpleAdapter(activity, memberList, R.layout.member_list,
//                new String[]{"icon", "title"}, new int[]{R.id.ivListImage, R.id.tvList});
//        listView.setAdapter(memberAdaoter);
//        listView.setOnItemClickListener((new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//                NavController navController = Navigation.findNavController(v);
//                if (position != 3) {
//                    navController.navigate(memberAction[position]);
//                } else {
//                    preferences.edit().putInt("Mb_no", 0).commit();
//                    navController.popBackStack(R.id.placeListFragment, false);
//                }
//            }
//        }));
//    }



