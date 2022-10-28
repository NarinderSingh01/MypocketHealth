package com.medical.mypockethealth.ProviderFragments.MainFrameSection.NotificationSection;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.medical.mypockethealth.Adaptors.UserSection.NotificationSection.NotificationRecycleViewAdaptor;
import com.medical.mypockethealth.Classes.NotificationSection.ActivationStateInformation;
import com.medical.mypockethealth.Classes.NotificationSection.NotificationInformation;
import com.medical.mypockethealth.Classes.URLBuilder;
import com.medical.mypockethealth.ClientActivity.ClientMainFrame;
import com.medical.mypockethealth.ClientFragments.HomeFragment;
import com.medical.mypockethealth.ClientFragments.NotificationSection.NotificationFragment;
import com.medical.mypockethealth.ProviderActivity.ProviderMainFrame;
import com.medical.mypockethealth.ProviderFragments.ProviderHomeFragment;
import com.medical.mypockethealth.R;

import java.util.ArrayList;
import java.util.List;


public class ProviderNotificationFragment extends Fragment {

    private static final String TAG = "NotificationFragment";

    private ImageView loading;
    private LinearLayout notification;
    private NotificationRecycleViewAdaptor notificationRecycleViewAdaptor;

    public ProviderNotificationFragment() {
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
        return inflater.inflate(R.layout.fragment_provider_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        establishViews(view);

        getNotifications();


        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                        new ProviderHomeFragment()).addToBackStack(null).commit();
            }
        });


        RecyclerView medicine_recycle =view.findViewById(R.id.notification_holder);

        medicine_recycle.setLayoutManager(new LinearLayoutManager(getContext()));

        notificationRecycleViewAdaptor=new NotificationRecycleViewAdaptor(new ArrayList<>());

        medicine_recycle.setAdapter(notificationRecycleViewAdaptor);

    }


    private void establishViews(View view)
    {
        loading=view.findViewById(R.id.loading);
        notification=view.findViewById(R.id.notification);

        loading.setVisibility(View.VISIBLE);
        notification.setVisibility(View.GONE);

    }

    private void getNotifications()
    {

        DatabaseReference databaseReference= FirebaseDatabase.getInstance()
                .getReference(URLBuilder.FirebaseDataNodes.Notification).child(ProviderMainFrame.id).child(URLBuilder.FirebaseDataNodes.myNotification);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                NotificationInformation notificationInformation;

                List<NotificationInformation> notificationInformationList=new ArrayList<>();

                if(snapshot.exists())
                {


                    Fragment currentFragment = requireActivity().getSupportFragmentManager().findFragmentById(R.id.main_frame_layout);

                    if (currentFragment instanceof ProviderNotificationFragment) {

                        loading.setVisibility(View.GONE);
                        notification.setVisibility(View.GONE);

                        for (DataSnapshot child : snapshot.getChildren()) {

                            notificationInformation= child.getValue(NotificationInformation.class);

                            notificationInformationList.add(notificationInformation);

                        }

                        notificationRecycleViewAdaptor.loadData(notificationInformationList);
                    }


                       updateNotificationStatus();

                }
                else
                {
                    Fragment currentFragment = requireActivity().getSupportFragmentManager().findFragmentById(R.id.main_frame_layout);

                    if (currentFragment instanceof ProviderNotificationFragment) {

                        notification.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);
                    }



                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                notification.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);

            }
        });


    }

    private void updateNotificationStatus()
    {

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(URLBuilder.FirebaseDataNodes.Notification);

        ActivationStateInformation activationStateInformation=new ActivationStateInformation("1",ProviderHomeFragment.activationStateInformation.getKey());

        databaseReference.child(ProviderMainFrame.id).child(URLBuilder.FirebaseDataNodes.activationStatus).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                databaseReference.child(ProviderMainFrame.id).child(URLBuilder.FirebaseDataNodes.activationStatus)
                        .child(ProviderHomeFragment.activationStateInformation.getKey()).setValue(activationStateInformation).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

            }
        });


    }
}
