package com.example.funtaipei.travel;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.funtaipei.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TravelDetailInsertFragment extends Fragment {


    public TravelDetailInsertFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_travel_detail_insert, container, false);
    }

}
