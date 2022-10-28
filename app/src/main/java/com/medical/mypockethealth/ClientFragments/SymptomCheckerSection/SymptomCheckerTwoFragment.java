package com.medical.mypockethealth.ClientFragments.SymptomCheckerSection;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import com.medical.mypockethealth.R;


public class SymptomCheckerTwoFragment extends Fragment {

    private static final String TAG = "SymptomCheckerTwoFragme";

    private Dialog pop_up_box;

    public SymptomCheckerTwoFragment() {
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
        return inflater.inflate(R.layout.fragment_symptom_checker_two, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                        new SymptomPageOneFragment()).addToBackStack(null).commit();

            }
        });


        view.findViewById(R.id.male).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SymptomsResultScreenFragment.userSymptomsDataModel.setGender("Male");

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                        new SymptomPageThreeFragment()).addToBackStack(null).commit();

            }
        });


        view.findViewById(R.id.female).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SymptomsResultScreenFragment.userSymptomsDataModel.setGender("female");

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                        new SymptomPageThreeFragment()).addToBackStack(null).commit();

            }
        });

        view.findViewById(R.id.other).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 popUpBox();

            }
        });


    }

    private void popUpBox()
    {
        pop_up_box =new Dialog(getContext());
        pop_up_box.setContentView(R.layout.other_layout);
        pop_up_box.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop_up_box.setCanceledOnTouchOutside(true);
        Window window= pop_up_box.getWindow();
        window.setGravity(Gravity.CENTER);
        pop_up_box.show();

        EditText editText= pop_up_box.findViewById(R.id.about_text);

        pop_up_box.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "onClick: clicked");

                if(editText.getText().toString().length()==0)
                {

                    editText.setError("Please share your views with us");

                }

                else
                {

                    editText.setError(null);

                    pop_up_box.dismiss();

                    SymptomsResultScreenFragment.userSymptomsDataModel.setGender(editText.getText().toString().trim());

                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                        new SymptomPageThreeFragment()).addToBackStack(null).commit();

                }

            }
        });

    }

}