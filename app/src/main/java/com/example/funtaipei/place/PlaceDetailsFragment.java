package com.example.funtaipei.place;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.location.Address;
import android.location.Geocoder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.Constraints;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.funtaipei.Common;
import com.example.funtaipei.R;
import com.example.funtaipei.favoritePlace.FavoritePlace;
import com.example.funtaipei.favoritePlace.InsertFavoritePlace;
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


public class PlaceDetailsFragment extends Fragment {
    private FragmentActivity activity;
    private Place place;
    private TextView tvAddress;
    private TextView tvPhone;
    private  TextView tvName;
    private ImageView ivPlace;
    private  Button btFavorite ,btLogin, btRegister,btGood;
    private  CommonTask favoritePlaceTask,  memberGetIdTask, loginTask;
    private GoogleMap map;
    private  int mbNo;
    private EditText etEmail, etPassword;
    private FavoritePlace favoritePlace;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
       place = (Place) (getArguments() != null ? getArguments().getSerializable("place") : null);
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

            place = (Place) bundle.getSerializable("place");
            showPlace(place);
            if (bundle != null) {
                tvName.setText(place.getPC_NAME());
                tvPhone.setText(String.valueOf(place.getPC_PHONE()).trim());
                tvAddress.setText(place.getPC_ADDRESS());

//                Button btCancel = view.findViewById(R.id.btCanceldt);
//                btCancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        /* 回前一個Fragment */
//                        navController.popBackStack();
//                    }
//                });

                //按下收藏按鈕新增至收藏
                btFavorite = view.findViewById(R.id.btFavorite);
                btFavorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {final SharedPreferences pref = activity.getSharedPreferences(Common.PREFERENCES_MEMBER, Context.MODE_PRIVATE);
//                        final int MB_NO = pref.getInt("mb_no", 0);
                        if (mbNo == 0){
                            LayoutInflater inflater = LayoutInflater.from(activity);
                             View view = inflater.inflate(R.layout.fragment_login,null);

                            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
                            final AlertDialog alert = alertDialogBuilder.create();
                            alert.setTitle(R.string.textPleaseLogin);
                            alert.setIcon(R.drawable.alert);
                            alert.setView(view);
                            alert.show();







                            etEmail = view.findViewById(R.id.etEmail);
                            etPassword = view.findViewById(R.id.etPassword);
                            //找不到btLogin
                            btLogin = view.findViewById(R.id.btLogin);

                            btLogin.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String MB_EMAIL = etEmail.getText().toString();
                                    String MB_PASSWORD = etPassword.getText().toString();
                                    String url = Common.URL_SERVER + "/MemberServlet";
                                    JsonObject jsonObject = new JsonObject();
                                    //對應server的login
                                    jsonObject.addProperty("action", "login");
                                    jsonObject.addProperty("email", MB_EMAIL);
                                    jsonObject.addProperty("password", MB_PASSWORD);

                                    loginTask = new CommonTask(url, new Gson().toJson(jsonObject));
                                    boolean isValid = false;
                                    try {
                                        String inStr = loginTask.execute().get();
                                        isValid = Boolean.parseBoolean(inStr);
                                    } catch (Exception e) {
                                        Log.e(TAG, e.toString());
                                    }

                                    if(isValid){
                                        Common.showToast(getActivity(), R.string.textLoginSuccess);

                                        pref.edit().putString("email",MB_EMAIL)
                                                .putString("password", MB_PASSWORD)
                                                .putInt("mb_no",getUserIdByEmail(MB_EMAIL))
                                                .apply();
                                        alert.cancel();
                                    } else {
                                        Common.showToast(getActivity(), R.string.textLoginFail);
                                    }

                                }
                            });

                            //找不到btRegister
                            btRegister = view.findViewById(R.id.btRegister);


                            btRegister.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Navigation.findNavController(v).navigate(R.id.registerFragment);
                                    alert.cancel();
                                }
                            });

                        }


                        if (mbNo != 0) {
//                        int mbNo = pref.getInt("mb_no", 0);
                            int pcId = place.getPC_ID();
                            String url = Common.URL_SERVER + "/FavoritePlaceServlet";
                            InsertFavoritePlace insertFavoritePlace = new InsertFavoritePlace(mbNo, pcId);
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
                LatLng position = new LatLng(place.getLAT(), place.getLNG());
                String title = place.getPC_NAME();
                String snippet = place.getPC_ADDRESS();

                // 打標記並移動地圖至標記所在地
                map.addMarker(new MarkerOptions()
                        .position(position)
                        .title(title)
                        .snippet(snippet));
                moveMap(position);

            }
        });

//         btGood = view.findViewById(R.id.btGood);
//         btGood.setOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View v) {
//                 int id = (int)btGood.getTag();
//                 if( id == R.drawable.like2){
//
//                     btGood.setTag(R.drawable.like);
//                     btGood.setImageResource(R.drawable.like);
//
//                     Common.showToast(getActivity(), R.string.textGoodFail);
//
//                 } else {
//
//                     btGood.setTag(R.drawable.like2);
//                     btGood.setImageResource(R.drawable.like2);
//                     Common.showToast(getActivity(), R.string.textGoodSuccess);
//
//                 }
//
//             }
//         });


    }






    private void showPlace(Place place) {
        String url = Common.URL_SERVER + "/PlaceServlet";
        int id = place.getPC_ID();
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

        tvName.setText(place.getPC_NAME());
        tvPhone.setText(String.valueOf(place.getPC_PHONE()));
        tvAddress.setText(place.getPC_ADDRESS());
        if(place.getLAT() < -180||place.getLNG() < -180)

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

    private int getUserIdByEmail(String mb_email) {
        int mb_no = 0;
        if(Common.networkConnected(activity)){
            String url = Common.URL_SERVER + "/MemberServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getUserIdByEmail");
            jsonObject.addProperty("email", mb_email);
            String jsonOut = jsonObject.toString();
            memberGetIdTask = new CommonTask(url , jsonOut);
            try {
                //傳入string 回傳string轉型 int(id)
                String result = memberGetIdTask.execute().get();
                Log.d(TAG, result);
                mb_no = Integer.parseInt(result);
            }catch (Exception e){
                Log.e(TAG, e.toString());
            }
        }else{
            Common.showToast(activity, R.string.textNoNetwork);
        }
        return mb_no;
    }




}

