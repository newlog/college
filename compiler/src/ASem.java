
import taulasimbols.*;

import java.io.*;
import java.util.*;

public class ASem {

	//Atributos
	public static TaulaSimbols ts = null;
	public static ASem_Dec aASem_Dec = null; 
	public static ASem_Exp aASem_Exp = null;
	public static ASem_Inst aASem_Inst = null;
	
	//Constructor: Crea Bloque actual y inicializa ts
	public ASem(){
		
		//Recupero la tabla de símbolos
		ts=Singleton_TS.getInstance();
	
		//Creamos el bloque actual, bloque 0
		Bloc bloc0 = new Bloc();
        ts.inserirBloc(bloc0);
        
		//Establezco el BloqueActual a 0
	    ts.setBlocActual(0);
	    
        //Creamos las clases a controlar
		aASem_Dec = new ASem_Dec();
	}
	
	public void insertaConstante(Token oToken, Semantic oExp){
		aASem_Dec.insertarConstante(oToken, oExp);
	}
	
	public void insertaVariable(Token oToken, ITipus oTipus){
		aASem_Dec.insertarVariable(oToken, oTipus);
	}
	
	public static Semantic insertarVector(Semantic s1, Semantic s2, Token oToken){
		return aASem_Dec.insertarVector(s1, s2, oToken);
	}
	
	public static Semantic insertarCadena(Semantic s1, Token oToken){
		return aASem_Dec.insertarCadena(s1, oToken);
	}
	
	public Funcio insertaFuncion(Funcio oFuncio, Token oTokenTipus) {
		//Verificar que no existe la función en la TS para el bloque actual (bloque 0)
		return aASem_Dec.insertarFuncion(oFuncio, oTokenTipus);
	}
	
	public void setTipusFuncion(Funcio oFuncio,Token oTokenTipus){
		//D: Modifico el tipo de la función con el tipo recibido
		aASem_Dec.setTipusFuncion(oFuncio, oTokenTipus);
	}
	
	public Funcio insertaParametro(Funcio oFuncio, Token oToken,ITipus oTipus,TipusPasParametre oTPasParametre){
		//D: Inserta el parámetro en función del Token, Tipo y Paso de parametros recibidos
		return aASem_Dec.insertaParametro(oFuncio,oToken,oTipus,oTPasParametre);
	}
  	
	public Semantic calcularUnario(Semantic osOper,Semantic osTerme, Token oToken){
		//D: Calcula el valor del Terme al aplicarle el operador y lo devuelve encapsulado en Semantic
		return aASem_Exp.calcularUnario(osOper, osTerme, oToken);
	}
	
	public Semantic calcularBinario(Semantic s1,Semantic soOper,Semantic s2,Token oToken){
		//D: Calcula el valor de operar s1 con s2 mediante soOper y lo devuelve encapsulado en Semantic
		return aASem_Exp.calcularBinario(s1, soOper, s2, oToken);
	}
	
	public Semantic calcularRelacional(Semantic osSem1,Token oToken,Semantic osSem2){
		//Calcula el valor relacional de operar s1 con s2 
		return aASem_Exp.calcularRelacional(osSem1, oToken, osSem2);
	}
	
	public Semantic makeCteEntera(Token oToken){
		//D: Genero constante entera
		return ASem_Exp.makeCteEntera(oToken);
	}
	
	public Semantic makeCteLogica(Token oToken){
		//D: Genero constante logica
		return ASem_Exp.makeCteLogica(oToken);
	}
	
	public Semantic makeCteCadena(Token oToken){
		//D: Genero constante cadena
		return ASem_Exp.makeCteCadena(oToken);
	}	
	
	public Semantic makeTrimCadena(Semantic s,Token oToken){
		//D: Devulevo cadena con TRIM hecho
		return ASem_Exp.makeTrimCadena(s,oToken);
	}
	
