package com.studiokai.kaibeta;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by titusjuocepis on 6/21/17.
 */

class AdapterBookingEvents extends RecyclerView.Adapter<AdapterBookingEvents.ViewHolder> {

    private List<ModelBookingListItem> bookingEvents;

    AdapterBookingEvents(List<ModelBookingListItem> events) {
        bookingEvents = events;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_booking_events, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ModelBookingListItem event = bookingEvents.get(position);
        int newPos = position+1;
        holder.textEvent.setText("Event " + newPos);
        holder.textStartTime.setText(event.getStartTime());
        holder.textEndTime.setText(event.getEndTime());
    }

    @Override
    public int getItemCount() {
        return bookingEvents.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textEvent, textStartTime, textEndTime;

        ViewHolder(View itemView) {
            super(itemView);

            textEvent = (TextView) itemView.findViewById(R.id.text_event);
            textStartTime = (TextView) itemView.findViewById(R.id.text_start_time);
            textEndTime = (TextView) itemView.findViewById(R.id.text_end_time);
        }
    }
}
