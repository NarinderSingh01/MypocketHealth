package com.medical.mypockethealth.ApplicationFragments;

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

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.medical.mypockethealth.AgoraSection.VideoChatViewActivity;
import com.medical.mypockethealth.ApplicationBase.BaseActivity;
import com.medical.mypockethealth.BaseFragments.LoginOrSignUpFragment;
import com.medical.mypockethealth.BaseFragments.RegisterSection.UserRegisterSection.UserInformationFragment;
import com.medical.mypockethealth.Classes.ClientInformation.ClientInformation;
import com.medical.mypockethealth.Classes.ClientInformation.PatientStateInformation;
import com.medical.mypockethealth.Classes.DialogShower;
import com.medical.mypockethealth.Classes.NetworkSection.NetworkStatus;
import com.medical.mypockethealth.Classes.ProviderInformation.ProviderInformation;
import com.medical.mypockethealth.Classes.URLBuilder;
import com.medical.mypockethealth.ClientActivity.ClientMainFrame;
import com.medical.mypockethealth.ProviderActivity.ProviderMainFrame;
import com.medical.mypockethealth.R;
import com.google.gson.Gson;


public class BaseFragment extends Fragment {

     public static  String videoChatStatus=null;
     private Dialog loading_box;

    public BaseFragment() {
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
        return inflater.inflate(R.layout.fragment_base, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        animationApplier(view);


          view.findViewById(R.id.user).setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  BaseActivity.mode=0;

                   sessionHandler(BaseActivity.mode);

              }
          });


        view.findViewById(R.id.provider).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BaseActivity.mode=1;

                sessionHandler(BaseActivity.mode);

            }
        });

    }


    private void sessionHandler(int mode)
    {

        if(mode==0)
        {

            SharedPreferences sharedPreferences= requireContext().getSharedPreferences(ClientMainFrame.client_information_file, Context.MODE_PRIVATE);
            String json = sharedPreferences.getString(ClientMainFrame.client_information_key, "");

            if(json.length()==0)
            {

                requireActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.frame_holder,new LoginOrSignUpFragment()).addToBackStack(null).commit();
            }
            else
            {

                Gson gson = new Gson();
                String data = sharedPreferences.getString(ClientMainFrame.client_information_key, "");
                ClientInformation clientInformation = gson.fromJson(data, ClientInformation.class);
                ClientMainFrame.id=clientInformation.getId();


                if(!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED))
                {

                  if(BaseActivity.videoChatRequestInformation!=null)
                  {

                      openLoadingBox();
                      activateMyState();

                  }

                  else
                  {
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


                }

                else
                {
                    DialogShower dialogShower=new DialogShower(R.layout.internet_error,getContext());
                    dialogShower.showDialog();
                }

            }

        }
        else if(mode==1)
        {

            SharedPreferences sharedPreferences= requireContext().getSharedPreferences(ProviderMainFrame.provider_information_file, Context.MODE_PRIVATE);
            String json = sharedPreferences.getString(ProviderMainFrame.provider_information_key, "");

            if(json.length()==0)
            {

                requireActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.frame_holder,new LoginOrSignUpFragment()).addToBackStack(null).commit();
            }
            else
            {
                Gson gson = new Gson();
                String data = sharedPreferences.getString(ProviderMainFrame.provider_information_key, "");
                ProviderInformation providerInformation = gson.fromJson(data, ProviderInformation.class);
                ProviderMainFrame.id=providerInformation.getId();

                if(!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED))
                {
                    startActivity(new Intent(getContext(), ProviderMainFrame.class));
                }

                else
                {
                    DialogShower dialogShower=new DialogShower(R.layout.internet_error,getContext());
                    dialogShower.showDialog();
                }

            }

        }


    }


    private void openLoadingBox() {
        loading_box = new Dialog(getContext());
        loading_box.setContentView(R.layout.basic_loading_layout);
        loading_box.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading_box.setCanceledOnTouchOutside(false);
        loading_box.setCancelable(false);
        Window window = loading_box.getWindow();
        window.setGravity(Gravity.CENTER);
        loading_box.show();



    }



    private void activateMyState()
    {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(URLBuilder.FirebaseDataNodes.PatientStateInformation);

        databaseReference.child(ClientMainFrame.id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                String random_key_generator=databaseReference.push().getKey();

               PatientStateInformation patientStateInformation=new PatientStateInformation("1",random_key_generator);

                assert random_key_generator != null;

                databaseReference.child(ClientMainFrame.id).child(random_key_generator).setValue(patientStateInformation).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                        loading_box.dismiss();

                        Intent intent=new Intent(getContext(), VideoChatViewActivity.class);
                        intent.putExtra(VideoChatViewActivity.data_key,BaseActivity.videoChatRequestInformation);
                        startActivity(intent);
                    }
                });

            }
        });



    }


    private String getUserId()
    {
        SharedPreferences preferences= requireContext().getSharedPreferences(ClientMainFrame.client_information_file, Context.MODE_PRIVATE);
        String json = preferences.getString(ClientMainFrame.client_information_key, "");
        Gson gson = new Gson();
        ClientInformation clientInformation = gson.fromJson(json, ClientInformation.class);

        return clientInformation.getId();
    }

    private void animationApplier(View view)
    {

        ImageView imageView=view.findViewById(R.id.front);
        Button learner,instructor;
        learner=view.findViewById(R.id.user);
        instructor=view.findViewById(R.id.provider);

        Animation top,bottom;

        top= AnimationUtils.loadAnimation(getContext(),R.anim.top_mover);
        bottom=AnimationUtils.loadAnimation(getContext(),R.anim.bottom_mover);

        imageView.setAnimation(top);
        learner.setAnimation(bottom);
        instructor.setAnimation(bottom);

    }
}