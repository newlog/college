/*
 * net_trama.c
 *
 *  Created on: 10/12/2011
 *      Author: gabi
 */

#include "net_trama.h"


char * index_messagge [64] = {
		"1-a","1-b","1-c","1-d","1-e","1-f","1-g","1-h",
		"2-a","2-b","2-c","2-d","2-e","2-f","2-g","2-h",
		"3-a","3-b","3-c","3-d","3-e","3-f","3-g","3-h",
		"4-a","4-b","4-c","4-d","4-e","4-f","4-g","4-h",
		"5-a","5-b","5-c","5-d","5-e","5-f","5-g","5-h",
		"6-a","6-b","6-c","6-d","6-e","6-f","6-g","6-h",
		"7-a","7-b","7-c","7-d","7-e","7-f","7-g","7-h",
		"8-a","8-b","8-c","8-d","8-e","8-f","8-g","8-h",
};

//msg_server, msg_player--> 80bytes
int process_trama(char* msg_server,char* msg_player,Board * boardActual,Board * previousBoard){

	printf("Message Server: %s\n",msg_server);
	if ( !msg_server ) {
		printf("Error in Message Server\n");
		return ERROR_TRAMA_SERVER;
	}
	if ( !msg_player ) {
			printf("Error in Message Player\n");
			return ERROR_TRAMA_SERVER;
	}
	if ( boardActual== NULL ) {
				printf("Error in boardActual\n");
				return ERROR_TRAMA_SERVER;
	}


	if(msg_server[0]=='R'||msg_server[0]=='r'){//Moviment Ready

		if(msg_server[14]=='P'||msg_server[14]=='p'){//Peo al pas
			Make_peo_al_pas_movement(msg_server,msg_player,boardActual);

		}else{
			if(msg_server[14]=='C'||msg_server[14]=='c'){//Peo Corona
				Make_corona_movement(msg_server,msg_player,boardActual);

			}else{
				Make_simple_movement(msg_server,msg_player,boardActual);
			}
		}


	}else{//Trama KO
		//printf("Trama KO\n");


		//Make_movement_ko(msg_server, msg_player, boardActual, previousBoard);

	}



	return SUCCESS;
}

int Make_simple_movement(char* msg_server,char* msg_player,Board * boardActual){



	char initial_row=' ', initial_column=' ', final_row=' ', final_column=' ';
	int index=0;
	printf("--- WE READ ==> READY:");

	//Number of row, initial movement. If valor == 0 are Enroc
	index=index+6;
	printf("%c",msg_server[index]);
	initial_row=msg_server[index];

	if(initial_row == '0'){//Enroc trama == 0-0-0 o 0-0

		//Letter of column, initial movement
		index=index+3;
		printf("\t SI es '-' Enroc larg, sino curt:%c\n",msg_server[index]);
		if(msg_server[index] == '-'){

			Make_Enroc_Llarg(msg_player,boardActual);

		}else{
			Make_Enroc_curt(msg_player,boardActual);
		}


	}else{//SIMPLE MOVEMENT

		//Letter of column, initial movement
		index=index+2;
		initial_column=msg_server[index];

		//Number of final_row
		index=index+2;
		final_row=msg_server[index];

		//Letter of column, final movement
		index=index+2;
		final_column=msg_server[index];

		printf("-%c:%c-%c\n",initial_column,final_row,final_column);

		//Update board

		/*logChess(INFO,"______Trama Server BOARD NO actualitzat negra___\n");
		logBoard(INFO, boardActual->totalBlackPieces);
		logChess(INFO,"______Trama Server  BOARD NO actualitzat blanc___\n");
		logBoard(INFO, boardActual->totalWhitePieces);
		logChess(INFO,"_________\n");*/

		update_board(initial_row, initial_column, final_row, final_column,boardActual);


		logChess(INFO,"______Trama Server BOARD actualitzat negra___\n");
		logBoard(INFO, boardActual->totalBlackPieces);
		logChess(INFO,"______Trama Server BOARD actualitzat blanc___\n");
		logBoard(INFO, boardActual->totalWhitePieces);
		logChess(INFO,"_________\n");

		//make the Negascout and put next movement in Board nextMove
		Board nextMove;
		search_next_movement(boardActual, &nextMove);

		//Generating a new board with the position actual and the next move
		BitMap start=0;
		BitMap end=0;

		update_board_after_search( boardActual, &nextMove, &start, &end );

		logChess(INFO,"______NEGASCOUT BOARD actualitzat negra___\n");
		logBoard(INFO, boardActual->totalBlackPieces);
		logChess(INFO,"______NEGASCOUT BOARD actualitzat blanc___\n");
		logBoard(INFO, boardActual->totalWhitePieces);
		logChess(INFO,"_________\n");

		/*
		 * Generating the message to send to the server
		 */
		 make_mesage_to_server(&start, &end,msg_player);



	}

	return SUCCESS;
}

