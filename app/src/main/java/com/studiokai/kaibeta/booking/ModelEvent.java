package com.studiokai.kaibeta.booking;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by titusjuocepis on 6/20/17.
 */

class ModelEvent {

    String mStart, mEnd, mSummary, mDescription, mLocation;
    List<String> mAttendees;

    ModelEvent(String start, String end, String summary, String description, String location,
               List<String> attendees) {

        mStart = start;
        mEnd = end;
        mSummary = summary;
        mDescription = description;
        mLocation = location;
        mAttendees = attendees;
    }

    ModelEvent(String start, String end) {
        mStart = start;
        mEnd = end;
        mSummary = "";
        mDescription = "";
        mLocation = "";
        mAttendees = new ArrayList<>();
    }
}
