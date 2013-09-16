/*
 * move_functions.c
 *
 *  Created on: 25/nov/2011
 *      Author: Newlog
 */
#include <stdio.h>
#include "move_functions.h"

int slidingUpDownRightLeft( BitMap * board,
							int col,
							int row,
							int * generalCounter, Board *boardStruct) {

	if ( board == NULL ) {
		logChess(FATAL, "The board is null.");
		return NULL_BOARD;
	}

	if ( boardStruct == NULL ) {
		logChess(FATAL, "The board struct is null.");
		return NULL_BOARD_STRUCT;
	}

	if ( boardStruct->boardState == NULL ) {
		logChess(FATAL, "The board.boardState array is null.");
		return NULL_POSITION;
	}

	if ( generalCounter == NULL ) {
		logChess(FATAL, "The general counter is null.");
		return NULL_COUNTER;
	}
	if ( col == -1 || row == -1 ) {
		logChess(INFO, "No pieces in the board.");
		return NO_PIECES_IN_BOARD;
	}

	int i = 0;
	/*
	 * The left shift
	 */
	int numberOfLeftShifts = col;
	BitMap aux_board = *board;
	for ( i = 0; i < numberOfLeftShifts; i++ ) {
		aux_board <<= 1;
		if(checkResolveConflicts(&aux_board, generalCounter, boardStruct)==0)
			break;
	}

	/*
	 * The right shift
	 */
	int numberOfRightShifts = 7 - col;
	aux_board = *board;
	for(i = 0; i < numberOfRightShifts; i++){
		aux_board >>= 1;
		if(checkResolveConflicts(&aux_board, generalCounter, boardStruct)==0)
			break;
	}

	/*
	 * The "up shift" (it must go inside a loop). Without the loop is just the upper one.
	 * LEFT SHIFT
	 */
	int numberOfUpShifts = 7 - row;
	aux_board = *board;
	for(i = 0; i < numberOfUpShifts; i++){
		aux_board >>= 8;
		if(checkResolveConflicts(&aux_board, generalCounter, boardStruct)==0)
			break;
	}

	/*
	 * The "down shift" (it must go inside a loop). Without the loop is just the down one.
	 * RIGHT SHIFT
	 */
	int numberOfDownShifts = row;
	aux_board = *board;

	for(i = 0; i < numberOfDownShifts; i++){
		aux_board <<= 8;
		if(checkResolveConflicts(&aux_board, generalCounter, boardStruct)==0)
			break;
	}


	return SUCCESS;
}


int moveDiagonalLeftRight(	BitMap * board,
							int col,
							int row,
							int * generalCounter,
							Board *boardStruct){

	if ( board == NULL ) {
		logChess(FATAL, "The board is null.");
		return NULL_BOARD;
	}
	if ( boardStruct == NULL ) {
		logChess(FATAL, "The board struct is null.");
		return NULL_BOARD_STRUCT;
	}
	if ( boardStruct->boardState == NULL ) {
		logChess(FATAL, "The board.boardState array is null.");
		return NULL_POSITION;
	}
	if ( generalCounter == NULL ) {
		logChess(FATAL, "The general counter is null.");
		return NULL_COUNTER;
	}
	if ( col == -1 || row == -1 ) {
		logChess(INFO, "No pieces in the board.");
		return NO_PIECES_IN_BOARD;
	}


	int move_left = col;
	int move_right = 7 - col;

	int move_up = 7 - row;
	int move_down = row;


	/*
	 *  Evaluation of the maximum number of movement to do for every diagonal
	 *  direction
	 */

	int i;
	int move_up_right = (move_up < move_right) ? move_up : move_right;
	int move_up_left = (move_up < move_left) ? move_up : move_left;
	int move_down_right = (move_down < move_right) ? move_down : move_right;
	int move_down_left = (move_down < move_left) ? move_down : move_left;



	/*
	 * Generate the up-right diagonal movement
	 */
	BitMap aux_board = *board;

	for( i = 0; i < move_up_right; i++)
	{
		aux_board >>= 9;
		if ( !checkResolveConflicts(&aux_board, generalCounter, boardStruct) )
			break;
	}

	/*
	 * Generate the up-left diagonal movement
	 */
	aux_board = *board;
	for (i = 0; i < move_up_left; i++)
	{
		aux_board >>= 7;
		if ( !checkResolveConflicts(&aux_board, generalCounter, boardStruct) )
			break;
	}

	/*
	 * Generate the down-right diagonal movement
	 */
	aux_board = *board;
	for (i = 0; i < move_down_right; i++)
	{
		aux_board <<= 7;
		if ( !checkResolveConflicts(&aux_board, generalCounter, boardStruct) )
			break;

	}


	/*
	 * Generate the down-left diagonal movement
	 */
	aux_board = *board;
	for (i = 0; i < move_down_left; i++)
	{
		aux_board <<= 9;
		if ( !checkResolveConflicts(&aux_board, generalCounter, boardStruct) )
			break;
	}

	return SUCCESS;

}


