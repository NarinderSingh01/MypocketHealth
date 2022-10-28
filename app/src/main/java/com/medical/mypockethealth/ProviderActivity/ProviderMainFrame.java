package com.medical.mypockethealth.ProviderActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.medical.mypockethealth.ClientActivity.ClientMainFrame;
import com.medical.mypockethealth.ProviderFragments.ProviderHomeFragment;
import com.medical.mypockethealth.R;

public class ProviderMainFrame extends AppCompatActivity {



    private static final String TAG = "ProviderMainFrame";

    public static final String provider_information_file="provider_information_file";
    public static final String provider_information_key="provider_information_key";
    public static final String provider_edit_information_file="provider_edit_information_file";
    public static final String provider_edit_information_key="provider_edit_information_key";
    public static final String provider_create_profile_file="provider_create_profile_file";
    public static final String provider_create_profile_key="provider_create_profile_key";
    public static String id=null;
    public static boolean providerAppStatus=false;
    public static boolean state=false,activateStatus=false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_main_frame);

        ProviderMainFrame.providerAppStatus=true;

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,new ProviderHomeFragment()).addToBackStack(null).commit();

    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 1) {

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);

        } else {
            getSupportFragmentManager().popBackStack();
        }
    }
}