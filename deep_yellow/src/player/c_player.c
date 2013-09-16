#include "c_player.h"
#include "configuracio.h"


/* �s el procediment principal, reb per l�nia de comandes 3 par�metres com a 
* m�xim, el nom, el color de les fitxes i la ip del servidor
*/
int main(void)
{
	if ( initLog(ALL, FILE_MODE) != 0) {
		fprintf(stderr, "[-] The log library could not be initialized.");
		exit(-1);
	}

	int sd,ok;	
	char nom[ MAX_BUFFER ] = {0};				//Par�metres de la conexi�
	int port;
	char direccio[ 20 ] = {0};
	char fitxes[ 20 ] = {0};
	//char buffer[ MAX_BUFFER ]= {0};				//Buffer per emmagatzemar la informaci� que s�envia o rep.
	jugada info;
	int mode=0;						//Indica si es juga mode huma o maquina

	ok=Inici_Entorn(&port,direccio,nom,&info,&mode);
	if (ok==1) {
		sd = Conectar(direccio,port);		
		if (sd>0) {			//Hem pogut establir el socket
			printf("\nNou jugador %s amb les fitxes: %s en el servidor: %s\n",nom,fitxes,direccio);
			Inici_Partida(sd,nom,info,mode);	//Iniciem la partida
			Desconectar(sd);
		} else {				//No hem pogut comunicarnos
			printf("\nNo s�ha pogut connectar amb el servidor");
		}		
	} else {
		printf("\nAplicacio Player finalitzada");	
	}
	printf("\n");
	return 0;	
}


/* S'encarrega de controlar la inicialitzaci� de totes les dades necessaris per poder jugar en una partida
*   Retorna puerto: Port al que ens conectarem
*  Retorna direccio: Direccio ip del servidor
*  Retorna nom: Login del jugador
*  Retorna info: L'estructura del jugador inicialitzada
*  Retorna estat de si cal connectar-se per jugar
*  Retorna mode: 1: Huma, 0: Maquina
*/
int Inici_Entorn(int *port, char direccio[20],char nom[MAX_BUFFER],jugada * info,int * mode)	{
	int estat=0;	
	int temps=30;
	char aux[20];
	char fitxes[10];
	Inicialitza(port,direccio); 		    		//Obtenim els par�metres de conexi� cap al servidor
		
	estat=Realitza_Operacio(port,direccio,&temps,fitxes,nom);
	if (estat==1)	{
		do	{
			printf("\nEn quin mode vols jugar la partida? (1.Huma / 2. Maquina)");			
			fflush(stdin);
			scanf(" %[^\n]",aux);
			if ((strcmp(aux,"1")==0)||(strcmp(aux,"2")==0))	{
				*mode=atoi(aux);	
			}
		
		}while (*mode==0);			
		Inicialitza_Jugada(info,fitxes,temps);		//Inicialitzem l'estructura de l'alumne	
	}
	
	return estat;
}

/** S'encarrega d'obtenir els parametres necessaris a traves del servidor en funcio de l'operaci� que volgui fer-se
*  Par�metre direccio: Direccio ip del servidor
*  Retorna puerto: Port al que ens conectarem per jugar. Quan ens el passen indica el port de peticions del servidor
*  Retorna temps: �s el temps que t� cada jugador
*  Retorna jugar: 1 si jugarem en una partida, 0 si volem sortir
*/
int Realitza_Operacio(int * port,char direccio[ 20 ],int * temps,char fitxes[ 10 ],char login[ MAX_BUFFER ])
{
	int port_server = *port;			//Ens apuntem el port de peticios del servidor.
	int jugar = 0;
	char opcio[ 10 ] = {0};
	int sortir = 0;
		do {
			printf("\n\n\n*****Modul Jugador Player 6.6*****\n");
			printf("\nQuina operacio vols realitzar:\n");
			printf("\n\t1.Crear Partida\n");
			printf("\n\t2.Unir Partida\n");
			printf("\n\t3.Llistat de Partides disponibles\n");
			printf("\n\t4.Sortir\n");			
			printf("\n\t\tOpcio:");
			fflush(stdin);
			scanf(" %[^\n]",opcio);
			
			if (strcmp(opcio,"1")==0)	{
				jugar=Crear_Partida(port_server,port,direccio,temps,fitxes,login);
			}else if (strcmp(opcio,"2")==0)	{
				jugar=Unir_Partida(port_server,port,direccio,temps,fitxes,login);
			}else if (strcmp(opcio,"3")==0)	{	
				Llistar_Partides(port_server,direccio);
			}else if (strcmp(opcio,"4")==0)	{
				sortir=1;
				jugar=0;
			}else	{
				printf("\nIntrodueix una opcio correcta");	
			}
			if (jugar==1)	{			//Si s'ha creat la partida o ens unit a alguna
				sortir=1;	
			}		
		}while (sortir==0);
	return jugar;
}

