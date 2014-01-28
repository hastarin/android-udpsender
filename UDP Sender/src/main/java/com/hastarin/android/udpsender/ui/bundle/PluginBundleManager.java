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

package com.hastarin.android.udpsender.ui.bundle;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.hastarin.android.udpsender.Constants;

/**
 * Class for managing the {@link com.twofortyfouram.locale.Intent#EXTRA_BUNDLE} for this plug-in.
 */
public final class PluginBundleManager {
    /**
     * Type: {@code String}.
     * <p/>
     * Key for bundled data of a Host to send data to.
     */
    public static final String BUNDLE_EXTRA_STRING_HOST = "com.hastarin.android.udpsender.extra.HOST"; //$NON-NLS-1$

    /**
     * Type: {@code String}.
     * <p/>
     * Key for bundled data of a Port to send data to.
     */
    public static final String BUNDLE_EXTRA_INT_PORT = "com.hastarin.android.udpsender.extra.PORT"; //$NON-NLS-1$

    /**
     * Type: {@code String}.
     * <p/>
     * Key for bundled data of a Port to send data to.
     */
    public static final String BUNDLE_EXTRA_STRING_PORT = "com.hastarin.android.udpsender.extra.PORT2"; //$NON-NLS-1$

    /**
     * Type: {@code String}.
     * <p/>
     * Key for bundled data of text data to send.
     */
    public static final String BUNDLE_EXTRA_STRING_TEXT = "com.hastarin.android.udpsender.extra.TEXT"; //$NON-NLS-1$

    /**
     * Type: {@code String}.
     * <p/>
     * Key for bundled data of hex data to send.
     */
    public static final String BUNDLE_EXTRA_STRING_HEX = "com.hastarin.android.udpsender.extra.HEX"; //$NON-NLS-1$

    /**
     * Type: {@code String}.
     * <p/>
     * Key for bundled data of hex data to send.
     */
    public static final String BUNDLE_EXTRA_BOOL_INPUTTEXT = "com.hastarin.android.udpsender.extra.INPUTTEXT"; //$NON-NLS-1$

    /**
     * Type: {@code int}.
     * <p/>
     * versionCode of the plug-in that saved the Bundle.
     */
    /*
     * This extra is not strictly required, however it makes backward and forward compatibility significantly
     * easier. For example, suppose a bug is found in how some version of the plug-in stored its Bundle. By
     * having the version, the plug-in can better detect when such bugs occur.
     */
    public static final String BUNDLE_EXTRA_INT_VERSION_CODE =
            "com.hastarin.android.udpsender.extra.INT_VERSION_CODE"; //$NON-NLS-1$

    // Used by Tasker to replace variables in the Bundle
    public static final String TASKER_EXTRAS_VARIABLES_REPLACE_KEYS = "net.dinglisch.android.tasker.extras.VARIABLE_REPLACE_KEYS";

    // Bundle should contain this many keys
    private static final int EXPECTED_KEYS = 5;

