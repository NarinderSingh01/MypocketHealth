package com.medical.mypockethealth.BaseFragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.medical.mypockethealth.ApplicationBase.BaseActivity;
import com.medical.mypockethealth.Classes.ClientInformation.ClientInformation;
import com.medical.mypockethealth.Classes.DialogShower;
import com.medical.mypockethealth.Classes.NetworkSection.NetworkStatus;
import com.medical.mypockethealth.Classes.ProviderInformation.ProviderInformation;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.ClientFragments.BottomNavigationSection.SettingsSection.SettingFragment;
import com.medical.mypockethealth.R;
import com.medical.mypockethealth.Threads.BaseThreads.OTPVerificationCaller;
import com.medical.mypockethealth.Threads.BaseThreads.UpdatePasswordCaller;
import com.medical.mypockethealth.Threads.BaseThreads.UserSection.UserRegisterCaller;

import org.jetbrains.annotations.NotNull;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;


public class ForgotPasswordVerificationFragment extends Fragment implements OTPVerificationCaller.CallbackFromOTPVerificationCaller,
        UpdatePasswordCaller.CallbackFromUpdatePasswordCaller
{

    private static final String TAG = "ForgotPasswordVerificat";


    public static final String data_key = "data_key";

    private boolean match_status = false;
    private String entered_otp = null;
    private Button submit;
    private Dialog password_box;
    private ImageView password_loading;
    private Button password_ok;
    private TextView loading;
    private ClientInformation clientInformation;
    private ProviderInformation providerInformation;
    private final Handler handler = new Handler();


    public ForgotPasswordVerificationFragment() {
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
        return inflater.inflate(R.layout.fragment_forgot_password_verification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        OtpTextView otpTextView = view.findViewById(R.id.otp_holder);
        submit = view.findViewById(R.id.submit);
        setOtpInformation(view);

        otpTextView.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {

            }

            @Override
            public void onOTPComplete(@NotNull String otp) {

                entered_otp = otp;

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (entered_otp == null) {
                    Toast.makeText(getContext(), "Please enter your OTP", Toast.LENGTH_SHORT).show();

                } else {


                    if (!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED)) {

                        submit.setText(R.string.loading);
                        submit.setEnabled(false);

                        assert getArguments() != null;

                        if (BaseActivity.mode == 0) {

                            clientInformation = (ClientInformation) getArguments().get(VerificationFragment.data_key);

                            otpVerification(entered_otp, clientInformation.getRequestId());

                        } else {

                            providerInformation = (ProviderInformation) getArguments().get(VerificationFragment.data_key);

                            otpVerification(entered_otp, providerInformation.getRequestId());

                        }

                    } else {
                        submit.setText(R.string.submit);
                        submit.setEnabled(true);

                        DialogShower dialogShower = new DialogShower(R.layout.internet_error, R.style.translate_animator, getContext());
                        dialogShower.showDialog();
                    }

                }


            }
        });
    }


    private void otpVerification(String enterOtp, String requestId) {

        new Thread(new OTPVerificationCaller(enterOtp, requestId, ForgotPasswordVerificationFragment.this)).start();

    }

    private void setOtpInformation(View view) {

        TextView textView = view.findViewById(R.id.otp_information);
        String value;

        assert getArguments() != null;

        if (BaseActivity.mode == 0) {
            ClientInformation clientInformation = (ClientInformation) getArguments().get(VerificationFragment.data_key);
            value = "A verification code has been sent to your phone number (" + clientInformation.getPhone() + ").Please enter the code below";

        } else {

            ProviderInformation providerInformation = (ProviderInformation) getArguments().get(VerificationFragment.data_key);
            value = "A verification code has been sent to your phone number (" + providerInformation.getPhoneNumber() + ").Please enter the code below";

        }

        textView.setText(value);
    }

    @Override
    public void confirmationOTPVerificationCaller(ResponseInformation responseInformation) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                if (responseInformation.getSuccess().equals(ResponseInformation.fail_response_type)) {

                    submit.setText(R.string.submit);
                    submit.setEnabled(true);

                    Toast.makeText(getContext(), responseInformation.getMessage(), Toast.LENGTH_SHORT).show();

                } else {

                    passwordHandler();

                }

            }
        });

    }


    private void passwordHandler()
    {

        password_box=new Dialog(getContext());
        password_box.setContentView(R.layout.new_password_layout);
        password_box.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        password_box.setCanceledOnTouchOutside(false);
        password_box.setCancelable(false);
        Window window=password_box.getWindow();
        window.setGravity(Gravity.CENTER);
        password_box.show();

        TextInputLayout new_password_layout;
        EditText new_password;


        new_password_layout=password_box.findViewById(R.id.new_password_layout);
        new_password=password_box.findViewById(R.id.new_password);

        password_loading=password_box.findViewById(R.id.loading);
        password_ok=password_box.findViewById(R.id.ok);

        password_loading.setVisibility(View.GONE);


        password_box.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submit.setText(R.string.submit);
                submit.setEnabled(true);
                password_box.dismiss();

            }
        });

        password_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(new_password.getText().toString().trim().length()==0)
                {
                    new_password_layout.setError("please enter your new password");

                }
                else
                {
                    new_password_layout.setError(null);

                    clientInformation.setPassword(new_password.getText().toString());


                    if(!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED))
                    {

                        password_loading.setVisibility(View.VISIBLE);
                        password_ok.setVisibility(View.GONE);

                        new Thread(new UpdatePasswordCaller(clientInformation, ForgotPasswordVerificationFragment.this)).start();
                    }

                    else
                    {
                        DialogShower dialogShower=new DialogShower(R.layout.internet_error,getContext());
                        dialogShower.showDialog();
                    }



                }



            }
        });


    }

    @Override
    public void confirmationCallbackFromUpdatePasswordCaller(ResponseInformation responseInformation) {


        handler.post(new Runnable() {
            @Override
            public void run() {

                password_loading.setVisibility(View.GONE);
                password_ok.setVisibility(View.VISIBLE);

                if (responseInformation.getSuccess().equals(ResponseInformation.fail_response_type)) {

                    Toast.makeText(getContext(), responseInformation.getMessage(), Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(getContext(), responseInformation.getMessage(), Toast.LENGTH_SHORT).show();

                    password_box.dismiss();

                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_holder,
                            new LoginFragment()).addToBackStack(null).commit();
                }

            }
        });


    }
}