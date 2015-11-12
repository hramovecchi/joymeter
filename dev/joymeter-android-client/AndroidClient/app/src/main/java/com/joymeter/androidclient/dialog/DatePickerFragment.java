package com.joymeter.androidclient.dialog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;

import com.joymeter.utils.DateUtils;

import java.util.Calendar;

/**
 * Created by hramovecchi on 03/11/2015.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private static final int PICK_TIME = 1;

    private Calendar c;
    private DatePickerDialog datePickerDialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle setDate = this.getArguments();

        // Use the current date as the default date in the picker
        c = Calendar.getInstance();
        c.setTimeInMillis(setDate.getLong("date"));

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        return datePickerDialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        c.set(year, month, day);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("DATE_PICKED", c.getTimeInMillis());

        Bundle args = new Bundle();
        args.putLong("date", c.getTimeInMillis());

        DialogFragment timeFragment = new TimePickerFragment();
        timeFragment.setArguments(args);
        timeFragment.setTargetFragment(this, PICK_TIME);
        timeFragment.show(getActivity().getFragmentManager(), "timePicker");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_TIME) {
            if (resultCode == Activity.RESULT_OK) {
                Long date = data.getLongExtra("DATE_AND_TIME_PICKED", 0L);

                Intent resultIntent = new Intent();
                resultIntent.putExtra("DATE_PICKED", date);
                Fragment f = getTargetFragment();
                f.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, resultIntent);
            }
        }
    }
}
