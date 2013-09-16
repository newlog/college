#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <signal.h>
#include "Mensajes.h"
#include "Salida.h"
/**************************************************
* Este codigo interpretara la salida del comando: *
*            ndisasm -b32 ejecutable              *
***************************************************/
void Errors (int numError);
void Mensaje();
int main(int argc, char **argv) {
Mensaje();
signal(SIGCHLD, SIG_IGN);					//Els fills matats no quedaran zombis
if ( argc < 2 || argc > 2 ) Errors(1);
if ( strcmp(argv[1],"--help") == 0 || strcmp(argv[1],"-h") == 0 ) Errors(7);
int fd[2], pid;
char salida[10000], argumentos[150];

bzero(argumentos, sizeof(argumentos));
if ( strlen(argv[1]) > 144 ) Errors(6);

strncat(argumentos, argv[1], 144);

if ( pipe(fd) == -1 ) Errors(2);
pid = fork();
switch (pid) {
	case -1: Errors(3); break;				//No s'ha creat el nou proces
	case 0:
		close(fd[0]);
		if ( dup2(fd[1],1) == -1 ) Errors(4);		//Tot el que surti per pantalla ho enviem a fd[1]
		//if ( execlp("ndisasm", "-b32" , argumentos, NULL) == -1 ) Errors(5);		//Executem ndisasm -b32 <programa>
		if ( execlp("ndisasm", "ndisasm", "-u", argumentos, NULL) == -1 ) Errors(5);		//Executem ndisasm -b32 <programa>
		//if ( execlp("ndisasm", argumentos, NULL) == -1 ) Errors(5);		//Executem ndisasm -b32 <programa>
		close(fd[1]);
	break;
	default:
		close(fd[1]);
		bzero(salida,sizeof(salida));
		read (fd[0], salida, 10000);
		Proceso (salida);
		close(fd[0]);
		kill(pid, SIGKILL);
	break;
}

return 0;
}

