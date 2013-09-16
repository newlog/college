/*
 * evaluation.c
 *
 *  Created on: 10/dic/2011
 *      Author: paolotaglinani
 */

#include "evaluation.h"
#include <stdio.h>
#include <string.h>
#include "../log/log.h"
#include "../moves/move_functions.h"
#include "../moves/piece_movements.h"
#include "../structs/board.h"

/*
 * ---------------------------------------------------------------------
 * | DEFINITION OF THE VALUE FOR THE EVALUATION FOR THE AMENACED PIECES	|
 * ---------------------------------------------------------------------
 */

int menacedHeuristicValue[]={
	   300,						//KING value
		45,						//QUEEN value
		25,						//ROOK value
		17,						//BISHOP value
		15,						//KNIGHT value
		5						//PAWN value

};
/*
 * ---------------------------------------------------------
 * | DEFINITION OF THE POSITION TABLE FOR EVERY PIECE TYPE	|
 * ---------------------------------------------------------
 */
//ALL THE TABLE ARE FROM WHITE VIEW


/*
 * PAWNS
 * Position of the pawns in the middle game: pawns are inolved to advance
 */
int pawnPositionTableMiddle []={

		0,   0,   0,   0,   0,   0,   0,   0,
		1,   2,   1,   1,   1,   1,   2,   1,
		1,   2,   1,   2,   2,   1,   2,   1,
		1,   2,   2,   8,   8,   2,   2,   1,
		1,   2,   5,  15,  15,   5,   2,   1,
		2,  10,   1,   8,   8,   1,  10,   2,
		-6,  -4,   1, -24, -24,   1,  -4,  -6,
		0,   0,   0,   0,   0,   0,   0,   0

};
/*
 *Position for the pawns in the opening game, pawns are involved to advance
 */
int pawnPositionTableOpening []={

		0,   0,   0,   0,   0,   0,   0,   0,
		1,   2,   1,   1,   1,   1,   2,   1,
		1,   2,   1,   2,   2,   1,   2,   1,
		1,   2,   2,   8,   8,   2,   2,   1,
		1,   2,  10,  15,  15,  10,   2,   1,
		2,  10,   8,   8,   8,   8,  10,   2,
		-5, -10, -10, -24, -24, -10, -10,  -5,
		0,   0,   0,   0,   0,   0,   0,   0

};

/*
 * Position for the pawn for the end game: pawn are more involved than before to advance
 */
int pawnPositionTableEnd []={

/*		0,   0,   0,   0,   0,   0,   0,   0,
		1,  25,  30,  30,  30,  30,  25,   1,
		5,  25,  30,  30,  30,  30,  25,   5,
		5,  15,  20,  20,  20,  20,  15,   5,
		5,  15,  10,  15,  15,  10,  15,   5,
		10,  10,   8,   8,   8,   8,  10,  10,
		-5, -10, -10, -24, -24, -10, -10,  -5,
		0,   0,   0,   0,   0,   0,   0,   0
*/
		90,   90,   90,   90,   90,  90,   90,  90,
		30,  40,  50,  50,  50,  50,  40,   30,
		20,  25,  30,  30,  30,  30,  25,   20,
		18,  18,  20,  20,  20,  20,  18,   18,
		15,  15,  10,  15,  15,  10,  15,   15,
		10,  10,   8,   8,   8,   8,  10,  10,
		-5, -10, -10, -24, -24, -10, -10,  -5,
		0,   0,   0,   0,   0,   0,   0,   0


};
/*
  BISHOPS
    - centralization bonus, smaller than for knight
    - good squares on the own half of the board
    - one board for all the game phase
 */

int bishopPositionTableAll []={

		-4,  -4,  -4,  -4,  -4,  -4,  -4,  -4,
		-4,   0,   0,   0,   0,   0,   0,  -4,
		-4,   7,  13,  16,  16,  13,   7,  -4,
		-4,  15,  16,  20,  20,  16,  15,  -4,
		-4,  14,  16,  20,  20,  16,  14,  -4,
		-4,  10,  13,  16,  16,  13,  10,  -4,
		-4,  13,  10,  10,  10,  10,  13,  -4,
		-4,  -4, -12,  -4,  -4, -12,  -4,  -4
};


