package com.example.davidh.calculatorapplication;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class CalcPreferenceActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        addPreferencesFromResource(R.xml.preferences);
    }
}
