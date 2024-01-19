package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ziac.aquastpapp.R;
import java.util.List;
import Models.FiltersClass;
import Models.PumpMotor_Blower_DailyLogClass;

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

    }

    @Override
    public int getItemCount() {
        return filtersClassList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        public Viewholder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
