package com.medical.mypockethealth.ClientFragments.SymptomCheckerSection;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.medical.mypockethealth.ClientFragments.SymptomCheckerSection.BodySection.MaleSection.MaleFrontBodyFragment;
import com.medical.mypockethealth.R;

public class PointOnBodyFragment extends Fragment {

    private static final String TAG = "PointOnBodyFragment";

    public PointOnBodyFragment() {
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
        return inflater.inflate(R.layout.fragment_point_on_body, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        if (isVisible()){
//
//            Toast.makeText(requireContext(), "coming soon", Toast.LENGTH_SHORT).show();
//
//        }

//        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.body_holder,
//                new MaleFrontBodyFragment()).addToBackStack(null).commit();

    }
}