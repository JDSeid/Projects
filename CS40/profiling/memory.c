/* HW6 UM, Comp40 Tufts
 * memory.c
 * Addison Mirliani and Jacob Seidman
 * 4.19.21
 * This is the implementation of the memory module of the um
 */
 
#include "memory.h"

static Stack_T ids;

static uint32_t* makeWordArray(int length);
static uint32_t *initRegisters();

/* Initializes the memory so that the ids are empty, registers are all zero,
   and segment zero is initialized to the given length and filled with zeroes */
void initMemory(int numCommands)
{
    counter = 1;
    segments = Seq_new(256);
    ids = Stack_new();
    registers = initRegisters();
    mapSegment(numCommands);
    segment0 = (uint32_t *)Seq_get(segments, 0);
}

/* Creates a new segment of given length and sets all of its words equal to
   zero. It reaturns the index of where in memory the segment was placed (the
   segment ID) */
uint32_t mapSegment(uint32_t length)
{
    //fprintf(stderr, "in map segment\n");
    if(Stack_empty(ids)){
        Seq_addhi(segments, (void *)makeWordArray(length));
        return Seq_length(segments) - 1;
    }
    else{
        int index = (int)(uintptr_t)Stack_pop(ids);
        Seq_put(segments, index, (void*)makeWordArray(length));
        return index;
    }
}

/* Frees the segment in the given position */
void unmapSegment(uint32_t index)
{
    Stack_push(ids, (void *)(uintptr_t)index);
    uint32_t *segment = (uint32_t *)Seq_get(segments, index);
    Seq_put(segments, index, NULL);
    free(segment);
}

/* Duplicates the segment at m[segmentID] and replaces segment zero with the
   duplicated segment. It also changes the program counter 
   position to the given value
*/
void loadProgram(uint32_t segmentID, uint32_t counterPosition)
{
    counter = counterPosition + 1;
    if(segmentID == 0){
        return;
    }
    uint32_t *segment = (uint32_t *)Seq_get(segments, segmentID);
    unmapSegment(0);
    mapSegment(segment[0]);
    for(uint32_t i = 0; i < segment[0]; i++){
        ((uint32_t *)Seq_get(segments, 0))[i] = segment[i];
    }
    segment0 = (uint32_t *)Seq_get(segments, 0);
}

/* Frees all memory associated with the segments, registers, and the stack 
    of IDS */
void freeAllMemory()
{   
    /* free all segments, and the sequence of segments */
    for(int i = 0; i < (int)Seq_length(segments); i++){
        uint32_t *segment = (uint32_t *)Seq_get(segments, i);
        if(segment != NULL){
            free(segment);
        }
    }
    
    Seq_free(&segments);
    
    /* free the array of registers */
    free(registers);
    
    /* free the stack */
    Stack_free(&ids);
    
}
/* Returns an array of uint32_t's with the given length+1. The 0th index is 
    initliaed to the contain length of the array, and all other indices are
    initialized to 0.
    Constraint: length >=0 */
static uint32_t *makeWordArray(int length)
{
    uint32_t* arr = malloc((length+1) * sizeof(uint32_t));
    
    arr[0] = (uint32_t)(length+1);
    for(uint32_t i = 1; i < (uint32_t)(length + 1); i++){
        arr[i] = 0;
    }
    return arr;
}

/*Returns an array of 8 uint32_t's. Initializes all values to 0  */
static uint32_t *initRegisters()
{
    uint32_t* arr = malloc(NUM_REGISTERS * sizeof(uint32_t));
    for(int i = 0; i < NUM_REGISTERS ; i++){
        arr[i] = 0;
    }
    return arr;
}


