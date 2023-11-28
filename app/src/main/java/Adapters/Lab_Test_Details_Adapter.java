package Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import Models.LabTestClass;

public class Lab_Test_Details_Adapter extends RecyclerView.Adapter<Lab_Test_Details_Adapter.Viewholder> {
    Context context;
    private List<LabTestClass> labTestClasses;

    public Lab_Test_Details_Adapter(Context context, List<LabTestClass> labTestClasses) {
        this.context = context;
        this.labTestClasses = labTestClasses;
    }

    @NonNull
    @Override
    public Lab_Test_Details_Adapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull Lab_Test_Details_Adapter.Viewholder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        public Viewholder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
