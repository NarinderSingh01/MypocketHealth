package com.medical.mypockethealth.ApplicationBase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.WindowManager;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.medical.mypockethealth.ApplicationFragments.BaseFragment;
import com.medical.mypockethealth.BaseFragments.IntroductionFragment;
import com.medical.mypockethealth.Classes.AppUpdateInformation.AppUpdateInformation;
import com.medical.mypockethealth.Classes.NetworkSection.NetworkStatus;
import com.medical.mypockethealth.Classes.URLBuilder;
import com.medical.mypockethealth.Classes.VideoChatInformation.VideoChatRequestInformation;
import com.medical.mypockethealth.R;

import java.util.Timer;
import java.util.TimerTask;

public class UpdateHandlerActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";

    public static int mode=0;
    public static int network_status=1;
    private BroadcastReceiver networkChangeReceiver;
    public static boolean notificationStatus=false;
    public static VideoChatRequestInformation videoChatRequestInformation;
    public static final String data_key="data_key";
    private AppUpdateManager appUpdateManager;
    public static final int RC_APP_UPDATE=1;
    private AppUpdateInformation appUpdateInformation;


    @Override
    protected void onResume() {
        super.onResume();

        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {

                if(appUpdateInfo.updateAvailability()== UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS &&
                        appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE))
                {

                    try {
                        appUpdateManager.startUpdateFlowForResult(appUpdateInfo,AppUpdateType.IMMEDIATE,UpdateHandlerActivity.this,RC_APP_UPDATE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_handler);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        appUpdateManager= AppUpdateManagerFactory.create(this);

        checkForUpdates();


    }



    private void checkForUpdates()
    {
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {

                if(appUpdateInfo.updateAvailability()==UpdateAvailability.UPDATE_AVAILABLE &&
                        appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE))
                {
                    appUpdateInformation=new AppUpdateInformation(appUpdateInfo.availableVersionCode());

                    if(appUpdateInfo.updateAvailability()==UpdateAvailability.UPDATE_AVAILABLE &&
                            appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE))
                    {
                        try {
                            appUpdateManager.startUpdateFlowForResult(appUpdateInfo,AppUpdateType.IMMEDIATE,UpdateHandlerActivity.this,RC_APP_UPDATE);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                    }

                }




            }
        });
    }



    private void updateAndLoadApplication()
    {

        updateAppUpdateStatus();

        startActivity(new Intent(UpdateHandlerActivity.this,BaseActivity.class));
    }

    private void updateAppUpdateStatus()
    {

        SharedPreferences sharedPreferences = getSharedPreferences(URLBuilder.appUpdateFile, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor1=sharedPreferences.edit();

        editor1.clear();
        editor1.apply();

        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();

        String json = gson.toJson(appUpdateInformation);

        editor.putString(URLBuilder.appUpdateKey, json);

        editor.apply();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==RC_APP_UPDATE && resultCode!=RESULT_OK)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        }
        else
        {

            updateAndLoadApplication();

        }

    }




    @Override
    protected void onPause() {
        super.onPause();

    }




}