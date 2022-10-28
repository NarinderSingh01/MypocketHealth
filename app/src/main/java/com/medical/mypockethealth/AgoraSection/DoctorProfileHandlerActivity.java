package com.medical.mypockethealth.AgoraSection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.medical.mypockethealth.Classes.ClientInformation.PatientStateInformation;
import com.medical.mypockethealth.Classes.ProviderInformation.ProviderInformation;
import com.medical.mypockethealth.Classes.URLBuilder;
import com.medical.mypockethealth.Classes.VideoChatInformation.VideoChatRequestInformation;
import com.medical.mypockethealth.ClientActivity.ClientMainFrame;
import com.medical.mypockethealth.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class DoctorProfileHandlerActivity extends AppCompatActivity {

    private VideoChatRequestInformation videoChatRequestInformation;
    public static final String data_key="data_key";
    public static PatientStateInformation patientStateInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile_handler);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setData();

        findViewById(R.id.calling).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(DoctorProfileHandlerActivity.this,"coming soon",Toast.LENGTH_SHORT).show();

            }
        });

        findViewById(R.id.chatting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(DoctorProfileHandlerActivity.this,"coming soon",Toast.LENGTH_SHORT).show();

            }

        });

        findViewById(R.id.video_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activateMyState();

            }
        });

    }

    
    private void activateMyState()
    {

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(URLBuilder.FirebaseDataNodes.PatientStateInformation);

        String random_key_generator=databaseReference.push().getKey();

        PatientStateInformation patientStateInformation=new PatientStateInformation("1",random_key_generator);

        assert random_key_generator != null;



    }


    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder=new AlertDialog.Builder(DoctorProfileHandlerActivity.this)
                .setMessage("Are you sure you want leave this session").setNegativeButton("NO", (dialogInterface, i) -> {

                }).setPositiveButton("YES", (dialogInterface, i) -> {


                    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(URLBuilder.FirebaseDataNodes.PatientStateInformation);

                    databaseReference.child(ClientMainFrame.id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                          ClientMainFrame.callEndStatus=false;
                          removeMyRequest();

                        }
                    });
                });
        builder.setCancelable(false);
        builder.show();


    }


    private void removeMyRequest()
    {

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(URLBuilder.FirebaseDataNodes.VideoChatRequest);

        databaseReference.child(ClientMainFrame.id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                ClientMainFrame.activateStatusCheck=false;

             startActivity(new Intent(DoctorProfileHandlerActivity.this,ClientMainFrame.class));

            }
        });
    }

    @Override
    protected void onDestroy() {

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(URLBuilder.FirebaseDataNodes.PatientStateInformation);

        String random_key_generator=databaseReference.push().getKey();

        PatientStateInformation patientStateInformation=new PatientStateInformation("0",random_key_generator);

        assert random_key_generator != null;




        super.onDestroy();
    }

    private void setData()
    {

        ImageView profile_image;
        TextView name,speciality_field,experience,about_doctor,working_address;

        profile_image=findViewById(R.id.profile_image);
        name=findViewById(R.id.name);
        speciality_field=findViewById(R.id.speciality_field);
        experience=findViewById(R.id.experience);
        about_doctor=findViewById(R.id.about_doctor);
        working_address=findViewById(R.id.working_address);

        videoChatRequestInformation=(VideoChatRequestInformation) getIntent().getSerializableExtra(DoctorProfileHandlerActivity.data_key);

        ProviderInformation providerInformation= videoChatRequestInformation.getProviderInformation();

        String user="Dr. " + providerInformation.getFirstName()+" "+providerInformation.getSurName();

        name.setText(user);

        speciality_field.setText(providerInformation.getSpecialization());
        String experience_field=providerInformation.getExperience()+" Year exp";
        experience.setText(experience_field);
        about_doctor.setText(providerInformation.getBio());
        String address=providerInformation.getWorkLocation()+"\n"+providerInformation.getAddress();
        working_address.setText(address);
        Picasso.with(DoctorProfileHandlerActivity.this).load(Uri.parse(providerInformation.getProfileImage())).error(R.drawable.profile).into(profile_image);

    }

}