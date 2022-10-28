package com.medical.mypockethealth.Adaptors.ChatSection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.mypockethealth.Classes.ChatSection.ChatInformation;
import com.medical.mypockethealth.R;

import java.util.List;

public class ChatShowerRecycleViewAdaptor extends RecyclerView.Adapter<ChatShowerRecycleViewAdaptor.InnerChatShowerRecycleViewAdaptor> {

    private List<ChatInformation> chatInformation;
    private final Context context;


    public ChatShowerRecycleViewAdaptor(List<ChatInformation> chatInformation, Context context) {
        this.chatInformation = chatInformation;
        this.context = context;
    }

    @NonNull
    @Override
    public InnerChatShowerRecycleViewAdaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InnerChatShowerRecycleViewAdaptor(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InnerChatShowerRecycleViewAdaptor holder, int position) {


        if(chatInformation.get(position).getType().equals("0"))
        {
            holder.sender_layout.setVisibility(View.GONE);
            holder.receiver_layout.setVisibility(View.VISIBLE);
            holder.receiverMessage.setText(chatInformation.get(position).getMessage());
            holder.receiverTime.setText(chatInformation.get(position).getDate());
        }
        else
        {

            holder.sender_layout.setVisibility(View.VISIBLE);
            holder.receiver_layout.setVisibility(View.GONE);
            holder.senderMessage.setText(chatInformation.get(position).getMessage());
            holder.senderTime.setText(chatInformation.get(position).getDate());

        }




    }


    public final void setData(List<ChatInformation> chatInformation)
    {

        this.chatInformation=chatInformation;

        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return chatInformation!=null && chatInformation.size()>0 ?chatInformation.size():0;
    }

    static class InnerChatShowerRecycleViewAdaptor extends RecyclerView.ViewHolder
    {

        RelativeLayout sender_layout,receiver_layout;
        TextView receiverMessage,receiverTime,senderMessage,senderTime;

        public InnerChatShowerRecycleViewAdaptor(@NonNull View itemView) {
            super(itemView);

            sender_layout=itemView.findViewById(R.id.sender_layout);
            receiver_layout=itemView.findViewById(R.id.receiver_layout);
            receiverMessage=itemView.findViewById(R.id.receiverMessage);
            receiverTime=itemView.findViewById(R.id.receiverTime);
            senderMessage=itemView.findViewById(R.id.senderMessage);
            senderTime=itemView.findViewById(R.id.senderTime);

        }
    }



}
