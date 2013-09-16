import java.util.Vector;

import taulasimbols.*;

public class ASem_Inst {

	public static Semantic getAssignOperator(Token oToken){
		//D: Devuelvo Semantic con el token del operador encapsulado
		
		Semantic osResult = new Semantic();		
		osResult.setValue("OPER_ASSIGN", oToken);
		
		return osResult;
	}
	
	public static void checkAssign(Semantic osSem1,Semantic osOper,Semantic osSem2, Token oToken){

		//D: Verifico tipos en la asignación
		
		//D: Verificaciones a hacer:
		//D: 1- Que osSem1 no sea ESTATIC
		//D: 3- Que son del mismo tipo o de tipo equivalente y que se pueden operar con el operador recibido
		
		Semantic osAux = new Semantic();
			
		if(((String)osSem1.getValue("ESTATIC")).equals("CERT")){
			//Es estático, no puedo hacer la asignación
			Singleton_Error.getInstance().writeErrorSem(16, null, oToken);
		}else{
			//Miro que el tipo no sea no sea indefinido
			if(((ITipus)osSem1.getValue("TIPUS")).equals(Utils.aTipusIndefinit) || ((ITipus)osSem2.getValue("TIPUS")).equals(Utils.aTipusIndefinit)){
				//En este caso no tengo que hacer nada. No muestro el error pq ya lo habré mostrado al asignar tipo indefinido.
				return;
			}else{											
				//Si el operador es &=, los dos tipos han de ser CADENA
				if(((Token)osOper.getValue("OPER_ASSIGN")).kind==Babel2009.CONCATIGUAL){
					if(((ITipus)osSem1.getValue("TIPUS")).getNom().equals("CADENA")&&((ITipus)osSem2.getValue("TIPUS")).getNom().equals("CADENA")){
						//Puedo concatenar cadenas. NO hago nada
						return;
					}else{
						//Uno de los dos tipos no es cadena, no puedo concatenar
						if(!((ITipus)osSem1.getValue("TIPUS")).getNom().equals("CADENA")){
							Singleton_Error.getInstance().writeErrorSem(39, null, oToken);
						}else{
							Singleton_Error.getInstance().writeErrorSem(40, null, oToken);
						}
					}
				}
				//Si el operador es += o -= los dos tipos han de ser SENCER
				if(((Token)osOper.getValue("OPER_ASSIGN")).kind==Babel2009.MASIGUAL || ((Token)osOper.getValue("OPER_ASSIGN")).kind==Babel2009.MENOSIGUAL){
					if(((ITipus)osSem1.getValue("TIPUS")).equals(Utils.aTipusSencer)&&((ITipus)osSem2.getValue("TIPUS")).equals(Utils.aTipusSencer)){
						//Puedo operar enteros. NO hago nada
						if ( ((Token)osOper.getValue("OPER_ASSIGN")).kind==Babel2009.MASIGUAL ) {
							if ( ((String)osSem2.getValue("ESTATIC")).compareTo("CERT") == 0 ) {
								/*	 GC: REALIZAMOS EL += con expresion derecha estática	*/
								CodigoEnsamblador.gc("# Codigo para la asignacion += no estatica");
								String R = Utils.getReg();
								CodigoEnsamblador.gc("lw "+R+", "+(String)osSem1.getValue("DIR"));
								CodigoEnsamblador.gc("addi "+R+", "+R+", "+(Integer)osSem2.getValue("VALOR"));
								CodigoEnsamblador.gc("sw "+R+", "+(String)osSem1.getValue("DIR"));
								Utils.setReg(R);
								CodigoEnsamblador.gc("# Fin codigo");
								/*	 FIN GC 	*/
							} else {
								/*	 GC: REALIZAMOS EL += con expresion derecha estática	*/
								CodigoEnsamblador.gc("# Codigo para la asignacion += no estatica");
								String R = Utils.getReg();
								CodigoEnsamblador.gc("lw "+R+", "+(String)osSem1.getValue("DIR"));
								CodigoEnsamblador.gc("add "+R+", "+R+", "+(String)osSem2.getValue("REG"));
								CodigoEnsamblador.gc("sw "+R+", "+(String)osSem1.getValue("DIR"));
								Utils.setReg(R);
								CodigoEnsamblador.gc("# Fin codigo");
								/*	 FIN GC 	*/
							}
						} else if ( ((Token)osOper.getValue("OPER_ASSIGN")).kind==Babel2009.MENOSIGUAL ) {
							if ( ((String)osSem2.getValue("ESTATIC")).compareTo("CERT") == 0 ) {
								/*	 GC: REALIZAMOS EL += con expresion derecha estática	*/
								CodigoEnsamblador.gc("# Codigo para la asignacion += no estatica");
								String R = Utils.getReg();
								CodigoEnsamblador.gc("lw "+R+", "+(String)osSem1.getValue("DIR"));
								String sRegSec = Utils.getReg();
								CodigoEnsamblador.gc("li "+sRegSec+", "+(Integer)osSem2.getValue("VALOR"));
								CodigoEnsamblador.gc("sub "+R+", "+R+", "+sRegSec);
								CodigoEnsamblador.gc("sw "+R+", "+(String)osSem1.getValue("DIR"));
								Utils.setReg(sRegSec);
								Utils.setReg(R);
								CodigoEnsamblador.gc("# Fin codigo");
								/*	 FIN GC 	*/
							} else {
								/*	 GC: REALIZAMOS EL += con expresion derecha estática	*/
								CodigoEnsamblador.gc("# Codigo para la asignacion += no estatica");
								String R = Utils.getReg();
								CodigoEnsamblador.gc("lw "+R+", "+(String)osSem1.getValue("DIR"));
								CodigoEnsamblador.gc("sub "+R+", "+R+", "+(String)osSem2.getValue("REG"));
								CodigoEnsamblador.gc("sw "+R+", "+(String)osSem1.getValue("DIR"));
								Utils.setReg(R);
								CodigoEnsamblador.gc("# Fin codigo");
								/*	 FIN GC 	*/
							}	
						}
						return; 
					}else{
						//Puede que el operador de la derecha sea una función que devuelva un sencer
						if((String)osSem2.getValue("FUNCIO")!=null){
							if(((ITipus)osSem1.getValue("TIPUS")).equals(Utils.aTipusSencer)){
								//osSem2 es una función que devuelve un SENCER. Puedo operar. No hago nada
								return;
							}
						}
						//Uno de los dos tipos no es entero, no puedo operar
						if(!((ITipus)osSem1.getValue("TIPUS")).equals(Utils.aTipusSencer)){
							Singleton_Error.getInstance().writeErrorSem(41, null, oToken);
						}else{
							Singleton_Error.getInstance().writeErrorSem(42, null, oToken);
						}
					}
				}				
				//Si el operador es = los dos tipos han de ser iguales 
				if(((Token)osOper.getValue("OPER_ASSIGN")).kind==Babel2009.IGUAL){
					//Miro si los dos tipos son iguales
					if(((ITipus)osSem1.getValue("TIPUS")).getNom().equals("CADENA") && ((ITipus)osSem2.getValue("TIPUS")).getNom().equals("CADENA")){
						if(((TipusCadena)osSem1.getValue("TIPUS")).equals(((TipusCadena)osSem2.getValue("TIPUS")))){
							//Es tipus cadena. Hacemos CAST para consultar longitud (i no tener sólo tamaño). 
							//Son iguales, no hay que hacer nada
							return;
						}else{
							//Genero semantic para mostrar bien el error 
							osAux.setValue("LONG_VAR",((Integer)((TipusCadena)osSem1.getValue("TIPUS")).getLongitud()).toString());
							osAux.setValue("LONG_EXP",((Integer)((TipusCadena)osSem2.getValue("TIPUS")).getLongitud()).toString());
							Singleton_Error.getInstance().writeErrorSem(43, osAux, oToken);
							return;
						}
					} else {
						//Si no son cadenas pero son iguales
						if(((ITipus)osSem1.getValue("TIPUS")).equals(((ITipus)osSem2.getValue("TIPUS")))){						
							if( ((ITipus)osSem1.getValue("TIPUS")).equals(Utils.aTipusLogic) ||  ((ITipus)osSem1.getValue("TIPUS")).equals(Utils.aTipusSencer) ) {
								/* 			GC: LOS TIPOS SON IGUALES (ENTERO O LOGIC) 			*/
								CodigoEnsamblador.gc("# Genero codigo para la asignacion");
								String R = Utils.getReg();
								//Parte derecha estatica
								if ( ((String)osSem2.getValue("ESTATIC")).compareTo("CERT") == 0) {
									//Si no es string el cast peta. (Podria ser un entero)
									//Si es instanceof String significa que los dos son logic pk los tipus hemos comprobado que sean iguales
									if ( osSem2.getValue("VALOR") instanceof String ) {
										if ( ((String)osSem2.getValue("VALOR")).compareTo("CERT") == 0 ) {
											CodigoEnsamblador.gc("li "+R+", 1");
										} else {
											CodigoEnsamblador.gc("li "+R+", 0");
										}
										CodigoEnsamblador.gc("sw "+R+", "+(String)osSem1.getValue("DIR"));
									} else {
										//Si es instanceof Integer significa que los dos son sencer pk los tipus hemos comprobado que sean iguales
										//El problema es que no sabemos si se trata de una casilla de un vector o una variable sencer
										if(((ITipus)osSem1.getValue("TIPUS")).getNom().substring(0, 2).equals("V_") ) {
											//Se trata de la casilla de un vector
											CodigoEnsamblador.gc("# La parte izquierda de la asignacion es la casilla de un vector. La parte derecha es estatica");
											CodigoEnsamblador.gc("li "+R+", "+((Integer)osSem2.getValue("VALOR")).intValue());
											CodigoEnsamblador.gc("sw "+R+", "+(String)osSem1.getValue("DIR"));
										} else {
											//Se trata de una variable sencera
											CodigoEnsamblador.gc("# La parte izquierda de la asignacion es una variable sencera. La parte derecha es estatica.");
											CodigoEnsamblador.gc("li "+R+", "+((Integer)osSem2.getValue("VALOR")).intValue());
											CodigoEnsamblador.gc("sw "+R+", "+(String)osSem1.getValue("DIR"));
										}
									}
								//Parte derecha no estatica
								} else {
									CodigoEnsamblador.gc("sw "+osSem2.getValue("REG")+", "+(String)osSem1.getValue("DIR"));
								}
								Utils.setReg(R);
								/* 							FIN GC					 			*/
								return;
							} else {
								//MIRAR SI ES VECTOR - VECTOR
								// I DESPRÉS MIRAR (UNA MICA MÉS AVALL DEL CODI) ELS CASOS ESTATIC-VECTOR, VECTOR-ESTATIC.
								if(((ITipus)osSem1.getValue("TIPUS")).getNom().substring(0, 2).equals("V_") ) {
									//Tots dos son vectors
									/* GC: ASIGNACIÓN DE VARIABLES PARA DOS CASILLAS DE VECTORES */
									System.out.println("ES VECTOOOOOORKJNDSKLJIDSFGBLJKDFS");
								}
							}
						}else{
							if(((ITipus)osSem2.getValue("TIPUS")).equals(Utils.aTipusIndefinit)||((ITipus)osSem2.getValue("TIPUS")).equals(Utils.aTipusIndefinit)){
								//Alguna de las partes es indefinida. No muestro error pq ya lo habré mostrado
								return;
							}else{						
								//Las partes no son indefinidas pero tampoco son iguales. No tenemos inefinidos ni cadenas: puede ser SENCER, LOGIC, VECTOR
								//Genero semantic para mostrar bien el error
								System.out.println(((ITipus)osSem1.getValue("TIPUS")).getNom().substring(0, 2));
								System.out.println(((ITipus)osSem2.getValue("TIPUS")).getNom().substring(0, 2));
								if(((ITipus)osSem1.getValue("TIPUS")).getNom().substring(0, 2).equals("V_") && ((ITipus)osSem2.getValue("TIPUS")).getNom().substring(0, 2).equals("V_")){
									//Son vectores de tamaño diferente
									//LOS VECTORES NO SE PUEDEN ASIGNAR JEJEJEJE
									Singleton_Error.getInstance().writeErrorSem(45, osAux, oToken);
									return;
								}
								if(((ITipus)osSem1.getValue("TIPUS")).getNom().substring(0, 2).equals("V_") && !((ITipus)osSem2.getValue("TIPUS")).getNom().substring(0, 2).equals("V_")){
									//El primero es vector pero el segundo no
									osAux.setValue("TIPUS_VAR","VECTOR");
									osAux.setValue("TIPUS_EXP",((ITipus)osSem2.getValue("TIPUS")).getNom());
								}
								if(!((ITipus)osSem1.getValue("TIPUS")).getNom().substring(0, 2).equals("V_") && ((ITipus)osSem2.getValue("TIPUS")).getNom().substring(0, 2).equals("V_")){
									//El segundo es vector pero el primero no
									osAux.setValue("TIPUS_VAR",((ITipus)osSem1.getValue("TIPUS")).getNom());
									osAux.setValue("TIPUS_EXP","VECTOR");
								}
								if(!((ITipus)osSem1.getValue("TIPUS")).getNom().substring(0, 2).equals("V_") && !((ITipus)osSem2.getValue("TIPUS")).getNom().substring(0, 2).equals("V_")){
									//Ninguno de los dos es vector, pero son diferentes
									osAux.setValue("TIPUS_VAR",((ITipus)osSem1.getValue("TIPUS")).getNom());
									osAux.setValue("TIPUS_EXP",((ITipus)osSem2.getValue("TIPUS")).getNom());
								}
								Singleton_Error.getInstance().writeErrorSem(17, osAux, oToken);							
								
							}
						}
					}
				}
			}

		}
		
	}
	
