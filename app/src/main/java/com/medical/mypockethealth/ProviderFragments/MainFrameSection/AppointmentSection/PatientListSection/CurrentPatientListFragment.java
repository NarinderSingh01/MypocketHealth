package com.medical.mypockethealth.ProviderFragments.MainFrameSection.AppointmentSection.PatientListSection;

import android.app.AlertDialog;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.medical.mypockethealth.Adaptors.ProviderSection.AppointmentSection.PatientDetailsSection.PatientDetailsRecycleViewAdaptor;
import com.medical.mypockethealth.AgoraSection.VideoChatViewActivity;
import com.medical.mypockethealth.ApplicationBase.BaseActivity;
import com.medical.mypockethealth.Classes.ClientInformation.ClientInformation;
import com.medical.mypockethealth.Classes.ClientInformation.PatientInformation;
import com.medical.mypockethealth.Classes.ClientInformation.PatientStateInformation;
import com.medical.mypockethealth.Classes.DialogShower;
import com.medical.mypockethealth.Classes.NetworkSection.NetworkStatus;
import com.medical.mypockethealth.Classes.ProviderInformation.ProviderInformation;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.Classes.URLBuilder;
import com.medical.mypockethealth.Classes.VideoChatInformation.VideoChatInformation;
import com.medical.mypockethealth.Classes.VideoChatInformation.VideoChatRequestInformation;
import com.medical.mypockethealth.ProviderActivity.ProviderMainFrame;
import com.medical.mypockethealth.R;
import com.medical.mypockethealth.Threads.ProviderSection.BookingSection.ActivateServiceCaller;
import com.medical.mypockethealth.Threads.ProviderSection.BookingSection.ChangeBookingStatusCaller;
import com.medical.mypockethealth.Threads.ProviderSection.BookingSection.TodayPatientDetailsFetcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class CurrentPatientListFragment extends Fragment implements TodayPatientDetailsFetcher.CallbackFromTodayPatientDetailsFetcher,
        PatientDetailsRecycleViewAdaptor.CallBackFromPatientDetailsRecycleViewAdaptor,ActivateServiceCaller.CallbackFromActivateServiceCaller,
        ChangeBookingStatusCaller.CallBackFromChangeBookingStatusCaller
{

    private static final String TAG = "CurrentPatientListFragm";

    private ImageView loading,reload;
    private final Handler handler = new Handler();
    private ClientInformation clientInformation;
    private Dialog loading_box;
    private boolean joinClickStatus=false,videoSessionStatus=false;
    private PatientDetailsRecycleViewAdaptor patientDetailsRecycleViewAdaptor;

    @Override
    public void confirmationCancelBookingCaller(ResponseInformation responseInformation, PatientInformation patientInformation) {

        getPatientList();
    }


    enum SessionType
    {
        Video,Audio,Chat
    }
    public CurrentPatientListFragment() {
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
        return inflater.inflate(R.layout.fragment_current_patient_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        establishViews(view);

        getPatientList();

        ProviderMainFrame.activateStatus=false;    // use to manage state of calling

        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED))
                {


                    loading.setVisibility(View.VISIBLE);
                    reload.setVisibility(View.GONE);

                    getPatientList();

                }

                else
                {
                    reload.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.GONE);

                    DialogShower dialogShower=new DialogShower(R.layout.internet_error,getContext());
                    dialogShower.showDialog();
                }

            }
        });

        RecyclerView recyclerView=view.findViewById(R.id.current_holder);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        patientDetailsRecycleViewAdaptor =new PatientDetailsRecycleViewAdaptor(new ArrayList<>(),getContext(), CurrentPatientListFragment.this);

        recyclerView.setAdapter(patientDetailsRecycleViewAdaptor);

    }

    private void getPatientList()
    {
        if (!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED)) {

            loading.setVisibility(View.VISIBLE);
            reload.setVisibility(View.GONE);
            
            new Thread(new TodayPatientDetailsFetcher(CurrentPatientListFragment.this)).start();

        } else {

            reload.setVisibility(View.VISIBLE);
            loading.setVisibility(View.GONE);

            DialogShower dialogShower=new DialogShower(R.layout.internet_error,getContext());
            dialogShower.showDialog();
        }
    }

    private void establishViews(View view)
    {
        loading=view.findViewById(R.id.loading);
        reload=view.findViewById(R.id.reload);
    }

    @Override
    public void confirmation(ResponseInformation responseInformation) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                loading.setVisibility(View.GONE);
                reload.setVisibility(View.VISIBLE);

                if(responseInformation.getSuccess().equals(ResponseInformation.fail_response_type))
                {

                    patientDetailsRecycleViewAdaptor.loadData(new ArrayList<>());

                    Toast.makeText(getContext(),responseInformation.getMessage(),Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    @Override
    public void information(List<ClientInformation> clientInformation) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                loading.setVisibility(View.GONE);

                patientDetailsRecycleViewAdaptor.loadData(clientInformation);
            }
        });

    }
    @Override
    public void changeBookingStatus(ClientInformation clientInformation) {

        AlertDialog.Builder builder=new AlertDialog.Builder(getContext())
                .setMessage("Have you done with this patient").setNegativeButton("NO", (dialogInterface, i) -> {

                }).setPositiveButton("YES", (dialogInterface, i) -> {

                 changeBookingStatusCaller(clientInformation);

                });
        builder.setCancelable(false);
        builder.show();


    }
    
    private void changeBookingStatusCaller(ClientInformation clientInformation)
    {

        if(!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED))
        {
            Toast.makeText(getContext(),clientInformation.getUserId(), Toast.LENGTH_SHORT).show();
            new Thread(new ChangeBookingStatusCaller(new PatientInformation(null,null,clientInformation.getDate(),clientInformation.getSlotTime(),null,null,
                    clientInformation.getId(),clientInformation.getUserId()),
                    CurrentPatientListFragment.this,"4",getContext())).start();
        }

        else
        {

            DialogShower dialogShower=new DialogShower(R.layout.internet_error,getContext());
            dialogShower.showDialog();
        }
        
    }
    
    

    @Override
    public void videoCall(ClientInformation information) {

        if(!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED))
        {
             this.clientInformation=information;
             videoSessionStatus=true;
             VideoChatViewActivity.clientInformation=clientInformation;

            AlertDialog.Builder builder=new AlertDialog.Builder(getContext())
                    .setMessage("Are you sure you want to make a call with this patient").setNegativeButton("NO", (dialogInterface, i) -> {

                    }).setPositiveButton("YES", (dialogInterface, i) -> {

                        information.setSessionType(SessionType.Video.toString());

                       ProviderMainFrame.state=true;
                       joinClickStatus=true;
                       openLoadingBox();
                       checkPatientState(information);

                    });
            builder.setCancelable(false);
            builder.show();
        }

        else
        {
            DialogShower dialogShower=new DialogShower(R.layout.internet_error,getContext());
            dialogShower.showDialog();
        }
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

    @Override
    public void message(ClientInformation clientInformation) {


        Toast.makeText(getContext(), "coming soon", Toast.LENGTH_SHORT).show();

//        if(!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED))
//        {
//
//            AlertDialog.Builder builder=new AlertDialog.Builder(getContext())
//                    .setMessage("Are you sure you want to chat with this patient").setNegativeButton("NO", (dialogInterface, i) -> {
//
//                    }).setPositiveButton("YES", (dialogInterface, i) -> {
//
//
//                        clientInformation.setSessionType(SessionType.Chat.toString());
//
//
//                    });
//            builder.setCancelable(false);
//            builder.show();
//        }
//
//        else
//        {
//            DialogShower dialogShower=new DialogShower(R.layout.internet_error,getContext());
//            dialogShower.showDialog();
//        }

    }

    private void checkPatientState(ClientInformation information)
    {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance()
                .getReference(URLBuilder.FirebaseDataNodes.PatientStateInformation).child(information.getUserId());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                PatientStateInformation patientStateInformation = null;

                if(snapshot.exists())
                {

                    for (DataSnapshot child : snapshot.getChildren()) {

                         patientStateInformation=child.getValue(PatientStateInformation.class);

                        break;

                    }


                    assert patientStateInformation != null;

                    if(patientStateInformation.getPatientCallingState().equals(ResponseInformation.unAvailable))
                    {

                        loading_box.dismiss();

                        if(!ProviderMainFrame.activateStatus)
                        {
                            if(videoSessionStatus)
                            {
                                Toast.makeText(getContext(),"patient is on another call",Toast.LENGTH_SHORT).show();
                                videoSessionStatus=false;
                            }

                        }


                        ProviderMainFrame.state=false;
                        joinClickStatus=false;

                    }
                    else
                    {

                        if(ProviderMainFrame.state)
                        {
                            joinClickStatus=false;

                            new Thread(new ActivateServiceCaller(clientInformation,CurrentPatientListFragment.this,getContext())).start();
                        }


                    }

                }
                else
                {
                    ProviderMainFrame.state=false;
                    loading_box.dismiss();

                    if(joinClickStatus)
                    {
                        joinClickStatus=false;

                        Toast.makeText(getContext(), "Unable to connect to patient", Toast.LENGTH_SHORT).show();
                    }


                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    @Override
    public void confirmationActivateServiceCaller(ResponseInformation responseInformation) {

        handler.post(new Runnable() {
            @Override
            public void run() {


                loading_box.dismiss();

                if(responseInformation.getSuccess().equals(ResponseInformation.fail_response_type))
                {

                    Toast.makeText(getContext(),responseInformation.getMessage(),Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    @Override
    public void informationActivateServiceCaller(VideoChatInformation videoChatInformation) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                uploadVideoChatRequest(videoChatInformation);


            }
        });

    }

    private ProviderInformation getProviderInformation()
    {
        SharedPreferences preferences= requireContext().getSharedPreferences(ProviderMainFrame.provider_information_file, Context.MODE_PRIVATE);
        String json = preferences.getString(ProviderMainFrame.provider_information_key, "");
        Gson gson = new Gson();

        return gson.fromJson(json, ProviderInformation.class);

    }

    private void uploadVideoChatRequest(VideoChatInformation videoChatInformation)
    {
        ProviderInformation providerInformation=getProviderInformation();
        providerInformation.setUserId(clientInformation.getUserId());
        providerInformation.setEhrStatus(clientInformation.getEhrStatus());
        providerInformation.setPatientName(clientInformation.getPatientName());
        providerInformation.setPatientAge(clientInformation.getPatientAge());
        providerInformation.setPatientPhone(clientInformation.getPhoneNumber());
        providerInformation.setMedicalAid(clientInformation.getMedicalAid());
        providerInformation.setPracticeNumberPhone(videoChatInformation.getPracticeNumberPhone());

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(URLBuilder.FirebaseDataNodes.VideoChatRequest);

        databaseReference.child(clientInformation.getUserId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {


                String random_key_generator=databaseReference.push().getKey();
                assert random_key_generator != null;

                VideoChatRequestInformation videoChatRequestInformation=new VideoChatRequestInformation(videoChatInformation.getToke(),
                        videoChatInformation.getChannelName(),videoChatInformation.getRtmToken(),
                        videoChatInformation.getMainId(),providerInformation,random_key_generator);


                databaseReference.child(clientInformation.getUserId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        databaseReference.child(clientInformation.getUserId()).child(random_key_generator).setValue(videoChatRequestInformation).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                loading_box.dismiss();

                                Intent intent=new Intent(getContext(),VideoChatViewActivity.class);
                                intent.putExtra(VideoChatViewActivity.data_key,videoChatRequestInformation);
                                startActivity(intent);

                            }
                        });

                    }
                });


            }
        });



    }

}