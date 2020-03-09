package com.example.funtaipei.travelDetail;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.funtaipei.Common;
import com.example.funtaipei.R;
import com.example.funtaipei.task.CommonTask;
import com.example.funtaipei.task.ImageTask;
import com.example.funtaipei.travel.Group;
import com.example.funtaipei.travel.ManTravelDetail;
import com.example.funtaipei.travel.Place;
import com.example.funtaipei.travel.Travel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class TravelDetailFragment extends Fragment {
    private FragmentActivity activity;
    private CommonTask travelDetailGetAllTask, groupGetAllTask;
    private CommonTask travelDeleteTask, groupDeleteTask;
    private ImageTask travelImageTask, groupImageTask, travelDetailImageTask;
    private List<TravelDetail> travelDetails = new ArrayList<>();
    private List<Group> groups;
    private RecyclerView travel_detail_recycleview;
    private CommonTask placeGetAllTask;
    private CommonTask travelCollectionTask;
    private ImageTask placeImageTask;
    private RecyclerView group_recycleview;
    private RecyclerView stationRecycleView;
    private Button btnAddTravel;
    private Place places;
    private Button btnAddGroup,signUpButton, collectionUpButton;
    private Travel travel;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_travel_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);


        //報名按鈕
        signUpButton = view.findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "按讚拉",Toast.LENGTH_SHORT).show();
            }
        });

        //收藏按鈕
        collectionUpButton = view.findViewById(R.id.collectionUpButton);
        collectionUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = activity.getSharedPreferences(Common.PREFERENCES_MEMBER, Context.MODE_PRIVATE);
                int memId = pref.getInt("mb_no", 0);
                int travelId = travel.getTravel_id();
                String url = Common.URL_SERVER + "/TravelCollectionServlet";
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "travelCollectionInsert");
                jsonObject.addProperty("memId", memId);
                jsonObject.addProperty("travelId", travelId);
                travelCollectionTask = new CommonTask(url, jsonObject.toString());
                int count = 0;
                try {
                    String result = travelCollectionTask.execute().get();
                    count = Integer.parseInt(result);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
                if (count == 1) {
                    Common.showToast(activity, R.string.textFavoriteSuccess);
                } else {
                    Common.showToast(activity, R.string.textInsertFail);
                }
            }
        });

        //這邊是接前一頁的圖片和ID
        ImageView imageView = view.findViewById(R.id.travel_imageview);
        TextView travel_title = view.findViewById(R.id.travel_title);
        TextView travel_id = view.findViewById(R.id.travel_id);
        Bundle bundle = getArguments();
        if (bundle != null || bundle.getSerializable("travel") != null || bundle.getInt("travelId") != 0) {
            if (bundle != null) {
                travel = (Travel) bundle.get("travel");
                if (travel == null ) {
                    int id = bundle.getInt("travelId");

                    if (Common.networkConnected(activity)) {
                        String url = Common.URL_SERVER + "/TravelServlet";
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("action", "findByGroup");
                        jsonObject.addProperty("id", id);
                        String jsonOut = jsonObject.toString();

                        travelDetailGetAllTask = new CommonTask(url, jsonOut);
                        try {
                            String jsonIn = travelDetailGetAllTask.execute().get();
                            travel = new Gson().fromJson(jsonIn, Travel.class);
                        } catch (Exception e) {
                            Log.d(TAG, "getTravelDetails: ");
                        }
                    } else {
                        Common.showToast(activity, R.string.textNoNetwork);
                    }
                }
                if (travel != null) {
                    String url = Common.URL_SERVER + "/TravelServlet";
                    ImageTask imageTask = new ImageTask(url, travel.getTravel_id(), getResources().getDisplayMetrics().widthPixels / 4);
                    try {
                        Bitmap image = imageTask.execute().get();
                        imageView.setImageBitmap(image);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    travel_id.setText(String.valueOf(travel.getTravel_id()));
                    travel_title.setText(travel.getTravel_name());
//                    travelDetails = getTravelDetails();
//                    showTravelDetail(travelDetails);
                }
            }
        } else {
            Common.showToast(activity, R.string.textNoGroupsFound);
            //           navController.popBackStack();
            return;
        }
        //Detail的RecycleView
        travel_detail_recycleview = view.findViewById(R.id.travel_detail_recycleview);
        travel_detail_recycleview.setLayoutManager(new LinearLayoutManager(activity));

        travelDetails = getTravelDetails();
        showTravelDetail(travelDetails);
    }


    private List<TravelDetail> getTravelDetails(){
        List<TravelDetail> travelDetails = null;
        if(Common.networkConnected(activity)){
            String url = Common.URL_SERVER + "/TravelDetailServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "findByTravelId");
            jsonObject.addProperty("id", travel.getTravel_id());
            String jsonOut = jsonObject.toString();

            travelDetailGetAllTask = new CommonTask(url, jsonOut);
            try{
                String jsonIn = travelDetailGetAllTask.execute().get();
                Type typelist = new TypeToken<List<TravelDetail>>(){
                }.getType();
                travelDetails = new Gson().fromJson(jsonIn, typelist);
            }catch(Exception e){
                Log.d(TAG, "getTravelDetails: ");
            }
        } else {
            Common.showToast(activity, "沒有景點");
        }
        return travelDetails;
    }
    private void showTravelDetail(List<TravelDetail> travelDetails) {
        if (travelDetails == null) {
            return;
        }
        ManTravelAdapter manTravelAdapter = (ManTravelAdapter) travel_detail_recycleview.getAdapter();
        if (manTravelAdapter == null) {
            travel_detail_recycleview.setAdapter(new ManTravelAdapter(activity, travelDetails));
        } else {
            manTravelAdapter.setManTravelDetail(travelDetails);
            manTravelAdapter.notifyDataSetChanged();
        }
    }


    public class ManTravelAdapter extends RecyclerView.Adapter<ManTravelAdapter.MyViewHolder> {

        private LayoutInflater layoutInflater;
        private List<TravelDetail> travelDetails;
        private int imageSize;

        ManTravelAdapter(Context context, List<TravelDetail> travelDetails){
            layoutInflater = LayoutInflater.from(context);
            this.travelDetails = travelDetails;
            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        }

        void setManTravelDetail(List<TravelDetail> travelDetails){
            this.travelDetails = travelDetails;
        }



        public class MyViewHolder extends RecyclerView.ViewHolder{
            ImageView imageView;
            TextView travelName;

            MyViewHolder(View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView);
                travelName = itemView.findViewById(R.id.travelName);
            }
        }

        @NonNull
        @Override
        public ManTravelAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.travelcollection_item, parent, false);
            return new MyViewHolder(itemView);
        }



        @Override
        public int getItemCount() {
            return travelDetails.size();
        }


        @Override
        public void onBindViewHolder(@NonNull final ManTravelAdapter.MyViewHolder holder, final int position) {
            final TravelDetail travelDetail = travelDetails.get(position);
            //圖片設定
            String url = Common.URL_SERVER + "/PlaceServlet";
            int id = travelDetail.getPc_id();
            ImageTask imageTask = new ImageTask(url, id , imageSize, holder.imageView);
            imageTask.execute();
            //文字設定
            holder.travelName.setText(travelDetail.getPc_name());
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View v) {
                    PopupMenu popupMenu = new PopupMenu(activity, v, Gravity.END);
                    popupMenu.inflate(R.menu.popup_menu);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){

                                case R.id.delete:
                                    if(Common.networkConnected(activity)){
                                        String url = Common.URL_SERVER + "/TravelDetailServlet";
                                        JsonObject jsonObject = new JsonObject();
                                        jsonObject.addProperty("action", "travelDetailDelete");
                                        jsonObject.addProperty("travel_id", travelDetail.getTravel_id());
                                        jsonObject.addProperty("pc_id", travelDetail.getPc_id());
                                        int count = 0;
                                        try{
                                            CommonTask travelDetailDeleteTask = new CommonTask(url, jsonObject.toString());
                                            String result = travelDetailDeleteTask.execute().get();
                                            count = Integer.valueOf(result);
                                        }catch(Exception e){
                                            Log.d(TAG, "onMenuItemClick: ");
                                        }
                                        if(count == 0){
                                            Common.showToast(activity, R.string.textDeleteFail);
                                        } else {
                                            travelDetails.remove(travelDetail);
                                            ManTravelAdapter.this.notifyDataSetChanged();
                                            ManTravelAdapter.this.travelDetails.remove(travelDetail);
                                            Common.showToast(activity, R.string.textDeleteSuccess);
                                        }
                                    } else {
                                        Common.showToast(activity, R.string.textNoNetwork);
                                    }
                                    break;
                            }

                            return true;
                        }
                    });
                    popupMenu.show();
                    return true;
                }
            });
        }
    }












































