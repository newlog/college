#include "search_functions.h"
#include <string.h>


int negascout (Board * actualBoard, int alpha, int beta, int actualDepth, BitMap * nextMove) {

	// If we've reached the "end" of the tree, we return its value
	// We do this at first, because we don't want to store vars in the stack before knowing if we need them
	if ( actualDepth == MAX_DEPTH )
	{
		/*
		 * 	LOL TEST
		 */
		actualBoard->heuristic = evaluate( actualBoard, actualDepth );
		return actualBoard->heuristic;
		/*
		 * 	FI LOL TEST
		 */
		//return evaluate( actualBoard, actualDepth );
	}

	// We declare the needed variables.
	Board children[MAX_CHILDREN];		//Array of children
	int childrenNumber= -1;				//Number of generated children
	int returnValue = -1;				//Return value for copyStruct() and createChildren()
	// Negascout algorithm variables.
	int a, b, t, i;

	// The children array is inited with the actual board value.
	// This is done this way to save us time and just change a few values from the father to the child.
	returnValue = copyBoardStruct(children, actualBoard);
	if ( returnValue ) {
		logChess(WARN, "The structure was not copied correctly.");
		logChess(WARN, "Negascout will still run, cross your fingers.");
	}
	// The children boards are created.
	returnValue = createChildren(actualBoard, children, &childrenNumber);
	if ( returnValue ) {
		logChess(WARN, "An error ocurred while creating the children.");
		logChess(WARN, "Negascout will still run, cross your fingers.");
	}
	// The actual values for alpha and beta are refreshed.
	a = alpha;
	b = beta;
	// All children are processed and his own negascout function is called if we do not prune.
	actualDepth++;
	for (i = 0; i < childrenNumber; i++)
	{

		logBoard(INFO, children[i].occupiedSquares);
		// Recursive call to negascout. A depth search algorithm is applied.
		t = -( negascout(&children[i], -b, -a, actualDepth, nextMove) );

		// Subsearch
		if ( ( t > a) && ( t < beta) && ( i > 0) && ( actualDepth < MAX_DEPTH - 1) ) {
			//TODO: We have to think if in the subsearch we have to increment the actualDepth as in the normal search
			a = -( negascout(&children[i], -beta, -t, actualDepth, nextMove) );
		}
		// We calculate the max of a and t. (It is done with the macro defined in the .h)
		// The t is the return value of the previous negascout call
		a = max(a, t);
		// If alpha (the past alpha or the returned alpha) is greater than beta, we have to prune.
		if ( a >= beta ) {
			/*
			 * 	LOL TEST
			 */
			actualBoard->heuristic = a;
			/*
			 * 	FI LOL TEST
			 */
			return a;
		}

		// We assign to beta, alpha plus one, so in the next loop we will not prune because of a, but t.
		b = a + 1;
	}
	//TODO: Test it
	if ( actualDepth == 1 ) {
		// We have to return the move which more heuristic returns us.
		// So we have to search the children array in order to get the best son.
		//We reuse the returnValue variable to no store more data in the stack.
		returnValue = 0;	//We init returnValue to 0, in order to prevent fail.
		t = MIN_INT_VALUE;
		for ( i = 0; i < childrenNumber; i++ )
		{
			// We won't have two equal heuristics because it would be a pruned branch.
			if ( children[i].heuristic > t ) {
				*nextMove = children[i].occupiedSquares;
				t = children[i].heuristic;
			}
		}
	}
	// We've finished the children nodes, so we have to go a level up returnig alpha.
	/*
	 * 	LOL TEST
	 */
	actualBoard->heuristic = a;
	/*
	 * 	FI LOL TEST
	 */
	return a;
}


