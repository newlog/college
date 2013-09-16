#ifndef _JUGADA_H_
#define _JUGADA_H_

#define MAX_BUFFER 80

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "../structs/board.h"
#include "../network/net_trama.h"

#define FIRTS_PLAY	1
#define NORMAL_PLAY	0

int MAX_DEPTH;

typedef struct { //TODO
	Board boardJugada;
	int first_play;
	Board previousBoard;
	int Type_Piece_Corona;



}jugada;

void Inicialitza_Jugada(jugada * info,char fitxes[10],int temps);
void Destrueix_Jugada(jugada * info);
void Decidir_Moviment(char msg_server[MAX_BUFFER],char msg_player[MAX_BUFFER],jugada * info);
void Decidir_Fitxa(char fitxa[MAX_BUFFER],jugada * info);

//funcions auxiliars per la implementaci� de demanar moviment per teclat
void SubStringJ(char origen[MAX_BUFFER],int offset,char desti[MAX_BUFFER]);
int StartWithJ(char mensaje[MAX_BUFFER],char start[MAX_BUFFER]);


#endif
