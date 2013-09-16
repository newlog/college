

#include <stdio.h>
#include "../log/log.h"
#include "../moves/piece_movements.h"

int test_move_rooks(Board board) {

	int ppSuccess = -1, i = 0, generalCounter = 0, col = -1, row = -1;
	int rookPositions[8], retValue;
	BitMap oneRook = 0x0000000000000100;
	ppSuccess = getPiecePosition(&oneRook, Rooks, rookPositions);
	if ( ppSuccess != SUCCESS ) {
		logChess(WARN, "The piece position could not be retrieved.");
	}
	if ( rookPositions != NULL ) {
		col = rookPositions[0] % 8;
		row = (rookPositions[0] / 8);
	} else {
		logChess(WARN, "The position array is null.");
	}

	retValue = moveRook(&oneRook, col, row, &generalCounter,&board);
	BitMap rookMove = 0;
	for (i = 0; i < MAX_NUM_MOVES; i++) {
		rookMove |= board.boardState[i].generatedMove;
		printf("Killed piece type = %d BY ROOK\n", board.boardState[i].killedTypePiece);
	}
	logBoard(INFO, rookMove);

	return retValue;
}

int test_move_king(Board board) {

	int ppSuccess = -1, i = 0, generalCounter = 0, col = -1, row = -1;
	int kingPositions[8], retValue;
	//BitMap oneKing = 0x0000000000000001;
	//BitMap oneKing = 0x0000000400000000;
	BitMap oneKing = 0x1000000000000000;
	//BitMap oneKing = 0x0000000000002000;
	//ppSuccess = getPiecePosition(&(board.blackPieces[Rooks]), Rooks, rookPositions);
	ppSuccess = getPiecePosition(&oneKing, King, kingPositions);
	if ( ppSuccess != SUCCESS ) {
		logChess(WARN, "The piece position could not be retrieved.");
	}
	if ( kingPositions != NULL ) {
		col = kingPositions[0] % 8;
		row = (kingPositions[0] / 8);
	} else {
		logChess(WARN, "The position array is null.");
	}
	retValue = moveKing(&oneKing, col, row, &generalCounter,&board);
	BitMap kingMove = 0;
	for (i = 0; i < MAX_NUM_MOVES; i++) {
		kingMove |= board.boardState[i].generatedMove;
		printf("Killed piece type = %d BY KING\n", board.boardState[i].killedTypePiece);
	}

	logBoard(INFO, kingMove);

	return retValue;
}

int test_move_knight(Board board) {

	int ppSuccess = -1, i = 0, generalCounter = 0, col = -1, row = -1;
	int knightPositions[8], retValue;
	/*
	 * Different positions for the knight
	 */
	//BitMap oneknight = 0x0000000400000000;
	//BitMap oneknight = 0x2000000000000000;
	//BitMap oneknight = 0x0000000000002000;
	BitMap oneknight = 0x0000400000000000;

	ppSuccess = getPiecePosition(&oneknight, Knights, knightPositions);
	if ( ppSuccess != SUCCESS ) {
		logChess(WARN, "The piece position could not be retrieved.");
	}
	if ( knightPositions != NULL ) {
		col = knightPositions[0] % 8;
		row = (knightPositions[0] / 8);
	} else {
		logChess(WARN, "The position array is null.");
	}

	retValue = moveKnight(&oneknight, col, row, &generalCounter,&board);
	BitMap knightMove = 0;
	for (i = 0; i < MAX_NUM_MOVES; i++) {
		knightMove |= board.boardState[i].generatedMove;
		printf("Killed piece type = %d BY KNIGHT\n", board.boardState[i].killedTypePiece);
	}

	logBoard(INFO, knightMove);

	return retValue;
}

int test_move_bishop(Board board){

	int ppSuccess = -1, i = 0, generalCounter = 0, col = -1, row = -1;
	int bishopPositions[8], retValue;
	/*
	 * Different positions for the knight
	 */
	//BitMap onebishop = 0x0000000400000000;
	//BitMap onebishop = 0x2000000000000000;
	//BitMap onebishop = 0x0000000000002000;
	BitMap onebishop = 0x2000000000000000;

	ppSuccess = getPiecePosition(&onebishop, Bishops, bishopPositions);
	if ( ppSuccess != SUCCESS ) {
		logChess(WARN, "The piece position could not be retrieved.");
	}
	if ( bishopPositions != NULL ) {
		col = bishopPositions[0] % 8;
		row = (bishopPositions[0] / 8);
	} else {
		logChess(WARN, "The position array is null.");
	}

	retValue = moveBishop(&onebishop, col, row, &generalCounter,&board);
	BitMap bishopMove = 0;
	for (i = 0; i < MAX_NUM_MOVES; i++) {
		bishopMove |= board.boardState[i].generatedMove;
		printf("Killed piece type = %d BY BISHOP\n", board.boardState[i].killedTypePiece);
	}

	logBoard(INFO, bishopMove);

	return retValue;

}

int test_move_queen(Board board){

	int ppSuccess = -1, i = 0, generalCounter = 0, col = -1, row = -1;
	int queenPositions[8], retValue;
	/*
	 * Different positions for the knight
	 */
	BitMap onequeen = 0x0000000000000400;
	//BitMap onequeen = 0x2000000000000000;
	//BitMap onequeen = 0x0000000000002000;
	//BitMap onequeen = 0x0100000000000000;

	ppSuccess = getPiecePosition(&onequeen, Queens, queenPositions);
	if ( ppSuccess != SUCCESS ) {
		logChess(WARN, "The piece position could not be retrieved.");
	}
	if ( queenPositions != NULL ) {
		col = queenPositions[0] % 8;
		row = (queenPositions[0] / 8);
	} else {
		logChess(WARN, "The position array is null.");
	}

	retValue = moveQueen(&onequeen, col, row, &generalCounter,&board);
	BitMap queenMove = 0;
	for (i = 0; i < MAX_NUM_MOVES; i++) {
		queenMove |= board.boardState[i].generatedMove;
		printf("Killed piece type = %d BY QUEEN\n", board.boardState[i].killedTypePiece);
	}

	logBoard(INFO, queenMove);

	return retValue;

}

int test_move_pawn(Board board){

	int ppSuccess = -1, i = 0, generalCounter = 0, col = -1, row = -1;
	int positionPawn[8], retValue;
	/*
	 * Different positions for the knight
	 */
	//BitMap onepawn = 0x0000000400000000;
	//BitMap onepawn = 0x0010000000000000;
	BitMap onepawn = 0x0080000000000000;
	//BitMap onepawn = 0x2000000000000000;
	//BitMap onepawn = 0x0000000000002000;
	//BitMap onepawn = 0x0100000000000000;

	ppSuccess = getPiecePosition(&onepawn, Pawns, positionPawn);
	if ( ppSuccess != SUCCESS ) {
		logChess(WARN, "The piece position could not be retrieved.");
	}
	if ( positionPawn != NULL ) {
		col = positionPawn[0] % 8;
		row = (positionPawn[0] / 8);
	} else {
		logChess(WARN, "The position array is null.");
	}

	retValue = movePawn(&onepawn, col, row, &generalCounter, &board);
	BitMap pawnMove = 0;
	for (i = 0; i < MAX_NUM_MOVES; i++) {
		pawnMove |= board.boardState[i].generatedMove;
		printf("Killed piece type = %d BY PAWN\n", board.boardState[i].killedTypePiece);
	}

	logBoard(INFO, pawnMove);

	return retValue;

}
