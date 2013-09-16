
package taulasimbols;

/**
 * <p>Classe que representa una funció del llenguatge Babel</p> 
 */
public class Funcio extends Procediment {

	/**<p>Tipus que retorna la funció</p>*/
    private ITipus tipus;

    /**<p>Desplaçament d'on deixa la funció el resultat</p>*/
    private int desplacament;

    /**<p>Constructor de la classe Funcio</p>*/
    public Funcio() {
    }
    
    /**
     * <p>Constructor de la classe Funcio</p>
     * @param (String) nom
     */
    public Funcio (String nom) {
    	super(nom);
    }
    
    /**
     * <p>Constructor de la classe Funcio</p>
     * @param (String) nom
     * @param (ITipus) tipus
     */
    public Funcio (String nom, ITipus tipus) {
    	super(nom);
    	this.tipus = tipus;
    }
    
    /**
     * <p>Constructor de la classe Funcio</p>
     * @param (String) nom
     * @param (String) etiqueta
     */
    public Funcio (String nom, String etiqueta) {
    	super(nom, etiqueta);
    }
    
    /**
     * <p>Constructor de la classe Funcio</p>
     * @param (String) nom
     * @param (ITipus) tipus
     * @param (String) etiqueta
     */
    public Funcio (String nom, ITipus tipus, String etiqueta) {
    	super(nom, etiqueta);
    	this.tipus = tipus;
    }
    
    /**
     * <p>Constructor de la classe Funcio</p>
     * @param (String) nom
     * @param (ITipus) tipus
     * @param (String) etiqueta
     * @param (int) tamanyFrame
     */
    public Funcio (String nom, ITipus tipus, String etiqueta, int tamanyFrame) {
    	super(nom, etiqueta, tamanyFrame);
    	this.tipus = tipus;
    }
    
	/**
	 * <p>Obté el tipus de la funció</p>
	 * @return ITipus
	 */
    public ITipus getTipus() {        
        return tipus;
    } 

	/**
	 * <p>Estableix el tipus de la funció</p>
	 * @param (ITipus) tipus 
	 */
    public void setTipus(ITipus tipus) {        
        this.tipus = tipus;
    } 

	/**
	 * <p>Obté el desplaçament d'on deixa la funció el resultat</p>
	 * @return int
	 */
    public int getDesplacament() {        
        return desplacament;
    } 

	/**
	 * <p>Estableix el desplaçament d'on deixa la funció el resultat</p>
	 * @param (int) desplacament 
	 */
    public void setDeplacament(int desplacament) {        
        this.desplacament = desplacament;
    } 
    
    /**
	 * <p>Obté tota la informació del objecte en format XML</p>
	 * @return String
	 */
    public String toXml() {        
    	String result = "<Funcio Nom=\"" + getNom() + "\">";
    	result += "<Parametres>";
    	for (int i=0; i<getNumeroParametres(); i++)
    		result += obtenirParametre(i).toXml();
    	result += "</Parametres>";
    	result += tipus.toXml();
    	result += "</Funcio>";
        return result;
    } 
 }
