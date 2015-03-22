package com.github.sigute.organisationchart.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.github.sigute.organisationchart.R;
import com.github.sigute.organisationchart.fragments.EmployeeDetailFragment;
import com.github.sigute.organisationchart.fragments.listfragment.EmployeeListFragment;
import com.github.sigute.organisationchart.organisation.Employee;


public class EmployeeListActivity extends ActionBarActivity
        implements EmployeeListFragment.Callbacks
{
    //TODO decide what to do about tablet mode!

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_list);

        if (findViewById(R.id.employee_detail_container) != null)
        {
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((EmployeeListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.employee_list)).setActivateOnItemClick(true);
        }
    }

    @Override
    public void onItemSelected(Employee employee)
    {
        if (mTwoPane)
        {
            Bundle arguments = new Bundle();
            arguments.putString(EmployeeDetailFragment.EmployeeKeys.NAME, employee.getFirstName());
            arguments
                    .putString(EmployeeDetailFragment.EmployeeKeys.SURNAME, employee.getLastName());
            arguments.putString(EmployeeDetailFragment.EmployeeKeys.ROLE, employee.getRole());
            arguments.putString(EmployeeDetailFragment.EmployeeKeys.ID, employee.getId());

            EmployeeDetailFragment fragment = new EmployeeDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.employee_detail_container, fragment).commit();
        }
        else
        {
            Intent detailIntent = new Intent(this, EmployeeDetailActivity.class);
            detailIntent
                    .putExtra(EmployeeDetailFragment.EmployeeKeys.NAME, employee.getFirstName());
            detailIntent
                    .putExtra(EmployeeDetailFragment.EmployeeKeys.SURNAME, employee.getLastName());
            detailIntent.putExtra(EmployeeDetailFragment.EmployeeKeys.ROLE, employee.getRole());
            detailIntent.putExtra(EmployeeDetailFragment.EmployeeKeys.ID, employee.getId());
            startActivity(detailIntent);
        }
    }
}
