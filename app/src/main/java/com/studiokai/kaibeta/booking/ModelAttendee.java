package com.studiokai.kaibeta.booking;

/**
 * Created by titusjuocepis on 7/16/17.
 */

class ModelAttendee {

    String email, displayName, responseStatus;

    ModelAttendee(String mail, String name, String status) {
        email = mail;
        displayName = name;
        responseStatus = status;
    }
}