int make_firts_play(char* msg_player,Board * boardActual){


	//make the Negascout and put next movement in Board nextMove
	Board nextMove;
	search_next_movement(boardActual, &nextMove);

	//Generating a new board with the position actual and the next move
	BitMap start=0;
	BitMap end=0;

	update_board_after_search( boardActual, &nextMove, &start, &end );

	logChess(INFO,"______NEGASCOUT BOARD actualitzat negra___\n");
	logBoard(INFO, boardActual->totalBlackPieces);
	logChess(INFO,"______NEGASCOUT BOARD actualitzat blanc___\n");
	logBoard(INFO, boardActual->totalWhitePieces);
	logChess(INFO,"_________\n");

	/*
	 * Generating the message to send to the server
	 */
	make_mesage_to_server(&start, &end,msg_player);

	return SUCCESS;
}

int Compare_position_color_pieces_board(Board* boardActual,Board* nextMove,int* killer){

	if(boardActual->myColor==BLACK){
		if((boardActual->totalWhitePieces&nextMove->totalWhitePieces)!=boardActual->totalWhitePieces){
			*killer=KILL_PIECE;
		}


	}else{
		if((boardActual->totalBlackPieces&nextMove->totalBlackPieces)!=boardActual->totalBlackPieces){
			*killer=KILL_PIECE;
		}

	}

	return SUCCESS;
}

int update_board_after_search(Board* boardActual, Board* nextMove, BitMap* start, BitMap* end ){

	BitMap nextPlusActual = 0, auxBitMap=0;
	int i=0;

	int killer=-1;
	Compare_position_color_pieces_board(boardActual,nextMove,&killer);

	if(killer == -1){

	//if((boardActual->totalWhitePieces&nextMove->totalWhitePieces)==boardActual->totalWhitePieces){

	//TODO:no killedTypePieces valor incorrecto S'utiiliza la funcion "Compare_position_color_pieces_board() para que funcione el codigo"
	//if ( nextMove->killedTypePiece == -1 ) {


		//logChess(INFO,",,,NO KILL PIECE!!!");
		//nextPlusActual have 2 position of the movement
		nextPlusActual = boardActual->occupiedSquares ^ nextMove->occupiedSquares;
	} else {
		logChess(INFO,"---------> WE KILL<-------------\n");

		//nextPlusActual Only have 1 first position of the movement
		nextPlusActual = boardActual->occupiedSquares ^ nextMove->occupiedSquares;

		//search the position of piece killed

		for(i = 0; i < N_TYPES; i++){

			if(boardActual->myColor == WHITE){
				if((boardActual->whitePieces[i] & nextPlusActual) != 0){

					auxBitMap = boardActual->whitePieces[i] ^ nextPlusActual;

					auxBitMap = auxBitMap ^ nextMove->whitePieces[i];

					nextPlusActual = auxBitMap| nextPlusActual;
					break;
				}


			}else{
				if((boardActual->blackPieces[i] & nextPlusActual) != 0){



					auxBitMap = boardActual->blackPieces[i] ^ nextPlusActual;

					//logChess(INFO,"______Som negres auxbitmap1___\n");
					//logBoard(INFO, auxBitMap);

					auxBitMap = auxBitMap ^ nextMove->blackPieces[i];

					//logChess(INFO,"______Som negres auxbitmap2___\n");
					//logBoard(INFO, auxBitMap);

					nextPlusActual = auxBitMap | nextPlusActual;

					break;
				}

			}
		}

	}


	/*logChess(INFO,"___NEGASCOUT BOARD NO actualitzat, Actual BOARD___\n");
	logBoard(INFO, boardActual->occupiedSquares);
	logChess(INFO,"___NEGASCOUT BOARD NO actualitzat, NEXT MOVE___\n");
	logBoard(INFO, nextMove->occupiedSquares);*/
	logChess(INFO,"___NEGASCOUT BOARD NO actualitzat, MOVEMENT Start/END___\n");
	logBoard(INFO, nextPlusActual);
	logChess(INFO,"____________________\n");

	//Extracting the actual position to search in the board what piece to replace
	extractActualNextBitmap(start, end, &nextPlusActual, &(nextMove->occupiedSquares));

	//Actualize boardActual whit 2 BitMap, start(Start position of the movement) and end (End position of movement)
	actualizeBoard(boardActual,end,start);

	return SUCCESS;
}

