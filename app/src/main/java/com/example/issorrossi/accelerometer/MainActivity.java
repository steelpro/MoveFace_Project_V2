package com.example.issorrossi.accelerometer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    int imagePath = R.drawable.mailhead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        /** BUTTON SELECTIONS CREATED **/
        Button btnLu = (Button) findViewById(R.id.btnLu); //BUTTON FOR DR. LU
        btnLu.setOnClickListener(this);

        Button btnMailman = (Button) findViewById(R.id.btnMailman); //BUTTON FOR ERIC
        btnMailman.setOnClickListener(this);

        Button btnStart = (Button) findViewById(R.id.btnStart); //BUTTON FOR START GAME
        btnStart.setOnClickListener(this);

        Button btnHighScore = (Button) findViewById(R.id.btnHighScore); //BUTTON FOR START GAME
        btnHighScore.setOnClickListener(this);
        /** END BUTTONS **/


        /** LIGHT SENSOR CODE **/
        SensorManager sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if (lightSensor == null) //IF THERE IS NO LIGHT SENSOR BUILT IN
            Toast.makeText(MainActivity.this, "No Light Sensor! quit-", Toast.LENGTH_LONG).show();

        else
            sensorManager.registerListener(lightSensorEventListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        /** END LIGHT SENSOR CODE **/
    }


    /** CODE TO CREATE SENSOR MANAGER **/
    SensorEventListener lightSensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            //WHEN LIGHT SENSOR IS CHANGED, EXECUTE FOLLOWING CODE

            View someView = findViewById(R.id.activity_menu);
            View view = someView.getRootView(); //STORE LAYOUT

            if (event.sensor.getType() == Sensor.TYPE_LIGHT) {

                float currentReading = event.values[0];
                String value = String.valueOf(currentReading);
                String val = String.valueOf(value.charAt(0));
                //VAL IS INTEGER FORM OF LIGHT BEING TAKEN IN

                switch (val) { //CHANGE BASED ON INTEGER VALUE OF LIGHT TAKEN IN
                    case "0": view.setBackgroundColor(Color.DKGRAY); break;
                    case "1": view.setBackgroundColor(Color.DKGRAY); break;
                    case "2": view.setBackgroundColor(Color.GRAY); break;
                    case "3": view.setBackgroundColor(Color.GRAY); break;
                    case "4": view.setBackgroundColor(Color.LTGRAY); break;
                    case "5": view.setBackgroundColor(Color.LTGRAY); break;
                    case "6": view.setBackgroundColor(Color.BLUE); break;
                    case "7": view.setBackgroundColor(Color.BLUE); break;
                    case "8": view.setBackgroundColor(Color.GREEN); break;
                    case "9": view.setBackgroundColor(Color.GREEN); break;
                    case "10": view.setBackgroundColor(Color.WHITE); break;
                    default: view.setBackgroundColor(Color.WHITE); break;
                }
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy){}
        //METHOD NEEDED FOR SENSOR TO WORK
    };
    /** END LIGHT SENSOR MANAGER **/


    /** CODE FOR BUTTONS **/
    @Override
    public void onClick(View v) {

        switch(v.getId()) { //SWITCH IMAGE PATH VARIABLE TO DIFFERENT PICTURES

            case R.id.btnLu: imagePath = R.drawable.luhead; break;
            case R.id.btnMailman: imagePath = R.drawable.mailhead; break;
            case R.id.btnStart:
                Intent intent = new Intent(this, MoveFace.class);
                intent.putExtra("image", imagePath);
                startActivity(intent);
                Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnHighScore:
                viewScores(); //SHOW HIGH SCORES IF ANY
                break;
        }
    }
    /** END CODE FOR BUTTONS **/


    /** CODE TO SHOW DATABASE RECORDS **/
    public void viewScores() {

        MyDBHandler dbHandler = new MyDBHandler(this); //IMPORT DATABASE
        Cursor result = dbHandler.getData();

        if (result.getCount() == 0) { //IF NO RECORDS HAVE BEEN CREATED
            showMessage("Error", "No data has been found!");
            return;
        }

        StringBuffer buffer = new StringBuffer(); //CREATED BUFFER TO STORE RECORDS
        while (result.moveToNext()) { //GIVE IT ALL THE RECORDS
            buffer.append("ID: " + result.getString(0) + "\n");
            buffer.append("Name: " + result.getString(1) + "\n");
            buffer.append("Score (Seconds): " + result.getString(2) + "\n\n");
        }

        showMessage("Data", buffer.toString()); //DISPLAY RECORDS
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    /** END CODE TO SHOW RECORDS **/
}