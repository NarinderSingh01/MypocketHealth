package com.medical.mypockethealth.ProviderFragments.MainFrameSection.ScheduleSection;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.medical.mypockethealth.ApplicationBase.BaseActivity;
import com.medical.mypockethealth.Classes.DialogShower;
import com.medical.mypockethealth.Classes.NetworkSection.NetworkStatus;
import com.medical.mypockethealth.Classes.ProviderInformation.ProviderInformation;
import com.medical.mypockethealth.Classes.ResponseInformation;
import com.medical.mypockethealth.Classes.SlotsSection.SlotInformation;
import com.medical.mypockethealth.ProviderActivity.ProviderMainFrame;
import com.medical.mypockethealth.R;
import com.medical.mypockethealth.Threads.ProviderSection.ScheduleSection.AddScheduleCaller;
import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.util.Calendar;

public class AddScheduleFragment extends Fragment
        implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener,AddScheduleCaller.CallbackFromAddScheduleCaller
{

    private static final String TAG = "AddScheduleFragment";

    private LinearLayout morning_box,afternoon_box,evening_box;
    private final Calendar calendar=Calendar.getInstance();
    private CardView morning_add_on_time,morning_add_off_time,afternoon_add_on_time,afternoon_add_off_time,evening_add_on_time,evening_add_off_time;
    private EditText morning_set_per_slot_time,afternoon_set_per_slot_time,evening_set_per_slot_time;
    private TextView morning_set_on_time,morning_set_off_time,afternoon_set_on_time,afternoon_set_off_time,evening_set_on_time,evening_set_off_time;
    private View view;
    private boolean morningStartTimeClicked=false,morningEndTimeClicked=false,afternoonStartTimeClicked=false,afternoonEndTimeClicked=false,
            eveningStartTimeClicked=false,eveningEndTimeClicked=false,morning_switch_status=false,afternoon_switch_status=false,evening_switch_status=false,
            morningPerSlotFilled=false,afternoonPerSlotFilled=false,eveningPerSlotFilled=false,morning_clear_check=false,after_clear_check=false,evening_clear_check=false;
    private ImageView loading;
    private Button next;
    private int morningHourBackUp =-1,morningAfterBackup=-1,afternoonHourBackUp =-1,afternoonAfterBackup=-1,eveningHourBackUp =-1,eveningAfterBackup=-1;
    private String selected_date=null,selected_time=null,morning_start_time=null,morning_end_time=null,after_start_time=null,after_end_time=null,
            evening_start_time=null,evening_end_time=null,morning_start_division_time=null,afternoon_start_division_time=null,evening_start_division_time=null;

    private Dialog alert_box;
    private Button alert_ok;
    private final Handler handler=new Handler();
    private BottomSheetDialog bottomSheetDialog;

    public enum DataStatus
    {
        notSelected,selected
    }

