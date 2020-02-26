package com.example.funtaipei.group;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import java.util.ArrayList;
import java.util.List;


public class GroupListFragment extends Fragment {
    private static final String TAG = "TAG_GroupListFragment";
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvGroup;
    private Activity activity;
    private CommonTask groupGetAllTask, memberGetIdTask, loginTask;
    private ImageTask groupImageTask;
    private List<Group> groups;
    private EditText etEmail, etPassword;
    private Button btLogin, btRegister;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_group_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity.setTitle("旅遊團體");
        SearchView searchView = view.findViewById(R.id.searchView);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        rvGroup = view.findViewById(R.id.rvGroup);
        rvGroup.setLayoutManager(new LinearLayoutManager(activity));
        groups = getGroups();
        showGroups(groups);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                showGroups(groups);
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
                GroupAdapter groupAdapter = (GroupAdapter) rvGroup.getAdapter();
                if (groupAdapter != null) {
                    if (newText.isEmpty()) {
                        showGroups(groups);
                    } else {
                        List<Group> searchGroups = new ArrayList<>();
                        for (Group group : groups) {
                            if (group.getGP_NAME().toUpperCase().contains(newText.toUpperCase())) {
                                searchGroups.add(group);
                            }
                            if (String.valueOf(group.getGP_ID()).contains(newText.toUpperCase())) {
                                searchGroups.add(group);
                            }
                        }
                        groupAdapter.setGroups(searchGroups);
                    }
                    groupAdapter.notifyDataSetChanged();
                    return true;
                }
                return false;
            }
        });
        FloatingActionButton btAdd = view.findViewById(R.id.btAdd);
        btAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View view) {
                final SharedPreferences pref = activity.getSharedPreferences(Common.PREFERENCES_MEMBER, Context.MODE_PRIVATE);
                final int MB_NO = pref.getInt("mb_no", 0);
                if (MB_NO == 0){
                    LayoutInflater inflater = LayoutInflater.from(activity);
                    final View v = inflater.inflate(R.layout.fragment_login,null);

                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
                    final AlertDialog alert = alertDialogBuilder.create();
                    alert.setTitle(R.string.textPleaseLogin);
                    alert.setIcon(R.drawable.alert);
                    alert.setView(v);
                    alert.show();







                    etEmail = v.findViewById(R.id.etEmail);
                    etPassword = v.findViewById(R.id.etPassword);
                    btLogin = v.findViewById(R.id.btLogin);
                    btLogin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String MB_EMAIL = etEmail.getText().toString();
                            String MB_PASSWORD = etPassword.getText().toString();
                            String url = Common.URL_SERVER + "/MemberServlet";
                            JsonObject jsonObject = new JsonObject();
                            //對應server的login
                            jsonObject.addProperty("action", "login");
                            jsonObject.addProperty("email", MB_EMAIL);
                            jsonObject.addProperty("password", MB_PASSWORD);

                            loginTask = new CommonTask(url, new Gson().toJson(jsonObject));
                            boolean isValid = false;
                            try {
                                String inStr = loginTask.execute().get();
                                isValid = Boolean.parseBoolean(inStr);
                            } catch (Exception e) {
                                Log.e(TAG, e.toString());
                            }

                            if(isValid){
                                Common.showToast(getActivity(), R.string.textLoginSuccess);

                                pref.edit().putString("email",MB_EMAIL)
                                        .putString("password", MB_PASSWORD)
                                        .putInt("mb_no",getUserIdByEmail(MB_EMAIL))
                                        .apply();
                                alert.cancel();
                            } else {
                                Common.showToast(getActivity(), R.string.textLoginFail);
                            }

                        }
                    });
                    btRegister = v.findViewById(R.id.btRegister);
                    btRegister.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Navigation.findNavController(view).navigate(R.id.registerFragment);
                            alert.cancel();
                        }
                    });

                }
                if (MB_NO != 0) {
                    Navigation.findNavController(view).navigate(R.id.action_groupListFragment_to_groupInsertFragment);
                }
            }
        });
    }

    private List<Group> getGroups() {
        List<Group> groups = null;
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "/GroupServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAll");
            String jsonOut = jsonObject.toString();
            groupGetAllTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = groupGetAllTask.execute().get();
                Type listType = new TypeToken<List<Group>>() {
                }.getType();
                Gson gson = new GsonBuilder().setDateFormat("yyyy/MM/dd").create();
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
            Common.showToast(activity, R.string.textNoGroupsFound);
        }
        GroupAdapter groupAdapter = (GroupAdapter) rvGroup.getAdapter();
        // 如果spotAdapter不存在就建立新的，否則續用舊有的
        if (groupAdapter == null) {
            rvGroup.setAdapter(new GroupAdapter(activity, groups));
        } else {
            groupAdapter.setGroups(groups);
            groupAdapter.notifyDataSetChanged();
        }
    }


    private class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.MyViewHolder> {
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
            Button button;
            TextView tvName, tvEventdate, tvNotes;


            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView);
                tvName = itemView.findViewById(R.id.tvName);
                tvEventdate = itemView.findViewById(R.id.tvEventdate);
//                tvNotes = itemView.findViewById(R.id.tvNotes);


            }
        }

        @Override
        public int getItemCount() {
            return groups.size();
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.fragment_group_item_view, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
            final Group group = groups.get(position);

            String url = Common.URL_SERVER + "/GroupServlet";
            int id = group.getGP_ID();

            groupImageTask = new ImageTask(url, id, imageSize, myViewHolder.imageView);
            groupImageTask.execute();
            myViewHolder.tvName.setText(group.getGP_NAME());
            myViewHolder.tvEventdate.setText("活動日期：" + new SimpleDateFormat("yyyy/MM/dd").format(group.getGP_EVENTDATE()) + new SimpleDateFormat("（E）").format(group.getGP_EVENTDATE()));

            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("group", group);
                    Navigation.findNavController(view).navigate(R.id.action_groupListFragment_to_groupDetailFragment, bundle);
                }
            });

        }

    }
    private int getUserIdByEmail(String mb_email) {
        int mb_no = 0;
        if(Common.networkConnected(activity)){
            String url = Common.URL_SERVER + "/MemberServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getUserIdByEmail");
            jsonObject.addProperty("email", mb_email);
            String jsonOut = jsonObject.toString();
            memberGetIdTask = new CommonTask(url , jsonOut);
            try {
                //傳入string 回傳string轉型 int(id)
                String result = memberGetIdTask.execute().get();
                Log.d(TAG, result);
                mb_no = Integer.parseInt(result);
            }catch (Exception e){
                Log.e(TAG, e.toString());
            }
        }else{
            Common.showToast(activity, R.string.textNoNetwork);
        }
        return mb_no;
    }
    @Override
    public void onStop() {
        super.onStop();
        if(memberGetIdTask != null) {
            memberGetIdTask.cancel(true);
            memberGetIdTask = null;
        }

        if(loginTask != null) {
            loginTask.cancel(true);
            loginTask = null;
        }

        if (groupGetAllTask != null) {
            groupGetAllTask.cancel(true);
            groupGetAllTask = null;
        }

        if (groupImageTask != null) {
            groupImageTask.cancel(true);
            groupImageTask = null;
        }
    }
}
