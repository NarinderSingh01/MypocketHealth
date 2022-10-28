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

public class AddMorbidityRecycleViewAdaptor extends RecyclerView.Adapter<AddMorbidityRecycleViewAdaptor.InnerAddMorbidityRecycleViewAdaptor> {


    private List<String> morbidity;
    private final callBackFromAddMorbidityRecycleViewAdaptor callBackFromAddMorbidityRecycleViewAdaptor;

    public AddMorbidityRecycleViewAdaptor(List<String> morbidity, 
                                          AddMorbidityRecycleViewAdaptor.callBackFromAddMorbidityRecycleViewAdaptor callBackFromAddMorbidityRecycleViewAdaptor) {
        this.morbidity = morbidity;
        this.callBackFromAddMorbidityRecycleViewAdaptor = callBackFromAddMorbidityRecycleViewAdaptor;
    }

    public interface callBackFromAddMorbidityRecycleViewAdaptor
    {

        void deleteItemAddMorbidityRecycleViewAdaptor(int position);
        void innerFilesAddMorbidityRecycleViewAdaptor(String name);

    }

    @NonNull
    @Override
    public InnerAddMorbidityRecycleViewAdaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InnerAddMorbidityRecycleViewAdaptor(LayoutInflater.from(parent.getContext()).inflate(R.layout.medicine_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InnerAddMorbidityRecycleViewAdaptor holder, int position) {

        holder.name.setText(morbidity.get(position));

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(callBackFromAddMorbidityRecycleViewAdaptor!=null)

                {

                    callBackFromAddMorbidityRecycleViewAdaptor.deleteItemAddMorbidityRecycleViewAdaptor(position);
                }



            }
        });

        holder.files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(callBackFromAddMorbidityRecycleViewAdaptor!=null)

                {

                    callBackFromAddMorbidityRecycleViewAdaptor.innerFilesAddMorbidityRecycleViewAdaptor(null);
                }



            }
        });



    }

    @Override
    public int getItemCount() {
        return (morbidity!=null && morbidity.size()!=0 ? morbidity.size():0);
    }

    public void loadData(List<String> morbidity)
    {
        this.morbidity=morbidity;
        notifyDataSetChanged();
    }


    static class InnerAddMorbidityRecycleViewAdaptor extends RecyclerView.ViewHolder
    {

        TextView name;
        ImageView remove;
        CardView files;

        public InnerAddMorbidityRecycleViewAdaptor(@NonNull View itemView) {
            super(itemView);
            
            name=itemView.findViewById(R.id.name);
            remove=itemView.findViewById(R.id.remove);
            files=itemView.findViewById(R.id.files);
            
        }
    }
}