int make_mesage_to_server(BitMap* start, BitMap* end,char* msg_player){

	/*
	 * Generating the message to send to the server
	 */
	int positionStart[8];
	int positionEnd[8];


	int success1 = getPiecePosition(start,0,positionStart);
	int success2 = getPiecePosition(end,0,positionEnd);

	if((success1 || success2) != SUCCESS)
	{
		printf("ERROR make simple movement\n");
		return ERROR_FIND_POSITION;
	}

	if((positionStart[0] || positionEnd[0]) != -9)
	{
		char * from= index_messagge[positionStart[0]];
		char * doblePoint=":";
		char * to=index_messagge[positionEnd[0]];
		printf("From : %d, To: %d\n", positionStart[0],positionEnd[0]);
		strncat(msg_player,from,3);
		strncat(msg_player,doblePoint,1);
		strncat(msg_player,to,3);
		printf("SEND Message player: %s\n",msg_player);
	}
	else
	{
		strcpy(msg_player,"x-x:x-x");
		printf("ERROR make simple movement, SEND Message player: %s\n",msg_player);

		return ERROR_GENERATING_MESSAGE;
	}

	return SUCCESS;
}

int search_next_movement(Board* boardActual,Board* nextMove){

	//Negascout
	int retValue = -1;
	time_t timeStamp1 = {0};
	time_t timeStamp2 = {0};
	double seconds = 0;

	MOVEMENTS++;
	timeStamp1 = time(NULL);
	retValue = alpha_beta_negamax(boardActual, MIN_INT_VALUE, MAX_INT_VALUE, 0, nextMove);
	timeStamp2 = time(NULL);

	/*
	 * nextMove.occupiedSquares is a bitmap with all the pieces in the board with the move done
	 */
	logChess(INFO, "Next move: ");
	logBoard(INFO, nextMove->occupiedSquares);
	seconds = difftime(timeStamp2, timeStamp1);
	printf("Execution time: %.2f\n", seconds);

	return SUCCESS;
}


