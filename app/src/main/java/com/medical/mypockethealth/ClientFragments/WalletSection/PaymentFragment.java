package com.medical.mypockethealth.ClientFragments.WalletSection;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.medical.mypockethealth.ApplicationBase.BaseActivity;
import com.medical.mypockethealth.Classes.ClientInformation.ClientInformation;
import com.medical.mypockethealth.Classes.ClientInformation.PatientInformation;
import com.medical.mypockethealth.Classes.DialogShower;
import com.medical.mypockethealth.Classes.NetworkSection.NetworkStatus;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.Classes.URLBuilder;
import com.medical.mypockethealth.ClientActivity.ClientMainFrame;
import com.medical.mypockethealth.ClientFragments.BottomNavigationSection.BookingSection.ClientBookingSection;
import com.medical.mypockethealth.ClientFragments.DoctorBookingSection.DoctorBookingAddPaymentFragment;
import com.medical.mypockethealth.ClientFragments.DoctorBookingSection.DoctorBookingConfirmFragment;
import com.medical.mypockethealth.R;
import com.medical.mypockethealth.Threads.UserSection.BookingSection.BookDoctorCaller;


public class PaymentFragment extends Fragment implements BookDoctorCaller.CallbackFromBookDoctorCaller {

    private final Handler handler = new Handler();

    public PaymentFragment() {
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
        return inflater.inflate(R.layout.fragment_payment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(DoctorBookingConfirmFragment.doctorBookingStatus==1)
                {

                    DoctorBookingConfirmFragment.doctorBookingStatus=0;

                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                            new DoctorBookingConfirmFragment()).addToBackStack(null).commit();
                }
                else
                {

                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                            new WalletUserFragment()).addToBackStack(null).commit();

                }



            }
        });

        view.findViewById(R.id.male).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(),"coming soon",Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                        new DoctorBookingAddPaymentFragment()).addToBackStack(null).commit();
            }
        });

        view.findViewById(R.id.male2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(),"coming soon",Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.medical_payment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(!getUserInformation().getMedicalAidStatus().equals("0"))
                {
                    createBooking();
                }
                else
                {
                    openBottomSheet();

                }


            }
        });



    }

    private void openBottomSheet()
    {

        BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(requireContext(),R.style.BottomSheetDialogTheme);

        View bottom_view= LayoutInflater.from(getContext()).inflate(R.layout.user_account_activation_layout
                ,
                (RelativeLayout) bottomSheetDialog.findViewById(R.id.relative_layout));

        bottomSheetDialog.setContentView(bottom_view);

        bottomSheetDialog.show();

        TextView userName=bottomSheetDialog.findViewById(R.id.user_name);

        assert userName != null;

        String name=getUserInformation().getFirstName()+" " + getUserInformation().getSurName();
        userName.setText(name);

    }

    private ClientInformation getUserInformation()
    {

        SharedPreferences preferences= getActivity().getSharedPreferences(ClientMainFrame.client_information_file, Context.MODE_PRIVATE);
        String json = preferences.getString(ClientMainFrame.client_information_key, "");
        Gson gson = new Gson();
        return gson.fromJson(json, ClientInformation.class);

    }

    private void createBooking() {

        if (!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED)) {

            new Thread(new BookDoctorCaller(DoctorBookingConfirmFragment.doctorInformation,
                    PaymentFragment.this, getContext())).start();


        } else {
            DialogShower dialogShower = new DialogShower(R.layout.internet_error, getContext());
            dialogShower.showDialog();
        }

    }

    @Override
    public void confirmation(ResponseInformation responseInformation) {


        handler.post(new Runnable() {
            @Override
            public void run() {

                if(responseInformation.getSuccess().equals(String.valueOf(ResponseInformation.fail_response_type)))
                {

                    Toast.makeText(getContext(),responseInformation.getMessage(),Toast.LENGTH_SHORT).show();

                }
                else
                {

                    sendAppointmentRequest(responseInformation.getBookingId());

                    Toast.makeText(getContext(),"Slot booked Successfully",Toast.LENGTH_SHORT).show();

                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                            new ClientBookingSection()).addToBackStack(null).commit();
                }

            }
        });

    }

    private void sendAppointmentRequest(String bookingId)
    {

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(URLBuilder.FirebaseDataNodes.BookingRequest);

        String random_key_generator=databaseReference.push().getKey();

        PatientInformation patientInformation=new PatientInformation(DoctorBookingConfirmFragment.doctorInformation.getPatientName(),
                DoctorBookingConfirmFragment.doctorInformation.getPatientAge(),DoctorBookingConfirmFragment.doctorInformation.getSelected_date(),
                DoctorBookingConfirmFragment.doctorInformation.getSlotTime(),random_key_generator,getProfileImage(),bookingId,ClientMainFrame.id);

        assert random_key_generator != null;

        databaseReference.child(DoctorBookingConfirmFragment.doctorInformation.getId()).child(random_key_generator).setValue(patientInformation).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {



            }
        });



    }

    private String getProfileImage()
    {
        SharedPreferences preferences= requireContext().getSharedPreferences(ClientMainFrame.client_information_file, Context.MODE_PRIVATE);
        String json = preferences.getString(ClientMainFrame.client_information_key, "");
        Gson gson = new Gson();
        ClientInformation clientInformation = gson.fromJson(json, ClientInformation.class);

        return clientInformation.getProfileImage();
    }

}