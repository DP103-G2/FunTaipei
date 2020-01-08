package com.example.funtaipei.travelCollection;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.funtaipei.Common;
import com.example.funtaipei.R;
import com.example.funtaipei.task.CommonTask;
import com.example.funtaipei.task.ImageTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static java.lang.String.valueOf;


public class TravelCollectionFragment extends Fragment {
    private Activity activity;
    private RecyclerView travelCollectionRecycleView;
    private ImageTask travelCollectionImageTask;
    private CommonTask travelCollectionGetAllTask;
    private CommonTask travelCollectionDeleteTask;
    private ImageView travelColleationImage, travelCollection_Memberimageview;
    private TextView travelCollection_MemberName, travelCollection_MemberEmail, travelCollection_MemberId;
    private List<TravelCollection> travelCollections;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity.setTitle("行程收藏");
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_travel_collection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TravelCollection資料
        travelCollectionRecycleView = view.findViewById(R.id.travelCollectionRecycleview);
        travelCollectionRecycleView.setLayoutManager(new LinearLayoutManager(activity));
        travelCollections = getTravelCollections();
        showTravelCollections(travelCollections);


//        Member資料
//        travelCollection_member_recycleview = view.findViewById(R.id.travelCollection_member_recycleview);
//        travelCollection_member_recycleview.setLayoutManager(new LinearLayoutManager(activity));
//        travelCollection_member_recycleview = (RecyclerView) getTravelCollectionMember();
//        showTravelCollectionMembers(travelCollections);
        travelCollection_Memberimageview = view.findViewById(R.id.travelCollection_Memberimageview);
        travelCollection_MemberId = view.findViewById(R.id.travelCollection_MemberId);
        travelCollection_MemberName = view.findViewById(R.id.travelCollection_MemberName);
        travelCollection_MemberEmail = view.findViewById(R.id.travelCollection_MemberEmail);



    }

