package com.github.sigute.organisationchart.fragments;

import com.github.sigute.organisationchart.organisation.Employee;

/**
 * Created by spikereborn on 21/03/2015.
 */
public class EmployeeListItemEmployee implements EmployeeListItem
{
    private Employee employee;

    public EmployeeListItemEmployee(Employee employee)
    {
        this.employee = employee;
    }

    @Override
    public boolean isHeader()
    {
        return false;
    }

    public Employee getEmployee()
    {
        return employee;
    }
}
