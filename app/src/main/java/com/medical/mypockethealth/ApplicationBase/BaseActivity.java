

package com.medical.mypockethealth.ApplicationBase;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.content.IntentSender;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.medical.mypockethealth.ApplicationFragments.BaseFragment;
import com.medical.mypockethealth.BaseFragments.IntroductionFragment;
import com.medical.mypockethealth.BaseFragments.RegisterSection.ProviderRegistrationSection.SignUpAsProviderFragment;
import com.medical.mypockethealth.Classes.AppUpdateInformation.AppUpdateInformation;
import com.medical.mypockethealth.Classes.NetworkSection.NetworkStatus;

import com.medical.mypockethealth.Classes.URLBuilder;
import com.medical.mypockethealth.Classes.VideoChatInformation.VideoChatRequestInformation;

import com.medical.mypockethealth.ProviderActivity.ProviderMainFrame;
import com.medical.mypockethealth.R;

import java.util.Timer;
import java.util.TimerTask;


public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    public static int mode=0;
    public static int network_status=1;
    private BroadcastReceiver networkChangeReceiver;
    public static boolean notificationStatus=false;
    public static VideoChatRequestInformation videoChatRequestInformation;
    public static final String data_key="data_key";
    private AppUpdateManager appUpdateManager;
    public static final int RC_APP_UPDATE=1;

    private void broadCast_caller()
    {
        networkChangeReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                
                int status= NetworkStatus.get_connectivity_status(context);

                if(status== NetworkStatus.TYPE_MOBILE)
                {

                    network_status=1;
                }
                else if(status== NetworkStatus.TYPE_WIFI)
                {

                    network_status=1;
                }

                else if(status== NetworkStatus.TYPE_NOT_CONNECTED)
                {
                    network_status= NetworkStatus.TYPE_NOT_CONNECTED;
                }

            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, intentFilter);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
         broadCast_caller();

        videoChatRequestInformation=(VideoChatRequestInformation)getIntent().getSerializableExtra(BaseActivity.data_key);
        
        appUpdateManager=AppUpdateManagerFactory.create(this);

        checkForUpdates();

//        startActivity(new Intent(BaseActivity.this, ClientMainFrame.class));

        
//        getSupportFragmentManager().beginTransaction().replace(R.id.frame_holder,new IntroductionFragment()).addToBackStack(null).commit();
//
//        Timer timer=new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//
//                getSupportFragmentManager().beginTransaction().replace(R.id.frame_holder,new BaseFragment()).addToBackStack(null).commit();
//
//            }
//
//        },2000);


//        SharedPreferences sharedPreferences1=getSharedPreferences(ClientMainFrame.client_information_file, Context.MODE_PRIVATE);
//
//        SharedPreferences.Editor editor1=sharedPreferences1.edit();
//
//        editor1.clear();
//        editor1.apply();
//
//        SharedPreferences sharedPreferences=getSharedPreferences(URLBuilder.appUpdateFile, Context.MODE_PRIVATE);
//
//        SharedPreferences.Editor editor=sharedPreferences.edit();
//
//        editor.clear();
//
//        editor.apply();

        
    }



    private void checkForUpdates()
    {
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {


                if(appUpdateInfo.updateAvailability()==UpdateAvailability.UPDATE_AVAILABLE &&
                        appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE))
                {

                    SharedPreferences sharedPreferences=getSharedPreferences(URLBuilder.appUpdateFile, Context.MODE_PRIVATE);
                    String json = sharedPreferences.getString(URLBuilder.appUpdateKey, "");

                    if(json.length()!=0)
                    {

                        Gson gson = new Gson();
                        String data = sharedPreferences.getString(URLBuilder.appUpdateKey, "");
                        AppUpdateInformation information = gson.fromJson(data, AppUpdateInformation.class);

                        if(information.getVersionCode()!=appUpdateInfo.availableVersionCode())
                        {
                           startActivity(new Intent(BaseActivity.this,UpdateHandlerActivity.class));
                        }
                        else
                        {
                            loadApplication();
                        }


                    }
                    else
                    {
                        startActivity(new Intent(BaseActivity.this,UpdateHandlerActivity.class));
                    }

                }

                else
                {
                    loadApplication();
                }



            }
        });
    }



    private void loadApplication()
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_holder,new IntroductionFragment()).addToBackStack(null).commit();

        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                getSupportFragmentManager().beginTransaction().replace(R.id.frame_holder,new BaseFragment()).addToBackStack(null).commit();

            }

        },2000);
    }
    
    @Override
    protected void onPause() {
        super.onPause();

    }



    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 2) {

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);

        } else {
            getSupportFragmentManager().popBackStack();
        }
    }


}