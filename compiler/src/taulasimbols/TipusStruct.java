package taulasimbols;

import java.util.Hashtable;

/**
 * <p>Clase que representa un tipus struct del llenguatge Babel</p>
 */
public class TipusStruct extends ITipus {

	/** <p> Taula de hash amb els camps del tipus struct </p> */
	public Hashtable llistaCampStruct = new Hashtable();
	
	/** <p> Creador del tipus struct </p> */
	public TipusStruct() {
	}
	
	/**
	 * <p> Creador del tipus struct
	 * @param nom Nom que te aquest tupus
	 * @param tamany tamany que ocupa aquest tipus
	 */
	public TipusStruct(String nom, int tamany) {
		this.nom = nom;
		this.tamany = tamany;
	}

	/**
	 * <p> Insereix el camp struct dintre la llista de camps </p>
	 * @param campo
	 */
	public void inserirCampStruct(CampStruct campo) {
		llistaCampStruct.put(campo.getNom(), campo);
	}

	/**
	 * <p> Obté el camp struc a partir del seu nom </p>
	 * @param nom nom del camp struct que es vol obtenir
	 * @return retorna el camp amb el nom que se li ha passat per 
	 * parametre o null si no existeix en la llista.
	 */
	public CampStruct obtenirCampStruct(String nom) {
		return (CampStruct) llistaCampStruct.get(nom);
	}

	/**
	 * <p> Obté el número de camps que conté el struct </p>
	 * @return retorna el número de camps que conté el struct
	 */
	public int getNumeroCampStructs() {
		return llistaCampStruct.size();
	}
	
	/**
	 * <p>Compara l'objecte que se li pasa per paramtre amb l'objecte acual,
	 * retorna cert si són iguals.</p>
	 * @param (Object)obj
	 * @return boolean
	 */
	public boolean equals(Object obj) {
		if (obj instanceof TipusStruct) {
			TipusStruct struct = (TipusStruct) obj;
			if (llistaCampStruct == null)
				return struct.llistaCampStruct == null;
			else
				return llistaCampStruct.equals(struct.llistaCampStruct);
		} else {
			return false;
		}
	}
	
	 /**
	 * <p>Obté tota la informació del objecte en format XML</p>
	 * @return String
	 */
	public String toXml() {
		String result = "<TipusStruct Nom=\"" + nom + 
			"\" Tamany=\"" + tamany + 	
			"\" NumeroCamps=\"" + getNumeroCampStructs() + "\">";
		
		result += "<CampsStructs>";
		for(int i=0; i<getNumeroCampStructs(); i++) {
			CampStruct camp = (CampStruct) llistaCampStruct.values().toArray()[i];
			result += camp.toXml();
		}
		result += "</CampsStructs>";
		result += "</TipusStruct>";
		return result;
	}
}
