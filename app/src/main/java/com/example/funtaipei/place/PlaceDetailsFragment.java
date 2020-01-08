package com.example.funtaipei.place;


import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
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
import com.example.funtaipei.task.ImageTask;

import static android.content.ContentValues.TAG;


public class PlaceDetailsFragment extends Fragment {
    private FragmentActivity activity;
    private Place place;
    private TextView tvAddress;
    private TextView tvPhone;
    private  TextView tvName;
    private ImageView ivPlace;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        place = (Place) (getArguments() != null ? getArguments().getSerializable("place") : null);
    }


    public PlaceDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_place_details, container, false);

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

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
            showPlace();
            if (bundle != null) {
                tvName.setText(place.getPC_NAME());
                tvPhone.setText(String.valueOf(place.getPC_PHONE()).trim());
                tvAddress.setText(place.getPC_ADDRESS());

                Button btCancel = view.findViewById(R.id.btCanceldt);
                btCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /* 回前一個Fragment */
                        navController.popBackStack();
                    }
                });
            }
        }
    }






    private void showPlace() {
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
    }
}
