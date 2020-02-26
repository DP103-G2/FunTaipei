package com.example.funtaipei.travel;


import android.app.Activity;
import android.app.job.JobScheduler;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.funtaipei.Common;
import com.example.funtaipei.R;
import com.example.funtaipei.task.CommonTask;
import com.example.funtaipei.task.ImageTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ManTravelInsertFragment extends Fragment {
    final String TAG = "onClick";
    private Activity activity;
    private CommonTask placeGetAllTask;
    private CommonTask placeDeleteTask;
    private ImageTask placeImageTask;
    private List<Place> places;
    private RecyclerView tc_RecycleView;
    private SearchView tc_insertSearch;
    private Travel travel;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_man_travel_insert, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //NaviGation
        final NavController navController = Navigation.findNavController(view);

        //接前一頁的Travel_id
        Bundle bundle = getArguments();
        if(bundle == null || bundle.getSerializable("travel") == null){
            return;
        }
        if(bundle != null ){
            travel = (Travel) bundle.getSerializable("travel");
            if (travel != null){
                String url = Common.URL_SERVER + "/TravelServlet";
                ImageTask imageTask = new ImageTask(url, travel.getTravel_id(), getResources().getDisplayMetrics().widthPixels / 4);
                try {
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        //SearchView
        tc_insertSearch = view.findViewById(R.id.tc_insertSearch);
        tc_insertSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()){
                    showPlace(places);
                } else {
                    List<Place> searchPlace = new ArrayList<>();
                    for(Place place: places){
                        if (place.getPC_NAME().toUpperCase().contains(newText.toUpperCase())){
                            searchPlace.add(place);
                        }
                    }
                    showPlace(searchPlace);
                }
                return true;
            }
            @Override
            public boolean onQueryTextSubmit(String newText) {

                return false;
            }
        });

        //RecycleView
        tc_RecycleView = view.findViewById(R.id.tc_recycleview);
        tc_RecycleView.setLayoutManager(new LinearLayoutManager(activity));
        places = getPlaces();
        showPlace(places);

    }

    //getPlaceList
    private List<Place> getPlaces(){
        List<Place> places = null;
        if(Common.networkConnected(activity)){
            String url = Common.URL_SERVER + "/PlaceServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAll");
            String jsonOut = jsonObject.toString();
            placeGetAllTask = new CommonTask(url, jsonOut);
            try{
                String jsonIn = placeGetAllTask.execute().get();
                Type typelist = new TypeToken<List<Place>>(){
                }.getType();
                places = new Gson().fromJson(jsonIn, typelist);
            }catch(Exception e){
                e.printStackTrace();
            }
        } else {
            Common.showToast(activity, R.string.textNoPlacesFound);
        }
        return places;
    }

    //ShowPlaceList
    private List<Place> showPlace(List<Place> places) {
        if (this.places == null) {
            return this.places;
        }
        PlaceAdapter placeAdapter = (PlaceAdapter) tc_RecycleView.getAdapter();
        if (this.places != null) {
            tc_RecycleView.setAdapter(new PlaceAdapter(activity, this.places));
        } else {
            placeAdapter.setPlace(this.places);
            placeAdapter.notifyDataSetChanged();
        }
        return places;
    }

    //Adapter
    public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.MyViewHolder>{
        private LayoutInflater layoutInflater;
        private int imageSize;
        private List<Place> places;

        PlaceAdapter(Context context, List<Place> places){
            layoutInflater = LayoutInflater.from(context);
            this.places = places;
            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        }

        void setPlace(List<Place> places){
           this.places = places;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView tvName;
            Button tc_btn;

            MyViewHolder(View itemView){
                super(itemView);
                imageView = itemView.findViewById(R.id.tc_imageView);
                tvName = itemView.findViewById(R.id.tc_tvName);
                tc_btn = itemView.findViewById(R.id.tc_btn);


            }
        }

        //設定RecycleView Items
        @NonNull
        @Override
        public PlaceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.tcinsert_item, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public int getItemCount() {
            return places.size();
        }





        @Override
        public void onBindViewHolder(@NonNull PlaceAdapter.MyViewHolder holder, int position) {
            final  Place place = places.get(position);

            String url = Common.URL_SERVER + "/PlaceServlet";
            //Get Place Images
            final int travel_id = travel.getTravel_id();
            final int id = place.getPC_ID();

            placeImageTask = new ImageTask(url, id, imageSize, holder.imageView);
            placeImageTask.execute();
            //Get Place ID
            holder.tvName.setText(place.getPC_NAME());
            holder.tc_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Common.networkConnected(activity)){
                        String url = Common.URL_SERVER + "/TravelDetailServlet";
                        TravelDetail travelDetail = new TravelDetail(travel_id, id);
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("travelDetail", new Gson().toJson(travelDetail));
                        jsonObject.addProperty("action", "travelDetailInsert");
                        int count = 0;
                        try{
                            String result = new CommonTask(url, jsonObject.toString()).execute().get();
                            count = Integer.valueOf(result);
                        }catch (Exception e){
                            Log.d(TAG, "onClick: ");
                        }
                        if(count == 0){
                            Common.showToast(getActivity(), R.string.textInsertFail);
                        } else {
                            Common.showToast(getActivity(), R.string.textInsertSuccess);
                        }
                    } else {
                        Common.showToast(getActivity(), R.string.textNoNetwork);
                    }
                    NavController navController = Navigation.findNavController(v);
                    navController.popBackStack();
                }
            });

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
