#include <stdio.h>
#include <stdlib.h>
#include <inttypes.h>
#include <time.h>

void shuffle(uint8_t *list, uint16_t list_size);

void shuffle(uint8_t *list, uint16_t list_size) {

    int i, j, k, alkio, alkio2;

    for (i = 0; i < list_size-1; i++) {
        j = rand() % (list_size-i) + i;
        alkio = list[j];
        for (k = j; k > i; k--) {
            alkio2 = list[k-1];
            list[k] = alkio2;
        }
        list[i] = alkio;
    }
}
