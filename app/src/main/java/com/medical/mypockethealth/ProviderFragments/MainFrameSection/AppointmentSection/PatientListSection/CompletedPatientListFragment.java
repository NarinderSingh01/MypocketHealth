package com.medical.mypockethealth.ProviderFragments.MainFrameSection.AppointmentSection.PatientListSection;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.medical.mypockethealth.Adaptors.ProviderSection.AppointmentSection.PatientDetailsSection.CompletedPatientDetailsRecycleViewAdaptor;
import com.medical.mypockethealth.ApplicationBase.BaseActivity;
import com.medical.mypockethealth.Classes.ClientInformation.ClientInformation;
import com.medical.mypockethealth.Classes.DialogShower;
import com.medical.mypockethealth.Classes.NetworkSection.NetworkStatus;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.R;
import com.medical.mypockethealth.Threads.ProviderSection.BookingSection.CompletedPatientDetailsFetcher;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class CompletedPatientListFragment extends Fragment implements CompletedPatientDetailsFetcher.CallbackFromCompletedPatientDetailsFetcher,
        DatePickerDialog.OnDateSetListener,CompletedPatientDetailsRecycleViewAdaptor.CallBackFromCompletedPatientDetailsRecycleViewAdaptor{

    private CardView service_holder;
    private ImageView loading;
    private BottomSheetDialog bottomSheetDialog;
    private final Calendar calendar=Calendar.getInstance();
    private boolean isStartDateClicked=false,isEndDateClicked=false,isValidStartDate=false,isValidEndDate=false,isValidDateFormat=false;
    private String startDate,endDate;
    private View view;
    private final Handler handler=new Handler();
    private CompletedPatientDetailsRecycleViewAdaptor completedPatientDetailsRecycleViewAdaptor;
    private int startYear=-1,startMonth=-1,startDay=-1,endYear=-1,endMonth=-1,endDay=-1;

    public CompletedPatientListFragment() {
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
        return inflater.inflate(R.layout.fragment_completed_patient_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view=view;

        establishViews();

        loading.setVisibility(View.GONE);

        RecyclerView recyclerView=view.findViewById(R.id.complete_holder);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        completedPatientDetailsRecycleViewAdaptor=new CompletedPatientDetailsRecycleViewAdaptor(new ArrayList<>(),getContext(),CompletedPatientListFragment.this);

        recyclerView.setAdapter(completedPatientDetailsRecycleViewAdaptor);

        view.findViewById(R.id.pick_start_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isStartDateClicked=true;

                DialogFragment dialogFragment=new com.medical.mypockethealth.Classes.DateAndTimeForFragmentSection.DefaultDatePickerForFragment();
                dialogFragment.setTargetFragment(CompletedPatientListFragment.this,0);
                assert getFragmentManager() != null;
                dialogFragment.show(getFragmentManager(),"time_picker");
            }
        });

        view.findViewById(R.id.pick_end_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isEndDateClicked=true;

                DialogFragment dialogFragment=new com.medical.mypockethealth.Classes.DateAndTimeForFragmentSection.DefaultDatePickerForFragment();
                dialogFragment.setTargetFragment(CompletedPatientListFragment.this,0);
                assert getFragmentManager() != null;
                dialogFragment.show(getFragmentManager(),"time_picker");
            }
        });


        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(startDate==null)
                {
                    Toast.makeText(getContext(),"Please select Start Date",Toast.LENGTH_SHORT).show();
                }

                else if(endDate==null)
                {
                    Toast.makeText(getContext(),"Please select End Date",Toast.LENGTH_SHORT).show();

                }
                else
                {


                    if(!isValidStartDate)
                    {
                        dialogCaller();
                    }
                    else if(!isValidEndDate)
                    {
                        dialogCaller();

                    }

                    else if(!isValidDateFormat)
                    {

                        Dialog dateDialog=new Dialog(getContext());
                        dateDialog.setContentView(R.layout.date_less_dialog);
                        dateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dateDialog.setCanceledOnTouchOutside(false);
                        Window window=dateDialog.getWindow();
                        window.setGravity(Gravity.CENTER);
                        dateDialog.show();

                        dateDialog.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dateDialog.dismiss();
                            }
                        });
                    }

                    else
                    {


                        if(!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED))
                        {

                            service_holder.setVisibility(View.GONE);
                            loading.setVisibility(View.VISIBLE);

                            new Thread(new CompletedPatientDetailsFetcher(startDate,endDate,
                                    CompletedPatientListFragment.this)).start();

                        }

                        else
                        {
                            DialogShower dialogShower=new DialogShower(R.layout.internet_error,getContext());
                            dialogShower.showDialog();
                        }

                    }
                }

            }
        });
    }


    private void dialogCaller()
    {
        Dialog dateDialog=new Dialog(getContext());
        dateDialog.setContentView(R.layout.future_date_warning);
        dateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dateDialog.setCanceledOnTouchOutside(false);
        Window window=dateDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        dateDialog.show();

        dateDialog.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dateDialog.dismiss();
            }
        });

    }

    private void establishViews()
    {

        loading=view.findViewById(R.id.loading);
        service_holder=view.findViewById(R.id.service_period_details_holder);

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {


        if(isStartDateClicked)
        {
            isStartDateClicked=false;
            calendar.set(Calendar.MONTH,month);
            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.DAY_OF_MONTH,day);

            String month1=String.valueOf(month+1);
            String day1=String.valueOf(day);

            if(month1.length()==1)
            {
                month1="0"+month1;

            }
            if(day1.length()==1)
            {
                day1="0"+day1;
            }

            startDate=year+"-"+month1+"-"+day1;

            ((TextView)this.view.findViewById(R.id.select_start_date)).setText(startDate);


            dateChecker(year,month,day,0);


        }
        else if(isEndDateClicked)
        {
            isEndDateClicked=false;
            calendar.set(Calendar.MONTH,month);
            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.DAY_OF_MONTH,day);

            String month1=String.valueOf(month+1);
            String day1=String.valueOf(day);

            if(month1.length()==1)
            {
                month1="0"+month1;

            }
            if(day1.length()==1)
            {
                day1="0"+day1;
            }

            endDate=year+"-"+month1+"-"+day1;

            ((TextView)this.view.findViewById(R.id.end_date)).setText(endDate);

            dateChecker(year,month,day,1);
        }


    }

    private void dateChecker(int year,int month,int date,int mode)
    {

        Calendar calendar=Calendar.getInstance();
        int year1=calendar.get(Calendar.YEAR);
        int month1=calendar.get(Calendar.MONTH);
        int day=calendar.get(Calendar.DAY_OF_MONTH);

        month1+=1;
        month+=1;


        if(mode==0)
        {

            startYear=year;
            startMonth=month;
            startDay=date;

            if(year>year1)
            {

                isValidStartDate=false;
            }

            else if(year<year1 && month>month1)
            {
                isValidStartDate=true;
            }
            else if(month>month1)
            {

                isValidStartDate=false;

            }

            else if(month==month1)
            {
                isValidStartDate= date <= day;
            }
            else
            {
                isValidStartDate=true;
            }
        }

        else if(mode==1)
        {

            endYear=year;
            endMonth=month;
            endDay=date;

            if(year>year1)
            {

                isValidEndDate=false;
            }

            else if(year<year1 && month>month1)
            {
                isValidEndDate=true;
            }
            else if(month>month1)
            {

                isValidEndDate=false;

            }

            else if(month==month1)
            {
                isValidEndDate= date <= day;
            }
            else
            {
                isValidEndDate=true;
            }
        }


        if(isValidStartDate && isValidEndDate)
        {


            if(startYear>endYear)
            {

                isValidDateFormat=false;
            }

            else if(startYear<endYear && startMonth>endMonth)
            {

                isValidDateFormat=true;
            }
            else if(startMonth>endMonth)
            {


                isValidDateFormat=false;

            }

            else if(startMonth==endMonth)
            {

                isValidDateFormat= startDay <= endDay;
            }
            else
            {


                isValidDateFormat=true;
            }


        }
    }


    @Override
    public void confirmation(ResponseInformation responseInformation) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                if(responseInformation.getSuccess().equals(ResponseInformation.fail_response_type))
                {
                    service_holder.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.GONE);

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

                service_holder.setVisibility(View.GONE);
                loading.setVisibility(View.GONE);

                completedPatientDetailsRecycleViewAdaptor.loadData(clientInformation);
            }
        });

    }

    @Override
    public void previousRecord(ClientInformation clientInformation) {
        
        Dialog previousRecord=new Dialog(getContext());
        previousRecord.setContentView(R.layout.previous_record_layout);
        previousRecord.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        previousRecord.setCanceledOnTouchOutside(false);
        Window window=previousRecord.getWindow();
        window.setGravity(Gravity.CENTER);
        previousRecord.show();

        ImageView imageView=previousRecord.findViewById(R.id.prescription);

        Picasso.with(getContext()).load(Uri.parse(clientInformation.getPrescriptionImage())).error(R.drawable.profile).into(imageView);
    }
}