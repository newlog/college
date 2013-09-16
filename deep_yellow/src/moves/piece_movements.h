/*
 * piece_movements.h
 *
 *  Created on: 29/11/2011
 *      Author: newlog
 */


#ifndef PIECE_MOVEMENTS_H_
#define PIECE_MOVEMENTS_H_

#include <stdio.h>
#include "move_functions.h"
#include "../log/log.h"
#include "../search/evaluation.h"


/*
 * This function returns (as the 3rd referenced parameter) all the possible moves of the passed Rook.
 * @param board This variable identifies the position in the board of one rook.
 * @param col This variable identifies the column in where is the rook [0..7]
 * @param row This variable identifies the row in where is the rook [0..7]
 * @param generalCounter This variable identifies the last position "object" modified. Which (position) array position has been modified last. This variable
 * @returns An int value identifying the success or the error of the function.
 * 			SUCCESS --> Everything went as expected.
 * 			NO_PIECES_IN_BOARD --> The board was empty. The array of positions is 0.
 *			NULL_BOARD --> The board parameter is null.
 *			NULL_POSITION --> The position parameter is null.
 *			NULL_COUNTER --> The general counter is null.
 */
int moveRook(BitMap * board, int col, int row, int * generalCounter, Board * boardStruct);

/*
 * This function returns all the possible movements of the king
 * @param board This variable identifies the position in the board of one king.
 * @param col This variable identifies the column in where is the king [0..7]
 * @param row This variable identifies the row in where is the king [0..7]
 * @param generalCounter This variable identifies the last position "object" modified. Which (position) array position has been modified last.
 * @returns An int value identifying the success or the error of the function.
 * 			SUCCESS --> Everything went as expected.
 * 			NO_PIECES_IN_BOARD --> The board was empty. The array of positions is 0.
 *			NULL_BOARD --> The board parameter is null.
 *			NULL_POSITION --> The position parameter is null.
 *			NULL_COUNTER --> The general counter is null.
 */

int moveKing(BitMap * board, int col, int row, int * generalCounter, Board *boardStruct);

/*
 * This a specific movement for the piece of the horse.
 * @param board This variable identifies the position in the board of one piece.
 * @param col This variable identifies the column in where is the piece [0..7]
 * @param row This variable identifies the row in where is the piece [0..7]
 * @param generalCounter This variable identifies the last position "object" modified. Which (position) array position has been modified last.
 * @returns An int value identifying the success or the error of the function.
 * 			SUCCESS --> Everything went as expected.
 * 			NO_PIECES_IN_BOARD --> The board was empty. The array of positions is 0.
 *			NULL_BOARD --> The board parameter is null.
 *			NULL_POSITION --> The position parameter is null.
 *			NULL_COUNTER --> The general counter is null.
 */
int moveKnight(BitMap * board, int col, int row, int * generalCounter, Board *boardStruct);
int moveBishop(BitMap * board, int col, int row, int * generalCounter, Board *boardStruct);
int moveQueen(BitMap * board, int col, int row, int * generalCounter, Board *boardStruct);
int movePawn(BitMap * board, int col, int row, int * generalCounter, Board * boardStruct);



/*
 * This function is the one used to create all children of ONE piece and store all that movements in the childrenBoardArray.
 * The ONE piece IS an opponent's piece because when you are generating the children, is the turn your opponent.
 * Once the board are generated, the opponentColorAssigned is CHANGED another time.
 * This function is used in the search algorithm.
 * We use two function to not overload them with conditions.
 * @param i This variable is used to choose the piece to move. Its domain is:
 * 			King = 		0,
 *			Queen = 	1,
 *			Rooks = 	2,
 *			Bishops = 	3,
 *			Knights = 	4,
 *			Pawns = 	5
 * @param actualBoard This variable is the node used to generate all the moves. The actual state of the board.
 * @param generalCounter This variable is used for internal use of the move pieces functions. It must be modified an past among all the move pieces functions.
 * @param moreGeneralCounter This variable is used as an index of all the MAX_CHILDREN positions of childrenBoardArray.
 * @param childrenBoardArray This variable is the one used to store all the board children of one node.
 */

int move_white_piece(int piece_type, Board * actualBoard, int * generalCounter, int * moreGeneralCounter, Board * childrenBoardArray);
int move_black_piece(int piece_type, Board * actualBoard, int * generalCounter, int * moreGeneralCounter, Board * childrenBoardArray);
int buildChildrenTypeArray ( int piece_type, Board * actualBoard, int * generalCounter, int * moreGeneralCounter, Board * childrenBoardArray );


int refreshChildBoard( int * generalCounter, int *moreGeneralCounter, Board* childrenBoardArray,Board *actualBoard, BitMap *aux_board, int piece_type);


struct Move
{
/*	void movePawn();
	void moveQueen();
	void moveKing();
	void moveBishop();
	void moveKnight();
*/
};

#endif
