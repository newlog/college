/*
 ============================================================================
 Name        : Chess.c
 Author      : AI Team
 Version     :
 Copyright   : CopyLeft
 Description : Hello World in C, Ansi-style
 ============================================================================
 */

#include <stdio.h>
#include <stdlib.h>		//exit()
#include "log/log.h"
#include "structs/board.h"
#include "moves/piece_movements.h"
#include "tests/move_tests.h"
#include "tests/search_tests.h"
#include <string.h>
#include <time.h>
/*
int test_move_rooks(Board board);
int test_move_king(Board board);
int test_move_knight(Board board);
int test_move_bishop(Board board);
int test_move_queen(Board board);
int test_move_pawn(Board board);

int test_create_children(Board * actualBoard, Board * children);

int main()
{

	int retValue = -1;
	time_t timeStamp1 = {0};
	time_t timeStamp2 = {0};
	double seconds = 0;
	BitMap nextMove = 0;

	//Board board = initBoard();
	Board board;
	create_complex_board_test(&board);
	//create_two_opponent_rooks_board_test(&board);
	//create_two_opponent_pawns_board_test(&board);


	if ( initLog(ALL, FILE_MODE) != 0) {
		fprintf(stderr, "[-] The log library could not be initialized.");
		exit(-1);
	}

	/
	 * 	MOVEMENT TESTS START
	 */

	//logChess(INFO, "Rook movements:");
	//retValue = test_move_rooks(board);
	//logChess(INFO, "King movements:");
	//retValue = test_move_king(board);
	//logChess(INFO, "Knight movements:");
	//retValue = test_move_knight(board);
	//logChess(INFO, "Bishop movements:");
	//retValue = test_move_bishop(board);
	//logChess(INFO, "Queen movements:");
	//retValue = test_move_queen(board);
	//logChess(INFO, "Pawn movements:");
	//retValue = test_move_pawn(board);

	/*
	 * MOVEMENT TESTS END
	 */



	/*
	 * 	SEARCH TESTS START
	 */

	// Create children
	/*Board children[MAX_CHILDREN];
	retValue = test_create_children( &board, children);
	logChess(INFO, "#########################################################");
	logChess(INFO, "#########################################################");
	logBoard(INFO, children[0].occupiedSquares);
	logChess(INFO, "#########################################################");
	logChess(INFO, "#########################################################");

	Board newChildren[MAX_CHILDREN];
	int i = 0;
	for (i = 0; i < MAX_CHILDREN; i++) {
		memcpy(&newChildren[i], &children[0], sizeof(Board));
	}
	retValue = test_create_children(&children[0], newChildren);*/



	// Negascout
	/*
	timeStamp1 = time(NULL);

	retValue = negascout(&board, MIN_INT_VALUE, MAX_INT_VALUE, 0, &nextMove);

	timeStamp2 = time(NULL);

	logChess(INFO, "Next move: ");
	logBoard(INFO, nextMove);
	seconds = difftime(timeStamp2, timeStamp1);

	printf("Execution time: %.2f\n", seconds);

	/
	 * SEARCH TESTS END
	 */
/*
	return 0;
}
*/
