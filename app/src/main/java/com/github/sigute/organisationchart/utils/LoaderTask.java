package com.github.sigute.organisationchart.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.util.Pair;

import com.github.sigute.organisationchart.R;
import com.github.sigute.organisationchart.exceptions.DatabaseInsertException;
import com.github.sigute.organisationchart.exceptions.DatabaseSelectException;
import com.github.sigute.organisationchart.exceptions.ImageSaveException;
import com.github.sigute.organisationchart.exceptions.NetworkIOException;
import com.github.sigute.organisationchart.exceptions.NetworkUnavailableException;
import com.github.sigute.organisationchart.exceptions.ServerException;
import com.github.sigute.organisationchart.exceptions.ServerResponseReadException;
import com.github.sigute.organisationchart.organisation.Employee;
import com.github.sigute.organisationchart.organisation.Organisation;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.util.List;

/**
 * Loader task - retrieves data, parses JSON, checks offline database if there is no network.
 *
 * @author Sigute
 */
public class LoaderTask extends AsyncTask<Void, Void, Pair<Organisation, String>>
{
    /**
     * Implement this listener to get feedback from the task.
     */
    public interface TaskListener
    {
        void onTaskStarted();

        void onTaskFinished(Organisation organisation);

        void onTaskFailure(String errorMessage);
    }

    Context context;
    private TaskListener listener;

    public LoaderTask(Context context, TaskListener taskListener)
    {
        this.context = context;
        this.listener = taskListener;
    }

    @Override
    protected void onPreExecute()
    {
        listener.onTaskStarted();
    }

    @Override
    protected Pair<Organisation, String> doInBackground(Void[] voids)
    {
        Organisation organisation = null;

        String jsonString = null;
        try
        {
            jsonString = Server.retrieveJSONString(context);
        }
        catch (NetworkUnavailableException e)
        {
            organisation = organisationFromDatabase();
            if (organisation != null)
            {
                return new Pair<>(organisation, null);
            }
            return new Pair<>(null, context.getString(R.string.error_network_unavailable));
        }
        catch (ServerException e)
        {
            organisation = organisationFromDatabase();
            if (organisation != null)
            {
                return new Pair<>(organisation, null);
            }
            return new Pair<>(null, context.getString(R.string.error_server));
        }
        catch (NetworkIOException e)
        {
            organisation = organisationFromDatabase();
            if (organisation != null)
            {
                return new Pair<>(organisation, null);
            }
            return new Pair<>(null, context.getString(R.string.error_network_io));
        }
        catch (ServerResponseReadException e)
        {
            organisation = organisationFromDatabase();
            if (organisation != null)
            {
                return new Pair<>(organisation, null);
            }
            return new Pair<>(null, context.getString(R.string.error_server_response_read));
        }

        try
        {
            organisation = JsonParser.parseOutJsonData(jsonString);
        }
        catch (JSONException e)
        {
            organisation = organisationFromDatabase();
            if (organisation != null)
            {
                return new Pair<>(organisation, null);
            }
            return new Pair<>(null, context.getString(R.string.error_json_parsing));
        }

        try
        {
            setEmployeeImages(organisation);
        }
        catch (MalformedURLException | NetworkIOException | ServerResponseReadException | NetworkUnavailableException | ServerException e)
        {
            //could not retrieve all photos. Not ideal, but have placeholder picture, so will just go with that... Employee data is still valid.
        }
        try
        {
            DatabaseHelper.getInstance(context).insertOrganisation(organisation);
        }
        catch (DatabaseInsertException e)
        {
            // insert failed. I guess we are not storing data offline this time...
        }
        return new Pair<>(organisation, null);
    }

    private Organisation organisationFromDatabase()
    {
        try
        {
            return DatabaseHelper.getInstance(context).selectOrganisation();
        }
        catch (DatabaseSelectException e)
        {
            return null;
        }
    }

    private void setEmployeeImages(Organisation organisation)
            throws MalformedURLException, NetworkIOException, ServerResponseReadException,
            NetworkUnavailableException, ServerException
    {
        List<Employee> employees = organisation.getAllEmployees();
        for (Employee employee : employees)
        {
            String imageUrl = employee.getImageURL();
            Bitmap bitmap = Server.retrieveBitmap(context, imageUrl);
            try
            {
                Storage.save(context, bitmap, employee.getId());
            }
            catch (ImageSaveException e)
            {
                //could not save
                //rely on the placeholder for now and only show employee data
            }
        }
    }

    @Override
    protected void onPostExecute(Pair<Organisation, String> pair)
    {
        if (pair.first != null)
        {
            listener.onTaskFinished(pair.first);
        }
        else
        {
            listener.onTaskFailure(pair.second);
        }
    }
}