package com.medical.mypockethealth.Adaptors.UserSection.BookingSection;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.mypockethealth.Classes.DateInformation.DateInformation;
import com.medical.mypockethealth.R;

import java.util.List;

public class DateShowerRecycleViewAdaptor extends RecyclerView.Adapter<DateShowerRecycleViewAdaptor.InnerDateShowerRecycleViewAdaptor> {

  private List<DateInformation> dateInformation;
  private final callBackFromDateShowerRecycleViewAdaptor callBackFromDateShowerRecycleViewAdaptor;
  private int row_index;
  private final Context context;


    public DateShowerRecycleViewAdaptor(List<DateInformation> dateInformation,
                                        DateShowerRecycleViewAdaptor.callBackFromDateShowerRecycleViewAdaptor callBackFromDateShowerRecycleViewAdaptor,Context context) {
        this.dateInformation = dateInformation;
        this.callBackFromDateShowerRecycleViewAdaptor = callBackFromDateShowerRecycleViewAdaptor;
        this.context=context;
    }

    public interface callBackFromDateShowerRecycleViewAdaptor
    {

        void selectedDate(DateInformation dateInformation);

    }



    @NonNull
    @Override
    public InnerDateShowerRecycleViewAdaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InnerDateShowerRecycleViewAdaptor(LayoutInflater.from(parent.getContext()).inflate(R.layout.slot_box_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InnerDateShowerRecycleViewAdaptor holder,@SuppressLint("RecyclerView") int position) {


        holder.day.setText(dateInformation.get(position).getDay());
        holder.date.setText(dateInformation.get(position).getDate());


        holder.date_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                row_index=position;

                notifyDataSetChanged();


            }
        });


        if(row_index==position){


            holder.date_select.setBackgroundResource(R.drawable.slot_background_layout);

            holder.day.setTextColor(context.getResources().getColor(R.color.white));
            holder.date.setTextColor(context.getResources().getColor(R.color.white));

            if(callBackFromDateShowerRecycleViewAdaptor!=null)
            {

                callBackFromDateShowerRecycleViewAdaptor.selectedDate(dateInformation.get(position));
            }

        }
        else
        {

            holder.day.setTextColor(context.getResources().getColor(R.color.card_background_color));
            holder.date.setTextColor(context.getResources().getColor(R.color.card_background_color));

            holder.date_select.setBackgroundResource(0);
        }


    }

    public void loadData(List<DateInformation> dateInformation)
    {
        this.dateInformation=dateInformation;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (dateInformation!=null && dateInformation.size()!=0 ? dateInformation.size():0);
    }

    static class InnerDateShowerRecycleViewAdaptor extends RecyclerView.ViewHolder
    {

        TextView day,date;
        LinearLayout date_select;

        public InnerDateShowerRecycleViewAdaptor(@NonNull View itemView) {
            super(itemView);

            day=itemView.findViewById(R.id.day);
            date=itemView.findViewById(R.id.date);
            date_select=itemView.findViewById(R.id.date_select);
        }
    }
}
