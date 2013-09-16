
import java.util.Hashtable;
import java.util.Vector;
import taulasimbols.*;


public class ASem_Dec extends Utils {
	//Variable de desplazamiento general
	public static int iDesplazamiento = 0;
	public static int iDesplazamientoParametros = 0;
	private int iStringSize = 0;
	private int iVectorSize = 0;
//	public Hashtable<String, String> listaFunciones = new Hashtable<String, String>();
	
	public void insertarConstante(Token oToken, Semantic oExp){
		
		//Variables
		Object oObjectValor;
		TaulaSimbols ts=Singleton_TS.getInstance();
		
		//Verifico que es estática
		if(((String)oExp.getValue("ESTATIC")).toUpperCase().equals("CERT")){
			oObjectValor = oExp.getValue("VALOR");
		}else{
			//No es estática, no conozco su valor. Le asignaré un valor de 1 que no comprometa el código
			Singleton_Error.getInstance("").writeErrorSem(2, null, oToken);
			oObjectValor = new Integer(1);
		}
		
		//Verifico que no esté ya insertada
		if(this.isNotInTS(oToken)){
			//Creo la constante y la inserto en el bloque actual
			Constant cte = new Constant(oToken.image.toUpperCase(), (ITipus) oExp.getValue("TIPUS"), oObjectValor);
			ts.obtenirBloc(ts.getBlocActual()).inserirConstant(cte);
			System.out.println("TS Despues de insertar cte: " + ts.toXml());
		}else{
			//La CONSTANTE ya existe en el bloque actual
			Singleton_Error.getInstance().writeErrorSem(1, null, oToken);
		}
	}
	
	public void insertarVariable(Token oToken,ITipus oTipo){
		
		//Variables
		Object oObjectValor;
		TaulaSimbols ts=Singleton_TS.getInstance();
		//Verifico que no esté ya insertada
		if(this.isNotInTS(oToken)){
			//Creo la variable y la inserto en el bloque actual
			Variable var = new Variable(oToken.image.toUpperCase(),oTipo,0);
			
			/*****ASIGNACION DEL DESPLAZAMIENTO EN MEMORIA DE CADA VARIABLE (SE AUMENTA POSITIVAMENTE)	  *****/
			var.setDesplacament(iDesplazamiento);
			/*****											FIN											  *****/
			
			ts.obtenirBloc(ts.getBlocActual()).inserirVariable(var);
			
			/*****				 			AUMENTO GENERAL DEL DESPLAZAMIENTO							  *****/
			//El desplazamiento depende del TIPUS (oTipo). (Sencer, logic, cadena, vector)
			if ( oTipo.getNom().compareTo("SENCER") == 0 ) {
				iDesplazamiento += 4;	//Sumamos cuatro ya que un entero ocupa 4 bytes
			}else if ( oTipo.getNom().compareTo("LOGIC") == 0 ) {
				iDesplazamiento += 4;
			} else if ( oTipo.getNom().substring(0, 1).compareTo("V") == 0 ) {	//getNom() nos devuelve V_X_Y_S y nosotros cojemos la V
				iDesplazamiento += iVectorSize;	//Sumamos iVectorSize que lo calculamos en el método insertaVector()
			} else if( oTipo.getNom().compareTo("CADENA") == 0 ) {
				iDesplazamiento += iStringSize;	//Sumamos iStringSize que lo calculamos en el método insertaCadena()
			}
			/*****				 			FIN AUMENTO GENERAL DEL DESPLAZAMIENTO						 *****/
			
			System.out.println("TS Despues de insertar var: " + ts.toXml());
		}else{
			//La VARIABLE ya existe en el bloque actual
			Singleton_Error.getInstance().writeErrorSem(3, null, oToken);
		}
	}

