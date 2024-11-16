package com.example.securitytoken;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    private TextView code, time;
    private EditText enterCode;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //assign information to textview/button
        code = findViewById(R.id.code);
        time = findViewById(R.id.time);
        enterCode = findViewById(R.id.EnterCode);

        final Button verifyButton = findViewById(R.id.verify);
        verifyButton.setOnClickListener(view -> {
            if (enterCode.getText().toString().equals(code.getText().toString())){
                //move onto verified screen
                Intent verified = new Intent(this, verified.class);
                startActivity(verified);

            } else{
                Toast.makeText(getApplicationContext(), "Incorrect code, try again..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void countdownTimer() {

        //60second timer
        Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                Calendar calendar = Calendar.getInstance();
                final int sec = calendar.get(Calendar.SECOND);

                runOnUiThread(() -> setTimeRemaining(sec));
            }
        }, 0, 1000);
    }

    @SuppressLint("SetTextI18n")
    public void setTimeRemaining (int seconds){

        seconds = 60 - seconds;
        time.setText(seconds + " seconds remaining");
    }

    @SuppressLint("SetTextI18n")
    public void setCode(){

        //passcode calculations for every change
        Calendar calendar = Calendar.getInstance();
        int passCode = (calendar.get(Calendar.MINUTE) * 1245 + 100000);

        //Update UI
        code.setText(Integer.toString(passCode));
    }

    public void onStart(){
        super.onStart();

        setCode();
        countdownTimer();

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.TIME_TICK");

        //change code every minute
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().compareTo(Intent.ACTION_TIME_TICK)==0){
                    setCode();
                }
            }
        };
        registerReceiver(broadcastReceiver, filter);
    }
    public void onStop() {

        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }
}