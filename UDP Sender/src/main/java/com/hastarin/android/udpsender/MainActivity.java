package com.hastarin.android.udpsender;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Restore preferences
        SharedPreferences settings = getPreferences(0);
        ToggleButton toggleButton = ((ToggleButton) findViewById(R.id.toggleButton));
        boolean checked = settings.getBoolean("toggleChecked", false);
        toggleButton.setChecked(checked);
        toggleButton.setVisibility(View.GONE);
        EditText editText = (EditText) findViewById(R.id.editTextIP);
        if (checked) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        editText.setText(settings.getString("host", ""), TextView.BufferType.EDITABLE);
        editText = (EditText) findViewById(R.id.editTextPort);
        if (checked) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        editText.setText(settings.getString("port", ""), TextView.BufferType.EDITABLE);
        editText = (EditText) findViewById(R.id.editTextData);
        editText.setText(settings.getString("dataText", ""), TextView.BufferType.EDITABLE);
        editText = (EditText) findViewById(R.id.editTextHexData);
        editText.setText(settings.getString("dataHex", ""), TextView.BufferType.EDITABLE);
    }

    @Override
    public void onPause() {
        super.onPause();

        // Get current values
        EditText editText = (EditText) findViewById(R.id.editTextIP);
        String host = editText.getText().toString();
        editText = (EditText) findViewById(R.id.editTextPort);
        String port = editText.getText().toString();
        editText = (EditText) findViewById(R.id.editTextData);
        String dataText = editText.getText().toString();
        editText = (EditText) findViewById(R.id.editTextHexData);
        String dataHex = editText.getText().toString();

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getPreferences(0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("host", host);
        editor.putString("port", port);
        editor.putString("dataText", dataText);
        editor.putString("dataHex", dataHex);
        editor.putBoolean("toggleChecked", ((ToggleButton) findViewById(R.id.toggleButton)).isChecked());

        // Commit the edits!
        editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_send:
                this.sendData(null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void sendData(View view) {
        Context context = getApplicationContext();

        EditText editText = (EditText) findViewById(R.id.editTextIP);
        String host = editText.getText().toString();
        if (!host.matches("\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b")) {
            CharSequence text = "Error: Invalid IP Address";
            Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        editText = (EditText) findViewById(R.id.editTextPort);
        String port = editText.getText().toString();
        if (!port.matches("^(6553[0-5]|655[0-2]\\d|65[0-4]\\d\\d|6[0-4]\\d{3}|[1-5]\\d{4}|[1-9]\\d{0,3}|0)$")) {
            CharSequence text = "Error: Invalid Port Number";
            Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        editText = (EditText) findViewById(R.id.editTextData);
        String dataText = editText.getText().toString();
        editText = (EditText) findViewById(R.id.editTextHexData);
        String dataHex = editText.getText().toString();
        if (dataText.length() < 1 && dataHex.length() < 2) {
            CharSequence text = "Error: Text/Hex required to send";
            Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        String uriString = "udp://" + host + ":" + port + "/";
        if (dataHex.length() >= 2) {
            uriString += Uri.encode("0x" + dataHex);
        } else {
            uriString += Uri.encode(dataText);
        }
        Uri uri = Uri.parse(uriString);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        intent.addCategory(Intent.CATEGORY_DEFAULT);

        startActivity(intent);
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
