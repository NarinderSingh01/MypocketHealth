package com.medical.mypockethealth.Adaptors.ProviderSection.EHRSection;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewEHRRecycleViewAdaptor extends RecyclerView.Adapter<ViewEHRRecycleViewAdaptor.InnerViewEHRRecycleViewAdaptor> {


    @NonNull
    @Override
    public InnerViewEHRRecycleViewAdaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull InnerViewEHRRecycleViewAdaptor holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class InnerViewEHRRecycleViewAdaptor extends RecyclerView.ViewHolder
    {

        public InnerViewEHRRecycleViewAdaptor(@NonNull View itemView) {
            super(itemView);
        }
    }
}
