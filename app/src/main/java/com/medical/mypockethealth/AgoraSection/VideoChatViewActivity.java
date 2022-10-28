
package com.medical.mypockethealth.AgoraSection;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.medical.mypockethealth.Adaptors.ProviderSection.EHRSection.AddPrescriptionMedicineRecycleViewAdaptor;
import com.medical.mypockethealth.Adaptors.ProviderSection.EHRSection.DiagnosisRecycleViewAdaptor;
import com.medical.mypockethealth.Adaptors.ProviderSection.EHRSection.ViewEHRRecycleViewAdaptor;
import com.medical.mypockethealth.Adaptors.UserSection.EHRSection.FollowUpFileRecycleViewAdaptor;
import com.medical.mypockethealth.Adaptors.UserSection.EHRSection.PrescriptionRecycleViewAdaptor;
import com.medical.mypockethealth.Adaptors.UserSection.EHRSection.ReferralRecycleViewAdaptor;
import com.medical.mypockethealth.Adaptors.UserSection.EHRSection.SickNoteFileRecycleViewAdaptor;
import com.medical.mypockethealth.Adaptors.UserSection.ProfileSection.AllergyRecycleViewAdaptor;
import com.medical.mypockethealth.Adaptors.UserSection.ProfileSection.MedicationRecycleViewAdaptor;
import com.medical.mypockethealth.Adaptors.UserSection.ProfileSection.MorbidityRecycleViewAdaptor;
import com.medical.mypockethealth.ApplicationBase.BaseActivity;
import com.medical.mypockethealth.Classes.ClientInformation.AllergieList;
import com.medical.mypockethealth.Classes.ClientInformation.ClientInformation;
import com.medical.mypockethealth.Classes.ClientInformation.PatientInformation;
import com.medical.mypockethealth.Classes.ClientInformation.PatientStateInformation;
import com.medical.mypockethealth.Classes.DialogShower;
import com.medical.mypockethealth.Classes.EHRSection.EHRFilesInformation;
import com.medical.mypockethealth.Classes.EHRSection.FollowUpInformation.FollowUpInformation;
import com.medical.mypockethealth.Classes.EHRSection.PrescriptionInformation.PrescriptionInformation;
import com.medical.mypockethealth.Classes.EHRSection.ReferralInformation.ReferralInformation;
import com.medical.mypockethealth.Classes.EHRSection.SickNoteInformation.SickNoteInformation;
import com.medical.mypockethealth.Classes.ICRInformation.ICRInformationRoot;
import com.medical.mypockethealth.Classes.NetworkSection.NetworkStatus;
import com.medical.mypockethealth.Classes.ProviderInformation.ProviderInformation;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.Classes.URLBuilder;
import com.medical.mypockethealth.Classes.VideoChatInformation.VideoChatRequestInformation;
import com.medical.mypockethealth.ClientActivity.ClientMainFrame;
import com.medical.mypockethealth.ProviderActivity.ProviderMainFrame;
import com.medical.mypockethealth.R;
import com.medical.mypockethealth.Threads.BaseThreads.ProviderSection.EHRSection.CreateSheetCaller;
import com.medical.mypockethealth.Threads.BaseThreads.ProviderSection.EHRSection.DiagnosisDetailFetcher;
import com.medical.mypockethealth.Threads.ProviderSection.BookingSection.ChangeBookingStatusCaller;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.medical.mypockethealth.Threads.UserSection.EHRSection.EHRFileCaller;
import com.medical.mypockethealth.Threads.UserSection.EHRSection.FollowUpFileAccessCaller;
import com.medical.mypockethealth.Threads.UserSection.EHRSection.PrescriptionFileAccessCaller;
import com.medical.mypockethealth.Threads.UserSection.EHRSection.ReferralFileAccessCaller;
import com.medical.mypockethealth.Threads.UserSection.EHRSection.SickNoteFileAccessCaller;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;


