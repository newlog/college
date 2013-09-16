#ifndef NEGASCOUT_H_
#define NEGASCOUT_H_

#include "../log/log.h"
#include "../structs/board.h"
#include "../moves/piece_movements.h"
#include "evaluation.h"

#include "../player/jugada.h"

//#define MAX_DEPTH			5		// Maximum depth of the search algorithm
#define MAX_CHILDREN		137		// Maximum number of children (each move for every piece)

#ifndef max
	#define max( a, b ) ( ((a) > (b)) ? (a) : (b) )
#endif

int negascout(Board * board, int alpha, int beta, int actualDepth, BitMap * nextMove);
int alpha_beta_negamax(Board * board, int alpha, int beta, int actualDepth, Board * nextMove);


/*
 * This function generates all the child for the root actualBoard.
 * @param actualBoard Board variable that identifies the root board state.
 * @param childBoardArray Board array of MAX_CHILDREN positions. This variable will be modified with all the children.
 * @param moreGeneralCounter This variable identifies the number of children generated. You have to pass a 0, and, eventually, you achieve the number of children.
 * @return Returns a code definig the success of the function:
 * 				NULL_BOARD if the actualBoard or childrenBoardArray are null.
 * 				NULL_INT if the moreGeneralCounter is null.
 * 				SUCCESS if everything goes ok.
 */
int createChildren ( Board * actualBoard, Board * childrenBoardArray, int * moreGeneralCounter);


/*
 * This function copies the actual board structure into an array of board structures.
 * @param childBoardArray Board array of MAX_CHILDREN positions. This variable will contain all the copies of actualBoard.
 * @param actualBoard Board variable that identifies the root board state.
 * @return Returns a code definig the success of the function:
 * 				NULL_BOARD if the actualBoard or childrenBoardArray are null.
 * 				SUCCESS if everything goes ok.
 */
int copyBoardStruct(Board * children, Board * actualBoard);


int evaluate( Board * board, int actualDepth );

#endif
