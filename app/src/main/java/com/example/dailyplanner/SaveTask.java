package com.example.dailyplanner;

import android.os.Parcel;
import android.os.Parcelable;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//Класс для хранения данных
public class SaveTask{
    public static String id;
    public static Map<String, List<StructTask>> allTask = new HashMap<>();
    public void setIdOfTask(int year, int month, int day){
        id = year + "-" + month + "-" + day;
    }
    public void setIdOfTaskInString(String xid){
        id = xid;
    }

    public void SetTaskInfo(String xtimeInHours, String xtimeInMinutes, String xnameOfTask, String xdescriptionOfTask) {

        StructTask newTask = new StructTask(xtimeInHours, xtimeInMinutes, xnameOfTask, xdescriptionOfTask);

        if(allTask.containsKey(id)){
            allTask.get(id).add(newTask);
        }
        else{
            List<StructTask> DateTaskInfo = new ArrayList<>();
            DateTaskInfo.add(newTask);
            allTask.put(id, DateTaskInfo);
        }
    }
    public void EditTaskInfo(String xtimeInHours, String xtimeInMinutes, String xnameOfTask, String xdescriptionOfTask, String idAllTask, int idTask){
        allTask.get(idAllTask).get(idTask).nameOfTask = xnameOfTask;
        allTask.get(idAllTask).get(idTask).descriptionOfTask = xdescriptionOfTask;
        allTask.get(idAllTask).get(idTask).timeInHours = xtimeInHours;
        allTask.get(idAllTask).get(idTask).timeInMinutes = xtimeInMinutes;
    }
}
//Структура для хранения данных задачи
class StructTask{
    public String nameOfTask, descriptionOfTask, timeInHours, timeInMinutes;
    public StructTask(String xtimeInHours, String xtimeInMinutes, String xnameOfTask, String xdescriptionOfTask){
        timeInHours = xtimeInHours;
        timeInMinutes = xtimeInMinutes;
        nameOfTask = xnameOfTask;
        descriptionOfTask = xdescriptionOfTask;
    }
    public String getTimeInString(){
        return timeInHours + " : " + timeInMinutes + " - " + (Integer.parseInt(timeInHours.trim())+1) + " : " + timeInMinutes;
    }
}

