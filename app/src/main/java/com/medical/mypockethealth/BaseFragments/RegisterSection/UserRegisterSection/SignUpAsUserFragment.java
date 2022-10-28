
package com.medical.mypockethealth.BaseFragments.RegisterSection.UserRegisterSection;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;
import com.medical.mypockethealth.Adaptors.ProviderSection.EHRSection.DiagnosisRecycleViewAdaptor;
import com.medical.mypockethealth.Adaptors.UserSection.RegistrationSection.MedicalAidRecycleViewAdaptor;
import com.medical.mypockethealth.AgoraSection.VideoChatViewActivity;
import com.medical.mypockethealth.ApplicationBase.BaseActivity;
import com.medical.mypockethealth.BaseFragments.CongratulationsFragment;
import com.medical.mypockethealth.BaseFragments.LoginFragment;
import com.medical.mypockethealth.BaseFragments.RegisterSection.PolicyFragment;

import com.medical.mypockethealth.BaseFragments.VerificationFragment;
import com.medical.mypockethealth.Classes.ClientInformation.ClientInformation;
import com.medical.mypockethealth.Classes.DialogShower;
import com.medical.mypockethealth.Classes.MedicalAidInformation.MedicalAidInformation;
import com.medical.mypockethealth.Classes.NetworkSection.NetworkStatus;
import com.medical.mypockethealth.Classes.ProfessionalTypeInformation.ProfessionalTypeInformation;
import com.medical.mypockethealth.Classes.ProviderInformation.ProviderInformation;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.Classes.URLBuilder;
import com.medical.mypockethealth.ClientActivity.ClientMainFrame;
import com.medical.mypockethealth.R;
import com.medical.mypockethealth.Threads.BaseThreads.EmailPhoneCheckerCaller;
import com.medical.mypockethealth.Threads.BaseThreads.OtpCaller;
import com.medical.mypockethealth.Threads.BaseThreads.UserSection.MedicalAidCaller;
import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.medical.mypockethealth.Threads.BaseThreads.UserSection.UserRegisterCaller;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;


