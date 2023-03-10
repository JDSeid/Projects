/* HW6 UM, Comp40 Tufts
 * um.c
 * Addison Mirliani and Jacob Seidman
 * 4.19.21
 * This is the driver file for the um, which stores the program in memory and
 * starts the um.
 */

#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <stdbool.h>
#include <bitpack.h>
#include <sys/stat.h>
#include "memory.h"
#include "commands.h"

FILE *open_or_abort(char *fname);
uint32_t read_word(FILE* file);

int main(int argc, char *argv[])
{
    if (argc != 2) {
        fprintf(stderr, "Usage: %s <filename>\n", argv[0]);
        exit(EXIT_FAILURE);
    }
    
    FILE *inputFile = open_or_abort(argv[1]);
    struct stat st; /*Get info about input file */
    if (stat(argv[1], &st)){
        fprintf(stderr, "Could not open file %s\n", argv[1]);
        exit(EXIT_FAILURE);
    }
    
    int numWords = st.st_size / 4; /*Find the number of words in input file */
    
    /* initialiaze segment 0 to contain the correct number of words */
    initMemory(numWords);
    /*Fills segment 0 with all the words from the input file */
    for(int i = 0; i < numWords; i++){
        uint32_t word = read_word(inputFile);
        setWord(0, i, word);
    }
    fclose(inputFile);
    
    runProgram(); /*Starts the um */

    return 0;
    
}

/* Reads in and returns a single 32 bit word from the given file.
    file must be non NULL
*/
uint32_t read_word(FILE* file)
{
    uint32_t packed_word = 0;

    for (int j = 3; j >= 0; j--) {
        uint32_t next_c = getc(file);
        packed_word = Bitpack_newu(packed_word, 8, 8 * j, next_c);
    }
    
    return packed_word;
    
    
}

/* Opens a file for reading and exits with EXIT_FAILURE if the file cannot be
   opened. This returns a FILE pointer to the file.
 */
FILE *open_or_abort(char *fname)
{
    FILE *fp = fopen(fname, "r");
    if (fp == NULL) {
        fprintf(stderr, "Could not open file %s\n", fname);
        exit(EXIT_FAILURE);
        }
        return fp;
}
