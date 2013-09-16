
package taulasimbols;

/**
 * <p>Clase que representa una constant del llenguatge Babel</p>
 */
public class Constant {

	/**<p>nom de la constant</p>*/
    private String nom;

    /**<p>valor de la constant</p>*/
    private Object valor;

    /**<p>tipus de la constant</p>*/
    private ITipus tipus;

    /** <p>Creador de la clase Constant</p>*/
    public Constant() {
    }
    
    /**
     * <p>Creador de la clase Constant</p>
     * @param (String) nom
     * @param (ITipus) tipus
     * @param (Object) valor
     */
    public Constant(String nom, ITipus tipus, Object valor) {
    	this.nom = nom;
    	this.tipus = tipus;
    	this.valor = valor;
    }
    
	/**
	 * <p>Obté el nom de la constant</p>
	 * @return String
	 */
    public String getNom() {        
        return nom;
    } 

	/**
	 * <p>Estableix el nom de la constant</p>
	 * @param (String)nom 
	 */
    public void setNom(String nom) {        
        this.nom = nom;
    } 

	/**
	 * <p>Obté el valor de la constant</p>
	 * @return Object
	 */
    public Object getValor() {        
        return valor;
    } 

	/**
	 * <p>Estableix el valor de la constant</p>
	 * @param (Object) valor 
	 */
    public void setValor(Object valor) {        
        this.valor = valor;
    } 

	/**
	 * <p>Obté el tipus de la constant</p>
	 * @return ITipus
	 */
    public ITipus getTipus() {        
        return tipus;
    } 

	/**
	 * <p>Estableix el tipus de la constant</p>
	 * @param (ITipus) tipus 
	 */
    public void setTipus(ITipus tipus) {        
        this.tipus = tipus;
    } 

    /**
	 * <p>Obté tota la informació del objecte en format XML</p>
	 * @return String
	 */
    public String toXml() {
    	String result = "<Constant Nom=\"" + nom + "\"";
    	if (valor != null)
    		result += " Valor=\"" + valor.toString() + "\">";
    	else
    		result += " Valor=\"null\">";
    	
    	result += tipus.toXml();
    	result += "</Constant>";
        return result;
    } 
 }
