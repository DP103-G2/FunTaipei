package com.example.funtaipei.group;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.funtaipei.Common;
import com.example.funtaipei.R;
import com.example.funtaipei.task.CommonTask;
import com.example.funtaipei.task.ImageTask;

import com.example.funtaipei.travel.Travel;
import com.example.funtaipei.travelDetail.TravelDetail;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class GroupInsertTravelFragment extends Fragment {
    private final static String TAG = "TAG_GroupInsertFragment";
    private final static String PREFERENCES_NAME = "TravelDetail";
    private FragmentActivity activity;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvTravel;
    private CommonTask travelGetAllTask, travelDetailTask;
    private ImageTask travelImageTask;
    private List<Travel> travels;
    private SearchView searchView;
    private TravelDetail travelDetail;
    private SharedPreferences perferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_group_insert_travel, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        activity.setTitle("選擇行程");
        searchView = view.findViewById(R.id.searchView);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        rvTravel = view.findViewById(R.id.rvGroup);
        rvTravel.setLayoutManager(new LinearLayoutManager(activity));
        travels = getTravel();
        showTravels(travels);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                showTravels(travels);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                TravelAdapter travelAdapter = (TravelAdapter) rvTravel.getAdapter();
                if (travelAdapter != null) {
                    if (newText.isEmpty()) {
                        showTravels(travels);
                    } else {
                        List<Travel> searchTravels = new ArrayList<>();
                        for (Travel travel : travels) {
                            if (travel.getTravel_name().toUpperCase().contains(newText.toUpperCase())) {
                                searchTravels.add(travel);
                            }
                            if (String.valueOf(travel.getTravel_id()).contains(newText.toUpperCase())) {
                                searchTravels.add(travel);
                            }
                        }
                        travelAdapter.setTravels(searchTravels);
                    }
                    travelAdapter.notifyDataSetChanged();
                    return true;
                }
                return false;
            }
        });

    }


    private List<Travel> getTravel() {
        List<Travel> travels = null;
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "/TravelServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAll");
            String jsonOut = jsonObject.toString();
            travelGetAllTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = travelGetAllTask.execute().get();
                Type listType = new TypeToken<List<Travel>>() {

                }.getType();
                Gson gson = new GsonBuilder().setDateFormat("yyyy/MM/dd").create();
                travels = gson.fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }
        return travels;
    }

    private void showTravels(List<Travel> travels) {
        if (travels == null || travels.isEmpty()) {
            Common.showToast(activity, R.string.textNoTravelsFound);
        }
        TravelAdapter travelAdapter = (TravelAdapter) rvTravel.getAdapter();
        if (travelAdapter == null) {
            rvTravel.setAdapter(new TravelAdapter(activity, travels));
        } else {
            travelAdapter.setTravels(travels);
            travelAdapter.notifyDataSetChanged();
        }


    }

    private class TravelAdapter extends RecyclerView.Adapter<TravelAdapter.MyViewHolder> {
        private LayoutInflater layoutInflater;
        private List<Travel> travels;


        private int imageSize;
        private boolean[] detailExpanded;

        TravelAdapter(Context context, List<Travel> travels) {
            layoutInflater = LayoutInflater.from(context);
            this.travels = travels;
            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
            detailExpanded = new boolean[travels.size()];
        }

        void setTravels(List<Travel> travels) {
            this.travels = travels;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            Button btPick;
            TextView tvTravelName;
            TextView tvTravelId;

            public MyViewHolder(View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView);
                tvTravelName = itemView.findViewById(R.id.tvTravelName);
                tvTravelId = itemView.findViewById(R.id.tvTravelId);
                btPick = itemView.findViewById(R.id.btPick);
            }
        }

        @Override
        public int getItemCount() {
            return travels.size();
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.group_insert_travel_item, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int position) {
            final Travel travel = travels.get(position);
            String url = Common.URL_SERVER + "/TravelServlet";
            int id = travel.getTravel_id();
            travelImageTask = new ImageTask(url, id, imageSize, myViewHolder.imageView);
            travelImageTask.execute();
            myViewHolder.tvTravelName.setText(travel.getTravel_name());
            myViewHolder.tvTravelId.setText(String.valueOf(travel.getTravel_id()));


            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("travel", travel);
                    Navigation.findNavController(view).navigate(R.id.action_groupInsertTravelFragment_to_travelDetailFragment, bundle);
                }
            });


            myViewHolder.btPick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    perferences = activity.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
                    String Name = travel.getTravel_name();
                    int travelid = travel.getTravel_id();
                    perferences.edit()

                            .putInt("travelid", travelid)
                            .putString("travelName", Name)
                            .apply();


                    Common.showToast(activity, "你已選取" + Name);
                    Navigation.findNavController(view).popBackStack();

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
            travelImageTask = null;
        }
    }
}
