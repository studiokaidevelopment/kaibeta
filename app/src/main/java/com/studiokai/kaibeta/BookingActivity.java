package com.studiokai.kaibeta;

import android.os.Bundle;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RelativeLayout;

import com.google.gson.Gson;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class BookingActivity extends Activity implements View.OnClickListener {

    private TextView mOutputText;
    private Button buttonDateSelect, buttonBookIt;
    private RelativeLayout bookingLayout;
    AdapterBookingList adapter;
    View.OnClickListener listener;
    ArrayList<ModelTimeSlot> times;

    private static DatePickerDialog.OnDateSetListener dateSetListener;

    /**
     * Create the main activity.
     * @param savedInstanceState previously saved instance data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        mOutputText = (TextView) findViewById(R.id.text_output);
        bookingLayout = (RelativeLayout) findViewById(R.id.bookingLayout);
        buttonDateSelect = (Button) findViewById(R.id.button_update);
        buttonBookIt = (Button) findViewById(R.id.button_book_it);

        times = new ArrayList<>();

        buttonDateSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(bookingLayout);
            }
        });

        buttonBookIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<String> attendees = new ArrayList<String>();
                attendees.add("tjuocepis@gmail.com");
                ModelTimeSlot timeSlot = times.get(0);
                ModelEvent event = new ModelEvent(timeSlot.getDateStart(), timeSlot.getDateEnd(),
                        "Studio Kai Session", "1 Hour of Recording",
                        "16269 W Woodbine Circle, Vernon Hills, IL 60061", attendees);
                InsertEventTask insertEventTask = new InsertEventTask();
                Gson gson = new Gson();
                insertEventTask.execute(gson.toJson(event));
            }
        });

        listener = this;

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                Date date = new Date(year, month, dayOfMonth-1);
                String dayOfWeek = sdf.format(date);
                String dateMin, dateMax;
                TextView summary = (TextView) findViewById(R.id.text_booking_summary);

                if (dayOfWeek.equals("Saturday")) {
                    dateMin = toRFC3339(year, month, dayOfMonth, 11, 0, 0);
                    dateMax = toRFC3339(year, month, dayOfMonth, 22, 59, 59);
                }
                else {
                    dateMin = toRFC3339(year, month, dayOfMonth, 10, 0, 0);
                    dateMax = toRFC3339(year, month, dayOfMonth, 19, 59, 59);
                }

                if (dayOfWeek.equals("Sunday")) {
                    mOutputText.setVisibility(View.GONE);
                    summary.setVisibility(View.VISIBLE);
                    summary.setText("We are not open on " + dayOfWeek);
                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.events_list);
                    recyclerView.setAdapter(new AdapterBookingList(new ArrayList<ModelTimeSlot>(), listener));
                }
                else {
                    mOutputText.setVisibility(View.VISIBLE);
                    mOutputText.setText("Select Available Time:");
                    summary.setVisibility(View.GONE);
                    GetEventsTask task = new GetEventsTask(getBaseContext(), listener);
                    task.execute(dateMin, dateMax, dayOfWeek);

                }

                buttonDateSelect.setText(month+1 + "/" + dayOfMonth + "/" + year);
                buttonBookIt.setVisibility(View.GONE);
            }
        };

    }

    public void showDatePickerDialog(View v) {

        DialogFragment newFragment = new BookingActivity.DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public String toRFC3339(int year, int month, int day, int h, int m, int s) {

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, h, m, s);
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

        return sdf.format(date);
    }

    @Override
    public void onClick(View v) {

        times = adapter.getCheckedTimes();

        if (times.isEmpty())
            buttonBookIt.setVisibility(View.GONE);
        else
            buttonBookIt.setVisibility(View.VISIBLE);

        for (int i = 0; i < times.size(); i++) {
            Toast.makeText(this, times.get(i).getDateStart(), Toast.LENGTH_SHORT).show();
        }
    }

    private class InsertEventTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            return GET("http://10.0.2.2:8080/createEvent?event="+params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(BookingActivity.this, s, Toast.LENGTH_SHORT).show();
        }
    }

    private class GetEventsTask extends AsyncTask<String, Void, String> {

        Context mContext;
        View.OnClickListener mListener;
        String dayOfWeek;

        GetEventsTask(Context context, View.OnClickListener listener) {
            mContext = context;
            mListener = listener;
        }

        @Override
        protected String doInBackground(String... params) {
            dayOfWeek = params[2];
            return GET("http://10.0.2.2:8080/getEvents?start="+params[0]+"&end="+params[1]);
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

            String start, end = "";
            HashMap<String, ModelTimeSlot> timeSlots = makeTimeSlots();

            if (json != null) {

                for (int i = 0; i < json.length(); i++) {

                    try {
                        start = json.getJSONObject(i).getString("mStart");
                        end = json.getJSONObject(i).getString("mEnd");
                        String startTime = "";
                        String endTime = "";

                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
                            Date startDate = sdf2.parse(start);
                            Date endDate = sdf2.parse(end);
                            startTime = sdf.format(startDate);
                            endTime = sdf.format(endDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (timeSlots.containsKey(startTime)) {

                            timeSlots.remove(start);
                            timeSlots.put(startTime, new ModelTimeSlot(startTime, endTime, false, start, end));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            else {

                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.events_list);
                recyclerView.setAdapter(new AdapterBookingList(new ArrayList<ModelTimeSlot>(), listener));
                mOutputText.setText("We apologize, but there was an error with our server");

                return;
            }

            ArrayList<ModelTimeSlot> timesList = new ArrayList<>(timeSlots.values());
            Collections.sort(timesList, new ModelTimeSlot.ModelTimeSlotComparator());

            adapter = new AdapterBookingList(timesList, mListener);
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.events_list);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(adapter);
        }

        private HashMap<String, ModelTimeSlot> makeTimeSlots() {

            HashMap<String, ModelTimeSlot> map = new HashMap<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            cal.set(year, month, day, 11, 0, 0);
            String start = sdf.format(cal.getTime());
            cal.set(year, month, day, 12, 0, 0);
            String end = sdf.format(cal.getTime());
            map.put("11:00", new ModelTimeSlot("11:00", "12:00", true, start, end));
            cal.set(year, month, day, 12, 0, 0);
            start = sdf.format(cal.getTime());
            cal.set(year, month, day, 13, 0, 0);
            end = sdf.format(cal.getTime());
            map.put("12:00", new ModelTimeSlot("12:00", "13:00", true, start, end));
            cal.set(year, month, day, 13, 0, 0);
            start = sdf.format(cal.getTime());
            cal.set(year, month, day, 14, 0, 0);
            end = sdf.format(cal.getTime());
            map.put("13:00", new ModelTimeSlot("13:00", "14:00", true, start, end));
            cal.set(year, month, day, 14, 0, 0);
            start = sdf.format(cal.getTime());
            cal.set(year, month, day, 15, 0, 0);
            end = sdf.format(cal.getTime());
            map.put("14:00", new ModelTimeSlot("14:00", "15:00", true, start, end));
            cal.set(year, month, day, 15, 0, 0);
            start = sdf.format(cal.getTime());
            cal.set(year, month, day, 16, 0, 0);
            end = sdf.format(cal.getTime());
            map.put("15:00", new ModelTimeSlot("15:00", "16:00", true, start, end));
            cal.set(year, month, day, 16, 0, 0);
            start = sdf.format(cal.getTime());
            cal.set(year, month, day, 17, 0, 0);
            end = sdf.format(cal.getTime());
            map.put("16:00", new ModelTimeSlot("16:00", "17:00", true, start, end));
            cal.set(year, month, day, 17, 0, 0);
            start = sdf.format(cal.getTime());
            cal.set(year, month, day, 18, 0, 0);
            end = sdf.format(cal.getTime());
            map.put("17:00", new ModelTimeSlot("17:00", "18:00", true, start, end));
            cal.set(year, month, day, 18, 0, 0);
            start = sdf.format(cal.getTime());
            cal.set(year, month, day, 19, 0, 0);
            end = sdf.format(cal.getTime());
            map.put("18:00", new ModelTimeSlot("18:00", "19:00", true, start, end));
            cal.set(year, month, day, 19, 0, 0);
            start = sdf.format(cal.getTime());
            cal.set(year, month, day, 20, 0, 0);
            end = sdf.format(cal.getTime());
            map.put("19:00", new ModelTimeSlot("19:00", "20:00", true, start, end));

            if (dayOfWeek.equals("Saturday")) {
                cal.set(year, month, day, 20, 0, 0);
                start = sdf.format(cal.getTime());
                cal.set(year, month, day, 21, 0, 0);
                end = sdf.format(cal.getTime());
                map.put("20:00", new ModelTimeSlot("20:00", "21:00", true, start, end));
                cal.set(year, month, day, 21, 0, 0);
                start = sdf.format(cal.getTime());
                cal.set(year, month, day, 22, 0, 0);
                end = sdf.format(cal.getTime());
                map.put("21:00", new ModelTimeSlot("21:00", "22:00", true, start, end));
                cal.set(year, month, day, 22, 0, 0);
                start = sdf.format(cal.getTime());
                cal.set(year, month, day, 23, 0, 0);
                end = sdf.format(cal.getTime());
                map.put("22:00", new ModelTimeSlot("22:00", "23:00", true, start, end));
            }
            else {
                cal.set(year, month, day, 10, 0, 0);
                start = sdf.format(cal.getTime());
                cal.set(year, month, day, 11, 0, 0);
                end = sdf.format(cal.getTime());
                map.put("10:00", new ModelTimeSlot("10:00", "11:00", true, start, end));
            }

            return map;
        }
    }

    String GET(String url){
        InputStream inputStream = null;
        String result = "";

        URL urlObj = null;
        try {
            urlObj = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection urlConnection = null;
        try {
            assert urlObj != null;
            urlConnection = (HttpURLConnection) urlObj.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // receive response as inputStream
        try {
            assert urlConnection != null;
            inputStream = urlConnection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // convert inputstream to string
        if(inputStream != null)
            try {
                result = convertInputStreamToString(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        else
            result = "Did not work!";

        return result;
    }

    String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public static class DatePickerFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), dateSetListener, year, month, day);
            dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

            return dialog;
        }
    }
}
