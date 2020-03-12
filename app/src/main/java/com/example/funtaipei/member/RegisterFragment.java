package com.example.funtaipei.member;

//HI

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.funtaipei.Common;
import com.example.funtaipei.R;
import com.example.funtaipei.task.CommonTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class RegisterFragment extends Fragment {
    private final static String TAG = "TAG_RegisterFragment";
    private Activity activity;
    private EditText etEmail, etPassword, etName;
    private TextView tvDate, textView5;
    private Button btOK, btBack;
    private Date Date1;
    private SimpleDateFormat simpleDateFormat;
    private RadioGroup rgGender;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        activity = getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = Navigation.findNavController(view);
        etEmail = view.findViewById(R.id.edEmail);
        etPassword = view.findViewById(R.id.etPassword);
        etName = view.findViewById(R.id.etName);
        rgGender = view.findViewById(R.id.rgGender);
        textView5 = view.findViewById(R.id.textView5);

        /* RadioGroup註冊OnCheckedChangeListener監聽器 */
        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = group.findViewById(checkedId);
            }
        });

        tvDate = view.findViewById(R.id.tvDate);
        simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");


        tvDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, String.valueOf(event.getAction()));
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    int inType = tvDate.getInputType();
                    tvDate.setInputType(InputType.TYPE_NULL);
                    tvDate.onTouchEvent(event);
                    tvDate.setInputType(inType);
                    Calendar calendar = Calendar.getInstance();
                    DatePickerDialog datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            Date1 = new Date(year - 1900, month, dayOfMonth);
                            tvDate.setText(simpleDateFormat.format(Date1));
                            //btDatePicker2.setEnabled(true);
                        }
                    },
                            calendar.getTime().getYear() + 1900, calendar.getTime().getMonth(), calendar.getTime().getDate());
                    datePickerDialog.show();
                }
                return true;
            }
        });
        btOK = view.findViewById(R.id.btOk);
        btOK.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onClick(View v) {

                String MB_EMAIL = etEmail.getText().toString();
                if (MB_EMAIL.length() <= 0) {
                    Common.showToast(activity, R.string.textEmailIsInvalid);
                    return;
                }
                String MB_PASSWORD = etPassword.getText().toString();
                if (MB_PASSWORD.length() <= 0) {
                    Common.showToast(activity, R.string.textPasswordIsInvalid);
                    return;
                }
                String MB_NAME = etName.getText().toString();
                if (MB_NAME.length() <= 0) {
                    Common.showToast(activity, R.string.textNameIsInvalid);
                    return;
                }
                String MB_GENDER = rgGender.getCheckedRadioButtonId() == R.id.rbMale ? "男" : "女";
                if (MB_GENDER.length() <= 0) {
                    Common.showToast(activity, R.string.textGenderIsInvalid);
                    return;
                }
                //simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
                Date MB_BIRTHDAY = Date1;
                String MB_BIRTHDAY_Str = tvDate.getText().toString();
                if (MB_BIRTHDAY_Str.length() <= 0) {
                    Common.showToast(activity, R.string.textBirthdayIsInvalid);
                } else {
                    try {
                        MB_BIRTHDAY = simpleDateFormat.parse(MB_BIRTHDAY_Str);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if (Common.networkConnected(activity)) {
                    String url = Common.URL_SERVER + "/MemberServlet";
                    Member member = new Member(0, MB_EMAIL, MB_PASSWORD, MB_NAME, MB_GENDER, MB_BIRTHDAY, 1);
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "memberInsert");
                    Gson gson = new GsonBuilder().setDateFormat("yyyy/MM/dd").create();
                    jsonObject.addProperty("member", gson.toJson(member));
                    int count = 0;
                    try {
                        String result = new CommonTask(url, jsonObject.toString()).execute().get();
                        count = Integer.valueOf(result);
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                    if (count == 0) {
                        Common.showToast(getActivity(), R.string.textRegisterFail);
                    } else if (count == -1) { //判斷已註冊的帳號
                        Common.showToast(getActivity(), R.string.textRegistered);

                    } else if (count == 1) {
                        Common.showToast(getActivity(), R.string.textRegisterSuccess);
                        navController.popBackStack();
                    }
                } else {
                    Common.showToast(getActivity(), R.string.textNoNetwork);
                }
                /* 回前一個Fragment */

            }
        });
        textView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etEmail.setText(R.string.textmemEmail);
                etPassword.setText(R.string.textPass);
                etName.setText(R.string.textmemName);
                tvDate.setText(R.string.textDate);
            }
        });



        btBack = view.findViewById(R.id.btBack);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.popBackStack();
            }
        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}