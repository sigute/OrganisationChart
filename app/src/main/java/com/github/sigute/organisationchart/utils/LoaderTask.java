package com.github.sigute.organisationchart.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.github.sigute.organisationchart.organisation.Employee;
import com.github.sigute.organisationchart.organisation.Organisation;

import java.util.List;

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
        setEmployeeImages(organisation);
        return organisation;
    }

    private void setEmployeeImages(Organisation organisation)
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
    protected void onPostExecute(Organisation organisation)
    {
        //TODO add success/failure bool and failure reason to listener?
        listener.onTaskFinished(organisation);
    }
}