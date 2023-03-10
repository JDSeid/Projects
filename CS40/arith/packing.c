#include <bitpack.h>
#include <stdio.h>


struct quantized{

  unsigned a, pb, pr;
  int b, c, d;
};

typedef struct quantized quantized;

const int WORDLENGTH = 32;

/*Name - printBits
Purpose - print out word in readable binary
Parameters - uint32_t n- the word to be printed out
return value - None
effects - prints out to screen readable binary from the input word
error,notes,restrictions - None
*/
void printBits(uint32_t n)
{
    int binary[32];
    for(int i = 0; i < 32; i++){
        binary[i] = 0;
    }
    int index = 0;
    while(n > 0){
        binary[index] = n % 2;
        n /= 2;
        index++;
    }
    for(int j = 31; j >= 0; j--){
        printf("%d", binary[j]);
    }
}

/*Name - Pack
Purpose - packs data from qauntized struct given into a uint32_t word
Parameters - quantized q- the data you want to pack
return value - None
effects - Creates a word with the data given packed inside
error,notes,restrictions - None
*/
void pack(quantized q)
{
    uint32_t word = 0;
    word = Bitpack_newu(word, 4, 0, q.pr);
    word = Bitpack_newu(word, 4, 4, q.pb);
    word = Bitpack_news(word, 5, 8, q.d);
    word = Bitpack_news(word, 5, 13, q.c);
    word = Bitpack_news(word, 5, 18, q.b);
    word = Bitpack_newu(word, 9, 23, q.a);
    printBits(word);
}

/*Name - Unpack
Purpose -Takes a packed word and puts the data into a qaunitzed stuct
Parameters - uint32_t word
return value - Quantized struct- contains data that was packed in the input
word
effects - None
error,notes,restrictions - None
*/
quantized unpack(uint32_t word)
{
    quantized q;
    q.pr = Bitpack_getu(word, 4, 0);
    q.pb = Bitpack_getu(word, 4, 4);
    q.d = Bitpack_gets(word, 5, 8);
    q.c = Bitpack_gets(word, 5, 13);
    q.b= Bitpack_gets(word, 5, 18);
    q.a = Bitpack_getu(word, 9, 23);
    return q;
}
