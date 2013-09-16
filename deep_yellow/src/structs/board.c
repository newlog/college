/*
 * board.c
 *
 *  Created on: 16/nov/2011
 *      Author: paolotagliani
 */
#include <stdio.h>
#include "board.h"
#include "../log/log.h"
#include "../search/evaluation.h"


Board initBoard()
{
	Board board;

	/*
	 * Initialization of all the bitboard to 0
	 */
	int i;
	for(i = 0; i < N_TYPES; i++)
	{
		board.blackPieces[i] = 0;
		board.whitePieces[i] = 0;
	}

	/*
	 * Initialization of every piece's bitboard and white
	 * and black summary bitboard
	 */
	board.whitePieces[Pawns] =		0x00FF000000000000;
	board.whitePieces[Rooks] =		0x8100000000000000;
	board.whitePieces[Knights] =	0x4200000000000000;
	board.whitePieces[Bishops] =	0x2400000000000000;
	board.whitePieces[Queens] =		0x1000000000000000;
	board.whitePieces[King] =		0x0800000000000000;

	board.totalWhitePieces =	board.whitePieces[Pawns]	|
								board.whitePieces[Rooks]	|
								board.whitePieces[Bishops]	|
								board.whitePieces[Knights]	|
								board.whitePieces[Queens]	|
								board.whitePieces[King];

	board.blackPieces[Pawns] =		0xFF00;
	board.blackPieces[Rooks] =		0x81;
	board.blackPieces[Knights] =	0x42;
	board.blackPieces[Bishops] =	0x24;
	board.blackPieces[Queens] =		0x10;
	board.blackPieces[King] =		0x8;

	board.totalBlackPieces =	board.blackPieces[Pawns]	|
								board.blackPieces[Rooks]	|
		                        board.blackPieces[Bishops]	|
		                        board.blackPieces[Knights]	|
		                        board.blackPieces[Queens]	|
		                        board.blackPieces[King];

	board.occupiedSquares = board.totalBlackPieces | board.totalWhitePieces;

	board.actualColor = getPlayerColor();
	board.myColor=getPlayerColor();

	for(i = 0; i < MAX_NUM_MOVES; i++){
		board.boardState[i].generatedMove = 0;
		board.boardState[i].killedTypePiece = -1;
	}

	board.heuristic = MIN_INT_VALUE;
	board.killedTypePiece = -1;

	MOVEMENTS=0;

	return board;
}

int getPiecePosition(BitMap * board, int pieceType, int * position) {

	if ( position == NULL ) {
		logChess(FATAL, "The position array is null.");
		return NULL_POSITION;
	}
	// A -9 is assigned because when the row and col are calculated we can obtain a -1 when the are no pieces in the board.
	// row <-- position[x] / 8, col <-- position[x] % 8.
	int i = 0;
	for (i = 0; i < 8; i++)
		position[i] = -9;

	if ( board == NULL ) {
		logChess(FATAL, "The board is null.");
		return NULL_BOARD;
	}
	if ( *board == 0 ) {
		return NO_PIECES_IN_BOARD;
	}

	int count = 0, pieceCount_aux = 0;



	pieceCount_aux = pieceCount(board);

	/*switch ( pieceType ) {
		case 0:
			pieceCount = 1;
			break;
		case 1:
			pieceCount = 1;
			break;
		case 2:
			pieceCount = 2;
			break;
		case 3:
			pieceCount = 2;
			break;
		case 4:
			pieceCount = 2;
			break;
		case 5:
			pieceCount = 8;
			break;
		default:
			logChess(FATAL, "Invalid piece type.");
			return BAD_PIECE_TYPE;
	}*/


	BitMap aux_board1 = (*board);
	BitMap aux_board2 = (*board);

	for (i = 0; i < 64; i++) {
		aux_board2 = aux_board1;
		aux_board2 &= 0x0000000000000001;

		if ( aux_board2 == 0x0000000000000001 ) {
			if ( count > -1 && count < 8) {
				position[count] = 63 - i;
				count++;
				pieceCount_aux--;
			} else {
				logChess(WARN, "Index out of bounds.");
			}
		if ( pieceCount_aux == 0 )
			break;
		}
		aux_board1 >>= 1;
	}

	return SUCCESS;
}


Board initZero(){

	Board board;

	/*
	 * Initialization of all the bitboard to 0
	 */
	int i;
	for(i = 0; i < N_TYPES; i++)
	{
		board.blackPieces[i] = 0;
		board.whitePieces[i] = 0;
	}


	board.totalWhitePieces =	board.whitePieces[Pawns]	|
								board.whitePieces[Rooks]	|
								board.whitePieces[Bishops]	|
								board.whitePieces[Knights]	|
								board.whitePieces[Queens]	|
								board.whitePieces[King];


	board.totalBlackPieces =	board.blackPieces[Pawns]	|
								board.blackPieces[Rooks]	|
		                        board.blackPieces[Bishops]	|
		                        board.blackPieces[Knights]	|
		                        board.blackPieces[Queens]	|
		                        board.blackPieces[King];

	board.occupiedSquares = board.totalBlackPieces | board.totalWhitePieces;

	board.actualColor = -1;
	board.myColor=-1;

	for(i = 0; i < MAX_NUM_MOVES; i++){
		board.boardState[i].generatedMove = 0;
		board.boardState[i].killedTypePiece = -1;
	}

	board.heuristic = MIN_INT_VALUE;
	board.killedTypePiece = -1;

	return board;
}

int getPlayerColor() {
	//return WHITE;
	return BLACK;
}

