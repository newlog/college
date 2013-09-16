package org.UchihaCorp.Estel.communication;

import java.io.File;

import org.UchihaCorp.Estel.MenuActivity;
import org.UchihaCorp.Estel.MusicActivity;
import org.UchihaCorp.Estel.common.Constants;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class DownloadThread extends Thread {
	
/*	public Connection con = null;
	public boolean songDownloaded = false;
	public String songName = null;
	public MusicActivity ma = new MusicActivity();
	public MenuActivity meac = new MenuActivity();
	public String myFilePath;
	public Context ctx;
*/	
	public DownloadThread(int iSong, Context context) {
//		if ( ma != null ) songName = ma.getSongTitle(iSong);
//		if ( meac != null ) con = meac.getConnection();
//		
//		String myPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MP3Music/";
//		String mp3FileName = songName + ".mp3";
//		myFilePath = myPath + mp3FileName;
//		
//		ctx = context;
	}
/*	
	private boolean isAlreadyDownloaded (String sSong) {
		
		File f = new File(myFilePath);
		if ( f != null && f.exists() ) return true;
		else return false;
	}
	
	public void run() {
		boolean bSuccess = false;
		if ( !this.isAlreadyDownloaded(songName) ) {
			
			if ( con != null ) {
				
				con.writeBytes(songName);
				bSuccess = con.receiveAndWriteSong(myFilePath);
				
			} else {
				Log.e("", Constants.EDWT01);
			}
			
			if ( !bSuccess ) Log.e("", Constants.EDWT02);
		}
	}
*/
}
