package com.medical.mypockethealth.Adaptors.ProviderSection.AppointmentSection.PatientDetailsSection;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.mypockethealth.Classes.ClientInformation.ClientInformation;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PatientDetailsRecycleViewAdaptor extends RecyclerView.Adapter<PatientDetailsRecycleViewAdaptor.InnerPatientDetailsRecycleViewAdaptor> {

    private List<ClientInformation> clientInformation;
    private final Context context;

    private final CallBackFromPatientDetailsRecycleViewAdaptor callBackFromPatientDetailsRecycleViewAdaptor;

    public PatientDetailsRecycleViewAdaptor(List<ClientInformation> clientInformation, Context context,
                                            CallBackFromPatientDetailsRecycleViewAdaptor callBackFromPatientDetailsRecycleViewAdaptor) {
        this.clientInformation = clientInformation;
        this.context = context;
        this.callBackFromPatientDetailsRecycleViewAdaptor = callBackFromPatientDetailsRecycleViewAdaptor;
    }

    public interface CallBackFromPatientDetailsRecycleViewAdaptor
    {

        void videoCall(ClientInformation clientInformation);
        void message(ClientInformation clientInformation);
        void changeBookingStatus(ClientInformation clientInformation);
    }

    @NonNull
    @Override
    public InnerPatientDetailsRecycleViewAdaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InnerPatientDetailsRecycleViewAdaptor(LayoutInflater.from(parent
                .getContext()).inflate(R.layout.patient_layout,parent,false));
    }

    public void loadData(List<ClientInformation> clientInformation)
    {
        this.clientInformation=clientInformation;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull InnerPatientDetailsRecycleViewAdaptor holder, int position) {

        holder.name.setText(clientInformation.get(position).getPatientName());
        holder.age.setText(clientInformation.get(position).getPatientAge());
        holder.slot_time.setText(clientInformation.get(position).getSlotTime());

        if(clientInformation.get(position).getEhrStatus().equals(ResponseInformation.success_response_type))
        {
            holder.previous_record.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.previous_record.setVisibility(View.GONE);
        }

        
        Picasso.with(context).load(Uri.parse(clientInformation.get(position).getProfileImage())).error(R.drawable.profile).into(holder.profile_image);


        holder.video_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(callBackFromPatientDetailsRecycleViewAdaptor!=null)
                {

                    callBackFromPatientDetailsRecycleViewAdaptor.videoCall(clientInformation.get(position));
                }

            }
        });

        holder.message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(callBackFromPatientDetailsRecycleViewAdaptor!=null)
                {

                    callBackFromPatientDetailsRecycleViewAdaptor.message(clientInformation.get(position));
                }

            }
        });

        holder.done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(callBackFromPatientDetailsRecycleViewAdaptor!=null)
                {

                    callBackFromPatientDetailsRecycleViewAdaptor.changeBookingStatus(clientInformation.get(position));
                }

            }
        });

    }

    @Override
    public int getItemCount() {

        return (clientInformation!=null && clientInformation.size()!=0 ? clientInformation.size():0);
    }

    static class InnerPatientDetailsRecycleViewAdaptor extends RecyclerView.ViewHolder
    {
        TextView name,age,slot_time;
        ImageView profile_image;
        LinearLayout video_call,message,previous_record,done;

        public InnerPatientDetailsRecycleViewAdaptor(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.name);
            age=itemView.findViewById(R.id.age);
            slot_time=itemView.findViewById(R.id.slot_time);
            video_call=itemView.findViewById(R.id.video_call);
            message=itemView.findViewById(R.id.message);
            profile_image=itemView.findViewById(R.id.profile_image);
            previous_record=itemView.findViewById(R.id.previous_record);
            done=itemView.findViewById(R.id.done);

        }
    }
}
