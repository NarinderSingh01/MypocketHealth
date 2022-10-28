package com.medical.mypockethealth.ClientFragments.BottomNavigationSection.SettingsSection.ChatSection;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.medical.mypockethealth.Adaptors.ChatSection.ChatShowerRecycleViewAdaptor;
import com.medical.mypockethealth.ApplicationBase.BaseActivity;
import com.medical.mypockethealth.Classes.ChatSection.ChatInformation;
import com.medical.mypockethealth.Classes.DialogShower;
import com.medical.mypockethealth.Classes.NetworkSection.NetworkStatus;
import com.medical.mypockethealth.Classes.URLBuilder;
import com.medical.mypockethealth.ClientActivity.ClientMainFrame;
import com.medical.mypockethealth.ClientFragments.BottomNavigationSection.SettingsSection.SettingFragment;
import com.medical.mypockethealth.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PatientChatFragment extends Fragment {

    private EditText message;
    private FloatingActionButton floatingActionButton;
    private final List<ChatInformation> chatInformationArrayList=new ArrayList<>();
    private ImageView loading,reload;
    private RecyclerView recyclerView;
    private ChatShowerRecycleViewAdaptor chatShowerRecycleViewAdaptor;

    public PatientChatFragment() {
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
        return inflater.inflate(R.layout.fragment_patient_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        disableBottomNavigation();

        establishView(view);


        if(!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED))
        {


            loading.setVisibility(View.VISIBLE);
            reload.setVisibility(View.GONE);

            receiveData();

        }

        else
        {
            loading.setVisibility(View.GONE);
            reload.setVisibility(View.VISIBLE);

            DialogShower dialogShower=new DialogShower(R.layout.internet_error,getContext());
            dialogShower.showDialog();
        }



        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED))
                {


                    loading.setVisibility(View.VISIBLE);
                    reload.setVisibility(View.GONE);
                    receiveData();

                }

                else
                {
                    loading.setVisibility(View.GONE);
                    reload.setVisibility(View.VISIBLE);

                    DialogShower dialogShower=new DialogShower(R.layout.internet_error,getContext());
                    dialogShower.showDialog();
                }

            }
        });

        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_frame_layout, new SettingFragment()).addToBackStack(null).commit();

                enableBottomNavigation();
            }
        });

        recyclerView = view.findViewById(R.id.chat_holder);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        chatShowerRecycleViewAdaptor=new ChatShowerRecycleViewAdaptor(new ArrayList<>(),getContext());

        recyclerView.setAdapter(chatShowerRecycleViewAdaptor);



        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(message.getText().length()!=0)
                {

                    if(!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED))
                    {

                        if(message.getText().toString().trim().length()!=0)
                        {
                            sendMessage(message.getText().toString());
                            message.getText().clear();
                        }

                    }

                    else
                    {
                        DialogShower dialogShower=new DialogShower(R.layout.internet_error,getContext());
                        dialogShower.showDialog();
                    }

                }

            }
        });

    }


    private void sendMessage(String message)
    {

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(URLBuilder.FirebaseDataNodes.panelChat);

        String random_key_generator=databaseReference.push().getKey();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date date = new Date();

        ChatInformation chatInformation=new ChatInformation(message,"1",formatter.format(date),random_key_generator);

        assert random_key_generator != null;

        databaseReference.child(ClientMainFrame.id).child(random_key_generator).setValue(chatInformation).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {


                receiveData();

                updateLastMessage(chatInformation);

            }
        });

    }



    private void updateLastMessage(ChatInformation chatInformation)
    {

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(URLBuilder.FirebaseDataNodes.lastMessage).child(ClientMainFrame.id);

        databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                createLastMessage(chatInformation);
            }
        });


    }

    private void createLastMessage(ChatInformation chatInformation)
    {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(URLBuilder.FirebaseDataNodes.lastMessage);

        databaseReference.child(ClientMainFrame.id).setValue(chatInformation).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {


            }
        });
    }

    private void receiveData()
    {

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(URLBuilder.FirebaseDataNodes.panelChat).child(ClientMainFrame.id);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if(snapshot.exists())
                {
                    loading.setVisibility(View.GONE);
                    reload.setVisibility(View.GONE);

                    chatInformationArrayList.clear();

                    for (DataSnapshot child : snapshot.getChildren()) {

                        ChatInformation chatInformation=child.getValue(ChatInformation.class);

                        chatInformationArrayList.add(chatInformation);
                    }

                    chatShowerRecycleViewAdaptor.setData(chatInformationArrayList);
                    recyclerView.scrollToPosition(chatInformationArrayList.size() - 1);
                }
                else
                {

                    loading.setVisibility(View.GONE);
                    reload.setVisibility(View.VISIBLE);

                    Toast.makeText(getContext(),"Chat not found",Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void establishView(View view)
    {
        message=view.findViewById(R.id.editComment);
        floatingActionButton=view.findViewById(R.id.floatingActionButton3);
        loading=view.findViewById(R.id.loading);
        reload=view.findViewById(R.id.reload);
    }

    private void enableBottomNavigation()
    {

        View view1 = requireActivity().findViewById(R.id.meowBottomNavigation);

        view1.setVisibility(View.VISIBLE);

    }

    private void disableBottomNavigation()
    {

        View view1 = requireActivity().findViewById(R.id.meowBottomNavigation);

        view1.setVisibility(View.GONE);

    }
}