/*
 * QUEEN
 */
/*
 * There's no best position for the queen in the opening and middle phase. The queen has an high value
 * of "menace" in this case because has more movement
 */
int queenPositionTableOpeningMiddle []={

		0,  0,  0,  0,  0,  0,  0,  0,
		0,  0,  0,  0,  0,  0,  0,  0,
		0,  0,  0,  0,  0,  0,  0,  0,
		0,  0,  0,  0,  0,  0,  0,  0,
		0,  0,  0,  0,  0,  0,  0,  0,
		0,  0,  0,  0,  0,  0,  0,  0,
		0,  0,  0,  0,  0,  0,  0,  0,
		0,  0,  0,  0,  0,  0,  0, 	0

};

/*
 * In the final the queen is incouraged to stay in the center
 */
int queenPositionTableEnd []={

		0,  0,  0,  0,  0,  0,  0,  0,
		0, 10, 10, 10, 10, 10, 10,  0,
		0, 10, 10, 15, 15, 10, 10,  0,
		0, 10, 15, 20, 20, 15, 10,  0,
		0, 10, 15, 20, 20, 15, 10,  0,
		0, 10, 10, 15, 15, 10, 10,  0,
		0, 10, 10, 10, 10, 10, 10,  0,
		0,  0,  0,  0,  0,  0,  0,  0

};

/*
 * ROOKS
 */
/*
 * In the opening the rooks are encouraged to stay in the first rank and to protect the king
 */
int rookPositionTableOpening[] = {


		0,  0,  0,  0,  0,  0,  0,  0,
		0,  0,  0,  0,  0,  0,  0,  0,
		-5,  0,  0,  0,  0,  0,  0  -5,
		-5,  0,  0,  0,  0,  0,  0, -5,
		-5,  0,  0,  0,  0,  0,  0, -5,
		-5,  0,  0,  0,  0,  0,  0, -5,
		-5,  0,  0,  0,  0,  0,  0, -5,
		0,  0, 10, 10, 10, 10,  0,  0
};

/*
 * In the middle the rook are encourage to go high in the center.
 */
int rookPositionTableMiddle[] = {

		0,  0,  0,  0,  0,  0,  0,  0,
		0,  0,  0,  0,  0,  0,  0,  0,
		-5,  5,  5,  5,  5,  5,  5  -5,
		-5,  5,  5,  5,  5,  5,  5, -5,
		-5,  5,  5,  5,  5,  5,  5, -5,
		-5,  5,  5,  5,  5,  5,  5, -5,
		-5,  5,  5,  5,  5,  5,  5, -5,
		0,  0, 15, 15, 15, 15,  0,  0
};

/*
 * In the end the rook are more encouraged to advance and to occupy the last rank.
 */
int rookPositionTableEnd[] = {

		0,  5,  5,  5,  5,  5,  5,  0,
		0, 10, 10, 10, 10, 10, 10,  0,
		-5, 10, 10, 15, 15, 10, 10, -5,
		-5, 10, 15, 20, 20, 15, 10, -5,
		-5, 10, 15, 20, 20, 15, 10, -5,
		-5, 10, 10, 15, 15, 10, 10, -5,
		-5, 10, 10, 10, 10, 10, 10, -5,
		0,  0,  0,  0,  0,  0,  0,  0
};


/*
 KNIGHTS
 	 -Centralization bonus
 	 -penality to not being developed
 	 -one table for all the
 */
int knightPositionTableAll []={

		-8,  -8,  -8,  -8,  -8,    -8,  -8,  -8,
		-8,   0,   0,   0,    0,    0,   0,  -8,
		-8,   0,  13,  16,   16,   13,   0,  -8,
		-8,   0,  16,  20,   20,   16,   0,  -8,
		-8,   0,  16,  20,   20,   16,   0,  -8,
		-8,  10,  13,  16,   16,   13,  10,  -8,
		-8,  13,  10,  10,   10,   10,  13,  -8,
		-8,  -8, -12,  -8,   -8,  -12,  -8,  -8

};

/*
 * KING
 */
