package Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.ziac.aquastpapp.R;

import java.util.List;

import Models.StpModelClass;

public class SiteLocationAdapter extends RecyclerView.Adapter<SiteLocationAdapter.StpViewHolder> {

    private List<StpModelClass> stpModelClasses;
    Context context;

    public SiteLocationAdapter(List<StpModelClass> stpModelClasses, Context context) {
        this.stpModelClasses = stpModelClasses;
        this.context = context;
    }

    @NonNull
    @Override
    public SiteLocationAdapter.StpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.companydetails, parent , false);
        return new StpViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SiteLocationAdapter.StpViewHolder holder, int position) {
        holder.Sucode.setText(stpModelClasses.get(position).getSucode());
        holder.StpName.setText(stpModelClasses.get(position).getStpname());
        holder.SiteName.setText(stpModelClasses.get(position).getSitename());

        holder.Selectcompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          /*      SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
// Get a SharedPreferences.Editor to add data
                SharedPreferences.Editor editor = sharedPreferences.edit();
// Store values for your stpModelClass
                editor.putString("su_code", stpModelClasses.getSuCode());
                editor.putString("com_code", stpModelClasses.getComCode());
                editor.putString("user_code", stpModelClass.getUserCode());
                editor.putString("person_name", stpModelClass.getPersonName());
                editor.putString("username", stpModelClass.getUsername());
                editor.putString("sstp1_code", stpModelClass.getSstp1Code());
                editor.putString("stp_name", stpModelClass.getStpName());
                editor.putString("site_code", stpModelClass.getSiteCode());
                editor.putString("site_name", stpModelClass.getSiteName());
                editor.putString("stp_active", stpModelClass.getStpActive());

// Apply the changes
                editor.apply();*/
            }
        });


    }

    @Override
    public int getItemCount() {
        return stpModelClasses.size();
    }

    public class StpViewHolder extends RecyclerView.ViewHolder {

        TextView Sucode,SiteName,StpName ,Selectcompany;

        public StpViewHolder(@NonNull View itemView) {
            super(itemView);
            Sucode = itemView.findViewById(R.id.sucode);
            StpName = itemView.findViewById(R.id.stpname);
            SiteName = itemView.findViewById(R.id.sitename);
            Selectcompany = itemView.findViewById(R.id.selectcompany);


        }
    }
}
