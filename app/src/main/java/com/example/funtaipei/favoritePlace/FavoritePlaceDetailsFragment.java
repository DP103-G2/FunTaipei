package com.example.funtaipei.favoritePlace;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.location.Address;
import android.location.Geocoder;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Constraints;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.funtaipei.Common;
import com.example.funtaipei.R;
import com.example.funtaipei.favoritePlace.InsertFavoritePlace;
import com.example.funtaipei.place.Place;
import com.example.funtaipei.task.CommonTask;
import com.example.funtaipei.task.ImageTask;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import static android.content.ContentValues.TAG;


public class FavoritePlaceDetailsFragment extends Fragment {
    private FragmentActivity activity;
    private Place place;
    private FavoritePlace favoritePlace;
    private TextView tvAddress;
    private TextView tvPhone;
    private  TextView tvName;
    private ImageView ivPlace;
    private  Button btFavorite;
    private  CommonTask favoritePlaceTask;
    private GoogleMap map;
    private  int mbNo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        favoritePlace = (FavoritePlace) (getArguments() != null ? getArguments().getSerializable("place") : null);
        mbNo = Common.getmb_No(activity);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_place_details, container, false);

    }


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        final NavController navController = Navigation.findNavController(view);
        super.onViewCreated(view, savedInstanceState);
        activity.setTitle("旅遊點細節");
        ivPlace = view.findViewById(R.id.ivPlace);
        tvName = view.findViewById(R.id.tvName);
        tvPhone = view.findViewById(R.id.tvPhone);
        tvAddress = view.findViewById(R.id.tvAddress);
        Bundle bundle = getArguments();
        if (bundle != null) {
            place = (Place) bundle.getSerializable("favoriteplace");
            showPlace(place);
            if (bundle != null) {
                tvName.setText(favoritePlace.getPc_name());
                tvPhone.setText(String.valueOf(favoritePlace.getPC_PHONE()).trim());
                tvAddress.setText(favoritePlace.getPc_address());
//
//                Button btCancel = view.findViewById(R.id.btCanceldt);
//                btCancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        /* 回前一個Fragment */
//                        navController.popBackStack();
//                    }
//                });
//
                //按下收藏按鈕新增至收藏
                btFavorite = view.findViewById(R.id.btFavorite);
                btFavorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


//                        int mbNo = pref.getInt("mb_no", 0);
                        int pcId =favoritePlace.getPc_id();
                        String url = Common.URL_SERVER + "/FavoritePlaceServlet";
                        InsertFavoritePlace insertFavoritePlace = new InsertFavoritePlace(mbNo,pcId);
                        // InsertFavoritePlace insertFavoritePlace = new InsertFavoritePlace(1,2);
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("action", "favoritePlaceInsert");
                        Gson gson = new GsonBuilder().setDateFormat("yyyy/MM/dd").create();
                        jsonObject.addProperty("favoritePlace", gson.toJson(insertFavoritePlace));
                        int count = 0;
                        try {
                            String result = new CommonTask(url, jsonObject.toString()).execute().get();
                            count = Integer.valueOf(result);
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                        if (count == 0) {
                            Common.showToast(getActivity(), R.string.textFavoriteFail);
                        } else {
                            Common.showToast(getActivity(), R.string.textFavoriteSuccess);
                        }

                    }


                });


            }
        }

        // 地圖
        MapView mapView = view.findViewById(R.id.mapView);
        // 呼叫MapView.onCreate()與onStart()才可正常顯示地圖
        mapView.onCreate(savedInstanceState);
        mapView.onStart();
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                //地圖樣式（測試中）
                // map.MapType = GoogleMap.MapTypeHybrid;



//               LatLng position = (new LatLng(50.379444, 2.773611));
                LatLng position = new LatLng(favoritePlace.getLAT(), favoritePlace.getLNG());
                String title = favoritePlace.getPc_name();
                String snippet = favoritePlace.getPc_address();

                // 打標記並移動地圖至標記所在地
                map.addMarker(new MarkerOptions()
                        .position(position)
                        .title(title)
                        .snippet(snippet));
                moveMap(position);

            }
        });
    }






    private void showPlace(Place Place) {
        String url = Common.URL_SERVER + "/PlaceServlet";
        int id = favoritePlace.getPc_id();
        int imageSize = getResources().getDisplayMetrics().widthPixels / 3;
        Bitmap bitmap = null;
        try {
            bitmap = new ImageTask(url, id, imageSize).execute().get();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        if (bitmap != null) {
            ivPlace.setImageBitmap(bitmap);
        } else {
            ivPlace.setImageResource(R.drawable.no_image);
        }

        tvName.setText(favoritePlace.getPc_name());
        tvPhone.setText(String.valueOf(favoritePlace.getPC_PHONE()));
        tvAddress.setText(favoritePlace.getPc_address());
        if(favoritePlace.getLAT() < -180||favoritePlace.getLNG() < -180)

        {
            Common.showToast(activity, "place not found");
            return;
        }




    }









    private void moveMap(LatLng latLng) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(17)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory
                .newCameraPosition(cameraPosition);
        map.animateCamera(cameraUpdate);
    }



}

