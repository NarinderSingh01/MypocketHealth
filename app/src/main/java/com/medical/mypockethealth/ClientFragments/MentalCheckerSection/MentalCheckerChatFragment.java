package com.medical.mypockethealth.ClientFragments.MentalCheckerSection;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.medical.mypockethealth.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MentalCheckerChatFragment extends Fragment {


    public MentalCheckerChatFragment() {
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
        return inflater.inflate(R.layout.fragment_mental_checker_chat, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        disableBottomNavigation();

        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                        new MentalCheckerOneFragment()).addToBackStack(null).commit();

                enableBottomNavigation();

            }
        });

    }


    private void enableBottomNavigation()
    {

        MeowBottomNavigation view1 = getActivity().findViewById(R.id.meowBottomNavigation);

        view1.setVisibility(View.VISIBLE);

    }

    private void disableBottomNavigation()
    {

        MeowBottomNavigation view1 = getActivity().findViewById(R.id.meowBottomNavigation);

        view1.setVisibility(View.GONE);

    }
}