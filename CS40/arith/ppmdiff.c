#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <math.h>
#include "pnm.h"
#include "a2methods.h"
#include "a2blocked.h"

#define MIN

typedef A2Methods_UArray2 A2;

double imageSim(Pnm_ppm image1, Pnm_ppm image2);
void RMDSApply(int i, int j, A2 a, void *elem, void *cl);


struct closure{
    double sum;
    Pnm_ppm image1;
    Pnm_ppm image2;
};

int main(int argc, char* argv[])
{
    if(argc > 3)
    {
        fprintf(stderr, "Incorrect number of arguments\n");
        exit(EXIT_FAILURE);
    }
    FILE *fp1;
    FILE *fp2;
    if(argc == 1)
    {
      fp1 = stdin;
      fp2 = stdin;
    }
    else if(argc == 2){
        fp1 = stdin;
        fp2 = fopen(argv[1], "r");
    }
    else{
        fp1 = fopen(argv[1], "r");
        fp2 = fopen(argv[2], "r");
    }
    //read in images
    Pnm_ppm image1 = Pnm_ppmread(fp1, uarray2_methods_blocked);
    Pnm_ppm image2 = Pnm_ppmread(fp2, uarray2_methods_blocked);
    double difference = imageSim(image1, image2);
    unsigned width, height;
    if((image1->width) < (image2->width)) {
      width = image1->width;
    }
    else{
        width = image2->width;
    }
    if((image1->height) < (image2->height)) {
        height = image1->height;
    }
    else{
        height = image2->height;
    }

    printf("width: %d\theight: %d\n", width, height);

    difference /= (3 * width * height);
    difference = sqrt(difference);

    printf("%f\n", difference);


    return(0);
}

double imageSim(Pnm_ppm image1, Pnm_ppm image2)
{
    struct closure cl;
    cl.sum = 0;
    cl.image2 = image2;
    cl.image1 = image1;
    image1->methods->map_block_major(image1->pixels, RMDSApply, &cl);
    return cl.sum;
}

void RMDSApply(int i, int j, A2 a, void *elem, void *cl)
{
    struct closure* clPtr = (struct closure*)cl;
    Pnm_ppm image1 = clPtr->image1;
    Pnm_ppm image2 = clPtr->image2;
    A2 array2 = image2->pixels;
    //printf("i: %d\t j: %d\n", i, j);
    if(0 <= i && i < image2->methods->width(a) && 0 <= j &&
        j < image2->methods->height(a) &&
        i < image2->methods->width(array2) &&
        j < image2->methods->height(array2)) {

        double denom1 = image1->denominator;
        double denom2 = image2->denominator;

        double red1 = ((double)((struct Pnm_rgb*)elem)->red) / denom1;
        double green1 = ((double)((struct Pnm_rgb*)elem)->green) / denom1;
        double blue1 = ((double)((struct Pnm_rgb*)elem)->blue) / denom1;

        void* p = image2->methods->at(array2, i, j);

        double red2 = ((double)((struct Pnm_rgb*)p)->red) / denom2;
        double green2 = ((double)((struct Pnm_rgb*)p)->green) / denom2;
        double blue2 = ((double)((struct Pnm_rgb*)p)->blue) / denom2;

        //printf("red1: %f\t red2: %f\n", red1, red2);
        //printf("blue1: %f\t blue2: %f\n", blue1, blue2);
        //printf("green1: %f\t green2: %f\n", green1, green2);
        clPtr->sum += (red1 - red2) * (red1 - red2);
        clPtr->sum += (blue1 - blue2) * (blue1 - blue2);
        clPtr->sum += (green1 - green2) * (green1 - green2);
        //printf("Sum: %f\n", clPtr->sum);
    }
}
