#include <bitpack.h>
#include <stdio.h>
#include <except.h>
#include <assert.h>
#include <math.h>

unsigned int MAXWIDTH = 64;

Except_T Bitpack_Overflow = { "Overflow packing bits" };
void printbytes(void *p, unsigned int len);

// int main()
// {
//
//     uint64_t word = 16;
//     int64_t value = -7;
//     unsigned width = 64;
//     unsigned lsb = 0;
//     uint64_t result = Bitpack_news(word, width, lsb, value);
//     //int x = (int) result;
//     //printf("%ld\n", x);
//     int64_t get = Bitpack_gets(result, width, lsb);
//     printf("%d\n", (int) get);
//
//
// }

/*
 * Print bytes in memory in hex
 */
// void printbytes(void *p, unsigned int len)
// {
//   unsigned int i;
//   unsigned char *cp = (unsigned char *)p;
//   *cp += len - 1;
//   for (i = 0; i < len; i++) {
//     printf("%02X", *cp--);
//   }
//   printf("\n");
// }

bool Bitpack_fitsu(uint64_t n, unsigned width)
{
    assert(width <= MAXWIDTH);
    return (n <= pow(2, width) - 1);

}
bool Bitpack_fitss(int64_t n, unsigned width)
{
      assert(width <= MAXWIDTH);
      return  n >= (pow(2, width - 1) * -1)  && (n <= pow(2, width - 1) - 1);
}

uint64_t Bitpack_getu(uint64_t word, unsigned width, unsigned lsb)
{

    assert(0 < width && width + lsb <= MAXWIDTH);
    uint64_t mask = ~0;
    mask = mask >> (MAXWIDTH - width) << lsb;
    word = word & mask;
    return word >> lsb;
}

int64_t Bitpack_gets(uint64_t word, unsigned width, unsigned lsb)
{
    // printf("width: %d\n", (int) (width));
    assert(0 < width && width + lsb <= MAXWIDTH);
    int64_t result = Bitpack_getu(word, width, lsb);
    // printf("result: %d\n", (int) result);
    if(!Bitpack_fitss(result, width)){
        uint64_t mask = ~0;
        mask = mask << width;
        result = result | mask;
    }

    return result;
}

uint64_t Bitpack_newu(uint64_t word, unsigned width, unsigned lsb, uint64_t value)
{
    assert(width > 0 && width + lsb <= MAXWIDTH);
    if(!Bitpack_fitsu(value, width)){
        RAISE(Bitpack_Overflow);
    }
    uint64_t mask = ~0;
    mask = mask >> (MAXWIDTH - width) << lsb;
    mask = ~mask;

    word = word & mask;
    value = value << lsb;
    word = word | value;
    return word;

}


 uint64_t Bitpack_news(uint64_t word, unsigned width, unsigned lsb, int64_t value)
 {
     assert(width > 0 && width + lsb <= MAXWIDTH);
     if(!Bitpack_fitss(value, width)){
         RAISE(Bitpack_Overflow);
     }
     uint64_t mask = ~0;
     mask = mask >> (MAXWIDTH - width);
     value = mask & value;

     return Bitpack_newu(word, width, lsb, value);
 }
