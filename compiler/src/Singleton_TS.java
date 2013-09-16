import java.util.*;
import java.io.*;
import taulasimbols.*;

class Singleton_TS {

	//Atributo interono instanceTS que será Singleton
	static private TaulaSimbols instanceTS = null;
	
	//Constructor privado, para que no se pueda instancias desde otras clases
	private Singleton_TS(){}
	
	//Metodo para recuperar la instancia
	static public TaulaSimbols getInstance(){
		
		//Miro si ya está instanciada, si no pido el espacio
		if(instanceTS==null){
			instanceTS=new TaulaSimbols();
		}
		return instanceTS;
	}
}
