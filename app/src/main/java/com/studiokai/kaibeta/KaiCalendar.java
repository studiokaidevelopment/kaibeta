package com.studiokai.kaibeta;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

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

class KaiCalendar {

    private KaiCalendarListener kaiCalendarListener;

    void setCalendarListener(KaiCalendarListener listener) {
        kaiCalendarListener = listener;
    }

    void insertEvent(ModelEvent event) {
        InsertEventTask insertEventTask = new InsertEventTask();
        Gson gson = new Gson();
        insertEventTask.execute(gson.toJson(event));
    }

    void loadEvents(String start, String end) {
        GetEventsTask task = new GetEventsTask();
        task.execute(start, end);
    }

    private class InsertEventTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            return HTTP.GET("http://10.0.2.2:8080/createEvent?event="+params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            kaiCalendarListener.onEventInserted(s);
        }
    }

    private class GetEventsTask extends AsyncTask<String, Void, String> {

        String mStart, mEnd, dayOfWeek;

        @Override
        protected String doInBackground(String... params) {
            mStart = params[0];
            mEnd = params[1];
            return HTTP.GET("http://10.0.2.2:8080/getEvents?start="+mStart+"&end="+mEnd);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            JSONArray json = null;

            try {
                json = new JSONArray(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            List<ModelEvent> eventsList = null;

            if (json != null) {

                eventsList = new ArrayList<>();

                for (int i = 0; i < json.length(); i++) {

                    try {

                        String start = json.getJSONObject(i).getString("mStart");
                        String end = json.getJSONObject(i).getString("mEnd");
                        String summary = json.getJSONObject(i).getString("mSummary");
                        String description = json.getJSONObject(i).getString("mDescription");
                        String location = json.getJSONObject(i).getString("mLocation");
                        List<String> attendees = new ArrayList<>();

                        Log.d("[LOAD EVENTS] ---> ", "Start = " + start);

                        JSONArray attendeeArray = json.getJSONObject(i).getJSONArray("mAttendees");

                        for (int j = 0; j < attendeeArray.length(); j++) {
                            attendees.add(attendeeArray.getString(j));
                        }

                        eventsList.add(new ModelEvent(start, end, summary, description, location, attendees));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            kaiCalendarListener.onEventsLoaded(eventsList);
        }
    }
}
