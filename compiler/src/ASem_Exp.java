import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import taulasimbols.*;

public class ASem_Exp extends Utils {
	
	public static Semantic calcularUnario(Semantic osOper,Semantic osTerme, Token oToken){
		/* GC: EXPRESIÓN NO ESTÁTICA, DEBEMOS PEDIR REGISTRO 	*/
			String Registro = Utils.getReg();
		/*														*/
		//D: Verificaciones a hacer:
		//D: Miro si es o no estática
		//D: 		Si no es estática	
		//D: 			- Muestro error si los tipos no concuerdan con la operación a realizar (Ex.: Entiendo que -logic no tiene sentido)
		//D:					(Es importante hacer esto pq el valor lo tendré en generación de código, no ahora, pero tengo que verificar que semánticamente el cálculo es OK)
		//D: 			- Retorno osTerme sin modificarlo. No tiene sentido modificarlo pq no es valor estático
		//D: 		Si es estática
		//D: 			- Verifico que en las operaciones concuerden con los tipos
		//D: 			- Retorno el resultado de la ejecución de la operación
		
		//D: Semantic de Resultado
		Semantic osResult = new Semantic();
		
		//D: Asigno los valores iniciales a osResult
		osResult.setValue("TIPUS", osTerme.getValue("TIPUS"));
		osResult.setValue("ESTATIC", osTerme.getValue("ESTATIC"));
		if (osTerme.getValue("REG") != null ) osResult.setValue("REG", osTerme.getValue("REG"));
		
		if(osTerme.getValue("FUNCIO")!=null){
			osResult.setValue("FUNCIO", (String)osTerme.getValue("FUNCIO"));
		}
		
		if(((String)osTerme.getValue("ESTATIC")).equals("CERT")){ //D: He de consultar si es estático o no, pq podría venir de función (no estático y por tanto sin valor) (si no hago la consulta daría NullPointerException)
			osResult.setValue("VALOR", osTerme.getValue("VALOR")); //D: De momento le asigno el valor original, aunque luego lo cambie, para evitar errores en cadena por no poner valor si hay algún error en los tipos
		}
		
		//Si no hay operador, no ha de hacer ningún cambio. No ha de operar unario
		if(osOper.getValue("OPER") != null){	//Aquí no entra quan se li ha operat lògicament
			
			osResult.setValue("OPERAT","CERT"); //Atribut per comprovar tipus de pas de funcio
			
			if(((String)osTerme.getValue("ESTATIC")).toUpperCase().equals("CERT")){
				//D: El Terme tiene valor estático, puedo hacer cálculos en el Semántico
				if(((String)osOper.getValue("OPER")).equals("OP_MAS") || ((String)osOper.getValue("OPER")).equals("OP_MENOS")){
					//D: El operador es para trabajar con tipus SENCER
					
					if(((ITipus)osTerme.getValue("TIPUS")).equals(Utils.aTipusSencer)){
						//D: osTerme es un sencer, puedo calcular
						if(((String)osOper.getValue("OPER")).equals("OP_MENOS")){
							//D: Es un MENOS, multiplico el valor por -1
							osResult.setValue("VALOR", ((Integer)osTerme.getValue("VALOR"))*(-1));
							
							/*  GC: DEBEMOS GENERAR EL CÓDIGO PARA EL MÁS O EL MENOS 	*/
							String RegSecundario = Utils.getReg();
							CodigoEnsamblador.gc("# Codigo de expresion para cuando hay un numero negativo al principio de una expresion");
							//Cargamos en el registro el contenido de la dirección donde se encuentra el ID
							CodigoEnsamblador.gc("li "+RegSecundario+", "+((Integer)osTerme.getValue("VALOR")).intValue());
							//Multiplicamos por -1 el contenido de dicho registro
							CodigoEnsamblador.gc("mul "+Registro+", "+RegSecundario+", -0x1");
							CodigoEnsamblador.gc("mflo "+Registro);
							CodigoEnsamblador.gc("# Fin codigo");
							Utils.setReg(RegSecundario);
							//Almacenamos en el semantic el registro que contiene el resultado de la operación
							osResult.setValue("REG", Registro);
							/* 							FIN GC					 		*/
							
						}else{
							//D: Es un MAS, lo dejo igual
							osResult.setValue("VALOR", osTerme.getValue("VALOR"));
							//D: Es un MAS, lo dejo igual
							
							/*  GC: DEBEMOS GENERAR EL CÓDIGO PARA EL MÁS O EL MENOS 	*/
							//-->String RegSecundario = Utils.getReg();
							//-->CodigoEnsamblador.gc("# Codigo de expresion para cuando hay un numero positivo al principio de una expresion");
							//Cargamos en el registro el contenido de la dirección donde se encuentra el ID
							//-->CodigoEnsamblador.gc("li "+RegSecundario+", "+((Integer)osTerme.getValue("VALOR")).intValue());
							//-->CodigoEnsamblador.gc("# Fin codigo");
							//-->Utils.setReg(RegSecundario);
							//Almacenamos en el semantic el registro que contiene el resultado de la operación
							osResult.setValue("REG", Registro);
							/* 							FIN GC					 		*/
							
						}
						
					}else{
						//D: osTerme NO es un sencer, NO puedo calcular
						Singleton_Error.getInstance().writeErrorSem(9, null, oToken);
						osResult.setValue("TIPUS", Utils.aTipusIndefinit); //Establezco el tipo a indefinido: valor no válido
					}
				}else{
					//D: El operador es para trabajar con LOGIC
					if(((ITipus)osTerme.getValue("TIPUS")).equals(Utils.aTipusLogic)){
						//D: osTerme es un logic, puedo calcular
						//D: Como la operación es un NOT, cambio los valores al opuesto
						if(((String)osTerme.getValue("VALOR")).equals("CERT")){
							osResult.setValue("VALOR","FALS");
							
							/*  GC: DEBEMOS GENERAR EL CÓDIGO PARA EL CIERTO 	*/
							String RegSecundario = Utils.getReg();
							//Cargamos en el registro el contenido de la dirección donde se encuentra el ID
							CodigoEnsamblador.gc("# Asignacion del valor FALS en una variable booleana");
							CodigoEnsamblador.gc("li "+RegSecundario+", 0");
							Utils.setReg(RegSecundario);
							//Almacenamos en el semantic el registro que contiene el resultado de la operación
							osResult.setValue("REG", Registro);
							/* 							FIN GC					 		*/
							
						}else{
							osResult.setValue("VALOR","CERT");
							/*  GC: DEBEMOS GENERAR EL CÓDIGO PARA EL CIERTO 	*/
							String RegSecundario = Utils.getReg();
							//Cargamos en el registro el contenido de la dirección donde se encuentra el ID
							CodigoEnsamblador.gc("# Asignacion del valor CERT en una variable booleana");
							CodigoEnsamblador.gc("li "+RegSecundario+", 1");
							//--->Utils.setReg(RegSecundario);
							//Almacenamos en el semantic el registro que contiene el resultado de la operación
							osResult.setValue("REG", Registro);
							/* 							FIN GC					 		*/
						}
						
					}else{
						//D: osTerme NO es un logic, NO puedo calcular
						Singleton_Error.getInstance().writeErrorSem(10, null, oToken);
						osResult.setValue("TIPUS", Utils.aTipusIndefinit); //Establezco el tipo a indefinido: valor no válido						
					}
				}			
			}else{
				
				//D: El Terme no tiene valor estático
				if(((String)osOper.getValue("OPER")).equals("OP_MAS") || ((String)osOper.getValue("OPER")).equals("OP_MENOS")){
					//D: El operador es para trabajar con tipus SENCER
					if(((ITipus)osTerme.getValue("TIPUS")).equals(Utils.aTipusSencer)){
						//D: osTerme es un sencer, podría calcular en gen. código. No hay que hacer nada, ya devolveré el mismo osTerme
						
						/*  GC: DEBEMOS GENERAR EL CÓDIGO PARA EL MÁS O EL MENOS 	*/
						//D: osTerme es un sencer, puedo calcular
						if(((String)osOper.getValue("OPER")).equals("OP_MENOS")){
							//D: Es un MENOS, multiplico el valor por -1
							
							String RegSecundario = Utils.getReg();
							CodigoEnsamblador.gc("# Codigo de expresion para cuando hay un ID negativo al principio de una expresion");
							CodigoEnsamblador.gc("move "+RegSecundario+", "+(String)osTerme.getValue("REG"));
							//Multiplicamos por -1 el contenido de dicho registro
							CodigoEnsamblador.gc("mul "+Registro+", "+RegSecundario+", -1");
							CodigoEnsamblador.gc("mflo "+Registro);
							CodigoEnsamblador.gc("# Fin codigo");
							Utils.setReg(RegSecundario);
							//Almacenamos en el semantic el registro que contiene el resultado de la operación
							osResult.setValue("REG", Registro);
						}else{
							//D: Es un MAS, lo dejo igual
							//Almacenamos en el semantic el registro que contiene el resultado de la operación
							osResult.setValue("REG", Registro);
						}
						/* 							FIN GC					 		*/
						
					}else{
						//D: osTerme NO es un sencer, NO podría calcular en gen. código
						Singleton_Error.getInstance().writeErrorSem(9, null, oToken);
						osResult.setValue("TIPUS", Utils.aTipusIndefinit);
					}
				}else{
					//D: El operador es para trabajar con LOGIC
					if(((ITipus)osTerme.getValue("TIPUS")).equals(Utils.aTipusLogic)){
						//D: osTerme es un logic, podría calcular en gen. código. No hay que hacer nada, ya devolveré el mismo osTerme
						
						/*  GC: DEBEMOS GENERAR EL CÓDIGO PARA EL MÁS O EL MENOS 	*/
						//D: osTerme es un logic, puedo calcular
						if(((String)osOper.getValue("OPER")).equals("OP_NOT")){
							//D: Es una NOT
							//Cojemos la dirección del ID que nos viene de Terme()
							//String sDir = (String)osTerme.getValue("DIR");
							String RegSecundario = Utils.getReg();
							CodigoEnsamblador.gc("# Codigo de expresion para cuando hay un ID al que hacerle una NOT");
							//Cargamos en el registro el contenido de la dirección donde se encuentra el ID
							CodigoEnsamblador.gc("move "+RegSecundario+", "+(String)osTerme.getValue("REG"));
							//Comprobamos si es un 0 o un 1
							String Eti1 = Utils.getEtiqueta();
							String Eti2 = Utils.getEtiqueta();
							CodigoEnsamblador.gc("beq "+RegSecundario+", 0, "+Eti1);
							CodigoEnsamblador.gc("li "+RegSecundario+", 1");
							CodigoEnsamblador.gc("j "+Eti2);
							CodigoEnsamblador.gc(Eti1+":");
							CodigoEnsamblador.gc("li "+RegSecundario+", 0");
							CodigoEnsamblador.gc(Eti2+":");
							CodigoEnsamblador.gc("# Fin codigo");
							Utils.setReg(RegSecundario);
							//Almacenamos en el semantic el registro que contiene el resultado de la operación
							osResult.setValue("REG", Registro);
						}
					}else{
						//D: osTerme NO es un logic, NO podría calcular en gen. código
						Singleton_Error.getInstance().writeErrorSem(10, null, oToken);
						osResult.setValue("TIPUS", Utils.aTipusIndefinit);
					}
				}
			}
		} else {
			if(osTerme.getValue("OPERAT")!=null){
				osResult.setValue("OPERAT","CERT"); //Atribut per comprovar tipus de pas de funcio
			}
		}
		
		/* GC: FIN EXPRESIÓN NO ESTÁTICA, DEBEMOS LIBERAR REGISTRO 	*/
		Utils.setReg(Registro);
		/*															*/
		return osResult;
	}
	
	public static Semantic calcularBinario(Semantic s1,Semantic soOper,Semantic s2,Token oToken){
		//D: Ver que operación hay que hacer y en función de eso ejecutar 
		//operación LOGICA o de SENCER o de CADENA
		
		//D: Semantic de Resultado
		Semantic osResult=new Semantic();				
		
		//D: Operacionens de LOGIC:
		if(((String)soOper.getValue("OPER")).equals("OP_OR")){			osResult=calcularLogic(s1,soOper,s2,oToken); }
		else if(((String)soOper.getValue("OPER")).equals("OP_AND")){	osResult=calcularLogic(s1,soOper,s2,oToken); }
		
		//D: Operaciones de SENCER:
		if(((String)soOper.getValue("OPER")).equals("OP_MAS")){			osResult=calcularSencer(s1,soOper,s2,oToken); }
		else if(((String)soOper.getValue("OPER")).equals("OP_MENOS")){	osResult=calcularSencer(s1,soOper,s2,oToken); }
		else if(((String)soOper.getValue("OPER")).equals("OP_DIV")){	osResult=calcularSencer(s1,soOper,s2,oToken); }
		else if(((String)soOper.getValue("OPER")).equals("OP_MUL")){	osResult=calcularSencer(s1,soOper,s2,oToken); }
		
		//D: Operaciones de CADENA
		if(((String)soOper.getValue("OPER")).equals("OP_CONCAT")){	osResult=calcularCadena(s1,soOper,s2,oToken); }
		
		if(soOper.getValue("OPER")!=null){
			osResult.setValue("OPERAT","CERT"); //Atribut per comprovar tipus de pas de funcio
		}
		
		return osResult;
	}
	
