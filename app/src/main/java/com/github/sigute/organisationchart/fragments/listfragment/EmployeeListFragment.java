package com.github.sigute.organisationchart.fragments.listfragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.github.sigute.organisationchart.organisation.Employee;
import com.github.sigute.organisationchart.organisation.Organisation;
import com.github.sigute.organisationchart.organisation.Team;
import com.github.sigute.organisationchart.utils.LoaderTask;

import java.util.ArrayList;

public class EmployeeListFragment extends ListFragment implements LoaderTask.TaskListener
{
    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks
    {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(Employee employee);

        public void onFailure(String errorMessage);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks()
    {
        @Override
        public void onItemSelected(Employee employee)
        {
        }

        @Override
        public void onFailure(String errorMessage)
        {
        }
    };

    ArrayList<EmployeeListItem> listItems;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EmployeeListFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new LoaderTask(getActivity(), this).execute();
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks))
        {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach()
    {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id)
    {
        super.onListItemClick(listView, view, position, id);

        EmployeeListItem listItem = (EmployeeListItem) listItems.get(position);
        if (listItem.isHeader())
        {
            //should not get called here anyway, as set to non-clickable in adapter, but just making sure
            return;
        }

        mCallbacks.onItemSelected(((EmployeeListItemEmployee) listItem).getEmployee());
    }

    @Override
    public void onTaskStarted()
    {
        //could do something here in the future, like show a custom spinner?
    }

    @Override
    public void onTaskFinished(Organisation organisation)
    {
        listItems = new ArrayList<>();
        listItems.add(new EmployeeListItemEmployee(organisation.getCEO()));
        for (Team team : organisation.getTeams())
        {
            listItems.add(new EmployeeListItemHeader(team.getName()));
            listItems.add(new EmployeeListItemEmployee(team.getTeamLeader()));
            for (Employee teamMember : team.getTeamMembers())
            {
                listItems.add(new EmployeeListItemEmployee(teamMember));
            }
        }

        EmployeeListAdapter adapter = new EmployeeListAdapter(getActivity(), listItems);
        setListAdapter(adapter);
    }

    @Override
    public void onTaskFailure(String errorMessage)
    {
        mCallbacks.onFailure(errorMessage);
    }
}
