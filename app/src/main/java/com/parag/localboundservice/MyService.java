package com.parag.localboundservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import java.util.Random;

public class MyService extends Service {

    private final String TAG = getClass().getSimpleName();
    private boolean isRandomGenerationOn;
    private int randomNumber;
    private final int MIN = 1;
    private final int MAX = 1000;

    class MyServiceBinder extends Binder
    {
         MyService getService()
        {
            return MyService.this;
        }
    }

    IBinder myServiceBinder = new MyServiceBinder();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return myServiceBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.d(TAG, "onRebind");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {
        isRandomGenerationOn = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                startRandomNumberGenerator();
            }
        }).start();
        return START_STICKY;
    }

    public void startRandomNumberGenerator()
    {
        Random random = new Random();
        while(isRandomGenerationOn)
        {
            try {

                if(isRandomGenerationOn) {
                    randomNumber = random.nextInt(MAX) + MIN;
                    Log.d(TAG, "Random number = " + randomNumber);
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopRandomNumberGenerator()
    {
        isRandomGenerationOn = false;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        stopRandomNumberGenerator();
        super.onDestroy();

    }

    public int getRandomNumber()
    {
        return randomNumber;
    }
}
