package com.example.funtaipei.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.example.funtaipei.member.LoginFragment;
import com.example.funtaipei.R;
import com.example.funtaipei.group.GroupListFragment;
import com.example.funtaipei.place.PlaceListFragment;
import com.example.funtaipei.travelCollection.TravelCollectionFragment;
import com.example.funtaipei.travel.TravelListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private GroupListFragment groupListFragment;
    private PlaceListFragment placeListFragment;
    private LoginFragment loginFragment;
    private TravelListFragment travelListFragment;
    private TravelCollectionFragment travelCollectionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        placeListFragment = new PlaceListFragment();
        groupListFragment = new GroupListFragment();
        loginFragment = new LoginFragment();
        travelListFragment = new TravelListFragment();
        travelCollectionFragment = new TravelCollectionFragment();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(this, R.id.fragment);
        //自動連接Fragment
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }
    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fragment).commit();
    }


}
