package com.medical.mypockethealth.ClientFragments.DoctorBookingSection;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.medical.mypockethealth.Adaptors.UserSection.BookingSection.DoctorDetailsRecycleViewAdaptor;
import com.medical.mypockethealth.ApplicationBase.BaseActivity;
import com.medical.mypockethealth.BaseFragments.RegisterSection.UserRegisterSection.SignUpAsUserFragment;
import com.medical.mypockethealth.Classes.ClientInformation.ClientInformation;
import com.medical.mypockethealth.Classes.DialogShower;
import com.medical.mypockethealth.Classes.DoctorInformation.DoctorInformation;
import com.medical.mypockethealth.Classes.NetworkSection.NetworkStatus;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.ClientActivity.ClientMainFrame;
import com.medical.mypockethealth.ClientFragments.HomeFragment;
import com.medical.mypockethealth.ClientFragments.SymptomCheckerSection.SymptomConsultSectionFragment;
import com.medical.mypockethealth.R;
import com.medical.mypockethealth.Threads.UserSection.BookingSection.DoctorDetailsFetcher;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;


public class DoctorBookingTwoFragment extends Fragment implements DoctorDetailsFetcher.CallbackFromDoctorDetailsFetcher,
        DoctorDetailsRecycleViewAdaptor.callBackFromDoctorDetailsRecycleViewAdaptor {


//    private static final String TAG = "DoctorBookingTwoFragment";

    private EditText edit_search;

    public static final String data_key = "data_key";

    private ImageView loading, reload;
    private final Handler handler = new Handler();
    private boolean isToggleSwitched = false, yes_clicked = false, no_clicked = false;
    private RadioButton yes, no;
    private LinearLayout prescription_box;
    private List<DoctorInformation> doctorInformationList;
    private DoctorDetailsRecycleViewAdaptor doctorDetailsRecycleViewAdaptor;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("online_doctors");

    private ExecutorService executorService;

    public DoctorBookingTwoFragment() {
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
        return inflater.inflate(R.layout.fragment_doctor_booking_two, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        establishViews(view);

        getData();

        executorService = Executors.newFixedThreadPool(5);

        getDoctorsList();

//        disableBottomNavigation();

        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (HomeFragment.mode == 0) {
                    HomeFragment.mode = 0;

                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                            new DoctorBookingOneFragment()).addToBackStack(null).commit();
                } else {
                    HomeFragment.mode = 0;

                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                            new SymptomConsultSectionFragment()).addToBackStack(null).commit();
                }

            }
        });


        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED)) {

                    loading.setVisibility(View.VISIBLE);
                    reload.setVisibility(View.GONE);

                    getDoctorsList();

                } else {

                    reload.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.GONE);

                    DialogShower dialogShower = new DialogShower(R.layout.internet_error, getContext());
                    dialogShower.showDialog();

                }

            }
        });


        RecyclerView recyclerView = view.findViewById(R.id.doctor_holder);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        doctorDetailsRecycleViewAdaptor = new DoctorDetailsRecycleViewAdaptor(new ArrayList<>(),
                DoctorBookingTwoFragment.this, getContext());

        recyclerView.setAdapter(doctorDetailsRecycleViewAdaptor);


    }

    private void enableBottomNavigation() {

//        BottomNavigationView view1 = requireActivity().findViewById(R.id.bottom_navigation_view);
//
//        view1.setVisibility(View.VISIBLE);

    }


    private void establishViews(View view) {
        loading = view.findViewById(R.id.loading);
        reload = view.findViewById(R.id.reload);
        reload.setVisibility(View.GONE);
        loading.setVisibility(View.GONE);
    }

    private void getData() {

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if (isVisible()) {

                    getDoctorsList();

                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                if (isVisible()) {

                    getDoctorsList();

                }

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getDoctorsList() {

        if (!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED)) {

            loading.setVisibility(View.VISIBLE);
            reload.setVisibility(View.GONE);

            executorService.execute(new DoctorDetailsFetcher(DoctorBookingTwoFragment.this));

        } else {

            reload.setVisibility(View.VISIBLE);
            loading.setVisibility(View.GONE);

            DialogShower dialogShower = new DialogShower(R.layout.internet_error, getContext());
            dialogShower.showDialog();
        }

    }

    @Override
    public void confirmationCallbackFromDoctorDetailsFetcher(ResponseInformation responseInformation) {
        handler.post(new Runnable() {
            @Override
            public void run() {

//                Toast.makeText(requireContext(), "getted", Toast.LENGTH_SHORT).show();

                loading.setVisibility(View.GONE);
                reload.setVisibility(View.VISIBLE);

                doctorInformationList = new ArrayList<>();

                doctorDetailsRecycleViewAdaptor.loadData(new ArrayList<>());

                doctorDetailsRecycleViewAdaptor.notifyDataSetChanged();

                if (responseInformation.getSuccess().equals(ResponseInformation.fail_response_type)) {

                    Toast.makeText(getContext(), responseInformation.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    @Override
    public void informationCallbackFromDoctorDetailsFetcher(List<DoctorInformation> doctorInformation) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                loading.setVisibility(View.GONE);

                doctorInformationList = doctorInformation;

                doctorDetailsRecycleViewAdaptor.loadData(doctorInformation);

//                if (doctorInformation!=null && doctorInformation.size()!=0){
//
//
//
//                }else {
//
//                    Log.d(TAG, "run: entered");
//
//                    doctorInformationList = new ArrayList<>();
//
//                    doctorDetailsRecycleViewAdaptor.loadData(new ArrayList<>());
//
//                    doctorDetailsRecycleViewAdaptor.notifyDataSetChanged();
//
//                }

            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void showDescription(DoctorInformation doctorInformation) {


        bottomSheetHandler(doctorInformation, 1);

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void book(DoctorInformation doctorInformation) {

        bottomSheetHandler(doctorInformation, 0);

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void bottomSheetHandler(DoctorInformation doctorInformation, int type) {

        if (type == 0) {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme);

            View bottom_view = LayoutInflater.from(requireContext()).inflate(R.layout.patient_details_layout
                    ,
                    (RelativeLayout) bottomSheetDialog.findViewById(R.id.relative_layout));

            bottomSheetDialog.setContentView(bottom_view);

            bottomSheetDialog.show();

            EditText name, age;
            Button save;
            ImageView profile_image;
            TextView speciality, name_field;

            name = bottomSheetDialog.findViewById(R.id.name);
            age = bottomSheetDialog.findViewById(R.id.age);
            profile_image = bottomSheetDialog.findViewById(R.id.profile_image);
            save = bottomSheetDialog.findViewById(R.id.save);
            speciality = bottomSheetDialog.findViewById(R.id.speciality);
            name_field = bottomSheetDialog.findViewById(R.id.name_field);
            yes = bottomSheetDialog.findViewById(R.id.yes);
            no = bottomSheetDialog.findViewById(R.id.no);
            prescription_box = bottomSheetDialog.findViewById(R.id.prescription_box);

            ClientInformation clientInformation = getUserInformation();

            String patientName = clientInformation.getFirstName() + " " + clientInformation.getSurName();
            assert name != null;
            name.setText(patientName);

            assert age != null;

            try {

                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date d = sdf.parse(clientInformation.getPatientAge());
                Calendar c = Calendar.getInstance();
                assert d != null;
                c.setTime(d);
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH) + 1;
                int date = c.get(Calendar.DATE);
                LocalDate l1 = LocalDate.of(year, month, date);
                LocalDate now1 = LocalDate.now();
                Period diff1 = Period.between(l1, now1);
                age.setText(String.valueOf(diff1.getYears()));


            } catch (Exception e) {
                Log.d(TAG, "bottomSheetHandler: " + e.getMessage());
            }

            LabeledSwitch labeledSwitch = bottomSheetDialog.findViewById(R.id.toggle_switch);

            assert labeledSwitch != null;

            labeledSwitch.setOn(isToggleSwitched);


            labeledSwitch.setOnToggledListener(new OnToggledListener() {
                @Override
                public void onSwitched(ToggleableView toggleableView, boolean isOn) {

                    isToggleSwitched = isOn;

                }
            });

            disablePrescriptionFiled();

            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    activateField(SignUpAsUserFragment.IdentityType.yes);

                    enablePrescriptionFiled();

                }
            });

            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    activateField(SignUpAsUserFragment.IdentityType.no);

                    disablePrescriptionFiled();

                }
            });

            String name_section;

            if (doctorInformation.getUserTitle() != null && doctorInformation.getUserType() != null) {

                name_section = doctorInformation.getUserTitle() + " " +
                        doctorInformation.getFirstName().charAt(0) + " " + doctorInformation.getSurName();

            } else {

                name_section = "Dr. " + doctorInformation.getFirstName().charAt(0) + " " + doctorInformation.getSurName();

            }

//            String name_section = "Dr. " + doctorInformation.getFirstName().charAt(0) + " " + doctorInformation.getSurName();
            assert name_field != null;
            name_field.setText(name_section);

            assert speciality != null;
            speciality.setText(doctorInformation.getSpecialization());

            Picasso.with(getContext()).load(Uri.parse(doctorInformation.getProfileImage())).error(R.drawable.profile).into(profile_image);


            assert save != null;
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (name.getText().toString().trim().length() == 0) {
                        name.setError("Please enter your patient name");
                    } else if (profile_name_checker(name.getText().toString())) {
                        name.setError("spacial characters and numbers not allowed");
                    } else {

                        doctorInformation.setPatientName(name.getText().toString());

                    }

                    if (age.getText().toString().trim().length() == 0) {
                        age.setError("Please enter your patient age");
                    } else {

                        doctorInformation.setPatientAge(age.getText().toString());

                        if (!yes_clicked && !no_clicked) {
                            Toast.makeText(getContext(), "Do you have previous medical record", Toast.LENGTH_SHORT).show();
                        } else {
                            if (yes_clicked) {

                                if (isToggleSwitched) {

                                    doctorInformation.setEhrStatus(ResponseInformation.success_response_type);

                                    bottomSheetDialog.dismiss();

                                    selectSlotSection(doctorInformation);

                                } else {
                                    Toast.makeText(getContext(), "Kindly allow doctor to access your EHR files", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                doctorInformation.setEhrStatus(ResponseInformation.fail_response_type);

                                bottomSheetDialog.dismiss();

                                selectSlotSection(doctorInformation);
                            }
                        }


                    }


                }
            });
        } else {

            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme);

            View bottom_view = LayoutInflater.from(requireContext()).inflate(R.layout.bio_sheet_layout
                    ,
                    (RelativeLayout) bottomSheetDialog.findViewById(R.id.relative_layout));

            bottomSheetDialog.setContentView(bottom_view);

            bottomSheetDialog.show();

            bottomSheetDialog.setCanceledOnTouchOutside(false);

            ImageView doctor_image;
            TextView doctor_name, speciality, bio;
            Button save;


            doctor_name = bottomSheetDialog.findViewById(R.id.doctor_name);
            speciality = bottomSheetDialog.findViewById(R.id.speciality);
            doctor_image = bottomSheetDialog.findViewById(R.id.doctor_image);
            bio = bottomSheetDialog.findViewById(R.id.bio);
            save = bottomSheetDialog.findViewById(R.id.save);

            String name;

            if (doctorInformation.getUserTitle() != null && doctorInformation.getUserType() != null) {

                name = doctorInformation.getUserTitle() + " " +
                        doctorInformation.getFirstName().charAt(0) + " " + doctorInformation.getSurName();

            } else {

                name = "Dr. " + doctorInformation.getFirstName().charAt(0) + " " + doctorInformation.getSurName();

            }

//            String name = "Dr. " + doctorInformation.getFirstName().charAt(0) + " " + doctorInformation.getSurName();

            assert doctor_name != null;
            doctor_name.setText(name);

            assert speciality != null;
            speciality.setText(doctorInformation.getSpecialization());

            assert bio != null;
            bio.setText(doctorInformation.getBio());

            Picasso.with(requireContext()).load(Uri.parse(doctorInformation.getProfileImage())).error(R.drawable.profile).into(doctor_image);


            assert save != null;
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    bottomSheetDialog.dismiss();
                }
            });


        }

    }

    private ClientInformation getUserInformation() {

        SharedPreferences preferences = requireActivity().getSharedPreferences(ClientMainFrame.client_information_file, Context.MODE_PRIVATE);
        String json = preferences.getString(ClientMainFrame.client_information_key, "");
        Gson gson = new Gson();
        return gson.fromJson(json, ClientInformation.class);

    }

    private void selectSlotSection(DoctorInformation doctorInformation) {
        DoctorBookingSelectSlotFragment doctorBookingSelectSlotFragment = new DoctorBookingSelectSlotFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(DoctorBookingSelectSlotFragment.data_key, doctorInformation);

        DoctorBookingSelectSlotFragment.doctorInformation = null;

        doctorBookingSelectSlotFragment.setArguments(bundle);
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout
                , doctorBookingSelectSlotFragment).addToBackStack(null).commit();
    }

    private boolean profile_name_checker(String value) {
        boolean result = false;
        Pattern pattern = Pattern.compile("[^a-zA-Z\\s]");
        Matcher matcher = pattern.matcher(value);

        while (matcher.find()) {
            result = true;

        }
        return result;
    }


    private void activateField(SignUpAsUserFragment.IdentityType identityType) {
        switch (identityType) {

            case yes:

                yes.setChecked(true);
                no.setChecked(false);
                yes_clicked = true;
                no_clicked = false;

                break;

            case no:
                yes.setChecked(false);
                no.setChecked(true);
                yes_clicked = false;
                no_clicked = true;

                break;

        }
    }


    private void enablePrescriptionFiled() {
        prescription_box.setVisibility(View.VISIBLE);
    }

    private void disablePrescriptionFiled() {
        prescription_box.setVisibility(View.GONE);
    }


    @Override
    public void call(int position) {


//        Intent intent = new Intent(Intent.ACTION_DIAL);
//        intent.setData(Uri.parse("tel:"+doctorInformationList.get(position).getPhone()));
//        startActivity(intent);

    }


//    private String getProviderType(DoctorInformation providerInformation) {
//
//        String type;
//
//        if (providerInformation.getUserType() != null && providerInformation.getUserType().length() != 0) {
//
//            if (providerInformation.getUserType().equalsIgnoreCase("doctor")) {
//
//                type = "Dr.";
//
//            } else if (providerInformation.getUserType().equalsIgnoreCase("nurse")) {
//
//                type = "Nur.";
//
//            } else {
//
//                type = "";
//
//            }
//
//        } else {
//
//            type = "";
//
//        }
//
//        return type;
//    }


}