
package com.medical.mypockethealth.ClientFragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.medical.mypockethealth.ApplicationBase.BaseActivity;
import com.medical.mypockethealth.Classes.ClientInformation.ClientInformation;
import com.medical.mypockethealth.Classes.DialogShower;
import com.medical.mypockethealth.Classes.NetworkSection.NetworkStatus;
import com.medical.mypockethealth.Classes.NotificationSection.ActivationStateInformation;
import com.medical.mypockethealth.Classes.NotificationSection.NotificationInformation;
import com.medical.mypockethealth.Classes.ProviderInformation.ProviderInformation;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.Classes.URLBuilder;
import com.medical.mypockethealth.Classes.VideoChatInformation.VideoChatRequestInformation;
import com.medical.mypockethealth.ClientActivity.ClientMainFrame;
import com.medical.mypockethealth.ClientFragments.BottomNavigationSection.SettingsSection.ProfileSection.ViewProfileFragment;
import com.medical.mypockethealth.ClientFragments.DoctorBookingSection.DoctorBookingOneFragment;
import com.medical.mypockethealth.ClientFragments.EHRSection.EHROneFragment;
import com.medical.mypockethealth.ClientFragments.NotificationSection.NotificationFragment;
import com.medical.mypockethealth.ClientFragments.SymptomCheckerSection.SymptomPageOneFragment;
import com.medical.mypockethealth.ClientFragments.WalletSection.WalletUserFragment;
import com.medical.mypockethealth.ProviderActivity.ProviderMainFrame;
import com.medical.mypockethealth.ProviderFragments.ProviderHomeFragment;
import com.medical.mypockethealth.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.medical.mypockethealth.Threads.BaseThreads.UserSection.GetMedicalStatusCaller;
import com.medical.mypockethealth.Threads.ProviderSection.AccountSection.AccountActivationStatusCaller;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment implements AccountActivationStatusCaller.CallbackFromAccountActivationStatusCaller,
        GetMedicalStatusCaller.CallbackFromGetMedicalStatusCaller {

    private static final String TAG = "HomeFragment";

    public static int mode = 0;
    private final Handler handler = new Handler();
    private BottomSheetDialog bottomSheetDialog;
    private TextView notification_sign;
    public static ActivationStateInformation activationStateInformation;

    public HomeFragment() {
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setData(view);

        checkAccountActivationStatus();

        getMedicalStatus();

        getNotifications();

        enableBottomNav();

        view.findViewById(R.id.symptom_checker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(getContext(), "Coming soon", Toast.LENGTH_SHORT).show();

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout
                        , new SymptomPageOneFragment()).addToBackStack(null).commit();
            }
        });

        view.findViewById(R.id.notification_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout
                        , new NotificationFragment()).addToBackStack(null).commit();

            }
        });

        view.findViewById(R.id.consult_doctor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!getUserInformation().getProfileStatus().equals(ResponseInformation.success_response_type)) {

                    bottomSheetHandler();

                } else {

                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout
                            , new DoctorBookingOneFragment()).addToBackStack(null).commit();

                }

            }
        });

        view.findViewById(R.id.wallet_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout
//                        ,new WalletUserFragment()).addToBackStack(null).commit();

                Toast.makeText(getContext(), "Coming soon", Toast.LENGTH_SHORT).show();

            }
        });

        view.findViewById(R.id.mental_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), "Coming soon", Toast.LENGTH_SHORT).show();

            }
        });
        view.findViewById(R.id.woman_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), "Coming soon", Toast.LENGTH_SHORT).show();

            }
        });
        view.findViewById(R.id.allied_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), "Coming soon", Toast.LENGTH_SHORT).show();

            }
        });
        view.findViewById(R.id.pharmacy_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), "Coming soon", Toast.LENGTH_SHORT).show();

            }
        });
        view.findViewById(R.id.covid_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), "Coming soon", Toast.LENGTH_SHORT).show();

            }
        });
