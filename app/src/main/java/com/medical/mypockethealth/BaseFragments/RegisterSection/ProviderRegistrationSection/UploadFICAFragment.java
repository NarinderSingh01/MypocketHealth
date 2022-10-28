package com.medical.mypockethealth.BaseFragments.RegisterSection.ProviderRegistrationSection;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.medical.mypockethealth.R;

import java.io.File;


public class UploadFICAFragment extends Fragment {

    private static final String TAG = "UploadFICAFragment";

    private ImageView selected_image,upload_image;
    public static String selected_image_path;
    private CardView image_holder;
    public static boolean isImageSelected=false;

    public UploadFICAFragment() {
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
        return inflater.inflate(R.layout.fragment_upload_f_i_c_a, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        establishViews(view);

        image_holder.setVisibility(View.GONE);

        setDefault();


        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isImageSelected=false;

                requireActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.frame_holder,new SignUpAsProviderFragment()).addToBackStack(null).commit();
            }
        });


        upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int permission= ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

                if(permission!= PackageManager.PERMISSION_GRANTED)
                {

                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                            ,Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }
                else
                {
                    Intent intent=new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");

                    startActivityForResult(intent,1);


                }


            }
        });


        view.findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(selected_image_path!=null)
                {
                    isImageSelected=true;

                    requireActivity().getSupportFragmentManager().beginTransaction().
                            replace(R.id.frame_holder,new SignUpAsProviderFragment()).addToBackStack(null).commit();
                }

                else
                {

                    Toast.makeText(getContext(),"kindly upload your FICA Docs",Toast.LENGTH_SHORT).show();

                }


            }
        });

    }


    private void setDefault()
    {
        isImageSelected=false;
        selected_image_path=null;
    }

    private void establishViews(View view)
    {

        selected_image=view.findViewById(R.id.uploaded_image);
        upload_image=view.findViewById(R.id.upload_image);
        image_holder=view.findViewById(R.id.image_holder);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {



                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");

                startActivityForResult(intent, 1);


            } else {

                if(shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE))
                {

                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                            ,Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

                }
                else
                {
                    Intent intent=new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri=Uri.fromParts("package", requireContext().getPackageName(),null);
                    intent.setData(uri);
                    requireContext().startActivity(intent);

                }
            }
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == Activity.RESULT_OK)
        {
            isImageSelected=true;

            assert data != null;
            Uri image=data.getData();

            ContentResolver contentResolver= requireContext().getContentResolver();
            Cursor cursor=contentResolver.query(image,null,null,null,null);

            if(cursor!=null)
            {
                while (cursor.moveToNext())
                {

                    File file=new File(cursor.getString(cursor.getColumnIndex("_data")));

                    this.selected_image_path=file.toString();

                    selected_image.setImageURI(image);

                    image_holder.setVisibility(View.VISIBLE);


                }

                cursor.close();

            }

        }

    }

}