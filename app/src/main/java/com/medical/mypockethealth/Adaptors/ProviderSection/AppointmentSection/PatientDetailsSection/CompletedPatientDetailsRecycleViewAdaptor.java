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

public class CompletedPatientDetailsRecycleViewAdaptor extends RecyclerView.Adapter<CompletedPatientDetailsRecycleViewAdaptor.InnerCompletedPatientDetailsRecycleViewAdaptor> {


    private List<ClientInformation> clientInformation;
    private final Context context;
    private final CallBackFromCompletedPatientDetailsRecycleViewAdaptor callBackFromCompletedPatientDetailsRecycleViewAdaptor;


    public CompletedPatientDetailsRecycleViewAdaptor(List<ClientInformation> clientInformation, Context context, 
                                                     CallBackFromCompletedPatientDetailsRecycleViewAdaptor callBackFromCompletedPatientDetailsRecycleViewAdaptor) {
        this.clientInformation = clientInformation;
        this.context = context;
        this.callBackFromCompletedPatientDetailsRecycleViewAdaptor = callBackFromCompletedPatientDetailsRecycleViewAdaptor;
    }

    public interface CallBackFromCompletedPatientDetailsRecycleViewAdaptor
    {

        void previousRecord(ClientInformation clientInformation);
    }
    

    @NonNull
    @Override
    public InnerCompletedPatientDetailsRecycleViewAdaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InnerCompletedPatientDetailsRecycleViewAdaptor(LayoutInflater.
                from(parent.getContext()).inflate(R.layout.complete_patient_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InnerCompletedPatientDetailsRecycleViewAdaptor holder, int position) {

        holder.name.setText(clientInformation.get(position).getPatientName());
        holder.age.setText(clientInformation.get(position).getPatientAge());
        holder.slot_time.setText(clientInformation.get(position).getSlotTime());

        Picasso.with(context).load(Uri.parse(clientInformation.get(position).getProfileImage())).error(R.drawable.profile).into(holder.profile_image);


        if(clientInformation.get(position).getEhrStatus().equals(ResponseInformation.success_response_type))
        {
            holder.previous_record.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.previous_record.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return (clientInformation!=null && clientInformation.size()!=0 ? clientInformation.size():0);
    }

    public void loadData(List<ClientInformation> clientInformation)
    {
        this.clientInformation=clientInformation;
        notifyDataSetChanged();
    }

    static class InnerCompletedPatientDetailsRecycleViewAdaptor extends RecyclerView.ViewHolder
    {
        TextView name,age,slot_time;
        ImageView profile_image;
        LinearLayout previous_record;
       

        public InnerCompletedPatientDetailsRecycleViewAdaptor(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.name);
            age=itemView.findViewById(R.id.age);
            slot_time=itemView.findViewById(R.id.slot_time);
            profile_image=itemView.findViewById(R.id.profile_image);
            previous_record=itemView.findViewById(R.id.previous_record);
        }
    }

}
