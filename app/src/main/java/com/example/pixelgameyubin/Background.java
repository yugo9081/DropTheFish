package com.example.pixelgameyubin;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Background { // for moving background
    int x = 0, y = 0;
    Bitmap background;
    long currentTimeMills = System.currentTimeMillis();
    Date date = new Date(currentTimeMills);
    SimpleDateFormat sdfSec = new SimpleDateFormat("ss");
    String secText = sdfSec.format(date);
    int time = Integer.parseInt(secText);

    Background(int screenX, int screenY, Resources res) { //changing the background by time
        if (time >= 0 && time < 15) {
            background = BitmapFactory.decodeResource(res, R.drawable.bg1);
            background = Bitmap.createScaledBitmap(background, screenX, screenY, false);
        } else if (time >= 15 && time < 25) {
            background = BitmapFactory.decodeResource(res, R.drawable.bg2);
            background = Bitmap.createScaledBitmap(background, screenX, screenY, false);
        } else if (time >= 25 && time < 35) {
            background = BitmapFactory.decodeResource(res, R.drawable.bg3);
            background = Bitmap.createScaledBitmap(background, screenX, screenY, false);
        } else if (time >= 35 && time < 47) {
            background = BitmapFactory.decodeResource(res, R.drawable.bg4);
            background = Bitmap.createScaledBitmap(background, screenX, screenY, false);
        } else if (time >= 47 && time < 60) {
            background = BitmapFactory.decodeResource(res, R.drawable.bg7);
            background = Bitmap.createScaledBitmap(background, screenX, screenY, false);
        }


    }


}
