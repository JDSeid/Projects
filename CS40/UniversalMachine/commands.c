/* HW6 UM, Comp40 Tufts
 * commands.c
 * Addison Mirliani and Jacob Seidman
 * 4.19.21
 * This is the implementation of the commands module of the um
 */

#include "commands.h"

typedef enum Um_opcode {
        CMOV = 0, SLOAD, SSTORE, ADD, MUL, DIV,
        NAND, HALT, MAP, UNMAP, OUT, IN, LOADP, LV
} Um_opcode;

static void runCommand(uint32_t opCode, uint32_t command);
static void halt();
static void cmov(uint32_t a, uint32_t b, uint32_t c);
static void sload(uint32_t a, uint32_t b, uint32_t c);
static void sstore(uint32_t a, uint32_t b, uint32_t c);
static void add(uint32_t a, uint32_t b, uint32_t c);
static void multiply(uint32_t a, uint32_t b, uint32_t c);
static void divide(uint32_t a, uint32_t b, uint32_t c);
static void nand(uint32_t a, uint32_t b, uint32_t c);
static void map(uint32_t b, uint32_t c);
static void unmap(uint32_t c);
static void halt();
static void out(uint32_t c);
static void in(uint32_t c);
static void loadp(uint32_t b, uint32_t c);
static void loadv(uint32_t commmand);
static uint32_t getOpCode(uint32_t command);
static uint32_t getA(uint32_t command);
static uint32_t getB(uint32_t command);
static uint32_t getC(uint32_t command);

/* Unpacks the op code of each word in segment zero and executes the
 * appropriate instruction by either calling the load value function or calling
 * the runCommand function, which handles all other commands */
void runProgram()
{
    uint32_t opCode = 0;
    while(opCode != HALT) {
        /* get next command from memory */
        uint32_t command = getNextCommand();
        opCode = getOpCode(command);
        if(opCode == 13){
            /* Load Value*/
            loadv(command);
        } else{
            runCommand(opCode, command);
        }
    }
}

/* Unpacks the A, B, and C values of the command and executes the appropriate
 * instruction based on the op code. */
static void runCommand(uint32_t opCode, uint32_t command)
{
    uint32_t A = getA(command);
    uint32_t B = getB(command);
    uint32_t C = getC(command);
    
    switch(opCode) {
        case CMOV:
            cmov(A, B, C);
            break;
        case SLOAD:
            sload(A, B, C);
            break;
        case SSTORE:
            sstore(A, B, C);
            break;
        case ADD:
            add(A, B, C);
            break;
        case MUL:
            multiply(A, B, C);
            break;
        case DIV:
            divide(A, B, C);
            break;
        case NAND:
            nand(A, B, C);
            break;
        case HALT:
            halt();
            break;
        case MAP:
            map(B, C);
            break;
        case UNMAP:
            unmap(C);
            break;
        case OUT:
            out(C);
            break;
        case IN:
            in(C);
            break;
        case LOADP:
            loadp(B, C);
            break;
        
    }
    
}

/* If the value in register c is not equal to zero, the value in register a is
 * set to the value in register b. Otherwise, nothing happens. */
static void cmov(uint32_t a, uint32_t b, uint32_t c)
{
    if(getRegister(c) != 0) {
        setRegister(a, getRegister(b));
    }
}

/* Loads the value at m[$r[b]][$r[c]] and stores the word in register a */
static void sload(uint32_t a, uint32_t b, uint32_t c)
{
    setRegister(a, getWord(getRegister(b), getRegister(c)));
}

/* Sets the word at m[$r[a]][$r[b]] equal to the value in register c */
static void sstore(uint32_t a, uint32_t b, uint32_t c)
{
    setWord(getRegister(a), getRegister(b), getRegister(c));
}

/* Adds the values in registers b and c, mods it by 2^32, and stores the result
 * in register a */
static void add(uint32_t a, uint32_t b, uint32_t c)
{
    setRegister(a, (getRegister(b) + getRegister(c)));
}


/* Multiplies the values in registers b and c, mods it by 2^32, and stores the 
    result in register a */
static void multiply(uint32_t a, uint32_t b, uint32_t c)
{
    setRegister(a, (getRegister(b) * getRegister(c)));
}

/* Divides the value in register b by the value in register b, and stores the
    result in register a. Any remained is truncated*/
static void divide(uint32_t a, uint32_t b, uint32_t c)
{
    setRegister(a, (getRegister(b) / getRegister(c)));
}

/* Takes the bitwise nand of the values in register b and c. Places the result
   in register a. */
static void nand(uint32_t a, uint32_t b, uint32_t c)
{
    setRegister(a, ~(getRegister(b) & getRegister(c)));
}

/* Maps a new segment to an unoccupied index with a lenght equal to the value 
    in register c. Stores the index of the new segment into register b */
static void map(uint32_t b, uint32_t c)
{
    uint32_t index = mapSegment(getRegister(c));
    setRegister(b, index);
}

/*Frees all memory associated with the segment with an index equal to the value
  in register c. Adds the unmapped segment's ID onto the stack of 
    useable ids*/
static void unmap(uint32_t c)
{
    unmapSegment(getRegister(c));
}

/* Frees all memory associated with the UM */
static void halt()
{
    freeAllMemory();
}

/*Converts the value in register c to its corresponding ascii character, and 
  prints it to stdout*/
static void out(uint32_t c)
{
    putc(getRegister(c), stdout);
}

/* Reads in a single character from stdin. Stores its ascii value in 
    register c. If EOF is read in from stdin, sets register c to ~0*/
static void in(uint32_t c)
{
    uint32_t input = getc(stdin);
    if ((int)input == EOF) {
        input = ~0;
    }
    setRegister(c, input);
}

/*Duplicates the segment at the index stored in register b and loads it into 
  segment 0. Sets the program counter equal to the value in register c */
static void loadp(uint32_t b, uint32_t c)
{
    loadProgram(getRegister(b), getRegister(c));
}

/*Loads the value given in the um instruction into the register in the given
    um instruction*/
static void loadv(uint32_t command)
{
    uint32_t value = Bitpack_getu(command, 25, 0);
    uint32_t a = Bitpack_getu(command, 3, 25);
    setRegister(a, value);
}

/* Unpacks and returns the opcode from a given um instruction*/
static uint32_t getOpCode(uint32_t command)
{
    return Bitpack_getu(command, 4, 28);
}

/* Unpacks and returns the first register from a given um instruction*/
static uint32_t getA(uint32_t command)
{
    return Bitpack_getu(command, 3, 6); 
}

/* Unpacks and returns the second register from a givenum instruction*/
static uint32_t getB(uint32_t command)
{
    return Bitpack_getu(command, 3, 3);
}

/* Unpacks and returns the third register from a given um instruction*/
static uint32_t getC(uint32_t command)
{
    return Bitpack_getu(command, 3, 0);
}


