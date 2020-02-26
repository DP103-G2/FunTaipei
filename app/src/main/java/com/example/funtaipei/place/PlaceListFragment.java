package com.example.funtaipei.place;


import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.funtaipei.Common;
import com.example.funtaipei.place.Place;
import com.example.funtaipei.R;
import com.example.funtaipei.task.CommonTask;
import com.example.funtaipei.task.ImageTask;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class PlaceListFragment extends Fragment{
    private static final String TAG = "TAG_PlaceListFragment";
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvPlace;
    private Activity activity;
    private CommonTask placeGetAllTask;
    private CommonTask placeDeleteTask;
    private ImageTask placeImageTask;
    private List<Place> places;
    private Button btHotel,btResturant,btView;
    private View v;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        v = inflater.inflate(R.layout.fragment_hotel,container,false);
        btHotel =(Button)v.findViewById(R.id.btHotel);

        return inflater.inflate(R.layout.fragment_place_list, container, false);


    }


    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SearchView searchView = view.findViewById(R.id.searchView);
        rvPlace = view.findViewById(R.id.rvPlace);
        rvPlace.setLayoutManager(new LinearLayoutManager(activity));
        //rvPlace.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL,false)); //橫向滑動
        places = getPlaces();
        showPlaces(places);
        activity.setTitle("旅遊景點");
        Button btHotel = view.findViewById(R.id.btHotel);
        btHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_placeListFragment_to_hotelFragment);

            }
        });
        Button btResturant = view.findViewById(R.id.btResturant);
        btResturant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_placeListFragment_to_restaurantFragment);
            }
        });
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                showPlaces(places);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                // 如果搜尋條件為空字串，就顯示原始資料；否則就顯示搜尋後結果
                if (newText.isEmpty()) {
                    showPlaces(places);
                } else {
                    List<Place> searchPlaces = new ArrayList<>();
                    // 搜尋原始資料內有無包含關鍵字(不區別大小寫)
                    for (Place place : places) {
                        if (place.getPC_NAME().toUpperCase().contains(newText.toUpperCase())) {
                            searchPlaces.add(place);
                        }
                    }
                    showPlaces(searchPlaces);
                }
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });

    }
//
    private List<Place> getPlaces() {
        List<Place> places = null;
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "/PlaceServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getPlace");
            String jsonOut = jsonObject.toString();
            placeGetAllTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = placeGetAllTask.execute().get();
                Type listType = new TypeToken<List<Place>>() {
                }.getType();
                places = new Gson().fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }
        return places;
    }

    private void showPlaces(List<Place> places) {
        if (places == null || places.isEmpty()) {
            Common.showToast(activity, R.string.textNoPlacesFound);
        }
        PlaceAdapter placeAdapter = (PlaceAdapter) rvPlace.getAdapter();
        // 如果spotAdapter不存在就建立新的，否則續用舊有的
        if (placeAdapter == null) {
            rvPlace.setAdapter(new PlaceAdapter(activity, places));
        } else {
            placeAdapter.setPlaces(places);
            placeAdapter.notifyDataSetChanged();
        }
    }

    public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.MyViewHolder> {
        private LayoutInflater layoutInflater;
        private List<Place> places;
        private int imageSize;

        PlaceAdapter(Context context, List<Place> places) {
            layoutInflater = LayoutInflater.from(context);
            this.places = places;
            /* 螢幕寬度除以4當作將圖的尺寸 */
            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        }

        void setPlaces(List<Place> places) {

            this.places = places;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView tvName, tvPhone, tvAddress;

            MyViewHolder(View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView);
                tvName = itemView.findViewById(R.id.tvName);
                tvPhone = itemView.findViewById(R.id.tvPhone);
                tvAddress = itemView.findViewById(R.id.tvAddress);
            }
        }


        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.item_view_place, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            final Place place = places.get(position);
            String url = Common.URL_SERVER + "/PlaceServlet";
            int id = place.getPC_ID();
            placeImageTask = new ImageTask(url, id, imageSize, holder.imageView);
            placeImageTask.execute();
            holder.tvName.setText(place.getPC_NAME());
            holder.tvPhone.setText(String.valueOf(place.getPC_PHONE()));
            holder.tvAddress.setText(place.getPC_ADDRESS());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("place", place);
                    Navigation.findNavController(view).navigate(R.id.action_placeListFragment_to_placeDetailsFragment, bundle);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public boolean onLongClick(final View view) {
                    PopupMenu popupMenu = new PopupMenu(activity, view, Gravity.END);
                    popupMenu.inflate(R.menu.popup_menu);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.update:
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("place", place);
                                    Navigation.findNavController(view)
                                            .navigate(R.id.action_placeListFragment_to_placeDetailsFragment, bundle);
                                    break;

                                case R.id.delete:
                                    if (Common.networkConnected(activity)) {
                                        String url = Common.URL_SERVER + "/PlaceServlet";
                                        JsonObject jsonObject = new JsonObject();
                                        jsonObject.addProperty("action", "placeDelete");
                                        jsonObject.addProperty("placeId", place.getPC_NAME());
                                        int count = 0;
                                        try {
                                            placeDeleteTask = new CommonTask(url, jsonObject.toString());
                                            String result = placeDeleteTask.execute().get();
                                            count = Integer.valueOf(result);
                                        } catch (Exception e) {
                                            Log.e(TAG, e.toString());
                                        }
                                        if (count == 0) {
                                            Common.showToast(activity, R.string.textDeleteFail);
                                        } else {
                                            places.remove(place);
                                            PlaceAdapter.this.notifyDataSetChanged();
                                            // 外面spots也必須移除選取的spot
                                            PlaceListFragment.this.places.remove(place);
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

      @Override
        public int getItemCount() {
            return places.size();

        }
    }
    @Override
    public void onStop() {
        super.onStop();
        if (placeGetAllTask != null) {
            placeGetAllTask.cancel(true);
            placeGetAllTask = null;
        }

        if (placeImageTask != null) {
            placeImageTask.cancel(true);
            placeImageTask = null;
        }

        if (placeDeleteTask != null) {
            placeDeleteTask.cancel(true);
            placeDeleteTask = null;
        }
    }

}
