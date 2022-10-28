package com.medical.mypockethealth.Adaptors.ProviderSection.EHRSection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.mypockethealth.Adaptors.UserSection.RegistrationSection.AddMedicineListRecycleViewAdaptor;
import com.medical.mypockethealth.R;

import java.util.List;
import java.util.Map;

public class AddPrescriptionMedicineRecycleViewAdaptor extends RecyclerView.Adapter<AddPrescriptionMedicineRecycleViewAdaptor.InnerAddPrescriptionMedicineRecycleViewAdaptor> {

    private List<String> medicineDiagnosis;
    private List<String> medicineName;
    private List<String> medicineQuantity;

    private final callBackFromAddPrescriptionMedicineRecycleViewAdaptor callBackFromAddMedicineListRecycleViewAdaptor;


    public AddPrescriptionMedicineRecycleViewAdaptor(List<String> medicineDiagnosis, List<String> medicineName, List<String> medicineQuantity,
                                                     callBackFromAddPrescriptionMedicineRecycleViewAdaptor callBackFromAddMedicineListRecycleViewAdaptor) {
        this.medicineDiagnosis = medicineDiagnosis;
        this.medicineName = medicineName;
        this.medicineQuantity = medicineQuantity;
        this.callBackFromAddMedicineListRecycleViewAdaptor = callBackFromAddMedicineListRecycleViewAdaptor;
    }

    public interface callBackFromAddPrescriptionMedicineRecycleViewAdaptor
    {

        void deleteItem(int position);
        void innerFiles(String name);

    }

    public void loadData( List<String> medicineDiagnosis,List<String> medicineName,List<String> medicineQuantity)
    {
        this.medicineDiagnosis=medicineDiagnosis;
        this.medicineName=medicineName;
        this.medicineQuantity=medicineQuantity;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InnerAddPrescriptionMedicineRecycleViewAdaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InnerAddPrescriptionMedicineRecycleViewAdaptor(LayoutInflater.from(parent.getContext()).inflate(R.layout.add_prescription_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InnerAddPrescriptionMedicineRecycleViewAdaptor holder, int position) {

        holder.diagnosis.setText(medicineDiagnosis.get(position));
        holder.name.setText(medicineName.get(position));
        holder.quantity.setText(medicineQuantity.get(position));
        
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(callBackFromAddMedicineListRecycleViewAdaptor!=null)
                {
                    callBackFromAddMedicineListRecycleViewAdaptor.deleteItem(position);

                }
                
            }
        });
     

    }

    @Override
    public int getItemCount() {
        return (medicineName!=null && medicineName.size()!=0 ? medicineName.size():0);
    }

    static class InnerAddPrescriptionMedicineRecycleViewAdaptor extends RecyclerView.ViewHolder
    {
        TextView name,quantity,diagnosis;
        ImageView remove;
        CardView files;

        public InnerAddPrescriptionMedicineRecycleViewAdaptor(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.name);
            quantity=itemView.findViewById(R.id.quantity);
            diagnosis=itemView.findViewById(R.id.diagnosis);
            remove=itemView.findViewById(R.id.remove);
            files=itemView.findViewById(R.id.files);
        }
    }
}
