package com.github.sigute.organisationchart.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by spikereborn on 22/03/2015.
 */
public class Storage
{
    public static void save(Context context, Bitmap bitmapImage, String imageID)
    {
        File directory = context.getDir("profile_images", Context.MODE_PRIVATE);
        // Create imageDir
        File path = new File(directory, imageID + ".jpg");

        FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream(path);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        }
        catch (FileNotFoundException e)
        {
            //TODO handle this
            e.printStackTrace();
        }
        catch (IOException e)
        {
            //TODO handle this
            e.printStackTrace();
        }
    }

    public static Bitmap load(Context context, String imageId)
    {
        File directory = context.getDir("profile_images", Context.MODE_PRIVATE);
        File f = new File(directory, imageId + ".jpg");

        try
        {
            return BitmapFactory.decodeStream(new FileInputStream(f));
        }
        catch (FileNotFoundException e)
        {
            //TODO handle this!
            e.printStackTrace();
        }

        return null;
    }
}
