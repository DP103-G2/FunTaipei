package com.example.funtaipei.travelDetail;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.funtaipei.R;


public class TravelDetailInsertFragment extends Fragment {


    public TravelDetailInsertFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_travel_detail_insert, container, false);
    }

}
