package com.medical.mypockethealth.ClientFragments.DoctorBookingSection;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.medical.mypockethealth.ClientActivity.ClientMainFrame;
import com.medical.mypockethealth.ClientFragments.HomeFragment;
import com.medical.mypockethealth.R;


public class DoctorBookingOneFragment extends Fragment{

    private static final String TAG = "DoctorBookingOneFragmen";



    public DoctorBookingOneFragment() {
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
        return inflater.inflate(R.layout.fragment_doctor_booking_one, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              
                 startActivity(new Intent(getContext(), ClientMainFrame.class));

            }
        });


        view.findViewById(R.id.virtual_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HomeFragment.mode=0;

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                        new DoctorBookingTwoFragment()).addToBackStack(null).commit();

            }
        });

        view.findViewById(R.id.script_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HomeFragment.mode=0;

                Toast.makeText(getContext(),"coming soon",Toast.LENGTH_SHORT).show();

//                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
//                        new DoctorBookingTwoFragment()).addToBackStack(null).commit();
            }
        });

        view.findViewById(R.id.consult_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HomeFragment.mode=0;
                
                Toast.makeText(getContext(),"coming soon",Toast.LENGTH_SHORT).show();

//                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
//                        new DoctorBookingTwoFragment()).addToBackStack(null).commit();

            }
        });



    }




}