package com.studiokai.kaibackend;

/**
 * Created by titusjuocepis on 6/16/17.
 */

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarRequestInitializer;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.google.appengine.api.appidentity.AppIdentityService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An asynchronous task that handles the Google Calendar API call.
 * Placing the API calls in their own task ensures the UI stays responsive.
 */
class CalendarRequestTask {

    private Calendar mService;
    private Logger mLogger;

    CalendarRequestTask() {

        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        HttpTransport transport = null;
        mLogger = Logger.getLogger(CalendarRequestTask.class.getName());

        try {
            transport = GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }

        GoogleCredential credential = null;
        try {
            credential = GoogleCredential.getApplicationDefault()
                    .createScoped(Collections.singleton(CalendarScopes.CALENDAR_READONLY));
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert transport != null;
        mService = new Calendar.Builder(transport, jsonFactory, credential).setApplicationName("Kai Backend").build();
    }

    /**
     * Fetch a list of the next 10 events from the primary calendar.
     * @return List of Strings describing returned events.
     * @throws IOException
     */
    String getDataFromApi(Date minDate, Date maxDate) throws IOException {

        DateTime min = new DateTime(minDate);
        DateTime max = new DateTime(maxDate);
        ArrayList<String> eventStrings = new ArrayList<>();

        Events events = mService.events().list("studiokaidevelopment@gmail.com")
                .setMaxResults(10)
//                .setTimeMin(min)
//                .setTimeMax(max)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();

        List<Event> items = events.getItems();

//        for (Event event : items) {
//            DateTime start = event.getStart().getDateTime();
//            if (start == null) {
//                // All-day events don't have start times, so just use
//                // the start date.
//                start = event.getStart().getDate();
//            }
//            eventStrings.add(
//                    String.format("%s (%s) %s", event.getSummary(), start, event.getLocation()));
//        }

        mLogger.info(items.size()+"");
        return items.get(0).getSummary();
    }
}