package com.github.sigute.organisationchart.fragments.listfragment;

/**
 * Headers, holds its own title.
 *
 * @author Sigute
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
