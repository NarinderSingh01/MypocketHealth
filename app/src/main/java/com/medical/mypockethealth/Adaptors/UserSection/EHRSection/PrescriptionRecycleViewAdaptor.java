package com.medical.mypockethealth.Adaptors.UserSection.EHRSection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.mypockethealth.ApplicationBase.BaseActivity;
import com.medical.mypockethealth.Classes.EHRSection.FollowUpInformation.FollowUpInformation;
import com.medical.mypockethealth.Classes.EHRSection.PrescriptionInformation.PrescriptionInformation;
import com.medical.mypockethealth.Classes.EHRSection.ReferralInformation.ReferralInformation;
import com.medical.mypockethealth.R;

import java.util.List;

public class PrescriptionRecycleViewAdaptor extends RecyclerView.Adapter<PrescriptionRecycleViewAdaptor.InnerPrescriptionRecycleViewAdaptor> {


    private List<PrescriptionInformation> prescriptionInformation;
    private callBackFromPrescriptionRecycleViewAdaptor callBackFromFollowUpFileRecycleViewAdaptor;


    public PrescriptionRecycleViewAdaptor(List<PrescriptionInformation> prescriptionInformation, 
                                          callBackFromPrescriptionRecycleViewAdaptor callBackFromFollowUpFileRecycleViewAdaptor) {
        this.prescriptionInformation = prescriptionInformation;
        this.callBackFromFollowUpFileRecycleViewAdaptor = callBackFromFollowUpFileRecycleViewAdaptor;
    }

    public interface callBackFromPrescriptionRecycleViewAdaptor
    {
        
        void openFile(PrescriptionInformation prescriptionInformation);
        void downloadFile(PrescriptionInformation prescriptionInformation);

    }


    @NonNull
    @Override
    public InnerPrescriptionRecycleViewAdaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InnerPrescriptionRecycleViewAdaptor(LayoutInflater.from(parent.getContext()).inflate(R.layout.medicine_holder_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InnerPrescriptionRecycleViewAdaptor holder, int position) {

        String name=prescriptionInformation.get(position).getName() + " " + filterSpeciality(prescriptionInformation.get(position).getSpeciality());
        holder.name.setText(name);
        holder.date.setText(prescriptionInformation.get(position).getDate());

        holder.openFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(callBackFromFollowUpFileRecycleViewAdaptor!=null)
                {
                    callBackFromFollowUpFileRecycleViewAdaptor.openFile(prescriptionInformation.get(position));
                }
            }
        });

        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(callBackFromFollowUpFileRecycleViewAdaptor!=null)
                {
                    callBackFromFollowUpFileRecycleViewAdaptor.downloadFile(prescriptionInformation.get(position));
                }
            }
        });



    }


    private String filterSpeciality(String speciality)
    {

        String[] value=speciality.split("");

        StringBuilder stringBuilder=new StringBuilder();

        for (String s : value) {

            if(!s.equals("H"))
            {
                stringBuilder.append(s);
            }
            else
            {
                break;
            }

        }

        return stringBuilder.toString();

    }
    public void loadData(List<PrescriptionInformation> prescriptionInformation)
    {
        this.prescriptionInformation=prescriptionInformation;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return (prescriptionInformation!=null && prescriptionInformation.size()!=0 ? prescriptionInformation.size():0);
    }

    static class InnerPrescriptionRecycleViewAdaptor extends RecyclerView.ViewHolder
    {

        TextView name,date,openFile;
        LinearLayout download;

        public InnerPrescriptionRecycleViewAdaptor(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.name);
            date=itemView.findViewById(R.id.date);
            openFile=itemView.findViewById(R.id.openFile);
            download=itemView.findViewById(R.id.download);
        }
    }

}
