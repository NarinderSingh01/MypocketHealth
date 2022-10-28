package com.medical.mypockethealth.Adaptors.UserSection.RegistrationSection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.mypockethealth.R;

import java.util.List;

public class AddMedicineListRecycleViewAdaptor extends RecyclerView.Adapter<AddMedicineListRecycleViewAdaptor.InnerAddMedicineListRecycleViewAdaptor> {

    private List<String> medicine;

    private final callBackFromAddMedicineListRecycleViewAdaptor callBackFromAddMedicineListRecycleViewAdaptor;


    public AddMedicineListRecycleViewAdaptor(List<String> medicine, AddMedicineListRecycleViewAdaptor.callBackFromAddMedicineListRecycleViewAdaptor callBackFromAddMedicineListRecycleViewAdaptor) {
        this.medicine = medicine;
        this.callBackFromAddMedicineListRecycleViewAdaptor = callBackFromAddMedicineListRecycleViewAdaptor;
    }

    public interface callBackFromAddMedicineListRecycleViewAdaptor
    {

        void deleteItem(int position);
        void innerFiles(String name);

    }

    public void loadData(List<String> medicine)
    {
        this.medicine=medicine;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InnerAddMedicineListRecycleViewAdaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InnerAddMedicineListRecycleViewAdaptor(LayoutInflater.from(parent.getContext()).inflate(R.layout.medicine_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InnerAddMedicineListRecycleViewAdaptor holder, int position) {

        holder.name.setText(medicine.get(position));

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(callBackFromAddMedicineListRecycleViewAdaptor!=null)

                {

                    callBackFromAddMedicineListRecycleViewAdaptor.deleteItem(position);
                }



            }
        });

        holder.files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(callBackFromAddMedicineListRecycleViewAdaptor!=null)

                {

                    callBackFromAddMedicineListRecycleViewAdaptor.innerFiles(null);
                }



            }
        });


    }

    @Override
    public int getItemCount() {
        return (medicine!=null && medicine.size()!=0 ? medicine.size():0);
    }

    static class InnerAddMedicineListRecycleViewAdaptor extends RecyclerView.ViewHolder
    {
        TextView name;
        ImageView remove;
        CardView files;

        public InnerAddMedicineListRecycleViewAdaptor(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.name);
            remove=itemView.findViewById(R.id.remove);
            files=itemView.findViewById(R.id.files);
        }
    }
}
