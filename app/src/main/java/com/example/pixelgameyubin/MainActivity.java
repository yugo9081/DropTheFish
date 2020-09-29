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
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private boolean isMute;
    private Context mContext;
    private Activity mActivity;
    private MediaPlayer title_bgm;
    private RelativeLayout mRelativeLayout;
    private ImageButton mButton;
    private PopupWindow mPopupWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        // Get the activity
        mActivity = MainActivity.this;

        TextView highScoreTxt = findViewById(R.id.highScoreText);

        final SharedPreferences prefs = getSharedPreferences("game", MODE_PRIVATE);
        highScoreTxt.setText("HIGHSCORE: " + prefs.getInt("highscore", 0));
        mContext = getApplicationContext();

        isMute = prefs.getBoolean("isMute", false);
        final ImageView volumeCtrl = findViewById(R.id.volumeCtrl);
        title_bgm = MediaPlayer.create(mActivity, R.raw.title);
        title_bgm.setLooping(true);
        title_bgm.setVolume(100, 100);
        if(!isMute){
            title_bgm.start();
        }

        if (isMute){
            volumeCtrl.setImageResource(R.drawable.mute);
        }

        else{
            volumeCtrl.setImageResource(R.drawable.volumnup);

        }




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


                if(isMute){
                    if(title_bgm.isPlaying()){
                        title_bgm.pause();
                    }
                }
                else{
                    title_bgm.start();
                }
            }
        });



        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title_bgm.stop();
                startActivity(new Intent(MainActivity.this, GameActivity.class));
            }
        });






        // Get the widgets reference from XML layout
        mRelativeLayout = (RelativeLayout) findViewById(R.id.rl);
        mButton = (ImageButton) findViewById(R.id.help_but);

        // Set a click listener for the text view
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialize a new instance of LayoutInflater service
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

                // Inflate the custom layout/view
                View customView = inflater.inflate(R.layout.popup,null);

                // Initialize a new instance of popup window
                mPopupWindow = new PopupWindow(
                        customView,
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );

                // Set an elevation value for popup window
                // Call requires API level 21
                if(Build.VERSION.SDK_INT>=21){
                    mPopupWindow.setElevation(5.0f);
                }

                // Get a reference for the custom view close button
                ImageButton closeButton = (ImageButton) customView.findViewById(R.id.ib_close);

                // Set a click listener for the popup window close button
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Dismiss the popup window
                        mPopupWindow.dismiss();
                    }
                });

                // Finally, show the popup window at the center location of root relative layout
                mPopupWindow.showAtLocation(mRelativeLayout, Gravity.CENTER,0,0);
            }
        });


    }


}
