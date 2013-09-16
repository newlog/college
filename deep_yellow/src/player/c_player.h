#ifndef _C_PLAYER_H_
#define _C_PLAYER_H_

#define MAX_BUFFER 80

#include "comunicacio.h"
#include "jugada.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int Inici_Entorn(int *port, char direccio[20],char nom[MAX_BUFFER],jugada * info,int * mode);
int Realitza_Operacio(int * port,char direccio[20],int * temps,char fitxes[10],char login[MAX_BUFFER]);
void Llistar_Partides(int port,char direccio[20]);
void Obtenir_Dades(char login[10],char passwlogin[10],char nomP[16],char passw[10],char fitxes[10]);
int Unir_Partida(int port_server, int * port,char direccio[20],int * temps,char fitxes[10],char login[MAX_BUFFER]);
int Crear_Partida(int port_server, int * port,char direccio[20],int * temps,char fitxes[10],char login[MAX_BUFFER]);

void Inici_Partida(int sd,char nom[MAX_BUFFER],jugada info,int mode);
int Accio_Asociada(int sd,char msg_server[MAX_BUFFER],jugada * info,int mode);
int Executar_movimient(char msg_player[MAX_BUFFER],char msg_server[MAX_BUFFER],int sd,jugada * info,int mode);
void Decidir_Moviment_Mode(char msg_server[MAX_BUFFER],char msg_player[MAX_BUFFER],jugada * info,int mode);
void Decidir_Fitxa_Mode(char fitxa[MAX_BUFFER],jugada * j,int mode);

void SubString(char origen[MAX_BUFFER],int offset,char desti[MAX_BUFFER]);
int StartWith(char mensaje[MAX_BUFFER],char start[MAX_BUFFER]);
int indexOf(char paraula[MAX_BUFFER],char caracter);
void TallaString(char origen[20],int i);

#endif

