package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.ziac.aquastpapp.R;

import java.util.ArrayList;
import java.util.List;

import Models.StpModelClass;
import Models.zList;

public class LocationsAdapter extends RecyclerView.Adapter<LocationsAdapter.StpViewHolder> {

    private List<StpModelClass> stpModelClasses;
    Context context;

    public LocationsAdapter(ArrayList<zList> stpModelClasses, Context context) {

        this.context = context;
    }

    @NonNull
    @Override
    public LocationsAdapter.StpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.companydetails, parent , false);
        return new StpViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationsAdapter.StpViewHolder holder, int position) {
        holder.Stpcode.setText(stpModelClasses.get(position).getSuCode());
        holder.StpLocation.setText(stpModelClasses.get(position).getSTPName());

        holder.SelectCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return stpModelClasses.size();
    }

    public class StpViewHolder extends RecyclerView.ViewHolder {

        TextView Stpcode,StpLocation;
        AppCompatButton SelectCompany;
        public StpViewHolder(@NonNull View itemView) {
            super(itemView);
            Stpcode = itemView.findViewById(R.id.stpcode);
            StpLocation = itemView.findViewById(R.id.sitename);
            SelectCompany = itemView.findViewById(R.id.selectcompany);
        }
    }
}
