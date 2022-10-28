package com.medical.mypockethealth.BaseFragments.RegisterSection.ProviderRegistrationSection;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.github.gcacace.signaturepad.views.SignaturePad;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;
import com.medical.mypockethealth.ApplicationBase.BaseActivity;
import com.medical.mypockethealth.BaseFragments.CongratulationsFragment;
import com.medical.mypockethealth.BaseFragments.RegisterSection.PolicyFragment;
import com.medical.mypockethealth.BaseFragments.RegisterSection.ProviderTypeFragment;
import com.medical.mypockethealth.BaseFragments.RegisterSection.UserRegisterSection.SignUpAsUserFragment;
import com.medical.mypockethealth.BaseFragments.VerificationFragment;
import com.medical.mypockethealth.Classes.ClientInformation.ClientInformation;
import com.medical.mypockethealth.Classes.ConsultChargesInformation.ChargesInformation;
import com.medical.mypockethealth.Classes.DialogShower;
import com.medical.mypockethealth.Classes.NetworkSection.NetworkStatus;
import com.medical.mypockethealth.Classes.ProfessionalTypeInformation.ProfessionalTypeInformation;
import com.medical.mypockethealth.Classes.ProviderInformation.ProviderInformation;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.Classes.SpecialitiesInformation.SpecialitiesInformation;
import com.medical.mypockethealth.Classes.URLBuilder;
import com.medical.mypockethealth.ProviderActivity.ProviderMainFrame;
import com.medical.mypockethealth.R;
import com.medical.mypockethealth.Threads.BaseThreads.EmailPhoneCheckerCaller;
import com.medical.mypockethealth.Threads.BaseThreads.OtpCaller;
import com.medical.mypockethealth.Threads.BaseThreads.ProviderSection.ConsultChargesCaller;
import com.medical.mypockethealth.Threads.BaseThreads.ProviderSection.ProviderRegisterCaller;
import com.medical.mypockethealth.Threads.ProviderSection.ProfileSection.SpecialitiesTypeFetcher;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