//    //Get Travel Detail
//    private List<TravelDetail> getTravelDetails() {
//        List<TravelDetail> travelDetails = null;
//        if (Common.networkConnected(activity)) {
//            String url = Common.URL_SERVER + "/TravelDetailServlet";
//            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("action", "findByTravelId");
//            jsonObject.addProperty("id", travel.getTravel_id());
//            String jsonOut = jsonObject.toString();
//
//            travelDetailGetAllTask = new CommonTask(url, jsonOut);
//            try {
//                String jsonIn = travelDetailGetAllTask.execute().get();
//                Type listType = new TypeToken<List<TravelDetail>>() {
//                }.getType();
//                travelDetails = new Gson().fromJson(jsonIn, listType);
//            } catch (Exception e) {
//                Log.d(TAG, "getTravelDetails: ");
//            }
//        } else {
//            Common.showToast(activity, "沒有景點");
//        }
//        return travelDetails;
//    }
//    //ShowTravelDetail
//    private void showtravelDetail(List<TravelDetail> travelDetails) {
//        if (travelDetails == null || travelDetails.isEmpty()) {
//            Common.showToast(activity, "沒有景點");
//            return;
//        }
//        TravelDetailAdapter travelDetailAdapter = (TravelDetailAdapter) travel_detail_recycleview.getAdapter();
//        if (travelDetailAdapter == null) {
//            travel_detail_recycleview.setAdapter(new TravelDetailAdapter(activity, travelDetails));
//        } else {
//            travelDetailAdapter.setTravelDetails(travelDetails);
//            travelDetailAdapter.notifyDataSetChanged();
//        }
//    }
//
//
//    private class TravelDetailAdapter extends RecyclerView.Adapter<TravelDetailAdapter.MyViewHolder> {
//        private LayoutInflater layoutInflater;
//        private List<TravelDetail> travelDetails;
//        private List<Place> places;
//        private int imageSize;
//
//        TravelDetailAdapter(Context context, List<TravelDetail> travelDetails) {
//            layoutInflater = LayoutInflater.from(context);
//            this.travelDetails = travelDetails;
//            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
//        }
//
//        void setTravelDetails(List<TravelDetail> travelDetails) {
//            this.travelDetails = travelDetails;
//        }
//
//        class MyViewHolder extends RecyclerView.ViewHolder {
//            ImageView imageView;
//            TextView travel_id, pc_name, pc_id, travelName;
//
//            MyViewHolder(View itemView) {
//                super(itemView);
//                imageView = itemView.findViewById(R.id.imageView);
//                travel_id = itemView.findViewById(R.id.travel_id);
//                pc_name = itemView.findViewById(R.id.pc_name);
//
//
//            }
//        }
//
//        @NonNull
//        @Override
//        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View itemView = layoutInflater.inflate(R.layout.travelcollection_item, parent, false);
//            return new MyViewHolder(itemView);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull final TravelDetailAdapter.MyViewHolder holder, int position) {
////            final TravelDetail travelDetail = travelDetails.get(position);
////            String url = Common.URL_SERVER + "/PlaceServlet";
////            int id = travelDetail.getPc_id();
////            travelImageTask = new ImageTask(url, id, imageSize, holder.imageView);
////            travelImageTask.execute();
////            holder.pc_name.setText(String.valueOf(travelDetail.getPc_name()));
////
//////
////            //下面這行是跳轉到旅遊點細節
////            holder.itemView.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    Navigation.findNavController(v).navigate(R.id.action_travelDetailFragment_to_placeDetailsFragment);
////                }
////            });
//            final TravelDetail travelDetail = travelDetails.get(position);
//            //圖片設定
//            String url = Common.URL_SERVER + "/PlaceServlet";
//            int id = travelDetail.getPc_id();
//            ImageTask imageTask = new ImageTask(url, id , imageSize, holder.imageView);
//            imageTask.execute();
//            //文字設定
//            holder.travelName.setText(travelDetail.getPc_name());
//            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(final View v) {
//                    PopupMenu popupMenu = new PopupMenu(activity, v, Gravity.END);
//                    popupMenu.inflate(R.menu.popup_menu);
//                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                        @Override
//                        public boolean onMenuItemClick(MenuItem item) {
//                            switch (item.getItemId()){
//                                case R.id.update:
//                                    Navigation.findNavController(v).navigate(R.id.action_manageTravel_to_manTravelDetail);
//                                    break;
//                                case R.id.delete:
//                                    if(Common.networkConnected(activity)){
//                                        String url = Common.URL_SERVER + "/TravelDetailServlet";
//                                        JsonObject jsonObject = new JsonObject();
//                                        jsonObject.addProperty("action", "travelDetailDelete");
//                                        jsonObject.addProperty("travel_id", travelDetail.getTravel_id());
//                                        jsonObject.addProperty("pc_id", travelDetail.getPc_id());
//                                        int count = 0;
//                                        try{
//                                            CommonTask travelDetailDeleteTask = new CommonTask(url, jsonObject.toString());
//                                            String result = travelDetailDeleteTask.execute().get();
//                                            count = Integer.valueOf(result);
//                                        }catch(Exception e){
//                                            Log.d(TAG, "onMenuItemClick: ");
//                                        }
//                                        if(count == 0){
//                                            Common.showToast(activity, R.string.textDeleteFail);
//                                        } else {
//                                            travelDetails.remove(travelDetail);
//                                            TravelDetailAdapter.this.notifyDataSetChanged();
//                                            TravelDetailAdapter.this.travelDetails.remove(travelDetail);
//                                            Common.showToast(activity, R.string.textDeleteSuccess);
//                                        }
//                                    } else {
//                                        Common.showToast(activity, R.string.textNoNetwork);
//                                    }
//                                    break;
//                            }
//
//                            return true;
//                        }
//                    });
//                    popupMenu.show();
//                    return true;
//                }
//            });
//
//        }
//
//        @Override
//        public int getItemCount() {
//            return travelDetails.size();
//        }
//    }

    //-----------------------------------------以下為GruopsRecycleView---------------------------------------------------------------------------

