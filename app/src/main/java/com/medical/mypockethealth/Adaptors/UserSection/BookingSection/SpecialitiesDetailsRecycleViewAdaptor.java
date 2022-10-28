package com.medical.mypockethealth.Adaptors.UserSection.BookingSection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.mypockethealth.Classes.SpecialitiesInformation.SpecialitiesInformation;
import com.medical.mypockethealth.R;

import java.util.ArrayList;
import java.util.List;

public class SpecialitiesDetailsRecycleViewAdaptor extends
        RecyclerView.Adapter<SpecialitiesDetailsRecycleViewAdaptor.InnerSpecialitiesDetailsRecycleViewAdaptor> implements Filterable {

    private static final String TAG = "SpecialitiesDetailsRecy";

    private List<SpecialitiesInformation> specialitiesInformation;
    private List<SpecialitiesInformation> filteredCategoriesSpecialitiesInformation;
    public static List<SpecialitiesInformation> unfilteredSpecialitiesInformation;
    private final callBackFromSpecialitiesDetailsRecycleViewAdaptor callBackFromSpecialitiesDetailsRecycleViewAdaptor;
    private boolean status=false;


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                 status=false;

                List<SpecialitiesInformation> filter=new ArrayList<>();

                if(constraint==null || constraint.length()==0)
                {

                    status=true;

                    filter.addAll(SpecialitiesDetailsRecycleViewAdaptor.unfilteredSpecialitiesInformation);



                }
                else
                {


                    String value=constraint.toString().toLowerCase().trim();

                    for (SpecialitiesInformation filteredCategory : filteredCategoriesSpecialitiesInformation) {

                        if(filteredCategory.getTitle().toLowerCase().contains(value))
                        {

                            filter.add(filteredCategory);
                            status=true;

                        }

                    }


                }

                FilterResults filterResults=new FilterResults();
                filterResults.values=filter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                specialitiesInformation.clear();
                specialitiesInformation.addAll((List)results.values);
                notifyDataSetChanged();

                if(callBackFromSpecialitiesDetailsRecycleViewAdaptor!=null)
                {

                    callBackFromSpecialitiesDetailsRecycleViewAdaptor.searchStatus(status);
                }
            }
        };
    }

    public interface callBackFromSpecialitiesDetailsRecycleViewAdaptor
    {

        void selected(int position);
        void searchStatus(boolean status);
    }


    public SpecialitiesDetailsRecycleViewAdaptor(List<SpecialitiesInformation> specialitiesInformation,
                                                 SpecialitiesDetailsRecycleViewAdaptor.callBackFromSpecialitiesDetailsRecycleViewAdaptor callBackFromSpecialitiesDetailsRecycleViewAdaptor) {
        this.specialitiesInformation = specialitiesInformation;
        this.filteredCategoriesSpecialitiesInformation = specialitiesInformation;
        this.callBackFromSpecialitiesDetailsRecycleViewAdaptor = callBackFromSpecialitiesDetailsRecycleViewAdaptor;
    }

    @NonNull
    @Override
    public InnerSpecialitiesDetailsRecycleViewAdaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InnerSpecialitiesDetailsRecycleViewAdaptor(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.speciality_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InnerSpecialitiesDetailsRecycleViewAdaptor holder, int position) {


        holder.specialities.setText(specialitiesInformation.get(position).getTitle());

        holder.speciality_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(callBackFromSpecialitiesDetailsRecycleViewAdaptor!=null)
                {
                    callBackFromSpecialitiesDetailsRecycleViewAdaptor.selected(position);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return (specialitiesInformation!=null && specialitiesInformation.size()!=0 ? specialitiesInformation.size():0);
    }

    public void loadData(List<SpecialitiesInformation> specialitiesInformation)
    {
        this.specialitiesInformation=specialitiesInformation;
        this.filteredCategoriesSpecialitiesInformation=specialitiesInformation;
        notifyDataSetChanged();
    }

    static class InnerSpecialitiesDetailsRecycleViewAdaptor extends RecyclerView.ViewHolder
    {

        TextView specialities;

        LinearLayout speciality_layout;

        public InnerSpecialitiesDetailsRecycleViewAdaptor(@NonNull View itemView) {
            super(itemView);

            specialities=itemView.findViewById(R.id.specialities);
            speciality_layout=itemView.findViewById(R.id.speciality_layout);
        }
    }
}