	public static void checkEscriure(Semantic osSem1,Token oToken){
		//Verifico que osSem1 es de tipo SENCER o LOGIC o CADENA
		
		if(osSem1.getValue("TIPUS")!=null){
			if(((ITipus)osSem1.getValue("TIPUS")).equals(Utils.aTipusIndefinit)){
				//Es tipo indefinido, no hago nada pq ya he mostrado el error antes.
			}else{
				if(((ITipus)osSem1.getValue("TIPUS")).getNom().equals("LOGIC") || 
						((ITipus)osSem1.getValue("TIPUS")).getNom().equals("SENCER") ||
						(((ITipus)osSem1.getValue("TIPUS")).getNom().equals("CADENA"))&&((String)osSem1.getValue("ESTATIC")).equals("CERT")){
					
					if ( ((ITipus)osSem1.getValue("TIPUS")).getNom().equals("SENCER") ) {
						if (osSem1.getValue("ESTATIC").equals("CERT")) {
							/*			GC: Escribo entero			*/
							CodigoEnsamblador.gc("# Codigo para escribir un entero estatico");
							CodigoEnsamblador.gc("li $v0, 1");
							CodigoEnsamblador.gc("li $a0, "+((Integer)osSem1.getValue("VALOR")).intValue());
							CodigoEnsamblador.gc("syscall");
							CodigoEnsamblador.gc("# Fin codigo");
							/*				FIN GC					*/
						} else {
							/*			GC: Escribo entero			*/
							CodigoEnsamblador.gc("# Codigo para escribir un entero no estatico");
							CodigoEnsamblador.gc("li $v0, 1");
							CodigoEnsamblador.gc("move $a0, "+(String)osSem1.getValue("REG"));
							CodigoEnsamblador.gc("syscall");
							CodigoEnsamblador.gc("# Fin codigo");
							/*				FIN GC					*/
						}
					}
					
					if ( ((ITipus)osSem1.getValue("TIPUS")).getNom().equals("LOGIC") ) {
						if (osSem1.getValue("ESTATIC").equals("CERT")) {
							/*			GC: Escribo entero			*/
							CodigoEnsamblador.gc("# Codigo para escribir un logico estatico");
							//Esto aquí es inutil--CodigoEnsamblador.gc("li $v0, 1");
							CodigoEnsamblador.gc("li $v0, 4");	 //Printf
							
							if ( osSem1.getValue("VALOR") instanceof Integer) {
								if (((Integer)osSem1.getValue("VALOR")).intValue() == 1 ) 
									CodigoEnsamblador.gc("la $a0, CERT"); //Lo que quieres escribir
								else if (((Integer)osSem1.getValue("VALOR")).intValue() == 0 ) 
									CodigoEnsamblador.gc("la $a0, FALS"); //Lo que quieres escribir
							} else if ( osSem1.getValue("VALOR") instanceof String ){
								if ( ((String)osSem1.getValue("VALOR")).compareTo("CERT") == 0 ) 
									CodigoEnsamblador.gc("la $a0, CERT"); //Lo que quieres escribir
								else if (((String)osSem1.getValue("VALOR")).compareTo("FALS") == 0 ) 
									CodigoEnsamblador.gc("la $a0, FALS"); //Lo que quieres escribir
							}
							CodigoEnsamblador.gc("syscall");
							CodigoEnsamblador.gc("# Fin codigo");
							/*				FIN GC					*/
						} else {
							String Eti1 = Utils.getEtiqueta();
							String Eti2 = Utils.getEtiqueta();
							/*			GC: Escribo entero			*/
							CodigoEnsamblador.gc("# Codigo para escribir un logico no estatico");
							CodigoEnsamblador.gc("li $v0, 1");
							CodigoEnsamblador.gc("li $v0, 4");	 //Printf
							CodigoEnsamblador.gc("beq "+osSem1.getValue("REG")+", $0, "+Eti1);
							CodigoEnsamblador.gc("la $a0, CERT"); //Lo que quieres escribir
							CodigoEnsamblador.gc("j "+Eti2);
							CodigoEnsamblador.gc(Eti1+":");
							CodigoEnsamblador.gc("la $a0, FALS"); //Lo que quieres escribir
							CodigoEnsamblador.gc(Eti2+":");
							CodigoEnsamblador.gc("syscall");
							CodigoEnsamblador.gc("# Fin codigo");
							/*				FIN GC					*/
						}
					}
					
					if ( ((ITipus)osSem1.getValue("TIPUS")).getNom().equals("CADENA") ) {
						if (osSem1.getValue("ESTATIC").equals("CERT")) {
							/*			GC: Escribo entero			*/
							CodigoEnsamblador.gc("# Codigo para escribir una cadena estatica");
							CodigoEnsamblador.gc("li $v0, 1");
							CodigoEnsamblador.gc("li $v0, 4");	 //Printf
							String Eti = Utils.getEtiqueta(); 
							CodigoEnsamblador.gc("la $a0, "+Eti); //Lo que quieres escribir
							CodigoEnsamblador.gc("syscall");
							//Añadimos el secmento .asciiz con el valor de la constante cadena
							CodigoEnsamblador.gc(".data");
							//CodigoEnsamblador.gc(".align 0");
							CodigoEnsamblador.gc(Eti+":");
							CodigoEnsamblador.gc(".asciiz \""+osSem1.getValue("VALOR")+"\"");
							CodigoEnsamblador.gc(".text");
							//CodigoEnsamblador.gc(".align 2");
							CodigoEnsamblador.gc("# Fin codigo");
							/*				FIN GC					*/
						} else {
							//La cadena ha de ser estatica. Nomes es poden escriure cadenes constants
						}
					}
					//Entonces es OK, no tengo que hacer nada
					return;
				}else{
					//Error
					Singleton_Error.getInstance().writeErrorSem(19, null, oToken);
					return;
				}
			}
		}
	}
	
