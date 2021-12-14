package com.meetbutani.attendanceapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.window.SplashScreen;
import android.window.SplashScreenView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

    Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        int SLEEP_TIME = 1000; // 4000

        this.thread = new Thread() {
            @Override
            public void run() {
                super.run();

                try {
                    sleep(SLEEP_TIME);
                }
                // Error
                catch (Exception e) {
                    e.printStackTrace();
                }
                // always
                finally {
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (currentUser != null && currentUser.isEmailVerified()) {
                        startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                    } else {
                        startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                        finish();
                    }
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        this.thread.start();
    }
}