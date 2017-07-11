package com.studiokai.kaibeta.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by titusjuocepis on 6/20/17.
 */

public class HTTP {

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