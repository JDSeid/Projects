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
static inline void halt();
static inline void cmov(uint32_t a, uint32_t b, uint32_t c);
static inline void sload(uint32_t a, uint32_t b, uint32_t c);
static inline void sstore(uint32_t a, uint32_t b, uint32_t c);
static inline void add(uint32_t a, uint32_t b, uint32_t c);
static inline void multiply(uint32_t a, uint32_t b, uint32_t c);
static inline void divide(uint32_t a, uint32_t b, uint32_t c);
static inline void nand(uint32_t a, uint32_t b, uint32_t c);
static inline void map(uint32_t b, uint32_t c);
static inline void unmap(uint32_t c);
static inline void halt();
static inline void out(uint32_t c);
static inline void in(uint32_t c);
static inline void loadp(uint32_t b, uint32_t c);
static inline void loadv(uint32_t commmand);
static inline uint32_t getOpCode(uint32_t command);
static inline uint32_t getA(uint32_t command);
static inline uint32_t getB(uint32_t command);
static inline uint32_t getC(uint32_t command);

/* Gets and returns the word in segment 0 at the index given by the program
    counter. Increments the counter by 1 */
static inline uint32_t getNextCommand()
{
    return segment0[counter++];
}

/* Sets a given word found in the location m[segmentID][wordID] to the given
   value */
static inline void setWord(uint32_t segmentID, uint32_t wordID, uint32_t word)
{
    uint32_t *segment = (uint32_t *)Seq_get(segments, segmentID);
    segment[wordID + 1] = word;
}

/* Returns the word in the given location m[segmentID][wordID] */
static inline uint32_t getWord(uint32_t segmentID, uint32_t wordID)
{
    uint32_t *segment = (uint32_t *)Seq_get(segments, segmentID);
    return segment[wordID + 1];
}

/* Sets the given index to the given value stored in word
    Constraint: 0 <= index <= 7 */
static inline void setRegister(uint32_t index, uint32_t word)
{
    registers[index] = word;
}


/* Returns the value stored at a given registers
    Constraint: 0 <= index <= 7 */
static inline uint32_t getRegister(uint32_t index)
{
    return registers[index];
}

/* Unpacks the op code of each word in segment zero and executes the
 * appropriate instruction by either calling the load value function or calling
 * the runCommand function, which handles all other commands */
void runProgram()
{
    uint32_t opCode = 0;
    uint32_t command;
    while(opCode != HALT) {
        /* get next command from memory */
        command = getNextCommand();
        opCode = getOpCode(command);
        if(opCode == 13){
            /* Load Value*/
            loadv(command);
        } else {
            runCommand(opCode, command);
        }
    }
}

/* Unpacks the A, B, and C values of the command and executes the appropriate
 * instruction based on the op code. */
static void runCommand(uint32_t opCode, uint32_t command)
{
    uint32_t A;
    uint32_t B;
    uint32_t C;
    
    switch(opCode) {
        // case LV:
        //     loadv(command);
        //     break;
        case CMOV:
            A = getA(command);
            B = getB(command);
            C = getC(command);
            cmov(A, B, C);
            break;
        case SLOAD:
            A = getA(command);
            B = getB(command);
            C = getC(command);
            sload(A, B, C);
            break;
        case SSTORE:
            A = getA(command);
            B = getB(command);
            C = getC(command);
            sstore(A, B, C);
            break;
        case ADD:
            A = getA(command);
            B = getB(command);
            C = getC(command);
            add(A, B, C);
            break;
        case MUL:
            A = getA(command);
            B = getB(command);
            C = getC(command);
            multiply(A, B, C);
            break;
        case DIV:
            A = getA(command);
            B = getB(command);
            C = getC(command);
            divide(A, B, C);
            break;
        case NAND:
            A = getA(command);
            B = getB(command);
            C = getC(command);
            nand(A, B, C);
            break;
        case HALT:
            halt(); 
            break;
        case MAP:
            B = getB(command);
            C = getC(command);
            map(B, C);
            break;
        case UNMAP:
            C = getC(command);
            unmap(C);
            break;
        case OUT:
            C = getC(command);
            out(C);
            break;
        case IN:
            C = getC(command);
            in(C);
            break;
        case LOADP:
            B = getB(command);
            C = getC(command);
            loadp(B, C);
            break;
    }
    
}

