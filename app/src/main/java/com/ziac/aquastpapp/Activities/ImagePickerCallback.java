package com.ziac.aquastpapp.Activities;

import android.content.Context;
import android.net.Uri;

import java.util.ArrayList;

import Models.IncidentsClass;

public interface ImagePickerCallback {
    void onImagePicked(ArrayList<IncidentsClass> incidentS, Context context);
}
