package com.joymeter.androidclient.cursor;

import android.content.Context;
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

    private final Context context;

    public ActivityArrayAdapter(Context context, List<ActivityDTO> activities) {
        super(context, R.layout.activity_list_item ,activities);
        this.context = context;
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

            LayoutInflater inflater = LayoutInflater.from(context);
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
        //TODO set an image for an activity
        //holder.img.setImageDrawable();
        holder.title.setText(activity.getSummary());
        holder.description.setText(activity.getDescription());

        return convertView;
    }
}
