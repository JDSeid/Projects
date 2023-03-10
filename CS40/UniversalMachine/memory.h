/* HW6 UM, Comp40 Tufts
 * memory.h
 * Addison Mirliani and Jacob Seidman
 * 4.19.21
 * This is the interface of the memory module of the um
 */

#ifndef MEMORY_H
#define MEMORY_H

#define NUM_REGISTERS 8

#include <stack.h>
#include <seq.h>
#include <stdint.h>
#include <stdlib.h>
#include <stdio.h>
#include <assert.h>
#include <malloc.h>

/* Initializes the memory so that the ids are empty, registers are all zero,
   and segment zero is initialized to the given length and filled with zeroes */
void initMemory(int numCommands);

/* Sets a given word found in the location m[segmentID][wordID] to the given
   value */
void setWord(uint32_t segmentID, uint32_t wordID, uint32_t word);

/* Returns the word in the given location m[segmentID][wordID] */
uint32_t getWord(uint32_t segmentID, uint32_t wordID);

/* Duplicates the segment at m[segmentID] and replaces segment zero with the
   duplicated segment. It also changes the program counter 
   position to the given value
*/
void loadProgram(uint32_t segmentID, uint32_t counterPosition);

/* Gets and returns the word in segment 0 at the index given by the program
    counter. Increments the counter by 1 */
uint32_t getNextCommand();

/* Creates a new segment of given length and sets all of its words equal to
   zero. It reaturns the index of where in memory the segment was placed (the
   segment ID) */
uint32_t mapSegment(uint32_t length);

/* Frees the segment in the given position */
void unmapSegment(uint32_t index);

/* Sets the given index to the given value stored in word
    Constraint: 0 <= index <= 7 */
void setRegister(uint32_t index, uint32_t word);

/* Returns the value stored at a given registers
    Constraint: 0 <= index <= 7 */
uint32_t getRegister(uint32_t index);

/* Frees all memory associated with the segments, registers, and the stack 
    of IDS */
void freeAllMemory();


#endif