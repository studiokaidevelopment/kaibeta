package com.studiokai.kaibeta.utilities;

import android.util.Log;

import com.google.api.client.http.HttpResponse;
import com.google.gson.Gson;
import com.studiokai.kaibeta.booking.ModelEvent;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by titusjuocepis on 6/20/17.
 */

public class HTTP {

    public static String POST(String url, String body){

        URL urlObj = null;
        try {
            urlObj = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection urlConnection = null;
        try {
            assert urlObj != null;
            urlConnection = (HttpURLConnection) urlObj.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Host", "com.studiokai.kaibeta");
            urlConnection.connect();

            DataOutputStream outStream = new DataOutputStream(urlConnection.getOutputStream());
            outStream.writeBytes(URLEncoder.encode(body, "UTF-8"));
            outStream.flush();
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        // receive response as inputStream
//        InputStream inputStream = null;
//        try {
//            assert urlConnection != null;
//            inputStream = urlConnection.getInputStream();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        // convert inputstream to string
//        String result = "";
//        if(inputStream != null)
//            try {
//                result = convertInputStreamToString(inputStream);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        else
//            result = "Did not work!";

        return "WOOPS";
    }

    public static String GET(String url){

        URL urlObj = null;
        try {
            urlObj = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection urlConnection = null;
        try {
            assert urlObj != null;
            urlConnection = (HttpURLConnection) urlObj.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // receive response as inputStream
        InputStream inputStream = null;
        try {
            assert urlConnection != null;
            inputStream = urlConnection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // convert inputstream to string
        String result = "";
        if(inputStream != null)
            try {
                result = convertInputStreamToString(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        else
            result = "Did not work!";

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException{

        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";

        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();

        return result;
    }
}
