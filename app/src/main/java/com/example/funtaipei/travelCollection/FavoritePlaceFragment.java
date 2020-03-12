package com.example.funtaipei.travelCollection;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.funtaipei.Common;
import com.example.funtaipei.R;
import com.example.funtaipei.favoritePlace.FavoritePlace;
import com.example.funtaipei.task.CommonTask;
import com.example.funtaipei.task.ImageTask;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;





public class FavoritePlaceFragment extends Fragment {
    private static final String TAG = "TAG_PlaceListFragment";
    private GridView gvMember;
    private RecyclerView FvRecycleview;
    private Activity activity;
    private CommonTask placeGetAllTask;
    private CommonTask placeDeleteTask;
    private ImageTask placeImageTask;
    private List<FavoritePlace> favoriteplaces;
    private int mbNo;


    public static FavoritePlaceFragment newInstance(){
        return new FavoritePlaceFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        mbNo = Common.getmb_No(activity);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_favorite_place, container, false);


    }


    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SearchView searchView = view.findViewById(R.id.searchView);
        FvRecycleview = view.findViewById(R.id.FvRecycleview);
        FvRecycleview.setLayoutManager(new LinearLayoutManager(activity));//直上直下
        //rvPlace.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL,false)); //橫向滑動
        favoriteplaces = getFavoritePlaces();
        showFavoritePlaces(favoriteplaces);
        activity.setTitle("我的收藏");
        SharedPreferences pref = activity.getSharedPreferences(Common.PREFERENCES_MEMBER, Context.MODE_PRIVATE);

    }




    private List<FavoritePlace> getFavoritePlaces() {
        List<FavoritePlace> favoritePlaces = null;
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "/FavoritePlaceServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "findById");
            jsonObject.addProperty("mbNo", mbNo);
            String jsonOut = jsonObject.toString();
            placeGetAllTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = placeGetAllTask.execute().get();
                Type listType = new TypeToken<List<FavoritePlace>>() {
                }.getType();
                favoritePlaces = new Gson().fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }
        return favoritePlaces;

    }


    private void showFavoritePlaces(List<FavoritePlace> favoritePlaces) {
        if (favoritePlaces == null || favoritePlaces.isEmpty()) {
            Common.showToast(activity, R.string.textNoPlacesFound);
        }

        FavoritePlaceAdapter favoritePlaceAdapter = (FavoritePlaceAdapter) FvRecycleview.getAdapter();
        if (favoritePlaceAdapter ==null){
            FvRecycleview.setAdapter(new FavoritePlaceAdapter(activity, favoritePlaces));
        } else {
            favoritePlaceAdapter.setPlaces(favoritePlaces);
            favoritePlaceAdapter.notifyDataSetChanged();
        }
    }

    public class FavoritePlaceAdapter extends RecyclerView.Adapter<FavoritePlaceFragment.FavoritePlaceAdapter.MyViewHolder> {
        private LayoutInflater layoutInflater;
        private List<FavoritePlace> favoriteplaces;
        private int imageSize;

        FavoritePlaceAdapter(Context context, List<FavoritePlace> favoritePlaces) {
            layoutInflater = LayoutInflater.from(context);
            this.favoriteplaces = favoritePlaces;
            /* 螢幕寬度除以3當作將圖的尺寸 */
            imageSize = getResources().getDisplayMetrics().widthPixels / 3;
        }

        void setPlaces(List<FavoritePlace> favoriteplaces) {

            this.favoriteplaces = favoriteplaces;
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
        public FavoritePlaceFragment.FavoritePlaceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.item_view_favorite_place, parent, false);
            return new FavoritePlaceFragment.FavoritePlaceAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull FavoritePlaceFragment.FavoritePlaceAdapter.MyViewHolder holder, int position) {
            final FavoritePlace favoritePlace = favoriteplaces.get(position);
            String url = Common.URL_SERVER + "/PlaceServlet";
            final int id = favoritePlace.getPc_id();
            placeImageTask = new ImageTask(url, id, imageSize, holder.imageView);
            placeImageTask.execute();
            holder.tvName.setText(favoritePlace.getPc_name());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("place", favoritePlace);
                    // Navigation.findNavController(view).navigate(R.id.action_favoritePlaceFragment_to_favoritePlaceDetailsFragment, bundle);
                    Navigation.findNavController(view).navigate(R.id.favoritePlaceDetailsFragment, bundle);

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

                                case R.id.delete:
                                    if (Common.networkConnected(activity)) {
                                        String url = Common.URL_SERVER + "/FavoritePlaceServlet";
                                        JsonObject jsonObject = new JsonObject();
                                        jsonObject.addProperty("action", "favoritePlaceDelete");
                                        jsonObject.addProperty("Pc_id", favoritePlace.getPc_id());
                                        jsonObject.addProperty("mbNo", mbNo);
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
                                            favoriteplaces.remove(favoritePlace);
                                            FavoritePlaceFragment.FavoritePlaceAdapter.this.notifyDataSetChanged();
                                            FavoritePlaceFragment.this.favoriteplaces.remove(favoritePlace);
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
            return favoriteplaces.size();

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