public class SignUpAsUserFragment extends Fragment implements MedicalAidCaller.CallbackFromMedicalAidCaller, OtpCaller.CallbackFromOtpCaller,
        EmailPhoneCheckerCaller.CallbackFromEmailCheckerCaller, MedicalAidRecycleViewAdaptor.callBackFromMedicalAidRecycleViewAdaptor,
        UserRegisterCaller.CallbackFromMerchantRegisterCaller{

    private static final String TAG = "SignUpAsUserFragment";

    private TextInputLayout first_name_layout, last_name_layout, medical_layout, phone_layout, password_layout, address_layout,
            city_layout, postal_layout, confirm_password_layout, id_number_layout, email_layout;

    private EditText choose_medial_aid, first_name, last_name, medicalNumber, phone, password, confirm_password, id_number,
            email, address, city, postalCode;

    private CardView choose_medical_layout;
    private TextView verify, resend_otp, set_Date;
    private RecyclerView medical_holder;
    private AutoCompleteTextView medical_aid_layout;
    private Dialog pop_up_box, otp_box_layout, loading_box;
    private ProgressBar progressBar;
    private int progressStatus = 100;
    private RelativeLayout progress_handler;

    private String filled_type = null, first_name_assign = null, last_name_assign = null, aid_number_assign = null, phone_assign = null,
            password_assign = null, confirm_password_assign = null, id_number_assign = null, entered_otp = null,
            verified_email = SignUpAsUserFragment.IdentityType.notSelected.toString(),
            email_backup = SignUpAsUserFragment.IdentityType.notSelected.toString(), email_assign = null, address_assign = null,
            city_assign = null, postal_assign = null, selected_age = null;

    private boolean yes_clicked = false, no_clicked = false, isFirstNameFilled = false, firstNameConfirmation = false, isLastNameFilled = false,
            lastNameConfirmation = false, isAidNumberFilled = false, aidNumberConfirmation = false, isIdNumberFilled = false,
            idNumberConfirmation = false, isPhoneFilled = false, phoneConfirmation = false, isPasswordFilled = false, passwordConfirmation = false,
            isConfirmPasswordFilled = false, confirmPasswordConfirmation = false, password_match_confirmation = false, isPassword_matched = false,
            isToggleSwitched = false, match_status = false, isEmailFilled = false, email_confirmation = false, isAddressFilled = false,
            address_confirmation = false, isCityFilled = false, city_confirmation = false, isPostalCodeFilled = false, postal_confirmation = false;

    private Button login;
    private ImageView loading, medical_list, pick_date;
    private RadioButton yes, no;
    public static String passwordBackup;
    public static String otp, requestId;
    private final Handler handler = new Handler();
    private ClientInformation clientInformation;
    private MedicalAidRecycleViewAdaptor medicalAidRecycleViewAdaptor;

    private static final int REQUEST_CHECK_SETTINGS = 1;
    private static final int REQUEST_GRANT_PERMISSION = 4;
    private FusedLocationProviderClient fusedLocationClient;
    LocationRequest locationRequest;
    public static Location currentLocation;
    private LocationCallback locationCallback;
    private ProviderInformation providerInformation;
    private List<ProfessionalTypeInformation> professionalTypeInformationList;


    @Override
    public void confirmationCallbackFromMedicalAidCaller(ResponseInformation responseInformation) {

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
    public void informationCallbackFromMedicalAidCaller(List<MedicalAidInformation> medicalAidInformation) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                medical_holder.setVisibility(View.VISIBLE);
                medicalAidRecycleViewAdaptor.loadData(medicalAidInformation);
                loading_box.dismiss();

            }
        });

    }

    @Override
    public void confirmation(ResponseInformation responseInformation) {
        handler.post(new Runnable() {
            @Override
            public void run() {

                loading.setVisibility(View.GONE);
                login.setVisibility(View.VISIBLE);

                if (responseInformation.getSuccess().equals(ResponseInformation.fail_response_type)) {


                    if (responseInformation.getMessage().equals(getString(R.string.ready_send))) {
                        VerificationFragment verificationFragment = new VerificationFragment();
                        Bundle bundle = new Bundle();

                        clientInformation.setRequestId(requestId);

                        bundle.putSerializable(VerificationFragment.data_key, clientInformation);
                        verificationFragment.setArguments(bundle);

                        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_holder,
                                verificationFragment).addToBackStack(null).commit();
                    } else {
                        Toast.makeText(getContext(), responseInformation.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                } else {

                    VerificationFragment verificationFragment = new VerificationFragment();
                    Bundle bundle = new Bundle();

                    clientInformation.setRequestId(responseInformation.getRequestId());

                    requestId = responseInformation.getRequestId();

                    bundle.putSerializable(VerificationFragment.data_key, clientInformation);
                    verificationFragment.setArguments(bundle);

                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_holder,
                            verificationFragment).addToBackStack(null).commit();
                }

            }
        });
    }

    @Override
    public void confirmationEmailCheckerCaller(ResponseInformation responseInformation) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                if (otp_box_layout.isShowing()) {
                    otp_box_layout.dismiss();
                }

                verify.setText(R.string.verify_email);

                if (responseInformation.getSuccess().equals(ResponseInformation.fail_response_type)) {

                    Toast.makeText(getContext(), responseInformation.getMessage(), Toast.LENGTH_SHORT).show();

                } else {

                    SignUpAsUserFragment.otp = responseInformation.getOtp();


                    otpHandler();

                }

            }
        });


    }

    private void otpHandler() {

        otp_box_layout.setContentView(R.layout.otp_validation_layout);
        otp_box_layout.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        otp_box_layout.setCanceledOnTouchOutside(false);
        Window window = otp_box_layout.getWindow();
        window.setGravity(Gravity.CENTER);
        otp_box_layout.show();

        TextView information = otp_box_layout.findViewById(R.id.otp_information);

        resend_otp = otp_box_layout.findViewById(R.id.resend_otp);
        progressBar = otp_box_layout.findViewById(R.id.progressBar2);
        progress_handler = otp_box_layout.findViewById(R.id.progress_handler);


        enableLoadAndResend();

        disableResendOption();

        progressHandler();

        resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                disableResendOption();
                progressHandler();
                resendOtpCaller();
            }
        });

        String value = "A verification code has been sent to your email ID (" + email_backup + ").Please enter the code below";

        information.setText(value);

        OtpTextView otpTextView = otp_box_layout.findViewById(R.id.otp_holder);

        otpTextView.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {

            }

            @Override
            public void onOTPComplete(@NotNull String otp) {

                entered_otp = otp;

                match_status = SignUpAsUserFragment.otp.equals(otp);
            }
        });


        otp_box_layout.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (entered_otp == null) {
                    Toast.makeText(getContext(), "Please enter your OTP", Toast.LENGTH_SHORT).show();

                } else {
                    if (match_status) {

                        Toast.makeText(getContext(), "Email is verified", Toast.LENGTH_SHORT).show();

                        verified_email = email_backup;

                        dismissBox();

                    } else {
                        Toast.makeText(getContext(), "The entered otp is incorrect", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    private void resendOtpCaller() {

        if (!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED)) {

            ClientInformation clientInformation = new ClientInformation();
            clientInformation.setEmail(email_backup);

            new Thread(new EmailPhoneCheckerCaller(clientInformation, SignUpAsUserFragment.this)).start();

        } else {

            DialogShower dialogShower = new DialogShower(R.layout.internet_error, getContext());
            dialogShower.showDialog();
        }

    }

    private void dismissBox() {
        verify.setVisibility(View.GONE);
        otp_box_layout.dismiss();

    }

    private void progressHandler() {

        progressStatus = 100;

        new Thread(new Runnable() {
            public void run() {
                while (progressStatus > 0) {
                    progressStatus -= 1;
                    handler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressStatus);
                            if (progressStatus == 0) {

                                enableResendOption();

                            }

                        }

                    });
                    try {
                        // Sleep for 200 milliseconds.
                        Thread.sleep(1005);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();

    }


    private void enableLoadAndResend() {
        progress_handler.setVisibility(View.VISIBLE);
    }

    private void enableResendOption() {
        resend_otp.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private void disableResendOption() {
        resend_otp.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void selectedCode(MedicalAidInformation medicalAidInformation) {

        choose_medial_aid.setText(medicalAidInformation.getTitle());
        medicalAidRecycleViewAdaptor.loadData(new ArrayList<>());
    }



    public enum IdentityType {
        yes, no, notSelected
    }


    public SignUpAsUserFragment() {
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
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        establishViews(view);

        disableMedicalNumberFiled();

        emailHandler();

        addressHandler();

        LabeledSwitch labeledSwitch = view.findViewById(R.id.toggle_switch);

        labeledSwitch.setOn(isToggleSwitched);

        CountryCodePicker codePicker;

        codePicker = view.findViewById(R.id.code_picker);

        medical_holder.setLayoutManager(new LinearLayoutManager(getContext()));

        medicalAidRecycleViewAdaptor = new MedicalAidRecycleViewAdaptor(new ArrayList<>(), SignUpAsUserFragment.this);

        medical_holder.setAdapter(medicalAidRecycleViewAdaptor);


        pick_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                datePickerBox();
            }
        });


        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED)) {

                    email_backup = email.getText().toString();
                    ClientInformation clientInformation = new ClientInformation();
                    clientInformation.setEmail(email_backup);

                    verify.setText(R.string.loading);

                    new Thread(new EmailPhoneCheckerCaller(clientInformation, SignUpAsUserFragment.this)).start();
                } else {

                    verify.setText(R.string.verify_email);

                    DialogShower dialogShower = new DialogShower(R.layout.internet_error, getContext());
                    dialogShower.showDialog();
                }


            }
        });


        medical_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getMedicalAidDetails();

                openLoadingBox();
            }
        });

        labeledSwitch.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {

                isToggleSwitched = isOn;

            }
        });

        view.findViewById(R.id.sign_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), "soon", Toast.LENGTH_SHORT).show();

