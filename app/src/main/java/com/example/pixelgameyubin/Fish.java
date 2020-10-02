package com.example.pixelgameyubin;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.pixelgameyubin.GameView.screenRatioX;
import static com.example.pixelgameyubin.GameView.screenRatioY;

public class Fish {

    public int speed = 20;
    public boolean wasShot = true;
    int x = 0, y, width, height, fishCounter = 1;
    Bitmap fish1, fish2, fish3, fish4;

    Fish(Resources res) {

        fish1 = BitmapFactory.decodeResource(res, R.drawable.fish2);
        fish2 = BitmapFactory.decodeResource(res, R.drawable.fish2);
        fish3 = BitmapFactory.decodeResource(res, R.drawable.fish2);
        fish4 = BitmapFactory.decodeResource(res, R.drawable.fish2);

        width = fish1.getWidth();
        height = fish1.getHeight();

        width /= 6;
        height /= 6;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        fish1 = Bitmap.createScaledBitmap(fish1, width, height, false);
        fish2 = Bitmap.createScaledBitmap(fish2, width, height, false);
        fish3 = Bitmap.createScaledBitmap(fish3, width, height, false);
        fish4 = Bitmap.createScaledBitmap(fish4, width, height, false);

        y = -height;
    }

    Bitmap getFish() {

        if (fishCounter == 1) {
            fishCounter++;
            return fish1;
        }

        if (fishCounter == 2) {
            fishCounter++;
            return fish2;
        }

        if (fishCounter == 3) {
            fishCounter++;
            return fish3;
        }

        fishCounter = 1;

        return fish4;
    }

    Rect getCollisionShape() {
        return new Rect(x, y, x + width, y + height);
    }

}