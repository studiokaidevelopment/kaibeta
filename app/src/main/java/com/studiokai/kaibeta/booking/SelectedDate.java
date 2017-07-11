package com.studiokai.kaibeta.booking;

import java.util.Date;

/**
 * Created by titusjuocepis on 6/21/17.
 */

class SelectedDate {

    private static Date mDate;
    private static String mDayOfWeek;
    private static int mYear, mMonth, mDay;

    static void setSelectedDate(Date date, String dayOfWeek, int year, int month, int day) {
        mDate = date;
        mDayOfWeek = dayOfWeek;
        mYear = year;
        mMonth = month;
        mDay = day;
    }

    public static Date getDate() {
        return mDate;
    }

    public static String getDayOfWeek() {
        return mDayOfWeek;
    }

    public static int getYear() {
        return mYear;
    }

    public static int getMonth() {
        return mMonth;
    }

    public static int getDay() {
        return mDay;
    }
}
