package com.example.pixelgameyubin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static android.graphics.BitmapFactory.decodeResource;

public class GameView extends SurfaceView implements Runnable {

    private Thread thread;
    private boolean isPlaying, isGameOver = false;
    private int screenX, screenY, score = 0;
    public static float screenRatioX, screenRatioY;
    private Paint paint;
    private Bird[] birds;
    private SharedPreferences prefs;
    private Random random;
    private SoundPool soundPool;
    private List<Bullet> bullets;
    private int sound;
    private MediaPlayer bgm;
    private MediaPlayer game_over;
    private Flight flight;
    private GameActivity activity;

    private Background background1, background2;



    public GameView(GameActivity activity, int screenX, int screenY) {
        super(activity);

        this.activity = activity;

        prefs = activity.getSharedPreferences("game", Context.MODE_PRIVATE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .build();

        } else
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

        sound = soundPool.load(activity, R.raw.shoot, 1);


        //Adding background music
        long currentTimeMills = System.currentTimeMillis();
        Date date = new Date(currentTimeMills);
        SimpleDateFormat sdfSec = new SimpleDateFormat("ss");
        String secText = sdfSec.format(date);
        int time = Integer.parseInt(secText);

        if (time >= 0 && time < 15) {
            bgm = MediaPlayer.create(activity, R.raw.sunshine);
            bgm.setLooping(true);
            bgm.setVolume(100, 100);

        } else if (time >= 15 && time < 25) {
            bgm = MediaPlayer.create(activity, R.raw.earth);
            bgm.setLooping(true);
            bgm.setVolume(100, 100);

        } else if (time >= 25 && time < 35) {
            bgm = MediaPlayer.create(activity, R.raw.fwends);
            bgm.setLooping(true);
            bgm.setVolume(100, 100);

        } else if (time >= 35 && time < 47) {
            bgm = MediaPlayer.create(activity, R.raw.global);
            bgm.setLooping(true);
            bgm.setVolume(100, 100);

        } else if (time >= 47 && time < 60) {
            bgm = MediaPlayer.create(activity, R.raw.palm);
            bgm.setLooping(true);
            bgm.setVolume(100, 100);

        }
        //if player doesn't choose mute
        if (!prefs.getBoolean("isMute", false)){
            bgm.start();
        }


        this.screenX = screenX;
        this.screenY = screenY;
        screenRatioX = 1920f / screenX;
        screenRatioY = 1080f / screenY;

        background1 = new Background(screenX, screenY, getResources());
        background2 = new Background(screenX, screenY, getResources());

        flight = new Flight(this, screenY, getResources());

        bullets = new ArrayList<>();

        background2.x = screenX;

        paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.WHITE);

        birds = new Bird[4];

        for (int i = 0; i < 4; i++) {

            Bird bird = new Bird(getResources());
            birds[i] = bird;

        }

        random = new Random();

    }

    @Override
    public void run() {
        while (isPlaying) {

            update();
            draw();
            sleep();

        }


    }

    private void update() {

        background1.x -= 10 * screenRatioX;
        background2.x -= 10 * screenRatioX;

        if (background1.x + background1.background.getWidth() < 0) {
            background1.x = screenX;
        }

        if (background2.x + background2.background.getWidth() < 0) {
            background2.x = screenX;
        }

        if (flight.isGoingUp)
            flight.y -= 45 * screenRatioY;
        else
            flight.y += 40 * screenRatioY;

        if (flight.y < 0)
            flight.y = 0;

        if (flight.y >= screenY - flight.height)
            flight.y = screenY - flight.height;

        List<Bullet> trash = new ArrayList<>();

        for (Bullet bullet : bullets) {

            if (bullet.x > screenX)
                trash.add(bullet);

            bullet.x += 65 * screenRatioX;

            for (Bird bird : birds) {

                if (Rect.intersects(bird.getCollisionShape(),
                        bullet.getCollisionShape())) {

                    score++;
                    bird.x = -500;
                    bullet.x = screenX + 500;
                    bird.wasShot = true;

                }

            }

        }

        for (Bullet bullet : trash)
            bullets.remove(bullet);

        for (Bird bird : birds) {

            bird.x -= bird.speed;

            if (bird.x + bird.width < 0) {

                if (!bird.wasShot) {
                    isGameOver = true;
                    return;
                }

                int bound = (int) (30 * screenRatioX);
                bird.speed = random.nextInt(bound);

                if (bird.speed < 10 * screenRatioX)
                    bird.speed = (int) (10 * screenRatioX);

                bird.x = screenX;
                bird.y = random.nextInt(screenY - bird.height);

                bird.wasShot = false;
            }

            if (Rect.intersects(bird.getCollisionShape(), flight.getCollisionShape())) {

                isGameOver = true;
                return;
            }

        }

    }

    private void draw() {

        if (getHolder().getSurface().isValid()) {
            Typeface tf =Typeface.createFromAsset(activity.getAssets(),"droid.ttf");
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);

            for (Bird bird : birds)
                canvas.drawBitmap(bird.getBird(), bird.x, bird.y, paint);

            paint.setTypeface(tf);
            canvas.drawText(score + "", screenX / 2f, 164, paint);

            if (isGameOver) {
                Bitmap bmp = decodeResource(getResources(), R.drawable.over);
                int centreX, centreY;
                isPlaying = false;
                bgm.stop(); //stopping the background music

                centreX = (canvas.getWidth()  - bmp.getWidth()) /2;
                centreY = (canvas.getHeight() - bmp.getHeight()) /2;
                canvas.drawBitmap(bmp, centreX, centreY, paint); //show gameover image

                //game over sound
                game_over = MediaPlayer.create(activity, R.raw.splash);
                game_over.setVolume(100, 100);
                if (!prefs.getBoolean("isMute", false)){
                    game_over.start();
                }

                getHolder().unlockCanvasAndPost(canvas);
                saveIfHighScore();

                waitBeforeExiting();

                return;
            }

            canvas.drawBitmap(flight.getFlight(), flight.x, flight.y, paint);

            for (Bullet bullet : bullets)
                canvas.drawBitmap(bullet.bullet, bullet.x, bullet.y, paint);

            getHolder().unlockCanvasAndPost(canvas);

        }

    }


    private void waitBeforeExiting() {

        try {

            Thread.sleep(3000);

            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void saveIfHighScore() {

        if (prefs.getInt("highscore", 0) < score) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("highscore", score);
            editor.apply();
        }

    }

    private void sleep() {

        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {


        isPlaying = true;

        thread = new Thread(this);
        thread.start();

    }

    public void pause() {

        try {

            isPlaying = false; //stopping the game
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getX() < screenX / 2) {
                    flight.isGoingUp = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                flight.isGoingUp = false;
                if (event.getX() > screenX / 2)
                    flight.toShoot++;
                break;
        }

        return true;
    }

    public void newBullet() {

        if (!prefs.getBoolean("isMute", false)){
            soundPool.play(sound, 1, 1, 0, 0, 1);

        }



        Bullet bullet = new Bullet(getResources());
        bullet.x = flight.x + flight.width;
        bullet.y = flight.y + (flight.height / 2);
        bullets.add(bullet);

    }
}