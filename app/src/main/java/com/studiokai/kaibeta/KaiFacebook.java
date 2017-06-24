package com.studiokai.kaibeta;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by titusjuocepis on 6/24/17.
 */

public class KaiFacebook {

    private List<ModelFBPost> fbPosts;

    KaiFacebook() {
        fbPosts = new ArrayList<>();
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

            JSONObject jsonObject = null;

            try {
                jsonObject = new JSONObject(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONArray json = null;
            try {
                assert jsonObject != null;
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

                    ModelFBPost post = new ModelFBPost(createdTime, message, story, mAttachments);
                    fbPosts.add(post);
                }

                for (ModelFBPost post : fbPosts) {
                    Log.d("[KaiFacebook] ---> ", post.createdTime);
                    Log.d("[KaiFacebook] ---> ", post.message);
                    Log.d("[KaiFacebook] ---> ", post.story);
                }
            }
        }
    };
}
