package com.medical.mypockethealth.Adaptors.UserSection.ProfileSection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.mypockethealth.Adaptors.UserSection.RegistrationSection.AddAllergiesRecycleViewAdaptor;
import com.medical.mypockethealth.Classes.ClientInformation.AllergieList;
import com.medical.mypockethealth.R;

import java.util.List;

public class AllergyRecycleViewAdaptor extends RecyclerView.Adapter<AllergyRecycleViewAdaptor.InnerAllergyRecycleViewAdaptor> {

    private final List<AllergieList> allergies;

    public AllergyRecycleViewAdaptor(List<AllergieList> allergies) {
        this.allergies = allergies;
    }


    @NonNull
    @Override
    public InnerAllergyRecycleViewAdaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InnerAllergyRecycleViewAdaptor(LayoutInflater.from(parent.getContext()).inflate(R.layout.allergy_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InnerAllergyRecycleViewAdaptor holder, int position) {

            holder.name.setText(allergies.get(position).getAllergyName());

    }

    @Override
    public int getItemCount() {
        return (allergies!=null && allergies.size()!=0 ? allergies.size():0);
    }

    static class InnerAllergyRecycleViewAdaptor extends RecyclerView.ViewHolder
    {
        TextView name;

        public InnerAllergyRecycleViewAdaptor(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.name);
        }
    }
}
