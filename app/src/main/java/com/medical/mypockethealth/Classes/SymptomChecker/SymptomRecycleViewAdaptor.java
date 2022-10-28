package com.medical.mypockethealth.Classes.SymptomChecker;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.mypockethealth.R;

import java.util.ArrayList;
import java.util.List;

public class SymptomRecycleViewAdaptor extends RecyclerView.Adapter<SymptomRecycleViewAdaptor.InnerSymptomRecycleViewAdaptor> implements Filterable {

    private static final String TAG = "SymptomRecycleViewAdapt";
    private List<SymptomInformation> symptomInformation;
    private List<SymptomInformation> filteredSymptomInformation;
    public static List<SymptomInformation> unFilteredSymptomInformation;

    callBackFromSymptomRecycleViewAdaptor callBackFromSymptomRecycleViewAdaptor;

    public SymptomRecycleViewAdaptor(List<SymptomInformation> symptomInformation,
                                     callBackFromSymptomRecycleViewAdaptor callBackFromSymptomRecycleViewAdaptor) {
        this.symptomInformation = symptomInformation;
        this.filteredSymptomInformation = symptomInformation;
        this.callBackFromSymptomRecycleViewAdaptor = callBackFromSymptomRecycleViewAdaptor;
    }

    public interface callBackFromSymptomRecycleViewAdaptor {

        void onClick(SymptomInformation symptomInformation);

    }

    @NonNull
    @Override
    public InnerSymptomRecycleViewAdaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InnerSymptomRecycleViewAdaptor(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.symptoms_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InnerSymptomRecycleViewAdaptor holder, int position) {

        Log.d(TAG, "onBindViewHolder: called");

        holder.text.setText(symptomInformation.get(position).getName());

        holder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBackFromSymptomRecycleViewAdaptor.onClick(symptomInformation.get(position));
            }
        });

    }

    public void loadData(List<SymptomInformation> symptomInformation) {
        this.symptomInformation = symptomInformation;
        this.filteredSymptomInformation = symptomInformation;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return (symptomInformation != null && symptomInformation.size() != 0 ? symptomInformation.size() : 0);
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {


                List<SymptomInformation> filter = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {

                    filter.addAll(SymptomRecycleViewAdaptor.unFilteredSymptomInformation);

//                    Log.d(TAG, "performFiltering: list size : "+FileManagerHealthLockerAdapter.unFilteredList.size());


                } else {

                    String value = constraint.toString().toLowerCase().trim();

                    for (SymptomInformation symptomInformation : filteredSymptomInformation) {

                        if (symptomInformation.getName().toLowerCase().contains(value)) {

                            filter.add(symptomInformation);

                        }
                    }

                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                symptomInformation.clear();
                symptomInformation.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };
    }

    static class InnerSymptomRecycleViewAdaptor extends RecyclerView.ViewHolder {

        private TextView text;

        public InnerSymptomRecycleViewAdaptor(@NonNull View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.txt_symptom);
        }
    }
}