public class SignUpAsProviderFragment extends Fragment implements SpecialitiesTypeFetcher.CallbackFromRegisterAsCategoriesFetcher,
        OtpCaller.CallbackFromOtpCaller, EmailPhoneCheckerCaller.CallbackFromEmailCheckerCaller, ConsultChargesCaller.CallbackFromConsultChargesCaller,
        ProviderRegisterCaller.CallbackFromProviderRegisterCaller, AdapterView.OnItemSelectedListener {

    private static final String TAG = "SignUpAsProviderFlagmen";
    private static final int REQUEST_CHECK_SETTINGS = 1;
    private static final int REQUEST_GRANT_PERMISSION = 4;
    public static String passwordBackup;
    public static Location currentLocation;
    private final Handler handler = new Handler();
    LocationRequest locationRequest;
    private EditText email, firstName, lastName, phone, address, city, postalCode, experience, professionalRegistrationNumber,
            department, workLocation, password, practice_number;
    private Button signUp;
    private TextView add_fica_documents, add_registration_documents, verify, resend_otp, loadingProgressText;
    private ImageView loading, profile_image, upload_image;
    private Bundle bundle;
    private AutoCompleteTextView medical_aid, consult_charges, provider_type;
    private AutoCompleteTextView autoCompleteTextView;
    private Dialog pop_up_box, otp_box_layout;
    private RadioButton yes, no;
    private ProgressBar progressBar;
    private int progressStatus = 100;
    private BottomSheetDialog signatureSheet;
    private RelativeLayout progress_handler;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private ProviderInformation providerInformation;
    private Dialog loading_box;
    private List<ProfessionalTypeInformation> professionalTypeInformationList;
    private final String[] titles_list = new String[8];
    private ArrayAdapter<String> spinnerTitlesAdapter;

    private Spinner spinner_title;

    private boolean location_status = false, isEmailFilled = false, email_confirmation = false, isNameFilled = false, name_confirmation = false,
            isSurNameFilled = false, surName_confirmation = false, isPhoneNumberFilled = false, phone_confirmation = false, isAddressFilled = false,
            address_confirmation = false, isCityFilled = false, city_confirmation = false, isPostalCodeFilled = false, postal_confirmation = false,
            isRegistrationFilled = false, registration_confirmation = false, yes_clicked = false, no_clicked = false,
            isToggleSwitched = false, isPasswordFilled = false, password_confirmation = false, match_status = false, isExperienceFilled = false,
            experience_confirmation = false, signStatus = false, isPracticeNumberFilled = false, practice_confirmation = false;

    private String email_assign = null, name_assign = null, surName_assign = null, phone_assign = null, address_assign = null, city_assign = null,
            postal_assign = null, registration_assign = null, experience_assign = null,
            selected_image_path = null, filled_type = null, selected_fica_documents = null, selected_registration_documents = null,
            password_assign = null, email_backup = SignUpAsUserFragment.IdentityType.notSelected.toString(),
            verified_email = SignUpAsUserFragment.IdentityType.notSelected.toString(), entered_otp = null,
            fica_document_name = null, registration_document_name = null,
            selected_signature_path = null, profile_image_name = null, practice_assign = null, provider_title = null;


    public SignUpAsProviderFragment() {
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
        return inflater.inflate(R.layout.fragment_sign_up_as_provider, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        establishViews(view);

        titles_list[0] = "Dr.";
        titles_list[1] = "Nur.";
        titles_list[2] = "Mr.";
        titles_list[3] = "Ms.";
        titles_list[4] = "Prof.";
        titles_list[5] = "Sr.";
        titles_list[6] = "Mrs.";
        titles_list[7] = "Miss.";

        spinner_title.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        spinnerTitlesAdapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, titles_list);
        spinnerTitlesAdapter.setDropDownViewResource(R.layout.spinner_dropdown_lay);
        //Setting the ArrayAdapter data on the Spinner
        spinner_title.setAdapter(spinnerTitlesAdapter);

        setItem();

        getSpecialityList();

        getConsultChargesList();

        emailHandler();

        addressHandler();

        verify.setVisibility(View.GONE);

        CountryCodePicker codePicker;
        codePicker = view.findViewById(R.id.code_picker);

        LabeledSwitch labeledSwitch = view.findViewById(R.id.toggle_switch);

        labeledSwitch.setOn(isToggleSwitched);


        labeledSwitch.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {

                isToggleSwitched = isOn;

            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activateField(SignUpAsUserFragment.IdentityType.yes);

                filled_type = SignUpAsUserFragment.IdentityType.yes.toString();

            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activateField(SignUpAsUserFragment.IdentityType.no);

                filled_type = SignUpAsUserFragment.IdentityType.no.toString();

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

                    new Thread(new EmailPhoneCheckerCaller(clientInformation, SignUpAsProviderFragment.this)).start();

                } else {

                    verify.setText(R.string.verify_email);

                    DialogShower dialogShower = new DialogShower(R.layout.internet_error, getContext());
                    dialogShower.showDialog();
                }


            }
        });


        //----------------------------------------------------------------------------------------------------------------------------------------//


        // this was use to fetch location details by clicking on address edit-text


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


        //-------------------------------------------------------------------------------------------------------------------------------------------//

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


        view.findViewById(R.id.select_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int permission = ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

                if (permission != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                            , Manifest.permission.WRITE_EXTERNAL_STORAGE}, 5);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");

                    startActivityForResult(intent, 5);

                }

            }
        });

        view.findViewById(R.id.upload_fica).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dateDialog = new Dialog(getContext());
                dateDialog.setContentView(R.layout.pdf_warning_layout);
                dateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dateDialog.setCanceledOnTouchOutside(false);
                Window window = dateDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                dateDialog.show();

                dateDialog.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dateDialog.dismiss();

                        int permission = ActivityCompat.checkSelfPermission(requireContext(),
                                Manifest.permission.READ_EXTERNAL_STORAGE);

                        if (permission != PackageManager.PERMISSION_GRANTED) {

                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                                    , Manifest.permission.WRITE_EXTERNAL_STORAGE}, 6);

                        } else {

                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("application/pdf");
                            startActivityForResult(intent, 6);

                        }

                    }
                });

            }
        });

        view.findViewById(R.id.upload_registration).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                informationBox();

            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                setDefault();


                if (email.getText().toString().trim().length() == 0) {

                    email.setError("Please enter your email");

                    isEmailFilled = false;
                } else if (!emailPatternChecker(email.getText().toString())) {

                    email.setError("Invalid email address");

                } else {

                    email.setError(null);
                    email_assign = email.getText().toString();
                    email_confirmation = true;
                    isEmailFilled = true;

                }

                if (firstName.getText().toString().trim().length() == 0) {

                    firstName.setError("Please enter your first name");

                    isNameFilled = false;

                } else if (profile_name_checker(firstName.getText().toString())) {
                    firstName.setError("spacial characters and numbers not allowed");
                } else {

                    firstName.setError(null);
                    name_assign = firstName.getText().toString();
                    name_confirmation = true;
                    isNameFilled = true;
                }


                if (lastName.getText().toString().trim().length() == 0) {

                    lastName.setError("Please enter your surname");

                    isSurNameFilled = false;

                } else if (profile_name_checker(lastName.getText().toString())) {
                    lastName.setError("spacial characters and numbers not allowed");
                } else {

                    lastName.setError(null);
                    surName_assign = lastName.getText().toString();
                    surName_confirmation = true;
                    isSurNameFilled = true;
                }


                if (phone.getText().toString().trim().length() == 0) {

                    phone.setError("Please enter your phone number");

                    isPhoneNumberFilled = false;
                } else if (!phonePatternChecker(phone.getText().toString())) {

                    phone.setError("Invalid phone number");

                } else {
                    phone.setError(null);

                    phone_assign = phone.getText().toString();
                    phone_confirmation = true;
                    isPhoneNumberFilled = true;

                }

                if (address.getText().toString().trim().length() == 0) {
                    address.setError("please enter your address");
                    isAddressFilled = false;
                } else {
                    address.setError(null);

                    address_assign = address.getText().toString();

                    address_confirmation = true;

                    isAddressFilled = true;

                }

                if (city.getText().toString().trim().length() == 0) {
                    city.setError("please enter your city");

                    isCityFilled = false;
                } else {
                    city.setError(null);

                    city_assign = city.getText().toString();

                    city_confirmation = true;

                    isCityFilled = true;

                }

                if (postalCode.getText().toString().trim().length() == 0) {
                    postalCode.setError("please enter your postal code");

                    isPostalCodeFilled = false;
                } else {
                    postalCode.setError(null);

                    postal_assign = postalCode.getText().toString();

                    postal_confirmation = true;

                    isPostalCodeFilled = true;

                }


                if (practice_number.getText().toString().trim().length() == 0) {
                    practice_number.setError("please enter your practice number");

                    isPracticeNumberFilled = false;
                } else {
                    practice_number.setError(null);

                    practice_assign = practice_number.getText().toString();

                    practice_confirmation = true;

                    isPracticeNumberFilled = true;

                }


                if (professionalRegistrationNumber.getText().toString().trim().length() == 0) {
                    professionalRegistrationNumber.setError("please enter your registration number");

                    isRegistrationFilled = false;
                } else {
                    professionalRegistrationNumber.setError(null);

                    registration_assign = professionalRegistrationNumber.getText().toString();

                    registration_confirmation = true;

                    isRegistrationFilled = true;

                }

                if (experience.getText().toString().trim().length() == 0) {
                    experience.setError("please enter your experience");

                    isExperienceFilled = false;
                } else {
                    experience.setError(null);

                    experience_assign = experience.getText().toString();

                    experience_confirmation = true;

                    isExperienceFilled = true;

                }

                if (password.getText().toString().trim().length() == 0) {
                    password.setError("please enter your password");

                    isPasswordFilled = false;
                } else {
                    password.setError(null);

                    password_assign = password.getText().toString();

                    password_confirmation = true;

                    isPasswordFilled = true;

                }


                if (name_confirmation && isNameFilled && email_confirmation && isEmailFilled && surName_confirmation &&
                        isSurNameFilled && phone_confirmation && isPhoneNumberFilled && address_confirmation && isAddressFilled &&
                        city_confirmation && isCityFilled && postal_confirmation && isPostalCodeFilled
                        && registration_confirmation && isRegistrationFilled && password_confirmation &&
                        isPasswordFilled && isExperienceFilled && experience_confirmation && isPracticeNumberFilled && practice_confirmation) {

                    @SuppressLint("HardwareIds") String android_id = Settings.Secure.getString(requireContext().getContentResolver(), Settings.Secure.ANDROID_ID);

                    providerInformation = new ProviderInformation();
                    providerInformation.setEmail(email_assign);
                    providerInformation.setFirstName(name_assign);
                    providerInformation.setSurName(surName_assign);
                    providerInformation.setCountryCode(codePicker.getSelectedCountryCode());
                    providerInformation.setPhoneNumber(phone_assign);
                    providerInformation.setAddress(address_assign);
                    providerInformation.setPracticeNumberPhone(getString(R.string.None));
                    providerInformation.setCity(city_assign);
                    providerInformation.setPostalCode(postal_assign);
                    providerInformation.setPracticeNumber(practice_assign);
                    providerInformation.setProfessionalRegistrationNumber(registration_assign);
                    providerInformation.setUserTitle(provider_title);


                    //                    doctor, nurse, physiotherapists
//                    if (ProviderTypeFragment.providerTypeString.equalsIgnoreCase("doctor")) {
//
//                        providerInformation.setUserType("doctor");
//
//                    } else if (ProviderTypeFragment.providerTypeString.equalsIgnoreCase("nurse")) {
//
//                        providerInformation.setUserType("nurse");
//
//                    } else if (ProviderTypeFragment.providerTypeString.equalsIgnoreCase("physiotherapists")) {
//
//                        providerInformation.setUserType("Mental Healthcare Worker");
//
//                    }

                    providerInformation.setUserType("provider");

                    if (department.getText().toString().length() != 0) {
                        providerInformation.setDepartment(department.getText().toString());
                    } else {
                        providerInformation.setDepartment(getString(R.string.None));
                    }

                    if (workLocation.getText().toString().length() != 0) {
                        providerInformation.setWorkLocation(workLocation.getText().toString());
                    } else {
                        providerInformation.setWorkLocation(getString(R.string.None));
                    }

                    providerInformation.setPassword(password_assign);
                    providerInformation.setExperience(experience_assign);
                    providerInformation.setRegId(getRegId());
                    providerInformation.setDeviceId(android_id);
                    providerInformation.setDeviceType("android");
                    providerInformation.setLat("0.0");
                    providerInformation.setLog("0.0");

                    //-----------------------------------------------------------------------------------------------------------------------------------//

                    // this section was use to set lat and log in object to send it to the back-end

//                    providerInformation.setLat(String.valueOf(currentLocation.getLatitude()));
//                    providerInformation.setLog(String.valueOf(currentLocation.getLongitude()));

                    //-------------------------------------------------------------------------------------------------------------------------------------//


                    providerInformation.setProfessionalType("1");


                    if (selected_image_path != null) {

                        providerInformation.setProfileImage(selected_image_path);
                        providerInformation.setProfileImageName(profile_image_name);

                    } else {
                        providerInformation.setProfileImage(SignUpAsUserFragment.IdentityType.notSelected.toString());
                    }


                    if (!medical_aid.getText().toString().equals("specialization")) {
                        providerInformation.setSpecialization(medical_aid.getText().toString());

                        if (!consult_charges.getText().toString().equals("Consult Charges")) {

                            providerInformation.setConsultCharges(consult_charges.getText().toString());

                            if (selected_fica_documents != null) {
                                providerInformation.setFicaDocument(selected_fica_documents);
                                providerInformation.setFicaDocumentName(fica_document_name);

                                if (selected_registration_documents != null) {
                                    providerInformation.setRegistrationDocument(selected_registration_documents);
                                    providerInformation.setRegistrationDocumentName(registration_document_name);

                                    if (!yes_clicked && !no_clicked) {
                                        Toast.makeText(getContext(), "Do you want to do online OPD", Toast.LENGTH_SHORT).show();
                                    } else {

                                        if (yes_clicked) {
                                            providerInformation.setOnlineOPDStatus("1");
                                        } else {
                                            providerInformation.setOnlineOPDStatus("0");
                                        }

                                        if (verify.isShown()) {
                                            Toast.makeText(getContext(), "Please verify your email", Toast.LENGTH_SHORT).show();
                                        } else {
                                            if (isToggleSwitched) {

                                                openSignatureSheet();

                                            } else {
                                                Toast.makeText(getContext(), "Kindly accept the My pocket Health Terms of use & Privacy Policy", Toast.LENGTH_SHORT).show();
                                            }
                                        }


                                    }

                                } else {

                                    Toast.makeText(getContext(), "Kindly upload your registration documents", Toast.LENGTH_SHORT).show();
                                }


                            } else {
                                Toast.makeText(getContext(), "Kindly upload your FICA documents", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Please select your consult charges", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Kindly select your speciality", Toast.LENGTH_SHORT).show();
                    }


                }


            }
        });
    }

    private void setItem() {

        if (ProviderTypeFragment.providerTypeString.equalsIgnoreCase("doctor")) {

            provider_title = "Dr.";

        } else if (ProviderTypeFragment.providerTypeString.equalsIgnoreCase("nurse")) {

            provider_title = "Nur.";

        } else if (ProviderTypeFragment.providerTypeString.equalsIgnoreCase("physiotherapists")) {

            provider_title = titles_list[0];

            spinner_title.setSelection(0);

        } else {

            provider_title = titles_list[0];

            spinner_title.setSelection(0);

        }

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
                    address.setError("please enter your address");
                }


            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });


    }

    private void openSignatureSheet() {

        signatureSheet = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme);

        View bottom_view = LayoutInflater.from(getContext()).inflate(R.layout.signature_layout
                ,
                (RelativeLayout) signatureSheet.findViewById(R.id.relative_layout));

        signatureSheet.setContentView(bottom_view);

        signatureSheet.show();

        signatureSheet.setCanceledOnTouchOutside(false);
        signatureSheet.setCancelable(false);

        signatureSheet.findViewById(R.id.create_signature).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createSignatureSheet();
            }
        });

        signatureSheet.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signatureSheet.dismiss();

            }
        });


    }

    private void uploadSignatureSheet() {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Objects.requireNonNull(Objects.requireNonNull(requireContext())), R.style.BottomSheetDialogTheme);

        View bottom_view = LayoutInflater.from(getContext()).inflate(R.layout.upload_signature_layout
                ,
                (RelativeLayout) bottomSheetDialog.findViewById(R.id.relative_layout));

        bottomSheetDialog.setContentView(bottom_view);

        bottomSheetDialog.show();

        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.setCancelable(false);

        selected_signature_path = null;

        upload_image = bottomSheetDialog.findViewById(R.id.upload_image);

        assert upload_image != null;

        upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int permission = ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

                if (permission != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                            , Manifest.permission.WRITE_EXTERNAL_STORAGE}, 8);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");

                    startActivityForResult(intent, 8);


                }

            }
        });


        bottomSheetDialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetDialog.dismiss();

            }
        });

        bottomSheetDialog.findViewById(R.id.upload_signature).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selected_signature_path != null) {
                    providerInformation.setUploadedSignature(selected_signature_path);

                    signatureSheet.dismiss();
                    bottomSheetDialog.dismiss();


                    popUpBox();
                } else {
                    Toast.makeText(getContext(), "Kindly upload your signature", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void createSignatureSheet() {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Objects.requireNonNull(Objects.requireNonNull(requireContext())), R.style.BottomSheetDialogTheme);

        View bottom_view = LayoutInflater.from(getContext()).inflate(R.layout.create_signature_layout
                ,
                (RelativeLayout) bottomSheetDialog.findViewById(R.id.relative_layout));

        bottomSheetDialog.setContentView(bottom_view);

        bottomSheetDialog.show();

        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.setCancelable(false);

        signStatus = false;

        final SignaturePad mSignaturePad = (SignaturePad) bottomSheetDialog.findViewById(R.id.signature_pad);


        assert mSignaturePad != null;

        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {

            @Override
            public void onStartSigning() {

                //Event triggered when the pad is touched

            }

            @Override
            public void onSigned() {
                //Event triggered when the pad is signed

                signStatus = true;

            }

            @Override
            public void onClear() {
                //Event triggered when the pad is cleared

                signStatus = false;
            }
        });

        bottomSheetDialog.findViewById(R.id.clear_sign).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mSignaturePad.clear();

            }
        });

        bottomSheetDialog.findViewById(R.id.create_signature).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (signStatus) {

                    File digital_signature = new File(requireContext().getCacheDir(), "demo");

                    try {
                        digital_signature.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //Convert bitmap to byte array

                    Bitmap bitmap = mSignaturePad.getTransparentSignatureBitmap();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                    byte[] bitmapData = bos.toByteArray();

                    //write the bytes in file

                    FileOutputStream fileOutputStream = null;
                    try {
                        fileOutputStream = new FileOutputStream(digital_signature);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        assert fileOutputStream != null;
                        fileOutputStream.write(bitmapData);
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    providerInformation.setDigitalSignature(digital_signature);

                    signatureSheet.dismiss();
                    bottomSheetDialog.dismiss();

                    popUpBox();
                } else {
                    Toast.makeText(getContext(), "Kindly create your signature", Toast.LENGTH_SHORT).show();
                }
            }
        });


        bottomSheetDialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetDialog.dismiss();

            }
        });

    }

    private File storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d(TAG,
                    "Error while creating media file, Please ask for storage permission");
            return null;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }

        return pictureFile;
    }

    private File getOutputMediaFile() {

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + requireContext().getPackageName()
                + "/Files");


        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName = "MI_" + timeStamp + ".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    private String getRegId() {

        return FirebaseInstanceId.getInstance().getToken();

    }

    private void informationBox() {

        Dialog dateDialog = new Dialog(getContext());
        dateDialog.setContentView(R.layout.document_information_layout);
        dateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dateDialog.setCanceledOnTouchOutside(false);
        Window window = dateDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        dateDialog.show();

        dateDialog.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dateDialog.cancel();

                Dialog dateDialog1 = new Dialog(getContext());
                dateDialog1.setContentView(R.layout.pdf_warning_layout);
                dateDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dateDialog1.setCanceledOnTouchOutside(false);
                Window window1 = dateDialog.getWindow();
                window1.setGravity(Gravity.CENTER);
                dateDialog1.show();

                dateDialog1.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dateDialog1.dismiss();

                        int permission = ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

                        if (permission != PackageManager.PERMISSION_GRANTED) {

                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                                    , Manifest.permission.WRITE_EXTERNAL_STORAGE}, 7);
                        } else {
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("application/pdf");
                            startActivityForResult(intent, 7);

                        }
                    }
                });


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
                    if (!verified_email.equals(s.toString())) {
                        verify.setVisibility(View.VISIBLE);
                    } else {
                        verify.setVisibility(View.GONE);
                    }

                } else {
                    verify.setVisibility(View.GONE);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });


    }

    private void requestForOtp() {
        if (!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED)) {

            loading.setVisibility(View.VISIBLE);
            signUp.setVisibility(View.GONE);

            new Thread(new OtpCaller(providerInformation, SignUpAsProviderFragment.this)).start();

        } else {
            DialogShower dialogShower = new DialogShower(R.layout.internet_error, getContext());
            dialogShower.showDialog();
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 5) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");

                startActivityForResult(intent, 5);


            } else {

                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                            , Manifest.permission.WRITE_EXTERNAL_STORAGE}, 5);

                } else {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", requireContext().getPackageName(), null);
                    intent.setData(uri);
                    requireContext().startActivity(intent);

                }
            }
        } else if (requestCode == 6) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                startActivityForResult(intent, 6);


            } else {

                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                            , Manifest.permission.WRITE_EXTERNAL_STORAGE}, 6);

                } else {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", requireContext().getPackageName(), null);
                    intent.setData(uri);
                    requireContext().startActivity(intent);

                }
            }
        } else if (requestCode == 7) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                startActivityForResult(intent, 7);


            } else {

                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                            , Manifest.permission.WRITE_EXTERNAL_STORAGE}, 7);

                } else {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", requireContext().getPackageName(), null);
                    intent.setData(uri);
                    requireContext().startActivity(intent);

                }
            }
        } else if (requestCode == 8) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");

                startActivityForResult(intent, 8);


            } else {

                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                            , Manifest.permission.WRITE_EXTERNAL_STORAGE}, 8);

                } else {

                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", requireContext().getPackageName(), null);
                    intent.setData(uri);
                    requireContext().startActivity(intent);

                }
            }
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 5 && resultCode == Activity.RESULT_OK) {

            assert data != null;
            Uri image = data.getData();
            profile_image_name = get_data(data.getData());
            selected_image_path = image.toString();
            profile_image.setImageURI(image);


        } else if (requestCode == 6 && resultCode == Activity.RESULT_OK) {
            assert data != null;

            selected_fica_documents = data.getData().toString();
            fica_document_name = get_data(data.getData());
            add_fica_documents.setText(R.string.upload);

            Toast.makeText(requireContext(), "" + fica_document_name, Toast.LENGTH_SHORT).show();


        } else if (requestCode == 7 && resultCode == Activity.RESULT_OK) {
            assert data != null;

            selected_registration_documents = data.getData().toString();
            registration_document_name = get_data(data.getData());
            add_registration_documents.setText(R.string.upload);
            Toast.makeText(requireContext(), "" + registration_document_name, Toast.LENGTH_SHORT).show();


        } else if (requestCode == 8 && resultCode == Activity.RESULT_OK) {

            assert data != null;
            Uri image = data.getData();

            ContentResolver contentResolver = requireContext().getContentResolver();
            Cursor cursor = contentResolver.query(image, null, null, null, null);

            if (cursor != null) {
                while (cursor.moveToNext()) {

                    @SuppressLint("Range") File file = new File(cursor.getString(cursor.getColumnIndex("_data")));

                    this.selected_signature_path = file.toString();

                    upload_image.setImageURI(image);

                    providerInformation.setDigitalSignature(file);

                }

                cursor.close();

            }


        }

    }

    private boolean checkExtension(String document) {
        return document.endsWith(".pdf");
    }

    @SuppressLint("Range")
    private String get_data(Uri uri) {
        String value = null;
        ContentResolver contentResolver = requireActivity().getContentResolver();
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {

                value = cursor.getString(cursor.getColumnIndex("_display_name"));

            }
            cursor.close();


        }

        return value;
    }

    private boolean emailPatternChecker(String email) {


        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());

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

    private void getSpecialityList() {

        if (!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED)) {


            new Thread(new SpecialitiesTypeFetcher(SignUpAsProviderFragment.this)).start();

        } else {
            DialogShower dialogShower = new DialogShower(R.layout.internet_error, getContext());
            dialogShower.showDialog();
        }

    }

    private void getConsultChargesList() {

        if (!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED)) {


            new Thread(new ConsultChargesCaller(SignUpAsProviderFragment.this)).start();

        } else {
            DialogShower dialogShower = new DialogShower(R.layout.internet_error, getContext());
            dialogShower.showDialog();
        }

    }

    private void setDefault() {
        email.setError(null);
        firstName.setError(null);
        lastName.setError(null);
        phone.setError(null);
        address.setError(null);
        practice_number.setError(null);
        city.setError(null);
        postalCode.setError(null);
        professionalRegistrationNumber.setError(null);
        department.setError(null);
        workLocation.setError(null);
        isEmailFilled = false;
        isPracticeNumberFilled = false;
        practice_confirmation = false;
        email_confirmation = false;
        isNameFilled = false;
        name_confirmation = false;
        isSurNameFilled = false;
        surName_confirmation = false;
        isPhoneNumberFilled = false;
        phone_confirmation = false;
        isAddressFilled = false;
        address_confirmation = false;
        isCityFilled = false;
        city_confirmation = false;
        isPostalCodeFilled = false;
        postal_confirmation = false;
        isRegistrationFilled = false;
        registration_confirmation = false;
        email_assign = null;
        name_assign = null;
        practice_assign = null;
        surName_assign = null;
        phone_assign = null;
        address_assign = null;
        city_assign = null;
        postal_assign = null;
        registration_assign = null;

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

                    location_status = false;

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

                        .setMessage("Click ok than select permissions and set allow location")

                        .setNegativeButton("CANCEL", (dialogInterface, i) -> {

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
                            location_status = true;

                            List<Address> addresses = getLocationDetails(String.valueOf(currentLocation.getLatitude()), String.valueOf(currentLocation.getLongitude()));

                            if (addresses != null) {

                                city.setText(addresses.get(0).getSubAdminArea());
                                postalCode.setText(addresses.get(0).getPostalCode());
                                address.setText(addresses.get(0).getAddressLine(0));
                                address.setFocusableInTouchMode(true);

                                city.setError(null);
                                address.setError(null);
                                postalCode.setError(null);

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

                location_status = true;

                List<Address> addresses = getLocationDetails(String.valueOf(currentLocation.getLatitude()), String.valueOf(currentLocation.getLongitude()));

                if (addresses != null) {

                }
            }

        };
    }

    private void establishViews(View view) {

        email = view.findViewById(R.id.email);
        firstName = view.findViewById(R.id.firstName);
        lastName = view.findViewById(R.id.lastName);
        phone = view.findViewById(R.id.phone);
        address = view.findViewById(R.id.address);
        city = view.findViewById(R.id.city);
        postalCode = view.findViewById(R.id.postalCode);
        professionalRegistrationNumber = view.findViewById(R.id.professionalRegistrationNumber);
        department = view.findViewById(R.id.department);
        workLocation = view.findViewById(R.id.workLocation);
        signUp = view.findViewById(R.id.login);
        add_fica_documents = view.findViewById(R.id.add_fica_documents);
        add_registration_documents = view.findViewById(R.id.add_registration_documents);
        loading = view.findViewById(R.id.loading);
        profile_image = view.findViewById(R.id.profile_image);
        medical_aid = view.findViewById(R.id.medical_aid);
        yes = view.findViewById(R.id.personal);
        no = view.findViewById(R.id.business);
        add_fica_documents = view.findViewById(R.id.add_fica_documents);
        add_registration_documents = view.findViewById(R.id.add_registration_documents);
        password = view.findViewById(R.id.password);
        verify = view.findViewById(R.id.verify);
        practice_number = view.findViewById(R.id.practice_number);
        experience = view.findViewById(R.id.experience);
        consult_charges = view.findViewById(R.id.consult_charges);
        spinner_title = view.findViewById(R.id.spinner_title);
        otp_box_layout = new Dialog(getContext());
        selected_registration_documents = null;
        selected_fica_documents = null;


    }

    private void popUpBox() {
        pop_up_box = new Dialog(getContext());
        pop_up_box.setContentView(R.layout.bio_layout);
        pop_up_box.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop_up_box.setCanceledOnTouchOutside(true);
        Window window = pop_up_box.getWindow();
        window.setGravity(Gravity.CENTER);
        pop_up_box.show();

        EditText editText = pop_up_box.findViewById(R.id.editTextTextMultiLine);

        pop_up_box.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "onClick: clicked");

                if (editText.getText().toString().trim().length() == 0) {
                    editText.setError("Please tell us about your self");
                } else {

                    editText.setError(null);

                    providerInformation.setBio(editText.getText().toString());

                    pop_up_box.dismiss();


                    registerProvider(providerInformation); // this method is use to make direct registration without phone number verification

//                    requestForOtp();      // this method is use to make phone number verification before registration

                }

            }


        });

    }

    private void registerProvider(ProviderInformation providerInformation) {
        openLoadingBox();
        uploadCloudInformation(providerInformation);

    }

    private void uploadCloudInformation(ProviderInformation providerInformation) {

        StorageReference storageReference = FirebaseStorage.getInstance().getReference(providerInformation.getEmail() + "/");

        storageReference.child(providerInformation.getFicaDocumentName()).putFile(Uri.parse(providerInformation.getFicaDocument())).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                    @Override
                    public void onSuccess(Uri uri) {

                        providerInformation.setFicaDocument(uri.toString());

                        storageReference.child(providerInformation.getRegistrationDocumentName()).
                                putFile(Uri.parse(providerInformation.getRegistrationDocument())).
                                addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                                    @Override
                                    public void onSuccess(Uri uri) {

                                        providerInformation.setRegistrationDocument(uri.toString());

                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("DoctorDocument");


                                        if (!providerInformation.getProfileImage().equals(SignUpAsUserFragment.IdentityType.notSelected.toString())) {


                                            storageReference.child(providerInformation.getProfileImageName()).putFile(Uri.parse(providerInformation.getProfileImage())).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                                                        @Override
                                                        public void onSuccess(Uri uri) {

                                                            providerInformation.setProfileImage(uri.toString());

                                                            new Thread(new ProviderRegisterCaller(providerInformation, SignUpAsProviderFragment.this)).start();

                                                        }
                                                    });

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                    loading_box.dismiss();

                                                    Log.d(TAG, "onFailure: error: " + e.getMessage());

                                                }
                                            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                                                    Log.d(TAG, "onProgress: " + (snapshot.getBytesTransferred() / snapshot.getTotalByteCount()) * 100);

                                                    long value = (snapshot.getBytesTransferred() / snapshot.getTotalByteCount()) * 100;
                                                    int progress_status = (int) value;

                                                    String status = "getting profile ready.. " + progress_status + "%";

                                                    loadingProgressText.setText(status);

                                                }
                                            });


                                        } else {

                                            new Thread(new ProviderRegisterCaller(providerInformation, SignUpAsProviderFragment.this)).start();
                                        }


                                    }
                                });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                loading_box.dismiss();

                                Log.d(TAG, "onFailure: error: " + e.getMessage());


                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                                Log.d(TAG, "onProgress: " + (snapshot.getBytesTransferred() / snapshot.getTotalByteCount()) * 100);

                                long value = (snapshot.getBytesTransferred() / snapshot.getTotalByteCount()) * 100;
                                int progress_status = (int) value;

                                String status = "creating profile.. " + progress_status + "%";

                                loadingProgressText.setText(status);

                            }
                        });


                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.d(TAG, "onFailure: error: " + e.getMessage());


            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                Log.d(TAG, "onProgress: " + (snapshot.getBytesTransferred() / snapshot.getTotalByteCount()) * 100);


                long value = (snapshot.getBytesTransferred() / snapshot.getTotalByteCount()) * 100;
                int progress_status = (int) value;

                String status = "Uploading " + progress_status + "%";


            }
        });
    }

    @Override
    public void confirmationProviderRegisterCaller(ResponseInformation responseInformation) {

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
    public void informationProviderRegisterCaller(ProviderInformation providerInformation) {

        addDataIntoSharedPreferencesAsProvider(providerInformation);

        handler.post(new Runnable() {
            @Override
            public void run() {

                loading_box.dismiss();

                requireActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.frame_holder, new CongratulationsFragment()).addToBackStack(null).commit();

            }
        });

    }

    private void addDataIntoSharedPreferencesAsProvider(ProviderInformation providerInformation) {

        ProviderMainFrame.id = providerInformation.getId();

        providerInformation.setPasswordBackup(SignUpAsProviderFragment.passwordBackup);
        providerInformation.setPracticeNumberPhone(getResources().getString(R.string.none));

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(ProviderMainFrame.provider_information_file, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();

        String json = gson.toJson(providerInformation);

        editor.putString(ProviderMainFrame.provider_information_key, json);

        editor.apply();

    }

    private void openLoadingBox() {

        loading_box = new Dialog(getContext());
        loading_box.setContentView(R.layout.register_loading_layout);
        loading_box.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading_box.setCanceledOnTouchOutside(false);
        loading_box.setCancelable(false);
        Window window = loading_box.getWindow();
        window.setGravity(Gravity.CENTER);
        loading_box.show();
        loadingProgressText = loading_box.findViewById(R.id.loading);

    }

    @Override
    public void confirmationCallbackFromConsultChargesCaller(ResponseInformation responseInformation) {

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
    public void informationCallbackFromConsultChargesCaller(List<ChargesInformation> chargesInformation) {

        ArrayList<String> result = new ArrayList<>();


        for (ChargesInformation chargesInformation1 : chargesInformation) {

            result.add(chargesInformation1.getAmount());
        }


        handler.post(new Runnable() {
            @Override
            public void run() {

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.drop_down_layout, R.id.my_text,
                        result);

                consult_charges.setAdapter(arrayAdapter);
            }
        });

    }

    @Override
    public void confirmationCallbackFromSpecialitiesTypeFetcher(ResponseInformation responseInformation) {

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
    public void informationCallbackFromSpecialitiesTypeFetcher(List<SpecialitiesInformation> specialitiesInformation) {

        ArrayList<String> result = new ArrayList<>();

        for (SpecialitiesInformation specialitiesInformation1 : specialitiesInformation) {

            result.add(specialitiesInformation1.getName());
        }

        handler.post(new Runnable() {
            @Override
            public void run() {

                if (isVisible()) {

                    Log.d(TAG, "informationCallbackFromSpecialitiesTypeFetcher: " + result.size());

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireActivity(), R.layout.drop_down_layout, R.id.my_text,
                            result);

                    medical_aid.setAdapter(arrayAdapter);

                }

            }
        });

    }

    @Override
    public void confirmation(ResponseInformation responseInformation) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                loading.setVisibility(View.GONE);
                signUp.setVisibility(View.VISIBLE);

                if (responseInformation.getSuccess().equals(ResponseInformation.fail_response_type)) {

                    if (responseInformation.getMessage().equals(
                            getString(R.string.ready_send))) {
                        VerificationFragment verificationFragment = new VerificationFragment();
                        Bundle bundle = new Bundle();

                        bundle.putSerializable(VerificationFragment.data_key, providerInformation);
                        verificationFragment.setArguments(bundle);

                        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_holder,
                                verificationFragment).addToBackStack(null).commit();
                    } else {
                        Toast.makeText(getContext(), responseInformation.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                } else {


                    VerificationFragment verificationFragment = new VerificationFragment();

                    Bundle bundle = new Bundle();
                    providerInformation.setRequestId(responseInformation.getRequestId());
                    bundle.putSerializable(VerificationFragment.data_key, providerInformation);
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

                    Log.d(TAG, "run: " + responseInformation.getOtp());

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

    private void dismissBox() {
        verify.setVisibility(View.GONE);
        otp_box_layout.dismiss();

    }

    private void resendOtpCaller() {

        if (!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED)) {

            ClientInformation clientInformation = new ClientInformation();
            clientInformation.setEmail(email_backup);

            new Thread(new EmailPhoneCheckerCaller(clientInformation, SignUpAsProviderFragment.this)).start();
        } else {

            DialogShower dialogShower = new DialogShower(R.layout.internet_error, getContext());
            dialogShower.showDialog();
        }

    }

    private void disableLoadAndResend() {
        progress_handler.setVisibility(View.GONE);
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        ((TextView) parent.getChildAt(0)).setTextColor(Color.GRAY);
        ((TextView) parent.getChildAt(0)).setTextSize(18);

        provider_title = titles_list[position];

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

        Log.d(TAG, "onNothingSelected: entered");

        if (ProviderTypeFragment.providerTypeString.equalsIgnoreCase("doctor")) {

            provider_title = "Dr.";

        } else if (ProviderTypeFragment.providerTypeString.equalsIgnoreCase("nurse")) {

            provider_title = "Nur.";

        } else if (ProviderTypeFragment.providerTypeString.equalsIgnoreCase("physiotherapists")) {

            provider_title = titles_list[0];

            spinner_title.setSelection(0);

        } else {

            provider_title = titles_list[0];

            spinner_title.setSelection(0);

        }

    }
}