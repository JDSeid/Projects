#include <pnm.h>
#include <assert.h>


struct videoPixel{
    float y, pb, pr;
};

typedef struct videoPixel videoPixel;

/*Name - convert
Purpose - convers a given pixel from RGB to video pixel format
Parameters - Pnm_rgb pixel: the pixel to be converted
             int denominator: teh denominator to use in the conversion
return value - a videoPixel struct containing the converted RGB values
effects - none
errors,notes,restrictions - pixel must have its red, green, and blue values
                            initialized
                          - denominator must be > 0
*/
videoPixel convert(Pnm_rgb pixel, int denominator)
{
    assert(denominator > 0);
    videoPixel vp;
    vp.y = 0.299 * pixel->red + 0.587 * pixel->green + 0.114 * pixel->blue;
    vp.y /= denominator;
    vp.pb = -0.168736 * pixel->red - 0.331264 * pixel->green + 0.5 *
                                                                pixel->blue;
    vp.pb /= denominator;
    vp.pr = 0.5 * pixel->red - 0.418688 * pixel->green - 0.081312 *
                                                                pixel->blue;
    vp.pr /= denominator;
    return vp;
}



/*Name - revert
Purpose - converts a given video pixel struct to an RGB pixel
Parameters - videoPixel vp: the pixel to be converted
             int denominator: teh denominator to use in the conversion
return value - a Pnm_rgb struct containing the converted video pixel values
effects - none
errors,notes,restrictions - vp must have its y, pb, and pr values
                            initialized
                          - denominator must be > 0
*/
struct Pnm_rgb revert(videoPixel vp, int denominator)
{
    struct Pnm_rgb pixel;
    float red = (1.0 * vp.y + 0.0 * vp.pb + 1.402 * vp.pr) * denominator;
    if(red < 0){
        red = 0;
    }
    if(red > denominator){
        red = denominator;
    }
    pixel.red = red;
    float green = (1.0 * vp.y - 0.344136 * vp.pb - 0.714136 * vp.pr) *
                                              denominator;
    if(green < 0){
        green = 0;
    }
    if(green > denominator){
        green = denominator;
    }
    pixel.green = green;
    float blue = (1.0 * vp.y + 1.772 * vp.pb + 0.0 * vp.pr) * denominator;
    if(blue < 0){
        blue = 0;
    }
    if(blue > denominator){
        blue = denominator;
    }
    pixel.blue = blue;
    return pixel;
}
