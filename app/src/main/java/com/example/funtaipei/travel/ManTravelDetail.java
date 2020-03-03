package com.example.funtaipei.travel;


import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
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
import com.example.funtaipei.task.CommonTask;
import com.example.funtaipei.task.ImageTask;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static android.graphics.Color.LTGRAY;


/**
 * A simple {@link Fragment} subclass.
 */
public class ManTravelDetail extends Fragment {

    private Activity activity;
    private Travel travel;
    private List<TravelDetail> travelDetails;
    private FloatingActionButton manBtnAdd;
    private CommonTask travelDetailGetAllTask;
    private CommonTask travelDetailDeleteTask;
    private ImageTask travelDetailImageTask;
    final String TAG = "getTravelDetails Error";
    private RecyclerView td_recycleView;
    private Place place;
    private ManTravelAdapter manTravelAdapter;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_man_travel_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //接前一頁
        ImageView imageView = view.findViewById(R.id.imageView);
        TextView textView = view.findViewById(R.id.textView);
        Bundle bundle = getArguments();
        if(bundle != null){
            travel = (Travel) bundle.getSerializable("travel");
            String url = Common.URL_SERVER + "/TravelServlet";
            ImageTask imageTask = new ImageTask(url, travel.getTravel_id(), getResources().getDisplayMetrics().widthPixels / 4);
            try{
                Bitmap bitmap = imageTask.execute().get();
                imageView.setImageBitmap(bitmap);
            }catch (Exception e){
                e.printStackTrace();
            }
            textView.setText(travel.getTravel_name());
        }


        //RecycleView
        td_recycleView = view.findViewById(R.id.td_recycleView);
        td_recycleView.setLayoutManager(new LinearLayoutManager(activity));
        travelDetails = getTravelDetails();
        showTravelDetail(travelDetails);


        //長按移動

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemDrag());
        itemTouchHelper.attachToRecyclerView(td_recycleView);

        //Floation Button
        manBtnAdd = view.findViewById(R.id.manBtnAdd);
        manBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("travel", travel);
                Navigation.findNavController(v).navigate(R.id.action_manTravelDetail_to_manTravelInsertFragment, bundle);
            }
        });
    }
    //get TravelDetails
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
    //show
    private void showTravelDetail(List<TravelDetail> travelDetails){
        if(travelDetails == null){
            return;
        }
        ManTravelAdapter manTravelAdapter = (ManTravelAdapter) td_recycleView.getAdapter();
        if(manTravelAdapter == null){
            td_recycleView.setAdapter(new ManTravelAdapter(activity, travelDetails));
        } else {
            manTravelAdapter.setManTravelDetail(travelDetails);
            manTravelAdapter.notifyDataSetChanged();


        }
    }
    //======================================RecycleView=================================//

    private class ManTravelAdapter extends RecyclerView.Adapter<ManTravelAdapter.MyViewHolder>{

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
        public ManTravelAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.travelcollection_item, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public int getItemCount() {
            return travelDetails.size();
        }

        @Override
        public void onBindViewHolder(@NonNull ManTravelAdapter.MyViewHolder holder, int position) {
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
                public boolean onLongClick(View v) {
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
                                            travelDetailDeleteTask = new CommonTask(url, jsonObject.toString());
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
    //拖拉改變位置功能
    class ItemDrag extends ItemTouchHelper.Callback{


        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        }



        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Collections.swap(getTravelDetails(), fromPosition, toPosition);

            manTravelAdapter.notifyItemMoved(fromPosition, toPosition);

            return true;
        }
        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return true;
        }

               @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }

        @Override
        public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
            if(actionState != ItemTouchHelper.ACTION_STATE_DRAG){
                viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
            }
            super.onSelectedChanged(viewHolder, actionState);
        }

        @Override
        public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setBackgroundColor(0);

        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (travelDetailGetAllTask != null) {
            travelDetailGetAllTask.cancel(true);
            travelDetailGetAllTask = null;
        }

        if (travelDetailGetAllTask != null) {
            travelDetailGetAllTask.cancel(true);
            travelDetailGetAllTask = null;
        }

        if (travelDetailDeleteTask != null) {
            travelDetailDeleteTask.cancel(true);
            travelDetailDeleteTask = null;
        }
    }



}
