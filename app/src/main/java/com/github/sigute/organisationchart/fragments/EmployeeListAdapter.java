package com.github.sigute.organisationchart.fragments;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.sigute.organisationchart.R;

import java.util.ArrayList;

/**
 * Created by spikereborn on 21/03/2015.
 */
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
        final EmployeeListItem i = listItems.get(position);
        if (i != null)
        {
            if (i.isHeader())
            {
                EmployeeListItemHeader header = (EmployeeListItemHeader) i;
                v = View.inflate(getContext(), R.layout.list_item_header, null);
                v.setOnClickListener(null);
                v.setOnLongClickListener(null);
                v.setLongClickable(false);
                final TextView sectionView = (TextView) v.findViewById(R.id.text_view_header);
                sectionView.setText(header.getHeaderTitle());
            }
            else
            {
                EmployeeListItemEmployee ei = (EmployeeListItemEmployee) i;
                v = View.inflate(getContext(), R.layout.list_item_employee, null);
                //TODO bind views for employee cell
                //final TextView title = (TextView) v.findViewById(R.id.list_item_entry_title);
            }
        }
        return v;
    }
}
