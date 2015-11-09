package com.joymeter.androidclient.dialog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;

import com.joymeter.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by hramovecchi on 03/11/2015.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private Calendar c;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        c.set(year, month, day);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("DATE_PICKED", getFormatedDate());

        Fragment f = getTargetFragment();

        f.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, resultIntent);
    }

    public long getTimeInMillis(){
        return c.getTimeInMillis();
    }

    public String getFormatedDate(){
        return DateUtils.getInstance().getFormatedDate(c.getTime());
    }
}