//
//        view.findViewById(R.id.pharmacy_layout).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Toast.makeText(getContext(),"coming soon",Toast.LENGTH_SHORT).show();
//
////                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout
////                        ,new EPharmacyOneFragment()).addToBackStack(null).commit();
//            }
//        });
//
        view.findViewById(R.id.ehr_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!getUserInformation().getProfileStatus().equals(ResponseInformation.success_response_type)) {
                    bottomSheetHandler();
                } else {
                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout
                            , new EHROneFragment()).addToBackStack(null).commit();
                }

            }
        });


    }


    private void getNotifications() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference(URLBuilder.FirebaseDataNodes.Notification).child(ClientMainFrame.id).child(URLBuilder.FirebaseDataNodes.activationStatus);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if (snapshot.exists()) {
                    Fragment currentFragment = requireActivity().getSupportFragmentManager().findFragmentById(R.id.main_frame_layout);

                    if (currentFragment instanceof HomeFragment) {

                        for (DataSnapshot child : snapshot.getChildren()) {

                            activationStateInformation = child.getValue(ActivationStateInformation.class);

                            break;

                        }

                        assert activationStateInformation != null;

                        if (activationStateInformation.getState().equals("0")) {
                            notification_sign.setVisibility(View.VISIBLE);
                        } else {
                            notification_sign.setVisibility(View.INVISIBLE);
                        }

                    }


                } else {
                    Fragment currentFragment = requireActivity().getSupportFragmentManager().findFragmentById(R.id.main_frame_layout);

                    if (currentFragment instanceof HomeFragment) {

                        // exception part
                    }


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });


    }

    private void getMedicalStatus() {

        new Thread(new GetMedicalStatusCaller(HomeFragment.this)).start();

    }

    private void checkAccountActivationStatus() {

        if (getUserInformation().getProfileStatus().equals(ResponseInformation.fail_response_type)) {

            if (!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED)) {

                new Thread(new AccountActivationStatusCaller(ClientMainFrame.id, HomeFragment.this)).start();
            } else {


                DialogShower dialogShower = new DialogShower(R.layout.internet_error, getContext());
                dialogShower.showDialog();
            }

        } else if (getUserInformation().getProfileStatus().equals(ResponseInformation.success_response_type)) {
            // section use when profile status is 1
        }

    }


    private ClientInformation getUserInformation() {
        SharedPreferences preferences = requireContext().getSharedPreferences(ClientMainFrame.client_information_file, Context.MODE_PRIVATE);
        String json = preferences.getString(ClientMainFrame.client_information_key, "");
        Gson gson = new Gson();
        return gson.fromJson(json, ClientInformation.class);
    }


    private void setData(View view) {

        ImageView profile_image;
        TextView firstName, lastName, day_type;

        profile_image = view.findViewById(R.id.profile_image);
        firstName = view.findViewById(R.id.firstName);
        lastName = view.findViewById(R.id.lastName);
        day_type = view.findViewById(R.id.day_type);
        notification_sign = view.findViewById(R.id.notification_sign);

        day_type.setText(getDayStatus());

        SharedPreferences preferences = requireContext().getSharedPreferences(ClientMainFrame.client_information_file, Context.MODE_PRIVATE);
        String json = preferences.getString(ClientMainFrame.client_information_key, "");
        Gson gson = new Gson();
        ClientInformation clientInformation = gson.fromJson(json, ClientInformation.class);

        firstName.setText(clientInformation.getFirstName());
        lastName.setText(clientInformation.getSurName());

        if (clientInformation.getProfileImage().length() != 0) {
            Picasso.with(getContext()).load(Uri.parse(clientInformation.getProfileImage())).error(R.drawable.ic_user).into(profile_image);
        }

    }


    private String getDayStatus() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("HH");
        Date date = new Date();

        int value = Integer.parseInt(formatter.format(date));

        if (value >= 1 && value <= 12) {
            return "Good Morning";
        } else if (value >= 12 && value <= 16) {
            return "Good Afternoon";
        }
        return "Good Evening";
    }

    private void enableBottomNavigation() {

//        BottomNavigationView view1 = requireActivity().findViewById(R.id.bottom_navigation_view);
//
//        view1.setVisibility(View.VISIBLE);

    }


    @Override
    public void confirmationAccountActivationStatusCaller(ResponseInformation responseInformation) {

        handler.post(new Runnable() {
            @Override
            public void run() {


                if (responseInformation.getSuccess().equals(String.valueOf(ResponseInformation.fail_response_type))) {

                    Toast.makeText(getContext(), responseInformation.getMessage(), Toast.LENGTH_SHORT).show();

                } else {

                    if (!responseInformation.getProfileStatus().equals(ResponseInformation.success_response_type)) {
                        bottomSheetHandler();
                    } else {
                        updateInformation();
                        confirmationBottomSheetHandler();
                    }


                }
            }
        });

    }

    private void confirmationBottomSheetHandler() {

        handler.post(new Runnable() {

            @Override
            public void run() {

                bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme);

                View bottom_view = LayoutInflater.from(getContext()).inflate(R.layout.account_confirmation_layout
                        ,
                        (RelativeLayout) bottomSheetDialog.findViewById(R.id.relative_layout));

                bottomSheetDialog.setContentView(bottom_view);

                bottomSheetDialog.show();

                TextView userName = bottomSheetDialog.findViewById(R.id.user_name);

                assert userName != null;

                String name = getUserInformation().getFirstName() + " " + getUserInformation().getSurName();
                userName.setText(name);

            }
        });


    }

    private void updateInformation() {
        SharedPreferences preferences = requireContext().getSharedPreferences(ClientMainFrame.client_information_file, Context.MODE_PRIVATE);
        String json = preferences.getString(ClientMainFrame.client_information_key, "");
        Gson gson = new Gson();
        ClientInformation clientInformation = gson.fromJson(json, ClientInformation.class);
        clientInformation.setProfileStatus(ResponseInformation.success_response_type);

        SharedPreferences.Editor editor = preferences.edit();

        editor.clear();
        editor.apply();

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(ClientMainFrame.client_information_file, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor_one = sharedPreferences.edit();

        Gson file = new Gson();

        String json_one = file.toJson(clientInformation);

        editor_one.putString(ClientMainFrame.client_information_key, json_one);

        editor_one.apply();


    }

    private void bottomSheetHandler() {
        handler.post(new Runnable() {

            @Override
            public void run() {

                bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme);

                View bottom_view = LayoutInflater.from(getContext()).inflate(R.layout.user_account_activation_layout
                        ,
                        (RelativeLayout) bottomSheetDialog.findViewById(R.id.relative_layout));

                bottomSheetDialog.setContentView(bottom_view);

                bottomSheetDialog.show();

                TextView userName = bottomSheetDialog.findViewById(R.id.user_name);

                assert userName != null;

                String name = getUserInformation().getFirstName() + " " + getUserInformation().getSurName();
                userName.setText(name);

            }
        });


    }

    @Override
    public void confirmation(ResponseInformation responseInformation) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                if (responseInformation.getSuccess().equals(ResponseInformation.fail_response_type)) {

                    Toast.makeText(getContext(), responseInformation.getMessage(), Toast.LENGTH_SHORT).show();

                }

            }
        });


    }

    @Override
    public void information(ClientInformation clientInformation) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                updateDataIntoSharedPreferencesAsUser(clientInformation);

            }
        });


    }

    private void updateDataIntoSharedPreferencesAsUser(ClientInformation clientInformation) {

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(ClientMainFrame.client_information_file, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        editor.apply();

        Gson gson = new Gson();

        SharedPreferences sharedPreferences3 = requireContext().getSharedPreferences(ClientMainFrame.client_information_file, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor2 = sharedPreferences3.edit();

        String json1 = gson.toJson(clientInformation);

        editor2.putString(ClientMainFrame.client_information_key, json1);

        editor2.apply();

    }

    private void enableBottomNav() {

        View view1 = requireActivity().findViewById(R.id.meowBottomNavigation);

        view1.setVisibility(View.VISIBLE);

    }

}