    /**
     * Method to verify the content of the bundle are correct.
     * <p/>
     * This method will not mutate {@code bundle}.
     *
     * @param bundle bundle to verify. May be null, which will always return false.
     * @return true if the Bundle is valid, false if the bundle is invalid.
     */
    public static boolean isBundleValid(final Bundle bundle) {
        if (null == bundle) {
            return false;
        }

        /*
         * Make sure the expected extras exist
         */
        if (!bundle.containsKey(BUNDLE_EXTRA_STRING_HOST)) {
            if (Constants.IS_LOGGABLE) {
                Log.e(Constants.LOG_TAG,
                        String.format("bundle must contain extra %s", BUNDLE_EXTRA_STRING_HOST)); //$NON-NLS-1$
            }
            return false;
        }
        if (!bundle.containsKey(BUNDLE_EXTRA_INT_PORT) && !bundle.containsKey(BUNDLE_EXTRA_STRING_PORT)) {
            if (Constants.IS_LOGGABLE) {
                Log.e(Constants.LOG_TAG,
                        String.format("bundle must contain extra %s or %s", BUNDLE_EXTRA_INT_PORT, BUNDLE_EXTRA_STRING_PORT)); //$NON-NLS-1$
            }
            return false;
        }
        if (!bundle.containsKey(BUNDLE_EXTRA_STRING_TEXT) && !bundle.containsKey(BUNDLE_EXTRA_STRING_HEX)) {
            if (Constants.IS_LOGGABLE) {
                Log.e(Constants.LOG_TAG,
                        String.format("bundle must contain extra %s or %s", BUNDLE_EXTRA_STRING_TEXT, BUNDLE_EXTRA_STRING_HEX)); //$NON-NLS-1$
            }
            return false;
        }
        if (!bundle.containsKey(BUNDLE_EXTRA_INT_VERSION_CODE)) {
            if (Constants.IS_LOGGABLE) {
                Log.e(Constants.LOG_TAG,
                        String.format("bundle must contain extra %s", BUNDLE_EXTRA_INT_VERSION_CODE)); //$NON-NLS-1$
            }
            return false;
        }

        /*
         * Make sure the correct number of extras exist. Run this test after checking for specific Bundle
         * extras above so that the error message is more useful. (E.g. the caller will see what extras are
         * missing, rather than just a message that there is the wrong number).
         */
        if (EXPECTED_KEYS > bundle.keySet().size()) {
            if (Constants.IS_LOGGABLE) {
                Log.e(Constants.LOG_TAG,
                        String.format("bundle must contain %d keys, but currently contains %d keys: %s", EXPECTED_KEYS, bundle.keySet().size(), bundle.keySet())); //$NON-NLS-1$
            }
            return false;
        }

        if (TextUtils.isEmpty(bundle.getString(BUNDLE_EXTRA_STRING_HOST))) {
            if (Constants.IS_LOGGABLE) {
                Log.e(Constants.LOG_TAG,
                        String.format("bundle extra %s appears to be null or empty.  It must be a non-empty string", BUNDLE_EXTRA_STRING_HOST)); //$NON-NLS-1$
            }
            return false;
        }

        if (bundle.containsKey(BUNDLE_EXTRA_INT_PORT)) {
            if (bundle.getInt(BUNDLE_EXTRA_INT_PORT, 0) != bundle.getInt(BUNDLE_EXTRA_INT_PORT, 1)) {
                if (Constants.IS_LOGGABLE) {
                    Log.e(Constants.LOG_TAG,
                            String.format("bundle extra %s appears to be the wrong type.  It must be an int", BUNDLE_EXTRA_INT_VERSION_CODE)); //$NON-NLS-1$
                }

                return false;
            }
        }

        if (bundle.getInt(BUNDLE_EXTRA_INT_VERSION_CODE, 0) != bundle.getInt(BUNDLE_EXTRA_INT_VERSION_CODE, 1)) {
            if (Constants.IS_LOGGABLE) {
                Log.e(Constants.LOG_TAG,
                        String.format("bundle extra %s appears to be the wrong type.  It must be an int", BUNDLE_EXTRA_INT_VERSION_CODE)); //$NON-NLS-1$
            }

            return false;
        }

        return true;
    }

    /**
     * @param context Application context.
     * @param host    The host value to be displayed by the plug-in. Cannot be null.
     * @param port    The port value to be displayed by the plug-in. Cannot be null.
     * @param text    The text value to be displayed by the plug-in. Can be null.
     * @param hex     The hex value to be displayed by the plug-in. Can be null.
     * @param inputText True if the plugin is set to allow text input. Can be null.
     * @return A plug-in bundle.
     */
    public static Bundle generateBundle(final Context context, final String host, final String port, final String text, final String hex, final boolean inputText) {
        final Bundle result = new Bundle();
        result.putInt(BUNDLE_EXTRA_INT_VERSION_CODE, Constants.getVersionCode(context));
        result.putString(BUNDLE_EXTRA_STRING_HOST, host);
        result.putString(BUNDLE_EXTRA_STRING_PORT, port);
        result.putString(BUNDLE_EXTRA_STRING_TEXT, text);
        result.putString(BUNDLE_EXTRA_STRING_HEX, hex);
        result.putString(TASKER_EXTRAS_VARIABLES_REPLACE_KEYS, BUNDLE_EXTRA_STRING_TEXT);
        result.putBoolean(BUNDLE_EXTRA_BOOL_INPUTTEXT, inputText);

        return result;
    }

    /**
     * Private constructor prevents instantiation
     *
     * @throws UnsupportedOperationException because this class cannot be instantiated.
     */
    private PluginBundleManager() {
        throw new UnsupportedOperationException("This class is non-instantiable"); //$NON-NLS-1$
    }
}