package com.medical.mypockethealth.Adaptors.ProviderSection.EHRSection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.mypockethealth.Classes.ICRInformation.ICRInformationRoot;
import com.medical.mypockethealth.Classes.SlotsSection.SlotInformation;
import com.medical.mypockethealth.R;

import java.util.ArrayList;
import java.util.List;

public class DiagnosisRecycleViewAdaptor extends RecyclerView.Adapter<DiagnosisRecycleViewAdaptor.InnerDiagnosisRecycleViewAdaptor> {


    private List<ArrayList<String>> details;
    private callBackFromDiagnosisRecycleViewAdaptor callBackFromDiagnosisRecycleViewAdaptor;


    public DiagnosisRecycleViewAdaptor(List<ArrayList<String>> details,
                                       DiagnosisRecycleViewAdaptor.callBackFromDiagnosisRecycleViewAdaptor
                                               callBackFromDiagnosisRecycleViewAdaptor) {
        this.details = details;
        this.callBackFromDiagnosisRecycleViewAdaptor = callBackFromDiagnosisRecycleViewAdaptor;
    }

    public interface callBackFromDiagnosisRecycleViewAdaptor
    {

        void selectedCode(List<String> list);
    }


    @NonNull
    @Override
    public InnerDiagnosisRecycleViewAdaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InnerDiagnosisRecycleViewAdaptor(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.diagnosis_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InnerDiagnosisRecycleViewAdaptor holder, int position) {


        holder.diagnosis.setText(details.get(position).toString());

        holder.information_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(callBackFromDiagnosisRecycleViewAdaptor!=null)
                {

                    callBackFromDiagnosisRecycleViewAdaptor.selectedCode(details.get(position));

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return (details!=null && details.size()!=0 ? details.size():0);
    }

    public void loadData(List<ArrayList<String>> details )
    {
        this.details=details;
        notifyDataSetChanged();
    }

    static class InnerDiagnosisRecycleViewAdaptor extends RecyclerView.ViewHolder
    {

        TextView diagnosis;
        RelativeLayout information_layout;

        public InnerDiagnosisRecycleViewAdaptor(@NonNull View itemView) {
            super(itemView);

            diagnosis=itemView.findViewById(R.id.diagnosis);
            information_layout=itemView.findViewById(R.id.information_layout);
        }
    }
}
