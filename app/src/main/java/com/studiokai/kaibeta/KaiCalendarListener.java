package com.studiokai.kaibeta;

import java.util.List;

/**
 * Created by titusjuocepis on 6/20/17.
 */

public interface KaiCalendarListener {

    void onEventsLoaded(List<ModelEvent> events);
    void onEventInserted(String response);
}