package com.joymeter.utils;

import android.net.Uri;

import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.joymeter.dto.ActivityDTO;

/**
 * Created by hramovecchi on 08/03/2016.
 */
public class ShareUtils {

    public static ShareContent joymeterShareLinkContent(ActivityDTO activity){
        return new ShareLinkContent.Builder()
                .setContentTitle("JoyMeter Share Activity: " + activity.getSummary())
                .setContentDescription(activity.getDescription())
                .setContentUrl(Uri.parse("http://joymeter-joymeterwebapi.rhcloud.com"))
                .build();
    }
}