	public static Semantic calcularRelacional(Semantic osSem1,Token oToken,Semantic osSem2){
		//Acciones:
		/*
		 * 1- No sea indefinido
		 * 2- Los dos sean de tipo SENCER
		 * 3- Calculo los valores
		 */
		Semantic osResult = new Semantic();
		osResult.setValue("TIPUS",Utils.aTipusIndefinit);
		osResult.setValue("ESTATIC", "FALS");
		//Guardo registro que tendré que liberar después de la condicion del si
		/* GC: Guardamos el registro para saber si saltamos o no en el si (o per) */
		String R = Utils.getReg();
		osResult.setValue("REGOPERACIONAL",R);
		/*			FIN GC 				*/
		
		if((osSem1.getValue("TIPUS")!=null)&& (osSem2.getValue("TIPUS")!=null)){
			if(((ITipus)osSem1.getValue("TIPUS")).equals(Utils.aTipusIndefinit) || ((ITipus)osSem2.getValue("TIPUS")).equals(Utils.aTipusIndefinit)){
				//Alguno de los dos es Indefinido, no hago nada. 
				return osResult;
			}else{
				if((((ITipus)osSem1.getValue("TIPUS")).getNom().equals("LOGIC")&& ((ITipus)osSem2.getValue("TIPUS")).getNom().equals("LOGIC")) || ((ITipus)osSem1.getValue("TIPUS")).getNom().equals("SENCER")&& ((ITipus)osSem2.getValue("TIPUS")).getNom().equals("SENCER")){
					//Se cual sea la opcion luego, lo que retorne será de tipus LOGIC
					osResult.setValue("TIPUS", Utils.getTipusSimple("LOGIC"));
					//Miro si puedo operar
					if(((String)osSem1.getValue("ESTATIC")).equals("CERT") && ((String)osSem2.getValue("ESTATIC")).equals("CERT")){
						//Opera
						osResult.setValue("ESTATIC", "CERT"); //En cualquier caso la expresión será estática
						
						osResult.setValue("OPERAT","CERT"); //Atribut per comprovar tipus de pas de funcio
						
						if(oToken.image.equals("==")){
							if(((ITipus)osSem1.getValue("TIPUS")).getNom().equals("LOGIC")){
								if(((String)osSem1.getValue("VALOR")).equals("FALS") && ((String)osSem2.getValue("VALOR")).equals("FALS")){
									osResult.setValue("VALOR", "CERT");
								}else if(((String)osSem1.getValue("VALOR")).equals("FALS") && !((String)osSem2.getValue("VALOR")).equals("CERT")){
									osResult.setValue("VALOR", "FALS");
								}else if(!((String)osSem1.getValue("VALOR")).equals("CERT") && ((String)osSem2.getValue("VALOR")).equals("FALS")){
									osResult.setValue("VALOR", "FALS");
								}else if(!((String)osSem1.getValue("VALOR")).equals("CERT") && !((String)osSem2.getValue("VALOR")).equals("CERT")){
									osResult.setValue("VALOR", "CERT");
								}								
							}else{
								if(((Integer)osSem1.getValue("VALOR"))==((Integer)osSem2.getValue("VALOR"))){
									osResult.setValue("VALOR", "CERT");
								}else{
									osResult.setValue("VALOR", "FALS");
								}
							}
						}
						if(oToken.image.equals(">=")){
							if(((ITipus)osSem1.getValue("TIPUS")).getNom().equals("LOGIC")){
								if(((String)osSem1.getValue("VALOR")).equals("FALS") && ((String)osSem2.getValue("VALOR")).equals("FALS")){
									osResult.setValue("VALOR", "CERT");
								}else if(((String)osSem1.getValue("VALOR")).equals("FALS") && !((String)osSem2.getValue("VALOR")).equals("CERT")){
									osResult.setValue("VALOR", "FALS");
								}else if(!((String)osSem1.getValue("VALOR")).equals("CERT") && ((String)osSem2.getValue("VALOR")).equals("FALS")){
									osResult.setValue("VALOR", "CERT");
								}else if(!((String)osSem1.getValue("VALOR")).equals("CERT") && !((String)osSem2.getValue("VALOR")).equals("CERT")){
									osResult.setValue("VALOR", "CERT");
								}								
							}else{
								if(((Integer)osSem1.getValue("VALOR"))>=((Integer)osSem2.getValue("VALOR"))){
									osResult.setValue("VALOR", "CERT");
								}else{
									osResult.setValue("VALOR", "FALS");
								}
							}
						}
						if(oToken.image.equals("<=")){
							if(((ITipus)osSem1.getValue("TIPUS")).getNom().equals("LOGIC")){
								if(((String)osSem1.getValue("VALOR")).equals("FALS") && ((String)osSem2.getValue("VALOR")).equals("FALS")){
									osResult.setValue("VALOR", "CERT");
								}else if(((String)osSem1.getValue("VALOR")).equals("FALS") && !((String)osSem2.getValue("VALOR")).equals("CERT")){
									osResult.setValue("VALOR", "CERT");
								}else if(!((String)osSem1.getValue("VALOR")).equals("CERT") && ((String)osSem2.getValue("VALOR")).equals("FALS")){
									osResult.setValue("VALOR", "FALS");
								}else if(!((String)osSem1.getValue("VALOR")).equals("CERT") && !((String)osSem2.getValue("VALOR")).equals("CERT")){
									osResult.setValue("VALOR", "CERT");
								}								
							}else{
								if(((Integer)osSem1.getValue("VALOR"))<=((Integer)osSem2.getValue("VALOR"))){
									osResult.setValue("VALOR", "CERT");
								}else{
									osResult.setValue("VALOR", "FALS");
								}
							}
						}
						if(oToken.image.equals(">")){
							if(((ITipus)osSem1.getValue("TIPUS")).getNom().equals("LOGIC")){
								if(((String)osSem1.getValue("VALOR")).equals("FALS") && ((String)osSem2.getValue("VALOR")).equals("FALS")){
									osResult.setValue("VALOR", "FALS");
								}else if(((String)osSem1.getValue("VALOR")).equals("FALS") && !((String)osSem2.getValue("VALOR")).equals("CERT")){
									osResult.setValue("VALOR", "FALS");
								}else if(!((String)osSem1.getValue("VALOR")).equals("CERT") && ((String)osSem2.getValue("VALOR")).equals("FALS")){
									osResult.setValue("VALOR", "CERT");
								}else if(!((String)osSem1.getValue("VALOR")).equals("CERT") && !((String)osSem2.getValue("VALOR")).equals("CERT")){
									osResult.setValue("VALOR", "FALS");
								}								
							}else{
								if(((Integer)osSem1.getValue("VALOR"))>((Integer)osSem2.getValue("VALOR"))){
									osResult.setValue("VALOR", "CERT");
								}else{
									osResult.setValue("VALOR", "FALS");
								}
							}
						}
						if(oToken.image.equals("<")){
							if(((ITipus)osSem1.getValue("TIPUS")).getNom().equals("LOGIC")){
								if(((String)osSem1.getValue("VALOR")).equals("FALS") && ((String)osSem2.getValue("VALOR")).equals("FALS")){
									osResult.setValue("VALOR", "FALS");
								}else if(((String)osSem1.getValue("VALOR")).equals("FALS") && !((String)osSem2.getValue("VALOR")).equals("CERT")){
									osResult.setValue("VALOR", "CERT");
								}else if(!((String)osSem1.getValue("VALOR")).equals("CERT") && ((String)osSem2.getValue("VALOR")).equals("FALS")){
									osResult.setValue("VALOR", "FALS");
								}else if(!((String)osSem1.getValue("VALOR")).equals("CERT") && !((String)osSem2.getValue("VALOR")).equals("CERT")){
									osResult.setValue("VALOR", "FALS");
								}								
							}else{
								if(((Integer)osSem1.getValue("VALOR"))<((Integer)osSem2.getValue("VALOR"))){
									osResult.setValue("VALOR", "CERT");
								}else{
									osResult.setValue("VALOR", "FALS");
								}
							}
						}
						if(oToken.image.equals("<>")){
							if(((ITipus)osSem1.getValue("TIPUS")).getNom().equals("LOGIC")){
								if(((String)osSem1.getValue("VALOR")).equals("FALS") && ((String)osSem2.getValue("VALOR")).equals("FALS")){
									osResult.setValue("VALOR", "FALS");
								}else if(((String)osSem1.getValue("VALOR")).equals("FALS") && !((String)osSem2.getValue("VALOR")).equals("CERT")){
									osResult.setValue("VALOR", "CERT");
								}else if(!((String)osSem1.getValue("VALOR")).equals("CERT") && ((String)osSem2.getValue("VALOR")).equals("FALS")){
									osResult.setValue("VALOR", "CERT");
								}else if(!((String)osSem1.getValue("VALOR")).equals("CERT") && !((String)osSem2.getValue("VALOR")).equals("CERT")){
									osResult.setValue("VALOR", "FALS");
								}								
							}else{
								if(((Integer)osSem1.getValue("VALOR"))!=((Integer)osSem2.getValue("VALOR"))){
									osResult.setValue("VALOR", "CERT");
								}else{
									osResult.setValue("VALOR", "FALS");
								}
							}
						}
						/* 	GC: Codigo para saber si se salta o no en el si*/
						if ( osResult.getValue("VALOR") != null ) {
							if ( ((String)osResult.getValue("VALOR")).compareTo("CERT") == 0 ) {
								CodigoEnsamblador.gc("# La condicion del si es cierta");
								CodigoEnsamblador.gc("li "+R+", 1");
							} else if ( ((String)osResult.getValue("VALOR")).compareTo("FALS") == 0 ) {
								CodigoEnsamblador.gc("# La condicion del si es falsa");
								CodigoEnsamblador.gc("li "+R+", 0");
							} else {
								CodigoEnsamblador.gc("# La condicion del si se ha evaluado pero no se ha obtenido un resultado valido. Argumentos estáticos. Default: CERT");
								CodigoEnsamblador.gc("li "+R+", 1");
							}
						}
						/*		FIN GC		*/
						//El segundo valor no es estático!
					} else if(((String)osSem1.getValue("ESTATIC")).equals("CERT") && ((String)osSem2.getValue("ESTATIC")).equals("FALS")){
							//Opera
							osResult.setValue("ESTATIC", "FALS"); //La expresión no será estática
							
							osResult.setValue("OPERAT","CERT"); //Atribut per comprovar tipus de pas de funcio
							
							if(oToken.image.equals("==")){
								if(((ITipus)osSem1.getValue("TIPUS")).getNom().equals("LOGIC")){
									if(((String)osSem1.getValue("VALOR")).equals("FALS") ){
										if (osSem2.getValue("REG") != null) {
											String R2 = Utils.getReg();
											CodigoEnsamblador.gc("li "+R2+", 0");	//Almacenamos el valor de OsSem1
											CodigoEnsamblador.gc("seq "+R+", "+R2+", "+(String)osSem2.getValue("REG")); //R es nuestro REGOPERACIONAL
										}
									} else if ( ((String)osSem1.getValue("VALOR")).equals("CERT") ){
										if (osSem2.getValue("REG") != null) {
											String R2 = Utils.getReg();
											CodigoEnsamblador.gc("li "+R2+", 1");	//Almacenamos el valor de OsSem1
											CodigoEnsamblador.gc("seq "+R+", "+R2+", "+(String)osSem2.getValue("REG")); //R es nuestro REGOPERACIONAL
										}
									}								
								}else{
									//Es sencer, no es logic
									if (osSem2.getValue("REG") != null && osSem1.getValue("VALOR") instanceof Integer) {
										String R2 = Utils.getReg();
										CodigoEnsamblador.gc("li "+R2+", "+((Integer)osSem1.getValue("VALOR")).intValue() );	//Almacenamos el valor de OsSem1
										CodigoEnsamblador.gc("seq "+R+", "+R2+", "+(String)osSem2.getValue("REG")); //R es nuestro REGOPERACIONAL
									}
								}
							}
							if(oToken.image.equals(">=")){
								if(((ITipus)osSem1.getValue("TIPUS")).getNom().equals("LOGIC")){
									if(((String)osSem1.getValue("VALOR")).equals("FALS") ){
										if (osSem2.getValue("REG") != null) {
											String R2 = Utils.getReg();
											CodigoEnsamblador.gc("li "+R2+", 0");	//Almacenamos el valor de OsSem1
											CodigoEnsamblador.gc("sge "+R+", "+R2+", "+(String)osSem2.getValue("REG")); //R es nuestro REGOPERACIONAL
										}
									} else if ( ((String)osSem1.getValue("VALOR")).equals("CERT") ){
										if (osSem2.getValue("REG") != null) {
											String R2 = Utils.getReg();
											CodigoEnsamblador.gc("li "+R2+", 1");	//Almacenamos el valor de OsSem1
											CodigoEnsamblador.gc("sge "+R+", "+R2+", "+(String)osSem2.getValue("REG")); //R es nuestro REGOPERACIONAL
										}
									}								
								}else{
									//Es sencer, no es logic
									if (osSem2.getValue("REG") != null && osSem1.getValue("VALOR") instanceof Integer) {
										String R2 = Utils.getReg();
										CodigoEnsamblador.gc("li "+R2+", "+((Integer)osSem1.getValue("VALOR")).intValue() );	//Almacenamos el valor de OsSem1
										CodigoEnsamblador.gc("sge "+R+", "+R2+", "+(String)osSem2.getValue("REG")); //R es nuestro REGOPERACIONAL
									}
								}
							}
							if(oToken.image.equals("<=")){
								if(((ITipus)osSem1.getValue("TIPUS")).getNom().equals("LOGIC")){
									if(((String)osSem1.getValue("VALOR")).equals("FALS") ){
										if (osSem2.getValue("REG") != null) {
											String R2 = Utils.getReg();
											CodigoEnsamblador.gc("li "+R2+", 0");	//Almacenamos el valor de OsSem1
											CodigoEnsamblador.gc("sle "+R+", "+R2+", "+(String)osSem2.getValue("REG")); //R es nuestro REGOPERACIONAL
										}
									} else if ( ((String)osSem1.getValue("VALOR")).equals("CERT") ){
										if (osSem2.getValue("REG") != null) {
											String R2 = Utils.getReg();
											CodigoEnsamblador.gc("li "+R2+", 1");	//Almacenamos el valor de OsSem1
											CodigoEnsamblador.gc("sle "+R+", "+R2+", "+(String)osSem2.getValue("REG")); //R es nuestro REGOPERACIONAL
										}
									}								
								}else{
									//Es sencer, no es logic
									if (osSem2.getValue("REG") != null && osSem1.getValue("VALOR") instanceof Integer) {
										String R2 = Utils.getReg();
										CodigoEnsamblador.gc("li "+R2+", "+((Integer)osSem1.getValue("VALOR")).intValue() );	//Almacenamos el valor de OsSem1
										CodigoEnsamblador.gc("sle "+R+", "+R2+", "+(String)osSem2.getValue("REG")); //R es nuestro REGOPERACIONAL
									}
								}
							}
							if(oToken.image.equals(">")){
								if(((ITipus)osSem1.getValue("TIPUS")).getNom().equals("LOGIC")){
									if(((String)osSem1.getValue("VALOR")).equals("FALS") ){
										if (osSem2.getValue("REG") != null) {
											String R2 = Utils.getReg();
											CodigoEnsamblador.gc("li "+R2+", 0");	//Almacenamos el valor de OsSem1
											CodigoEnsamblador.gc("sgt "+R+", "+R2+", "+(String)osSem2.getValue("REG")); //R es nuestro REGOPERACIONAL
										}
									} else if ( ((String)osSem1.getValue("VALOR")).equals("CERT") ){
										if (osSem2.getValue("REG") != null) {
											String R2 = Utils.getReg();
											CodigoEnsamblador.gc("li "+R2+", 1");	//Almacenamos el valor de OsSem1
											CodigoEnsamblador.gc("sgt "+R+", "+R2+", "+(String)osSem2.getValue("REG")); //R es nuestro REGOPERACIONAL
										}
									}								
								}else{
									//Es sencer, no es logic
									if (osSem2.getValue("REG") != null && osSem1.getValue("VALOR") instanceof Integer) {
										String R2 = Utils.getReg();
										CodigoEnsamblador.gc("li "+R2+", "+((Integer)osSem1.getValue("VALOR")).intValue() );	//Almacenamos el valor de OsSem1
										CodigoEnsamblador.gc("sgt "+R+", "+R2+", "+(String)osSem2.getValue("REG")); //R es nuestro REGOPERACIONAL
									}
								}
							}
							if(oToken.image.equals("<")){
								if(((ITipus)osSem1.getValue("TIPUS")).getNom().equals("LOGIC")){
									if(((String)osSem1.getValue("VALOR")).equals("FALS") ){
										if (osSem2.getValue("REG") != null) {
											String R2 = Utils.getReg();
											CodigoEnsamblador.gc("li "+R2+", 0");	//Almacenamos el valor de OsSem1
											CodigoEnsamblador.gc("slt "+R+", "+R2+", "+(String)osSem2.getValue("REG")); //R es nuestro REGOPERACIONAL
										}
									} else if ( ((String)osSem1.getValue("VALOR")).equals("CERT") ){
										if (osSem2.getValue("REG") != null) {
											String R2 = Utils.getReg();
											CodigoEnsamblador.gc("li "+R2+", 1");	//Almacenamos el valor de OsSem1
											CodigoEnsamblador.gc("slt "+R+", "+R2+", "+(String)osSem2.getValue("REG")); //R es nuestro REGOPERACIONAL
										}
									}								
								}else{
									//Es sencer, no es logic
									if (osSem2.getValue("REG") != null && osSem1.getValue("VALOR") instanceof Integer) {
										String R2 = Utils.getReg();
										CodigoEnsamblador.gc("li "+R2+", "+((Integer)osSem1.getValue("VALOR")).intValue() );	//Almacenamos el valor de OsSem1
										CodigoEnsamblador.gc("slt "+R+", "+R2+", "+(String)osSem2.getValue("REG")); //R es nuestro REGOPERACIONAL
									}
								}
							}
							if(oToken.image.equals("<>")){
								if(((ITipus)osSem1.getValue("TIPUS")).getNom().equals("LOGIC")){
									if(((String)osSem1.getValue("VALOR")).equals("FALS") ){
										if (osSem2.getValue("REG") != null) {
											String R2 = Utils.getReg();
											CodigoEnsamblador.gc("li "+R2+", 0");	//Almacenamos el valor de OsSem1
											CodigoEnsamblador.gc("sne "+R+", "+R2+", "+(String)osSem2.getValue("REG")); //R es nuestro REGOPERACIONAL
										}
									} else if ( ((String)osSem1.getValue("VALOR")).equals("CERT") ){
										if (osSem2.getValue("REG") != null) {
											String R2 = Utils.getReg();
											CodigoEnsamblador.gc("li "+R2+", 1");	//Almacenamos el valor de OsSem1
											CodigoEnsamblador.gc("sne "+R+", "+R2+", "+(String)osSem2.getValue("REG")); //R es nuestro REGOPERACIONAL
										}
									}								
								}else{
									//Es sencer, no es logic
									if (osSem2.getValue("REG") != null && osSem1.getValue("VALOR") instanceof Integer) {
										String R2 = Utils.getReg();
										CodigoEnsamblador.gc("li "+R2+", "+((Integer)osSem1.getValue("VALOR")).intValue() );	//Almacenamos el valor de OsSem1
										CodigoEnsamblador.gc("sne "+R+", "+R2+", "+(String)osSem2.getValue("REG")); //R es nuestro REGOPERACIONAL
									}
								}
							}
						
					//El primero no es estático!
					} else if(((String)osSem1.getValue("ESTATIC")).equals("FALS") && ((String)osSem2.getValue("ESTATIC")).equals("CERT")){
						//Opera
						osResult.setValue("ESTATIC", "FALS"); //La expresión no será estática
						
						osResult.setValue("OPERAT","CERT"); //Atribut per comprovar tipus de pas de funcio
						
						if(oToken.image.equals("==")){
							if(((ITipus)osSem1.getValue("TIPUS")).getNom().equals("LOGIC")){
								if(((String)osSem2.getValue("VALOR")).equals("FALS") ){
									if (osSem1.getValue("REG") != null) {
										String R2 = Utils.getReg();
										CodigoEnsamblador.gc("li "+R2+", 0");	//Almacenamos el valor de OsSem1
										CodigoEnsamblador.gc("seq "+R+", "+(String)osSem1.getValue("REG")+", "+R2); //R es nuestro REGOPERACIONAL
									}
								} else if ( ((String)osSem2.getValue("VALOR")).equals("CERT") ){
									if (osSem1.getValue("REG") != null) {
										String R2 = Utils.getReg();
										CodigoEnsamblador.gc("li "+R2+", 1");	//Almacenamos el valor de OsSem1
										CodigoEnsamblador.gc("seq "+R+", "+(String)osSem1.getValue("REG")+", "+R2); //R es nuestro REGOPERACIONAL
									}
								}								
							}else{
								//Es sencer, no es logic
								if (osSem1.getValue("REG") != null && osSem2.getValue("VALOR") instanceof Integer) {
									String R2 = Utils.getReg();
									CodigoEnsamblador.gc("li "+R2+", "+((Integer)osSem2.getValue("VALOR")).intValue() );	//Almacenamos el valor de OsSem1
									CodigoEnsamblador.gc("seq "+R+", "+(String)osSem1.getValue("REG")+", "+R2); //R es nuestro REGOPERACIONAL
								}
							}
						}
						if(oToken.image.equals(">=")){
							if(((ITipus)osSem1.getValue("TIPUS")).getNom().equals("LOGIC")){
								if(((String)osSem2.getValue("VALOR")).equals("FALS") ){
									if (osSem1.getValue("REG") != null) {
										String R2 = Utils.getReg();
										CodigoEnsamblador.gc("li "+R2+", 0");	//Almacenamos el valor de OsSem1
										CodigoEnsamblador.gc("sge "+R+", "+(String)osSem1.getValue("REG")+", "+R2); //R es nuestro REGOPERACIONAL
									}
								} else if ( ((String)osSem2.getValue("VALOR")).equals("CERT") ){
									if (osSem1.getValue("REG") != null) {
										String R2 = Utils.getReg();
										CodigoEnsamblador.gc("li "+R2+", 1");	//Almacenamos el valor de OsSem1
										CodigoEnsamblador.gc("sge "+R+", "+(String)osSem1.getValue("REG")+", "+R2); //R es nuestro REGOPERACIONAL
									}
								}								
							}else{
								//Es sencer, no es logic
								if (osSem1.getValue("REG") != null && osSem2.getValue("VALOR") instanceof Integer) {
									String R2 = Utils.getReg();
									CodigoEnsamblador.gc("li "+R2+", "+((Integer)osSem2.getValue("VALOR")).intValue() );	//Almacenamos el valor de OsSem1
									CodigoEnsamblador.gc("sge "+R+", "+(String)osSem1.getValue("REG")+", "+(R2)); //R es nuestro REGOPERACIONAL
								}
							}
						}
						if(oToken.image.equals("<=")){
							if(((ITipus)osSem1.getValue("TIPUS")).getNom().equals("LOGIC")){
								if(((ITipus)osSem1.getValue("TIPUS")).getNom().equals("LOGIC")){
									if(((String)osSem2.getValue("VALOR")).equals("FALS") ){
										if (osSem1.getValue("REG") != null) {
											String R2 = Utils.getReg();
											CodigoEnsamblador.gc("li "+R2+", 0");	//Almacenamos el valor de OsSem1
											CodigoEnsamblador.gc("sle "+R+", "+(String)osSem1.getValue("REG")+", "+R2); //R es nuestro REGOPERACIONAL
										}
									} else if ( ((String)osSem2.getValue("VALOR")).equals("CERT") ){
										if (osSem1.getValue("REG") != null) {
											String R2 = Utils.getReg();
											CodigoEnsamblador.gc("li "+R2+", 1");	//Almacenamos el valor de OsSem1
											CodigoEnsamblador.gc("sle "+R+", "+(String)osSem1.getValue("REG")+", "+R2); //R es nuestro REGOPERACIONAL
										}
									}								
								}else{
									//Es sencer, no es logic
									if (osSem1.getValue("REG") != null && osSem2.getValue("VALOR") instanceof Integer) {
										String R2 = Utils.getReg();
										CodigoEnsamblador.gc("li "+R2+", "+((Integer)osSem2.getValue("VALOR")).intValue() );	//Almacenamos el valor de OsSem1
										CodigoEnsamblador.gc("sle "+R+", "+(String)osSem1.getValue("REG")+", "+R2); //R es nuestro REGOPERACIONAL
									}
								}
							}
						}
						if(oToken.image.equals(">")){
							if(((ITipus)osSem1.getValue("TIPUS")).getNom().equals("LOGIC")){
								if(((String)osSem2.getValue("VALOR")).equals("FALS") ){
									if (osSem1.getValue("REG") != null) {
										String R2 = Utils.getReg();
										CodigoEnsamblador.gc("li "+R2+", 0");	//Almacenamos el valor de OsSem1
										CodigoEnsamblador.gc("sgt "+R+", "+(String)osSem1.getValue("REG")+", "+R2); //R es nuestro REGOPERACIONAL
									}
								} else if ( ((String)osSem2.getValue("VALOR")).equals("CERT") ){
									if (osSem1.getValue("REG") != null) {
										String R2 = Utils.getReg();
										CodigoEnsamblador.gc("li "+R2+", 1");	//Almacenamos el valor de OsSem1
										CodigoEnsamblador.gc("sgt "+R+", "+(String)osSem1.getValue("REG")+", "+R2); //R es nuestro REGOPERACIONAL
									}
								}								
							}else{
								//Es sencer, no es logic
								if (osSem1.getValue("REG") != null && osSem2.getValue("VALOR") instanceof Integer) {
									String R2 = Utils.getReg();
									CodigoEnsamblador.gc("li "+R2+", "+((Integer)osSem2.getValue("VALOR")).intValue() );	//Almacenamos el valor de OsSem1
									CodigoEnsamblador.gc("sgt "+R+", "+(String)osSem1.getValue("REG")+", "+R2); //R es nuestro REGOPERACIONAL
								}
							}
						}
						if(oToken.image.equals("<")){
							if(((ITipus)osSem1.getValue("TIPUS")).getNom().equals("LOGIC")){
								if(((String)osSem2.getValue("VALOR")).equals("FALS") ){
									if (osSem1.getValue("REG") != null) {
										String R2 = Utils.getReg();
										CodigoEnsamblador.gc("li "+R2+", 0");	//Almacenamos el valor de OsSem1
										CodigoEnsamblador.gc("slt "+R+", "+(String)osSem1.getValue("REG")+", "+R2); //R es nuestro REGOPERACIONAL
									}
								} else if ( ((String)osSem2.getValue("VALOR")).equals("CERT") ){
									if (osSem1.getValue("REG") != null) {
										String R2 = Utils.getReg();
										CodigoEnsamblador.gc("li "+R2+", 1");	//Almacenamos el valor de OsSem1
										CodigoEnsamblador.gc("slt "+R+", "+(String)osSem1.getValue("REG")+", "+R2); //R es nuestro REGOPERACIONAL
									}
								}								
							}else{
								//Es sencer, no es logic
								if (osSem1.getValue("REG") != null && osSem2.getValue("VALOR") instanceof Integer) {
									String R2 = Utils.getReg();
									CodigoEnsamblador.gc("li "+R2+", "+((Integer)osSem2.getValue("VALOR")).intValue() );	//Almacenamos el valor de OsSem1
									CodigoEnsamblador.gc("slt "+R+", "+(String)osSem1.getValue("REG")+", "+R2); //R es nuestro REGOPERACIONAL
								}
							}
						}
						if(oToken.image.equals("<>")){
							if(((ITipus)osSem1.getValue("TIPUS")).getNom().equals("LOGIC")){
								if(((String)osSem2.getValue("VALOR")).equals("FALS") ){
									if (osSem1.getValue("REG") != null) {
										String R2 = Utils.getReg();
										CodigoEnsamblador.gc("li "+R2+", 0");	//Almacenamos el valor de OsSem1
										CodigoEnsamblador.gc("sne "+R+", "+(String)osSem1.getValue("REG")+", "+R2); //R es nuestro REGOPERACIONAL
									}
								} else if ( ((String)osSem2.getValue("VALOR")).equals("CERT") ){
									if (osSem1.getValue("REG") != null) {
										String R2 = Utils.getReg();
										CodigoEnsamblador.gc("li "+R2+", 1");	//Almacenamos el valor de OsSem1
										CodigoEnsamblador.gc("sne "+R+", "+(String)osSem1.getValue("REG")+", "+R2); //R es nuestro REGOPERACIONAL
									}
								}								
							}else{
								//Es sencer, no es logic
								if (osSem1.getValue("REG") != null && osSem2.getValue("VALOR") instanceof Integer) {
									String R2 = Utils.getReg();
									CodigoEnsamblador.gc("li "+R2+", "+((Integer)osSem2.getValue("VALOR")).intValue() );	//Almacenamos el valor de OsSem1
									CodigoEnsamblador.gc("sne "+R+", "+(String)osSem1.getValue("REG")+", "+R2); //R es nuestro REGOPERACIONAL
								}
							}
						}
						
						//El primero&segundo no es estático!
					} else if(((String)osSem1.getValue("ESTATIC")).equals("FALS") && ((String)osSem2.getValue("ESTATIC")).equals("FALS")){
						//Opera
						osResult.setValue("ESTATIC", "FALS"); //La expresión no será estática
						
						osResult.setValue("OPERAT","CERT"); //Atribut per comprovar tipus de pas de funcio
						
						if(oToken.image.equals("==")){
							if (osSem1.getValue("REG") != null && osSem2.getValue("REG") != null) {
								CodigoEnsamblador.gc("seq "+R+", "+(String)osSem1.getValue("REG")+", "+(String)osSem2.getValue("REG")); //R es nuestro REGOPERACIONAL
							}						
						}
						if(oToken.image.equals(">=")){
							if (osSem1.getValue("REG") != null && osSem2.getValue("REG") != null) {
								CodigoEnsamblador.gc("sge "+R+", "+(String)osSem1.getValue("REG")+", "+(String)osSem2.getValue("REG")); //R es nuestro REGOPERACIONAL
							}						
						}
						if(oToken.image.equals("<=")){
							if (osSem1.getValue("REG") != null && osSem2.getValue("REG") != null) {
								CodigoEnsamblador.gc("sle "+R+", "+(String)osSem1.getValue("REG")+", "+(String)osSem2.getValue("REG")); //R es nuestro REGOPERACIONAL
							}						
						}
						if(oToken.image.equals(">")){
							if (osSem1.getValue("REG") != null && osSem2.getValue("REG") != null) {
								CodigoEnsamblador.gc("sgt "+R+", "+(String)osSem1.getValue("REG")+", "+(String)osSem2.getValue("REG")); //R es nuestro REGOPERACIONAL
							}						
						}
						if(oToken.image.equals("<")){
							if (osSem1.getValue("REG") != null && osSem2.getValue("REG") != null) {
								CodigoEnsamblador.gc("slt "+R+", "+(String)osSem1.getValue("REG")+", "+(String)osSem2.getValue("REG")); //R es nuestro REGOPERACIONAL
							}						
						}
						if(oToken.image.equals("<>")){
							if (osSem1.getValue("REG") != null && osSem2.getValue("REG") != null) {
								CodigoEnsamblador.gc("sne "+R+", "+(String)osSem1.getValue("REG")+", "+(String)osSem2.getValue("REG")); //R es nuestro REGOPERACIONAL
							}						
						}
					//Aquí en teoria no entrará nunca ya que se cumplirá una condicion antes
					} else {
						//No puedo operar, retorno tipo LOGIC y un valor de CERT por defecto												
						osResult.setValue("VALOR", "CERT");
						CodigoEnsamblador.gc("# La condicion del si no se ha evaluado. Argumentros estáticos. Default: CERT");
						CodigoEnsamblador.gc("li "+R+", 1");
					}				
				}else{
					//Alguno de los dos no es SENCER, error
					Singleton_Error.getInstance().writeErrorSem(47, null, oToken);
				}
					
			}
			
		}
		return osResult;
	}
	
