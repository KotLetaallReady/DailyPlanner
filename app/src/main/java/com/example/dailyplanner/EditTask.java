package com.example.dailyplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

public class EditTask extends AppCompatActivity {
    int flagOfEditTask;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        int id = getIntent().getIntExtra("id", -1);
        this.id = id;
        int flagOfEditTask = getIntent().getIntExtra("flag", -1);
        this.flagOfEditTask = flagOfEditTask;

        //Проверка на действие с задачей (либо создание новой задачи, либо редактирование старой)
        MainActivity mainObject = new MainActivity();
        if(flagOfEditTask == 1) {
            FrameLayout frameToDeleteButton = findViewById(R.id.frameToButtonDeleteTask);
            Button buttonToDeleteTask = new Button(this);
            buttonToDeleteTask.setText("Delete");
            buttonToDeleteTask.setTextSize(20);
            buttonToDeleteTask.setBackgroundColor(getResources().getColor(R.color.purple_200));
            frameToDeleteButton.addView(buttonToDeleteTask);

            buttonToDeleteTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Чистка SharedPreferences из-за особенностей OnStop()
                    SharedPreferences sp = getSharedPreferences("mapToSave", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.clear();
                    editor.apply();
                    editor.commit();

                    SaveTask.allTask.get(mainObject.idDate).remove(id);
                    Intent intent = new Intent(EditTask.this, MainActivity.class);
                    startActivity(intent);
                }
            });

            EditText timeHoursEditText = findViewById(R.id.editTimeForTaskHours);
            EditText timeMinutesEditText = findViewById(R.id.editTimeForTaskMinutes);
            EditText descriptionEditTask = findViewById(R.id.editDescriptionText);
            EditText nameEditTask = findViewById(R.id.editNameOfTask);

            timeHoursEditText.setText(SaveTask.allTask.get(mainObject.idDate).get(id).timeInHours);
            timeMinutesEditText.setText(SaveTask.allTask.get(mainObject.idDate).get(id).timeInMinutes);
            descriptionEditTask.setText(SaveTask.allTask.get(mainObject.idDate).get(id).descriptionOfTask);
            nameEditTask.setText(SaveTask.allTask.get(mainObject.idDate).get(id).nameOfTask);

            Button buttonAdd = findViewById(R.id.buttonAdd);
            buttonAdd.setText("Изменить");
        }
    }

    public void OnClickButtonAdd(View view) {
        if (flagOfEditTask == 0) {
            onClickAddNewTask();
        }else{
            onClickEditTask();
        }
    }
    public void onClickAddNewTask(){
        String timeHours = "12";
        String timeMinutes = "00";
        String name = "Дело", description = "Описание дела";

        MainActivity mainObject = new MainActivity();

        EditText timeHoursEditText = findViewById(R.id.editTimeForTaskHours);
        EditText timeMinutesEditText = findViewById(R.id.editTimeForTaskMinutes);
        EditText descriptionEditTask = findViewById(R.id.editDescriptionText);
        EditText nameEditTask = findViewById(R.id.editNameOfTask);

        //Проверка на пустоту
        if(!timeHoursEditText.getText().toString().equals("") ) {
            timeHours = timeHoursEditText.getText().toString();
        }
        if(!timeMinutesEditText.getText().toString().equals("") ) {
            timeMinutes = timeMinutesEditText.getText().toString();
        }
        if(!descriptionEditTask.getText().toString().equals("") ){
            description = descriptionEditTask.getText().toString();
        }
        if(!nameEditTask.getText().toString().equals("") ) {
            name = nameEditTask.getText().toString();
        }

        //Проверка на корректность установленного времени
        if((Integer.parseInt(timeHours.trim()) <= 24 && Integer.parseInt(timeMinutes.trim()) <=60) && (Integer.parseInt(timeHours.trim()) >= 0 && Integer.parseInt(timeMinutes.trim()) >= 0)){
            if(Integer.parseInt(timeHours.trim()) == 24 && Integer.parseInt(timeHours.trim()) > 0) {

                timeHoursEditText.setText("12");
                timeMinutesEditText.setText("00");
            }else{
                timeHours = timeHoursEditText.getText().toString();
                timeMinutes = timeMinutesEditText.getText().toString();
                mainObject.setDateId.SetTaskInfo(timeHours, timeMinutes, name, description);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        }else{
            timeHoursEditText.setText("12");
            timeMinutesEditText.setText("00");
        }
        //Чистка SharedPreferences из-за особенностей OnStop()
        SharedPreferences sp = getSharedPreferences("mapToSave", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
        editor.commit();

    }
    public void onClickEditTask(){
        String timeHours = "12";
        String timeMinutes = "00";
        String name = "Дело", description = "Описание дела";

        MainActivity mainObject = new MainActivity();

        EditText timeHoursEditText = findViewById(R.id.editTimeForTaskHours);
        EditText timeMinutesEditText = findViewById(R.id.editTimeForTaskMinutes);
        EditText descriptionEditTask = findViewById(R.id.editDescriptionText);
        EditText nameEditTask = findViewById(R.id.editNameOfTask);

        timeHours = timeHoursEditText.getText().toString();
        timeMinutes = timeMinutesEditText.getText().toString();
        name = nameEditTask.getText().toString();
        description = descriptionEditTask.getText().toString();

        mainObject.setDateId.EditTaskInfo(timeHours, timeMinutes, name, description, mainObject.idDate, id);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        SharedPreferences sp = getSharedPreferences("mapToSave", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
        editor.commit();
    }

    public void BackButton(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}