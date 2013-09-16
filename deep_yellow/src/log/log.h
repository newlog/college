/*
 * log.h
 *
 *  Created on: 03/11/2011
 *      Author: newlog
 */


#ifndef LOG_H_
#define LOG_H_

#include "../structs/board.h"

/*
 * 	OFF: 	este es el nivel de m�nimo detalle, deshabilita todos los logs.
 *	FATAL: 	se utiliza para mensajes cr�ticos del sistema, generalmente despu�s de guardar el mensaje el programa abortar�.
 *	ERROR: 	se utiliza en mensajes de error de la aplicaci�n que se desea guardar, estos eventos afectan al programa pero lo dejan seguir funcionando, como por ejemplo que alg�n par�metro de configuraci�n no es correcto y se carga el par�metro por defecto.
 *	WARN: 	se utiliza para mensajes de alerta sobre eventos que se desea mantener constancia, pero que no afectan al correcto funcionamiento del programa.
 *	INFO: 	se utiliza para mensajes similares al modo verbose en otras aplicaciones.
 *	DEBUG: 	se utiliza para escribir mensajes de depuraci�n. Este nivel no debe estar activado cuando la aplicaci�n se encuentre en producci�n.
 *	TRACE: 	se utiliza para mostrar mensajes con un mayor nivel de detalle que debug.
 *	ALL: 	este es el nivel de m�ximo detalle, habilita todos los logs (en general equivale a TRACE).
 */
#define OFF				0
#define FATAL			1
#define ERROR			2
#define WARN			3
#define INFO			4
#define DEBUG			5
#define TRACE			6
#define ALL				7

#define SUCCESS			0	// Standard return value for log library
#define ALREADY_INIT	-1	// Error code for double log library initialization
#define BAD_LOG_LEVEL	-2	// Error code for incorrect initialization log level.
#define BAD_MODE		-3	// Error code for incorrect initialization mode.
#define NOT_INIT		-4	// Error code for not initializated log library
#define NOT_DEFINED_MSG	-5	// Error code for not defined message as log.
#define ERR_OP_FILE		-6	// Error creating output log file
#define ERR_CL_FILE		-7	// Error closing output log file
#define LOG_NOT_DEF		-50	// Error ocurred because logActivated is not defined

/*
 * This macros define if the log will be output in the screen or a file.
 */
#define SCREEN_MODE		0	// Value for logging to a screen.
#define FILE_MODE		1	// Value for logging to a file.

#define FILE_NAME		"ChessLog.log"	// Log file name.


/*
 * This variables are used to define if the log must be done or not.
 */
//#define logActivated
#undef logActivated

/*
 * If the logActivated variable is not defined, the log action is not done,
 * so the logChess macro is replaced by a no effect instruction.
 * This no effect instruction is suitable for the cases when the chess_log macro
 * is assigned to a variable. Resulting in the variable getting the LOG_NOT_DEF value.
 */
#ifdef logActivated
	#define logChess(logLevel, msg)       	logWithDebug(__FILE__, __LINE__, __FUNCTION__, logLevel, msg)
#else
	#define logChess(logLevel, msg)			LOG_NOT_DEF;
#endif


int definedLogLevel;		// This variable represent the log level.
int definedMode;			// This variable represents the mode in which the log should be output.
int alreadyInit;			// This variable is checked to know if the log library is already inited.

/*
 * This function initialize the log library.
 * @param logLevelParam This variable identify the minimum log level that should be logged. Its value could be - in priority order - OFF, FATAL, ERROR, WARN, INFO, DEBUG, TRACE, ALL.
 * @param modeParam This variable identify where the log should be output. Its value could be SCREEN_MODE or FILE_MODE.
 * @return This function returns:
 * 			SUCCESS if everything goes right,
 * 			BAD_LOG_LEVEL if the logLevelParam is not correct,
 * 			BAD_MODE_PARAM if the modeParam is not correct,
 * 			ALREADY_INIT if the log library has been inited previously,
 * 			ERR_OP_FILE if the log file could not be created,
 * 			ERR_CL_FILE if the created log file could not be closed.
 */
int initLog(	int logLevelParam,
				int modeParam);

/*
 * This function logs the message from the user with some debug parameters.
 * @param file This variable identifies the code file from which the log function has been done.
 * @param line This variable identifies the line of code from which the log function has been called.
 * @param func This variable identifies the line of code from which the log function has been called.
 * @param logLevel This variable identifies the log level of the logged event.
 * @param msg This variable identifies the message to be logged.
 * @return This function returns:
 *			SUCCESS if everything goes right,
 *			NOT_INIT if the log library has not been inited yet,
 *			BAD_LOG_LEVEL if the logLevelParam is not correct,
 * 			ERR_OP_FILE if the log file could not be created,
 * 			ERR_CL_FILE if the created log file could not be closed.
 */
int logWithDebug(	const char *file,
					int line,
					const char * func,
					int logLevel,
					const char * msg);
/*
 * This function logs the state of the BitMap argument.
 * @param logLevel This variable identifies the log level of the logged event.
 * @param board This variable identifies the BitMap variable to log.
 * @return This function returns:
 * 			SUCCESS if everything goes right,
 * 			NOT_INIT if the log library has not been inited yet,
 * 			BAD_LOG_LEVEL if the logLevelParam is not correct,
 * 			ERR_OP_FILE if the log file could not be created,
 * 			ERR_CL_FILE if the created log file could not be closed.
 *
 */
int logBoard(	int logLevel,
				BitMap board);

#endif /* LOG_H_ */