	private static Semantic calcularLogic(Semantic s1,Semantic osOper,Semantic s2,Token oToken){
		
		//D: Semantic de Resultado
		Semantic osResult=new Semantic();
		
		//D: Miro si es estática
		//D:	si es estática
		//D:		- Miro que los tipos de los dos semantics sean LOGIC
		//D:		- Calculo los resultados
		//D:	si no es estática
		//D:		- Miro que los tipos de los dos semantics sean LOGIC
		//D:		- Retorno semantica con valor de ESTATICA a FALS (y con VALOR a CERT)
		
		//D:Asigno valores iniciales, que luego iré modificando
		osResult.setValue("TIPUS",Utils.aTipusIndefinit);
		osResult.setValue("ESTATIC", "FALS");
		osResult.setValue("VALOR", "CERT");
		osResult.setValue("OPERAT","CERT"); //Atribut per comprovar tipus de pas de funcio
		if(((String)s1.getValue("ESTATIC")).equals("CERT")&&((String)s2.getValue("ESTATIC")).equals("CERT")){
			//D:Las dos son estáticas
			osResult.setValue("ESTATIC", "CERT");
			if(((ITipus)s1.getValue("TIPUS")).equals(Utils.aTipusLogic)&&((ITipus)s2.getValue("TIPUS")).equals(Utils.aTipusLogic)){
				//D:Los dos son LOGIC, puedo operar
				osResult.setValue("TIPUS",Utils.aTipusLogic);
				if(((String)osOper.getValue("OPER")).equals("OP_AND")){
					System.out.println("Asem_Exp.calcularLogic(): Executiting static AND. First&Second operator static.");
					if(((String)s1.getValue("VALOR")).equals("CERT") && ((String)s2.getValue("VALOR")).equals("CERT")){
						osResult.setValue("VALOR","CERT");
						
						/*  GC: DEBEMOS GENERAR EL CÓDIGO PARA REALIZAR LA AND ESTATICA 	*/
						String R = Utils.getReg();
						CodigoEnsamblador.gc("# Codigo de expresion para realizar una and estatica. Sabemos que el resultado es cierto");
						//Cargamos en el registro el contenido de la dirección donde se encuentra el primer ID
						CodigoEnsamblador.gc("li "+R+", 1");
						CodigoEnsamblador.gc("# Fin codigo");
						//---->Utils.setReg(R);
						//Almacenamos en el semantic el registro que contiene el resultado de la operación
						osResult.setValue("REG", R);
						/*						FIN CODIGO							*/
					}else{
						osResult.setValue("VALOR","FALS");
						
						/*  GC: DEBEMOS GENERAR EL CÓDIGO PARA REALIZAR LA AND ESTATICA 	*/
						String R = Utils.getReg();
						CodigoEnsamblador.gc("# Codigo de expresion para realizar una and estatica. Sabemos que el resultado es falso");
						//Cargamos en el registro el contenido de la dirección donde se encuentra el primer ID
						CodigoEnsamblador.gc("li "+R+", 0");
						CodigoEnsamblador.gc("# Fin codigo");
						//---->Utils.setReg(R);
						//Almacenamos en el semantic el registro que contiene el resultado de la operación
						osResult.setValue("REG", R);
						/*						FIN CODIGO							*/
						
					}
				System.out.println("Asem_Exp.calcularLogic(): Exititing static AND. First&Second operator static.");
				} else if(((String)osOper.getValue("OPER")).equals("OP_OR")){
					System.out.println("Asem_Exp.calcularLogic(): Executiting static OR. First&Second operator static.");
					if(((String)s1.getValue("VALOR")).equals("CERT")||((String)s2.getValue("VALOR")).equals("CERT")){
						osResult.setValue("VALOR","CERT");
						/*  GC: DEBEMOS GENERAR EL CÓDIGO PARA REALIZAR LA OR ESTATICA 	*/
						String R = Utils.getReg();
						CodigoEnsamblador.gc("# Codigo de expresion para realizar una or estatica. Sabemos que el resultado es cierto");
						//Cargamos en el registro el contenido de la dirección donde se encuentra el primer ID
						CodigoEnsamblador.gc("li "+R+", 1");
						CodigoEnsamblador.gc("# Fin codigo");
						//---->Utils.setReg(R);
						//Almacenamos en el semantic el registro que contiene el resultado de la operación
						osResult.setValue("REG", R);
						/*						FIN CODIGO							*/
					}else{
						osResult.setValue("VALOR","FALS");
						/*  GC: DEBEMOS GENERAR EL CÓDIGO PARA REALIZAR LA OR ESTATICA 	*/
						String R = Utils.getReg();
						CodigoEnsamblador.gc("# Codigo de expresion para realizar una or estatica. Sabemos que el resultado es falso");
						//Cargamos en el registro el contenido de la dirección donde se encuentra el primer ID
						CodigoEnsamblador.gc("li "+R+", 0");
						CodigoEnsamblador.gc("# Fin codigo");
						//----->Utils.setReg(R);
						//Almacenamos en el semantic el registro que contiene el resultado de la operación
						osResult.setValue("REG", R);
						/*						FIN CODIGO							*/
					}
					System.out.println("Asem_Exp.calcularLogic(): Exititing static OR. First&Second operator static.");
				}
				//EN TEORIA, EN ESTE NOT NO ENTRA NUNCA!!!! YA LO HACE EN EL CALCULA_UNARIO, NO PUEDE HABER UN REG1 NOT REG2
				else if(((String)osOper.getValue("OPER")).equals("OP_NOT")){
					System.out.println("Asem_Exp.calcularLogic(): Executiting static NOT. First&Second operator static.");
					if(((String)s1.getValue("VALOR")).equals("CERT")){
						osResult.setValue("VALOR","FALS");
						/*  GC: DEBEMOS GENERAR EL CÓDIGO PARA REALIZAR LA NOT ESTATICA 	*/
						String R = Utils.getReg();
						CodigoEnsamblador.gc("# Codigo de expresion para realizar una not estatica. Sabemos que el resultado es falso");
						//Cargamos en el registro el contenido de la dirección donde se encuentra el primer ID
						CodigoEnsamblador.gc("li "+R+", 0");
						CodigoEnsamblador.gc("# Fin codigo");
						//---->Utils.setReg(R);
						//Almacenamos en el semantic el registro que contiene el resultado de la operación
						osResult.setValue("REG", R);
						/*						FIN CODIGO							*/
					}else{
						osResult.setValue("VALOR","CERT");
						/*  GC: DEBEMOS GENERAR EL CÓDIGO PARA REALIZAR LA NOT ESTATICA 	*/
						String R = Utils.getReg();
						CodigoEnsamblador.gc("# Codigo de expresion para realizar una not estatica. Sabemos que el resultado es cierto");
						//Cargamos en el registro el contenido de la dirección donde se encuentra el primer ID
						CodigoEnsamblador.gc("li "+R+", 1");
						CodigoEnsamblador.gc("# Fin codigo");
						//---->Utils.setReg(R);
						//Almacenamos en el semantic el registro que contiene el resultado de la operación
						osResult.setValue("REG", R);
						/*						FIN CODIGO							*/
					}
					System.out.println("Asem_Exp.calcularLogic(): Executiting static NOT. First&Second operator static.");
				}
			}else{
				//D:Al menos uno de los dos no es LOGIC
				Singleton_Error.getInstance().writeErrorSem(10, null, oToken);
				//D: Dejo en osResult los valores por defecto
			}
		}else{
			//D: Por lo menos una de las dos no es estática
			if(((ITipus)s1.getValue("TIPUS")).equals(Utils.aTipusLogic)&&((ITipus)s2.getValue("TIPUS")).equals(Utils.aTipusLogic)){
				//D: Los dos son LOGIC, poría operar
				osResult.setValue("TIPUS",Utils.aTipusLogic);
				//D: Dejo en osResult los valores por defecto
				
				if(((String)osOper.getValue("OPER")).equals("OP_AND")){
					
					if(((String)s1.getValue("ESTATIC")).equals("CERT") && ((String)s2.getValue("ESTATIC")).equals("FALS")){
						System.out.println("Asem_Exp.calcularLogic(): Executiting not static AND. Second operator not static.");
						/*  GC: DEBEMOS GENERAR EL CÓDIGO PARA REALIZAR LA AND NO ESTATICA 	*/
						String R = Utils.getReg();
						CodigoEnsamblador.gc("# Codigo de expresion para realizar una and no estatica");
						//Realizamos la and
						if ( ((String)s1.getValue("VALOR")).compareTo("CERT") == 0 ) {
							CodigoEnsamblador.gc("andi "+R+", "+(String)s2.getValue("REG")+", 1");
						} else {
							CodigoEnsamblador.gc("andi "+R+", "+(String)s2.getValue("REG")+", 0");
						}
						CodigoEnsamblador.gc("# Fin codigo");
						//----->Utils.setReg(R);
						//Almacenamos en el semantic el registro que contiene el resultado de la operación
						osResult.setValue("REG", R);
						/*						FIN CODIGO							*/
						System.out.println("Asem_Exp.calcularLogic(): Exiting not static AND. Second operator not static.");
					} else if(((String)s1.getValue("ESTATIC")).equals("FALS")&&((String)s2.getValue("ESTATIC")).equals("CERT")){
						System.out.println("Asem_Exp.calcularLogic(): Executiting not static AND. First operator not static.");
						/*  GC: DEBEMOS GENERAR EL CÓDIGO PARA REALIZAR LA AND NO ESTATICA 	*/
						String R = Utils.getReg();
						CodigoEnsamblador.gc("# Codigo de expresion para realizar una and no estatica");
						//Realizamos la and
						if ( ((String)s2.getValue("VALOR")).compareTo("CERT") == 0 ) {
							CodigoEnsamblador.gc("andi "+R+", "+(String)s1.getValue("REG")+", 1");
						} else {
							CodigoEnsamblador.gc("andi "+R+", "+(String)s1.getValue("REG")+", 0");
						}
						CodigoEnsamblador.gc("# Fin codigo");
						//---->Utils.setReg(R);
						//Almacenamos en el semantic el registro que contiene el resultado de la operación
						osResult.setValue("REG", R);
						/*						FIN CODIGO							*/
						System.out.println("Asem_Exp.calcularLogic(): Exititing not static AND. First operator not static.");
					} else if(((String)s1.getValue("ESTATIC")).equals("FALS")&&((String)s2.getValue("ESTATIC")).equals("FALS")){
						System.out.println("Asem_Exp.calcularLogic(): Executing not static AND. First&Second operator not static.");
						/*  GC: DEBEMOS GENERAR EL CÓDIGO PARA REALIZAR LA AND NO ESTATICA 	*/
						String R1 = Utils.getReg(); R1 = (String)s1.getValue("REG");
						String R2 = Utils.getReg(); R2 = (String)s2.getValue("REG");
						CodigoEnsamblador.gc("# Codigo de expresion para realizar una and no estatica");
						//Realizamos la and
						CodigoEnsamblador.gc("and "+R1+", "+R1+", "+R2);
						CodigoEnsamblador.gc("# Fin codigo");
						Utils.setReg(R2);
						//---->Utils.setReg(R1);
						//Almacenamos en el semantic el registro que contiene el resultado de la operación
						osResult.setValue("REG", R1);
						/*						FIN CODIGO							*/
						System.out.println("Asem_Exp.calcularLogic(): Exiting not static AND. First&Second operator not static.");
					}
					
				} else if(((String)osOper.getValue("OPER")).equals("OP_OR")){
					if(((String)s1.getValue("ESTATIC")).equals("CERT") && ((String)s2.getValue("ESTATIC")).equals("FALS")){
						System.out.println("Asem_Exp.calcularLogic(): Executing not static OR. Second operator not static.");
						/*  GC: DEBEMOS GENERAR EL CÓDIGO PARA REALIZAR LA OR NO ESTATICA 	*/
						String R = Utils.getReg();
						CodigoEnsamblador.gc("# Codigo de expresion para realizar una and no estatica");
						//Realizamos la or
						if ( ((String)s1.getValue("VALOR")).compareTo("CERT") == 0 ) {
							CodigoEnsamblador.gc("ori "+R+", "+(String)s2.getValue("REG")+", 1");
						} else {
							CodigoEnsamblador.gc("ori "+R+", "+(String)s2.getValue("REG")+", 0");
						}
						CodigoEnsamblador.gc("# Fin codigo");
						//---->Utils.setReg(R);
						//Almacenamos en el semantic el registro que contiene el resultado de la operación
						osResult.setValue("REG", R);
						/*						FIN CODIGO							*/
						System.out.println("Asem_Exp.calcularLogic(): Exiting not static OR. Second operator not static.");
					} else if(((String)s1.getValue("ESTATIC")).equals("FALS")&&((String)s2.getValue("ESTATIC")).equals("CERT")){
						System.out.println("Asem_Exp.calcularLogic(): Executing not static OR. First operator not static.");
						/*  GC: DEBEMOS GENERAR EL CÓDIGO PARA REALIZAR LA AND NO ESTATICA 	*/
						String R = Utils.getReg();
						CodigoEnsamblador.gc("# Codigo de expresion para realizar una or no estatica");
						//Realizamos la or
						if ( ((String)s2.getValue("VALOR")).compareTo("CERT") == 0 ) {
							CodigoEnsamblador.gc("ori "+R+", "+(String)s1.getValue("REG")+", 1");
						} else {
							CodigoEnsamblador.gc("ori "+R+", "+(String)s1.getValue("REG")+", 0");
						}
						CodigoEnsamblador.gc("# Fin codigo");
						//---->Utils.setReg(R);
						//Almacenamos en el semantic el registro que contiene el resultado de la operación
						osResult.setValue("REG", R);
						/*						FIN CODIGO							*/
						System.out.println("Asem_Exp.calcularLogic(): Exiting not static OR. First operator not static.");
					} else if(((String)s1.getValue("ESTATIC")).equals("FALS")&&((String)s2.getValue("ESTATIC")).equals("FALS")){
						System.out.println("Asem_Exp.calcularLogic(): Executing not static OR. First&Second operator not static.");
						/*  GC: DEBEMOS GENERAR EL CÓDIGO PARA REALIZAR LA OR NO ESTATICA 	*/
						String R1 = Utils.getReg(); R1 = (String)s1.getValue("REG");
						String R2 = Utils.getReg(); R2 = (String)s2.getValue("REG");
						CodigoEnsamblador.gc("# Codigo de expresion para realizar una and no estatica");
						//Realizamos la or
						CodigoEnsamblador.gc("or "+R1+", "+R1+", "+R2);
						CodigoEnsamblador.gc("# Fin codigo");
						Utils.setReg(R2);
						//---->Utils.setReg(R1);
						//Almacenamos en el semantic el registro que contiene el resultado de la operación
						osResult.setValue("REG", R1);
						/*						FIN CODIGO							*/
						System.out.println("Asem_Exp.calcularLogic(): Exiting not static OR. First&Second operator not static.");
					}
					
				}
				//NO PUEDE HABER UNA NOT...
//					else if(((String)osOper.getValue("OPER")).equals("OP_NOT")){
//					
//				}
					
			}else{
				//D: Al menos uno de los dos no es LOGIC, no podría operar
				Singleton_Error.getInstance().writeErrorSem(10, null, oToken);
				//D: Dejo en osResult los valores por defecto
			}			
		}
		
		return osResult;
	}
	
