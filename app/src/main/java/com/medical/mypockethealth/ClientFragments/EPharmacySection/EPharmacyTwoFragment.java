package com.medical.mypockethealth.ClientFragments.EPharmacySection;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.medical.mypockethealth.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class EPharmacyTwoFragment extends Fragment {


    public EPharmacyTwoFragment() {
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
        return inflater.inflate(R.layout.fragment_e_pharmacy_two, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                        new EPharmacyOneFragment()).addToBackStack(null).commit();
            }
        });

        view.findViewById(R.id.buy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetHandler();
            }
        });
    }




    private void bottomSheetHandler()
    {
        BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(requireContext(),R.style.BottomSheetDialogTheme);

        View bottom_view= LayoutInflater.from(getContext()).inflate(R.layout.add_chat_layout,
                (RelativeLayout) bottomSheetDialog.findViewById(R.id.relative_layout));

        bottomSheetDialog.setContentView(bottom_view);

        bottomSheetDialog.show();

        bottom_view.findViewById(R.id.view_cart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                bottomSheetDialog.cancel();

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                        new EPharmacyThreeFragment()).addToBackStack(null).commit();

            }
        });


    }




}