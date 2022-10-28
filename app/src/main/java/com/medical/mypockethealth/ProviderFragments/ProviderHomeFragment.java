package com.medical.mypockethealth.ProviderFragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.angads25.toggle.widget.LabeledSwitch;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.medical.mypockethealth.Adaptors.ProviderSection.AppointmentSection.RequestAppointmentRecycleViewAdaptor;
import com.medical.mypockethealth.ApplicationBase.BaseActivity;
import com.medical.mypockethealth.Classes.ClientInformation.PatientInformation;
import com.medical.mypockethealth.Classes.DialogShower;
import com.medical.mypockethealth.Classes.DocDetails;
import com.medical.mypockethealth.Classes.NetworkSection.NetworkStatus;
import com.medical.mypockethealth.Classes.NotificationSection.ActivationStateInformation;
import com.medical.mypockethealth.Classes.ProviderInformation.ProviderInformation;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.Classes.URLBuilder;
import com.medical.mypockethealth.MyPocketHealthRetrofit.MvpViewModel;
import com.medical.mypockethealth.ProviderActivity.ProviderMainFrame;
import com.medical.mypockethealth.ProviderFragments.MainFrameSection.AppointmentSection.AppointmentFragment;
import com.medical.mypockethealth.ProviderFragments.MainFrameSection.NotificationSection.ProviderNotificationFragment;
import com.medical.mypockethealth.ProviderFragments.MainFrameSection.ScheduleSection.SlotFragment;
import com.medical.mypockethealth.ProviderFragments.MainFrameSection.SettingSection.SettingProviderFragment;
import com.medical.mypockethealth.R;
import com.medical.mypockethealth.Threads.ProviderSection.AccountSection.AccountActivationStatusCaller;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class ProviderHomeFragment extends Fragment implements RequestAppointmentRecycleViewAdaptor.callBackFromRequestAppointmentRecycleViewAdaptor,
        AccountActivationStatusCaller.CallbackFromAccountActivationStatusCaller {

    private static final String TAG = "ProviderHomeFragment";

    private final Handler handler = new Handler();
    private final List<PatientInformation> patientInformationList = new ArrayList<>();
    private boolean load_status = false;
    private TextView notification_sign, receive_text;
    private BottomSheetDialog bottomSheetDialog;
    public static ActivationStateInformation activationStateInformation;
    private RequestAppointmentRecycleViewAdaptor requestAppointmentRecycleViewAdaptor;

    private List<DocDetails> list = new ArrayList<>();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("online_doctors");
    private LabeledSwitch toggle_switch;

    enum RequestType {
        profileImage, profileName, speciality
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

    private void updateInformation() {

        SharedPreferences preferences = requireContext().getSharedPreferences(ProviderMainFrame.provider_information_file, Context.MODE_PRIVATE);
        String json = preferences.getString(ProviderMainFrame.provider_information_key, "");
        Gson gson = new Gson();
        ProviderInformation providerInformation = gson.fromJson(json, ProviderInformation.class);
        providerInformation.setProfileStatus(ResponseInformation.success_response_type);

        SharedPreferences.Editor editor = preferences.edit();

        editor.clear();
        editor.apply();

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(ProviderMainFrame.provider_information_file, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor_one = sharedPreferences.edit();

        Gson file = new Gson();

        String json_one = file.toJson(providerInformation);

        editor_one.putString(ProviderMainFrame.provider_information_key, json_one);

        editor_one.apply();

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

                if (getProviderInformation().getUserTitle() != null && getProviderInformation().getUserTitle().length() != 0) {

                    String name = getProviderInformation().getUserTitle() + " " + getProviderInformation().getFirstName() + " " + getProviderInformation().getSurName();
                    userName.setText(name);

                } else {

                    String name = "Dr. " + getProviderInformation().getFirstName() + " " + getProviderInformation().getSurName();
                    userName.setText(name);

                }

            }
        });


    }

    private void bottomSheetHandler() {

        handler.post(new Runnable() {
            @Override
            public void run() {

                bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme);

                View bottom_view = LayoutInflater.from(getContext()).inflate(R.layout.account_warning_layout
                        ,
                        (RelativeLayout) bottomSheetDialog.findViewById(R.id.relative_layout));

                bottomSheetDialog.setContentView(bottom_view);

                bottomSheetDialog.show();

                TextView userName = bottomSheetDialog.findViewById(R.id.user_name);

                assert userName != null;

                if (getProviderInformation().getUserTitle() != null && getProviderInformation().getUserTitle().length() != 0) {

                    String name = getProviderInformation().getUserTitle() + " " + getProviderInformation().getFirstName() + " " + getProviderInformation().getSurName();
                    userName.setText(name);

                } else {

                    String name = "Dr. " + getProviderInformation().getFirstName() + " " + getProviderInformation().getSurName();
                    userName.setText(name);

                }

            }
        });

    }

    private ProviderInformation getProviderInformation() {
        SharedPreferences preferences = requireContext().getSharedPreferences(ProviderMainFrame.provider_information_file, Context.MODE_PRIVATE);
        String json = preferences.getString(ProviderMainFrame.provider_information_key, "");
        Gson gson = new Gson();
        return gson.fromJson(json, ProviderInformation.class);
    }

    public ProviderHomeFragment() {
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
        return inflater.inflate(R.layout.fragment_provider_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setData(view);

        getAppointmentRequest();

        checkAccountActivationStatus();

        getNotifications();

        view.findViewById(R.id.consult_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
//                        new ConsultOneFragment()).addToBackStack(null).commit();

                Toast.makeText(getContext(), "coming soon", Toast.LENGTH_SHORT).show();

            }
        });

        view.findViewById(R.id.today_bookings_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), "coming soon", Toast.LENGTH_SHORT).show();

            }
        });

        view.findViewById(R.id.pathology_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), "coming soon", Toast.LENGTH_SHORT).show();

            }
        });


        view.findViewById(R.id.notification_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                        new ProviderNotificationFragment()).addToBackStack(null).commit();

            }
        });

        view.findViewById(R.id.wallet_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), "coming soon", Toast.LENGTH_SHORT).show();

            }
        });


        view.findViewById(R.id.market_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), "coming soon", Toast.LENGTH_SHORT).show();

            }
        });
        view.findViewById(R.id.profile_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), "coming soon", Toast.LENGTH_SHORT).show();

            }
        });


        view.findViewById(R.id.setting_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                        new SettingProviderFragment()).addToBackStack(null).commit();
            }
        });

        view.findViewById(R.id.my_bookings_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                        new AppointmentFragment()).addToBackStack(null).commit();
            }
        });

        view.findViewById(R.id.schedule_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                        new SlotFragment()).addToBackStack(null).commit();
            }
        });


        toggle_switch.setOnClickListener(v -> {

            if (toggle_switch.isOn()) {

                databaseReference.child(getProviderInformation().getId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(@NonNull Void aVoid) {

                        if (isVisible()) {

                            Toast.makeText(requireContext(), "doc offline", Toast.LENGTH_SHORT).show();

                            receive_text.setText(R.string.im_un_available);

                            // api hit to make alert of minimum providers

                            notifyHealthAlert();

                        }

                    }
                });

            } else {

                DocDetails docDetails = new DocDetails(getProviderInformation().getId());

                databaseReference.child(getProviderInformation().getId()).setValue(docDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (isVisible()) {

                            if (task.isSuccessful()) {

                                Toast.makeText(requireContext(), "doc online", Toast.LENGTH_SHORT).show();

                                receive_text.setText(R.string.im_available);

                            } else {

                                Toast.makeText(requireContext(), "something went wrong", Toast.LENGTH_SHORT).show();

                            }

                        }

                    }
                });

            }

        });


    }

    private void notifyHealthAlert() {

        try {

            new MvpViewModel().notifyHealthAlertLiveData().observe(requireActivity(), new Observer<Map>() {
                @Override
                public void onChanged(Map map) {

//                    if (map.get("success")!=null){
//
//                        if (map.get("success").equals("1")) {
//
//                            Log.d(TAG, "onChanged: notifying success 1");
//
//                        } else {
//
//                            Log.d(TAG, "onChanged: notifying success 0");
//
//                        }
//
//                    }else {
//
//                        Log.d(TAG, "onChanged: unable to notify , gives null");
//
//                    }

                }
            });

        } catch (Exception e) {

            Log.d(TAG, "onSuccess: exception while notifying");

        }

    }


    private void setData(View view) {

        ImageView profile_image;
        TextView firstName, lastName, day_type;

        toggle_switch = view.findViewById(R.id.toggle_switch);
        receive_text = view.findViewById(R.id.receive_text);

        profile_image = view.findViewById(R.id.profile_image);
        firstName = view.findViewById(R.id.firstName);
        lastName = view.findViewById(R.id.lastName);
        day_type = view.findViewById(R.id.day_type);
        notification_sign = view.findViewById(R.id.notification_sign);

        day_type.setText(getDayStatus());

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(ProviderMainFrame.provider_information_file, Context.MODE_PRIVATE);
        String editData = sharedPreferences.getString(ProviderMainFrame.provider_information_key, "");

        if (editData.length() != 0) {
            Gson gson = new Gson();
            ProviderInformation providerInformation = gson.fromJson(editData, ProviderInformation.class);

//            String name = "Dr. " + providerInformation.getFirstName();

            if (getProviderInformation().getUserTitle() != null && getProviderInformation().getUserTitle().length() != 0) {

                String name = getProviderInformation().getUserTitle() + " " + providerInformation.getFirstName();
                firstName.setText(name);

            } else {

                String name = "Dr. " + providerInformation.getFirstName();
                firstName.setText(name);

            }

            lastName.setText(providerInformation.getSurName());

            if (providerInformation.getProfileImage().length() != 0) {
                Picasso.with(getContext()).load(Uri.parse(providerInformation.getProfileImage())).error(R.drawable.profile).into(profile_image);
            }
        }


    }

    private void getAppointmentRequest() {

        Log.d(TAG, "getAppointmentRequest: " + ProviderMainFrame.id);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference(URLBuilder.FirebaseDataNodes.BookingRequest).child(ProviderMainFrame.id);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if (snapshot.exists()) {
                    patientInformationList.clear();

                    for (DataSnapshot child : snapshot.getChildren()) {

                        PatientInformation patientInformation = child.getValue(PatientInformation.class);

                        patientInformationList.add(patientInformation);


                    }

                    if (!load_status) {
                        load_status = true;

                        openAppointmentRequestSheet(patientInformationList);
                    }


                } else {
                    // empty case
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void getNotifications() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference(URLBuilder.FirebaseDataNodes.Notification).child(ProviderMainFrame.id).child(URLBuilder.FirebaseDataNodes.activationStatus);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    Fragment currentFragment = requireActivity().getSupportFragmentManager().findFragmentById(R.id.main_frame_layout);

                    if (currentFragment instanceof ProviderHomeFragment) {

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

                    if (currentFragment instanceof ProviderHomeFragment) {

                        // exception part

                    }


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {



            }
        });


    }

    private void openAppointmentRequestSheet(List<PatientInformation> patientInformation) {
        handler.post(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme);

                View bottom_view = LayoutInflater.from(requireContext()).inflate(R.layout.appointment_list_layout
                        ,
                        (RelativeLayout) bottomSheetDialog.findViewById(R.id.relative_layout));

                bottomSheetDialog.setContentView(bottom_view);

                bottomSheetDialog.show();

                bottomSheetDialog.setCanceledOnTouchOutside(false);
                bottomSheetDialog.setCancelable(false);

                ImageView doctor_image, cancel;
                TextView doctor_name, speciality;

                doctor_name = bottomSheetDialog.findViewById(R.id.doctor_name);
                speciality = bottomSheetDialog.findViewById(R.id.speciality);
                doctor_image = bottomSheetDialog.findViewById(R.id.doctor_image);
                cancel = bottomSheetDialog.findViewById(R.id.cancel);

                assert cancel != null;

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        bottomSheetDialog.dismiss();

                    }
                });

                RecyclerView recyclerView = bottomSheetDialog.findViewById(R.id.appointment_holder);

                assert recyclerView != null;

                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                requestAppointmentRecycleViewAdaptor = new RequestAppointmentRecycleViewAdaptor(new ArrayList<>(),
                        ProviderHomeFragment.this, getContext());

                recyclerView.setAdapter(requestAppointmentRecycleViewAdaptor);

                requestAppointmentRecycleViewAdaptor.loadData(patientInformation);

                String name;

                if (getProviderInformation().getUserTitle() != null && getProviderInformation().getUserTitle().length() != 0) {

                    name = getProviderInformation().getUserTitle() + " " + requestForDetail(RequestType.profileName);

                } else {

                    name = "Dr. " + getProviderInformation().getUserTitle() + " " + requestForDetail(RequestType.profileName);

                }

                assert doctor_name != null;
                doctor_name.setText(name);

                assert speciality != null;
                speciality.setText(requestForDetail(RequestType.speciality));

                Picasso.with(requireContext()).load(Uri.parse(requestForDetail(RequestType.profileImage))).error(R.drawable.profile).into(doctor_image);


            }
        });

    }

    @Override
    public void refresh(PatientInformation patientInformation) {


        removeRequest(patientInformation);


    }

    private void removeRequest(PatientInformation patientInformation) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(URLBuilder.FirebaseDataNodes.BookingRequest);

        databaseReference.child(ProviderMainFrame.id).child(patientInformation.getRequestKey()).removeValue();

        if (patientInformationList.size() != 0) {
            for (int i = 0; i < patientInformationList.size(); i++) {

                if (patientInformationList.get(i).getRequestKey().equals(patientInformation.getRequestKey())) {
                    patientInformationList.remove(i);

                    break;
                }

            }
        }

        requestAppointmentRecycleViewAdaptor.loadData(patientInformationList);


    }

    private void checkAccountActivationStatus() {

        if (getProviderInformation().getProfileStatus().equals(ResponseInformation.fail_response_type)) {

            if (!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED)) {

                new Thread(new AccountActivationStatusCaller(ProviderMainFrame.id, ProviderHomeFragment.this)).start();
            } else {


                DialogShower dialogShower = new DialogShower(R.layout.internet_error, getContext());
                dialogShower.showDialog();
            }

        } else if (getProviderInformation().getProfileStatus().equals(ResponseInformation.success_response_type)) {

        }

    }


    private String requestForDetail(RequestType requestType) {
        SharedPreferences preferences = requireContext().getSharedPreferences(ProviderMainFrame.provider_information_file, Context.MODE_PRIVATE);
        String json = preferences.getString(ProviderMainFrame.provider_information_key, "");
        Gson gson = new Gson();
        ProviderInformation providerInformation = gson.fromJson(json, ProviderInformation.class);

        switch (requestType) {
            case profileName:

                return providerInformation.getFirstName().charAt(0) + " " + providerInformation.getSurName();

            case profileImage:

                return providerInformation.getProfileImage();

            case speciality:

                return providerInformation.getSpecialization();

        }

        return null;
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

    @Override
    public void onResume() {
        super.onResume();

        getData();

    }

    private void getData() {

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (isVisible()) {

                    if (snapshot.exists()) {

                        list.clear();

                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                            DocDetails university = postSnapshot.getValue(DocDetails.class);

                            list.add(university);

                        }

                        DocDetails docDetails = new DocDetails(getProviderInformation().getId());

                        for (int i = 0; i < list.size(); i++) {

                            if (list.get(i).getDocId().equals(docDetails.getDocId())) {

                                toggle_switch.setOn(true);

                                receive_text.setText(R.string.im_available);

                                break;

                            } else {

                                toggle_switch.setOn(false);

                                receive_text.setText(R.string.im_un_available);

                            }

                        }

                    } else {

                        Toast.makeText(requireContext(), "not contains", Toast.LENGTH_SHORT).show();

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.d(TAG, "onCancelled: " + error.getMessage());

            }
        });

    }


}