	private static Semantic calcularSencer(Semantic s1,Semantic osOper,Semantic s2,Token oToken){
		//TODO: Ojo! Controlar que pasa si quieren multiplicar, dividir,... una variable con un entero, o dos enteros
		
		//D: Semantic de Resultado
		Semantic osResult=new Semantic();
		
		//D: Miro si es estática
		//D:	si es estática
		//D:		- Miro que los tipos de los dos semantics sean SENCER
		//D:		- Calculo los resultados
		//D:	si no es estática
		//D:		- Miro que los tipos de los dos semantics sean SENCER
		//D:		- Retorno semantica con valor de 1 (valor que puede comprometer poco el código)
		
		//D: Asigno valores iniciales, que luego iré modificando
		osResult.setValue("TIPUS",Utils.aTipusIndefinit);
		osResult.setValue("ESTATIC", "FALS");
		osResult.setValue("VALOR", new Integer(1));
		//¿?¿?¿?
		//osResult.setValue("REG", s1.getValue("REG"));
		
		if(((String)s1.getValue("ESTATIC")).equals("CERT")&&((String)s2.getValue("ESTATIC")).equals("CERT")){
			//D: Las dos son estáticas
			osResult.setValue("ESTATIC", "CERT");
			if(((ITipus)s1.getValue("TIPUS")).equals(Utils.aTipusSencer)&&((ITipus)s2.getValue("TIPUS")).equals(Utils.aTipusSencer)){
				//D: Los dos son SENCER, puedo operar
				osResult.setValue("TIPUS",Utils.aTipusSencer);
				if(((String)osOper.getValue("OPER")).equals("OP_MAS")){
					System.out.println("Asem_Exp.calcularSencer(): Executing static add. First&Second operator static.");
					osResult.setValue("VALOR",(Integer)s1.getValue("VALOR")+(Integer)s2.getValue("VALOR"));
					
					/*  GC: DEBEMOS GENERAR EL CÓDIGO PARA REALIZAR LA SUMA ESTATICA 	*/
					String R = Utils.getReg();
					CodigoEnsamblador.gc("# Codigo de expresion para realizar una suma estatica");
//					//Cargamos en el registro el contenido de la dirección donde se encuentra el primer ID
//					CodigoEnsamblador.gc("li "+R+", "+((Integer)s1.getValue("VALOR")).intValue());
//					//Realizamos la suma
//					CodigoEnsamblador.gc("addi "+R+", "+R+", "+((Integer)s2.getValue("VALOR")).intValue());
					//Con que ya sabemos el resultado no necesitamos hacer la suma
					CodigoEnsamblador.gc("li "+R+", "+((Integer)osResult.getValue("VALOR")).intValue());
					CodigoEnsamblador.gc("# Fin codigo");
					//---->Utils.setReg(R);
					//Almacenamos en el semantic el registro que contiene el resultado de la operación
					osResult.setValue("REG", R);
					/*						FIN CODIGO							*/
					System.out.println("Asem_Exp.calcularSencer(): Exiting static add. First&Second operator static.");
				}
				else if(((String)osOper.getValue("OPER")).equals("OP_MENOS")){
					osResult.setValue("VALOR",(Integer)s1.getValue("VALOR")-(Integer)s2.getValue("VALOR"));
					System.out.println("Asem_Exp.calcularSencer(): Executing static sub. First&Second operator static.");
					/*  GC: DEBEMOS GENERAR EL CÓDIGO PARA REALIZAR LA SUMA ESTATICA 	*/
					String R1 = Utils.getReg();
//					String R2 = Utils.getReg();
					CodigoEnsamblador.gc("# Codigo de expresion para realizar una resta estatica");
//					//Cargamos en el registro el contenido de la dirección donde se encuentra el primer ID
//					CodigoEnsamblador.gc("li "+R1+", "+((Integer)s1.getValue("VALOR")).intValue());
//					//Cargamos en el registro el contenido de la dirección donde se encuentra el segundo ID
//					CodigoEnsamblador.gc("li "+R2+", "+((Integer)s2.getValue("VALOR")).intValue());
//					//Realizamos la suma
//					CodigoEnsamblador.gc("sub "+R1+", "+R1+", "+R2);
					//Con que ya sabemos el resultado no necesitamos hacer la resta
					CodigoEnsamblador.gc("li "+R1+", "+((Integer)osResult.getValue("VALOR")).intValue());
					CodigoEnsamblador.gc("# Fin codigo");
//					Utils.setReg(R2);
					//---->Utils.setReg(R1);
					//Almacenamos en el semantic el registro que contiene el resultado de la operación
					osResult.setValue("REG", R1);
					/*						FIN CODIGO							*/
					System.out.println("Asem_Exp.calcularSencer(): Exititing static sub. First&Second operator static.");
				}
				else if(((String)osOper.getValue("OPER")).equals("OP_DIV")){
					System.out.println("Asem_Exp.calcularSencer(): Executiting static division. First&Second operator static.");
					//Como excepción cojemos el registro aquí en vez de hacerlo en el bloque de GC
					String R1 = Utils.getReg();
					//D: Control de división por 0
					if((Integer)s2.getValue("VALOR")==0){
						Singleton_Error.getInstance().writeErrorSem(35, null, oToken);
						//D: Dejo el valor por defecto, 1
						//Para la generación de código debo generar el código normalmente para que no pete posteriormente. Aun así haré la comprobación para lanzar un error
						//Comprobamos que el segundo registro no sea cero
//						CodigoEnsamblador.gc("b "+(String)s2.getValue("VALOR")+", $0, ERROR2");
						CodigoEnsamblador.gc("# Saltamos obligatoriamente ya que estáticamente sabemos que el operando derecho de la division es 0");
						CodigoEnsamblador.gc("b ERROR2");
						//Debemos devolver un valor de un registro, sino en el checkAssign nos dirá que el registro es nulo.
						//Lo pasamos aunque sabemos que está vacío. No se comprobará ya que sabemos seguro que en este punto del código se salta al error
						osResult.setValue("REG", R1);
					}else{
						osResult.setValue("VALOR",(Integer)s1.getValue("VALOR")/(Integer)s2.getValue("VALOR"));
						
						/*  GC: DEBEMOS GENERAR EL CÓDIGO PARA REALIZAR LA DIV ESTATICA 	*/
						CodigoEnsamblador.gc("# Codigo de expresion para realizar una division estatica");
						//Con que ya sabemos el resultado no necesitamos hacer la div
						CodigoEnsamblador.gc("li "+R1+", "+((Integer)osResult.getValue("VALOR")).intValue());
						CodigoEnsamblador.gc("# Fin codigo");
						//Almacenamos en el semantic el registro que contiene el resultado de la operación
						osResult.setValue("REG", R1);
						/*						FIN CODIGO							*/
						System.out.println("Asem_Exp.calcularSencer(): Exiting static division. First&Second operator static.");
					}
					
				}
				else if(((String)osOper.getValue("OPER")).equals("OP_MUL")){
					System.out.println("Asem_Exp.calcularSencer(): Executiting static mult. First&Second operator static.");
					osResult.setValue("VALOR",(Integer)s1.getValue("VALOR")*(Integer)s2.getValue("VALOR"));
					/*  GC: DEBEMOS GENERAR EL CÓDIGO PARA REALIZAR LA MULT ESTATICA 	*/
					String R1 = Utils.getReg();
//					String R2 = Utils.getReg();
					CodigoEnsamblador.gc("# Codigo de expresion para realizar una multiplicion estatica");
					//Cargamos en el registro el contenido de la dirección donde se encuentra el primer ID
//					CodigoEnsamblador.gc("li "+R1+", "+((Integer)s1.getValue("VALOR")).intValue());
//					//Cargamos en el registro el contenido de la dirección donde se encuentra el segundo ID
//					CodigoEnsamblador.gc("li "+R2+", "+((Integer)s2.getValue("VALOR")).intValue());
//					//Realizamos la suma
//					CodigoEnsamblador.gc("mul "+R1+", "+R1+", "+R2);
					//Con que ya sabemos el resultado no necesitamos hacer la suma
					CodigoEnsamblador.gc("li "+R1+", "+((Integer)osResult.getValue("VALOR")).intValue());
					CodigoEnsamblador.gc("# Fin codigo");
//					Utils.setReg(R2);
					//----->Utils.setReg(R1);
					//Almacenamos en el semantic el registro que contiene el resultado de la operación
					osResult.setValue("REG", R1);
					/*						FIN CODIGO							*/
					System.out.println("Asem_Exp.calcularSencer(): Exiting static mult. First&Second operator static.");
				}
			}else{
				//D: Al menos uno de los dos no es SENCER
				Singleton_Error.getInstance().writeErrorSem(9, null, oToken);
				//D: Dejo en osResult los valores por defecto
			}
		}else{
			//D: Por lo menos una de las dos no es estática
			if(((ITipus)s1.getValue("TIPUS")).equals(Utils.aTipusSencer)&&((ITipus)s2.getValue("TIPUS")).equals(Utils.aTipusSencer)){
				//D: Los dos son SENCER, poría operar
				osResult.setValue("TIPUS",Utils.aTipusSencer);
				
				if(((String)osOper.getValue("OPER")).equals("OP_MAS")){
					
					if(((String)s1.getValue("ESTATIC")).equals("CERT") && ((String)s2.getValue("ESTATIC")).equals("FALS")){
						System.out.println("Asem_Exp.calcularSencer(): Executing not static add. Second operator not static.");
						/*  GC: DEBEMOS GENERAR EL CÓDIGO PARA REALIZAR LA SUMA NO ESTATICA 	*/
						String R = Utils.getReg();
						CodigoEnsamblador.gc("# Codigo de expresion para realizar una suma no estatica");
						//Realizamos la suma
						CodigoEnsamblador.gc("addi "+R+", "+(String)s2.getValue("REG")+", "+((Integer)s1.getValue("VALOR")).intValue());
						//->CodigoEnsamblador.gc("addi "+(String)s1.getValue("REG")+", "+(String)s2.getValue("REG")+", "+((Integer)s1.getValue("VALOR")).intValue());
						CodigoEnsamblador.gc("# Fin codigo");
						//----->Utils.setReg(R);
						//Almacenamos en el semantic el registro que contiene el resultado de la operación
						osResult.setValue("REG", R);
						//->osResult.setValue("REG", (String)s1.getValue("REG"));
						/*						FIN CODIGO							*/
						System.out.println("Asem_Exp.calcularSencer(): Exiting not static add. Second operator not static.");
					} else if(((String)s1.getValue("ESTATIC")).equals("FALS")&&((String)s2.getValue("ESTATIC")).equals("CERT")){
						System.out.println("Asem_Exp.calcularSencer(): Executing not static add. First operator not static.");
						/*  GC: DEBEMOS GENERAR EL CÓDIGO PARA REALIZAR LA SUMA ESTATICA 	*/
						String R = Utils.getReg();
						CodigoEnsamblador.gc("# Codigo de expresion para realizar una suma no estatica");
						//Realizamos la suma
						CodigoEnsamblador.gc("addi "+R+", "+(String)s1.getValue("REG")+", "+((Integer)s2.getValue("VALOR")).intValue());
						//->CodigoEnsamblador.gc("addi "+(String)s1.getValue("REG")+", "+(String)s1.getValue("REG")+", "+((Integer)s2.getValue("VALOR")).intValue());
						CodigoEnsamblador.gc("# Fin codigo");
						//----->Utils.setReg(R);
						//Almacenamos en el semantic el registro que contiene el resultado de la operación
						osResult.setValue("REG", R);
						//->osResult.setValue("REG", (String)s1.getValue("REG"));
						/*						FIN CODIGO							*/
						System.out.println("Asem_Exp.calcularSencer(): Exiting not static add. First operator not static.");
					} else if(((String)s1.getValue("ESTATIC")).equals("FALS")&&((String)s2.getValue("ESTATIC")).equals("FALS")){
						System.out.println("Asem_Exp.calcularSencer(): Executing not static add. First&Second operator not static.");
						/*  GC: DEBEMOS GENERAR EL CÓDIGO PARA REALIZAR LA SUMA ESTATICA 	*/
						String R1 = Utils.getReg(); R1 = (String)s1.getValue("REG");
						String R2 = Utils.getReg(); R2 = (String)s2.getValue("REG");
						CodigoEnsamblador.gc("# Codigo de expresion para realizar una suma no estatica");
						//Realizamos la suma
						CodigoEnsamblador.gc("add "+R1+", "+R1+", "+R2);
						CodigoEnsamblador.gc("# Fin codigo");
						Utils.setReg(R2);
						//--->Utils.setReg(R1);
						//Almacenamos en el semantic el registro que contiene el resultado de la operación
						osResult.setValue("REG", R1);
						/*						FIN CODIGO							*/
						System.out.println("Asem_Exp.calcularSencer(): Exiting not static add. First&Second operator not static.");
					}
				}
				else if(((String)osOper.getValue("OPER")).equals("OP_MENOS")){

					
					if(((String)s1.getValue("ESTATIC")).equals("CERT") && ((String)s2.getValue("ESTATIC")).equals("FALS")){
						System.out.println("Asem_Exp.calcularSencer(): Executing not static sub. Second operator not static.");
						/*  GC: DEBEMOS GENERAR EL CÓDIGO PARA REALIZAR LA RESTA NO ESTATICA 	*/
						String R1 = Utils.getReg();
						CodigoEnsamblador.gc("# Codigo de expresion para realizar una resta no estatica");
						//Cargamos en el registro el contenido de la dirección donde se encuentra el primer ID
						CodigoEnsamblador.gc("li "+R1+", "+((Integer)s1.getValue("VALOR")).intValue());
						//Realizamos la resta
						//---->CodigoEnsamblador.gc("sub "+R1+", "+R1+", "+(String)s2.getValue("REG"));
						CodigoEnsamblador.gc("sub "+R1+", "+R1+", "+(String)s2.getValue("VALOR"));
						CodigoEnsamblador.gc("# Fin codigo");
						//---->Utils.setReg(R1);
						//Almacenamos en el semantic el registro que contiene el resultado de la operación
						osResult.setValue("REG", R1);
						/*						FIN CODIGO							*/
						System.out.println("Asem_Exp.calcularSencer(): Exiting not static sub. Second operator not static.");
					} else if(((String)s1.getValue("ESTATIC")).equals("FALS")&&((String)s2.getValue("ESTATIC")).equals("CERT")){
						System.out.println("Asem_Exp.calcularSencer(): Executing not static sub. First operator not static.");
						/*  GC: DEBEMOS GENERAR EL CÓDIGO PARA REALIZAR LA RETSA NO ESTATICA 	*/
						String R1 = Utils.getReg();
						CodigoEnsamblador.gc("# Codigo de expresion para realizar una resta no estatica");
						//Cargamos en el registro el contenido de la dirección donde se encuentra el primer ID
						CodigoEnsamblador.gc("li "+R1+", "+((Integer)s2.getValue("VALOR")).intValue());
						//Realizamos la resta
						//---->CodigoEnsamblador.gc("sub "+R1+", "+(String)s2.getValue("REG")+", "+R1);
						//---->CodigoEnsamblador.gc("sub "+(String)s1.getValue("REG")+", "+(String)s2.getValue("REG")+", "+R1);
						CodigoEnsamblador.gc("sub "+(String)s1.getValue("REG")+", "+(String)s1.getValue("REG")+", "+R1);
						CodigoEnsamblador.gc("# Fin codigo");
						Utils.setReg(R1);
						//Almacenamos en el semantic el registro que contiene el resultado de la operación
						//---->osResult.setValue("REG", R1);
						osResult.setValue("REG", (String)s1.getValue("REG"));
						/*						FIN CODIGO							*/
						System.out.println("Asem_Exp.calcularSencer(): Exiting not static sub. First operator not static.");
					} else if(((String)s1.getValue("ESTATIC")).equals("FALS")&&((String)s2.getValue("ESTATIC")).equals("FALS")){
						System.out.println("Asem_Exp.calcularSencer(): Executing not static sub. First&Second operator not static.");
						/*  GC: DEBEMOS GENERAR EL CÓDIGO PARA REALIZAR LA RESTA NO ESTATICA 	*/
						String R1 = Utils.getReg(); R1 = (String)s1.getValue("REG");
						String R2 = Utils.getReg(); R2 = (String)s2.getValue("REG");
						CodigoEnsamblador.gc("# Codigo de expresion para realizar una suma no estatica");
						//Realizamos la resta
						CodigoEnsamblador.gc("sub "+R1+", "+R1+", "+R2);
						CodigoEnsamblador.gc("# Fin codigo");
						Utils.setReg(R2);
						//----->Utils.setReg(R1);
						//Almacenamos en el semantic el registro que contiene el resultado de la operación
						osResult.setValue("REG", R1);
						/*						FIN CODIGO							*/
						System.out.println("Asem_Exp.calcularSencer(): Exiting not static sub. First&Second operator not static.");
					}
					
				}
				else if(((String)osOper.getValue("OPER")).equals("OP_DIV")){
					if(((String)s1.getValue("ESTATIC")).equals("CERT") && ((String)s2.getValue("ESTATIC")).equals("FALS")){
						System.out.println("Asem_Exp.calcularSencer(): Executing not static division. Second operator not static.");
						/*  GC: DEBEMOS GENERAR EL CÓDIGO PARA REALIZAR LA DIV NO ESTATICA 	*/
						String R1 = Utils.getReg();
						CodigoEnsamblador.gc("# Codigo de expresion para realizar una division no estatica");
						//Cargamos en el registro el contenido de la dirección donde se encuentra el primer ID
						CodigoEnsamblador.gc("li "+R1+", "+((Integer)s1.getValue("VALOR")).intValue());
						//Comprobamos que el segundo registro no sea cero
						CodigoEnsamblador.gc("beq "+(String)s2.getValue("REG")+", $0, ERROR2");
						//Realizamos la div
						//---->CodigoEnsamblador.gc("div "+R1+", "+R1+", "+(String)s2.getValue("REG"));
						CodigoEnsamblador.gc("div "+R1+", "+(String)s2.getValue("REG"));
						CodigoEnsamblador.gc("mflo "+R1);
						CodigoEnsamblador.gc("# Fin codigo");
						//Utils.setReg(R1);
						//Almacenamos en el semantic el registro que contiene el resultado de la operación
						osResult.setValue("REG", R1);
						/*						FIN CODIGO							*/
						System.out.println("Asem_Exp.calcularSencer(): Exiting not static division. Second operator not static.");
					} else if(((String)s1.getValue("ESTATIC")).equals("FALS")&&((String)s2.getValue("ESTATIC")).equals("CERT")){
						System.out.println("Asem_Exp.calcularSencer(): Executing not static division. First operator not static.");
						//D: Control de división por 0						
						if((Integer)s2.getValue("VALOR")==0){
							Singleton_Error.getInstance().writeErrorSem(35, null, oToken);
							CodigoEnsamblador.gc("b ERROR2");
							osResult.setValue("REG", (String)s1.getValue("REG"));
						} else {
							/*  GC: DEBEMOS GENERAR EL CÓDIGO PARA REALIZAR LA DIV NO ESTATICA 	*/
							String R1 = Utils.getReg();
							CodigoEnsamblador.gc("# Codigo de expresion para realizar una division no estatica");
							//Cargamos en el registro el contenido de la dirección donde se encuentra el primer ID
							CodigoEnsamblador.gc("li "+R1+", "+((Integer)s2.getValue("VALOR")).intValue());
							//Realizamos la div
							//---->CodigoEnsamblador.gc("div "+R1+", "+(String)s1.getValue("REG")+", "+R1);
							CodigoEnsamblador.gc("div "+(String)s1.getValue("REG")+", "+R1);
							CodigoEnsamblador.gc("mflo "+(String)s1.getValue("REG"));
							CodigoEnsamblador.gc("# Fin codigo");
							//Utils.setReg(R1);
							//Almacenamos en el semantic el registro que contiene el resultado de la operación
							osResult.setValue("REG", (String)s1.getValue("REG"));
							/*						FIN CODIGO							*/
							System.out.println("Asem_Exp.calcularSencer(): Exiting not static division. First operator not static.");
						}
					} else if(((String)s1.getValue("ESTATIC")).equals("FALS")&&((String)s2.getValue("ESTATIC")).equals("FALS")){
						System.out.println("Asem_Exp.calcularSencer(): Executing not static division. First&Second operator not static.");
						/*  GC: DEBEMOS GENERAR EL CÓDIGO PARA REALIZAR LA DIV NO ESTATICA 	*/
						String R1 = Utils.getReg(); R1 = (String)s1.getValue("REG");
						String R2 = Utils.getReg(); R2 = (String)s2.getValue("REG");
						CodigoEnsamblador.gc("# Codigo de expresion para realizar una division no estatica");
						//Comprobamos que el segundo registro no sea cero
						CodigoEnsamblador.gc("beq "+(String)s2.getValue("REG")+", $0, ERROR2");
						//Realizamos la div
						//---->CodigoEnsamblador.gc("div "+R1+", "+R1+", "+R2);
						CodigoEnsamblador.gc("div "+R1+", "+R2);
						CodigoEnsamblador.gc("mflo "+R1);
						CodigoEnsamblador.gc("# Fin codigo");
						Utils.setReg(R2);
						//Utils.setReg(R1);
						//Almacenamos en el semantic el registro que contiene el resultado de la operación
						osResult.setValue("REG", R1);
						/*						FIN CODIGO							*/
						System.out.println("Asem_Exp.calcularSencer(): Exiting not static division. First&Second operator not static.");
					}
				}
				else if(((String)osOper.getValue("OPER")).equals("OP_MUL")){
					if(((String)s1.getValue("ESTATIC")).equals("CERT") && ((String)s2.getValue("ESTATIC")).equals("FALS")){
						System.out.println("Asem_Exp.calcularSencer(): Executing multiplicacion no estatica. Second operator not static.");
						/*  GC: DEBEMOS GENERAR EL CÓDIGO PARA REALIZAR LA MULT NO ESTATICA 	*/
						String R1 = Utils.getReg();
						CodigoEnsamblador.gc("# Codigo de expresion para realizar una mult no estatica");
						//Cargamos en el registro el contenido de la dirección donde se encuentra el primer ID
						CodigoEnsamblador.gc("li "+R1+", "+((Integer)s1.getValue("VALOR")).intValue());
						//Realizamos la mult
						//---->CodigoEnsamblador.gc("mul "+R1+", "+R1+", "+(String)s2.getValue("REG"));
						CodigoEnsamblador.gc("mult "+R1+", "+(String)s2.getValue("REG"));
						CodigoEnsamblador.gc("mflo "+R1);
						CodigoEnsamblador.gc("# Fin codigo");
						//----->Utils.setReg(R1);
						//Almacenamos en el semantic el registro que contiene el resultado de la operación
						osResult.setValue("REG", R1);
						/*						FIN CODIGO							*/
						System.out.println("Asem_Exp.calcularSencer(): Exiting multiplicacion no estatica. Second operator not static.");
					} else if(((String)s1.getValue("ESTATIC")).equals("FALS")&&((String)s2.getValue("ESTATIC")).equals("CERT")){
						System.out.println("Asem_Exp.calcularSencer(): Executing multiplicacion no estatica. First operator not static.");
						/*  GC: DEBEMOS GENERAR EL CÓDIGO PARA REALIZAR LA MULT NO ESTATICA 	*/
						String R1 = Utils.getReg();
						CodigoEnsamblador.gc("# Codigo de expresion para realizar una mult no estatica");
						//Cargamos en el registro el contenido de la dirección donde se encuentra el primer ID
						CodigoEnsamblador.gc("li "+R1+", "+((Integer)s2.getValue("VALOR")).intValue());
						//Realizamos la mult
						//---->CodigoEnsamblador.gc("mul "+R1+", "+(String)s1.getValue("REG")+", "+R1);
						CodigoEnsamblador.gc("mult "+(String)s1.getValue("REG")+", "+R1);
						CodigoEnsamblador.gc("mflo "+(String)s1.getValue("REG"));
						CodigoEnsamblador.gc("# Fin codigo");
						//Utils.setReg(R1);
						//Almacenamos en el semantic el registro que contiene el resultado de la operación
						osResult.setValue("REG", (String)s1.getValue("REG"));
						/*						FIN CODIGO							*/
						System.out.println("Asem_Exp.calcularSencer(): Exiting multiplicacion no estatica. First operator not static.");
					
					} else if(((String)s1.getValue("ESTATIC")).equals("FALS")&&((String)s2.getValue("ESTATIC")).equals("FALS")){
						System.out.println("Asem_Exp.calcularSencer(): Executing multiplicacion no estatica. First&Second operator not static.");
						/*  GC: DEBEMOS GENERAR EL CÓDIGO PARA REALIZAR LA MULT NO ESTATICA 	*/
						String R1 = Utils.getReg(); R1 = (String)s1.getValue("REG");
						String R2 = Utils.getReg(); R2 = (String)s2.getValue("REG");
						CodigoEnsamblador.gc("# Codigo de expresion para realizar una mult no estatica");
						//Realizamos la mult
						//---->CodigoEnsamblador.gc("mul "+R1+", "+R1+", "+R2);
						CodigoEnsamblador.gc("mult "+R1+", "+R2);
						CodigoEnsamblador.gc("mflo "+R1);
						CodigoEnsamblador.gc("# Fin codigo");
						Utils.setReg(R2);
						///---->Utils.setReg(R1);
						//Almacenamos en el semantic el registro que contiene el resultado de la operación
						osResult.setValue("REG", R1);
						/*						FIN CODIGO							*/
						System.out.println("Asem_Exp.calcularSencer(): Exiting multiplicacion no estatica. First&Second operator not static.");
					}
				}
				//D: Dejo en osResult los valores por defecto
			}else{
				//D: Al menos uno de los dos no es LOGIC, no podría operar
				Singleton_Error.getInstance().writeErrorSem(9, null, oToken);
				//D: Dejo en osResult los valores por defecto
			}			
		}
		
		return osResult;
	}	

