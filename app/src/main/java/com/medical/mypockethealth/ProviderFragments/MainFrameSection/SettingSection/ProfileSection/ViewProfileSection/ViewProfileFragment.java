package com.medical.mypockethealth.ProviderFragments.MainFrameSection.SettingSection.ProfileSection.ViewProfileSection;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.medical.mypockethealth.Classes.ProviderInformation.ProviderInformation;
import com.medical.mypockethealth.ProviderActivity.ProviderMainFrame;
import com.medical.mypockethealth.ProviderFragments.MainFrameSection.SettingSection.SettingProviderFragment;
import com.medical.mypockethealth.R;
import com.squareup.picasso.Picasso;


public class ViewProfileFragment extends Fragment {

    private static final String TAG = "ViewProfileFragment";


    public enum ImageStatus {
        notSelected, selected
    }


    public ViewProfileFragment() {
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
        return inflater.inflate(R.layout.fragment_view_profile, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        setData(view);


        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                        new SettingProviderFragment()).addToBackStack(null).commit();
            }
        });


    }

    private void setData(View view) {
        TextView firstName, surName, phoneNumber, email,
                address, city, postal_code, registration_number,
                practice_number, hospital_name, experience, specialist, consult_charges;

        ImageView profile_image;

        firstName = view.findViewById(R.id.firstName);
        surName = view.findViewById(R.id.surName);
        phoneNumber = view.findViewById(R.id.phoneNumber);
        profile_image = view.findViewById(R.id.profile_image);
        email = view.findViewById(R.id.email);
        address = view.findViewById(R.id.address);
        city = view.findViewById(R.id.city);
        postal_code = view.findViewById(R.id.postal_code);
        registration_number = view.findViewById(R.id.registration_number);
        practice_number = view.findViewById(R.id.practice_number);
        hospital_name = view.findViewById(R.id.hospital_name);
        experience = view.findViewById(R.id.experience);
        specialist = view.findViewById(R.id.specialist);
        consult_charges = view.findViewById(R.id.consult_charges);


        SharedPreferences preferences = requireContext().getSharedPreferences(ProviderMainFrame.provider_information_file, Context.MODE_PRIVATE);
        String json = preferences.getString(ProviderMainFrame.provider_information_key, "");
        Gson gson = new Gson();
        ProviderInformation providerInformation = gson.fromJson(json, ProviderInformation.class);

        String name;

        if (providerInformation.getUserTitle() != null && providerInformation.getUserTitle().length() != 0) {

            name = providerInformation.getUserTitle() + " " + providerInformation.getFirstName();

        } else {

            name = providerInformation.getFirstName();

        }

        firstName.setText(name);

        surName.setText(providerInformation.getSurName());
        phoneNumber.setText(providerInformation.getPhoneNumber());
        email.setText(providerInformation.getEmail());
        address.setText(providerInformation.getAddress());
        city.setText(providerInformation.getCity());
        postal_code.setText(providerInformation.getPostalCode());
        registration_number.setText(providerInformation.getProfessionalRegistrationNumber());

        if (!(providerInformation.getDepartment().equals(getString(R.string.None)))) {
            practice_number.setText(providerInformation.getDepartment());
        } else {
            practice_number.setText(getString(R.string.None));
        }

        if (!(providerInformation.getWorkLocation().equals(getString(R.string.None)))) {
            hospital_name.setText(providerInformation.getWorkLocation());
        } else {
            hospital_name.setText(getString(R.string.None));
        }

        experience.setText(providerInformation.getExperience());
        specialist.setText(providerInformation.getSpecialization());
        consult_charges.setText(providerInformation.getConsultCharges());

        Picasso.with(getContext()).load(Uri.parse(providerInformation.getProfileImage())).error(R.drawable.profile).into(profile_image);
    }
}