	public void insertarVariableParametro(Token oToken,ITipus oTipo, Parametre oParam){
		
		//Variables
		Object oObjectValor;
		TaulaSimbols ts=Singleton_TS.getInstance();
		//Verifico que no esté ya insertada
		if(this.isNotInTS(oToken)){
			//Creo la variable y la inserto en el bloque actual
			//Parametre param = new Parametre(oToken.image.toUpperCase(),oTipo,0);
			
			/*****ASIGNACION DEL DESPLAZAMIENTO EN MEMORIA DE CADA VARIABLE (SE AUMENTA POSITIVAMENTE)	  *****/
			//param.setDesplacament(iDesplazamientoParametros);
			oParam.setDesplacament(iDesplazamientoParametros);
			/*****											FIN											  *****/
			
			ts.obtenirBloc(ts.getBlocActual()).inserirVariable(oParam);
			
			/*****				 			AUMENTO GENERAL DEL DESPLAZAMIENTO							  *****/
			//El desplazamiento depende del TIPUS (oTipo). (Sencer, logic, cadena, vector)
			if ( oParam.getTipus().getNom().compareTo("SENCER") == 0 ) {
				iDesplazamientoParametros += 4;	//Sumamos cuatro ya que un entero ocupa 4 bytes
			}else if ( oParam.getTipus().getNom().compareTo("LOGIC") == 0 ) {
				iDesplazamientoParametros += 4;
			} else if ( oParam.getTipus().getNom().substring(0, 1).compareTo("V") == 0 ) {	//getNom() nos devuelve V_X_Y_S y nosotros cojemos la V
				iDesplazamientoParametros += iVectorSize;	//Sumamos iVectorSize que lo calculamos en el método insertaVector()
			} else if( oParam.getTipus().getNom().compareTo("CADENA") == 0 ) {
				iDesplazamientoParametros += iStringSize;	//Sumamos iStringSize que lo calculamos en el método insertaCadena()
			}
			/*****				 			FIN AUMENTO GENERAL DEL DESPLAZAMIENTO						 *****/
			
			System.out.println("TS Despues de insertar var: " + ts.toXml());
		}else{
			//La VARIABLE ya existe en el bloque actual
			Singleton_Error.getInstance().writeErrorSem(3, null, oToken);
		}
	}
	
