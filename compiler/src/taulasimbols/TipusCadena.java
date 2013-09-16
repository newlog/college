package taulasimbols;

/**
 * <p>Clase que representa el tipus cadena del llenguatge Babel.</p>
 */
public class TipusCadena extends ITipus {

	/**<p>longitud de la cadena</p>*/
	private int longitud;

	/**<p>Creador del tipus cadena</p>*/
	public TipusCadena() {
	}
	
	/**
	 * <p>Creador del tipus cadena</p>
	 * @param (int) longitud
	 */
	public TipusCadena(int longitud) {
		this.longitud = longitud;
	}
	
	/**
	 * <p>Creador del tipus cadena</p>
	 * @param (String) nom
	 * @param (int) tamany
	 */
	public TipusCadena(String nom, int tamany) {
		super.nom = nom;
		super.tamany = tamany;
	}
	
	/**
	 * <p>Creador del tipus cadena</p>
	 * @param (String) nom
	 * @param (int) tamany
	 * @param (int) longitud 
	 */
	public TipusCadena(String nom, int tamany, int longitud) {
		super.nom = nom;
		super.tamany = tamany;
		this.longitud = longitud;
	}
	
	
	/**
	 * <p>Obté la longitud de la cadena</p>
	 * @return int
	 */
	public int getLongitud() {
		return longitud;
	}

	/**
	 * <p>Estableix la longitud de la cadena</p>
	 * @param (int) longitud
	 */
	public void setLongitud(int longitud) {
		this.longitud = longitud;
	}
	
	/**
	 * <p>Compara l'objecte que se li pasa per paramtre amb l'objecte acual,
	 * retorna cert si són iguals.</p>
	 * @param (Object)obj
	 * @return boolean
	 */
	public boolean equal(Object o) {
		if (o instanceof TipusCadena) {
			TipusCadena cadena = (TipusCadena) o;
			return super.equals(cadena) && cadena.longitud == longitud;
		} else {
			return false;
		}
	}
	
	/**
	 * <p>Obté tota la informació del objecte en format XML</p>
	 * @return String
	 */
	public String toXml() {
		String str = "<TipusCadena Nom=\"" + nom + 
			"\" Tamany=\"" + tamany + 
			"\" Longitud=\"" + longitud + "\"></TipusCadena>";
		return str;
	}
}
