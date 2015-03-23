package com.github.sigute.organisationchart.fragments.listfragment;

/**
 * Interface for objects in employee list. Objects can be either employees or section headers.
 * Headers are treated differently - non clickable, shown in different style.
 * Interface allows inserting both to the same lsit and checking.
 *
 * @author Sigute
 */
public interface EmployeeListItem
{
    public boolean isHeader();
}
