package com.github.sigute.organisationchart.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.github.sigute.organisationchart.R;
import com.github.sigute.organisationchart.fragments.EmployeeDetailFragment;

/**
 * Activity shows detail fragment and handles navigation to list activity.
 *
 * @author Sigute
 */
public class EmployeeDetailActivity extends ActionBarActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle arguments = new Bundle();
        arguments.putString(EmployeeDetailFragment.EmployeeKeys.NAME,
                getIntent().getStringExtra(EmployeeDetailFragment.EmployeeKeys.NAME));
        arguments.putString(EmployeeDetailFragment.EmployeeKeys.SURNAME,
                getIntent().getStringExtra(EmployeeDetailFragment.EmployeeKeys.SURNAME));
        arguments.putString(EmployeeDetailFragment.EmployeeKeys.ROLE,
                getIntent().getStringExtra(EmployeeDetailFragment.EmployeeKeys.ROLE));
        arguments.putString(EmployeeDetailFragment.EmployeeKeys.ID,
                getIntent().getStringExtra(EmployeeDetailFragment.EmployeeKeys.ID));
        EmployeeDetailFragment fragment = new EmployeeDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction().add(R.id.employee_detail_container, fragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == android.R.id.home)
        {
            NavUtils.navigateUpTo(this, new Intent(this, EmployeeListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
