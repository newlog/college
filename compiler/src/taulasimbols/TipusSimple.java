package taulasimbols;

/**
 * <p>Clase que representa un tipus simple del llenguatge Babel, com per exemple un
 * sencer, caracter, real, ...</p>
 */
public class TipusSimple extends ITipus {

	/**<p>Valor mínim que pot assolir el tipus</p>*/
	private Object minim;
	
	/**<p>Valor màxim que pot assolir el tipus</p>*/
	private Object maxim;

	/**<p>Constructor de TipusSimple</p>*/
	public TipusSimple() {
	}
	
	/**
	 * <p>Constructor de TipusSimple</p>
	 * @param (String) nom del tipus
	 * @param (int) tamany que ocupa el tipus
	 */
	public TipusSimple(String nom, int tamany) {
		this.nom = nom;
		this.tamany = tamany;
	}
	
	/**
	 * <p>Constructor de TipusSimple</p>
	 * @param (Object) valor mínim que pot assolir aquest tipus
	 * @param (Object) valor màxim que pot assolir aquest tipus
	 */
	public TipusSimple(Object minim, Object maxim) {
		this.minim = minim;
		this.maxim = maxim;
	}
	/**
	 * <p>Constructor de TipusSimple</p>
	 * @param (String) nom del tipus
	 * @param (int) tamany que ocupa el tipus
	 * @param (Object) valor mínim que pot assolir aquest tipus
	 * @param (Object) valor màxim que pot assolir aquest tipus
	 */
	public TipusSimple(String nom, int tamany, Object minim, Object maxim) {
		this.nom = nom;
		this.tamany = tamany;
		this.minim = minim;
		this.maxim = maxim;
	}
	
	/**
	 * <p>Obté el valor mínim que pot assolir el tipus simple</p>
	 * @return Object
	 */
	public Object getMinim() {
		return minim;
	}

	/**
	 * <p>Estableix el valor mínim que pot assolir el tipus simple</p>
	 * @param (Object) minim
	 */
	public void setMinim(Object minim) {
		this.minim = minim;
	}
	
	/**
	 * <p>Obté el valor màxim que pot assolir el tipus simple</p>
	 * @return Object
	 */
	public Object getMaxim() {
		return maxim;
	}

	/**
	 * <p>Estableix el valor màxim que pot assolir el tipus simple</p>
	 * @param (Object) maxim
	 */
	public void setMaxim(Object maxim) {
		this.maxim = maxim;
	}
	
	/**
	 * <p>Compara l'objecte que se li pasa per paramtre amb l'objecte acual,
	 * retorna cert si són iguals.</p>
	 * @param (Object)obj
	 * @return boolean
	 */
	public boolean equals(Object obj) {
		if (obj instanceof TipusSimple) {
			TipusSimple tipus = (TipusSimple) obj;
			boolean equals = super.equals(tipus);
			if (maxim == null)
				equals &= tipus.maxim == null;
			else
				equals &= maxim.equals(tipus.maxim);
			
			if (minim == null)
				equals &= tipus.minim == null;
			else
				equals &= minim.equals(tipus.minim);
			
			return equals;			
		} else {
			return false;
		}
	}
	
	 /**
	 * <p>Obté tota la informació del objecte en format XML</p>
	 * @return String
	 */
	public String toXml() {
		String str = "<TipusSimple Nom=\"" + nom + "\"" + 
			" Tamany=\"" + tamany + "\"";
		if (minim != null)
			str += " Mínim=\"" + minim.toString() + "\"";
		else
			str += " Mínim=\"null\"";
		
		if (maxim != null)
			str += " Màxim=\"" + maxim.toString() + "\">";
		else
			str += " Màxim=\"null\">";
		
		str += "</TipusSimple>";
		return str;
	}
}