	private static Semantic calcularCadena(Semantic s1,Semantic osOper,Semantic s2,Token oToken){
		// Ya sabemos que donde venimos, sOper es una concatencación
		//D: Semantic de Resultado
		Semantic osResult=new Semantic();
		String sAux1=null;
		String sAux2=null;
		Integer oiLongPrimeraCadena = new Integer( ((TipusCadena)s1.getValue("TIPUS")).getLongitud() );
		Integer oiLongSegundaCadena = new Integer( ((TipusCadena)s2.getValue("TIPUS")).getLongitud() );
		
		//D: Miro si es estática
		//D:	si es estática
		//D:		- Miro que los tipos de los dos semantics sean CADENA
		//D:		- Concateno las cadenas
		//D:	si no es estática
		//D:		- Miro que los tipos de los dos semantics sean CADENA
		//D:		- Retorno semantica con valor de 1 (valor que puede comprometer poco el código)
		
		//Asigno valores iniciales, que luego iré modificando
		osResult.setValue("TIPUS",Utils.aTipusIndefinit);
		osResult.setValue("ESTATIC", "FALS");
		osResult.setValue("VALOR", "");
		
		if(((String)s1.getValue("ESTATIC")).equals("CERT")&&((String)s2.getValue("ESTATIC")).equals("CERT")){
			//D: Las dos son estáticas
			osResult.setValue("ESTATIC", "CERT");
			if(((ITipus)s1.getValue("TIPUS")).getNom().equals("CADENA") &&((ITipus)s2.getValue("TIPUS")).getNom().equals("CADENA")){
				//D: Los dos son CADENA, puedo operar
				//D: Creo la cadena con longitud igual a la suma de las dos cadenas
				//D: Asigno como valor la concatenación de las dos cadenas
				//D: (Lo paso por variable auxiliar para quitarle las comillas)
				sAux1 = (String)s1.getValue("VALOR");
				//sAux1 = sAux1.substring(1, sAux1.length()-1);
				sAux2 = (String)s2.getValue("VALOR");
				//sAux2 = sAux2.substring(1, sAux2.length()-1);
				
				osResult.setValue("VALOR",sAux1.concat(sAux2));
				osResult.setValue("TIPUS",Utils.getTipusCadena(sAux1.length()+sAux2.length()));
				
				/*		GC: CONCATENACIÓN DE DOS CADENAS		*/
				Integer oiIndice = new Integer(0);
				String R = Utils.getReg();
				//El índice apunta justo al \0
				oiIndice = new Integer(sAux1.length());
				int iLong = sAux1.length();
				//Mientras no se acabe ni la primera cadena, ni la segunda
				CodigoEnsamblador.gc("# Codigo para concatenar dos cadenas estaticas");
				while ( oiIndice.intValue() < oiLongPrimeraCadena && oiIndice.intValue() < oiLongSegundaCadena ) {
					CodigoEnsamblador.gc("lb "+R+", "+(oiIndice.intValue() - iLong) + ((Integer)s2.getValue("DIR")).intValue());
					CodigoEnsamblador.gc("sb "+R+", "+oiIndice.intValue() + ((Integer)s1.getValue("DIR")).intValue());
					oiIndice = oiIndice + 1;
				}
				oiIndice = oiIndice + 1;
				CodigoEnsamblador.gc("# Copiamos el byte nulo al final de la cadena");
				CodigoEnsamblador.gc("sb $0, "+oiIndice.intValue() + ((Integer)s1.getValue("DIR")).intValue());
				CodigoEnsamblador.gc("# Fin codigo");
				Utils.setReg(R);
				//No estoy seguro de si es necesario, pero juraria que sí, por si hay más de una &
				osResult.setValue("DIR", (String)s1.getValue("DIR"));
				/*					FIN GC						*/
				
			}else{
				//D: Al menos uno de los dos no es CADENA
				Singleton_Error.getInstance().writeErrorSem(11, null, oToken);
				//D: Dejo en osResult los valores por defecto
			}
		}else{
			//D: Por lo menos una de las dos no es estática
			if(!((ITipus)s1.getValue("TIPUS")).equals(Utils.aTipusIndefinit) &&!((ITipus)s1.getValue("TIPUS")).equals(Utils.aTipusIndefinit)){
				if(((ITipus)s1.getValue("TIPUS")).getNom().equals("CADENA") &&((ITipus)s2.getValue("TIPUS")).getNom().equals("CADENA")){
					//D: Los dos son CADENA, poría operar. 
					osResult.setValue("TIPUS",Utils.getTipusCadena(((TipusCadena)s1.getValue("TIPUS")).getLongitud()+((TipusCadena)s2.getValue("TIPUS")).getLongitud()));				
					//D: Dejo en osResult los valores por defecto
					
					/*		GC: CONCATENACIÓN DE DOS CADENAS		*/
					Integer oiIndice = new Integer(0);
					String Rd1 = Utils.getReg();
					String Rd2 = Utils.getReg();
					String R1 = Utils.getReg();
					String RegLE = Utils.getReg();
					String Eti1 = Utils.getEtiqueta();
					String Eti2 = Utils.getEtiqueta();
					String Eti3 = Utils.getEtiqueta();

					CodigoEnsamblador.gc("# Codigo para concatenar dos cadenas no estaticas");
					CodigoEnsamblador.gc("# \t Primero avanzamos el puntero hasta el byte nulo de la primera cadena");
					CodigoEnsamblador.gc("xor "+RegLE+", "+RegLE);
					CodigoEnsamblador.gc("li "+Rd1+", "+(String)s1.getValue("DIR"));
					CodigoEnsamblador.gc("li "+Rd2+", "+(String)s2.getValue("DIR"));
					CodigoEnsamblador.gc("li "+Rd2+", "+(String)s2.getValue("DIR"));
					CodigoEnsamblador.gc(Eti1+":");
					CodigoEnsamblador.gc("lb "+R1+", (0)"+Rd1);
					CodigoEnsamblador.gc("beq "+R1+", $0, "+Eti2);
					CodigoEnsamblador.gc("addi "+Rd1+", 1");
					CodigoEnsamblador.gc("addi "+RegLE+", "+RegLE+", 1");
					CodigoEnsamblador.gc("bne "+R1+", $0, "+Eti1);
					CodigoEnsamblador.gc(Eti2+":");
					
					CodigoEnsamblador.gc("# \t Segundo copiamos la cadena dependiendo hasta el fin de la primera o la segunda (controlando limites)");
					CodigoEnsamblador.gc("li "+Rd1+", "+(String)s1.getValue("DIR"));
					CodigoEnsamblador.gc("li "+Rd2+", "+(String)s2.getValue("DIR"));
					CodigoEnsamblador.gc("add "+Rd1+", "+RegLE+", "+Rd1);
					CodigoEnsamblador.gc(Eti3+":");
					CodigoEnsamblador.gc("lb "+R1+", (0)"+Rd2);
					CodigoEnsamblador.gc("sb "+R1+", (0)"+Rd1);
					CodigoEnsamblador.gc("addi "+Rd1+", "+Rd1+", 1");
					CodigoEnsamblador.gc("addi "+Rd2+", "+Rd2+", 1");
					CodigoEnsamblador.gc("addi "+RegLE+", "+RegLE+", 1");
					
					if (oiLongPrimeraCadena.intValue() <= oiLongSegundaCadena.intValue() ) 
						CodigoEnsamblador.gc("blt "+RegLE+", "+oiLongPrimeraCadena.intValue()+", "+Eti3);
					else
						CodigoEnsamblador.gc("blt "+RegLE+", "+oiLongSegundaCadena.intValue()+", "+Eti3);
					
					CodigoEnsamblador.gc("addi "+Rd1+", "+Rd1+", 1");
					CodigoEnsamblador.gc("sb $0, (0)"+Rd1 );
					
					CodigoEnsamblador.gc("# Fin codigo");
					Utils.setReg(RegLE);
					Utils.setReg(R1);
					Utils.setReg(Rd1);
					Utils.setReg(Rd2);
					//No estoy seguro de si es necesario, pero juraria que sí, por si hay más de una &
					osResult.setValue("DIR", (String)s1.getValue("DIR"));
					/*					FIN GC						*/
					
					
				}else{
					//D: Al menos uno de los dos no es CADENA, no podría operar
					Singleton_Error.getInstance().writeErrorSem(11, null, oToken);
					//D: Dejo en osResult los valores por defecto
				}	
			}else{
				Singleton_Error.getInstance().writeErrorSem(11, null, oToken);
			}
		}
		
		return osResult;
	}
	
