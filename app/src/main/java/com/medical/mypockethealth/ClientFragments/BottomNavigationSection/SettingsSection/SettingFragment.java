package com.medical.mypockethealth.ClientFragments.BottomNavigationSection.SettingsSection;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.medical.mypockethealth.ApplicationBase.BaseActivity;
import com.medical.mypockethealth.BaseFragments.RegisterSection.PolicyFragment;
import com.medical.mypockethealth.Classes.ClientInformation.ClientInformation;
import com.medical.mypockethealth.Classes.DialogShower;
import com.medical.mypockethealth.Classes.NetworkSection.NetworkStatus;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.Classes.URLBuilder;
import com.medical.mypockethealth.ClientActivity.ClientMainFrame;
import com.medical.mypockethealth.ClientFragments.BottomNavigationSection.SettingsSection.ChatSection.PatientChatFragment;
import com.medical.mypockethealth.ClientFragments.BottomNavigationSection.SettingsSection.ProfileSection.EditProfileFragment;
import com.medical.mypockethealth.ClientFragments.BottomNavigationSection.SettingsSection.ProfileSection.EditUserInformationFragment;
import com.medical.mypockethealth.ClientFragments.BottomNavigationSection.SettingsSection.ProfileSection.ViewProfileFragment;
import com.medical.mypockethealth.ClientFragments.HomeFragment;
import com.medical.mypockethealth.R;
import com.medical.mypockethealth.Threads.BaseThreads.LogOutCaller;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.medical.mypockethealth.Threads.UserSection.SettingSection.UpdatePasswordCaller;


public class SettingFragment extends Fragment implements LogOutCaller.CallbackFromLogOutCaller,UpdatePasswordCaller.CallbackFromUpdatePasswordCaller {


    private TextView create_profile,view_profile;
    private final Handler handler=new Handler();
    private Dialog logout_box,password_box;
    private ImageView logout_loading,password_loading;
    private Button logout_ok,password_ok;
    private boolean isCurrentPasswordFilled=false;
    private boolean isNewPasswordFilled=false;
    private boolean current_password_confirmation=false;
    private boolean new_password_confirmation=false;
    private final boolean password_match_confirmation=false;
    private final boolean isPassword_matched=false;
    private LinearLayout user_information;
    private String current_password_assign=null,new_password_assign=null;

