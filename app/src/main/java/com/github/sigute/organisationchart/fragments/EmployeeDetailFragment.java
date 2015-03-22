package com.github.sigute.organisationchart.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.sigute.organisationchart.R;

public class EmployeeDetailFragment extends Fragment
{
    //TODO also add image!

    public class EmployeeKeys
    {
        public static final String NAME = "EMPLOYEE_NAME";
        public static final String SURNAME = "EMPLOYEE_SURNAME";
        public static final String ROLE = "EMPLOYEE_ROLE";
        public static final String ID = "EMPLOYEE_ID";
    }

    private String name;
    private String surname;
    private String role;
    private String id;

    public EmployeeDetailFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        name = getArguments().getString(EmployeeKeys.NAME, "");
        surname = getArguments().getString(EmployeeKeys.SURNAME, "");
        role = getArguments().getString(EmployeeKeys.ROLE, "");
        id = getArguments().getString(EmployeeKeys.ID, "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_employee_detail, container, false);


        ((TextView) rootView.findViewById(R.id.text_view_employee_name)).setText(name);
        ((TextView) rootView.findViewById(R.id.text_view_employee_surname)).setText(surname);
        ((TextView) rootView.findViewById(R.id.text_view_employee_role)).setText(role);
        ((TextView) rootView.findViewById(R.id.text_view_employee_id)).setText(id);

        return rootView;
    }
}
