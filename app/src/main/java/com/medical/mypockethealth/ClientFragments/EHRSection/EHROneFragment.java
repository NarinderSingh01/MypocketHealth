package com.medical.mypockethealth.ClientFragments.EHRSection;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.medical.mypockethealth.ApplicationBase.BaseActivity;
import com.medical.mypockethealth.Classes.DialogShower;
import com.medical.mypockethealth.Classes.EHRSection.EHRFilesInformation;
import com.medical.mypockethealth.Classes.NetworkSection.NetworkStatus;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.ClientActivity.ClientMainFrame;
import com.medical.mypockethealth.ClientFragments.BottomNavigationSection.BookingSection.CurrentBookingFragment;
import com.medical.mypockethealth.ClientFragments.EHRSection.FollowUpSection.FollowUpFragment;
import com.medical.mypockethealth.ClientFragments.EHRSection.PrescriptionSection.PrescriptionFragment;
import com.medical.mypockethealth.ClientFragments.EHRSection.ReferralSection.ReferralFragment;
import com.medical.mypockethealth.ClientFragments.EHRSection.SickNotes.SickFragment;
import com.medical.mypockethealth.ClientFragments.HomeFragment;
import com.medical.mypockethealth.R;
import com.medical.mypockethealth.Threads.UserSection.BookingSection.CurrentBookingDataFetcher;
import com.medical.mypockethealth.Threads.UserSection.EHRSection.EHRFileCaller;

import java.util.ArrayList;


public class EHROneFragment extends Fragment implements EHRFileCaller.CallbackFromEHRFileCaller{

    private static final String TAG = "EHROneFragment";

    private final Handler handler=new Handler();
    private LinearLayout ehr_layout;
    private ImageView loading,reload;

    public EHROneFragment() {
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
        return inflater.inflate(R.layout.fragment_e_h_r_one, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        establishViews(view);

        ehr_layout.setVisibility(View.GONE);

        getEHRDetails();

        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), ClientMainFrame.class));
            }
        });

        view.findViewById(R.id.follow_up_box).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                        new FollowUpFragment()).addToBackStack(null).commit();
            }
        });

        view.findViewById(R.id.prescription_box).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                        new PrescriptionFragment()).addToBackStack(null).commit();
                
            }
        });

        view.findViewById(R.id.sick_box).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                        new SickFragment()).addToBackStack(null).commit();
            }
        });

        view.findViewById(R.id.referral_box).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                        new ReferralFragment()).addToBackStack(null).commit();
               
            }
        });


        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getEHRDetails();

            }
        });


    }

    private void getEHRDetails()
    {
        if(!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED))
        {
            loading.setVisibility(View.VISIBLE);
            reload.setVisibility(View.GONE);


            new Thread(new EHRFileCaller(EHROneFragment.this, ClientMainFrame.id)).start();

        }

        else
        {
            reload.setVisibility(View.VISIBLE);
            loading.setVisibility(View.GONE);

            DialogShower dialogShower=new DialogShower(R.layout.internet_error,getContext());
            dialogShower.showDialog();
        }
    }

    private void establishViews(View view)
    {
        ehr_layout=view.findViewById(R.id.ehr_layout);
        loading=view.findViewById(R.id.loading);
        reload=view.findViewById(R.id.reload);
    }


    @Override
    public void confirmationEHRFileCaller(ResponseInformation responseInformation) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                if(responseInformation.getSuccess().equals(ResponseInformation.fail_response_type))
                {
                    loading.setVisibility(View.GONE);
                    reload.setVisibility(View.VISIBLE);

                }
            }
        });

    }

    @Override
    public void informationEHRFileCaller(EHRFilesInformation ehrFilesInformation) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                loading.setVisibility(View.GONE);
                reload.setVisibility(View.GONE);
                ehr_layout.setVisibility(View.VISIBLE);

                uploadEHRFilesInformation(ehrFilesInformation);

            }
        });

    }

    private void uploadEHRFilesInformation(EHRFilesInformation ehrFilesInformation)
    {
        TextView follow_up,prescription_note,sick_note,referral_note;

        follow_up=requireView().findViewById(R.id.follow_up);
        prescription_note=requireView().findViewById(R.id.prescription);
        sick_note=requireView().findViewById(R.id.sick_note);
        referral_note=requireView().findViewById(R.id.referral_note);

        String follow=ehrFilesInformation.getFollowUpSheets()+" files";
        String prescription=ehrFilesInformation.getPrescriptionSheets()+" files";
        String sick=ehrFilesInformation.getMedicalCertificateSheets()+" files";
        String referral=ehrFilesInformation.getReferralSheets()+" files";

        follow_up.setText(follow);
        prescription_note.setText(prescription);
        sick_note.setText(sick);
        referral_note.setText(referral);
    }
}