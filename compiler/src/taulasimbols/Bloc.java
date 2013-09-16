
package taulasimbols;

import java.util.Hashtable;

/**
 * <p>Classe que representa un bloc del llenguatge Babel</p>
 */
public class Bloc {

	/**<p>Llista de constants del bloc</p>*/
    private Hashtable llistaConstants = new Hashtable();

    /**<p>Llista de variables del bloc</p>*/
    private Hashtable llistaVariables = new Hashtable();

    /**<p>Llista de procediments del bloc</p>*/
    private Hashtable llistaProcediments = new Hashtable();

    /**<p>Constructor de la classe Bloc</p>*/
    public Bloc() {
    }
    
    /**
     * <p>Constructor de la classe Bloc. Aquest constructor
     * insereix tots els paràmetres del procediment com a variables</p>
     * @param (Procediment) procediment
     */
    public Bloc(Procediment procediment) {
    	for(int i=0; i<procediment.getNumeroParametres(); i++)
    		inserirVariable(procediment.obtenirParametre(i));
    }
    
	/**
	 * <p>Insereix la constant en la llista de constants</p>
	 * @param (Constant) constant 
	 */
    public void inserirConstant(Constant constant) {        
        llistaConstants.put(constant.getNom(), constant);
    } 

	/**
	 * <p>Obté la constant a partir del nom</p>
	 * @param (String) nom 
	 * @return Constant
	 */
    public Constant obtenirConstant(String nom) {        
        return (Constant) llistaConstants.get(nom);
    } 

    /**
     * <p>Insereix la variable en la llista de variables</p>
     * @param (Variable) variable 
     */
    public void inserirVariable(Variable variable) {        
        llistaVariables.put(variable.getNom(), variable);
    } 

    /**
	 * <p>Obté la variable a partir del nom</p>
	 * @param (String) nom 
	 * @return Variable
	 */
    public Variable obtenirVariable(String nom) {        
        return (Variable) llistaVariables.get(nom);
    } 

    /**
     * <p>Insereix el procediment en la llista de procediments</p>
     * @param (Procediment) funcio 
     */
    public void inserirProcediment(Procediment funcio) {        
        llistaProcediments.put(funcio.getNom(), funcio);
    } 

    /**
	 * <p>Obté el procediment a partir del nom</p>
	 * @param (String) nom 
	 * @return Constant
	 */
    public Procediment obtenirProcediment(String nom) {        
        return (Procediment) llistaProcediments.get(nom);
    } 

    /**
	 * <p>Obté tota la informació del objecte en format XML</p>
	 * @return String
	 */
    public String toXml() {        
    	String result = "<Bloc>";
    	
    	result += "<Constants>";
    	for (int i=0; i<llistaConstants.size(); i++)
    		result += ((Constant)llistaConstants.values().toArray()[i]).toXml();
    	result += "</Constants>";
    	
    	result += "<Variables>";
    	for (int i=0; i<llistaVariables.size(); i++)
    		result += ((Variable)llistaVariables.values().toArray()[i]).toXml();
    	result += "</Variables>";
    	
    	result += "<Procediments>";
    	for (int i=0; i<llistaProcediments.size(); i++)
    		result += ((Procediment)llistaProcediments.values().toArray()[i]).toXml();
    	result += "</Procediments>";
    	
    	result += "</Bloc>";
        return result;
    }  
 }
