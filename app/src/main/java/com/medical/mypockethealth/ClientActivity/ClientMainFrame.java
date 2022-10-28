package com.medical.mypockethealth.ClientActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.medical.mypockethealth.AgoraSection.DoctorProfileHandlerActivity;
import com.medical.mypockethealth.AgoraSection.VideoChatViewActivity;
import com.medical.mypockethealth.Classes.ClientInformation.PatientAppInStateInformation;
import com.medical.mypockethealth.Classes.ClientInformation.PatientStateInformation;
import com.medical.mypockethealth.Classes.URLBuilder;
import com.medical.mypockethealth.Classes.VideoChatInformation.VideoChatRequestInformation;
import com.medical.mypockethealth.ClientFragments.BottomNavigationSection.BookingSection.ClientBookingSection;
import com.medical.mypockethealth.ClientFragments.BottomNavigationSection.SettingsSection.SettingFragment;
import com.medical.mypockethealth.ClientFragments.HomeFragment;
import com.medical.mypockethealth.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ClientMainFrame extends AppCompatActivity{

    private static final String TAG = "ClientMainFrame";

    public static final String client_information_file="client_information_file";
    public static final String client_information_key="client_information_key";
    public static final String client_edit_information_file="client_edit_information_file";
    public static final String client_edit_information_key="client_edit_information_key";
    public static String id=null;
    public static boolean patientAppStatus=false;
    public static boolean activateStatusCheck=false;
    public static boolean callEndStatus=false;
    public static PatientStateInformation patientStateInformation;


    enum BottomField
    {
        home,booking,reminder,setting
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_main_frame);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ClientMainFrame.patientAppStatus=true;

        checkVideoChatRequest();

        if(!activateStatusCheck)
        {
            activateStatusCheck=true;

            activateMyState();
        }


        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout, new HomeFragment()).addToBackStack(null).commit();

        bottomNavigationHandler();

    }

    private void activateMyState()
    {

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(URLBuilder.FirebaseDataNodes.PatientStateInformation);

        databaseReference.child(ClientMainFrame.id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                String random_key_generator=databaseReference.push().getKey();

                patientStateInformation=new PatientStateInformation("0",random_key_generator);

                assert random_key_generator != null;

                databaseReference.child(ClientMainFrame.id).child(random_key_generator).setValue(patientStateInformation).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                      // update app status

                    }
                });

            }
        });



    }


    private void checkVideoChatRequest()
    {

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(URLBuilder.FirebaseDataNodes.VideoChatRequest);

        databaseReference.child(ClientMainFrame.id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                DatabaseReference databaseReference= FirebaseDatabase.getInstance()
                        .getReference(URLBuilder.FirebaseDataNodes.VideoChatRequest).child(ClientMainFrame.id);

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        VideoChatRequestInformation videoChatRequestInformation=null;

                        if(snapshot.exists())
                        {
                            for (DataSnapshot child : snapshot.getChildren()) {

                                videoChatRequestInformation= child.getValue(VideoChatRequestInformation.class);

                                break;

                            }

                            ClientMainFrame.callEndStatus=true;

                            activateMyState(videoChatRequestInformation);

                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }


    private void activateMyState(VideoChatRequestInformation videoChatRequestInformation)
    {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(URLBuilder.FirebaseDataNodes.PatientStateInformation);

        String random_key_generator=databaseReference.push().getKey();

        PatientStateInformation patientStateInformation=new PatientStateInformation("1",random_key_generator);

        assert random_key_generator != null;

        databaseReference.child(ClientMainFrame.id).child(ClientMainFrame.patientStateInformation.getObjectKey()).setValue(patientStateInformation).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {


                Intent intent=new Intent(ClientMainFrame.this, VideoChatViewActivity.class);
                intent.putExtra(VideoChatViewActivity.data_key,videoChatRequestInformation);
                startActivity(intent);

            }
        });

    }

    private void bottomNavigationHandler()
    {

        MeowBottomNavigation meowBottomNavigation=(MeowBottomNavigation)findViewById(R.id.meowBottomNavigation);

        meowBottomNavigation.show(1,true);

        meowBottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_home));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_booking));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_reminder));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_settings));


        meowBottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {

                switch (item.getId())
                {

                    case 1:

                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,new HomeFragment()).addToBackStack(null).commit();

                        break;

                    case 2:

                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,new ClientBookingSection()).addToBackStack(null).commit();

                        break;

                    case 3:

                        Toast.makeText(ClientMainFrame.this,"coming soon",Toast.LENGTH_SHORT).show();

                        break;

                    case 4:

                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,new SettingFragment()).addToBackStack(null).commit();

                        break;

                }


            }
        });

        meowBottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                // your codes
            }
        });

        meowBottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                // your codes
            }
        });

    }

    @Override
    public void onBackPressed() {


    }
}
