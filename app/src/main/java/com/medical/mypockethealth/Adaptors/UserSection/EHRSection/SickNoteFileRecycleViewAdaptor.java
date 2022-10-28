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
import com.medical.mypockethealth.Classes.EHRSection.SickNoteInformation.SickNoteInformation;
import com.medical.mypockethealth.R;

import java.util.List;

public class SickNoteFileRecycleViewAdaptor extends RecyclerView.Adapter<SickNoteFileRecycleViewAdaptor.InnerSickNoteFileRecycleViewAdaptor> {


    private List<SickNoteInformation> sickNoteInformation;
    private final callBackFromSickNoteFileRecycleViewAdaptor callBackFromSickNoteFileRecycleViewAdaptor;



    public SickNoteFileRecycleViewAdaptor(List<SickNoteInformation> sickNoteInformation,
                                          SickNoteFileRecycleViewAdaptor.callBackFromSickNoteFileRecycleViewAdaptor
                                                  callBackFromSickNoteFileRecycleViewAdaptor) {
        this.sickNoteInformation = sickNoteInformation;
        this.callBackFromSickNoteFileRecycleViewAdaptor = callBackFromSickNoteFileRecycleViewAdaptor;
    }

    public interface callBackFromSickNoteFileRecycleViewAdaptor
    {
        
        void openFile(SickNoteInformation sickNoteInformation);
        void downloadFile(SickNoteInformation sickNoteInformation);

    }


    @NonNull
    @Override
    public InnerSickNoteFileRecycleViewAdaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InnerSickNoteFileRecycleViewAdaptor(LayoutInflater.from(parent.getContext()).inflate(R.layout.medicine_holder_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InnerSickNoteFileRecycleViewAdaptor holder, int position) {
        

        String name=sickNoteInformation.get(position).getName()+" " + filterSpeciality(sickNoteInformation.get(position).getSpeciality());
        holder.name.setText(name);
        holder.date.setText(sickNoteInformation.get(position).getDate());

        holder.openFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(callBackFromSickNoteFileRecycleViewAdaptor!=null)
                {
                    callBackFromSickNoteFileRecycleViewAdaptor.openFile(sickNoteInformation.get(position));
                }
            }
        });

        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(callBackFromSickNoteFileRecycleViewAdaptor!=null)
                {
                    callBackFromSickNoteFileRecycleViewAdaptor.downloadFile(sickNoteInformation.get(position));
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


    public void loadData(List<SickNoteInformation> sickNoteInformation)
    {
        this.sickNoteInformation=sickNoteInformation;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return (sickNoteInformation!=null && sickNoteInformation.size()!=0 ? sickNoteInformation.size():0);
    }

    static class InnerSickNoteFileRecycleViewAdaptor extends RecyclerView.ViewHolder
    {

        TextView name,date,openFile;
        LinearLayout download;

        public InnerSickNoteFileRecycleViewAdaptor(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.name);
            date=itemView.findViewById(R.id.date);
            openFile=itemView.findViewById(R.id.openFile);
            download=itemView.findViewById(R.id.download);
        }
    }

}