//    private List<Group> getGroups() {
//        List<Group> groups = null;
//        if (Common.networkConnected(activity)) {
//            String url = Common.URL_SERVER + "/GroupServlet";
//            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("action", "findByTravelId");
//            jsonObject.addProperty("id", travel.getTravel_id());
//            String jsonOut = jsonObject.toString();
//
//            groupGetAllTask = new CommonTask(url, jsonOut);
//            try {
//                String jsonIn = groupGetAllTask.execute().get();
//                Type listType = new TypeToken<List<Group>>() {
//                }.getType();
//                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
//                groups = gson.fromJson(jsonIn, listType);
//
//            } catch (Exception e) {
//                Log.d(TAG, "getGroups: ");
//            }
//        } else {
//            Common.showToast(activity, "No NetWork Connection");
//        }
//        return groups;
//    }
//
//    //ShowGroups
//    private void showGroups(List<Group> groups) {
//        if (groups == null || groups.isEmpty()) {
//            Common.showToast(activity, "No Groups Founded");
//            return;
//        }
//        GroupAdapter groupAdapter = (GroupAdapter) group_recycleview.getAdapter();
//        if (groupAdapter == null) {
//            group_recycleview.setAdapter(new GroupAdapter(activity, groups));
//        } else {
//            groupAdapter.setGroups(groups);
//            groupAdapter.notifyDataSetChanged();
//        }
//    }
//
//    //以下是GroupRecycleView
//    private class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.MyViewHolder> {
//
//        private LayoutInflater layoutInflater;
//        private List<Group> groups;
//        private int imageSize;
//
//        GroupAdapter(Context context, List<Group> groups) {
//            layoutInflater = LayoutInflater.from(context);
//            this.groups = groups;
//            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
//        }
//
//        void setGroups(List<Group> groups) {
//            this.groups = groups;
//        }
//
//        class MyViewHolder extends RecyclerView.ViewHolder {
//            ImageView groupImage;
//            TextView group_id, groupTitle, gp_eventstart, gp_datestart, gp_dateend;
//
//            MyViewHolder(View itemView) {
//                super(itemView);
//                groupImage = itemView.findViewById(R.id.groupImage);
//                group_id = itemView.findViewById(R.id.group_id);
//                groupTitle = itemView.findViewById(R.id.groupTitle);
//                gp_eventstart = itemView.findViewById(R.id.gp_eventstart);
//                gp_datestart = itemView.findViewById(R.id.gp_datestart);
//                gp_dateend = itemView.findViewById(R.id.gp_dateend);
//            }
//
//        }
//
//        @Override
//        public int getItemCount() {
//            return groups.size();
//        }
//
//
//        @NonNull
//        @Override
//        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View itemView = layoutInflater.inflate(R.layout.group_item, parent, false);
//            return new MyViewHolder(itemView);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//            final Group group = groups.get(position);
//            String url = Common.URL_SERVER + "/TravelDetailServlet";
//            int id = group.getGP_ID();
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            groupImageTask = new ImageTask(url, id, imageSize, holder.groupImage);
//            groupImageTask.execute();
//            holder.group_id.setText(String.valueOf(group.getGP_ID()));
//            holder.groupTitle.setText(group.getGP_NAME());
//            holder.gp_eventstart.setText(simpleDateFormat.format(group.getGP_EVENTDATE()));
//            holder.gp_datestart.setText(simpleDateFormat.format(group.getGP_DATESTAR()));
//            holder.gp_dateend.setText(simpleDateFormat.format(group.getGP_DATEEND()));
//
//        }
//    }

    @Override
    public void onStop() {
        super.onStop();
        if (groupGetAllTask != null) {
            groupGetAllTask.cancel(true);
            groupGetAllTask = null;
        }

        if (groupImageTask != null) {
            groupImageTask.cancel(true);
            groupImageTask = null;
        }

        if (groupDeleteTask != null) {
            groupDeleteTask.cancel(true);
            groupDeleteTask = null;
        }
    }
}

