package com.example.dailyplanner;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static String idDate;
    public SaveTask setDateId = new SaveTask();
    public Map<View, Integer> mapIdTask = new HashMap<>();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        idDate = sdf.format(d);

        LinearLayout layoutTasks = findViewById(R.id.linearLayout);
        layoutTasks.removeAllViews();

        SharedPreferences sp = getSharedPreferences("mapToSave", MODE_PRIVATE);

        if (sp.contains("map")) {
            Gson gson = new Gson();
            String json = sp.getString("map", "");
            Type type = new TypeToken<Map<String, List<StructTask>>>() {}.getType();
            SaveTask.allTask = gson.fromJson(json, type);
            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            editor.apply();
            editor.commit();
        }

        setDateId.setIdOfTaskInString(idDate);
        mapIdTask.clear();
        if(SaveTask.allTask.containsKey(idDate)){
            int localIdTask = 0;

            for(StructTask oneBlock : SaveTask.allTask.get(idDate)){
                View blockOfTask = getLayoutInflater().inflate(R.layout.block_of_task, layoutTasks, false);
                mapIdTask.put(blockOfTask, localIdTask);
                localIdTask++;
                TextView nameInBlock = blockOfTask.findViewById(R.id.textNameInBlock);
                TextView descriptionInBlock = blockOfTask.findViewById(R.id.textDescriptionInBlock);
                TextView timeInBlock = blockOfTask.findViewById(R.id.textTimeInBlock);
                if(oneBlock.nameOfTask.length() <= 10) {
                    nameInBlock.setText(oneBlock.nameOfTask);
                }else{
                    String partOfName = oneBlock.nameOfTask.substring(0, 10) + "...";
                    nameInBlock.setText(partOfName);
                }
                if(oneBlock.descriptionOfTask.length() <= 20) {
                    descriptionInBlock.setText(oneBlock.descriptionOfTask);
                }else{
                    String partOfDescription = oneBlock.descriptionOfTask.substring(0, 20) + "...";
                    descriptionInBlock.setText(partOfDescription);
                }
                timeInBlock.setText(oneBlock.getTimeInString());

                layoutTasks.addView(blockOfTask);

                OnClickToEditTask(blockOfTask);
            }
        }
        OnClickDate();
    }


    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences = getSharedPreferences("mapToSave", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(SaveTask.allTask);
        editor.putString("map", json);
        editor.apply();
        editor.commit();
    }



    public void OnClickDate(){
        CalendarView calendar = findViewById(R.id.calendarView);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                setDateId.setIdOfTask(year, (month+1), dayOfMonth);

                idDate = year + "-" + (month+1) + "-" + dayOfMonth;

                LinearLayout layoutTasks = findViewById(R.id.linearLayout);
                mapIdTask.clear();
                layoutTasks.removeAllViews();

                if(SaveTask.allTask.containsKey(idDate)){
                    int localIdTask = -1;
                    for(StructTask oneBlock : SaveTask.allTask.get(idDate)){

                        View blockOfTask = getLayoutInflater().inflate(R.layout.block_of_task, layoutTasks, false);

                        localIdTask++;
                        mapIdTask.put(blockOfTask, localIdTask);

                        TextView nameInBlock = blockOfTask.findViewById(R.id.textNameInBlock);
                        TextView descriptionInBlock = blockOfTask.findViewById(R.id.textDescriptionInBlock);
                        TextView timeInBlock = blockOfTask.findViewById(R.id.textTimeInBlock);
                        if(oneBlock.nameOfTask.length() < 10) {
                            nameInBlock.setText(oneBlock.nameOfTask);
                        }else{
                            String partOfName = oneBlock.nameOfTask.substring(0, 10) + "...";
                            nameInBlock.setText(partOfName);
                        }
                        if(oneBlock.descriptionOfTask.length() < 20) {
                            descriptionInBlock.setText(oneBlock.descriptionOfTask);
                        }else{
                            String partOfDescription = oneBlock.descriptionOfTask.substring(0, 20) + "...";
                            descriptionInBlock.setText(partOfDescription);
                        }
                        timeInBlock.setText(oneBlock.getTimeInString());

                        layoutTasks.addView(blockOfTask);

                        OnClickToEditTask(blockOfTask);
                    }
                }else{
                    layoutTasks.removeAllViews();
                }
            }
        });
    }


    public void AddTask(View view){
        LinearLayout layoutTasks = findViewById(R.id.linearLayout);

        View blockOfTask = getLayoutInflater().inflate(R.layout.block_of_task, layoutTasks, false);

        TextView nameInBlock = blockOfTask.findViewById(R.id.textNameInBlock);
        TextView descriptionInBlock = blockOfTask.findViewById(R.id.textDescriptionInBlock);
        TextView timeInBlock = blockOfTask.findViewById(R.id.textTimeInBlock);

        nameInBlock.setText("Задача");
        descriptionInBlock.setText("Описание");
        timeInBlock.setText("12 : 00 - 13 : 00");

        layoutTasks.addView(blockOfTask);

        Intent intent = new Intent(MainActivity.this, EditTask.class);
        intent.putExtra("flag", 0);
        startActivity(intent);
    }

    public void OnClickToEditTask(View blockOfTask){
        blockOfTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditTask.class);
                intent.putExtra("flag", 1);
                intent.putExtra("id", mapIdTask.get(blockOfTask));
                intent.putExtra("idDate", idDate);
                startActivity(intent);
            }
        });
    }

}