int moveUpKing(BitMap * board,
			int col,
			int row,
			int * generalCounter,
			Board * boardStruct){

	if ( board == NULL ) {
		logChess(FATAL, "The board is null.");
		return NULL_BOARD;
	}
	if ( boardStruct == NULL ) {
		logChess(FATAL, "The board struct is null.");
		return NULL_BOARD_STRUCT;
	}
	if ( boardStruct->boardState == NULL ) {
		logChess(FATAL, "The board.boardState array is null.");
		return NULL_POSITION;
	}

	if ( generalCounter == NULL ) {
		logChess(FATAL, "The general counter is null.");
		return NULL_COUNTER;
	}
	if ( col == -1 || row == -1 ) {
		logChess(INFO, "No pieces in the board.");
		return NO_PIECES_IN_BOARD;
	}

	int up = ((row + 1) < 8);

	if( up )
	{
		BitMap aux_board = *board;
		aux_board >>= 8;
		//boardStruct->boardState[*generalCounter].generatedMove = aux_board;
		//(*generalCounter)++;
		checkResolveConflicts(&aux_board, generalCounter, boardStruct);

	}

	return SUCCESS;
}


int moveUpPawn(BitMap * board,
			int col,
			int row,
			int * generalCounter,
			Board * boardStruct){

	if ( board == NULL ) {
		logChess(FATAL, "The board is null.");
		return NULL_BOARD;
	}
	if ( boardStruct == NULL ) {
		logChess(FATAL, "The board struct is null.");
		return NULL_BOARD_STRUCT;
	}
	if ( boardStruct->boardState == NULL ) {
		logChess(FATAL, "The board.boardState array is null.");
		return NULL_POSITION;
	}

	if ( generalCounter == NULL ) {
		logChess(FATAL, "The general counter is null.");
		return NULL_COUNTER;
	}
	if ( col == -1 || row == -1 ) {
		logChess(INFO, "No pieces in the board.");
		return NO_PIECES_IN_BOARD;
	}

	int up = ((row + 1) < 8);

	if( up )
	{
		BitMap aux_board = *board;
		aux_board >>= 8;
		if ( !checkConflicts(&aux_board, boardStruct) ) {
			boardStruct->boardState[*generalCounter].generatedMove = aux_board;
			(*generalCounter)++;
		} else {
			return COLISION_FOUND;
		}

	}

	return SUCCESS;
}



int move2Up(BitMap * board,
			int col,
			int row,
			int * generalCounter,
			Board *boardStruct){

	if ( board == NULL ) {
		logChess(FATAL, "The board is null.");
		return NULL_BOARD;
	}
	if ( boardStruct == NULL ) {
		logChess(FATAL, "The board struct is null.");
		return NULL_BOARD_STRUCT;
	}
	if ( boardStruct->boardState == NULL ) {
		logChess(FATAL, "The board.boardState array is null.");
		return NULL_POSITION;
	}

	if ( generalCounter == NULL ) {
		logChess(FATAL, "The general counter is null.");
		return NULL_COUNTER;
	}
	if ( col == -1 || row == -1 ) {
		logChess(INFO, "No pieces in the board.");
		return NO_PIECES_IN_BOARD;
	}

	int up = ((row + 2) < 8);

	if( up )
	{
		BitMap aux_board = *board;
		aux_board >>= 16;
		if ( !checkConflicts(&aux_board, boardStruct) ) {
			boardStruct->boardState[*generalCounter].generatedMove = aux_board;
			(*generalCounter)++;
		}

	}

	return SUCCESS;
}

