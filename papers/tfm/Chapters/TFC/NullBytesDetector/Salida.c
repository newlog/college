#define GNU_SOURCE
#include <stdio.h>
#include <string.h>
#include <stdlib.h>				//Para los tipos de variable ssize_y y size_t

void Proceso (char * salida) {
//printf("%s\n", salida);
char *linea = NULL, CodigoMaquina[70], Opcode[70], *str1;
const char *delim = "\n";
size_t i = 0, j;
size_t BytesNulos = 0;
char *saveptr1;


for(str1 = salida ; ; str1 = NULL) {		//El primer valor de str1 ha de ser cadena, despues ha de ser NULL siempre
	linea = strtok_r(str1, delim, &saveptr1);		//Dejamos que strtok_r haga su magia
	if (linea == NULL) break;				//Si ya lo hemos capturado todo salimos
	
	bzero(CodigoMaquina, sizeof(CodigoMaquina));
	i = 0;
	while ( (linea[10+i] < 97) || (linea[10+i] > 122)  ) {	
	//Mientras no leamos una minúscula (signo de que es un opcode)
		CodigoMaquina[i] = linea[10+i];				//En linea[10] empieza el codigo maquina	
		i++;
	}//fin while
	bzero(Opcode, sizeof(Opcode));
	j = i;
	while ( ((linea[10+i] != '\n') || (linea[10+i] != '\0')) && ((i+10) < strlen(linea))) {
		Opcode[i-j] = linea[10+i];
		i++;
	}
	
	i = 0;
	while ( CodigoMaquina[i] != '\0' ) {
		if ( ( (i % 2) == 0 ) && ( CodigoMaquina[i] == 48 ) && ( CodigoMaquina[i+1] == 48 ) ) {
			//Tenemos un byte nulo
			printf("Byte Nulo en %s perteneciente a la instrucción         %s\n", CodigoMaquina, Opcode);
			BytesNulos++;
		}
		i++;
	}
}//Fin for
printf("\nHay un total de %d bytes nulos\n", BytesNulos);
}
