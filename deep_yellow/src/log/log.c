/*
 * log.c
 *
 *  Created on: 03/11/2011
 *      Author: newlog
 */

#include <stdio.h>
#include "log.h"

int initLog(	int logLevelParam,
				int modeParam) {

	if ( logLevelParam < 0 || logLevelParam > 7 ) {
		fprintf(stderr, "[-] Incorrect initialitation log level.");
		return BAD_LOG_LEVEL;
	}
	if ( modeParam < 0 || modeParam > 1 ) {
		fprintf(stderr, "[-] Incorrect log mode.");
		return BAD_MODE;
	}
	if ( alreadyInit == 1 ) {
		fprintf(stderr, "[-] The log library has been already initialized.");
		return ALREADY_INIT;
	}

	if ( ! alreadyInit ) {
		int success = 0;
		definedLogLevel = logLevelParam;
		definedMode = modeParam;

		if ( definedMode == FILE_MODE ) {
			FILE * fd = fopen(FILE_NAME, "w");
			if ( fd == NULL ) {
				fprintf(stderr, "[-] The log file could not be created.\n");
				return ERR_OP_FILE;
			}
			if ( fd != NULL && fclose(fd) != 0 ) {
				fprintf(stderr, "[-] The created log file could not be closed.\n");
				return ERR_CL_FILE;
			}
		}

		success++;
		alreadyInit = success;
	}

	return SUCCESS;
}



int logWithDebug(	const char *file,
					int line,
					const char *func,
					int logLevel,
					const char * msg) {

	if ( !alreadyInit ) {
		fprintf(stderr, "[-] The log library has not been initialized.");
		return NOT_INIT;
	}
	if ( logLevel < 0 || logLevel > 7 ) {
		fprintf(stderr, "[-] Incorrect log level.");
		return BAD_LOG_LEVEL;
	}
	if ( msg == NULL ) {
		fprintf(stderr, "[-] Message not defined.");
		return NOT_DEFINED_MSG;
	}

	if ( logLevel <= definedLogLevel ) {
		if ( definedMode == FILE_MODE ) {
			FILE * fd = NULL;

			fd = fopen(FILE_NAME, "a");

			if (fd == NULL) {
			  fprintf(stderr, "[-] The log file could not be opened.\n");
			  return ERR_OP_FILE;
			}

			if ( fprintf(fd, "%s (line %d) from %s(): %s\n", file, line, func, msg) < 0 )
				fprintf(stderr, "[-] The log file could not be written.\n");

			if ( fd != NULL && fclose(fd) != 0 ) {
				fprintf(stderr, "[-] The log file could not be closed.\n");
				return ERR_CL_FILE;
			}

		} else if ( definedMode == SCREEN_MODE ) {
			fprintf(stdin, "%s (line %d) from %s(): %s\n", file, line, func, msg);
		}
	}

    return SUCCESS;
}

int logBoard(	int logLevel,
				BitMap board)
{
	if ( !alreadyInit ) {
		fprintf(stderr, "[-] The log library has not been initialized.");
		return NOT_INIT;
	}
	if ( logLevel < 0 || logLevel > 7 ) {
		fprintf(stderr, "[-] Incorrect log level.");
		return BAD_LOG_LEVEL;
	}

	if ( logLevel <= definedLogLevel ) {
		int i,j;
		unsigned int long long mask_bitmap = 	0x80;
		unsigned int long long auxiliar_board = 0;
		FILE * fd = NULL;

		if ( definedMode == FILE_MODE ) {
			fd = fopen(FILE_NAME, "a");
			if (fd == NULL) {
				fprintf(stderr, "[-] The log file could not be opened.\n");
				return ERR_OP_FILE;
			}
		}
		if ( definedMode == FILE_MODE ) {
			fprintf(fd, "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n");
			fprintf(fd, "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n     ");
		} else if ( definedMode == SCREEN_MODE)  {
			fprintf(stdin, "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n");
			fprintf(stdin, "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n     ");
		}
		for (j = 0; j < 8; j++)
		{
			mask_bitmap = 0x80;
			for (i = 0; i < 8; i++)
			{
				auxiliar_board = board & mask_bitmap;
				if ( auxiliar_board == mask_bitmap ) {
					if ( definedMode == FILE_MODE ) {
						if ( fprintf(fd, "1  ") < 0 )
							fprintf(stderr, "[-] The log file could not be written.\n");
					} else if ( definedMode == SCREEN_MODE )
						fprintf(stdin, "1 ");
				}
				else
				{
					if ( definedMode == FILE_MODE ) {
						if ( fprintf(fd, "0  ") < 0 )
							fprintf(stderr, "[-] The log file could not be written.\n");
					} else if (definedMode == SCREEN_MODE )
						fprintf(stdin, "0 ");
				}

				mask_bitmap >>= 1;

			}
			if ( definedMode == FILE_MODE ) {
				if ( fprintf(fd, "\n     ") < 0 )
					fprintf(stderr, "[-] The log file could not be written.\n");
			} else if ( definedMode == SCREEN_MODE){
				fprintf(stdin, "\n     ");
			}
			board >>= 8;
		}
		if ( definedMode == FILE_MODE ) {
			fprintf(fd, "\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n");
			fprintf(fd, "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n");
		} else if ( definedMode == SCREEN_MODE)  {
			fprintf(stdin, "\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n");
			fprintf(stdin, "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n");
		}
		if ( fd != NULL && fclose(fd) != 0 ) {
			fprintf(stderr, "[-] The created log file could not be closed.\n");
			return ERR_CL_FILE;
		}
	}

	return SUCCESS;
}
