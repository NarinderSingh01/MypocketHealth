package com.medical.mypockethealth.Adaptors.UserSection.ProfileSection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.mypockethealth.Classes.ClientInformation.MorbiditiesList;
import com.medical.mypockethealth.R;

import java.util.List;

public class MorbidityRecycleViewAdaptor extends RecyclerView.Adapter<MorbidityRecycleViewAdaptor.InnerMorbidityRecycleViewAdaptor> {

    private final List<MorbiditiesList> morbiditiesLists;

    public MorbidityRecycleViewAdaptor(List<MorbiditiesList> morbiditiesLists) {
        this.morbiditiesLists = morbiditiesLists;
    }

    @NonNull
    @Override
    public InnerMorbidityRecycleViewAdaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InnerMorbidityRecycleViewAdaptor(LayoutInflater.from(parent.getContext()).inflate(R.layout.allergy_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InnerMorbidityRecycleViewAdaptor holder, int position) {

        holder.name.setText(morbiditiesLists.get(position).getMorbidityName());

    }

    @Override
    public int getItemCount() {
        return (morbiditiesLists!=null && morbiditiesLists.size()!=0 ? morbiditiesLists.size():0);
    }

    static class InnerMorbidityRecycleViewAdaptor extends RecyclerView.ViewHolder
    {
        TextView name;

        public InnerMorbidityRecycleViewAdaptor(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.name);
        }
    }
}
