
package taulasimbols;

/**
 * <p>Classe que representa una variable del llenguatge Babel</p>
 */
public class Variable {

	/**<p>nom de la variable</p>*/
    private String nom;

    /**<p>tipus de la variable</p>*/
    private ITipus tipus;

    /**<p>deslaçament de la variable</p>*/
    private int desplacament;

    /** <p>Creador de la clase Variable</p>*/
    public Variable() {
    }
    
    /**
     * <p>Creador de la clase Variable</p>
     * @param (String) nom
     * @param (ITipus) tipus
     * @param (int) desplacament
     */
    public Variable(String nom, ITipus tipus, int desplacament) {
    	this.nom = nom;
    	this.tipus = tipus;
    	this.desplacament = desplacament;
    }
    
    /**
	 * <p>Obté el nom de la variable</p>
	 * @return String
	 */
    public String getNom() {        
        return nom;
    } 

    /**
	 * <p>Estableix el nom de la variable</p>
	 * @param (String)nom 
	 */
    public void setNom(String nom) {        
        this.nom = nom;
    } 

    /**
	 * <p>Obté el tipus de la variable</p>
	 * @return ITipus
	 */
    public ITipus getTipus() {        
        return tipus;
    } 

	/**
	 * <p>Estableix el tipus de la variable</p>
	 * @param (ITipus) tipus 
	 */
    public void setTipus(ITipus tipus) {        
        this.tipus = tipus;
    }  

	/**
	 * <p>Obté el desplaçament de la variable</p>
	 * @return int
	 */
    public int getDesplacament() {        
        return desplacament;
    } 

	/**
	 * <p>Estableix el desplaçament de la variable</p>
	 * @param (int) desplaçament 
	 */
    public void setDesplacament(int desplacament) {        
        this.desplacament = desplacament;
    } 
    
    /**
     * <p>Obté un flag que indica si la variable es parametre o no<p>
     * @return (boolean) si es parametre
     */
    public boolean getEsParametre() {
    	return false;
    }

    /**
	 * <p>Obté tota la informació del objecte en format XML</p>
	 * @return String
	 */
    public String toXml() {        
    	String result = "<Variable Nom=\"" + nom + "\" Desplacament=\"" + desplacament + "\">";
    	result += tipus.toXml();
    	result += "</Variable>";
        return result;
    } 
 }
