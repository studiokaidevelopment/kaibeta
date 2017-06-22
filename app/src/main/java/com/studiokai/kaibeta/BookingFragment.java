package com.studiokai.kaibeta;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookingFragment extends Fragment implements View.OnClickListener, BookingListener {

    private TextView mOutputText, mWarning;
    private Button buttonDateSelect, buttonBookIt;
    private RelativeLayout bookingLayout;
    private RecyclerView.Adapter mAdapter;
    private List<ModelBookingListItem> selectedTimes;
    private KaiCalendar kaiCalendar;
    private BookingManager bookingManager;
    private View.OnClickListener clickListener;
    private RecyclerView recyclerView;

    private static DatePickerDialog.OnDateSetListener dateSetListener;

    public BookingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        mOutputText = (TextView) view.findViewById(R.id.text_output);
        mWarning = (TextView) view.findViewById(R.id.text_warning);
        bookingLayout = (RelativeLayout) view.findViewById(R.id.bookingLayout);
        buttonDateSelect = (Button) view.findViewById(R.id.button_update);
        buttonBookIt = (Button) view.findViewById(R.id.button_book_it);
        recyclerView = (RecyclerView) view.findViewById(R.id.events_list);

        selectedTimes = new ArrayList<>();
        kaiCalendar = new KaiCalendar();
        bookingManager = new BookingManager();
        clickListener = this;

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

                if (buttonBookIt.getText().toString().equals("Book It")) {

                    updateUI("Events ready to book:", View.VISIBLE, "", View.GONE, null, View.VISIBLE,
                            "Confirm", View.VISIBLE, new AdapterBookingEvents(createEventsFromSelectedTimes(selectedTimes)));
                }
                else {

                    AdapterBookingEvents adapter = (AdapterBookingEvents) recyclerView.getAdapter();
                    List<ModelEvent> events = adapter.getBookingEvents();

                    showCreateEventDialog(events);
                }
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
                    updateUI("", View.GONE, warning, View.VISIBLE, buttonDate, View.VISIBLE, null,
                            View.GONE, new AdapterBookingTimes(new ArrayList<ModelBookingListItem>(),
                                    clickListener));
                }
                else {

                    kaiCalendar.loadEvents(dateMin, dateMax);
                    String output = "Select Available Times:";
                    updateUI(output, View.VISIBLE, "", View.GONE, buttonDate, View.VISIBLE, null,
                            View.GONE, null);
                }
            }
        };

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        buttonDateSelect.setOnClickListener(null);
        buttonBookIt.setOnClickListener(null);
        kaiCalendar.setCalendarListener(null);
        bookingManager.setBookingListener(null);
        dateSetListener = null;
    }

    @Override
    public void onClick(View v) {

        AdapterBookingTimes timesAdapter = (AdapterBookingTimes) mAdapter;
        selectedTimes = timesAdapter.getCheckedTimes();

        if (selectedTimes.isEmpty())
            buttonBookIt.setVisibility(View.GONE);
        else
            buttonBookIt.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTimesReady(List<ModelBookingListItem> events) {

        mAdapter = new AdapterBookingTimes(events, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onErrorGettingEvents(String msg) {
        mOutputText.setText(msg);
    }



    // Helper Functions

    private List<ModelEvent> createEventsFromSelectedTimes(List<ModelBookingListItem> selectedTimes) {

        List<ModelEvent> events = new ArrayList<>();

        for (ModelBookingListItem item : selectedTimes) {

            events.add(new ModelEvent(item.getStart(), item.getEnd(), "Studio Kai Session",
                    "1 hour of studio time", "16269 W Woodbine Circle, Vernon Hills IL 60061",
                    new ArrayList<String>()));
        }

        return events;
    }

    private void updateUI(String output, int outputVisibility, String warning, int warningVisibility,
                          String dateButton, int dateButtonVisibility, String bookItButton,
                          int bookItButtonVisibility, RecyclerView.Adapter adapter) {

        mOutputText.setText(output);
        mOutputText.setVisibility(outputVisibility);
        mWarning.setText(warning);
        mWarning.setVisibility(warningVisibility);
        if (dateButton != null)
            buttonDateSelect.setText(dateButton);
        buttonDateSelect.setVisibility(dateButtonVisibility);
        if (bookItButton != null)
            buttonBookIt.setText(bookItButton);
        buttonBookIt.setVisibility(bookItButtonVisibility);

        if (adapter != null) {
            mAdapter = adapter;
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                    LinearLayoutManager.VERTICAL, false));
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

    private String toRFC3339(int year, int month, int day, int h, int m, int s) {

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, h, m, s);
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

        return sdf.format(date);
    }

    private void showCreateEventDialog(final List<ModelEvent> events) {

        final int nEvents = events.size();

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setCancelable(false);
        String endOfMessage = nEvents > 1 ? nEvents+"\n1 hour sessions with STUDIO KAI"
                : nEvents+"\n1 hour session with STUDIO KAI";
        alertDialogBuilder.setMessage("Are you sure you would like to create " + endOfMessage);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                for (ModelEvent item : events) {

                    item.mAttendees.add("tjuocepis@gmail.com");

                    ModelEvent event = new ModelEvent(item.mStart, item.mEnd,
                            item.mSummary, item.mDescription,
                            "16269 W Woodbine Circle, Vernon Hills, IL 60061", item.mAttendees);

                    kaiCalendar.insertEvent(event);
                }

                updateUI("Select Available Times", View.VISIBLE, "", View.GONE, "Select Date",
                        View.VISIBLE, null, View.GONE, new AdapterBookingTimes(
                                new ArrayList<ModelBookingListItem>(), clickListener));

                Toast.makeText(getActivity(), "STUDIO KAI thanks you! The " + nEvents +
                                " should events should appear in your Calendar shortly",
                        Toast.LENGTH_LONG).show();

            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialogBuilder.create().show();
    }



    // Date Picker Dialog

    public void showDatePickerDialog(View v) {

        DialogFragment newFragment = new BookingFragment.DatePickerFragment();
        newFragment.show(getActivity().getFragmentManager(), "datePicker");
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
