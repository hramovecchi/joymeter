package com.joymeter.androidclient;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;

import com.joymeter.androidclient.picker.DatePickerFragment;
import com.joymeter.androidclient.picker.DurationPickerFragment;
import com.joymeter.dto.ActivityDTO;
import com.joymeter.dto.AdviceDTO;
import com.joymeter.utils.DateUtils;
import com.joymeter.utils.DurationUtils;

import java.util.Calendar;

/**
 * A placeholder fragment containing a simple view.
 */
public class SingleActivityFragment extends Fragment {

    private static final int PICK_DATE = 1;
    private static final int PICK_DURATION = 2;

    private Long initialDate = null;
    private int hoursDuration = 0;
    private int minutesDuration = 0;

    private Long id;

    private EditText summary;
    private EditText type;
    private EditText initial;
    private EditText duration;
    private EditText description;
    private RatingBar loj;
    private CheckBox share;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_single, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        summary = (EditText)view.findViewById(R.id.summaryInput);
        type = (EditText)view.findViewById(R.id.typeInput);
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
            type.setText(activity.getType());

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
                dateFragment.setTargetFragment(getSingleActivityFragment(), PICK_DATE);
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
                durationFragment.setTargetFragment(getSingleActivityFragment(), PICK_DURATION);
                durationFragment.show(getActivity().getFragmentManager(), "durationPicker");
            }
        });
    }

    public ActivityDTO getActivityDTO(){
        ActivityDTO activity= new ActivityDTO();
        activity.setSummary(summary.getText().toString());
        activity.setType(type.getText().toString());
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_DATE) {
            if (resultCode == Activity.RESULT_OK) {
                initialDate = data.getLongExtra("DATE_PICKED", 0L);
                initial.setText(DateUtils.getInstance().getFormatedDate(initialDate));
            }
        } else  if (requestCode == PICK_DURATION) {
            if (resultCode == Activity.RESULT_OK) {
                hoursDuration = data.getIntExtra("HOURS_PICKED", 0);
                minutesDuration = data.getIntExtra("MINUTES_PICKED", 0);
                duration.setText(DurationUtils.getInstance().getDuration(hoursDuration, minutesDuration));
            }
        }
    }

    public Fragment getSingleActivityFragment(){
        return this;
    }
}
