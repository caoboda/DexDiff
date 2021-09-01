package com.fcxin.voice;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.view.Window;

/**
 * 听写设置界面
 */
public class Settings extends PreferenceActivity implements OnPreferenceChangeListener {

	public static final String PREFER_NAME = "com.iflytek.setting";
	private EditTextPreference mVadbosPreference;
	private EditTextPreference mVadeosPreference;

	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		getPreferenceManager().setSharedPreferencesName(PREFER_NAME);
		addPreferencesFromResource(R.layout.iat_setting);

		mVadbosPreference = (EditTextPreference)findPreference("iat_vadbos_preference");
		//mVadbosPreference.getEditText().addTextChangedListener(new SettingTextWatcher(Settings.this,mVadbosPreference,0,10000));

		mVadeosPreference = (EditTextPreference)findPreference("iat_vadeos_preference");
		//	mVadeosPreference.getEditText().addTextChangedListener(new SettingTextWatcher(Settings.this,mVadeosPreference,0,10000));
	}
	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		return true;
	}
}
