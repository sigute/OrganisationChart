package com.github.sigute.organisationchart.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.github.sigute.organisationchart.exceptions.NetworkIOException;
import com.github.sigute.organisationchart.exceptions.NetworkUnavailableException;
import com.github.sigute.organisationchart.exceptions.ServerException;
import com.github.sigute.organisationchart.exceptions.ServerResponseReadException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Helper class to retrieve data from the server.
 *
 * @author Sigute
 */
public class Server
{
    private static final String SERVER_URL = "http://mubaloo.com/dev/developerTestResources/team.json";
    private static final int READ_TIMEOUT_MILLISECONDS = 10 * 1000;
    private static final int CONNECT_TIMEOUT_MILLISECONDS = 15 * 1000;

    public static String retrieveJSONString(Context context)
            throws NetworkUnavailableException, ServerException, NetworkIOException,
            ServerResponseReadException
    {
        checkConnectivity(context);

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
            is = getInputStream(url);
        }
        catch (IOException e)
        {
            if (is != null)
            {
                try
                {
                    is.close();
                }
                catch (IOException e2)
                {
                    //we tried, nothing else to do really...
                }
            }

            throw new NetworkIOException(e.getMessage());
        }

        try
        {
            return convertToString(is);
        }
        catch (IOException e)
        {
            throw new ServerResponseReadException(e.getMessage());
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
    }

    private static InputStream getInputStream(URL url) throws IOException, ServerException
    {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(READ_TIMEOUT_MILLISECONDS);
        conn.setConnectTimeout(CONNECT_TIMEOUT_MILLISECONDS);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);

        conn.connect();
        int response = conn.getResponseCode();
        if (response != 200)
        {
            throw new ServerException("" + response);
        }

        return conn.getInputStream();
    }

    private static void checkConnectivity(Context context) throws NetworkUnavailableException
    {
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected())
        {
            throw new NetworkUnavailableException("Network currently unavailable");
        }
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
            throws NetworkUnavailableException, MalformedURLException, ServerException,
            NetworkIOException, ServerResponseReadException
    {
        checkConnectivity(context);

        URL url = new URL(imageURL);

        InputStream is = null;
        try
        {
            is = getInputStream(url);
        }
        catch (IOException e)
        {
            if (is != null)
            {
                try
                {
                    is.close();
                }
                catch (IOException e2)
                {
                    //we tried, nothing else to do really...
                }
            }

            throw new NetworkIOException(e.getMessage());
        }

        Bitmap image = BitmapFactory.decodeStream(is);

        try
        {
            is.close();
        }
        catch (IOException e)
        {
            //we tried, nothing else to do really...
        }

        if (image == null)
        {
            throw new ServerResponseReadException("Could not read bitmap");
        }

        return image;
    }
}