/** Permet unirse a una partida ja existent
*  Par�metre port_server: Indica el port de les peticions del servidor
*  Retorna port: Port al que ens conectarem per jugar. Quan ens el passen indica el port de peticions del servidor
*  Par�metre direccio: Direccio ip del servidor
*  Retorna temps: �s el temps que t� cada jugador
*  Retorna fitxes: Fitxes amb les que jugara
*  Retorna nom: El nom del jugador
*  Retorna jugar: 1 si jugarem en una partida, 0 en cas contrari
*/
int Crear_Partida(int port_server, int * port,char direccio[ 20 ],int * temps,char fitxes[ 10 ],char login[ MAX_BUFFER ])
{		
	int jugar=0;
	int t = 0,i = 0,sd = 0;	
	char nomP[ 16 ] = {0};
	char passw[ 10 ] = {0};	
	char passwlogin[ 10 ] = {0};
	char aux[ MAX_BUFFER ] = {0};
	char portS[ 20 ] = {0};
	char msg[ MAX_BUFFER ] = {0};	
	char tempsPartida[ 20 ] = {0};
				
	Obtenir_Dades(login,passwlogin,nomP,passw,fitxes);	//Obtenim les dades de la partida a la que ens volem unir
	i = 0;									
	do	{
		printf("\nIntrodueix el temps(en minuts) per jugador:");
		scanf(" %[^\n]",tempsPartida);
		t=atoi(tempsPartida);
		if ((t>=1)&&(t<120))	{			//Minim d'un minut la partida
			i=1;
			*temps=t;					//Guardem el temps pel jugador						
		}else	{
			printf("\n\t\tEl temps ha de ser entre 1 minut i dues hores");
		}		
	}while (i==0);		
	
	sd=Conectar(direccio,port_server);		//Inicia la comunicaci� amb el servidor	
	if (sd>0)	{					//Establerta la comunicacio			
		strcpy(msg,"INIT:CP:");
		strcat(msg,nomP);
		strcat(msg,":");
		strcat(msg,passw);
		strcat(msg,":");
		strcat(msg,tempsPartida);
		strcat(msg,":");
		strcat(msg,fitxes);
		strcat(msg,":");		
		strcat(msg,login);			
		strcat(msg,":");
		strcat(msg,passwlogin);					
		
		Enviar_Missatge(sd,msg);		//Enviem l'acci� que volem realitzar amb els par�metres
		Rebre_Missatge(sd,aux);			//Rebem la resposta a la petici� demanada	
		Desconectar2(sd);					//Finalitzem la comunicacio		
		
		if (StartWith(aux,"OK:")==1)	{
			SubString(aux,3,aux);
			i=indexOf(aux,':');
			strcpy(portS,aux);
			TallaString(portS,i);
			*port=atoi(portS);			//el port per on jugar			
			jugar=1;
		}else	{
			SubString(aux,3,aux);
			printf("\nNo s'ha pogut crear la partida.%s",aux);
			jugar=0;			
		}	
		
	}else	{					//No pot connectar-se
		printf("\nNo pot establir-se la comunicacio amb els parametres establerts");
	}	
	
	return jugar;
}	


