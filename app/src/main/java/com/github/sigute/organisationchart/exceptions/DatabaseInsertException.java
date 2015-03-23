package com.github.sigute.organisationchart.exceptions;

/**
 * This exception is used when database insert fails.
 *
 * @author Sigute
 */
public class DatabaseInsertException extends Exception
{
    public DatabaseInsertException(String message)
    {
        super(message);
    }
}
