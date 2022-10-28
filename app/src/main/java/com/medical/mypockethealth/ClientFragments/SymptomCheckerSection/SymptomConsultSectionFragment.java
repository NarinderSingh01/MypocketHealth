package com.medical.mypockethealth.ClientFragments.SymptomCheckerSection;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.medical.mypockethealth.ClientFragments.DoctorBookingSection.DoctorBookingTwoFragment;
import com.medical.mypockethealth.ClientFragments.HomeFragment;
import com.medical.mypockethealth.R;


public class SymptomConsultSectionFragment extends Fragment {


    public SymptomConsultSectionFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_symptom_consult_section, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                        new SymptomCheckerThirteenFragment()).addToBackStack(null).commit();

            }
        });

        view.findViewById(R.id.consult).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HomeFragment.mode=1;

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                        new DoctorBookingTwoFragment()).addToBackStack(null).commit();

            }
        });

        view.findViewById(R.id.virtual).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HomeFragment.mode=1;

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                        new DoctorBookingTwoFragment()).addToBackStack(null).commit();

            }
        });
    }
}