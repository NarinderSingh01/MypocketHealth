package com.medical.mypockethealth.Classes.DateAndTimeForActivitySection;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.medical.mypockethealth.R;

import java.util.Calendar;

public class DatePickerActivity extends DialogFragment {


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), R.style.dialog_theme,(DatePickerDialog.OnDateSetListener) getActivity(),
                year,month,day);
    }
}