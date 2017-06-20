package com.studiokai.kaibeta;

import java.util.List;

/**
 * Created by titusjuocepis on 6/20/17.
 */

public class ModelEvent {

    private String mStart, mEnd, mSummary, mDescription, mLocation;
    private List<String> mAttendees;

    ModelEvent(String start, String end, String summary, String description, String location,
               List<String> attendees) {

        mStart = start;
        mEnd = end;
        mSummary = summary;
        mDescription = description;
        mLocation = location;
        mAttendees = attendees;
    }
}
