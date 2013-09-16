CFLAGS = -Wall -Wextra -ggdb
all: link
Mensajes.o: Mensajes.c Mensajes.h
	gcc -c Mensajes.c $(CFLAGS)
Salida.o: Salida.c Salida.h
	gcc -c Salida.c $(CFLAGS)
Nullbytes.o: Nullbytes.c
	gcc -c Nullbytes.c $(CFLAGS)
link: Mensajes.o Salida.o Nullbytes.o
	gcc Mensajes.o Salida.o Nullbytes.o -o NullBytes
