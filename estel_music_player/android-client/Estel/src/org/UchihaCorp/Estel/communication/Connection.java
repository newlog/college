package org.UchihaCorp.Estel.communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UTFDataFormatException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

import org.UchihaCorp.Estel.MenuActivity;
import org.UchihaCorp.Estel.SettingsActivity;
import org.UchihaCorp.Estel.common.Constants;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

public class Connection {
	
	private Socket socket;
	private DataInputStream dis = null;
	private DataOutputStream dos = null;
	private boolean isConnected = false;
	private String sPort = null;
	private String sIP = null;
	private boolean bRandom = false;
	
	public Connection() {
		this.Connect();
	}
	
	public void Connect(){

		if ( isConnected == false ) {
//			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//			
//			String sPort = prefs.getString("port_preference", "0");
//			String sIP = prefs.getString("ip_preference", "0.0.0.0");
			
			SettingsActivity sa = new SettingsActivity();
			sPort = sa.getPort();
			sIP = sa.getIP();
			bRandom = sa.getRandom();
			
//			String sPort = "1337";
//			String sIP = "192.168.1.46";
			
			if ( sPort == null || sPort.compareTo("0") == 0 ) 
				System.out.println(Constants.ECON01);
			else {
				if ( sIP == null || sIP.compareTo("0.0.0.0") == 0) {
					System.out.println(Constants.ECON09);
				} else {
					Integer oiPort = null;
					try {
						oiPort = new Integer(sPort);
						
						try {
							socket = new Socket(sIP, oiPort.intValue());
							dos = new DataOutputStream(socket.getOutputStream());
							dis = new DataInputStream(socket.getInputStream());
							
							isConnected = true;
							System.out.println("Connection established.");
							
						}catch(Exception e){
							Log.e("", Constants.ECON03 + ": " + e.getMessage());
						}
						
					} catch (NumberFormatException e) {
						Log.e("", Constants.ECON02 + ": " + e.getMessage());
					}
				}
			}
	}
	} 

	
	public String readBytes(){
		String cad = null;
		
		try{
			if ( dis != null ) {
				byte []cadenaDeBytes = new byte[512];
				int bytesRead = dis.read(cadenaDeBytes);
				byte [] fitedByteArray;
				if ( bytesRead > 0 ) {
					cad = new String(cadenaDeBytes, 0, bytesRead, "US-ASCII");
				} else if ( bytesRead == 0) {
					cad = new String("");
				} else if ( bytesRead == -1 ) {
					this.closeAll();
					System.out.println(Constants.ECON14);
				}
			} else {
				System.out.println("The DataInputStream is null. Probably because the connection has been closed or has been never created.");
			}
		} catch (IOException ioe) {
			System.out.println(Constants.ECON04 + ": -IO Error- " + ioe.getMessage());
			cad = new String("");
			this.closeAll();
			System.out.println(Constants.ECON14);
		}
		
		return cad;
	}
	

	
	public void writeBytes(String cad){
		try{
			byte[] cadenaDeBytes = cad.getBytes();
			dos.write(cadenaDeBytes);
//			dos.flush();
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(Constants.ECON05 + ": " + e.getMessage());
		}
	}
	
	public int readInt(){
		int num=0;
		try{
			num = dis.readInt();
		}catch(Exception e){
			Log.e("", Constants.ECON06 + ": " + e.getMessage());
		}
		return num;
	}
	
	public long readLong(){
		long num = 0;
		try{
			if ( dis != null ) {
				num = dis.readLong();
			} else {
				Log.e("", "The DataInputStream is null. Probably because the connection has been closed.");
			}
		}catch(Exception e){
			Log.e("", Constants.ECON11 + ": " + e.getMessage());
		}
		return num;
	}
		
	public void writeInt(int num){
		try{
			dos.writeInt(num);
			dos.flush();
		}catch(Exception e){
			Log.e("", Constants.ECON07 + ": " + e.getMessage());
		}
	}
	
	public void closeAll(){
		this.writeBytes(Constants.endCommunication);
		try {
			if (socket != null) socket.close();
			if (dos != null) dos.close();
			if (dis != null) dis.close();
		} catch ( Exception e ) {
			Log.e("", Constants.ECON08 + ": " + e.getMessage());
		} finally {
			isConnected = false;
		}
	
	}
	
	public void closeAllWithoutSendingMessage(){
		MenuActivity.ivGreenCircle.setVisibility(View.INVISIBLE);
    	MenuActivity.ivRedCircle.setVisibility(View.VISIBLE);
		try {
			if (socket != null) socket.close();
			if (dos != null) dos.close();
			if (dis != null) dis.close();
		} catch ( Exception e ) {
			Log.e("", Constants.ECON08 + ": " + e.getMessage());
		} finally {
			isConnected = false;
		}
	
	}
	
	public DataInputStream getDataInputStream() {
		return dis;
	}
	
	public boolean getIsConnected() {
		return isConnected;
	}
	
	public String getIP() {
		return sIP;
	}
	public String getPort() {
		return sPort;
	}
	public boolean getRandom() {
		return bRandom;
	}
		
}