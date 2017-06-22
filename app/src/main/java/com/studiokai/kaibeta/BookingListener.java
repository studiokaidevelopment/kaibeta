package com.studiokai.kaibeta;

import android.widget.EditText;

import java.util.List;

/**
 * Created by titusjuocepis on 6/20/17.
 */

public interface BookingListener {

    void onTimesReady(List<ModelBookingListItem> events);

    void onErrorGettingEvents(String msg);
}
