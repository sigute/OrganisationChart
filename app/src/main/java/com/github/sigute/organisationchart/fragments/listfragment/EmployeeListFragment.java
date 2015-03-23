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


/**
 * This fragment retrieves and shows and list of employees.
 *
 * @author Sigute
 */
public class EmployeeListFragment extends ListFragment implements LoaderTask.TaskListener
{
    private EmployeeListFragmentCallbacks callbacks = sDummyEmployeeListFragmentCallbacks;

    /**
     * Must implement this! Returns data from fragment.
     */
    public interface EmployeeListFragmentCallbacks
    {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(Employee employee);

        /**
         * Callback for when an error occurs.
         */
        public void onFailure(String errorMessage);
    }

    // A dummy implementation used only when this fragment is not attached to an activity. Does not do anything.
    private static EmployeeListFragmentCallbacks sDummyEmployeeListFragmentCallbacks = new EmployeeListFragmentCallbacks()
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

        if (!(activity instanceof EmployeeListFragmentCallbacks))
        {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        callbacks = (EmployeeListFragmentCallbacks) activity;
    }

    @Override
    public void onDetach()
    {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        callbacks = sDummyEmployeeListFragmentCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id)
    {
        super.onListItemClick(listView, view, position, id);

        EmployeeListItem listItem = listItems.get(position);
        if (listItem.isHeader())
        {
            //should not get called here anyway, as set to non-clickable in adapter, but just making sure
            return;
        }

        callbacks.onItemSelected(((EmployeeListItemEmployee) listItem).getEmployee());
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
        callbacks.onFailure(errorMessage);
    }
}
