package org.UchihaCorp.Estel;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

// We create the music tab activity.
public class SettingsActivity extends PreferenceActivity {

	SharedPreferences prefs = null;
		
	public SettingsActivity() {
		Context ctx = MenuActivity.getMenuActivityInstance();
		if ( ctx != null )
			prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
	}
	
	public void onCreate(Bundle savedInstanceState) {			//This method will be executed when the activity is created
        super.onCreate(savedInstanceState);
        
        addPreferencesFromResource(R.layout.settings);
        
    }
	
	public String getIP() {		
		if ( prefs != null )
			return prefs.getString("ip_preference", "0.0.0.0");
		else return "0.0.0.0";
	}
	
	public String getPort() {
		if ( prefs != null )
			return prefs.getString("port_preference", "0");
		else return "0";
	}
	
	public boolean getRandom() {
		if ( prefs != null )
			return prefs.getBoolean("random_music", false);
		else return false;
	}
	
}
