#include <bitpack.h>
#include <stdio.h>
#include <except.h>
#include <assert.h>
#include <math.h>

unsigned int MAXWIDTH = 64;

Except_T Bitpack_Overflow = { "Overflow packing bits" };
void printbytes(void *p, unsigned int len);


/*Name - Bitpack_fitsu
Purpose - Checks to see if number n can fit in width unsigned bits
Parameters - n: the number to be checked
             width: the number of bits
return value - true if n can fit in width unsigned bits, false otherwise
effects - None
error,notes,restrictions - width must be less than MAXWIDTH
*/
bool Bitpack_fitsu(uint64_t n, unsigned width)
{
    assert(width <= MAXWIDTH);
    return (n <= pow(2, width) - 1);

}
/*Name - Bitpack_fitss
Purpose - Checks to see if number n can fit in width signed bits
Parameters - n: the number to be checked
             width: the number of bits
return value - true if n can fit in width signed bits, false otherwise
effects - None
error,notes,restrictions - width must be less than MAXWIDTH
*/
bool Bitpack_fitss(int64_t n, unsigned width)
{
      assert(width <= MAXWIDTH);
      return  n >= (pow(2, width - 1) * -1)  && (n <= pow(2, width - 1) - 1);
}

/*Name - Bitpack_getu
Purpose - Retrieves an unsigned value that has been packed into a word
Parameters - word: the word the value is being retrived from
             width: the number of bits the value takes up
             lsb: the least significant bit of the value being retrieved
return value - the value that has been retrieved from the word
effects - None
error,notes,restrictions - width must be greater than 0, and width+lsb must be
                            less than MAXWIDTH
*/
uint64_t Bitpack_getu(uint64_t word, unsigned width, unsigned lsb)
{

    assert(0 < width && width + lsb <= MAXWIDTH);
    uint64_t mask = ~0;
    mask = mask >> (MAXWIDTH - width) << lsb;
    word = word & mask;
    return word >> lsb;
}
/*Name - Bitpack_gets
Purpose - Retrieves a signed value that has been packed into a word
Parameters - word: the word the value is being retrived from
             width: the number of bits the value takes up
             lsb: the least significant bit of the value being retrieved
return value - the value that has been retrieved from the word
effects - None
error,notes,restrictions - width must be greater than 0, and width+lsb must be
                            less than MAXWIDTH
*/
int64_t Bitpack_gets(uint64_t word, unsigned width, unsigned lsb)
{
    assert(0 < width && width + lsb <= MAXWIDTH);
    int64_t result = Bitpack_getu(word, width, lsb);
    if(!Bitpack_fitss(result, width)){
        uint64_t mask = ~0;
        mask = mask << width;
        result = result | mask;
    }

    return result;
}
/*Name - Bitpack_newu
Purpose - Packs an unsigned value into the input word
Parameters - uint64_t word-the word to pack to, unsigned width-the width in the
                                                     word that will be taken up
unsigned lsb-the least significant bite in the word for the value
uint64_t value-the value that is being packed
return value - a word with the value packe inside
effects - None
error,notes,restrictions - width must be greater than 0, and width+lsb must be
                            less than MAXWIDTH
*/
uint64_t Bitpack_newu(uint64_t word, unsigned width, unsigned lsb, uint64_t
                        value)
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

/*Name - Bitpack_news
Purpose - Packs an signed value into the input word
Parameters - uint64_t word-the word to pack to, signed width-the width in the
                                                     word that will be taken up
unsigned lsb- the least significant bite in the word for the value
uint64_t value- the value that is being packed
return value - a word with the value packe inside
effects - None
error,notes,restrictions - width must be greater than 0, and width+lsb must be
                            less than MAXWIDTH
*/
 uint64_t Bitpack_news(uint64_t word, unsigned width, unsigned lsb, int64_t
                        value)
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
