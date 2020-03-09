package com.example.funtaipei;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Common {
    public static final String PREFERENCES_MEMBER = "member";
    public static final Gson GSON = new GsonBuilder().setDateFormat("yyyy/MM/dd").create();
    //this is a appple
    public static String URL_SERVER = "http://10.0.2.2:8080/FunTaipei1";
//    public static String URL_SERVER = "http://10.0.2.2:8080/GP2System/";

    public static boolean networkConnected(Activity activity) {
        ConnectivityManager conManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager != null ? conManager.getActiveNetworkInfo() : null;
        return networkInfo != null && networkInfo.isConnected();
    }

    public static void showToast(Context context, int messageResId){
        Toast.makeText(context,messageResId,Toast.LENGTH_SHORT).show();
    }
    public static void showToast(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
    public static int getmb_No(Context context){
        SharedPreferences pref = context.getSharedPreferences(Common.PREFERENCES_MEMBER, Context.MODE_PRIVATE);
        return  pref.getInt("mb_no",0);
    }
}
