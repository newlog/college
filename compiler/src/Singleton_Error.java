import java.util.*;
import java.io.*;
import taulasimbols.*;

public class Singleton_Error {

	//Atributo interno Error que será Singleton
	static private Errors instanceError=null;
	
	//Constructor privado, para que no se pueda instancias desde otras clases
	private Singleton_Error(){}
	
	//Metodo para recuperar la instancia
	public static Errors getInstance(String nomFitxer){
		
		//Miro si ya está instanciada, si no pido el espacio
		if(instanceError==null){
			instanceError=new Errors(nomFitxer);
		}
		return instanceError;
	}
	
	//Metodo para recuperar la instancia sin pasar cadena
	public static Errors getInstance(){
		
		//Miro si ya está instanciada, si no pido el espacio
		if(instanceError==null){
			System.out.println("ERROR! Fichero de errores no instanciado!");
		}
		return instanceError;
	}
}
