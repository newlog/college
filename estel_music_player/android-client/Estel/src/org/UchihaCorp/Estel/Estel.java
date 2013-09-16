package org.UchihaCorp.Estel;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabWidget;

import org.UchihaCorp.Estel.common.Constants;


public class Estel extends TabActivity {
	
//	public TabHost tabHost;
//	public TabHost.TabSpec spec;  					// Resusable TabSpec for each tab
	private boolean inited = false;
	
	public Estel() {
		
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	  super.onCreate(savedInstanceState);
    	    setContentView(R.layout.main);
    	    
    	    TabHost tabHost;
    	    Resources res = getResources(); 		// Resource object to get Drawables
    	    tabHost = getTabHost();			  		// The activity TabHost
    	    Intent intent;  						// Reusable Intent for each tab

    	    // Create an Intent to launch an Activity for the tab (to be reused)
    	    intent = new Intent().setClass(this, MenuActivity.class);
    	    TabHost.TabSpec spec;
    	    // Initialize a TabSpec for each tab and add it to the TabHost
    	    spec = tabHost.newTabSpec("menu").setIndicator(Constants.MENU_TAB_NAME,
    	                      res.getDrawable(R.drawable.menu_tab))
    	                  .setContent(intent);
    	    tabHost.addTab(spec);

    	    // Do the same for the other tabs
    	    intent = new Intent().setClass(this, MusicActivity.class);
    	    spec = tabHost.newTabSpec("music").setIndicator(Constants.MUSIC_TAB_NAME,
    	                      res.getDrawable(R.drawable.music_tab))
    	                  .setContent(intent);
    	    tabHost.addTab(spec);
    	    startActivity(intent);
    	    
    	    tabHost.setCurrentTab(0);
    	    
    	    inited = true;
    }
    

}
