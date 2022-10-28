package com.medical.mypockethealth.ClientFragments.BottomNavigationSection.SettingsSection.ProfileSection;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.medical.mypockethealth.Adaptors.UserSection.ProfileSection.AllergyRecycleViewAdaptor;
import com.medical.mypockethealth.Adaptors.UserSection.ProfileSection.MedicationRecycleViewAdaptor;
import com.medical.mypockethealth.Adaptors.UserSection.ProfileSection.MorbidityRecycleViewAdaptor;
import com.medical.mypockethealth.AgoraSection.VideoChatViewActivity;
import com.medical.mypockethealth.Classes.ClientInformation.ClientInformation;
import com.medical.mypockethealth.ClientActivity.ClientMainFrame;
import com.medical.mypockethealth.ClientFragments.BottomNavigationSection.SettingsSection.SettingFragment;
import com.medical.mypockethealth.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;


public class ViewProfileFragment extends Fragment {

    private static final String TAG = "ViewProfileFragment";

    private LinearLayout mobility_layout,allergy_layout,medicine_layout,script_layout;
    private RecyclerView medicine_holder,mobility_holder,allergy_holder;
    private ImageView script_image;

    enum ActivatedField
    {
        medicine,allergy,morbidity,scriptImage
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
        return inflater.inflate(R.layout.fragment_view_profile2, container, false);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setData(view);

         view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 requireActivity().getSupportFragmentManager().beginTransaction().
                         replace(R.id.main_frame_layout,new SettingFragment()).addToBackStack(null).commit();
             }
         });

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setData(View view)
    {
        TextView firstName,surName,idNumber,phoneNumber,email,age;
        ImageView profile_image;

        firstName=view.findViewById(R.id.firstName);
        surName=view.findViewById(R.id.surName);
        idNumber=view.findViewById(R.id.idNumber);
        phoneNumber=view.findViewById(R.id.phoneNumber);
        profile_image=view.findViewById(R.id.profile_image);
        email=view.findViewById(R.id.email);
        mobility_layout=view.findViewById(R.id.mobility_layout);
        allergy_layout=view.findViewById(R.id.allergy_layout);
        medicine_layout=view.findViewById(R.id.medicine_layout);
        medicine_holder=view.findViewById(R.id.medicine_holder);
        mobility_holder=view.findViewById(R.id.mobility_holder);
        allergy_holder=view.findViewById(R.id.allergy_holder);
        script_layout=view.findViewById(R.id.script_layout);
        script_image=view.findViewById(R.id.script_image);
        age=view.findViewById(R.id.age);
        

        SharedPreferences preferences= requireContext().getSharedPreferences(ClientMainFrame.client_information_file, Context.MODE_PRIVATE);
        String json = preferences.getString(ClientMainFrame.client_information_key, "");
        Gson gson = new Gson();
        ClientInformation clientInformation = gson.fromJson(json, ClientInformation.class);

        firstName.setText(clientInformation.getFirstName());
        surName.setText(clientInformation.getSurName());
        idNumber.setText(clientInformation.getIdNumber());
        phoneNumber.setText(clientInformation.getPhoneNumber());
        email.setText(clientInformation.getEmail());


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

        }
        catch (Exception e)
        {
            Log.d(TAG, "bottomSheetHandler: " + e.getMessage());
        }

         if(clientInformation.getJourneyInformationStatus().equals("1"))
         {
             if(clientInformation.getAllergieList()!=null)
             {
                 activateField(ActivatedField.allergy,clientInformation);
             }
             else
             {
                 allergy_layout.setVisibility(View.GONE);
             }
             if(clientInformation.getMorbiditiesList()!=null)
             {
                 activateField(ActivatedField.morbidity,clientInformation);
             }
             else
             {
                 medicine_layout.setVisibility(View.GONE);
             }
             if(clientInformation.getMedicationsList()!=null)
             {
                 activateField(ActivatedField.medicine,clientInformation);
             }
             else
             {
                 mobility_layout.setVisibility(View.GONE);
             }
             if(clientInformation.getScript_image().length()!=0)
             {
                 activateField(ActivatedField.scriptImage,clientInformation);
             }
             else
             {
                 script_layout.setVisibility(View.GONE);
             }
         }


        Picasso.with(getContext()).load(Uri.parse(clientInformation.getProfileImage())).error(R.drawable.no).into(profile_image);
    }



    private void activateField(ActivatedField activatedField,ClientInformation clientInformation)
    {
        switch (activatedField)
        {
            case allergy:

                allergy_layout.setVisibility(View.VISIBLE);

                AllergyRecycleViewAdaptor allergyRecycleViewAdaptor=new AllergyRecycleViewAdaptor(clientInformation.getAllergieList());

                allergy_holder.setLayoutManager(new LinearLayoutManager(getContext()));

                allergy_holder.setAdapter(allergyRecycleViewAdaptor);

                break;

            case medicine:

                medicine_layout.setVisibility(View.VISIBLE);

                MedicationRecycleViewAdaptor medicationRecycleViewAdaptor=new MedicationRecycleViewAdaptor(clientInformation.getMedicationsList());

                medicine_holder.setLayoutManager(new LinearLayoutManager(getContext()));

                medicine_holder.setAdapter(medicationRecycleViewAdaptor);

                break;

            case morbidity:

                mobility_layout.setVisibility(View.VISIBLE);

                MorbidityRecycleViewAdaptor morbidityRecycleViewAdaptor=new MorbidityRecycleViewAdaptor(clientInformation.getMorbiditiesList());

                mobility_holder.setLayoutManager(new LinearLayoutManager(getContext()));

                mobility_holder.setAdapter(morbidityRecycleViewAdaptor);

                break;

            case scriptImage:

                script_layout.setVisibility(View.VISIBLE);

                Picasso.with(getContext()).load(Uri.parse(clientInformation.getScript_image())).error(R.drawable.no).into(script_image);

                script_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Dialog zoomImage=new Dialog(getContext());
                        zoomImage.setContentView(R.layout.zoom_image_layout);
                        zoomImage.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        zoomImage.setCanceledOnTouchOutside(true);
                        Window window=zoomImage.getWindow();
                        window.setGravity(Gravity.CENTER);
                        zoomImage.show();

                        ImageView imageView=zoomImage.findViewById(R.id.myZoomageView);

                        Picasso.with(getContext()).load(Uri.parse(clientInformation.getScript_image())).error(R.drawable.no).into(imageView);

                    }
                });

                break;

        }


    }
}