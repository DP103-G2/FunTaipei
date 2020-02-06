package com.example.funtaipei.travelCollection;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.funtaipei.Common;
import com.example.funtaipei.R;

import java.io.File;
import java.util.List;


public class Travel_collection_insert extends Fragment {

    private final static String TAG = "TAG_Travel_collection_insert";
    private FragmentActivity activity;
    private ImageView imageView;
    private EditText travelName;
    private Button btnTakePic, btnPickPic, btnAdd, btnCancel;
    private byte[] image;
    private static final int REQ_TAKE_PICTURE = 0;
    private static final int REQ_PICK_PICTURE = 1;
    private static final int REQ_CROP_PICTURE = 2;
    private Uri contentUri;

    public Travel_collection_insert() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_travel_collection_insert, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navigation = Navigation.findNavController(view);

        travelName = view.findViewById(R.id.travelName);

        btnTakePic = view.findViewById(R.id.btnTakePic);
        btnTakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                file = new File(file, "picture.jpg");
                contentUri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".provider", file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);

                if (intent.resolveActivity(activity.getPackageManager()) != null){
                    startActivityForResult(intent, REQ_TAKE_PICTURE);
                }else{
                    Common.showToast(getActivity(), R.string.textNoCameraApp);
                }
            }
        });

        btnPickPic = view.findViewById(R.id.btnPickPic);
        btnPickPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_PICK_PICTURE);
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = travelName.getText().toString().trim();
                if (name.length() < 0){
                    Common.showToast(getActivity(), R.string.textNameIsInvalid);
                    return;
                }
                List<TravelCollection> travelCollectionList;
                if (Common.networkConnected(activity)){
                    String url = Common.URL_SERVER + "TravelCollectionServlet";
                    //20200119卡住
                }
            }
        });

    }
}
