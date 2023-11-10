package com.example.cyclingmobileapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.cyclingmobileapp.lib.event.RequiredField;

import java.util.List;

public class RequiredFieldList extends ArrayAdapter<RequiredField> {
    List<RequiredField> requiredFields;
    private final Activity context;

    public RequiredFieldList(Activity context, List<RequiredField> requiredFields) {
        super(context, R.layout.layout_event_type_list, requiredFields);
        this.context = context;
        this.requiredFields = requiredFields;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_required_field_list, null, true);

        TextView requiredFieldName = (TextView) listViewItem.findViewById(R.id.requiredFieldName);
        TextView requiredFieldType = (TextView) listViewItem.findViewById(R.id.requiredFieldType);

        RequiredField requiredField = requiredFields.get(position);

        requiredFieldName.setText(requiredField.getName());
        requiredFieldType.setText(requiredField.getType());

        return listViewItem;
    }
}
