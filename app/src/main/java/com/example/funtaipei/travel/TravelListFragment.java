package com.example.funtaipei.travel;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.funtaipei.Common;
import com.example.funtaipei.R;
import com.example.funtaipei.main.MainActivity;
import com.example.funtaipei.task.CommonTask;
import com.example.funtaipei.task.ImageTask;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class TravelListFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private Activity activity;
    private CommonTask travelGetAllTask;
    private CommonTask travelDeleteTask;
    private ImageTask travelImageTask;
    private List<Travel> travels;
    private List<Integer> list = new ArrayList<>(4);



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_travel_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        activity.setTitle("攻略行程");
        super.onViewCreated(view, savedInstanceState);

        //Banner存入圖片
        list.add(R.drawable.taipeiontshot);
        list.add(R.drawable.b2);
        list.add(R.drawable.bb);
        list.add(R.drawable.dd);

        //Banner
        BannerAdapter adapter = new BannerAdapter(getActivity(), list);
        final RecyclerView topreCycleView = view.findViewById(R.id.toprecycler);
        final SmoothLinearLayoutManager layoutManager = new SmoothLinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        topreCycleView.setLayoutManager(layoutManager);
        topreCycleView.setHasFixedSize(true);
        topreCycleView.setAdapter(adapter);
        topreCycleView.scrollToPosition(list.size() * 10);
        //讓圖片一頁一頁滑過去
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(topreCycleView);
        //自動輪播
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                topreCycleView.smoothScrollToPosition(layoutManager.findFirstVisibleItemPosition() + 1);
            }
        }, 2000, 2000, TimeUnit.MILLISECONDS);


        //搜尋功能
        SearchView searchView = view.findViewById(R.id.searchView);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                showTravels(travels);
                swipeRefreshLayout.setRefreshing(false);
            }
        });




        //行程明細
        recyclerView = view.findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        travels = getTravels();
        showTravels(travels);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    showTravels(travels);
                } else {
                    List<Travel> searchTravels = new ArrayList<>();
                    for (Travel travel : travels) {
                        if (String.valueOf(travel.getTravel_id()).contains(newText.toUpperCase())) {
                            searchTravels.add(travel);
                       }
                        //下面這段有問題 2020-1-18
                        else if(travel.getTravel_name().contains(newText.toUpperCase())){
                            searchTravels.add(travel);
                        }
                            showTravels(searchTravels);
                        }
                    }
                    return true;
                }
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }


        });
//        btnInsert = view.findViewById(R.id.btnInsert);
//        btnInsert.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Navigation.findNavController(v).navigate(R.id.action_fragment_travel_to_travelInsert);
//            }
//        });

    }


        //取得Image資料
//    private List<Image> getImages(){
//        List<Image> images = null;
//        if (Common.networkConnected(activity)){
//            String url = Common.URL_SERVER + "ImageServlet";
//            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("action", "getAll");
//            String jsonOut = jsonObject.toString();
//            ImageGetAllTask = new CommonTask(url, jsonOut);
//            try{
//                String jsonIn = ImageGetAllTask.execute().get();
//            }
//        }
//    }
        //取得Travel資料
    private List<Travel> getTravels() {
        List<Travel> travels = null;
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "TravelServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAll");
            String jsonOut = jsonObject.toString();
            travelGetAllTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = travelGetAllTask.execute().get();
                Type listType = new TypeToken<List<Travel>>() {
                }.getType();
                travels = new Gson().fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }
        return travels;
    }
    //Show出Travel資料
    private void showTravels(List<Travel> travels) {
        if (travels == null || travels.isEmpty()) {
            Common.showToast(activity, "No Travel Found");
            return;
        }
        TravelAdapter travelAdapter = (TravelAdapter) recyclerView.getAdapter();

        if (travelAdapter == null) {
            recyclerView.setAdapter(new TravelAdapter(activity, travels));
        } else {
            travelAdapter.setTravels(travels);
            travelAdapter.notifyDataSetChanged();
        }
    }
    //==========================================以下為RecycleView Travel=================================================
    private class TravelAdapter extends RecyclerView.Adapter<TravelAdapter.MyViewHolder> {
        private LayoutInflater layoutInflater;
        private List<Travel> travels;
        private int imageSize;

        TravelAdapter(Context context, List<Travel> travels) {
            layoutInflater = LayoutInflater.from(context);
            this.travels = travels;
            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        }

        void setTravels(List<Travel> travels) {
            this.travels = travels;
        }
        @Override
        public int getItemCount() {
            return travels.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView travel_title, travel_person,travel_id;

            MyViewHolder(View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView);
                travel_id = itemView.findViewById(R.id.travel_id);
                travel_title = itemView.findViewById(R.id.travel_title);

            }

        }

        @NonNull
        @Override
        public TravelAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemVIew = layoutInflater.inflate(R.layout.travel_item, parent, false);
            return new MyViewHolder(itemVIew);
        }

        @Override
        public void onBindViewHolder(@NonNull TravelAdapter.MyViewHolder holder, int position) {
            final Travel travel = travels.get(position);
            String url = Common.URL_SERVER + "TravelServlet";
            int travel_id = travel.getTravel_id();
            travelImageTask = new ImageTask(url, travel_id, imageSize, holder.imageView);
            travelImageTask.execute();
            holder.travel_id.setText(String.valueOf(travel.getTravel_id()));
            holder.travel_title.setText(travel.getTravel_name());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("travel", travel);
                    Navigation.findNavController(v).navigate(R.id.action_fragment_travel_to_travel_detail, bundle);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View v) {
                    PopupMenu popupMenu = new PopupMenu(activity, v, Gravity.END);
                    popupMenu.inflate(R.menu.popup_menu);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.update:
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("travel", travel);
                                    Navigation.findNavController(v).navigate(R.id.action_fragment_travel_to_travel_update, bundle);
                                case R.id.delete:
                                    if (Common.networkConnected(activity)) {
                                        String url = Common.URL_SERVER + "/TravelServlet";
                                        JsonObject jsonObject = new JsonObject();
                                        jsonObject.addProperty("action", "travelDelete");
                                        jsonObject.addProperty("travelId", travel.getTravel_id());
                                        int count = 0;
                                        try {
                                            travelDeleteTask = new CommonTask(url, jsonObject.toString());
                                            String result = travelDeleteTask.execute().get();
                                            count = Integer.valueOf(result);
                                        } catch (Exception e) {
                                            Log.d(TAG, "onMenuItemClick: " + e);
                                        }
                                        if (count == 0) {
                                            Common.showToast(activity, R.string.textDeleteFail);
                                        } else {
                                            travels.remove(travel);
                                            TravelAdapter.this.notifyDataSetChanged();
                                            TravelListFragment.this.travels.remove(travel);
                                            Common.showToast(activity, R.string.textDeleteSucess);
                                        }
                                    } else {
                                        Common.showToast(activity, R.string.textNoNetwork);
                                    }
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
        if (travelGetAllTask != null) {
            travelGetAllTask.cancel(true);
            travelGetAllTask = null;
        }
        if (travelImageTask != null) {
            travelImageTask.cancel(true);
        }
        if (travelDeleteTask != null) {
            travelDeleteTask.cancel(true);
            travelDeleteTask = null;
        }
    }
}
