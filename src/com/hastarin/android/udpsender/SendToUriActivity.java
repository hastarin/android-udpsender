package com.hastarin.android.udpsender;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Locale;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.Toast;
//import android.util.Log;

public class SendToUriActivity extends Activity {

	public static byte[] hexStringToBytes(String input) {
		input = input.toLowerCase(Locale.US);
		int n = input.length() / 2;
		byte[] output = new byte[n];
		int l = 0;
		for (int k = 0; k < n; k++) {
			char c = input.charAt(l++);
			byte b = (byte) ((c >= 'a' ? (c - 'a' + 10) : (c - '0')) << 4);
			c = input.charAt(l++);
			b |= (byte) (c >= 'a' ? (c - 'a' + 10) : (c - '0'));
			output[k] = b;
		}
		return output;
	}
	
	public static String bytesToHex(byte[] bytes) {
	    final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
	    char[] hexChars = new char[bytes.length * 2];
	    int v;
	    for ( int j = 0; j < bytes.length; j++ ) {
	        v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		final Uri uri = intent.getData();
        if (uri == null) return;
		//Log.v(getString(R.string.app_name), "Intent received " + uri.toString());
		String msg = uri.getLastPathSegment();
        if(msg == null) return;
        byte[] msgBytes = msg.getBytes();
		if (msg.startsWith("\\0x")) {
			msg = msg.replace("\\0x", "0x");
			msgBytes = msg.getBytes();
		} else if (msg.startsWith("0x")) {
			msg = msg.replace("0x", "");
			msgBytes = hexStringToBytes(msg);
		}

		final byte[] buf = msgBytes;
		
		//Log.d(getString(R.string.app_name), msgBytes.toString());
		//Log.d(getString(R.string.app_name), "0x" + bytesToHex(msgBytes));

		new Thread(new Runnable() {
			public void run() {
				try {
					InetAddress serverAddress = InetAddress.getByName(uri
							.getHost());
					//Log.v(getString(R.string.app_name), serverAddress.getHostAddress());
					DatagramSocket socket = new DatagramSocket();
					DatagramPacket packet = new DatagramPacket(buf, buf.length,
							serverAddress, uri.getPort());
					socket.send(packet);
					socket.close();
				} catch (final UnknownHostException e) {
                    Activity activity = getParent();
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), e.toString(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
					e.printStackTrace();
				} catch (final SocketException e) {
                    Activity activity = getParent();
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), e.toString(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
					e.printStackTrace();
				} catch (final IOException e) {
                    Activity activity = getParent();
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), e.toString(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
					e.printStackTrace();
				}
			}
		}).start();

		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

}
