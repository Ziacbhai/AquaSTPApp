package Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ziac.aquastpapp.Activities.Global;
import com.ziac.aquastpapp.Activities.RepairBreakUpActivity;
import com.ziac.aquastpapp.Activities.RepairTwoImageListActivity;
import com.ziac.aquastpapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Models.RepairClass2;

public class Repair_details_Adapter extends RecyclerView.Adapter<Repair_details_Adapter.Viewholder> {

    private List<RepairClass2> repairClass2;
    Context context;

    public Repair_details_Adapter(List<RepairClass2> repairClass2, Context context) {
        this.repairClass2 = repairClass2;
        this.context = context;
    }

//    public void setasChecked() {
//
//        RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
//        String url = Global.urldeletevehicle ;
//        url=url+"vehmas_code="+vehmascode;
//        //progressDialog.show();
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                //JSONObject respObj = new JSONObject(response);
//                try {
//                    String msg = response.getString("msg");
//                    boolean isSuccess = response.getBoolean("isSuccess");
//                } catch (JSONException e) {
//                    throw new RuntimeException(e);
//                }
//
////                StockFragment stockFragment = new StockFragment();
////                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
////                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
////                fragmentTransaction.replace(R.id.frame_layout, stockFragment);
////                fragmentTransaction.addToBackStack(null);
////                fragmentTransaction.commit();
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                //Global.customtoast(context.getApplicationContext(), getLayoutInflater(),"Failed to get my stock .." + error.getMessage());
//            }
//        })
//        {
//            @Override
//            public Map<String, String> getHeaders() {
//                Map<String, String> headers = new HashMap<String, String>();
//                String accesstoken = Global.sharedPreferences.getString("access_token", "");
//                headers.put("Authorization", "Bearer " + accesstoken);
//                return headers;
//            }
//
//
//        };
//        queue.add(jsonObjectRequest);
//
//    }

    @NonNull
    @Override
    public Repair_details_Adapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.repair_details_design, parent, false);
        Viewholder viewholder = new Viewholder(view);
        Animation animation = AnimationUtils.loadAnimation(context,R.anim.fade_in);
        viewholder.itemView.startAnimation(animation);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Repair_details_Adapter.Viewholder holder, @SuppressLint("RecyclerView") int position) {

        holder.Eq_Name.setText(repairClass2.get(position).getD_Equipment_Name());
        holder.Eq_number.setText(repairClass2.get(position).getD_Equipment_Number());
        //  holder.Repair_repaired_check.setText(repairsClassList.get(position).getD_Repaired());
        holder.Remark.setText(repairClass2.get(position).getD_Remark());
        holder.Amount.setText(repairClass2.get(position).getD_Amount());

    /*    String repairedStatus = repairsClassList.get(position).getD_Repaired();
        boolean isRepaired = Boolean.parseBoolean(repairedStatus);
        holder.Repair_repaired_check.setChecked(isRepaired);*/

        String repairedStatus = repairClass2.get(position).getD_Repaired();

//        boolean isRepaired = Boolean.parseBoolean(repairedStatus);
//        holder.Repair_repaired_check.setChecked(isRepaired);
//        if (!isRepaired) {
//            holder.Repair_repaired_check.setClickable(true);
//            holder.Repair_repaired_check.setFocusable(true);
//            holder.Repair_repaired_check.setEnabled(true);
//        } else {
//            holder.Repair_repaired_check.setClickable(false);
//            holder.Repair_repaired_check.setFocusable(true);
//            holder.Repair_repaired_check.setEnabled(false);
//        }


        // Inside your Activity or Fragment
        Global.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isChecked = Global.sharedPreferences.getBoolean("checkboxState", false);

        holder.Repair_repaired_check.setChecked(isChecked);
        holder.Repair_repaired_check.setEnabled(!isChecked);

        holder.Repair_repaired_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.Repair_repaired_check.isChecked()) {
                    //Toast.makeText(context, "checked", Toast.LENGTH_SHORT).show();
                    holder.Repair_repaired_check.setEnabled(false);

                    // Save the state in SharedPreferences
                    Global.editor = Global.sharedPreferences.edit();
                    Global.editor.putBoolean("checkboxState", true);
                    Global.editor.commit();


                }
            }
        });



//        holder.Repair_repaired_check.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (holder.Repair_repaired_check.isChecked()) {
//                    Toast.makeText(context, "checked", Toast.LENGTH_SHORT).show();
//                    holder.Repair_repaired_check.setEnabled(false);
//                    // setasChecked();  // Uncomment this line if it's a custom method you need to call
//                }
//            }
//        });

        holder.RImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.repairClass2 = repairClass2.get(position);
                Intent intent = new Intent(context, RepairTwoImageListActivity.class);
                context.startActivity(intent);
            }
        });
        holder.RBreakup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.repairClass2 = repairClass2.get(position);
                Intent intent = new Intent(context, RepairBreakUpActivity.class);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return repairClass2.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView Eq_Name, Eq_number, Repaired, Remark, Amount;

         CheckBox Repair_repaired_check;
        ImageView RImage, RBreakup;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            Eq_Name = itemView.findViewById(R.id.repair_equipment_name);
            Eq_number = itemView.findViewById(R.id.repair_equipment_id);
            Repair_repaired_check = itemView.findViewById(R.id.repair_repaired_check);
            Remark = itemView.findViewById(R.id.repair_remark);
            Amount = itemView.findViewById(R.id.repair_amount);
            RImage = itemView.findViewById(R.id.repair2_image);
            RBreakup = itemView.findViewById(R.id.repair_breakup);
        }
    }
}
