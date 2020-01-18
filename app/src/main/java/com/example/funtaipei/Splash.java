package com.example.funtaipei;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.funtaipei.main.MainActivity;

public class Splash extends AppCompatActivity {
    private RelativeLayout logo;
    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Animation fadeIn = AnimationUtils.loadAnimation(Splash.this, R.anim.fade_in);
        logo = findViewById(R.id.logo);
        imageView = findViewById(R.id.imageView);
        logo.setAnimation(fadeIn);

        Thread timer = new Thread(){
            @Override
            public void run() {
                super.run();
                try{
                    sleep(3000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    startActivity(new Intent(Splash.this, MainActivity.class));
                    finish();
                    overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                }
            }
        };

        timer.start();

    }
}
