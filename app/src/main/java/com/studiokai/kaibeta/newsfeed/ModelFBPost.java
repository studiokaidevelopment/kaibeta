package com.studiokai.kaibeta.newsfeed;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by titusjuocepis on 6/24/17.
 */

class ModelFBPost {

    String createdTime, message, story, target;
    List<String> attachments;
    List<Bitmap> images;

    ModelFBPost(String time, String msg, String story, String target, List<String> attachments,
                List<Bitmap> attachmentImages) {

        createdTime = time;
        message = msg;
        this.story = story;
        this.attachments = attachments;
        images = attachmentImages;
        this.target = target;
    }
}
