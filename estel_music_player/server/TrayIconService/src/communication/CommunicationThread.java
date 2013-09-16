package communication;

import interfaces.InterfaceMethods;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Iterator;

import util.Constants;


public class CommunicationThread extends Thread {

	private static boolean bConnected = false;
	private ArrayList<String> alFileNames = null;
	private String sPathWithoutFiles = null;
	private Connection con = null;
	
	public CommunicationThread(Connection c) {
		
		InterfaceMethods im = new InterfaceMethods();
		sPathWithoutFiles = im.getMusicPath();
		alFileNames = getFileNames();
		
		if ( sPathWithoutFiles == null || alFileNames == null ) {
			bConnected = false;
		}
		
//		//It shows the file in the specified path		
//		for (int i = 0; i < alFileNames.size(); i++) {
//			System.out.println(sPathWithoutFiles + alFileNames.get(i));
//		}
		
		con = c;
	}
	
	private ArrayList<String> getFileNames() {
		ArrayList<String> al = new ArrayList<String>();
		File dir = null;
		
		//Only the .mp3 files will be listed (It is only filtered by the name -not sufficiently secure)
//		FilenameFilter filter = new FilenameFilter() {
//		    public boolean accept(File dir, String name) {
//		        return !name.endsWith(".mp3");
//		    }
//		};
		//The path is setted
		if (sPathWithoutFiles != null) 
			dir = new File(sPathWithoutFiles);
		else {
			System.out.println(Constants.ECOM02);
			return null;
		}
		//The MP3 files in the path are listed
//		String [] children = dir.list(filter); 	// 	<-- When filter enabled
		String [] children = dir.list();	//  <-- When filter not enabled
		if (children == null) {
		    System.out.println(Constants.ECOM01);
		} else {
		    for (int i=0; i<children.length; i++) {
		        // Insert the filename
		        al.add(children[i]);
		    }
		}

		return al;
	}
	
	public void run() {
		
		if ( con != null && con.getIsConnected() ) {
			int numberOfFiles = alFileNames.size();
			con.writeBytes(Constants.initFiles);
			if ( (con.readBytes()).compareToIgnoreCase(Constants.playlistControlTag) == 0 ) {
			
				con.writeInt(numberOfFiles);
				if ( (con.readBytes()).compareToIgnoreCase(Constants.numberOfSongsControlTag) == 0 ) {
					//Here we have to send song names
					String sAllSongs = null;
					int x = 0;
					for (Iterator iterator = alFileNames.iterator(); iterator.hasNext(); x++) {
						String sSong = (String) iterator.next();
						if ( sSong.endsWith(".mp3") ) {
							if ( x == 0) sAllSongs =  sSong.substring(0, sSong.length() - 4 ) + Constants.songsSeparator;
							else sAllSongs = sAllSongs + sSong.substring(0, sSong.length() - 4 ) + Constants.songsSeparator;
						} else {
							if ( x == 0) sAllSongs =  sSong + Constants.songsSeparator;
							else sAllSongs = sAllSongs + sSong + Constants.songsSeparator;	
						}
					}
					if ( sAllSongs == null ) sAllSongs = "";
					else if ( sAllSongs.endsWith(Constants.songsSeparator) ) sAllSongs = sAllSongs.substring(0, sAllSongs.length() - 1);
					
					con.writeInt(sAllSongs.length());
					if ( (con.readBytes()).compareToIgnoreCase("SONG-TITLES-LENGTH-RECV") == 0 ) {
					
						con.writeBytes(sAllSongs);

						if ( (con.readBytes()).compareToIgnoreCase(Constants.endFilesControlTag) == 0 ) {
							//The songs were already send.
							//Now we have to wait to songs petitions.
							while ( con != null && con.getIsConnected() ) {
								try {
									String songName = con.readBytes();
									if ( songName != null ) {
										if ( songName.compareToIgnoreCase(Constants.endCommunication) == 0 ) {
											System.out.println("Communication end string received.");
	//										InterfaceMethods im = new InterfaceMethods();
	//										im.createDisconnectedByClientDialog();
											// Wait for new connections
											con.closeAllWithoutSendingMessage();
											con.waitConnection();
											break;
											
										} else {
											System.out.println("Song name received.");
											boolean isIn = false;
											for (Iterator iterator = alFileNames.iterator(); iterator.hasNext();) {
												String song = (String) iterator.next();
												if ( song.compareToIgnoreCase(songName + ".mp3") == 0) {
													isIn = true;
													break;
												}
											}
											if ( isIn ) {
												System.out.println("The song file is in the music directory.");
												con.writeBytes(Constants.sendingSong);
												String data = con.readBytes();
												if ( data.compareToIgnoreCase(Constants.sendingSongControlTag) == 0 ) {
													con.sendSong(sPathWithoutFiles + "/" + songName + ".mp3");
												} else System.out.println(Constants.ECOM09);
											} else {
												long lError = -1;
												con.writeLong(lError);
												System.out.println(Constants.ECOM08);
											}
										}
									}
								} catch (Exception e) {
									System.out.println(Constants.ECOM07);
								}
							}
		
						} else {
							System.out.println(Constants.ECOM04);
						}
					} else {
						System.out.println("Total song titles length received incorrectly.");
					}
				} else {
					System.out.println(Constants.ECOM05);
				}
			} else {
				System.out.println(Constants.ECOM06);
			}
			
		} else {
			System.out.println(Constants.ECOM03);
		}
		
		
		System.out.println(Constants.ICOM01);
		
	}
	
	
	
}