/** Permet unirse a una partida ja existent
*  Par�metre port_server: Indica el port de les peticions del servidor
*  Retorna port: Port al que ens conectarem per jugar. Quan ens el passen indica el port de peticions del servidor
*  Par�metre direccio: Direccio ip del servidor
*  Retorna temps: �s el temps que t� cada jugador
*  Retorna fitxes: Fitxes amb les que jugara
*  Retorna nom: El nom del jugador
*  Retorna jugar: 1 si jugarem en una partida, 0 en cas contrari
*/
int Unir_Partida(int port_server, int * port,char direccio[ 20 ],int * temps,char fitxes[ 10 ],char login[ MAX_BUFFER ]) 
{	
	int jugar=0;
	int i = 0,sd = 0;	
	char nomP[ 16 ] = {0};
	char passw[ 10 ] = {0};	
	char passwlogin[ 10 ] = {0};
	char aux[ MAX_BUFFER ] = {0};
	char portS[ 20 ] = {0};
	char msg[ MAX_BUFFER ] = {0};
				
	Obtenir_Dades(login,passwlogin,nomP,passw,fitxes);	//Obtenim les dades de la partida a la que ens volem unir
	
	sd = Conectar(direccio,port_server);
	if (sd>0)	{		//Establerta la comunicacio			
		strcpy(msg,"INIT:UP:");
		strcat(msg,nomP);
		strcat(msg,":");
		strcat(msg,passw);
		strcat(msg,":");
		strcat(msg,fitxes);
		strcat(msg,":");
		strcat(msg,login);
		strcat(msg,":");
		strcat(msg,passwlogin);		
						
		Enviar_Missatge(sd,msg);			//Enviem l'acci� que volem realitzar					

		Rebre_Missatge(sd,aux);			//Rebem la resposta a la petici� demanada	
		Desconectar2(sd);					//Finalitzem la comunicacio
		if (StartWith(aux,"OK:")==1)	{
			SubString(aux,3,aux);
			i=indexOf(aux,':');
			strcpy(portS,aux);
			TallaString(portS,i);
			*port=atoi(portS);			//el port per on jugar
			SubString(aux,i+1,aux);
			*temps=atoi(aux);			//obtenim el temps de la partida						
			*temps=(*temps)/60;
			jugar=1;
		}else	{
			SubString(aux,3,aux);
			printf("\nNo s'ha pogut unir a la partida.%s",aux);
			jugar=0;			
		}	
	}else	{
		printf("\nNo pot establir-se la comunicacio amb els parametres establerts");
	}
	return jugar;
}

/** Demana a l'usuari un conjunt de dades per poder establir la partida
* Retorna: Nom del jugador
* Retorna: Password del jugador
* Retorna: Nom de la partida
* Retorna: Password de la partida
* Retorna: Fitxes amb les que juga
*/
void Obtenir_Dades(char login[ 10 ],char passwlogin[ 10 ],char nomP[ 16 ],char passw[ 10 ],char fitxes[ 10 ])
{
	int valid = 0;	
	do	{
		printf("\nIntrodueix el teu nom d'usuari:");
		fflush(stdin);
		scanf(" %[^\n]",login);		
		if (strlen(login)<=1)	{
			printf("\n\t\tUn nom d'usuari ha de tenir entre 2-10 caracters");	
		}
	}while (strlen(login)<=1);
	
	do	{
		printf("\nIntrodueix el teu password:");
		fflush(stdin);
		scanf(" %[^\n]",passwlogin);		
		if (strlen(passwlogin)<=1)	{
			printf("\n\t\tEl password ha de tenir entre 2-10 caracters");	
		}
	}while (strlen(passwlogin)<=1);
		
	do	{
		printf("\nIntrodueix el nom de la partida:");
		fflush(NULL);
		scanf(" %[^\n]", nomP); 	//leer con espacios
		if (strlen(nomP)<=1)	{
			printf("\n\t\tEl nom de la partida ha de tenir entre 2-10 caracters");	
		}
	}while (strlen(nomP)<=1);		
		//El password pot ser buit
	printf("\nVols password per la partida, 1-Si, 2-No:");
	fflush(NULL);
	scanf("%s",passw);	
	if (strcmp(passw,"1")==0) {
		printf("\nIntrodueix el password de la partida:");
		fflush(NULL);
		scanf(" %[^\n]",passw);	
	}else {
		strcpy(passw,"buit");	
	}	
	valid = 0;
	do	{
		printf("\nAmb quines fitxes vols jugar?(Blanques o Negres):");
		fflush(NULL);
		scanf(" %[^\n]",fitxes);		
		if ((strcmp(fitxes,"Blanques")==0)||(strcmp(fitxes,"Negres")==0))	{
			valid=1;
		}
	}while (valid==0);		
}

