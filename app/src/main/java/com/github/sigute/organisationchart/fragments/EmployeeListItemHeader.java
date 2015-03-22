package com.github.sigute.organisationchart.fragments;

/**
 * Created by spikereborn on 21/03/2015.
 */
public class EmployeeListItemHeader implements EmployeeListItem
{
    private String headerTitle;

    public EmployeeListItemHeader(String headerTitle)
    {
        this.headerTitle = headerTitle;
    }

    @Override
    public boolean isHeader()
    {
        return true;
    }

    public String getHeaderTitle()
    {
        return headerTitle;
    }
}
