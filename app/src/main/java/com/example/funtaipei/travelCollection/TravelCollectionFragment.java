package com.example.funtaipei.travelCollection;


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

import com.example.funtaipei.Common;
import com.example.funtaipei.R;
import com.example.funtaipei.member.Member;
import com.example.funtaipei.task.CommonTask;
import com.example.funtaipei.task.ImageTask;
import com.example.funtaipei.travel.Travel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class TravelCollectionFragment extends Fragment {
    private Activity activity;

    private RecyclerView travelCollectionRecycleView;
    private ImageTask travelCollectionImageTask;
    private CommonTask travelCollectionGetAllTask;
    private CommonTask travelCollectionDeleteTask;
    private TextView travelCollection_MemberName, travelCollection_MemberEmail, travelCollection_MemberId;
    private List<TravelCollection> travelCollections;
    private Travel travel;
    private Member member;


    private FloatingActionButton travelCollectionbtnAdd;

    public static TravelCollectionFragment newInstance() {
        return new TravelCollectionFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_travel_collection, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);





        //TravelCollection資料
        travelCollectionRecycleView = view.findViewById(R.id.FvRecycleview);
        travelCollectionRecycleView.setLayoutManager(new LinearLayoutManager(activity));
        travelCollections = getTravelCollections();
        showTravelCollections(travelCollections);


    }

    //--------------------------------以下是TravelCollection資料-------------------------------------------------------------


    private List<TravelCollection> getTravelCollections() {
        List<TravelCollection> travelCollections = null;
        if (Common.networkConnected(activity)) {
            //取得帳號資料
            SharedPreferences pref = activity.getSharedPreferences(Common.PREFERENCES_MEMBER, Context.MODE_PRIVATE);
            int memId = pref.getInt("mb_no", 0);
            String url = Common.URL_SERVER + "/TravelCollectionServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAll");
            jsonObject.addProperty("action", "getTravelInfo");
            jsonObject.addProperty("memId", memId);
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
            Common.showToast(activity, "您尚未收藏行程");
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
        public TravelCollectionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.travelcollection_item, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull TravelCollectionAdapter.MyViewHolder holder, int position) {
            final TravelCollection travelCollection = travelCollections.get(position);
            String url = Common.URL_SERVER + "/TravelServlet";
            final int id = travelCollection.getTravel_id();
            travelCollectionImageTask = new ImageTask(url, id, imageSize, holder.imageView);
            travelCollectionImageTask.execute();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            holder.travelName.setText(travelCollection.getTravel_name());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("travelCollection", travelCollection);
                    Navigation.findNavController(view).navigate(R.id.travel_Collection_detailFragment, bundle);
                }
            });
            //增加行程按鈕監聽
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View v) {
                    PopupMenu popupMenu = new PopupMenu(activity, v, Gravity.END);
                    popupMenu.inflate(R.menu.popup_menu);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                //刪除
                                case R.id.delete:
                                    if (Common.networkConnected(activity)) {
                                        SharedPreferences pref = activity.getSharedPreferences(Common.PREFERENCES_MEMBER, Context.MODE_PRIVATE);
                                        int memId = pref.getInt("mb_no", 0);

                                        String url = Common.URL_SERVER + "/TravelCollectionServlet";
                                        JsonObject jsonObject = new JsonObject();
                                        jsonObject.addProperty("action", "travelCollectionDelete");
                                        jsonObject.addProperty("mb_no", memId);
                                        jsonObject.addProperty("travel_id", travelCollection.getTravel_id());

                                        int count = 0;
                                        try {
                                            travelCollectionDeleteTask = new CommonTask(url, jsonObject.toString());
                                            String result = travelCollectionDeleteTask.execute().get();
                                            count = Integer.valueOf(result);
                                        } catch (Exception e) {
                                            Log.d(TAG, "onMenuItemClick: ");
                                        }
                                        if (count == 0) {
                                            Common.showToast(activity, R.string.textDeleteFail);
                                        } else {
                                            travelCollections.remove(travelCollection);
                                            TravelCollectionAdapter.this.notifyDataSetChanged();
                                            //外部travelCollections也必須刪除
                                            TravelCollectionFragment.this.travelCollections.remove(travelCollection);
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


