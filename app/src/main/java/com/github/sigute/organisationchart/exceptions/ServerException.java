package com.github.sigute.organisationchart.exceptions;


/**
 * This exception is used when the server returns invalid response code.
 *
 * @author Sigute
 */
public class ServerException extends Exception
{
    public ServerException(String message)
    {
        super(message);
    }
}
