package com.example.funtaipei.group;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MasterFragment extends Fragment {
    private static final String TAG = "TAG_MasterFragment";
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvGroup;
    private Activity activity;
    private CommonTask groupGetByIdTask, groupDeleteTask;
    private ImageTask groupImageTask;
    private List<Group> groups;
    private FloatingActionButton btAdd;
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
        return inflater.inflate(R.layout.fragment_master, container, false);
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
                showGroups(groups);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        btAdd = view.findViewById(R.id.btAdd);
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.groupInsertFragment);
            }
        });
        imageView = view.findViewById(R.id.imageView);
        textView = view.findViewById(R.id.textView);
//        if (groups == null || groups.isEmpty()) {
//            imageView.setVisibility(View.VISIBLE);
//            textView.setVisibility(View.VISIBLE);
////            btGoGroup.setVisibility(View.VISIBLE);
////            btGoGroup.setEnabled(true);
//        }


        if (groups == null || groups.isEmpty()) {
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);


            new AlertDialog.Builder(activity)
                    .setTitle("您尚未揪團")
                    .setIcon(R.drawable.alert)
                    .setMessage("您可以在團體建立揪團")
                    .setPositiveButton(R.string.textaddGroup, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Navigation.findNavController(view).navigate(R.id.groupInsertFragment);
                            dialogInterface.cancel();
                        }

                    })
                    .setNegativeButton(R.string.textNo, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
//                            navController.popBackStack();
                        }
                    })
                    .show();

//            new AlertDialog.Builder(activity)
//                    /* 設定標題 */
//                    .setTitle(R.string.textTitle)
//                    /* 設定圖示 */
//                    .setIcon(R.drawable.alert)
//                    /* 設定訊息文字 */
//                    .setMessage(R.string.textMessage)
//                    /* 設定positive與negative按鈕上面的文字與點擊事件監聽器 */
//                    .setPositiveButton(R.string.textYes, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            /* 結束此Activity頁面 */
//                            activity.finish();
//                        }
//                    })
//                    .setNegativeButton(R.string.textNo, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            /* 關閉對話視窗 */
//                            dialog.cancel();
//                        }
//                    })
//                    .show();
        }



    }

    private List<Group> getGroups() {
        List<Group> groups = null;
        final SharedPreferences pref = activity.getSharedPreferences(Common.PREFERENCES_MEMBER, Context.MODE_PRIVATE);
        final int MB_NO = pref.getInt("mb_no", 0);
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "/GroupServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "master");
            jsonObject.addProperty("id",MB_NO);
            String jsonOut = jsonObject.toString();
            groupGetByIdTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = groupGetByIdTask.execute().get();
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
//            Common.showToast(activity, R.string.textNoInsertGroup);
        }
        GroupAdapter groupAdapter = (GroupAdapter) rvGroup.getAdapter();
        if (groupAdapter == null) {
            rvGroup.setAdapter(new GroupAdapter(activity, groups));
        } else {
            groupAdapter.setGroups(groups);
            groupAdapter.notifyDataSetChanged();;
        }
    }


    private class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.MyViewHolder>{
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
        public GroupAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.fragment_group_item_view, parent, false);
            return new GroupAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull GroupAdapter.MyViewHolder myViewHolder, int position) {
            final Group group = groups.get(position);
            String url = Common.URL_SERVER + "/GroupServlet";
            int id = group.getGP_ID();
            groupImageTask = new ImageTask(url, id, imageSize, myViewHolder.imageView);
            groupImageTask.execute();
//            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            Calendar curDate = Calendar.getInstance();
            if (group.getGP_DATEEND().getTime() < curDate.getTimeInMillis() && group.getGP_EVENTDATE().getTime() > curDate.getTimeInMillis()) {
                myViewHolder.textView.setText("截止報名");
                myViewHolder.textView.setTextColor(Color.parseColor("#FFD700"));
            } else if (group.getGP_DATESTAR().getTime() < curDate.getTimeInMillis() && group.getGP_DATEEND().getTime() > curDate.getTimeInMillis()){
                myViewHolder.textView.setText("報名中");
                myViewHolder.textView.setTextColor(Color.parseColor("#00FF99"));
            }


            else if(group.getGP_DATESTAR().getTime() > curDate.getTimeInMillis()) {
                myViewHolder.textView.setText("未開放");
                myViewHolder.textView.setTextColor(Color.parseColor("#E91E63"));
                myViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(final View view) {
                        PopupMenu popupMenu = new PopupMenu(activity, view, Gravity.END);
                        popupMenu.inflate(R.menu.update_group);
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                switch (menuItem.getItemId()) {
                                    case R.id.update:
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("group", group);
                                        Navigation.findNavController(view).navigate(R.id.groupUpdateFragment, bundle);
                                        break;
                                    case R.id.delete:
                                        if (Common.networkConnected(activity)) {
                                            String url = Common.URL_SERVER + "/GroupServlet";
                                            JsonObject jsonObject = new JsonObject();
                                            jsonObject.addProperty("action", "delete");
                                            jsonObject.addProperty("id", group.getGP_ID());
                                            int count = 0;
                                            try {
                                                groupDeleteTask = new CommonTask(url, jsonObject.toString());
                                                String result = groupDeleteTask.execute().get();
                                                count = Integer.valueOf(result);
                                            } catch (Exception e) {
                                                Log.e(TAG, e.toString());
                                            }
                                            if (count != 2) {
                                                Common.showToast(activity, R.string.textDeleteFail);
                                            } else {
                                                groups.remove(group);
                                                GroupAdapter.this.notifyDataSetChanged();
                                                MasterFragment.this.groups.remove(group);
                                                Common.showToast(activity, R.string.textDeleteSuccess);
                                            }
                                        } else {
                                            Common.showToast(activity, R.string.textNoNetwork);
                                        }

                                }
                                return true;
                            }


                        });
                        popupMenu.show();
                        return true;
                    }

                });
            }
//            else /*if (group.getGP_DATEEND().getTime() <= curDate.getTimeInMillis())*/ {
//                myViewHolder.textView.setText("截止報名");
//                myViewHolder.textView.setTextColor(Color.parseColor("#FFD700"));
//            }
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

        if (groupDeleteTask != null) {
            groupDeleteTask.cancel(true);
            groupDeleteTask = null;
        }
    }


}
