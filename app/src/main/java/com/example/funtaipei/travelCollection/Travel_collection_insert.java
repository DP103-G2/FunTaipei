package com.example.funtaipei.travelCollection;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.funtaipei.Common;
import com.example.funtaipei.R;
import com.example.funtaipei.task.CommonTask;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class Travel_collection_insert extends Fragment {

    private final static String TAG = "TravelCollection_insert";
    private FragmentActivity activity;
    private ImageView imageView;
    private EditText edtTitle;
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
        imageView = view.findViewById(R.id.imageView);

       edtTitle = view.findViewById(R.id.edtTitle);
        //拍照
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
        //從照片庫選擇照片
        btnPickPic = view.findViewById(R.id.btnPickPic);
        btnPickPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_PICK_PICTURE);
            }
        });

        btnAdd = view.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtTitle.toString().trim();
                if(name.length() <= 0){
                    Common.showToast(getActivity(),R.string.textNameIsInvalid);
                    return;
                }
                if(Common.networkConnected(activity)){
                    String url = Common.URL_SERVER + "/TravelCollectionServlet";
                    TravelCollection travelCollection = new TravelCollection(name);
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "insert");
                    if(image != null){
                        jsonObject.addProperty("imageBase64", Base64.encodeToString(image, Base64.DEFAULT));
                    }
                    int count = 0;
                    try{
                        String result = new CommonTask(url, jsonObject.toString()).execute().get();
                        count =Integer.valueOf(result);
                    } catch (Exception e){
                        Log.d(TAG, "onClick: ");
                    }
                    if (count == 0){
                        Common.showToast(getActivity(), R.string.textInsertFail);
                    } else {
                        Common.showToast(getActivity(), R.string.textInsertSuccess);
                    }
                } else {
                    Common.showToast(getActivity(), R.string.textNoNetwork);
                }
                navigation.popBackStack();
            }
        });

        //取消回到上一頁
        btnCancel = view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigation.popBackStack();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case REQ_TAKE_PICTURE:
                    crop(contentUri);
                    break;
                case REQ_PICK_PICTURE:
                    crop(data.getData());
                    break;
                case REQ_CROP_PICTURE:
                    Uri uri = data.getData();
                    Bitmap bitmap = null;
                    if(uri != null){
                        try{
                            bitmap = BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(uri));
                            imageView.setImageBitmap(bitmap);
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                            image = out.toByteArray();
                        } catch (FileNotFoundException e){
                            Log.d(TAG, "onActivityResult: ");
                        }
                    }
                    if(bitmap != null){
                        imageView.setImageBitmap(bitmap);
                    } else {
                        imageView.setImageResource(R.drawable.no_image);
                    }
                    break;
            }
        }
    }

    private void crop(Uri sourceImageUri){
        File file = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        file = new File(file, "picture_cropped.jpg");
        Uri uri = Uri.fromFile(file);
        //開啟截圖功能
        Intent intent = new Intent("com.android.camera.action.CROP");
        //授權讓截圖程式可以讀取資料
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //設定圖片來源與類型
        intent.setDataAndType(sourceImageUri, "image/*");
        //設定要截圖
        intent.putExtra("crop", "true");
        //設定截圖框大小，0代表user任意調整大小
        intent.putExtra("aspectX", 0);
        intent.putExtra("aspectY", 0);
        //設定圖片輸出寬高，0代表維持原尺寸
        intent.putExtra("outputX", 0);
        intent.putExtra("outputY", 0);
        //是否保持原圖比例
        intent.putExtra("scale", true);
        //設定截圖後圖片位置
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        //設定是否要回傳值
        intent.putExtra("return-data", true);
        if(intent.resolveActivity(activity.getPackageManager()) != null){
            startActivityForResult(intent, REQ_CROP_PICTURE);
        } else {
            Toast.makeText(activity, R.string.textNoImageCropAppFound, Toast.LENGTH_SHORT).show();
        }
    }
}
