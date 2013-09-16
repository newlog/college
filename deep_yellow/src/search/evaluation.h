/*
 * evaluation.h
 *
 *      Author: paolotaglinani
 */

#ifndef EVALUATION_H_
#define EVALUATION_H_

#include "../structs/board.h"

//Defining error code
#define ERROR_EVALUATION_POSITION 	-14
#define ERROR_GET_VALUE_POSITION 	-15
#define ERROR_EVALUATE_COLOR		-16
#define ERROR_NULL_POSITION			-17
#define ERROR_POSITION_VALUE		-18
#define	ERROR_COLOR					-19

//Defining value for every pieces
#define QUEEN_VALUE 	900
#define ROOK_VALUE  	500
#define BISHOP_VALUE 	325
#define KNIGHT_VALUE	300
#define PAWN_VALUE		100

//Defining constant to evaluate the check
#define KING_IN_CHECK		1
#define KING_NOT_IN_CHEK	0

//Definig value to assign to the heuristic if we're in check
#define OUR_CHECK_VALUE	 	-20000
#define OPP_CHECK_VALUE		10000

//Definig value for the phase of the game
#define OPENING			0
#define MIDDLE			1
#define END				2

//defining of value for the opening
#define OPEN_VALUE      		8
#define END_MATERIAL_VALUE		2400


//defining constant to involve pawns in the game
#define BONUS_PAWN_OPEN			1.5
#define BONUS_PAWN_MIDDLE		1.2


/*
 * Function used to evaluate a child node in the search treee. It uses 3 principal function to
 * evaluate the node:
 * - MATERIAL: based on how many pieces we have on the board, assigning a weight for every pieces
 * - POSITION: based on the position of a piece in the chess board
 * - MENACED:  based on the number of the piece menaced in a position
 *
 * @param board, the actual board state: the child to analyze
 * @param depth, the actual depth in the search tree
 */
int get_Heuristic (Board * board, int depth);

/*
 * This function count the number of pieces in a bitmap.
 * @param pieceBoard the board containing pieces
 * @return number of pieces in this board
 */
int pieceCount(BitMap * pieceBoard);

/*
 * Function that calculate the value of the material for every piece type.
 * @param board the actual board representation
 * @param pieceType the type of piece to analyze
 */
int getMaterial(Board * board,  int pieceType);

/*
 * Funtcion to evaluate the side of the movement, to use it as in the negamax algoritmh
 * @param depth the depth of the search tree
 * @return +1 if move my pieces, -1 instead
 */
int sideToMove( int depth);

/*
 * Function that return all the material on the board. Evaluating black and white
 * @param board, the actua board configuration
 * @return the material value
 */
int getTotalTableMaterial(Board *board);

/*
 * Function that calculate the total piece value on the board.
 * This function calculate the piece value for black and for white and calculate the difference, according to the color assigned to the player.
 *
 * @param board the current configuration of the table
 * @return the value of the position heuristic
 */
int getTotalPiecePositionValue(Board * board, int depth, int phase);

/*
 * Function that calculate the value for a type of piece. We have to pass to it the array that contain all the pieces of determinate type (you can find it in board.h).
 * It calculate the position of every pieces and calculate the value of every position and return the value.
 *
 * @param piecesPosition array of BitMap that contain all the pieces of a given type
 * @param pieceType the type of pieces we're analyzing
 * @param pieceColor the color of the piece we're analyzing
 * @return the value of the position for every pieces
 */
int valueForPiecesPosition(BitMap * piecesPosition, int pieceType, int pieceColor, int phase);


/*
 * Function that return the position of the piece relative to the white-oriented table,
 * in order to be used with the position table.
 * @param position, the position of the piece
 * @param pieceColor, the color of the piece that is analyzed
 * @return the relative position
 */
int getRelativePosition(int position, int pieceColor);

/*
 * Function that return the value of a position for a tpe of piece
 * @param position, the position which I want to find the value
 * @param pieceType, use to determinate what table I have to use to determinate the value
 * @return the value of a position in a determinate pieceTable
 */
int getValueForPosition(int position, int pieceType, int phase);

/*
 * Function that calculate the number of menaced pieces in a particular situation and give a weight to every menaced pieces.
 * @param actualBoard, the actual state of the board
 * @param pieceColor, the color of the pieces qich we've to calcualate material
 * @param pieceType, the type of piece
 * @return the value of the heuristic menaced for asingle type and a single color
 */
int menacedPiecesSingleType(Board * actualBoard, int pieceColor, int pieceType);

/*
 *Function that calculate the total menaced value of the actualBoard
 *@param myColor, the color of my pieces
 *@param actualBoard, the current board state
 *@return the value of the menaced heuristic
 */
int getMenacedValueTotal(int myColor, Board * actualBoard);

/*
 *Function that calculate the  menaced value of a single color
 *@param pieceColor, the color of wich calculate the menaced value
 *@param actualBoard, the current board state
 *@return the value of the menaced heuristic for one color
 */
int menacedPiecesOneColor(int pieceColor, Board * actualBoard);

/*
 * Function that evaluate if we're in check in a particular game situation, used to evaluate the heuristic
 * @param actualBoard, the current board configuration
 * @return an int code that indicate if we are or not in check
 */
int evaluateOurCheck(Board * actualBoard);

/*
 * This function return the current game phase
 * @param board, the current board state
 * @param movement, the number of current movement
 * @param mineMaterial, the value of mine material, to calculate the game phaes
 * @return a constant that indicate the current game phase
 */
int getGamePhase(Board * board, int movement, int mineMaterial);

/*
 * This function return the number of current movement, counting the depth of the tree
 * @param depth the current depth of the tree
 * @return the current movement value
 */
int getCurrentMovement(int depth);

/*
 * This function evaluate if the opponent is in check, by looking if exist the king
 * @param actualBoard the actual state of the board
 * @return a constant that indicate the check or not
 */
int evaluateOpponentCheck(Board * actualBoard);

/*Function that detects the checkmate in the final phase
 *@param actualBoard the actual state of the board
 *@return integer value 100 if possible checkmate or 0 if d'ont  possible checkmate  */
int jake_mate_heuristic(Board* actualBoard, int depth);


/*
 * Function that generates bitmaps to calculate checkmate the white pieces
 *@param actualBoard the actual state of the board
 *@param king_position_oponent bitmap whit position of the opponent's king
 *@param mov_position_king_oponent bitmap whit movement of the opponent's king
 *@param mov_my_pieces bitmap whit total movement of all pieces
 *@return SUCCESS or Not
 */
int white_jake_mate(Board* actualBoard, BitMap* king_position_oponent, BitMap* mov_position_king_oponent,BitMap*  mov_my_pieces);

/*
 * Function that generates bitmaps to calculate checkmate the black pieces
 *@param actualBoard the actual state of the board
 *@param king_position_oponent bitmap whit position of the opponent's king
 *@param mov_position_king_oponent bitmap whit movement of the opponent's king
 *@param mov_my_pieces bitmap whit total movement of all pieces
 *@return SUCCESS or Not
 */
int black_jake_mate(Board* actualBoard, BitMap* king_position_oponent, BitMap* mov_position_king_oponent,BitMap*  mov_my_pieces);

#endif /* EVALUATION_H_ */
