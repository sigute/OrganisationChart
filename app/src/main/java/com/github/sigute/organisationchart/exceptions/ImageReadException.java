package com.github.sigute.organisationchart.exceptions;


/**
 * This exception is used when image read from data store fails.
 *
 * @author Sigute
 */
public class ImageReadException extends Exception
{
    public ImageReadException(String message)
    {
        super(message);
    }
}

