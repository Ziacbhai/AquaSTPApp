package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ziac.aquastpapp.R;

import org.w3c.dom.Text;

import java.util.List;
import Models.FiltersClass;
//import Models.PumpMotorBlower_LogClass;

public class FiltersDailyLogAdapter extends RecyclerView.Adapter<FiltersDailyLogAdapter.Viewholder> {

    Context context;
    private List<FiltersClass> filtersClassList;

    public FiltersDailyLogAdapter(Context context, List<FiltersClass> filtersClassList) {
        this.context = context;
        this.filtersClassList = filtersClassList;
    }

    @NonNull
    @Override
    public FiltersDailyLogAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_daily_log_details,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FiltersDailyLogAdapter.Viewholder holder, int position) {
        holder.Filter_equip_name.setText(filtersClassList.get(position).getEquip_name());
        holder.Filter_reading.setText(filtersClassList.get(position).getReading_time());

        /*holder.Filter_image.setText(sensorsModelClassList.get(position).getEquip_name());*/
       /* holder.Filter_image_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
    }

    @Override
    public int getItemCount() {
        return filtersClassList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView Filter_equip_name,Filter_reading;
        ImageView Filter_image,Filter_image_upload;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            Filter_equip_name = itemView.findViewById(R.id.filter_equip_name);
            //Filter_image = itemView.findViewById(R.id.filter_image);
            Filter_reading = itemView.findViewById(R.id.filter_reading);
           // Filter_image_upload = itemView.findViewById(R.id.filter_image_upload);

        }
    }
}
