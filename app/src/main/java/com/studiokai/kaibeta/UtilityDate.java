package com.studiokai.kaibeta;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by titusjuocepis on 6/21/17.
 */

public class UtilityDate {

    public static String convertToTime(String dateString) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        SimpleDateFormat timeFormat = new SimpleDateFormat("H:mm");
        Date date = null;

        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timeFormat.format(date);
    }
}