//                requireActivity().getSupportFragmentManager().beginTransaction().
//                        replace(R.id.frame_holder,new LoginFragment()).addToBackStack(null).commit();
            }
        });

        view.findViewById(R.id.termsAndConditions).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString(PolicyFragment.data_key, URLBuilder.policy_link);

                PolicyFragment policyFragment = new PolicyFragment();
                policyFragment.setArguments(bundle);

                requireActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.frame_holder, policyFragment).addToBackStack(null).commit();

            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activateField(IdentityType.yes);

                enableMedicalNumberFiled();

                filled_type = IdentityType.yes.toString();

            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activateField(IdentityType.no);

                disableMedicalNumberFiled();

                medicalAidRecycleViewAdaptor.loadData(new ArrayList<>());

                filled_type = IdentityType.no.toString();

            }
        });


     //---------------------------------------------------------------------------------------------------------------------------//


        // this section was use to get real time location details after clicking on address edit-text


//        address.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED)) {
//
//                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
//                    createLocationRequest();
//                    settingsCheck();
//
//
//                    if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//                        Log.d(TAG, "onMenuItemClick: requesting call");
//                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_GRANT_PERMISSION);
//
//                    }
//                    if (locationCallback == null) {
//                        Log.d(TAG, "onMenuItemClick: location call back called");
//                        buildLocationCallback();
//                    }
//                    if (currentLocation == null) {
//                        Log.d(TAG, "onMenuItemClick: current call back");
//
//                        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
//                    } else {
//                        Log.d(TAG, "onMenuItemClick: calling nothing");
//                    }
//
//                } else {
//                    DialogShower dialogShower = new DialogShower(R.layout.internet_error, getContext());
//                    dialogShower.showDialog();
//                }
//
//            }
//        });



