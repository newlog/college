
package taulasimbols;

/**
 * <p>Classe que representa els diferents tipus de pas de 
 * paràmetres que pot tenir el llenguatge Babel.</p>
 */
public class TipusPasParametre {

	/**<p>Tipus de pas de paràmetre per referència</p>*/
    public static TipusPasParametre REFERENCIA = new TipusPasParametre("REFERENCIA");

	/**<p>Tipus de pas de paràmetre per valor</p>*/
    public static TipusPasParametre VALOR = new TipusPasParametre("VALOR");
    
    /**<p>nom del tipus de pas de paramtre</p>*/
	private String nom;

	/**
	 * Constructor de la classe TipusPasParametre
	 * @param nom
	 */
	private TipusPasParametre(String nom) {
		this.nom = nom;
	}
	
    public String toString() {
    	return nom;
    }
 }
