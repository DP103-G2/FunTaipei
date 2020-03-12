package com.example.funtaipei.group;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
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
import com.example.funtaipei.travel.Travel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyGroupMemberFragment extends Fragment {
    private static final String TAG = "TAG_MyGroupMember";
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvMember;
    private Activity activity;
    private CommonTask jgTask, groupTask;
    private ImageTask mbImageTask;
    private List<JoinGroup> joinGroups;
    private ImageView imageView;
    private TextView textView;
    private SearchView searchView;
    private Group group;

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

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        rvMember = view.findViewById(R.id.rvMember);
        rvMember.setLayoutManager(new LinearLayoutManager(activity));
        joinGroups = getJoinGroups();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                joinGroups = getJoinGroups();
                showJoinGroups(joinGroups);
                swipeRefreshLayout.setRefreshing(false);

            }
        });
        showJoinGroups(joinGroups);
        joinGroups = getJoinGroups();
        imageView = view.findViewById(R.id.imageView);
        textView = view.findViewById(R.id.textView);
        searchView = view.findViewById(R.id.searchView);
        if (joinGroups == null || joinGroups.isEmpty()) {
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
            searchView.setVisibility(View.GONE);
        }

        if (group == null ) {
            Bundle bundle = getArguments();
            int id = bundle.getInt("gpid");

            if (Common.networkConnected(activity)) {
                String url = Common.URL_SERVER + "/GroupServlet";
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "findById");
                jsonObject.addProperty("id", id);
                String jsonOut = jsonObject.toString();


                groupTask = new CommonTask(url, jsonOut);
                try {
                    String jsonIn = groupTask.execute().get();
                    Gson gson = new GsonBuilder().setDateFormat("yyyy/MM/dd HH:mm:ss").create();
                    group = gson.fromJson(jsonIn, Group.class);
                } catch (Exception e) {
                    Log.d(TAG, e.toString());
                }
            } else {
                Common.showToast(activity, R.string.textNoNetwork);
            }

        }
        activity.setTitle("\"" + group.getGP_NAME() + "\" 參團名單");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                JoinGroupAdapter joinGroupAdapter = (JoinGroupAdapter) rvMember.getAdapter();
                if (joinGroupAdapter != null) {
                    if (s.isEmpty()) {
                        showJoinGroups(joinGroups);
                    } else {
                        List<JoinGroup> searchJoinGroups = new ArrayList<>();
                        for (JoinGroup joinGroup : joinGroups) {
                            if (joinGroup.getMB_NAME().toUpperCase().contains(s.toUpperCase())) {
                                searchJoinGroups.add(joinGroup);
                            }
                        }
                        joinGroupAdapter.setJoinGroups(searchJoinGroups);
                    }
                    joinGroupAdapter.notifyDataSetChanged();
                    return true;
                }
                return false;
            }
        });

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
                Gson gson = new GsonBuilder().setDateFormat("yyyy/MM/dd HH:mm:ss").create();
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
//            Common.showToast(activity, R.string.textNoMemberJoin);
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

        if (groupTask != null) {
            groupTask.cancel(true);
            groupTask = null;
        }
    }
}
