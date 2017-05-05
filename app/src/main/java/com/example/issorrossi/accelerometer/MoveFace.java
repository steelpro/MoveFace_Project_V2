package com.example.issorrossi.accelerometer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class MoveFace extends Activity implements SensorEventListener{

    Bitmap face;
    Bitmap body;
    SensorManager sm;
    Face ourView;
    float x, y, sensorX, sensorY;
    public Canvas canvas;
    boolean win;
    Paint paint = new Paint();
    boolean isDestroyed = false;
    Intent intent2;
    public long startTime;
    public long stopTime;
    public String duration;

    @Override
    public void onSensorChanged(SensorEvent event) {
        try{
            Thread.sleep(20);
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }

        sensorX = event.values[1];
        //value[1] is usually Y axis but app is in landscape so...
        sensorY = event.values[0];
        //...these values flip flop
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // no code to add here
    }

    public class Face extends SurfaceView implements Runnable{

        SurfaceHolder ourHolder;
        boolean isRunning = true;

        Thread ourThread = null;

        public Face(Context context){
            super(context);
            ourHolder = getHolder();
        }

        public void resume(){
            isRunning = true;
            ourThread = new Thread(this);
            ourThread.start();
            canvas = null;
        }

        public void destroy(){
            isRunning = false;
            ourThread = null;
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {

            if(event.getAction() == MotionEvent.ACTION_DOWN) {

                paint.setTextSize(150);
                paint.setColor(Color.BLUE);

                //IF PLAYER WINS
                if(sensorX < 3.23 && sensorX > 2.83 && sensorY < -1.17 && sensorY > -1.65){

                    stopTime = System.nanoTime(); //STOP COUNTING
                    long dur = (stopTime - startTime) / 1000000000; //CALCULATE DURATION
                    duration = String.valueOf(dur);
                    intent2.putExtra("duration", duration); //SEND TO INTENT
                    win = true; //CHANGE TO WIN
                }
                else { win = false; }

                isDestroyed = true;
            }
            return true;
        }

        @Override
        public void run() {

            while(isRunning){

                if(!ourHolder.getSurface().isValid())
                    continue;

                float startX = 700; //change starting points if need be.
                float startY = 250; //add these variables to canvas.drawBitmap

                canvas = ourHolder.lockCanvas();
                canvas.drawColor(Color.WHITE);
                canvas.drawBitmap(body, 1100, 150, null);
                canvas.drawBitmap(face, startX + sensorX * 200, startY + sensorY * 150, null);
                //changing sensorX*num and sensorY*num changes travel distance on screen

                ourHolder.unlockCanvasAndPost(canvas);

                if(isDestroyed){
                    //Once Canvas is Locked it guards against other threads accessing resources
                    canvas = ourHolder.lockCanvas();

                    if(!win)
                        canvas.drawText("You lose!!!", 200, 600, paint);
                    else //LAUNCH SECOND ACTIVITY ONLY IF WIN
                        startActivity(intent2);

                    ourHolder.unlockCanvasAndPost(canvas);
                    destroy();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //initializes all Devices Sensors
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        intent2 = new Intent(this, ScoreActivity.class);

        startTime = System.nanoTime(); //START COUNTING WHEN GAME BEGINS

        //if devices has an accelerometer create a listener
        if (sm.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0){
            Sensor Accelerometer = sm.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
            sm.registerListener(this, Accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }

        Intent i = getIntent();
        int imagePath = i.getIntExtra("image", 0);
        int suit = R.drawable.suit;

        face = BitmapFactory.decodeResource(getResources(), imagePath);
        body = BitmapFactory.decodeResource(getResources(), suit);
        x = y = sensorX = sensorY = 0;

        ourView = new Face(this);
        ourView.resume();
        setContentView(ourView);
    }

    @Override
    protected void onPause(){
        sm.unregisterListener(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ourView.destroy();
    }
}