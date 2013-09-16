
package taulasimbols;

import java.util.Vector;

/**
 * <p>Classe que representa un procediment del llenguatge Babel</p> 
 */
public class Procediment {

	/**<p>Nom del procediment</p>*/
    private String nom;
    
    /**<p>Nom de l'etiqueta</p>*/
    private String etiqueta;
    
    /**<p>Tamany en bytes que ocupa el frame del procediment</p>*/
    private int tamanyFrame;

    /**<p>llista de paràmetres del procediment</p>*/
    private Vector llistaParametre = new Vector();

    /**<p>Constructor de la classe Procediment</p>*/
    public Procediment() {
    }
    
    /**
     * <p>Constructor de la classe Procediment</p>
     * @param (String) nom
     */
    public Procediment(String nom) {
    	this.nom = nom;
    }
    
    /**
     * <p>Constructor de la classe Procediment</p>
     * @param (String) nom
     * @param (String) etiqueta
     */
    public Procediment(String nom, String etiqueta) {
    	this.nom = nom;
    	this.etiqueta = etiqueta;
    }
    
    /**
     * <p>Constructor de la classe Procediment</p>
     * @param (String) nom
     * @param (String) etiqueta
     * @param (int) tamanyFrame
     */
    public Procediment(String nom, String etiqueta, int tamanyFrame) {
    	this.nom = nom;
    	this.etiqueta = etiqueta;
    	this.tamanyFrame = tamanyFrame;
    }
    
	/**
	 * <p>Obté el nom del procediment.</p>
	 * @return String
	 */
    public String getNom() {        
        return nom;
    } 

	/**
	 * <p>Estableix el nom del procediment</p>
	 * @param (String) nom 
	 */
    public void setNom(String nom) {        
        this.nom = nom;
    } 

    /**
     * <p>Obté el nom de l'etiqueta</p>
     * @return String
     */
	public String getEtiqueta() {
		return etiqueta;
	}

	/**
	 * <p>Estableix el nom de l'etiqueta</p>
	 * @param (String) etiqueta
	 */
	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}

	 /**
     * <p>Obté el tamany en bytes del frame del procediment</p>
     * @return int
     */
	public int getTamanyFrame() {
		return tamanyFrame;
	}

	/**
	 * <p>Estableix el tamany en bytes del frame del procediment</p>
	 * @param (int) tamanyFrame
	 */
	public void setTamanyFrame(int tamanyFrame) {
		this.tamanyFrame = tamanyFrame;
	}
	
	/**
	 * <p>Insereix el paràmetre en la llista de paràmetres</p>
	 * @param (Parametre) parametre 
	 */
    public void inserirParametre(Parametre parametre) {        
        llistaParametre.add(parametre);
    } 

	/**
	 * <p>Obté el paràmetre que està en la possició <b>index</b></p>
	 * @param (int) index 
	 * @return Parametre
	 */
    public Parametre obtenirParametre(int index) {        
        return (Parametre) llistaParametre.get(index);
    } 
    
    /**
     * <p>Obté el úmero de paràmetres del procediment</p>
     * @return int
     */
    public int getNumeroParametres() {
    	return llistaParametre.size();
    }

    /**
	 * <p>Obté tota la informació del objecte en format XML</p>
	 * @return String
	 */
    public String toXml() {        
    	String result = "<Procediment Nom=\"" + nom + "\">";
    	result += "<Parametres>";
    	for (int i=0; i<getNumeroParametres(); i++)
    		result += obtenirParametre(i).toXml();
    	result += "</Parametres>";
    	result += "</Procediment>";
        return result;
    } 
 }
