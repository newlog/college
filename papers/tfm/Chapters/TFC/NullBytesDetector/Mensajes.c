#include <stdio.h>
#include <stdlib.h>

void Errors (int numError) {
switch(numError) {
	case 1:
		printf("[-] Usage: ./NullBytes <program>\n");break;
	case 2:
		printf("[-] No se ha podido crear la tuberia\n");break;
	case 3:
		printf("[-] No se ha podido crear el hijo\n");break;
	case 4:
		printf("[-] No se han podido duplicar los descriptores de archivo\n");break;
	case 5:
		printf("[-] No se han podido ejecutar ndisasm\n");break;
	case 6:
		printf("[-] La longitud del parametro es de mas de 145 caracteres\n");break;
	case 7:
		printf("[+] NullBytes te señalará qué instrucciones desensambladas contienen los bytes nulos y además cuantos hay en total\n");
		printf("[+] El shellcode desensamblado no puede tener más de 5000 bytes.\n");
		printf("[+] Usage: ./NullByte.o <program>\n");break;
	default:
	break;	
}
	exit(-1);
}

void Mensaje () {
	printf("\n[+] NullBytes v0.1, por Albert López Fernández\n");
	printf("[+] Haciendo uso de la utilidad ndisasm,\n");
	printf("[+] NullBytes te permite saber si tu shellcode contiene bytes nulos y donde se encuentran\n\n");
}