	public static void checkLlegir(Semantic osSem1,Token oToken){
		//Solo se pueden leer variables de tipo SIMPLE
		
		if(osSem1.getValue("TIPUS")!=null){
			if(((ITipus)osSem1.getValue("TIPUS")).equals(Utils.aTipusIndefinit)){
				//Es tipo indefinido, no hago nada pq ya he mostrado el error antes.
			}else{
				if(((String)osSem1.getValue("ESTATIC")).equals("CERT")){
					//No es una variable
					Singleton_Error.getInstance().writeErrorSem(14, null, oToken);
				}else{
					if((((ITipus)osSem1.getValue("TIPUS")).getNom().equals("LOGIC") || 
						((ITipus)osSem1.getValue("TIPUS")).getNom().equals("SENCER"))  
						&& ((String)osSem1.getValue("FUNCIO"))==null ){
						
						if ( ((ITipus)osSem1.getValue("TIPUS")).getNom().equals("SENCER") ) {
							if (osSem1.getValue("ESTATIC").equals("FALS")) {
								/*			GC: Escribo entero			*/
								CodigoEnsamblador.gc("# Codigo para leer un entero estatico");
								CodigoEnsamblador.gc("li $v0, 5");
								CodigoEnsamblador.gc("syscall");
								CodigoEnsamblador.gc("sw $v0, "+(String)osSem1.getValue("DIR"));
								CodigoEnsamblador.gc("# Fin codigo");
								/*				FIN GC					*/
							}
						}
						if ( ((ITipus)osSem1.getValue("TIPUS")).getNom().equals("LOGIC") ) {
							if (osSem1.getValue("ESTATIC").equals("FALS")) {
								/*			GC: Escribo entero			*/
								CodigoEnsamblador.gc("# Codigo para leer un logic estatico");
								//--CodigoEnsamblador.gc("li $v0, 5");
								//--CodigoEnsamblador.gc("syscall");
								
								//Leemos y guardamos el logic introducido por el usuario
								String RStr1 = Utils.getReg();
								String RStr2 = Utils.getReg();
								String ResultPrimeraComparacion = Utils.getReg();
								String ResultSegundaComparacion = Utils.getReg();
								String RDest = Utils.getReg();
								String EtiStrcmp1 = Utils.getEtiqueta();
								String EtiStrcmp2 = Utils.getEtiqueta();
								String EtiVueltaStrcmp1 = Utils.getEtiqueta();
								String EtiVueltaStrcmp2 = Utils.getEtiqueta();
								
								CodigoEnsamblador.gc("li $v0,8");
								CodigoEnsamblador.gc("la $a0,readedLogic");
								CodigoEnsamblador.gc("addi $a1, $zero, 5");
								CodigoEnsamblador.gc("syscall   #got string 1");
								
								//Almacenamos la dirección de lo instroducido por el usuario y lo que vamos a comparar
								CodigoEnsamblador.gc("la "+RStr1+",readedLogic  #pass address of str1");
								CodigoEnsamblador.gc("la "+RStr2+",CERT  #pass address of str2");
								CodigoEnsamblador.gc("j strcmpCode"+EtiStrcmp1+"  #call strcmpCode");
								GC.strcmpCode(RStr1, RStr2, RDest, EtiStrcmp1, EtiVueltaStrcmp1);
								CodigoEnsamblador.gc(EtiVueltaStrcmp1+":");
								
								CodigoEnsamblador.gc("move "+ResultPrimeraComparacion+", "+RDest);
								CodigoEnsamblador.gc("la "+RStr2+",FALS  #pass address of str2");
								CodigoEnsamblador.gc("j strcmpCode"+EtiStrcmp2+"  #call strcmpCode");
								GC.strcmpCode(RStr1, RStr2, RDest, EtiStrcmp2, EtiVueltaStrcmp2);
								CodigoEnsamblador.gc(EtiVueltaStrcmp2+":");
								
								CodigoEnsamblador.gc("move "+ResultSegundaComparacion+", "+RDest);
								
								
								String Eti = Utils.getEtiqueta();
								String Eti2 = Utils.getEtiqueta();
								String Eti3 = Utils.getEtiqueta();
								//--CodigoEnsamblador.gc("la "+R+", CERT");
								//--CodigoEnsamblador.gc("la "+R2+", FALS");
								//Comparamos lo leido con "CERT". Si es igual saltamos
								CodigoEnsamblador.gc("beqz "+ResultPrimeraComparacion+", "+Eti);
									//Comprobamos si es igual a "FALS". Si no lo es nos vamos
									CodigoEnsamblador.gc("bnez "+ResultSegundaComparacion+", "+Eti2);
									//Es igual a "FALS"
									String R3 = Utils.getReg();
									CodigoEnsamblador.gc("li "+R3+", 0");
									CodigoEnsamblador.gc("sw "+R3+", "+(String)osSem1.getValue("DIR"));
									Utils.setReg(R3);
									CodigoEnsamblador.gc("b "+Eti2);
								//Si es "CERT"
								CodigoEnsamblador.gc(Eti+":");
								//Es igual a "CERT"
								String R4 = Utils.getReg();
								CodigoEnsamblador.gc("li "+R4+", 1");
								CodigoEnsamblador.gc("sw "+R4+", "+(String)osSem1.getValue("DIR"));
								Utils.setReg(R4);
								CodigoEnsamblador.gc("b "+Eti2);
								//Fin
								CodigoEnsamblador.gc(Eti2+":");
								
								//CodigoEnsamblador.gc("sw $v0, "+(String)osSem1.getValue("DIR"));
								CodigoEnsamblador.gc("# Fin codigo");
								/*				FIN GC					*/
								Utils.setReg(RStr1);
								Utils.setReg(RStr2);
								Utils.setReg(ResultPrimeraComparacion);
								Utils.setReg(ResultSegundaComparacion);
								Utils.setReg(RDest);
							}
						}
						return;
					}else
						//Entonces no es tipus simple
						Singleton_Error.getInstance().writeErrorSem(15, null, oToken);
				}
			}
		}
		
	}
	
