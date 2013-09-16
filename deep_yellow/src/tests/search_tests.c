#include "search_tests.h"


int test_create_children(Board *actualBoard, Board *children){

//Board children[MAX_CHILDREN];
	int count = 0, count2 = 0, createdChildren = -1;
	for ( count = 0; count < MAX_CHILDREN; count++ ) {
		children[count].actualColor = actualBoard->actualColor;
		children[count].occupiedSquares = actualBoard->occupiedSquares;
		children[count].tipo_pieza = actualBoard->tipo_pieza;
		children[count].totalBlackPieces = actualBoard->totalBlackPieces;
		children[count].totalWhitePieces = actualBoard->totalWhitePieces;
		for (count2 = 0; count2 < N_TYPES; count2++ ) {
			children[count].whitePieces[count2] = actualBoard->whitePieces[count2];
			children[count].blackPieces[count2] = actualBoard->blackPieces[count2];
		}
		for (count2 = 0; count2 < MAX_NUM_MOVES; count2++ ) {
			children[count].boardState[count2].generatedMove = actualBoard->boardState[count2].generatedMove;
			children[count].boardState[count2].killedTypePiece = actualBoard->boardState[count2].killedTypePiece;
		}
		children[count].heuristic = actualBoard->heuristic;
	}
	int childrenNumber =0;
	createdChildren = createChildren(actualBoard, children, &childrenNumber);
	logChess(INFO, "CREATE CHILDREN RESULTS: \n");

	for(count = 0; count < childrenNumber; count++){
		logBoard(INFO, children[count].occupiedSquares);
	}


	return SUCCESS;

}

int create_complex_board_test(Board* boardTest){
		int i=0;


		/*
		 * Initialization of all the bitboard to 0
		 */
		for(i = 0; i < N_TYPES; i++)
		{
			boardTest->blackPieces[i] = 0;
			boardTest->whitePieces[i] = 0;
		}

		/*
		 * Initialization of every piece's bitboard
		 */

		boardTest->whitePieces[King] =		0x4000000000000000;
		boardTest->whitePieces[Queens] =	0x0004000000000000;
		boardTest->whitePieces[Rooks] =		0x0001004000000000;
		boardTest->whitePieces[Bishops] =	0x0000000010000000;
		boardTest->whitePieces[Knights] =	0x0400000000800000;
		boardTest->whitePieces[Pawns] =		0x0002000002200200;



		boardTest->totalWhitePieces =
				boardTest->whitePieces[Pawns]	|
				boardTest->whitePieces[Rooks]	|
				boardTest->whitePieces[Bishops]	|
				boardTest->whitePieces[Knights]	|
				boardTest->whitePieces[Queens]	|
				boardTest->whitePieces[King];

		boardTest->blackPieces[King] =		0x0000008000000000;
		boardTest->blackPieces[Queens] =	0x0000000000000008;
		boardTest->blackPieces[Rooks] =		0x0000000000002000;
		boardTest->blackPieces[Bishops] =	0x0000000001000000;
		boardTest->blackPieces[Knights] =	0x0000000400000400;
		boardTest->blackPieces[Pawns] =		0x0040020000044000;

		boardTest->totalBlackPieces =
				boardTest->blackPieces[Pawns]	|
				boardTest->blackPieces[Rooks]	|
				boardTest->blackPieces[Bishops]	|
				boardTest->blackPieces[Knights]	|
				boardTest->blackPieces[Queens]	|
				boardTest->blackPieces[King];

		boardTest->occupiedSquares = boardTest->totalBlackPieces | boardTest->totalWhitePieces;

		boardTest->actualColor = getPlayerColor();

		for(i = 0; i < MAX_NUM_MOVES; i++){
			boardTest->boardState[i].generatedMove = 0;
			boardTest->boardState[i].killedTypePiece = -1;
		}

		boardTest->heuristic = 0;
		boardTest->killedTypePiece = -1;


	return SUCCESS;
}