int Make_corona_movement(char* msg_server,char* msg_player,Board * boardActual){

	logChess(INFO,"\t!!!!!!!!!CORONA MOVEMENT!!!!!!!!!!\n");
	char initial_row=' ', initial_column=' ', final_row=' ', final_column=' ';
	int index=0;
	printf("--- WE READ CORONA ==> READY:");

	//Number of row, initial movement.
	index=index+6;
	printf("%c",msg_server[index]);
	initial_row=msg_server[index];
	//Letter of column, initial movement
	index=index+2;
	initial_column=msg_server[index];

	//Number of final_row
	index=index+2;
	final_row=msg_server[index];

	//Letter of column, final movement
	index=index+2;
	final_column=msg_server[index];

	printf("-%c:%c-%c\n",initial_column,final_row,final_column);

	//Update board

	update_board(initial_row, initial_column, final_row, final_column,boardActual);


	logChess(INFO,"______Trama Server BOARD actualitzat negra___\n");
	logBoard(INFO, boardActual->totalBlackPieces);
	logChess(INFO,"______Trama Server BOARD actualitzat blanc___\n");
	logBoard(INFO, boardActual->totalWhitePieces);
	logChess(INFO,"_________\n");



	/*logChess(INFO,"______Trama Server BOARD NO actualitzat negra___\n");
			logBoard(INFO, boardActual->totalBlackPieces);
			logChess(INFO,"______Trama Server  BOARD NO actualitzat blanc___\n");
			logBoard(INFO, boardActual->totalWhitePieces);
			logChess(INFO,"_________\n");*/


	/*
	 * Change pawn whit new piece in message server
	 * */
	int color_pawn=-1;

	if(boardActual->myColor == BLACK){
		color_pawn = WHITE;
	}else{
		color_pawn = BLACK;
	}

	switch(msg_server[21]){
	case 'D':
	case 'd':
		 change_pawn_corona(boardActual,final_row, final_column,1,color_pawn);
		break;
	case 'A':
	case 'a':
		change_pawn_corona(boardActual,final_row, final_column,3,color_pawn);
		break;
	case 'T':
	case 't':
		change_pawn_corona(boardActual,final_row, final_column,2,color_pawn);
		break;
	case 'C':
	case 'c':
		change_pawn_corona(boardActual,final_row, final_column,4,color_pawn);
		break;


	default:
		logChess(INFO,"-------INCORRECT CORONA TYPE PIECE-------");

		break;

	}


	/*LOG CORONA*/

	/*if(boardActual->myColor == BLACK){

	logChess(INFO,"______Trama Server CORONA PAWN___\n");
	logBoard(INFO, boardActual->whitePieces[5]);
	logChess(INFO,"______Trama Server NEW QUEEN___\n");
	logBoard(INFO, boardActual->whitePieces[1]);
	logChess(INFO,"_________\n");
		}else{
			logChess(INFO,"______Trama Server CORONA PAWN___\n");
				logBoard(INFO, boardActual->blackPieces[5]);
				logChess(INFO,"______Trama Server NEW QUEEN___\n");
				logBoard(INFO, boardActual->blackPieces[1]);
				logChess(INFO,"_________\n");
		}
	 */


	//------------


	//make the Negascout and put next movement in Board nextMove
	Board nextMove;
	search_next_movement(boardActual, &nextMove);

	//Generating a new board with the position actual and the next move
	BitMap start=0;
	BitMap end=0;

	update_board_after_search( boardActual, &nextMove, &start, &end );


	logChess(INFO,"______NEGASCOUT BOARD actualitzat negra___\n");
	logBoard(INFO, boardActual->totalBlackPieces);
	logChess(INFO,"______NEGASCOUT BOARD actualitzat blanc___\n");
	logBoard(INFO, boardActual->totalWhitePieces);
	logChess(INFO,"_________\n");

	/*
	 * Generating the message to send to the server
	 */
	make_mesage_to_server(&start, &end,msg_player);



	return SUCCESS;
}

int change_pawn_corona(Board* boardActual,char final_row,char final_column,int Type_Piece_change,int color_pawn){

	printf("Piece CORONA:%d\n",Type_Piece_change);

	BitMap finalBitMap = 0x8000000000000000;	//Position of movement the piece

	getBitMapPosition(final_row, final_column, &finalBitMap);

	if(color_pawn == BLACK ){

		//delete pawn
		boardActual->blackPieces[5] = boardActual->blackPieces[5] ^ finalBitMap;

		//put new piece
		boardActual->blackPieces[Type_Piece_change] = boardActual->blackPieces[Type_Piece_change] | finalBitMap;


	}else{
		//delete pawn
		boardActual->whitePieces[5] = boardActual->whitePieces[5] ^ finalBitMap;

		//put new piece
		boardActual->whitePieces[Type_Piece_change] = boardActual->whitePieces[Type_Piece_change] | finalBitMap;

	}




	return SUCCESS;
}


int Make_peo_al_pas_movement(char* msg_server,char* msg_player,Board * boardActual){
	//TODO:Make_peo_al_pas_movement
	logChess(INFO,"\t!!!!!!!!!PEO AL PAS MOVEMENT!!!!!!!!!!\n");

	//Update Board
	update_board(msg_server[6],msg_server[8],msg_server[10],msg_server[12], boardActual);

	//BitMap whit final position
	BitMap finalBitMap = 0x8000000000000000;
	getBitMapPosition(msg_server[10],msg_server[12], &finalBitMap);

	if ( boardActual->myColor == WHITE ) {

		finalBitMap >>= 8;
		boardActual->whitePieces[Pawns]^=finalBitMap;

	}else{

		finalBitMap <<= 8;
		boardActual->blackPieces[Pawns]^=finalBitMap;

	}

	actualizeTotalBoard(boardActual);


	//make the Negascout and put next movement in Board nextMove
	Board nextMove;
	search_next_movement(boardActual, &nextMove);

	//Generating a new board with the position actual and the next move
	BitMap start=0;
	BitMap end=0;

	update_board_after_search( boardActual, &nextMove, &start, &end );

	logChess(INFO,"______NEGASCOUT BOARD actualitzat negra___\n");
	logBoard(INFO, boardActual->totalBlackPieces);
	logChess(INFO,"______NEGASCOUT BOARD actualitzat blanc___\n");
	logBoard(INFO, boardActual->totalWhitePieces);
	logChess(INFO,"_________\n");

	/*
	 * Generating the message to send to the server
	 */
	make_mesage_to_server(&start, &end,msg_player);


	return SUCCESS;
}


