package com.medical.mypockethealth.Adaptors.UserSection.ProfileSection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.mypockethealth.Classes.ClientInformation.AllergieList;
import com.medical.mypockethealth.Classes.ClientInformation.MedicationsList;
import com.medical.mypockethealth.Classes.ClientInformation.MorbiditiesList;
import com.medical.mypockethealth.R;

import java.util.List;

public class MedicationRecycleViewAdaptor extends RecyclerView.Adapter<MedicationRecycleViewAdaptor.InnerMedicationRecycleViewAdaptor> {

    private final List<MedicationsList> getMedicationsList;

    public MedicationRecycleViewAdaptor(List<MedicationsList> getMedicationsList) {
        this.getMedicationsList = getMedicationsList;
    }

    @NonNull
    @Override
    public InnerMedicationRecycleViewAdaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InnerMedicationRecycleViewAdaptor(LayoutInflater.from(parent.getContext()).inflate(R.layout.allergy_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InnerMedicationRecycleViewAdaptor holder, int position) {

        holder.name.setText(getMedicationsList.get(position).getMedicationName());

    }

    @Override
    public int getItemCount() {
        return (getMedicationsList!=null && getMedicationsList.size()!=0 ? getMedicationsList.size():0);
    }

    static class InnerMedicationRecycleViewAdaptor extends RecyclerView.ViewHolder
    {
        TextView name;

        public InnerMedicationRecycleViewAdaptor(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.name);
        }
    }
}
