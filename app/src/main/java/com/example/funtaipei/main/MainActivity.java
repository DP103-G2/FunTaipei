package com.example.funtaipei.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.funtaipei.Splash;
import com.example.funtaipei.member.LoginFragment;
import com.example.funtaipei.R;
import com.example.funtaipei.group.GroupListFragment;
import com.example.funtaipei.place.PlaceListFragment;
import com.example.funtaipei.travelCollection.TabCollectionFragment;
import com.example.funtaipei.travel.TravelListFragment;
import com.example.funtaipei.travelCollection.TabCollectionFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;
    private GroupListFragment groupListFragment;
    private PlaceListFragment placeListFragment;
    private LoginFragment loginFragment;
    private TravelListFragment travelListFragment;
//    private TravelCollectionFragment travelCollectionFragment;
    private TabCollectionFragment tabCollectionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        placeListFragment = new PlaceListFragment();
        groupListFragment = new GroupListFragment();
        loginFragment = new LoginFragment();
        travelListFragment = new TravelListFragment();
        tabCollectionFragment = new TabCollectionFragment();

//        travelCollectionFragment = new TravelCollectionFragment();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(this, R.id.fragment);
        //自動連接Fragment
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }



}
