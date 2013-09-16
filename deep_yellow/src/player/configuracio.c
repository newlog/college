#include "configuracio.h"

/* Permet inicialitzar els paràmetres de conexió, considera que hi ha 
paràmetres per defecte, es a dir, que si no els posa l´ usuari els suposa
*  Retorna puerto: Port al que ens conectarem
*  Retorna direccio: Direccio ip del servidor
*/
void Inicialitza(int *port,char direccio[ 20 ])
{	
	int Port_Server=9001;
	FILE * fin = NULL;
    int i = 0;
    char aux[301] = {0};
    fin=fopen("config.txt","r");
    if (fin != NULL)  {                                       //El fitxer de configuració existia
                fgets(aux,300,fin);                             //Llegim la linea del titol
                fscanf(fin,"%s",aux);
                fscanf(fin,"%s",aux);
                fscanf(fin,"%s",aux);
                fscanf(fin,"%d",port);	              		//Tenim el port de connexio al servidor
                fscanf(fin,"%s",aux);
                fscanf(fin,"%s",aux);
                fscanf(fin,"%s",aux);                
                fscanf(fin,"%s",aux);                           //Obtenim l'adreça IP pero hem de treure les " "
                aux[0]=' ';
                aux[strlen(aux)-1]='\0';                        //Adreça IP correcte
                for (i=0;aux[i+1]!='\0';i++)      {             //Movem una casella endarrera
                        direccio[i]=aux[i+1];
                }
				direccio[i]='\0';
                fclose(fin);
                printf("\nConfiguracio carregada del fitxer correctament");
       
        } else {                                               //El fitxer de configuracio no existia, mirem arguments                
			*port = Port_Server;
        	strcpy(direccio,"127.0.0.1");	        
        }	
}

