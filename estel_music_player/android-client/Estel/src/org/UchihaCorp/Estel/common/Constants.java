package org.UchihaCorp.Estel.common;

public class Constants {
	
	// TAB NAMES
	public static final String MENU_TAB_NAME = 				"Estel Music Streaming";
	public static final String MUSIC_TAB_NAME = 			"Shared Music";
	
	//	ALERT STRINGS
	public static final String ALERT_TITLE = 				"Connection closed by the server";
	public static final String ALERT_BODY =					"The server disconnected.";
	
	// POSITIONS
	public static final float GAME_BUTTON_HEIGHT = 			100;
	public static final float GAME_BUTTON_WIDTH	=			105;

	public static final float NORMAL_BUTTON_HEIGHT = 		45;
	public static final float NORMAL_BUTTON_WIDTH =			150;
	public static final float NORMAL_TEXTFIELD_HEIGHT =		23;
	public static final float NORMAL_TEXTFIELD_WIDTH =		135;
	public static final float NORMAL_LABEL_HEIGHT =			45;
	public static final float NORMAL_LABEL_WIDTH =			180;
	public static final float NORMAL_SWITCH_HEIGHT =		80;
	public static final float NORMAL_SWITCH_WIDTH =			120;

	public static final float SCREEN_WIDTH =				320;
	public static final float SCREEN_HEIGTH =				480;

	public static final float TOP_OFFSET =					120;
	public static final float CENTER_POS_X =				(SCREEN_WIDTH/2) - (NORMAL_BUTTON_WIDTH/2);
	public static final float CENTER_POS_Y =				60;
	
	
	
	//			PROTOCOL
	public static final String initFiles = 					"INIT-PLAYLIST";		//Receive initiate playlist control tag
	public static final String playlistControlTag =			"INIT-PLAYLIST-RECV";	//Send initiate playlist control tag
																					//Receive number of songs
	public static final String numberOfSongsControlTag =	"NUM-SONGS-RECV";		//Send number of songs control tag
																					//Receive song names
	//public static final String endFiles = 					"END-PLAYLIST";			//Receive end playlist control tag
	public static final String endFilesControlTag = 		"END-PLAYLIST-RECV";	//Send end playlist control tag
	public static final String songSeparator = 				"/";					//Song separator
	//--
	public static final String endCommunication = 			"END-COMMUNICATION";
	//--
	public static final String sendingSong = 				"SENDING-SONG";
	public static final String sendingSongControlTag = 		"SENDING-SONG-RECV";
	
	
	
	//			COMMUNICATION ERRORS 	
	public static final String ECOM01 = 					"The specified directory is incorrect.";
	public static final String ECOM02 = 					"The music path is null.";
	public static final String ECOM03 = 					"No client connected. The communication can not start.";
	
//	public static final String ECOM04 =						"End playlist control tag not received correctly";
	public static final String ECOM05 =						"Number of songs not received correctly.";
	public static final String ECOM06 =						"Start playlist control tag not received correctly.";
	public static final String ECOM07 =						"Something went wrong with the CommunicationThread sleep.";
	
	//			COMMUNICATION INFORMATION
	public static final String ICOM01 = 					"Closing communication thread. Ending connections.";
	
	//			CONNECTION ERRORS
	public static final String ECON01 = 					"The port to listen is null or is not setted.";
	public static final String ECON02 = 					"The port to listen is incorrect.";
	public static final String ECON03 = 					"The connection could not be setup.";
	public static final String ECON04 = 					"Error reading string.";
	public static final String ECON05 = 					"Error writing string.";
	public static final String ECON06 = 					"Error reading int value.";
	public static final String ECON07 = 					"Error writing int value.";
	public static final String ECON08 = 					"Error closing connections.";
	public static final String ECON09 = 					"The IP is incorrect or is not setted.";
	public static final String ECON10 = 					"Error receiving the song.";
	public static final String ECON11 = 					"Error reading long.";
	public static final String ECON12 = 					"The requested song is not in the server.";
	public static final String ECON13 = 					"The received song length is incorrect.";
	public static final String ECON14 =						"Connection closed because the end of the stream has been received.";
	
	//			MUSICACTIVITY ERRORS
	public static final String EMUS01 = 					"The song title could not be retrieved.";
	public static final String EMUS02 = 					"The selected song could not be played.";
	
	//			DOWNLOADTHREAD ERRORS
	public static final String EDWT01 = 					"The connection object is null. The song could not be downloaded.";
	public static final String EDWT02 = 					"Something went wrong when downloading the song.";
	public static final String EDWT03 = 					"The song name could not be retrieved.";
	
}
