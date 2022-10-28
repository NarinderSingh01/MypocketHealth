package com.medical.mypockethealth.ClientFragments.DoctorBookingSection;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.medical.mypockethealth.Classes.ProviderInformation.ProviderInformation;
import com.medical.mypockethealth.Classes.VideoChatInformation.VideoChatRequestInformation;
import com.medical.mypockethealth.ClientFragments.HomeFragment;
import com.medical.mypockethealth.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;


public class DoctorBookingProfileFragment extends Fragment {

    private static final String TAG = "DoctorBookingProfileFra";
    private VideoChatRequestInformation videoChatRequestInformation;
    public static final String data_key="data_key";

    public DoctorBookingProfileFragment() {
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
        return inflater.inflate(R.layout.fragment_doctor_booking_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setData(view);

        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                        new HomeFragment()).addToBackStack(null).commit();

            }
        });

        view.findViewById(R.id.calling).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(),"coming soon",Toast.LENGTH_SHORT).show();

            }
        });

        view.findViewById(R.id.video_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                activateMyState();

                Toast.makeText(getContext(),"video call",Toast.LENGTH_SHORT).show();

//                Intent intent=new Intent(getContext(),VideoChatViewActivity.class);
//                intent.putExtra(VideoChatViewActivity.data_key,videoChatRequestInformation);
//
//                startActivity(intent);


            }
        });
    }



    private void setData(View view)
    {

        ImageView profile_image;
        TextView name,speciality_field,experience,about_doctor,working_address;

        profile_image=view.findViewById(R.id.profile_image);
        name=view.findViewById(R.id.name);
        speciality_field=view.findViewById(R.id.speciality_field);
        experience=view.findViewById(R.id.experience);
        about_doctor=view.findViewById(R.id.about_doctor);
        working_address=view.findViewById(R.id.working_address);

        assert getArguments() != null;
        videoChatRequestInformation=(VideoChatRequestInformation) getArguments().get(DoctorBookingProfileFragment.data_key);

        ProviderInformation providerInformation= videoChatRequestInformation.getProviderInformation();

        String user="Dr. " + providerInformation.getFirstName()+" "+providerInformation.getSurName();

        name.setText(user);

        speciality_field.setText(providerInformation.getSpecialization());
        String experience_field=providerInformation.getExperience()+" Year exp";
         experience.setText(experience_field);
         about_doctor.setText(providerInformation.getBio());
         String address=providerInformation.getWorkLocation()+"\n"+providerInformation.getAddress();
         working_address.setText(address);
        Picasso.with(getContext()).load(Uri.parse(providerInformation.getProfileImage())).error(R.drawable.profile).into(profile_image);

    }


}