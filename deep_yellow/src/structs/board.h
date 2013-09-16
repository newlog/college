/*
 * board.h
 *
 *  Created on: 16/11/2011
 *      Author: newlog
 */


#ifndef BOARD_H_
#define BOARD_H_



/*
 * This variable is needed to evaluate the phase of the game. It's a global variable used
 * in the evaluation
 */
int MOVEMENTS;

/*
 * This is a representation of the chess board. We can access the field of this struct
 * using the variable bitmap.
 * EX: whiteKing.bitmap;
 */
typedef unsigned long long int BitMap;

#define N_TYPES					6		// Total number of piece types (king, queens, rooks...)
#define MAX_NUM_MOVES			27		// Maximum number of possible moves (27 possible max moves as queen)
#define MAX_INT_VALUE		65000
#define MIN_INT_VALUE		-65000

//Define error messages
#define SUCCESS 				0
#define NO_PIECES_IN_BOARD		-1
#define BAD_PIECE_TYPE			-2
#define NULL_BOARD				-3
#define NULL_POSITION			-4
#define NULL_COUNTER			-5
#define BAD_MOVE_GENERATION		-6
#define NULL_BOARD_STRUCT		-7
#define NULL_MOVEMENT			-8
#define INCORRECT_PIECE_TYPE	-9
#define NULL_INT				-10
#define BAD_DEPTH				-11

// Color definitions for each player
#define BLACK					0
#define WHITE					1

// Constant for the pawn moves. (Necessary to know if we can jump 1 or 2 steps).
#define COLISION_FOUND 					1

/*
 * This struct is used when the movement generation is done.
 * Here we have all generated moves from one piece
 */
typedef struct _moveData {
	BitMap generatedMove;		//A move generated from a particular board state
	int killedTypePiece;		//Contain the type of pieced killed in the move, or -1 if not kill anyone
} MoveData;

typedef struct Board
{
	BitMap whitePieces[N_TYPES];										//Arrays with all the BitMap of every white piece type
	BitMap blackPieces[N_TYPES];										//Arrays with all the BitMap of every white piece type

	BitMap totalWhitePieces, totalBlackPieces, occupiedSquares;			//Structured with all the white, black pieced and the union
																		//of the two

	MoveData boardState[MAX_NUM_MOVES];									// This variable identifies the generated moves of a piece type

	enum piezas{														//Enumeration used to assign a numeric value to every piece type
		King = 		0,
		Queens = 	1,
		Rooks = 	2,
		Bishops = 	3,
		Knights = 	4,
		Pawns = 	5
	} tipo_pieza;



   int actualColor : 8;													//The color that is actually moving

   int myColor	:	8;													//The color assigned to the player


   int heuristic;														//Heuristic value for this table configuration
   int killedTypePiece;	//TODO:this is never called, can I throw it?


}Board;

/*
 * This method initialize the board to the initial chess board situation.
 * @return the board initialized as the initial chess board state
 */
Board initBoard();

//TODO:question: this is called and then changed, can we eliminate this?
int getPlayerColor();

/*
 * This function returns (as the 3rd referenced parameter) the position of each piece of pieceType.
 * @param board This variable identifies the positions of each piece of the type pieceType.
 * @param pieceType This variable identifies the type of the pieces. As in the enum tipo_piezas.
 * 					0 --> King
 * 					1 --> Queens
 * 					2 --> Rooks
 * 					3 --> Bishops
 * 					4 --> Knights
 * 					5 --> Pawns
 * @param position A pointer to an 8 positions array where the values different to -9 identifies the position of each piece in the board.
 * 			ex.: 	Rooks: [0, 7, -9, -9, -9, -9, -9, -9]
 * 					Pawns: [8, 9, 10, 11, 12, 13, 14, 15]
 * @returns An int value identifying the success or the error of the function.
 * 			SUCCESS --> Everything went as expected.
 * 			NO_PIECES_IN_BOARD --> The board was empty. The array of positions is -9 (the why is explained in code).
 * 			BAD_PIECE_TYPE --> The piece type is not correct. [King, Queens, Knight, Rooks, Bishops, Pawns]
 *			NULL_BOARD --> The board parameter is null.
 *			NULL_POSITION --> The position parameter is null.
 */
int getPiecePosition(BitMap * board, int pieceType, int * position);

//TODO:Duplicate method!!! eliminate one!
int getPlayerColor();

/*
 * This method initialize a board with all the array representing the chess board initialized to 0.
 * @return a board initialized to zero
 */
Board initZero();

#endif