//King in the initial game are encouraged to stay covered
int  kingPositionTableOpeningMiddle [] = {

		-40, -40, -40, -40, -40, -40, -40, -40,
		-40, -40, -40, -40, -40, -40, -40, -40,
		-40, -40, -40, -40, -40, -40, -40, -40,
		-40, -40, -40, -40, -40, -40, -40, -40,
		-40, -40, -40, -40, -40, -40, -40, -40,
		-40, -40, -40, -40, -40, -40, -40, -40,
		-15, -15, -20, -20, -20, -20, -15, -15,
		0,  20,  30, -30,   0, -20,  30,  20

};

//  During the middle game kings are encouraged to stay in the corners, while in the
//  end game kings are encouraged to move towards the center


int  kingPositionTableEnd [] = {

		0,  10,  20,  30,  30,  20,  10,   0,
		10,  20,  30,  40,  40,  30,  20,  10,
		20,  30,  40,  50,  50,  40,  30,  20,
		30,  40,  50,  60,  60,  50,  40,  30,
		30,  40,  50,  60,  60,  50,  40,  30,
		20,  30,  40,  50,  50,  40,  30,  20,
		10,  20,  30,  40,  40,  30,  20,  10,
		0,  10,  20,  30,  30,  20,  10,   0
};


/*
 * Array for the conversion between the array of position and the index on the board
 */

int indexPosition[]={

		56, 57, 58, 59, 60, 61, 62, 63,
		48, 49, 50, 51, 52, 53, 54, 55,
		40, 41, 42, 43, 44, 45, 46, 47,
		32, 33, 34, 35, 36, 37, 38, 39,
		24, 25, 26, 27, 28, 29, 30, 31,
		16, 17, 18, 19, 20, 21, 22, 23,
		8,  9, 10, 11, 12, 13, 14, 15,
		0,  1,  2,  3,  4,  5,  6,  7

};

int get_Heuristic (Board *board , int depth){

	if ( board == NULL ) {
		logChess(WARN, "The board is null.");
		return NULL_BOARD;
	}

	if ( depth < 0 ) {
		logChess(WARN, "The depth is not correct. Minor than 0.");
		return BAD_DEPTH;
	}

	if(evaluateOurCheck(board)!=KING_IN_CHECK)
	{

		Board tmpBoard;
		memcpy(&tmpBoard, board, sizeof(Board));

		int material = 		getTotalTableMaterial(&tmpBoard);

		int mineMaterial=	getMaterial(board,board->myColor);					 //Calculating mine material to evaluate the game phase
		int movements=		getCurrentMovement(depth);							 //Calculating number of movements to calculate game phase
		int phase	 =		getGamePhase(board,movements,mineMaterial); 		 //Calculating phase of the game to be used in position
		int position = 		getTotalPiecePositionValue(&tmpBoard,depth,phase);	// Calculating game phase according to the position

		int menaced  =		getMenacedValueTotal(tmpBoard.myColor,&tmpBoard);

		int side = 			sideToMove(depth);
		int evaluation = 	0;
		if ( phase == END) {
			evaluation=2*material+ 0.5*position+ 0.1*menaced;
		} else {
			evaluation=1.6*material + 0.9*position+ 0.2*menaced;
		}


		//printf("material:%d + position:%d + menaced:%d = evaluation:%d\n", material, position, menaced, evaluation); //ONLY FOR TEST

		return evaluation * side;
	}

	else 	//we are in check, it's not useful to check with the heuristic
	{
		return OUR_CHECK_VALUE;
	}
}

int getTotalTableMaterial(Board *board)
{
	int material;
	//calculating the material for the white and the black
	int material_white= getMaterial(board,WHITE);
	int material_black= getMaterial(board,BLACK);

	if(board->myColor==BLACK)
		material=material_black-material_white;		//If I am black I have to use the black material first
	else
		material=material_white-material_black;
	return material;
}

