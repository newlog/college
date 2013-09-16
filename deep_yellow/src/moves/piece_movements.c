

#include "piece_movements.h"
#include "move_functions.h"



int moveRook(BitMap * board, int col, int row, int * generalCounter, Board *boardStruct)
{

	if ( board == NULL ) {
		logChess(FATAL, "The board is null.");
		return NULL_BOARD;
	}
	if ( boardStruct == NULL ) {
		logChess(FATAL, "The board struct is null.");
		return NULL_BOARD_STRUCT;
	}
	if ( boardStruct->boardState == NULL ) {
		logChess(FATAL, "The boardStruct.boardState array is null.");
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

	/*
	 * Init Position
	 */
	int i;

	(*generalCounter) = 0;
	for (i = 0; i < MAX_NUM_MOVES; i++) {
		boardStruct->boardState[i].generatedMove = 0;
		boardStruct->boardState[i].killedTypePiece = -1;
	}
	return slidingUpDownRightLeft(board, col, row, generalCounter,boardStruct);
}


int moveKing(BitMap * board, int col, int row,  int * generalCounter, Board *boardStruct)
{

	if ( board == NULL ) {
		logChess(FATAL, "The board is null.");
		return NULL_BOARD;
	}
	if ( boardStruct == NULL ) {
		logChess(FATAL, "The board struct is null.");
		return NULL_BOARD_STRUCT;
	}
	if ( boardStruct->boardState == NULL ) {
		logChess(FATAL, "The boardStruct.boardState array is null.");
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



	int i;
	(*generalCounter) = 0;
	for (i = 0; i < MAX_NUM_MOVES; i++) {
		boardStruct->boardState[i].generatedMove = 0;
		boardStruct->boardState[i].killedTypePiece = -1;
	}

	/*
	 * Calculation of all the possible movement
	 */

	int down = moveDownKing(board, col, row, generalCounter,boardStruct);
	if (down != SUCCESS) return down;

	int down_left = moveDownLeft(board, col, row, generalCounter,boardStruct);
	if (down_left != SUCCESS) return down_left;

	int down_right = moveDownRight(board, col, row, generalCounter,boardStruct);
	if (down_right != SUCCESS) return down_right;

	int left = moveLeft(board, col, row, generalCounter,boardStruct);
	if(left != SUCCESS) return left;

	int right = moveRight(board, col, row, generalCounter,boardStruct);
	if(right != SUCCESS) return right;

	int up = moveUpKing(board, col, row, generalCounter,boardStruct);
	if(up != SUCCESS) return up;

	int up_left = moveUpLeft(board, col, row, generalCounter,boardStruct);
	if(up_left != SUCCESS) return up_left;

	int up_right = moveUpRight(board, col, row, generalCounter,boardStruct);
	if(up_right != SUCCESS) return up_right;

	return SUCCESS;
}


int moveKnight(BitMap * board,
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
		logChess(FATAL, "The boardStruct.boardState array is null.");
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

	int i;
	(*generalCounter) = 0;
	for (i = 0; i < MAX_NUM_MOVES; i++) {
		boardStruct->boardState[i].generatedMove = 0;
		boardStruct->boardState[i].killedTypePiece = -1;
	}

	return jumpHorse(board, col, row, generalCounter,boardStruct);

}

int moveBishop(BitMap * board, int col, int row,  int * generalCounter, Board *boardStruct)
{
	if ( board == NULL ) {
		logChess(FATAL, "The board is null.");
		return NULL_BOARD;
	}
	if ( boardStruct == NULL ) {
		logChess(FATAL, "The board struct is null.");
		return NULL_BOARD_STRUCT;
	}
	if ( boardStruct->boardState == NULL ) {
		logChess(FATAL, "The boardStruct.boardState array is null.");
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

	int i;
	(*generalCounter) = 0;
	for (i = 0; i < MAX_NUM_MOVES; i++) {
		boardStruct->boardState[i].generatedMove = 0;
		boardStruct->boardState[i].killedTypePiece = -1;
	}
	return moveDiagonalLeftRight(board,col,row,generalCounter,boardStruct);

}

int moveQueen(BitMap * board, int col, int row,  int * generalCounter, Board *boardStruct)
{
	if ( board == NULL ) {
		logChess(FATAL, "The board is null.");
		return NULL_BOARD;
	}
	if ( boardStruct == NULL ) {
		logChess(FATAL, "The board struct is null.");
		return NULL_BOARD_STRUCT;
	}
	if ( boardStruct->boardState == NULL ) {
		logChess(FATAL, "The boardStruct.boardState array is null.");
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

	int i;
	(*generalCounter) = 0;
	for (i = 0; i < MAX_NUM_MOVES; i++) {
		boardStruct->boardState[i].generatedMove = 0;
		boardStruct->boardState[i].killedTypePiece = -1;
	}

	int retval= moveDiagonalLeftRight(board,col,row,generalCounter,boardStruct);
	int retval2= slidingUpDownRightLeft(board,col,row, generalCounter,boardStruct);

	if((retval && retval2)!=SUCCESS)
		return BAD_MOVE_GENERATION;

	return SUCCESS;
}



int movePawn(BitMap * board, int col, int row,  int * generalCounter, Board * boardStruct)

{
	if ( board == NULL ) {
		logChess(FATAL, "The board is null.");
		return NULL_BOARD;
	}
	if ( boardStruct == NULL ) {
		logChess(FATAL, "The board struct is null.");
		return NULL_BOARD_STRUCT;
	}
	if ( boardStruct->boardState == NULL ) {
		logChess(FATAL, "The boardStruct.boardState array is null.");
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

	int i;
	(*generalCounter) = 0;
	for (i = 0; i < MAX_NUM_MOVES; i++) {
		boardStruct->boardState[i].generatedMove = 0;
		boardStruct->boardState[i].killedTypePiece = -1;
	}

	return stepPawn(board, col, row, generalCounter, boardStruct);
}


int move_white_piece(int piece_type, Board * actualBoard, int * generalCounter, int * moreGeneralCounter, Board * childrenBoardArray) {

	int retValue = -1;
	/*
	 * Let's create a board variable only with one movement. First move functions parameter.
	 */
	switch (piece_type) {
		case King:
			retValue = buildChildrenTypeArray ( piece_type,actualBoard, generalCounter, moreGeneralCounter, childrenBoardArray);
			break;
		case Queens:
			retValue = buildChildrenTypeArray ( piece_type,actualBoard, generalCounter, moreGeneralCounter, childrenBoardArray);
			break;
		case Rooks:
			retValue = buildChildrenTypeArray ( piece_type,actualBoard, generalCounter, moreGeneralCounter, childrenBoardArray);
			break;
		case Bishops:
			retValue = buildChildrenTypeArray ( piece_type,actualBoard, generalCounter, moreGeneralCounter, childrenBoardArray);
			break;
		case Knights:
			retValue = buildChildrenTypeArray ( piece_type,actualBoard, generalCounter, moreGeneralCounter, childrenBoardArray);
			break;
		case Pawns:
			retValue = buildChildrenTypeArray ( piece_type,actualBoard, generalCounter, moreGeneralCounter, childrenBoardArray);
			break;
		default:
			logChess(WARN, "Incorrect piece type.");
	}


	return SUCCESS;
}


int move_black_piece(int piece_type, Board * actualBoard, int * generalCounter, int * moreGeneralCounter, Board * childrenBoardArray) {
	/*
	 * Let's create a board variable only with one movement. First move functions parameter.
	 */
	int retValue = -1;
	switch (piece_type) {
	case King:
		retValue = buildChildrenTypeArray ( piece_type,actualBoard, generalCounter, moreGeneralCounter, childrenBoardArray);
		break;
	case Queens:
		retValue = buildChildrenTypeArray ( piece_type,actualBoard, generalCounter, moreGeneralCounter, childrenBoardArray);
		break;
	case Rooks:
		retValue = buildChildrenTypeArray ( piece_type,actualBoard, generalCounter, moreGeneralCounter, childrenBoardArray);
		break;
	case Bishops:
		retValue = buildChildrenTypeArray ( piece_type,actualBoard, generalCounter, moreGeneralCounter, childrenBoardArray);
		break;
	case Knights:
		retValue = buildChildrenTypeArray ( piece_type,actualBoard, generalCounter, moreGeneralCounter, childrenBoardArray);
		break;
	case Pawns:
		retValue = buildChildrenTypeArray ( piece_type,actualBoard, generalCounter, moreGeneralCounter, childrenBoardArray);
		break;
		default:
			logChess(WARN, "Incorrect piece type.");
	}

	return SUCCESS;
}




int buildChildrenTypeArray ( int piece_type, Board * actualBoard, int * generalCounter, int * moreGeneralCounter, Board * childrenBoardArray )
{
	int i = 0, ppSuccess = -1;
	int position[8];
	int col = -1, row = -1;
	int retValue = -1;

	// We get the position of the white or black pieces in order to generate the movement
	if ( actualBoard->actualColor == WHITE ) {
		ppSuccess = getPiecePosition(&(actualBoard->whitePieces[piece_type]), piece_type, position);
	} else {
		ppSuccess = getPiecePosition(&(actualBoard->blackPieces[piece_type]), piece_type, position);
	}

	if ( ppSuccess != SUCCESS ) {
		//logChess(WARN, "The piece position could not be retrieved.");
	}
	if ( position != NULL ) {
		for (i = 0; i < 8; i++) {	//Because we can (max) have 8 pieces from the same type (8 pawns)
			if ( position[i] != -9 ) {
				col = position[i] % 8;
				row = position[i] / 8;
				BitMap aux_board = 0x8000000000000000;
				aux_board >>= position[i];
				retValue = -1;
					switch (piece_type) {
					case King:
						retValue = moveKing(&aux_board, col, row, generalCounter, actualBoard);
						break;
					case Queens:
						retValue = moveQueen(&aux_board, col, row, generalCounter, actualBoard);
						break;
					case Rooks:
						retValue = moveRook(&aux_board, col, row, generalCounter, actualBoard);
						break;
					case Bishops:
						retValue = moveBishop(&aux_board, col, row, generalCounter, actualBoard);
						break;
					case Knights:
						retValue = moveKnight(&aux_board, col, row, generalCounter, actualBoard);
						break;
					case Pawns:
						retValue = movePawn(&aux_board, col, row, generalCounter, actualBoard);
						break;
						default:
							logChess(WARN, "Incorrect piece type.");
					}

				if ( retValue == SUCCESS ) {
					refreshChildBoard( generalCounter, moreGeneralCounter, childrenBoardArray, actualBoard, &aux_board, piece_type);
				}
			} else {
				break;
			}
		}
	} else {
		logChess(WARN, "The position array is null.");
	}

	return SUCCESS;
}

int refreshChildBoard( int * generalCounter, int *moreGeneralCounter, Board* childrenBoardArray,Board *actualBoard, BitMap *aux_board, int piece_type ) {
	int aux_count = 0, i = 0;
	int killed_type_piece = -1;
	for ( aux_count = 0; aux_count < (*generalCounter); aux_count++) {
		/*
		 * We are refreshing all the board data for each child
		 */

		// 1) We have to erase the moved piece from its square.
		if ( actualBoard->actualColor == WHITE ) {
			childrenBoardArray[(*moreGeneralCounter)].whitePieces[piece_type] ^= *aux_board;
		} else if ( actualBoard->actualColor == BLACK ) {
			childrenBoardArray[(*moreGeneralCounter)].blackPieces[piece_type] ^= *aux_board;
		}

		// 2) Refresh all general data (blacks, whites and totals).
		// 2.1) we refresh our pieces
		if ( actualBoard->actualColor == WHITE ) {
			childrenBoardArray[(*moreGeneralCounter)].whitePieces[piece_type] ^=
					actualBoard->boardState[aux_count].generatedMove;
		} else if ( actualBoard->actualColor == BLACK ) {
			childrenBoardArray[(*moreGeneralCounter)].blackPieces[piece_type] ^=
								actualBoard->boardState[aux_count].generatedMove;
		}

		// 2.2) We refresh opponent. We only refresh opponent if we have killed something.
		killed_type_piece = actualBoard->boardState[aux_count].killedTypePiece;
		if ( killed_type_piece != -1 && killed_type_piece != -9) {
			if ( actualBoard->actualColor == BLACK && killed_type_piece >= 0 ) {
				childrenBoardArray[(*moreGeneralCounter)].whitePieces[killed_type_piece] ^=
						actualBoard->boardState[aux_count].generatedMove;
			} else if ( actualBoard->actualColor == WHITE && killed_type_piece >= 0 ) {
				childrenBoardArray[(*moreGeneralCounter)].blackPieces[killed_type_piece] ^=
										actualBoard->boardState[aux_count].generatedMove;
			}
		}

		childrenBoardArray[(*moreGeneralCounter)].totalWhitePieces =
				childrenBoardArray[(*moreGeneralCounter)].whitePieces[Pawns]	|
				childrenBoardArray[(*moreGeneralCounter)].whitePieces[Rooks]	|
				childrenBoardArray[(*moreGeneralCounter)].whitePieces[Bishops]	|
				childrenBoardArray[(*moreGeneralCounter)].whitePieces[Knights]	|
				childrenBoardArray[(*moreGeneralCounter)].whitePieces[Queens]	|
				childrenBoardArray[(*moreGeneralCounter)].whitePieces[King];

		childrenBoardArray[(*moreGeneralCounter)].totalBlackPieces =
				childrenBoardArray[(*moreGeneralCounter)].blackPieces[Pawns]	|
				childrenBoardArray[(*moreGeneralCounter)].blackPieces[Rooks]	|
				childrenBoardArray[(*moreGeneralCounter)].blackPieces[Bishops]	|
				childrenBoardArray[(*moreGeneralCounter)].blackPieces[Knights]	|
				childrenBoardArray[(*moreGeneralCounter)].blackPieces[Queens]	|
				childrenBoardArray[(*moreGeneralCounter)].blackPieces[King];

		childrenBoardArray[(*moreGeneralCounter)].occupiedSquares =
				childrenBoardArray[(*moreGeneralCounter)].totalBlackPieces |
				childrenBoardArray[(*moreGeneralCounter)].totalWhitePieces;

		childrenBoardArray[(*moreGeneralCounter)].actualColor =
				childrenBoardArray[(*moreGeneralCounter)].actualColor == WHITE ? BLACK : WHITE;

		/*
		 * TODO: HERE IS WHERE WE WILL PUNCTUATE THE NODES TO ORDER THEM IN THE ARRAY
		 */
		//childrenBoardArray[(*moreGeneralCounter)].killedTypePiece = actualBoard->boardState[aux_count].killedTypePiece;
		//childrenBoardArray[(*moreGeneralCounter)].heuristic = get_Heuristic(actualBoard, 0);

		for(i = 0; i < MAX_NUM_MOVES; i++){
			childrenBoardArray[(*moreGeneralCounter)].boardState[i].generatedMove = 0;
			childrenBoardArray[(*moreGeneralCounter)].boardState[i].killedTypePiece = -1;
		}


		(*moreGeneralCounter)++;
	}
	return SUCCESS;
}
