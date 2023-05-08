package com.example.delahuertaalamesa.load;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.RotateAnimation;

import androidx.appcompat.app.AppCompatActivity;

import com.example.delahuertaalamesa.MainActivity;
import com.example.delahuertaalamesa.databinding.ActivityScreenLoadBinding;

public class ScreenLoad extends AppCompatActivity {
    private ActivityScreenLoadBinding binding;
    private final int TIME = 4500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // binding
        binding = ActivityScreenLoadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // hide buttons
        // View decorView = getWindow().getDecorView();
        // int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        // decorView.setSystemUiVisibility(uiOptions);

        startAnimation();
    }

    /**
     * starts the icon rotation animation
     */
    private void startAnimation() {
        RotateAnimation rotateAnimation = new RotateAnimation(0, (360 * 3), RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(TIME);
        rotateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        binding.imgLogo.startAnimation(rotateAnimation);

        checkNetwork();
    }

    /**
     * checks the internet connection,
     * does not allow to continue until connection is available
     */
    private void checkNetwork() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()) {
            // if there is internet connection at this time
            new Handler().postDelayed(() -> {
                binding.btnEnter.setVisibility(View.VISIBLE);
            }, TIME);
            binding.btnEnter.setText("Comenzar");

            binding.btnEnter.setOnClickListener(v -> {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            });
        } else {
            // if there is NO internet connection at the moment
            new Handler().postDelayed(() -> {
                binding.tvInfoRed.setVisibility(View.VISIBLE);
                binding.btnEnter.setText("Reconectar");
                binding.btnEnter.setVisibility(View.VISIBLE);
            }, TIME);

            binding.btnEnter.setOnClickListener(v -> {
                binding.btnEnter.setVisibility(View.INVISIBLE);
                binding.tvInfoRed.setVisibility(View.INVISIBLE);

                startAnimation();
            });
        }
    }
}