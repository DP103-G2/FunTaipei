package com.example.funtaipei.travel;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.funtaipei.Common;
import com.example.funtaipei.R;
import com.example.funtaipei.member.Member;
import com.example.funtaipei.task.CommonTask;
import com.example.funtaipei.task.ImageTask;
import com.example.funtaipei.travelCollection.TravelCollection;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class ManageTravel extends Fragment {

    private Activity activity;
    private Member member;
    private Travel travel;
    private List<Travel> travels;
    private CommonTask travelGetAllTask;
    private CommonTask travelDeleteTask;
    private ImageTask travelImageTask;
    private RecyclerView recyclerView;
    private FloatingActionButton btnAdd;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_manage_travel, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity.setTitle("行程管理");

        recyclerView = view.findViewById(R.id.manTrecycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        travels = getTravels();
        showTravels(travels);

        btnAdd = view.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.travelInsert);
            }
        });


    }

    private List<Travel> getTravels() {
        List<Travel> travels = null;
        if(Common.networkConnected(activity)){
            //取得帳號資料
            SharedPreferences pref = activity.getSharedPreferences(Common.PREFERENCES_MEMBER, Context.MODE_PRIVATE);
            int memId = pref.getInt("mb_no", 0);
            String url = Common.URL_SERVER + "/TravelServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAll");
            jsonObject.addProperty("action", "findBymemId");
            jsonObject.addProperty("memId", memId);
            String jsonOut = jsonObject.toString();
            travelGetAllTask = new CommonTask(url, jsonOut);
            try{
                String jsonIn = travelGetAllTask.execute().get();
                Type listType = new TypeToken<List<Travel>>(){
                }.getType();
                travels = new Gson().fromJson(jsonIn, listType);
            }catch (Exception e){
                Log.d(TAG, "travels: ");
            }
        }else {
            Common.showToast(activity, R.string.textNoNetwork);
        }
        return travels;
    }

    private void showTravels(List<Travel> travels){
        if (travels == null || travels.isEmpty()){
            return;
        }
        TravelAdapter travelAdapter = (TravelAdapter) recyclerView.getAdapter();

        if(travelAdapter == null){
            recyclerView.setAdapter(new TravelAdapter(activity, travels));
        } else {
            travelAdapter.setTravels(travels);
            travelAdapter.notifyDataSetChanged();
        }
    }


    private class TravelAdapter extends RecyclerView.Adapter<TravelAdapter.MyViewHolder>{

        private LayoutInflater layoutInflater;
        private int imageSize;
        private List<TravelCollection> travelCollections;
        private List<Travel> travels;

        TravelAdapter(Context context, List<Travel> travelList){
            layoutInflater = LayoutInflater.from(context);
            this.travels = travelList;
            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        }

        void setTravels(List<Travel> travels){
            this.travels = travels;
        }

        @Override
        public int getItemCount() {
            return travels.size();
        }


        class MyViewHolder extends RecyclerView.ViewHolder{
           ImageView imageView;
           TextView travelName;
           MyViewHolder(View itemView){
               super(itemView);
               imageView = itemView.findViewById(R.id.imageView);
               travelName = itemView.findViewById(R.id.travelName);
           }
        }

        @NonNull
        @Override
        public TravelAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemview = layoutInflater.inflate(R.layout.travelcollection_item, parent, false);
            return new MyViewHolder(itemview);
        }

        @Override
        public void onBindViewHolder(@NonNull TravelAdapter.MyViewHolder holder, int position) {
            final Travel travel = travels.get(position);
            String url = Common.URL_SERVER + "/TravelServlet";
            int travel_id = travel.getTravel_id();
            travelImageTask = new ImageTask(url, travel_id, imageSize, holder.imageView);
            travelImageTask.execute();
            holder.travelName.setText(travel.getTravel_name());
            //選項點擊事件
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("travel", travel);
                    Navigation.findNavController(v).navigate(R.id.travel_Collection_detailFragment, bundle);
                }
            });
            //選項長按點擊事件
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(activity, v, Gravity.END);
                    popupMenu.inflate(R.menu.popup_menu);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.delete:
                                    if(Common.networkConnected(activity)){
                                        String url = Common.URL_SERVER + "/TravelServlet";
                                        JsonObject jsonObject = new JsonObject();
                                        jsonObject.addProperty("action", "travelDelete");
                                        jsonObject.addProperty("action" , travel.getTravel_id());

                                        int count = 0;
                                        try{
                                            travelDeleteTask = new CommonTask(url, jsonObject.toString());
                                            String result = travelDeleteTask.execute().get();
                                            count = Integer.valueOf(result);
                                        }catch (Exception e){
                                            Log.d(TAG, "onMenuItemClick: ");
                                        }
                                        if(count == 0){
                                            Common.showToast(activity, R.string.textDeleteFail);
                                        } else {
                                           travels.remove(travel);
                                           TravelAdapter.this.notifyDataSetChanged();
                                           ManageTravel.this.travels.remove(travel);
                                           Common.showToast(activity, R.string.textDeleteSuccess);
                                        }
                                    }else {
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

    @Override
    public void onStop() {
        super.onStop();
        if(travelGetAllTask != null){
            travelGetAllTask.cancel(true);
            travelGetAllTask = null;
        }
        if(travelDeleteTask != null){
            travelDeleteTask.cancel(true);
            travelDeleteTask = null;
        }
        if(travelImageTask != null){
            travelImageTask.cancel(true);
            travelImageTask = null;
        }
    }
}
