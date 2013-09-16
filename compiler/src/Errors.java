/*
 *
 *	Errors, classe que gestiona l'impresiÃ³ dels errors en els fitxers.
 *
 */
import java.io.*;
import java.util.*;

public class Errors
{
	/*Errors lexics*/
	public static final int ERR_LEX_1  = 1;
	public static final int ERR_LEX_2  = 2;
	/*El fitxer d'error*/
	public static FileWriter m_fwFitxer;
	public static BufferedWriter m_fitxer;

	public Errors(String nomFitxer)
	{
		try {
			m_fwFitxer = new FileWriter(nomFitxer);
			m_fitxer   = new BufferedWriter(m_fwFitxer);
		} catch(IOException ioe) {
			ioe.printStackTrace(System.err);
		}
	}

	public static void tanca()
	{
		try {
			m_fitxer.close();
			m_fwFitxer.close();
		} catch(IOException ioe) {
			ioe.printStackTrace(System.err);
		}
	}
	//IMAGINO QUE AIXÒ HO HEM DE TREURE, NO?
	public static void errorLexic(int tipus, Token matchedToken)
	{
 		String error;
		if(tipus == ERR_LEX_1) {
			error  = new String("[ERR_LEX_1] "+matchedToken.beginLine+", carÃ cter["+matchedToken.image+"] desconegut");
		} else {
            error = new String("[ERR_LEX_2] "+matchedToken.beginLine+", identificador ["+matchedToken.image+"] massa llarg");
		}
		try {
			m_fitxer.write(error);
			m_fitxer.newLine();
			m_fitxer.flush();
		} catch(IOException ioe) {
			ioe.printStackTrace(System.err);
		}
		System.out.println(error); //per mostrar-ho per pantalla
	}
	
