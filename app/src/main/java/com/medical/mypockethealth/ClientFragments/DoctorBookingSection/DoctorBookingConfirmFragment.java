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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.medical.mypockethealth.BaseFragments.RegisterSection.UserRegisterSection.SignUpAsUserFragment;
import com.medical.mypockethealth.Classes.ClientInformation.ClientInformation;
import com.medical.mypockethealth.Classes.DoctorInformation.DoctorInformation;
import com.medical.mypockethealth.Classes.URLBuilder;
import com.medical.mypockethealth.ClientFragments.WalletSection.PaymentFragment;
import com.medical.mypockethealth.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DoctorBookingConfirmFragment extends Fragment {

    private static final String TAG = "DoctorBookingConfirmFra";

    public static final String doctor_information_key="doctor_information_key";
    
    public static List<ClientInformation> clientInformationArrayList=new ArrayList<>();
    private LinearLayout prescription_box;
    public static DoctorInformation doctorInformation;
    public static String selected_image_path=null;
    BottomSheetDialog bottomSheetDialog;
    private RadioButton yes,no;
    private boolean yes_clicked=false,no_clicked=false,isToggleSwitched=false;;
    public static int doctorBookingStatus=0;

    public static final String data_key="data_key";


    public enum IdentityType {
        yes,no,notSelected
    }

    public DoctorBookingConfirmFragment() {
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
        return inflater.inflate(R.layout.fragment_doctor_booking_confirme, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


         setData(view);

        view.findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DoctorBookingConfirmFragment.doctorBookingStatus=1;

                createBooking();

            }
        });


        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                        new DoctorBookingSelectSlotFragment()).addToBackStack(null).commit();
            }
        });

        
    }


    
    private void createBooking()
    {

        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                new PaymentFragment()).addToBackStack(null).commit();
    }


    private void setData(View view)
    {

        ImageView profile_image;
        TextView speciality,experience,consult_fee,name_field,selected_slot,clinic_name,address,date;

        profile_image=view.findViewById(R.id.profile_image);
        speciality=view.findViewById(R.id.speciality);
        experience=view.findViewById(R.id.experience);
        consult_fee=view.findViewById(R.id.consult_fee);
        name_field=view.findViewById(R.id.name);
        selected_slot=view.findViewById(R.id.selected_slot);
        clinic_name=view.findViewById(R.id.clinic_name);
        address=view.findViewById(R.id.address);
        date=view.findViewById(R.id.date);

        if(doctorInformation ==null)
        {
            assert getArguments() != null;
            doctorInformation=(DoctorInformation)getArguments().get(DoctorBookingConfirmFragment.doctor_information_key);
        }


        assert doctorInformation != null;

//        String name="Dr. " + doctorInformation.getFirstName().charAt(0)+" "+doctorInformation.getSurName();

        String name;

        if (doctorInformation.getUserTitle() != null && doctorInformation.getUserType() != null) {

            name = doctorInformation.getUserTitle() + " " +
                    doctorInformation.getFirstName().charAt(0) + " " + doctorInformation.getSurName();

        } else {

            name = "Dr. " + doctorInformation.getFirstName().charAt(0) + " " + doctorInformation.getSurName();

        }

        name_field.setText(name);

        speciality.setText(doctorInformation.getSpecialization());

        clinic_name.setText(doctorInformation.getWorkLocation());
        address.setText(doctorInformation.getAddress());

        String ex=doctorInformation.getExperience()+" yrs experience";
        experience.setText(ex);
        
        selected_slot.setText(doctorInformation.getSlotTime());

        date.setText(setFilteredDate(doctorInformation.getDateInformation().
                getYear(),doctorInformation.getDateInformation().getMonth(),doctorInformation.getDateInformation().getDate()));

        String consultFee= URLBuilder.CurrencySign.R+" "+doctorInformation.getConsultCharges()+" Consultation Fees";

        consult_fee.setText(consultFee);

        Picasso.with(getContext()).load(Uri.parse(doctorInformation.getProfileImage())).error(R.drawable.profile).into(profile_image);

    }

  
    private void enablePrescriptionFiled()
    {
        prescription_box.setVisibility(View.VISIBLE);
    }
    private void disablePrescriptionFiled()
    {
        prescription_box.setVisibility(View.GONE);
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

    private String setFilteredDate(String year,String month,String day)   {

        String result = "";

        try {
            result = requireContext().getResources().getStringArray(R.array.month_names)[Integer.parseInt(month)-1];
        } catch (ArrayIndexOutOfBoundsException e) {
            result = Integer.toString(Integer.parseInt(month)-1);
        }


        return day+", "+result+" "+year;

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