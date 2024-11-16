package com.example.securitytoken;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Time;
import java.util.ArrayList;

public class verified extends AppCompatActivity {

    private ListView timeStamp;
    private ArrayAdapter<Time> adapter;

    //shared preferences
    private final String name = "Time Stamps";
    private SharedPreferences SPTimeStamps;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verified);

        //initialize shared preferences
        timeStamp = findViewById(R.id.timeStamp);
        SPTimeStamps = getSharedPreferences(name, 0);
        editor = SPTimeStamps.edit();


        //clear button
        final Button PASS = findViewById(R.id.pass);
        PASS.setOnClickListener(view -> {
            //clear list and set adapter to empty
            TimeList.getInstance().dateArrayList.clear();
            adapter.clear();
            timeStamp.setAdapter(adapter);
        });
    }

    private void saveTimeStamp(){

        //save most recent timestamps
        long saveRecentTimeStamp = System.currentTimeMillis();
        editor.putLong(name, saveRecentTimeStamp);
        editor.commit();

        Time currentTime = new Time(saveRecentTimeStamp);

        //add to time listview
        TimeList.getInstance().addTime(currentTime);
    }

    public void loadTimeStamp(){

        //load saved timestamps
        long timeStamp = SPTimeStamps.getLong(name, 0);
        Time time = new Time(timeStamp);

        //add date to time listview
        TimeList.getInstance().addTime(time);
    }

    private void listTimeStamp(){

        //update listview with data
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, TimeList.getInstance().dateArrayList);
        timeStamp.setAdapter(adapter);
    }


    @Override
    protected void onStart() {

        super.onStart();

        //load time stamps, save new ones, and display
        loadTimeStamp();
        saveTimeStamp();
        listTimeStamp();
    }

    @Override
    public void onBackPressed()
    {
        //go back to first screen
        super.onBackPressed();
    }
}

class TimeList{

    public ArrayList<Time> dateArrayList = new ArrayList<>();
    private static TimeList instance;

    public void addTime(Time currentTime) {

        if (!dateArrayList.contains(currentTime)){
            dateArrayList.add(currentTime);
        }
    }

    static TimeList getInstance() {

        if (instance == null){
            instance = new TimeList();
        }
        return instance;
    }
}
