/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.studiokai.kaibackend;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.sun.javafx.runtime.async.AsyncOperation;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.logging.Logger;

import javax.inject.Named;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "myApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "kaibackend.studiokai.com",
                ownerName = "kaibackend.studiokai.com",
                packagePath = ""
        )
)
public class MyEndpoint {

    /**
     * A simple endpoint method that takes a name and says Hi back
     */
    @ApiMethod(name = "test")
    public MyBean test(@Named("minDate") String minDate, @Named("maxDate") String maxDate) {

//        Date min = new Date(Long.parseLong(minDate));
//        Date max = new Date(Long.parseLong(maxDate));
//        CalendarRequestTask getEventsTask = new CalendarRequestTask();
//        String events = "NULL";
//
//        try {
//            events = getEventsTask.getDataFromApi(min, max);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
////        StringBuilder sBuilder = new StringBuilder();
//
//
////        for (String event : events) {
////            sBuilder.append(event);
////        }
//
//        MyBean response = new MyBean();
//
////        if (events.isEmpty())
////            response.setData("EMPTY!");
////        else
////            response.setData(sBuilder.toString());
//
//        response.setData(events);

        Calendar mService;
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        HttpTransport transport = null;

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

        Events events = null;
        try {
            events = mService.events().list("primary")
                    .setSingleEvents(true)
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert events != null;
        List<Event> items = events.getItems();

        MyBean response = new MyBean();
        response.setData("SIZE = " + items.size());

        return response;
    }

    @ApiMethod(name = "sayHi")
    public MyBean sayHi(@Named("name") String name) {

        MyBean response = new MyBean();
        response.setData("Test = " + name);

        return response;
    }

}