//    private List<TravelCollection> getTravelCollectionMember() {
//        List<TravelCollection> travelCollections = null;
//        if (Common.networkConnected(activity)) {
//            String url = Common.URL_SERVER + "TravelCollectionServlet";
//            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("action", "getAll");
//            String jsonOut = jsonObject.toString();
//            try {
//                String jsonIn = travelCollectionGetAllTask.execute().get();
//                Type listType = new TypeToken<List<TravelCollection>>() {
//                }.getType();
//                travelCollections = new Gson().fromJson(jsonIn, listType);
//            } catch (Exception e) {
//                Log.d(TAG, "getTravelCollections: ");
//            }
//        } else {
//            Common.showToast(activity, R.string.textNoNetwork);
//        }
//        return travelCollections;
//    }
//
//    private void showTravelCollectionMembers(List<TravelCollection> travelCollections){
//        if(travelCollections == null || travelCollections.isEmpty()){
//            Common.showToast(activity, "No Member Found");
//            return;
//        }
//        TravelCollectionMemberAdapter travelCollectionMemberAdapter = (TravelCollectionMemberAdapter) travelCollection_member_recycleview.getAdapter();
//        if( travelCollectionMemberAdapter == null){
//            travelCollection_member_recycleview.setAdapter(new TravelCollectionAdapter(activity, travelCollections));
//        }else{
//            travelCollectionMemberAdapter.setTravelCollections(travelCollections);
//            travelCollectionMemberAdapter.notifyDataSetChanged();
//        }
//    }
//
//    private class TravelCollectionMemberAdapter extends RecyclerView.Adapter<TravelCollectionMemberAdapter.MyViewHolder>{
//        private LayoutInflater layoutInflater;
//        private List<TravelCollection> travelCollections;
//        private int imageSize;
//
//        TravelCollectionMemberAdapter(Context context, List<TravelCollection> travelCollections){
//            layoutInflater = LayoutInflater.from(context);
//            this.travelCollections = travelCollections;
//            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
//        }
//
//        void setTravelCollections(List<TravelCollection> travelCollections){
//            this.travelCollections = travelCollections;
//        }
//
//        class MyViewHolder extends RecyclerView.ViewHolder{
//            ImageView imageView;
//            TextView travelCollection_MemberId, travelCollection_MemberName, travelCollection_MemberEmail;
//
//            MyViewHolder(View itemView){
//                super(itemView);
//                imageView = itemView.findViewById(R.id.travelCollection_Memberimageview);
//                travelCollection_MemberId = itemView.findViewById(R.id.travelCollection_MemberId);
//                travelCollection_MemberName = itemView.findViewById(R.id.travelCollection_MemberName);
//                travelCollection_MemberEmail = itemView.findViewById(R.id.travelCollection_MemberEmail);
//            }
//        }
//
//        @Override
//        public int getItemCount() {
//             return travelCollections.size();
//        }
//
//        @NonNull
//        @Override
//        public TravelCollectionMemberAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View itemView = layoutInflater.inflate(R.layout.travelcollection_member_item, parent, false);
//            return new MyViewHolder(itemView);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull TravelCollectionMemberAdapter.MyViewHolder holder, int position) {
//            final TravelCollection travelCollection = travelCollections.get(position);
//            String url = Common.URL_SERVER + "TravelCollectionServlet";
//            int id = travelCollection.getMb_no();
//            travelCollectionImageTask = new ImageTask(url, id, imageSize, holder.imageView);
//            travelCollectionImageTask.execute();
//            holder.travelCollection_MemberId.setText(String.valueOf(travelCollection.getMb_no()));
//            holder.travelCollection_MemberName.setText(travelCollection.getMb_name());
//            holder.travelCollection_MemberEmail.setText(travelCollection.getMb_email());
//        }
//
//
//    }


    //--------------------------------以下是TravelCollection資料-------------------------------------------------------------

    private List<TravelCollection> getTravelCollections() {
        List<TravelCollection> travelCollections = null;
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "TravelCollectionServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAll");
            String jsonOut = jsonObject.toString();
            travelCollectionGetAllTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = travelCollectionGetAllTask.execute().get();
                Type listType = new TypeToken<List<TravelCollection>>() {
                }.getType();
                travelCollections = new Gson().fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.d(TAG, "getTravelCollections: ");
            }
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }
        return travelCollections;
    }

    private void showTravelCollections(List<TravelCollection> travelCollections) {
        if (travelCollections == null || travelCollections.isEmpty()) {
            Common.showToast(activity, "No TravelCollections Found");
            return;
        }
        TravelCollectionAdapter travelCollectionAdapter = (TravelCollectionAdapter) travelCollectionRecycleView.getAdapter();

        if (travelCollectionAdapter == null) {
            travelCollectionRecycleView.setAdapter(new TravelCollectionAdapter(activity, travelCollections));
        } else {
            travelCollectionAdapter.setTravelCollections(travelCollections);
            travelCollectionAdapter.notifyDataSetChanged();
        }
    }

    private class TravelCollectionAdapter extends RecyclerView.Adapter<TravelCollectionAdapter.MyViewHolder> {

        private LayoutInflater layoutInflater;
        private List<TravelCollection> travelCollections;
        private int imageSize;

        TravelCollectionAdapter(Context context, List<TravelCollection> travelCollections) {
            layoutInflater = LayoutInflater.from(context);
            this.travelCollections = travelCollections;
            //螢幕寬度 / 3 當圖片尺寸
            imageSize = getResources().getDisplayMetrics().widthPixels / 3;
        }

        void setTravelCollections(List<TravelCollection> travelCollections) {
            this.travelCollections = travelCollections;
        }


        @Override
        public int getItemCount() {
            return travelCollections.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView travelCollectionImage;
            TextView Group_ID, Group_Name, gp_datestart, gp_dateend, gp_eventstart;

            MyViewHolder(View itemView) {
                super(itemView);
                travelColleationImage = itemView.findViewById(R.id.travelCollectionImage);
                Group_ID = itemView.findViewById(R.id.Group_ID);
                Group_Name = itemView.findViewById(R.id.Group_Name);
                gp_datestart = itemView.findViewById(R.id.gp_datestart);
                gp_dateend = itemView.findViewById(R.id.gp_dateend);
                gp_eventstart = itemView.findViewById(R.id.gp_eventstart);
            }

        }


        @NonNull
        @Override
        public TravelCollectionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.travelcollection_item, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull TravelCollectionAdapter.MyViewHolder holder, int position) {
            final TravelCollection travelCollection = travelCollections.get(position);
            String url = Common.URL_SERVER + "TravelCollectionServlet";
            int id = travelCollection.getMb_no();
            travelCollectionImageTask = new ImageTask(url, id, imageSize, holder.travelCollectionImage);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            travelCollectionImageTask.execute();
            holder.Group_ID.setText(String.valueOf(travelCollection.getGp_id()));
            holder.Group_Name.setText(travelCollection.getGp_name());
            holder.gp_datestart.setText(simpleDateFormat.format(travelCollection.getGP_DATESTART()));
            holder.gp_dateend.setText(simpleDateFormat.format(travelCollection.getGP_DATEEND()));
            holder.gp_eventstart.setText(simpleDateFormat.format(travelCollection.getGP_EVENTDATE()));
            //增加行程按鈕監聽
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
            //增加按鈕常按事件
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (travelCollectionGetAllTask != null) {
            travelCollectionGetAllTask.cancel(true);
            travelCollectionGetAllTask = null;
        }
        if (travelCollectionImageTask != null) {
            travelCollectionImageTask.cancel(true);
            travelCollectionImageTask = null;
        }
        if (travelCollectionDeleteTask != null) {
            travelCollectionDeleteTask.cancel(true);
            travelCollectionDeleteTask = null;
        }
    }
}


