package com.medical.mypockethealth.Adaptors.UserSection.BookingSection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.mypockethealth.Classes.ClientInformation.ClientInformation;
import com.medical.mypockethealth.R;

import java.util.List;

public class AddPatientDetailsRecycleViewAdaptor extends RecyclerView.Adapter<AddPatientDetailsRecycleViewAdaptor.InnerAddPatientDetailsRecycleViewAdaptor> {

    private List<ClientInformation> clientInformation;
    
    private final callBackFromAddPatientDetailsRecycleViewAdaptor callBackFromAddPatientDetailsRecycleViewAdaptor;


    public AddPatientDetailsRecycleViewAdaptor(List<ClientInformation> clientInformation,
                                               AddPatientDetailsRecycleViewAdaptor.callBackFromAddPatientDetailsRecycleViewAdaptor callBackFromAddPatientDetailsRecycleViewAdaptor) {
        this.clientInformation = clientInformation;
        this.callBackFromAddPatientDetailsRecycleViewAdaptor = callBackFromAddPatientDetailsRecycleViewAdaptor;
    }

    public interface callBackFromAddPatientDetailsRecycleViewAdaptor
    {

        void deleteItem(int position);

    }
    
    
    @NonNull
    @Override
    public InnerAddPatientDetailsRecycleViewAdaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InnerAddPatientDetailsRecycleViewAdaptor(LayoutInflater.from(parent.getContext()).inflate(R.layout.add_patient_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InnerAddPatientDetailsRecycleViewAdaptor holder, int position) {

        
        holder.name.setText(clientInformation.get(position).getUsername());

        holder.age.setText(clientInformation.get(position).getAge());

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(callBackFromAddPatientDetailsRecycleViewAdaptor!=null)

                {

                    callBackFromAddPatientDetailsRecycleViewAdaptor.deleteItem(position);
                }



            }
        });
        
        
    }

    public void loadData(List<ClientInformation> clientInformation)
    {
        this.clientInformation=clientInformation;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return (clientInformation!=null && clientInformation.size()!=0 ? clientInformation.size():0);
    }

    static class InnerAddPatientDetailsRecycleViewAdaptor extends RecyclerView.ViewHolder
    {

         TextView name,age;
        ImageView remove;

        public InnerAddPatientDetailsRecycleViewAdaptor(@NonNull View itemView) {
            super(itemView);
            
            name=itemView.findViewById(R.id.name);
            age=itemView.findViewById(R.id.age);
            remove=itemView.findViewById(R.id.remove);
            
        }
    }
}
