import java.util.Hashtable;

import taulasimbols.*;


public class Utils {
	
	/* NOTE: Pongo en Utils las funciones de getTipus por si se han de 
	 * llamar desde más de un sitio además de en ASem_Dec
	 */
	public static TipusSimple aTipusSencer = new TipusSimple("SENCER", 1, new Integer(-128), new Integer(127));
	public static TipusSimple aTipusLogic = new TipusSimple("LOGIC", 1, new Integer(0), new Integer(1));
	public static TipusIndefinit aTipusIndefinit = new TipusIndefinit();
	public static boolean arrayRegistres [] = 
	{true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true};
	public static int numEtiqueta = 0;
	public static Hashtable<String, String> listaFunciones = new Hashtable<String, String>();
	
	public static ITipus getTipusSimple(String sTipus){
		//Asigno tipo según cadena de entrada sTipus
		
		ITipus oAux = null;
		
		if(sTipus.toUpperCase().equals("SENCER")){ oAux = aTipusSencer; }
		else if(sTipus.toUpperCase().equals("LOGIC")){ oAux = aTipusLogic; }
		else{  oAux = aTipusIndefinit; }
		
		return oAux;
	}
	
	public static ITipus getTipusVector(Integer nLimitInferior,Integer nLimitSuperior,Token oToken){
		ITipus oAux = null;
		
		//Creo la dimension en funcion de los limites y el tipo
		DimensioArray oDimensio = new DimensioArray(getTipusSimple(oToken.image),nLimitInferior,nLimitSuperior);
		
		//Creo codi amb sintaxi ex: V_LI_LS_S
		String sCodi = "";
		if(getTipusSimple(oToken.image).equals(aTipusIndefinit)){
			sCodi = "V_" + nLimitInferior.toString()+ "_" + nLimitSuperior.toString() + "_I";
		}else{
			sCodi = "V_" + nLimitInferior.toString()+ "_" + nLimitSuperior.toString() + "_" + oToken.image.substring(0,1).toUpperCase();
		}
		//Creo el array
		TipusArray oTipusArray = new TipusArray(sCodi, nLimitSuperior.intValue() - nLimitInferior.intValue(), getTipusSimple(oToken.image));
		//Le asigno la dimension
		oTipusArray.inserirDimensio(oDimensio);
		
		oAux = oTipusArray;
		
		return oAux;
	}
	
	public static ITipus getTipusCadena(Integer nLongitud){
		ITipus oAux = null;
		
		//Creo el array
		TipusCadena oTipusCadena = new TipusCadena("CADENA",1*nLongitud,nLongitud);
		//TODO: Preguntar si es necesario pasarle un NOMBRE con algún tipo de sintaxis al TipusCadena
		
		oAux = oTipusCadena;
		
		return oAux;
	}
	
	public static String getEtiqueta() {
		numEtiqueta++;
		return "ETI"+numEtiqueta;
	}
	
	public static String getReg() {
		int i;
		boolean faltaReg = true;
		for (i = 0; i < arrayRegistres.length; i++) {
			if (arrayRegistres[i]) {arrayRegistres[i]=false; faltaReg=false; break;}
		}
		if (faltaReg) {System.out.println("[-] Se ha pedido un registro, pero no hay ninguno libre"); return ""+(-1);}
		else {System.out.println("SE DA REGISTRO $"+(i+8));return "$"+(i+8);}
	}
	public static void setReg(String reg) {
		System.out.println("SE LIBERA REGISTRO "+reg);
		if (reg != null && reg.compareTo("") != 0) {
			Integer i = new Integer(reg.substring(1));
			if (i.intValue() >= 8 && i.intValue() <= 24) arrayRegistres[i.intValue()-8] = true;
			else System.out.println("[-] El registro a liberar no es válido. Registro "+reg);
		} else {
			System.out.println("[-] El registro pasado al método setReg es nulo o está vacío.");
		}
	}
	
	public static Semantic calculaDireccion(Semantic osResult) {
		
		return osResult;
	}
}
