package com.example.issorrossi.accelerometer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ScoreActivity extends AppCompatActivity {

    public String name;
    public String score = "0";
    public MyDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        dbHandler = new MyDBHandler(this);

        TextView tvScore = (TextView) findViewById(R.id.tvScore);
        Intent i = getIntent();

        score = i.getStringExtra("duration"); //IMPORT DURATION
        tvScore.setText("Your score = " + score + " seconds"); //TELL THEM THE SCORE

        Button btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendMessage();
            }
        }); //CALL METHOD WHEN BUTTON IS CLICKED
    }

    public void sendMessage() {

        EditText txtName = (EditText) findViewById(R.id.etName);

        if (txtName.getText().toString().matches("")) //IF NAME ISN'T FILLED
            Toast.makeText(ScoreActivity.this, "No data to pass!", Toast.LENGTH_LONG).show();

        else {
            name = txtName.getEditableText().toString();
            int intScore = Integer.parseInt(score);
            addToDatabase(name, intScore); //IMPORT NAME AND SEND THAT AND SCORE TO DATABASE
        }
    }

    public void addToDatabase(String name, int score){

        HighScore highScore = new HighScore(name, score);
        boolean isInsert = dbHandler.addScore(highScore);

        if (!isInsert) //IF INSERT IS UNSUCCESSFUL
            Toast.makeText(ScoreActivity.this, "Data could not be stored!", Toast.LENGTH_LONG).show();
        else {
            Intent reset = new Intent(this, MainActivity.class);
            Toast.makeText(ScoreActivity.this, "Data for " + name + " has been stored!", Toast.LENGTH_LONG).show();
            startActivity(reset); //GO BACK TO MAIN MENU
        }
    }
}