int Make_Enroc_Llarg(char * msg_player, Board * boardActual){
	logChess(INFO,"\tENROC LLARG MOVEMENT!\n");

	if ( boardActual->myColor == WHITE ) {

		//King move
		boardActual->blackPieces[King] 	<<= 3;
		//Rook move
		BitMap leftRook = 0x0000000000000080;
		boardActual->blackPieces[Rooks] ^= leftRook;
		leftRook >>= 2;
		boardActual->blackPieces[Rooks] |= leftRook;



	} else if ( boardActual->myColor == BLACK ) {

		//King move
		boardActual->whitePieces[King] 	<<= 3;
		//Rook move
		BitMap leftRook = 0x8000000000000000;
		boardActual->whitePieces[Rooks] ^= leftRook;
		leftRook >>= 2;
		boardActual->whitePieces[Rooks] |= leftRook;

	}

	actualizeTotalBoard(boardActual);


	//make the Negascout and put next movement in Board nextMove
	Board nextMove;
	search_next_movement(boardActual, &nextMove);

	//Generating a new board with the position actual and the next move
	BitMap start=0;
	BitMap end=0;

	update_board_after_search( boardActual, &nextMove, &start, &end );

	logChess(INFO,"______NEGASCOUT BOARD actualitzat negra___\n");
	logBoard(INFO, boardActual->totalBlackPieces);
	logChess(INFO,"______NEGASCOUT BOARD actualitzat blanc___\n");
	logBoard(INFO, boardActual->totalWhitePieces);
	logChess(INFO,"_________\n");

	/*
	 * Generating the message to send to the server
	 */
	make_mesage_to_server(&start, &end,msg_player);


	return SUCCESS;
}