int pieceCount(BitMap * actualBoard){

	//Control that the board isn't null
	if ( actualBoard == NULL ) {
		logChess(FATAL, "The board is null.");
		return NULL_BOARD;
	}

	if ( *actualBoard == 0 ) {
		return 0;	//there are no piece of these type
	}

	BitMap aux_board=*actualBoard;
	BitMap aux_board2=*actualBoard;

	int i;
	int pieceCount=0;

	//shift 64 time and control if the last bit is 1
	for(i=0; i<64; i++){

		aux_board2=aux_board;
		aux_board2&=0x0000000000000001;

		if(aux_board2==0x0000000000000001)
			pieceCount++;

		aux_board>>=1;
	}

	return pieceCount;//number of counted piece
}


int getMaterial(Board * board,  int pieceType){

	//Control that the board isn't null
	if ( board == NULL ) {
		logChess(FATAL, "The board is null.");
		return NULL_BOARD;
	}

	int material=0;
	BitMap * currentPieces;

	//assign to current pieces the black or white pieces
	if(pieceType==BLACK)
		currentPieces = board->blackPieces;
	else
		currentPieces = board->whitePieces;

	int pawns =		pieceCount(&currentPieces[Pawns]);
	int bishop =	pieceCount(&currentPieces[Bishops]);
	int knight =	pieceCount(&currentPieces[Knights]);
	int queen = 	pieceCount(&currentPieces[Queens]);
	int rook = 		pieceCount(&currentPieces[Rooks]);

	/*
	 * Log of the board									//Used only for testing
	 */
	/*
	printf("%s: %d \n", "Pawns :", pawns);
	printf("%s: %d \n", "Bishop :", bishop);
	printf("%s: %d \n", "Knight :", knight);
	printf("%s: %d \n", "Queen :", queen);
	printf("%s: %d \n", "Rook :", rook);
	 */

	//calculate the material for every pieces
	material += pawns * PAWN_VALUE;
	material += rook * ROOK_VALUE;
	material += bishop * BISHOP_VALUE;
	material += knight * KNIGHT_VALUE;
	material += queen * QUEEN_VALUE;

	return material;

}

int sideToMove(int depth){

	if((depth%2)==0)		//I am in the level 0,2,4,6,..etc;enemies turn
		return  1;
	else					//I am in the level 1,3,5,6..I am moving
		return -1;

}


int getTotalPiecePositionValue(Board * board, int depth, int phase){


	/*
	 *FIRST WE CALCULATE THE POSITIONS VALUE FOR WITE PIECES
	 */
	int pieceValueWhite=0;

	pieceValueWhite+=valueForPiecesPosition(&board->whitePieces[Pawns],Pawns,WHITE,phase);
	pieceValueWhite+=valueForPiecesPosition(&board->whitePieces[Rooks],Rooks,WHITE,phase);
	pieceValueWhite+=valueForPiecesPosition(&board->whitePieces[Knights],Knights,WHITE,phase);
	pieceValueWhite+=valueForPiecesPosition(&board->whitePieces[Bishops],Bishops,WHITE,phase);
	pieceValueWhite+=valueForPiecesPosition(&board->whitePieces[Queens],Queens,WHITE,phase);
	pieceValueWhite+=valueForPiecesPosition(&board->whitePieces[King],King,WHITE,phase);

	/*
	 * SECOND WE CALCULATE THE POSITION VALUE FOR ALL THE BLACK PIECES
	 */
	int pieceValueBlack=0;

	pieceValueBlack+=valueForPiecesPosition(&board->blackPieces[Pawns],Pawns,BLACK,phase);
	pieceValueBlack+=valueForPiecesPosition(&board->blackPieces[Rooks],Rooks,BLACK,phase);
	pieceValueBlack+=valueForPiecesPosition(&board->blackPieces[Knights],Knights,BLACK,phase);
	pieceValueBlack+=valueForPiecesPosition(&board->blackPieces[Bishops],Bishops,BLACK,phase);
	pieceValueBlack+=valueForPiecesPosition(&board->blackPieces[Queens],Queens,BLACK,phase);
	pieceValueBlack+=valueForPiecesPosition(&board->blackPieces[King],King,BLACK,phase);

	/*
	 * Now we calculate the heuristic value, depending if we're black or white
	 */
	if(board->myColor==WHITE)
		return pieceValueWhite-pieceValueBlack;
	else
	{
		return pieceValueBlack-pieceValueWhite;
	}
}


