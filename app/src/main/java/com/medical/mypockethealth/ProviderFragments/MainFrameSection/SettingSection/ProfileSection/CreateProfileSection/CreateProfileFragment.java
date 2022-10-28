package com.medical.mypockethealth.ProviderFragments.MainFrameSection.SettingSection.ProfileSection.CreateProfileSection;

import android.Manifest;
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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.medical.mypockethealth.Adaptors.ProviderSection.ProfileSection.ChargesListRecycleViewAdaptor;
import com.medical.mypockethealth.ApplicationBase.BaseActivity;
import com.medical.mypockethealth.BaseFragments.RegisterSection.UserRegisterSection.SignUpAsUserFragment;
import com.medical.mypockethealth.Classes.ClientInformation.ClientInformation;
import com.medical.mypockethealth.Classes.ConsultChargesInformation.ChargesInformation;
import com.medical.mypockethealth.Classes.DialogShower;
import com.medical.mypockethealth.Classes.NetworkSection.NetworkStatus;
import com.medical.mypockethealth.Classes.ProviderInformation.ProviderInformation;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.ProviderActivity.ProviderMainFrame;
import com.medical.mypockethealth.ProviderFragments.MainFrameSection.SettingSection.ProfileSection.ViewProfileSection.ViewProfileFragment;
import com.medical.mypockethealth.ProviderFragments.MainFrameSection.SettingSection.SettingProviderFragment;
import com.medical.mypockethealth.R;
import com.medical.mypockethealth.Threads.BaseThreads.EmailPhoneCheckerCaller;
import com.medical.mypockethealth.Threads.BaseThreads.ProviderSection.ConsultChargesCaller;
import com.medical.mypockethealth.Threads.ProviderSection.ProfileSection.EditProviderProfileCaller;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

