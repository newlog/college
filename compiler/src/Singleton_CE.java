
public class Singleton_CE {
	//Atributo interno Error que será Singleton
	static private CodigoEnsamblador instanceCE = null;
	
	//Constructor privado, para que no se pueda instancias desde otras clases
	private Singleton_CE(){}
	
	//Metodo para recuperar la instancia
	public static CodigoEnsamblador getInstance(String nomFitxer){
		
		//Miro si ya está instanciada, si no pido el espacio
		if(instanceCE == null){
			instanceCE = new CodigoEnsamblador(nomFitxer);
		}
		return instanceCE;
	}
	
	//Metodo para recuperar la instancia sin pasar cadena
	public static CodigoEnsamblador getInstance(){
		
		//Miro si ya está instanciada, si no pido el espacio
		if(instanceCE == null){
			System.out.println("ERROR! Fichero ensamblador no instanciado!");
		}
		return instanceCE;
	}
}
