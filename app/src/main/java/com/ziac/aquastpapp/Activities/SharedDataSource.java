package com.ziac.aquastpapp.Activities;

import java.util.ArrayList;
import java.util.List;

import Models.PumpMotor_Blower_DailyLogClass;

public class SharedDataSource {
    private static SharedDataSource instance;
    private List<PumpMotor_Blower_DailyLogClass> data;

    private SharedDataSource() {
        data = new ArrayList<>();
    }

    public static SharedDataSource getInstance() {
        if (instance == null) {
            instance = new SharedDataSource();
        }
        return instance;
    }

    public List<PumpMotor_Blower_DailyLogClass> getData() {
        return data;
    }

    public void updateData(List<PumpMotor_Blower_DailyLogClass> newData) {
        data.clear();
        data.addAll(newData);
    }
}

