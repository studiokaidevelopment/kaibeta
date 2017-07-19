package com.studiokai.kaibeta.booking;

import java.util.List;

/**
 * Created by titusjuocepis on 6/20/17.
 */

interface KaiCalendarListener {

    void onEventsLoaded(List<ModelEvent> events);
    void onEventInserted(String response);
}
