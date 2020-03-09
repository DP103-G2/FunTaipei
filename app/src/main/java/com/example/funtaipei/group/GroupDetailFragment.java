package com.example.funtaipei.group;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.funtaipei.Common;
import com.example.funtaipei.R;
import com.example.funtaipei.task.CommonTask;
import com.example.funtaipei.task.ImageTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class GroupDetailFragment extends Fragment {
    private FragmentActivity activity;
    private final static String TAG = "TAG_GroupDetailFragment";
    private ImageView imageView;
    private TextView tvName, tvDate, tvGroupNo, tvPeople, tvNotes, textView;
    private Button btLogin, btRegister;
    private EditText etEmail, etPassword;
    private Button btJoin, btTravel;
    private Group group;
    private byte[] image;
    private CommonTask memberGetIdTask, loginTask, joinGroupTask, deleteTask;
    private JoinGroup joinGroup;
    private int MB_NO;

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
        textView = view.findViewById(R.id.textView);

        final SharedPreferences pref = activity.getSharedPreferences(Common.PREFERENCES_MEMBER, Context.MODE_PRIVATE);
        MB_NO = pref.getInt("mb_no", 0);
        Bundle bundle = getArguments();

//        if (bundle == null || bundle.getSerializable("group") == null) {
//            Common.showToast(activity, R.string.textNoGroupsFound);
//            navController.popBackStack();
//            return;
//        }
        if (bundle.getSerializable("group") != null) {
            group = (Group) bundle.getSerializable("group");
        }
        if (bundle.getSerializable("gpid") != null) {
            group = (Group) bundle.getSerializable("gpid");
        }
        showGroup();
        getCheck();
        final Calendar curDate = Calendar.getInstance();

        if (joinGroup != null) {
            if (joinGroup.getMASTER() == 1) {
                btJoin.setText("查看參團名單");
                btJoin.setBackgroundColor(Color.parseColor("#72E774"));
//                btJoin.getBackground().setColorFilter(0xFF000000, android.graphics.PorterDuff.Mode.MULTIPLY );
            } else if ( group.getGP_DATEEND().getTime() < curDate.getTimeInMillis() && joinGroup.getMASTER() == 0) {
                btJoin.setVisibility(View.GONE);
            } else {
                btJoin.setText("取消參團");
                btJoin.setTextColor(Color.parseColor("#E91E63"));
                btJoin.setBackgroundColor(View.GONE);
            }

        }


        btJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final SharedPreferences pref = activity.getSharedPreferences(Common.PREFERENCES_MEMBER, Context.MODE_PRIVATE);
                final int mbno = pref.getInt("mb_no", 0);
                if (mbno != 0) {
                    if (Common.networkConnected(activity)) {
                        String url = Common.URL_SERVER + "/JoinGroupServlet";
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("action", "findByMb");
                        jsonObject.addProperty("mb", mbno);
                        jsonObject.addProperty("gp", group.getGP_ID());
                        String jsonOut = jsonObject.toString();
                        joinGroupTask = new CommonTask(url, jsonOut);
                        try {
                            String jsonIn = joinGroupTask.execute().get();
                            joinGroup = new Gson().fromJson(jsonIn, JoinGroup.class);
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                    } else {
                        Common.showToast(activity, R.string.textNoNetwork);
                    }
                }
                if (joinGroup != null) {
                    if (joinGroup.getMASTER() == 1) {
                        Bundle bundle = new Bundle();
                        int id = group.getGP_ID();
                        bundle.putInt("gpid", id);
                        Navigation.findNavController(view).navigate(R.id.myGroupMemberFragment, bundle);
                    } else {
                        new AlertDialog.Builder(activity)
                                /* 設定標題 */
                                .setTitle("確定要取消 \"" + group.getGP_NAME() + "\" 嗎？")
                                /* 設定圖示 */
                                .setIcon(R.drawable.alert)
                                /* 設定訊息文字 */
                                //.setMessage(R.string.textMessage)
                                /* 設定positive與negative按鈕上面的文字與點擊事件監聽器 */
                                .setPositiveButton(R.string.textYes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        int gp = group.getGP_ID();


                                        if (Common.networkConnected(activity)) {
                                            String url = Common.URL_SERVER + "/JoinGroupServlet";
                                            JsonObject jsonObject = new JsonObject();
                                            jsonObject.addProperty("action", "delete");
                                            jsonObject.addProperty("mb", mbno);
                                            jsonObject.addProperty("gp", gp);
                                            int count = 0;
                                            try {
                                                deleteTask = new CommonTask(url, jsonObject.toString());
                                                String result = deleteTask.execute().get();
                                                count = Integer.valueOf(result);
                                            } catch (Exception e) {
                                                Log.e(TAG, e.toString());
                                            }
                                            if (count != 0) {
                                                Common.showToast(getActivity(), R.string.textCancelGroupTrue);
                                                btJoin.setText("立即報名");
                                                btJoin.setTextColor(Color.parseColor("#FCFAFA"));
                                                btJoin.setBackgroundColor(Color.parseColor("#E91E63"));

                                                int tvid = group.getTRAVEL_ID();
                                                String name = group.getGP_NAME();
                                                int ENROLLMENT = group.getGP_ENROLLMENT() - 1;
                                                int upper = group.getGP_UPPER();
                                                int lower = group.getGP_LOWER();
                                                Date dateStart = group.getGP_DATESTAR();
                                                Date dateEnd = group.getGP_DATEEND();
                                                Date eventDate = group.getGP_EVENTDATE();
                                                int status = group.getGP_STATUS();
                                                String notes = group.getGP_NOTES();


                                                if (Common.networkConnected(activity)) {
                                                    String url2 = Common.URL_SERVER + "/GroupServlet";
                                                    group.setGroup(tvid, name, ENROLLMENT, upper, lower, dateStart, dateEnd, eventDate, status, notes);

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
                                            } else {
                                                Common.showToast(getActivity(), R.string.textCancelGroupFail);
                                            }
                                        } else {
                                            Common.showToast(activity, R.string.textNoNetwork);
                                        }
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
                }


                if (mbno == 0) {
                    LayoutInflater inflater = LayoutInflater.from(activity);
                    final View v = inflater.inflate(R.layout.fragment_login, null);

                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
                    final AlertDialog alert = alertDialogBuilder.create();
                    alert.setTitle(R.string.textPleaseLogin);
                    alert.setIcon(R.drawable.alert);
                    alert.setView(v);
                    alert.show();


                    etEmail = v.findViewById(R.id.etEmail);
                    etPassword = v.findViewById(R.id.etPassword);
                    btLogin = v.findViewById(R.id.btLogin);
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

                            if (isValid) {
                                Common.showToast(getActivity(), R.string.textLoginSuccess);

                                pref.edit().putString("email", MB_EMAIL)
                                        .putString("password", MB_PASSWORD)
                                        .putInt("mb_no", getUserIdByEmail(MB_EMAIL))
                                        .apply();
                                getJoinGroup();
                                if (joinGroup != null) {
                                    if (joinGroup.getMASTER() == 1) {
                                        btJoin.setText("查看參團名單");
                                        btJoin.setBackgroundColor(Color.parseColor("#72E774"));
                                    } else if (group.getGP_DATEEND().getTime() > curDate.getTimeInMillis()) {
                                        btJoin.setText("取消參團");
                                        btJoin.setTextColor(Color.parseColor("#E91E63"));
                                        btJoin.setBackgroundColor(View.GONE);
                                    }
                                }
                            }
                            showGroup();
                            alert.cancel();


                        }
                    });
                    btRegister = v.findViewById(R.id.btRegister);
                    btRegister.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Navigation.findNavController(view).navigate(R.id.registerFragment);
                            alert.cancel();
                        }
                    });

                } else if (joinGroup == null) {
                    final SharedPreferences pref2 = activity.getSharedPreferences(Common.PREFERENCES_MEMBER, Context.MODE_PRIVATE);
                    final int mbno2 = pref.getInt("mb_no", 0);
                    new AlertDialog.Builder(activity)
                            /* 設定標題 */
                            .setTitle("確定要報名 \"" + group.getGP_NAME() + "\" 嗎？")
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
                                        JoinGroup jg = new JoinGroup(id, mbno2, 0, 0);
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
                                            btJoin.setText("取消參團");
                                            btJoin.setTextColor(Color.parseColor("#E91E63"));
                                            btJoin.setBackgroundColor(View.GONE);

                                            //這裡報名人數要+1已解決

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


                                            if (Common.networkConnected(activity)) {
                                                String url2 = Common.URL_SERVER + "/GroupServlet";
                                                group.setGroup(tvid, name, ENROLLMENT, upper, lower, dateStart, dateEnd, eventDate, status, notes);

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
//
                                    } else {
                                        Common.showToast(getActivity(), R.string.textNoNetwork);
                                    }
                                    /* 回前一個Fragment */
                                    //navController.popBackStack();

                                    getCheck();
                                    if (joinGroup != null) {
                                        if (joinGroup.getMASTER() == 1) {
                                            btJoin.setText("查看參團名單");
                                            btJoin.setBackgroundColor(Color.parseColor("#72E774"));
                                        }
                                    }
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


    }

    private void getJoinGroup() {
        final SharedPreferences pref = activity.getSharedPreferences(Common.PREFERENCES_MEMBER, Context.MODE_PRIVATE);
        int mbno2 = pref.getInt("mb_no", 0);
        if (mbno2 != 0) {
            if (Common.networkConnected(activity)) {
                String url2 = Common.URL_SERVER + "/JoinGroupServlet";
                JsonObject jsonObject2 = new JsonObject();
                jsonObject2.addProperty("action", "findByMb");
                jsonObject2.addProperty("mb", mbno2);
                jsonObject2.addProperty("gp", group.getGP_ID());
                String jsonOut = jsonObject2.toString();
                joinGroupTask = new CommonTask(url2, jsonOut);
                try {
                    String jsonIn = joinGroupTask.execute().get();
                    joinGroup = new Gson().fromJson(jsonIn, JoinGroup.class);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            } else {
                Common.showToast(activity, R.string.textNoNetwork);
            }
        } else {


            Common.showToast(getActivity(), R.string.textLoginFail);
        }
    }


    private void getCheck() {
        if (MB_NO != 0) {
            if (Common.networkConnected(activity)) {
                String url = Common.URL_SERVER + "/JoinGroupServlet";
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "findByMb");
                jsonObject.addProperty("mb", MB_NO);
                jsonObject.addProperty("gp", group.getGP_ID());
                String jsonOut = jsonObject.toString();
                joinGroupTask = new CommonTask(url, jsonOut);
                try {
                    String jsonIn = joinGroupTask.execute().get();
                    joinGroup = new Gson().fromJson(jsonIn, JoinGroup.class);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            } else {
                Common.showToast(activity, R.string.textNoNetwork);
            }
        }
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
        textView.setText("最後報名日期：" + new SimpleDateFormat("yyyy/MM/dd").format(group.getGP_DATEEND()) + new SimpleDateFormat("（E）").format(group.getGP_DATEEND()));
        tvNotes.setText(group.getGP_NOTES());
        tvName.setText(group.getGP_NAME());
        tvGroupNo.setText("團體編號：" + String.valueOf(group.getGP_ID()));
        tvDate.setText("活動日期：" + new SimpleDateFormat("yyyy/MM/dd").format(group.getGP_EVENTDATE()) + new SimpleDateFormat("（E）").format(group.getGP_EVENTDATE()));
        tvPeople.setText("可報 " + String.valueOf(group.getGP_UPPER() - group.getGP_ENROLLMENT()) + " 團位 " + String.valueOf(group.getGP_UPPER()));
    }

    private int getUserIdByEmail(String mb_email) {
        int mb_no = 0;
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "/MemberServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getUserIdByEmail");
            jsonObject.addProperty("email", mb_email);
            String jsonOut = jsonObject.toString();
            memberGetIdTask = new CommonTask(url, jsonOut);
            try {
                //傳入string 回傳string轉型 int(id)
                String result = memberGetIdTask.execute().get();
                Log.d(TAG, result);
                mb_no = Integer.parseInt(result);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }
        return mb_no;
    }


    @Override
    public void onStop() {
        super.onStop();
        if (memberGetIdTask != null) {
            memberGetIdTask.cancel(true);
            memberGetIdTask = null;
        }
        if (loginTask != null) {
            loginTask.cancel(true);
            loginTask = null;
        }
        if (joinGroupTask != null) {
            joinGroupTask.cancel(true);
            joinGroupTask = null;
        }
        if (deleteTask != null) {
            deleteTask.cancel(true);
            deleteTask = null;
        }

    }
}