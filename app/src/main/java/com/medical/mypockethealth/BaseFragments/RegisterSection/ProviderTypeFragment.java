package com.medical.mypockethealth.BaseFragments.RegisterSection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.medical.mypockethealth.BaseFragments.RegisterSection.ProviderRegistrationSection.SignUpAsProviderFragment;
import com.medical.mypockethealth.R;


public class ProviderTypeFragment extends Fragment {

    private Button doctor, nurse, physiotherapists;
    private LinearLayout upper_section;
    public static ProviderType providerType;
    public static String providerTypeString="";


    public enum ProviderType {
        doctor, nurse, physiotherapists
    }


    public ProviderTypeFragment() {
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
        return inflater.inflate(R.layout.fragment_provider_type, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        establishViews(view);

        animationApplier();

        doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                providerType = ProviderType.doctor;

                providerTypeString = "doctor";

                requireActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.frame_holder, new SignUpAsProviderFragment()).addToBackStack(null).commit();

            }
        });

        nurse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                providerType = ProviderType.nurse;

                providerTypeString = "nurse";

                requireActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.frame_holder, new SignUpAsProviderFragment()).addToBackStack(null).commit();

//                Toast.makeText(getContext(),"we will soon start this service",Toast.LENGTH_SHORT).show();

            }
        });

        physiotherapists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                providerType = ProviderType.physiotherapists;

                providerTypeString = "physiotherapists";

                requireActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.frame_holder, new SignUpAsProviderFragment()).addToBackStack(null).commit();

//                Toast.makeText(getContext(),"we will soon start this service",Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void animationApplier() {

        Animation top;

        top = AnimationUtils.loadAnimation(getContext(), R.anim.top_mover);

        upper_section.setAnimation(top);

        doctor.setTranslationY(800);
        nurse.setTranslationY(800);
        physiotherapists.setTranslationY(800);


        doctor.setAlpha(0);
        nurse.setAlpha(0);
        physiotherapists.setAlpha(0);


        doctor.animate().translationY(0).alpha(1).setDuration(600).setStartDelay(300).start();
        nurse.animate().translationY(0).alpha(1).setDuration(600).setStartDelay(400).start();
        physiotherapists.animate().translationY(0).alpha(1).setDuration(600).setStartDelay(500).start();

    }


    private void establishViews(View view) {

        doctor = view.findViewById(R.id.doctor);
        nurse = view.findViewById(R.id.nurse);
        physiotherapists = view.findViewById(R.id.physiotherapists);
        upper_section = view.findViewById(R.id.upper_section);

    }
}