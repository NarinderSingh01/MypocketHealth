package com.medical.mypockethealth.ClientFragments.EHRSection.SickNotes;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.medical.mypockethealth.Adaptors.UserSection.EHRSection.AddFileRecordRecycleViewAdaptor;
import com.medical.mypockethealth.Adaptors.UserSection.EHRSection.SickNoteFileRecycleViewAdaptor;
import com.medical.mypockethealth.ApplicationBase.BaseActivity;
import com.medical.mypockethealth.Classes.DialogShower;
import com.medical.mypockethealth.Classes.EHRSection.FollowUpInformation.FollowUpInformation;
import com.medical.mypockethealth.Classes.EHRSection.SickNoteInformation.SickNoteInformation;
import com.medical.mypockethealth.Classes.NetworkSection.NetworkStatus;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.ClientActivity.ClientMainFrame;
import com.medical.mypockethealth.ClientFragments.EHRSection.EHROneFragment;
import com.medical.mypockethealth.ClientFragments.EHRSection.FollowUpSection.FollowUpFragment;
import com.medical.mypockethealth.R;
import com.medical.mypockethealth.Threads.UserSection.EHRSection.FollowUpFileAccessCaller;
import com.medical.mypockethealth.Threads.UserSection.EHRSection.SickNoteFileAccessCaller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.os.Environment.DIRECTORY_DOCUMENTS;


public class SickFragment extends Fragment implements AddFileRecordRecycleViewAdaptor.callBackFromAddFileRecordRecycleViewAdaptor,
        SickNoteFileAccessCaller.CallbackFromSickNoteFileAccessCaller,SickNoteFileRecycleViewAdaptor.callBackFromSickNoteFileRecycleViewAdaptor{

    private static final String TAG = "SickFragment";

    private ImageView loading,reload;
    private final Handler handler=new Handler();
    private String baseUrl;
    private SickNoteFileRecycleViewAdaptor sickNoteFileRecycleViewAdaptor;
    private Dialog loading_box;
    private SickNoteInformation sickNoteInformation;
    

    public SickFragment() {
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
        return inflater.inflate(R.layout.fragment_sick, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        establishViews(view);

        getSickNoteDetails();

        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                        new EHROneFragment()).addToBackStack(null).commit();
            }
        });

        RecyclerView medicine_recycle =view.findViewById(R.id.medicine_holder);

        medicine_recycle.setLayoutManager(new LinearLayoutManager(getContext()));


        sickNoteFileRecycleViewAdaptor=new SickNoteFileRecycleViewAdaptor(new ArrayList<>(), SickFragment.this);


        medicine_recycle.setAdapter(sickNoteFileRecycleViewAdaptor);
    }


    private void establishViews(View view)
    {
        loading=view.findViewById(R.id.loading);
        reload=view.findViewById(R.id.reload);
    }


    private void getSickNoteDetails()
    {
        if(!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED))
        {
            loading.setVisibility(View.VISIBLE);
            reload.setVisibility(View.GONE);



            new Thread(new SickNoteFileAccessCaller(2, SickFragment.this, ClientMainFrame.id)).start();

        }

        else
        {
            reload.setVisibility(View.VISIBLE);
            loading.setVisibility(View.GONE);

            DialogShower dialogShower=new DialogShower(R.layout.internet_error,getContext());
            dialogShower.showDialog();
        }
    }

    @Override
    public void deleteItem(int position) {
        
    }

    @Override
    public void innerFiles(String name) {

    }

    @Override
    public void confirmationSickNoteFileAccessCaller(ResponseInformation responseInformation) {

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
    public void informationSickNoteFileAccessCaller(List<SickNoteInformation> sickNoteInformation,String baseUrl) {

        this.baseUrl=baseUrl;

        handler.post(new Runnable() {
            @Override
            public void run() {

                loading.setVisibility(View.GONE);
                reload.setVisibility(View.GONE);

                sickNoteFileRecycleViewAdaptor.loadData(sickNoteInformation);
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

        this.sickNoteInformation=sickNoteInformation;

        int permission = ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                    , Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        else {

            openBottomSheet(sickNoteInformation);
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                openBottomSheet(sickNoteInformation);

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

    private void openBottomSheet(SickNoteInformation sickNoteInformation)
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

                    sickNoteInformation.setFolderName(folder_name.getText().toString());

                    startDownloading(sickNoteInformation);

                    forgotBottomSheet.dismiss();

                }
                else
                {
                    folder_name.setError("enter folder name");
                }

            }
        });

    }


    private void startDownloading(SickNoteInformation sickNoteInformation)
    {

        openLoadingBox();

        String fileName=sickNoteInformation.getId()+".pdf";

        File dir = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOCUMENTS).getPath()
                +"/"+sickNoteInformation.getFolderName());

        if (!dir.exists()) {
            dir.mkdir();

        }
        new Thread(new Runnable() {
            @Override
            public void run() {

                AndroidNetworking.download(baseUrl+"/"+sickNoteInformation.getId(),dir.toString(),fileName)
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
                        Toast.makeText(getContext(), "File saved to "+sickNoteInformation.getFolderName(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d(TAG, "onError: " +anError.getMessage());

                        loading_box.dismiss();

                        Toast.makeText(getContext(), "Unable to download file", Toast.LENGTH_SHORT).show();

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