int Make_Enroc_curt(char* msg_player,Board * boardActual){

	logChess(INFO,"\t!!!!!!!!!ENROC CURT MOVEMENT!!!!!!!!!!\n");

	if ( boardActual->myColor == BLACK ) {

		//King move
		boardActual->whitePieces[King] 	>>= 2;
		//Rook move
		BitMap rightRook = 0x0100000000000000;
		boardActual->whitePieces[Rooks] ^= rightRook;
		rightRook <<= 2;
		boardActual->whitePieces[Rooks] |= rightRook;

	} else if ( boardActual->myColor == WHITE ) {

		//King move
		boardActual->blackPieces[King] 	>>= 2;
		//Rook move
		BitMap rightRook = 0x0000000000000001;
		boardActual->blackPieces[Rooks] ^= rightRook;
		rightRook <<= 2;
		boardActual->blackPieces[Rooks] |= rightRook;

	}

	actualizeTotalBoard(boardActual);

	/*logChess(INFO,"Fa Enrroc negresKING\n");
	logBoard(INFO, boardActual->blackPieces[King]);
	logChess(INFO,"Fa Enrroc negres,Rook\n");
	logBoard(INFO, boardActual->blackPieces[Rooks]);
	logChess(INFO,"Fa Enrroc TOTAL\n");
	logBoard(INFO, boardActual->occupiedSquares);*/

	//make the Negascout and put next movement in Board nextMove
	Board nextMove;
	search_next_movement(boardActual, &nextMove);

	//Generating a new board with the position actual and the next move
	BitMap start=0;
	BitMap end=0;

	update_board_after_search( boardActual, &nextMove, &start, &end );

	logChess(INFO,"______NEGASCOUT BOARD actualitzat negra___\n");
	logBoard(INFO, boardActual->totalBlackPieces);
	logChess(INFO,"______NEGASCOUT BOARD actualitzat blanc___\n");
	logBoard(INFO, boardActual->totalWhitePieces);
	logChess(INFO,"_________\n");

	/*
	 * Generating the message to send to the server
	 */
	make_mesage_to_server(&start, &end,msg_player);


	return SUCCESS;
}
int Make_movement_ko( char* msg_server, char* msg_player, Board * boardActual, Board * previousBoard ){
	//TODO:Make_movement_ko

	/* Els codi de error son:
					0:"  Sintaxi del moviment incorrecta."
					-2:"La fitxa no pot realitzar aquest moviment."
					-3:"Impossible moure la fitxa per estar en jake."
					-4:"A la casella origen no hi ha cap fitxa teva."
	 */
	logChess(INFO,"\t!!!!!!!!!ERROR MOVEMENT!!!!!!!!!!\n");

	/*if(msg_server[2] == '-'){//error -2,-3,-4
		printf("SERVER Message error=%c\n",msg_server[3]);



	}else{//error 0
		printf("SERVER Message error=%c\n",msg_server[2]);



	}*/

	//Return to previous move
	memcpy(boardActual,previousBoard,sizeof(Board));


	////Search a firs children!!!!

	//search_next_movement(boardActual, &nextMove);//REMPLACE WHIT FIRST CHILDREN
	Board nextMove;

	first_children(&nextMove,boardActual);

	logChess(INFO,"______ERROR KO!!!!!!!!! negra___\n");
	logBoard(INFO, nextMove.totalBlackPieces);
	logChess(INFO,"______ERROR KO!!!!!!!!! blanc___\n");
	logBoard(INFO, nextMove.totalWhitePieces);
	logChess(INFO,"_________\n");

	//Generating a new board with the position actual and the next move
	BitMap start=0;
	BitMap end=0;

	update_board_after_search(boardActual ,&nextMove, &start, &end );




	/*
	 * Generating the message to send to the server
	 */
	make_mesage_to_server(&start, &end,msg_player);




	//printf("Mesage Value:%s\n",msg_server);
	//exit (0);
	//TODO: Tractar Ko Trama;



	return SUCCESS;

}

int first_children(Board* nextMove,Board* boardActual){
	//nextMove Board whit move

	logChess(INFO, "---first_children---boardActual BLACK");
	logBoard(INFO,boardActual->totalBlackPieces);
	logChess(INFO, "---first_children---boardActual WHITE");
	logBoard(INFO, boardActual->totalWhitePieces);



	Board children[MAX_CHILDREN];		//Array of children
	int childrenNumber= -1;				//Number of generated children
	int returnValue = -1;

	// The children array is inited with the actual board value.
	// This is done this way to save us time and just change a few values from the father to the child.
	returnValue = copyBoardStruct(children, boardActual);
	if ( returnValue ) {
		logChess(WARN, "The structure was not copied correctly.");
		logChess(WARN, "Negascout will still run, cross your fingers.");
	}

	// The children boards are created.
	returnValue = createChildren(boardActual, children, &childrenNumber);
	if ( returnValue ) {
		logChess(WARN, "An error ocurred while creating the children.");
		logChess(WARN, "Negascout will still run, cross your fingers.");
	}

	//Generate Random Move and select children
		int numberRandom=rand()%childrenNumber;
		//printf("childrenNumber = %d, random = %d\n",childrenNumber,numberRandom);
		printf("childrenNumber = %d",numberRandom);
		if(numberRandom > childrenNumber || childrenNumber < 0){
			printf("ERROR_Make_movement= %d\n",numberRandom);
			return ERROR_Make_movement_ko;

		}

		memcpy(nextMove,&children[numberRandom],sizeof(Board));


		logChess(INFO, "---first_children---children[numberRandom] BLACK");
		logBoard(INFO,children[numberRandom].totalBlackPieces);
		logChess(INFO, "---first_children---children[numberRandom] WHITE");
		logBoard(INFO, children[numberRandom].totalWhitePieces);


	return SUCCESS;
}

