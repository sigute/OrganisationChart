package com.github.sigute.organisationchart.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by spikereborn on 21/03/2015.
 */
public class Server
{
    private static final String SERVER_URL = "http://mubaloo.com/dev/developerTestResources/team.json";
    private static final int READ_TIMEOUT_MILISECONDS = 10 * 1000;
    private static final int CONNECT_TIMEOUT_MILISECONDS = 15 * 1000;


    public static String retrieveJSONString(Context context)
    {
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected())
        {
            //TODO no network, throw exception or something
        }

        URL url;
        try
        {
            url = new URL(SERVER_URL);
        }
        catch (MalformedURLException e)
        {
            //hard-coded URL could not be parsed as URL.
            //this would be very clear from running the code first time
            //throwing IllegalStateException, in case URL is changed in the future -
            // app will crash and the developer will be forced to fix it
            throw new IllegalStateException("Hardcoded URL is wrong! " + SERVER_URL);
        }

        InputStream is = null;
        try
        {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(READ_TIMEOUT_MILISECONDS);
            conn.setConnectTimeout(CONNECT_TIMEOUT_MILISECONDS);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            conn.connect();
            int response = conn.getResponseCode();
            if (response != 200)
            {
                //TODO add exception or something
                return "";
            }
            Log.d("debug!", "The response is: " + response);

            is = conn.getInputStream();
            return convertToString(is);
        }
        catch (ProtocolException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (is != null)
            {
                try
                {
                    is.close();
                }
                catch (IOException e)
                {
                    //we tried, nothing else to do really...
                }
            }
        }

        //TODO add exception or something
        return "";
    }

    private static String convertToString(InputStream stream) throws IOException
    {
        BufferedReader r = new BufferedReader(new InputStreamReader(stream));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null)
        {
            total.append(line);
        }
        return total.toString();
    }

    public static Bitmap retrieveBitmap(Context context, String imageURL)
    {
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected())
        {
            //TODO no network, throw exception or something
        }

        URL url;
        try
        {
            url = new URL(imageURL);
        }
        catch (MalformedURLException e)
        {
            //TODO this might be wrong, as comes from third party! deal with it...
            return null;
        }

        InputStream is = null;
        try
        {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(READ_TIMEOUT_MILISECONDS);
            conn.setConnectTimeout(CONNECT_TIMEOUT_MILISECONDS);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            conn.connect();
            int response = conn.getResponseCode();
            if (response != 200)
            {
                //TODO add exception or something
                return null;
            }
            Log.d("debug!", "The response is: " + response);

            is = conn.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(is);
            return myBitmap;
        }
        catch (ProtocolException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (is != null)
            {
                try
                {
                    is.close();
                }
                catch (IOException e)
                {
                    //we tried, nothing else to do really...
                }
            }
        }

        //TODO add exception or something
        return null;
    }
}
