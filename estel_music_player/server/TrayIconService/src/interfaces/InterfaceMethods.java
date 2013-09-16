package interfaces;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import communication.CommunicationThread;


import util.Constants;

public class InterfaceMethods extends JPanel {

	//Settings window frame
	private static JFrame settingsFrame = null;
	private static JTextField tfFolder = null;
	private static JTextField tfPort = null;
	private static JFileChooser fc = null;
	
	public InterfaceMethods() {
		
	}
	
	public void createWindow(int iWindow, String sTitle) {
		
		switch (iWindow) {
		case 0:												//Exit window
			
			break;
		case 1:												//Settings window
			createSettingsWindow(sTitle);
			break;
			
		case 2:												//Disconnect window
			
			break;
			
		case 3:												//Connect window
			
			break;
		default:											//Error
			System.out.println(Constants.EINT02);
			break;
		
		}

	}
	
	public void createSettingsWindow(String sTitle) {
		
		
		//Create the settingsFrame.
		settingsFrame = new JFrame(sTitle);
		//The frame icon is setted 
		settingsFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(Constants.settingsIconImage));
		//Set layout to null in order to use setBounds. Yes, I don't want to mess with layouts...
		settingsFrame.setLayout(null);
		//Hide dialog at close
		settingsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//Set window size
		settingsFrame.setBounds(0, 0, Constants.iWindowWidth, Constants.iWindowHeight);
		
		//Create components and put them in the settingsFrame.
		JLabel lFolder = new JLabel(Constants.folderLabel);
		lFolder.setBounds(Constants.iLeftMargin, Constants.iPadding, Constants.iLabelWidth, Constants.iLabelHeight);
		tfFolder = new JTextField();			//TextBox is created and positioned
		tfFolder.setBounds(Constants.iLeftMargin, Constants.iPadding + Constants.iLabelHeight, Constants.iTextFieldWidth, Constants.iTextFieldHeight);
		JButton bExamine = new JButton(Constants.folderButton);			//Button is created and positioned
		bExamine.setBounds(Constants.iLeftMargin + Constants.iPadding + 20 + Constants.iTextFieldWidth , Constants.iPadding + Constants.iLabelHeight, Constants.iButtonWidth, Constants.iButtonHeight);
		
		fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		
		int nextYLocation = Constants.iPadding * 2 + Constants.iLabelHeight;
		
		JLabel lPort = new JLabel(Constants.portLabel);
		lPort.setBounds(Constants.iLeftMargin, nextYLocation + Constants.iPadding, Constants.iLabelWidth, Constants.iLabelHeight);
		tfPort = new JTextField(Constants.portTextField);			//TextBox is created and positioned
		tfPort.setBounds(Constants.iLeftMargin, nextYLocation + Constants.iPadding + Constants.iLabelHeight, Constants.iTextFieldWidth, Constants.iTextFieldHeight);
		
		nextYLocation +=  nextYLocation + Constants.iPadding + Constants.iLabelHeight;
		
		JButton bSave = new JButton(Constants.saveButton);			//Button is created and positioned
		bSave.setBounds((Constants.iWindowWidth/2) - (Constants.iButtonWidth/2) , nextYLocation + Constants.iPadding - 20, Constants.iButtonWidth, Constants.iButtonHeight);
		
		//Adding components to window
		settingsFrame.getContentPane().add(lFolder);
		settingsFrame.getContentPane().add(tfFolder);
		settingsFrame.getContentPane().add(bExamine);
		settingsFrame.getContentPane().add(lPort);
		settingsFrame.getContentPane().add(tfPort);
		settingsFrame.getContentPane().add(bSave);
		
		
		//The window is centered on the screen (because the parameter is null)
		settingsFrame.setLocationRelativeTo(null);
		//Show the settingsFrame
		settingsFrame.setVisible(true);
		settingsFrame.validate();
		
		//Set the save button functionality
		bSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//The Settings window is 'closed'
				settingsFrame.dispose();
			}
		});
		
		//Set the examine button functionality
		bExamine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int returnVal = fc.showOpenDialog(InterfaceMethods.this);

	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	                File file = fc.getSelectedFile();
	                //This is where a real application would open the file.
	                System.out.println("Opening: " + file.getAbsolutePath() + ".");
	                tfFolder.setText(file.getAbsolutePath());
	            } else {
	            	System.out.println("Open command cancelled by user.");
	            }

			}
		});
		
	}
	
	public String getMusicPath() {
		if ( tfFolder != null ) return tfFolder.getText();
		else return null;
	}
	
	public String getPortToListen() {
		if ( tfPort != null ) return tfPort.getText();
		else return null;
	}
	
	public void createDisconnectedByClientDialog() {
		
		JFrame frame = new JFrame(Constants.sMBTitle);
		JLabel text = new JLabel(Constants.sMBText);
		text.setBounds(10, 10, Constants.iMessageBoxWidth, 20);
		frame.setSize(Constants.iMessageBoxWidth, Constants.iMessageBoxHeight);
		frame.setVisible(true);
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().add(text);
		frame.setLocationRelativeTo(null);
		frame.validate();
		
	}
}
