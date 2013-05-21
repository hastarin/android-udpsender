package com.hastarin.android.udpsender;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class SendToUriActivity extends Activity {

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		final Uri uri = intent.getData();
        if (Constants.IS_LOGGABLE) {
            Log.v(getString(R.string.app_name), "Intent received " + uri.toString());
        }
        UdpSender udpSender = new UdpSender();
        udpSender.SendTo(this.getApplicationContext(), uri);
        finish();
	}

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}
}
