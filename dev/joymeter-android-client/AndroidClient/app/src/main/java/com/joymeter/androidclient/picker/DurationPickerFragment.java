package com.joymeter.androidclient.picker;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import com.joymeter.androidclient.picker.dialog.DurationPickerDialog;
import com.joymeter.events.bus.DatePickedEvent;
import com.joymeter.events.bus.DurationPickedEvent;
import com.joymeter.events.bus.EventsBus;

import java.util.Calendar;

/**
 * Created by hramovecchi on 12/11/2015.
 */
public class DurationPickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private Calendar c;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle setDuration = this.getArguments();

        int hour = setDuration.getInt("hours");
        int minute = setDuration.getInt("minutes");

        TimePickerDialog durationPicker =  new DurationPickerDialog(getActivity(), this, hour, minute);
        durationPicker.setTitle("Duración:");

        return durationPicker;
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        EventsBus.getInstance().post(new DurationPickedEvent(hourOfDay, minute));
    }
}
