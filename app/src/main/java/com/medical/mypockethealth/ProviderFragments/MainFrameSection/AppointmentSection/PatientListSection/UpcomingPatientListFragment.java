package com.medical.mypockethealth.ProviderFragments.MainFrameSection.AppointmentSection.PatientListSection;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.medical.mypockethealth.Adaptors.ProviderSection.AppointmentSection.PatientDetailsSection.UpcomingPatientDetailRecycleViewAdaptor;
import com.medical.mypockethealth.ApplicationBase.BaseActivity;
import com.medical.mypockethealth.Classes.ClientInformation.ClientInformation;
import com.medical.mypockethealth.Classes.DialogShower;
import com.medical.mypockethealth.Classes.NetworkSection.NetworkStatus;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.R;
import com.medical.mypockethealth.Threads.ProviderSection.BookingSection.UpcomingPatientListCaller;

import java.util.ArrayList;
import java.util.List;


public class UpcomingPatientListFragment extends Fragment implements UpcomingPatientListCaller.CallbackFromUpcomingPatientListCaller
{

    private ImageView loading,reload;
    private final Handler handler = new Handler();
    private UpcomingPatientDetailRecycleViewAdaptor upcomingPatientDetailRecycleViewAdaptor;

    public UpcomingPatientListFragment() {
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
        return inflater.inflate(R.layout.fragment_upcoming_patient_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        establishViews(view);

        getPatientList();

        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED))
                {


                    loading.setVisibility(View.VISIBLE);
                    reload.setVisibility(View.GONE);

                    getPatientList();

                }

                else
                {
                    reload.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.GONE);

                    DialogShower dialogShower=new DialogShower(R.layout.internet_error,getContext());
                    dialogShower.showDialog();
                }

            }
        });

        RecyclerView recyclerView=view.findViewById(R.id.upcoming_holder);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        upcomingPatientDetailRecycleViewAdaptor =new UpcomingPatientDetailRecycleViewAdaptor(new ArrayList<>(),getContext());

        recyclerView.setAdapter(upcomingPatientDetailRecycleViewAdaptor);
    }

    private void getPatientList()
    {
        if (!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED)) {

            loading.setVisibility(View.VISIBLE);
            reload.setVisibility(View.GONE);

            new Thread(new UpcomingPatientListCaller(UpcomingPatientListFragment.this)).start();

        } else {

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
    public void confirmation(ResponseInformation responseInformation) {
        handler.post(new Runnable() {
            @Override
            public void run() {

                loading.setVisibility(View.GONE);
                reload.setVisibility(View.VISIBLE);

                if(responseInformation.getSuccess().equals(ResponseInformation.fail_response_type))
                {

                    Toast.makeText(getContext(),responseInformation.getMessage(),Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    @Override
    public void information(List<ClientInformation> clientInformation) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                loading.setVisibility(View.GONE);

                upcomingPatientDetailRecycleViewAdaptor.loadData(clientInformation);
            }
        });


    }
    
}