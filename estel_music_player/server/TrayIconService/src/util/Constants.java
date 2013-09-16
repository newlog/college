package util;

public class Constants {

	//			MAIN BUTTON TITLES
	public static final String exitButtonTitle =			"Exit";
	public static final String settingsButtonTitle =		"Settings";
	public static final String disconnectButtonTitle =		"Disconnect";
	public static final String connectButtonTitle =			"Connect";
	
	//			SETTINGS WINDOW STRINGS
	public static final String folderLabel =				"Choose the folder to share:";
	public static final String portLabel =					"Choose the port to listen:";
	public static final String folderButton =				"...";
	public static final String portTextField =				"1337";
	public static final String saveButton =					"Save!";
	
	//			ICON IMAGE
	public static final String iconImage = 					"C:\\Proyectos\\Eclipse\\Python\\TrayIconService\\img\\glider-16.png";
	public static final String settingsIconImage = 			"C:\\Proyectos\\Eclipse\\Python\\TrayIconService\\img\\VVendettaBlancoNegro.jpg";
	public static final String settingsMenuImage = 			"C:\\Proyectos\\Eclipse\\Python\\TrayIconService\\img\\MenuSettingsIcon.gif";
	
	//			ICON HOVER TITLE
	public static final String iconHoverTitle =				"Estel Streaming Music Server";
	
	//			WINDOW NUMBER
	public static final int	iExitWindow =					0;
	public static final int	iSettingsWindow =				1;
	public static final int	iDisconnectWindow =				2;
	public static final int	iConnectWindow =				3;
	//			WINDOW TITLES
	public static final String	sSettingsWindow =			"Settings";
	//			MESSAGEBOX
	public static final String sMBTitle = 					"Connection closed by client";
	public static final String sMBText = 					"You have to reconnect with the try icon connect button to listen new petitions.";
	
	//			WINDOW & COMPONENTS SIZES AND POSITIONS
	public static final int iWindowWidth =					300;
	public static final int iWindowHeight =					300;

	public static final int iButtonWidth =					70; 					
	public static final int iButtonHeight =					20;
	
	public static final int iTextFieldWidth =				120; 					
	public static final int iTextFieldHeight =				20;
	
	public static final int iPadding =						30;
	public static final int iLeftMargin =					30;
	
	public static final int iLabelHeight = 					20;
	public static final int iLabelWidth = 					190;
	
	public static final int iMessageBoxWidth =				480;
	public static final int iMessageBoxHeight = 			100;
	
	
	//			PROTOCOL
	public static final String initFiles = 					"INIT-PLAYLIST";		//Send initiate playlist control tag
	public static final String playlistControlTag =			"INIT-PLAYLIST-RECV";	//Receive initiate playlist control tag
																					//Send number of songs
	public static final String numberOfSongsControlTag =	"NUM-SONGS-RECV";		//Receive number of songs control tag
																					//Send songs
	//public static final String endFiles = 					"END-PLAYLIST";			//Send end playlist control tag
	public static final String endFilesControlTag = 		"END-PLAYLIST-RECV";	//Receive end playlist control tag
	public static final String songsSeparator =		 		"/";					//Song separator
	//--
	public static final String endCommunication =		 	"END-COMMUNICATION";	//Client disconnecting
	//--
	public static final String sendingSong = 				"SENDING-SONG";
	public static final String sendingSongControlTag = 		"SENDING-SONG-RECV";
	
	
	/***			ERRORS			***/
	//			INTERFACE ERRORS
	public static final String EINT01 = 					"Error loading tray icon.";
	public static final String EINT02 = 					"The specified window number does not exist. No window loaded on clic.";
	
	//			COMMUNICATION ERRORS 	
	public static final String ECOM01 = 					"The specified directory is incorrect.";
	public static final String ECOM02 = 					"The music path is null.";
	public static final String ECOM03 = 					"No client connected. The communication can not start.";
	
	public static final String ECOM04 =						"End playlist control tag not received correctly.";
	public static final String ECOM05 =						"Number of songs control tag not received correctly.";
	public static final String ECOM06 =						"Start playlist control tag not received correctly.";
	public static final String ECOM07 =						"The sleep gone wrong.";
	public static final String ECOM08 =						"The requested song is not in the choosen directory.";
	public static final String ECOM09 =						"Sending song tag not received correctly.";
	
	//			COMMUNICATION INFORMATION
	public static final String ICOM01 = 					"Closing communication thread.";
	
	//			CONNECTION ERRORS
	public static final String ECON01 = 					"The port to listen is null.";
	public static final String ECON02 = 					"The port to listen is incorrect.";
	public static final String ECON03 = 					"The connection could not be setup.";
	public static final String ECON04 = 					"Error reading string.";
	public static final String ECON05 = 					"Error writing string.";
	public static final String ECON06 = 					"Error reading int value.";
	public static final String ECON07 = 					"Error writing int value.";
	public static final String ECON08 = 					"Error closing connections.";
	public static final String ECON09 = 					"Error sending song.";
	public static final String ECON10 = 					"The file to send could not be opened.";
	public static final String ECON11 = 					"The file could not be read.";
	public static final String ECON12 = 					"The file stream could not be closed.";
	public static final String ECON13 = 					"The file to send was too big.";
	public static final String ECON14 = 					"Error writing long.";
	public static final String ECON15 =						"Connection closed because the end of the stream has been received.";
	public static final String ECON16 = 					"The sending song receive message was not received correctly. Song not send.";
}