int moveDownKing(BitMap * board,
			int col,
			int row,
			int * generalCounter,
			Board *boardStruct){

	if ( board == NULL ) {
		logChess(FATAL, "The board is null.");
		return NULL_BOARD;
	}
	if ( boardStruct == NULL ) {
		logChess(FATAL, "The board struct is null.");
		return NULL_BOARD_STRUCT;
	}
	if ( boardStruct->boardState == NULL ) {
		logChess(FATAL, "The board.boardState array is null.");
		return NULL_POSITION;
	}

	if ( generalCounter == NULL ) {
		logChess(FATAL, "The general counter is null.");
		return NULL_COUNTER;
	}
	if ( col == -1 || row == -1 ) {
		logChess(INFO, "No pieces in the board.");
		return NO_PIECES_IN_BOARD;
	}

	int down = (( row - 1) >= 0);

	if( down )
	{
		BitMap aux_board = *board;
		aux_board <<= 8;
		//boardStruct->boardState[*generalCounter].generatedMove = aux_board;
		//(*generalCounter)++;
		checkResolveConflicts(&aux_board, generalCounter, boardStruct);

	}

	return SUCCESS;
}

int moveDownPawn(BitMap * board,
			int col,
			int row,
			int * generalCounter,
			Board *boardStruct){

	if ( board == NULL ) {
		logChess(FATAL, "The board is null.");
		return NULL_BOARD;
	}
	if ( boardStruct == NULL ) {
		logChess(FATAL, "The board struct is null.");
		return NULL_BOARD_STRUCT;
	}
	if ( boardStruct->boardState == NULL ) {
		logChess(FATAL, "The board.boardState array is null.");
		return NULL_POSITION;
	}

	if ( generalCounter == NULL ) {
		logChess(FATAL, "The general counter is null.");
		return NULL_COUNTER;
	}
	if ( col == -1 || row == -1 ) {
		logChess(INFO, "No pieces in the board.");
		return NO_PIECES_IN_BOARD;
	}

	int down = (( row - 1) >= 0);

	if( down )
	{
		BitMap aux_board = *board;
		aux_board <<= 8;
		if ( !checkConflicts(&aux_board, boardStruct) ) {
			boardStruct->boardState[*generalCounter].generatedMove = aux_board;
			(*generalCounter)++;
		} else {
			return COLISION_FOUND;
		}
	}

	return SUCCESS;
}


int move2Down(BitMap * board,
			int col,
			int row,
			int * generalCounter,
			Board *boardStruct){

	if ( board == NULL ) {
		logChess(FATAL, "The board is null.");
		return NULL_BOARD;
	}
	if ( boardStruct == NULL ) {
		logChess(FATAL, "The board struct is null.");
		return NULL_BOARD_STRUCT;
	}
	if ( boardStruct->boardState == NULL ) {
		logChess(FATAL, "The board.boardState array is null.");
		return NULL_POSITION;
	}

	if ( generalCounter == NULL ) {
		logChess(FATAL, "The general counter is null.");
		return NULL_COUNTER;
	}
	if ( col == -1 || row == -1 ) {
		logChess(INFO, "No pieces in the board.");
		return NO_PIECES_IN_BOARD;
	}

	int down = (( row - 2) >= 0);

	if( down )
	{
		BitMap aux_board = *board;
		aux_board <<= 16;
		if ( !checkConflicts(&aux_board, boardStruct) ) {
			boardStruct->boardState[*generalCounter].generatedMove = aux_board;
			(*generalCounter)++;
		}
	}

	return SUCCESS;
}

