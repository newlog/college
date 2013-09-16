package org.UchihaCorp.Estel;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.UchihaCorp.Estel.common.Constants;
import org.UchihaCorp.Estel.communication.CommunicationThread;
import org.UchihaCorp.Estel.communication.Connection;
import org.UchihaCorp.Estel.communication.DownloadThread;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;


// We create the music tab activity.
public class SongsActivity extends Activity implements OnClickListener {
	
	private ImageButton ibPlay, ibPause, ibPrevious, ibNext;
	private int nextSong = -1, previousSong = -1, possibleSong = -1;
	private int actualSong = -1;
	private int[] songsPlayed ={-1,-1,-1,-1,-1};
	private int songsPlayedIndex = 0;
	private int lengthWhenPaused = 0;
	private MusicActivity ma = new MusicActivity();
	private static String myFilePath = null;
	private MediaPlayer mp = null;
	private MenuActivity meac = new MenuActivity();
	private int iSong = -1;
	private boolean isRandom = false;
	private int songNumber = -1;
	
	public SongsActivity() {
		mp = new MediaPlayer();
		isRandom = MenuActivity.con.getRandom();
	}


	public void onCreate(Bundle savedInstanceState) {			//This method will be executed when the activity is created
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song_dialog);
        
        Bundle pastData = null; 
        if ( this.getIntent() != null ) pastData = this.getIntent().getExtras();
        
        String songTitle = null;
        songNumber = -1;
        if ( pastData != null ) {
        	songTitle = pastData.getString("songTitle");
        	songNumber = pastData.getInt("songNumber");
        }
        
        if ( songTitle != null ) this.setTitle(songTitle);
        else this.setTitle("");
        