static inline uint64_t Bitpack_getu(uint64_t word, unsigned width, unsigned lsb)
{
        return (((((uint64_t)~0) >> (64-width)) << lsb) & word) >> lsb;
}

/* If the value in register c is not equal to zero, the value in register a is
 * set to the value in register b. Otherwise, nothing happens. */
static inline void cmov(uint32_t a, uint32_t b, uint32_t c)
{
    if(getRegister(c) != 0) {
        setRegister(a, getRegister(b));
    }
}

/* Loads the value at m[$r[b]][$r[c]] and stores the word in register a */
static inline void sload(uint32_t a, uint32_t b, uint32_t c)
{
    setRegister(a, getWord(getRegister(b), getRegister(c)));
}

/* Sets the word at m[$r[a]][$r[b]] equal to the value in register c */
static inline void sstore(uint32_t a, uint32_t b, uint32_t c)
{
    setWord(getRegister(a), getRegister(b), getRegister(c));
}

/* Adds the values in registers b and c, mods it by 2^32, and stores the result
 * in register a */
static inline void add(uint32_t a, uint32_t b, uint32_t c)
{
    setRegister(a, (getRegister(b) + getRegister(c)));
}


/* Multiplies the values in registers b and c, mods it by 2^32, and stores the 
    result in register a */
static inline void multiply(uint32_t a, uint32_t b, uint32_t c)
{
    setRegister(a, (getRegister(b) * getRegister(c)));
}

/* Divides the value in register b by the value in register b, and stores the
    result in register a. Any remained is truncated*/
static inline void divide(uint32_t a, uint32_t b, uint32_t c)
{
    setRegister(a, (getRegister(b) / getRegister(c)));
}

/* Takes the bitwise nand of the values in register b and c. Places the result
   in register a. */
static inline void nand(uint32_t a, uint32_t b, uint32_t c)
{
    setRegister(a, ~(getRegister(b) & getRegister(c)));
}

/* Maps a new segment to an unoccupied index with a lenght equal to the value 
    in register c. Stores the index of the new segment into register b */
static inline void map(uint32_t b, uint32_t c)
{
    uint32_t index = mapSegment(getRegister(c));
    setRegister(b, index);
}

/*Frees all memory associated with the segment with an index equal to the value
  in register c. Adds the unmapped segment's ID onto the stack of 
    useable ids*/
static inline void unmap(uint32_t c)
{
    unmapSegment(getRegister(c));
}

/* Frees all memory associated with the UM */
static inline void halt()
{
    freeAllMemory();
    exit(EXIT_SUCCESS);
}

/*Converts the value in register c to its corresponding ascii character, and 
  prints it to stdout*/
static inline void out(uint32_t c)
{
    putc(getRegister(c), stdout);
}

/* Reads in a single character from stdin. Stores its ascii value in 
    register c. If EOF is read in from stdin, sets register c to ~0*/
static inline void in(uint32_t c)
{
    uint32_t input = getc(stdin);
    if ((int)input == EOF) {
        input = ~0;
    }
    setRegister(c, input);
}

/*Duplicates the segment at the index stored in register b and loads it into 
  segment 0. Sets the program counter equal to the value in register c */
static inline void loadp(uint32_t b, uint32_t c)
{
    loadProgram(getRegister(b), getRegister(c));
}

/*Loads the value given in the um instruction into the register in the given
    um instruction*/
static inline void loadv(uint32_t command)
{
    uint32_t value = Bitpack_getu(command, 25, 0);
    uint32_t a = Bitpack_getu(command, 3, 25);
    setRegister(a, value);
}

/* Unpacks and returns the opcode from a given um instruction*/
static inline uint32_t getOpCode(uint32_t command)
{
    return Bitpack_getu(command, 4, 28);
}

/* Unpacks and returns the first register from a given um instruction*/
static inline uint32_t getA(uint32_t command)
{
    return Bitpack_getu(command, 3, 6); 
}

/* Unpacks and returns the second register from a givenum instruction*/
static inline uint32_t getB(uint32_t command)
{
    return Bitpack_getu(command, 3, 3);
}

/* Unpacks and returns the third register from a given um instruction*/
static inline uint32_t getC(uint32_t command)
{
    return Bitpack_getu(command, 3, 0);
}


