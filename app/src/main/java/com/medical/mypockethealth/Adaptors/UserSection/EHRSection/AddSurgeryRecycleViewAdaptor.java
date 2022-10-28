package com.medical.mypockethealth.Adaptors.UserSection.EHRSection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.mypockethealth.R;

import java.util.List;

public class AddSurgeryRecycleViewAdaptor extends RecyclerView.Adapter<AddSurgeryRecycleViewAdaptor.InnerAddSurgeryRecycleViewAdaptor> {

    private static final String TAG = "AddSurgeryRecycleViewAd";

     private List<String> surgery;
     private final callBackFromAddSurgeryRecycleViewAdaptor callBackFromAddSurgeryRecycleViewAdaptor;


    public AddSurgeryRecycleViewAdaptor(List<String> surgery,
                                        AddSurgeryRecycleViewAdaptor.callBackFromAddSurgeryRecycleViewAdaptor callBackFromAddSurgeryRecycleViewAdaptor) {
        this.surgery = surgery;
        this.callBackFromAddSurgeryRecycleViewAdaptor = callBackFromAddSurgeryRecycleViewAdaptor;
    }

    public interface callBackFromAddSurgeryRecycleViewAdaptor
    {

        void deleteItemAddSurgeryRecycleViewAdaptor(int position);

    }

    @NonNull
    @Override
    public InnerAddSurgeryRecycleViewAdaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InnerAddSurgeryRecycleViewAdaptor(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.medicine_holder_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InnerAddSurgeryRecycleViewAdaptor holder, int position) {

        holder.name.setText(surgery.get(position));

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(callBackFromAddSurgeryRecycleViewAdaptor!=null)

                {

                    callBackFromAddSurgeryRecycleViewAdaptor.deleteItemAddSurgeryRecycleViewAdaptor(position);
                }



            }
        });


    }

    public void loadData(List<String> surgery)
    {
        this.surgery=surgery;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (surgery!=null && surgery.size()!=0 ? surgery.size():0);
    }

    static class InnerAddSurgeryRecycleViewAdaptor extends RecyclerView.ViewHolder
    {

        TextView name;
        ImageView remove;

        public InnerAddSurgeryRecycleViewAdaptor(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.name);
            remove=itemView.findViewById(R.id.remove);
        }
    }
}