int moveLeft (	BitMap * board,
				int col,
				int row,
				int * generalCounter,
				Board *boardStruct){

	if ( board == NULL ) {
		logChess(FATAL, "The board is null.");
		return NULL_BOARD;
	}
	if ( boardStruct == NULL ) {
		logChess(FATAL, "The board struct is null.");
		return NULL_BOARD_STRUCT;
	}
	if ( boardStruct->boardState == NULL ) {
		logChess(FATAL, "The board.boardState array is null.");
		return NULL_POSITION;
	}

	if ( generalCounter == NULL ) {
		logChess(FATAL, "The general counter is null.");
		return NULL_COUNTER;
	}
	if ( col == -1 || row == -1 ) {
		logChess(INFO, "No pieces in the board.");
		return NO_PIECES_IN_BOARD;
	}

	int left = (( col - 1 ) >= 0);

	if( left )
	{
		BitMap aux_board = *board;
		aux_board <<= 1;
		checkResolveConflicts(&aux_board, generalCounter, boardStruct);
		//boardStruct->boardState[*generalCounter].generatedMove = aux_board;
		//(*generalCounter)++;

	}

	return SUCCESS;
}

int moveRight(BitMap * board,
			int col,
			int row,
			int * generalCounter,
			Board *boardStruct){

	if ( board == NULL ) {
		logChess(FATAL, "The board is null.");
		return NULL_BOARD;
	}
	if ( boardStruct == NULL ) {
		logChess(FATAL, "The board struct is null.");
		return NULL_BOARD_STRUCT;
	}
	if ( boardStruct->boardState == NULL ) {
		logChess(FATAL, "The board.boardState array is null.");
		return NULL_POSITION;
	}

	if ( generalCounter == NULL ) {
		logChess(FATAL, "The general counter is null.");
		return NULL_COUNTER;
	}
	if ( col == -1 || row == -1 ) {
		logChess(INFO, "No pieces in the board.");
		return NO_PIECES_IN_BOARD;
	}

	int right = (( col + 1 ) < 8);

	if ( right )
	{
		BitMap aux_board = *board;
		aux_board >>= 1;
		//boardStruct->boardState[*generalCounter].generatedMove = aux_board;
		//(*generalCounter)++;
		checkResolveConflicts(&aux_board, generalCounter, boardStruct);


	}

	return SUCCESS;
}

int moveUpRight(BitMap * board,
			int col,
			int row,
			int * generalCounter,
			Board *boardStruct){

	if ( board == NULL ) {
		logChess(FATAL, "The board is null.");
		return NULL_BOARD;
	}
	if ( boardStruct == NULL ) {
		logChess(FATAL, "The board struct is null.");
		return NULL_BOARD_STRUCT;
	}
	if ( boardStruct->boardState == NULL ) {
		logChess(FATAL, "The board.boardState array is null.");
		return NULL_POSITION;
	}
	if ( generalCounter == NULL ) {
		logChess(FATAL, "The general counter is null.");
		return NULL_COUNTER;
	}
	if ( col == -1 || row == -1 ) {
		logChess(INFO, "No pieces in the board.");
		return NO_PIECES_IN_BOARD;
	}

	int right = (( col + 1 ) < 8);
	int up=(( row + 1 ) < 8);

	if( right && up )
	{
		BitMap aux_board = *board;
		aux_board >>= 9;
		//boardStruct->boardState[*generalCounter].generatedMove = aux_board;
		//(*generalCounter)++;
		checkResolveConflicts(&aux_board, generalCounter, boardStruct);

	}

	return SUCCESS;
}

