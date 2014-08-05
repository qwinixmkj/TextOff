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

public class GroupActivity extends Activity implements OnClickListener{
	private ListView mGroupLIst;
	private Button mGroupFromContact, mGroupManual;
	private String[] gnames;
	private TextView mNoGroupText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.groups_layout);
		ActionBar actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setTitle("Groups");
		mGroupFromContact = (Button) findViewById(R.id.button1);
		mGroupManual = (Button) findViewById(R.id.button2);
		gnames = getIntent().getStringArrayExtra("groupnames");
		mGroupLIst = (ListView) findViewById(R.id.listView1);
		mNoGroupText = (TextView) findViewById(R.id.nogroup);
		if (gnames.length == 0) {
			mNoGroupText.setVisibility(View.VISIBLE);
		}
		else
		{
			GroupListAdapter mAdapter = new GroupListAdapter(GroupActivity.this, gnames);
			mGroupLIst.setAdapter(mAdapter);
		}
		mGroupFromContact.setOnClickListener(this);
		mGroupManual.setOnClickListener(this);
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
			Intent intent = new Intent(GroupActivity.this, CreateGroupFromContacts.class);
			startActivity(intent);
			break;
		case R.id.button2:
			Intent intent1 = new Intent(GroupActivity.this, CreateGroupManuallyActivity.class);
			startActivity(intent1);
			break;
		default:
			break;
		}
	}

}
