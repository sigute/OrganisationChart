package com.github.sigute.organisationchart.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.github.sigute.organisationchart.exceptions.ImageReadException;
import com.github.sigute.organisationchart.exceptions.ImageSaveException;

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
            throws ImageSaveException
    {
        File directory = context.getDir("profile_images", Context.MODE_PRIVATE);
        File path = new File(directory, imageID + ".jpg");

        FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream(path);
            if (!bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos))
            {
                throw new ImageSaveException("Compress failed");
            }
        }
        catch (FileNotFoundException e)
        {
            throw new ImageSaveException("Could not open stream");
        }
        finally
        {
            if (fos != null)
            {
                try
                {
                    fos.close();
                }
                catch (IOException e)
                {
                    //tried to close, nothing else can be done
                }
            }
        }
    }

    public static Bitmap load(Context context, String imageId) throws ImageReadException
    {
        File directory = context.getDir("profile_images", Context.MODE_PRIVATE);
        File f = new File(directory, imageId + ".jpg");

        Bitmap image = null;
        FileInputStream fis = null;
        try
        {
            fis = new FileInputStream(f);
            image = BitmapFactory.decodeStream(fis);

            if (image == null)
            {
                throw new ImageReadException("Image could not be read");
            }
        }
        catch (FileNotFoundException e)
        {
            throw new ImageReadException("Could not open stream");
        }
        finally
        {
            if (fis != null)
            {
                try
                {
                    fis.close();
                }
                catch (IOException e)
                {
                    //tried to close, nothing else can be done
                }
            }
        }

        return image;
    }
}
