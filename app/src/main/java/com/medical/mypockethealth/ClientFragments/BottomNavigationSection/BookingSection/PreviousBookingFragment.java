
package com.medical.mypockethealth.ClientFragments.BottomNavigationSection.BookingSection;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.medical.mypockethealth.Adaptors.UserSection.BookingSection.BookedDoctorsRecycleViewAdaptor;
import com.medical.mypockethealth.ApplicationBase.BaseActivity;
import com.medical.mypockethealth.Classes.BookDoctorInformation.BookDoctorInformation;
import com.medical.mypockethealth.Classes.DialogShower;
import com.medical.mypockethealth.Classes.NetworkSection.NetworkStatus;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.R;
import com.medical.mypockethealth.Threads.UserSection.BookingSection.CancelBookingCaller;
import com.medical.mypockethealth.Threads.UserSection.BookingSection.PreviousBookingDataFetcher;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class PreviousBookingFragment extends Fragment implements BookedDoctorsRecycleViewAdaptor.callBackFromBookedDoctorsRecycleViewAdaptor
        , DatePickerDialog.OnDateSetListener,PreviousBookingDataFetcher.CallbackFromPreviousBookingDataFetcher,CancelBookingCaller.CallBackFromCancelBookingCaller{

    private static final String TAG = "PreviousBookingFragment";

    private CardView service_holder;
    private ImageView loading;
    private  BottomSheetDialog bottomSheetDialog;
    private final Calendar calendar=Calendar.getInstance();
    private boolean isStartDateClicked=false,isEndDateClicked=false,isValidStartDate=false,isValidEndDate=false,isValidDateFormat=false;
    private String startDate,endDate;
    private View view;
    private final Handler handler=new Handler();

    private BookedDoctorsRecycleViewAdaptor doctorsRecycleViewAdaptor;

    private int startYear=-1,startMonth=-1,startDay=-1,endYear=-1,endMonth=-1,endDay=-1;

    public PreviousBookingFragment() {
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
        return inflater.inflate(R.layout.fragment_previous_booking, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view=view;

        establishViews();

        loading.setVisibility(View.GONE);

        RecyclerView recyclerView=view.findViewById(R.id.complete_holder);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        doctorsRecycleViewAdaptor=new BookedDoctorsRecycleViewAdaptor(new ArrayList<>(),PreviousBookingFragment.this,getContext());

        recyclerView.setAdapter(doctorsRecycleViewAdaptor);

        view.findViewById(R.id.pick_start_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isStartDateClicked=true;

                DialogFragment dialogFragment=new com.medical.mypockethealth.Classes.DateAndTimeForFragmentSection.DefaultDatePickerForFragment();
                dialogFragment.setTargetFragment(PreviousBookingFragment.this,0);
                assert getFragmentManager() != null;
                dialogFragment.show(getFragmentManager(),"time_picker");
            }
        });

        view.findViewById(R.id.pick_end_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isEndDateClicked=true;

                DialogFragment dialogFragment=new com.medical.mypockethealth.Classes.DateAndTimeForFragmentSection.DefaultDatePickerForFragment();
                dialogFragment.setTargetFragment(PreviousBookingFragment.this,0);
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

                            new Thread(new PreviousBookingDataFetcher(startDate,endDate,PreviousBookingFragment.this)).start();

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
    public void cancelBooking(BookDoctorInformation bookDoctorInformation) {

        if(!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED))
        {

            AlertDialog.Builder builder=new AlertDialog.Builder(getContext())
                    .setMessage("Are You Sure You Want To cancel this booking").setNegativeButton("NO", (dialogInterface, i) -> {

                    }).setPositiveButton("YES", (dialogInterface, i) -> {

                        new Thread(new CancelBookingCaller(bookDoctorInformation,PreviousBookingFragment.this)).start();

                    });
            builder.setCancelable(false);
            builder.show();
        }

        else
        {
            DialogShower dialogShower=new DialogShower(R.layout.internet_error,getContext());
            dialogShower.showDialog();
        }

    }

    @Override
    public void viewDetail(BookDoctorInformation bookDoctorInformation) {

        bottomSheetHandler(bookDoctorInformation);

    }

    private void bottomSheetHandler(BookDoctorInformation bookDoctorInformation)
    {

        handler.post(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {

                bottomSheetDialog=new BottomSheetDialog(requireContext(),R.style.BottomSheetDialogTheme);

                View bottom_view= LayoutInflater.from(requireContext()).inflate(R.layout.previous_sheet_layout
                        ,
                        (RelativeLayout) bottomSheetDialog.findViewById(R.id.relative_layout));

                bottomSheetDialog.setContentView(bottom_view);

                bottomSheetDialog.show();

                ImageView doctor_image;
                TextView doctor_name,appointment_date,slot_time,consult_charges,speciality,booking_status,patient_name,patient_age;

                doctor_name=bottomSheetDialog.findViewById(R.id.doctor_name);
                appointment_date=bottomSheetDialog.findViewById(R.id.appointment_date);
                slot_time=bottomSheetDialog.findViewById(R.id.slot_time);
                consult_charges=bottomSheetDialog.findViewById(R.id.consult_charges);
                speciality=bottomSheetDialog.findViewById(R.id.speciality);
                doctor_image=bottomSheetDialog.findViewById(R.id.doctor_image);
                booking_status=bottomSheetDialog.findViewById(R.id.booking_status);
                patient_name=bottomSheetDialog.findViewById(R.id.patient_name);
                patient_age=bottomSheetDialog.findViewById(R.id.patient_age);

                String name="Dr. " + bookDoctorInformation.getFirstName().charAt(0)+" "+bookDoctorInformation.getSurName();

                assert doctor_name != null;
                doctor_name.setText(name);

                assert speciality != null;
                speciality.setText(bookDoctorInformation.getSpecialization());

                assert appointment_date != null;
                appointment_date.setText(bookDoctorInformation.getDate());

                assert slot_time != null;
                slot_time.setText(bookDoctorInformation.getSlotTime());


                assert consult_charges != null;
                consult_charges.setText(bookDoctorInformation.getConsultCharges());

                assert patient_name != null;
                patient_name.setText(bookDoctorInformation.getPatientName());

                assert patient_age != null;
                patient_age.setText(bookDoctorInformation.getPatientAge());

                switch (bookDoctorInformation.getBookingStatus()) {
                    case "0":
                        assert booking_status != null;

                        booking_status.setText(R.string.pending);

                        booking_status.setTextColor(getResources().getColor(R.color.yellow));

                        break;
                    case "1":
                        assert booking_status != null;

                        booking_status.setText(R.string.confirmed);

                        booking_status.setTextColor(getResources().getColor(R.color.green_one));
                        break;
                    case "2":
                        assert booking_status != null;

                        booking_status.setText(R.string.Cancelled);
                        booking_status.setTextColor(getResources().getColor(R.color.red));
                        break;

                    case "3":

                        assert booking_status != null;

                        booking_status.setText(R.string.Rejected);

                        booking_status.setTextColor(getResources().getColor(R.color.red));

                        break;

                    case "4":

                        assert booking_status != null;

                        booking_status.setText(R.string.complete);

                        booking_status.setTextColor(getResources().getColor(R.color.green_one));

                        break;
                }

                Picasso.with(requireContext()).load(Uri.parse(bookDoctorInformation.getProfileImage())).error(R.drawable.profile).into(doctor_image);


                Button save=bottomSheetDialog.findViewById(R.id.save);

                assert save != null;

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        bottomSheetDialog.dismiss();

                    }
                });





            }
        });

    }

    @Override
    public void getFinalData(List<BookDoctorInformation> bookDoctorInformation) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                service_holder.setVisibility(View.GONE);
                loading.setVisibility(View.GONE);

                doctorsRecycleViewAdaptor.loadData(bookDoctorInformation);
            }
        });
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
    public void confirmationCancelBookingCaller(ResponseInformation responseInformation) {

        handler.post(new Runnable() {
            @Override
            public void run() {


                if(responseInformation.getSuccess().equals(String.valueOf(ResponseInformation.fail_response_type)))
                {

                    Toast.makeText(getContext(),responseInformation.getMessage(),Toast.LENGTH_SHORT).show();

                }
                else
                {

                    Toast.makeText(getContext(),responseInformation.getMessage(),Toast.LENGTH_SHORT).show();

                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                            new CurrentBookingFragment()).addToBackStack(null).commit();

                }
            }
        });
    }
}