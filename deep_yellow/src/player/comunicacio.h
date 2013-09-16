#ifndef _COMUNICACIO_H_
#define _COMUNICACIO_H_

#define MAX_BUFFER 80

#include <sys/stat.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <sys/socket.h>
#include <sys/wait.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <unistd.h>
#include <errno.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>


int Conectar(char ip_server[20],int port_server);
void Desconectar(int sd);
void Desconectar2(int sd);
void Enviar_Missatge(int sd,char buffer[MAX_BUFFER]);
void Rebre_Missatge(int sd,char buffer[MAX_BUFFER]);


#endif