        ibPlay = (ImageButton) findViewById(R.id.playButton);
        if ( ibPlay != null ) {
        	ibPlay.setTag(new Integer(1));
        	ibPlay.setOnClickListener(this);
        }
//        ibPause = (ImageButton) findViewById(R.id.pauseButton);
//        if ( ibPause != null ) {
//        ibPause.setTag(new Integer(2));
//        	ibPause.setOnClickListener(this);
//        }
        ibPrevious = (ImageButton) findViewById(R.id.previousButton);
        if ( ibPrevious != null ) {
        	ibPrevious.setTag(new Integer(3));
        	ibPrevious.setOnClickListener(this);
        }
        ibNext = (ImageButton) findViewById(R.id.nextButton);
        if ( ibNext != null ) {
        	ibNext.setTag(new Integer(4));
        	ibNext.setOnClickListener(this);
        }
        
        
        this.playSong();

    }
	
	private void playSong() {
		// Sending the first song petition
        String songName = selectSong(songNumber);
		if (songName != null ) {
			if ( !this.isAlreadyDownloaded(songName) ) {  
				Connection con = null;
				if ( meac != null ) con = meac.getConnection();
				if ( con != null ) {	
					con.writeBytes(songName);							//SENDING THE SONG NAME if it isn't already downloaded
				} else Log.e("", Constants.EDWT01);
			}
			//TODO: Falta comprobar que la canci—n se haya descargado completamente
			if ( iSong != -1 && myFilePath != null ) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Toast toast2 = Toast.makeText(this, "Donwloading song...", 3000);
				toast2.show();
				this.playSongFile(iSong, myFilePath, songName);
			}
		}
	}
	
	public String selectSong(int privateSongNumber) {
		// -- We can come here when a list item is tap or when the previous or next button are pressed -- //
		String songName = null;
		if ( privateSongNumber != -1 && privateSongNumber != -10) {		//Here we process the song that the user has clicked. 
			//1.- Refresh all state data
			if (privateSongNumber != -1) iSong = privateSongNumber;
			else iSong = 0;
			
		} else if ( privateSongNumber == -10 ) {	//Previous button pressed!
			int iSongTemp = iSong;
			if ( songsPlayedIndex > 0 ) iSong = songsPlayed[songsPlayedIndex - 2];
			else iSong = songsPlayed[3];
			if ( iSong == -1 ) iSong = iSongTemp;
		} else {
			if ( isRandom ) {
				//1.- Get the possible song
				iSong = getPossibleSong();
				//2.- We search for it in the songs array. If it is found in the array, we try for 3 other songs
				int x = 0;
				boolean isIn = false;
				do {
					isIn = false;
					for (int i = 0; i < songsPlayed.length; i++) {
						if ( songsPlayed[i] == iSong ) {
							isIn = true;
							iSong = getPossibleSong();
							break;
						}
					}
					x++;
				} while( isIn && x < 3 );

			} else { //Not random
				int numberOfSongs = this.getNumberOfSongs();
				if ( actualSong < numberOfSongs - 1 ) {
					iSong++;
				} else {
					iSong = 0;
				}
			}
			
		}
		if ( privateSongNumber != -10 ) { // Previous button NOT pressed
			//3.- Refresh all state data
			songsPlayed[songsPlayedIndex] = iSong;
//			this.setNextSong(iSong);
    		//7.- Refresh the actual song and play it
    		if ( songsPlayedIndex > 0) previousSong = songsPlayed[songsPlayedIndex - 1];
    		else previousSong = songsPlayed[0];
    		actualSong = iSong;
    		if ( songsPlayedIndex < 4 ) songsPlayedIndex++;
    		else songsPlayedIndex = 0; 
		} else {
			if ( songsPlayedIndex > 0 ) songsPlayedIndex--;
    		else songsPlayedIndex = 0;
		}
		
		// Setting up myFilePath variable
		String myPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MP3Music/";
		String mp3FileName = ma.getSongTitle(iSong) + ".mp3";
		myFilePath = myPath + mp3FileName;
		
		//Getting song name
		if ( ma != null ) songName = ma.getSongTitle(iSong);
		if ( songName == null ) Log.e("", Constants.EDWT03);
		songNumber = -1;
		return songName;
	}
	
	private boolean isAlreadyDownloaded (String sSong) {
		if ( myFilePath != null ) {
			File f = new File(myFilePath);
			if ( f != null && f.exists() ) return true;
			else return false;
		} else {
			return false;
		}
	}
	
	public boolean isSongFinished() {
		if ( mp != null ) return mp.isPlaying();
		else return false;		//This is not correct. If mp is null, something went really bad
		
	}
	
	@Override
	public void onClick(View v) {
		
		Integer tag = (Integer) v.getTag();
		
		switch (tag.intValue()) {
		case 1:	//play
			Log.e("", "Play pressed");
			if ( mp != null && !mp.isPlaying() ) {
				//This means that the song is not playing and the play button is pressed
				mp.start();
				// When the song plays, the play button changes to pause button
	    		ibPlay.setImageResource(R.drawable.pause);
			} else if ( mp != null && mp.isPlaying() ) {
				//This means that the song is playing and the pause button is pressed
				mp.pause();
				// When the song pauses, the pause button changes to play button
	    		ibPlay.setImageResource(R.drawable.play);
			}
			break;
		case 2:	//pause
			Log.e("", "Pause pressed");
			break;
			
		case 3:	//previous
			Log.e("", "Previous pressed");
			if ( mp != null && mp.isPlaying() ) {
				int length = mp.getCurrentPosition();
				if ( length <= 7 * 1000 ) { 	//If have only past five seconds, the previous song is played
					songNumber = -10;
					playSong();
					
				} else { 						//The song is restarted
					mp.seekTo(0);
				}
			}
			break;
			
		case 4:	//next
			Log.e("", "Next pressed");
			playSong();
			break;
			
		default:
				
		}
		
	}
	
	
	private int getNumberOfSongs() {
		return CommunicationThread.getNumberOfSongs();
	}
	
	private int getPossibleSong() {
//		return possibleSong;
		int n = getNumberOfSongs();
		if ( n != -1) {
			Random r = new Random();
			return r.nextInt(n);
		} else {
			return 1;
		}
	}
	
	private int getNextSong() {
		return nextSong;
	}
	
	private void setNextSong (int i) {
		nextSong = i;
	}
	
	private int getPreviousSong() {
		return previousSong;
	}
	
	private void setPreviousSong(int n) {
		previousSong = n;
	}
	
	private void playSongFile(int iSong, String myFilePath, String privSongName) {
    	if ( mp != null ) {
    		try {
    			//TODO: Falta comprobar si la canci—n existe (si se ha creado correctamente) y si la targeta sd est‡ disponible
    			if ( mp.isPlaying() ) mp.reset();
	        	mp.setDataSource(myFilePath);
	    		mp.prepare();
	    		mp.start();
	    		if ( privSongName != null ) this.setTitle(privSongName);
	    		// When the song starts playing, the play button changes to pause button
	    		ibPlay.setImageResource(R.drawable.pause);
    		} catch ( IOException ex ) {
            	Log.e("", Constants.EMUS02 + ": " + ex.getMessage());
    		}
    	}
	}
	
	public static String getMyFilePath() {
		return myFilePath;
	}
	
}