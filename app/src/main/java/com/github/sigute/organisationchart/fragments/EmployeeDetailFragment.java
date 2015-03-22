package com.github.sigute.organisationchart.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.sigute.organisationchart.R;
import com.github.sigute.organisationchart.organisation.Employee;

/**
 * A fragment representing a single Employee detail screen.
 * This fragment is either contained in a {@link com.github.sigute.organisationchart.activities.EmployeeListActivity}
 * in two-pane mode (on tablets) or a {@link com.github.sigute.organisationchart.activities.EmployeeDetailActivity}
 * on handsets.
 */
public class EmployeeDetailFragment extends Fragment
{
    private Employee employee;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EmployeeDetailFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //TODO get employee here!
        // if (getArguments().containsKey(ARG_ITEM_ID))
        // {
        // Load the dummy content specified by the fragment
        // arguments. In a real-world scenario, use a Loader
        // to load content from a content provider.

        // }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_employee_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (employee != null)
        {
            //TODO add more text views and stuff here
            ((TextView) rootView.findViewById(R.id.text_view_name))
                    .setText(employee.getFirstName());
            ((TextView) rootView.findViewById(R.id.text_view_surname))
                    .setText(employee.getLastName());
        }

        return rootView;
    }
}