int valueForPiecesPosition(BitMap * piecesPosition, int pieceType, int pieceColor, int phase){


	if ( piecesPosition == NULL ) {
		logChess(FATAL, "The position is null.");
		return ERROR_NULL_POSITION;
	}


	int retValue=0;
	int piecesPositionFound [8];

	int result=getPiecePosition(piecesPosition, pieceType, piecesPositionFound);
	if(result ==SUCCESS)//if not return SUCCESS the table is empty
	{
		int i;
		for(i=0; i < 8; i++)
		{
			if(piecesPositionFound[i] == -1 || piecesPositionFound[i] == -9)
				break;//exit from the loop if there's not more piece of these type

			int position=getRelativePosition(piecesPositionFound[i],pieceColor);
			retValue+=getValueForPosition(position,pieceType, phase);//here we sum every value of position for every piece
		}
	}

	return retValue;
}

int getRelativePosition(int position, int pieceColor){

	//Test the position
	if (position<0 || position > 63)
	{
		logChess(FATAL, "The position is invalid.");
		return ERROR_POSITION_VALUE;
	}
	//Test the color
	if (pieceColor!=BLACK && pieceColor!=WHITE)
	{
		logChess(FATAL, "The color is invalid.");
		return ERROR_COLOR;
	}


	if(pieceColor==WHITE)

		return position;
	else
		return 63-position;
}

int getValueForPosition(int position, int pieceType, int phase){

	if (position<0 || position > 63)
		{
			logChess(FATAL, "The position is invalid.");
			return ERROR_POSITION_VALUE;
		}

	switch (pieceType)
	{
	case Pawns:
		switch (phase)
		{
		case OPENING:
			return pawnPositionTableOpening[indexPosition[position]]*BONUS_PAWN_OPEN;
		case MIDDLE:
			return pawnPositionTableMiddle[indexPosition[position]]*BONUS_PAWN_MIDDLE;
		case END:
			return pawnPositionTableEnd[indexPosition[position]];
		default:
			break;
		}
		break;
		case Knights:
			switch (phase)
			{
			case OPENING:
				return knightPositionTableAll[indexPosition[position]];
			case MIDDLE:
				return knightPositionTableAll[indexPosition[position]];
			case END:
				return knightPositionTableAll[indexPosition[position]];
			default:
				break;
			}
			break;
			case Rooks:

				switch (phase)
				{
				case OPENING:
					return rookPositionTableOpening[indexPosition[position]];
				case MIDDLE:
					return rookPositionTableMiddle[indexPosition[position]];
				case END:
					return rookPositionTableEnd[indexPosition[position]];
				default:
					break;
				}
				break;
				case Queens:
					switch (phase)
					{
					case OPENING:
						return queenPositionTableOpeningMiddle[indexPosition[position]];
					case MIDDLE:
						return queenPositionTableOpeningMiddle[indexPosition[position]];
					case END:
						return queenPositionTableEnd[indexPosition[position]];
					default:
						break;
					}
					break;
					case King:
						switch (phase)
						{
						case OPENING:
							return kingPositionTableOpeningMiddle[indexPosition[position]];
						case MIDDLE:
							return kingPositionTableOpeningMiddle[indexPosition[position]];
						case END:
							return kingPositionTableEnd[indexPosition[position]];
						default:
							break;
						}
						break;
						case Bishops:
							switch (phase)
							{
							case OPENING:
								return bishopPositionTableAll[indexPosition[position]];
								break;
							case MIDDLE:
								return bishopPositionTableAll[indexPosition[position]];
								break;
							case END:
								return bishopPositionTableAll[indexPosition[position]];
								break;
							}
							break;

	}

	return ERROR_GET_VALUE_POSITION;//if I not enter in the switch the value it's not correct

}

int getMenacedValueTotal(int myColor, Board * actualBoard){

	if(myColor==BLACK)
		return menacedPiecesOneColor(BLACK,actualBoard) - menacedPiecesOneColor(WHITE,actualBoard);
	else
		return menacedPiecesOneColor(WHITE,actualBoard) - menacedPiecesOneColor(BLACK,actualBoard);
}