	public static Semantic makeCteEntera(Token oToken){
		//D: Genero cte entera
		Semantic osResult = new Semantic();
		
		osResult.setValue("ESTATIC","CERT"); 
		osResult.setValue("TIPUS",Utils.getTipusSimple("SENCER")); 
		osResult.setValue("VALOR",Integer.parseInt(oToken.image));
		/*		GC: Guardamos el valor entero en un registro		*/
//		CodigoEnsamblador.gc("# Codigo para guardar un valor entero en un registro (FACTOR)");
//		String R = Utils.getReg();
//		CodigoEnsamblador.gc("li "+R+", "+Integer.parseInt(oToken.image));
		//osResult.setValue("REG", R);
		/*							FIN GC							*/	
		return osResult;
	}
	
	public static Semantic makeCteLogica(Token oToken){
		//D: Genero cte logica
		Semantic osResult = new Semantic();
		
		osResult.setValue("ESTATIC","CERT"); 
		osResult.setValue("TIPUS",Utils.getTipusSimple("LOGIC")); 
		osResult.setValue("VALOR",oToken.image.toUpperCase()); 
		/*		GC: Guardamos el valor entero en un registro		*/
//		CodigoEnsamblador.gc("# Codigo para guardar un valor logico en un registro (FACTOR)");
//		String R = Utils.getReg();
//		if (oToken.image.compareTo("CERT") == 0) CodigoEnsamblador.gc("li "+R+", 1");
//		else CodigoEnsamblador.gc("li "+R+", 0");
		//osResult.setValue("REG", R);
		/*							FIN GC							*/	
		
		return osResult;
	}
	
