package com.joymeter.androidclient;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;

import com.joymeter.androidclient.dialog.DatePickerFragment;
import com.joymeter.dto.ActivityDTO;

import java.util.Calendar;

/**
 * A placeholder fragment containing a simple view.
 */
public class SingleActivityFragment extends Fragment {

    private static final int PICK_DATE = 1;

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
    }

    private void populateActivity() {

        ActivityDTO activity = (ActivityDTO) getActivity().getIntent().getSerializableExtra("joymeterActivity");

        if (activity != null){
            summary.setText(activity.getSummary());
            type.setText(activity.getType());
            initial.setText(String.valueOf(activity.getStartDate()));
            duration.setText(String.valueOf(activity.getEndDate()));
            description.setText(activity.getDescription());
            loj.setRating(Long.valueOf(activity.getLevelOfJoy()));
            share.setChecked(!activity.getClassified());
            id = activity.getId();
        }

        initial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dateFragment = new DatePickerFragment();
                dateFragment.setTargetFragment(getTargetFragment(), PICK_DATE);
                dateFragment.show(getActivity().getFragmentManager(), "datePicker");
            }
        });
    }

    public ActivityDTO getActivityDTO(){
        ActivityDTO activity= new ActivityDTO();
        activity.setSummary(summary.getText().toString());
        activity.setType(type.getText().toString());

        //TODO initial is always the current time for now
        long now = Calendar.getInstance().getTimeInMillis();
        activity.setStartDate(now);
        activity.setEndDate(now + (Long.valueOf(duration.getText().toString()) * 60 + 1000));

        activity.setDescription(description.getText().toString());
        activity.setLevelOfJoy(Math.round(loj.getRating()));
        activity.setClassified(!share.isChecked());

        if (id != null){
            activity.setId(id);
        }

        return activity;
    }
}
