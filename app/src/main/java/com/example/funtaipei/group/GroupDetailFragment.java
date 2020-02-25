package com.example.funtaipei.group;


import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.funtaipei.Common;
import com.example.funtaipei.R;

import com.example.funtaipei.task.CommonTask;
import com.example.funtaipei.task.ImageTask;
import com.example.funtaipei.travel.Travel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Date;


public class GroupDetailFragment extends Fragment {
    private FragmentActivity activity;
    private final static String TAG = "TAG_GroupDetailFragment";
    private ImageView imageView;
    private TextView tvName, tvDate, tvGroupNo, tvPeople, tvNotes;
    private Button btJoin, btTravel;
    private Group group;
    private byte[] image;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = Navigation.findNavController(view);
        activity.setTitle("旅遊團體內容");
        imageView = view.findViewById(R.id.imageView);
        tvName = view.findViewById(R.id.tvName);
        tvDate = view.findViewById(R.id.tvDate);
        tvGroupNo = view.findViewById(R.id.tvGroupNo);
        tvPeople = view.findViewById(R.id.tvPeople);
        tvNotes = view.findViewById(R.id.tvNotes);


        btJoin = view.findViewById(R.id.btJoin);
        btJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(activity)
                        /* 設定標題 */
                        .setTitle(R.string.textJoinGroup)
                        /* 設定圖示 */
                        .setIcon(R.drawable.alert)
                        /* 設定訊息文字 */
                        //.setMessage(R.string.textMessage)
                        /* 設定positive與negative按鈕上面的文字與點擊事件監聽器 */
                        .setPositiveButton(R.string.textYes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                int id = group.getGP_ID();


                                if (Common.networkConnected(activity)) {
                                    String url = Common.URL_SERVER + "/JoinGroupServlet";
                                    JoinGroup jg = new JoinGroup(id, 14, 0);
                                    JsonObject jsonObject = new JsonObject();
                                    jsonObject.addProperty("action", "jgInsert");
                                    jsonObject.addProperty("jg", new Gson().toJson(jg));
                                    int count = 0;
                                    try {
                                        String result = new CommonTask(url, jsonObject.toString()).execute().get();
                                        count = Integer.valueOf(result);
                                    } catch (Exception e) {
                                        Log.e(TAG, e.toString());
                                    }
                                    if (count == 0) {
                                        Common.showToast(getActivity(), R.string.textJoinFail);
                                    } else {
                                        Common.showToast(getActivity(), R.string.textIJoinSuccess);


                                        //這裡報名人數要+1解決

                                        int tvid = group.getTRAVEL_ID();
                                        String name = group.getGP_NAME();
                                        int ENROLLMENT = group.getGP_ENROLLMENT() + 1;
                                        int upper = group.getGP_UPPER();
                                        int lower = group.getGP_LOWER();
                                        Date dateStart = group.getGP_DATESTAR();
                                        Date dateEnd = group.getGP_DATEEND();
                                        Date eventDate = group.getGP_EVENTDATE();
                                        int status = group.getGP_STATUS();
                                        String notes = group.getGP_NOTES();
                                        int MB_NO = group.getMB_NO();

                                        if (Common.networkConnected(activity)) {
                                            String url2 = Common.URL_SERVER + "/GroupServlet";
                                            group.setGroup(tvid, name, ENROLLMENT, upper, lower, dateStart, dateEnd, eventDate, status, notes, MB_NO);

                                            jsonObject.addProperty("action", "groupUpdate");
                                            Gson gson = new GsonBuilder().setDateFormat("yyyy/MM/dd").create();
                                            jsonObject.addProperty("group", gson.toJson(group));
                                            if (image != null) {
                                                jsonObject.addProperty("imageBase64", Base64.encodeToString(image, Base64.DEFAULT));
                                            }
                                            //int count2 = 0;
                                            try {
                                                String result = new CommonTask(url2, jsonObject.toString()).execute().get();
                                                //count = Integer.valueOf(result);
                                            } catch (Exception e) {
                                                Log.e(TAG, e.toString());
                                            }
                                        }
                                    }

                                } else {
                                    Common.showToast(getActivity(), R.string.textNoNetwork);
                                }
                                /* 回前一個Fragment */
                                //navController.popBackStack();
                                showGroup();


                            }
                        })
                        .setNegativeButton(R.string.textNo, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                /* 關閉對話視窗 */
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });
        btTravel = view.findViewById(R.id.btTravel);
        btTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int travelId = group.getTRAVEL_ID();
                Bundle bundle = new Bundle();
                bundle.putInt("travelId", travelId);
                Navigation.findNavController(view).navigate(R.id.action_groupDetailFragment_to_travelDetailFragment, bundle);
            }
        });


        Bundle bundle = getArguments();
        if (bundle == null || bundle.getSerializable("group") == null) {
            Common.showToast(activity, R.string.textNoGroupsFound);
            navController.popBackStack();
            return;
        }
        group = (Group) bundle.getSerializable("group");
        showGroup();

    }

    private void showGroup() {
        String url = Common.URL_SERVER + "/GroupServlet";
        int id = group.getGP_ID();
        int imageSize = getResources().getDisplayMetrics().heightPixels / 2;
        Bitmap bitmap = null;
        try {
            bitmap = new ImageTask(url, id, imageSize).execute().get();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.drawable.no_image);
        }
        tvNotes.setText(group.getGP_NOTES());
        tvName.setText(group.getGP_NAME());
        tvGroupNo.setText("團體編號：" + String.valueOf(group.getGP_ID()));
        tvDate.setText("活動日期：" + new SimpleDateFormat("yyyy/MM/dd").format(group.getGP_EVENTDATE()) + new SimpleDateFormat("（E）").format(group.getGP_EVENTDATE()));
        tvPeople.setText("可報 " + String.valueOf(group.getGP_UPPER() - group.getGP_ENROLLMENT()) + " 團位 " + String.valueOf(group.getGP_UPPER()));
    }
}