int menacedPiecesOneColor(int pieceColor, Board * actualBoard){

	int menacedValue=0;
	int i;

	if(pieceColor==BLACK)
	{
		for (i=0; i<N_TYPES; i++)
			menacedValue += menacedPiecesSingleType(actualBoard,BLACK,i);
	}
	else
	{
		for (i=0; i<N_TYPES; i++)
			menacedValue += menacedPiecesSingleType(actualBoard,WHITE,i);
	}

	return menacedValue;
}

int menacedPiecesSingleType(Board * actualBoard, int pieceColor, int pieceType){

	int piecesPosition[8], menacedValue;

	//Select the proper took color according to pieceColor
	BitMap piecesBitmap=0;

	if(pieceColor==BLACK)
	{
		piecesBitmap=actualBoard->blackPieces[pieceType];
	}
	else
	{
		piecesBitmap=actualBoard->whitePieces[pieceType];

	}


	int ppSuccess=getPiecePosition(&piecesBitmap,pieceType,piecesPosition);

	//Check if a table is empty return 0: cannot menace anyone!
	if(ppSuccess == NO_PIECES_IN_BOARD)
	{
		return 0;
	}
	if ( ppSuccess != SUCCESS ) {
		//logChess(WARN, "The piece position could not be retrieved.");
	}

	//for each position in the table let's calculate the menaced pieces
	int i;
	for(i=0; i<8; i++)
	{
		if((piecesPosition[i] == -9) ||( piecesPosition[i] ==- 1))
		{
			break;//exit from the for cycle if there are no extra movement to search
		}
		else
		{
			int col = piecesPosition[i] % 8;
			int row = (piecesPosition[i] / 8);

			BitMap aux_board = 0x8000000000000000;
			aux_board >>= piecesPosition[i];

			int generalCounter=0;

			Board tempBoard;
			memcpy(&tempBoard,actualBoard, sizeof(Board));

			switch (pieceType) {
			case King:
				moveKing(&aux_board, col, row, &generalCounter, &tempBoard);
				break;
			case Queens:
				moveQueen(&aux_board, col, row, &generalCounter, &tempBoard);
				break;
			case Rooks:
				moveRook(&aux_board, col, row, &generalCounter, &tempBoard);
				break;
			case Bishops:
				moveBishop(&aux_board, col, row, &generalCounter, &tempBoard);
				break;
			case Knights:
				moveKnight(&aux_board, col, row, &generalCounter, &tempBoard);
				break;
			case Pawns:
				movePawn(&aux_board, col, row, &generalCounter, &tempBoard);
				break;
			default:
				logChess(WARN, "Incorrect piece type.");
				break;
			}

			int j;
			for(j=0; j<MAX_NUM_MOVES; j++)
			{
				if((tempBoard.boardState[j].killedTypePiece > -1) && (tempBoard.boardState[j].killedTypePiece < 6))
				{
					menacedValue+=menacedHeuristicValue[tempBoard.boardState[j].killedTypePiece];
				}
				else
					break;
			}
		}
	}

	return menacedValue;

}

int evaluateOurCheck(Board * actualBoard){

	if(actualBoard->myColor==WHITE)
	{
		//Check the existence of the king in the white
		if(actualBoard->whitePieces[King]==0)
			return KING_IN_CHECK;
		else
			return KING_NOT_IN_CHEK;
	}
	else
	{
		if(actualBoard->blackPieces[King]==0)
			return KING_IN_CHECK;
		else
			return KING_NOT_IN_CHEK;
	}

	return ERROR_EVALUATE_COLOR;
}

int getGamePhase(Board * board, int movement, int mineMaterial){

	if (movement <= OPEN_VALUE )
		return OPENING;
	else if(movement > OPEN_VALUE && mineMaterial < END_MATERIAL_VALUE )
		return END;
	else
		return MIDDLE;

}

int getCurrentMovement(int depth){

	return MOVEMENTS+depth;
}

int evaluateOpponentCheck(Board * actualBoard){

	if(actualBoard->myColor==WHITE)
	{
		if(actualBoard->blackPieces[King]==0)
			return KING_IN_CHECK;
		else
			return KING_NOT_IN_CHEK;
	}
	else
	{
		if(actualBoard->whitePieces[King]==0)
			return KING_IN_CHECK;
		else
			return KING_NOT_IN_CHEK;
	}

	return ERROR_EVALUATE_COLOR;

}

