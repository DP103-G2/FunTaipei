package com.example.funtaipei.favoritePlace;//package com.example.funtaipei.favoritePlace;
//
//import android.app.Activity;
//import android.content.Context;
//import android.os.Build;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.PopupMenu;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.annotation.RequiresApi;
//import androidx.fragment.app.Fragment;
//import androidx.navigation.Navigation;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.funtaipei.Common;
//import com.example.funtaipei.R;
//import com.example.funtaipei.place.Place;
//import com.example.funtaipei.task.CommonTask;
//import com.example.funtaipei.task.ImageTask;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.gson.Gson;
//import com.google.gson.JsonObject;
//import com.google.gson.reflect.TypeToken;
//
//import java.lang.reflect.Type;
//import java.util.List;
//
//public class FvPlaceListFragment extends Fragment {
//    private static final String TAG = "TAG_FvPlaceListFragment";
//    private RecyclerView rvFavoritePlace;
//    private Activity activity;
//    private List<FavoritePlace> favoriteplaces;
//    private CommonTask placeGetAllTask;
//    private CommonTask placeDeleteTask;
//    private ImageTask placeImageTask;
//    private FloatingActionButton favoriteplacebtnAdd;
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        activity = getActivity();
//    }
//
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
//    }
//
//
//    @Override
//    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        rvFavoritePlace = view.findViewById(R.id.rvPlace);
//        rvFavoritePlace.setLayoutManager(new LinearLayoutManager(activity));
//
//
//        favoriteplaces = getFavoritePlaces();
//        showFavoritePlaces(favoriteplaces);
//        activity.setTitle("收藏旅遊點");
//    }
//
//    private List<FavoritePlace> getFavoritePlaces() {
//        List<FavoritePlace> favoritePlaces = null;
//        if (Common.networkConnected(activity)) {
//            String url = Common.URL_SERVER + "/FavoritePlaceServlet";
//            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("action", "getFavoritePlace");
//            String jsonOut = jsonObject.toString();
//            placeGetAllTask = new CommonTask(url, jsonOut);
//            try {
//                String jsonIn = placeGetAllTask.execute().get();
//                Type listType = new TypeToken<List<Place>>() {
//                }.getType();
//                favoritePlaces = new Gson().fromJson(jsonIn, listType);
//            } catch (Exception e) {
//                Log.e(TAG, e.toString());
//            }
//        } else {
//            Common.showToast(activity, R.string.textNoNetwork);
//        }
//        return favoritePlaces;
//    }
//
//
//    private void showFavoritePlaces(List<FavoritePlace> favoritePlaces) {
//        if (favoritePlaces == null || favoritePlaces.isEmpty()) {
//            Common.showToast(activity, R.string.textNoPlacesFound);
//        }
//        FvPlaceListFragment.FvPlaceAdapter fvPlaceAdapter = (FvPlaceListFragment.FvPlaceAdapter)  rvFavoritePlace.getAdapter();
//        /* 如果spotAdapter不存在就建立新的，否則續用舊有的 */
//        if (fvPlaceAdapter == null) {
//            rvFavoritePlace.setAdapter(new FvPlaceListFragment.FvPlaceAdapter(activity, favoriteplaces));
//        } else {
//            fvPlaceAdapter.setFavoritePlaces(favoriteplaces);
//            fvPlaceAdapter.notifyDataSetChanged();
//        }
//    }
//
//    public class FvPlaceAdapter extends RecyclerView.Adapter<FvPlaceAdapter.MyViewHolder> {
//        private LayoutInflater layoutInflater;
//        private List<FavoritePlace> favoritePlaces;
//        private int imageSize;
//
//
//        FvPlaceAdapter(Context context, List<FavoritePlace> favoritePlaces) {
//            layoutInflater = LayoutInflater.from(context);
//            this.favoritePlaces = favoritePlaces;
//            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
//        }
//
//        void setFavoritePlaces(List<FavoritePlace> favoritePlaces) {
//            this.favoritePlaces = favoritePlaces;
//        }
//
//        class MyViewHolder extends RecyclerView.ViewHolder {
//            ImageView imageView;
//            TextView tvName, tvPhone, tvAddress;
//
//            MyViewHolder(View itemView) {
//                super(itemView);
//                imageView = itemView.findViewById(R.id.imageView);
//                tvName = itemView.findViewById(R.id.tvName);
//            }
//
//        }
//
//        @NonNull
//        @Override
//        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View itemView = layoutInflater.inflate(R.layout.item_view_favorite_place, parent, false);
//            return new MyViewHolder(itemView);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//            final FavoritePlace favoritePlace = favoritePlaces.get(position);
//            String url = Common.URL_SERVER + "/PlaceServlet";
//            int id = favoritePlace.getPc_id();
//            placeImageTask = new ImageTask(url, id, imageSize, holder.imageView);
//            placeImageTask.execute();
//            holder.tvName.setText(favoritePlace.getMb_name());
////            holder.tvPhone.setText(String.valueOf(favoritePlace.getPC_PHONE()));
////            holder.tvAddress.setText(place.getPC_ADDRESS());
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("place", favoritePlace);
//                    Navigation.findNavController(view).navigate(R.id.action_placeListFragment_to_placeDetailsFragment, bundle);
//                }
//            });
//            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//                @Override
//                public boolean onLongClick(final View view) {
//                    PopupMenu popupMenu = new PopupMenu(activity, view, Gravity.END);
//                    popupMenu.inflate(R.menu.popup_menu);
//                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                        @Override
//                        public boolean onMenuItemClick(MenuItem item) {
//                            switch (item.getItemId()) {
////                                case R.id.insert:
////                                    Navigation.findNavController(view).navigate(R.id.action_placeListFragment_to_placeDetailsFragment);
////                                    break;
//                                case R.id.update:
//                                    Bundle bundle = new Bundle();
//                                    bundle.putSerializable("place", favoritePlace);
//                                    Navigation.findNavController(view)
//                                            .navigate(R.id.action_placeListFragment_to_placeDetailsFragment, bundle);
//                                    break;
//
//                                case R.id.delete:
//                                    if (Common.networkConnected(activity)) {
//                                        String url = Common.URL_SERVER + "/PlaceServlet";
//                                        JsonObject jsonObject = new JsonObject();
//                                        jsonObject.addProperty("action", "placeDelete");
//                                        jsonObject.addProperty("placeId", favoritePlace.getGp_name());
//                                        int count = 0;
//                                        try {
//                                            placeDeleteTask = new CommonTask(url, jsonObject.toString());
//                                            String result = placeDeleteTask.execute().get();
//                                            count = Integer.valueOf(result);
//                                        } catch (Exception e) {
//                                            Log.e(TAG, e.toString());
//                                        }
//                                        if (count == 0) {
//                                            Common.showToast(activity, R.string.textDeleteFail);
//                                        } else {
//                                            favoritePlaces.remove(favoritePlace);
//                                            FvPlaceAdapter.this.notifyDataSetChanged();
//                                            // 外面spots也必須移除選取的spot
//                                            FvPlaceListFragment.this.favoriteplaces.remove(favoritePlace);
//                                            Common.showToast(activity, R.string.textDeleteSuccess);
//                                        }
//                                    } else {
//                                        Common.showToast(activity, R.string.textNoNetwork);
//                                    }
//                                    break;
//                            }
//                            return true;
//                        }
//                    });
//                    popupMenu.show();
//                    return true;
//                }
//            });
//        }
//
//        @Override
//        public int getItemCount() {
//            return 0;
//        }
//    }
//}