	public static Semantic checkSurticicle(Semantic osH, Token oToken){
		//Miro que el osH tenga valor en BCICLE. Si no tiene, es que es un surtcicle fuera de ciclo
		
		if(osH.getValue("BCICLE")!=null){
			//Entonces está bien, vengo de un CICLE
			//Retorno para poder generar warning por cicle sense surtcicle
			if(((String)osH.getValue("BCICLE")).equals("FALS")){
				//Llavors tinc dos surtcicles al mateix cicle
				Singleton_Error.getInstance().writeErrorSem(46, null, oToken);
			}
			osH.setValue("BCICLE","FALS");			
		}else{
			//No vengo de CICLE
			Singleton_Error.getInstance().writeErrorSem(26, null, oToken);
		}
		
		return osH;
	}
	
	public static void checkWarningCicle(Semantic osH, Token oToken){
		
		if(osH.getValue("BCICLE")!=null){
			if(((String)osH.getValue("BCICLE")).equals("CERT")){
				//No se ha puesto el valor a FALS, no ha habido surtcicle
				Singleton_Error.getInstance().writeErrorSem(101, null, oToken);
			}
		}
	}
	
	public static void checkFiFuncio(Semantic osH,Token oToken){
		//TODO: Verificar
		if(osH.getValue("BFUNCIO")!=null){
			if(((String)osH.getValue("BFUNCIO")).equals("CERT")){
				//No se ha puesto el valor a FALS, no ha habido RETORNA
				Singleton_Error.getInstance().writeErrorSem(51, null, oToken);
			}
		}	
	}
	