/** Permet obtenir una llista de totes les partides que s'estan jugant en aquest moment
*  Par�metre port: Indica el port de peticions del servidor
*  Par�metre direccio: Direccio ip del servidor
*/
void Llistar_Partides(int port,char direccio[ 20 ]) 
{		
	int i = 0;
	char partides[ MAX_BUFFER ] = {0};	
	char msg[ MAX_BUFFER ] = {0};
	int sd = 0;

	sd=Conectar(direccio,port);
	if (sd>0)	{		//Establerta la comunicacio			
		strcpy(msg,"INIT:LP:");
		Enviar_Missatge(sd,msg);			//Enviem l'acci� que volem realitzar
		Rebre_Missatge(sd,partides);			//Rebem la resposta a la petici� demanada				
		if (strcmp(partides,"OK:INIT0")==0)	{
			printf("\nNo hi ha cap partida disponible en aquest moment");
			Desconectar2(sd);				//Finalitzem la comunicacio
		}else if (strcmp(partides,"OK:INIT")==0)	{			
			strcpy(msg,"NEXT");
			Enviar_Missatge(sd,msg);			//Anem demanant partides
			Rebre_Missatge(sd,partides);						
			printf("\nEl format de la partida:\n\t\t Nom Partida : Jugador Blanques : Jugador Negres : Temps Jugador");
			printf("\nPartides disponibles:");			
			while (strcmp(partides,"OK:END")!=0)	{														
				SubString(partides,3,partides);		
				i=indexOf(partides,'|');			//Obtenim els parametres	
				TallaString(partides,i);		//Guardem la informacio fins al port						
				printf("\n%s",partides);
				strcpy(msg,"NEXT");
				Enviar_Missatge(sd,msg);			//Anem demanant partides
				Rebre_Missatge(sd,partides);			//Obtinc el seguent					
			}								
			Desconectar2(sd);					//Finalitzem la comunicacio						
		}else {
			printf("\nError durant la comunicacio");
			Desconectar2(sd);
		}		
			
	}else	{
		printf("\nLa comunicacio no pot establir-se amb la IP i port indicats");
	}	
}



/* Implementa el protocol transferencia de informaci� durant la partida
* Par�metre sd: �s el file descriptor asociat al socket
* Par�metre nom: Indica el login del jugador
* Par�metre info: Es l'estructura del jugador
* Par�metre mode: 1:Huma, 0:Maquina
*/

void Inici_Partida(int sd,char nom[ MAX_BUFFER ],jugada info,int mode)
{
	int ok = 0;
	char buffer[ MAX_BUFFER ] = {0};
	Enviar_Missatge(sd,nom);				//Enviem el nom
	do	{
		printf("\nEsperant el torn\n");
		Rebre_Missatge(sd,buffer);		//Esperem torn i movimient anterior
		ok=Accio_Asociada(sd,buffer,&info,mode);
	}while	(ok!=-1);
	Destrueix_Jugada(&info);			//Destruim l'estructura de l'alumne
}

