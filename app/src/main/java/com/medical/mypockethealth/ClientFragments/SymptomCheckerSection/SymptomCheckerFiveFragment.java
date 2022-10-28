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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.medical.mypockethealth.Classes.SymptomChecker.SymptomInformation;
import com.medical.mypockethealth.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class SymptomCheckerFiveFragment extends Fragment {

    public static List<SymptomInformation> symptoms_added_list = new ArrayList<>();
    private ChipGroup chipGroup;

    public SymptomCheckerFiveFragment() {
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
        return inflater.inflate(R.layout.fragment_symptom_checker_five, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chipGroup = view.findViewById(R.id.chipGroup);

        onCLicks(view);

    }

    @Override
    public void onResume() {
        super.onResume();

        if (chipGroup.getChildCount() < 1) {

            if (symptoms_added_list != null && symptoms_added_list.size() != 0) {

                create_symptom_views();

            }

        }

//        if (symptoms_added_list != null && symptoms_added_list.size() != 0) {
//
//            create_symptom_views();
//
//        }

    }

    private void create_symptom_views() {

        for (int i = 0; i < symptoms_added_list.size(); i++) {

            Chip chip = new Chip(requireActivity());
            ChipDrawable drawable = ChipDrawable.createFromAttributes(requireActivity(), null, 0, R.style.Widget_MaterialComponents_Chip_Entry);
            chip.setChipDrawable(drawable);
            chip.setCheckable(false);
            chip.setClickable(false);
            chip.setTextColor(requireActivity().getResources().getColor(R.color.white));
            chip.setChipBackgroundColor(getResources().getColorStateList(R.color.app_theme, null));
            chip.setCloseIconTint(getResources().getColorStateList(R.color.white, null));
            chip.setIconStartPadding(3f);
            chip.setPadding(60, 20, 60, 20);
            chip.setText(symptoms_added_list.get(i).getName());
            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Dialog warning_box = new Dialog(getContext());
                    warning_box.setContentView(R.layout.remove_symptom_pop_up_box);
                    warning_box.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    Window window = warning_box.getWindow();
                    warning_box.setCanceledOnTouchOutside(false);
                    window.setGravity(Gravity.CENTER);
                    warning_box.show();

                    Button yesButton, noBtn;

                    yesButton = warning_box.findViewById(R.id.yes_btn);
                    noBtn = warning_box.findViewById(R.id.no_btn);

                    String accept = "Yes";
                    yesButton.setText(accept);
                    yesButton.setTextColor(Color.WHITE);

                    String cancel = "No";
                    noBtn.setText(cancel);
                    noBtn.setTextColor(Color.WHITE);

                    yesButton.setOnClickListener(view2 -> {

                        chipGroup.removeView(chip);

                        removeChip(chip);

                        warning_box.dismiss();

                    });

                    noBtn.setOnClickListener(view2 -> {

                        warning_box.dismiss();

                    });

                }
            });

            chipGroup.addView(chip);

        }

    }

    private void removeChip(Chip chip) {

        for (int i = 0; i < symptoms_added_list.size(); i++) {

            if (symptoms_added_list.get(i).getName().equalsIgnoreCase(chip.getText().toString().trim())) {

                symptoms_added_list.remove(symptoms_added_list.get(i));

            } else {

                Log.d(TAG, "removeChip: doesn't exist");

            }

        }

    }

    private void onCLicks(View view) {

        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                symptoms_added_list.clear();

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                        new SymptomCheckerFourFragment()).addToBackStack(null).commit();
            }
        });

        view.findViewById(R.id.add_symptoms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                        new SymptomCheckerSixFragment()).addToBackStack(null).commit();

            }
        });

        view.findViewById(R.id.btn_next_symptoms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (symptoms_added_list != null && symptoms_added_list.size() == 0) {

                    Toast.makeText(requireContext(), "please add Symptom(s)", Toast.LENGTH_SHORT).show();

                } else {

                    SymptomsResultScreenFragment.userSymptomsDataModel.setSymptomsList(symptoms_added_list);

                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                            new SymptomsResultScreenFragment()).addToBackStack(null).commit();

                }
            }
        });

    }


}