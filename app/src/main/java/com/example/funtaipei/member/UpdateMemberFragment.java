package com.example.funtaipei.member;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.funtaipei.R;


public class UpdateMemberFragment extends Fragment {
    private static final String TAG = "TAG_UpdateMemberFragment";
    private Activity activity;
//    private ImageView ivMember;
//    private Member member;
//    private TextView tvEmail;
//    private EditText etName, etBdy;
//    private byte[] image;
//    private static final int REQ_TAKE_PICTURE = 0;
//    private static final int REQ_PICK_IMAGE = 1;
//    private static final int REQ_CROP_PICTURE = 2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_update_member, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        ivMember = view.findViewById(R.id.ivMember);
//        tvEmail = view.findViewById(R.id.tvEmail);
//        etName = view.findViewById(R.id.etName);
//        etBdy = view.findViewById(R.id.etBdy);

//        final NavController navController = Navigation.findNavController(view);
//        Bundle bundle = getArguments();
//        if (bundle != null && bundle.getSerializable("member") != null) {
//            member = (Member) bundle.getSerializable("member");
//        }
    }
}
