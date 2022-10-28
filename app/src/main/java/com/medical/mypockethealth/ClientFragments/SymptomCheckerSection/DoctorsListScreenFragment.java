package com.medical.mypockethealth.ClientFragments.SymptomCheckerSection;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.mypockethealth.Adaptors.UserSection.BookingSection.DoctorDetailsRecycleViewAdaptor;
import com.medical.mypockethealth.ApplicationBase.BaseActivity;
import com.medical.mypockethealth.Classes.DialogShower;
import com.medical.mypockethealth.Classes.DoctorInformation.DoctorInformation;
import com.medical.mypockethealth.Classes.DoctorInformation.DoctorInformationRoot;
import com.medical.mypockethealth.Classes.NetworkSection.NetworkStatus;
import com.medical.mypockethealth.MyPocketHealthRetrofit.MvpViewModel;
import com.medical.mypockethealth.R;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class DoctorsListScreenFragment extends Fragment implements DoctorDetailsRecycleViewAdaptor.callBackFromDoctorDetailsRecycleViewAdaptor {

    private ImageView reload;
    private DoctorDetailsRecycleViewAdaptor doctorDetailsRecycleViewAdaptor;
    public static String specialistId;
    private RecyclerView recyclerView;
    private Dialog loading_box;

    public DoctorsListScreenFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doctors_list_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        establishViews(view);

        view.findViewById(R.id.backButton).setOnClickListener(v -> {

            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                    new SymptomsResultScreenFragment()).addToBackStack(null).commit();

        });

        getDoctorsList();

//        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (HomeFragment.mode == 0) {
//                    HomeFragment.mode = 0;
//
//                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
//                            new DoctorBookingOneFragment()).addToBackStack(null).commit();
//                } else {
//                    HomeFragment.mode = 0;
//
//                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
//                            new SymptomConsultSectionFragment()).addToBackStack(null).commit();
//                }
//
//
//            }
//        });


        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED)) {
//
//                    loading.setVisibility(View.VISIBLE);
//                    reload.setVisibility(View.GONE);
//                    recyclerView.setVisibility(View.GONE);
//
//                    getDoctorsList();
//
//                } else {
//                    reload.setVisibility(View.VISIBLE);
//                    loading.setVisibility(View.GONE);
//                    recyclerView.setVisibility(View.GONE);
//
//                    DialogShower dialogShower = new DialogShower(R.layout.internet_error, getContext());
//                    dialogShower.showDialog();
//                }

            }
        });


        doctorDetailsRecycleViewAdaptor = new DoctorDetailsRecycleViewAdaptor(new ArrayList<>(),
                DoctorsListScreenFragment.this, getContext());

        recyclerView.setAdapter(doctorDetailsRecycleViewAdaptor);

    }

    private void establishViews(View view) {

        recyclerView = view.findViewById(R.id.doctor_holder);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        reload = view.findViewById(R.id.reload);

//        reload.setVisibility(View.GONE);
//        loading.setVisibility(View.VISIBLE);
//        recyclerView.setVisibility(View.GONE);
    }

    private void getDoctorsList() {

        if (!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED)) {

            openLoadingBox();
            reload.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);

            Log.d(TAG, "getDoctorsList: "+specialistId);

            new MvpViewModel().getDoctorsAsPerSpecialistLiveData(specialistId).observe(requireActivity(), new Observer<DoctorInformationRoot>() {
                @Override
                public void onChanged(DoctorInformationRoot doctorInformationRoot) {

                    if (doctorInformationRoot != null) {

                        if (doctorInformationRoot.getSuccess().equals("1")) {

                            if (doctorInformationRoot.getDetails() != null && doctorInformationRoot.getDetails().size() != 0) {

                                loading_box.dismiss();
                                reload.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);

                                doctorDetailsRecycleViewAdaptor.loadData(doctorInformationRoot.getDetails());

                            } else {

                                loading_box.dismiss();
                                reload.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.GONE);

                                Toast.makeText(requireContext(), "No Doctors Found", Toast.LENGTH_SHORT).show();

                            }

                        } else {

                            loading_box.dismiss();
                            reload.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);

                            Toast.makeText(requireContext(), "" + doctorInformationRoot.getMessage(), Toast.LENGTH_SHORT).show();

                        }

                    } else {

                        loading_box.dismiss();
                        reload.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);

                        Toast.makeText(requireContext(), "We are having technical issue. Please try again later", Toast.LENGTH_SHORT).show();

                    }

                }
            });

        } else {

            reload.setVisibility(View.VISIBLE);

            DialogShower dialogShower = new DialogShower(R.layout.internet_error, getContext());
            dialogShower.showDialog();
        }


    }

    @Override
    public void book(DoctorInformation doctorInformation) {

    }

    @Override
    public void showDescription(DoctorInformation doctorInformation) {

    }

    private void openLoadingBox() {

        Log.d(TAG, "open: entered loading");

        loading_box = new Dialog(getContext());
        loading_box.setContentView(R.layout.loading_layout);
        loading_box.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading_box.setCanceledOnTouchOutside(false);
        loading_box.setCancelable(false);
        Window window = loading_box.getWindow();
        window.setGravity(Gravity.CENTER);
        loading_box.show();
    }

//    private ClientInformation getUserInformation() {
//
//        SharedPreferences preferences = requireActivity().getSharedPreferences(ClientMainFrame.client_information_file, Context.MODE_PRIVATE);
//        String json = preferences.getString(ClientMainFrame.client_information_key, "");
//        Gson gson = new Gson();
//        return gson.fromJson(json, ClientInformation.class);
//
//    }
//
//    private void selectSlotSection(DoctorInformation doctorInformation) {
//        DoctorBookingSelectSlotFragment doctorBookingSelectSlotFragment = new DoctorBookingSelectSlotFragment();
//
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(DoctorBookingSelectSlotFragment.data_key, doctorInformation);
//
//        DoctorBookingSelectSlotFragment.doctorInformation = null;
//
//        doctorBookingSelectSlotFragment.setArguments(bundle);
//        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout
//                , doctorBookingSelectSlotFragment).addToBackStack(null).commit();
//    }
//
//    private boolean profile_name_checker(String value) {
//        boolean result = false;
//        Pattern pattern = Pattern.compile("[^a-zA-Z\\s]");
//        Matcher matcher = pattern.matcher(value);
//
//        while (matcher.find()) {
//            result = true;
//
//        }
//        return result;
//    }
//
//
//    private void activateField(SignUpAsUserFragment.IdentityType identityType) {
//
//    }


    @Override
    public void call(int position) {

    }
}