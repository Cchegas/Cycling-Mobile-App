package com.example.cyclingmobileapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.cyclingmobileapp.lib.event.EventType;

import java.util.List;

public class EventTypeList extends ArrayAdapter<EventType> {
    List<EventType> eventTypes;
    private final Activity context;

    public EventTypeList(Activity context, List<EventType> eventTypes) {
        super(context, R.layout.layout_event_type_list, eventTypes);
        this.context = context;
        this.eventTypes = eventTypes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_event_type_list, null, true);

        TextView eventTypeLabel = listViewItem.findViewById(R.id.eventTypeLabelInput);

        EventType eventType = eventTypes.get(position);
        eventTypeLabel.setText(eventType.getLabel());

        return listViewItem;
    }
}
