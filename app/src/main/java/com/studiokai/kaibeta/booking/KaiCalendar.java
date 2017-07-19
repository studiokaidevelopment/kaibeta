package com.studiokai.kaibeta.booking;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.studiokai.kaibeta.utilities.HTTP;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by titusjuocepis on 6/20/17.
 */

class KaiCalendar {

    private KaiCalendarListener kaiCalendarListener;

    void setCalendarListener(KaiCalendarListener listener) {

        kaiCalendarListener = listener;
    }


    // INSERT EVENT

    private class InsertEventTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            return HTTP.GET("http://10.0.2.2:8080/events/insert?event="+params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            kaiCalendarListener.onEventInserted(s);
        }
    }

    void insertEvent(ModelEvent event) {
        InsertEventTask insertEventTask = new InsertEventTask();
        Gson gson = new Gson();
        insertEventTask.execute(gson.toJson(event));
    }


    // GET EVENTS

    private class GetEventsTask extends AsyncTask<String, Void, String> {

        String mStart, mEnd;

        @Override
        protected String doInBackground(String... params) {

            mStart = params[0];
            mEnd = params[1];

            return HTTP.GET("http://10.0.2.2:8080/events/start/"+mStart+"/end/"+mEnd);
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);

            List<ModelEvent> events = parseEventsResponse(s);
            kaiCalendarListener.onEventsLoaded(events);
        }
    }

    void loadEvents(String start, String end) {
        GetEventsTask task = new GetEventsTask();
        task.execute(start, end);
    }

    private List<ModelEvent> parseEventsResponse(String response) {

        JSONArray json = null;

        try {
            json = new JSONArray(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        List<ModelEvent> eventsList = null;

        if (json != null) {

            eventsList = new ArrayList<>();

            for (int i = 0; i < json.length(); i++) {

                String start = "";
                String startTimeZone = "";
                String end = "";
                String endTimeZone = "";
                String summary = "";
                String description = "";
                String location = "";

                try {

                    start = json.getJSONObject(i).getJSONObject("start").getString("dateTime");
                    end = json.getJSONObject(i).getJSONObject("end").getString("dateTime");
                    summary = json.getJSONObject(i).getString("summary");
                    description = json.getJSONObject(i).getString("description");
                    location = json.getJSONObject(i).getString("location");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                eventsList.add(new ModelEvent(new ModelTime(start, startTimeZone),
                        new ModelTime(end, endTimeZone), summary, description,
                        location, null));
            }
        }

        return eventsList;
    }
}