/*Realitza una accion segons el codi enviat pel servidor
* Par�metre sd: �s el file descriptor asociat al socket
* Par�metre msg_server: �s el missatge que el servidor ha enviat
* Par�metre info: �s l'estructura on l'alumne emmagatzema informaci� necess�ria per l'heur�stica
* Par�metre mode: 1:Huma, 0:Maquina
*/
int Accio_Asociada(int sd,char msg_server[ MAX_BUFFER ],jugada * info,int mode)
{
	int ok = 1;
	char msg_player[ MAX_BUFFER ] = {0};
	char aux[ MAX_BUFFER ] = {0};

	if (StartWith(msg_server,"Tablas")==1)	{			//La partida esta en empat
		ok=-1;
		SubString(msg_server,6,aux);
		printf("\nT'han empatat la partida: %s",aux);
	}
	if (strcmp(msg_server,"Lose")==0)		{		//Perdudo la partida
		ok=-1;
		printf("\nPartida perduda");
	}
	if (strcmp(msg_server,"Exit")==0)		{		//L�altre s�ha desconectat
		ok=-1;
		printf("\nPartida interrumpuda per l'altre player, has guanyat");
	}
	if (strcmp(msg_server,"Time")==0)		{		//L�altre s�ha desconectat
		ok=-1;
		printf("\nL'altre jugador ha consumit el seu temps, has guanyat");
	}	
	if (ok==1)	{						//Continuem amb la partida
		Decidir_Moviment_Mode(msg_server,msg_player,info,mode);	//Decidim l�acci� a realitzar
		ok=Executar_movimient(msg_player,msg_server,sd,info,mode);	//Env�em l�ordre
	}
	return ok;
}

/* Executa el moviment que l� usuari a escollit
* Par�metre msg_player: �s el moviment que enviar� l� usuari
* Par�metre msg_server: �s el missatge que ens ha enviat el servidor
* Par�metre sd: �s el file descriptor de la comunicaci�
* Par�metre info: �s l'estructura on l'alumne emmagatzema informaci� necess�ria per l'heur�stica
* Par�metre mode: 1:Huma, 0:Maquina
* Retorna -1 si la partida ha finalitzat
*/
int Executar_movimient(char msg_player[ MAX_BUFFER ],char msg_server[ MAX_BUFFER ],int sd,jugada * info,int mode)
{
	char replay[ MAX_BUFFER ] = {0};
	char fitxa[ MAX_BUFFER ] = {0};
	char aux[ MAX_BUFFER ] = {0};
	int ok = 1;
	if (msg_server[0] == '\0') {} //Avoid warn.
	do	{
		ok = 1;
		Enviar_Missatge(sd,msg_player);			//enviem l�acci� que s�ha decidit
		Rebre_Missatge(sd,replay);			//rebem la resposta per part del servidor
		do	{
			if (strcmp(replay,"Coronacion")==0)	{		//Decidim la fitxa de la coronaci�
				Decidir_Fitxa_Mode(fitxa,info,mode); 			//Escollim D(dama),C(cavall),A(alfil),T(torre)
				Enviar_Missatge(sd,fitxa);			//Env�em la nova fitxa
				Rebre_Missatge(sd,replay);			//Rebem la resposta per part del servidor
				ok = 1;
				if (strcmp(replay,"Coronacion")==0)	{	//Ficha invalida, hem d'escollir altre cop
					Decidir_Fitxa_Mode(fitxa,info,mode);
					ok = 0;
				}
			}
		}while (ok != 1);
		if (StartWith(replay,"ko")==1)	{			//Si el servidor envia un missatge que comen�a per ko, es que el movimient era incorrecte
			Decidir_Moviment_Mode(replay,msg_player,info,mode);	//Decidim novament l� acci�
			ok = 0;
		}
	}while (ok != 1);						//al sortir, en replay tinc les instruccions del server

	if (strcmp(replay,"Win")==0)	{			//Hem ganado la partida
		ok = -1;
		printf("\nPartida guanyada");
	}
	if (StartWith(replay,"Tablas")==1)	{		//La partida esta empatada
		ok = -1;
		SubString(replay,7,aux);
		printf("\nHas empatat la partida: %s",aux);
	}
	if (strcmp(replay,"Exit")==0)	{			//Es desconecta el player
		ok = -1;
		printf("\nDesconnexio de la partida realitzada, has perdut");
	}
	return ok;
}


