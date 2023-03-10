#include <compress40.h>
#include <arith40.h>
#include <math.h>
#include <assert.h>
#include <stdlib.h>

static const int BLOCKSIZE = 4;

struct quantized{

  unsigned a, pb, pr;
  int b, c, d;
};
typedef struct quantized quantized;

struct videoPixel{
    float y, pb, pr;
};

typedef struct videoPixel videoPixel;



/*Name - Quantize
Purpose - Make sure qauntized values are in range of 15 to -15
Parameters - Float chroma-the value your trying check
return value - int-The in range chroma value
effects - None
error,notes,restrictions - None
*/
int quantize(float chroma)
{
    chroma *= (31/.3);
    if(chroma > 31)
    {
        chroma = 31;
    }
    if(chroma < -31)
    {
        chroma = -31;
    }
    return (int)round(chroma);
}

/*Name - Calculate
Purpose - Take in values and return a struct with the qaunitzed versions
Parameters - videoPixel pixels[BLOCKSIZE]- The videoPixel struct being
                being calcuated
return value - qauntized struct- conatining the qauntized versions of the
               inputed values
effects - None
error,notes,restrictions - All pixels in the pixels array must have their
                            y, pb, and pr values initialized
*/
quantized calculate(videoPixel pixels[BLOCKSIZE])
{
    assert(pixels != NULL);
    quantized q;
    float pb = 0, pr = 0, a = 0, b = 0, c = 0, d = 0;
    for(int i = 0; i < BLOCKSIZE; i++){
        pb += pixels[i].pb;
        pr += pixels[i].pr;
    }
    pb /= BLOCKSIZE;
    pr /= BLOCKSIZE;
    q.pb = Arith40_index_of_chroma(pb);
    q.pr = Arith40_index_of_chroma(pr);
    a = (pixels[3].y + pixels[2].y + pixels[1].y + pixels[0].y)/ BLOCKSIZE;
    b = (pixels[3].y + pixels[2].y - pixels[1].y - pixels[0].y) / BLOCKSIZE;
    c = (pixels[3].y - pixels[2].y + pixels[1].y - pixels[0].y) / BLOCKSIZE;
    d = (pixels[3].y - pixels[2].y - pixels[1].y + pixels[0].y) / BLOCKSIZE;
    q.b = quantize(b);
    q.c = quantize(c);
    q.d = quantize(d);
    q.a = a * 63;
    return q;

}


/*Name - Decode
Purpose - Convert qaunitzed values given to unqaunitzed forms
Parameters - Quantized q- a qauntized struct containing qaunitzed values
return value - VideoPixel Struct- containing the unqaunitzed values
effects - None
error,notes,restrictions - q must have all of its members initialized
*/
videoPixel* decode(quantized q)
{
    videoPixel *vps = malloc(sizeof(videoPixel) * BLOCKSIZE);
    assert(vps != NULL);
    for(int i = 0; i < BLOCKSIZE; i++){
        vps[i].pr = Arith40_chroma_of_index(q.pr);
        vps[i].pb = Arith40_chroma_of_index(q.pb);
    }
    float b = (float)q.b/(31/.3);
    float c = (float)q.c/(31/.3);
    float d = (float)q.d/(31/.3);
    float a = (float)q.a/63;
    vps[0].y = a - b - c + d;
    vps[1].y = a - b + c - d;
    vps[2].y = a + b - c - d;
    vps[3].y = a + b + c + d;

    return vps;
}
