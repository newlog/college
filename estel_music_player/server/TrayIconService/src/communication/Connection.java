package communication;

import interfaces.InterfaceMethods;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UTFDataFormatException;
import java.net.ServerSocket;
import java.net.Socket;
import util.Constants;

public class Connection {
	
	private Socket socket = null;
	private ServerSocket ss = null;
	private DataInputStream dis = null;
	private DataOutputStream dos = null;
	private boolean isConnected = false;
	
	public Connection() {
		this.waitConnection();
	}
	
	public void waitConnection() {
		
		InterfaceMethods im = new InterfaceMethods();
		String sPort = im.getPortToListen();
		
		if ( sPort == null ) 
			System.out.println(Constants.ECON01);
		else {
			Integer oiPort = null;
			try {
				//oiPort = new Integer(sPort);
				oiPort = new Integer(1337);
				try {
					ss = new ServerSocket(oiPort.intValue());
					System.out.println("Waiting connection...");
					socket = ss.accept();
					socket.setSoTimeout(0);				//Infinite timeout
					dis = new DataInputStream(socket.getInputStream());
					dos = new DataOutputStream(socket.getOutputStream());
					isConnected = true;
					System.out.println("Connection established.");
					
				}catch(Exception e){
					System.out.println(Constants.ECON03 + ": " + e.getMessage());
				}
				
			} catch (NumberFormatException e) {
				System.out.println(Constants.ECON02 + ": " + e.getMessage());
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
					this.waitConnection();
					System.out.println(Constants.ECON15);
				}
			} else {
				System.out.println("The DataInputStream is null. Probably because the connection has been closed or has been never created.");
			}
		} catch (IOException ioe) {
			System.out.println(Constants.ECON04 + ": -IO Error- " + ioe.getMessage());
			cad = new String("");
			this.closeAll();
			System.out.println(Constants.ECON15);
		}
		
		return cad;
	}
		
//	private void buildFitedByteArray( byte[] source, byte[] dest) {
//		for (int i = 0; i < dest.length; i++) {
//			dest[i] = source[i]; 
//		}
//	}
	
	public void writeBytes(String cad){
		try{
			byte[] cadenaDeBytes = cad.getBytes();
			dos.write(cadenaDeBytes);
			//dos.flush();
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(Constants.ECON05 + ": " + e.getMessage());
		}
	}
	
	public void sendSong(String file){
		try {
			System.out.println("Sending song: " + file);
			File myFile = new File (file);
			long length = myFile.length();
			dos.writeLong(length);
			
			System.out.println("Song length send: "+length);

			byte [] mybytearray  = new byte [(int)myFile.length()];
			mybytearray = this.fileToByteArray(myFile);
			if ( mybytearray != null ) {
				dos.write(mybytearray, 0, mybytearray.length);
				dos.flush();
				System.out.println("Song send.");
			} else {
				System.out.println("The song could not be send.");
			}
		} catch (Exception e) {
			System.out.println(Constants.ECON09 + ": " + e.getMessage());
		}
	}
	
	public final byte[] longToBytes(long v) {
	    byte[] writeBuffer = new byte[ 8 ];

	    writeBuffer[0] = (byte)(v >>> 56);
	    writeBuffer[1] = (byte)(v >>> 48);
	    writeBuffer[2] = (byte)(v >>> 40);
	    writeBuffer[3] = (byte)(v >>> 32);
	    writeBuffer[4] = (byte)(v >>> 24);
	    writeBuffer[5] = (byte)(v >>> 16);
	    writeBuffer[6] = (byte)(v >>>  8);
	    writeBuffer[7] = (byte)(v >>>  0);

	    return writeBuffer;
	}
	
	public int readInt(){
		int num=0;
		try{
			num = dis.readInt();
		}catch(Exception e){
			System.out.println(Constants.ECON06 + ": " + e.getMessage());
		}
		return num;
	}
		
	public void writeInt(int num){
		try{
			dos.writeInt(num);
			dos.flush();
		}catch(Exception e){
			System.out.println(Constants.ECON07 + ": " + e.getMessage());
		}
	}
	
	public void writeLong(long num){
		try{
			dos.writeLong(num);
			dos.flush();
		}catch(Exception e){
			System.out.println(Constants.ECON14 + ": " + e.getMessage());
		}
	}
	
	public void closeAll(){
		this.writeBytes(Constants.endCommunication);
		try{
			if ( dis != null ) dis.close();
			if ( dos != null ) dos.close();
			if ( socket != null ) socket.close();
			if ( ss != null ) ss.close();
		} catch(Exception e) {
			System.out.println(Constants.ECON08 + ": " + e.getMessage());
		} finally {
			isConnected = false;
		}
	}
	
	public void closeAllWithoutSendingMessage(){
		try{
			if ( dis != null ) dis.close();
			if ( dos != null ) dos.close();
			if ( socket != null ) socket.close();
			if ( ss != null ) ss.close();
		} catch(Exception e) {
			System.out.println(Constants.ECON08 + ": " + e.getMessage());
		} finally {
			isConnected = false;
		}
	}
	
	
	public boolean getIsConnected() {
		return isConnected;
	}
	
	
	private byte[] fileToByteArray(File file) {
		
	    if ( file.length() < Long.MAX_VALUE ) {
		    byte []buffer = new byte[(int) file.length()];
		    InputStream ios = null;
		    try {
		        try {
					ios = new FileInputStream(file);
				} catch (FileNotFoundException e1) {
					System.out.println(Constants.ECON10 + ": " + e1.getMessage());
				}
		        try {
					ios.read(buffer);
				} catch (IOException e2) {
					System.out.println(Constants.ECON11 + ": " + e2.getMessage());
				}        
		    } finally { 
		        try {
		             if ( ios != null ) 
		                  ios.close();
		        } catch ( IOException e3) {
		        	System.out.println(Constants.ECON12 + ": " + e3.getMessage());
		        }
		    }
		    return buffer;
	    } else {
	    	System.out.println(Constants.ECON13 + ": File size = " + file.length() + " bytes.");
	    	return null;
	    }
	    
	    
	}
	
		
}
