/* HW6 UM, Comp40 Tufts
 * commands.c
 * Addison Mirliani and Jacob Seidman
 * 4.19.21
 * This is the interface of the commands module of the um
 */

#ifndef COMMANDS_H
#define COMMANDS_H

#include <stdint.h>
#include <stdlib.h>
// #include "bitpack.h"
#include <stdio.h>
#include "memory.h"

/* Unpacks the op code of each word in segment zero and executes the
 * appropriate instruction */
void runProgram();

#endif