package Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.ziac.aquastpapp.Activities.BlowersDailyLogActivity;
import com.ziac.aquastpapp.Activities.FiltersDailyLogActivity;
import com.ziac.aquastpapp.Activities.Global;
import com.ziac.aquastpapp.Activities.HandoverRemarksActivity;
import com.ziac.aquastpapp.Activities.MeterDailyLogActivity;
import com.ziac.aquastpapp.Activities.PumpMotorDailyLogActivity;
import com.ziac.aquastpapp.Activities.SensorsDailyLogActivity;
import com.ziac.aquastpapp.R;

import java.util.ArrayList;
import java.util.List;

import Models.DashboardItem;

public class BlankFragment extends Fragment {

    private RecyclerView dashboardRecyclerView;
    private DashboardAdapter dashboardAdapter;
    private List<DashboardItem> dashboardItems;
    String layouthandover_remark;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_blank, container, false);

        setupRecyclerView();
        loadDashboardData();
        dashboardRecyclerView = view.findViewById(R.id.dashboardRecyclerView);

        String usertype = Global.sharedPreferences.getString("user_type", "");

        return  view;
    }



    private void loadDashboardData() {
        dashboardItems = new ArrayList<>();

        // Add dashboard items
        dashboardItems.add(new DashboardItem(
                "Pumps / Motors",
                "Monitor and control system pumps",
                R.drawable.motortwo,
                R.drawable.gradient_orange
        ));

        dashboardItems.add(new DashboardItem(
                "Blowers",
                "Manage air flow equipment",
                R.drawable.fan,
                R.drawable.gradient_blue
        ));

        dashboardItems.add(new DashboardItem(
                "Filters",
                "Track filter status and maintenance",
                R.drawable.filter,
                R.drawable.gradient_pink
        ));

        dashboardItems.add(new DashboardItem(
                "Meters",
                "View and record measurement data",
                R.drawable.speedometer,
                R.drawable.gradient_green
        ));

        dashboardItems.add(new DashboardItem(
                "Sensors",
                "Monitor environmental parameters",
                R.drawable.sensor,
                R.drawable.gradient_cyan
        ));

        dashboardItems.add(new DashboardItem(
                "Handover Remarks",
                "View and add shift comments",
                R.drawable.handover,
                R.drawable.gradient_yellow
        ));

        // Set adapter
        dashboardAdapter = new DashboardAdapter(requireActivity(), dashboardItems, (item, position) -> {
            // Handle item click
            handleItemClick(item, position);
        });

        dashboardRecyclerView.setAdapter(dashboardAdapter);
    }

    private void handleItemClick(DashboardItem item, int position) {
        Intent intent;

        switch (position) {
            case 0:
                // Navigate to Pumps/Motors Activity
                intent = new Intent(requireActivity(), PumpMotorDailyLogActivity.class);
                startActivity(intent);
                break;

            case 1:
                // Navigate to Blowers Activity
                intent = new Intent(requireActivity(), BlowersDailyLogActivity.class);
                startActivity(intent);
                break;

            case 2:
                // Navigate to Filters Activity
                intent = new Intent(requireActivity(), FiltersDailyLogActivity.class);
                startActivity(intent);
                break;

            case 3:
                // Navigate to Meters Activity
                intent = new Intent(requireActivity(), MeterDailyLogActivity.class);
                startActivity(intent);
                break;

            case 4:
                // Navigate to Sensors Activity
                intent = new Intent(requireActivity(), SensorsDailyLogActivity.class);
                startActivity(intent);
                break;

            case 5:
                // Navigate to Handover Remarks Activity
                intent = new Intent(requireActivity(), HandoverRemarksActivity.class);
                startActivity(intent);
                break;
        }

        // Add transition animation
       // overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

    private void setupRecyclerView() {
        dashboardRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        dashboardRecyclerView.setHasFixedSize(true);
    }


    public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.DashboardViewHolder> {

        private Context context;
        private List<DashboardItem> dashboardItems;
        private OnItemClickListener listener;

        // Interface for click handling
        public interface OnItemClickListener {
            void onItemClick(DashboardItem item, int position);
        }

        public DashboardAdapter(Context context, List<DashboardItem> dashboardItems, OnItemClickListener listener) {
            this.context = context;
            this.dashboardItems = dashboardItems;
            this.listener = listener;
        }

        @NonNull
        @Override
        public DashboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_dashboard_card, parent, false);
            return new DashboardViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DashboardViewHolder holder, int position) {
            DashboardItem item = dashboardItems.get(position);

            holder.itemTitle.setText(item.getTitle());
            holder.itemDescription.setText(item.getDescription());
            holder.itemIcon.setImageResource(item.getIconResId());

            // Set gradient background for icon container
            holder.iconBackground.setBackgroundResource(item.getGradientResId());

            // Click listener
            holder.dashboardCard.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(item, position);
                }
            });

            // Add subtle animation
            holder.itemView.setAlpha(0f);
            holder.itemView.animate()
                    .alpha(1f)
                    .setDuration(300)
                    .setStartDelay(position * 50)
                    .start();
        }

        @Override
        public int getItemCount() {
            return dashboardItems.size();
        }

        public static class DashboardViewHolder extends RecyclerView.ViewHolder {
            MaterialCardView dashboardCard;
            ImageView itemIcon;
            TextView itemTitle, itemDescription;
            View iconBackground;

            public DashboardViewHolder(@NonNull View itemView) {
                super(itemView);
                dashboardCard = itemView.findViewById(R.id.dashboardCard);
                itemIcon = itemView.findViewById(R.id.itemIcon);
                itemTitle = itemView.findViewById(R.id.itemTitle);
                itemDescription = itemView.findViewById(R.id.itemDescription);
                iconBackground = itemView.findViewById(R.id.iconBackground);
            }
        }
    }

}