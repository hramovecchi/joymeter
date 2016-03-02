package com.joymeter.androidclient.cursor;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.joymeter.androidclient.R;
import com.joymeter.dto.ActivityDTO;

import java.util.List;

/**
 * Created by hramovecchi on 20/08/2015.
 */
public class ActivityArrayAdapter extends ArrayAdapter<ActivityDTO>{

    public ActivityArrayAdapter(Context context, List<ActivityDTO> activities) {
        super(context, R.layout.activity_list_item, activities);
    }

    private class ViewHolder{
        ImageView img;
        TextView title;
        TextView description;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder;

        if (convertView == null){

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.activity_list_item, parent, false);

            //initialize the view holder
            holder = new ViewHolder();
            holder.img = (ImageView)convertView.findViewById(R.id.icon);
            holder.title = (TextView)convertView.findViewById(R.id.firstLine);
            holder.description = (TextView)convertView.findViewById(R.id.secondLine);

            convertView.setTag(holder);

        } else {
            // recycle the already inflated view
            holder = (ViewHolder) convertView.getTag();
        }

        // update the item view
        ActivityDTO activity = getItem(position);
        holder.img.setImageDrawable(getLevelOfJoyImage(activity.getLevelOfJoy()));
        holder.title.setText(activity.getSummary());
        holder.description.setText(activity.getDescription());

        return convertView;
    }

    private Drawable getLevelOfJoyImage(Integer levelOfJoy) {
        switch (levelOfJoy){
            case 1:
                return getContext().getResources().getDrawable(R.mipmap.loj_1);
            case 2:
                return getContext().getResources().getDrawable(R.mipmap.loj_2);
            case 3:
                return getContext().getResources().getDrawable(R.mipmap.loj_3);
            case 4:
                return getContext().getResources().getDrawable(R.mipmap.loj_4);
            case 5:
                return getContext().getResources().getDrawable(R.mipmap.loj_5);
            default:
                return getContext().getResources().getDrawable(R.mipmap.loj_5);
        }
    }
}