package com.studiokai.kaibeta.booking;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by titusjuocepis on 6/20/17.
 */

public class ModelEvent {

    String summary, description, location;
    ModelTime start, end;
    List<ModelAttendee> attendees;

    ModelEvent(ModelTime start, ModelTime end, String summary, String description, String location,
               List<ModelAttendee> attendees) {

        this.start = start;
        this.end = end;
        this.summary = summary;
        this.description = description;
        this.location = location;
        this.attendees = attendees;
    }

    ModelEvent(ModelTime start, ModelTime end) {
        this.start = start;
        this.end = end;
        summary = "";
        description = "";
        location = "";
        attendees = new ArrayList<>();
    }
}
