#include <stdio.h>
#include <stdlib.h>
#include <inttypes.h>

void movavg(float *array, uint8_t array_size, uint8_t window_size);

void movavg(float *array, uint8_t array_size, uint8_t window_size) {
    int i, j;

    for (i = 0; i < array_size-window_size+1; i++) {

        float avg = 0;

        for (j = i; j < i+window_size; j++) {
                avg+=array[j];
        }
        if (i < array_size-window_size) {
        printf("%.2f,", avg / window_size);
        }
        else {
        printf("%.2f", avg / window_size);
        }
    }
}
