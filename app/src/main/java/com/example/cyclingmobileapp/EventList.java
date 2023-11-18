package com.example.cyclingmobileapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.cyclingmobileapp.lib.event.Event;
import com.example.cyclingmobileapp.lib.event.EventType;

import java.util.List;

public class EventList extends ArrayAdapter<Event> {
    List<Event> events;
    private final Activity context;

    public EventList(Activity context, List<Event> events) {
        super(context, R.layout.layout_event_type_list, events);
        this.context = context;
        this.events = events;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_event_list, null, true);

        TextView eventLabel = (TextView) listViewItem.findViewById(R.id.eventLabel);

        Event event = events.get(position);
        eventLabel.setText(event.getTitle());

        return listViewItem;
    }
}