int moveUpLeft(BitMap * board,
			int col,
			int row,
			int * generalCounter,
			Board *boardStruct){

	if ( board == NULL ) {
		logChess(FATAL, "The board is null.");
		return NULL_BOARD;
	}
	if ( boardStruct == NULL ) {
		logChess(FATAL, "The board struct is null.");
		return NULL_BOARD_STRUCT;
	}
	if ( boardStruct->boardState == NULL ) {
		logChess(FATAL, "The board.boardState array is null.");
		return NULL_POSITION;
	}

	if ( generalCounter == NULL ) {
		logChess(FATAL, "The general counter is null.");
		return NULL_COUNTER;
	}
	if ( col == -1 || row == -1 ) {
		logChess(INFO, "No pieces in the board.");
		return NO_PIECES_IN_BOARD;
	}

	int left = (( col - 1 ) >= 0);
	int up=(( row + 1 ) < 8);

	if(left && up)
	{
		BitMap aux_board = *board;
		aux_board >>= 7;
		//boardStruct->boardState[*generalCounter].generatedMove = aux_board;
		//(*generalCounter)++;
		checkResolveConflicts(&aux_board, generalCounter, boardStruct);

	}

	return SUCCESS;
}


int moveDownLeft(BitMap * board,
			int col,
			int row,
			int * generalCounter,
			Board *boardStruct){

	if ( board == NULL ) {
		logChess(FATAL, "The board is null.");
		return NULL_BOARD;
	}
	if ( boardStruct == NULL ) {
		logChess(FATAL, "The board struct is null.");
		return NULL_BOARD_STRUCT;
	}
	if ( boardStruct->boardState == NULL ) {
		logChess(FATAL, "The board.boardState array is null.");
		return NULL_POSITION;
	}

	if ( generalCounter == NULL ) {
		logChess(FATAL, "The general counter is null.");
		return NULL_COUNTER;
	}
	if ( col == -1 || row == -1 ) {
		logChess(INFO, "No pieces in the board.");
		return NO_PIECES_IN_BOARD;
	}

	int left=(( col - 1 ) >= 0);
	int down=(( row - 1 ) >= 0);

	if(left && down)
	{
		BitMap aux_board = *board;
		aux_board <<= 9;
		//boardStruct->boardState[*generalCounter].generatedMove = aux_board;
		//(*generalCounter)++;
		checkResolveConflicts(&aux_board, generalCounter, boardStruct);

	}

	return SUCCESS;
}

int moveDownRight(BitMap * board,
			int col,
			int row,
			int * generalCounter,
			Board *boardStruct){

	if ( board == NULL ) {
		logChess(FATAL, "The board is null.");
		return NULL_BOARD;
	}
	if ( boardStruct == NULL ) {
		logChess(FATAL, "The board struct is null.");
		return NULL_BOARD_STRUCT;
	}
	if ( boardStruct->boardState == NULL ) {
		logChess(FATAL, "The board.boardState array is null.");
		return NULL_POSITION;
	}

	if ( generalCounter == NULL ) {
		logChess(FATAL, "The general counter is null.");
		return NULL_COUNTER;
	}
	if ( col == -1 || row == -1 ) {
		logChess(INFO, "No pieces in the board.");
		return NO_PIECES_IN_BOARD;
	}

	int right= (( col + 1 ) < 8);
	int down=(( row - 1 ) >= 0);

	if(right && down)
	{
		BitMap aux_board = *board;
		aux_board <<= 7;
		//boardStruct->boardState[*generalCounter].generatedMove = aux_board;
		//(*generalCounter)++;
		checkResolveConflicts(&aux_board, generalCounter, boardStruct);

	}

	return SUCCESS;
}


