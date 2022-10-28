package com.medical.mypockethealth.BaseFragments;

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

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.medical.mypockethealth.ApplicationBase.BaseActivity;
import com.medical.mypockethealth.BaseFragments.RegisterSection.ProviderRegistrationSection.SignUpAsProviderFragment;
import com.medical.mypockethealth.BaseFragments.RegisterSection.UserRegisterSection.SignUpAsUserFragment;
import com.medical.mypockethealth.Classes.ClientInformation.ClientInformation;
import com.medical.mypockethealth.Classes.DialogShower;
import com.medical.mypockethealth.Classes.NetworkSection.NetworkStatus;
import com.medical.mypockethealth.Classes.ProviderInformation.ProviderInformation;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.ClientActivity.ClientMainFrame;
import com.medical.mypockethealth.ProviderActivity.ProviderMainFrame;
import com.medical.mypockethealth.R;
import com.medical.mypockethealth.Threads.BaseThreads.OTPVerificationCaller;
import com.medical.mypockethealth.Threads.BaseThreads.ProviderSection.ProviderRegisterCaller;
import com.medical.mypockethealth.Threads.BaseThreads.UserSection.UserRegisterCaller;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.time.LocalDate;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

public class VerificationFragment extends Fragment implements UserRegisterCaller.CallbackFromMerchantRegisterCaller
        , ProviderRegisterCaller.CallbackFromProviderRegisterCaller, OTPVerificationCaller.CallbackFromOTPVerificationCaller {

    private static final String TAG = "VerificationFragment";

    public static final String data_key = "data_key";

    private boolean match_status = false;
    private String entered_otp = null;
    private Button submit;
    private Dialog loading_box;
    private TextView loading;
    private ClientInformation clientInformation;
    private ProviderInformation providerInformation;
    private final Handler handler = new Handler();


    public VerificationFragment() {
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
        return inflater.inflate(R.layout.fragment_verification, container, false);
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

        new Thread(new OTPVerificationCaller(enterOtp, requestId, VerificationFragment.this)).start();

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

                        storageReference.child(providerInformation.getRegistrationDocumentName()).putFile(Uri.parse(providerInformation.getRegistrationDocument())).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                                    @Override
                                    public void onSuccess(Uri uri) {

                                        providerInformation.setRegistrationDocument(uri.toString());


                                        if (!providerInformation.getProfileImage().equals(SignUpAsUserFragment.IdentityType.notSelected.toString())) {


                                            storageReference.child(providerInformation.getProfileImageName()).putFile(Uri.parse(providerInformation.getProfileImage())).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                                                        @Override
                                                        public void onSuccess(Uri uri) {

                                                            providerInformation.setProfileImage(uri.toString());

                                                            new Thread(new ProviderRegisterCaller(providerInformation, VerificationFragment.this)).start();

                                                        }
                                                    });

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                    loading_box.dismiss();
                                                    submit.setText(R.string.submit);
                                                    submit.setEnabled(true);

                                                    Log.d(TAG, "onFailure: error: " + e.getMessage());


                                                }
                                            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                                                    Log.d(TAG, "onProgress: " + (snapshot.getBytesTransferred() / snapshot.getTotalByteCount()) * 100);

                                                    long value = (snapshot.getBytesTransferred() / snapshot.getTotalByteCount()) * 100;
                                                    int progress_status = (int) value;

                                                    String status = "getting profile ready.. " + progress_status + "%";

                                                    loading.setText(status);

                                                }
                                            });


                                        } else {


                                            new Thread(new ProviderRegisterCaller(providerInformation, VerificationFragment.this)).start();
                                        }


                                    }
                                });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                loading_box.dismiss();
                                submit.setText(R.string.submit);
                                submit.setEnabled(true);

                                Log.d(TAG, "onFailure: error: " + e.getMessage());


                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                                Log.d(TAG, "onProgress: " + (snapshot.getBytesTransferred() / snapshot.getTotalByteCount()) * 100);

                                long value = (snapshot.getBytesTransferred() / snapshot.getTotalByteCount()) * 100;
                                int progress_status = (int) value;

                                String status = "creating profile.. " + progress_status + "%";

                                loading.setText(status);

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

    private void openLoadingBox() {
        loading_box = new Dialog(getContext());
        loading_box.setContentView(R.layout.register_loading_layout);
        loading_box.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading_box.setCanceledOnTouchOutside(false);
        loading_box.setCancelable(false);
        Window window = loading_box.getWindow();
        window.setGravity(Gravity.CENTER);
        loading_box.show();
        loading = loading_box.findViewById(R.id.loading);


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
    public void confirmationUserRegisterCaller(ResponseInformation responseInformation) {
        handler.post(new Runnable() {
            @Override
            public void run() {

                submit.setText(R.string.submit);
                submit.setEnabled(true);

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

                submit.setText(R.string.submit);
                submit.setEnabled(true);

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


    private void addDataIntoSharedPreferencesAsProvider(ProviderInformation providerInformation) {

        ProviderMainFrame.id = providerInformation.getId();

        providerInformation.setPasswordBackup(SignUpAsProviderFragment.passwordBackup);

        providerInformation.setPracticeNumberPhone(getString(R.string.None));

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(ProviderMainFrame.provider_information_file, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();

        String json = gson.toJson(providerInformation);

        editor.putString(ProviderMainFrame.provider_information_key, json);

        editor.apply();

    }


    @Override
    public void confirmationProviderRegisterCaller(ResponseInformation responseInformation) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                submit.setText(R.string.submit);
                submit.setEnabled(true);
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

                submit.setText(R.string.submit);
                submit.setEnabled(true);
                loading_box.dismiss();

                requireActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.frame_holder, new CongratulationsFragment()).addToBackStack(null).commit();

            }
        });

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

                    if (BaseActivity.mode == 0) {

                        new Thread(new UserRegisterCaller(clientInformation, VerificationFragment.this)).start();

                    } else {
                        openLoadingBox();
                        uploadCloudInformation(providerInformation);
                    }
                }

            }
        });

    }
}