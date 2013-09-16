/*
 * move_functions.h
 *
 *  Created on: 25/11/2011
 *      Author: newlog
 */


#ifndef MOVE_FUNCTIONS_H_
#define MOVE_FUNCTIONS_H_

#include "../structs/board.h"
#include "../log/log.h"
#include "../structs/board.h"

/*
 * This function returns (as the 3rd referenced parameter) all the possible vertical/horizontal moves.
 * @param board This variable identifies the position in the board of one rook.
 * @param col This variable identifies the column in where is the rook [0..7]
 * @param row This variable identifies the row in where is the rook [0..7]
 * @param generalCounter This variable identifies the last position "object" modified. Which (position) array position has been modified last.
 * @returns An int value identifying the success or the error of the function.
 * 			SUCCESS --> Everything went as expected.
 * 			NO_PIECES_IN_BOARD --> The board was empty. The array of positions is 0.
 *			NULL_BOARD --> The board parameter is null.
 *			NULL_POSITION --> The position parameter is null.
 *			NULL_COUNTER --> The general counter is null.
 */
int slidingUpDownRightLeft(	BitMap * board,
		int col,
		int row,
		int * generalCounter,
		Board *boardStruct);


/*
 * This function returns (as the 3rd referenced parameter) all the possible diagonal moves.
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
int moveDiagonalLeftRight(	BitMap * board,
		int col,
		int row,
		int * generalCounter,
		Board *boardStruct);


/*
 * This a movement vertical up of one cell.
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
int moveUp(BitMap * board,
		int col,
		int row,
		int * generalCounter,
		Board *boardStruct);
/*
 * The same as moveUp but moving two positions instead.
 */
int move2Up(BitMap * board,
		int col,
		int row,
		int * generalCounter,
		Board *boardStruct);

/*
 * This a movement vertical down of one cell.
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
int moveDown(BitMap * board,
		int col,
		int row,
		int * generalCounter,
		Board *boardStruct);
/*
 * The same as moveDown but moving two positions instead.
 */
int move2Down(BitMap * board,
		int col,
		int row,
		int * generalCounter,
		Board *boardStruct);

/*
 * This a movement horizontal left of one cell.
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
int moveLeft(BitMap * board,
		int col,
		int row,
		int * generalCounter,
		Board *boardStruct);

/*
 * This a movement horizontal right of one cell.
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
int moveRight(BitMap * board,
		int col,
		int row,
		int * generalCounter,
		Board *boardStruct);

/*
 * This a movement diagonal up right of one cell.
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
int moveUpRight(BitMap * board,
		int col,
		int row,
		int * generalCounter,
		Board *boardStruct);

/*
 * This a movement diagonal up left of one cell.
 * @param board This variable identifies the position in the board of one piece.
 * @param col This variable identifies the column in where is the piece [0..7]
 * @param row This variable identifies the row in where is the piece [0..7]
 * @param position This variable is an MAX_NUM_MOVES array with every possible move as a complete bitmap.
 * @param generalCounter This variable identifies the last position "object" modified. Which (position) array position has been modified last.
 * @returns An int value identifying the success or the error of the function.
 * 			SUCCESS --> Everything went as expected.
 * 			NO_PIECES_IN_BOARD --> The board was empty. The array of positions is 0.
 *			NULL_BOARD --> The board parameter is null.
 *			NULL_POSITION --> The position parameter is null.
 *			NULL_COUNTER --> The general counter is null.
 */
int moveUpLeft(BitMap * board,
		int col,
		int row,
		int * generalCounter,
		Board *boardStruct);

/*
 * This a movement diagonal down left of one cell.
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
int moveDownLeft(BitMap * board,
		int col,
		int row,
		int * generalCounter,
		Board *boardStruct);

/*
 * This a movement diagonal down right of one cell.
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
int moveDownRight(BitMap * board,
		int col,
		int row,
		int * generalCounter,
		Board *boardStruct);
/*
 * This a specific movement for the piece of the knight.
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
int jumpHorse(BitMap * board, int col, int row, int * generalCounter, Board *boardStruct);

/*
 * This a specific movement for the piece of the pawn.
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
int stepPawn(BitMap * board, int col, int row, int * generalCounter, Board * boardStruct);



//TODO: Falta comentar.
int moveDownKing(BitMap * board, int col, int row, int * generalCounter, Board *boardStruct);
//TODO: Falta comentar.
int moveUpKing(BitMap * board, int col, int row, int * generalCounter, Board * boardStruct);

/*
 * This checks if my generated movement is in conflict with one piece on the board
 * @param movement The generated movement
 * @param boardStruct This variable contains all the information on the board
 * @return a boolean value that indicates if I might generate the next movement (true) or not (0)
 */
int checkConflicts(BitMap * movement, Board * boardStruct);

/*
 * This checks my color and say if a movement has to be throw away or saved
 * @param movement the generated movement
 * @param boardStruct This variable contains all the information of the board
 * @return a boolean value that indicates if it's needed to save or not the movement
 */

int resolveConflicts(BitMap * movement, Board * boardStruct);

/*
 * This function checks if there's a conflict and , in case a conflict is found,
 * resolve it by adding the movement and the piece killed if the conflict is about an enemies.
 * It not do anything if the conflic is about our pieces.
 * @param movement the generated movement
 * @param boardStruct This variable contains all the information of the board
 * @return TRUE if a conflict is found, FALSE if there's no conflict.
 */
int checkResolveConflicts(BitMap * movement,int * generalCounter,Board *boardStruct);

/*This function checks what type of piece you've killed and return it
 * @param movement, the generated movement
 * @param boardStruct, a variable with all the information of the board
 * @return The type of killed piece
 */
int getKilledPieceType(BitMap *movement, Board *boardStruct);


#endif