/* Busca el moviment que ha de realitzar
* Par�metre msg_server: �s el missatge que el servidor ens ha enviat.
* Retorna msg_player: �s el movimient a enviar
* Par�metre info: �s l'estructura on l'alumne emmagatzema la informaci� necess�ria per la cerca
* Par�metre mode: 1:Huma, 0:Maquina
*/
void Decidir_Moviment_Mode(char msg_server[ MAX_BUFFER ],char msg_player[ MAX_BUFFER ],jugada * info,int mode)
{	
	if (mode == 1)	{
		char display[ MAX_BUFFER ] = {0};	
		if (StartWith(msg_server,"ko")==1)	{
			/* Els codi de error son:
				0:"  Sintaxi del moviment incorrecta."
				-2:"La fitxa no pot realitzar aquest moviment."
				-3:"Impossible moure la fitxa per estar en jake."
				-4:"A la casella origen no hi ha cap fitxa teva."
			*/
			SubString(msg_server,4,display);
		}else	{
			strcpy(display,msg_server);	
		}
		memset(msg_player, '\0', sizeof(msg_player));
		printf("\nMissatge del servidor:%s",display);		
		printf("\nIntrodueix l'accio a realitzar. Exit(sortir) o <Moviment>(moure fitxa)");
		printf("\nAccio:");
		fflush(stdin);
		scanf("%s",msg_player);
	}else	{
		Decidir_Moviment(msg_server,msg_player,info);
	}	
}

/* Busca quina fitxa hem de canviar pel pe� al coronar 
* Retorna fitxa: �s el misstage que enviarem al servidor indicant la nova fitxa
* Par�metre info: �s l'estructura on l'alumne emmagatzema informaci� necess�ria per l'heur�stica
* Par�metre mode: 1:Huma, 0:Maquina
*/
void Decidir_Fitxa_Mode(char fitxa[ MAX_BUFFER ],jugada * j,int mode)
{
	int i = 0;
	if (mode==1)	{
		printf("\nIntrodueix la inicial de la fitxa: Dama,Cavall,Torre o Alfil \nFitxa: ");
		fflush(NULL);
		scanf("%s",fitxa);
		fitxa[1]='\0';	
		i=2;
		
	}else	{
		Decidir_Fitxa(fitxa,j);	
	}	
}


/* T� la funci� de verificar si una cadena de car�cters comen�a per un text concret
* Par�metre text: �s on busquem el fragment
* Par�metre start: �s el fragment a buscar
* Retorna 1 si comen�a, 0 en cas contrari
*/
int StartWith(char text[ MAX_BUFFER ],char start[ MAX_BUFFER ])
{
	int ok = 1,i = 0;
	while ((ok==1)&&(i<MAX_BUFFER)&&(start[i]!='\0'))	{		
		if (text[i]!=start[i])	{
			ok=0;	
		}		
		i++;
	}	
	return ok;
}

/* Retornar una cadena de car�cters que sigui un substring de la original a partir d� un offset
* Par�metre origen: �s on busquem el fragment
* Par�metre offset: �s a partir d� on comen�em a copiar
* Retorna desti: �s la cadena dest� on copiem
*/
void SubString(char origen[ MAX_BUFFER ],int offset,char desti[ MAX_BUFFER ])
{
	int i = 0, j = offset, ok = 0;
	while ((origen[j]!='\0')&&(j<MAX_BUFFER))	{
		desti[i]=origen[j];			
		i++;
		j++;
	}
	
	if (i!=MAX_BUFFER)	{
		desti[i]=origen[j];
		ok=1;
	}	
	if (ok==0)	{
		memset(desti, '\0', sizeof(desti));
	}	
}

/** Retorna la casella de la primera ocurr�ncia del car�cter
* Par�metre paraula: �s on busquem
* Par�metre caracter: El car�cter que cal trobar
* Retorna posicio: Val la casella de la primera ocurr�ncia, -1 si no existeix
*/
int indexOf(char paraula[ MAX_BUFFER ],char caracter)
{
	int posicio = -1, i = 0;	
	while ((paraula[i]!='\0')&&(i<MAX_BUFFER)&&(posicio==-1))	{		
		if (paraula[i]==caracter)	{
			posicio=i;	
		}
		i++;
	}
	return posicio;
}

/**Guardem la informacio fins al port						
* Par�metre origen: �s on busquem el fragment
* Par�metre i: �s fins on copiem
* Retorna desti: �s la cadena dest� on copiem
*/
void TallaString(char origen[20],int i)
{
	origen[i] = '\0';
}
