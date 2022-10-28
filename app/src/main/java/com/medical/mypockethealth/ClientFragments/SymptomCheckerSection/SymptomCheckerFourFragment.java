package com.medical.mypockethealth.ClientFragments.SymptomCheckerSection;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import com.medical.mypockethealth.R;

import static android.content.ContentValues.TAG;


public class SymptomCheckerFourFragment extends Fragment {

    private int over_weight_or_obese_status = 0; // 1 for yes , 2 for no , 3 for don't know
    private int smoke_cigarette = 0; // 1 for yes , 2 for no , 3 for don't know
    private int suffered_from_injury = 0; // 1 for yes , 2 for no , 3 for don't know
    private int high_cholesterol = 0; // 1 for yes , 2 for no , 3 for don't know
    private int hypertension = 0; // 1 for yes , 2 for no , 3 for don't know

    private RadioButton tension_yes, tension_no, tension_not_know, cholesterol_yes, cholesterol_no, cholesterol_not_know,
            injury_yes, injury_no, injury_not_know, smoking_yes, smoking_no, smoking_not_know, weight_yes, weight_no, weight_not_know;

    public SymptomCheckerFourFragment() {
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
        return inflater.inflate(R.layout.fragment_symptom_checker_four, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findIds(view);

        selectionsSet();

        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                        new SymptomPageThreeFragment()).addToBackStack(null).commit();
            }
        });

        view.findViewById(R.id.next_statements).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (over_weight_or_obese_status == 0 || smoke_cigarette == 0 || suffered_from_injury == 0 || high_cholesterol == 0 || hypertension == 0) {

                    Toast.makeText(requireContext(), "Selection of at-least one from each option is mandatory", Toast.LENGTH_SHORT).show();

                } else {

                    SymptomsResultScreenFragment.userSymptomsDataModel.setOverweight_or_obese(String.valueOf(over_weight_or_obese_status));
                    SymptomsResultScreenFragment.userSymptomsDataModel.setSmoke_cigarettes(String.valueOf(smoke_cigarette));
                    SymptomsResultScreenFragment.userSymptomsDataModel.setSuffered_an_injury(String.valueOf(suffered_from_injury));
                    SymptomsResultScreenFragment.userSymptomsDataModel.setHigh_cholesterol(String.valueOf(high_cholesterol));
                    SymptomsResultScreenFragment.userSymptomsDataModel.setHypertension(String.valueOf(hypertension));

                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                            new SymptomCheckerFiveFragment()).addToBackStack(null).commit();

                }
            }
        });
    }

    private void getSelections() {

//        Log.d(TAG, "getSelections: " + SymptomCheckerThirteenFragment.userSymptomsDataModel.getCheck_up_for());
//        Log.d(TAG, "getSelections: " + SymptomCheckerThirteenFragment.userSymptomsDataModel.getGender());
//        Log.d(TAG, "getSelections: " + SymptomCheckerThirteenFragment.userSymptomsDataModel.getAge());
//
//        Log.d(TAG, "getSelections:1 " + over_weight_or_obese_status);
//        Log.d(TAG, "getSelections:2 " + smoke_cigarette);
//        Log.d(TAG, "getSelections:3 " + suffered_from_injury);
//        Log.d(TAG, "getSelections:4 " + high_cholesterol);
//        Log.d(TAG, "getSelections:5 " + hypertension);


    }

    private void findIds(View view) {

        tension_yes = view.findViewById(R.id.tension_yes);
        tension_no = view.findViewById(R.id.tension_no);
        tension_not_know = view.findViewById(R.id.tension_not_know);
        cholesterol_yes = view.findViewById(R.id.cholesterol_yes);
        cholesterol_no = view.findViewById(R.id.cholesterol_no);
        cholesterol_not_know = view.findViewById(R.id.cholesterol_not_know);
        injury_yes = view.findViewById(R.id.injury_yes);
        injury_no = view.findViewById(R.id.injury_no);
        injury_not_know = view.findViewById(R.id.injury_not_know);
        smoking_yes = view.findViewById(R.id.smoking_yes);
        smoking_no = view.findViewById(R.id.smoking_no);
        smoking_not_know = view.findViewById(R.id.smoking_not_know);
        weight_yes = view.findViewById(R.id.weight_yes);
        weight_no = view.findViewById(R.id.weight_no);
        weight_not_know = view.findViewById(R.id.weight_not_know);

    }

    private void selectionsSet() {

        weight_yes.setOnClickListener(v -> {

            weight_yes.setChecked(true);
            weight_no.setChecked(false);
            weight_not_know.setChecked(false);

            over_weight_or_obese_status = 1;

        });

        weight_no.setOnClickListener(v -> {

            weight_yes.setChecked(false);
            weight_no.setChecked(true);
            weight_not_know.setChecked(false);

            over_weight_or_obese_status = 2;

        });

        weight_not_know.setOnClickListener(v -> {

            weight_yes.setChecked(false);
            weight_no.setChecked(false);
            weight_not_know.setChecked(true);

            over_weight_or_obese_status = 3;

        });

        smoking_yes.setOnClickListener(v -> {

            smoking_yes.setChecked(true);
            smoking_no.setChecked(false);
            smoking_not_know.setChecked(false);

            smoke_cigarette = 1;

        });

        smoking_no.setOnClickListener(v -> {

            smoking_yes.setChecked(false);
            smoking_no.setChecked(true);
            smoking_not_know.setChecked(false);

            smoke_cigarette = 2;

        });

        smoking_not_know.setOnClickListener(v -> {

            smoking_yes.setChecked(false);
            smoking_no.setChecked(false);
            smoking_not_know.setChecked(true);

            smoke_cigarette = 3;

        });

        injury_yes.setOnClickListener(v -> {

            injury_yes.setChecked(true);
            injury_no.setChecked(false);
            injury_not_know.setChecked(false);

            suffered_from_injury = 1;

        });

        injury_no.setOnClickListener(v -> {

            injury_yes.setChecked(false);
            injury_no.setChecked(true);
            injury_not_know.setChecked(false);

            suffered_from_injury = 2;

        });

        injury_not_know.setOnClickListener(v -> {

            injury_yes.setChecked(false);
            injury_no.setChecked(false);
            injury_not_know.setChecked(true);

            suffered_from_injury = 3;

        });

        cholesterol_yes.setOnClickListener(v -> {

            cholesterol_yes.setChecked(true);
            cholesterol_no.setChecked(false);
            cholesterol_not_know.setChecked(false);

            high_cholesterol = 1;

        });

        cholesterol_no.setOnClickListener(v -> {

            cholesterol_yes.setChecked(false);
            cholesterol_no.setChecked(true);
            cholesterol_not_know.setChecked(false);

            high_cholesterol = 2;

        });

        cholesterol_not_know.setOnClickListener(v -> {

            cholesterol_yes.setChecked(false);
            cholesterol_no.setChecked(false);
            cholesterol_not_know.setChecked(true);

            high_cholesterol = 3;

        });

        tension_yes.setOnClickListener(v -> {

            tension_yes.setChecked(true);
            tension_no.setChecked(false);
            tension_not_know.setChecked(false);

            hypertension = 1;

        });

        tension_no.setOnClickListener(v -> {

            tension_yes.setChecked(false);
            tension_no.setChecked(true);
            tension_not_know.setChecked(false);

            hypertension = 2;

        });

        tension_not_know.setOnClickListener(v -> {

            tension_yes.setChecked(false);
            tension_no.setChecked(false);
            tension_not_know.setChecked(true);

            hypertension = 3;

        });

    }


}