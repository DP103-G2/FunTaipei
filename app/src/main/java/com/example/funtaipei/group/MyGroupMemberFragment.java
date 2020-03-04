package com.example.funtaipei.group;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.funtaipei.Common;
import com.example.funtaipei.R;
import com.example.funtaipei.task.CommonTask;
import com.example.funtaipei.task.ImageTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyGroupMemberFragment extends Fragment {
    private static final String TAG = "TAG_MyGroupMember";
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvMember;
    private Activity activity;
    private CommonTask jgTask;
    private ImageTask mbImageTask;
    private List<JoinGroup> joinGroups;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_my_group_member, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity.setTitle("參團名單");
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        rvMember = view.findViewById(R.id.rvMember);
        rvMember.setLayoutManager(new LinearLayoutManager(activity));
        joinGroups = getJoinGroups();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);

            }
        });
        showJoinGroups(joinGroups);
        joinGroups = getJoinGroups();

    }

    private List<JoinGroup> getJoinGroups() {
        Bundle bundle = getArguments();

        int id = bundle.getInt("gpid");
        List<JoinGroup> joinGroups = null;
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "/JoinGroupServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action","findMember");
            jsonObject.addProperty("id",id);
            String jsonOut = jsonObject.toString();
            jgTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = jgTask.execute().get();
                Type listType = new TypeToken<List<JoinGroup>>(){}.getType();
                Gson gson = new GsonBuilder().setDateFormat("yyyy/MM/dd").create();
                joinGroups = gson.fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }
        return joinGroups;
    }


    private void showJoinGroups(List<JoinGroup> joinGroups) {
        if (joinGroups == null || joinGroups.isEmpty()) {
            Common.showToast(activity, R.string.textNoMemberJoin);
        }
        JoinGroupAdapter joinGroupAdapter = (JoinGroupAdapter) rvMember.getAdapter();
        if (joinGroupAdapter == null) {
            rvMember.setAdapter(new JoinGroupAdapter(activity, joinGroups));
        } else {
            joinGroupAdapter.setJoinGroups(joinGroups);
            joinGroupAdapter.notifyDataSetChanged();
        }

    }



    private class JoinGroupAdapter extends RecyclerView.Adapter<JoinGroupAdapter.MyViewHolder>{
        private LayoutInflater layoutInflater;
        private List<JoinGroup> joinGroups;
        private int imageSize;

        JoinGroupAdapter(Context context, List<JoinGroup> joinGroups) {
            layoutInflater = LayoutInflater.from(context);
            this.joinGroups = joinGroups;
            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        }
        void setJoinGroups(List<JoinGroup> joinGroups) {
            this.joinGroups = joinGroups;
        }
        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView tvName;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView);
                tvName = itemView.findViewById(R.id.tvName);
            }
        }
        @Override
        public int getItemCount() {
            return joinGroups.size();
        }
        @NonNull
        @Override
        public JoinGroupAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.group_member_item, parent, false);
            return new JoinGroupAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            final JoinGroup joinGroup = joinGroups.get(position);
            String url = Common.URL_SERVER + "/JoinGroupServlet";
            int id = joinGroup.getMB_NO();
            mbImageTask = new ImageTask(url, id, imageSize, holder.imageView);
            mbImageTask.execute();
            holder.tvName.setText(joinGroup.getMB_NAME());

        }




    }

    @Override
    public void onStop() {
        super.onStop();

        if (jgTask != null) {
            jgTask.cancel(true);
            jgTask = null;
        }

        if (mbImageTask != null) {
            mbImageTask.cancel(true);
            mbImageTask = null;
        }
    }
}