	public static Semantic makeCteCadena(Token oToken){
		//D: Genero cte cadena
		Semantic osResult = new Semantic();
		
		osResult.setValue("ESTATIC","CERT"); 		
		osResult.setValue("VALOR",oToken.image.substring(1,oToken.image.length()-1)); //D: No lo paso a UperCase pq es directamente la cadena lo que quiero
		osResult.setValue("TIPUS",Utils.getTipusCadena(oToken.image.length()-2)); 
		
		return osResult;
	}
	
	public static Semantic makeTrimCadena(Semantic s,Token oToken){
		//D: Verifico cadena y hago TRIM
		
		Semantic osResult = new Semantic();
		String sAux = null;
		
		//D: Establezco valores por defecto
		osResult.setValue("ESTATIC","FALS"); 
		osResult.setValue("TIPUS",Utils.aTipusIndefinit); //TODO: Justificar esta decisión en la memoria
		osResult.setValue("VALOR","");
		
		//D: Verificamos que el tipo sea CADENA
		if(((ITipus)s.getValue("TIPUS")).getNom().equals("CADENA")){
			//D: Guardo el tipo en el resultado
			osResult.setValue("TIPUS",(ITipus)s.getValue("TIPUS"));
			//D: Miro si es o no estático
			if(((String)s.getValue("ESTATIC")).equals("CERT")){
				//D: Establezco el valor a estático
				osResult.setValue("ESTATIC","CERT");
				//D: Calculo el TRIM (Lo paso por variable auxiliar para quitarle las comillas)
				sAux = (String)s.getValue("VALOR");
				sAux = sAux.substring(1, sAux.length()-1);
				osResult.setValue("VALOR",(sAux).trim());
				//System.out.println(sAux);
			}else{
				//D: El valor no es estático, no puedo calcular el TRIM. No hago nada.
			}
		}else{
			//D: Muestro error por no ser de tipus cadena
			Singleton_Error.getInstance().writeErrorSem(11, null, oToken);
		}
		
		return osResult;
	}
	
	public static Semantic getFuncion(Token oToken){
		
		//D: Recupero el Descriptor de la función en función del ID
		Semantic osResult = new Semantic();
		
		TaulaSimbols ts=Singleton_TS.getInstance();
		osResult.setValue("FUNCIO",ts.obtenirBloc(0).obtenirProcediment(((String)oToken.image).toUpperCase()));
		
		return osResult;
	}
	
	public static Semantic retornaSemFuncion(Semantic osFuncion){
		//D: Devuelvo Semantic con los atributos de la función en función del osFuncion recibido
		Semantic osResult = new Semantic();
		
		osResult.setValue("ESTATIC", "FALS");
		//Si la función no existe, no tiene objeto FUNCION y el tipus es indefinido
		if(osFuncion.getValue("FUNCIO")==null){
			osResult.setValue("TIPUS", Utils.aTipusIndefinit);
		}else{
			osResult.setValue("TIPUS", ((Funcio)osFuncion.getValue("FUNCIO")).getTipus());			
		}
		osResult.setValue("FUNCIO","CERT"); //D: Me servirá en instrucciones para identificar que es una función 
		
		return osResult;
	}
	
	public static void checkFuncionSinParametros(Semantic osFuncion){
		
		//D: Verifico que la función no tiene parámetros. Si tiene muestro error.
		Semantic osSemantic=new Semantic(); //Semantic para tratamiento de Errores
		Funcio oFuncio = null;
		oFuncio = (Funcio)osFuncion.getValue("FUNCIO");
		
		if(oFuncio==null){ 
			//D: El ID no está declarado como Funcion, pero no muestro el error pq ya lo habrá mostrado antes
		}else{
			if(oFuncio.getNumeroParametres()>0){
				//D: Miro si tiene parámetros (si estoy aquí no tendría que tenerlos, por tanto es un error)
				osSemantic.setValue("PARAM_DEC", oFuncio.getNumeroParametres());
				osSemantic.setValue("PARAM_US", new Integer(0));
				Singleton_Error.getInstance().writeErrorSem(20, osSemantic, Babel2009.getToken(0)); //Como token le paso el Token actual del Babel parser para que muestre la linea
			}
		}
	}
	
	public static void checkFuncionParametro(Semantic osFuncion,Semantic osExpParametro){
	
		Funcio oFuncio = ((Funcio)osFuncion.getValue("FUNCIO"));
		Integer nParam = ((Integer)osFuncion.getValue("COUNTP"))-1;
		Semantic osAux = new Semantic();
		
		//D: Si la función no existe, que no haga nada (no muestro el error pq ya lo he mostrado antes)
		if(oFuncio!=null){
			//Si ya no tengo más parámetros declarados, tampoco tengo que hacer nada, es un error
			if(nParam+1>oFuncio.getNumeroParametres()){
				Singleton_Error.getInstance().writeErrorSem(55, osAux, Babel2009.getToken(0));
				return;
			}
			//Si el parámetro es indefinido, entonces no tengo que mirar nada. Ya he dado el error antes
			if(!((ITipus)osExpParametro.getValue("TIPUS")).equals(Utils.aTipusIndefinit)){
				//Miro que el tipo del parámetro que recibo sea del mismo tipo que el parámetro número COUNTP
				if(oFuncio.obtenirParametre(nParam.intValue()).getTipus().getNom().equals(((ITipus)osExpParametro.getValue("TIPUS")).getNom())){
					//Los tipos parecen iguales, miro si es cadena o otro
					if(oFuncio.obtenirParametre(nParam.intValue()).getTipus().getNom().equals("CADENA")){
						//Miro eq. estructural de tipos
						if(((TipusCadena)oFuncio.obtenirParametre(nParam.intValue()).getTipus()).getLongitud()==((TipusCadena)osExpParametro.getValue("TIPUS")).getLongitud()){
							//Entonces es CADENA estructuralmente equivalente
						}else{
							//Error, los dos son cadena pero no son est. eq.
							osAux.setValue("NUM_PARAM", nParam+1); //Le sumo uno para mostrar mejor el mensaje
							osAux.setValue("TIPUS_LONG", ((TipusCadena)oFuncio.obtenirParametre(nParam.intValue()).getTipus()).getLongitud()); //Le sumo uno para mostrar mejor el mensaje
							Singleton_Error.getInstance().writeErrorSem(52, osAux, Babel2009.getToken(0));
						}
					}
					
					//Los tipos son iguales, esto es correcto (falta ver si lo puedo pasar por ref o por val)				
				}else{
					//Error, el tipo de parámetro es diferente
					osAux.setValue("NUM_PARAM", nParam+1); //Le sumo uno para mostrar mejor el mensaje		
					if(oFuncio.obtenirParametre(nParam.intValue()).getTipus().getNom().substring(0,2).equals("V_")){
						if(((ITipus)osExpParametro.getValue("TIPUS")).getNom().substring(0,2).equals("V_")){
							//Los dos son vectores pero son estructuralmente no equiv.
							Singleton_Error.getInstance().writeErrorSem(53, osAux, Babel2009.getToken(0));
							return;
						}else{
							osAux.setValue("TIPUS_PARAM","VECTOR");
						}
					}else{
						osAux.setValue("TIPUS_PARAM",oFuncio.obtenirParametre(nParam.intValue()).getTipus().getNom());
					}					
					Singleton_Error.getInstance().writeErrorSem(21, osAux, Babel2009.getToken(0));					
				}				
				//POTSER LI TINC QUE RESTAR 1 A NPARAM!!!! JA QUE AL LLISTA DE PARAMETRES COMENÇA PER 0
				//Miro si el tipo de paso es adecuado para el tipo de variable
				//hE CANVIAT EL nParam.intValue()-1, li he afegit el -1
				if(oFuncio.obtenirParametre(nParam.intValue()).getTipusPasParametre()==TipusPasParametre.REFERENCIA){
					if(((String)osExpParametro.getValue("ESTATIC")).equals("CERT")){
						//Error, no puedo pasar por REF una cosa ESTATICA
						osAux.setValue("NUM_PARAM", nParam+1); //Le sumo uno para mostrar mejor el mensaje	
						Singleton_Error.getInstance().writeErrorSem(22, osAux, Babel2009.getToken(0));	
					}else{
						//CAS PARTICULAR: Mirem si OPERAT ha estat assignat. Operat es cert quan "j+5", o be "cert and b",...
						if(osExpParametro.getValue("OPERAT")!=null){
							//Error, no puc passar per referencia aixo
							osAux.setValue("NUM_PARAM", nParam+1); //Le sumo uno para mostrar mejor el mensaje	
							Singleton_Error.getInstance().writeErrorSem(22, osAux, Babel2009.getToken(0));
						}
						if(osExpParametro.getValue("FUNCIO")!=null){
							//L'expressió és una funció, tampoc la puc passar per paràmetres
							osAux.setValue("NUM_PARAM", nParam+1); //Le sumo uno para mostrar mejor el mensaje	
							Singleton_Error.getInstance().writeErrorSem(54, osAux, Babel2009.getToken(0));
						}
					}
				}
				
				
				//TODO: Miro si ya no tengo más parámetros que asignar (uso>dec)
			}
		}
		
	}
	
