#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <math.h>
#include "pnm.h"
#include "a2methods.h"
#include "a2blocked.h"

typedef A2Methods_UArray2 A2;

int main(int argc, char* argv[])
{
A2 pixels = uarray2_methods_blocked->new(3, 2, sizeof(struct Pnm_rgb));
Pnm_ppm image = malloc(sizeof(struct Pnm_ppm));
image->pixels = pixels;
image->methods = uarray2_methods_blocked;
image->height = 2;
image->width = 3;
image->denominator = 100;

((struct Pnm_rgb*)image->methods->at(image->pixels, 0, 0))->red = 50;
((struct Pnm_rgb*)image->methods->at(image->pixels, 0, 0))->green = 100;
((struct Pnm_rgb*)image->methods->at(image->pixels, 0, 0))->blue = 1;

((struct Pnm_rgb*)image->methods->at(image->pixels, 0, 1))->red = 75;
((struct Pnm_rgb*)image->methods->at(image->pixels, 0, 1))->green = 75;
((struct Pnm_rgb*)image->methods->at(image->pixels, 0, 1))->blue = 75;

((struct Pnm_rgb*)image->methods->at(image->pixels, 1, 0))->red = 25;
((struct Pnm_rgb*)image->methods->at(image->pixels, 1, 0))->green = 25;
((struct Pnm_rgb*)image->methods->at(image->pixels, 1, 0))->blue = 0;

((struct Pnm_rgb*)image->methods->at(image->pixels, 1, 1))->red = 20;
((struct Pnm_rgb*)image->methods->at(image->pixels, 1, 1))->green = 40;
((struct Pnm_rgb*)image->methods->at(image->pixels, 1, 1))->blue = 60;

((struct Pnm_rgb*)image->methods->at(image->pixels, 2, 1))->red = 0;
((struct Pnm_rgb*)image->methods->at(image->pixels, 2, 1))->green = 0;
((struct Pnm_rgb*)image->methods->at(image->pixels, 2, 1))->blue = 0;

Pnm_ppmwrite(stdout, image);
(void) argc;
(void) argv;

}
