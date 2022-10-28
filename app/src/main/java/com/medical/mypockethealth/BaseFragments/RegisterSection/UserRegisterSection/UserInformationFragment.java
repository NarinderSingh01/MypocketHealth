package com.medical.mypockethealth.BaseFragments.RegisterSection.UserRegisterSection;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.medical.mypockethealth.Adaptors.UserSection.RegistrationSection.AddAllergiesRecycleViewAdaptor;
import com.medical.mypockethealth.Adaptors.UserSection.RegistrationSection.AddMedicineListRecycleViewAdaptor;
import com.medical.mypockethealth.Adaptors.UserSection.RegistrationSection.AddMorbidityRecycleViewAdaptor;
import com.medical.mypockethealth.ApplicationBase.BaseActivity;
import com.medical.mypockethealth.BaseFragments.CongratulationsFragment;
import com.medical.mypockethealth.Classes.ClientInformation.ClientInformation;
import com.medical.mypockethealth.Classes.DialogShower;
import com.medical.mypockethealth.Classes.MedicalAidInformation.MedicalAidInformation;
import com.medical.mypockethealth.Classes.NetworkSection.NetworkStatus;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.Classes.URLBuilder;
import com.medical.mypockethealth.ClientActivity.ClientMainFrame;
import com.medical.mypockethealth.R;
import com.medical.mypockethealth.Threads.BaseThreads.UserSection.ClientInformationCaller;
import com.medical.mypockethealth.Threads.BaseThreads.UserSection.MorbidityCaller;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UserInformationFragment extends Fragment implements MorbidityCaller.CallbackFromMorbidityCaller,
        AddMedicineListRecycleViewAdaptor.callBackFromAddMedicineListRecycleViewAdaptor,ClientInformationCaller.CallbackFromClientInformationCaller,
        AddMorbidityRecycleViewAdaptor.callBackFromAddMorbidityRecycleViewAdaptor,AddAllergiesRecycleViewAdaptor.callBackFromAddAllergiesRecycleViewAdaptor{

    private static final String TAG = "UserInformationFragment";


    private LinearLayout Allergies_layout;
    private CardView edit_morbidity_layout;
    private EditText edit_morbidity;
    private AutoCompleteTextView morbidity_layout;
    private RadioButton yes,no;
    private ImageView upload_image,loading,cancel,choose_from_gallery,choose_from_camera;
    private Button submit;
    private final List<String> imagesEncodedList=new ArrayList<>();
    private Dialog pop_up_box;
    private String selected_image_path=null;
    private boolean yes_clicked=false;
    private boolean no_clicked=false;
    private TextView loadingProgress;
    private AddMedicineListRecycleViewAdaptor medicineListRecycleViewAdaptor;
    private AddMorbidityRecycleViewAdaptor addMorbidityRecycleViewAdaptor;
    private AddAllergiesRecycleViewAdaptor addAllergiesRecycleViewAdaptor;
    private final Handler handler = new Handler();
    private final List<String> medicine=new ArrayList<>();
    private final List<String> morbidity = new ArrayList<>();
    private final List<String> updatedMorbidityList = new ArrayList<>();
    private final List<String> allergiesList = new ArrayList<>();
    private byte[] selected_image;
    private Dialog loading_box;




    public enum IdentityType {
        yes,no,notSelected
    }

    @Override
    public void confirmationCallbackFromMorbidityCaller(ResponseInformation responseInformation) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                if(responseInformation.getSuccess().equals(ResponseInformation.fail_response_type))
                {

                    Toast.makeText(getContext(),responseInformation.getMessage(),Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    @Override
    public void informationCallbackFromMorbidityCaller(List<MedicalAidInformation> medicalAidInformationList) {


        for (MedicalAidInformation medicalAidInformation1 : medicalAidInformationList) {

            morbidity.add(medicalAidInformation1.getTitle());
        }


        handler.post(new Runnable() {
            @Override
            public void run() {

                uploadData();

            }
        });


    }

    private void uploadData()
    {
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(getContext(),R.layout.drop_down_layout,R.id.my_text,
                morbidity);

        morbidity_layout.setAdapter(arrayAdapter);
    }

    @Override
    public void deleteItemAddAllergiesRecycleViewAdaptor(int position) {

        allergiesList.remove(position);
        addAllergiesRecycleViewAdaptor.loadData(allergiesList);
    }

    @Override
    public void innerFilesAddAllergiesRecycleViewAdaptor(String name) {

    }


    @Override
    public void deleteItemAddMorbidityRecycleViewAdaptor(int position) {

        updatedMorbidityList.remove(position);
        addMorbidityRecycleViewAdaptor.loadData(updatedMorbidityList);

        if(updatedMorbidityList.size()==0)
        {
            morbidity_layout.setText(R.string.co_morbidities);
            edit_morbidity_layout.setVisibility(View.GONE);
            uploadData();
        }

    }

    @Override
    public void innerFilesAddMorbidityRecycleViewAdaptor(String name) {

    }

    @Override
    public void deleteItem(int position) {

        medicine.remove(position);
        medicineListRecycleViewAdaptor.loadData(medicine);

    }

    @Override
    public void innerFiles(String name) {


    }

    @Override
    public void confirmation(ResponseInformation responseInformation) {

        loading.setVisibility(View.GONE);
        submit.setVisibility(View.VISIBLE);


        handler.post(new Runnable() {
            @Override
            public void run() {

                if (responseInformation.getSuccess().equals(ResponseInformation.fail_response_type)) {

                    Toast.makeText(getContext(), responseInformation.getMessage(), Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    @Override
    public void information(ClientInformation clientInformation) {

        updateFile(clientInformation);

        handler.post(new Runnable() {
            @Override
            public void run() {

                submit.setText(R.string.submit);
                submit.setEnabled(true);

                startActivity(new Intent(getContext(), ClientMainFrame.class));

            }
        });

    }

    private void updateFile(ClientInformation clientInformation)
    {
        SharedPreferences sharedPreferences= requireContext().getSharedPreferences(ClientMainFrame.client_information_file, Context.MODE_PRIVATE);
        Gson gson = new Gson();

        SharedPreferences.Editor editor1=sharedPreferences.edit();

        editor1.clear();
        editor1.apply();


        SharedPreferences sharedPreferences3= requireContext().getSharedPreferences(ClientMainFrame.client_information_file, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor2=sharedPreferences3.edit();

        String json1 = gson.toJson(clientInformation);

        editor2.putString(ClientMainFrame.client_information_key,json1);

        editor2.apply();

    }


    public UserInformationFragment() {
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
        return inflater.inflate(R.layout.fragment_user_information, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        establishViews(view);

        getMorbidityList();

        disableAllergiesNumberFiled();

        RecyclerView medicine_recycle,morbidity_holder,allergies_holder;
        medicine_recycle= view.findViewById(R.id.medicine_holder);
        morbidity_holder= view.findViewById(R.id.mobility_holder);
        allergies_holder= view.findViewById(R.id.allergies_holder);

        medicine_recycle.setLayoutManager(new LinearLayoutManager(getContext()));
        morbidity_holder.setLayoutManager(new LinearLayoutManager(getContext()));
        allergies_holder.setLayoutManager(new LinearLayoutManager(getContext()));

        medicineListRecycleViewAdaptor=new AddMedicineListRecycleViewAdaptor(new ArrayList<>(),
                UserInformationFragment.this);
        addMorbidityRecycleViewAdaptor=new AddMorbidityRecycleViewAdaptor(new ArrayList<>(),
                UserInformationFragment.this);
        addAllergiesRecycleViewAdaptor=new AddAllergiesRecycleViewAdaptor(new ArrayList<>(),
                UserInformationFragment.this);


        medicine_recycle.setAdapter(medicineListRecycleViewAdaptor);
        morbidity_holder.setAdapter(addMorbidityRecycleViewAdaptor);
        allergies_holder.setAdapter(addAllergiesRecycleViewAdaptor);



        view.findViewById(R.id.morbidity_tick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edit_morbidity.getText().toString().trim().length()!=0)
                {
                    updatedMorbidityList.add(edit_morbidity.getText().toString());
                    addMorbidityRecycleViewAdaptor.loadData(updatedMorbidityList);

                }


            }
        });

        morbidity_layout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if(!morbidity.get(position).equals(getString(R.string.other)))
                {
                    Log.d(TAG, "onItemClick: not other");
                    edit_morbidity_layout.setVisibility(View.GONE);
                    updatedMorbidityList.add(morbidity.get(position));
                    addMorbidityRecycleViewAdaptor.loadData(updatedMorbidityList);
                }
                else
                {
                    Log.d(TAG, "onItemClick: other");

                    edit_morbidity_layout.setVisibility(View.VISIBLE);
                }

            }
        });

        view.findViewById(R.id.skip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getContext(), ClientMainFrame.class));
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activateField(SignUpAsUserFragment.IdentityType.yes);

                enableAllergiesNumberFiled();

            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activateField(SignUpAsUserFragment.IdentityType.no);

                disableAllergiesNumberFiled();

            }
        });

        view.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addFileBox();
            }
        });

        view.findViewById(R.id.allergies).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addAllergies();
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                upload_image.setImageResource(R.drawable.ic_plus__1_);

                cancel.setVisibility(View.GONE);

                selected_image_path=null;
                selected_image=null;

            }
        });

        view.findViewById(R.id.upload_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetHandler();

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClientInformation clientInformation=new ClientInformation();

                clientInformation.setUserId(getUserId());

                if(!morbidity_layout.getText().toString().equals("Co-morbidities"))
                {

                    clientInformation.setMorbidityList(updatedMorbidityList);

                }

                checkFillDetails(clientInformation);
            }
        });
    }


    private void bottomSheetHandler()
    {
        BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(requireContext(),R.style.BottomSheetDialogTheme);

        View bottom_view= LayoutInflater.from(requireContext()).inflate(R.layout.choose_camera_layout
                ,
                (RelativeLayout) bottomSheetDialog.findViewById(R.id.relative_layout));

        bottomSheetDialog.setContentView(bottom_view);

        bottomSheetDialog.show();

        choose_from_gallery=bottomSheetDialog.findViewById(R.id.choose_from_gallery);
        choose_from_camera=bottomSheetDialog.findViewById(R.id.choose_from_camera);



        choose_from_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetDialog.dismiss();

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


        choose_from_camera.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                bottomSheetDialog.dismiss();

                int permission= ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA);

                if(permission!= PackageManager.PERMISSION_GRANTED)
                {

                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                            ,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},2);
                }
                else
                {
                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 2);


                }


            }
        });



    }

    private void checkFillDetails(ClientInformation clientInformation)
    {

        clientInformation.setMedicineList(medicine);

        if(!yes_clicked && !no_clicked)
        {
            Toast.makeText(getContext(),"Do you have allergies",Toast.LENGTH_SHORT).show();
        }

        else
        {
            if(yes_clicked)
            {

                if(allergiesList.size()!=0)
                {

                    clientInformation.setAllergiesList(allergiesList);

                    saveInformationCaller(clientInformation);
                }
                else
                {
                    Toast.makeText(getContext(),"Add allergies",Toast.LENGTH_SHORT).show();
                }



            }
            else
            {
                clientInformation.setAllergies(SignUpAsUserFragment.IdentityType.notSelected.toString());

                saveInformationCaller(clientInformation);

            }
        }

    }

    private String getUserId()
    {
        SharedPreferences sharedPreferences= requireContext().getSharedPreferences(ClientMainFrame.client_information_file, Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String data = sharedPreferences.getString(ClientMainFrame.client_information_key, "");
        ClientInformation clientInformation = gson.fromJson(data, ClientInformation.class);

        return clientInformation.getId();
    }


    private void saveInformationCaller(ClientInformation clientInformation)
    {

        if (!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED)) {

            if(selected_image_path!=null)
            {

                openLoadingBox();
                getGalleryImageLink(selected_image_path,clientInformation);


            }
            else if(selected_image!=null)
            {

                openLoadingBox();
                getCameraImageLink(selected_image,clientInformation);
            }

            else
            {
                clientInformation.setScriptImageStatus(false);
                uploadPatientData(clientInformation);
            }


        } else {

            DialogShower dialogShower=new DialogShower(R.layout.internet_error,getContext());
            dialogShower.showDialog();
        }
    }


    public void uploadPatientData(ClientInformation clientInformation)
    {
        loading.setVisibility(View.VISIBLE);
        submit.setVisibility(View.GONE);

        new Thread(new ClientInformationCaller(UserInformationFragment.this,clientInformation)).start();
    }

    private void getCameraImageLink(byte[] image,ClientInformation clientInformation)
    {

        StorageReference storageReference = FirebaseStorage.getInstance().getReference(clientInformation.getPhoneNumber() + "/");

        storageReference.child(URLBuilder.FirebaseDataNodes.myScript).putBytes(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                    @Override
                    public void onSuccess(Uri uri) {

                        clientInformation.setScriptImageStatus(true);

                        clientInformation.setScriptImage(uri.toString());

                        loading_box.dismiss();

                        uploadPatientData(clientInformation);

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.d(TAG, "onFailure: error: " + e.getMessage());


            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                Log.d(TAG, "onProgress: " + (snapshot.getBytesTransferred() / snapshot.getTotalByteCount()) * 100);


                long value = (snapshot.getBytesTransferred() / snapshot.getTotalByteCount()) * 100;
                int progress_status = (int) value;

                String status = "updating profile.. " + progress_status + "%";
                loadingProgress.setText(status);


            }
        });


    }

    private void getGalleryImageLink(String image,ClientInformation clientInformation)
    {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(clientInformation.getPhoneNumber() + "/");

        storageReference.child(URLBuilder.FirebaseDataNodes.myScript).putFile(Uri.parse(image)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                    @Override
                    public void onSuccess(Uri uri) {

                        clientInformation.setScriptImageStatus(true);

                        clientInformation.setScriptImage(uri.toString());

                        loading_box.dismiss();

                        uploadPatientData(clientInformation);

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.d(TAG, "onFailure: error: " + e.getMessage());


            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                Log.d(TAG, "onProgress: " + (snapshot.getBytesTransferred() / snapshot.getTotalByteCount()) * 100);

                long value = (snapshot.getBytesTransferred() / snapshot.getTotalByteCount()) * 100;
                int progress_status = (int) value;

                String status = "updating profile.. " + progress_status + "%";
                loadingProgress.setText(status);


            }
        });


    }


    private void openLoadingBox() {

        loading_box = new Dialog(getContext());
        loading_box.setContentView(R.layout.user_loading_layout);
        loading_box.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading_box.setCanceledOnTouchOutside(false);
        loading_box.setCancelable(false);
        Window window = loading_box.getWindow();
        window.setGravity(Gravity.CENTER);
        loading_box.show();
        loadingProgress = loading_box.findViewById(R.id.loading);


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

        if (requestCode == 2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 2);


            } else {

                if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA))
                {

                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                            ,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},2);
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
            boolean isImageSelected = true;

            imagesEncodedList.clear();

            assert data != null;
            Uri image=data.getData();

            this.selected_image_path=image.toString();

            selected_image=null;

            imagesEncodedList.add(selected_image_path);

            upload_image.setImageURI(image);

            cancel.setVisibility(View.VISIBLE);


        }


        if(requestCode == 2 && resultCode == Activity.RESULT_OK)
        {

            assert data != null;

            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            upload_image.setImageBitmap(thumbnail);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            selected_image = byteArrayOutputStream.toByteArray();
            selected_image_path=null;

            cancel.setVisibility(View.VISIBLE);


        }

    }



    private void addAllergies()
    {
        pop_up_box =new Dialog(getContext());
        pop_up_box.setContentView(R.layout.add_allergies_layout);
        pop_up_box.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop_up_box.setCanceledOnTouchOutside(true);
        Window window= pop_up_box.getWindow();
        window.setGravity(Gravity.CENTER);
        pop_up_box.show();

        EditText editText= pop_up_box.findViewById(R.id.about_text);

        pop_up_box.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(editText.getText().toString().trim().length()==0)
                {
                    editText.setError("kindly add your allergy name");
                }

                else
                {

                    editText.setError(null);

                    pop_up_box.dismiss();

                    addAllergiesData(editText.getText().toString());

                }

            }
        });
    }

    private void addFileBox()
    {
        pop_up_box =new Dialog(getContext());
        pop_up_box.setContentView(R.layout.add_medicine_layout);
        pop_up_box.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop_up_box.setCanceledOnTouchOutside(true);
        Window window= pop_up_box.getWindow();
        window.setGravity(Gravity.CENTER);
        pop_up_box.show();

        EditText editText= pop_up_box.findViewById(R.id.about_text);

        pop_up_box.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(editText.getText().toString().trim().length()==0)
                {
                    editText.setError("kindly add your file name");
                }

                else
                {

                    editText.setError(null);

                    pop_up_box.dismiss();

                    addFileData(editText.getText().toString());

                }

            }
        });

    }


    private void addAllergiesData(String name)
    {

        allergiesList.add(name);
        addAllergiesRecycleViewAdaptor.loadData(allergiesList);


    }

    private void addFileData(String name)
    {

        medicine.add(name);
        medicineListRecycleViewAdaptor.loadData(medicine);


    }

    private void getMorbidityList()
    {

        if (!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED)) {


            new Thread(new MorbidityCaller(UserInformationFragment.this)).start();

        } else {
            DialogShower dialogShower=new DialogShower(R.layout.internet_error,getContext());
            dialogShower.showDialog();
        }

    }
    private void establishViews(View view)
    {
        Allergies_layout=view.findViewById(R.id.Allergies_layout);
        yes=view.findViewById(R.id.yes);
        no=view.findViewById(R.id.no);
        morbidity_layout=view.findViewById(R.id.morbidities_layout);
        upload_image=view.findViewById(R.id.upload_image);
        submit=view.findViewById(R.id.login);
        loading=view.findViewById(R.id.loading);
        cancel=view.findViewById(R.id.cancel);
        edit_morbidity_layout=view.findViewById(R.id.edit_morbidity_layout);
        edit_morbidity=view.findViewById(R.id.edit_morbidity);

    }

    private void activateField(SignUpAsUserFragment.IdentityType identityType) {
        switch (identityType) {

            case yes:

                yes.setChecked(true);
                no.setChecked(false);
                yes_clicked = true;
                no_clicked = false;

                break;

            case no:
                yes.setChecked(false);
                no.setChecked(true);
                yes_clicked = false;
                no_clicked = true;

                break;

        }
    }

    private void enableAllergiesNumberFiled()
    {

        Allergies_layout.setVisibility(View.VISIBLE);


    }

    private void disableAllergiesNumberFiled()
    {

        Allergies_layout.setVisibility(View.GONE);


    }
}