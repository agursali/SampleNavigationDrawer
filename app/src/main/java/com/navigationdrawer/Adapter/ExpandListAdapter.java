package com.navigationdrawer.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.navigationdrawer.GetSet.ExpandListGetSet;
import com.navigationdrawer.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by AmolGursali on 9/11/2018.
 */

public class ExpandListAdapter extends BaseExpandableListAdapter
{
    Context context;
    ArrayList<ExpandListGetSet> listDataHeader;
    HashMap<ExpandListGetSet, List<String>> listDataChild;

    public ExpandListAdapter(Context context, ArrayList<ExpandListGetSet> listDataHeader, HashMap<ExpandListGetSet, List<String>> listDataChild)
    {
        this.context=context;
        this.listDataHeader=listDataHeader;
        this.listDataChild=listDataChild;
    }

    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listDataChild.get(this.listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        return listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listDataChild.get(this.listDataHeader.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        ExpandListGetSet expandListGetSet = listDataHeader.get(groupPosition);
//        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group_header, null);
        }

        TextView lblListHeader = convertView
                .findViewById(R.id.lblListHeader);

        ImageView img = convertView
                .findViewById(R.id.img);


        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(expandListGetSet.getTitle());
        img.setImageResource(expandListGetSet.getImg());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group_child, null);
        }

        TextView txtListChild = convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
