package com.medical.mypockethealth.BaseFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.medical.mypockethealth.ApplicationBase.BaseActivity;
import com.medical.mypockethealth.BaseFragments.RegisterSection.UserRegisterSection.UserInformationFragment;
import com.medical.mypockethealth.Classes.ClientInformation.ClientInformation;
import com.medical.mypockethealth.ClientActivity.ClientMainFrame;
import com.medical.mypockethealth.ProviderActivity.ProviderMainFrame;
import com.medical.mypockethealth.R;
import com.google.gson.Gson;


public class CongratulationsFragment extends Fragment {



    public CongratulationsFragment() {
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
        return inflater.inflate(R.layout.fragment_congratulations, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(BaseActivity.mode==0)
                {

                    requireActivity().getSupportFragmentManager().beginTransaction().
                            replace(R.id.frame_holder,new UserInformationFragment()).addToBackStack(null).commit();


                }
                else if(BaseActivity.mode==1)
                {
                    startActivity(new Intent(getContext(), ProviderMainFrame.class));
                }

            }
        });
    }


    private boolean getInformationStatus()
    {
        SharedPreferences sharedPreferences= requireContext().getSharedPreferences(ClientMainFrame.client_edit_information_file, Context.MODE_PRIVATE);
        String editData = sharedPreferences.getString(ClientMainFrame.client_edit_information_key, "");

        Gson gson = new Gson();
        ClientInformation clientInformation = gson.fromJson(editData,ClientInformation.class);

        return clientInformation.getJourneyInformationStatus().equals("0");
    }
}