package com.example.funtaipei.Member;


import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.funtaipei.Common;
import com.example.funtaipei.R;
import com.example.funtaipei.task.CommonTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;


public class LoginFragment extends Fragment {
    private static final String TAG = "TAG_LoginFragment";
    private Activity activity;
    private Button btLogin,btRegister;
    private EditText etEmail,etPassword;
    private CommonTask loginTask;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        btLogin = view.findViewById(R.id.btLogin);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String MB_EMAIL = etEmail.getText().toString();
                String MB_PASSWORD = etPassword.getText().toString();
                String url = Common.URL_SERVER + "/MemberServlet";
                JsonObject jsonObject = new JsonObject();
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
//                JsonObject jsonObject1 = new JsonObject();

                if(isValid){
                    Common.showToast(getActivity(),R.string.textLoginSuccess);
                } else {
                    Common.showToast(getActivity(),R.string.textLoginFail);
                }
                Navigation.findNavController(v).navigate((R.id.action_loginFragment_to_placeListFragment));

            }
        });
        btRegister = view.findViewById(R.id.btRegister);
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment2);
            }
        });

    }
}

