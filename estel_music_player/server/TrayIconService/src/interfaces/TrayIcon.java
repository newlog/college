package interfaces;

import javax.swing.* ;

import communication.CommunicationThread;
import communication.Connection;


import java.awt.* ;
import java.awt.event.*;

import util.Constants;

public class TrayIcon extends JFrame{
	
	private static final long serialVersionUID = 1L;
	private Connection con;
	private CommunicationThread comm;

	public TrayIcon() {
		//Setting the image of the system tray application
		SystemTray tray = SystemTray.getSystemTray();
		Image img = Toolkit.getDefaultToolkit().getImage(Constants.iconImage);
		
		//A popup menu is created and the button titles are setted
		PopupMenu popup = new PopupMenu();
		MenuItem mItemExit = new MenuItem(Constants.exitButtonTitle);
		MenuItem mItemSettings = new MenuItem(Constants.settingsButtonTitle);
		MenuItem mItemDisconnect = new MenuItem(Constants.disconnectButtonTitle);
		MenuItem mItemConnect = new MenuItem(Constants.connectButtonTitle);
		
		
		//The actions of every item in the menu are specified
		mItemExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mItemSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InterfaceMethods im = new InterfaceMethods();
				im.createWindow(Constants.iSettingsWindow, Constants.sSettingsWindow);
			}
		});
		mItemDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ( con != null ) {
					con.writeBytes(Constants.endCommunication);
					con.closeAll();
				}
			}
		});
		mItemConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//The server is started. It waits for any connection. When a client connects, the communication starts in a new thread.
				con = new Connection();
				comm = new CommunicationThread(con);
				comm.start();
			}
		});
		
		//The items are added in the popup menu
		popup.add(mItemConnect);
		popup.add(mItemDisconnect);
		popup.add(mItemSettings);
		popup.addSeparator();
		popup.add(mItemExit);
		

		java.awt.TrayIcon trayIcon = new java.awt.TrayIcon(img, Constants.iconHoverTitle, popup);
		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			System.err.println(Constants.EINT01);
		}
		
		
		//--- atajo ---//
		
		InterfaceMethods im = new InterfaceMethods();
		im.createWindow(Constants.iSettingsWindow, Constants.sSettingsWindow);
		
		con = new Connection();
		comm = new CommunicationThread(con);
		comm.start();
		
	}
	
	public Connection getActualConnection() {
		return con;
	}

	public static void main(String[] args) {
		new TrayIcon();
	}
	
	
	
}
