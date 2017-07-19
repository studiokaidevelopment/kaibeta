package com.studiokai.kaibeta.booking;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.studiokai.kaibeta.R;
import com.studiokai.kaibeta.utilities.UtilityDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by titusjuocepis on 6/21/17.
 */

class AdapterBookingEvents extends RecyclerView.Adapter<AdapterBookingEvents.ViewHolder> {

    private List<ModelEvent> bookingEvents;

    AdapterBookingEvents(List<ModelEvent> events) {
        bookingEvents = events;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_booking_events_list, parent, false);

        return new ViewHolder(view);
    }

    List<ModelEvent> getBookingEvents() {
        return bookingEvents;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        ModelEvent event = bookingEvents.get(position);
        holder.textStartTime.setText(UtilityDate.convertToTime(event.start.dateTime));
        holder.textEndTime.setText(UtilityDate.convertToTime(event.end.dateTime));

        holder.editTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                EditText editText = (EditText) v;
                bookingEvents.get(position).summary = editText.getText().toString();
            }
        });

        holder.editDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                EditText editText = (EditText) v;
                bookingEvents.get(position).description = editText.getText().toString();
            }
        });

        holder.editGuests.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                bookingEvents.get(position).attendees = new ArrayList<>();

                EditText editText = (EditText) v;
                String[] guests = editText.getText().toString().split(", ");

                for (String guest : guests) {
                    bookingEvents.get(position).attendees.add(new ModelAttendee(guest, "", "notConfirmed"));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookingEvents.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textStartTime, textEndTime;
        EditText editTitle, editDescription, editLocation, editGuests;

        ViewHolder(View itemView) {

            super(itemView);

            textStartTime = (TextView) itemView.findViewById(R.id.text_start_time);
            textEndTime = (TextView) itemView.findViewById(R.id.text_end_time);
            editTitle = (EditText) itemView.findViewById(R.id.edit_title);
            editDescription = (EditText) itemView.findViewById(R.id.edit_description);
            editLocation = (EditText) itemView.findViewById(R.id.edit_location);
            editGuests = (EditText) itemView.findViewById(R.id.edit_attendees);
        }
    }
}
