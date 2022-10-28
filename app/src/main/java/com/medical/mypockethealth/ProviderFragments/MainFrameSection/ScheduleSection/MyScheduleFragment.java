package com.medical.mypockethealth.ProviderFragments.MainFrameSection.ScheduleSection;

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

import com.medical.mypockethealth.Adaptors.ProviderSection.SlotSection.SlotShowerRecycleViewAdaptor;
import com.medical.mypockethealth.ApplicationBase.BaseActivity;
import com.medical.mypockethealth.Classes.DialogShower;
import com.medical.mypockethealth.Classes.NetworkSection.NetworkStatus;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.Classes.SlotsSection.SlotInformation;
import com.medical.mypockethealth.R;
import com.medical.mypockethealth.Threads.ProviderSection.ScheduleSection.DeleteScheduleCaller;
import com.medical.mypockethealth.Threads.ProviderSection.ScheduleSection.MyScheduleFetcher;

import java.util.ArrayList;
import java.util.List;

public class MyScheduleFragment extends Fragment implements
        SlotShowerRecycleViewAdaptor.callBackFromSlotShowerRecycleViewAdaptor,MyScheduleFetcher.CallbackFromMyScheduleFetcher,
        DeleteScheduleCaller.CallbackFromDeleteScheduleCaller{

    private static final String TAG = "MyScheduleFragment";

    private ImageView loading,reload;
    private final Handler handler = new Handler();
    private SlotShowerRecycleViewAdaptor slotShowerRecycleViewAdaptor;


    public MyScheduleFragment() {
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
        return inflater.inflate(R.layout.fragment_my_schedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        establishViews(view);

        getMySlotsList();

        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                        new SlotFragment()).addToBackStack(null).commit();
            }
        });

        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED))
                {


                    loading.setVisibility(View.VISIBLE);
                    reload.setVisibility(View.GONE);

                    getMySlotsList();

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

        RecyclerView recyclerView=view.findViewById(R.id.slots_holder);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        slotShowerRecycleViewAdaptor =new SlotShowerRecycleViewAdaptor(new ArrayList<>(), MyScheduleFragment.this,getContext());

        recyclerView.setAdapter(slotShowerRecycleViewAdaptor);

    }

    private void getMySlotsList()
    {

        if (!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED)) {

            loading.setVisibility(View.VISIBLE);
            reload.setVisibility(View.GONE);


            new Thread(new MyScheduleFetcher(MyScheduleFragment.this)).start();

        } else {

            reload.setVisibility(View.VISIBLE);
            loading.setVisibility(View.GONE);

            DialogShower dialogShower=new DialogShower(R.layout.internet_error,getContext());
            dialogShower.showDialog();
        }



    }

    private void refreshData()
    {


    }
    private void establishViews(View view)
    {
        loading=view.findViewById(R.id.loading);
        reload=view.findViewById(R.id.reload);
    }

    @Override
    public void delete(SlotInformation slotInformation) {

        if (!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED)) {

            new Thread(new DeleteScheduleCaller(slotInformation,MyScheduleFragment.this)).start();

        } else {

            DialogShower dialogShower=new DialogShower(R.layout.internet_error,getContext());
            dialogShower.showDialog();
        }

    }

    @Override
    public void confirmation(ResponseInformation responseInformation) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                loading.setVisibility(View.GONE);
                reload.setVisibility(View.VISIBLE);

                if(responseInformation.getSuccess().equals("0"))
                {

                    Toast.makeText(getContext(),responseInformation.getMessage(),Toast.LENGTH_SHORT).show();


                    slotShowerRecycleViewAdaptor.loadData(new ArrayList<>());

                }
            }
        });
    }

    @Override
    public void information(List<SlotInformation> slotInformation) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                loading.setVisibility(View.GONE);

                slotShowerRecycleViewAdaptor.loadData(slotInformation);
            }
        });
    }

    @Override
    public void confirmationDeleteScheduleCaller(ResponseInformation responseInformation) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                if(responseInformation.getSuccess().equals(String.valueOf(ResponseInformation.fail_response_type)))
                {

                    Toast.makeText(getContext(),responseInformation.getMessage(),Toast.LENGTH_SHORT).show();

                }
                else
                {

                    Toast.makeText(getContext(),"Slot deleted Successfully",Toast.LENGTH_SHORT).show();

                    getMySlotsList();
                }



            }
        });

    }
}