	public static Semantic checkRetornar(Semantic osH, Token oToken, Semantic osSemRetornar ){
		
		Semantic sAux = new Semantic(); //Para el error
		//La funció [X] ha de ser del tipus [Y] però en la expressió del seu valor el tipus és [Z]
		
		if(osH.getValue("TIPUSFUNCIO")!=null){
			//Miro que el tipo de retorno sea igual al tipo de retorno de la función
			if(((ITipus)osSemRetornar.getValue("TIPUS")).equals(((ITipus)((Funcio)osH.getValue("TIPUSFUNCIO")).getTipus()))){
				//El tipo de retorno es correcto, no tengo que hacer nada
			}else{
				//El tipo no es correcto
				//Si lo que retorno es indefinido, no hace falta mirar nada
				if(((ITipus)osSemRetornar.getValue("TIPUS")).equals(Utils.aTipusIndefinit)){
					//Es indefinido, no haas nada aqui
				}else{
					//AÑADIDO por F3P2: Si lo que retorna la función es indefinido tampoco hace falta que ponga nada
					if(!((ITipus)((Funcio)osH.getValue("TIPUSFUNCIO")).getTipus()).equals(Utils.aTipusIndefinit)){
						sAux.setValue("NOMFUNCIO", ((Funcio)osH.getValue("TIPUSFUNCIO")).getNom());
						if(((ITipus)((Funcio)osH.getValue("TIPUSFUNCIO")).getTipus()).equals(Utils.aTipusIndefinit)){
							sAux.setValue("TIPUSFUNCIO","INDEFINIT");
						}else{
							sAux.setValue("TIPUSFUNCIO",((ITipus)((Funcio)osH.getValue("TIPUSFUNCIO")).getTipus()).getNom());
						}
						if(((ITipus)osSemRetornar.getValue("TIPUS")).getNom().substring(0,2).equals("V_")){
							sAux.setValue("TIPUSEXP","VECTOR");
						}else{
							sAux.setValue("TIPUSEXP",((ITipus)osSemRetornar.getValue("TIPUS")).getNom());
						}
						Singleton_Error.getInstance().writeErrorSem(23, sAux, oToken);
					}
				}
			}
		}
		
		//Miro que el osH tenga valor en BFUNCIO. Si no tiene, es que es un retornar fuera de funcio		
		if(osH.getValue("BFUNCIO")!=null){
			//Entonces está bien, vengo de un FUNCIO
			//Retorno para poder generar error por funcio sin retornar
			if(((String)osH.getValue("BFUNCIO")).equals("FALS")){
				//Llavors tinc dos retornar al mateix funcio
				Singleton_Error.getInstance().writeErrorSem(49, null, oToken);
			}
			osH.setValue("BFUNCIO","FALS");			
		}else{
			//No vengo de BFUNCIO
			Singleton_Error.getInstance().writeErrorSem(50, null, oToken);
		}
		
		return osH;
	}	
	
