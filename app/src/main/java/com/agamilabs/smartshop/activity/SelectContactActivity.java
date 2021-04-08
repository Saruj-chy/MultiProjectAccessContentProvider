package com.agamilabs.smartshop.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.agamilabs.smartshop.R;
import com.agamilabs.smartshop.adapter.ContactAdapter;
import com.agamilabs.smartshop.controller.AppController;
import com.agamilabs.smartshop.model.Contacts;

import java.util.ArrayList;

public class SelectContactActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView rvContacts;
    private ContactAdapter contactAdapter;
    private Button btn_add,btn_cancel,btn_mark_all;
    private ArrayList<String> allContacts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contact);

        rvContacts = findViewById(R.id.recycler_view_contacts);
        btn_mark_all = findViewById(R.id.btn_mark_all);
        btn_add = findViewById(R.id.btn_add);
        btn_cancel = findViewById(R.id.btn_cancel);

        allContacts = new ArrayList<String>();
        ArrayList<Contacts> contacts = getContacts();

        AppController.getAppController().getInAppNotifier().log("AllContacts",allContacts.toString());
        AppController.getAppController().getInAppNotifier().log("AllContactsSize",allContacts.size()+"");

        btn_mark_all.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        rvContacts.setHasFixedSize(true);
        rvContacts.setLayoutManager(new LinearLayoutManager(this));
        contactAdapter = new ContactAdapter(this,contacts,allContacts);
        rvContacts.setAdapter(contactAdapter);
    }

    public ArrayList<Contacts> getContacts() {
        String displayName,phoneNum,thumbnailUri;
        // Retrieve Contacts from phone
        Cursor result = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

        // Create list of contact objects
        ArrayList<Contacts> contact = new ArrayList<Contacts>();

        // For number of contacts create a contact object with name and number.
        for (int i = 0; i < result.getCount(); i++) {
            result.moveToPosition(i);
            displayName = result.getString(result.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)).trim();
            phoneNum = result.getString(result.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).trim();
            thumbnailUri = result.getString(result.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

            if(phoneNum.contains(" ")){
                phoneNum = phoneNum.replace(" ","").trim();
            }
            if(phoneNum.contains("-")){
                phoneNum = phoneNum.replace("-","").trim();
            }
            if(phoneNum.contains("+88")){
                phoneNum = phoneNum.replace("+88","").trim();
            }

            if(phoneNum.length() == 11 && !allContacts.contains(phoneNum)){
                contact.add(new Contacts(displayName,phoneNum,thumbnailUri,false));
                allContacts.add(phoneNum);
            }
            else {
                AppController.getAppController().getInAppNotifier().log("RemovePhnNum"+i,phoneNum);
            }
        }

        AppController.getAppController().getInAppNotifier().log("loadContacts",allContacts.toString());
        AppController.getAppController().getInAppNotifier().log("loadContactsSize",allContacts.size()+"");
        return contact;
    }

    @Override
    public void onClick(View view) {
        if(view == btn_add){
            AppController.getAppController().getInAppNotifier().log("SelectedPhoneNumber",contactAdapter.getSelectedPhoneNum()+"");
            AppController.getAppController().getInAppNotifier().log("SelectedPhoneNumberSize",contactAdapter.getSelectedPhoneNum().size()+"");
            NewCampaignActivity.getActivityInstance().updateRecipients(contactAdapter.getSelectedPhoneNum());
            onBackPressed();
        }
        else if(view == btn_cancel){
            onBackPressed();
        }
        else if(view == btn_mark_all){
            contactAdapter.selectAll();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}