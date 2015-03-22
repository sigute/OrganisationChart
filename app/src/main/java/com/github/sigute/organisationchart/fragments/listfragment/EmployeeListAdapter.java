package com.github.sigute.organisationchart.fragments.listfragment;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.sigute.organisationchart.R;
import com.github.sigute.organisationchart.organisation.Employee;

import java.util.ArrayList;

public class EmployeeListAdapter extends ArrayAdapter<EmployeeListItem>
{
    ArrayList<EmployeeListItem> listItems;

    public EmployeeListAdapter(Context context, ArrayList<EmployeeListItem> items)
    {
        super(context, 0, items);
        listItems = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v = convertView;
        final EmployeeListItem listItem = listItems.get(position);
        if (listItem != null)
        {
            if (listItem.isHeader())
            {
                EmployeeListItemHeader header = (EmployeeListItemHeader) listItem;
                v = View.inflate(getContext(), R.layout.list_item_header, null);
                v.setOnClickListener(null);
                v.setOnLongClickListener(null);
                v.setLongClickable(false);
                v.setClickable(false);
                final TextView sectionView = (TextView) v.findViewById(R.id.text_view_header);
                sectionView.setText(header.getHeaderTitle());
            }
            else
            {
                EmployeeListItemEmployee employeeListItemEmployee = (EmployeeListItemEmployee) listItem;
                Employee employee = employeeListItemEmployee.getEmployee();

                v = View.inflate(getContext(), R.layout.list_item_employee, null);
                final TextView nameSurname = (TextView) v
                        .findViewById(R.id.text_view_list_name_surname);
                nameSurname.setText(employee.getFirstName() + " " + employee.getLastName());

                final TextView roleTextView = (TextView) v.findViewById(R.id.text_view_list_role);
                if (employee.isCEO() || employee.isTeamLeader())
                {
                    roleTextView.setText(employee.getRole());
                }
                else
                {
                    roleTextView.setVisibility(View.GONE);
                }
            }
        }
        return v;
    }
}
