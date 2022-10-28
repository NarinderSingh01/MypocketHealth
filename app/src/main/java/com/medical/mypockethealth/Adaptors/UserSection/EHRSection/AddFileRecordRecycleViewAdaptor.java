package com.medical.mypockethealth.Adaptors.UserSection.EHRSection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.mypockethealth.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AddFileRecordRecycleViewAdaptor extends RecyclerView.Adapter<AddFileRecordRecycleViewAdaptor.InnerAddMedicineRecycleViewAdaptor> {

    private static final String TAG = "AddMedicineRecycleViewA";

    private  List<String> medicine;

    private final callBackFromAddFileRecordRecycleViewAdaptor callBackFromAddMedicineRecycleViewAdaptor;

    public interface callBackFromAddFileRecordRecycleViewAdaptor
    {

         void deleteItem(int position);
         void innerFiles(String name);

    }


    public AddFileRecordRecycleViewAdaptor(List<String> medicine,
                                           callBackFromAddFileRecordRecycleViewAdaptor callBackFromAddMedicineRecycleViewAdaptor) {
        this.medicine = medicine;
        this.callBackFromAddMedicineRecycleViewAdaptor = callBackFromAddMedicineRecycleViewAdaptor;
    }

    @NotNull
    @Override
    public InnerAddMedicineRecycleViewAdaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InnerAddMedicineRecycleViewAdaptor(LayoutInflater.from(parent.getContext()).inflate(R.layout.medicine_holder_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InnerAddMedicineRecycleViewAdaptor holder, int position) {

//        holder.name.setText(medicine.get(position));
//
//        holder.remove.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                 if(callBackFromAddMedicineRecycleViewAdaptor!=null)
//
//                 {
//
//                     callBackFromAddMedicineRecycleViewAdaptor.deleteItem(position);
//                 }
//
//
//
//            }
//        });
//
//        holder.files.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                if(callBackFromAddMedicineRecycleViewAdaptor!=null)
//
//                {
//
//                    callBackFromAddMedicineRecycleViewAdaptor.innerFiles(null);
//                }
//
//
//
//            }
//        });

    }


    public void loadData(List<String> medicine)
    {
        this.medicine=medicine;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return (medicine!=null && medicine.size()!=0 ? medicine.size():4);
    }

    static class InnerAddMedicineRecycleViewAdaptor extends RecyclerView.ViewHolder
    {

        TextView name;

        CardView files;

        public InnerAddMedicineRecycleViewAdaptor(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.name);
            files=itemView.findViewById(R.id.files);
        }
    }
}
