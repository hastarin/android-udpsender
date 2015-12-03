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

package com.hastarin.android.udpsender.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;
import com.hastarin.android.udpsender.R;
import com.hastarin.android.udpsender.TaskerPlugin;
import com.hastarin.android.udpsender.ui.bundle.BundleScrubber;
import com.hastarin.android.udpsender.ui.bundle.PluginBundleManager;

/**
 * This is the "Edit" activity for a Locale Plug-in.
 * <p/>
 * This Activity can be started in one of two states:
 * <ul>
 * <li>New plug-in instance: The Activity's Intent will not contain
 * {@link com.twofortyfouram.locale.Intent#EXTRA_BUNDLE}.</li>
 * <li>Old plug-in instance: The Activity's Intent will contain
 * {@link com.twofortyfouram.locale.Intent#EXTRA_BUNDLE} from a previously saved plug-in instance that the
 * user is editing.</li>
 * </ul>
 *
 * @see com.twofortyfouram.locale.Intent#ACTION_EDIT_SETTING
 * @see com.twofortyfouram.locale.Intent#EXTRA_BUNDLE
 */
public final class EditActivity extends AbstractPluginActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BundleScrubber.scrub(getIntent());

        final Bundle localeBundle = getIntent().getBundleExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE);
        BundleScrubber.scrub(localeBundle);

        setContentView(R.layout.activity_main);

        ((Button) findViewById(R.id.buttonSend)).setVisibility(View.GONE);
        if (null == savedInstanceState) {
            if (PluginBundleManager.isBundleValid(localeBundle)) {
                final boolean textInput = localeBundle.getBoolean(PluginBundleManager.BUNDLE_EXTRA_BOOL_INPUTTEXT);
                ((ToggleButton)findViewById(R.id.toggleButton)).setChecked(textInput);
                final String host =
                        localeBundle.getString(PluginBundleManager.BUNDLE_EXTRA_STRING_HOST);
                EditTextIPAddress editTextIPAddress = (EditTextIPAddress) findViewById(R.id.editTextIP);
                if (textInput) {
                    editTextIPAddress.setInputType(InputType.TYPE_CLASS_TEXT);
                }
                editTextIPAddress.setText(host);
                String portText = "";
                if (localeBundle.containsKey(PluginBundleManager.BUNDLE_EXTRA_STRING_PORT))
                {
                   portText = localeBundle.getString(PluginBundleManager.BUNDLE_EXTRA_STRING_PORT);
                }
                else
                {
                    final int port =
                            localeBundle.getInt(PluginBundleManager.BUNDLE_EXTRA_INT_PORT);
                    portText = Integer.toString(port);
                }
                EditText editText = (EditText) findViewById(R.id.editTextPort);
                if (textInput) {
                    editText.setInputType(InputType.TYPE_CLASS_TEXT);
                }
                editText.setText(portText);
                final String text =
                        localeBundle.getString(PluginBundleManager.BUNDLE_EXTRA_STRING_TEXT);
                if (!TextUtils.isEmpty(text)) {
                    ((EditText) findViewById(R.id.editTextData)).setText(text);
                }
                final String hex =
                        localeBundle.getString(PluginBundleManager.BUNDLE_EXTRA_STRING_HEX);
                if (!TextUtils.isEmpty(hex)) {
                    ((EditTextHex) findViewById(R.id.editTextHexData)).setText(hex);
                }
            }
        }
    }

    @Override
    public void finish() {
        if (!isCanceled()) {
            final String host = ((EditTextIPAddress) findViewById(R.id.editTextIP)).getText().toString();
            final String text = ((EditText) findViewById(R.id.editTextData)).getText().toString();
            final String hex = ((EditTextHex) findViewById(R.id.editTextHexData)).getText().toString();
            final boolean inputText = ((ToggleButton) findViewById(R.id.toggleButton)).isChecked();
            try {
                final String port = ((EditText) findViewById(R.id.editTextPort)).getText().toString();
                //TODO: replace with editable field
                final String localPort = "21345";
                if (host.length() > 0 && port.length() > 0 && (text.length() > 0 || hex.length() > 0)) {
                    final Intent resultIntent = new Intent();

                /*
                 * This extra is the data to ourselves: either for the Activity or the BroadcastReceiver. Note
                 * that anything placed in this Bundle must be available to Locale's class loader. So storing
                 * String, int, and other standard objects will work just fine. Parcelable objects are not
                 * acceptable, unless they also implement Serializable. Serializable objects must be standard
                 * Android platform objects (A Serializable class private to this plug-in's APK cannot be
                 * stored in the Bundle, as Locale's classloader will not recognize it).
                 */
                    final Bundle resultBundle =
                            PluginBundleManager.generateBundle(getApplicationContext(), host, port, text, hex, inputText, localPort);
                    resultIntent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE, resultBundle);

                    if ( TaskerPlugin.Setting.hostSupportsOnFireVariableReplacement( this ) )
                        TaskerPlugin.Setting.setVariableReplaceKeys( resultBundle, new String [] { PluginBundleManager.BUNDLE_EXTRA_STRING_HOST, PluginBundleManager.BUNDLE_EXTRA_STRING_PORT, PluginBundleManager.BUNDLE_EXTRA_STRING_TEXT } );
                /*
                 * The blurb is concise status text to be displayed in the host's UI.
                 */
                    Uri.Builder builder = new Uri.Builder();
                    builder.scheme("udp").authority(host + ":" + port);
                    builder.appendPath(TextUtils.isEmpty(hex) ? text : "0x" + hex );
                    builder.appendQueryParameter("localPort", localPort);

                    final String blurb = generateBlurb(getApplicationContext(), builder.build().toString());
                    resultIntent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_STRING_BLURB, blurb);

                    setResult(RESULT_OK, resultIntent);
                }
            }
            catch (NumberFormatException exception) {

            }
        }

        super.finish();
    }

    /**
     * @param context Application context.
     * @param message The toast message to be displayed by the plug-in. Cannot be null.
     * @return A blurb for the plug-in.
     */
    /* package */
    static String generateBlurb(final Context context, final String message) {
        final int maxBlurbLength =
                context.getResources().getInteger(R.integer.twofortyfouram_locale_maximum_blurb_length);

        if (message.length() > maxBlurbLength) {
            return message.substring(0, maxBlurbLength);
        }

        return message;
    }

    public void onToggleClicked(View view) {
        boolean on = ((ToggleButton) view).isChecked();


        EditText editTextIp = (EditText) findViewById(R.id.editTextIP);
        EditText editTextPort = (EditText) findViewById(R.id.editTextPort);
        if (on) {
            editTextIp.setInputType(InputType.TYPE_CLASS_TEXT);
            editTextPort.setInputType(InputType.TYPE_CLASS_TEXT);
        } else {
            editTextIp.setInputType(InputType.TYPE_CLASS_PHONE);
            editTextPort.setInputType(InputType.TYPE_CLASS_PHONE);
        }
    }
}