int jumpHorse(BitMap * board,
		 int col,
		 int row,
		 int * generalCounter,
		 Board *boardStruct){

	if ( board == NULL ) {
		logChess(FATAL, "The board is null.");
		return NULL_BOARD;
	}
	if ( boardStruct == NULL ) {
		logChess(FATAL, "The board struct is null.");
		return NULL_BOARD_STRUCT;
	}
	if ( boardStruct->boardState == NULL ) {
		logChess(FATAL, "The board.boardState array is null.");
		return NULL_POSITION;
	}
	if ( generalCounter == NULL ) {
		logChess(FATAL, "The general counter is null.");
		return NULL_COUNTER;
	}
	if ( col == -1 || row == -1 ) {
		logChess(INFO, "No pieces in the board.");
		return NO_PIECES_IN_BOARD;
	}

	int left_2=((col-2)>=0);
	int left=((col-1) >=0);

	int up_2=((row+2)<8);
	int up= ((row+1)<8);

	int down_2=((row-2)>=0);
	int down=((row-1)>=0);

	int right_2=((col+2)<8);
	int right= ((col+1)<8);


	/*
	 * Movements up-direction
	 */

	if(up_2)
	{
		if(left)
		{
			BitMap aux_board = *board;
			aux_board >>=15;
			checkResolveConflicts(&aux_board, generalCounter, boardStruct);
		}
		if(right)
		{
			BitMap aux_board = *board;
			aux_board >>= 17;
			checkResolveConflicts(&aux_board, generalCounter, boardStruct);
		}
	}

	if(down_2)
	{
		if(left)
		{
			BitMap aux_board = *board;
			aux_board <<=17;
			checkResolveConflicts(&aux_board, generalCounter, boardStruct);
		}
		if(right)
		{
			BitMap aux_board = *board;
			aux_board <<= 15;
			checkResolveConflicts(&aux_board, generalCounter, boardStruct);
		}
	}

	if(left_2)
	{
		if(up)
		{
			BitMap aux_board = *board;
			aux_board >>=6;
			checkResolveConflicts(&aux_board, generalCounter, boardStruct);
		}
		if(down)
		{
			BitMap aux_board = *board;
			aux_board <<= 10;
			checkResolveConflicts(&aux_board, generalCounter, boardStruct);
		}
	}

	if(right_2)
	{
		if(up)
		{
			BitMap aux_board = *board;
			aux_board >>=10;
			checkResolveConflicts(&aux_board, generalCounter, boardStruct);
		}
		if(down)
		{
			BitMap aux_board = *board;
			aux_board <<= 6;
			checkResolveConflicts(&aux_board, generalCounter, boardStruct);
		}
	}

	return SUCCESS;

}

int stepPawn(BitMap * board, int col, int row, int * generalCounter, Board * boardStruct) {

	if ( board == NULL ) {
		logChess(FATAL, "The board is null.");
		return NULL_BOARD;
	}
	if ( boardStruct == NULL ) {
		logChess(FATAL, "The board struct is null.");
		return NULL_BOARD_STRUCT;
	}
	if ( boardStruct->boardState == NULL ) {
		logChess(FATAL, "The board.boardState array is null.");
		return NULL_POSITION;
	}
	if ( col == -1 || row == -1 ) {
		logChess(INFO, "No pieces in the board.");
		return NO_PIECES_IN_BOARD;
	}
	if ( generalCounter == NULL ) {
		logChess(FATAL, "The general counter is null.");
		return NULL_COUNTER;
	}

	if (  boardStruct->actualColor == WHITE ) {
		if ( row == 1 ) {	// We can do 2 jumps
			if ( moveUpPawn(board, col, row, generalCounter, boardStruct) != COLISION_FOUND )
				move2Up(board, col, row, generalCounter, boardStruct);
		} else {
			moveUpPawn(board, col, row, generalCounter, boardStruct);
		}
		// Normal pawn kill, no enpassant
		if ( row < 7 && col < 7) {
			BitMap board_aux = *board;
			board_aux >>= 9;
			if ( (board_aux & boardStruct->totalBlackPieces) != 0 ) {
				moveUpRight(board, col, row, generalCounter, boardStruct);
			}
		}
		if ( row < 7 && col > 0) {
			BitMap board_aux = *board;
			board_aux >>= 7;
			if ( (board_aux & boardStruct->totalBlackPieces) != 0 ) {
				moveUpLeft(board, col, row, generalCounter, boardStruct);
			}
		}
		// END normal pawn kill, no enpassant
	} else if ( boardStruct->actualColor == BLACK ) {
		if ( row == 6 ) {	// We can do 2 jumps
			if ( moveDownPawn(board, col, row, generalCounter, boardStruct) != COLISION_FOUND )
				move2Down(board, col, row, generalCounter, boardStruct);
		} else {
			moveDownPawn(board, col, row, generalCounter, boardStruct);
		}
		// Normal pawn kill, no enpassant
		if ( row > 0 && col < 7) {
			BitMap board_aux = *board;
			board_aux <<= 7;
			if ( (board_aux & boardStruct->totalWhitePieces) != 0 ) {
				moveDownRight(board, col, row, generalCounter, boardStruct);
			}
		}
		if ( row < 7 && col > 0) {
			BitMap board_aux = *board;
			board_aux <<= 9;
			if ( (board_aux & boardStruct->totalWhitePieces) != 0 ) {
				moveDownLeft(board, col, row, generalCounter, boardStruct);
			}
		}
		// END normal pawn kill, no enpassant
	}
	return SUCCESS;
}

