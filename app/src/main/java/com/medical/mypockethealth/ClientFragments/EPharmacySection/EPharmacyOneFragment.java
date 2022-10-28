package com.medical.mypockethealth.ClientFragments.EPharmacySection;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.medical.mypockethealth.R;

public class EPharmacyOneFragment extends Fragment {



    public EPharmacyOneFragment() {
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
        return inflater.inflate(R.layout.fragment_epharmacy_one, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        view.findViewById(R.id.medicine).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout
                        ,new EPharmacyTwoFragment()).addToBackStack(null).commit();

            }
        });

        view.findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout
                        ,new EPharmacySearchMedicineFragment()).addToBackStack(null).commit();

            }
        });
    }
}