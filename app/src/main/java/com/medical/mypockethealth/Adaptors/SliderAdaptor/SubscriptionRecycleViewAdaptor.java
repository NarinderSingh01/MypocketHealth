package com.medical.mypockethealth.Adaptors.SliderAdaptor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.mypockethealth.Classes.SliderClasses.SliderInformation;
import com.medical.mypockethealth.R;

import java.util.List;

public class SubscriptionRecycleViewAdaptor extends RecyclerView.Adapter<SubscriptionRecycleViewAdaptor.InnerSubscriptionRecycleViewAdaptor> {


    private final List<SliderInformation> sliderInformation;
    private final CallBackFromSubscriptionRecycleViewAdaptor callBackFromSubscriptionRecycleViewAdaptor;

    public interface CallBackFromSubscriptionRecycleViewAdaptor
    {

        void onClicked(SliderInformation sliderInformation);
    }


    public SubscriptionRecycleViewAdaptor(List<SliderInformation> sliderInformation,
                                          CallBackFromSubscriptionRecycleViewAdaptor callBackFromSubscriptionRecycleViewAdaptor) {
        this.sliderInformation = sliderInformation;
        this.callBackFromSubscriptionRecycleViewAdaptor = callBackFromSubscriptionRecycleViewAdaptor;
    }

    @NonNull
    @Override
    public InnerSubscriptionRecycleViewAdaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InnerSubscriptionRecycleViewAdaptor(LayoutInflater.from(parent.getContext()).inflate(R.layout.subscription_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InnerSubscriptionRecycleViewAdaptor holder, int position) {


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(callBackFromSubscriptionRecycleViewAdaptor!=null)
                {
                    callBackFromSubscriptionRecycleViewAdaptor.onClicked(null);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return  (sliderInformation!=null && sliderInformation.size()!=0 ? sliderInformation.size():0);
    }

    static class InnerSubscriptionRecycleViewAdaptor extends RecyclerView.ViewHolder
    {

        LinearLayout linearLayout;

        public InnerSubscriptionRecycleViewAdaptor(@NonNull View itemView) {
            super(itemView);

            linearLayout=itemView.findViewById(R.id.Continue);
        }
    }
}
