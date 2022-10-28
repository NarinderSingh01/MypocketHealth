package com.medical.mypockethealth.ClientFragments.SymptomCheckerSection;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.mypockethealth.Adaptors.UserSection.SymptomChecker.SpecialistsRecycleViewAdaptor;
import com.medical.mypockethealth.Classes.SymptomChecker.GetSpecialistCaller;
import com.medical.mypockethealth.Classes.SymptomChecker.SpecialistInformation;
import com.medical.mypockethealth.Classes.SymptomChecker.SymptomInformation;
import com.medical.mypockethealth.Classes.SymptomChecker.UserSymptomsDataModel;
import com.medical.mypockethealth.R;

import java.util.ArrayList;
import java.util.List;


public class SymptomsResultScreenFragment extends Fragment implements GetSpecialistCaller.CallBackFromGetSpecialistCaller
        , SpecialistsRecycleViewAdaptor.callBackFromSymptomRecycleViewAdaptor {

    private static final String TAG = "SymptomList";
    private Dialog loading_box;
    public static UserSymptomsDataModel userSymptomsDataModel = new UserSymptomsDataModel();
    private SpecialistsRecycleViewAdaptor specialistsRecycleViewAdaptor;
    private RecyclerView recycler_specialists;
    private String specialistId = null;
    private ImageView not_found, backButton;
    private LinearLayout lay_main;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_symptoms_result_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recycler_specialists = view.findViewById(R.id.recycler_specialists);
        not_found = view.findViewById(R.id.not_found);
        backButton = view.findViewById(R.id.backButton);
        lay_main = view.findViewById(R.id.lay_main);

        backButton.setOnClickListener(v -> {

            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                    new SymptomCheckerFiveFragment()).addToBackStack(null).commit();

        });

        specialistsRecycleViewAdaptor = new SpecialistsRecycleViewAdaptor(new ArrayList<>(),
                SymptomsResultScreenFragment.this);

        recycler_specialists.setLayoutManager(new LinearLayoutManager(getContext()));

        recycler_specialists.setAdapter(specialistsRecycleViewAdaptor);

        not_found.setVisibility(View.GONE);
        lay_main.setVisibility(View.GONE);

        getSpecialities();

    }

    private void getSpecialities() {

        SymptomInformation symptomInformation = new SymptomInformation();

        List<Integer> list = new ArrayList<>();

        list.add(userSymptomsDataModel.getSymptomsList().get(0).getID());

        symptomInformation.setSymptomId(list);
        symptomInformation.setGender(userSymptomsDataModel.getGender());
        symptomInformation.setDate("2000");

        openLoadingBox();

//        Toast.makeText(requireContext(), "" + startDate, Toast.LENGTH_SHORT).show();

        new Thread(new GetSpecialistCaller(symptomInformation, SymptomsResultScreenFragment.this)).start();

    }

    private void openLoadingBox() {

        loading_box = new Dialog(getContext());
        loading_box.setContentView(R.layout.loading_layout);
        loading_box.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading_box.setCanceledOnTouchOutside(false);
        loading_box.setCancelable(false);
        Window window = loading_box.getWindow();
        window.setGravity(Gravity.CENTER);
        loading_box.show();

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void symptomListGetSpecialistCaller(List<SpecialistInformation> specialisation) {

        loading_box.dismiss();

        if (specialisation != null && specialisation.size() != 0) {

            specialistsRecycleViewAdaptor.loadData(specialisation);

            not_found.setVisibility(View.GONE);
            lay_main.setVisibility(View.VISIBLE);

        } else {

            not_found.setVisibility(View.VISIBLE);
            lay_main.setVisibility(View.GONE);

            specialistsRecycleViewAdaptor.loadData(new ArrayList<>());

            Toast.makeText(requireContext(), "No specialist found", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onClick(SpecialistInformation specialistInformation) {

        specialistId = String.valueOf(specialistInformation.getID());

        if (specialistId != null) {

            DoctorsListScreenFragment.specialistId = specialistId;

            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                    new DoctorsListScreenFragment()).addToBackStack(null).commit();

        } else {

            Toast.makeText(requireContext(), "unable to get doctors right now", Toast.LENGTH_SHORT).show();

        }

    }

}