int white_jake_mate(Board* actualBoard, BitMap* king_position_oponent, BitMap* mov_position_king_oponent,BitMap*  mov_my_pieces){
	int i=0;
	//king_position_oponent
	*king_position_oponent=actualBoard->blackPieces[King];

	//______mov_position_king_oponent_________
	int position[8];
	int col = -1, row = -1;
	int generalCounter=0;
	Board b_aux;
	memcpy(&b_aux,actualBoard,sizeof(Board));
	BitMap aux_bitmap = 0x8000000000000000;

	getPiecePosition(&(b_aux.blackPieces[King]), King, position);
	if ( position[0] != -9 ) {
		col = position[0] % 8;
		row = position[0] / 8;

		aux_bitmap >>= position[0];

		moveKing(&aux_bitmap, col, row, &generalCounter, &b_aux);
	}

	for(i=0;i<27;i++){
		*mov_position_king_oponent|=b_aux.boardState[i].generatedMove;
	}

	//__________mov_my_pieces___________
	for(i=0;i<8;i++) position[i]=-9;

	col = -1, row = -1;

	memcpy(&b_aux,actualBoard,sizeof(Board));
	aux_bitmap = 0;
	int retValue = -1;

	/////////////////////--------------
	int piece_type=0;
	for(piece_type=0; piece_type < N_TYPES; piece_type++){
		generalCounter=0;
		for(i = 0; i < MAX_NUM_MOVES; i++){
			b_aux.boardState[i].generatedMove = 0;
			b_aux.boardState[i].killedTypePiece = -1;
		}

		getPiecePosition(&(b_aux.whitePieces[piece_type]), piece_type, position);

		for (i = 0; i < 8; i++) {	//Because we can (max) have 8 pieces from the same type (8 pawns)
			if ( position[i] != -9 ) {
				col = position[i] % 8;
				row = position[i] / 8;
				BitMap aux_board = 0x8000000000000000;
				aux_board >>= position[i];
				retValue = -1;
				switch (piece_type) {
				case King:
					retValue = moveKing(&aux_board, col, row, &generalCounter, &b_aux);
					break;
				case Queens:
					retValue = moveQueen(&aux_board, col, row, &generalCounter, &b_aux);
					break;
				case Rooks:
					retValue = moveRook(&aux_board, col, row, &generalCounter, &b_aux);
					break;
				case Bishops:
					retValue = moveBishop(&aux_board, col, row, &generalCounter, &b_aux);
					break;
				case Knights:
					retValue = moveKnight(&aux_board, col, row, &generalCounter, &b_aux);
					break;
				case Pawns:
					retValue = movePawn(&aux_board, col, row, &generalCounter, &b_aux);
					break;
				default:
					logChess(WARN, "Incorrect piece type.");
					break;
				}


			} else {
				break;
			}
		}

		for(i=0;i<27;i++){
			*mov_my_pieces|=b_aux.boardState[i].generatedMove;
		}
	}
	////////////////-------------

	return SUCCESS;

}

