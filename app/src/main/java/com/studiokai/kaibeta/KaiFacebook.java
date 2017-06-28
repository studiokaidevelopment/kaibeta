package com.studiokai.kaibeta;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

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

    private List<ModelFBPost> fbPosts;
    private NewsFeedListener mListener;

    KaiFacebook(NewsFeedListener listener) {
        fbPosts = new ArrayList<>();
        mListener = listener;
    }

    void loadPosts() {
        GetKaiFBNewsTask getPostsTask = new GetKaiFBNewsTask();
        getPostsTask.execute();
    }

    public List<ModelFBPost> getFbPosts() {
        return fbPosts;
    }

    private class GetKaiFBNewsTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return HTTP.GET("http://10.0.2.2:8080/kai_posts");
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);

            JSONObject jsonObject = null;

            try {
                jsonObject = new JSONObject(s);
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }

            JSONArray json = null;
            try {
                json = jsonObject.getJSONArray("posts");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (json != null) {

                for (int i = 0; i < json.length(); i++) {

                    String createdTime = "", message = "", story = "";
                    List<String> mAttachments = new ArrayList<>();

                    try {
                        createdTime = json.getJSONObject(i).getString("created_time");
                        message = json.getJSONObject(i).getString("message");
                        story = json.getJSONObject(i).getString("story");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JSONArray attachments = null;
                    try {
                        attachments = json.getJSONObject(i).getJSONArray("attachments");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    assert attachments != null;
                    for (int k = 0; k < attachments.length(); k++) {
                        try {
                            mAttachments.add(attachments.getString(k));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    ModelFBPost post = new ModelFBPost(createdTime, message, story, mAttachments,
                            new ArrayList<Bitmap>());
                    fbPosts.add(post);
                }

                mListener.onFBPostsLoaded(fbPosts);

                GetBitmapsTask getBitmaps = new GetBitmapsTask(fbPosts);
                getBitmaps.execute();
            }
        }
    };

    private class GetBitmapsTask extends AsyncTask<String, Void, List<ModelFBPost>> {

        List<ModelFBPost> mPosts;

        GetBitmapsTask(List<ModelFBPost> posts) {
            mPosts = posts;
        }

        @Override
        protected List<ModelFBPost> doInBackground(String... params) {


                List<Bitmap> images = new ArrayList<>();

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

        @Override
        protected void onPostExecute(List<ModelFBPost> posts) {

            super.onPostExecute(posts);

            mListener.onFBPostImagesLoaded(posts);
        }
    }
}