public class CreateProfileFragment extends Fragment implements EmailPhoneCheckerCaller.CallbackFromEmailCheckerCaller,
        ChargesListRecycleViewAdaptor.callBackFromChargesListRecycleViewAdaptor, ConsultChargesCaller.CallbackFromConsultChargesCaller,
        EditProviderProfileCaller.CallbackFromEditProfileCaller, AdapterView.OnItemSelectedListener {

    private static final String TAG = "CreateProfileFragment";

    private EditText firstName, surname, email, address, city, postal_code, practice_number, hospital_name, experience, consult_charges, practiceNumberPhone;
    private ImageView profile_image, loading;
    private TextView verify, resend_otp;
    private Button save;
    private static final int REQUEST_CHECK_SETTINGS = 1;
    private static final int REQUEST_GRANT_PERMISSION = 2;
    private FusedLocationProviderClient fusedLocationClient;
    LocationRequest locationRequest;
    public static Location currentLocation;
    private LocationCallback locationCallback;
    private Dialog pop_up_box, otp_box_layout;
    private ProgressBar progressBar;
    private int progressStatus = 100;
    private RelativeLayout progress_handler;
    private final Handler handler = new Handler();
    private ChargesListRecycleViewAdaptor chargesListRecycleViewAdaptor;

    private String[] titles_list = new String[6];
    private ArrayAdapter<String> spinnerTitlesAdapter;

    private Spinner spinner_title;

    private boolean location_status = false, isEmailFilled = false, email_confirmation = false, isNameFilled = false,
            name_confirmation = false, isSurNameFilled = false, surName_confirmation = false, isAddressFilled = false, address_confirmation = false,
            isCityFilled = false, city_confirmation = false, isPostalCodeFilled = false, postal_confirmation = false,
            match_status = false, isExperienceFilled = false, experience_confirmation = false;

    private String emailBackup = null, selected_image_file = null, email_assign = null, entered_otp = null, verified_email = null,
            name_assign = null, surname_assign = null, address_assign = null, city_assign = null, postal_assign = null, provider_title = "",
            experience_assign = null, consult_assign = null;


    public enum ImageStatus {
        notSelected, selected
    }


    public CreateProfileFragment() {
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
        return inflater.inflate(R.layout.fragment_create_profile, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        establishViews(view);

        titles_list[0] = "Mr.";
        titles_list[1] = "Ms.";
        titles_list[2] = "Prof.";
        titles_list[3] = "Sr.";
        titles_list[4] = "Mrs.";
        titles_list[5] = "Miss.";

        spinner_title.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        spinnerTitlesAdapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, titles_list);
        spinnerTitlesAdapter.setDropDownViewResource(R.layout.spinner_dropdown_lay2);
        //Setting the ArrayAdapter data on the Spinner
        spinner_title.setAdapter(spinnerTitlesAdapter);

        emailHandler();

        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                        new SettingProviderFragment()).addToBackStack(null).commit();
            }
        });

        view.findViewById(R.id.select_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int permission = ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

                if (permission != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                            , Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");

                    startActivityForResult(intent, 1);


                }

            }
        });


        view.findViewById(R.id.address_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED)) {

                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
                    createLocationRequest();
                    settingsCheck();


                    if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        Log.d(TAG, "onMenuItemClick: requesting call");
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_GRANT_PERMISSION);

                    }
                    if (locationCallback == null) {
                        Log.d(TAG, "onMenuItemClick: location call back called");
                        buildLocationCallback();
                    }
                    if (currentLocation == null) {
                        Log.d(TAG, "onMenuItemClick: current call back");

                        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    } else {
                        Log.d(TAG, "onMenuItemClick: calling nothing");
                    }

                } else {
                    DialogShower dialogShower = new DialogShower(R.layout.internet_error, getContext());
                    dialogShower.showDialog();
                }


            }
        });


        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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


                if (email_confirmation) {

                    if (!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED)) {

                        verify.setText(R.string.loading);

                        ClientInformation clientInformation = new ClientInformation();
                        clientInformation.setEmail(email_assign);

                        new Thread(new EmailPhoneCheckerCaller(clientInformation, CreateProfileFragment.this)).start();

                    } else {
                        DialogShower dialogShower = new DialogShower(R.layout.internet_error, getContext());
                        dialogShower.showDialog();
                    }


                }

            }
        });


        consult_charges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                openChargesListBox();


            }
        });


        save.setOnClickListener(new View.OnClickListener() {
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


                if (surname.getText().toString().trim().length() == 0) {

                    surname.setError("Please enter your surname");

                    isSurNameFilled = false;

                } else if (profile_name_checker(surname.getText().toString())) {
                    surname.setError("spacial characters and numbers not allowed");
                } else {

                    surname.setError(null);
                    surname_assign = surname.getText().toString();
                    surName_confirmation = true;
                    isSurNameFilled = true;
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

                if (postal_code.getText().toString().trim().length() == 0) {
                    postal_code.setError("please enter your postal code");

                    isPostalCodeFilled = false;

                } else {

                    postal_code.setError(null);
                    postal_assign = postal_code.getText().toString();
                    postal_confirmation = true;
                    isPostalCodeFilled = true;

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


                if (name_confirmation && isNameFilled && email_confirmation && isEmailFilled &&
                        surName_confirmation && isSurNameFilled && address_confirmation &&
                        isAddressFilled && city_confirmation && isCityFilled && postal_confirmation &&
                        isPostalCodeFilled && isExperienceFilled) {


                    if (!verify.isShown()) {

                        ProviderInformation providerInformation = new ProviderInformation();

                        providerInformation.setUserTitle(provider_title);
                        providerInformation.setFirstName(name_assign);
                        providerInformation.setSurName(surname_assign);
                        providerInformation.setEmail(email_assign);
                        providerInformation.setAddress(address_assign);
                        providerInformation.setCity(city_assign);
                        providerInformation.setPostalCode(postal_assign);
                        providerInformation.setExperience(experience_assign);
                        providerInformation.setConsultCharges(consult_charges.getText().toString());

                        if (practice_number.getText().toString().length() != 0) {
                            providerInformation.setDepartment(practice_number.getText().toString());
                        } else {
                            providerInformation.setDepartment(getString(R.string.None));
                        }

                        if (hospital_name.getText().toString().length() != 0) {
                            providerInformation.setWorkLocation(hospital_name.getText().toString());
                        } else {
                            providerInformation.setWorkLocation(getString(R.string.None));
                        }

                        if (selected_image_file != null) {

                            providerInformation.setProfileImage(selected_image_file);

                        } else {
                            providerInformation.setProfileImage(SignUpAsUserFragment.IdentityType.notSelected.toString());
                        }


                        if (practiceNumberPhone.getText().toString().trim().length() != 0) {
                            if (!practiceNumberPhone.getText().toString().equals(getString(R.string.None))) {
                                providerInformation.setPracticeNumberPhone(practiceNumberPhone.getText().toString());
                            } else {
                                providerInformation.setPracticeNumberPhone(getString(R.string.None));
                            }

                        } else {
                            providerInformation.setPracticeNumberPhone(getString(R.string.None));
                        }


                        if (!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED)) {

                            save.setVisibility(View.GONE);
                            loading.setVisibility(View.VISIBLE);

                            new Thread(new EditProviderProfileCaller(providerInformation, CreateProfileFragment.this)).start();

                        } else {

                            DialogShower dialogShower = new DialogShower(R.layout.internet_error, getContext());
                            dialogShower.showDialog();
                        }


                    } else {
                        Toast.makeText(getContext(), "Please verify your email", Toast.LENGTH_SHORT).show();
                    }


                }


            }
        });

    }


    private void openChargesListBox() {

        pop_up_box = new Dialog(getContext());
        pop_up_box.setContentView(R.layout.charges_list_layout);
        pop_up_box.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop_up_box.setCanceledOnTouchOutside(false);
        Window window = pop_up_box.getWindow();
        window.setGravity(Gravity.CENTER);
        pop_up_box.show();

        RecyclerView diagnosis_holder = pop_up_box.findViewById(R.id.diagnosis_holder);

        diagnosis_holder.setLayoutManager(new LinearLayoutManager(getContext()));

        chargesListRecycleViewAdaptor = new ChargesListRecycleViewAdaptor(new ArrayList<>(),
                CreateProfileFragment.this);

        diagnosis_holder.setAdapter(chargesListRecycleViewAdaptor);

        getConsultChargesList();

    }


    private void getConsultChargesList() {

        if (!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED)) {


            new Thread(new ConsultChargesCaller(CreateProfileFragment.this)).start();

        } else {
            DialogShower dialogShower = new DialogShower(R.layout.internet_error, getContext());
            dialogShower.showDialog();
        }

    }

    @Override
    public void selectedCode(ChargesInformation chargesInformation) {

        pop_up_box.dismiss();
        consult_charges.setText(chargesInformation.getAmount());

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


        handler.post(new Runnable() {
            @Override
            public void run() {

                chargesListRecycleViewAdaptor.loadData(chargesInformation);

            }
        });


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


    private void setDefault() {

        firstName.setError(null);
        surname.setError(null);
        email.setError(null);
        address.setError(null);
        city.setError(null);
        postal_code.setError(null);
        practice_number.setError(null);
        hospital_name.setError(null);
        experience.setError(null);
        consult_charges.setError(null);

        isNameFilled = false;
        name_confirmation = false;
        isSurNameFilled = false;
        surName_confirmation = false;
        isAddressFilled = false;
        address_confirmation = false;
        isCityFilled = false;
        city_confirmation = false;
        isPostalCodeFilled = false;
        postal_confirmation = false;
        isExperienceFilled = false;
        experience_confirmation = false;


        email_assign = null;
        name_assign = null;
        surname_assign = null;
        address_assign = null;
        city_assign = null;
        postal_assign = null;
        experience_assign = null;
        consult_assign = null;

    }

    private boolean emailPatternChecker(String email) {


        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());

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
                            location_status = true;

                            List<Address> addresses = getLocationDetails(String.valueOf(currentLocation.getLatitude()), String.valueOf(currentLocation.getLongitude()));

                            if (addresses != null) {

                                city.setText(addresses.get(0).getSubAdminArea());
                                postal_code.setText(addresses.get(0).getPostalCode());
                                address.setText(addresses.get(0).getAddressLine(0));
                                address.setFocusableInTouchMode(true);

                                city.setError(null);
                                address.setError(null);
                                postal_code.setError(null);

                            }
                            // run threads
                        } else {
                            Log.d("TAG", "location is null");
                            buildLocationCallback();
                        }
                    }
                });
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


                // run threads


            }

            ;
        };
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");

                startActivityForResult(intent, 1);


            } else {

                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                            , Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

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

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

            assert data != null;
            Uri image = data.getData();

            ContentResolver contentResolver = requireContext().getContentResolver();
            Cursor cursor = contentResolver.query(image, null, null, null, null);

            if (cursor != null) {
                while (cursor.moveToNext()) {

                    File file = new File(cursor.getString(cursor.getColumnIndex("_data")));

                    this.selected_image_file = file.toString();

                    profile_image.setImageURI(image);


                }

                cursor.close();

            }

        }

    }


    private void emailHandler() {

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (emailPatternChecker(s.toString())) {

                    if (emailBackup.equals(s.toString())) {

                        verify.setVisibility(View.GONE);

                    } else {

                        verify.setVisibility(View.VISIBLE);

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

        String value = "A verification code has been sent to your email ID (" + email_assign + ").Please enter the code below";

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

                        verified_email = emailBackup;

                        dismissBox();

                    } else {
                        Toast.makeText(getContext(), "The entered otp is incorrect", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


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


    private void resendOtpCaller() {

        if (!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED)) {

            ClientInformation clientInformation = new ClientInformation();
            clientInformation.setEmail(emailBackup);

            new Thread(new EmailPhoneCheckerCaller(clientInformation, CreateProfileFragment.this)).start();

        } else {

            DialogShower dialogShower = new DialogShower(R.layout.internet_error, getContext());
            dialogShower.showDialog();
        }

    }

    @Override
    public void confirmationEditProfileCaller(ResponseInformation responseInformation) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                save.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);

                if (responseInformation.getSuccess().equals(ResponseInformation.fail_response_type)) {

                    Toast.makeText(getContext(), responseInformation.getMessage(), Toast.LENGTH_SHORT).show();

                }

            }
        });


    }

    @Override
    public void informationEditProfileCaller(ProviderInformation providerInformation) {

        updateDataIntoSharedPreferencesAsUser(providerInformation);

        handler.post(new Runnable() {
            @Override
            public void run() {

                save.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);

                requireActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.main_frame_layout, new ViewProfileFragment()).addToBackStack(null).commit();
            }
        });

    }


    private void updateDataIntoSharedPreferencesAsUser(ProviderInformation providerInformation) {

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(ProviderMainFrame.provider_information_file, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        editor.apply();

        Gson gson = new Gson();

        SharedPreferences sharedPreferences3 = requireContext().getSharedPreferences(ProviderMainFrame.provider_information_file, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor2 = sharedPreferences3.edit();

        String json1 = gson.toJson(providerInformation);

        editor2.putString(ProviderMainFrame.provider_information_key, json1);

        editor2.apply();

    }

    private void establishViews(View view) {

        firstName = view.findViewById(R.id.firstName);
        surname = view.findViewById(R.id.surname);
        email = view.findViewById(R.id.email);
        address = view.findViewById(R.id.address);
        practiceNumberPhone = view.findViewById(R.id.practiceNumber);
        city = view.findViewById(R.id.city);
        loading = view.findViewById(R.id.loading);
        save = view.findViewById(R.id.save);
        practice_number = view.findViewById(R.id.practice_number);
        hospital_name = view.findViewById(R.id.hospital_name);
        experience = view.findViewById(R.id.experience);
        consult_charges = view.findViewById(R.id.consult_charges);
        postal_code = view.findViewById(R.id.postal_code);
        profile_image = view.findViewById(R.id.profile_image);
        verify = view.findViewById(R.id.verify);

        spinner_title = view.findViewById(R.id.spinner_title);

        otp_box_layout = new Dialog(getContext());

        //- setup up provider information

        SharedPreferences preferences = requireContext().getSharedPreferences(ProviderMainFrame.provider_information_file, Context.MODE_PRIVATE);
        String json = preferences.getString(ProviderMainFrame.provider_information_key, "");
        Gson gson = new Gson();
        ProviderInformation providerInformation = gson.fromJson(json, ProviderInformation.class);

        String name;

        if (providerInformation.getUserTitle() != null && providerInformation.getUserTitle().length() != 0) {

            provider_title = providerInformation.getUserTitle();

            spinner_title.setVisibility(View.VISIBLE);

            setTitle(providerInformation.getUserTitle());

            name = providerInformation.getUserTitle() + " " + providerInformation.getFirstName();

        } else {

            spinner_title.setVisibility(View.GONE);

            name = providerInformation.getFirstName();

        }

        firstName.setText(name);

        surname.setText(providerInformation.getSurName());
        email.setText(providerInformation.getEmail());
        emailBackup = providerInformation.getEmail();
        address.setText(providerInformation.getAddress());
        city.setText(providerInformation.getCity());
        postal_code.setText(providerInformation.getPostalCode());

        if (!(providerInformation.getDepartment().equals(getString(R.string.None)))) {
            practice_number.setText(providerInformation.getDepartment());
        } else {
            practice_number.setText(getString(R.string.None));
        }

        if ((providerInformation.getPracticeNumberPhone().length() != 0)) {
            practiceNumberPhone.setText(providerInformation.getPracticeNumberPhone());
        } else {
            practiceNumberPhone.setText(getString(R.string.None));
        }


        if (!(providerInformation.getWorkLocation().equals(getString(R.string.None)))) {
            hospital_name.setText(providerInformation.getWorkLocation());
        } else {
            hospital_name.setText(getString(R.string.None));
        }

        experience.setText(providerInformation.getExperience());
        consult_charges.setText(providerInformation.getConsultCharges());


        Picasso.with(getContext()).load(Uri.parse(providerInformation.getProfileImage())).error(R.drawable.profile).into(profile_image);

    }

    private void setTitle(String providerTitle) {

//        String title = providerTitle;
//
//        int position = spinner_title.getItemAtPosition(0);
//
//        spinner_title.setSelection(position);

        spinner_title.setSelection(spinnerTitlesAdapter.getPosition(providerTitle));

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        ((TextView) parent.getChildAt(0)).setTextSize(13);
        ((TextView) parent.getChildAt(0)).setGravity(Gravity.END);

        provider_title = titles_list[position];

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

        ((TextView) parent.getChildAt(0)).setTextSize(13);
        ((TextView) parent.getChildAt(0)).setGravity(Gravity.END);

    }

}