	public static void checkSi(Semantic osSem,Token oToken){
		//Verifico que sea de tipus LOGIC
		if(osSem.getValue("TIPUS")!=null){
			if(!((ITipus)osSem.getValue("TIPUS")).equals(Utils.aTipusIndefinit)){
				if(((ITipus)osSem.getValue("TIPUS")).getNom().equals("LOGIC")){
					//Entonces es OK, no tengo que hacer nada
					return;
				}else{
					//La expresion del SI no es de tipus LOGIC
					Singleton_Error.getInstance().writeErrorSem(12, null, oToken);
				}
			}//Si es indefinido no tengo que hacer nada, ya habré mostrado el error
		}
		
	}
	
	public static void checkPer(Semantic osVar, Semantic osSem1, Semantic osSem2, Token oToken){
		
		//Verifico parametros de PER
		
		//Miro si ID está declarado y es de tipus sencer
		if(((ITipus)osVar.getValue("TIPUS")).equals(Utils.aTipusIndefinit)){
			//La variable no está declarada
			//No muestro error pq ya ha sido mostrado
			return;
		}else{
			if(((ITipus)osVar.getValue("TIPUS")).getNom().equals("SENCER")){
				//El id es ok, Hago el resto de comprovaciones
			}else{
				//El id No es entero, error
				Singleton_Error.getInstance().writeErrorSem(25, null, oToken);
			}
			if(((ITipus)osSem1.getValue("TIPUS")).equals(Utils.aTipusIndefinit)||((ITipus)osSem2.getValue("TIPUS")).equals(Utils.aTipusIndefinit)){
				//Si alguno de los dos es indefinido no tengo que hacer nada
			}else{
				if(((ITipus)osSem1.getValue("TIPUS")).equals(Utils.aTipusSencer)&&((ITipus)osSem2.getValue("TIPUS")).equals(Utils.aTipusSencer)){
					//Correcte
				}else{
					//Alguna de las dos expresiones el Per no son SENCER
					Singleton_Error.getInstance().writeErrorSem(48, null, oToken);
				}	
			}			
		}
	}	
}