int checkConflicts(BitMap * movement, Board * boardStruct){
	if ( movement == NULL ) {
		logChess(FATAL, "The movement is null.");
		return NULL_MOVEMENT;
	}
	if ( boardStruct == NULL ) {
		logChess(FATAL, "The board struct is null.");
		return NULL_BOARD_STRUCT;
	}

	if((*movement & boardStruct->occupiedSquares)!=0)
		return 1; 	// I found a collision, so I cannot generate more movements
	else return 0;
}

int resolveConflicts(BitMap * movement, Board * boardStruct){
	if ( movement == NULL ) {
		logChess(FATAL, "The movement is null.");
		return NULL_MOVEMENT;
	}
	if ( boardStruct == NULL ) {
		logChess(FATAL, "The board struct is null.");
		return NULL_BOARD_STRUCT;
	}
	if (boardStruct->actualColor == WHITE)
	{
		if ((*movement & boardStruct->totalBlackPieces)!=0)
			return 1; //Kill a black piece and I am white
		else return 0;
	}
	else if(boardStruct->actualColor == BLACK)
	{
		if ((*movement & boardStruct->totalWhitePieces)!=0)
			return 1; //Kill a white piece and I am black
		else return 0;
	}

	return SUCCESS;
}

int checkResolveConflicts(BitMap * movement,int * generalCounter,Board *boardStruct){

	if(checkConflicts(movement, boardStruct))	//If it returns 1, implies that there is a colision
	{											// But we don't know if is with one of our pieces or adversary
		if(resolveConflicts(movement, boardStruct)) {	//If it returns 1, implies that it can kill (opponent piece)
			//TODO: Need to add data to the MoveData struct (pieceType killed, etc)
			boardStruct->boardState[*generalCounter].generatedMove = *movement;
			boardStruct->boardState[*generalCounter].killedTypePiece = getKilledPieceType(movement,boardStruct);
			(*generalCounter)++;
			return 0; //exit from the for, I DO NOT generate other moves in these direction
		} else {
			// A collision has been found, but is with your own pieces, so the 'sliding' should stop
			// but the Board structure have not to be refreshed as with the killed pieces.
			return 0;
		}
	}
	else
	{
		boardStruct->boardState[*generalCounter].generatedMove = *movement;
		boardStruct->boardState[*generalCounter].killedTypePiece = -1;
		(*generalCounter)++;
		return 1; // there's no conflict! I add the movement to the position
				  // continue to generate other movements
	}

}


int getKilledPieceType(BitMap *movement, Board *boardStruct){

	BitMap  * enemiesPieces = NULL;
	enemiesPieces = (boardStruct->actualColor==WHITE) ?
					boardStruct->blackPieces: boardStruct->whitePieces;

	if((*movement&enemiesPieces[King])!=0)
		return King;
	else if((*movement & enemiesPieces[Queens])!=0)
		return Queens;
	else if((*movement & enemiesPieces[Rooks])!=0)
		return Rooks;
	else if((*movement & enemiesPieces[Bishops])!=0)
		return Bishops;
	else if((*movement & enemiesPieces[Knights])!=0)
		return Knights;
	else if((*movement & enemiesPieces[Pawns])!=0)
		return Pawns;
	else
	return INCORRECT_PIECE_TYPE;

	return SUCCESS;
}

