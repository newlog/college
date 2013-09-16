
package taulasimbols;

/**
 * <p>Clase que representa el tipus indefinit del
 * llenguatge Babel.</p>
 */
public class TipusIndefinit extends ITipus {
	
	/**<p>Creador del tipus indefinit</p>*/
	public TipusIndefinit() {
	}
	
	/**
	 * <p>Creador del tipus indefinit</p>
	 * @param (String) nom
	 * @param (int) tamany
	 */
	public TipusIndefinit(String nom, int tamany) {
		super.nom = nom;
		super.tamany = tamany;
	}

	/**
	 * <p>Obté tota la informació del objecte en format XML</p>
	 * @return String
	 */
	public String toXml() {
		String str = "<TipusIndefinit Nom=\"" + nom + 
			"\" Tamany=\"" + tamany + "\"></TipusIndefinit>";
		return str;
	}
 }