int update_board(char initial_row,char initial_column, char final_row, char final_column,Board* boardActual){

	BitMap initBitMap = 0x8000000000000000;		//Position of the position actual piece
	BitMap finalBitMap = 0x8000000000000000;	//Position of movement the piece
	int i = 0;
	int black_white = -1;						//1->white,0->black
	int type_piece = -1;

	//Update Bitmap with position
	getBitMapPosition(initial_row, initial_column, &initBitMap);
	getBitMapPosition(final_row, final_column, &finalBitMap);

	/*logChess(INFO, "Trama Server Update board:INITBITMAP");
	logBoard(INFO, initBitMap);
	logChess(INFO, "Trama Server Update board:finalTBITMAP");
	logBoard(INFO, finalBitMap);*/

	//Search Type piece initial movement
	for(i = 0; i < N_TYPES; i++){
			if ( (boardActual->blackPieces[i] & initBitMap) != 0 )
			{
				black_white=BLACK;
				type_piece=i;
				break;
			}
			if((boardActual->whitePieces[i] & initBitMap)!=0){
				black_white=WHITE;
				type_piece=i;
				break;
			}

	}

	if ( ( black_white == -1 ) || ( type_piece == -1 ) ){
		return ERROR_UPDATE_BOARD;
	}

	//Delete and insert the position of piece in board
	if(black_white == BLACK){
		//Delete
		boardActual->blackPieces[type_piece] = boardActual->blackPieces[type_piece] ^ initBitMap;
		//Insert
		boardActual->blackPieces[type_piece] = boardActual->blackPieces[type_piece] | finalBitMap;

		//Delete piece, if the final movement kill a piece
		search_and_update_kill_piece(boardActual,black_white,finalBitMap);

	}else{
		//Delete
		boardActual->whitePieces[type_piece] = boardActual->whitePieces[type_piece] ^ initBitMap;
		//Insert
		boardActual->whitePieces[type_piece] = boardActual->whitePieces[type_piece] | finalBitMap;

		//Delete piece, if the final movement kill a piece
		search_and_update_kill_piece(boardActual,black_white,finalBitMap);
	}



	actualizeTotalBoard(boardActual);

	return SUCCESS;

}

int search_and_update_kill_piece(Board* boardActual,int black_white,BitMap finalBitMap){
	int i=0;

	if(black_white==WHITE){//search piece BLACK killed

		//Search Type kill piece
		for(i = 0; i < N_TYPES; i++){
			if ( (boardActual->blackPieces[i] & finalBitMap) != 0 )
			{
				logChess(INFO,"------------>>>>>>>Nos han matado una pieza nuestra, color BLACK!!!!!!!!!\n");

				boardActual->blackPieces[i] = boardActual->blackPieces[i] ^ finalBitMap;

				break;
			}

		}



	}else{
		if(black_white == BLACK){//search piece BLACK killed

			for(i = 0; i < N_TYPES; i++){
				if ( (boardActual->whitePieces[i] & finalBitMap) != 0 )
				{
					logChess(INFO,"------------>>>>>>>Nos han matado una pieza nuestra, color WHITE!!!!!!!!!\n");

					boardActual->whitePieces[i] = boardActual->whitePieces[i] ^ finalBitMap;

					break;
				}

			}


		}else{
			return ERROR_COLOR_KILL_PIECEs_NOT_FOUND;
		}

	}

	return SUCCESS;
}

int getBitMapPosition(char row, char column,BitMap* actualBitMap){

	*actualBitMap=0x8000000000000000;
	int shift=0;
	//Number of shift


	switch(row){
		case '1':
			break;
		case '2':
			shift=shift+8;
			break;
		case '3':
			shift=shift+16;
			break;
		case '4':
			shift=shift+24;
			break;
		case '5':
			shift=shift+32;
			break;
		case '6':
			shift=shift+40;
			break;
		case '7':
			shift=shift+48;
			break;
		case '8':
			shift=shift+56;
			break;
		default:
			printf("ERROR ROW VALOR\n");
			break;
	}

	switch(column){
			case 'a':
			case 'A':

				break;
			case 'b':
			case 'B':
				shift=shift+1;
				break;
			case 'c':
			case 'C':
				shift=shift+2;
				break;
			case 'd':
			case 'D':
				shift=shift+3;
				break;
			case 'e':
			case 'E':
				shift=shift+4;
				break;
			case 'f':
			case 'F':
				shift=shift+5;
				break;
			case 'g':
			case 'G':
				shift=shift+6;
				break;
			case 'h':
			case 'H':
				shift=shift+7;
				break;
			default:
				printf("ERROR COLUMN VALOR\n");
				break;
		}

	//Search position
		*actualBitMap >>= shift;



	return SUCCESS;
}

