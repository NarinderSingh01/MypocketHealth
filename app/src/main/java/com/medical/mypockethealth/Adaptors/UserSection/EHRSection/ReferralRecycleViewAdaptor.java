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
import com.medical.mypockethealth.Classes.EHRSection.ReferralInformation.ReferralInformation;
import com.medical.mypockethealth.R;

import java.util.List;

public class ReferralRecycleViewAdaptor extends RecyclerView.Adapter<ReferralRecycleViewAdaptor.InnerReferralRecycleViewAdaptor> {


    private List<ReferralInformation> referralInformation;
    private callBackFromReferralRecycleViewAdaptor callBackFromFollowUpFileRecycleViewAdaptor;


    public ReferralRecycleViewAdaptor(List<ReferralInformation> referralInformation, 
                                      callBackFromReferralRecycleViewAdaptor callBackFromFollowUpFileRecycleViewAdaptor) {
        this.referralInformation = referralInformation;
        this.callBackFromFollowUpFileRecycleViewAdaptor = callBackFromFollowUpFileRecycleViewAdaptor;
    }

    public interface callBackFromReferralRecycleViewAdaptor
    {
        
        void openFile(ReferralInformation referralInformation);
        void downloadFile(ReferralInformation referralInformation);

    }


    @NonNull
    @Override
    public InnerReferralRecycleViewAdaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InnerReferralRecycleViewAdaptor(LayoutInflater.from(parent.getContext()).inflate(R.layout.medicine_holder_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InnerReferralRecycleViewAdaptor holder, int position) {

        

        String name=referralInformation.get(position).getName()+" " + filterSpeciality(referralInformation.get(position).getSpeciality());
        holder.name.setText(name);
        holder.date.setText(referralInformation.get(position).getDate());

        holder.openFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(callBackFromFollowUpFileRecycleViewAdaptor!=null)
                {
                    callBackFromFollowUpFileRecycleViewAdaptor.openFile(referralInformation.get(position));
                }
            }
        });

        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(callBackFromFollowUpFileRecycleViewAdaptor!=null)
                {
                    callBackFromFollowUpFileRecycleViewAdaptor.downloadFile(referralInformation.get(position));
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
    public void loadData(List<ReferralInformation> referralInformation)
    {
        this.referralInformation=referralInformation;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return (referralInformation!=null && referralInformation.size()!=0 ? referralInformation.size():0);
    }

    static class InnerReferralRecycleViewAdaptor extends RecyclerView.ViewHolder
    {

        TextView name,date,openFile;
        LinearLayout download;
      

        public InnerReferralRecycleViewAdaptor(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.name);
            date=itemView.findViewById(R.id.date);
            openFile=itemView.findViewById(R.id.openFile);
            download=itemView.findViewById(R.id.download);
          
        }
    }

}
