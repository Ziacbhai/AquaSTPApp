package Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.ziac.aquastpapp.Activities.Global;
import com.ziac.aquastpapp.Activities.MainActivity;
import com.ziac.aquastpapp.R;

import java.util.List;

import Models.StpModelClass;

public class SiteLocationAdapter extends RecyclerView.Adapter<SiteLocationAdapter.StpViewHolder> {

    private List<StpModelClass> stpModelClassList;
    Context context;

  /*  public void setFilteredList(List<StpModelClass> filteredList) {
        this.stpModelClassList = filteredList;
        notifyDataSetChanged();

    }*/

    public SiteLocationAdapter(List<StpModelClass> stpModelClassList, Context context) {
        this.stpModelClassList = stpModelClassList;
        this.context = context;
    }

    @NonNull
    @Override
    public SiteLocationAdapter.StpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stp2, parent, false);
        return new StpViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SiteLocationAdapter.StpViewHolder holder, @SuppressLint("RecyclerView") int position) {
       // holder.Sucode.setText(String.valueOf(stpModelClassList.get(position).getSucode()));
        holder.StpName.setText(stpModelClassList.get(position).getStpname());
        holder.SiteName.setText(stpModelClassList.get(position).getSitename());

        holder.Selectcompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                Global.editor = Global.sharedPreferences.edit();
                Global.editor.putInt("su_code", Global.StpList.get(position).getSucode());
                Global.editor.putString("com_code", Global.StpList.get(position).getComcode());
                Global.editor.putString("user_code", Global.StpList.get(position).getUsercode());
                Global.editor.putString("person_name", Global.StpList.get(position).getPersonname());
               // Global.editor.putString("username", Global.StpList.get(position).getUsername());
                Global.editor.putString("sstp1_code", Global.StpList.get(position).getSstp1code());
                Global.editor.putString("stp_name", Global.StpList.get(position).getStpname());
                Global.editor.putString("site_code", Global.StpList.get(position).getSitecode());
                Global.editor.putString("site_name", Global.StpList.get(position).getSitename());
                Global.editor.putString("stp_active", Global.StpList.get(position).getStpactive());
                Global.editor.putString("site_address", Global.StpList.get(position).getSite_address());
                Global.editor.putString("process_name", Global.StpList.get(position).getProcess__type());
                Global.editor.putString("stp_capacity", Global.StpList.get(position).getStp_capacity());
                Global.editor.commit();

                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return stpModelClassList.size();
    }


    public class StpViewHolder extends RecyclerView.ViewHolder {

        TextView Sucode, SiteName, StpName;
        AppCompatButton Selectcompany;

        public StpViewHolder(@NonNull View itemView) {
            super(itemView);
           // Sucode = itemView.findViewById(R.id.sucode);
            StpName = itemView.findViewById(R.id.stpname);
            SiteName = itemView.findViewById(R.id.sitename);
            Selectcompany = itemView.findViewById(R.id.selectcompany);


        }
    }
}
