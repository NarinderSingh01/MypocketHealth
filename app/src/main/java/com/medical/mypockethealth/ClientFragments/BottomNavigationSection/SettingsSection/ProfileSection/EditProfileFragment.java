package com.medical.mypockethealth.ClientFragments.BottomNavigationSection.SettingsSection.ProfileSection;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.medical.mypockethealth.ApplicationBase.BaseActivity;
import com.medical.mypockethealth.BaseFragments.RegisterSection.UserRegisterSection.SignUpAsUserFragment;
import com.medical.mypockethealth.Classes.ClientInformation.ClientInformation;
import com.medical.mypockethealth.Classes.DialogShower;
import com.medical.mypockethealth.Classes.NetworkSection.NetworkStatus;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.ClientActivity.ClientMainFrame;
import com.medical.mypockethealth.ClientFragments.BottomNavigationSection.SettingsSection.SettingFragment;
import com.medical.mypockethealth.R;
import com.medical.mypockethealth.Threads.BaseThreads.EmailPhoneCheckerCaller;
import com.medical.mypockethealth.Threads.UserSection.SettingSection.ProfileSection.EditUserProfileCaller;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;


public class EditProfileFragment extends Fragment implements EmailPhoneCheckerCaller.CallbackFromEmailCheckerCaller,
        EditUserProfileCaller.CallbackFromEditProfileCaller {

    private static final String TAG = "EditProfileFragment";

    private EditText firstName,surname,email;
    private ImageView loading;
    private ImageView profile_image;
    private TextView verify,resend_otp;
    private Button save;
    private ProgressBar progressBar;
    private int progressStatus = 100;
    private RelativeLayout progress_handler;
    public static String passwordBackup;
    private Dialog pop_up_box,otp_box_layout;
    private final Handler handler = new Handler();
    private boolean isNameFilled=false,isSurnameFilled=false,isEmailFilled=false,name_confirmation=false,
            surname_confirmation=false,email_confirmation=false,match_status=false;
    private String selected_image_file=null,name_assign=null,surname_assign=null,email_assign=null,
            emailBackup=null,entered_otp=null,verified_email=null;



    public EditProfileFragment() {
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
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        establishViews(view);

        setData();

        emailHandler();

        view.findViewById(R.id.select_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int permission= ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

                if(permission!= PackageManager.PERMISSION_GRANTED)
                {

                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                            ,Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }
                else
                {
                    Intent intent=new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent,1);


                }

            }
        });


        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requireActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.main_frame_layout,new SettingFragment()).addToBackStack(null).commit();
            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(email.getText().toString().trim().length()==0)
                {

                    email.setError("Please enter your email");

                    isEmailFilled=false;
                }
                else if(!emailPatternChecker(email.getText().toString()))
                {

                    email.setError("Invalid email address");

                }
                else
                {
                    email.setError(null);
                    email_assign=email.getText().toString();
                    email_confirmation=true;
                    isEmailFilled=true;

                }


                if(email_confirmation)
                {

                    if(!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED))
                    {

                        verify.setText(R.string.loading);

                        ClientInformation clientInformation=new ClientInformation();
                        clientInformation.setEmail(email_assign);

                        new Thread(new EmailPhoneCheckerCaller(clientInformation, EditProfileFragment.this)).start();

                    }

                    else
                    {
                        DialogShower dialogShower=new DialogShower(R.layout.internet_error,getContext());
                        dialogShower.showDialog();
                    }


                }

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 setDefault();

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

                    surname.setError("Please enter your first name");

                    isSurnameFilled = false;

                } else if (profile_name_checker(surname.getText().toString())) {
                    surname.setError("spacial characters and numbers not allowed");
                } else {

                    surname.setError(null);
                    surname_assign = surname.getText().toString();
                    surname_confirmation = true;
                    isSurnameFilled = true;
                }

                if(email.getText().toString().trim().length()==0)
                {

                    email.setError("Please enter your email");

                    isEmailFilled=false;
                }
                else if(!emailPatternChecker(email.getText().toString()))
                {

                    email.setError("Invalid email address");

                }
                else
                {
                    email.setError(null);
                    email_assign=email.getText().toString();
                    email_confirmation=true;
                    isEmailFilled=true;

                }

                if(verify.isShown())
                {
                    Toast.makeText(getContext(),"Please verify your email",Toast.LENGTH_SHORT).show();
                }


                if(isNameFilled && name_confirmation && isSurnameFilled && surname_confirmation &&
                        isEmailFilled && email_confirmation && !verify.isShown())
                {

                    ClientInformation clientInformation=new ClientInformation();

                    clientInformation.setFirstName(name_assign);
                    clientInformation.setSurName(surname_assign);
                    clientInformation.setEmail(email_assign);

                    if (selected_image_file != null) {

                        clientInformation.setProfileImage(selected_image_file);


                    } else {

                        clientInformation.setProfileImage(SignUpAsUserFragment.IdentityType.notSelected.toString());
                    }

                    if (!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED)) {

                        save.setVisibility(View.GONE);
                        loading.setVisibility(View.VISIBLE);

                        new Thread(new EditUserProfileCaller(clientInformation,EditProfileFragment.this)).start();

                    } else {

                        DialogShower dialogShower = new DialogShower(R.layout.internet_error, getContext());
                        dialogShower.showDialog();
                    }


                }
                else
                {
                    Toast.makeText(getContext(), "miss", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    private boolean emailPatternChecker(String email)
    {


        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());

    }

    private void emailHandler()
    {

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(emailBackup.equals(s.toString()))
                {
                    verify.setVisibility(View.GONE);
                }
                else
                {
                    verify.setVisibility(View.VISIBLE

                    );

                }


            }

            @Override
            public void afterTextChanged(Editable s) {




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




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {



                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");

                startActivityForResult(intent, 1);


            } else {

                if(shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE))
                {

                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                            ,Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

                }
                else
                {
                    Intent intent=new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri=Uri.fromParts("package", requireContext().getPackageName(),null);
                    intent.setData(uri);
                    requireContext().startActivity(intent);

                }
            }
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == Activity.RESULT_OK)
        {

            assert data != null;
            Uri image=data.getData();

            ContentResolver contentResolver= requireContext().getContentResolver();
            Cursor cursor=contentResolver.query(image,null,null,null,null);

            if(cursor!=null)
            {
                while (cursor.moveToNext())
                {

                    File file=new File(cursor.getString(cursor.getColumnIndex("_data")));

                    this.selected_image_file=file.toString();

                    profile_image.setImageURI(image);

                    Log.d(TAG, "onActivityResult: " + selected_image_file);


                }

                cursor.close();

            }

        }

    }

    private void establishViews(View view)
    {
        firstName = view.findViewById(R.id.firstName);
        surname = view.findViewById(R.id.surname);
        email = view.findViewById(R.id.email);
        ImageView select_image = view.findViewById(R.id.select_image);
        profile_image = view.findViewById(R.id.profile_image);
        loading = view.findViewById(R.id.loading);
        save = view.findViewById(R.id.save);
        verify = view.findViewById(R.id.verify);
        otp_box_layout = new Dialog(getContext());

    }


    private void setDefault() {

        email.setError(null);
        firstName.setError(null);
        surname.setError(null);
        isEmailFilled = false;
        email_confirmation = false;
        isNameFilled = false;
        name_confirmation = false;
        email_assign = null;
        name_assign = null;
        surname_confirmation=false;
        isSurnameFilled=false;
        surname_assign=null;


    }


    private void setData()
    {

        SharedPreferences preferences= requireContext().getSharedPreferences(ClientMainFrame.client_information_file, Context.MODE_PRIVATE);
        String json = preferences.getString(ClientMainFrame.client_information_key, "");
        Gson gson = new Gson();
        ClientInformation clientInformation = gson.fromJson(json, ClientInformation.class);


        firstName.setText(clientInformation.getFirstName());
        surname.setText(clientInformation.getSurName());
        email.setText(clientInformation.getEmail());
        emailBackup=clientInformation.getEmail();
        Picasso.with(getContext()).load(Uri.parse(clientInformation.getProfileImage())).error(R.drawable.no).into(profile_image);
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

    private void resendOtpCaller() {

        if (!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED)) {

            ClientInformation clientInformation = new ClientInformation();
            clientInformation.setEmail(emailBackup);

            new Thread(new EmailPhoneCheckerCaller(clientInformation, EditProfileFragment.this)).start();

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
    public void informationEditProfileCaller(ClientInformation clientInformation) {

        updateDataIntoSharedPreferencesAsUser(clientInformation);

        handler.post(new Runnable() {
            @Override
            public void run() {

                save.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);

                requireActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.main_frame_layout,new ViewProfileFragment()).addToBackStack(null).commit();
            }
        });

    }

    private void updateDataIntoSharedPreferencesAsUser(ClientInformation clientInformation)
    {

        SharedPreferences sharedPreferences= requireContext().getSharedPreferences(ClientMainFrame.client_information_file, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor=sharedPreferences.edit();

        editor.clear();
        editor.apply();

        Gson gson = new Gson();

        SharedPreferences sharedPreferences3= requireContext().getSharedPreferences(ClientMainFrame.client_information_file, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor2=sharedPreferences3.edit();

        String json1 = gson.toJson(clientInformation);

        editor2.putString(ClientMainFrame.client_information_key,json1);

        editor2.apply();

    }
}