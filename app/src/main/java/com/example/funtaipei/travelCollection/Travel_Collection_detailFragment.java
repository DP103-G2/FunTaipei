package com.example.funtaipei.travelCollection;


import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.funtaipei.Common;
import com.example.funtaipei.R;
import com.example.funtaipei.task.CommonTask;
import com.example.funtaipei.task.ImageTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class Travel_Collection_detailFragment extends Fragment {

    private Activity activity;
    private TravelCollection travelCollection;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
            String url = Common.URL_SERVER + "/TravelCollectionServlet";
            ImageTask imageTask = new ImageTask(url, travelCollection.getTravel_id(), getResources().getDisplayMetrics().widthPixels / 4);
            try{
                Bitmap bitmap = imageTask.execute().get();
               imageView.setImageBitmap(bitmap);
            }catch(Exception e){
                e.printStackTrace();
            }
            textView.setText(travelCollection.getTravel_name());
        }




    }
}
