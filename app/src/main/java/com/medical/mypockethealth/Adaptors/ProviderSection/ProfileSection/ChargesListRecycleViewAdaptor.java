package com.medical.mypockethealth.Adaptors.ProviderSection.ProfileSection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.mypockethealth.Adaptors.ProviderSection.EHRSection.DiagnosisRecycleViewAdaptor;
import com.medical.mypockethealth.Classes.ConsultChargesInformation.ChargesInformation;
import com.medical.mypockethealth.R;

import java.util.ArrayList;
import java.util.List;

public class ChargesListRecycleViewAdaptor extends RecyclerView.Adapter<ChargesListRecycleViewAdaptor.InnerChargesListRecycleViewAdaptor> {


    private List<ChargesInformation> chargesInformation;
    private final callBackFromChargesListRecycleViewAdaptor callBackFromChargesListRecycleViewAdaptor;


    public ChargesListRecycleViewAdaptor(List<ChargesInformation> chargesInformation,
                                         ChargesListRecycleViewAdaptor.callBackFromChargesListRecycleViewAdaptor callBackFromChargesListRecycleViewAdaptor) {
        this.chargesInformation = chargesInformation;
        this.callBackFromChargesListRecycleViewAdaptor = callBackFromChargesListRecycleViewAdaptor;
    }

    public interface callBackFromChargesListRecycleViewAdaptor
    {
        void selectedCode(ChargesInformation chargesInformation);
    }


    @NonNull
    @Override
    public InnerChargesListRecycleViewAdaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InnerChargesListRecycleViewAdaptor(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.diagnosis_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InnerChargesListRecycleViewAdaptor holder, int position) {


        holder.diagnosis.setText(chargesInformation.get(position).getAmount());

        holder.information_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(callBackFromChargesListRecycleViewAdaptor!=null)
                {
                    callBackFromChargesListRecycleViewAdaptor.selectedCode(chargesInformation.get(position));
                }

            }
        });


    }

    @Override
    public int getItemCount() {

        return (chargesInformation!=null && chargesInformation.size()!=0 ? chargesInformation.size():0);
    }

    public void loadData(List<ChargesInformation> chargesInformation )
    {
        this.chargesInformation=chargesInformation;
        notifyDataSetChanged();
    }

    static class InnerChargesListRecycleViewAdaptor extends RecyclerView.ViewHolder
    {

        TextView diagnosis;
        RelativeLayout information_layout;

        public InnerChargesListRecycleViewAdaptor(@NonNull View itemView) {
            super(itemView);

            diagnosis=itemView.findViewById(R.id.diagnosis);
            information_layout=itemView.findViewById(R.id.information_layout);

        }
    }
}
