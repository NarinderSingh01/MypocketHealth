package com.medical.mypockethealth.Adaptors.SliderAdaptor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.mypockethealth.Classes.SliderClasses.SliderInformation;
import com.medical.mypockethealth.R;

import java.util.List;

public class MapSliderRecycleViewAdaptor extends RecyclerView.Adapter<MapSliderRecycleViewAdaptor.InnerMapSliderRecycleViewAdaptor> {


    private final List<SliderInformation> sliderInformation;
    private final CallBackFromMapSliderRecycleViewAdaptor callBackFromMapSliderRecycleViewAdaptor;

    public interface CallBackFromMapSliderRecycleViewAdaptor
    {

        void onClicked(SliderInformation sliderInformation);
    }


    public MapSliderRecycleViewAdaptor(List<SliderInformation> sliderInformation,
                                       CallBackFromMapSliderRecycleViewAdaptor callBackFromMapSliderRecycleViewAdaptor) {
        this.sliderInformation = sliderInformation;
        this.callBackFromMapSliderRecycleViewAdaptor = callBackFromMapSliderRecycleViewAdaptor;
    }

    @NonNull
    @Override
    public InnerMapSliderRecycleViewAdaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InnerMapSliderRecycleViewAdaptor(LayoutInflater.from(parent.getContext()).inflate(R.layout.map_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InnerMapSliderRecycleViewAdaptor holder, int position) {

        holder.setInformation(sliderInformation.get(position));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(callBackFromMapSliderRecycleViewAdaptor!=null)
                {
                    callBackFromMapSliderRecycleViewAdaptor.onClicked(sliderInformation.get(position));
                }
            }
        });

    }

    @Override
    public int getItemCount() {

        return (sliderInformation!=null && sliderInformation.size()!=0 ? sliderInformation.size():0);
    }

    static class InnerMapSliderRecycleViewAdaptor extends RecyclerView.ViewHolder
    {

        ImageView imageView;
        CardView cardView;

        public InnerMapSliderRecycleViewAdaptor(@NonNull View itemView) {
            super(itemView);

            imageView=itemView.findViewById(R.id.maps);
            cardView=itemView.findViewById(R.id.cardView);
        }


        private void setInformation(SliderInformation sliderInformation)
        {

            imageView.setBackgroundResource(sliderInformation.getImage());


        }
    }

}
