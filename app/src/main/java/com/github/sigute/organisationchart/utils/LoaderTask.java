package com.github.sigute.organisationchart.utils;

import android.content.Context;
import android.os.AsyncTask;

import com.github.sigute.organisationchart.organisation.Organisation;

/**
 * Created by spikereborn on 21/03/2015.
 */
public class LoaderTask extends AsyncTask<Void, Void, Organisation>
{
    public interface TaskListener
    {
        void onTaskStarted();

        void onTaskFinished(Organisation organisation);
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
    protected Organisation doInBackground(Void[] voids)
    {
        String jsonString = Server.retrieveJSONString(context);
        Organisation organisation = JsonParser.parseOutJsonData(jsonString);
        return organisation;
    }

    @Override
    protected void onPostExecute(Organisation organisation)
    {
        //TODO add success/failure bool and failure reason to listener?
        listener.onTaskFinished(organisation);
    }
}