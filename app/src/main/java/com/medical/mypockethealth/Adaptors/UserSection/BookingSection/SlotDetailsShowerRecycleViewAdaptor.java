package com.medical.mypockethealth.Adaptors.UserSection.BookingSection;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.mypockethealth.Classes.SlotsSection.ClientSection.MergeSlotInformation;
import com.medical.mypockethealth.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SlotDetailsShowerRecycleViewAdaptor extends RecyclerView.Adapter<SlotDetailsShowerRecycleViewAdaptor.InnerSlotDetailsShowerRecycleViewAdaptor> {

private static final String TAG = "SlotShowerRecycleViewAd";

private List<MergeSlotInformation> slotInformation;

private callBackFromSlotDetailsShowerRecycleViewAdaptor callBackFromSlotDetailsShowerRecycleViewAdaptor;
private int row_index;
private final Context context;


    public SlotDetailsShowerRecycleViewAdaptor(List<MergeSlotInformation> slotInformation,
                                               SlotDetailsShowerRecycleViewAdaptor.callBackFromSlotDetailsShowerRecycleViewAdaptor callBackFromSlotDetailsShowerRecycleViewAdaptor,
                                               Context context) {
        this.slotInformation = slotInformation;
        this.callBackFromSlotDetailsShowerRecycleViewAdaptor = callBackFromSlotDetailsShowerRecycleViewAdaptor;
        this.context=context;
    }

    public interface callBackFromSlotDetailsShowerRecycleViewAdaptor
{

    void select(MergeSlotInformation slotInformation);

}


    public void loadData(List<MergeSlotInformation> mergeSlotInformation)
    {
        this.slotInformation=mergeSlotInformation;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SlotDetailsShowerRecycleViewAdaptor.InnerSlotDetailsShowerRecycleViewAdaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SlotDetailsShowerRecycleViewAdaptor.
                InnerSlotDetailsShowerRecycleViewAdaptor(LayoutInflater.from(parent.getContext()).inflate(R.layout.inner_slot_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SlotDetailsShowerRecycleViewAdaptor.InnerSlotDetailsShowerRecycleViewAdaptor holder, int position) {

        holder.slot_time.setText(slotInformation.get(position).getSlotTime());


        holder.date_holder.setEnabled(!slotInformation.get(position).isState());

        holder.date_holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                row_index=position;

                notifyDataSetChanged();


            }
        });

        if(row_index==position){

            holder.date_holder.setBackgroundResource(R.drawable.slot_background_layout);

            holder.slot_time.setTextColor(context.getResources().getColor(R.color.white));

            if(holder.date_holder.isEnabled())
            {
                if(callBackFromSlotDetailsShowerRecycleViewAdaptor!=null)
                {

                    callBackFromSlotDetailsShowerRecycleViewAdaptor.select(slotInformation.get(position));
                }
            }


        }
        else
        {

            holder.slot_time.setTextColor(context.getResources().getColor(R.color.card_background_color));

            holder.date_holder.setBackgroundResource(R.drawable.slot_unselect_layout);
        }


        if(!holder.date_holder.isEnabled())
        {
            holder.slot_time.setTextColor(context.getResources().getColor(R.color.gray_one));

            holder.date_holder.setBackgroundResource(R.drawable.fad_layout);
        }

    }


    @Override
    public int getItemCount() {
        return (slotInformation!=null && slotInformation.size()!=0 ? slotInformation.size():0);
    }

static class InnerSlotDetailsShowerRecycleViewAdaptor extends RecyclerView.ViewHolder
{
    RelativeLayout date_holder;
    TextView slot_time;

    public InnerSlotDetailsShowerRecycleViewAdaptor(@NonNull View itemView) {
        super(itemView);

     date_holder=itemView.findViewById(R.id.date_holder);
     slot_time=itemView.findViewById(R.id.slot_time);

    }

    
}
}

