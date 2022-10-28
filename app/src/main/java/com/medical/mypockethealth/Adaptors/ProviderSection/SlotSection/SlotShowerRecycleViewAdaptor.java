package com.medical.mypockethealth.Adaptors.ProviderSection.SlotSection;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.Classes.SlotsSection.SlotInformation;
import com.medical.mypockethealth.R;

import java.util.List;

public class SlotShowerRecycleViewAdaptor extends RecyclerView.Adapter<SlotShowerRecycleViewAdaptor.InnerSlotShowerRecycleViewAdaptor> {

    private static final String TAG = "SlotShowerRecycleViewAd";

    private List<SlotInformation> slotInformation;
    private final callBackFromSlotShowerRecycleViewAdaptor callBackFromSlotShowerRecycleViewAdaptor;
    private final Context context;

    public SlotShowerRecycleViewAdaptor(List<SlotInformation> slotInformation,
                                        SlotShowerRecycleViewAdaptor.callBackFromSlotShowerRecycleViewAdaptor callBackFromSlotShowerRecycleViewAdaptor, Context context) {
        this.slotInformation = slotInformation;
        this.callBackFromSlotShowerRecycleViewAdaptor = callBackFromSlotShowerRecycleViewAdaptor;
        this.context = context;
    }

    public interface callBackFromSlotShowerRecycleViewAdaptor
    {

        void delete(SlotInformation slotInformation);

    }


    public void loadData(List<SlotInformation> slotInformation)
    {
        this.slotInformation=slotInformation;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public InnerSlotShowerRecycleViewAdaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InnerSlotShowerRecycleViewAdaptor(LayoutInflater.from(parent.getContext()).inflate(R.layout.slot_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InnerSlotShowerRecycleViewAdaptor holder, int position) {

       holder.setDefault();

        holder.date.setText(setFilteredDate(slotInformation.get(position).getDate()));


       if(slotInformation.get(position).getMorningVisibilityStatus().equals(ResponseInformation.success_response_type))
       {

           holder.morning_box.setVisibility(View.VISIBLE);

           String timing=slotInformation.get(position).getMorningStartTime()+" - "+slotInformation.get(position).getMorningEndTime();

           holder.morning_slot.setText(timing);

           String division=slotInformation.get(position).getMorningPerSlot()+" per slot timing";

           holder.morning_slot_division.setText(division);
       }

        if(slotInformation.get(position).getAfternoonVisibilityStatus().equals(ResponseInformation.success_response_type))
        {

            holder.afternoon_box.setVisibility(View.VISIBLE);

            String timing=slotInformation.get(position).getAfternoonStartTime()+" - "+slotInformation.get(position).getAfternoonEndTime();

            holder.afternoon_slot.setText(timing);

            String division=slotInformation.get(position).getAfternoonPerlSot()+" per slot timing";

            holder.afternoon_slot_division.setText(division);
        }

        if(slotInformation.get(position).getEveningVisibilityStatus().equals(ResponseInformation.success_response_type))
        {

            holder.evening_box.setVisibility(View.VISIBLE);

            String timing=slotInformation.get(position).getEveningStartTime()+" - "+slotInformation.get(position).getEveningEndTime();

            holder.evening_slot.setText(timing);

            String division=slotInformation.get(position).getEveningPerSlot()+" per slot timing";

            holder.evening_slot_division.setText(division);
        }



        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder=new AlertDialog.Builder(context)
                        .setMessage("Are you sure you want to remove this slot").setNegativeButton("CANCEL", (dialogInterface, i) -> {

                        }).setPositiveButton("PROCEED", (dialogInterface, i) -> {


                            if(callBackFromSlotShowerRecycleViewAdaptor!=null)
                            {
                                callBackFromSlotShowerRecycleViewAdaptor.delete(slotInformation.get(position));
                            }

                        });
                builder.setCancelable(false);
                builder.show();

            }
        });
        
        
    }

    private String setFilteredDate(String date) {

        String[] a=date.split("");

        StringBuilder stringBuilder=new StringBuilder();

        int end_position=0;

        for (int i = 0; i < a.length; i++) {

            if(a[i].equals("-"))
            {

                end_position=i;

                break;
            }
            else
            {
                stringBuilder.append(a[i]);
            }

        }

        String year=stringBuilder.toString();

        stringBuilder.setLength(0);

        for (int i = end_position+1; i <a.length ; i++) {

            if(a[i].equals("-"))
            {

                end_position=i;

                break;
            }
            else
            {
                stringBuilder.append(a[i]);
            }
        }


        String month=stringBuilder.toString();

        stringBuilder.setLength(0);

        for (int i = end_position+1; i <a.length ; i++) {

            if(a[i].equals("-"))
            {

                end_position=i;

                break;
            }
            else
            {
                stringBuilder.append(a[i]);
            }
        }

        String day=stringBuilder.toString();

        stringBuilder.setLength(0);

        String result = "";

        try {
            result = context.getResources().getStringArray(R.array.month_names)[Integer.parseInt(month)-1];
        } catch (ArrayIndexOutOfBoundsException e) {
            result = Integer.toString(Integer.parseInt(month));
        }


        return day + ", " + result + " " + year;
    }


    @Override
    public int getItemCount() {
        return (slotInformation!=null && slotInformation.size()!=0 ? slotInformation.size():0);
    }

    static class InnerSlotShowerRecycleViewAdaptor extends RecyclerView.ViewHolder
    {

        LinearLayout morning_box,afternoon_box,evening_box;
        ImageView delete;
        TextView morning_slot,morning_slot_division,afternoon_slot,afternoon_slot_division,evening_slot,evening_slot_division,date;

        public InnerSlotShowerRecycleViewAdaptor(@NonNull View itemView) {
            super(itemView);

            morning_box=itemView.findViewById(R.id.morning_box);
            afternoon_box=itemView.findViewById(R.id.afternoon_box);
            evening_box=itemView.findViewById(R.id.evening_box);
            morning_slot=itemView.findViewById(R.id.morning_slot);
            morning_slot_division=itemView.findViewById(R.id.morning_slot_division);
            afternoon_slot=itemView.findViewById(R.id.afternoon_slot);
            afternoon_slot_division=itemView.findViewById(R.id.afternoon_slot_division);
            evening_slot=itemView.findViewById(R.id.evening_slot);
            evening_slot_division=itemView.findViewById(R.id.evening_slot_division);
            date=itemView.findViewById(R.id.date);
            delete=itemView.findViewById(R.id.delete);

        }

        public void setDefault()
        {
            morning_box.setVisibility(View.GONE);
            afternoon_box.setVisibility(View.GONE);
            evening_box.setVisibility(View.GONE);
        }
    }
}