int black_jake_mate(Board* actualBoard, BitMap* king_position_oponent, BitMap* mov_position_king_oponent,BitMap*  mov_my_pieces){
	int i=0;
	//king_position_oponent
	*king_position_oponent=actualBoard->whitePieces[King];

	//______mov_position_king_oponent_________
	int position[8];
	int col = -1, row = -1;
	int generalCounter=0;
	Board b_aux;
	memcpy(&b_aux,actualBoard,sizeof(Board));
	BitMap aux_bitmap = 0x8000000000000000;

	getPiecePosition(&(b_aux.whitePieces[King]), King, position);
	if ( position[0] != -9 ) {
		col = position[0] % 8;
		row = position[0] / 8;

		aux_bitmap >>= position[0];

		moveKing(&aux_bitmap, col, row, &generalCounter, &b_aux);
	}

	for(i=0;i<27;i++){
		*mov_position_king_oponent|=b_aux.boardState[i].generatedMove;
	}

	//__________mov_my_pieces___________
	for(i=0;i<8;i++) position[i]=-9;

	col = -1, row = -1;

	memcpy(&b_aux,actualBoard,sizeof(Board));
	aux_bitmap = 0;
	int retValue = -1;

	/////////////////////--------------
	int piece_type=0;
	for(piece_type=0; piece_type < N_TYPES; piece_type++){
		generalCounter=0;
		for(i = 0; i < MAX_NUM_MOVES; i++){
			b_aux.boardState[i].generatedMove = 0;
			b_aux.boardState[i].killedTypePiece = -1;
		}

		getPiecePosition(&(b_aux.blackPieces[piece_type]), piece_type, position);

		for (i = 0; i < 8; i++) {	//Because we can (max) have 8 pieces from the same type (8 pawns)
			if ( position[i] != -9 ) {
				col = position[i] % 8;
				row = position[i] / 8;
				BitMap aux_board = 0x8000000000000000;
				aux_board >>= position[i];
				retValue = -1;
				switch (piece_type) {
				case King:
					retValue = moveKing(&aux_board, col, row, &generalCounter, &b_aux);
					break;
				case Queens:
					retValue = moveQueen(&aux_board, col, row, &generalCounter, &b_aux);
					break;
				case Rooks:
					retValue = moveRook(&aux_board, col, row, &generalCounter, &b_aux);
					break;
				case Bishops:
					retValue = moveBishop(&aux_board, col, row, &generalCounter, &b_aux);
					break;
				case Knights:
					retValue = moveKnight(&aux_board, col, row, &generalCounter, &b_aux);
					break;
				case Pawns:
					retValue = movePawn(&aux_board, col, row, &generalCounter, &b_aux);
					break;
				default:
					logChess(WARN, "Incorrect piece type.");
					break;
				}


			} else {
				break;
			}
		}

		for(i=0;i<27;i++){
			*mov_my_pieces|=b_aux.boardState[i].generatedMove;
		}
	}
	////////////////-------------

	return SUCCESS;
}

int jake_mate_heuristic(Board* actualBoard, int depth){


	Board tmpBoard;
	memcpy(&tmpBoard, actualBoard, sizeof(Board));

	int mineMaterial=	getMaterial(actualBoard,actualBoard->myColor);					 //Calculating mine material to evaluate the game phase
	int movements=		getCurrentMovement(depth);							 //Calculating number of movements to calculate game phase
	int phase	 =		getGamePhase(actualBoard,movements,mineMaterial); 		 //Calculating phase of the game to be used in position

	if ( phase != END ) {
	//if ( phase == OPENING){
		return 0;
	}

	//BitMap necesaris
	BitMap king_position_oponent=0,mov_position_king_oponent=0,mov_my_pieces=0;
	BitMap total_king_oponent=0,killer_king=0;

	if(actualBoard->myColor==WHITE){
		//printf("WHITE");
		white_jake_mate(actualBoard, &king_position_oponent, &mov_position_king_oponent, &mov_my_pieces);

	}else{
		//printf("BLACK");
		//BLACK
		black_jake_mate(actualBoard, &king_position_oponent, &mov_position_king_oponent, &mov_my_pieces);
	}


	//Calculu jake mate
	if(mov_position_king_oponent != 0){//posicio del rei diferent a 0
		total_king_oponent = king_position_oponent | mov_position_king_oponent;
		killer_king =  total_king_oponent ^ mov_my_pieces;

		/*logChess(INFO,"_____king_position_oponent____\n");
		logBoard(INFO, king_position_oponent);
		logChess(INFO,"______mov_position_king_oponent___\n");
		logBoard(INFO, mov_position_king_oponent);
		logChess(INFO,"_____total_king_oponent = king_position_oponent | mov_position_king_oponent____\n");
		logBoard(INFO, total_king_oponent);
		logChess(INFO,"______mov_my_pieces___\n");
		logBoard(INFO, mov_my_pieces);
		logChess(INFO,"______killer_king___\n");
		logBoard(INFO, killer_king);
		logChess(INFO,"_________\n");*/


		if(total_king_oponent == killer_king ){//jake Mate
			//printf("\t-------JAKE MATE----------\n");

			return 100;

		}

	}


	return 0;
}

