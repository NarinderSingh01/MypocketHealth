package com.medical.mypockethealth.ClientFragments.SymptomCheckerSection;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.medical.mypockethealth.R;


public class SymptomPageThreeFragment extends Fragment {

    private static final String TAG = "SymptomPageThreeFragmen";

    public SymptomPageThreeFragment() {
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
        return inflater.inflate(R.layout.fragment_symptom_page_three, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        establishViews(view);

        EditText editText_age = view.findViewById(R.id.editText_age);

        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                        new SymptomCheckerTwoFragment()).addToBackStack(null).commit();
            }
        });

        view.findViewById(R.id.next_age).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editText_age.getText().toString().trim().length() == 0) {

                    Toast.makeText(requireContext(), "please enter age", Toast.LENGTH_SHORT).show();

                } else {

                    SymptomsResultScreenFragment.userSymptomsDataModel.setAge(editText_age.getText().toString().trim());

                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                            new SymptomCheckerFourFragment()).addToBackStack(null).commit();

                }
            }
        });


    }


//    private void establishViews(View view)
//    {
//
//
//    }
}