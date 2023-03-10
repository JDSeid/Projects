#include <compress40.h>
#include <arith40.h>
#include <pnm.h>
#include <a2plain.h>
#include <stdio.h>
#include <math.h>
#include <mem.h>
#include <stdlib.h>
#include <bitpack.h>
#include <assert.h>

struct quantized{

  unsigned a, pb, pr;
  int b, c, d;
};
typedef struct quantized quantized;

struct videoPixel{
    float y, pb, pr;
};

typedef struct videoPixel videoPixel;


typedef A2Methods_UArray2 A2;

static const int BLOCKSIZE = 4;
static const int DENOMINATOR = 255;


Pnm_ppm readAndTrim(FILE *fp);
videoPixel convert(Pnm_rgb pixel, int denominator);
struct Pnm_rgb revert(videoPixel vp, int denominator);
quantized calculate(videoPixel pixels[]);
videoPixel* decode(quantized q);
void pack(quantized q);
quantized unpack(uint32_t word);
Pnm_ppm ppmMaker(A2 pixels, unsigned width, unsigned height);


/*Name - compress40
Purpose - Compress an image taken in through the given stream. Prints the
image bitwise to stdout.
Parameters - FILE *fp: the stream from which the image is to be read in
return value - void
effects - Prints the compressed bitwiseimage to stdout
errors,notes,restrictions - fp must be nonnull. A valid ppm must be provided
*/
void compress40(FILE* fp)
{
    Pnm_ppm image = readAndTrim(fp);
    printf("COMP40 Compressed image format 2\n%u %u\n", image->width,
                                                    image->height);
    videoPixel vps[BLOCKSIZE];

    for(int i = 0; i < (int)image->height; i += 2){
        for(int j = 0; j < (int)image->width; j += 2){
            vps[0] = convert(((Pnm_rgb)image->methods->
                                at(image->pixels, j, i)),
                                        image->denominator);
            vps[1] = convert(((Pnm_rgb)image->methods->
                                at(image->pixels, j + 1, i)),
                                        image->denominator);
            vps[2] = convert(((Pnm_rgb)image->methods->
                                at(image->pixels, j, i + 1)),
                                        image->denominator);
            vps[3] = convert(((Pnm_rgb)image->methods->
                                at(image->pixels, j + 1, i + 1)),
                                        image->denominator);
            quantized q = calculate(vps);
            pack(q);
        }
    }
    Pnm_ppmfree(&image);

}
/*Name - decompress40
Purpose - decompresses an image read in through the given stream.
            Writes the decompressed image to stdout in ppm format
Parameters - FILE *fp: the stream from which the image is to be read in
Return value - void
effects - Prints the decompressed image to stdout in ppm format
errors,notes,restrictions - fp must be nonnull. A valid compressed image
                            must be provided
*/
void decompress40(FILE *fp)
{
  unsigned height, width;
  int read = fscanf(fp, "COMP40 Compressed image format 2\n%u %u", &width,
                                                                   &height);
  assert(read == 2);
  int c = getc(fp);
  A2 output = uarray2_methods_plain->new(width, height,
                                                    sizeof(struct Pnm_rgb));
  assert(c == '\n');
  uint32_t word = 0;
  for(unsigned i = 0; i < width * height / 2; i+=2){
      word = 0;
      for(int j = 0; j < 32; j++){
          if(getc(fp) == '1'){
              word += pow(2, 32 - j - 1);
          }
      }
      quantized q = unpack(word);
      videoPixel *vps = decode(q);
      struct Pnm_rgb rgbs[4];
      for(int k = 0; k < BLOCKSIZE; k++){
          rgbs[k] = revert(vps[k], DENOMINATOR);
      }
      free(vps);
      unsigned col = i % width;
      unsigned row = (i / width) * 2;
      *((Pnm_rgb)uarray2_methods_plain->at(output, col, row)) = rgbs[0];
      *((Pnm_rgb)uarray2_methods_plain->at(output, col + 1, row)) = rgbs[1];
      *((Pnm_rgb)uarray2_methods_plain->at(output, col, row + 1)) = rgbs[2];
      *((Pnm_rgb)uarray2_methods_plain->at(output, col + 1,row + 1)) = rgbs[3];


  }

  Pnm_ppm image = ppmMaker(output, width, height);
  Pnm_ppmwrite(stdout, image);
  Pnm_ppmfree(&image);
}
/*Name - readAndtrim
Purpose - reads in a ppm image from the given stream and returns a fully
            initialized pnm_ppm instance that has been trimmed such that its
            height and width are even
Parameters - FILE *fp: the stream from which the image is to be read in
return value - A fully initialized pnm_ppm instance with even height and width
effects - None
errors,notes,restrictions - fp must be nonnull. A valid compressed image
                            must be provided
*/
  Pnm_ppm readAndTrim(FILE *fp)
  {
      assert(fp != NULL);
      Pnm_ppm image = Pnm_ppmread(fp, uarray2_methods_plain);
      unsigned newWidth = image->width;
      unsigned newHeight = image->height;
      if(image->width % 2 == 1){
          newWidth = image->width - 1;
      }
      if(image->height % 2 == 1){
          newHeight = image->height - 1;
      }
      if(image->width != newWidth || image->height != newHeight){
          A2 newArray = uarray2_methods_plain->new(newWidth,
                                        newHeight, sizeof(struct Pnm_rgb));
          for(unsigned j = 0; j < newHeight; j++){
              for(unsigned i = 0; i < newWidth; i++) {
                  *((Pnm_rgb)uarray2_methods_plain->at(newArray, i, j)) =
                      *((Pnm_rgb)uarray2_methods_plain->at(image->pixels, i, j));
              }
          }
          uarray2_methods_plain->free(&(image->pixels));
          image->pixels = newArray;
          image->width = newWidth;
          image->height = newHeight;

      }

      return image;
  }

  /*Name - ppmMaker
  Purpose - Creates a ppm with the given pixel A2, width, and height
  Parameters - A2 pixels: a 2d array of pixels to be given to the ppm instance
               unsigned width: the width of the ppm
               unsigned height: the height of the ppm
  return value - A fully initialized ppm
  effects - none
  errors,notes,restrictions - width and height must be nonzero
  */
Pnm_ppm ppmMaker(A2 pixels, unsigned width, unsigned height)
{
    Pnm_ppm image = malloc(sizeof(struct Pnm_ppm));
    image->pixels = pixels;
    image->width = width;
    image->height = height;
    image->denominator = DENOMINATOR;
    image->methods = uarray2_methods_plain;
    return image;
}
