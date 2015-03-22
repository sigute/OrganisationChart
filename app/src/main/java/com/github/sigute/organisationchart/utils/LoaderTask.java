package com.github.sigute.organisationchart.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.util.Pair;

import com.github.sigute.organisationchart.R;
import com.github.sigute.organisationchart.exceptions.NetworkIOException;
import com.github.sigute.organisationchart.exceptions.NetworkUnavailableException;
import com.github.sigute.organisationchart.exceptions.ServerException;
import com.github.sigute.organisationchart.exceptions.ServerResponseReadException;
import com.github.sigute.organisationchart.organisation.Employee;
import com.github.sigute.organisationchart.organisation.Organisation;

import java.net.MalformedURLException;
import java.util.List;

/**
 * Created by spikereborn on 21/03/2015.
 */
public class LoaderTask extends AsyncTask<Void, Void, Pair<Organisation, String>>
{
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
        //TODO before trying online, see whether can retrieve organisation from database

        String jsonString = null;
        try
        {
            jsonString = Server.retrieveJSONString(context);
        }
        catch (NetworkUnavailableException e)
        {
            return new Pair<>(null, context.getString(R.string.error_network_unavailable));
        }
        catch (ServerException e)
        {
            return new Pair<>(null, context.getString(R.string.error_server));
        }
        catch (NetworkIOException e)
        {
            return new Pair<>(null, context.getString(R.string.error_network_io));
        }
        catch (ServerResponseReadException e)
        {
            return new Pair<>(null, context.getString(R.string.error_server_response_read));
        }

        Organisation organisation = JsonParser.parseOutJsonData(jsonString);
        try
        {
            setEmployeeImages(organisation);
        }
        catch (MalformedURLException | NetworkIOException | ServerResponseReadException | NetworkUnavailableException | ServerException e)
        {
            //could not retrieve all photos. Not ideal, but have placeholder picture, so will just go with that... Employee data is still valid.
        }
        DatabaseHelper.getInstance(context).insertOrganisation(organisation);
        return new Pair<>(organisation, null);
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
            Storage.save(context, bitmap, employee.getId());
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