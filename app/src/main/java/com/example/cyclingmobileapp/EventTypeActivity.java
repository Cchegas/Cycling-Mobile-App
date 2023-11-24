package com.example.cyclingmobileapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.cyclingmobileapp.lib.event.EventType;
import com.example.cyclingmobileapp.lib.event.RequiredField;
import com.example.cyclingmobileapp.lib.utils.ValidationUtil;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventTypeActivity extends AppCompatActivity {

    private static final String[] requiredFieldTypes = new String[]{RequiredField.STRING_TYPE, RequiredField.INT_TYPE, RequiredField.FLOAT_TYPE};
    private EventType eventType;
    private List<RequiredField> requiredFields;
    private ListView requiredFieldListView;
    private String documentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_type);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.eventTypeToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        requiredFields = new ArrayList<RequiredField>();

        TextView eventTypeHeader = findViewById(R.id.eventTypeHeader);
        Button addFieldButton = findViewById(R.id.eventTypeAddFieldButton);
        Button disableButton = findViewById(R.id.eventTypeDisableButton);
        Button doneButton = findViewById(R.id.eventTypeDoneButton);
        requiredFieldListView = findViewById(R.id.requiredFieldListView);

        requiredFieldListView.setOnItemClickListener((adapterView, view, i, l) -> {
            RequiredField requiredField = requiredFields.get(i);
            showRequiredFieldDialog(i);
        });
        addFieldButton.setOnClickListener(view -> {
            showRequiredFieldDialog(-1);
        });
        doneButton.setOnClickListener(view -> {
            onDone();
        });

        String eventTypeLabel = getIntent().getExtras() != null ? getIntent().getExtras().getString("label") : null;
        if (eventTypeLabel != null) {
            eventTypeHeader.setText("Modify event type");

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            // Retrieve the EventType data
            db.collection(EventType.COLLECTION_NAME).whereEqualTo("label", eventTypeLabel).whereEqualTo("enabled", true).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);

                    // Store the document's Id to update it's values later
                    documentId = documentSnapshot.getId();
                    disableButton.setOnClickListener(view -> {
                        eventType.setEnabled(false);
                        eventType.upload(documentId);
                        finish();
                    });

                    eventType = new EventType((String) documentSnapshot.get("label"), (boolean) documentSnapshot.get("enabled"));
                    HashMap<String, Object> requiredFieldData = (HashMap<String, Object>) documentSnapshot.get("requiredFields");
                    for (String requiredFieldName : requiredFieldData.keySet()) {
                        RequiredField requiredField = new RequiredField(requiredFieldName, (String) requiredFieldData.get(requiredFieldName), eventType);
                        eventType.addRequiredField(requiredField);
                        requiredFields.add(requiredField);
                    }
                    updateRequiredFieldListView();
                } else {
                    makeToast("Something went wrong.");
                    finish();
                }
            });
            EditText eventTypeLabelInput = findViewById(R.id.eventTypeLabel);
            eventTypeLabelInput.setText(eventTypeLabel);
        } else {
            eventTypeHeader.setText("Add an event type");
            eventType = new EventType("", true);
            // The disable button will function as a cancel button
            disableButton.setText(R.string.cancel_btn);
            disableButton.setOnClickListener(view -> {
                finish();
            });
        }
        updateRequiredFieldListView();
    }

    private void showRequiredFieldDialog(int index) {
        if (index >= requiredFields.size()) {
            return;
        }
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_required_field_dialog, null);
        dialogBuilder.setView(dialogView);

        EditText requiredFieldNameInput = dialogView.findViewById(R.id.requiredFieldNameInput);
        Spinner requiredFieldTypeInput = dialogView.findViewById(R.id.requiredFieldTypeInput);
        ArrayAdapter<String> requiredFieldTypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, requiredFieldTypes);
        requiredFieldTypeInput.setAdapter(requiredFieldTypeAdapter);

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        Button requiredFieldDeleteButton = dialogView.findViewById(R.id.requiredFieldDeleteButton);
        Button requiredFieldUpdateButton = dialogView.findViewById(R.id.requiredFieldUpdateButton);

        if (index < 0) {
            // If the dialog is being opened to add a new view, the delete button should act as a cancel button for the dialog instead
            requiredFieldDeleteButton.setText("Cancel");
            requiredFieldUpdateButton.setText("Add");
            requiredFieldDeleteButton.setOnClickListener(view -> {
                dialog.dismiss();
            });
            // Set event listeners for the update button when adding a required field
            requiredFieldUpdateButton.setOnClickListener(view -> {
                // Add the required field to requiredFields
                String newName = requiredFieldNameInput.getText().toString().trim();
                String newType = requiredFieldTypeInput.getSelectedItem().toString();
                if (!ValidationUtil.validateRegex(this, newName, "required field name", ".*[a-zA-Z].*", "at least one letter")) {
                    return;
                }

                requiredFields.add(new RequiredField(newName, newType, eventType));
                updateRequiredFieldListView();
                dialog.dismiss();
            });
        } else {
            RequiredField requiredField = requiredFields.get(index);

            requiredFieldNameInput.setText(requiredField.getName());
            requiredFieldTypeInput.setSelection(0);

            requiredFieldDeleteButton.setText("Delete");
            requiredFieldUpdateButton.setText("Update");
            requiredFieldDeleteButton.setOnClickListener(view -> {
                requiredFields.remove(index);
                updateRequiredFieldListView();
                dialog.dismiss();
            });
            requiredFieldUpdateButton.setOnClickListener(view -> {
                // Add the required field to requiredFields
                String newName = requiredFieldNameInput.getText().toString().trim();
                String newType = requiredFieldTypeInput.getSelectedItem().toString();
                if (!ValidationUtil.validateRegex(this, newName, "required field name", ".*[a-zA-Z].*", "at least one letter")) {
                    return;
                }
                requiredField.setName(newName);
                requiredField.setType(newType);
                requiredFields.set(index, requiredField);
                updateRequiredFieldListView();
                dialog.dismiss();
            });
        }
    }

    private void onDone() {
        String eventTypeLabel = ((EditText) findViewById(R.id.eventTypeLabel)).getText().toString().trim();
        if (!ValidationUtil.validateRegex(this, eventTypeLabel, "event type label", ".*[a-zA-Z].*", "at least one letter")) {
            return;
        }

        if (requiredFields.size() < EventType.minimumNumberOfRequiredFields()) {
            makeToast("Each event type must have at least " + EventType.minimumNumberOfRequiredFields() + " required field(s).");
            return;
        }

        // Make sure that the event type label doesn't already exist on an enabled event type
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(EventType.COLLECTION_NAME).whereEqualTo("label", eventTypeLabel).whereEqualTo("enabled", true).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<DocumentSnapshot> documents = task.getResult().getDocuments();
                if (documents.size() > 0 && !documents.get(0).getId().equals(documentId)) {
                    makeToast("An enabled event type already exists with the same name!");
                    return;
                }
                // No event type with the same name already exists, so it can be added in or updated
                String oldLabel = eventType.getLabel();
                eventType.setLabel(eventTypeLabel);
                // Replace the requiredFields for the event type
                eventType.setRequiredFields(requiredFields);
                if (oldLabel.equals("") || documentId == null) {
                    // If the event type is new, insert it as a new document
                    eventType.upload();
                } else {
                    // Otherwise, replace the data of the document at documentID
                    eventType.upload(documentId);
                }
                finish();
            } else {
                makeToast("Something went wrong!");
                finish();
            }
        });
    }

    private void updateRequiredFieldListView() {
        RequiredFieldList requiredFieldListAdapter = new RequiredFieldList(this, requiredFields);
        requiredFieldListView.setAdapter(requiredFieldListAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void makeToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}