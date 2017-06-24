package com.studiokai.kaibeta;

import java.util.List;

/**
 * Created by titusjuocepis on 6/24/17.
 */

public class ModelFBPost {

    String createdTime, message, story;
    List<String> attachments;

    ModelFBPost(String time, String msg, String story, List<String> attachments) {
        createdTime = time;
        message = msg;
        this.story = story;
        this.attachments = attachments;
    }
}
