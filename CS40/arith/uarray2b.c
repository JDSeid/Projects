/*
 * uarray2b.c
 *
 */

#include <uarray2b.h>
#include <stdlib.h>
#include <stdio.h>
#include <assert.h>
#include <math.h>
#include <uarray2.h>
#include <mem.h>
#include <uarray.h>
#include <stdbool.h>

#define T UArray2b_T

struct T {
    int width;
    int height;
    int size;
    int blocksize;
    UArray2_T blocks;
};

void applyInitialize(int col, int row, UArray2_T uarray2,
                    void *position, void *cl)
{
    (void)col;
    (void)row;
    (void)uarray2;
    int blocksize = (*(T *)cl)->blocksize;
    UArray_T uarray = UArray_new(blocksize * blocksize, (*(T *)cl)->size);
    *(UArray_T *)position = uarray;
}

void applyFree(int col, int row, UArray2_T uarray2, void *position, void *cl)
{
    (void) col;
    (void) row;
    (void) uarray2;
    (void) cl;
    assert(position != NULL && *(UArray_T*)position != NULL);
    UArray_free((UArray_T *)position);
}

T UArray2b_new (int width, int height, int size, int blocksize)
{
    assert(width >= 0 && height >= 0 && size > 0 && blocksize > 0);
    T uarray2b;
    NEW(uarray2b);
    uarray2b->width = width;
    uarray2b->height = height;
    uarray2b->size = size;
    uarray2b->blocksize = blocksize;
    double widthInBlocks = ceil((double)width / (double)blocksize);
    double heightInBlocks = ceil((double)height / (double)blocksize);
    uarray2b->blocks = UArray2_new(widthInBlocks, heightInBlocks, 8);

    /* use mapping to set every index in our blocks to an empty UArray_T */
    UArray2_map_row_major(uarray2b->blocks, applyInitialize, &uarray2b);

    return uarray2b;
}

T UArray2b_new_64K_block(int width, int height, int size)
{
    assert(size > 0);
    int maxBlockSize = 64 * 1024;
    int blocksize;
    if(size > maxBlockSize){
        blocksize = 1;
    }
    else{
        blocksize = sqrt((double)(maxBlockSize / size));
    }

    return UArray2b_new(width, height, size, blocksize);
}
void UArray2b_free (T *array2b)
{
    assert(array2b != NULL && *array2b != NULL);
    printf("about to map\n");
    UArray2_map_row_major((*array2b)->blocks, applyFree, NULL);
    printf("after map\n");
    UArray2_free(&(*array2b)->blocks);
    FREE(*array2b);

}
int UArray2b_width (T array2b)
{
    return array2b->width;
}
int UArray2b_height (T array2b)
{
    return array2b->height;
}
int UArray2b_size (T array2b)
{
    return array2b->size;
}
int UArray2b_blocksize(T array2b)
{
    return array2b->blocksize;
}
/* return a pointer to the cell in the given column and row.
* index out of range is a checked run-time error
*/
void *UArray2b_at(T array2b, int column, int row)
{
    int blocksize = array2b->blocksize;

    assert(array2b != NULL);
    assert(0 <= column && column < array2b->width);
    assert(0 <= row && row < array2b->height);

    int blockCol = column / blocksize;
    int blockRow = row / blocksize;
    int cellIndex = blocksize * (row % blocksize) + (column % blocksize);

    UArray_T *block = (UArray_T *)UArray2_at(array2b->blocks,
                                            blockCol, blockRow);
    assert(block);
    assert(*block);
    return UArray_at(*block, cellIndex);

}

bool checkInBounds(T array2b, int col, int row)
{
    return (0 <= col && col < array2b->width &&
            0 <= row && row < array2b->height);
}

struct cl {
    T array2b;
    void (*apply)(int, int, T , void *, void *);
    void *userCl;
};

void applyToBlocks(int col, int row, UArray2_T uarray2, void *uarray,
                    void *closure)
{
    (void)uarray2;
    /* call apply on each element in the UArray_T */
    UArray_T block = *(UArray_T *)uarray;
    struct cl *clStruct = (struct cl*) closure;
    int blocksize = clStruct->array2b->blocksize;
    for (int i = 0; i < UArray_length(block); i++) {
        /* check that the element is within bounds */
        int col2b = (col * blocksize) + (i % blocksize);
        int row2b = (row * blocksize) + (i / blocksize);
        if ( checkInBounds(clStruct->array2b, col2b, row2b) ) {
            clStruct->apply(col2b, row2b, clStruct->array2b, UArray_at(block, i),
                            clStruct->userCl);
        }
    }
}

 /* visits every cell in one block before moving to another block */
void UArray2b_map(T array2b, void apply(int col, int row, T array2b,
                    void *elem, void *cl), void *cl)
{
    struct cl closure;
    closure.array2b = array2b;
    closure.apply = apply;
    closure.userCl = cl;
    UArray2_map_row_major(array2b->blocks, applyToBlocks, &closure);
}
/*
* it is a checked run-time error to pass a NULL T
* to any function in this interface
*/

#undef T
