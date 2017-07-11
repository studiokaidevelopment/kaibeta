package com.studiokai.kaibeta.booking;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by titusjuocepis on 6/20/17.
 */

public class BookingManager implements KaiCalendarListener {

    private BookingListener bookingListener;
    private HashMap<String, ModelBookingListItem> allEvents;
    private List<ModelEvent> existingEvents;
    private Date selectedDate;
    private String dayOfWeek;

    @Override
    public void onEventsLoaded(List<ModelEvent> events) {

        existingEvents = events;

        if (events == null) {
            bookingListener.onErrorGettingEvents("There was an error connecting to our server");
            return;
        }

        dayOfWeek = SelectedDate.getDayOfWeek();
        setAllEvents(SelectedDate.getYear(), SelectedDate.getMonth(), SelectedDate.getDay());
        setUnavailableEvents(existingEvents);

        List<ModelBookingListItem> bookingEventsList = new ArrayList<>(allEvents.values());
        Collections.sort(bookingEventsList, new ModelBookingListItem.BookingListItemComparator());

        bookingListener.onTimesReady(bookingEventsList);
    }

    @Override
    public void onEventInserted(String response) {
        // TODO
    }

    void setBookingListener(BookingListener listener) {
        bookingListener = listener;
    }

    static String getDayOfWeek(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        return sdf.format(date);
    }

    static String getDayOfWeek(String date) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        SimpleDateFormat dayOfWeekFormat = new SimpleDateFormat("EEEE");

        try {
            return dayOfWeekFormat.format(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "INVALID";
    }

    private void setUnavailableEvents(List<ModelEvent> existingEvents) {

        for (ModelEvent event : existingEvents) {

            String start = event.mStart;
            String end = event.mEnd;

            if (allEvents.containsKey(start)) {

                allEvents.remove(start);
                allEvents.put(start, new ModelBookingListItem(start, end, false));
            }
        }
    }

    private void setAllEvents(int year, int month, int day) {

        allEvents = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        Calendar cal = Calendar.getInstance();

        cal.set(year, month, day, 11, 0, 0);
        String start = sdf.format(cal.getTime());
        cal.set(year, month, day, 12, 0, 0);
        String end = sdf.format(cal.getTime());
        allEvents.put(start, new ModelBookingListItem(start, end, true));

        cal.set(year, month, day, 12, 0, 0);
        start = sdf.format(cal.getTime());
        cal.set(year, month, day, 13, 0, 0);
        end = sdf.format(cal.getTime());
        allEvents.put(start, new ModelBookingListItem(start, end, true));

        cal.set(year, month, day, 13, 0, 0);
        start = sdf.format(cal.getTime());
        cal.set(year, month, day, 14, 0, 0);
        end = sdf.format(cal.getTime());
        allEvents.put(start, new ModelBookingListItem(start, end, true));

        cal.set(year, month, day, 14, 0, 0);
        start = sdf.format(cal.getTime());
        cal.set(year, month, day, 15, 0, 0);
        end = sdf.format(cal.getTime());
        allEvents.put(start, new ModelBookingListItem(start, end, true));

        cal.set(year, month, day, 15, 0, 0);
        start = sdf.format(cal.getTime());
        cal.set(year, month, day, 16, 0, 0);
        end = sdf.format(cal.getTime());
        allEvents.put(start, new ModelBookingListItem(start, end, true));

        cal.set(year, month, day, 16, 0, 0);
        start = sdf.format(cal.getTime());
        cal.set(year, month, day, 17, 0, 0);
        end = sdf.format(cal.getTime());
        allEvents.put(start, new ModelBookingListItem(start, end, true));

        cal.set(year, month, day, 17, 0, 0);
        start = sdf.format(cal.getTime());
        cal.set(year, month, day, 18, 0, 0);
        end = sdf.format(cal.getTime());
        allEvents.put(start, new ModelBookingListItem(start, end, true));

        cal.set(year, month, day, 18, 0, 0);
        start = sdf.format(cal.getTime());
        cal.set(year, month, day, 19, 0, 0);
        end = sdf.format(cal.getTime());
        allEvents.put(start, new ModelBookingListItem(start, end, true));

        cal.set(year, month, day, 19, 0, 0);
        start = sdf.format(cal.getTime());
        cal.set(year, month, day, 20, 0, 0);
        end = sdf.format(cal.getTime());
        allEvents.put(start, new ModelBookingListItem(start, end, true));

        if (dayOfWeek.equals("Saturday")) {

            cal.set(year, month, day, 20, 0, 0);
            start = sdf.format(cal.getTime());
            cal.set(year, month, day, 21, 0, 0);
            end = sdf.format(cal.getTime());
            allEvents.put(start, new ModelBookingListItem(start, end, true));

            cal.set(year, month, day, 21, 0, 0);
            start = sdf.format(cal.getTime());
            cal.set(year, month, day, 22, 0, 0);
            end = sdf.format(cal.getTime());
            allEvents.put(start, new ModelBookingListItem(start, end, true));

            cal.set(year, month, day, 22, 0, 0);
            start = sdf.format(cal.getTime());
            cal.set(year, month, day, 23, 0, 0);
            end = sdf.format(cal.getTime());
            allEvents.put(start, new ModelBookingListItem(start, end, true));
        }
        else {

            cal.set(year, month, day, 10, 0, 0);
            start = sdf.format(cal.getTime());
            cal.set(year, month, day, 11, 0, 0);
            end = sdf.format(cal.getTime());
            allEvents.put(start, new ModelBookingListItem(start, end, true));
        }
    }
}
