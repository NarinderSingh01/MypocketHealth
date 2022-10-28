package com.medical.mypockethealth.Adaptors.SliderAdaptor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.mypockethealth.Classes.SliderClasses.SliderInformation;
import com.medical.mypockethealth.R;

import java.util.List;

public class SliderRecycleViewAdaptor extends RecyclerView.Adapter<SliderRecycleViewAdaptor.InnerSliderRecycleViewAdaptor> {

   private final List<SliderInformation> sliderInformation;
   private final CallBackFromSliderRecycleViewAdaptor callBackFromSliderRecycleViewAdaptor;

   public interface CallBackFromSliderRecycleViewAdaptor
   {
       
       void onClicked(SliderInformation sliderInformation);
   }

    public SliderRecycleViewAdaptor(List<SliderInformation> sliderInformation, CallBackFromSliderRecycleViewAdaptor callBackFromSliderRecycleViewAdaptor) {
        this.sliderInformation = sliderInformation;
        this.callBackFromSliderRecycleViewAdaptor = callBackFromSliderRecycleViewAdaptor;
    }

    @NonNull
    @Override
    public InnerSliderRecycleViewAdaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InnerSliderRecycleViewAdaptor(LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InnerSliderRecycleViewAdaptor holder, int position) {

        
        holder.setInformation(sliderInformation.get(position));
        
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if(callBackFromSliderRecycleViewAdaptor!=null)
                {
                    
                    callBackFromSliderRecycleViewAdaptor.onClicked(sliderInformation.get(position));
                }
            }
        });
        
        
    }

    @Override
    public int getItemCount() {
        return  (sliderInformation!=null && sliderInformation.size()!=0 ? sliderInformation.size():0);
    }

    static class InnerSliderRecycleViewAdaptor extends RecyclerView.ViewHolder
    {

        ImageView image;
        RelativeLayout layout;
        TextView title;
        CardView cardView;

        public InnerSliderRecycleViewAdaptor(@NonNull View itemView) {
            super(itemView);

            image=itemView.findViewById(R.id.main_image);
            layout=itemView.findViewById(R.id.main_layout);
            title=itemView.findViewById(R.id.title);
            cardView=itemView.findViewById(R.id.cardView);

        }


        private void setInformation(SliderInformation sliderInformation)
        {

           image.setBackgroundResource(sliderInformation.getImage());
           
           layout.setBackgroundResource(sliderInformation.getBackgroundColor());
           
           title.setText(sliderInformation.getTitle());

        }
    }
}
