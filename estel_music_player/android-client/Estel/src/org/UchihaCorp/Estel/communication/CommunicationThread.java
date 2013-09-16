package org.UchihaCorp.Estel.communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;

import org.UchihaCorp.Estel.MenuActivity;
import org.UchihaCorp.Estel.MusicActivity;
import org.UchihaCorp.Estel.SongsActivity;
import org.UchihaCorp.Estel.common.Constants;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;


public class CommunicationThread extends Thread {

	private static boolean bConnected = false;
	private String sPathToSaveMP3 = null;
	private Connection con = null;
	private static int numberOfSongs = -1;
	private String[] saAllSongs = null;
//	private InterfaceFunctions infunc;
//	private MenuActivity ma = new MenuActivity();
	
	public CommunicationThread() {
		
	}
	
	public CommunicationThread(Connection c) {
		
		sPathToSaveMP3 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MP3Music/";
		con = c;
	}
	
	//In this thread only the communication with the server is done. Not the songs download.
	public void run() {
		if ( con != null && con.getIsConnected() ) {
			
			String data = con.readBytes();
			if ( data != null && (data).compareToIgnoreCase(Constants.initFiles) == 0 ) {
				con.writeBytes(Constants.playlistControlTag);
				if ( (numberOfSongs = con.readInt()) != -1 ) {
			
					con.writeBytes(Constants.numberOfSongsControlTag);
					//Here we have to receive to song names
					String sAllSongs = con.readBytes();
					
					saAllSongs = sAllSongs.split(Constants.songSeparator);

					con.writeBytes(Constants.endFilesControlTag);
						
					//The songs were already received.
					//Printing songs:
					Log.i("", sAllSongs);
					//Now we have to show them in the table.
					//Updating UI List with song names
					MusicActivity.updateListWithSongs(saAllSongs);

					while ( MenuActivity.getConnectionAlive() ) {

							String cad = con.readBytes();
							if ( cad != null && cad.compareToIgnoreCase(Constants.endCommunication) == 0 ) {
//								Context ctxt = MenuActivity.getMenuActivityInstance();
//								new AlertDialog.Builder(ctxt).setTitle("Argh").setMessage("Watch out!").setNeutralButton("Close", null).show();
////								ma.getMenuActivityInstance().showAlert();
								//No se como cojones mostrar un AlertDialog desde aqu’
//								MenuActivity.showAlert(); --> ESTE TENDRIA QUE FUNCIONAR
								MenuActivity.setConnectionAlive(false);
								con.closeAllWithoutSendingMessage();
							} else if ( cad != null && cad.compareToIgnoreCase(Constants.sendingSong) == 0 ) {
								con.writeBytes(Constants.sendingSongControlTag);
								
								String myFilePath = SongsActivity.getMyFilePath();
								this.receiveAndWriteSong(myFilePath);
							}
					}
						
				} else {
					Log.e("", Constants.ECOM05);
				}
			} else {
				Log.e("", Constants.ECOM06);
			}
			
		} else {
			Log.e("", Constants.ECOM03);
		}
		
		//Exiting the thread.
		//The connection is not closed because the song downloading is done from another thread.
	}

	public boolean receiveAndWriteSong(String file){
		
		try {
			DataInputStream dis = con.getDataInputStream();
			
			long fileLength = dis.readLong();
			
		    int bytesRead = 0;
		    FileOutputStream fos = new FileOutputStream(file);
		    DataOutputStream dosToFile = new DataOutputStream(fos);
		    long totalBytesWritten = 0;
		    byte[] buffer = new byte[5024];		// 8Kb 
		    do {
		    	bytesRead = dis.read(buffer, 0, 5024);
		    	if ( bytesRead > 0) {
			    	dosToFile.write(buffer, 0, bytesRead);
					dosToFile.flush();
					totalBytesWritten += bytesRead;				//Se acumula el numero de bytes escritos en el fichero
		    	} else if ( bytesRead == 0 ) {
		    		Log.e("","Zero bytes readed when downloading song.");
		    	} else if ( bytesRead == -1 ) {
		    		Log.e("","Read returned -1 when downloading song.");
		    	}
		    	if ( totalBytesWritten == fileLength ) break;
		    } while ( bytesRead > -1 );
		   	    
		    //TODO: Falta comprobar que los bytes recibidos son igual al tama–o del fichero y si no es as’, borrarlo y notificarlo.
		    
		    Log.e("", (totalBytesWritten/1024)/1024 + " Mb, " + totalBytesWritten/1024 +" Kb, " + totalBytesWritten + " bytes.");
		} catch (Exception e) {
			Log.e("", Constants.ECON10 + ": " + e.getMessage());
			return false;
		}
		
		return true;
	}
	
	public String[] getSaAllSongs() {
		return saAllSongs;
	}
	
	public static int getNumberOfSongs() {
		return numberOfSongs;
	}

}
