package com.studiokai.kaibeta;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by titusjuocepis on 6/19/17.
 */

class AdapterBookingList extends RecyclerView.Adapter<AdapterBookingList.ViewHolder> {

    private ArrayList<ModelTimeSlot> timeSlots;
    private View.OnClickListener mListener;

    AdapterBookingList(ArrayList<ModelTimeSlot> times, View.OnClickListener listener) {
        timeSlots = times;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        ModelTimeSlot itemTimeSlot = timeSlots.get(position);
        holder.startTime.setText(itemTimeSlot.getStartTime());
        holder.endTime.setText(itemTimeSlot.getEndTime());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                timeSlots.get(position).setChecked(isChecked);
            }
        });

        if (!itemTimeSlot.isAvailable()) {
            holder.checkBox.setVisibility(View.GONE);
            holder.availabilityFlag.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return timeSlots.size();
    }

    public ArrayList<ModelTimeSlot> getCheckedTimes() {

        ArrayList<ModelTimeSlot> checkedTimes = new ArrayList<>();

        for (int i = 0; i < timeSlots.size(); i++) {
            if (timeSlots.get(i).isChecked())
                checkedTimes.add(timeSlots.get(i));
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
            checkBox.setOnClickListener(mListener);
        }
    }
}