//------------------------------------------------------------------------------------------------------------------------------------//


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setDefault();

                if (first_name.getText().toString().trim().length() == 0) {

                    first_name_layout.setError("Please enter your first name");

                    isFirstNameFilled = false;

                } else if (profile_name_checker(first_name.getText().toString())) {
                    first_name_layout.setError("spacial characters and numbers not allowed");
                } else {

                    first_name_layout.setError(null);
                    first_name_assign = first_name.getText().toString();
                    firstNameConfirmation = true;
                    isFirstNameFilled = true;
                }


                if (last_name.getText().toString().trim().length() == 0) {

                    last_name_layout.setError("Please enter your surname");

                    isLastNameFilled = false;

                } else if (profile_name_checker(last_name.getText().toString())) {
                    last_name_layout.setError("spacial characters and numbers not allowed");
                } else {

                    last_name_layout.setError(null);
                    last_name_assign = last_name.getText().toString();
                    lastNameConfirmation = true;
                    isLastNameFilled = true;
                }


                if (email.getText().toString().trim().length() == 0) {

                    email_layout.setError("Please enter your email");

                    isEmailFilled = false;
                } else if (!emailPatternChecker(email.getText().toString())) {

                    email_layout.setError("Invalid email address");

                } else {
                    email.setError(null);
                    email_assign = email.getText().toString();
                    email_confirmation = true;
                    isEmailFilled = true;

                }


                if (medical_layout.isShown()) {
                    if (medicalNumber.getText().toString().trim().length() == 0) {

                        medical_layout.setError("Please enter your medical AID Number");

                        isAidNumberFilled = false;
                    } else {
                        medical_layout.setError(null);
                        aid_number_assign = medicalNumber.getText().toString();
                        aidNumberConfirmation = true;
                        isAidNumberFilled = true;

                    }
                }


                if (id_number.getText().toString().trim().length() == 0) {

                    id_number_layout.setError("Please enter your ID Number");

                    isIdNumberFilled = false;
                } else {
                    id_number_layout.setError(null);
                    id_number_assign = id_number.getText().toString();
                    idNumberConfirmation = true;
                    isIdNumberFilled = true;

                }


                if (address.getText().toString().trim().length() == 0) {
                    address_layout.setError("please enter your address");
                    isAddressFilled = false;
                } else {
                    address_layout.setError(null);

                    address_assign = address.getText().toString();

                    address_confirmation = true;

                    isAddressFilled = true;

                }


                if (city.getText().toString().trim().length() == 0) {
                    city_layout.setError("please enter your city");

                    isCityFilled = false;
                } else {
                    city.setError(null);

                    city_assign = city.getText().toString();

                    city_confirmation = true;

                    isCityFilled = true;

                }

                if (postalCode.getText().toString().trim().length() == 0) {
                    postal_layout.setError("please enter your postal code");

                    isPostalCodeFilled = false;
                } else {
                    postalCode.setError(null);

                    postal_assign = postalCode.getText().toString();

                    postal_confirmation = true;

                    isPostalCodeFilled = true;

                }

                if (phone.getText().toString().trim().length() == 0) {

                    phone_layout.setError("Please enter your phone number");

                    isPhoneFilled = false;
                } else if (!phonePatternChecker(phone.getText().toString())) {

                    phone_layout.setError("Invalid phone number");

                } else {
                    phone_layout.setError(null);

                    phone_assign = phone.getText().toString();
                    phoneConfirmation = true;
                    isPhoneFilled = true;

                }


                if (password.getText().toString().trim().length() == 0) {
                    password_layout.setError("please enter your password");

                    isPasswordFilled = false;
                } else {
                    password_layout.setError(null);

                    password_assign = password.getText().toString();

                    passwordConfirmation = true;

                    isPasswordFilled = true;

                }

                if (confirm_password.getText().toString().trim().length() == 0) {
                    confirm_password_layout.setError("please enter your confirm password");

                    isConfirmPasswordFilled = false;
                } else {
                    confirm_password_layout.setError(null);

                    confirm_password_assign = confirm_password.getText().toString();

                    confirmPasswordConfirmation = true;

                    isConfirmPasswordFilled = true;

                }

                if (passwordConfirmation && confirmPasswordConfirmation) {

                    if (password_assign.equals(confirm_password_assign)) {

                        password_match_confirmation = true;

                        isPassword_matched = true;
                    } else {
                        confirm_password_layout.setError("Password didn't matched");
                        isPassword_matched = false;
                    }
                }


                if (firstNameConfirmation && lastNameConfirmation && phoneConfirmation && confirmPasswordConfirmation &&
                        isFirstNameFilled && isLastNameFilled && isPhoneFilled && isConfirmPasswordFilled && isPassword_matched &&
                        isIdNumberFilled && idNumberConfirmation && isEmailFilled && email_confirmation && isAddressFilled && address_confirmation
                        && isCityFilled && city_confirmation && isPostalCodeFilled && postal_confirmation) {


                    @SuppressLint("HardwareIds") String android_id = Settings.
                            Secure.getString(requireContext().getContentResolver(),
                            Settings.Secure.ANDROID_ID);


                    clientInformation = new ClientInformation();

                    clientInformation.setFirstName(first_name_assign);
                    clientInformation.setSurName(last_name_assign);
                    clientInformation.setEmail(email_assign);
                    clientInformation.setAidNumber(aid_number_assign);
                    clientInformation.setIdNumber(id_number_assign);
                    clientInformation.setCountryCode(codePicker.getSelectedCountryCode());
                    clientInformation.setPhone(phone_assign);
                    clientInformation.setPassword(password_assign);
                    clientInformation.setAddress(address_assign);
                    clientInformation.setCity(city_assign);
                    clientInformation.setPostalCode(postal_assign);
                    passwordBackup = password_assign;
                    clientInformation.setDeviceId(android_id);
                    clientInformation.setDeviceType("android");
                    clientInformation.setRegId(getRegId());


                    if (!yes_clicked && !no_clicked) {
                        Toast.makeText(getContext(), "Do you have medical Aid", Toast.LENGTH_SHORT).show();
                    } else {
                        if (yes_clicked) {

                            if (choose_medial_aid.getText().toString().trim().length() != 0) {

                                clientInformation.setMedicalAid(choose_medial_aid.getText().toString());

                                if (aidNumberConfirmation && isAidNumberFilled) {
                                    clientInformation.setMedicalNumber(aid_number_assign);

                                    if (selected_age != null) {
                                        clientInformation.setPatientAge(selected_age);


                                        if (isToggleSwitched) {

                                            registerUser(clientInformation);   // this method is use for to do direct user registration without verification

//                                                requestForOtp();    // this was use to verify user phone number before registration

                                        } else {
                                            Toast.makeText(getContext(), "Kindly accept the MypocketHealth Terms of use & Privacy Policy", Toast.LENGTH_SHORT).show();
                                        }

  //------------------------------------------------------------------------------------------------------------------------------------//

                                        // this section was use to verify the email than make a patient register


//                                        if (!verify.isShown()) {
//                                            if (isToggleSwitched) {
//
//                                                registerUser(clientInformation);   // this method is use for to do direct user registration without verification
//
////                                                requestForOtp();    // this was use to verify user phone number before registration
//
//                                            } else {
//                                                Toast.makeText(getContext(), "Kindly accept the MypocketHealth Terms of use & Privacy Policy", Toast.LENGTH_SHORT).show();
//                                            }
//                                        } else {
//                                            Toast.makeText(getContext(), "Please verify your email", Toast.LENGTH_SHORT).show();
//                                        }


 //----------------------------------------------------------------------------------------------------------------------------------------//



                                        if (isToggleSwitched) {

                                            requestForOtp();

                                        } else {
                                            Toast.makeText(getContext(), "Kindly accept the MypocketHealth Terms of use & Privacy Policy", Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        Toast.makeText(getContext(), "Please select date of birth", Toast.LENGTH_SHORT).show();
                                    }

                                }

                            } else {
                                Toast.makeText(getContext(), "Please select medical Aid", Toast.LENGTH_SHORT).show();
                            }

                        } else {

                            clientInformation.setMedicalAid(SignUpAsUserFragment.IdentityType.notSelected.toString());
                            clientInformation.setAidNumber(SignUpAsUserFragment.IdentityType.notSelected.toString());


                            if (selected_age != null) {

                                clientInformation.setPatientAge(selected_age);

                                verify.setVisibility(View.GONE);

                                if (!verify.isShown()) {

                                    if (isToggleSwitched) {

                                        registerUser(clientInformation);   // this method is use for to do direct user registration without verification

//                                                requestForOtp();    // this was use to verify user phone number before registration


                                    } else {
                                        Toast.makeText(getContext(), "Kindly accept the MypocketHealth Terms of use & Privacy Policy", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(getContext(), "Please verify your email", Toast.LENGTH_SHORT).show();
                                }


                            } else {
                                Toast.makeText(getContext(), "Please select date of birth", Toast.LENGTH_SHORT).show();
                            }


                        }
                    }


                }


            }
        });


    }


    private void registerUser(ClientInformation clientInformation)
    {

        if (!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED)) {

            openLoadingBox();

            new Thread(new UserRegisterCaller(clientInformation, SignUpAsUserFragment.this)).start();

        } else {
            DialogShower dialogShower = new DialogShower(R.layout.internet_error, getContext());
            dialogShower.showDialog();
        }

    }


    @Override
    public void confirmationUserRegisterCaller(ResponseInformation responseInformation) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                loading_box.dismiss();

                if (responseInformation.getSuccess().equals(ResponseInformation.fail_response_type)) {

                    Toast.makeText(getContext(), responseInformation.getMessage(), Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    @Override
    public void informationUserRegisterCaller(ClientInformation clientInformation) {

        addDataIntoSharedPreferencesAsUser(clientInformation);

        handler.post(new Runnable() {
            @Override
            public void run() {

               loading_box.dismiss();

                requireActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.frame_holder, new CongratulationsFragment()).addToBackStack(null).commit();

            }
        });


    }

    private void addDataIntoSharedPreferencesAsUser(ClientInformation clientInformation) {

        ClientMainFrame.id = clientInformation.getId();

        clientInformation.setPasswordBackup(SignUpAsUserFragment.passwordBackup);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(ClientMainFrame.client_information_file, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();

        String json = gson.toJson(clientInformation);

        editor.putString(ClientMainFrame.client_information_key, json);

        editor.apply();

    }



    private void datePickerBox() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog mDatePicker = new DatePickerDialog(getContext(), R.style.dialog_theme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datepicker, int selected_year, int selected_month, int selected_day) {

                String month2 = String.valueOf(selected_month + 1);

                selected_age = selected_year + "-" + month2 + "-" + selected_day;

                setFilteredDate(String.valueOf(selected_year), String.valueOf(selected_month), String.valueOf(selected_day));


            }
        }, year, month, day);

        mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());

        mDatePicker.show();

    }


    private void setFilteredDate(String year, String month, String day) {

        String result = "";

        try {
            result = requireContext().getResources().getStringArray(R.array.month_names)[Integer.parseInt(month)];
        } catch (ArrayIndexOutOfBoundsException e) {
            result = Integer.toString(Integer.parseInt(month));
        }


        String date = day + " , " + result + " " + year;

        set_Date.setText(date);


    }

    private List<Address> getLocationDetails(String lat, String log) {

        Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(Double.parseDouble(lat), Double.parseDouble(log), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert addresses != null;
        return addresses.size() > 0 ? addresses : null;
    }

    protected void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public void settingsCheck() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(requireContext());
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(requireActivity(), new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                Log.d("TAG", "onSuccess: settingsCheck");
                getCurrentLocation();
            }
        });

        task.addOnFailureListener(requireActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.

                    Log.d("TAG", "onFailure: settingsCheck");
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(requireActivity(),
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }


    public void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {

                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_GRANT_PERMISSION);

            } else {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                        .setMessage("Click ok than select permissions and set allow location").setNegativeButton("CANCEL", (dialogInterface, i) -> {

                            Log.d(TAG, "onRequestPermissionsResult: canceled");

                        }).setPositiveButton("OK", (dialogInterface, i) -> {

                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", requireActivity().getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        });
                builder.setCancelable(false);
                builder.show();


            }
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Log.d("TAG", "onSuccess: getLastLocation");
                        // Got last known location. In some rare situations this can be null.

                        if (location != null) {
                            currentLocation = location;

                            Log.d(TAG, "onSuccess: " + currentLocation.getLatitude()); // call second time

                            List<Address> addresses = getLocationDetails(String.valueOf(currentLocation.getLatitude()), String.valueOf(currentLocation.getLongitude()));

                            if (addresses != null) {

                                city.setText(addresses.get(0).getSubAdminArea());
                                postalCode.setText(addresses.get(0).getPostalCode());
                                address.setText(addresses.get(0).getAddressLine(0));
                                address.setFocusableInTouchMode(true);

                                city_layout.setError(null);
                                address_layout.setError(null);
                                postal_layout.setError(null);

                            }
                            // run threads
                        } else {
                            Log.d("TAG", "location is null");
                            buildLocationCallback();
                        }
                    }
                });
    }

    private void buildLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    currentLocation = location;


                }

                Log.d(TAG, "onLocationResult: " + currentLocation.getLatitude());     // first time get result

                List<Address> addresses = getLocationDetails(String.valueOf(currentLocation.getLatitude()), String.valueOf(currentLocation.getLongitude()));

                if (addresses != null) {


                }


                // run threads


            }

            ;
        };
    }

    private void openLoadingBox() {

        loading_box.setContentView(R.layout.medical_loading_layout);
        loading_box.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading_box.setCanceledOnTouchOutside(false);
        loading_box.setCancelable(false);
        Window window = loading_box.getWindow();
        window.setGravity(Gravity.CENTER);
        loading_box.show();

    }


    private void addressHandler() {

        address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if (address.getText().toString().trim().length() != 0) {
                    address.setError(null);
                } else {
                    address_layout.setError("please enter your address");
                }


            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });


    }

    private void emailHandler() {

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if (emailPatternChecker(s.toString())) {
                    email_layout.setError(null);


                } else {

                    email_layout.setError("Invalid email address");
                    verify.setVisibility(View.GONE);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });


    }


    private boolean emailPatternChecker(String email) {


        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());

    }


    private String getRegId() {

        return FirebaseInstanceId.getInstance().getToken();

    }

    private void requestForOtp() {
        if (!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED)) {

            loading.setVisibility(View.VISIBLE);
            login.setVisibility(View.GONE);

            new Thread(new OtpCaller(clientInformation, SignUpAsUserFragment.this)).start();

        } else {
            DialogShower dialogShower = new DialogShower(R.layout.internet_error, getContext());
            dialogShower.showDialog();
        }
    }

    private void getMedicalAidDetails() {
        if (!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED)) {

            new Thread(new MedicalAidCaller(SignUpAsUserFragment.this)).start();

        } else {
            DialogShower dialogShower = new DialogShower(R.layout.internet_error, getContext());
            dialogShower.showDialog();
        }
    }

    private boolean phonePatternChecker(String phone) {


        return (!TextUtils.isEmpty(phone) && Patterns.PHONE.matcher(phone).matches());

    }

    public boolean profile_name_checker(String value) {
        boolean result = false;
        Pattern pattern = Pattern.compile("[^a-zA-Z\\s]");
        Matcher matcher = pattern.matcher(value);

        while (matcher.find()) {
            result = true;

        }
        return result;
    }

    private void establishViews(View view) {

        yes = view.findViewById(R.id.personal);
        no = view.findViewById(R.id.business);
        choose_medial_aid = view.findViewById(R.id.choose_medial_aid);
        medical_list = view.findViewById(R.id.medical_list);
        medical_holder = view.findViewById(R.id.medical_holder);
        choose_medical_layout = view.findViewById(R.id.choose_medical_layout);
        first_name_layout = view.findViewById(R.id.first_name_layout);
        id_number_layout = view.findViewById(R.id.id_number_layout);
        last_name_layout = view.findViewById(R.id.last_name_layout);
        medical_layout = view.findViewById(R.id.medical_layout);
        phone_layout = view.findViewById(R.id.phone_layout);
        password_layout = view.findViewById(R.id.password_layout);
        confirm_password_layout = view.findViewById(R.id.confirm_password_layout);
        first_name = view.findViewById(R.id.first_name);
        last_name = view.findViewById(R.id.last_name);
        id_number = view.findViewById(R.id.id_number);
        medicalNumber = view.findViewById(R.id.medicalNumber);
        phone = view.findViewById(R.id.phone);
        password = view.findViewById(R.id.password);
        confirm_password = view.findViewById(R.id.confirm_password);
        login = view.findViewById(R.id.login);
        loading = view.findViewById(R.id.loading);
        medical_aid_layout = view.findViewById(R.id.medical_aid);
        email_layout = view.findViewById(R.id.email_layout);
        email = view.findViewById(R.id.email);
        verify = view.findViewById(R.id.verify);
        address_layout = view.findViewById(R.id.address_layout);
        city_layout = view.findViewById(R.id.city_layout);
        postal_layout = view.findViewById(R.id.postal_layout);
        address = view.findViewById(R.id.address);
        city = view.findViewById(R.id.city);
        postalCode = view.findViewById(R.id.postalCode);
        set_Date = view.findViewById(R.id.set_Date);
        pick_date = view.findViewById(R.id.pick_date);
        otp_box_layout = new Dialog(getContext());
        loading_box = new Dialog(getContext());

    }


    private void setDefault() {

        first_name_layout.setError(null);
        last_name_layout.setError(null);
        medical_layout.setError(null);
        phone_layout.setError(null);
        password_layout.setError(null);
        confirm_password_layout.setError(null);
        address_layout.setError(null);
        city_layout.setError(null);
        postal_layout.setError(null);
        email_layout.setError(null);
        filled_type = null;
        first_name_assign = null;
        id_number_assign = null;
        last_name_assign = null;
        aid_number_assign = null;
        phone_assign = null;
        password_assign = null;
        confirm_password_assign = null;
        isFirstNameFilled = false;
        firstNameConfirmation = false;
        isIdNumberFilled = false;
        idNumberConfirmation = false;
        isLastNameFilled = false;
        lastNameConfirmation = false;
        isAidNumberFilled = false;
        aidNumberConfirmation = false;
        isPhoneFilled = false;
        phoneConfirmation = false;
        isPasswordFilled = false;
        passwordConfirmation = false;
        isConfirmPasswordFilled = false;
        confirmPasswordConfirmation = false;
        password_match_confirmation = false;
        isPassword_matched = false;
        address_assign = null;
        city_assign = null;
        postal_assign = null;
        isAddressFilled = false;
        address_confirmation = false;
        isCityFilled = false;
        city_confirmation = false;
        isPostalCodeFilled = false;
        postal_confirmation = false;

    }

    private void enableMedicalNumberFiled() {

        choose_medical_layout.setVisibility(View.VISIBLE);
        medical_layout.setVisibility(View.VISIBLE);


    }

    private void disableMedicalNumberFiled() {

        choose_medical_layout.setVisibility(View.GONE);
        medical_layout.setVisibility(View.GONE);


    }

    private void activateField(IdentityType identityType) {
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

}