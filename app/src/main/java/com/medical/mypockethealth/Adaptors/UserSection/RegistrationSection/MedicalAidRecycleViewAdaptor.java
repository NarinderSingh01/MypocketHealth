package com.medical.mypockethealth.Adaptors.UserSection.RegistrationSection;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.mypockethealth.Classes.MedicalAidInformation.MedicalAidInformation;
import com.medical.mypockethealth.R;

import java.util.ArrayList;
import java.util.List;

public class MedicalAidRecycleViewAdaptor extends RecyclerView.Adapter<MedicalAidRecycleViewAdaptor.InnerMedicalAidRecycleViewAdaptor> {

    private List<MedicalAidInformation> medicalAidInformationList;
    private final callBackFromMedicalAidRecycleViewAdaptor callBackFromMedicalAidRecycleViewAdaptor;

    public MedicalAidRecycleViewAdaptor(List<MedicalAidInformation> medicalAidInformationList,
                                        MedicalAidRecycleViewAdaptor.callBackFromMedicalAidRecycleViewAdaptor callBackFromMedicalAidRecycleViewAdaptor) {
        this.medicalAidInformationList = medicalAidInformationList;
        this.callBackFromMedicalAidRecycleViewAdaptor = callBackFromMedicalAidRecycleViewAdaptor;
    }

    public interface callBackFromMedicalAidRecycleViewAdaptor
    {

        void selectedCode(MedicalAidInformation medicalAidInformation);
    }


    @NonNull
    @Override
    public InnerMedicalAidRecycleViewAdaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InnerMedicalAidRecycleViewAdaptor(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.diagnosis_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InnerMedicalAidRecycleViewAdaptor holder, @SuppressLint("RecyclerView") int position) {


        holder.diagnosis.setText(medicalAidInformationList.get(position).getTitle());

        holder.information_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(callBackFromMedicalAidRecycleViewAdaptor!=null)
                {

                    callBackFromMedicalAidRecycleViewAdaptor.selectedCode(medicalAidInformationList.get(position));

                }
            }
        });

    }

    @Override
    public int getItemCount() {

        return (medicalAidInformationList!=null && medicalAidInformationList.size()!=0 ? medicalAidInformationList.size():0);
    }


    public void loadData(List<MedicalAidInformation> medicalAidInformation )
    {
        this.medicalAidInformationList=medicalAidInformation;
        notifyDataSetChanged();
    }


    static class InnerMedicalAidRecycleViewAdaptor extends RecyclerView.ViewHolder
    {
        TextView diagnosis;
        RelativeLayout information_layout;

        public InnerMedicalAidRecycleViewAdaptor(@NonNull View itemView) {
            super(itemView);

            diagnosis=itemView.findViewById(R.id.diagnosis);
            information_layout=itemView.findViewById(R.id.information_layout);

        }
    }
}
