package com.studiokai.kaibeta;

import android.support.annotation.NonNull;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by titusjuocepis on 6/20/17.
 */

class ModelBookingListItem implements Comparable<ModelBookingListItem> {

    private final String mStart, mEnd;
    private boolean isAvailable, isChecked;

    ModelBookingListItem(String start, String end, boolean available) {
        mStart = start;
        mEnd = end;
        isAvailable = available;
    }

    String getStart() {
        return mStart;
    }

    String getStartTime() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        SimpleDateFormat timeFormat = new SimpleDateFormat("H:mm");
        Date date = null;

        try {
            date = sdf.parse(mStart);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timeFormat.format(date);
    }

    String getEnd() {
        return mEnd;
    }

    String getEndTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        SimpleDateFormat timeFormat = new SimpleDateFormat("H:mm");
        Date date = null;

        try {
            date = sdf.parse(mEnd);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timeFormat.format(date);
    }

    boolean isAvailable() {
        return isAvailable;
    }

    void setAvailable(boolean available) {
        isAvailable = available;
    }

    boolean isChecked() {
        return isChecked;
    }

    void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public int compareTo(@NonNull ModelBookingListItem item) {

        String startTime = getStartTime().substring(0, 2);
        String otherStartTime = item.getStartTime().substring(0, 2);

        return Integer.parseInt(startTime) - Integer.parseInt(otherStartTime);
    }

    static class BookingListItemComparator implements Comparator<ModelBookingListItem> {

        @Override
        public int compare(ModelBookingListItem item1, ModelBookingListItem item2) {
            return item1.compareTo(item2);
        }
    }
}
