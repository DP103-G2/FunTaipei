package com.example.funtaipei.travelCollection;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.funtaipei.R;



public class Travel_collection_insert extends Fragment {

    private final static String TAG = "TAG_Travel_collection_insert";
    private FragmentActivity activity;


    public Travel_collection_insert() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_travel_collection_insert, container, false);
    }

}
