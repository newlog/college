import taulasimbols.ITipus;
import taulasimbols.TaulaSimbols;


public class GC {
	public void verificaLimitesVector(Semantic osExp, Token oToken) {
		Integer oiLi = null;
		Integer oiLs = null;
		TaulaSimbols ts=Singleton_TS.getInstance();
		ts.obtenirBloc(ts.getBlocActual()).obtenirVariable(oToken.image);
		
		if(ts.obtenirBloc(ts.getBlocActual()).obtenirVariable(oToken.image.toUpperCase())!=null){
			//D: Verifico que esté declarado como TipusArray mirando el inicio del código del nombre
			String osNombreVector = ((ITipus)ts.obtenirBloc(ts.getBlocActual()).obtenirVariable(oToken.image.toUpperCase()).getTipus()).getNom().substring(0, 2).toString();
			String osNombreVectorCompleto = ((ITipus)ts.obtenirBloc(ts.getBlocActual()).obtenirVariable(oToken.image.toUpperCase()).getTipus()).getNom().toString();
			if( osNombreVector.equals("V_")){
					//D: Sabemos que es un vector
					oiLi = new Integer(osNombreVectorCompleto.substring(2, 3));
					oiLs = new Integer(osNombreVectorCompleto.substring(4, 5));
			}
		}
		
		if ( ((String)osExp.getValue("ESTATIC")).compareTo("FALS") == 0 ) {
			//Si la expresión no es estática, NO tiene un VALOR asignado
			String R = Utils.getReg();
			CodigoEnsamblador.gc("# Codigo para comprobar los limites del vector cuando su expresion no es estatica");
			CodigoEnsamblador.gc("li "+R+", "+oiLi.intValue());
			CodigoEnsamblador.gc("blt "+((String)osExp.getValue("REG"))+", "+R+", ERROR");
			CodigoEnsamblador.gc("li "+R+", "+oiLs.intValue());
			CodigoEnsamblador.gc("bgt "+((String)osExp.getValue("REG"))+", "+R+", ERROR");
			CodigoEnsamblador.gc("# Fin codigo");
			Utils.setReg(R);
		} else if ( ((String)osExp.getValue("ESTATIC")).compareTo("CERT") == 0 ) {
			//Si la expresión es estática, en el semántico ya hemos calculado que el índice no supere los límites
//			String R = Utils.getReg();
//			String R2 = Utils.getReg();
//			CodigoEnsamblador.gc("# Codigo para comprobar los limites del vector cuando su expresion es estatica");
//			CodigoEnsamblador.gc("li "+R+", "+oiLi.intValue());
//			CodigoEnsamblador.gc("li "+R2+", "+(osExp.getValue("VALOR")));
//			CodigoEnsamblador.gc("blt "+R2+", "+R+", ERROR");
//			CodigoEnsamblador.gc("li "+R+", "+oiLs.intValue());
//			CodigoEnsamblador.gc("bgt "+R2+", "+R+", ERROR");
//			CodigoEnsamblador.gc("# Fin codigo");
//			Utils.setReg(R2);
//			Utils.setReg(R);
		}
	}
	
	
	public Semantic calculaDireccionAccesoVector(Semantic osExp, Token oToken) {
		Integer oiLi = null;
		Integer oiDespl = null;
		TaulaSimbols ts=Singleton_TS.getInstance();
		ts.obtenirBloc(ts.getBlocActual()).obtenirVariable(oToken.image);
		CodigoEnsamblador.gc("# CALCULO LA DIRECCION DE ACCESO AL VECTOR. NO LA DIRECCIÓN NORMAL");
		if(ts.obtenirBloc(ts.getBlocActual()).obtenirVariable(oToken.image.toUpperCase())!=null){
			//D: Verifico que esté declarado como TipusArray mirando el inicio del código del nombre
			String osNombreVector = ((ITipus)ts.obtenirBloc(ts.getBlocActual()).obtenirVariable(oToken.image.toUpperCase()).getTipus()).getNom().substring(0, 2).toString();
			String osNombreVectorCompleto = ((ITipus)ts.obtenirBloc(ts.getBlocActual()).obtenirVariable(oToken.image.toUpperCase()).getTipus()).getNom().toString();
			if( osNombreVector.equals("V_")){
					//D: Sabemos que es un vector
					oiLi = new Integer(osNombreVectorCompleto.substring(2, 3));
					oiDespl = new Integer( (ts.obtenirBloc(ts.getBlocActual()).obtenirVariable(oToken.image.toUpperCase()).getDesplacament()));
			}
		}
		
		if ( ((String)osExp.getValue("ESTATIC")).compareTo("FALS") == 0 ) {
			//Si la expresión no es estática, NO tiene un VALOR asignado
			String R1 = Utils.getReg();			
			CodigoEnsamblador.gc("# Código para calcular la dirección de un elemento vector (No estatico) (Exp 2.2)");
			CodigoEnsamblador.gc("li "+R1+", "+oiLi.intValue());
			//-->CodigoEnsamblador.gc("sub "+R1+", "+R1+", "+(String)osExp.getValue("REG"));
			CodigoEnsamblador.gc("sub "+R1+", "+(String)osExp.getValue("REG")+", "+R1);
			String R2 = Utils.getReg();
			CodigoEnsamblador.gc("li "+R2+", 4");
			//-->CodigoEnsamblador.gc("mul "+R1+", "+R1+", "+R2);
			CodigoEnsamblador.gc("mult "+R1+", "+R2);
			CodigoEnsamblador.gc("mflo "+R1);
			//Miramos en que bloque estamos para sumarle al desplazamiento la dirección de $gp o de $fp
			CodigoEnsamblador.gc("# Código para sumarle la direccion $gp o $fp al desplazamiento de la variable.");
			String RDesplTotal = Utils.getReg();
			if ( ts.getBlocActual() == 0 ) {
				CodigoEnsamblador.gc("la "+RDesplTotal+", 0($gp)");
			} else if ( ts.getBlocActual() == 1 ) {
				CodigoEnsamblador.gc("la "+RDesplTotal+", 0($fp)");
			}
			CodigoEnsamblador.gc("addi "+RDesplTotal+", "+RDesplTotal+", "+oiDespl.toString());
			CodigoEnsamblador.gc("add "+R1+", "+R1+", "+RDesplTotal);
			osExp.setValue("DIR", "0("+R1+")");
			Utils.setReg(R2);
			Utils.setReg(RDesplTotal);
			//Utils.setReg(R1);
		} else if ( ((String)osExp.getValue("ESTATIC")).compareTo("CERT") == 0 ) {
			String R1 = Utils.getReg();			
			CodigoEnsamblador.gc("# Código para calcular la dirección de un elemento vector (Estatico)(Exp 2.2)");
			CodigoEnsamblador.gc("li "+R1+", "+oiLi.intValue());
			//-->CodigoEnsamblador.gc("sub "+R1+", "+R1+", "+(Integer)osExp.getValue("VALOR"));
			String R3 = Utils.getReg();
			CodigoEnsamblador.gc("li "+R3+", "+(Integer)osExp.getValue("VALOR"));
			CodigoEnsamblador.gc("sub "+R1+", "+R3+", "+R1);
			Utils.setReg(R3);
			String R2 = Utils.getReg();
			CodigoEnsamblador.gc("li "+R2+", 4");
			//-->CodigoEnsamblador.gc("mul "+R1+", "+R1+", "+R2);
			CodigoEnsamblador.gc("mult "+R1+", "+R2);
			CodigoEnsamblador.gc("mflo "+R1);
			String RDesplTotal = Utils.getReg();
			if ( ts.getBlocActual() == 0 ) {
				CodigoEnsamblador.gc("la "+RDesplTotal+", 0($gp)");
			} else if ( ts.getBlocActual() == 1 ) {
				CodigoEnsamblador.gc("la "+RDesplTotal+", 0($fp)");
			}
			CodigoEnsamblador.gc("addi "+RDesplTotal+", "+RDesplTotal+", "+oiDespl.toString());
			CodigoEnsamblador.gc("add "+R1+", "+R1+", "+RDesplTotal);
			osExp.setValue("DIR", "0("+R1+")");
			Utils.setReg(R2);
			Utils.setReg(RDesplTotal);
			//Utils.setReg(R1);
		}
		return osExp;
	}
	
	
	/**
	 * 	This method writes the assembler implementation of strcmp C function. 
	 * @param R0
	 * @param R1
	 * @param R2
	 * @param R3
	 * @param R4
	 * @param R5
	 * @return In $v0 register is the strcmp() result value. 0 if equal.
	 */
	public static void strcmpCode(String RStr1, String RStr2, String RDest, String EtiStrcmp, String EtiVuelta) {
		
		String Eti1 = Utils.getEtiqueta();
		String Eti2 = Utils.getEtiqueta();
		String Eti3 = Utils.getEtiqueta();
		String Eti4 = Utils.getEtiqueta();
		
		String R0 = Utils.getReg();
		String R1 = Utils.getReg();
		String R2 = Utils.getReg();
		String R3 = Utils.getReg();
		String R4 = Utils.getReg();
		String R5 = Utils.getReg();
		
		
		CodigoEnsamblador.gc("strcmpCode"+EtiStrcmp+":");
		CodigoEnsamblador.gc("add "+R0+",$zero,$zero");
		CodigoEnsamblador.gc("add "+R1+",$zero,"+RStr1);
		CodigoEnsamblador.gc("add "+R2+",$zero,"+RStr2);
		CodigoEnsamblador.gc(Eti1+":");
		CodigoEnsamblador.gc("lb "+R3+"("+R1+")  #load a byte from each string");
		CodigoEnsamblador.gc("lb "+R4+"("+R2+")");
		CodigoEnsamblador.gc("beqz "+R3+","+Eti2+" #str1 end");
		CodigoEnsamblador.gc("beqz "+R4+","+Eti3);
		CodigoEnsamblador.gc("slt "+R5+","+R3+","+R4+"  #compare two bytes");
		CodigoEnsamblador.gc("bnez "+R5+","+Eti3);
		CodigoEnsamblador.gc("addi "+R1+","+R1+",1  #t1 points to the next byte of str1");
		CodigoEnsamblador.gc("addi "+R2+","+R2+",1");
		CodigoEnsamblador.gc("j "+Eti1);
		CodigoEnsamblador.gc(Eti3+":");
		CodigoEnsamblador.gc("addi "+RDest+",$zero,1");
		CodigoEnsamblador.gc("j "+Eti4);
		CodigoEnsamblador.gc(Eti2+":");
		CodigoEnsamblador.gc("bnez "+R4+","+Eti3);
		CodigoEnsamblador.gc("add "+RDest+",$zero,$zero");

		CodigoEnsamblador.gc(Eti4+":");
		CodigoEnsamblador.gc("j "+EtiVuelta);
		
		
		Utils.setReg(R0);
		Utils.setReg(R1);
		Utils.setReg(R2);
		Utils.setReg(R3);
		Utils.setReg(R4);
		Utils.setReg(R5);
		
	}
}
