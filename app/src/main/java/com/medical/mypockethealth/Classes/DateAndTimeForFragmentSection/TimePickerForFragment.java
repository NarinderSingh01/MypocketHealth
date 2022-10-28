package com.medical.mypockethealth.Classes.DateAndTimeForFragmentSection;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.medical.mypockethealth.R;

import java.util.Calendar;

public class TimePickerForFragment extends DialogFragment {


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar calendar=Calendar.getInstance();
        int hour=calendar.get(Calendar.HOUR);
        int minute=calendar.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), R.style.dialog_theme,(TimePickerDialog.OnTimeSetListener)getTargetFragment(),
                hour,minute,true);
    }
}
