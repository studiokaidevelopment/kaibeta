package com.studiokai.kaibeta.newsfeed;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.studiokai.kaibeta.utilities.HTTP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by titusjuocepis on 6/24/17.
 */

class KaiFacebook {

    private NewsFeedListener mListener;

    KaiFacebook(NewsFeedListener listener) {
        mListener = listener;
    }

    void loadPosts() {
        GetKaiFBNewsTask getPostsTask = new GetKaiFBNewsTask();
        getPostsTask.execute();
    }

    private class GetKaiFBNewsTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return HTTP.GET("http://10.0.2.2:8080/kai_posts");
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);

            List<ModelFBPost> fbPosts = parseKaiFBNewsResponse(s);
            mListener.onFBPostsLoaded(fbPosts);

            DownloadBitmapsTask getBitmaps = new DownloadBitmapsTask(fbPosts);
            getBitmaps.execute();
        }
    };

    private List<ModelFBPost> parseKaiFBNewsResponse(String response) {

        List<ModelFBPost> fbPosts = new ArrayList<>();
        JSONArray jsonArray;

        try {
            jsonArray = new JSONArray(response);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        for (int i = 0; i < jsonArray.length(); i++) {

            String createdTime = "", message = "", story = "", target = "";
            List<String> mAttachments = new ArrayList<>();

            JSONObject json = null;
            try {
                json = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (json != null) {

                try {
                    createdTime = json.getString("created_time");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    story = json.getString("story");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    message = json.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    target = json.getString("target");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONArray attachments = null;
                try {
                    attachments = json.getJSONArray("attachments");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (attachments != null) {
                    for (int k = 0; k < attachments.length(); k++) {
                        try {
                            mAttachments.add(attachments.getString(k));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                ModelFBPost post = new ModelFBPost(createdTime, message, story, target, mAttachments,
                        new ArrayList<Bitmap>());
                fbPosts.add(post);
            }
        }
        return fbPosts;
    }

    private class DownloadBitmapsTask extends AsyncTask<String, Void, List<ModelFBPost>> {

        private final List<ModelFBPost> mPosts;

        DownloadBitmapsTask(List<ModelFBPost> posts) {
            mPosts = posts;
        }

        @Override
        protected List<ModelFBPost> doInBackground(String... params) {

            return downloadBitmaps(mPosts);
        }

        @Override
        protected void onPostExecute(List<ModelFBPost> posts) {

            super.onPostExecute(posts);

            mListener.onFBPostImagesLoaded(posts);
        }

        private List<ModelFBPost> downloadBitmaps(List<ModelFBPost> fbPosts) {

            for (ModelFBPost post : mPosts) {

                boolean resizePic = true;

                for (String mUrl : post.attachments) {

                    try {
                        URL url = new URL(mUrl);
                        HttpURLConnection connection = null;
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.connect();

                        InputStream input = connection.getInputStream();
                        Bitmap fbPostPic = BitmapFactory.decodeStream(input);

                        double width = fbPostPic.getWidth();
                        double height = fbPostPic.getHeight();

                        Log.d("[BITMAP] ---> ", width + " " + height);

                        if (resizePic) {
                            width = width * 2;
                            height = height * 2;
                            resizePic = false;
                            post.images.add(Bitmap.createScaledBitmap(fbPostPic, (int)width,
                                    (int)height, false));
                        }
                        else {
                            post.images.add(Bitmap.createBitmap(fbPostPic, 0, 0, 720, 450));
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }
            return mPosts;
        }
    }
}
