
package taulasimbols;

/**
 * <p>Classe que representa un paràmetre d'una funció o procediment</p>
 */
public class Parametre extends Variable {

	/**<p>tipus del pas de paràmetre que se li fa a aquest parametre</p>*/
    private TipusPasParametre tipusPas;

    /** <p>Creador de la clase Variable</p>*/
    public Parametre() {
    }
    
    /**
     * <p>Creador de la clase Variable</p>
     * @param (String) nom
     * @param (ITipus) tipus
     * @param (int) desplacament
     */
    public Parametre(String nom, ITipus tipus, int desplacament) {
    	super(nom, tipus, desplacament);
    }
    
    /**
     * <p>Creador de la clase Variable</p>
     * @param (String) nom
     * @param (ITipus) tipus
     * @param (int) desplacament
     * @param (TipusPasParametre) tipusPas
     */
    public Parametre(String nom, ITipus tipus, int desplacament, TipusPasParametre tipusPas) {
    	super(nom, tipus, desplacament);
    	this.tipusPas = tipusPas;
    }
    
	/**
	 * <p>Obté el tipus de pas de paràmetre que se li fa a aquest parametre</p>
	 * @return TipusPasParametre
	 */
    public TipusPasParametre getTipusPasParametre() {        
        return tipusPas;
    } 

	/**
	 * <p>Estableix el pas de paràmetre que se li fa a aquest parametre</p>
	 * @param (TipusPasParametre) tipusPas 
	 */
    public void setTipusPasParametre(TipusPasParametre tipusPas) {        
        this.tipusPas = tipusPas;
    } 
    
    /**
     * <p>Obté un flag que indica si la variable es parametre o no<p>
     * @return (boolean) si es parametre
     */
    public boolean getEsParametre() {
    	return true;
    }

    /**
	 * <p>Obté tota la informació del objecte en format XML</p>
	 * @return String
	 */
    public String toXml() {        
    	String result = "<Parametre Nom=\"" + getNom() + 
    		"\" Desplacament=\"" + getDesplacament() + 
    		"\" TipusPasParametre=\"" + tipusPas.toString() + "\">";
    	result += getTipus().toXml();
    	result += "</Parametre>";
        return result;
    } 
 }
