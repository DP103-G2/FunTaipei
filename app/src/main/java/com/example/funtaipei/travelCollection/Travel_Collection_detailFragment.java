package com.example.funtaipei.travelCollection;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.funtaipei.Common;
import com.example.funtaipei.R;
import com.example.funtaipei.task.CommonTask;
import com.example.funtaipei.task.ImageTask;
import com.example.funtaipei.travel.Image;
import com.example.funtaipei.travel.Place;
import com.example.funtaipei.travel.Travel;
import com.example.funtaipei.travelDetail.TravelDetail;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class Travel_Collection_detailFragment extends Fragment {

    private Activity activity;
    private TravelCollection travelCollection;
    private ImageTask imageTask;
    private CommonTask getAllTask;
    private RecyclerView recyclerView;
    private List<TravelDetail> travelDetails;
    private Travel travel;
    private Place place;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_travel__collection_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //接前一頁title
        ImageView imageView = view.findViewById(R.id.imageView);
        TextView textView = view.findViewById(R.id.detailTextView);
        Bundle bundle = getArguments();
        if(bundle != null){
            travelCollection = (TravelCollection) bundle.getSerializable("travelCollection");
            String url = Common.URL_SERVER + "/TravelServlet";
            ImageTask imageTask = new ImageTask(url, travel.getTravel_id(), getResources().getDisplayMetrics().widthPixels / 4);
            try{
                Bitmap bitmap = imageTask.execute().get();
                imageView.setImageBitmap(bitmap);
            }catch(Exception e){
                e.printStackTrace();
            }
            textView.setText(travelCollection.getTravel_name());
        }
      // RecycleView

        recyclerView = view.findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        travelDetails = getTravelDetail();
        showTravelDetail(travelDetails);

    }

//-----------------------------------------------------------------------------------------------------------------
    //getTravelDetail
    public List<TravelDetail> getTravelDetail(){
        List<TravelDetail> travelDetails = null;
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "/TravelServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "findByTravelId");
            jsonObject.addProperty("id", travel.getTravel_id());
            String jsonOut = jsonObject.toString();

            getAllTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = getAllTask.execute().get();
                Type listType = new TypeToken<List<TravelDetail>>() {
                }.getType();
                travelDetails = new Gson().fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.d(TAG, "getTravelDetails: ");
            }
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }
       return travelDetails;
    }

    public void showTravelDetail(List<TravelDetail> travelDetails){
        TravelDetailAdapter travelDetailAdapter = (TravelDetailAdapter) recyclerView.getAdapter();
        if(travelDetailAdapter == null){
            recyclerView.setAdapter(new TravelDetailAdapter(activity, travelDetails));
        } else {
            travelDetailAdapter.setTravelDetails(travelDetails);
            travelDetailAdapter.notifyDataSetChanged();
        }

    }
//======================RecycleView======================//

        private class TravelDetailAdapter extends RecyclerView.Adapter<TravelDetailAdapter.MyViewHolder> {

        private LayoutInflater layoutInflater;
        private List<TravelDetail> travelDetails;
        private int imageSize;

        TravelDetailAdapter(Context context, List<TravelDetail> travelDetails){
            layoutInflater = LayoutInflater.from(context);
            this.travelDetails = travelDetails;
            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        }

        void setTravelDetails(List<TravelDetail> travelDetails){
            this.travelDetails = travelDetails;
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            ImageView imageView;
            TextView textView;
            MyViewHolder(View itemView){
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView);
                textView = itemView.findViewById(R.id.pc_name);
            }
        }
            @Override
            public int getItemCount() {
                return travelDetails.size();
            }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.travel_detail_item, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            final TravelDetail travelDetail = travelDetails.get(position);
            String url = Common.URL_SERVER + "/TravelServlet";
            int id = travelDetail.getPc_id();
            imageTask = new ImageTask(url, id, imageSize, holder.imageView);
            imageTask.execute();
            holder.textView.setText(travelDetail.getPc_name());
            //要寫刪 and 增

        }


    }

}
