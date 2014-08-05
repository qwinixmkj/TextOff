package com.qwinix.textoff.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class MainActivity extends Activity implements View.OnClickListener{
    private LinearLayout mChoseMessage, mChoseGroup;
    private TextView mMessageText, mGroupText;
    private HashMap<String, String> mgroups = new HashMap<String, String>();
    private SharedPreferences mPref;
    private Button mGroup, mMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);

        mPref = getSharedPreferences("blocksms", Context.MODE_PRIVATE);
        mChoseMessage = (LinearLayout) findViewById( R.id.spinner1ll);
        mChoseGroup = (LinearLayout) findViewById(R.id.spinner2ll);
        mMessageText = (TextView) findViewById(R.id.spinner1);
        mGroupText = (TextView) findViewById(R.id.spinner2);
        mGroup = (Button) findViewById(R.id.button1);
        mMessage = (Button) findViewById(R.id.button2);

        mChoseMessage.setOnClickListener(this);
        mChoseGroup.setOnClickListener(this);
        mGroup.setOnClickListener(this);

		/* if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }*/

        getGroupLists();
    }


    private void getGroupLists() {
        final String[] GROUP_PROJECTION = new String[] {
                ContactsContract.Groups._ID, ContactsContract.Groups.TITLE };
        Cursor cursor = getContentResolver().query(
                ContactsContract.Groups.CONTENT_URI, GROUP_PROJECTION,  ContactsContract.Groups.DELETED+"!='1' AND "+
                        ContactsContract.Groups.GROUP_VISIBLE+"!='0' ",
                null, ContactsContract.Groups.TITLE);




        while (cursor.moveToNext()) {

            String id = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Groups._ID));

            String gTitle = (cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Groups.TITLE)));

            if (gTitle.contains("Google+")) {
                // gTitle = gTitle.substring(gTitle.indexOf("Group:") + 6).trim();

            }
            else if (gTitle.contains("Favorite_")) {
                // gTitle = "Favorites";
            }
            else if (gTitle.contains("My Contacts")) {
                // gTitle = "Favorites";
            }
            else if (gTitle.contains("Starred in Android")
                    || gTitle.contains("My Contacts")) {
                continue;
            }
            else

                mgroups.put(gTitle, id);
        }
        System.out.println("Map : " + mgroups);

        // Log.d("GrpId  Title", gObj.getGroupIdList() +
        // gObj.getGroupTitle());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.spinner1ll:

                break;
            case R.id.spinner2ll:
                final CharSequence[] catlist = mgroups.keySet().toArray(new String[mgroups.size()]);
                if (catlist.length == 0) {
                    Toast.makeText(MainActivity.this, "No groups present", Toast.LENGTH_LONG).show();
                }
                else
                {
                    int selected =-1;
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Choose Group");
                    builder.setSingleChoiceItems(
                            catlist,
                            selected,
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(
                                        DialogInterface dialog,
                                        int which) {
                                    mGroupText.setText(catlist[which]);
                                    //Log.d("mathew", mgroups.get(catlist[which]));
                                    getPhoneNumbers(mgroups.get(catlist[which]));
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                break;

            case R.id.button1:
                String[] groups = mgroups.keySet().toArray(new String[mgroups.size()]);
                Intent intent = new Intent(MainActivity.this, GroupActivity.class);
                intent.putExtra("groupnames", groups);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    public void getPhoneNumbers(String groupID) {

        Log.d("mathew", groupID);
        ArrayList<String> mPhonenumbers = new ArrayList<String>();
        Set<String> phoneSet = new HashSet<String>();
        Uri groupURI = ContactsContract.Data.CONTENT_URI;
        String[] projection = new String[] {
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID };

        Cursor c = getContentResolver().query(
                groupURI,
                projection,
                ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID
                        + "=" + Integer.parseInt(groupID), null, null);

        while (c.moveToNext()) {
            String id = c
                    .getString(c
                            .getColumnIndex(ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID));
            Cursor pCur = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                    new String[] { id }, null);

            while (pCur.moveToNext()) {
                //  ConatctData data = new ConatctData();
				/*data.name = pCur
                        .getString(pCur
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
				 */
                String phone = pCur
                        .getString(pCur
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                // mPhonenumbers.add(phone);
                phoneSet.add(phone);
                Log.d("mathew", phone);
            }

            pCur.close();
            if (phoneSet.size() > 0) {
                mPref.edit().putStringSet("phonenumbers", phoneSet).commit();
            }
            //    System.out.println("number " +phoneSet);

        }
    }



    /**
     * A placeholder fragment containing a simple view.
     */
	/*  public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            return rootView;
        }


    }
	 */

}
