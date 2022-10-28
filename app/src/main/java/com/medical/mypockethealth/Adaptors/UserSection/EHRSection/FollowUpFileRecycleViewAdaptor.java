package com.medical.mypockethealth.Adaptors.UserSection.EHRSection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.mypockethealth.ApplicationBase.BaseActivity;
import com.medical.mypockethealth.Classes.EHRSection.FollowUpInformation.FollowUpInformation;
import com.medical.mypockethealth.R;

import java.util.List;

public class FollowUpFileRecycleViewAdaptor extends RecyclerView.Adapter<FollowUpFileRecycleViewAdaptor.InnerFollowUpFileRecycleViewAdaptor> {


    private List<FollowUpInformation> followUpInformation;
    private callBackFromFollowUpFileRecycleViewAdaptor callBackFromFollowUpFileRecycleViewAdaptor;


    public FollowUpFileRecycleViewAdaptor(List<FollowUpInformation> followUpInformation,
                                          FollowUpFileRecycleViewAdaptor.callBackFromFollowUpFileRecycleViewAdaptor
                                                  callBackFromFollowUpFileRecycleViewAdaptor) {
        this.followUpInformation = followUpInformation;
        this.callBackFromFollowUpFileRecycleViewAdaptor = callBackFromFollowUpFileRecycleViewAdaptor;
    }

    public interface callBackFromFollowUpFileRecycleViewAdaptor
    {
        
        void openFile(FollowUpInformation followUpInformation);
        void downloadFile(FollowUpInformation followUpInformation);


    }


    @NonNull
    @Override
    public InnerFollowUpFileRecycleViewAdaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InnerFollowUpFileRecycleViewAdaptor(LayoutInflater.from(parent.getContext()).inflate(R.layout.medicine_holder_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InnerFollowUpFileRecycleViewAdaptor holder, int position) {

        

        String name=followUpInformation.get(position).getProviderName()+" " + filterSpeciality(followUpInformation.get(position).getSpeciality());
        holder.name.setText(name);
        holder.date.setText(followUpInformation.get(position).getDate());

        holder.openFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(callBackFromFollowUpFileRecycleViewAdaptor!=null)
                {
                    callBackFromFollowUpFileRecycleViewAdaptor.openFile(followUpInformation.get(position));
                }
            }
        });


        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(callBackFromFollowUpFileRecycleViewAdaptor!=null)
                {
                    callBackFromFollowUpFileRecycleViewAdaptor.downloadFile(followUpInformation.get(position));
                }
            }
        });

      
    }

    
    private String filterSpeciality(String speciality)
    {
        
        String[] value=speciality.split("");
        
        StringBuilder stringBuilder=new StringBuilder();

        for (String s : value) {
            
            if(!s.equals("H"))
            {
                stringBuilder.append(s);
            }
            else 
            {
                break;
            }
            
        }
        
        return stringBuilder.toString();
        
    }
    public void loadData(List<FollowUpInformation> followUpInformation)
    {
        this.followUpInformation=followUpInformation;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return (followUpInformation!=null && followUpInformation.size()!=0 ? followUpInformation.size():0);
    }

    static class InnerFollowUpFileRecycleViewAdaptor extends RecyclerView.ViewHolder
    {

        TextView name,date,openFile;
        LinearLayout download;

        public InnerFollowUpFileRecycleViewAdaptor(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.name);
            date=itemView.findViewById(R.id.date);
            openFile=itemView.findViewById(R.id.openFile);
            download=itemView.findViewById(R.id.download);
           
        }
    }

}