    public SettingFragment() {
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
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        establishViews(view);

        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), ClientMainFrame.class));
            }
        });

        view.findViewById(R.id.change_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                passwordHandler();
            }
        });

        view.findViewById(R.id.chat_with_us).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requireActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.main_frame_layout,new PatientChatFragment()).addToBackStack(null).commit();

            }
        });
        

        user_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requireActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.main_frame_layout,new EditUserInformationFragment()).addToBackStack(null).commit();

            }
        });

        view.findViewById(R.id.my_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requireActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.main_frame_layout,new ViewProfileFragment()).addToBackStack(null).commit();
            }
        });

        view.findViewById(R.id.edit_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requireActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.main_frame_layout,new EditProfileFragment()).addToBackStack(null).commit();

            }
        });


        view.findViewById(R.id.privacy_policy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle=new Bundle();
                bundle.putString(PolicyFragment.data_key,URLBuilder.policy_link);

                PolicyFragment policyFragment=new PolicyFragment();
                policyFragment.setArguments(bundle);

                requireActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.main_frame_layout,policyFragment).addToBackStack(null).commit();
            }
        });


        view.findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                logoutHandler();

            }
        });

        enableBottomNav();

    }


    private ClientInformation getUserInformation()
    {
        SharedPreferences preferences= requireContext().getSharedPreferences(ClientMainFrame.client_information_file, Context.MODE_PRIVATE);
        String json = preferences.getString(ClientMainFrame.client_information_key, "");
        Gson gson = new Gson();
        return gson.fromJson(json, ClientInformation.class);
    }

    private void passwordHandler()
    {

        password_box=new Dialog(getContext());
        password_box.setContentView(R.layout.password_layout);
        password_box.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        password_box.setCanceledOnTouchOutside(true);
        Window window=password_box.getWindow();
        window.setGravity(Gravity.CENTER);
        password_box.show();

        TextInputLayout current_password_layout,new_password_layout;
        EditText current_password,new_password;

        current_password_layout=password_box.findViewById(R.id.current_password_layout);
        new_password_layout=password_box.findViewById(R.id.new_password_layout);
        current_password=password_box.findViewById(R.id.current_password);
        new_password=password_box.findViewById(R.id.new_password);

        password_loading=password_box.findViewById(R.id.loading);
        password_ok=password_box.findViewById(R.id.ok);

        password_loading.setVisibility(View.GONE);

        password_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(current_password.getText().toString().trim().length()==0)
                {
                    current_password_layout.setError("please enter your current password");

                    isCurrentPasswordFilled=false;
                }
                else
                {
                    current_password_layout.setError(null);

                    current_password_assign=current_password.getText().toString();

                    current_password_confirmation=true;

                    isCurrentPasswordFilled=true;

                }

                if(new_password.getText().toString().trim().length()==0)
                {
                    new_password_layout.setError("please enter your new  password");

                    isNewPasswordFilled=false;
                }
                else
                {

                    new_password_layout.setError(null);

                    new_password_assign=new_password.getText().toString();

                    new_password_confirmation=true;

                    isNewPasswordFilled=true;

                }


                if(current_password_confirmation && new_password_confirmation)
                {

                    if(passwordConfirmation(current_password_assign))
                    {
                        if(!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED))
                        {

                            password_loading.setVisibility(View.VISIBLE);
                            password_ok.setVisibility(View.GONE);


                            new Thread(new UpdatePasswordCaller(new_password_assign,BaseActivity.mode,
                                    SettingFragment.this)).start();

                        }

                        else
                        {
                            DialogShower dialogShower=new DialogShower(R.layout.internet_error,getContext());
                            dialogShower.showDialog();
                        }

                    }
                    else
                    {
                        current_password_layout.setError("Invalid current password");

                    }


                }

            }
        });


    }

    private boolean passwordConfirmation(String password)
    {
        SharedPreferences sharedPreferences= requireContext().getSharedPreferences(ClientMainFrame.client_information_file, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(ClientMainFrame.client_information_key, "");

        Gson gson = new Gson();
        ClientInformation clientInformation = gson.fromJson(json,ClientInformation.class);

        return password.equals(clientInformation.getPasswordBackup());
    }

    private void logoutHandler()
    {
        logout_box=new Dialog(getContext());
        logout_box.setContentView(R.layout.logout_layout);
        logout_box.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        logout_box.setCanceledOnTouchOutside(true);
        Window window=logout_box.getWindow();
        window.setGravity(Gravity.CENTER);
        logout_box.show();

        logout_loading =logout_box.findViewById(R.id.loading);
        logout_ok =logout_box.findViewById(R.id.ok);

        logout_loading.setVisibility(View.GONE);

        logout_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED)) {

                    logout_loading.setVisibility(View.VISIBLE);
                    logout_ok.setVisibility(View.GONE);


                    new Thread(new LogOutCaller(ClientMainFrame.id, SettingFragment.this,0)).start();


                } else {
                    DialogShower dialogShower=new DialogShower(R.layout.internet_error,getContext());
                    dialogShower.showDialog();
                }

            }
        });

    }

    private void establishViews(View view)
    {

        create_profile=view.findViewById(R.id.create_profile);
        view_profile=view.findViewById(R.id.view_profile);
        user_information=view.findViewById(R.id.user_information);

        if(getUserInformation().getJourneyInformationStatus().equals("1"))
        {
            user_information.setVisibility(View.VISIBLE);
        }
        else
        {
            user_information.setVisibility(View.GONE);
        }
    }

    @Override
    public void confirmationLogOutCaller(ResponseInformation responseInformation) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                logout_loading.setVisibility(View.GONE);
                logout_ok.setVisibility(View.VISIBLE);
                logout_box.dismiss();

                if(responseInformation.getSuccess().equals(ResponseInformation.fail_response_type))
                {

                    Toast.makeText(getContext(),responseInformation.getMessage(),Toast.LENGTH_SHORT).show();

                }
                else
                {
                    SharedPreferences sharedPreferences=requireActivity().getSharedPreferences(ClientMainFrame.client_information_file, Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor=sharedPreferences.edit();

                    editor.clear();
                    editor.apply();

                    SharedPreferences sharedPreferences1=requireActivity().getSharedPreferences(ClientMainFrame.client_edit_information_file, Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor1=sharedPreferences1.edit();

                    editor1.clear();
                    editor1.apply();


                    googleSignInHandler();


                    startActivity(new Intent(getContext(), BaseActivity.class));

                }

            }
        });


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

           mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
               @Override
               public void onComplete(@NonNull Task<Void> task) {
              
                   
               }
           });

        }


    }

    @Override
    public void confirmationUpdatePasswordCaller(ResponseInformation responseInformation) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                password_loading.setVisibility(View.GONE);
                password_ok.setVisibility(View.VISIBLE);
                password_box.dismiss();

                if(responseInformation.getSuccess().equals(ResponseInformation.fail_response_type))
                {

                    Toast.makeText(getContext(),responseInformation.getMessage(),Toast.LENGTH_SHORT).show();

                }
                else
                {

                    updateFile();

                    Toast.makeText(getContext(),responseInformation.getMessage(),Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getContext(),ClientMainFrame.class));

                }

            }
        });


    }

    private void updateFile()
    {

        SharedPreferences sharedPreferences= requireContext().getSharedPreferences(ClientMainFrame.client_information_file, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(ClientMainFrame.client_information_key, "");

        Gson gson = new Gson();
        ClientInformation clientInformation = gson.fromJson(json,ClientInformation.class);
        clientInformation.setPasswordBackup(new_password_assign);

        SharedPreferences sharedPreferences1=requireActivity().getSharedPreferences(ClientMainFrame.client_information_file, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor1=sharedPreferences1.edit();

        editor1.clear();
        editor1.apply();


        SharedPreferences sharedPreferences3= requireContext().getSharedPreferences(ClientMainFrame.client_information_file, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor2=sharedPreferences3.edit();

        String json1 = gson.toJson(clientInformation);

        editor2.putString(ClientMainFrame.client_information_key,json1);

        editor2.apply();

    }

    private void enableBottomNav() {

        View view1 = requireActivity().findViewById(R.id.meowBottomNavigation);

        view1.setVisibility(View.VISIBLE);

    }

}