package com.medical.mypockethealth.Adaptors.ProviderSection.AppointmentSection;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.mypockethealth.Classes.ClientInformation.ClientInformation;
import com.medical.mypockethealth.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookedAppointmentRecycleViewAdaptor extends RecyclerView.Adapter<BookedAppointmentRecycleViewAdaptor.InnerBookedAppointmentRecycleViewAdaptor> {

    private List<ClientInformation> clientInformation;
    private final Context context;


    public BookedAppointmentRecycleViewAdaptor(List<ClientInformation> clientInformation, Context context) {
        this.clientInformation = clientInformation;
        this.context = context;
    }

    @NonNull
    @Override
    public InnerBookedAppointmentRecycleViewAdaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InnerBookedAppointmentRecycleViewAdaptor(LayoutInflater
                .from(parent.getContext()).inflate(R.layout.patient_list_layout,parent,false));
    }


    public void loadData(List<ClientInformation> clientInformation)
    {
        this.clientInformation=clientInformation;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull InnerBookedAppointmentRecycleViewAdaptor holder, int position) {

        holder.name.setText(clientInformation.get(position).getPatientName());
        holder.age.setText(clientInformation.get(position).getPatientAge());
        holder.slot_time.setText(clientInformation.get(position).getSlotTime());
        holder.date.setText(setFilteredDate(clientInformation.get(position).getDate()));


        Picasso.with(context).load(Uri.parse(clientInformation.get(position).getImage())).error(R.drawable.profile).into(holder.profile_image);

    }

    @Override
    public int getItemCount() {
        return (clientInformation!=null && clientInformation.size()!=0 ? clientInformation.size():0);
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

    static class InnerBookedAppointmentRecycleViewAdaptor extends RecyclerView.ViewHolder
    {

        TextView name,age,slot_time,date;
        ImageView profile_image;

        public InnerBookedAppointmentRecycleViewAdaptor(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.name);
            age=itemView.findViewById(R.id.age);
            slot_time=itemView.findViewById(R.id.slot_time);
            profile_image=itemView.findViewById(R.id.profile_image);
            date=itemView.findViewById(R.id.date);
        }
    }
}