public class VideoChatViewActivity extends AppCompatActivity implements ChangeBookingStatusCaller.CallBackFromChangeBookingStatusCaller,
        DatePickerDialog.OnDateSetListener,CreateSheetCaller.CallbackFromCreateSheetCaller,EHRFileCaller.CallbackFromEHRFileCaller,
        FollowUpFileRecycleViewAdaptor.callBackFromFollowUpFileRecycleViewAdaptor,FollowUpFileAccessCaller.CallbackFromEhrFileAccessCaller,
        SickNoteFileAccessCaller.CallbackFromSickNoteFileAccessCaller,SickNoteFileRecycleViewAdaptor.callBackFromSickNoteFileRecycleViewAdaptor,
        AddPrescriptionMedicineRecycleViewAdaptor.callBackFromAddPrescriptionMedicineRecycleViewAdaptor,
        ReferralRecycleViewAdaptor.callBackFromReferralRecycleViewAdaptor,ReferralFileAccessCaller.CallbackFromReferralFileAccessCaller,
        PrescriptionFileAccessCaller.CallbackFromPrescriptionFileAccessCaller,PrescriptionRecycleViewAdaptor.callBackFromPrescriptionRecycleViewAdaptor,
        DiagnosisDetailFetcher.CallbackFromDiagnosisDetailFetcher,DiagnosisRecycleViewAdaptor.callBackFromDiagnosisRecycleViewAdaptor{

    private static final String TAG = VideoChatViewActivity.class.getSimpleName();

    private static final int PERMISSION_REQ_ID = 22;
    public static final String data_key="data_key";

    // Permission WRITE_EXTERNAL_STORAGE is not mandatory
    // for Agora RTC SDK, just in case if you wanna save
    // logs to external sdcard.
    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    };

    private RtcEngine mRtcEngine;
    private boolean mCallEnd,mMuted;
    private String examinationDate=null,fromDate=null,upToDate=null,baseUrl=null;
    private FrameLayout mLocalContainer;
    private RelativeLayout mRemoteContainer;
    private VideoCanvas mLocalVideo,mRemoteVideo;
    private FloatingActionButton ehr_section;
    private ImageView mCallBtn,mSwitchCameraBtn,inner_loading,inner_reload,mMuteBtn;
    private Dialog loading_box;
    private BottomSheetDialog followUpSheet,prescriptionSheet,sickNoteSheet,referralSheet,ehrSheetDialog;
    private TextView examination_date,from_date,up_date;
    private boolean followUpStatus=false,prescriptionStatus=false,sickNoteStatus=false,referralNoteStatus=false;
    private final Calendar calendar=Calendar.getInstance();
    private VideoChatRequestInformation videoChatRequestInformation;
    public static ClientInformation clientInformation;
    private final Handler handler=new Handler();
    private DateSelectedType dateSelectedType;
    private Dialog pop_up_box;
    private AddPrescriptionMedicineRecycleViewAdaptor addPrescriptionMedicineRecycleViewAdaptor;
    private final List<String> medicineDiagnosis=new ArrayList<>();
    private final List<String> medicineName=new ArrayList<>();
    private final List<String> medicineQuantity=new ArrayList<>();
    private RecyclerView diagnosis_holder;
    private EditText diagnosis;
    private final boolean selectedState=false;
    private DiagnosisRecycleViewAdaptor diagnosisRecycleViewAdaptor;
    private FollowUpFileRecycleViewAdaptor followUpFileRecycleViewAdaptor;
    private SickNoteFileRecycleViewAdaptor sickNoteFileRecycleViewAdaptor;
    private ReferralRecycleViewAdaptor referralRecycleViewAdaptor;
    private PrescriptionRecycleViewAdaptor prescriptionRecycleViewAdaptor;




    enum SheetResponseType
    {
        followUp,prescription,sickNote,referralNote
    }

    @Override
    public void confirmationCreateSheetCaller(ResponseInformation responseInformation, int mode) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                setDefault();

                if(responseInformation.getSuccess().equals(ResponseInformation.fail_response_type))
                {

                    Toast.makeText(VideoChatViewActivity.this,responseInformation.getMessage(),Toast.LENGTH_SHORT).show();

                }
                else
                {

                    if(followUpStatus)
                    {
                        followUpStatus=false;
                        boxIndicator(SheetResponseType.followUp);
                    }
                    else if(prescriptionStatus)
                    {
                        prescriptionStatus=false;
                        boxIndicator(SheetResponseType.prescription);
                    }
                    else if(sickNoteStatus)
                    {
                        sickNoteStatus=false;
                        boxIndicator(SheetResponseType.sickNote);
                    }
                    else if(referralNoteStatus)
                    {
                        referralNoteStatus=false;
                        boxIndicator(SheetResponseType.referralNote);

                    }

                }
            }
        });

    }

    private void setDefault()
    {
        if(loading_box!=null)
        {
            loading_box.dismiss();

        }
        if(followUpSheet!=null)
        {
            followUpSheet.dismiss();

        }
        if(prescriptionSheet!=null)
        {
            prescriptionSheet.dismiss();

        }
        if(sickNoteSheet!=null)
        {
            sickNoteSheet.dismiss();

        }
        if(referralSheet!=null)
        {
            referralSheet.dismiss();

        }
    }
    private void boxIndicator(SheetResponseType sheetResponseType)
    {

        DialogShower dialogShower;

        switch (sheetResponseType)
        {
            case followUp:

                dialogShower=new DialogShower(R.layout.follow_indicator_layout,VideoChatViewActivity.this);
                dialogShower.showDialog();

                break;

            case prescription:

                dialogShower=new DialogShower(R.layout.prescription_indicator_layout,VideoChatViewActivity.this);
                dialogShower.showDialog();

                break;

            case sickNote:

                dialogShower=new DialogShower(R.layout.sick_indicator_layout,VideoChatViewActivity.this);
                dialogShower.showDialog();

                break;

            case referralNote:

                dialogShower=new DialogShower(R.layout.referral_indicator_layout,VideoChatViewActivity.this);
                dialogShower.showDialog();

                break;
        }
    }

    enum DateSelectedType
    {
        examinationDate,fromDate,upDate
    }

    enum bottomSheetParameter
    {
        phone,email,date,address,followUpInformation,fillInformation,examinationDate,fromDate,upToDate,natureIllness,
        name,informationName,IDNumber,onDuty,speciality,patientName,patientAge,medicalAid,patientContact,diagnosis,description,
        quantity
    }

    // Customized logger view

    /**
     * Event handler registered into RTC engine for RTC callbacks.
     * Note that UI operations needs to be in UI thread because RTC
     * engine deals with the events in a separate thread.
     */
    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        /**
         * Occurs when the local user joins a specified channel.
         * The channel name assignment is based on channelName specified in the joinChannel method.
         * If the uid is not specified when joinChannel is called, the server automatically assigns a uid.
         *
         * @param channel Channel name.
         * @param uid User ID.
         * @param elapsed Time elapsed (ms) from the user calling joinChannel until this callback is triggered.
         */
        @Override
        public void onJoinChannelSuccess(String channel, final int uid, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Log.d(TAG, "run: Join channel success, uid:  " + (uid & 0xFFFFFFFFL));
                }
            });
        }

        @Override
        public void onUserJoined(final int uid, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Log.d(TAG, "run: First remote video decoded, uid: " +  (uid & 0xFFFFFFFFL));

                    setupRemoteVideo(uid);
                }
            });
        }

        /**
         * Occurs when a remote user (Communication)/host (Live Broadcast) leaves the channel.
         *
         * There are two reasons for users to become offline:
         *
         *     Leave the channel: When the user/host leaves the channel, the user/host sends a
         *     goodbye message. When this message is received, the SDK determines that the
         *     user/host leaves the channel.
         *
         *     Drop offline: When no data packet of the user or host is received for a certain
         *     period of time (20 seconds for the communication profile, and more for the live
         *     broadcast profile), the SDK assumes that the user/host drops offline. A poor
         *     network connection may lead to false detections, so we recommend using the
         *     Agora RTM SDK for reliable offline detection.
         *
         * @param uid ID of the user or host who leaves the channel or goes offline.
         * @param reason Reason why the user goes offline:
         *
         *     USER_OFFLINE_QUIT(0): The user left the current channel.
         *     USER_OFFLINE_DROPPED(1): The SDK timed out and the user dropped offline because no data packet was received within a certain period of time. If a user quits the call and the message is not passed to the SDK (due to an unreliable channel), the SDK assumes the user dropped offline.
         *     USER_OFFLINE_BECOME_AUDIENCE(2): (Live broadcast only.) The client role switched from the host to the audience.
         */
        @Override
        public void onUserOffline(final int uid, int reason) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Log.d(TAG, "run: User offline, uid:" + (uid & 0xFFFFFFFFL));


                    if(BaseActivity.mode==0)
                    {
                        releasePatientKey();
                    }
                    else
                    {
                        releaseDoctorKey();
                    }

                    onRemoteUserLeft(uid);
                }
            });
        }
    };

    private void setupRemoteVideo(int uid) {
        ViewGroup parent = mRemoteContainer;
        if (parent.indexOfChild(mLocalVideo.view) > -1) {
            parent = mLocalContainer;
        }

        // Only one remote video view is available for this
        // tutorial. Here we check if there exists a surface
        // view tagged as this uid.
        if (mRemoteVideo != null) {
            return;
        }

        /*
          Creates the video renderer view.
          CreateRendererView returns the SurfaceView type. The operation and layout of the view
          are managed by the app, and the Agora SDK renders the view provided by the app.
          The video display view must be created using this method instead of directly
          calling SurfaceView.
         */
        SurfaceView view = RtcEngine.CreateRendererView(getBaseContext());
        view.setZOrderMediaOverlay(parent == mLocalContainer);
        parent.addView(view);
        mRemoteVideo = new VideoCanvas(view, VideoCanvas.RENDER_MODE_HIDDEN, uid);
        // Initializes the video view of a remote user.
        mRtcEngine.setupRemoteVideo(mRemoteVideo);
    }

    private void onRemoteUserLeft(int uid) {
        if (mRemoteVideo != null && mRemoteVideo.uid == uid) {
            removeFromParent(mRemoteVideo);
            // Destroys remote view
            mRemoteVideo = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_chat_view);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        videoChatRequestInformation=(VideoChatRequestInformation)getIntent().getSerializableExtra(VideoChatViewActivity.data_key);


        establishViews();
        setUpPermissions();

        ProviderMainFrame.activateStatus=true;       // use to manage state of calling

        if(BaseActivity.mode==1)
        {
            ehr_section.setVisibility(View.VISIBLE);
        }
        else
        {
            ehr_section.setVisibility(View.GONE);
        }


        findViewById(R.id.btn_switch_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mRtcEngine.switchCamera();

            }
        });

        findViewById(R.id.leave_channel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED))
                {

                    if(BaseActivity.mode==1)
                    {
                        removeRequest(1);
                    }
                    else
                    {
                        removeRequest(0);
                    }

                }

                else
                {

                    DialogShower dialogShower=new DialogShower(R.layout.internet_error,VideoChatViewActivity.this);
                    dialogShower.showDialog();
                }



            }
        });

        findViewById(R.id.mute).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onLocalAudioMuteClicked(v);
            }
        });

        ehr_section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                handler.post(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {

                        side_handler(videoChatRequestInformation);

                    }
                });
            }
        });

    }


    private void side_handler(VideoChatRequestInformation videoChatRequestInformation) {

        Dialog slider_options_dialog = new Dialog(VideoChatViewActivity.this, R.style.translate_animator);
        slider_options_dialog.setContentView(R.layout.slide_options_layout);
        slider_options_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        slider_options_dialog.setCanceledOnTouchOutside(true);
        Window window = slider_options_dialog.getWindow();
        window.setGravity(Gravity.END);
        slider_options_dialog.show();

        LinearLayout view_ehr_layout,create_ehr_layout,patient_history_layout;
        create_ehr_layout=slider_options_dialog.findViewById(R.id.create_ehr_layout);
        view_ehr_layout=slider_options_dialog.findViewById(R.id.view_ehr_layout);
        patient_history_layout=slider_options_dialog.findViewById(R.id.patient_history_layout);

        
      
        if(!clientInformation.getJourneyInformationStatus().equals("0"))
        {
            patient_history_layout.setVisibility(View.VISIBLE);
        }
        else 
        {
            patient_history_layout.setVisibility(View.GONE);
        }
        
        if(videoChatRequestInformation.getProviderInformation().getEhrStatus().equals("1"))
        {
            view_ehr_layout.setVisibility(View.VISIBLE);
        }
        else
        {
            view_ehr_layout.setVisibility(View.GONE);
        }


        patient_history_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                openHistorySheet();

            }
        });
        
        
        view_ehr_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openEHRFilesSheet();

            }
        });

        create_ehr_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openCreateEHRSheet();

            }
        });


    }


    private void openCreateEHRSheet()
    {

        BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(VideoChatViewActivity.this,R.style.BottomSheetDialogTheme);

        View bottom_view= LayoutInflater.from(VideoChatViewActivity.this).inflate(R.layout.create_ehr_layout
                ,
                (RelativeLayout) bottomSheetDialog.findViewById(R.id.relative_layout));

        bottomSheetDialog.setContentView(bottom_view);

        bottomSheetDialog.show();

        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.setCancelable(false);


        bottomSheetDialog.findViewById(R.id.follow_up_box).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openFollowUpSheet();

            }
        });
        bottomSheetDialog.findViewById(R.id.prescription_box).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openPrescriptionSheet();

            }
        });
        bottomSheetDialog.findViewById(R.id.sick_box).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openSickNoteSheet();

            }
        });
        bottomSheetDialog.findViewById(R.id.referral_box).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openReferralSheet();

            }
        });

        bottomSheetDialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetDialog.dismiss();

            }
        });

    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

        String month1=String.valueOf(month+1);
        String day1=String.valueOf(dayOfMonth);

        if(month1.length()==1)
        {
            month1="0"+month1;

        }
        if(day1.length()==1)
        {
            day1="0"+day1;
        }


        String selectedDate= year+"-"+month1+"-"+day1;


        switch (dateSelectedType)
        {

            case examinationDate:

                examination_date.setText(selectedDate);

                examinationDate=selectedDate;

                break;

            case fromDate:

                from_date.setText(selectedDate);

                fromDate=selectedDate;

                break;

            case upDate:

                up_date.setText(selectedDate);

                upToDate=selectedDate;

                break;

        }
    }



    private void openFollowUpSheet()
    {

        followUpSheet=new BottomSheetDialog(VideoChatViewActivity.this,R.style.BottomSheetDialogTheme);

        View bottom_view= LayoutInflater.from(VideoChatViewActivity.this).inflate(R.layout.follow_up_layout
                ,
                (RelativeLayout) followUpSheet.findViewById(R.id.relative_layout));

        followUpSheet.setContentView(bottom_view);

        followUpSheet.show();

        followUpSheet.setCanceledOnTouchOutside(false);
        followUpSheet.setCancelable(false);

        TextView doctor_name,phone,email,date,address,practiceNumber;

        doctor_name=followUpSheet.findViewById(R.id.doctor_name);
        phone=followUpSheet.findViewById(R.id.phone);
        email=followUpSheet.findViewById(R.id.email);
        date=followUpSheet.findViewById(R.id.date);
        address=followUpSheet.findViewById(R.id.address);
        practiceNumber=followUpSheet.findViewById(R.id.practiceNumber);

        EditText editText=followUpSheet.findViewById(R.id.information);


        String name="Dr. " + videoChatRequestInformation.getProviderInformation().
                getFirstName()+" "+videoChatRequestInformation.getProviderInformation().getSurName();

        assert doctor_name != null;
        doctor_name.setText(name);


        assert phone != null;

        phone.setText(videoChatRequestInformation.getProviderInformation().getPracticeNumberPhone());

        assert email != null;
        email.setText(videoChatRequestInformation.getProviderInformation().getEmail());

        assert address != null;
        address.setText(videoChatRequestInformation.getProviderInformation().getAddress());

        assert date != null;
        date.setText(setDefaultDateTag());


        followUpSheet.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                followUpSheet.dismiss();

            }
        });

        followUpSheet.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                assert editText != null;
                if(editText.getText().toString().trim().length()!=0)
                {
                    Map<String,String> followUpSheetInformation=new HashMap<>();

                    followUpSheetInformation.put(URLBuilder.Parameter.userId.toString(),
                            videoChatRequestInformation.getProviderInformation().getUserId());

                    followUpSheetInformation.put(URLBuilder.Parameter.providerId.toString(),
                            videoChatRequestInformation.getProviderInformation().getId());

                    followUpSheetInformation.put(URLBuilder.Parameter.providerName.toString(),
                            doctor_name.getText().toString());


                    followUpSheetInformation.put(bottomSheetParameter.phone.toString()
                            ,"+"+videoChatRequestInformation.getProviderInformation().getCountryCode()+
                                    videoChatRequestInformation.getProviderInformation().getPracticeNumberPhone());



                    String specialityAndRegistration=videoChatRequestInformation.getProviderInformation()
                            .getSpecialization()+" HPCSA No : "+videoChatRequestInformation.getProviderInformation().getProfessionalRegistrationNumber();

                    followUpSheetInformation.put(bottomSheetParameter.speciality.toString(),
                            specialityAndRegistration);

                    followUpSheetInformation.put(bottomSheetParameter.email.toString(),
                            videoChatRequestInformation.getProviderInformation().getEmail());

                    followUpSheetInformation.put(bottomSheetParameter.address.toString(),
                            videoChatRequestInformation.getProviderInformation().getAddress());

                    followUpSheetInformation.put(bottomSheetParameter.followUpInformation.toString(),
                            editText.getText().toString());

                    followUpSheetInformation.put(bottomSheetParameter.date.toString(),
                            date.getText().toString());


                    if(!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED))
                    {

                        openLoadingBox();

                        followUpStatus=true;

                        new Thread(new CreateSheetCaller(0,VideoChatViewActivity.this,followUpSheetInformation)).start();

                    }

                    else
                    {

                        DialogShower dialogShower=new DialogShower(R.layout.internet_error,R.style.translate_animator,VideoChatViewActivity.this);
                        dialogShower.showDialog();
                    }



                }
                else
                {
                    editText.setError("Fill follow up information");
                }

            }
        });

    }

    private String setDefaultDateTag() {

        Calendar calendar=Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return setFilteredDate(String.valueOf(year), String.valueOf(month), String.valueOf(day));

    }

    private String setFilteredDate(String year, String month, String day) {

        String result = "";

        try {
            result = getResources().getStringArray(R.array.month_names)[Integer.parseInt(month)];
        } catch (ArrayIndexOutOfBoundsException e) {
            result = Integer.toString(Integer.parseInt(month));
        }


        return day + ", " + result + " " + year;




    }

    private void openReferralSheet()
    {
        referralSheet=new BottomSheetDialog(VideoChatViewActivity.this,R.style.BottomSheetDialogTheme);

        View bottom_view= LayoutInflater.from(VideoChatViewActivity.this).inflate(R.layout.referral_layout
                ,
                (RelativeLayout) referralSheet.findViewById(R.id.relative_layout));

        referralSheet.setContentView(bottom_view);

        referralSheet.show();

        referralSheet.setCanceledOnTouchOutside(false);
        referralSheet.setCancelable(false);

        TextView doctor_name,phone,email,date,address;
        EditText informationName,IDNumber,onDuty,followUpInformation;

        date=referralSheet.findViewById(R.id.date);
        address=referralSheet.findViewById(R.id.address);
        phone=referralSheet.findViewById(R.id.phone);
        email=referralSheet.findViewById(R.id.email);
        doctor_name=referralSheet.findViewById(R.id.doctor_name);
        informationName=referralSheet.findViewById(R.id.informationName);
        IDNumber=referralSheet.findViewById(R.id.IDNumber);
        onDuty=referralSheet.findViewById(R.id.onDuty);
        followUpInformation=referralSheet.findViewById(R.id.followUpInformation);


        assert informationName != null;
        informationName.setText(videoChatRequestInformation.getProviderInformation().getPatientName());

        String name="Dr. " + videoChatRequestInformation.getProviderInformation()
                .getFirstName()+" "+videoChatRequestInformation.getProviderInformation().getSurName();
        assert doctor_name != null;
        doctor_name.setText(name);

        assert phone != null;
        phone.setText(videoChatRequestInformation.getProviderInformation().getPracticeNumberPhone());

        assert email != null;
        email.setText(videoChatRequestInformation.getProviderInformation().getEmail());

        assert address != null;
        address.setText(videoChatRequestInformation.getProviderInformation().getAddress());

        assert date != null;
        date.setText(setDefaultDateTag());


        referralSheet.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(informationName.getText().toString().trim().length()!=0)
                {

                    assert IDNumber != null;
                    if(IDNumber.getText().toString().trim().length()!=0)
                    {
                        assert onDuty != null;
                        if(onDuty.getText().toString().trim().length()!=0)
                        {

                            assert followUpInformation != null;
                            if(followUpInformation.getText().toString().trim().length()!=0)
                            {

                                Map<String,String> referralSheetInformation=new HashMap<>();

                                referralSheetInformation.put(URLBuilder.Parameter.userId.toString(),
                                        videoChatRequestInformation.getProviderInformation().getUserId());

                                referralSheetInformation.put(URLBuilder.Parameter.providerId.toString(),
                                        videoChatRequestInformation.getProviderInformation().getId());

                                referralSheetInformation.put(bottomSheetParameter.name.toString(),
                                        doctor_name.getText().toString());

                                referralSheetInformation.put(bottomSheetParameter.phone.toString()
                                        ,"+"+videoChatRequestInformation.getProviderInformation().getCountryCode()+
                                                videoChatRequestInformation.getProviderInformation().getPracticeNumberPhone());


                                String specialityAndRegistration=videoChatRequestInformation.getProviderInformation()
                                        .getSpecialization()+" HPCSA No : "+videoChatRequestInformation.getProviderInformation().getProfessionalRegistrationNumber();

                                referralSheetInformation.put(bottomSheetParameter.speciality.toString(),
                                        specialityAndRegistration);

                                referralSheetInformation.put(bottomSheetParameter.email.toString(),
                                        videoChatRequestInformation.getProviderInformation().getEmail());

                                referralSheetInformation.put(bottomSheetParameter.address.toString(),
                                        videoChatRequestInformation.getProviderInformation().getAddress());

                                referralSheetInformation.put(bottomSheetParameter.date.toString(),
                                        date.getText().toString());

                                referralSheetInformation.put(bottomSheetParameter.informationName.toString(),
                                        informationName.getText().toString());

                                referralSheetInformation.put(bottomSheetParameter.IDNumber.toString(),
                                        IDNumber.getText().toString());

                                referralSheetInformation.put(bottomSheetParameter.onDuty.toString(),
                                        onDuty.getText().toString());

                                referralSheetInformation.put(bottomSheetParameter.followUpInformation.toString(),
                                        followUpInformation.getText().toString());


                                if(!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED))
                                {

                                    openLoadingBox();

                                    referralNoteStatus=true;

                                    new Thread(new CreateSheetCaller(3,VideoChatViewActivity.this,referralSheetInformation)).start();

                                }

                                else
                                {

                                    DialogShower dialogShower=new DialogShower(R.layout.internet_error,R.style.translate_animator,VideoChatViewActivity.this);
                                    dialogShower.showDialog();
                                }

                            }
                            else
                            {
                                Toast.makeText(VideoChatViewActivity.this, "Kindly fill follow up information", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(VideoChatViewActivity.this, "Kindly fill on duty information", Toast.LENGTH_SHORT).show();
                        }
                    }

                    else
                    {
                        Toast.makeText(VideoChatViewActivity.this, "Kindly enter ID Number", Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {

                    Toast.makeText(VideoChatViewActivity.this, "Kindly enter name", Toast.LENGTH_SHORT).show();
                }

            }
        });


        referralSheet.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                referralSheet.dismiss();

            }
        });
    }

    private void openSickNoteSheet()
    {
        sickNoteSheet=new BottomSheetDialog(VideoChatViewActivity.this,R.style.BottomSheetDialogTheme);

        View bottom_view= LayoutInflater.from(VideoChatViewActivity.this).inflate(R.layout.sick_note_layout
                ,
                (RelativeLayout) sickNoteSheet.findViewById(R.id.relative_layout));

        sickNoteSheet.setContentView(bottom_view);

        sickNoteSheet.show();

        sickNoteSheet.setCanceledOnTouchOutside(false);
        sickNoteSheet.setCancelable(false);

        TextView doctor_name,phone,email,date,address;
        ImageView pick_date,from_pick_date,up_to_pick_date;
        EditText fillInformation,natureInformation;

        date=sickNoteSheet.findViewById(R.id.date);
        address=sickNoteSheet.findViewById(R.id.address);
        phone=sickNoteSheet.findViewById(R.id.phone);
        email=sickNoteSheet.findViewById(R.id.email);
        doctor_name=sickNoteSheet.findViewById(R.id.doctor_name);
        pick_date=sickNoteSheet.findViewById(R.id.pick_date);
        from_pick_date=sickNoteSheet.findViewById(R.id.from_pick_date);
        up_to_pick_date=sickNoteSheet.findViewById(R.id.up_to_pick_date);
        fillInformation=sickNoteSheet.findViewById(R.id.fillInformation);
        natureInformation=sickNoteSheet.findViewById(R.id.natureInformation);
        examination_date=sickNoteSheet.findViewById(R.id.examination_date);
        from_date=sickNoteSheet.findViewById(R.id.from_date);
        up_date=sickNoteSheet.findViewById(R.id.up_date);


        assert fillInformation != null;
        fillInformation.setText(videoChatRequestInformation.getProviderInformation().getPatientName());

        String name="Dr. " + videoChatRequestInformation.getProviderInformation()
                .getFirstName()+" "+videoChatRequestInformation.getProviderInformation().getSurName();
        assert doctor_name != null;
        doctor_name.setText(name);

        assert phone != null;
        phone.setText(videoChatRequestInformation.getProviderInformation().getPracticeNumberPhone());

        assert email != null;
        email.setText(videoChatRequestInformation.getProviderInformation().getEmail());

        assert address != null;
        address.setText(videoChatRequestInformation.getProviderInformation().getAddress());

        assert date != null;
        date.setText(setDefaultDateTag());

        assert pick_date != null;
        pick_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dateSelectedType=DateSelectedType.examinationDate;

                DialogFragment dialogFragment=new com.medical.mypockethealth.Classes.DateAndTimeForActivitySection.DatePickerActivity();
                dialogFragment.show(getSupportFragmentManager(),"date_picker");

            }
        });

        assert from_pick_date != null;
        from_pick_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dateSelectedType=DateSelectedType.fromDate;

                DialogFragment dialogFragment=new com.medical.mypockethealth.Classes.DateAndTimeForActivitySection.DatePickerActivity();
                dialogFragment.show(getSupportFragmentManager(),"date_picker");
            }
        });

        assert up_to_pick_date != null;
        up_to_pick_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dateSelectedType=DateSelectedType.upDate;

                DialogFragment dialogFragment=new com.medical.mypockethealth.Classes.DateAndTimeForActivitySection.DatePickerActivity();
                dialogFragment.show(getSupportFragmentManager(),"date_picker");

            }
        });


        sickNoteSheet.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(fillInformation.getText().toString().trim().length()!=0)
                {
                    if(examinationDate!=null)
                    {
                        if(fromDate!=null)
                        {
                            if(upToDate!=null)
                            {

                                assert natureInformation != null;
                                if(natureInformation.getText().toString().length()!=0)
                                {

                                    Map<String,String> sickNoteSheetInformation=new HashMap<>();

                                    sickNoteSheetInformation.put(URLBuilder.Parameter.userId.toString(),
                                            videoChatRequestInformation.getProviderInformation().getUserId());

                                    sickNoteSheetInformation.put(URLBuilder.Parameter.providerId.toString(),
                                            videoChatRequestInformation.getProviderInformation().getId());

                                    sickNoteSheetInformation.put(bottomSheetParameter.name.toString(),
                                            doctor_name.getText().toString());

                                    String specialityAndRegistration=videoChatRequestInformation.getProviderInformation()
                                            .getSpecialization()+" HPCSA No : "+videoChatRequestInformation.getProviderInformation().getProfessionalRegistrationNumber();

                                    sickNoteSheetInformation.put(bottomSheetParameter.speciality.toString(),
                                            specialityAndRegistration);


                                    sickNoteSheetInformation.put(bottomSheetParameter.phone.toString()
                                            ,"+"+videoChatRequestInformation.getProviderInformation().getCountryCode()+
                                                    videoChatRequestInformation.getProviderInformation().getPracticeNumberPhone());


                                    sickNoteSheetInformation.put(bottomSheetParameter.email.toString(),
                                            videoChatRequestInformation.getProviderInformation().getEmail());

                                    sickNoteSheetInformation.put(bottomSheetParameter.address.toString(),
                                            videoChatRequestInformation.getProviderInformation().getAddress());

                                    sickNoteSheetInformation.put(bottomSheetParameter.date.toString(),
                                            date.getText().toString());

                                    sickNoteSheetInformation.put(bottomSheetParameter.fillInformation.toString(),
                                            fillInformation.getText().toString());

                                    sickNoteSheetInformation.put(bottomSheetParameter.examinationDate.toString(),
                                            examinationDate);

                                    sickNoteSheetInformation.put(bottomSheetParameter.fromDate.toString(),
                                            fromDate);

                                    sickNoteSheetInformation.put(bottomSheetParameter.upToDate.toString(),
                                            upToDate);

                                    sickNoteSheetInformation.put(bottomSheetParameter.natureIllness.toString(),
                                            natureInformation.getText().toString());


                                    if(!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED))
                                    {

                                        openLoadingBox();

                                        sickNoteStatus=true;

                                        new Thread(new CreateSheetCaller(2,VideoChatViewActivity.this,sickNoteSheetInformation)).start();

                                    }

                                    else
                                    {

                                        DialogShower dialogShower=new DialogShower(R.layout.internet_error,R.style.translate_animator,VideoChatViewActivity.this);
                                        dialogShower.showDialog();
                                    }

                                }

                                else
                                {
                                    Toast.makeText(VideoChatViewActivity.this, "Please fill nature of illness / operation", Toast.LENGTH_SHORT).show();
                                }


                            }
                            else
                            {
                                Toast.makeText(VideoChatViewActivity.this, "Please select up to date", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(VideoChatViewActivity.this, "Please select work from date", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(VideoChatViewActivity.this, "Please select examination date", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(VideoChatViewActivity.this, "Fill detail", Toast.LENGTH_SHORT).show();
                }


            }
        });


        sickNoteSheet.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sickNoteSheet.dismiss();

            }
        });

    }
    private void openPrescriptionSheet()
    {
        prescriptionSheet=new BottomSheetDialog(VideoChatViewActivity.this,R.style.BottomSheetDialogTheme);

        View bottom_view= LayoutInflater.from(VideoChatViewActivity.this).inflate(R.layout.prescription_sheet
                ,
                (RelativeLayout) prescriptionSheet.findViewById(R.id.relative_layout));

        prescriptionSheet.setContentView(bottom_view);

        prescriptionSheet.show();

        prescriptionSheet.setCanceledOnTouchOutside(false);
        prescriptionSheet.setCancelable(false);

        TextView date,inner_doctor_name,speciality,address,phone,email,patient_name,patient_age,medical_aid,patient_phone;
        RecyclerView medicine;
        CardView add;

        date=prescriptionSheet.findViewById(R.id.date);
        inner_doctor_name=prescriptionSheet.findViewById(R.id.inner_doctor_name);
        speciality=prescriptionSheet.findViewById(R.id.speciality);
        address=prescriptionSheet.findViewById(R.id.address);
        phone=prescriptionSheet.findViewById(R.id.phone);
        email=prescriptionSheet.findViewById(R.id.email);
        patient_name=prescriptionSheet.findViewById(R.id.patient_name);
        patient_age=prescriptionSheet.findViewById(R.id.patient_age);
        medical_aid=prescriptionSheet.findViewById(R.id.medical_aid);
        patient_phone=prescriptionSheet.findViewById(R.id.patient_phone);
        medicine=prescriptionSheet.findViewById(R.id.medicine_holder);
        add=prescriptionSheet.findViewById(R.id.add);


        assert add != null;
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addMedicineBox();

            }
        });


        assert medicine != null;

        medicine.setLayoutManager(new LinearLayoutManager(VideoChatViewActivity.this));


        addPrescriptionMedicineRecycleViewAdaptor=new AddPrescriptionMedicineRecycleViewAdaptor(new ArrayList<>(),new ArrayList<>(),new ArrayList<>(),
                VideoChatViewActivity.this);

        medicine.setAdapter(addPrescriptionMedicineRecycleViewAdaptor);

        String name="Dr. " + videoChatRequestInformation.getProviderInformation().
                getFirstName()+" "+videoChatRequestInformation.getProviderInformation().getSurName();
        assert date != null;

        date.setText(setDefaultDateTag());

        assert inner_doctor_name != null;
        inner_doctor_name.setText(name);

        String specialityAndRegistration=videoChatRequestInformation.getProviderInformation()
                .getSpecialization()+" HPCSA No : "+videoChatRequestInformation.getProviderInformation().getProfessionalRegistrationNumber();

        assert speciality != null;
        speciality.setText(specialityAndRegistration);

        assert patient_name != null;
        patient_name.setText(videoChatRequestInformation.getProviderInformation().getPatientName());

        assert patient_age != null;
        patient_age.setText(videoChatRequestInformation.getProviderInformation().getPatientAge());

        assert patient_phone != null;
        patient_phone.setText(videoChatRequestInformation.getProviderInformation().getPatientPhone());

        assert medical_aid != null;

        if(videoChatRequestInformation.getProviderInformation().getMedicalAid().equals("notSelected"))
        {
            medical_aid.setText(R.string.none);
        }
        else
        {
            medical_aid.setText(videoChatRequestInformation.getProviderInformation().getMedicalAid());
        }


        assert phone != null;
        phone.setText(videoChatRequestInformation.getProviderInformation().getPracticeNumberPhone());

        assert email != null;
        email.setText(videoChatRequestInformation.getProviderInformation().getEmail());

        assert address != null;
        address.setText(videoChatRequestInformation.getProviderInformation().getAddress());



        prescriptionSheet.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(medicineName.size()!=0)
                {
                    Map<String,String> prescriptionSheetInformation=new HashMap<>();

                    prescriptionSheetInformation.put(URLBuilder.Parameter.userId.toString(),
                            videoChatRequestInformation.getProviderInformation().getUserId());

                    prescriptionSheetInformation.put(URLBuilder.Parameter.providerId.toString(),
                            videoChatRequestInformation.getProviderInformation().getId());

                    prescriptionSheetInformation.put(bottomSheetParameter.name.toString(),
                            name);

                    prescriptionSheetInformation.put(bottomSheetParameter.phone.toString()
                            ,"+"+videoChatRequestInformation.getProviderInformation().getCountryCode()+
                                    videoChatRequestInformation.getProviderInformation().getPracticeNumberPhone());


                    String specialityAndRegistration=videoChatRequestInformation.getProviderInformation()
                            .getSpecialization()+" HPCSA No : "+videoChatRequestInformation.getProviderInformation().getProfessionalRegistrationNumber();

                    prescriptionSheetInformation.put(bottomSheetParameter.speciality.toString(),
                            specialityAndRegistration);

                    prescriptionSheetInformation.put(bottomSheetParameter.email.toString(),
                            videoChatRequestInformation.getProviderInformation().getEmail());

                    prescriptionSheetInformation.put(bottomSheetParameter.address.toString(),
                            videoChatRequestInformation.getProviderInformation().getAddress());

                    prescriptionSheetInformation.put(bottomSheetParameter.date.toString(),
                            date.getText().toString());

                    prescriptionSheetInformation.put(bottomSheetParameter.patientName.toString(),
                            patient_name.getText().toString());

                    prescriptionSheetInformation.put(bottomSheetParameter.patientAge.toString(),
                            patient_age.getText().toString());

                    prescriptionSheetInformation.put(bottomSheetParameter.medicalAid.toString(),
                            medical_aid.getText().toString());

                    prescriptionSheetInformation.put(bottomSheetParameter.patientContact.toString(),
                            patient_phone.getText().toString());

                    prescriptionSheetInformation.put(bottomSheetParameter.diagnosis.toString(),
                            new Gson().toJson(medicineDiagnosis));

                    prescriptionSheetInformation.put(bottomSheetParameter.description.toString(),
                            new Gson().toJson(medicineName));

                    prescriptionSheetInformation.put(bottomSheetParameter.quantity.toString(),
                            new Gson().toJson(medicineQuantity));


                    if(!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED))
                    {

                        openLoadingBox();

                        prescriptionStatus=true;

                        new Thread(new CreateSheetCaller(1,VideoChatViewActivity.this,prescriptionSheetInformation)).start();

                    }

                    else
                    {

                        DialogShower dialogShower=new DialogShower(R.layout.internet_error,R.style.translate_animator,VideoChatViewActivity.this);
                        dialogShower.showDialog();
                    }
                }
                else
                {
                    Toast.makeText(VideoChatViewActivity.this, "Please add medicine details", Toast.LENGTH_SHORT).show();
                }


            }
        });


        prescriptionSheet.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                prescriptionSheet.dismiss();

            }
        });


    }


    private void addMedicineBox()
    {

        pop_up_box =new Dialog(VideoChatViewActivity.this);
        pop_up_box.setContentView(R.layout.add_medicine_box);
        pop_up_box.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop_up_box.setCanceledOnTouchOutside(false);
        Window window= pop_up_box.getWindow();
        window.setGravity(Gravity.CENTER);
        pop_up_box.show();

        EditText medicine,quantity;
        ImageView cross;

        medicine=pop_up_box.findViewById(R.id.medicine);
        quantity=pop_up_box.findViewById(R.id.quantity);
        diagnosis=pop_up_box.findViewById(R.id.diagnosis);
        diagnosis_holder=pop_up_box.findViewById(R.id.diagnosis_holder);
        cross=pop_up_box.findViewById(R.id.cross);


        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                diagnosis.setText(null);
                diagnosis.setEnabled(true);

            }
        });

        diagnosis_holder.setLayoutManager(new LinearLayoutManager(this));

        diagnosisRecycleViewAdaptor=new DiagnosisRecycleViewAdaptor(new ArrayList<>(),VideoChatViewActivity.this);

        diagnosis_holder.setAdapter(diagnosisRecycleViewAdaptor);

        diagnosis.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                Log.d(TAG, "beforeTextChanged: ");


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if(s.toString().trim().length()!=0)
                {

                    if(diagnosis.isEnabled())
                    {
                        new Thread(new DiagnosisDetailFetcher(s.toString(),VideoChatViewActivity.this)).start();
                    }


                }
                else
                {

                }




            }

            @Override
            public void afterTextChanged(Editable s) {

                Log.d(TAG, "afterTextChanged: ");

            }
        });




        pop_up_box.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(diagnosis.getText().toString().trim().length()!=0)
                {
                    if(medicine.getText().toString().trim().length()!=0)
                    {
                        if(quantity.getText().toString().trim().length()!=0)
                        {
                            medicineDiagnosis.add(diagnosis.getText().toString());
                            medicineName.add(medicine.getText().toString());
                            medicineQuantity.add(quantity.getText().toString());

                            addPrescriptionMedicineRecycleViewAdaptor.loadData(medicineDiagnosis,medicineName,medicineQuantity);

                            pop_up_box.dismiss();

                        }
                        else
                        {
                            quantity.setError("enter medicine quantity");
                        }
                    }
                    else
                    {
                        medicine.setError("enter medicine name and unit of measurement");
                    }
                }
                else
                {
                    diagnosis.setError("enter diagnosis code");
                }



            }
        });

        pop_up_box.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pop_up_box.dismiss();

            }
        });


    }

    @Override
    public void selectedCode(List<String> list) {

        diagnosis.setEnabled(false);

        diagnosis.setText(list.get(0));

        diagnosisRecycleViewAdaptor.loadData(new ArrayList<>());


    }


    @Override
    public void confirmationDiagnosisDetailFetcher(ResponseInformation responseInformation) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                if(responseInformation.getSuccess().equals(ResponseInformation.fail_response_type))
                {

                    Toast.makeText(VideoChatViewActivity.this, responseInformation.getMessage(), Toast.LENGTH_SHORT).show();

                    diagnosisRecycleViewAdaptor.loadData(new ArrayList<>());

                }
            }
        });


    }

    @Override
    public void informationDiagnosisDetailFetcher(ICRInformationRoot icrInformationRoot) {


        handler.post(new Runnable() {
            @Override
            public void run() {

                diagnosisRecycleViewAdaptor.loadData(icrInformationRoot.getDetails());

                diagnosis_holder.setVisibility(View.VISIBLE);

            }
        });

    }


    @Override
    public void deleteItem(int position) {

        medicineDiagnosis.remove(position);
        medicineName.remove(position);
        medicineQuantity.remove(position);
        addPrescriptionMedicineRecycleViewAdaptor.loadData(medicineDiagnosis,medicineName,medicineQuantity);

    }

    @Override
    public void innerFiles(String name) {

    }

    
    private void  openHistorySheet()
    {

        BottomSheetDialog patientHistory=new BottomSheetDialog(VideoChatViewActivity.this,R.style.BottomSheetDialogTheme);

        View bottom_view= LayoutInflater.from(VideoChatViewActivity.this).inflate(R.layout.patient_history_layout
                ,
                (RelativeLayout) patientHistory.findViewById(R.id.relative_layout));

        patientHistory.setContentView(bottom_view);

        patientHistory.show();

          LinearLayout mobility_layout,allergy_layout,medicine_layout,script_layout;
          RecyclerView medicine_holder,mobility_holder,allergy_holder;
          ImageView script_image;

        mobility_layout=patientHistory.findViewById(R.id.mobility_layout);
        allergy_layout=patientHistory.findViewById(R.id.allergy_layout);
        medicine_layout=patientHistory.findViewById(R.id.medicine_layout);
        medicine_holder=patientHistory.findViewById(R.id.medicine_holder);
        mobility_holder=patientHistory.findViewById(R.id.mobility_holder);
        allergy_holder=patientHistory.findViewById(R.id.allergy_holder);
        script_layout=patientHistory.findViewById(R.id.script_layout);
        script_image=patientHistory.findViewById(R.id.script_image);

        assert allergy_layout != null;
        
        
        if(clientInformation.getAllergieList()!=null)
        {

            allergy_layout.setVisibility(View.VISIBLE);

            AllergyRecycleViewAdaptor allergyRecycleViewAdaptor=new AllergyRecycleViewAdaptor(clientInformation.getAllergieList());

            assert allergy_holder != null;
            allergy_holder.setLayoutManager(new LinearLayoutManager(VideoChatViewActivity.this));

            allergy_holder.setAdapter(allergyRecycleViewAdaptor);

        }
        else
        {
            allergy_layout.setVisibility(View.GONE);
        }


        assert mobility_layout != null;

        if(clientInformation.getMorbiditiesList()!=null)
        {
            mobility_layout.setVisibility(View.VISIBLE);

            MorbidityRecycleViewAdaptor morbidityRecycleViewAdaptor=new MorbidityRecycleViewAdaptor(clientInformation.getMorbiditiesList());

            assert mobility_holder != null;
            mobility_holder.setLayoutManager(new LinearLayoutManager(VideoChatViewActivity.this));

            mobility_holder.setAdapter(morbidityRecycleViewAdaptor);
        }
        else
        {
            mobility_layout.setVisibility(View.GONE);
        }


        assert medicine_layout != null;

        if(clientInformation.getMedicationsList()!=null)
        {

            medicine_layout.setVisibility(View.VISIBLE);

            MedicationRecycleViewAdaptor medicationRecycleViewAdaptor=new MedicationRecycleViewAdaptor(clientInformation.getMedicationsList());

            assert medicine_holder != null;
            medicine_holder.setLayoutManager(new LinearLayoutManager(VideoChatViewActivity.this));

            medicine_holder.setAdapter(medicationRecycleViewAdaptor);

        }
        else
        {
            medicine_layout.setVisibility(View.GONE);
        }


        assert script_layout != null;

        if(clientInformation.getScript_image()!=null)
        {
            script_layout.setVisibility(View.VISIBLE);

            Picasso.with(VideoChatViewActivity.this).load(Uri.parse(clientInformation.getScript_image())).error(R.drawable.no).into(script_image);
        }
        else
        {
            script_layout.setVisibility(View.GONE);
        }


        assert script_image != null;
        script_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Dialog zoomImage=new Dialog(VideoChatViewActivity.this);
                zoomImage.setContentView(R.layout.zoom_image_layout);
                zoomImage.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                zoomImage.setCanceledOnTouchOutside(true);
                Window window=zoomImage.getWindow();
                window.setGravity(Gravity.CENTER);
                zoomImage.show();

                ImageView imageView=zoomImage.findViewById(R.id.myZoomageView);

                Picasso.with(VideoChatViewActivity.this).load(Uri.parse(clientInformation.getScript_image())).error(R.drawable.no).into(imageView);

            }
        });
        
    }
    
    
    private void openEHRFilesSheet()
    {
        ehrSheetDialog=new BottomSheetDialog(VideoChatViewActivity.this,R.style.BottomSheetDialogTheme);

        View bottom_view= LayoutInflater.from(VideoChatViewActivity.this).inflate(R.layout.ehr_status_layout
                ,
                (RelativeLayout) ehrSheetDialog.findViewById(R.id.relative_layout));

        ehrSheetDialog.setContentView(bottom_view);

        ehrSheetDialog.show();

        ehrSheetDialog.setCanceledOnTouchOutside(false);
        ehrSheetDialog.setCancelable(false);


        getEHRDetails();

        ehrSheetDialog.findViewById(R.id.follow_up_box).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openFollowUpFiles();

            }
        });
        ehrSheetDialog.findViewById(R.id.prescription_box).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openPrescriptionFiles();

            }
        });
        ehrSheetDialog.findViewById(R.id.sick_box).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openSickNoteFiles();

            }
        });
        ehrSheetDialog.findViewById(R.id.referral_box).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openReferralFiles();

            }
        });

        ehrSheetDialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ehrSheetDialog.dismiss();

            }
        });
    }


    private void openPrescriptionFiles()
    {

        BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(VideoChatViewActivity.this,R.style.BottomSheetDialogTheme);

        View bottom_view= LayoutInflater.from(VideoChatViewActivity.this).inflate(R.layout.inner_ehr_files_layout
                ,
                (RelativeLayout) bottomSheetDialog.findViewById(R.id.relative_layout));

        bottomSheetDialog.setContentView(bottom_view);

        bottomSheetDialog.show();

        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.setCancelable(false);

        inner_loading=bottomSheetDialog.findViewById(R.id.inner_loading);
        inner_reload=bottomSheetDialog.findViewById(R.id.inner_reload);


        RecyclerView medicine_recycle =bottomSheetDialog.findViewById(R.id.file_holder);

        medicine_recycle.setLayoutManager(new LinearLayoutManager(VideoChatViewActivity.this));


        prescriptionRecycleViewAdaptor=new PrescriptionRecycleViewAdaptor(new ArrayList<>(),
                VideoChatViewActivity.this);


        medicine_recycle.setAdapter(prescriptionRecycleViewAdaptor);

        getPrescriptionNoteDetails();

        bottomSheetDialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetDialog.dismiss();

            }
        });


    }

    private void getPrescriptionNoteDetails()
    {
        if(!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED))
        {
            inner_loading.setVisibility(View.VISIBLE);
            inner_reload.setVisibility(View.GONE);


            new Thread(new PrescriptionFileAccessCaller(4, VideoChatViewActivity.this,
                    videoChatRequestInformation.getProviderInformation().getUserId())).start();


        }

        else
        {
            inner_reload.setVisibility(View.VISIBLE);
            inner_loading.setVisibility(View.GONE);

            DialogShower dialogShower=new DialogShower(R.layout.internet_error,VideoChatViewActivity.this);
            dialogShower.showDialog();
        }
    }

    @Override
    public void confirmationPrescriptionFileAccessCaller(ResponseInformation responseInformation) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                if(responseInformation.getSuccess().equals(ResponseInformation.fail_response_type))
                {
                    inner_loading.setVisibility(View.GONE);
                    inner_reload.setVisibility(View.VISIBLE);

                    Toast.makeText(VideoChatViewActivity.this, responseInformation.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    @Override
    public void informationPrescriptionFileAccessCaller(List<PrescriptionInformation> prescriptionInformation, String baseUrl) {

        this.baseUrl=baseUrl;

        handler.post(new Runnable() {
            @Override
            public void run() {

                inner_loading.setVisibility(View.GONE);
                inner_reload.setVisibility(View.GONE);

                prescriptionRecycleViewAdaptor.loadData(prescriptionInformation);
            }
        });



    }


    @Override
    public void openFile(PrescriptionInformation prescriptionInformation) {

        Intent intent = new Intent();
        intent.setDataAndType(Uri.parse(baseUrl+"/"+prescriptionInformation.getId()), "application/pdf");
        startActivity(intent);

    }

    @Override
    public void downloadFile(PrescriptionInformation prescriptionInformation) {

    }


    private void openReferralFiles()
    {

        BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(VideoChatViewActivity.this,R.style.BottomSheetDialogTheme);

        View bottom_view= LayoutInflater.from(VideoChatViewActivity.this).inflate(R.layout.inner_ehr_files_layout
                ,
                (RelativeLayout) bottomSheetDialog.findViewById(R.id.relative_layout));

        bottomSheetDialog.setContentView(bottom_view);

        bottomSheetDialog.show();

        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.setCancelable(false);

        inner_loading=bottomSheetDialog.findViewById(R.id.inner_loading);
        inner_reload=bottomSheetDialog.findViewById(R.id.inner_reload);


        RecyclerView medicine_recycle =bottomSheetDialog.findViewById(R.id.file_holder);

        medicine_recycle.setLayoutManager(new LinearLayoutManager(VideoChatViewActivity.this));


        referralRecycleViewAdaptor=new ReferralRecycleViewAdaptor(new ArrayList<>(),
                VideoChatViewActivity.this);


        medicine_recycle.setAdapter(referralRecycleViewAdaptor);

        getReferralNoteDetails();

        bottomSheetDialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetDialog.dismiss();

            }
        });



    }

    private void getReferralNoteDetails()
    {

        if(!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED))
        {
            inner_loading.setVisibility(View.VISIBLE);
            inner_reload.setVisibility(View.GONE);


            new Thread(new ReferralFileAccessCaller(3, VideoChatViewActivity.this,
                    videoChatRequestInformation.getProviderInformation().getUserId())).start();

        }

        else
        {
            inner_reload.setVisibility(View.VISIBLE);
            inner_loading.setVisibility(View.GONE);

            DialogShower dialogShower=new DialogShower(R.layout.internet_error,VideoChatViewActivity.this);
            dialogShower.showDialog();
        }

    }

    @Override
    public void confirmationReferralFileAccessCaller(ResponseInformation responseInformation) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                if(responseInformation.getSuccess().equals(ResponseInformation.fail_response_type))
                {
                    inner_loading.setVisibility(View.GONE);
                    inner_reload.setVisibility(View.VISIBLE);

                    Toast.makeText(VideoChatViewActivity.this, responseInformation.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    @Override
    public void informationReferralFileAccessCaller(List<ReferralInformation> referralInformation, String baseUrl) {

        this.baseUrl=baseUrl;

        handler.post(new Runnable() {
            @Override
            public void run() {

                inner_loading.setVisibility(View.GONE);
                inner_reload.setVisibility(View.GONE);

                referralRecycleViewAdaptor.loadData(referralInformation);
            }
        });


    }



    @Override
    public void openFile(ReferralInformation referralInformation) {

        Intent intent = new Intent();
        intent.setDataAndType(Uri.parse(baseUrl+"/"+referralInformation.getId()), "application/pdf");
        startActivity(intent);

    }

    @Override
    public void downloadFile(ReferralInformation referralInformation) {

    }


    private void openSickNoteFiles()
    {
        BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(VideoChatViewActivity.this,R.style.BottomSheetDialogTheme);

        View bottom_view= LayoutInflater.from(VideoChatViewActivity.this).inflate(R.layout.inner_ehr_files_layout
                ,
                (RelativeLayout) bottomSheetDialog.findViewById(R.id.relative_layout));

        bottomSheetDialog.setContentView(bottom_view);

        bottomSheetDialog.show();

        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.setCancelable(false);

        inner_loading=bottomSheetDialog.findViewById(R.id.inner_loading);
        inner_reload=bottomSheetDialog.findViewById(R.id.inner_reload);


        RecyclerView medicine_recycle =bottomSheetDialog.findViewById(R.id.file_holder);

        medicine_recycle.setLayoutManager(new LinearLayoutManager(VideoChatViewActivity.this));


        sickNoteFileRecycleViewAdaptor=new SickNoteFileRecycleViewAdaptor(new ArrayList<>(),
                VideoChatViewActivity.this);


        medicine_recycle.setAdapter(sickNoteFileRecycleViewAdaptor);

        getSickNoteDetails();

        bottomSheetDialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetDialog.dismiss();

            }
        });


    }


    private void getSickNoteDetails()
    {
        if(!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED))
        {
            inner_loading.setVisibility(View.VISIBLE);
            inner_reload.setVisibility(View.GONE);


            new Thread(new SickNoteFileAccessCaller(2, VideoChatViewActivity.this,
                    videoChatRequestInformation.getProviderInformation().getUserId())).start();

        }

        else
        {
            inner_reload.setVisibility(View.VISIBLE);
            inner_loading.setVisibility(View.GONE);

            DialogShower dialogShower=new DialogShower(R.layout.internet_error,VideoChatViewActivity.this);
            dialogShower.showDialog();
        }
    }

    @Override
    public void confirmationSickNoteFileAccessCaller(ResponseInformation responseInformation) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                if(responseInformation.getSuccess().equals(ResponseInformation.fail_response_type))
                {
                    inner_loading.setVisibility(View.GONE);
                    inner_reload.setVisibility(View.VISIBLE);

                    Toast.makeText(VideoChatViewActivity.this, responseInformation.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    @Override
    public void informationSickNoteFileAccessCaller(List<SickNoteInformation> sickNoteInformation, String baseUrl) {

        this.baseUrl=baseUrl;

        handler.post(new Runnable() {
            @Override
            public void run() {

                inner_loading.setVisibility(View.GONE);
                inner_reload.setVisibility(View.GONE);

                sickNoteFileRecycleViewAdaptor.loadData(sickNoteInformation);
            }
        });

    }


    private void openFollowUpFiles()
    {

        BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(VideoChatViewActivity.this,R.style.BottomSheetDialogTheme);

        View bottom_view= LayoutInflater.from(VideoChatViewActivity.this).inflate(R.layout.inner_ehr_files_layout
                ,
                (RelativeLayout) bottomSheetDialog.findViewById(R.id.relative_layout));

        bottomSheetDialog.setContentView(bottom_view);

        bottomSheetDialog.show();

        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.setCancelable(false);

        inner_loading=bottomSheetDialog.findViewById(R.id.inner_loading);
        inner_reload=bottomSheetDialog.findViewById(R.id.inner_reload);


        RecyclerView medicine_recycle =bottomSheetDialog.findViewById(R.id.file_holder);

        medicine_recycle.setLayoutManager(new LinearLayoutManager(VideoChatViewActivity.this));

        followUpFileRecycleViewAdaptor=new FollowUpFileRecycleViewAdaptor(new ArrayList<>(),
                VideoChatViewActivity.this);

        medicine_recycle.setAdapter(followUpFileRecycleViewAdaptor);


        getFollowUpDetails();

        bottomSheetDialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetDialog.dismiss();

            }
        });

    }


    private void getFollowUpDetails()
    {
        if(!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED))
        {
            inner_loading.setVisibility(View.VISIBLE);
            inner_reload.setVisibility(View.GONE);


            new Thread(new FollowUpFileAccessCaller(1, VideoChatViewActivity.this,
                    videoChatRequestInformation.getProviderInformation().getUserId())).start();

        }

        else
        {
            inner_reload.setVisibility(View.VISIBLE);
            inner_loading.setVisibility(View.GONE);

            DialogShower dialogShower=new DialogShower(R.layout.internet_error,VideoChatViewActivity.this);
            dialogShower.showDialog();
        }
    }


    @Override
    public void confirmationFollowUpFileAccessCaller(ResponseInformation responseInformation) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                if(responseInformation.getSuccess().equals(ResponseInformation.fail_response_type))
                {
                    inner_loading.setVisibility(View.GONE);
                    inner_reload.setVisibility(View.VISIBLE);

                    Toast.makeText(VideoChatViewActivity.this, responseInformation.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    @Override
    public void informationFollowUpFileAccessCaller(List<FollowUpInformation> followUpInformation, String baseUrl) {

        this.baseUrl=baseUrl;

        handler.post(new Runnable() {
            @Override
            public void run() {

                inner_loading.setVisibility(View.GONE);
                inner_reload.setVisibility(View.GONE);

                followUpFileRecycleViewAdaptor.loadData(followUpInformation);
            }
        });

    }




    @Override
    public void openFile(SickNoteInformation sickNoteInformation) {

        Intent intent = new Intent();
        intent.setDataAndType(Uri.parse(baseUrl+"/"+sickNoteInformation.getId()), "application/pdf");
        startActivity(intent);

    }

    @Override
    public void downloadFile(SickNoteInformation sickNoteInformation) {

    }


    @Override
    public void openFile(FollowUpInformation followUpInformation) {

        Intent intent = new Intent();
        intent.setDataAndType(Uri.parse(baseUrl+"/"+followUpInformation.getId()), "application/pdf");
        startActivity(intent);


    }

    @Override
    public void downloadFile(FollowUpInformation followUpInformation) {

        // use for download file
    }


    private void getEHRDetails()
    {
        if(!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED))
        {

            new Thread(new EHRFileCaller(VideoChatViewActivity.this,
                    videoChatRequestInformation.getProviderInformation().getUserId())).start();

        }

        else
        {
            DialogShower dialogShower=new DialogShower(R.layout.internet_error,VideoChatViewActivity.this);
            dialogShower.showDialog();
        }
    }

    @Override
    public void confirmationEHRFileCaller(ResponseInformation responseInformation) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                if(responseInformation.getSuccess().equals(ResponseInformation.fail_response_type))
                {

                    Toast.makeText(VideoChatViewActivity.this, responseInformation.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });


    }

    @Override
    public void informationEHRFileCaller(EHRFilesInformation ehrFilesInformation) {

        handler.post(new Runnable() {
            @Override
            public void run() {


                uploadEHRFilesInformation(ehrFilesInformation);

            }
        });

    }

    private void uploadEHRFilesInformation(EHRFilesInformation ehrFilesInformation)
    {
        TextView follow_up,prescription_note,sick_note,referral_note;

        follow_up=ehrSheetDialog.findViewById(R.id.follow_up);
        prescription_note=ehrSheetDialog.findViewById(R.id.prescription);
        sick_note=ehrSheetDialog.findViewById(R.id.sick_note);
        referral_note=ehrSheetDialog.findViewById(R.id.referral_note);

        String follow=ehrFilesInformation.getFollowUpSheets()+" files";
        String prescription=ehrFilesInformation.getPrescriptionSheets()+" files";
        String sick=ehrFilesInformation.getMedicalCertificateSheets()+" files";
        String referral=ehrFilesInformation.getReferralSheets()+" files";

        follow_up.setText(follow);
        prescription_note.setText(prescription);
        sick_note.setText(sick);
        referral_note.setText(referral);
    }


    private void viewEHRFilesSheet()
    {
        BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(VideoChatViewActivity.this,R.style.BottomSheetDialogTheme);

        View bottom_view= LayoutInflater.from(VideoChatViewActivity.this).inflate(R.layout.inner_ehr_files_layout
                ,
                (RelativeLayout) bottomSheetDialog.findViewById(R.id.relative_layout));

        bottomSheetDialog.setContentView(bottom_view);

        bottomSheetDialog.show();

        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.setCancelable(false);


        bottomSheetDialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetDialog.dismiss();

            }
        });

        RecyclerView recyclerView=bottomSheetDialog.findViewById(R.id.file_holder);

        assert recyclerView != null;

        recyclerView.setLayoutManager(new LinearLayoutManager(VideoChatViewActivity.this));

        ViewEHRRecycleViewAdaptor viewEHRRecycleViewAdaptor = new ViewEHRRecycleViewAdaptor();

        recyclerView.setAdapter(viewEHRRecycleViewAdaptor);


    }


    private void removeRequest(int mode)
    {
        if(mode==0)
        {

            AlertDialog.Builder builder=new AlertDialog.Builder(VideoChatViewActivity.this)
                    .setMessage("Are you sure you want leave this session").setNegativeButton("NO", (dialogInterface, i) -> {

                    }).setPositiveButton("YES", (dialogInterface, i) -> {

                        removeMyRequest();


                    });
            builder.setCancelable(false);
            builder.show();

        }
        else if(mode==1)
        {
            AlertDialog.Builder builder=new AlertDialog.Builder(VideoChatViewActivity.this)
                    .setMessage("Are you sure you want leave this session").setNegativeButton("NO", (dialogInterface, i) -> {

                    }).setPositiveButton("YES", (dialogInterface, i) -> {

                        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(URLBuilder.FirebaseDataNodes.VideoChatRequest);

                        databaseReference.child(videoChatRequestInformation.getProviderInformation().getUserId())
                                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                ProviderMainFrame.state=false;

                                releaseDoctorKey();

                            }
                        });
                    });
            builder.setCancelable(false);
            builder.show();



        }

    }


    private void releaseDoctorKey()
    {
        leaveChannel();
        RtcEngine.destroy();

        startActivity(new Intent(VideoChatViewActivity.this,ProviderMainFrame.class));
    }

    private void removeMyRequest()
    {

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(URLBuilder.FirebaseDataNodes.VideoChatRequest);

        databaseReference.child(ClientMainFrame.id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                releasePatientKey();

            }
        });
    }

    private void releasePatientKey()
    {
        leaveChannel();
        RtcEngine.destroy();
        ClientMainFrame.activateStatusCheck=false;
        BaseActivity.videoChatRequestInformation=null;
        startActivity(new Intent(VideoChatViewActivity.this,ClientMainFrame.class));

    }

    private void removeActivatedService()
    {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(URLBuilder.FirebaseDataNodes.VideoChatRequest);

        databaseReference.child(videoChatRequestInformation.getProviderInformation().getUserId()).removeValue();
    }



    private void deactivateState()
    {

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(URLBuilder.FirebaseDataNodes.PatientStateInformation);

        String random_key_generator=databaseReference.push().getKey();

        PatientStateInformation patientStateInformation=new PatientStateInformation("0",random_key_generator);

        assert random_key_generator != null;

        databaseReference.child(ClientMainFrame.id).child(ClientMainFrame.patientStateInformation.getObjectKey()).setValue(patientStateInformation).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                startActivity(new Intent(VideoChatViewActivity.this,ClientMainFrame.class));

            }
        });

    }

    private void setUpPermissions()
    {
        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID)) {
            initEngineAndJoinChannel();
        }
    }

    private void establishViews() {
        mLocalContainer = findViewById(R.id.local_video_view_container);
        mRemoteContainer = findViewById(R.id.remote_video_view_container);
        mCallBtn = findViewById(R.id.leave_channel);
        mMuteBtn = findViewById(R.id.mute);
        mSwitchCameraBtn = findViewById(R.id.btn_switch_camera);
        ehr_section = findViewById(R.id.ehr_section);
        showSampleLogs();
    }

    private void showSampleLogs() {
//        mLogView.logI("Welcome to Agora 1v1 video call");
//        mLogView.logW("You will see custom logs here");
//        mLogView.logE("You can also use this to show errors");
    }

    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode);
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQ_ID) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                    grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                showLongToast("Need permissions " + Manifest.permission.RECORD_AUDIO +
                        "/" + Manifest.permission.CAMERA);
                finish();
                return;
            }

            initEngineAndJoinChannel();
        }
    }

    private void showLongToast(final String msg) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initEngineAndJoinChannel() {

        initializeEngine();
        setupVideoConfig();
        setupLocalVideo();
        joinChannel();
    }

    private void initializeEngine() {
        try {
            mRtcEngine = RtcEngine.create(getBaseContext(), getString(R.string.agora_app_id), mRtcEventHandler);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }
    }

    private void setupVideoConfig() {
        // In simple use cases, we only need to enable video capturing
        // and rendering once at the initialization step.
        // Note: audio recording and playing is enabled by default.
        mRtcEngine.enableVideo();

        // Please go to this page for detailed explanation
        // https://docs.agora.io/en/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#af5f4de754e2c1f493096641c5c5c1d8f
        mRtcEngine.setVideoEncoderConfiguration(new VideoEncoderConfiguration(
                VideoEncoderConfiguration.VD_640x360,
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT));
    }

    private void setupLocalVideo() {
        // This is used to set a local preview.
        // The steps setting local and remote view are very similar.
        // But note that if the local user do not have a uid or do
        // not care what the uid is, he can set his uid as ZERO.
        // Our server will assign one and return the uid via the event
        // handler callback function (onJoinChannelSuccess) after
        // joining the channel successfully.
        SurfaceView view = RtcEngine.CreateRendererView(getBaseContext());
        view.setZOrderMediaOverlay(true);
        mLocalContainer.addView(view);
        // Initializes the local video view.
        // RENDER_MODE_HIDDEN: Uniformly scale the video until it fills the visible boundaries. One dimension of the video may have clipped contents.
        mLocalVideo = new VideoCanvas(view, VideoCanvas.RENDER_MODE_HIDDEN, 0);
        mRtcEngine.setupLocalVideo(mLocalVideo);
    }

    private void joinChannel() {

        String token = videoChatRequestInformation.getToke();
        mRtcEngine.joinChannel(token,videoChatRequestInformation.getChannelName(), "Extra Optional Data", 0);
    }




    @Override
    protected void onDestroy() {

        BaseActivity.videoChatRequestInformation=null;

        if (!mCallEnd) {

            leaveChannel();
        }
        /*
          Destroys the RtcEngine instance and releases all resources used by the Agora SDK.

          This method is useful for apps that occasionally make voice or video calls,
          to free up resources for other operations when not making calls.
         */
        RtcEngine.destroy();

        super.onDestroy();
    }

    private void leaveChannel() {

        if(mRtcEngine!=null)
        {
            mRtcEngine.leaveChannel();
        }
    }

    public void onLocalAudioMuteClicked(View view) {
        mMuted = !mMuted;
        // Stops/Resumes sending the local audio stream.
        mRtcEngine.muteLocalAudioStream(mMuted);
        int res = mMuted ? R.drawable.btn_mute : R.drawable.btn_unmute;
        mMuteBtn.setImageResource(res);
    }

    public void onSwitchCameraClicked(View view) {
        // Switches between front and rear cameras.
        mRtcEngine.switchCamera();
    }

    public void onCallClicked(View view) {
        if (mCallEnd) {
            startCall();
            mCallEnd = false;
            mCallBtn.setImageResource(R.drawable.btn_endcall);
        } else {
            endCall();
            mCallEnd = true;
            mCallBtn.setImageResource(R.drawable.btn_startcall);
        }

        showButtons(!mCallEnd);
    }

    private void startCall() {
        setupLocalVideo();
        joinChannel();
    }

    private void endCall() {
        removeFromParent(mLocalVideo);
        mLocalVideo = null;
        removeFromParent(mRemoteVideo);
        mRemoteVideo = null;
        leaveChannel();
    }

    private void showButtons(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
        mMuteBtn.setVisibility(visibility);
        mSwitchCameraBtn.setVisibility(visibility);
    }

    private ViewGroup removeFromParent(VideoCanvas canvas) {
        if (canvas != null) {
            ViewParent parent = canvas.view.getParent();
            if (parent != null) {
                ViewGroup group = (ViewGroup) parent;
                group.removeView(canvas.view);
                return group;
            }
        }
        return null;
    }

    private void switchView(VideoCanvas canvas) {
        ViewGroup parent = removeFromParent(canvas);
        if (parent == mLocalContainer) {
            if (canvas.view instanceof SurfaceView) {
                ((SurfaceView) canvas.view).setZOrderMediaOverlay(false);
            }
            mRemoteContainer.addView(canvas.view);
        } else if (parent == mRemoteContainer) {
            if (canvas.view instanceof SurfaceView) {
                ((SurfaceView) canvas.view).setZOrderMediaOverlay(true);
            }
            mLocalContainer.addView(canvas.view);
        }
    }

    public void onLocalContainerClick(View view) {
        switchView(mLocalVideo);
        switchView(mRemoteVideo);
    }


    private void openLoadingBox()
    {
        loading_box =new Dialog(VideoChatViewActivity.this);
        loading_box.setContentView(R.layout.loading_layout);
        loading_box.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading_box.setCanceledOnTouchOutside(false);
        loading_box.setCancelable(false);
        Window window= loading_box.getWindow();
        window.setGravity(Gravity.CENTER);
        loading_box.show();
    }


    @Override
    public void confirmationCancelBookingCaller(ResponseInformation responseInformation, PatientInformation patientInformation) {


    }


}
