package org.UchihaCorp.Estel;

import java.util.ArrayList;
import java.util.Arrays;

import org.UchihaCorp.Estel.common.Constants;
import org.UchihaCorp.Estel.communication.CommunicationThread;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;


// We create the music tab activity.
public class MusicActivity extends ListActivity {

	public static ListAdapter adapter;
	private static boolean inited = false;
	private static boolean goingFromUpdatePlaylist = false;
	
	public MusicActivity () {
		
	}
	
	
	/* TODO: Cuando se inicia la aplicaci—n, si antes de conectar se 
	 * visita la pesta–a de mœsica, una vez se conecta y se vuelve a 
	 * visitar la pesta–a de mœsica, el contenido de la lista no se 
	 * ha actualizado con las canciones. Sigue vac’o. Cuesti—n del 
	 * setListAdapter() y el onCreate, etc.
	 */
	public void onCreate(Bundle savedInstanceState) {			//This method will be executed when the activity is created
        super.onCreate(savedInstanceState);
        
        if ( !inited ) {
        	adapter = createAdapter();
        	setListAdapter(adapter);
        	inited = true;
        	finish();
        }
        
        if ( inited && goingFromUpdatePlaylist ) {
        	setListAdapter(adapter);
        	goingFromUpdatePlaylist = false;
        }
	}
	
    /**
     * Creates and returns a list adapter for the current list activity
     * @return
     */
    protected ListAdapter createAdapter()
    {	
    	
    	ArrayList<String> lst = new ArrayList<String>();
    	ListAdapter tmpAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lst);
    	return tmpAdapter;
    }
    
    
    @Override
    protected void onListItemClick (ListView l, View v, int position, long id) {
    	super.onListItemClick(l, v, position, id);
		// Get the item that was clicked
		Object o = this.getListAdapter().getItem(position);
		String keyword = o.toString();
//		
		Intent intent = new Intent(MusicActivity.this, SongsActivity.class);
		intent.putExtra("songTitle", keyword);
		intent.putExtra("songNumber", position);
	    startActivity(intent);
    }
    
    public static void updateListWithSongs(String[] saSongs) {
    	
    	if ( saSongs != null && saSongs.length != 0) {
    		for (int i = 0; i < saSongs.length; i++) {
    			if ( adapter != null ) 
    				((ArrayAdapter<String>)adapter).add(saSongs[i]);
			}
    	} 
    	goingFromUpdatePlaylist = true;
    	if ( adapter != null ) 
    		((BaseAdapter) adapter).notifyDataSetChanged();
    	
    }
    
    public String getSongTitle(int n) {
    	
    	Object o = null;
    	String sTitle = null;
    	
    	if ( adapter != null ) o = adapter.getItem(n);
		if ( o != null ) sTitle = o.toString();
		if ( sTitle != null ) return sTitle;
		else {
			System.out.println(Constants.EMUS01);
			return "";
		}
		
    }
}
