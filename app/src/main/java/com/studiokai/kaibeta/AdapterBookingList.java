package com.studiokai.kaibeta;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by titusjuocepis on 6/19/17.
 */

class AdapterBookingList extends RecyclerView.Adapter<AdapterBookingList.ViewHolder> {

    private List<ModelBookingListItem> bookingTimes;
    private View.OnClickListener clickListener;

    AdapterBookingList(List<ModelBookingListItem> times, View.OnClickListener listener) {
        bookingTimes = times;
        clickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_booking_times, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        ModelBookingListItem bookingItem = bookingTimes.get(position);
        holder.startTime.setText(bookingItem.getStartTime());
        holder.endTime.setText(bookingItem.getEndTime());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bookingTimes.get(position).setChecked(isChecked);
            }
        });

        if (!bookingItem.isAvailable()) {
            holder.checkBox.setVisibility(View.GONE);
            holder.availabilityFlag.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return bookingTimes.size();
    }

    List<ModelBookingListItem> getCheckedTimes() {

        List<ModelBookingListItem> checkedTimes = new ArrayList<>();

        for (int i = 0; i < bookingTimes.size(); i++) {
            if (bookingTimes.get(i).isChecked())
                checkedTimes.add(bookingTimes.get(i));
        }

        return checkedTimes;
    }



    // VIEW HOLDER

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView startTime, endTime, availabilityFlag;
        CheckBox checkBox;

        ViewHolder(View itemView) {
            super(itemView);
            startTime = (TextView) itemView.findViewById(R.id.text_start_time);
            endTime = (TextView) itemView.findViewById(R.id.text_end_time);
            availabilityFlag = (TextView) itemView.findViewById(R.id.text_availability);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            checkBox.setOnClickListener(clickListener);
        }
    }
}
