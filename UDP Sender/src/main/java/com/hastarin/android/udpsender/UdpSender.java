package com.hastarin.android.udpsender;

import android.content.Context;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.*;

import static com.hastarin.android.udpsender.HexHelper.bytesToHex;
import static com.hastarin.android.udpsender.HexHelper.hexStringToBytes;

public class UdpSender {
    final Handler toastHandler = new Handler();
    public void SendTo(final Context context, final Uri uri) {

        if (uri == null) return;
        String msg = Uri.decode(uri.getLastPathSegment());
        if(msg == null) return;
        byte[] msgBytes = msg.getBytes();
        if (msg.startsWith("\\0x")) {
            msg = msg.replace("\\0x", "0x");
            msgBytes = msg.getBytes();
        } else if (msg.startsWith("0x")) {
            msg = msg.replace("0x", "");
            if(!msg.matches("[a-fA-F0-9]+")) {
            	Toast.makeText(context, "ERROR: Invalid hex values", Toast.LENGTH_LONG).show();
            	return;
            }
            msgBytes = hexStringToBytes(msg);
        }

        final byte[] buf = msgBytes;

        String appName = context.getString(R.string.app_name);
        if(Constants.IS_LOGGABLE) {
            Log.d(appName, new String(msgBytes));
            Log.d(appName, "0x" + bytesToHex(msgBytes));
        }

        // see if we can find a localPort query parameter
        UrlQuerySanitizer sanitizer = new UrlQuerySanitizer();
        sanitizer.setAllowUnregisteredParamaters(true);
        sanitizer.parseUrl(uri.toString());
        final String _localPort = sanitizer.getValue("localPort");

        new Thread(new Runnable() {
            public void run() {
                try {
                    String host = uri.getHost();
                    String hostAddress = host.substring(0, host.lastIndexOf(":"));
                    int hostPort = Integer.parseInt(host.substring(host.lastIndexOf(":")+1));

                    InetAddress serverAddress = InetAddress.getByName(hostAddress);
                    //Log.v(getString(R.string.app_name), serverAddress.getHostAddress());
                    DatagramSocket socket;
                    if(_localPort != null)
                        socket = new DatagramSocket(Integer.parseInt(_localPort));
                    else
                        socket = new DatagramSocket(null);
//                    if (!socket.getBroadcast()) socket.setBroadcast(true);
                    DatagramPacket packet = new DatagramPacket(buf, buf.length,
                            serverAddress, hostPort);

                    socket.send(packet);
                    socket.close();
                } catch (final UnknownHostException e) {
                    toastHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, e.toString(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    e.printStackTrace();
                } catch (final SocketException e) {
                    toastHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, e.toString(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    e.printStackTrace();
                } catch (final IOException e) {
                    toastHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, e.toString(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
