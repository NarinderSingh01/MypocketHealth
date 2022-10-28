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

public class AddAllergiesRecycleViewAdaptor extends RecyclerView.Adapter<AddAllergiesRecycleViewAdaptor.InnerAddAllergiesRecycleViewAdaptor> {

    private List<String> allergies;

    private final callBackFromAddAllergiesRecycleViewAdaptor callBackFromAddAllergiesRecycleViewAdaptor;


    public AddAllergiesRecycleViewAdaptor(List<String> allergies, AddAllergiesRecycleViewAdaptor.
            callBackFromAddAllergiesRecycleViewAdaptor callBackFromAddAllergiesRecycleViewAdaptor) {
        this.allergies = allergies;
        this.callBackFromAddAllergiesRecycleViewAdaptor = callBackFromAddAllergiesRecycleViewAdaptor;
    }

    public interface callBackFromAddAllergiesRecycleViewAdaptor
    {

        void deleteItemAddAllergiesRecycleViewAdaptor(int position);
        void innerFilesAddAllergiesRecycleViewAdaptor(String name);

    }

    public void loadData(List<String> allergies)
    {
        this.allergies=allergies;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InnerAddAllergiesRecycleViewAdaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InnerAddAllergiesRecycleViewAdaptor(LayoutInflater.from(parent.getContext()).inflate(R.layout.medicine_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InnerAddAllergiesRecycleViewAdaptor holder, int position) {

        holder.name.setText(allergies.get(position));

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(callBackFromAddAllergiesRecycleViewAdaptor!=null)

                {

                    callBackFromAddAllergiesRecycleViewAdaptor.deleteItemAddAllergiesRecycleViewAdaptor(position);
                }



            }
        });

        holder.files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(callBackFromAddAllergiesRecycleViewAdaptor!=null)

                {

                    callBackFromAddAllergiesRecycleViewAdaptor.innerFilesAddAllergiesRecycleViewAdaptor(null);
                }



            }
        });


    }

    @Override
    public int getItemCount() {
        return (allergies!=null && allergies.size()!=0 ? allergies.size():0);
    }

    static class InnerAddAllergiesRecycleViewAdaptor extends RecyclerView.ViewHolder
    {
        TextView name;
        ImageView remove;
        CardView files;

        public InnerAddAllergiesRecycleViewAdaptor(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.name);
            remove=itemView.findViewById(R.id.remove);
            files=itemView.findViewById(R.id.files);
        }
    }
}
