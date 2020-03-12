package com.example.funtaipei.group;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.funtaipei.Common;
import com.example.funtaipei.R;
import com.example.funtaipei.task.CommonTask;
import com.example.funtaipei.task.ImageTask;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MasterFinishFragment extends Fragment {
    private static final String TAG = "TAG_asterFinishFragment";
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvGroup;
    private Activity activity;
    private CommonTask groupGetByIdTask;
    private ImageTask groupImageTask;
    private List<Group> groups;
    private ImageView imageView;
    private TextView textView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_master_finish, container, false);
    }


    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = Navigation.findNavController(view);
        activity.setTitle("我的揪團");
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        rvGroup = view.findViewById(R.id.rvGroup);
        rvGroup.setLayoutManager(new LinearLayoutManager(activity));
        groups = getGroups();
        showGroups(groups);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                groups = getGroups();
                showGroups(groups);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        imageView = view.findViewById(R.id.imageView);
        textView = view.findViewById(R.id.textView);
        if (groups == null || groups.isEmpty()) {
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
//            btGoGroup.setVisibility(View.VISIBLE);
//            btGoGroup.setEnabled(true);
        }
    }
    private List<Group> getGroups() {
        List<Group> groups = null;
        final SharedPreferences pref = activity.getSharedPreferences(Common.PREFERENCES_MEMBER, Context.MODE_PRIVATE);
        final int MB_NO = pref.getInt("mb_no", 0);
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "/GroupServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "findFinish");
            jsonObject.addProperty("id",MB_NO);
            String jsonOut = jsonObject.toString();
            groupGetByIdTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = groupGetByIdTask.execute().get();
                Type listType = new TypeToken<List<Group>>() {
                }.getType();
                Gson gson = new GsonBuilder().setDateFormat("yyyy/MM/dd HH:mm:ss").create();
                groups = gson.fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }

        return groups;
    }

    private void showGroups(List<Group> groups) {
        if (groups == null || groups.isEmpty()) {
//            Common.showToast(activity, R.string.textNoInsertGroup);
        }
        MasterFinishFragment.GroupAdapter groupAdapter = (MasterFinishFragment.GroupAdapter) rvGroup.getAdapter();
        if (groupAdapter == null) {
            rvGroup.setAdapter(new MasterFinishFragment.GroupAdapter(activity, groups));
        } else {
            groupAdapter.setGroups(groups);
            groupAdapter.notifyDataSetChanged();;
        }
    }


    private class GroupAdapter extends RecyclerView.Adapter<MasterFinishFragment.GroupAdapter.MyViewHolder>{
        private LayoutInflater layoutInflater;
        private List<Group> groups;
        private int imageSize;

        GroupAdapter(Context context, List<Group> groups) {
            layoutInflater = LayoutInflater.from(context);
            this.groups = groups;
            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        }

        void setGroups(List<Group> groups) {
            this.groups = groups;
        }
        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView tvName, tvEventedate, textView;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView);
                tvName = itemView.findViewById(R.id.tvName);
                tvEventedate = itemView.findViewById(R.id.tvEventdate);
                textView = itemView.findViewById(R.id.textView);
            }

        }

        @Override
        public int getItemCount() {
            return groups.size();
        }


        @NonNull
        @Override
        public MasterFinishFragment.GroupAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.fragment_group_item_view, parent, false);
            return new MasterFinishFragment.GroupAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MasterFinishFragment.GroupAdapter.MyViewHolder myViewHolder, int position) {
            final Group group = groups.get(position);
            String url = Common.URL_SERVER + "/GroupServlet";
            int id = group.getGP_ID();
            groupImageTask = new ImageTask(url, id, imageSize, myViewHolder.imageView);
            groupImageTask.execute();
            myViewHolder.textView.setText("已結束");
            myViewHolder.textView.setTextColor(Color.parseColor("#FF0000"));
            myViewHolder.tvName.setText(group.getGP_NAME());
            myViewHolder.tvEventedate.setText("活動日期：" + new SimpleDateFormat("yyyy/MM/dd").format(group.getGP_EVENTDATE()) + new SimpleDateFormat("（E）").format(group.getGP_EVENTDATE()));
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Bundle bundle = new Bundle();

                    bundle.putSerializable("gpid", group);
                    Navigation.findNavController(view).navigate(R.id.groupDetailFragment, bundle);
                }
            });

        }


    }

    @Override
    public void onStop() {
        super.onStop();

        if (groupGetByIdTask != null) {
            groupGetByIdTask.cancel(true);
            groupGetByIdTask = null;
        }

        if (groupImageTask != null) {
            groupImageTask.cancel(true);
            groupImageTask = null;
        }
    }














}
