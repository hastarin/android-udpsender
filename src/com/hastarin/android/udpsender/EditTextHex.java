package com.hastarin.android.udpsender;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

public class EditTextHex extends EditText {

	public EditTextHex(Context context) {
		super(context);
		initialize();
	}

	public EditTextHex(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	public EditTextHex(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize();
	}
	
	private void initialize() {
		setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
		setFilters(new InputFilter[] { new InputFilter() {
			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					android.text.Spanned dest, int dstart, int dend) {
				if (end > start) {
					String destTxt = dest.toString();
					String resultingTxt = destTxt.substring(0, dstart)
							+ source.subSequence(start, end)
							+ destTxt.substring(dend);
					if (!resultingTxt
							.matches("[a-fA-F0-9]+")) {
						return "";
					}
				}
				return null;
			}
		} });

		addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// Nothing happens here
			}
		});
	}
}
