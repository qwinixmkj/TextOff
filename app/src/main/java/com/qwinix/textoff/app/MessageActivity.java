package com.qwinix.textoff.app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


/**
 * SMS related actions
 */
public class MessageActivity extends Activity implements OnClickListener{

	private ListView mGroupLIst;
	private Button mSmsCreate;
	private String[] gnames;
	private TextView mNoGroupText;
    private int x;//hotfix changes

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_activity);
		ActionBar actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setTitle("Messages");
		mSmsCreate = (Button) findViewById(R.id.button1);
		gnames = getIntent().getStringArrayExtra("groupnames");
		mGroupLIst = (ListView) findViewById(R.id.listView1);
		mNoGroupText = (TextView) findViewById(R.id.nogroup);
		if (gnames.length == 0) {
			mNoGroupText.setVisibility(View.VISIBLE);
		}
		else
		{
			GroupListAdapter mAdapter = new GroupListAdapter(MessageActivity.this, gnames);
			mGroupLIst.setAdapter(mAdapter);
		}
		mSmsCreate.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			Intent intent = new Intent(MessageActivity.this, CreateGroupFromContacts.class);
			startActivity(intent);
			break;
		case R.id.button2:
			Intent intent1 = new Intent(MessageActivity.this, CreateGroupManuallyActivity.class);
			startActivity(intent1);
			break;
		default:
			break;
		}
	}


}
