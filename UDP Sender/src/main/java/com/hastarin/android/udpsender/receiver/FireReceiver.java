/*
 * Copyright 2013 two forty four a.m. LLC <http://www.twofortyfouram.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * <http://www.apache.org/licenses/LICENSE-2.0>
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.hastarin.android.udpsender.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import com.hastarin.android.udpsender.Constants;
import com.hastarin.android.udpsender.UdpSender;
import com.hastarin.android.udpsender.ui.bundle.BundleScrubber;
import com.hastarin.android.udpsender.ui.bundle.PluginBundleManager;

import java.util.Locale;

/**
 * This is the "fire" BroadcastReceiver for a Locale Plug-in setting.
 *
 * @see com.twofortyfouram.locale.Intent#ACTION_FIRE_SETTING
 * @see com.twofortyfouram.locale.Intent#EXTRA_BUNDLE
 */
public final class FireReceiver extends BroadcastReceiver
{

    /**
     * @param context {@inheritDoc}.
     * @param intent the incoming {@link com.twofortyfouram.locale.Intent#ACTION_FIRE_SETTING} Intent. This
     *            should contain the {@link com.twofortyfouram.locale.Intent#EXTRA_BUNDLE} that was saved by
     *            {@link com.hastarin.android.udpsender.ui.EditActivity} and later broadcast by Locale.
     */
    @Override
    public void onReceive(final Context context, final Intent intent)
    {
        /*
         * Always be strict on input parameters! A malicious third-party app could send a malformed Intent.
         */

        if (!com.twofortyfouram.locale.Intent.ACTION_FIRE_SETTING.equals(intent.getAction()))
        {
            if (Constants.IS_LOGGABLE)
            {
                Log.e(Constants.LOG_TAG,
                        String.format(Locale.US, "Received unexpected Intent action %s", intent.getAction())); //$NON-NLS-1$
            }
            return;
        }

        BundleScrubber.scrub(intent);

        final Bundle bundle = intent.getBundleExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE);
        BundleScrubber.scrub(bundle);

        if (PluginBundleManager.isBundleValid(bundle))
        {
            final String host =
                    bundle.getString(PluginBundleManager.BUNDLE_EXTRA_STRING_HOST);
            String portText = "";
            if (bundle.containsKey(PluginBundleManager.BUNDLE_EXTRA_STRING_PORT))
            {
                portText = bundle.getString(PluginBundleManager.BUNDLE_EXTRA_STRING_PORT);
            }
            else
            {
                final int port =
                        bundle.getInt(PluginBundleManager.BUNDLE_EXTRA_INT_PORT);
                portText = Integer.toString(port);
            }
            String dataText =
                    bundle.getString(PluginBundleManager.BUNDLE_EXTRA_STRING_TEXT);
            final String dataHex =
                    bundle.getString(PluginBundleManager.BUNDLE_EXTRA_STRING_HEX);

            Uri.Builder builder = new Uri.Builder();

            builder.scheme("udp").authority(host + ":" + portText);

            if (dataHex.length() >= 2) {
                builder.appendPath(Uri.encode("0x" + dataHex));
            } else {
                builder.appendPath(Uri.encode(dataText));
            }

            if (bundle.containsKey(PluginBundleManager.BUNDLE_EXTRA_INT_LOCAL_PORT))
            {
                String localPortText = bundle.getString(PluginBundleManager.BUNDLE_EXTRA_INT_LOCAL_PORT);
                builder.appendQueryParameter("localPort", localPortText);
            }

            Uri uri = builder.build();
            UdpSender udpSender = new UdpSender();
            udpSender.SendTo(context, uri);
        }
    }
}