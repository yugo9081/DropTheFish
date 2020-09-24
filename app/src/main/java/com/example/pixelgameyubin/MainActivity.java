package com.example.pixelgameyubin;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private boolean isMute;
    ImageButton imageButton;
    ImageView imageView;
    RelativeLayout.LayoutParams layoutParams;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        //fixing the imagebutton size
//        imageButton = (ImageButton) findViewById(R.id.play);
//        layoutParams = (RelativeLayout.LayoutParams) imageButton.getLayoutParams();
//        layoutParams.height = 150;
//        layoutParams.width = 550;
//        layoutParams.topMargin = 100;
//        imageButton.setLayoutParams(layoutParams);
//        imageButton.setScaleType(ImageButton.ScaleType.FIT_XY);


        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, GameActivity.class));
            }
        });

        TextView highScoreTxt = findViewById(R.id.highScoreText);

        final SharedPreferences prefs = getSharedPreferences("game", MODE_PRIVATE);
        highScoreTxt.setText("HighScore: " + prefs.getInt("highscore", 0));


        isMute = prefs.getBoolean("isMute", false);
        final ImageView volumeCtrl = findViewById(R.id.volumeCtrl);


        if (isMute)
            volumeCtrl.setImageResource(R.drawable.mute);
        else
            volumeCtrl.setImageResource(R.drawable.volumnup);


        volumeCtrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isMute = !isMute;
                if (isMute)
                    volumeCtrl.setImageResource(R.drawable.mute);
                else
                    volumeCtrl.setImageResource(R.drawable.volumnup);

                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("isMute", isMute);
                editor.apply();

            }
        });


    }


}