	public   Semantic insertarVector(Semantic s1, Semantic s2, Token oToken){
		iVectorSize = 0;
		/*
		 * Verificar:
		 * 1- Que los tipos de los límites están definidos 
		 * 2- Que los límites son ESTATICOS
		 * 3- Que los limites son SENCERS
		 * 4- Que el primero es inferior al segundo
		 */
		
		//Variables
		Semantic oSemantic = new Semantic();
		Boolean bVerif = false;
		ITipus oTipus1 = (ITipus)s1.getValue("TIPUS");
		ITipus oTipus2 = (ITipus)s2.getValue("TIPUS");
		Integer nValor1 = 0;
		Integer nValor2 = 0;
		
		//Si los tipos son indefinidos no hagas nada
		if(!(oTipus1.equals(Utils.aTipusIndefinit))&&!(oTipus2.equals(Utils.aTipusIndefinit))){		
			//D: Miro que los valores sean estáticos
			if(s1.getValue("ESTATIC").equals("CERT") && s2.getValue("ESTATIC").equals("CERT")){	
				//D: Miro que el tipo de los límites sean iguales
				if(oTipus1.getNom().equals(oTipus2.getNom())){
					//D: Miro que el tipo de los límites sean sencers (y por tanto tampoco sean indefinidos)
					if(oTipus1.equals(Utils.getTipusSimple("SENCER"))){
						//D: Miro que el valor del primero sea menor o igual que el del segundo
						nValor1 = (Integer) s1.getValue("VALOR");
						nValor2 = (Integer) s2.getValue("VALOR");
						if(nValor1<=nValor2){
							bVerif=true;
						}else{
							//D: Los valores son decrecientes
							Singleton_Error.getInstance().writeErrorSem(7, null, oToken);
						}
					}else{
						//D: Los valores no son SENCERS
						Singleton_Error.getInstance().writeErrorSem(6, null, oToken);
					}
				}else{
					//D: Los limites son de diferente tipo
					Singleton_Error.getInstance().writeErrorSem(30, null, oToken);
				}
			}else{
				//D: Los valores no son estáticos
				Singleton_Error.getInstance().writeErrorSem(8, null, oToken);
			}
		}else{
			//D: Los valores no son SENCERS
			Singleton_Error.getInstance().writeErrorSem(6, null, oToken);
		}
		
		if(bVerif){
			//D: Retornamos semantic con los tipos del vector
			oSemantic.setValue("TIPUS", Utils.getTipusVector(nValor1,nValor2,oToken));
			//Almacenamos el tamaño del vector para poder aumentar el iDesplazamiento de manera correcta
			iVectorSize = (nValor2.intValue() - nValor1.intValue() + 1) * 4; //Un sencer o un logic ocupan 4 bytes
		}else{
			//D: Retornamos semantic asignando unos tipos por defecto al vector
			oSemantic.setValue("TIPUS", Utils.getTipusVector(1,55,oToken));
		}
		
		return oSemantic;
	}
	
public  Semantic insertarCadena(Semantic s1, Token oToken){
		
		/*
		 * Verificar:
		 * 1- Que el tipo del límite está definido
		 * 2- Que el límite es ESTATICO
		 * 3- Que el límite es ENTERO
		 */
	
		//Variables
		Semantic oSemantic = new Semantic();
		Boolean bVerif = false;
		ITipus oTipus1 = (ITipus)s1.getValue("TIPUS");
		Integer nValor1 = 0;
		//Variable para la generacion de código
		iStringSize = 0;
		
		//Miro que el valor sea estático
		if(s1.getValue("ESTATIC").equals("CERT")){	
			//Miro que el tipo del límite sea ENTERO (y por tanto tampoco sean indefinido)
			if(oTipus1.equals(Utils.getTipusSimple("SENCER"))){
				//Miro que el valor sea meyor que cero
				nValor1 = (Integer) s1.getValue("VALOR");
				if(nValor1>0){
					bVerif=true;
				}else{
					//La longitud de la cadena es 0
					Singleton_Error.getInstance().writeErrorSem(33, null, oToken);
				}
			}else{
				//El valor no es ENTERO
				Singleton_Error.getInstance().writeErrorSem(32, null, oToken);
			}
		}else{
			//El valor no es estático
			Singleton_Error.getInstance().writeErrorSem(31, null, oToken);
		}
		
		if(bVerif){
			//Retornamos semantic con los tipos de la cadena
			oSemantic.setValue("TIPUS", Utils.getTipusCadena(nValor1));
			iStringSize = nValor1.intValue() * 1; //Cada caracter ocupa un byte
		}else{
			//Retornamos semantic asignando unos atributos por defecto a la cadena
			oSemantic.setValue("TIPUS", Utils.getTipusCadena(10));
		}
		
		return oSemantic;
	}
	
public Funcio insertarFuncion(Funcio oFuncio, Token oToken){
	//A: Hemos de verificar que la función no esté declarada en el contexto actual. 
	//A: Hemos de insertar la función en la tabla de símbolos

	/* GC: INICIALIZACIÓN DEL DESPLAZAMIENTO GLOBAL DE LA FUNCIÓN */
	iDesplazamientoParametros = 12;
	/*								FIN 					 	  */
	
	//Variables
	TaulaSimbols ts=Singleton_TS.getInstance();
	Funcio oResFuncio = null;
	
	oFuncio.setTipus(Utils.aTipusIndefinit);//D: Como al llamar a este procedimiento todavía no sé el tipo, de momento le asigno indefinido para evitar errores
	oFuncio.setDeplacament(0); //Le asigno el desplazamiento en vistas a la generación de código
	oFuncio.setNom(oToken.image.toUpperCase());//Le asigno el nombre recibido a traves del token
	
	//D: Verifico que el ID de la función no esté en el bloque actual (BLOQUE 0)
	if(this.isNotInTS(oToken)) {
		ts.obtenirBloc(0).inserirProcediment(oFuncio); //D: Inserto la función (que sólo tiene ID y desplazamiento)
		oResFuncio = (Funcio) ts.obtenirBloc(0).obtenirProcediment(oToken.image.toUpperCase());
		//D: En este punto no puedo pintar nada por pantalla pq la funcion aún no tiene tipo
	}else{
		//D: Mostramos error de función doblemente definida
		if(ts.obtenirBloc(0).obtenirProcediment(oToken.image.toUpperCase())!=null){
			//La funcio esta doblement definida
			Singleton_Error.getInstance().writeErrorSem(4, null, oToken);
		}else{
			//El ID ja esta definit per una altra cosa
			Singleton_Error.getInstance().writeErrorSem(44, null, oToken);
		}
		
		//D: Es un error, no quiero que la inserte en la TS. A partir de este punto, en oFuncio se insertaran las variables
		//y parametros pero como no se insertará en la TS no pasará nada. En caso de que la función erronea (con ID repetido)
		//contenga errores, los mostraré normalmente, pero ya habré indicado que la funcion es erronea. 
		//D: Puede dar un error de parametros si dentro de esta funcion erronea vuelvo a llamarla a sí misma, puesto que estaría llamando a la original. Eso es inevitable.
		//D: Puesto que no la inserto, devuelvo el mismo objeto que he recibido
		oResFuncio = oFuncio;
	}
	
	return oResFuncio;
}

public void setTipusFuncion(Funcio oFuncio, Token oToken){
	
	TaulaSimbols ts = Singleton_TS.getInstance();
	
	//D: Verifica que el Tipus es tipusSimple. Si no, establece a indefinido
	if(Utils.getTipusSimple(oToken.image).equals(Utils.aTipusIndefinit)){
		//D: Mostramos error, la función debería retornar un TipusSimple
		Singleton_Error.getInstance().writeErrorSem(34, null, null);
	}
	//D: Asignamos el tipo (si no es TipusSimple internamente ya se asigna un TipusIndefinit)
	if(ts.obtenirBloc(0).obtenirProcediment(oFuncio.getNom())!=null){ //Miro que realmente la tenga en la TS, que no haya habido error por duplicar ID
		((Funcio)ts.obtenirBloc(0).obtenirProcediment(oFuncio.getNom())).setTipus(Utils.getTipusSimple(oToken.image));
		System.out.println("TS Despues de modificar tipus funcion: " + ts.toXml());
	}
		
}

public Funcio insertaParametro(Funcio oFuncio, Token oToken,ITipus oTipus,TipusPasParametre oTPasParametre){
  	//Se ha de guardar el tipo del parametro, su id y como se pasa a la función
  	Parametre oParam = new Parametre(oToken.image.toUpperCase(), oTipus, 0, oTPasParametre); //Desplazamiento de 0
  	//En el objeto Funcio añadimos un parametro
  	oFuncio.inserirParametre(oParam);
  	//Insertamos la variable en el bloque atual (bloque 1 por ser una función)
  	//insertarVariable(Babel2009.getToken(0),oTipus);
  	insertarVariableParametro(Babel2009.getToken(0),oTipus, oParam);
	/* GENERACIÓN DE CÓDIGO: insertamos el nombre de la función */
	//listaFunciones.put(oToken.image.toUpperCase(), oToken.image.toUpperCase());
	/*				 			FIN 							*/
	
	
  	//Retornamos el objeto Función con el parámetro añadido
  	return oFuncio;
}

public boolean isNotInTS(Token oToken){
	
	boolean bResult=false;
	
	//D: Tal y como indican las especificaciones, verifico que el ID no exista ni en CTE ni en VAR ni como FUNCION para ba
	if(CTE_IsNotInTS(oToken)){
		if(VAR_IsNotInTS(oToken)){
			if(FUNCTION_IsNotInTS(oToken)){
				bResult = true;
			}
		}
	}
	
	return bResult;
}

public boolean isNotInTS2(Token oToken){
	
	boolean bResult=false;
	
	//D: Tal y como indican las especificaciones, verifico que el ID no exista ni en CTE ni en VAR ni como FUNCION para ba
	if(CTE_IsNotInTS(oToken)){
		if(VAR_IsNotInTS(oToken)){
			if(FUNCTION_IsNotInTS2(oToken)){
				bResult = true;
			}
		}
	}
	
	return bResult;
}

public boolean CTE_IsNotInTS(Token oToken){
	
	boolean bResult=false;
	TaulaSimbols ts = Singleton_TS.getInstance();
	
	//Busco la CONSTANTE en el bloque 0
//	if(ts.obtenirBloc(0).obtenirConstant(oToken.image.toUpperCase())==null){
//		bResult=true;
//	}
	//Busco la CONSTANTE en el bloque 1 o 0
	if(ts.obtenirBloc(ts.getBlocActual()).obtenirConstant(oToken.image.toUpperCase())==null /*&& bResult==true*/){
		bResult=true;
	}else{
		bResult=false; //NO esta al bloc 0 i SI Esta al bloc1
	}
	
	return bResult;
}

public boolean VAR_IsNotInTS(Token oToken){
	
	boolean bResult=false;
	TaulaSimbols ts = Singleton_TS.getInstance();
	
	//Busco la VARIABLE en el bloque 0
//	if(ts.obtenirBloc(0).obtenirVariable(oToken.image.toUpperCase())==null){
//		bResult=true; //No hi es al bloc 0
//	}
	//Busco la VARIABLE en el bloque 0 o 1
	if(ts.obtenirBloc(ts.getBlocActual()).obtenirVariable(oToken.image.toUpperCase())==null /*&& bResult==true*/){
		bResult=true; //NO esta ni al bloc 0 ni al bloc 1
	}else{
		bResult=false; //NO esta al bloc 0 i SI Esta al bloc1
	}
	
	return bResult;
}	

public boolean FUNCTION_IsNotInTS(Token oToken){
		
	boolean bResult=false;
	TaulaSimbols ts = Singleton_TS.getInstance();
	//Cuando lea id de funcion
	
	//Cuando declaro variable (bloque 1 OK) 
		
	//D: Busco la FUNCION en el bloque 0, pq es el único donde se declaran funciones
	//D: Ojo, no pongo 0 pq SI que puedo declarar dentro de una función una variable que tiene como nombre el mismo 
	//que una función (declarada en bloque 0). Si pusiese 0 vería que en el Bloque 0 existe el ID, pero como las 
	//especificaciones dice: "No pot existir un altre identificador amb el mateix nom en el mateix bloc", el bloque 0 
	//en este caso no sería el mismo bloque.
	
	if(ts.obtenirBloc(ts.getBlocActual()).obtenirProcediment(oToken.image.toUpperCase())==null){
		bResult=true;
	}
	
	return bResult;
	
}

public boolean FUNCTION_IsNotInTS2(Token oToken){
	//Cuando inserte la funcion que solo mire en el bloque 0
	boolean bResult=false;
	TaulaSimbols ts = Singleton_TS.getInstance();
	
	//D: Busco la FUNCION en el bloque 0, pq es el único donde se declaran funciones
	//D: Ojo, no pongo 0 pq SI que puedo declarar dentro de una función una variable que tiene como nombre el mismo 
	//que una función (declarada en bloque 0). Si pusiese 0 vería que en el Bloque 0 existe el ID, pero como las 
	//especificaciones dice: "No pot existir un altre identificador amb el mateix nom en el mateix bloc", el bloque 0 
	//en este caso no sería el mismo bloque.
	
	if(ts.obtenirBloc(0).obtenirProcediment(oToken.image.toUpperCase())==null){
		bResult=true;
	}
	
	return bResult;
	
}

}
