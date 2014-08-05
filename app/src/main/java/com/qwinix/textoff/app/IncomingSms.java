package com.qwinix.textoff.app;

import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class IncomingSms extends BroadcastReceiver {
	private Context mContext;
	// Get the object of SmsManager
	final SmsManager sms = SmsManager.getDefault();
	private SharedPreferences mPreferences;
	private Set<String> numbers = new HashSet<String>();
	private BroadcastReceiver sendBroadcastReceiver,deliveryBroadcastReceiver;
	String SENT = "SMS_SENT";
	String DELIVERED = "SMS_DELIVERED";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		mContext = context;
		mPreferences = context.getSharedPreferences("blocksms", context.MODE_PRIVATE);
		// Retrieves a map of extended data from the intent.
		numbers = mPreferences.getStringSet("phonenumbers", null);
		final Bundle bundle = intent.getExtras();


		try {

			if (bundle != null) {

				final Object[] pdusObj = (Object[]) bundle.get("pdus");

				for (int i = 0; i < pdusObj.length; i++) {

					SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
					String phoneNumber = currentMessage.getDisplayOriginatingAddress();

					String senderNum = phoneNumber;
					String message = currentMessage.getDisplayMessageBody();
					// senderNum = "(650) 555-1000";
					Log.d("mathew", "senderNum: "+ senderNum + "; message: " + message);
					/*   if (senderNum.equalsIgnoreCase("+919738448924")) {
						abortBroadcast();
					}*/
					/*  if (senderNum.equalsIgnoreCase("123")) {
 						abortBroadcast();
 						sendSMS("5556","on vocation");
 					}*/
					if (numbers.contains(senderNum)) {
						abortBroadcast(); 
						sendSMS(senderNum,"on vocation");
					}
					else
					{

						// Show Alert
						int duration = Toast.LENGTH_LONG;
						Toast toast = Toast.makeText(context, 
								"senderNum: "+ senderNum + ", message: " + message, duration);
						toast.show();
					}

				} // end for loop
			} // bundle is null

		} catch (Exception e) {
			Log.e("SmsReceiver", "Exception smsReceiver" +e);

		}

	}    
	public void sendSMS(String number, String medMessage) {
		PendingIntent sentPI = PendingIntent.getBroadcast(mContext, 0,
				new Intent(SENT), 0);
		PendingIntent deliveredPI = PendingIntent.getBroadcast(mContext, 0,
				new Intent(DELIVERED), 0);
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(number, null, medMessage, null, null);
	}
}
