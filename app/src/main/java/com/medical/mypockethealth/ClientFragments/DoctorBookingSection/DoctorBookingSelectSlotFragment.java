package com.medical.mypockethealth.ClientFragments.DoctorBookingSection;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.medical.mypockethealth.Adaptors.UserSection.BookingSection.DateShowerRecycleViewAdaptor;
import com.medical.mypockethealth.Adaptors.UserSection.BookingSection.SlotDetailsShowerRecycleViewAdaptor;
import com.medical.mypockethealth.ApplicationBase.BaseActivity;
import com.medical.mypockethealth.Classes.DateInformation.DateInformation;
import com.medical.mypockethealth.Classes.DialogShower;
import com.medical.mypockethealth.Classes.DoctorInformation.DoctorInformation;
import com.medical.mypockethealth.Classes.NetworkSection.NetworkStatus;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.Classes.SlotsSection.ClientSection.AfternoonSlotsInformation;
import com.medical.mypockethealth.Classes.SlotsSection.ClientSection.EveningSlotsInformation;
import com.medical.mypockethealth.Classes.SlotsSection.ClientSection.MergeSlotInformation;
import com.medical.mypockethealth.Classes.SlotsSection.ClientSection.MorningSlotsInformation;
import com.medical.mypockethealth.Classes.SlotsSection.ClientSection.SlotsDetailsInformation;
import com.medical.mypockethealth.Classes.URLBuilder;
import com.medical.mypockethealth.R;
import com.medical.mypockethealth.Threads.UserSection.BookingSection.SlotByDateFetcher;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialog;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;
import com.squareup.picasso.Picasso;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class DoctorBookingSelectSlotFragment extends Fragment implements
        SlotDetailsShowerRecycleViewAdaptor.callBackFromSlotDetailsShowerRecycleViewAdaptor,
        DateShowerRecycleViewAdaptor.callBackFromDateShowerRecycleViewAdaptor,
        SlotByDateFetcher.CallbackFromSlotByDateFetcher{

    private static final String TAG = "DoctorBookingSelectSlot";

    private TextView book,set_date;
    private final Calendar calendar = Calendar.getInstance();
    private String selected_date = null;
    private ImageView loading;
    private final Handler handler = new Handler();
    private LinearLayout slot_details,found;
    private SlotDetailsShowerRecycleViewAdaptor slotDetailsShowerRecycleViewAdaptor;
    private DateShowerRecycleViewAdaptor dateShowerRecycleViewAdaptor;
    public static String selected_slot;
    private Dialog slot_box;
    private ImageView slot_loading;
    private Button next;
    private int year,month,day;
    private HorizontalScrollView box_slider;
    private SlotsDetailsInformation slotDetailsInformation;
    private RelativeLayout morning_box,afternoon_box,evening_box;
    private RecyclerView slot_holder;
    private MergeSlotInformation selectedSlotInformation;
    public static DoctorInformation doctorInformation;
    private DateInformation dateInformation;
    public static final String data_key="data_key";



    enum SelectBoxType
    {
        morning,afternoon,evening
    }

    public DoctorBookingSelectSlotFragment() {
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
        return inflater.inflate(R.layout.fragment_doctor_booking_select_slot, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        establishViews(view);

        setDefaultDateTag(true,0,0,0);

        setData(view);

        Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT).show();

        RecyclerView date_holder;
        date_holder=view.findViewById(R.id.date_holder);
        slot_holder=view.findViewById(R.id.slot_holder);

        disableSlotSection();

        LinearLayoutManager dateHolderLayout;
        dateHolderLayout=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,true);
        dateHolderLayout.setReverseLayout(false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3, GridLayoutManager.HORIZONTAL, false);
        date_holder.setLayoutManager(dateHolderLayout);
        slot_holder.setLayoutManager(gridLayoutManager);

        dateShowerRecycleViewAdaptor =new DateShowerRecycleViewAdaptor(new ArrayList<>(),
                DoctorBookingSelectSlotFragment.this,getContext());

        slotDetailsShowerRecycleViewAdaptor=new SlotDetailsShowerRecycleViewAdaptor(new ArrayList<>(),
                DoctorBookingSelectSlotFragment.this,getContext());

        date_holder.setAdapter(dateShowerRecycleViewAdaptor);

        slot_holder.setAdapter(slotDetailsShowerRecycleViewAdaptor);

        getDefaultDateSet();

        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                        new DoctorBookingTwoFragment()).addToBackStack(null).commit();
            }
        });

        view.findViewById(R.id.set_Date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               openMonthYearSelector();
            }
        });


        morning_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activateField(SelectBoxType.morning,view);
                
                uploadData(SelectBoxType.morning);
                
                
            }
        });


        afternoon_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activateField(SelectBoxType.afternoon,view);

                uploadData(SelectBoxType.afternoon);
            }
        });

        evening_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activateField(SelectBoxType.evening,view);

                uploadData(SelectBoxType.evening);
            }
        });
        
       next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if(selectedSlotInformation!=null)
                {
                    DoctorBookingConfirmFragment doctorBookingConfirmFragment=new DoctorBookingConfirmFragment();
                    Bundle bundle=new Bundle();
                    
                    doctorInformation.setSelected_date(selected_date);
                    doctorInformation.setSlotTime(selectedSlotInformation.getSlotTime());
                    doctorInformation.setDateInformation(dateInformation);
                    
                    bundle.putSerializable(DoctorBookingConfirmFragment.doctor_information_key,doctorInformation);
                    
                    DoctorBookingConfirmFragment.doctorInformation=null;
                    
                    doctorBookingConfirmFragment.setArguments(bundle);

                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout
                            ,doctorBookingConfirmFragment).addToBackStack(null).commit();
                }
                else 
                    {
                        
                      Toast.makeText(getContext(),"Please select your slot",Toast.LENGTH_SHORT).show();
                        
                    }            }
        });
    }
    
    
    private void uploadData(SelectBoxType selectBoxType)
    {
        List<MergeSlotInformation> mergeSlotInformation=new ArrayList<>();
        
        switch (selectBoxType)
        {
            case morning:

                for (MorningSlotsInformation morningSlotsInformation : slotDetailsInformation.getMorningSlot()) {


                    mergeSlotInformation.add(new MergeSlotInformation(morningSlotsInformation.getSlotTime(),
                            morningSlotsInformation.getStatus(),checkTimeState(morningSlotsInformation.getSlotTime())));

                }

                slotDetailsShowerRecycleViewAdaptor.loadData(mergeSlotInformation);
                
                break;
                
            case afternoon:

                for (AfternoonSlotsInformation afternoonSlotsInformation : slotDetailsInformation.getAfternoonSlot()) {

                    mergeSlotInformation.add(new MergeSlotInformation(afternoonSlotsInformation.getSlotTime(),
                            afternoonSlotsInformation.getStatus(),checkTimeState(afternoonSlotsInformation.getSlotTime())));

                }

                slotDetailsShowerRecycleViewAdaptor.loadData(mergeSlotInformation);
                
                break;
                
            case evening:

                for (EveningSlotsInformation eveningSlotsInformation : slotDetailsInformation.getEveningSlot()) {

                    mergeSlotInformation.add(new MergeSlotInformation(eveningSlotsInformation.getSlotTime(),eveningSlotsInformation.getStatus(), checkTimeState(eveningSlotsInformation.getSlotTime())));
                }

                slotDetailsShowerRecycleViewAdaptor.loadData(mergeSlotInformation);
                
                break;
        }
    }


    private boolean checkTimeState(String startTime)
    {
        try {

            Log.d(TAG, "checkTimeState: calling ");

            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            String currentTime = simpleDateFormat.format(new Date());

            Date inTime = simpleDateFormat.parse(startTime);
            Date outTime = simpleDateFormat.parse(currentTime);

            assert inTime != null;
            if (isTimeAfter(inTime, outTime)) {

                return true;

            } else {

                return false;

            }


        }catch (Exception e)
        {
            Log.d(TAG, "onCreate: " + e.getMessage());
        }

        return false;

    }



    boolean isTimeAfter(Date startTime, Date endTime) {
        //Same way you can check with after() method also.
        return startTime.before(endTime);
    }


    private void disableSlotSection()
    {
        box_slider.setVisibility(View.GONE);
        next.setVisibility(View.GONE);
        slot_holder.setVisibility(View.GONE);
    }

    private void enableSlotSection()
    {
        box_slider.setVisibility(View.VISIBLE);
        next.setVisibility(View.VISIBLE);
        slot_holder.setVisibility(View.VISIBLE);
    }
    private void activateField(SelectBoxType selectBoxType,View view)
    {

        ImageView morning_image,afternoon_image,evening_image;
        TextView morning_text,afternoon_text,evening_text;

        morning_image=view.findViewById(R.id.morning_image);
        afternoon_image=view.findViewById(R.id.afternoon_image);
        evening_image=view.findViewById(R.id.evening_image);
        morning_text=view.findViewById(R.id.morning_text);
        afternoon_text=view.findViewById(R.id.afternoon_text);
        evening_text=view.findViewById(R.id.evening_text);




       switch (selectBoxType)
       {
           case morning:


                morning_box.setBackgroundResource(R.drawable.slot_background_layout);
                afternoon_box.setBackgroundResource(0);
                evening_box.setBackgroundResource(0);

               morning_text.setTextColor(getResources().getColor(R.color.white));
               afternoon_text.setTextColor(getResources().getColor(R.color.card_background_color));
               evening_text.setTextColor(getResources().getColor(R.color.card_background_color));

               morning_image.setColorFilter(getResources().getColor(R.color.white));
               afternoon_image.setColorFilter(getResources().getColor(R.color.card_background_color));
               evening_image.setColorFilter(getResources().getColor(R.color.card_background_color));

               break;

           case afternoon:

               morning_box.setBackgroundResource(0);
               afternoon_box.setBackgroundResource(R.drawable.slot_background_layout);
               evening_box.setBackgroundResource(0);

               morning_text.setTextColor(getResources().getColor(R.color.card_background_color));
               afternoon_text.setTextColor(getResources().getColor(R.color.white));
               evening_text.setTextColor(getResources().getColor(R.color.card_background_color));

               morning_image.setColorFilter(getResources().getColor(R.color.card_background_color));
               afternoon_image.setColorFilter(getResources().getColor(R.color.white));
               evening_image.setColorFilter(getResources().getColor(R.color.card_background_color));

               break;

           case evening:

               morning_box.setBackgroundResource(0);
               afternoon_box.setBackgroundResource(0);
               evening_box.setBackgroundResource(R.drawable.slot_background_layout);

               morning_text.setTextColor(getResources().getColor(R.color.black));
               afternoon_text.setTextColor(getResources().getColor(R.color.black));
               evening_text.setTextColor(getResources().getColor(R.color.white));

               morning_image.setColorFilter(getResources().getColor(R.color.black));
               afternoon_image.setColorFilter(getResources().getColor(R.color.black));
               evening_image.setColorFilter(getResources().getColor(R.color.white));


               break;
       }


    }
    private void getDefaultDateSet()
    {

        Calendar calendar = Calendar.getInstance();

        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int date=calendar.get(Calendar.DATE);

        int setMonth=(month+1);

        filterDates(date,getMonthDays(setMonth,year),year,setMonth);


    }
    private void openMonthYearSelector()
    {
        Calendar calendar = Calendar.getInstance();

        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int date=calendar.get(Calendar.DATE);

        calendar.clear();

        calendar.set(year, month, 1);            // Set minimum date to show in dialog
        long minDate = calendar.getTimeInMillis();    // Get milliseconds of the modified date

        calendar.clear();
        calendar.set(2100, 11, 31);   // Set maximum date to show in dialog
        long maxDate = calendar.getTimeInMillis();     // Get milliseconds of the modified date

        MonthYearPickerDialogFragment dialogFragment =  MonthYearPickerDialogFragment
                .getInstance(month, year, minDate, maxDate);

        dialogFragment.show(requireActivity().getSupportFragmentManager(), null);

        dialogFragment.setOnDateSetListener(new MonthYearPickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(int year, int monthOfYear) {

                int setMonth=(month+1);
                monthOfYear+=1;

                setDefaultDateTag(false,year,monthOfYear,date);

                if(setMonth!=monthOfYear)
                {
                    filterDates(1,getMonthDays(monthOfYear,year),year,monthOfYear);
                }
                else
                {
                    filterDates(date,getMonthDays(monthOfYear,year),year,monthOfYear);
                }


            }
        });
    }

    private void filterDates(int date,int maxDate,int year,int month)
    {

        List<DateInformation> dateInformation=new ArrayList<>();

        for (int i = date; i <=maxDate ; i++) {

            dateInformation.add(new DateInformation(String.valueOf(i),getDayName(i+"-"+month+"-"+year),String.valueOf(month),String.valueOf(year)));

        }


        dateShowerRecycleViewAdaptor.loadData(dateInformation);

    }


    private String getDayName(String date)
    {
        String day=null;

        try {
            @SuppressLint("SimpleDateFormat") Date date1=new SimpleDateFormat("dd-MM-yyyy").parse(date);

            @SuppressLint("SimpleDateFormat") Format dateFormat = new SimpleDateFormat("EEE");

           day=dateFormat.format(date1);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return day;
    }

    public static int getMonthDays(int month, int year) {
        int daysInMonth ;
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            daysInMonth = 30;
        }
        else {
            if (month == 2) {
                daysInMonth = (year % 4 == 0) ? 29 : 28;
            } else {
                daysInMonth = 31;
            }
        }
        return daysInMonth;
    }

    private void setDefaultDateTag(boolean defaultStatus,int year,int month,int day)
    {

         if(defaultStatus)
         {
              year=calendar.get(Calendar.YEAR);
              month=calendar.get(Calendar.MONTH);
              day=calendar.get(Calendar.DAY_OF_MONTH);

              month+=1;

         }
         else
         {
             year=year;
             month=month;
             day=day;
         }



        selected_date=year+"-"+month+"-"+day;

        setFilteredDate(String.valueOf(year),String.valueOf(month),String.valueOf(day));

    }

    private void setFilteredDate(String year,String month,String day)   {

        String result = "";

        try {
            result = requireContext().getResources().getStringArray(R.array.month_names)[Integer.parseInt(month)-1];
        } catch (ArrayIndexOutOfBoundsException e) {

            result = Integer.toString(Integer.parseInt(month)-1);
        }


        String date=result+" "+year;

        Log.d(TAG, "setFilteredDate: " + date);

        set_date.setText(date);



    }

    private void setData(View view)
    {

        ImageView profile_image;
        TextView speciality,experience,consult_fee,name_field;

        profile_image=view.findViewById(R.id.profile_image);
        speciality=view.findViewById(R.id.speciality);
        experience=view.findViewById(R.id.experience);
        consult_fee=view.findViewById(R.id.consult_fee);
        name_field=view.findViewById(R.id.name);

        
        if(doctorInformation==null)
        {
            assert getArguments() != null;
            doctorInformation=(DoctorInformation)getArguments().get(DoctorBookingSelectSlotFragment.data_key);
        }

//        String name="Dr. " + doctorInformation.getFirstName().charAt(0) + " "+doctorInformation.getSurName();

        String name;

        if (doctorInformation.getUserTitle() != null && doctorInformation.getUserType() != null) {

            name = doctorInformation.getUserTitle() + " " +
                    doctorInformation.getFirstName().charAt(0) + " " + doctorInformation.getSurName();

        } else {

            name = "Dr. " + doctorInformation.getFirstName().charAt(0) + " " + doctorInformation.getSurName();

        }

        name_field.setText(name);

        speciality.setText(doctorInformation.getSpecialization());

        String ex=doctorInformation.getExperience()+" yrs experience";
        experience.setText(ex);

        String consultFee= URLBuilder.CurrencySign.R+" "+doctorInformation.getConsultCharges()+" consultation Fees";

        consult_fee.setText(consultFee);

        Picasso.with(getContext()).load(Uri.parse(doctorInformation.getProfileImage())).error(R.drawable.profile).into(profile_image);

    }

    private void establishViews(View view)
    {
        set_date=view.findViewById(R.id.set_date);
        morning_box=view.findViewById(R.id.morning_box);
        afternoon_box=view.findViewById(R.id.afternoon_box);
        evening_box=view.findViewById(R.id.evening_box);
        loading=view.findViewById(R.id.loading);
        found=view.findViewById(R.id.found);
        next=view.findViewById(R.id.next);
        box_slider=view.findViewById(R.id.box_slider);

    }

    private void getSlotsInformation(DateInformation date) {

        if (!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED)) {

            loading.setVisibility(View.VISIBLE);
            found.setVisibility(View.GONE);

            selected_date=date.getYear()+"-"+date.getMonth()+"-"+date.getDate();

            new Thread(new SlotByDateFetcher(doctorInformation.getId()
                    , selected_date, DoctorBookingSelectSlotFragment.this)).start();

        } else {

            found.setVisibility(View.VISIBLE);
            loading.setVisibility(View.GONE);

            DialogShower dialogShower = new DialogShower(R.layout.internet_error, R.style.translate_animator, getContext());
            dialogShower.showDialog();
        }
    }

    @Override
    public void select(MergeSlotInformation slotInformation) {

        selectedSlotInformation=slotInformation;
        
    }

    @Override
    public void confirmationSlotByDateFetcher(ResponseInformation responseInformation) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                loading.setVisibility(View.GONE);
                found.setVisibility(View.VISIBLE);


                if (responseInformation.getSuccess().equals(ResponseInformation.fail_response_type)) {



                    disableSlotSection();


                }
            }
        });

    }

    @Override
    public void informationSlotByDateFetcher(SlotsDetailsInformation slotsDetailsInformation) {


        handler.post(new Runnable() {
            @Override
            public void run() {

                slotDetailsInformation=slotsDetailsInformation;


                loading.setVisibility(View.GONE);
                found.setVisibility(View.GONE
                );

                enableSlotSection();

                slotBoxHandler(slotsDetailsInformation);

            }
        });

    }

    private void slotBoxHandler(SlotsDetailsInformation slotsDetailsInformation)
    {
        if(slotsDetailsInformation.getMorningSlot().size()!=0)
        {
            morning_box.setVisibility(View.VISIBLE);
        }
        else
        {

            morning_box.setVisibility(View.GONE);

        }

        if(slotsDetailsInformation.getAfternoonSlot().size()!=0)
        {
            afternoon_box.setVisibility(View.VISIBLE);
        }
        else
        {

            afternoon_box.setVisibility(View.GONE);

        }

        if(slotsDetailsInformation.getEveningSlot().size()!=0)
        {
            evening_box.setVisibility(View.VISIBLE);
        }
        else
        {

            evening_box.setVisibility(View.GONE);

        }
    }

    @Override
    public void selectedDate(DateInformation dateInformation) {

        disableSlotSection();
        
        this.dateInformation=dateInformation;
        
        getSlotsInformation(dateInformation);

    }

//    private String getProviderType(DoctorInformation providerInformation) {
//
//        String type;
//
//        if (providerInformation.getUserType() != null && providerInformation.getUserType().length() != 0) {
//
//            if (providerInformation.getUserType().equalsIgnoreCase("doctor")) {
//
//                type = "Dr.";
//
//            } else if (providerInformation.getUserType().equalsIgnoreCase("nurse")) {
//
//                type = "Nur.";
//
//            } else {
//
//                type = "";
//
//            }
//
//        } else {
//
//            type = "";
//
//        }
//
//        return type;
//    }

}