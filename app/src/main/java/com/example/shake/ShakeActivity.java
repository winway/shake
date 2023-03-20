package com.example.shake;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ShakeActivity extends AppCompatActivity implements SensorEventListener {
    private static final String TAG = "ShakeActivity";

    private ImageView mShakeFlowerIV;
    private ImageView mShakeHandUpIV;
    private ImageView mSHakeHandDownIV;

    private SensorManager mSensorManager;
    private Sensor mSensor;

    private AnimationSet mUpAnimationSet;
    private AnimationSet mDownAnimationSet;

    private SoundPool mSoundPool;
    private int mSoundId;

    private Vibrator mVibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);

        initView();

        initSensor();

        initAnimation();

        initSoundPool();

        initVibrate();
    }

    private void initVibrate() {
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    private void initSoundPool() {
        mSoundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        mSoundId = mSoundPool.load(this, R.raw.awe, 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(this);
    }

    private void initSensor() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void initView() {
        mShakeFlowerIV = findViewById(R.id.shake_flower_iv);
        mShakeHandUpIV = findViewById(R.id.shake_hand_up_iv);
        mSHakeHandDownIV = findViewById(R.id.shake_hand_down_iv);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float[] values = sensorEvent.values;
        float xOffset = values[0];
        float yOffset = values[1];
        float zOffset = values[2];

        if (Math.abs(xOffset) > 12 || Math.abs(yOffset) > 12 || Math.abs(zOffset) > 12) {
            Log.i(TAG, "onSensorChanged: " + xOffset + ", " + yOffset + ", " + zOffset);

            mVibrator.vibrate(new long[]{300, 500}, -1);

            mSoundPool.play(mSoundId, 1, 1, 1, 0, 1);

            mShakeHandUpIV.startAnimation(mUpAnimationSet);
            mSHakeHandDownIV.startAnimation(mDownAnimationSet);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void initAnimation() {
        TranslateAnimation upUpAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1);
        upUpAnimation.setDuration(500);

        TranslateAnimation upDownAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, -1, Animation.RELATIVE_TO_SELF, 0);
        upDownAnimation.setDuration(500);

        mUpAnimationSet = new AnimationSet(true);
        mUpAnimationSet.addAnimation(upUpAnimation);
        mUpAnimationSet.addAnimation(upDownAnimation);
        mUpAnimationSet.setStartOffset(500);

        TranslateAnimation downDownAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1);
        downDownAnimation.setDuration(500);

        TranslateAnimation downUpAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
        downUpAnimation.setDuration(500);

        mDownAnimationSet = new AnimationSet(true);
        mDownAnimationSet.addAnimation(downDownAnimation);
        mDownAnimationSet.addAnimation(downUpAnimation);
        mDownAnimationSet.setStartOffset(500);
    }
}