int extractActualNextBitmap(BitMap * actual, BitMap * next, BitMap * total, BitMap * nextOriginal){

	//TODO: verify the actual and next
	int i;
	BitMap aux=0x8000000000000000;

	//Bitmap used to store the find position, don't know if are actual or next
	BitMap find1=0;
	BitMap find2=0;

	for (i=0; i<64; i++)
	{
		if((aux&(*total)) != 0)
		{
			if(find1!=0)
				find2=aux;
			else
				find1=aux;
		}
		aux>>=1;
	}

	/*
	 * Now we search for wath is actual and what is next
	 */

	if((find1&(*nextOriginal)) != 0)
	{
		*next=find1;
		*actual=find2;
	}
	else if((find2&(*nextOriginal)) != 0)
	{
		*next=find2;
		*actual=find1;
	}
	else
	{
		printf("ERROR extractActualNextBitmap\n");
		return ERROR_FIND_ACTUAL_AND_NEXT;
	}

	return SUCCESS;
}

int actualizeBoard(Board * actualBoard, BitMap * next, BitMap * actual ){

	//TODO:control of the input
	//Search in the actual to find the type of pieces and to erase it
	int i,type=-1;

	/*
	 * finding the piece in the actual board  and deleting it
	 */
	for(i=0; i<N_TYPES; i++)
	{
		if(actualBoard->myColor==WHITE)
		{
			if(((actualBoard->whitePieces[i])&(*actual)) != 0)
			{
				actualBoard->whitePieces[i]^=(*actual);
				type=i;
			}
		}
		else
		{
			if(((actualBoard->blackPieces[i])&(*actual)) != 0)
			{
				actualBoard->blackPieces[i]^=(*actual);
				type=i;
			}
		}
	}

	if(type==-1)
	{

		printf("ERROR actualizeBoard");


		logChess(INFO, "----------ERROR actualizeBoard, type=-1--------------");
		logChess(INFO, "-------------Bitmap actual:");
		logBoard(INFO, *actual);
		logChess(INFO, "-------------Bitmap totalBlack:");
		logBoard(INFO, actualBoard->totalBlackPieces);
		logChess(INFO, "-------------Bitmap totalWhite:");
		logBoard(INFO,actualBoard->totalWhitePieces );
		logChess(INFO, "-----------------------------------------------------");



		return ERROR_DELETE_ACTUAL;
	}

	/*
	 * Insert the piece in the new position in the board representation
	 */

	if(actualBoard->myColor == WHITE)
		actualBoard->whitePieces[type]|=(*next);
	else
		actualBoard->blackPieces[type]|=(*next);

	/*
	 * Now we search in the other color to see if we kill some pieces
	 */
	for(i=0; i<N_TYPES; i++)
	{
		if(actualBoard->myColor == WHITE)
		{
			if((actualBoard->blackPieces[i]&(*next)) != 0)
			{
				actualBoard->blackPieces[i]^=(*next); //deteting a killed enemies pieces
			}
		}
		else
		{
			if((actualBoard->whitePieces[i]&(*next)) != 0)
			{
				actualBoard->whitePieces[i]^=(*next); //deteting a killed enemies pieces
			}
		}
	}

	actualizeTotalBoard(actualBoard);

	return SUCCESS;
}

void actualizeTotalBoard(Board *board)
{
	board->totalWhitePieces =	board->whitePieces[Pawns]	|
								board->whitePieces[Rooks]	|
								board->whitePieces[Bishops]	|
								board->whitePieces[Knights]	|
								board->whitePieces[Queens]	|
								board->whitePieces[King];
	board->totalBlackPieces =	board->blackPieces[Pawns]	|
								board->blackPieces[Rooks]	|
		                        board->blackPieces[Bishops]	|
		                        board->blackPieces[Knights]	|
		                        board->blackPieces[Queens]	|
		                        board->blackPieces[King];

	board->occupiedSquares= board->totalWhitePieces | board->totalBlackPieces;

	board->heuristic = MIN_INT_VALUE;
	board->killedTypePiece = -1;


	int i;
	for(i = 0; i < MAX_NUM_MOVES; i++){
		board->boardState[i].generatedMove = 0;
		board->boardState[i].killedTypePiece = -1;
	}
}


