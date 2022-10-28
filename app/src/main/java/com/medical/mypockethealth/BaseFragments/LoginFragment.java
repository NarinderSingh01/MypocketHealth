

package com.medical.mypockethealth.BaseFragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.hbb20.CountryCodePicker;
import com.medical.mypockethealth.ApplicationBase.BaseActivity;
import com.medical.mypockethealth.BaseFragments.RegisterSection.UserRegisterSection.SignUpAsUserFragment;
import com.medical.mypockethealth.BaseFragments.RegisterSection.UserRegisterSection.UserInformationFragment;
import com.medical.mypockethealth.Classes.ClientInformation.ClientInformation;
import com.medical.mypockethealth.Classes.DialogShower;
import com.medical.mypockethealth.Classes.EHRSection.FollowUpInformation.FollowUpInformation;
import com.medical.mypockethealth.Classes.NetworkSection.NetworkStatus;
import com.medical.mypockethealth.Classes.ProviderInformation.ProviderInformation;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.ClientActivity.ClientMainFrame;
import com.medical.mypockethealth.ProviderActivity.ProviderMainFrame;
import com.medical.mypockethealth.ProviderFragments.MainFrameSection.SettingSection.ProfileSection.ViewProfileSection.ViewProfileFragment;
import com.medical.mypockethealth.R;
import com.medical.mypockethealth.Threads.BaseThreads.ForgotPasswordCaller;
import com.medical.mypockethealth.Threads.BaseThreads.LoginCaller;
import com.medical.mypockethealth.Threads.BaseThreads.LoginWithGoogleCaller;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.medical.mypockethealth.Threads.BaseThreads.OtpCaller;

import org.jetbrains.annotations.NotNull;


