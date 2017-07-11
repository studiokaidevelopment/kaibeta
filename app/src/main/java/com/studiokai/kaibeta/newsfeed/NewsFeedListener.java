package com.studiokai.kaibeta.newsfeed;

import java.util.List;

/**
 * Created by titusjuocepis on 6/24/17.
 */

interface NewsFeedListener {

    void onFBPostsLoaded(List<ModelFBPost> posts);
    void onFBPostImagesLoaded(List<ModelFBPost> images);
}
