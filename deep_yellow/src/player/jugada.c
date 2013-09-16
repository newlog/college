#include "jugada.h"

#include "../tests/search_tests.h"

/*Permet inicialitzar l'estructura jugada
* Par�metre fitxes: Indica el color de les fitxes
* Par�metre temps: Es el temps per jugador. El temps esta en minuts
* Retorna info: �s l'estructura on l'alumne emmagatzema la informaci� necess�ria per la cerca
*/
void Inicialitza_Jugada(jugada * info,char fitxes[ 10 ],int temps)
{
	//Create the new Board
	info->boardJugada=initBoard();
	info->previousBoard=initBoard();

	info->Type_Piece_Corona = Queens;


	if((fitxes[0]=='B') || (fitxes[0]=='b'))
	{
		info->boardJugada.myColor=WHITE;
		info->boardJugada.actualColor=WHITE;

		info->previousBoard.myColor=WHITE;
		info->previousBoard.actualColor=WHITE;

	}
	else{
		info->boardJugada.myColor=BLACK;
		info->boardJugada.actualColor=BLACK;

		info->previousBoard.myColor=BLACK;
		info->previousBoard.actualColor=BLACK;
	}

	info->first_play=FIRTS_PLAY;

	if(temps < 40){
		printf("MAX_DEPTH = 4\n");
		MAX_DEPTH = 4;
	}else{
		printf("MAX_DEPTH = 5\n");
		MAX_DEPTH = 5;
	}





	/*printf("HOLA\n");
	Board boardTest;
	create_complex_board_test(& boardTest);

	jake_mate_heuristic(& boardTest, 4);

	exit(0);*/

}

/*Permet alliberar i/o finaltizar l'estructura jugada
* Par�metre info: �s l'estructura on l'alumne emmagatzema la informaci� necess�ria per la cerca
*/
void Destrueix_Jugada(jugada * info)
{
	
}


/* Busca el moviment que ha de realitzar
* Par�metre msg_server: �s el missatge que el servidor ens ha enviat.
* Retorna msg_player: �s el movimient a enviar
* Par�metre info: �s l'estructura on l'alumne emmagatzema la informaci� necess�ria per la cerca
*/
void Decidir_Moviment(char msg_server[ MAX_BUFFER ],char msg_player[ MAX_BUFFER ],jugada * info)
{

	int i=0;
	for(i=0;i<MAX_BUFFER;i++){
		msg_player[i]='\0';
	}

	/*
	char display[ MAX_BUFFER ] = {0};	
	if (StartWithJ(msg_server,"ko")==1)	{
		// Els codi de error son:
			//0:"  Sintaxi del moviment incorrecta."
		//	-2:"La fitxa no pot realitzar aquest moviment."
			//-3:"Impossible moure la fitxa per estar en jake."
			//-4:"A la casella origen no hi ha cap fitxa teva."

		SubStringJ(msg_server,4,display);
	}else	{
		strcpy(display,msg_server);	
	}
	memset(msg_player, '\0', sizeof(msg_player));
	printf("\nMissatge del servidor:%s",display);		
	printf("\nIntrodueix l'accio a realitzar. Exit(sortir) o <Moviment>(moure fitxa)");
	printf("\nAccio:");
	fflush(NULL);
	scanf("%s",msg_player);

	*/


	if (StartWithJ(msg_server,"ko") == 1){
		// Els codi de error son:
		//	0:"  Sintaxi del moviment incorrecta."
		//	-2:"La fitxa no pot realitzar aquest moviment."
		//	-3:"Impossible moure la fitxa per estar en jake."
		//	-4:"A la casella origen no hi ha cap fitxa teva."
		//printf("Trama KO\n");

		Make_movement_ko(msg_server, msg_player, &info->boardJugada , &info->previousBoard );



	}else{

		//miramos si somos blancas y es el primer movimento
		if(info->first_play == FIRTS_PLAY ){

			if(info->boardJugada.myColor == WHITE){
				make_firts_play(msg_player, &info->boardJugada);

			}else{

				//Save board whit movement server
				memcpy(&info->previousBoard,&info->boardJugada,sizeof(Board));
				update_board(msg_server[6],msg_server[8],msg_server[10],msg_server[12],&info->previousBoard);

				//Generate the movement
				process_trama(msg_server,msg_player, &info->boardJugada,&info->previousBoard);
			}

			info->first_play = NORMAL_PLAY;

		}else{

			//Save board whit movement server
			memcpy(&info->previousBoard,&info->boardJugada,sizeof(Board));
			update_board(msg_server[6],msg_server[8],msg_server[10],msg_server[12],&info->previousBoard);

			//Generate the movement
			process_trama(msg_server,msg_player,&info->boardJugada,&info->previousBoard);
		}


		//RANDOM PLAY!!!!!!!
		/*memset(msg_player, '\0', sizeof(msg_player));
		strcpy(msg_player,"xxxxxxxxxxxxxxxxxxxx");
		printf("msg_player AUX:%s\n",msg_player);
		*/

	}


	/*
	//miramos si somos blancas y es el primer movimento
	if(info->first_play == FIRTS_PLAY ){

		if(info->boardJugada.myColor == WHITE){
			printf("\tI WHIRE FIRST MOVE!!!\n");
			make_firts_play(msg_player, &info->boardJugada);

		}else{

			//Save board whit movement server
			update_board(msg_server[6],msg_server[8],msg_server[10],msg_server[12],&info->previousBoard);

			//Generate the movement
			process_trama(msg_server,msg_player, &info->boardJugada,&info->previousBoard);
		}

		info->first_play = NORMAL_PLAY;

	}else{

		//Save board whit movement server
		update_board(msg_server[6],msg_server[8],msg_server[10],msg_server[12],&info->previousBoard);

		//Generate the movement
		process_trama(msg_server,msg_player,&info->boardJugada,&info->previousBoard);
	}
	*/




}