	public Semantic checkEsFuncion(Token oToken){
		
		Semantic osResult = new Semantic();
		
		//D: Verifico que es una función y devulevo el descriptor de la función asociado
		if(aASem_Dec.FUNCTION_IsNotInTS2(oToken)){
			//Error, el ID de la función no está en la TS
			Singleton_Error.getInstance().writeErrorSem(36, null, oToken);
			//Defino tipo indefinido para que no pete en cadena
			osResult.setValue("ESTATIC", "FALS");
			osResult.setValue("TIPUS", Utils.aTipusIndefinit);
		}else{
			osResult = aASem_Exp.getFuncion(oToken);			
		}
		return osResult;
	}
	
	public Semantic retornaSemFuncion(Semantic osFuncion){
		//D: Retorno Semantic con atributos encapsulados de la función
		return aASem_Exp.retornaSemFuncion(osFuncion);
	}
	
	public void checkFuncionSinParametros(Semantic osFuncion){
		//D: Verifico que la función no tiene parámetros (muestro error si corresponde)
		aASem_Exp.checkFuncionSinParametros(osFuncion);
	}
	
	public void checkFuncionParametro(Semantic osFuncion,Semantic osExpParametro){
		//D: Verifico cada parámetro y el tipo de paso en cada caso
		aASem_Exp.checkFuncionParametro(osFuncion, osExpParametro);
	}
	
	public static void checkFuncionFinParametros(Semantic osFuncion){
		//D: Verifico que no tengo que enviar más parámetros
		aASem_Exp.checkFuncionFinParametros(osFuncion);
	}
	
	public Semantic checkVector(Token oToken,Semantic osExp){
		//D: Verifico que sea vector y retorno semantic con parámetros
		return aASem_Exp.checkVector(oToken, osExp);
	}
	
	public Semantic checkEsCteVar(Token oToken){
		//D: Verifico si es CTE o VAR y retorno en consecuencia
		return aASem_Exp.checkEsCteVar(oToken);
	}
	
	public Semantic getAssignOperator(Token oToken){
		//D: Devuelvo Semantic con el operador encapsulado en ella
		return aASem_Inst.getAssignOperator(oToken);
	}
	
	public void checkAssign(Semantic osSem1,Semantic osOper,Semantic osSem2, Token oToken){
		//D: Verifico los tipos de la asingación
		aASem_Inst.checkAssign(osSem1, osOper, osSem2, oToken);
	}
	
	public void checkEscriure(Semantic osSem1,Token oToken){
		//D: Verifico que el tipo sea tipus simple o cadena
		aASem_Inst.checkEscriure(osSem1, oToken);
	}
	
	public void checkLlegir(Semantic osSem1,Token oToken){
		//D: Verifico que el tipo sea tipus simple o cadena
		//if(!aASem_Dec.isNotInTS(oToken)){ 
			//D: Si no está en TS ya se habrá mostrado el error, no tengo que hacer nada
			aASem_Inst.checkLlegir(osSem1, oToken);
		//}				
	}
	
	public Semantic checkSurticicle(Semantic osH, Token oToken){
		//Verifico surtcicle dentro de cicle
		return aASem_Inst.checkSurticicle(osH, oToken);
	}
	
	public void checkWarningCicle(Semantic osH, Token oToken){
		//Verifico cicle sin surtcicle para mostrar warning
		aASem_Inst.checkWarningCicle(osH, oToken);
	}
	
	public void checkSi(Semantic osSem,Token oToken){
		//Verifico que la condicion sea de tipus LOGIC
		aASem_Inst.checkSi(osSem, oToken);
	}
	
	public void checkPer(Token oToken, Semantic osSem1, Semantic osSem2){
		//Verifico parametros de PER
		Semantic osSemVar = new Semantic();
		osSemVar = aASem_Exp.checkEsCteVar(oToken);
		aASem_Inst.checkPer(osSemVar, osSem1, osSem2, oToken);
	}
	
	public void checkFiFuncio(Semantic osH,Token oToken){
		//Verifico que a funcio tiene RETORNAR
		aASem_Inst.checkFiFuncio(osH, oToken);
	}
	
	public Semantic checkRetornar(Semantic osH, Token oToken, Semantic osSemRetorar){
		//Establezco valor de BFUNCIO en retornar
		return aASem_Inst.checkRetornar(osH, oToken, osSemRetorar);
	}
}
