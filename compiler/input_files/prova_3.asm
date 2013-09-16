.text
.align 2
.globl main
main:
# CALCULO LA DIRECCIÓN DE UNA VARIABLE O CTE. NO LA DIRECCIÓN DE ACCESO A UN VECTOR.
# Codigo para leer un logic estatico
li $v0,8
la $a0,readedLogic
addi $a1, $zero, 5
syscall   #got string 1
la $8,readedLogic  #pass address of str1
la $9,CERT  #pass address of str2
j strcmpCodeETI1  #call strcmpCode
strcmpCodeETI1:
add $13,$zero,$zero
add $14,$zero,$8
add $15,$zero,$9
ETI5:
lb $16($14)  #load a byte from each string
lb $17($15)
beqz $16,ETI6 #str1 end
beqz $17,ETI7
slt $18,$16,$17  #compare two bytes
bnez $18,ETI7
addi $14,$14,1  #t1 points to the next byte of str1
addi $15,$15,1
j ETI5
ETI7:
addi $12,$zero,1
j ETI8
ETI6:
bnez $17,ETI7
add $12,$zero,$zero
ETI8:
j ETI3
ETI3:
move $10, $12
la $9,FALS  #pass address of str2
j strcmpCodeETI2  #call strcmpCode
strcmpCodeETI2:
add $13,$zero,$zero
add $14,$zero,$8
add $15,$zero,$9
ETI9:
lb $16($14)  #load a byte from each string
lb $17($15)
beqz $16,ETI10 #str1 end
beqz $17,ETI11
slt $18,$16,$17  #compare two bytes
bnez $18,ETI11
addi $14,$14,1  #t1 points to the next byte of str1
addi $15,$15,1
j ETI9
ETI11:
addi $12,$zero,1
j ETI12
ETI10:
bnez $17,ETI11
add $12,$zero,$zero
ETI12:
j ETI4
ETI4:
move $11, $12
beqz $10, ETI13
bnez $11, ETI14
li $13, 0
sw $13, 20($gp)
b ETI14
ETI13:
li $13, 1
sw $13, 20($gp)
b ETI14
ETI14:
# Fin codigo
# CALCULO LA DIRECCIÓN DE UNA VARIABLE O CTE. NO LA DIRECCIÓN DE ACCESO A UN VECTOR.
# Genero codigo para la asignacion
li $8, 1
sw $8, 24($gp)
# CALCULO LA DIRECCIÓN DE UNA VARIABLE O CTE. NO LA DIRECCIÓN DE ACCESO A UN VECTOR.
# CALCULO LA DIRECCIÓN DE UNA VARIABLE O CTE. NO LA DIRECCIÓN DE ACCESO A UN VECTOR.
# Codigo del punto (3) de expresiones (no estatico)
lw $8, 20($gp)
# Fin codigo
# CALCULO LA DIRECCIÓN DE UNA VARIABLE O CTE. NO LA DIRECCIÓN DE ACCESO A UN VECTOR.
# Codigo del punto (3) de expresiones (no estatico)
lw $9, 20($gp)
# Fin codigo
# Codigo de expresion para realizar una and no estatica
or $8, $8, $9
# Fin codigo
# CALCULO LA DIRECCIÓN DE UNA VARIABLE O CTE. NO LA DIRECCIÓN DE ACCESO A UN VECTOR.
# Codigo del punto (3) de expresiones (no estatico)
lw $9, 24($gp)
# Fin codigo
# Codigo de expresion para realizar una and no estatica
or $8, $8, $9
# Fin codigo
# Genero codigo para la asignacion
sw $8, 20($gp)
# Codigo para escribir una cadena estatica
li $v0, 1
li $v0, 4
la $a0, ETI16
syscall
.data
ETI16:
.asciiz "\n"
.text
# Fin codigo
# CALCULO LA DIRECCIÓN DE UNA VARIABLE O CTE. NO LA DIRECCIÓN DE ACCESO A UN VECTOR.
# Codigo del punto (3) de expresiones (no estatico)
lw $9, 20($gp)
# Fin codigo
# Codigo para escribir un logico no estatico
li $v0, 1
li $v0, 4
beq $9, $0, ETI17
la $a0, CERT
j ETI18
ETI17:
la $a0, FALS
ETI18:
syscall
# Fin codigo
# Codigo para cerrar el programa
li $v0, 10
syscall
# Ultima instruccion del main
jr $ra



# Codigo para el error en la comprobacion de los limites de un vector
ERROR:
li $v0, 4
la $a0, MERROR
syscall
li $v0, 10
syscall
# Fin codigo



# Codigo para el error en la division por cero
ERROR2:
li $v0, 4
la $a0, MERROR2
syscall
li $v0, 10
syscall
# Fin codigo
.data
.align 0
MERROR:
.asciiz "[-] Index out of bounds\n"
MERROR2:
.asciiz "[-] Division by zero, black hole incoming!\n"
CERT:
.asciiz "CERT"
FALS:
.asciiz "FALS"
readedLogic: 
.space 4 