    @Override
    public void confirmation(ResponseInformation responseInformation) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                next.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);

                if(responseInformation.getSuccess().equals(String.valueOf(ResponseInformation.fail_response_type)))
                {

                    Toast.makeText(getContext(),responseInformation.getMessage(),Toast.LENGTH_SHORT).show();

                }
                else
                {

                    Toast.makeText(getContext(),"Slot Added Successfully",Toast.LENGTH_SHORT).show();

                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                            new MyScheduleFragment()).addToBackStack(null).commit();
                }

            }
        });


    }

    public enum Alert
    {
        morning,afternoon,evening,morningExtend,afternoonExtend,eveningExtend,morningGreater,afternoonGreater,eveningGreater
    }

    public AddScheduleFragment() {
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
        return inflater.inflate(R.layout.fragment_add_schedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view=view;

        establishViews(view);

        switchHandler(view);

        setDefaultDateTag();

        morningBoxHandler(false);
        afternoonBoxHandler(false);
        eveningBoxHandler(false);


        loading.setVisibility(View.GONE);


        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,
                        new SlotFragment()).addToBackStack(null).commit();
            }
        });

        view.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment dialogFragment=new com.medical.mypockethealth.Classes.DateAndTimeForFragmentSection.DatePickerForFragment();
                dialogFragment.setTargetFragment(AddScheduleFragment.this,0);
                assert getFragmentManager() != null;
                dialogFragment.show(getFragmentManager(),"date_picker");

            }
        });

        morning_add_on_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setTimeStatusDefault();

                morningStartTimeClicked=true;

                DialogFragment dialogFragment=new com.medical.mypockethealth.Classes.DateAndTimeForFragmentSection.TimePickerForFragment();
                dialogFragment.setTargetFragment(AddScheduleFragment.this,0);
                assert getFragmentManager() != null;
                dialogFragment.show(getFragmentManager(),"time_picker");

            }
        });

        morning_add_off_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setTimeStatusDefault();

                if(!morning_set_on_time.getText().toString().equals("00:00"))
                {
                    morningEndTimeClicked=true;
                    DialogFragment dialogFragment=new com.medical.mypockethealth.Classes.DateAndTimeForFragmentSection.TimePickerForFragment();
                    dialogFragment.setTargetFragment(AddScheduleFragment.this,0);
                    assert getFragmentManager() != null;
                    dialogFragment.show(getFragmentManager(),"time_picker");
                }
                else
                {
                    Toast.makeText(getContext(),"Please select on timing first",Toast.LENGTH_SHORT).show();
                }

            }
        });

        afternoon_add_on_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setTimeStatusDefault();

                afternoonStartTimeClicked=true;
                DialogFragment dialogFragment=new com.medical.mypockethealth.Classes.DateAndTimeForFragmentSection.TimePickerForFragment();
                dialogFragment.setTargetFragment(AddScheduleFragment.this,0);
                assert getFragmentManager() != null;
                dialogFragment.show(getFragmentManager(),"time_picker");

            }
        });

        afternoon_add_off_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setTimeStatusDefault();

                if(!afternoon_set_on_time.getText().toString().equals("00:00"))
                {
                    afternoonEndTimeClicked=true;
                    DialogFragment dialogFragment=new com.medical.mypockethealth.Classes.DateAndTimeForFragmentSection.TimePickerForFragment();
                    dialogFragment.setTargetFragment(AddScheduleFragment.this,0);
                    assert getFragmentManager() != null;
                    dialogFragment.show(getFragmentManager(),"time_picker");
                }

                else
                {

                    Toast.makeText(getContext(),"Please select on timing first",Toast.LENGTH_SHORT).show();
                }
            }
        });

        evening_add_on_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setTimeStatusDefault();

                eveningStartTimeClicked=true;

                DialogFragment dialogFragment=new com.medical.mypockethealth.Classes.DateAndTimeForFragmentSection.TimePickerForFragment();
                dialogFragment.setTargetFragment(AddScheduleFragment.this,0);
                assert getFragmentManager() != null;
                dialogFragment.show(getFragmentManager(),"time_picker");

            }
        });
        evening_add_off_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setTimeStatusDefault();

                if(!evening_set_on_time.getText().toString().equals("00:00"))
                {
                    eveningEndTimeClicked=true;

                    DialogFragment dialogFragment=new com.medical.mypockethealth.Classes.DateAndTimeForFragmentSection.TimePickerForFragment();
                    dialogFragment.setTargetFragment(AddScheduleFragment.this,0);
                    assert getFragmentManager() != null;
                    dialogFragment.show(getFragmentManager(),"time_picker");
                }

                else
                {
                    Toast.makeText(getContext(),"Please select on timing first",Toast.LENGTH_SHORT).show();
                }

            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                 if(selected_date!=null)
                 {

                     if(!morning_switch_status && !afternoon_switch_status && !evening_switch_status)
                     {

                         Toast.makeText(getContext(),"Please create any of these slots",Toast.LENGTH_SHORT).show();

                     }
                     else
                     {

                         if(morning_switch_status)
                         {

                             if(morning_set_per_slot_time.getText().toString().trim().length()==0)
                             {

                                 morning_set_per_slot_time.setError("Please enter per slot time duration");

                                 morningPerSlotFilled=false;
                             }
                             else
                             {
                                 morning_set_per_slot_time.setError(null);

                                 morningPerSlotFilled=true;

                                 morning_start_division_time=morning_set_per_slot_time.getText().toString();
                             }

                             if(morning_set_on_time.getText().toString().equals("00:00"))
                             {
                                 morning_set_on_time.setTextColor(getResources().getColor(R.color.red));
                             }
                             else
                             {
                                 morning_set_on_time.setTextColor(getResources().getColor(R.color.blacker));
                             }

                             if(morning_set_off_time.getText().toString().equals("00:00"))
                             {
                                 morning_set_off_time.setTextColor(getResources().getColor(R.color.red));
                             }
                             else
                             {
                                 morning_set_off_time.setTextColor(getResources().getColor(R.color.blacker));
                             }


                             morning_clear_check= morningPerSlotFilled && morning_start_time != null && morning_end_time != null;

                         }

                         if(afternoon_switch_status)
                         {

                             if(afternoon_set_per_slot_time.getText().toString().trim().length()==0)
                             {

                                 afternoon_set_per_slot_time.setError("Please enter per slot time duration");

                                 afternoonPerSlotFilled=false;
                             }
                             else
                             {
                                 afternoon_set_per_slot_time.setError(null);

                                 afternoonPerSlotFilled=true;

                                 afternoon_start_division_time=afternoon_set_per_slot_time.getText().toString();
                             }

                             if(afternoon_set_on_time.getText().toString().equals("00:00"))
                             {
                                 afternoon_set_on_time.setTextColor(getResources().getColor(R.color.red));
                             }
                             else
                             {
                                 afternoon_set_on_time.setTextColor(getResources().getColor(R.color.blacker));
                             }

                             if(afternoon_set_off_time.getText().toString().equals("00:00"))
                             {
                                 afternoon_set_off_time.setTextColor(getResources().getColor(R.color.red));
                             }
                             else
                             {
                                 afternoon_set_off_time.setTextColor(getResources().getColor(R.color.blacker));
                             }



                             after_clear_check= afternoonPerSlotFilled && after_start_time != null && after_end_time != null;

                         }

                         if(evening_switch_status)
                         {

                             if(evening_set_per_slot_time.getText().toString().trim().length()==0)
                             {

                                 evening_set_per_slot_time.setError("Please enter per slot time duration");

                                 eveningPerSlotFilled=false;
                             }
                             else
                             {
                                 evening_set_per_slot_time.setError(null);

                                 eveningPerSlotFilled=true;

                                 evening_start_division_time=evening_set_per_slot_time.getText().toString();
                             }

                             if(evening_set_on_time.getText().toString().equals("00:00"))
                             {
                                 evening_set_on_time.setTextColor(getResources().getColor(R.color.red));
                             }
                             else
                             {
                                 evening_set_on_time.setTextColor(getResources().getColor(R.color.blacker));
                             }

                             if(evening_set_off_time.getText().toString().equals("00:00"))
                             {
                                 evening_set_off_time.setTextColor(getResources().getColor(R.color.red));
                             }
                             else
                             {
                                 evening_set_off_time.setTextColor(getResources().getColor(R.color.blacker));
                             }

                             evening_clear_check= eveningPerSlotFilled && evening_start_time != null && evening_end_time != null;

                         }

                         if(morning_switch_status)
                         {

                             if(!morning_clear_check)
                             {
                                 Toast.makeText(getContext(),"Please setup your morning status",Toast.LENGTH_SHORT).show();
                             }

                             else
                             {

                                 if(afternoon_switch_status && !after_clear_check)
                                 {
                                     // handle flow

                                 }
                                 else if(evening_switch_status && !evening_clear_check)
                                 {

                                 }
                                 else
                                 {
                                     slotSHandler();
                                 }

                             }
                         }

                         else if(afternoon_switch_status)
                         {

                             if(!after_clear_check)
                             {
                                 Toast.makeText(getContext(),"Please setup your afternoon status",Toast.LENGTH_SHORT).show();
                             }

                             else
                             {

                                 if(morning_switch_status && !morning_clear_check)
                                 {
                                     // handle flow

                                 }
                                 else if(evening_switch_status && !evening_clear_check)
                                 {

                                 }
                                 else
                                 {
                                     slotSHandler();
                                 }


                             }
                         }

                         else if(evening_switch_status)
                         {

                             if(!evening_clear_check)
                             {
                                 Toast.makeText(getContext(),"Please setup your evening status",Toast.LENGTH_SHORT).show();
                             }

                             else
                             {


                                 if(morning_switch_status && !morning_clear_check)
                                 {
                                     // handle flow

                                 }
                                 else if(afternoon_switch_status && !after_clear_check)
                                 {

                                 }
                                 else
                                 {
                                     slotSHandler();
                                 }

                             }
                         }


                     }

                 }else
                 {
                     Toast.makeText(getContext(), "Please select your slot date", Toast.LENGTH_SHORT).show();
                 }

            }
        });


    }

    private void slotSHandler()
    {

        SlotInformation slotInformation=new SlotInformation();

        slotInformation.setDate(selected_date);

        if(morning_switch_status && morning_clear_check)
        {
            slotInformation.setMorningVisibilityStatus(ResponseInformation.success_response_type);
            slotInformation.setMorningPerSlot(morning_start_division_time);
            slotInformation.setMorningStartTime(morning_start_time);
            slotInformation.setMorningEndTime(morning_end_time);
        }

        else
        {
            slotInformation.setMorningVisibilityStatus(ResponseInformation.fail_response_type);
            slotInformation.setMorningPerSlot(DataStatus.notSelected.toString());
            slotInformation.setMorningStartTime(DataStatus.notSelected.toString());
            slotInformation.setMorningEndTime(DataStatus.notSelected.toString());
        }

        if(afternoon_switch_status && after_clear_check)
        {

            slotInformation.setAfternoonVisibilityStatus(ResponseInformation.success_response_type);
            slotInformation.setAfternoonPerlSot(afternoon_start_division_time);
            slotInformation.setAfternoonStartTime(after_start_time);
            slotInformation.setAfternoonEndTime(after_end_time);

        }

        else
        {
            slotInformation.setAfternoonVisibilityStatus(ResponseInformation.fail_response_type);
            slotInformation.setAfternoonPerlSot(DataStatus.notSelected.toString());
            slotInformation.setAfternoonStartTime(DataStatus.notSelected.toString());
            slotInformation.setAfternoonEndTime(DataStatus.notSelected.toString());
        }

        if(evening_switch_status && evening_clear_check)
        {
            slotInformation.setEveningVisibilityStatus(ResponseInformation.success_response_type);
            slotInformation.setEveningPerSlot(evening_start_division_time);
            slotInformation.setEveningStartTime(evening_start_time);
            slotInformation.setEveningEndTime(evening_end_time);

        }

        else
        {
            slotInformation.setEveningVisibilityStatus(ResponseInformation.fail_response_type);
            slotInformation.setEveningPerSlot(DataStatus.notSelected.toString());
            slotInformation.setEveningStartTime(DataStatus.notSelected.toString());
            slotInformation.setEveningEndTime(DataStatus.notSelected.toString());
        }


         if(getProviderInformation().getProfileStatus().equals(ResponseInformation.success_response_type))
         {
             if (!(BaseActivity.network_status == NetworkStatus.TYPE_NOT_CONNECTED)) {

                 loading.setVisibility(View.VISIBLE);
                 next.setVisibility(View.GONE);


                 new Thread(new AddScheduleCaller(slotInformation,AddScheduleFragment.this)).start();


             } else {
                 DialogShower dialogShower=new DialogShower(R.layout.internet_error,getContext());
                 dialogShower.showDialog();
             }
         }
         else
         {
             bottomSheetHandler();
         }




    }

    private void bottomSheetHandler()
    {
        handler.post(new Runnable() {

            @Override
            public void run() {

                bottomSheetDialog=new BottomSheetDialog(requireContext(),R.style.BottomSheetDialogTheme);

                View bottom_view= LayoutInflater.from(getContext()).inflate(R.layout.account_warning_layout
                        ,
                        (RelativeLayout) bottomSheetDialog.findViewById(R.id.relative_layout));

                bottomSheetDialog.setContentView(bottom_view);

                bottomSheetDialog.show();

                TextView userName=bottomSheetDialog.findViewById(R.id.user_name);

                assert userName != null;

                String name=getProviderInformation().getFirstName()+" " + getProviderInformation().getSurName();
                userName.setText(name);

            }
        });


    }

    private ProviderInformation getProviderInformation()
    {
        SharedPreferences preferences= requireContext().getSharedPreferences(ProviderMainFrame.provider_information_file, Context.MODE_PRIVATE);
        String json = preferences.getString(ProviderMainFrame.provider_information_key, "");
        Gson gson = new Gson();
        return gson.fromJson(json, ProviderInformation.class);
    }


    private void setTimeStatusDefault()
    {
        morningStartTimeClicked=false;
        morningEndTimeClicked=false;
        afternoonStartTimeClicked=false;
        afternoonEndTimeClicked=false;
        eveningStartTimeClicked=false;
        eveningEndTimeClicked=false;
    }
    private void switchHandler(View view)
    {
        LabeledSwitch morning_switch,afternoon_switch,evening_switch;

        morning_switch=view.findViewById(R.id.morning_switch);
        afternoon_switch=view.findViewById(R.id.afternoon_switch);
        evening_switch=view.findViewById(R.id.evening_switch);


        morning_switch.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {

                morning_switch_status=isOn;

                morningBoxHandler(isOn);
            }
        });

        afternoon_switch.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {

                afternoon_switch_status=isOn;

                afternoonBoxHandler(isOn);
            }
        });

        evening_switch.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {

                evening_switch_status=isOn;

                eveningBoxHandler(isOn);
            }
        });
    }


    private void morningBoxHandler(boolean value)
    {

        if(value)
        {

            morning_box.setVisibility(View.VISIBLE);
        }
        else
        {
            morning_box.setVisibility(View.GONE);
        }

    }

    private void afternoonBoxHandler(boolean value)
    {

        if(value)
        {

            afternoon_box.setVisibility(View.VISIBLE);
        }
        else
        {
            afternoon_box.setVisibility(View.GONE);
        }

    }

    private void eveningBoxHandler(boolean value)
    {

        if(value)
        {

            evening_box.setVisibility(View.VISIBLE);
        }
        else
        {
            evening_box.setVisibility(View.GONE);
        }

    }

    private void establishViews(View view)
    {
        morning_box=view.findViewById(R.id.morning_box);
        afternoon_box=view.findViewById(R.id.afternoon_box);
        evening_box=view.findViewById(R.id.evening_box);
        morning_add_on_time=view.findViewById(R.id.morning_add_on_time);
        morning_add_off_time=view.findViewById(R.id.morning_add_off_time);
        afternoon_add_on_time=view.findViewById(R.id.afternoon_add_on_time);
        afternoon_add_off_time=view.findViewById(R.id.afternoon_add_off_time);
        evening_add_on_time=view.findViewById(R.id.evening_add_on_time);
        evening_add_off_time=view.findViewById(R.id.evening_add_off_time);
        morning_set_per_slot_time=view.findViewById(R.id.morning_set_per_slot_time);
        afternoon_set_per_slot_time=view.findViewById(R.id.afternoon_set_per_slot_time);
        evening_set_per_slot_time=view.findViewById(R.id.evening_set_per_slot_time);
        morning_set_on_time=view.findViewById(R.id.morning_set_on_time);
        morning_set_off_time=view.findViewById(R.id.morning_set_off_time);
        afternoon_set_on_time=view.findViewById(R.id.afternoon_set_on_time);
        afternoon_set_off_time=view.findViewById(R.id.afternoon_set_off_time);
        afternoon_set_off_time=view.findViewById(R.id.afternoon_set_off_time);
        evening_set_on_time=view.findViewById(R.id.evening_set_on_time);
        evening_set_off_time=view.findViewById(R.id.evening_set_off_time);
        loading=view.findViewById(R.id.loading);
        next=view.findViewById(R.id.next);

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {

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

        selected_date=year+"-"+month1+"-"+day1;

        setFilteredDate(String.valueOf(year),month1,day1);


    }

    private void setDefaultDateTag()
    {

        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int day=calendar.get(Calendar.DAY_OF_MONTH);

        String month1=String.valueOf(month+1);



        setFilteredDate(String.valueOf(year),month1,String.valueOf(day));

    }

    private void setFilteredDate(String year,String month,String day)   {

        String result = "";

        try {
            result = requireContext().getResources().getStringArray(R.array.month_names)[Integer.parseInt(month)-1];
        } catch (ArrayIndexOutOfBoundsException e) {
            result = Integer.toString(Integer.parseInt(month)-1);
        }


        String date=day+", "+result+" "+year;

        ((TextView)this.view.findViewById(R.id.set_date)).setText(date);



    }

    @Override
    public void onTimeSet(TimePicker view, int hour, int minute) {

        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.SECOND,0);

        selected_time=hour+":"+minute;

        checkTimingFlow(hour);

    }

    private void checkTimingFlow(int hour)
    {

        if(morningStartTimeClicked)
        {
            if(!morning_set_off_time.getText().toString().equals("00:00"))
            {

                if(hour>=1)
                {

                    if(hour<morningAfterBackup)
                    {
                        morningHourBackUp =hour;
                        setTiming(selected_time);
                    }
                    else
                    {
                        showAlertBox(Alert.morningGreater);
                    }

                }

                else
                {
                    showAlertBox(Alert.morning);
                }


            }

            else
            {
                if(hour>=1 && hour<12)
                {

                    morningHourBackUp =hour;
                    setTiming(selected_time);
                }
                else
                {
                    showAlertBox(Alert.morning);
                }
            }
        }


        if(morningEndTimeClicked)
        {


            if(hour> morningHourBackUp)
            {


                if(hour>=12)
                {
                    showAlertBox(Alert.morning);
                }
                else
                {
                    morningAfterBackup=hour;
                    setTiming(selected_time);
                }

            }
            else
            {

                showAlertBox(Alert.morningExtend);
            }

        }



        if(afternoonStartTimeClicked)
        {


            if(!afternoon_set_off_time.getText().toString().equals("00:00"))
            {

                if(hour>=12)
                {


                    if(hour<afternoonAfterBackup)
                    {
                        afternoonHourBackUp =hour;
                        setTiming(selected_time);
                    }
                    else
                    {

                        showAlertBox(Alert.morningGreater);
                    }

                }

                else
                {
                    showAlertBox(Alert.afternoon);
                }


            }

            else
            {



                if(hour>=12 && hour<16)
                {

                    afternoonHourBackUp =hour;
                    setTiming(selected_time);
                }
                else
                {


                    showAlertBox(Alert.afternoon);
                }
            }
        }


        if(afternoonEndTimeClicked)
        {


            if(hour>afternoonHourBackUp)
            {


                if(hour>=16)
                {
                    showAlertBox(Alert.afternoon);
                }
                else
                {
                    afternoonAfterBackup=hour;
                    setTiming(selected_time);
                }

            }
            else
            {


                showAlertBox(Alert.afternoonExtend);
            }

        }




        if(eveningStartTimeClicked)
        {

            Log.d(TAG, "checkTimingFlow: evening start");

            if(!evening_set_off_time.getText().toString().equals("00:00"))
            {

                Log.d(TAG, "checkTimingFlow: evning after not empty");

                if(hour>=16)
                {

                    Log.d(TAG, "checkTimingFlow: greater than 16");

                    if(hour<eveningAfterBackup)
                    {
                        eveningHourBackUp =hour;
                        setTiming(selected_time);
                    }
                    else
                    {
                        Log.d(TAG, "checkTimingFlow: start greater than end");
                        showAlertBox(Alert.morningGreater);
                    }

                }

                else
                {
                    showAlertBox(Alert.evening);
                }


            }

            else
            {

                Log.d(TAG, "checkTimingFlow: start called");

                if(hour==0)
                {
                    hour=24;
                }
                if(hour>=16 && hour<24)
                {

                    eveningHourBackUp =hour;
                    setTiming(selected_time);
                }
                else
                {

                    Log.d(TAG, "checkTimingFlow: eceed after 16");
                    showAlertBox(Alert.evening);
                }
            }
        }


        if(eveningEndTimeClicked)
        {


            Log.d(TAG, "checkTimingFlow: end called");
            if(hour>eveningHourBackUp)
            {

                Log.d(TAG, "checkTimingFlow: end greater than start");

                if(hour==0)
                {
                    hour=24;
                }

                if(hour>23)
                {

                    Log.d(TAG, "checkTimingFlow: end greater than 24");
                    showAlertBox(Alert.evening);
                }
                else
                {
                    eveningAfterBackup=hour;
                    setTiming(selected_time);
                }

            }
            else
            {

                Log.d(TAG, "checkTimingFlow: morning end smaller than start");

                if(hour==0)
                {
                    showAlertBox(Alert.evening);
                }
                else
                {
                    showAlertBox(Alert.eveningExtend);
                }

            }

        }







    }

    private void setTiming(String value)
    {

        if(morningStartTimeClicked)
        {

            Log.d(TAG, "setTiming: set morning start timing ");
            morningStartTimeClicked=false;

            morning_start_time=value;

            morning_set_on_time.setText(value);

            morning_set_on_time.setTextColor(getResources().getColor(R.color.blacker));

        }

        if(morningEndTimeClicked)
        {

            Log.d(TAG, "setTiming: set morning end timing");
            morningEndTimeClicked=false;

            morning_end_time=value;

            morning_set_off_time.setText(value);

            morning_set_off_time.setTextColor(getResources().getColor(R.color.blacker));

        }

        if(afternoonStartTimeClicked)
        {

            Log.d(TAG, "setTiming: set afternoon start timing ");
            afternoonStartTimeClicked=false;

            after_start_time=value;

            afternoon_set_on_time.setText(value);

            afternoon_set_on_time.setTextColor(getResources().getColor(R.color.blacker));

        }

        if(afternoonEndTimeClicked)
        {

            Log.d(TAG, "setTiming: set afternoon end timing");
            afternoonEndTimeClicked=false;

            after_end_time=value;

            afternoon_set_off_time.setText(value);

            afternoon_set_off_time.setTextColor(getResources().getColor(R.color.blacker));

        }


        if(eveningStartTimeClicked)
        {

            Log.d(TAG, "setTiming: set evening start timing ");
            eveningStartTimeClicked=false;

            evening_start_time=value;

            evening_set_on_time.setText(value);

            evening_set_on_time.setTextColor(getResources().getColor(R.color.blacker));

        }

        if(eveningEndTimeClicked)
        {

            Log.d(TAG, "setTiming: set afternoon end timing");
            eveningEndTimeClicked=false;

            evening_end_time=value;

            evening_set_off_time.setText(value);

            evening_set_off_time.setTextColor(getResources().getColor(R.color.blacker));

        }

    }



    private void showAlertBox(Alert alert)
    {

        switch (alert)
        {
            case morning:

                alert_box=new Dialog(getContext());
                alert_box.setContentView(R.layout.morning_layout);
                alert_box.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alert_box.setCanceledOnTouchOutside(false);
                Window window=alert_box.getWindow();
                window.setGravity(Gravity.CENTER);
                alert_box.show();

                alert_box.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alert_box.dismiss();
                    }
                });

                break;

            case afternoon:

                alert_box=new Dialog(getContext());
                alert_box.setContentView(R.layout.afternoon_layout);
                alert_box.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alert_box.setCanceledOnTouchOutside(false);
                Window window1=alert_box.getWindow();
                window1.setGravity(Gravity.CENTER);
                alert_box.show();

                alert_box.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alert_box.dismiss();
                    }
                });

                break;

            case evening:

                alert_box=new Dialog(getContext());
                alert_box.setContentView(R.layout.evening_layout);
                alert_box.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alert_box.setCanceledOnTouchOutside(false);
                Window window2=alert_box.getWindow();
                window2.setGravity(Gravity.CENTER);
                alert_box.show();

                alert_box.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alert_box.dismiss();
                    }
                });

                break;

            case morningExtend: case afternoonExtend: case eveningExtend:

            alert_box=new Dialog(getContext());
            alert_box.setContentView(R.layout.morning_extend_layout);
            alert_box.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alert_box.setCanceledOnTouchOutside(false);
            Window window3=alert_box.getWindow();
            window3.setGravity(Gravity.CENTER);
            alert_box.show();

            alert_box.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    alert_box.dismiss();
                }
            });


            break;

            case morningGreater:
                alert_box = new Dialog(getContext());
                alert_box.setContentView(R.layout.morning_greater_layout);
                alert_box.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alert_box.setCanceledOnTouchOutside(false);
                Window window4=alert_box.getWindow();
                window4.setGravity(Gravity.CENTER);
                alert_box.show();

                alert_box.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alert_box.dismiss();
                    }
                });

                break;



        }

    }


}