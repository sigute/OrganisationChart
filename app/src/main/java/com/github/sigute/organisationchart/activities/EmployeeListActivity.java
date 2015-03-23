package com.github.sigute.organisationchart.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import com.github.sigute.organisationchart.R;
import com.github.sigute.organisationchart.fragments.EmployeeDetailFragment;
import com.github.sigute.organisationchart.fragments.listfragment.EmployeeListFragment;
import com.github.sigute.organisationchart.organisation.Employee;

/**
 * Activity shows list fragment and handles navigation to detail activity.
 * Errors while retrieving data are displayed in this activity.
 *
 * @author Sigute
 */
public class EmployeeListActivity extends ActionBarActivity
        implements EmployeeListFragment.EmployeeListFragmentCallbacks
{
    private TextView errorView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_list);

        errorView = (TextView) findViewById(R.id.text_view_employee_list_error);
        errorView.setVisibility(View.GONE);
    }

    @Override
    public void onItemSelected(Employee employee)
    {
        Intent detailIntent = new Intent(this, EmployeeDetailActivity.class);
        detailIntent.putExtra(EmployeeDetailFragment.EmployeeKeys.NAME, employee.getFirstName());
        detailIntent.putExtra(EmployeeDetailFragment.EmployeeKeys.SURNAME, employee.getLastName());
        detailIntent.putExtra(EmployeeDetailFragment.EmployeeKeys.ROLE, employee.getRole());
        detailIntent.putExtra(EmployeeDetailFragment.EmployeeKeys.ID, employee.getId());

        startActivity(detailIntent);
    }

    @Override
    public void onFailure(String errorMessage)
    {
        errorView.setText(errorMessage);
        errorView.setVisibility(View.VISIBLE);

        findViewById(R.id.employee_list).setVisibility(View.GONE);
    }
}