int alpha_beta_negamax (Board * actualBoard, int a, int b, int actualDepth, Board * nextMove) {

	// If we've reached the "end" of the tree, we return its value
	// We do this at first, because we don't want to store vars in the stack before knowing if we need them
	if ( actualDepth == MAX_DEPTH )
	{
		return evaluate( actualBoard, actualDepth );
	}



	// We declare the needed variables.
	Board children[MAX_CHILDREN];		//Array of children
	int childrenNumber= -1;				//Number of generated children
	int returnValue = -1;				//Return value for copyStruct() and createChildren()
	int i = 0;
	int t = 0;

	// The children array is inited with the actual board value.
	// This is done this way to save us time and just change a few values from the father to the child.
	returnValue = copyBoardStruct(children, actualBoard);
	if ( returnValue ) {
		logChess(WARN, "The structure was not copied correctly.");
		logChess(WARN, "Negascout will still run, cross your fingers.");
	}
	// The children boards are created.
	returnValue = createChildren(actualBoard, children, &childrenNumber);
	if ( returnValue ) {
		logChess(WARN, "An error ocurred while creating the children.");
		logChess(WARN, "Negascout will still run, cross your fingers.");
	}
	// All children are processed and his own negascout function is called if we do not prune.
	for (i = 0; i < childrenNumber; i++)
	{

		//logBoard(INFO, children[i].occupiedSquares);

		/*
		 * Control that in the actualBoard we're not in check or if we've made a check with this movement
		 */

		if(evaluateOurCheck(&children[i])==KING_IN_CHECK)		//evaluate if we're in check
		{
			t=OUR_CHECK_VALUE*sideToMove(actualDepth);	//We don't put the - sign,because we evaluate the current child
		}
		else if(evaluateOpponentCheck(&children[i])==KING_IN_CHECK)
		{
			t=OPP_CHECK_VALUE*sideToMove(actualDepth);
		}
		else
		{
		// Recursive call to negascout. A depth search algorithm is applied.
		t = -( alpha_beta_negamax(&children[i], -b, -a, actualDepth+1, nextMove) );
		}
		if(actualBoard->myColor == actualBoard->actualColor){
				if(jake_mate_heuristic(actualBoard,actualDepth) > 0){
					t=t+100*sideToMove(actualDepth);//Bonus posible jake mate
				}
		}


		if (t >= b)  return b;

		if (t > a) {
			a = t;
			if ( actualDepth == 0)
				memcpy(nextMove, &children[i], sizeof(Board));
		}

	}

	return a;
}





int createChildren ( Board * actualBoard, Board * childrenBoardArray, int *moreGeneralCounter) {
	if ( actualBoard == NULL ) {
		logChess(FATAL, "The actualBoard is null.");
		return NULL_BOARD;
	}
	if ( childrenBoardArray == NULL ) {
		logChess(FATAL, "The childrenBoardArray is null.");
		return NULL_BOARD;
	}
	if ( moreGeneralCounter == NULL ) {
		logChess(FATAL, "The moreGeneralCounter is null.");
		return NULL_INT;
	}

	//TODO: It lacks an heuristic to choose in which order the children are generated!

	/*
	 * First he have to choose if we are going to move
	 */
	int i = 0;
	int result = -1;
	int generalCounter = 0;
	*moreGeneralCounter=0;
	if ( actualBoard->actualColor == WHITE ) {
		for (i = 0; i < N_TYPES; i++) {
			// In the move_piece function we hace to look if the intended moved piece is dead!!
			result = move_white_piece(i, actualBoard, &generalCounter, moreGeneralCounter, childrenBoardArray);
		}
	} else if ( actualBoard->actualColor == BLACK ) {
		for (i = 0; i < N_TYPES; i++) {
			// In the move_piece function we hace to look if the intended moved piece is dead!!
			result = move_black_piece(i, actualBoard, &generalCounter, moreGeneralCounter, childrenBoardArray);
		}
	}

	return SUCCESS;
}


int copyBoardStruct( Board * children, Board * actualBoard)
{

	if ( actualBoard == NULL ) {
		logChess(FATAL, "The actualBoard is null.");
		return NULL_BOARD;
	}
	if ( children == NULL ) {
		logChess(FATAL, "The childrenBoardArray is null.");
		return NULL_BOARD;
	}

	int i = 0;
	for (i = 0; i < MAX_CHILDREN; i++) {
		memcpy(&children[i], actualBoard, sizeof(Board));
	}

	return SUCCESS;
}



int evaluate( Board * board, int actualDepth )
{
	return get_Heuristic(board, actualDepth);
}








