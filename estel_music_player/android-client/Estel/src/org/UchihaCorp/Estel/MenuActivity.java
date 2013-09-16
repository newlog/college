package org.UchihaCorp.Estel;

import org.UchihaCorp.Estel.common.Constants;
import org.UchihaCorp.Estel.communication.CommunicationThread;
import org.UchihaCorp.Estel.communication.Connection;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

//We create the menu tab activity.
public class MenuActivity extends Activity {
	
	public static ImageView ivGreenCircle, ivRedCircle;
	public CommunicationThread comm;
	public static Connection con;
	private static boolean connectionAlive = false;
	public static Context instance;
	
	public MenuActivity() {
		instance = this;
	}
	
	
	public void onCreate(Bundle savedInstanceState) {			//This method will be executed when the activity is created
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menutabcontent);				//We add the TextView to the Activity.

        ivGreenCircle = (ImageView) findViewById(R.id.green_circle);
        ivRedCircle = (ImageView) findViewById(R.id.red_circle);
        
        
        /******		MAIN VIEW BUTTONS		*******/
        
        final Button connectButton = (Button) findViewById(R.id.ConnectButton);
        connectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	
            	//Connecting to server. When it connects, the communication starts in a new thread.
				con = new Connection();
				connectionAlive = true;
				comm = new CommunicationThread(con);
				comm.start();
				            	
				if ( con.getIsConnected() ) {
            		ivRedCircle.setVisibility(View.INVISIBLE);
            		ivGreenCircle.setVisibility(View.VISIBLE);
            	}
            }
        });
        
        final Button disconnectButton = (Button) findViewById(R.id.DisconnectButton);
        disconnectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	if ( con != null ) {
					Log.e("", Constants.ICOM01);
					connectionAlive = false;
					con.closeAll();
				}
            	ivGreenCircle.setVisibility(View.INVISIBLE);
            	ivRedCircle.setVisibility(View.VISIBLE);
            }
        });
        
        final Button exitButton = (Button) findViewById(R.id.ExitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	connectionAlive = false;
            	if ( con != null) con.closeAll();
                finish();
                System.exit(0);
            }
        });
        
    }
	
	/*
	 * This method creates the options menu for the options hardware button
	 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.layout.settingsmenu, menu);
        return true;
    }

    
    /*******  CONTEXTUAL MENU BUTTONS (WHEN OPTION HARDWARE BUTTON IS PRESSED)		*******/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.settings_button:
        	Intent intent = new Intent().setClass(this.getApplicationContext(), SettingsActivity.class);
    	    startActivityForResult(intent, 0);
            return true;
        case R.id.howto_button:
           
            return true;
        case R.id.about_button:
           
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    public CommunicationThread getCommunicationThread() {
    	return comm;
    }
    
    public Connection getConnection () {
    	return con;
    }
    
    public static boolean getConnectionAlive() {
    	return connectionAlive;
    }
    
    public static void setConnectionAlive(boolean value) {
    	connectionAlive = value;
    }

    public static void showAlert() {
    	if ( instance != null )
    		new AlertDialog.Builder(instance).setTitle(Constants.ALERT_TITLE).setMessage(Constants.ALERT_BODY).setNeutralButton("Close", null).show();
    }
    public static Context getMenuActivityInstance () {
    	return instance;
    }
}

