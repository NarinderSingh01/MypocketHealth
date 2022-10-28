package com.medical.mypockethealth.ClientFragments.EHRSection.FollowUpSection;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.mbms.DownloadRequest;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.androidnetworking.model.Progress;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.medical.mypockethealth.Adaptors.UserSection.EHRSection.FollowUpFileRecycleViewAdaptor;
import com.medical.mypockethealth.ApplicationBase.BaseActivity;
import com.medical.mypockethealth.Classes.DialogShower;
import com.medical.mypockethealth.Classes.EHRSection.FollowUpInformation.FollowUpInformation;
import com.medical.mypockethealth.Classes.NetworkSection.NetworkStatus;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.ClientActivity.ClientMainFrame;
import com.medical.mypockethealth.ClientFragments.EHRSection.EHROneFragment;
import com.medical.mypockethealth.R;
import com.medical.mypockethealth.Threads.UserSection.EHRSection.FollowUpFileAccessCaller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Request;

import static android.content.Context.DOWNLOAD_SERVICE;
import static android.os.Environment.DIRECTORY_DOCUMENTS;


public class FollowUpFragment extends Fragment implements FollowUpFileRecycleViewAdaptor.callBackFromFollowUpFileRecycleViewAdaptor,
        FollowUpFileAccessCaller.CallbackFromEhrFileAccessCaller{

    private static final String TAG = "FollowUpFragment";

    private ImageView loading,reload;
    private String baseUrl;
    private final Handler handler=new Handler();
    private FollowUpFileRecycleViewAdaptor followUpFileRecycleViewAdaptor;
    private Dialog loading_box;
    private FollowUpInformation followUpInformation;


    public FollowUpFragment() {
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
        return inflater.inflate(R.layout.fragment_follow_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        establishViews(view);

        getFollowUpDetails();

        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                        new EHROneFragment()).addToBackStack(null).commit();
            }
        });

        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              getFollowUpDetails();

            }
        });


        RecyclerView medicine_recycle =view.findViewById(R.id.medicine_holder);

        medicine_recycle.setLayoutManager(new LinearLayoutManager(getContext()));

        followUpFileRecycleViewAdaptor=new FollowUpFileRecycleViewAdaptor(new ArrayList<>(), FollowUpFragment.this);

        medicine_recycle.setAdapter(followUpFileRecycleViewAdaptor);

    }


    private void getFollowUpDetails()
    {
        if(!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED))
        {
            loading.setVisibility(View.VISIBLE);
            reload.setVisibility(View.GONE);


             new Thread(new FollowUpFileAccessCaller(1,FollowUpFragment.this, ClientMainFrame.id)).start();

        }

        else
        {
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
    public void confirmationFollowUpFileAccessCaller(ResponseInformation responseInformation) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                if(responseInformation.getSuccess().equals(ResponseInformation.fail_response_type))
                {
                    loading.setVisibility(View.GONE);
                    reload.setVisibility(View.VISIBLE);

                    Toast.makeText(getContext(), responseInformation.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    @Override
    public void informationFollowUpFileAccessCaller(List<FollowUpInformation> followUpInformation,String baseUrl) {

        this.baseUrl=baseUrl;

        handler.post(new Runnable() {
            @Override
            public void run() {

                loading.setVisibility(View.GONE);
                reload.setVisibility(View.GONE);

                followUpFileRecycleViewAdaptor.loadData(followUpInformation);
            }
        });


    }

 

    @Override
    public void openFile(FollowUpInformation followUpInformation) {

        if (followUpInformation.getId()!=null && followUpInformation.getId().length()!=0){

//            Intent intent = new Intent();
//            intent.setDataAndType(Uri.parse(baseUrl+"/"+followUpInformation.getId()), "application/pdf");
//            startActivity(intent);

            String pdf = baseUrl+"/"+followUpInformation.getId();

            Intent pdfIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdf));
            startActivity(pdfIntent);

        }else {

            Toast.makeText(requireContext(), "Unable to open pdf , please try again later", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void downloadFile(FollowUpInformation followUpInformation) {


        this.followUpInformation=followUpInformation;

        int permission = ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                    , Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        else {

            openBottomSheet(followUpInformation);
        }




    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                openBottomSheet(followUpInformation);

            } else {

                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                            , Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

                } else {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", requireContext().getPackageName(), null);
                    intent.setData(uri);
                    requireContext().startActivity(intent);

                }
            }

        }

    }


    private void openBottomSheet(FollowUpInformation followUpInformation)
    {

        BottomSheetDialog forgotBottomSheet = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme);

        View bottom_view = LayoutInflater.from(getContext()).inflate(R.layout.folder_name_layout
                ,
                (RelativeLayout) forgotBottomSheet.findViewById(R.id.relative_layout));

        forgotBottomSheet.setContentView(bottom_view);

        forgotBottomSheet.show();

        forgotBottomSheet.setCanceledOnTouchOutside(false);
        forgotBottomSheet.setCancelable(false);

        EditText folder_name=forgotBottomSheet.findViewById(R.id.folder_name);

        forgotBottomSheet.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                forgotBottomSheet.dismiss();
            }
        });

        forgotBottomSheet.findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(folder_name.getText().toString().trim().length()!=0)
                {
                    folder_name.setError(null);

                    followUpInformation.setFolderName(folder_name.getText().toString());

                    startDownloading(followUpInformation);

                    forgotBottomSheet.dismiss();

                }
                else
                {
                    folder_name.setError("enter folder name");
                }

            }
        });

    }



    private void startDownloading(FollowUpInformation followUpInformation)
    {
        
        openLoadingBox();

        String fileName=followUpInformation.getId()+".pdf";

       File dir = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOCUMENTS).getPath()
                +"/"+followUpInformation.getFolderName());

        if (!dir.exists()) {

            Log.d(TAG, "startDownloading: file creation " + dir.mkdir() );

        }
        new Thread(new Runnable() {
            @Override
            public void run() {

                AndroidNetworking.download(baseUrl+"/"+followUpInformation.getId(),dir.toString(),fileName)
                        .setTag("downloadTest")
                        .setPriority(Priority.MEDIUM)
                        .build().setDownloadProgressListener(new DownloadProgressListener() {
                    @Override
                    public void onProgress(long bytesDownloaded, long totalBytes) {


                    }
                }).startDownload(new DownloadListener() {
                    @Override
                    public void onDownloadComplete() {

                        loading_box.dismiss();
                        Toast.makeText(getContext(), "File saved to "+followUpInformation.getFolderName(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d(TAG, "onError: " +anError.getMessage());

                        loading_box.dismiss();

                        Toast.makeText(getContext(), "Unable to download file ", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        }).start();


    }


    private void openLoadingBox() {
        loading_box = new Dialog(getContext());
        loading_box.setContentView(R.layout.downloading_layout);
        loading_box.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading_box.setCanceledOnTouchOutside(false);
        loading_box.setCancelable(false);
        Window window = loading_box.getWindow();
        window.setGravity(Gravity.CENTER);
        loading_box.show();



    }

}