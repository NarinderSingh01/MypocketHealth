package com.medical.mypockethealth.Adaptors.UserSection.SymptomChecker;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.mypockethealth.Classes.SymptomChecker.SpecialistInformation;
import com.medical.mypockethealth.Classes.SymptomChecker.SymptomInformation;
import com.medical.mypockethealth.R;

import java.text.DecimalFormat;
import java.util.List;

public class SpecialistsRecycleViewAdaptor extends RecyclerView.Adapter<SpecialistsRecycleViewAdaptor.InnerSymptomRecycleViewAdaptor> {

    private static final String TAG = "SymptomRecycleViewAdapt";
    private List<SpecialistInformation> specialistInformation;
    private callBackFromSymptomRecycleViewAdaptor callBackFromSymptomRecycleViewAdaptor;

    public SpecialistsRecycleViewAdaptor(List<SpecialistInformation> specialistInformation,
                                         callBackFromSymptomRecycleViewAdaptor callBackFromSymptomRecycleViewAdaptor) {
        this.specialistInformation = specialistInformation;
        this.callBackFromSymptomRecycleViewAdaptor = callBackFromSymptomRecycleViewAdaptor;
    }

    public interface callBackFromSymptomRecycleViewAdaptor {

        void onClick(SpecialistInformation specialistInformation);

    }

    @NonNull
    @Override
    public InnerSymptomRecycleViewAdaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InnerSymptomRecycleViewAdaptor(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.specialists_lay, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InnerSymptomRecycleViewAdaptor holder, int position) {

        Log.d(TAG, "onBindViewHolder: called");

        holder.specialist.setText(specialistInformation.get(position).getName());

        Log.d(TAG, "onBindViewHolder: " + Double.parseDouble(new DecimalFormat("#.#").format(specialistInformation.get(position).getAccuracy())));

        String accuracy = String.valueOf(Double.parseDouble(new DecimalFormat("#.#").format(specialistInformation.get(position).getAccuracy())));

        holder.accuracy.setText(accuracy);

        holder.viewDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBackFromSymptomRecycleViewAdaptor.onClick(specialistInformation.get(position));
            }
        });

    }

    public void loadData(List<SpecialistInformation> symptomInformation) {
        this.specialistInformation = symptomInformation;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (specialistInformation != null && specialistInformation.size() != 0 ? specialistInformation.size() : 0);
    }

    static class InnerSymptomRecycleViewAdaptor extends RecyclerView.ViewHolder {

        private TextView viewDoctor, specialist, accuracy;

        public InnerSymptomRecycleViewAdaptor(@NonNull View itemView) {
            super(itemView);

            viewDoctor = itemView.findViewById(R.id.viewDoctor);
            specialist = itemView.findViewById(R.id.specialist);
            accuracy = itemView.findViewById(R.id.accuracy);
        }
    }
}
