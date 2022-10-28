package com.medical.mypockethealth.ClientFragments.MentalCheckerSection;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.medical.mypockethealth.ClientFragments.HomeFragment;
import com.medical.mypockethealth.R;

public class MentalCheckerOneFragment extends Fragment {


    private static final String TAG = "MentalCheckerOneFragmen";

    private ImageView yes,no;

    private RelativeLayout yes_layout,no_layout,dont_know;

    private Button signUp;

    public MentalCheckerOneFragment() {
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
        return inflater.inflate(R.layout.fragment_mental_checker_one, container, false);
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


        view.findViewById(R.id.basic_therapy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                        new MentalCategoryFragment()).addToBackStack(null).commit();

            }
        });




    }



}