	public void writeErrorSem(Integer idError, Semantic oSemantic, Token oToken){
		
		String sError = "";
		switch(idError){
					//Errores de enunciado:
					case 1:		sError = "[ERR_SEM_1] "+oToken.beginLine+", Constant ["+oToken.image+"] doblement definida"; break;
					case 2:		sError = "[ERR_SEM_2] "+oToken.beginLine+", Expressió no estàtica en declaració de constant"; break;
					case 3:		sError = "[ERR_SEM_3] "+oToken.beginLine+", Variable ["+oToken.image+"] doblement definida"; break;
					case 4:		sError = "[ERR_SEM_4] "+oToken.beginLine+", Funció doblement definida"; break;
					case 5:		sError = "[ERR_SEM_5] "+oToken.beginLine+", Paràmetre ["+oToken.image+"] doblement definit"; break;
					case 6:		sError = "[ERR_SEM_6] "+oToken.beginLine+", Tipus no sencers en límits de vector"; break;
					case 7:		sError = "[ERR_SEM_7] "+oToken.beginLine+", Límits decreixents en vector"; break;
					case 8:		sError = "[ERR_SEM_8] "+oToken.beginLine+", L’expressió en el límit del vector no és estàtica"; break;
					case 9:		sError = "[ERR_SEM_9] "+oToken.beginLine+", El tipus de l’expressió no és SENCER"; break;
					case 10:	sError = "[ERR_SEM_10] "+oToken.beginLine+", El tipus de l’expressió no és LOGIC"; break;
					case 11:	sError = "[ERR_SEM_11] "+oToken.beginLine+", El tipus de l’expressió no és CADENA"; break;
					case 12:	sError = "[ERR_SEM_12] "+oToken.beginLine+", La condició no és de tipus LOGIC"; break;
					case 13:	sError = "[ERR_SEM_13] "+oToken.beginLine+", L’identificador ["+oToken.image+"] no ha estat declarat"; break;
					case 14:	sError = "[ERR_SEM_14] "+oToken.beginLine+", L’identificador ["+oToken.image+"] en la instrucció LLEGIR no és una variable"; break;
					case 15:	sError = "[ERR_SEM_15] "+oToken.beginLine+", L’identificador ["+oToken.image+"] en la instrucció LLEGIR no és una variable de tipus simple"; break;
					case 16:	sError = "[ERR_SEM_16] "+oToken.beginLine+", L’identificador ["+oToken.image+"] en part esquerra d’assignació no és una variable"; break;
					case 17:	sError = "[ERR_SEM_17] "+oToken.beginLine+", La variable ["+oToken.image+"] i l'expressió d'assignació tenen tipus diferents. El tipus de la variable és ["+(String)oSemantic.getValue("TIPUS_VAR")+"] i el de l’expressió és ["+(String)oSemantic.getValue("TIPUS_EXP")+"]"; break;
					case 18:	sError = "[ERR_SEM_18] "+oToken.beginLine+", El tipus de l’índex d’accés del vector no és SENCER"; break;
					case 19:	sError = "[ERR_SEM_19] "+oToken.beginLine+", El tipus de l'expressió en ESCRIURE no és simple o no és una constant cadena"; break;
					case 20:	sError = "[ERR_SEM_20] "+oToken.beginLine+", La funció en declaració té ["+(Integer)oSemantic.getValue("PARAM_DEC")+"] paràmetres mentre que en ús té ["+(Integer)oSemantic.getValue("PARAM_US")+"]"; break;
					case 21:	sError = "[ERR_SEM_21] "+oToken.beginLine+", El tipus del paràmetre número ["+(Integer)oSemantic.getValue("NUM_PARAM")+"] de la funció no coincideix amb el tipus en la seva declaració ["+(String)oSemantic.getValue("TIPUS_PARAM")+"]"; break;
					case 22:	sError = "[ERR_SEM_22] "+oToken.beginLine+", El paràmetre número ["+(Integer)oSemantic.getValue("NUM_PARAM")+"] de la funció no es pot passar per referència"; break;
					case 23:	sError = "[ERR_SEM_23] "+oToken.beginLine+", La funció ["+(String)oSemantic.getValue("NOMFUNCIO")+"] ha de ser del tipus ["+(String)oSemantic.getValue("TIPUSFUNCIO")+"] però en la expressió del seu valor el tipus és ["+(String)oSemantic.getValue("TIPUSEXP")+"]"; break;
					case 24:	sError = "[ERR_SEM_24] "+oToken.beginLine+", S'ha excedit el límit del vector ["+oToken.image+"] , índex fora de rang."; break;
					case 25:	sError = "[ERR_SEM_25] "+oToken.beginLine+", L’identificador ["+oToken.image+"] no és de tipus sencer a l’instrucció PER"; break;
					case 26:	sError = "[ERR_SEM_26] "+oToken.beginLine+", SURTCICLE fora de CICLE"; break;
					case 101:	sError = "[WARNING_1] "+oToken.beginLine+", CICLE sense SURTCICLE"; break;
					
					//Errores propios:
					case 30:	sError = "[ERR_SEM_30] "+oToken.beginLine+", Els límits del vector són de diferent tipus"; break;
					case 31:	sError = "[ERR_SEM_31] "+oToken.beginLine+", L’expressió en la longitud de la cadena no és estàtica"; break;
					case 32:	sError = "[ERR_SEM_32] "+oToken.beginLine+", Tipus no sencers en la longitud de la cadena"; break;
					case 33:	sError = "[ERR_SEM_33] "+oToken.beginLine+", La longitud de la cadena no pot ser 0"; break;
					case 34:	sError = "[ERR_SEM_34] "+oToken.beginLine+", La funció ha de retornar un tipus simple (sencer o logic)"; break;
					case 35:	sError = "[ERR_SEM_35] "+oToken.beginLine+", No és possible fer una divisió entre 0"; break;
					case 36:	sError = "[ERR_SEM_36] "+oToken.beginLine+", La funció ["+oToken.image+"] no ha estat declarada"; break;
					case 37:	sError = "[ERR_SEM_37] "+oToken.beginLine+", L'identificador ["+oToken.image+"] no ha estat declarat com a VECTOR"; break;
					case 38:	sError = "[ERR_SEM_38] "+oToken.beginLine+", L’identificador ["+oToken.image+"] no ha estat declarat com a constant o variable"; break;
					case 39:	sError = "[ERR_SEM_39] "+oToken.beginLine+", L’identificador ["+oToken.image+"] en part esquerra d’assignació no és una cadena. No es pot concatenar"; break;
					case 40:	sError = "[ERR_SEM_40] "+oToken.beginLine+", La part dreta d’assignació no és de tipus cadena. No es pot concatenar"; break;
					case 41:	sError = "[ERR_SEM_41] "+oToken.beginLine+", L’identificador ["+oToken.image+"] en part esquerra d’assignació no és un sencer. No es pot operar"; break;
					case 42:	sError = "[ERR_SEM_42] "+oToken.beginLine+", La part dreta d’assignació no és de tipus sencer. No es pot operar"; break;
					case 43:	sError = "[ERR_SEM_43] "+oToken.beginLine+", La variable ["+oToken.image+"] de tipus [CADENA] i l'expressió d'assignació no són estructuralment equivalents. El tipus de la variable té longitud ["+(String)oSemantic.getValue("LONG_VAR")+"] mentre que el de l’expressió té longitud ["+(String)oSemantic.getValue("LONG_EXP")+"]"; break;
					case 44:	sError = "[ERR_SEM_44] "+oToken.beginLine+", L'identificador de la Funció ja ha estat declarat prèviament per a una constant o variable"; break;
					case 45:	sError = "[ERR_SEM_45] "+oToken.beginLine+", La variable i l'expressió d'assignació són vectors però no són estructuralment equivalents"; break;
					case 46:	sError = "[ERR_SEM_46] "+oToken.beginLine+", Hi ha més d'un SURTCICLE al mateix bloc d'instruccions"; break;
					case 47:	sError = "[ERR_SEM_47] "+oToken.beginLine+", Tipus no aptes per l'operador relacional"; break;
					case 48:	sError = "[ERR_SEM_48] "+oToken.beginLine+", La instrucció PER no es pot realitzar degut a que els seus límits no són sencers"; break;
					case 49:	sError = "[ERR_SEM_49] "+oToken.beginLine+", Hi ha més d'un RETORNAR al mateix bloc d'instruccions de la funció"; break;
					case 50:	sError = "[ERR_SEM_50] "+oToken.beginLine+", RETORNAR fora de FUNCIO"; break;
					case 51:	sError = "[ERR_SEM_51] "+oToken.beginLine+", FUNCIO sense RETORNAR"; break;
					case 52:	sError = "[ERR_SEM_52] "+oToken.beginLine+", El tipus del paràmetre número ["+(Integer)oSemantic.getValue("NUM_PARAM")+"] de la funció és tipus CADENA però no és estructuralment equivalent al declarat, de longitud ["+(Integer)oSemantic.getValue("TIPUS_LONG")+"]"; break;
					case 53:	sError = "[ERR_SEM_53] "+oToken.beginLine+", El tipus del paràmetre número ["+(Integer)oSemantic.getValue("NUM_PARAM")+"] de la funció és tipus VECTOR però no és estructuralment equivalent al declarat"; break;
					case 54:	sError = "[ERR_SEM_54] "+oToken.beginLine+", El paràmetre número ["+(Integer)oSemantic.getValue("NUM_PARAM")+"] de la funció correspon al resultat d'una funció, i no es pot passar per referència"; break;
					case 55:	sError = "[ERR_SEM_55] "+oToken.beginLine+", El número de paràmetres en ús excedeixen els declarats"; break;				
		
					
					//ERRORES DE GENERACIÓN DE CÓDIGO
					
					
					
		}
		try {
			m_fitxer.write(sError);
			m_fitxer.newLine();
		} catch (IOException e) { System.out.println("No se ha podido escribir en el archivo");}
	}

} //fi de la classe