public class LoginFragment extends Fragment implements LoginCaller.CallbackFromLoginCaller,LoginWithGoogleCaller.CallbackFromLoginWithGoogleCaller,
        ForgotPasswordCaller.CallbackFromForgotPasswordCaller{

    private static final String TAG = "LoginFragment";

    private TextInputLayout user_id,edit_password;
    private EditText phone_medical,password;
    private Button forgot_password,login_vula,done;
    private RelativeLayout google_login,facebook;
    private LinearLayout bottom_line;
    private LinearLayout poweredBy,submit,upper_side;
    private ImageView front,loading;
    private Button login;
    private String passwordBackup,requestId;
    public static final int google_response_code=0;
    private GoogleSignInClient mGoogleSignInClient;
    private final Handler handler=new Handler();
    private Dialog loading_box;
    private ClientInformation clientInformation;
    private BottomSheetDialog forgotBottomSheet;
    private boolean phone_medical_confirmation=false,password_confirmation=false,isPhoneMedicalFilled=false,isPasswordFilled=false;
    private String phone_medical_assign=null,password_assign=null;

    public LoginFragment() {
        // Required empty public constructor
    }

 

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if(BaseActivity.mode==0)
        {
            return inflater.inflate(R.layout.fragment_login, container, false);
        }
        else
        {
            return inflater.inflate(R.layout.provider_login_layout, container, false);
        }

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        establishViews(view);

        animationApplier();

        loading.setVisibility(View.GONE);

        CountryCodePicker codePicker;

        codePicker=view.findViewById(R.id.code_picker);


        google_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(loading.getVisibility()==View.VISIBLE)
                {
                    Toast.makeText(getContext(),"Please wait..",Toast.LENGTH_SHORT).show();
                }
                else
                {

                    openLoadingBox();

                    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                    startActivityForResult(signInIntent, google_response_code);

                }

            }
        });


        if(BaseActivity.mode==1)
        {
            login_vula.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(getContext(),"coming soon",Toast.LENGTH_SHORT).show();
                }
            });
        }

        view.findViewById(R.id.forgot_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openBottomSheet();

            }
        });

         login.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 if (phone_medical.getText().toString().trim().length() == 0) {

                     user_id.setError("Please enter your phone number");

                     isPhoneMedicalFilled = false;

                 } else if (!phonePatternChecker(phone_medical.getText().toString())) {

                     user_id.setError("Invalid phone number or medical number");

                 } else {
                     user_id.setError(null);

                     phone_medical_assign = phone_medical.getText().toString();
                     phone_medical_confirmation = true;
                     isPhoneMedicalFilled = true;

                 }


                 if (password.getText().toString().trim().length() == 0) {

                     edit_password.setError("Please enter your password");

                     isPasswordFilled = false;
                 } else {
                     edit_password.setError(null);
                     password_assign = password.getText().toString();
                     password_confirmation = true;
                     isPasswordFilled = true;

                 }



                 if(phone_medical_confirmation && password_confirmation && isPhoneMedicalFilled && isPasswordFilled)
                 {


                     @SuppressLint("HardwareIds") String android_id = Settings.
                             Secure.getString(requireContext().getContentResolver(),
                             Settings.Secure.ANDROID_ID);

                     ClientInformation clientInformation = new ClientInformation();

                     clientInformation.setLoginType(phone_medical_assign);
                     clientInformation.setPassword(password_assign);
                     clientInformation.setCountryCode(codePicker.getSelectedCountryCode());
                     passwordBackup=password_assign;
                     clientInformation.setDeviceId(android_id);
                     clientInformation.setDeviceType("android");
                     clientInformation.setRegId(getRegId());

                     ProviderInformation providerInformation=new ProviderInformation();

                     providerInformation.setLoginType(phone_medical_assign);
                     providerInformation.setPassword(password_assign);
                     providerInformation.setCountryCode(codePicker.getSelectedCountryCode());
                     passwordBackup=password_assign;
                     providerInformation.setDeviceId(android_id);
                     providerInformation.setDeviceType("android");
                     providerInformation.setRegId(getRegId());


                     if (!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED)) {

                         loading.setVisibility(View.VISIBLE);
                         login.setVisibility(View.GONE);


                         if(BaseActivity.mode==0)
                         {

                             new Thread(new LoginCaller(clientInformation,LoginFragment.this,0)).start();

                         }
                         else
                         {

                             new Thread(new LoginCaller(providerInformation,LoginFragment.this,1)).start();
                         }


                     } else {
                         DialogShower dialogShower=new DialogShower(R.layout.internet_error,getContext());
                         dialogShower.showDialog();
                     }

                 }

             }
         });


    }



    private void openBottomSheet()
    {

         forgotBottomSheet = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme);

        View bottom_view = LayoutInflater.from(getContext()).inflate(R.layout.phone_number_layout
                ,
                (RelativeLayout) forgotBottomSheet.findViewById(R.id.relative_layout));

        forgotBottomSheet.setContentView(bottom_view);

        forgotBottomSheet.show();

        forgotBottomSheet.setCanceledOnTouchOutside(false);
        forgotBottomSheet.setCancelable(false);

        EditText phoneNumber=forgotBottomSheet.findViewById(R.id.phone_number);
        CountryCodePicker codePicker=forgotBottomSheet.findViewById(R.id.code_picker);
        done=forgotBottomSheet.findViewById(R.id.done);


        forgotBottomSheet.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                forgotBottomSheet.dismiss();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (phoneNumber.getText().toString().trim().length() == 0) {

                    phoneNumber.setError("Please enter your phone number");


                } else if (!phonePatternChecker(phoneNumber.getText().toString())) {

                    phoneNumber.setError("Invalid phone number");

                } else {

                    phoneNumber.setError(null);

                    clientInformation = new ClientInformation();

                    clientInformation.setPhone(phoneNumber.getText().toString());
                    clientInformation.setCountryCode(codePicker.getSelectedCountryCode());

                    resetPassword(clientInformation);

                }

            }
        });

    }



    private void resetPassword(ClientInformation clientInformation)
    {

        this.clientInformation=clientInformation;

        if (!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED)) {

            done.setText(R.string.loading);

            new Thread(new ForgotPasswordCaller(clientInformation, LoginFragment.this)).start();

        } else {
            DialogShower dialogShower = new DialogShower(R.layout.internet_error, getContext());
            dialogShower.showDialog();
        }

    }


    @Override
    public void confirmationCallbackFromForgotPasswordCaller(ResponseInformation responseInformation) {


        handler.post(new Runnable() {
            @Override
            public void run() {

               done.setText(R.string.send_otp);

                if (responseInformation.getSuccess().equals(ResponseInformation.fail_response_type)) {

                    if (responseInformation.getMessage().equals(getString(R.string.ready_send))) {

                        forgotBottomSheet.dismiss();

                        ForgotPasswordVerificationFragment forgotPasswordVerificationFragment = new ForgotPasswordVerificationFragment();
                        Bundle bundle = new Bundle();

                        clientInformation.setRequestId(requestId);

                        bundle.putSerializable(VerificationFragment.data_key, clientInformation);
                        forgotPasswordVerificationFragment.setArguments(bundle);

                        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_holder,
                                forgotPasswordVerificationFragment).addToBackStack(null).commit();
                    } else {
                        Toast.makeText(getContext(), responseInformation.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                } else {


                    forgotBottomSheet.dismiss();

                    ForgotPasswordVerificationFragment forgotPasswordVerificationFragment = new ForgotPasswordVerificationFragment();
                    Bundle bundle = new Bundle();

                    clientInformation.setRequestId(responseInformation.getRequestId());

                    requestId = responseInformation.getRequestId();

                    bundle.putSerializable(VerificationFragment.data_key, clientInformation);
                    forgotPasswordVerificationFragment.setArguments(bundle);

                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_holder,
                            forgotPasswordVerificationFragment).addToBackStack(null).commit();
                }

            }
        });



    }


    private void openLoadingBox()
    {
        loading_box =new Dialog(getContext());
        loading_box.setContentView(R.layout.loading_layout);
        loading_box.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading_box.setCanceledOnTouchOutside(false);
        loading_box.setCancelable(false);
        Window window= loading_box.getWindow();
        window.setGravity(Gravity.CENTER);
        loading_box.show();

    }


    private String getRegId()
    {

        return FirebaseInstanceId.getInstance().getToken();

    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        Log.d(TAG, "onActivityResult: result called");

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == google_response_code) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        else
        {
            loading_box.dismiss();
            Log.d(TAG, "onActivityResult: false");
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {

        Log.d(TAG, "handleSignInResult: called");

        if(completedTask.getException()==null)
        {

            Log.d(TAG, "handleSignInResult: null");

            try {

                GoogleSignInAccount account = completedTask.getResult(ApiException.class);


                Log.d(TAG, "handleSignInResult: " + account.getDisplayName());

                @SuppressLint("HardwareIds") String android_id = Settings.
                        Secure.getString(requireContext().getContentResolver(),
                        Settings.Secure.ANDROID_ID);

                if(BaseActivity.mode==0)
                {
                    // client area

                    ClientInformation clientInformation=new ClientInformation();
                    clientInformation.setUsername(account.getDisplayName());
                    clientInformation.setEmail(account.getEmail());
                    clientInformation.setSocialId(account.getId());
                    clientInformation.setDeviceId(android_id);
                    clientInformation.setRegId("");
                    clientInformation.setDeviceType("android");

                    if(account.getPhotoUrl()!=null)
                    {
                        clientInformation.setProfileImage(account.getPhotoUrl().toString());
                    }
                    else
                    {
                        clientInformation.setProfileImage(ViewProfileFragment.ImageStatus.notSelected.toString());
                    }

                    new Thread(new LoginWithGoogleCaller(clientInformation,LoginFragment.this,0)).start();

                }
                else if(BaseActivity.mode==1)
                {
                    // provider area


                }


            } catch (ApiException e) {


                loading_box.dismiss();
                Toast.makeText(getContext(),"We are having technical issue. Please try again later",Toast.LENGTH_SHORT).show();


            }
        }
        else
        {
            loading_box.dismiss();
            Log.d(TAG, "handleSignInResult: not null" + completedTask.getException().getMessage());
        }

    }

    private void googleSignInHandler()
    {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(requireContext());

        if(account!=null)
        {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);

            mGoogleSignInClient.signOut();

        }
    }
    @Override
    public void onStart() {
        super.onStart();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(requireContext());

        if(account!=null)
        {
            Log.d(TAG, "onStart: sign in");

        }
        else
        {
            Log.d(TAG, "onStart: sign out");
        }
    }

    private void loginInWithGoogle()
    {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso);

    }
    private boolean phonePatternChecker(String phone) {


        return (!TextUtils.isEmpty(phone) && Patterns.PHONE.matcher(phone).matches());

    }

    private void establishViews(View view)
    {
        user_id=view.findViewById(R.id.details_location_layout);
        edit_password=view.findViewById(R.id.password_layout);
        forgot_password=view.findViewById(R.id.forgot_password);
        google_login=view.findViewById(R.id.google_login);
        facebook=view.findViewById(R.id.facebook_login);
        bottom_line=view.findViewById(R.id.bottom_line);
        submit=view.findViewById(R.id.view_cart);
        poweredBy=view.findViewById(R.id.poweredBy);
        front=view.findViewById(R.id.front);
        phone_medical=view.findViewById(R.id.phone_medical);
        password=view.findViewById(R.id.editPassword);
        loading=view.findViewById(R.id.loading);
        login=view.findViewById(R.id.login);
        upper_side=view.findViewById(R.id.upper_side);

        if(BaseActivity.mode==1)
        {
            login_vula=view.findViewById(R.id.vula);
        }


        
    }

    private void animationApplier()
    {

        poweredBy.setVisibility(View.GONE);
        bottom_line.setVisibility(View.GONE);

        Animation top;

        top= AnimationUtils.loadAnimation(getContext(),R.anim.top_mover);

        front.setAnimation(top);

        upper_side.setTranslationY(800);
        edit_password.setTranslationY(800);
        forgot_password.setTranslationY(800);
        submit.setTranslationY(800);
        poweredBy.setTranslationY(800);
        google_login.setTranslationY(800);
        facebook.setTranslationY(800);

        upper_side.setAlpha(0);
        edit_password.setAlpha(0);
        forgot_password.setAlpha(0);
        submit.setAlpha(0);
        poweredBy.setAlpha(0);
        google_login.setAlpha(0);
        facebook.setAlpha(0);


        upper_side.animate().translationY(0).alpha(1).setDuration(600).setStartDelay(300).start();
        edit_password.animate().translationY(0).alpha(1).setDuration(600).setStartDelay(400).start();
        forgot_password.animate().translationY(0).alpha(1).setDuration(600).setStartDelay(500).start();
        submit.animate().translationY(0).alpha(1).setDuration(600).setStartDelay(600).start();
        poweredBy.animate().translationY(0).alpha(1).setDuration(600).setStartDelay(700).start();
        google_login.animate().translationY(0).alpha(1).setDuration(600).setStartDelay(800).start();
        facebook.animate().translationY(0).alpha(1).setDuration(600).setStartDelay(900).start();


    }


    @Override
    public void confirmation(ResponseInformation responseInformation) {
        handler.post(new Runnable() {
            @Override
            public void run() {

                login.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);

                if(responseInformation.getSuccess().equals(ResponseInformation.fail_response_type))
                {

                    Toast.makeText(getContext(),responseInformation.getMessage(),Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    @Override
    public void information(Object information, String modeType) {

        addDataIntoSharedPreferences(information,modeType);

        handler.post(new Runnable() {
            @Override
            public void run() {

                login.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);

                if(modeType.equals(ResponseInformation.userMode))
                {
                    ClientInformation clientInformation=(ClientInformation)information;

                    if(clientInformation.getJourneyInformationStatus().equals("0"))
                    {
                        requireActivity().getSupportFragmentManager().beginTransaction().
                                replace(R.id.frame_holder,new UserInformationFragment()).addToBackStack(null).commit();
                    }
                    else
                    {
                        startActivity(new Intent(getContext(), ClientMainFrame.class));
                    }

                }
                else if(modeType.equals(ResponseInformation.providerMode))
                {

                    requireContext().startActivity(new Intent(getContext(), ProviderMainFrame.class));

                }
            }
        });

    }

    private void addDataIntoSharedPreferences(Object object,String mode)
    {

        if (mode.equals(ResponseInformation.userMode))
        {
            ClientInformation clientInformation=(ClientInformation) object;
            ClientMainFrame.id=clientInformation.getId();

            clientInformation.setPasswordBackup(passwordBackup);

            SharedPreferences sharedPreferences= requireContext().getSharedPreferences(
                    ClientMainFrame.client_information_file, Context.MODE_PRIVATE);

            SharedPreferences.Editor editor=sharedPreferences.edit();

            Gson gson = new Gson();

            String json = gson.toJson(clientInformation);

            editor.putString(ClientMainFrame.client_information_key,json);

            editor.apply();

        }
        else if(mode.equals(ResponseInformation.providerMode))
        {
            ProviderInformation providerInformation=(ProviderInformation)object;
            ProviderMainFrame.id=providerInformation.getId();

            providerInformation.setPasswordBackup(passwordBackup);

            providerInformation.setActivationStatus(providerInformation.getProfileStatus());

            SharedPreferences sharedPreferences= requireContext().getSharedPreferences(
                    ProviderMainFrame.provider_information_file, Context.MODE_PRIVATE);

            SharedPreferences.Editor editor=sharedPreferences.edit();

            Gson gson = new Gson();

            String json = gson.toJson(providerInformation);

            Log.d(TAG, "addDataIntoSharedPreferences: "+providerInformation.getUserTitle());
            Log.d(TAG, "addDataIntoSharedPreferences: "+providerInformation.getUserType());

            editor.putString(ProviderMainFrame.provider_information_key,json);

            editor.apply();
            
            
            // this area or provider file section

            
//            if(providerInformation.getCreateProfileStatus().equals("1"))
//            {
//                SharedPreferences sharedPreferences1= requireContext().getSharedPreferences(
//                        ProviderMainFrame.provider_create_profile_file, Context.MODE_PRIVATE);
//
//                SharedPreferences.Editor editor1=sharedPreferences1.edit();
//
//                String json1 = gson.toJson(providerInformation);
//
//                editor1.putString(ProviderMainFrame.provider_create_profile_key,json1);
//
//                editor1.apply();
//
//            }
            
            
        }

    }


    
    @Override
    public void confirmationLoginWithGoogleCaller(ResponseInformation responseInformation) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                submit.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
                loading_box.dismiss();

                if(responseInformation.getSuccess().equals(ResponseInformation.fail_response_type))
                {

                    Toast.makeText(getContext(),responseInformation.getMessage(),Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    @Override
    public void informationLoginWithGoogleCaller(Object information, String modeType) {

        addDataIntoSharedPreferences(information,modeType);

        handler.post(new Runnable() {
            @Override
            public void run() {

                submit.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
                loading_box.dismiss();

                if(modeType.equals(ResponseInformation.userMode))
                {

                    requireContext().startActivity(new Intent(getContext(),ClientMainFrame.class));
                }
                else if(modeType.equals(ResponseInformation.providerMode))
                {

                    requireContext().startActivity(new Intent(getContext(),ProviderMainFrame.class));

                }

            }
        });
    }


}