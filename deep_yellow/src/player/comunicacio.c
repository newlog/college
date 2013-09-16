#include "comunicacio.h"


/* Permet establir la comunicació amb el servidor, retonar el file descriptor pel que es comunicará
*/
int Conectar(char ip_server[ 20 ],int port_server)
{
	int sd = -1;						//Contindra el file descriptor asociat
	struct sockaddr_in direccion;	
	bzero((char*)&direccion,sizeof(direccion));
	if ((sd = socket(AF_INET,SOCK_STREAM,0)) != -1) {
		direccion.sin_family=AF_INET;
		direccion.sin_addr.s_addr=inet_addr(ip_server);		                            
		direccion.sin_port=htons(port_server);
		if(connect(sd,(struct sockaddr*)&direccion,sizeof(direccion))<0)  {  //Intentem conectar amb el servidor
			sd = -1;
		}					
	}	
	return sd;
}

/* Permet envia una cadena de caracters
*/
void Enviar_Missatge(int sd,char buffer[MAX_BUFFER])	{	
	int i=0;							//Netegem el que llegim per teclat
	int nbytes2=0;
	while(buffer[i]!='\0')	{
		i++;
	}	
	i++;
	while (i<MAX_BUFFER)	{
		buffer[i]=' ';
		i++;
	}
	
	nbytes2=write(sd,buffer,MAX_BUFFER);
	if (nbytes2==0)	{
		printf("\nHas estat expulsat del servidor, has perdut\n");	
	}		
}

/* Permet rebrer una cadena de caracters
*/
void Rebre_Missatge(int sd,char buffer[MAX_BUFFER])	{	
	int nbytes;		
	strcpy(buffer,"");	
	nbytes=read(sd,buffer,MAX_BUFFER);
	if (nbytes==0)	{
		printf("\nHas estat expulsat del servidor, has perdut\n");	
		exit(-1);
	}		
}

/* Permet desconectar la comunicació amb el servidor
* Paràmetre sd: És el file descriptor de la comunicació
*/
void Desconectar(int sd)	{		
	int ok=close(sd);					//Alliberem el socket
	if (ok==0)	{
		printf("\nDesconnexio realitzada correctament\n");			
	}else	{
		printf("\nError durant el procés de desconnexio\n");
	}
}

/* Permet desconectar la comunicació amb el servidor, no treu cap missatge
* Paràmetre sd: És el file descriptor de la comunicació
*/
void Desconectar2(int sd)	{
	close(sd);					//Alliberem el socket
}
