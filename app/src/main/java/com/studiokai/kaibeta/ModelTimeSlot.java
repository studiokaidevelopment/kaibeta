package com.studiokai.kaibeta;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Comparator;

/**
 * Created by titusjuocepis on 6/19/17.
 */

class ModelTimeSlot implements Comparable<ModelTimeSlot> {

    private String startTime, endTime, dateStart, dateEnd;
    private boolean isAvailable, isChecked;

    ModelTimeSlot(String start, String end, boolean available, String dStart, String dEnd) {
        startTime = start;
        endTime = end;
        isAvailable = available;
        dateStart = dStart;
        dateEnd = dEnd;
    }

    String getStartTime() {
        return startTime;
    }

    String getEndTime() {
        return endTime;
    }

    String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    boolean isAvailable() {
        return isAvailable;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public int compareTo(@NonNull ModelTimeSlot o) {

        int startInt = Integer.parseInt(startTime.substring(0, 2));
        int secondStartInt = Integer.parseInt(o.getStartTime().substring(0, 2));

        Log.d("[MODEL_TIME_SLOT] ---> ", "startInt = " + startInt + "secondInt = " + secondStartInt);

        return startInt - secondStartInt;
    }

    public static class ModelTimeSlotComparator implements Comparator<ModelTimeSlot> {

        @Override
        public int compare(ModelTimeSlot o1, ModelTimeSlot o2) {
            return o1.compareTo(o2);
        }
    }
}
