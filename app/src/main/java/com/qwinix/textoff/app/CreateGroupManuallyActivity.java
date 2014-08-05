package com.qwinix.textoff.app;
import java.util.ArrayList;
import java.util.HashMap;


import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class CreateGroupManuallyActivity extends Activity implements OnClickListener {
	private EditText mGroupName, mContactName, mContactNumber;
	private Button mCreateGroup;
	private ImageButton mAddContact;
	private String groupName, contactName, contactNumber;
	private HashMap<String, String> mContacts = new HashMap<String, String>();
	private ArrayList<String> mNames = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creategroup_manually);
        ActionBar actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setTitle("Create Group");
        mGroupName = (EditText) findViewById(R.id.groupname);
        mContactName = (EditText) findViewById(R.id.contactname);
        mContactNumber = (EditText) findViewById(R.id.contactnumber);
        mCreateGroup = (Button) findViewById(R.id.creategroup);
        mAddContact = (ImageButton) findViewById(R.id.addcontact);
        mCreateGroup.setOnClickListener(this);
        mAddContact.setOnClickListener(this);
        
    }
   
    String createContact(String name){
    	ArrayList<ContentProviderOperation> ops =  new ArrayList<ContentProviderOperation>(); 
    	ContactOperation co=new ContactOperation(this);    
	        ops.add(ContentProviderOperation.newInsert( 
	            ContactsContract.RawContacts.CONTENT_URI) 
	            .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null) 
	            .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null) 
	            .build() 
	        ); 
	        co.addPhoto(ops);
	        co.addContactToGroup(ops, groupName);
	        contactName(name,ops);
	        contactNumber(mContacts.get(name),ops);
	        return contactProvider(ops);
    }
    //### Contact provider to create a new contact   
    String contactProvider(ArrayList<ContentProviderOperation> ops){      
    	String what;
        try{ 
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops); 
            what="Contact Created ";
        }  
        catch (Exception e){                
            e.printStackTrace(); 
            what="Unable to Create Contact ";
        } 
        return what;
    }
    //### Contact Name
    void contactName(String name,ArrayList<ContentProviderOperation> ops){
    	 ops.add(ContentProviderOperation.newInsert( 
	                ContactsContract.Data.CONTENT_URI)               
	                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0) 
	                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE) 
	                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,name).build() 
	            ); 
    }
    //### Contact Number
    void contactNumber(String no,ArrayList<ContentProviderOperation> ops){
    	 ops.add(ContentProviderOperation. 
	                newInsert(ContactsContract.Data.CONTENT_URI) 
	                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0) 
	                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE) 
	                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, no) 
	                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE) .build() 
	            ); 
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

	@Override
	public void onClick(View v) {
		if (v.getId() == mCreateGroup.getId()) {
			groupName = mGroupName.getText().toString();
			contactName = mContactName.getText().toString();
			contactNumber = mContactNumber.getText().toString();
			if (groupName.isEmpty()) {
				Toast.makeText(CreateGroupManuallyActivity.this, "Please specify group name", Toast.LENGTH_LONG).show();
			}
			else if (contactName.isEmpty() || contactNumber.isEmpty()) {
				Toast.makeText(CreateGroupManuallyActivity.this, "Please fill all the fields", Toast.LENGTH_LONG).show();
			}
			else
			{
				mContacts.put(contactName, contactNumber);
				mNames.add(contactName);
			}
			Log.d("mathew", "map "+mContacts.size());
			Log.d("mathew", "arrra "+mNames.size());
			if (mContacts.size() > 0) {
				for (int i = 0; i < mNames.size(); i++) {
					createContact(mNames.get(i));
				}
				
			}
			Intent intent = new Intent(CreateGroupManuallyActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
			
		}
		else if (v.getId() == mAddContact.getId()) {
			contactName = mContactName.getText().toString();
			contactNumber = mContactNumber.getText().toString();
			if (contactName.isEmpty() || contactNumber.isEmpty()) {
				Toast.makeText(CreateGroupManuallyActivity.this, "Please fill all the fields", Toast.LENGTH_LONG).show();
			}
			else
			{
				mContacts.put(contactName, contactNumber);
				mNames.add(contactName);
				mContactName.setText("");
				mContactNumber.setText("");
				mContactName.requestFocus();
			}
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		default:
			break;
		}
		return true;
	}
    
}
