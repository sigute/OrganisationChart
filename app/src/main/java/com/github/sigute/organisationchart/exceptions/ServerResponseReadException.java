package com.github.sigute.organisationchart.exceptions;


/**
 * This exception is used when the response received from server cannot not be read.
 *
 * @author Sigute
 */
public class ServerResponseReadException extends Exception
{
    public ServerResponseReadException(String message)
    {
        super(message);
    }
}
