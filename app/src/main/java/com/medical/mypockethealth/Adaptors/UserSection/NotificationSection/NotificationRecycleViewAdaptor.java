package com.medical.mypockethealth.Adaptors.UserSection.NotificationSection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.mypockethealth.Classes.BookDoctorInformation.BookDoctorInformation;
import com.medical.mypockethealth.Classes.NotificationSection.NotificationInformation;
import com.medical.mypockethealth.R;

import java.util.List;

public class NotificationRecycleViewAdaptor extends RecyclerView.Adapter<NotificationRecycleViewAdaptor.InnerNotificationRecycleViewAdaptor> {

    private List<NotificationInformation> notificationInformation;

    public NotificationRecycleViewAdaptor(List<NotificationInformation> notificationInformation) {
        this.notificationInformation = notificationInformation;
    }

    @NonNull
    @Override
    public InnerNotificationRecycleViewAdaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
          return new InnerNotificationRecycleViewAdaptor(LayoutInflater.
                from(parent.getContext()).inflate(R.layout.notification_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InnerNotificationRecycleViewAdaptor holder, int position) {

        holder.title.setText(notificationInformation.get(position).getTitle());
        holder.message.setText(notificationInformation.get(position).getMessage());

    }


    public void loadData(List<NotificationInformation> notificationInformation)
    {
        this.notificationInformation=notificationInformation;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return(notificationInformation!=null && notificationInformation.size()!=0 ? notificationInformation.size():0);
    }

    static class InnerNotificationRecycleViewAdaptor extends RecyclerView.ViewHolder
    {

        private TextView title,message;

        public InnerNotificationRecycleViewAdaptor(@NonNull View itemView) {
            super(itemView);

            title=itemView.findViewById(R.id.title);
            message=itemView.findViewById(R.id.message);
        }
    }
}
