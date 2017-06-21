package com.studiokai.kaibeta;

import android.os.Bundle;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RelativeLayout;

import android.app.Activity;
import android.app.Dialog;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.*;

public class BookingActivity extends Activity implements View.OnClickListener, BookingListener {

    private TextView mOutputText, mWarning;
    private Button buttonDateSelect, buttonBookIt;
    private RelativeLayout bookingLayout;
    private AdapterBookingList mAdapter;
    private List<ModelBookingListItem> bookingTimes;
    private KaiCalendar kaiCalendar;
    private BookingManager bookingManager;

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
        mWarning = (TextView) findViewById(R.id.text_warning);
        bookingLayout = (RelativeLayout) findViewById(R.id.bookingLayout);
        buttonDateSelect = (Button) findViewById(R.id.button_update);
        buttonBookIt = (Button) findViewById(R.id.button_book_it);

        bookingTimes = new ArrayList<>();
        kaiCalendar = new KaiCalendar();
        bookingManager = new BookingManager();
    }

    @Override
    protected void onStart() {
        super.onStart();

        kaiCalendar.setCalendarListener(bookingManager);
        bookingManager.setBookingListener(this);

        buttonDateSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(bookingLayout);
            }
        });

        buttonBookIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<String> attendees = new ArrayList<>();
                attendees.add("tjuocepis@gmail.com");

                ModelBookingListItem bookingTime = bookingTimes.get(0);
                ModelEvent event = new ModelEvent(bookingTime.getStart(), bookingTime.getEnd(),
                        "Studio Kai Session", "1 Hour of Recording",
                        "16269 W Woodbine Circle, Vernon Hills, IL 60061", attendees);

                kaiCalendar.insertEvent(event);

            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                Date selectedDate = new Date(year, month, dayOfMonth-1);
                String dayOfWeek = BookingManager.getDayOfWeek(selectedDate);
                String dateMin = getMinDate(dayOfWeek, year, month, dayOfMonth);
                String dateMax = getMaxDate(dayOfWeek, year, month, dayOfMonth);
                String buttonDate = month+1 + "/" + dayOfMonth + "/" + year;

                SelectedDate.setSelectedDate(selectedDate, dayOfWeek, year, month, dayOfMonth);

                if (dayOfWeek.equals("Sunday")) {

                    String warning = "We are not open on " + dayOfWeek;
                    updateUI("", View.GONE, warning, View.VISIBLE, buttonDate, View.VISIBLE,
                             View.GONE, new ArrayList<ModelBookingListItem>());
                }
                else {

                    kaiCalendar.loadEvents(dateMin, dateMax);
                    String output = "Select Available Times:";
                    updateUI(output, View.VISIBLE, "", View.GONE, buttonDate, View.VISIBLE,
                             View.GONE, null);
                }
            }
        };
    }

    @Override
    protected void onPause() {
        super.onPause();

        buttonDateSelect.setOnClickListener(null);
        buttonBookIt.setOnClickListener(null);
        kaiCalendar.setCalendarListener(null);
        bookingManager.setBookingListener(null);
        dateSetListener = null;
    }

    @Override
    public void onClick(View v) {

        bookingTimes = mAdapter.getCheckedTimes();

        if (bookingTimes.isEmpty())
            buttonBookIt.setVisibility(View.GONE);
        else
            buttonBookIt.setVisibility(View.VISIBLE);

        for (int i = 0; i < bookingTimes.size(); i++) {
            Toast.makeText(this, bookingTimes.get(i).getStart(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onTimesReady(List<ModelBookingListItem> events) {

        mAdapter = new AdapterBookingList(events, this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.events_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onErrorGettingEvents(String msg) {
        mOutputText.setText(msg);
    }



    // Helper Functions

    private void updateUI(String output, int outputVisibility, String warning, int warningVisibility,
                          String dateButton, int dateButtonVisibility, int bookItButtonVisibility,
                          List<ModelBookingListItem> events) {

        mOutputText.setText(output);
        mOutputText.setVisibility(outputVisibility);
        mWarning.setText(warning);
        mWarning.setVisibility(warningVisibility);
        buttonDateSelect.setText(dateButton);
        buttonDateSelect.setVisibility(dateButtonVisibility);
        buttonBookIt.setVisibility(bookItButtonVisibility);

        if (events != null) {
            mAdapter = new AdapterBookingList(events, this);
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.events_list);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(mAdapter);
        }
    }

    private String getMinDate(String dayOfWeek, int year, int month, int day) {

        String dateMin;

        if (dayOfWeek.equals("Saturday"))
            dateMin = toRFC3339(year, month, day, 11, 0, 0);
        else
            dateMin = toRFC3339(year, month, day, 10, 0, 0);

        return dateMin;
    }

    private String getMaxDate(String dayOfWeek, int year, int month, int day) {

        String dateMax;

        if (dayOfWeek.equals("Saturday"))
            dateMax = toRFC3339(year, month, day, 22, 59, 59);
        else
            dateMax = toRFC3339(year, month, day, 19, 59, 59);

        return dateMax;
    }

    public String toRFC3339(int year, int month, int day, int h, int m, int s) {

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, h, m, s);
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

        return sdf.format(date);
    }



    // Date Picker Dialog

    public void showDatePickerDialog(View v) {

        DialogFragment newFragment = new BookingActivity.DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
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
