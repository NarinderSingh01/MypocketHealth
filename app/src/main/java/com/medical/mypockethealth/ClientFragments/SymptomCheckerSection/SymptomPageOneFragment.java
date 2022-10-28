package com.medical.mypockethealth.ClientFragments.SymptomCheckerSection;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.medical.mypockethealth.Classes.SymptomChecker.UserSymptomsDataModel;
import com.medical.mypockethealth.ClientFragments.HomeFragment;
import com.medical.mypockethealth.R;


public class SymptomPageOneFragment extends Fragment {

    private static final String TAG = "SymptomPageOneFragment";

    public static UserSymptomsDataModel userSymptomsDataModel;

    public SymptomPageOneFragment() {
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
        return inflater.inflate(R.layout.fragment_symptom_page_one, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                        new HomeFragment()).addToBackStack(null).commit();

            }
        });


        view.findViewById(R.id.my_self).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SymptomsResultScreenFragment.userSymptomsDataModel.setCheck_up_for("my_self");

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                        new SymptomCheckerTwoFragment()).addToBackStack(null).commit();

            }
        });


        view.findViewById(R.id.some_one_else).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SymptomsResultScreenFragment.userSymptomsDataModel.setCheck_up_for("some_one_else");

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                        new SymptomCheckerTwoFragment()).addToBackStack(null).commit();

            }
        });

        disableBottomNav();

    }


    private void disableBottomNav() {

        View view1 = requireActivity().findViewById(R.id.meowBottomNavigation);

        view1.setVisibility(View.GONE);

    }

}