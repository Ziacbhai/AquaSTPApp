package Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ziac.aquastpapp.Activities.Global;
import com.ziac.aquastpapp.Activities.LabTestDetailsActivity;
import com.ziac.aquastpapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Models.LabTestModel;


public class LabTestAdapter extends RecyclerView.Adapter<LabTestAdapter.Viewholder> {
    private List<LabTestModel> labTestModels;
    Context context;

    public LabTestAdapter(List<LabTestModel> labTestModels, Context context) {
        this.labTestModels = labTestModels;
        this.context = context;
    }

    @NonNull
    @Override
    public LabTestAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lab_test_design, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LabTestAdapter.Viewholder holder, int position) {

        holder.Refno.setText(labTestModels.get(position).getRefno());
        holder.CustomerRef.setText(labTestModels.get(position).getCustomerRef());
        holder.Sample_Received_By.setText(labTestModels.get(position).getSample_Received_By());
        holder.Sample_Particular.setText(labTestModels.get(position).getSample_Particular());

        holder.Lab_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.labTestModel1 = labTestModels.get(position);
                Intent i = new Intent(context, LabTestDetailsActivity.class);
                context.startActivity(i);
            }
        });

        String conNoString = labTestModels.get(position).getTRno();
        double conNo;
        try {
            conNo = Double.parseDouble(conNoString);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }
        String formattedConNo = removeTrailingZero(conNo);
        holder.TRno.setText(formattedConNo);

        String dateString = labTestModels.get(position).getLabDate();
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date;
        try {
            date = inputFormat.parse(dateString);
            String Date = outputFormat.format(date);
            holder.LabDate.setText(Date);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }

        String LabRefDate = labTestModels.get(position).getLabRefDate();

        SimpleDateFormat inputFormat1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputFormat1 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date1;
        try {
            date1 = inputFormat1.parse(LabRefDate);
            String Date2 = outputFormat1.format(date1);
            holder.LabRefDate.setText(Date2);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }


        String Start_Date = labTestModels.get(position).getTest_Start_Date();

        SimpleDateFormat inputFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputFormat2 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date3;
        try {
            date3 = inputFormat2.parse(Start_Date);
            String Date3 = outputFormat2.format(date3);
            holder.Test_Start_Date.setText(Date3);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }

        String Completion_Date = labTestModels.get(position).getTest_Completion_Date();

        SimpleDateFormat inputFormat_1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputFormat_1 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date_;
        try {
            date_ = inputFormat_1.parse(Completion_Date);
            String Date_ = outputFormat_1.format(date_);
            holder.Test_Completion_Date.setText(Date_);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }

        String Sample_Received_Date = labTestModels.get(position).getSample_Received_Date();

        SimpleDateFormat inputFormat_2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputFormat_2 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date2_;
        try {
            date2_ = inputFormat_2.parse(Sample_Received_Date);
            String Date_ = outputFormat_2.format(date2_);
            holder.Sample_Received_Date.setText(Date_);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }


    }

    private String removeTrailingZero(double value) {
        // Convert the double to a string
        String formattedValue = String.valueOf(value);

        // Remove trailing zero if it's a decimal number
        if (formattedValue.indexOf(".") > 0) {
            formattedValue = formattedValue.replaceAll("0*$", "").replaceAll("\\.$", "");
        }

        return formattedValue;
    }

    @Override
    public int getItemCount() {
        return labTestModels.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {
        private TextView TRno, Refno, LabDate, LabRefDate, CustomerRef, Sample_Received_Date,
                Test_Start_Date, Test_Completion_Date, Sample_Received_By, Sample_Particular;

        ImageView Lab_info;

        public Viewholder(@NonNull View itemView) {
            super(itemView);


            Lab_info = itemView.findViewById(R.id.lab_info);
            TRno = itemView.findViewById(R.id.tr_no);
            LabDate = itemView.findViewById(R.id.test_date);
            Refno = itemView.findViewById(R.id.lab_refno);
            LabRefDate = itemView.findViewById(R.id.lab_ref_date);
            CustomerRef = itemView.findViewById(R.id.lab_customerref);
            Sample_Received_Date = itemView.findViewById(R.id.sample_Received_date);
            Test_Start_Date = itemView.findViewById(R.id.test_Start_Date);
            Test_Completion_Date = itemView.findViewById(R.id.test_Completion_date);
            Sample_Received_By = itemView.findViewById(R.id.sample_ReceivedBy);
            Sample_Particular = itemView.findViewById(R.id.sampleParticulars);

        }
    }
}
