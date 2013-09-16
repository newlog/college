

#ifndef SEARCH_TEST_H_
#define SEARCH_TEST_H_

#include "../structs/board.h"
#include "../search/search_functions.h"


int test_create_children(Board *actualBoard, Board *childrenBoards);
int create_complex_board_test(Board* boardTest);
int create_two_opponent_rooks_board_test(Board* boardTest);
int create_two_opponent_pawns_board_test(Board* boardTest);


#endif
