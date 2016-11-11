package com.joymeter.androidclient;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;

import com.joymeter.androidclient.picker.DatePickerFragment;
import com.joymeter.androidclient.picker.DurationPickerFragment;
import com.joymeter.dto.ActivityDTO;
import com.joymeter.dto.AdviceDTO;
import com.joymeter.events.bus.DatePickedEvent;
import com.joymeter.events.bus.DurationPickedEvent;
import com.joymeter.events.bus.EventsBus;
import com.joymeter.events.bus.SuggestActivityEvent;
import com.joymeter.utils.DateUtils;
import com.joymeter.utils.DurationUtils;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;

/**
 * A placeholder fragment containing a simple view.
 */
public class SingleActivityFragment extends Fragment {

    private Long initialDate = null;
    private int hoursDuration = 0;
    private int minutesDuration = 0;

    private Long id;

    private EditText summary;
    private Spinner spinnerTypes;
    private EditText initial;
    private EditText duration;
    private EditText description;
    private RatingBar loj;
    private CheckBox share;

    private  ArrayAdapter<String> typeAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        EventsBus.getInstance().register(this);
        return inflater.inflate(R.layout.fragment_single, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Set<String> activitiesTypes = preferences.getStringSet(JoymeterPreferences.ACTIVITIES_TYPES, null);

        summary = (EditText)view.findViewById(R.id.summaryInput);
        spinnerTypes = (Spinner)view.findViewById(R.id.types_spinner);

        typeAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, activitiesTypes.toArray(new String[activitiesTypes.size()]));
        // Specify the layout to use when the list of choices appears
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerTypes.setAdapter(typeAdapter);

        initial = (EditText)view.findViewById(R.id.initialInput);
        duration = (EditText)view.findViewById(R.id.durationInput);
        description = (EditText)view.findViewById(R.id.descriptionInput);
        loj = (RatingBar)view.findViewById(R.id.levelOfJoyBar);
        share = (CheckBox)view.findViewById(R.id.shareCheckBox);

        populateActivity();

        if (initialDate == null){
            initialDate = Calendar.getInstance().getTimeInMillis();
        }
    }

    private void populateActivity() {
        ActivityDTO activity;

        AdviceDTO advice = (AdviceDTO)getActivity().getIntent().getSerializableExtra(JoymeterPreferences.JOYMETER_ADVICE);
        if (advice == null){
            activity = (ActivityDTO)getActivity().getIntent().getSerializableExtra(JoymeterPreferences.JOYMETER_ACTIVITY);
        } else {
            activity = advice.getSuggestedActivity();
        }

        if (activity != null){
            DateUtils util = DateUtils.getInstance();

            summary.setText(activity.getSummary());
            spinnerTypes.setSelection(typeAdapter.getPosition(activity.getType()));

            initial.setText(util.getFormatedDate(util.getDate(activity.getStartDate())));
            initialDate = activity.getStartDate();

            long durationInMillis = activity.getEndDate() - activity.getStartDate();

            hoursDuration = DurationUtils.getInstance().getHours(durationInMillis);
            minutesDuration = DurationUtils.getInstance().getMinutes(durationInMillis);
            duration.setText(DurationUtils.getInstance().getDuration(hoursDuration, minutesDuration));

            description.setText(activity.getDescription());
            loj.setRating(Long.valueOf(activity.getLevelOfJoy()));
            share.setChecked(!activity.isClassified());
            id = activity.getId();
        }

        initial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putLong("date", initialDate);

                DialogFragment dateFragment = new DatePickerFragment();
                dateFragment.setArguments(args);
                dateFragment.show(getActivity().getFragmentManager(), "datePicker");
            }
        });

        duration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putInt("hours", hoursDuration);
                args.putInt("minutes", minutesDuration);

                DialogFragment durationFragment = new DurationPickerFragment();
                durationFragment.setArguments(args);
                durationFragment.show(getActivity().getFragmentManager(), "durationPicker");
            }
        });
    }

    @Override
    public void onDestroy() {
        EventsBus.getInstance().unregister(this);
        super.onDestroy();
    }

    public ActivityDTO getActivityDTO(){
        ActivityDTO activity= new ActivityDTO();
        activity.setSummary(summary.getText().toString());
        activity.setType(spinnerTypes.getSelectedItem().toString());
        activity.setStartDate(initialDate);
        activity.setEndDate(DurationUtils.getInstance().getEndDate(initialDate, hoursDuration, minutesDuration));
        activity.setDescription(description.getText().toString());
        activity.setLevelOfJoy(Math.round(loj.getRating()));
        activity.setClassified(!share.isChecked());

        if (id != null){
            activity.setId(id);
        }

        return activity;
    }

    @Subscribe
    public void onDatePicked(DatePickedEvent event){
        initialDate = event.getTimeInMillis();
        initial.setText(DateUtils.getInstance().getFormatedDate(initialDate));
    }

    @Subscribe
    public void onDurationPicked(DurationPickedEvent event){
        hoursDuration = event.getHours();
        minutesDuration = event.getMinutes();
        duration.setText(DurationUtils.getInstance().getDuration(hoursDuration, minutesDuration));
    }
}
