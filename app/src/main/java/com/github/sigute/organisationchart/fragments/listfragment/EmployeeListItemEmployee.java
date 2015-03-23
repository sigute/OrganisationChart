package com.github.sigute.organisationchart.fragments.listfragment;

import com.github.sigute.organisationchart.organisation.Employee;

/**
 * Holds an employee. Not a header.
 *
 * @author Sigute
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