	public static void checkFuncionFinParametros(Semantic osFuncion){
		
		//D: Verifico que el número de parámetros recibido es igual al número de parámetros de la función
		Semantic osSemantic=new Semantic(); //Semantic para tratamiento de Errores
		
		//D: Si la función no existe, no tengo que hacer nada (no muestro el error pq ya lo he mostrado antes)
		if(((Funcio)osFuncion.getValue("FUNCIO"))!=null){
			//D: Miro si el contador de parámetros de uso es menor que el número de parámetros en declaración
			if((Integer)osFuncion.getValue("COUNTP")<((Funcio)osFuncion.getValue("FUNCIO")).getNumeroParametres()){
				//D: Usa menos parámetros que los que la función necesita según declaración
				osSemantic.setValue("PARAM_DEC", ((Funcio)osFuncion.getValue("FUNCIO")).getNumeroParametres());
				osSemantic.setValue("PARAM_US", (Integer)osFuncion.getValue("COUNTP"));
				Singleton_Error.getInstance().writeErrorSem(20, osSemantic, Babel2009.getToken(0)); //Como token le paso el Token actual del Babel parser para que muestre la linea
			}
		}
	}
	
	public static Semantic checkVector(Token oToken,Semantic osExp){
		
		Semantic osResult = new Semantic();
		TaulaSimbols ts=Singleton_TS.getInstance();
		Variable ovArray=null;
		boolean bTrobat = false;
		boolean bTrobatBloc1 = false;
		
		//D: Establezco valores por defecto
		//osResult = osExp;
		if (osExp.getValue("DIR") != null ) osResult.setValue("DIR", (String)osExp.getValue("DIR"));
		//Brutal... Cuando asigno este tipus a osResult, osExp también me cambia su tipo a indefinit...
		osResult.setValue("TIPUS",Utils.aTipusIndefinit);
		osResult.setValue("ESTATIC","FALS");
		
		//D: Verifico que el ID está declarado como vector. Busco en bloque 0 y bloque 1 para indicar si es Global o Local
		
		//D: Miro si está en bloque 0
		if(ts.obtenirBloc(0).obtenirVariable(oToken.image.toUpperCase())!=null){
			//D: Verifico que esté eclarado como TipusArray mirando el inicio del código del nombre
			if(((ITipus)ts.obtenirBloc(0).obtenirVariable(oToken.image.toUpperCase()).getTipus()).getNom().substring(0, 2).toString().equals("V_")){
					//D: Indico que está como global
					bTrobat = true;
					ovArray = ts.obtenirBloc(0).obtenirVariable(oToken.image.toUpperCase());
			}
		}
		//D: No es un else pq puede ser que en el bloque 0 la tenga declarada como VAR y en el bloque 1 como VECTOR. Entiendo que manda el Bloque 1
		//D: Miro si está en bloque 1 sólo si yo estoy en el bloque 1 (desde el bloque 0 no puedo consultar nada del bloque 1)
		if(ts.getBlocActual()==1){
			if(ts.obtenirBloc(1).obtenirVariable(oToken.image.toUpperCase())!=null){
				//D: Verifico que esté eclarado como TipusArray mirando el inicio del código del nombre
				if(((ITipus)ts.obtenirBloc(1).obtenirVariable(oToken.image.toUpperCase()).getTipus()).getNom().substring(0, 2).equals("V_")){
					//D: Indico que está como local
					bTrobat = true;
					//Si se ha encontrado en el bloque 1 es que se trata de una variable local o lo hemos de tratar como tal
					//Esto se debe a que puede estar declarada como global y local, pero como local tiene prioridad
					bTrobatBloc1 = true;
					ovArray = ts.obtenirBloc(1).obtenirVariable(oToken.image.toUpperCase());
				}
			}
		}
		
		//D: Si no tengo SCOPE, entonces es que el array no está definido
		if(bTrobat == false){
			//D: Muestro error (el tipo ya es indefinido por opciones por defecto) 
			Singleton_Error.getInstance().writeErrorSem(37, null, oToken);			
		}else{
			//D: La expresión ha de ser de tipo entero
			if(((ITipus)osExp.getValue("TIPUS")).equals(Utils.getTipusSimple("SENCER"))){
				//D: La expresión puede ser o no estática
				if(((String)osExp.getValue("ESTATIC")).equals("CERT")){
					//D: Verifico que los límites no excedan los límites del vector
					if(((Integer)osExp.getValue("VALOR"))>=((Integer)((TipusArray)ovArray.getTipus()).obtenirDimensio(0).getLimitInferior()) && ((Integer)osExp.getValue("VALOR"))<((Integer)((TipusArray)ovArray.getTipus()).obtenirDimensio(0).getLimitSuperior())){
						//D: Estoy dentro de los límites. No hago nada.
					}else{
						//D: Se ha excedido el límite del vector
						Singleton_Error.getInstance().writeErrorSem(24, null, oToken);	
					}
				}else{
					//D: En generación de código veremos lo que hay que hacer
					//Ya he verificado los límites dinámicos justo antes de entrar en este método
				}
				//D: Asigno valores de tipo a los del tipo de los elementos del vector
				osResult.setValue("TIPUS",(ITipus)((TipusArray)ovArray.getTipus()).getTipusElements());
				
				/* GC: MIRAMOS SI LA VARIABLE ES UN PARÁMETRO O NO  */
				if ( ovArray instanceof Parametre ) {
					//Si es un parametro miramos si está pasado por referencia o por valor
					if ( ((Parametre) ovArray).getTipusPasParametre().equals(TipusPasParametre.VALOR) ) {
//						osResult.setValue("DIR", ovArray.getDesplacament()+"($fp)");
//						System.out.println("DIRECCION DEL PARAMETRO VECTOR (V): "+ovArray.getNom()+" = "+ovArray.getDesplacament()+"($fp)");
						
					} else {
//						String R = Utils.getReg();
//						CodigoEnsamblador.gc("# Codigo para el calculo de un parametro vector pasado por referencia");
//						CodigoEnsamblador.gc("lw "+R+", "+ovArray.getDesplacament()+"($fp)");
//						CodigoEnsamblador.gc("# Fin codigo");
//						osResult.setValue("DIR", "0("+R+")");
//						Utils.setReg(R);
//						System.out.println("DIRECCION DEL PARAMETRO VECTOR (R): "+ovArray.getNom()+" = 0("+R+")");
					}
				} else {
					//No es un parametro
					if (bTrobatBloc1) {
						//Si se ha encontrado en el bloque 1, sabemos que no es parámetro, por tanto es local
//						osResult.setValue("DIR", ovArray.getDesplacament()+"$fp");
//						System.out.println("DIRECCION DE LA VARIABLE VECTOR LOCAL: "+ovArray.getNom()+" = "+ovArray.getDesplacament()+"$fp");
					} else {
						if (bTrobat) {
							//Si no se ha encontrado en el bloque 1 y (evidentemente no es parametro), es global
//							osResult.setValue("DIR", ovArray.getDesplacament()+"($gp)");
//							System.out.println("DIRECCION DE LA VARIABLE VECTOR GLOBAL: "+ovArray.getNom()+" = "+ovArray.getDesplacament()+"($gp)");
						}
					}
				}
				/*					FIN COMPROBACIÓN 				*/
				
			}else{
				//D: Muestro error por tipos no enteros en límites de acceso a vector
				Singleton_Error.getInstance().writeErrorSem(18, null, oToken);
			}
		}
		
		return osResult;
	}
	
	public static Semantic checkEsCteVar(Token oToken){
		//D: Verifico si es cte o var en bloque 0 o 1
		
		//Si tinc Bloc1:
//			- Si es cte o var
//				si es cte
//				si es var
		CodigoEnsamblador.gc("# CALCULO LA DIRECCIÓN DE UNA VARIABLE O CTE. NO LA DIRECCIÓN DE ACCESO A UN VECTOR.");
		Semantic osResult = new Semantic();
		Boolean bTrobat=false;
		TaulaSimbols ts = Singleton_TS.getInstance();
		Constant oConstant;
		Variable oVariable;
		/* VARIABLES GC */
		boolean esParam = false;
		boolean esRef = false;
		/* 	 FIN GC 	*/
		//D: Verifico si es CONSTANTE o VAR
		
		if(ts.getNumeroBlocs()>1){ //Verifico que tenga bloque 1
			if(ts.obtenirBloc(1).obtenirConstant(oToken.image.toUpperCase())==null){
				//D: No es CTE en bloque 1. Puede que sea variable.
				if(ts.obtenirBloc(1).obtenirVariable(oToken.image.toUpperCase())==null){
					//D: No es VAR en bloque 1
				}else{
					//D: Es VAR en bloque 1
					bTrobat=true;
					oVariable = ts.obtenirBloc(1).obtenirVariable(oToken.image.toUpperCase());
					osResult.setValue("TIPUS",(ITipus)oVariable.getTipus());
					osResult.setValue("ESTATIC", "FALS"); //Pq es una variable
					
					/* GC: MIRAMOS SI LA VARIABLE ES UN PARÁMETRO O NO  */
					if ( oVariable instanceof Parametre ) {
						//Si es un parametro miramos si está pasado por referencia o por valor
						if ( ((Parametre) oVariable).getTipusPasParametre().equals(TipusPasParametre.VALOR) ) {
							osResult.setValue("DIR", oVariable.getDesplacament()+"($fp)");
							System.out.println("DIRECCION DEL PARAMETRO (V): "+oVariable.getNom()+" = "+oVariable.getDesplacament()+"($fp)");
						} else {
							String R = Utils.getReg();
							CodigoEnsamblador.gc("# Codigo para el calculo de un parametro vector pasado por referencia");
							CodigoEnsamblador.gc("lw "+R+", "+oVariable.getDesplacament()+"($fp)");
							CodigoEnsamblador.gc("# Fin codigo");
							osResult.setValue("DIR", "0("+R+")");
							Utils.setReg(R);
							System.out.println("DIRECCION DEL PARAMETRO (R): "+oVariable.getNom()+" = 0("+R+")");
						}
					} else {
						//No es un parametro
						osResult.setValue("DIR", oVariable.getDesplacament()+"($fp)");
						System.out.println("DIRECCION DE LA VARIABLE LOCAL: "+oVariable.getNom()+" = "+oVariable.getDesplacament()+"($fp)");
					}
					/*					FIN COMPROBACIÓN 				*/
				}
			}else{
				//D: Es constante en bloque 1
				bTrobat=true;
				oConstant = ts.obtenirBloc(1).obtenirConstant(oToken.image.toUpperCase());
				osResult.setValue("TIPUS",(ITipus)oConstant.getTipus());
				osResult.setValue("ESTATIC", "CERT"); //Pq es una cte
				if(oConstant.getValor()!=null){
					//D: Verifico por posible valor nulo al ser de tipo indefinido
					osResult.setValue("VALOR",oConstant.getValor());
				}			
			}
		}
		

		
		//D: Miro en bloque 0		
		if(ts.obtenirBloc(0).obtenirConstant(oToken.image.toUpperCase())==null && bTrobat==false){
			//D: No es CTE en bloque 0. Puede que sea variable.
			if(ts.obtenirBloc(0).obtenirVariable(oToken.image.toUpperCase())==null){
				//D: Tampoco es VAR en bloque 0. Es un error.
				Singleton_Error.getInstance().writeErrorSem(38, null, oToken);
				osResult.setValue("TIPUS", Utils.aTipusIndefinit);
				osResult.setValue("ESTATIC", "FALS"); //Pongo estatic a Fals, desconozco el valor
			}else{
				if (ts.obtenirBloc(0).obtenirVariable(oToken.image.toUpperCase())!=null && bTrobat==false) {
					//D: Es VAR en bloque 0
					bTrobat=true;
					oVariable = ts.obtenirBloc(0).obtenirVariable(oToken.image.toUpperCase());
					osResult.setValue("TIPUS",(ITipus)oVariable.getTipus());
					osResult.setValue("ESTATIC", "FALS"); //Pq es una variable
					
					/* 			GC: ASIGNACIÓN DE LA DIRECCIÓN A UNA VARIABLE			 */
					//Estamos en el bloque 0, por tanto la variable es global (en el main no se pueden declarar variables)
					osResult.setValue("DIR", oVariable.getDesplacament()+"($gp)");
					System.out.println("DIRECCION DE LA VARIABLE GLOBAL: "+oVariable.getNom()+" = "+oVariable.getDesplacament()+"($gp)");
					/*      			 FIN ASIGNACIÓN DIRECCIÓN VARIABLE			      */
				}
			}
		}else{
			if (ts.obtenirBloc(0).obtenirConstant(oToken.image.toUpperCase())!=null && bTrobat==false) {
			//D: Es constante en bloque 0
			bTrobat=true;
			oConstant = ts.obtenirBloc(0).obtenirConstant(oToken.image.toUpperCase());
			osResult.setValue("TIPUS",(ITipus)oConstant.getTipus());
			osResult.setValue("ESTATIC", "CERT"); //Pq es una cte
			if(oConstant.getValor()!=null){
				//D: Verifico por posible valor nulo al ser de tipo indefinido
				osResult.setValue("VALOR",oConstant.getValor());
			}
			}
		}
		
		return osResult;
	}
}