int create_two_opponent_rooks_board_test(Board* boardTest) {
	int i=0;



	/*
	 * Initialization of all the bitboard to 0
	 */
	for(i = 0; i < N_TYPES; i++)
	{
		boardTest->blackPieces[i] = 0;
		boardTest->whitePieces[i] = 0;
	}

	/*
	 * Initialization of every piece's bitboard
	 */

	boardTest->whitePieces[King] =		0x0000000000000000;
	boardTest->whitePieces[Queens] =	0x0000000000000000;
	boardTest->whitePieces[Rooks] =		0x0000004000000000;
	boardTest->whitePieces[Bishops] =	0x0000000000000000;
	boardTest->whitePieces[Knights] =	0x0000000000000000;
	boardTest->whitePieces[Pawns] =		0x0000000000000000;



	boardTest->totalWhitePieces =
			boardTest->whitePieces[Pawns]	|
			boardTest->whitePieces[Rooks]	|
			boardTest->whitePieces[Bishops]	|
			boardTest->whitePieces[Knights]	|
			boardTest->whitePieces[Queens]	|
			boardTest->whitePieces[King];

	boardTest->blackPieces[King] =		0x0000000000000000;
	boardTest->blackPieces[Queens] =	0x0000000000000000;
	boardTest->blackPieces[Rooks] =		0x0000000800000000;
	boardTest->blackPieces[Bishops] =	0x0000000000000000;
	boardTest->blackPieces[Knights] =	0x0000000000000000;
	boardTest->blackPieces[Pawns] =		0x0000000000000000;

	boardTest->totalBlackPieces =
			boardTest->blackPieces[Pawns]	|
			boardTest->blackPieces[Rooks]	|
			boardTest->blackPieces[Bishops]	|
			boardTest->blackPieces[Knights]	|
			boardTest->blackPieces[Queens]	|
			boardTest->blackPieces[King];

	boardTest->occupiedSquares = boardTest->totalBlackPieces | boardTest->totalWhitePieces;

	boardTest->actualColor = getPlayerColor();

	for(i = 0; i < MAX_NUM_MOVES; i++){
		boardTest->boardState[i].generatedMove = 0;
		boardTest->boardState[i].killedTypePiece = -1;
	}

	boardTest->heuristic = 0;
	boardTest->killedTypePiece = -1;



	return SUCCESS;
}


int create_two_opponent_pawns_board_test(Board* boardTest) {
	int i=0;

	/*
	 * Initialization of all the bitboard to 0
	 */
	for(i = 0; i < N_TYPES; i++)
	{
		boardTest->blackPieces[i] = 0;
		boardTest->whitePieces[i] = 0;
	}

	/*
	 * Initialization of every piece's bitboard
	 */

	boardTest->whitePieces[King] =		0x0000000000000000;
	boardTest->whitePieces[Queens] =	0x0000000000000000;
	boardTest->whitePieces[Rooks] =		0x0000000000000000;
	boardTest->whitePieces[Bishops] =	0x0000000000000000;
	boardTest->whitePieces[Knights] =	0x0000000000000000;
	boardTest->whitePieces[Pawns] =		0x0000000008000000;



	boardTest->totalWhitePieces =
			boardTest->whitePieces[Pawns]	|
			boardTest->whitePieces[Rooks]	|
			boardTest->whitePieces[Bishops]	|
			boardTest->whitePieces[Knights]	|
			boardTest->whitePieces[Queens]	|
			boardTest->whitePieces[King];

	boardTest->blackPieces[King] =		0x0000000000000000;
	boardTest->blackPieces[Queens] =	0x0000000000000000;
	boardTest->blackPieces[Rooks] =		0x0000000000000000;
	boardTest->blackPieces[Bishops] =	0x0000000000000000;
	boardTest->blackPieces[Knights] =	0x0000000000000000;
	boardTest->blackPieces[Pawns] =		0x0000000000001000;

	boardTest->totalBlackPieces =
			boardTest->blackPieces[Pawns]	|
			boardTest->blackPieces[Rooks]	|
			boardTest->blackPieces[Bishops]	|
			boardTest->blackPieces[Knights]	|
			boardTest->blackPieces[Queens]	|
			boardTest->blackPieces[King];

	boardTest->occupiedSquares = boardTest->totalBlackPieces | boardTest->totalWhitePieces;

	boardTest->actualColor = getPlayerColor();

	for(i = 0; i < MAX_NUM_MOVES; i++){
		boardTest->boardState[i].generatedMove = 0;
		boardTest->boardState[i].killedTypePiece = -1;
	}
	boardTest->heuristic = 0;
	boardTest->killedTypePiece = -1;



	return SUCCESS;
}