/* Busca quina fitxa hem de canviar pel pe� al coronar 
* Retorna fitxa: �s el misstage que enviarem al servidor indicant la nova fitxa
* Par�metre info: �s l'estructura on l'alumne emmagatzema informaci� necess�ria per l'heur�stica
*/
void Decidir_Fitxa(char fitxa[ MAX_BUFFER ],jugada * j)
{
	/*printf("\nIntrodueix la inicial de la fitxa: Dama,Cavall,Torre o Alfil \nFitxa: ");
	fflush(NULL);
	scanf("%s",fitxa);
	fitxa[1]='\0';*/

	//Escollim D(dama),C(cavall),A(alfil),T(torre)
	//[0, 7, -9, -9, -9, -9, -9, -9]

	int position[8]={0},i=0, position_pawn=-1;
	if(j->boardJugada.myColor == WHITE){

		getPiecePosition(&j->boardJugada.whitePieces[Pawns], Pawns, position);

		for(i=0; i<8; i++){
			if((position[i] == 56) || (position[i] == 57 ) || (position[i] == 58 ) ||(position[i] == 59 ) ||
					(position[i] == 60 ) ||(position[i] == 61 ) ||(position[i] == 62 ) ||(position[i] == 63 ) ){

				BitMap aux_board = 0x8000000000000000;
				aux_board >>= position[i];

				position_pawn = position[i];

				//Delete Pawn corona
				j->boardJugada.whitePieces[Pawns] ^= aux_board;

				//Insert new Piece
				if(j->Type_Piece_Corona == Queens){
					j->boardJugada.whitePieces[Queens] |= aux_board;
				}else{
					j->boardJugada.whitePieces[Rooks] |= aux_board;
				}

				actualizeTotalBoard(&j->boardJugada);

				break;
			}
		}

	}else{
		if(j->boardJugada.myColor == BLACK){

			getPiecePosition(&j->boardJugada.blackPieces[Pawns], Pawns, position);

			for(i=0; i<8; i++){
				if((position[i] == 0) || (position[i] == 1 ) || (position[i] == 2 ) ||(position[i] == 3 ) ||
						(position[i] == 4 ) ||(position[i] == 5 ) ||(position[i] == 6 ) ||(position[i] == 7 ) ){

					BitMap aux_board = 0x8000000000000000;
					aux_board >>= position[i];

					position_pawn = position[i];

					//Delete Pawn corona
					j->boardJugada.blackPieces[Pawns] ^= aux_board;

					//Insert new Piece
					if(j->Type_Piece_Corona == Queens){
						j->boardJugada.blackPieces[Queens] |= aux_board;
					}else{
						j->boardJugada.blackPieces[Rooks] |= aux_board;
					}

					actualizeTotalBoard(&j->boardJugada);

					break;
				}
			}
		}

	}

	if(position_pawn == -1){
		printf("\n \t -->Decidir_Fitxa, jugada.c:Error Corona select position pawn<---\n");

	}

	if(j->Type_Piece_Corona == Queens){

		fitxa[0]='D';
		fitxa[1]='\0';

		j->Type_Piece_Corona = Rooks;

	}else{
		fitxa[0]='T';
		fitxa[1]='\0';
	}

}

/* T� la funci� de verificar si una cadena de car�cters comen�a per un text concret
* Par�metre text: �s on busquem el fragment
* Par�metre start: �s el fragment a buscar
* Retorna 1 si comen�a, 0 en cas contrari
*/
int StartWithJ(char text[ MAX_BUFFER ],char start[ MAX_BUFFER ])
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
void SubStringJ(char origen[ MAX_BUFFER ],int offset,char desti[ MAX_BUFFER ])
{
	int i = 0,j = offset, ok = 0;
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
