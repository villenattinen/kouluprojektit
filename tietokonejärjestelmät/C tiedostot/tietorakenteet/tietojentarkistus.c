#include <stdio.h>
#include <stdlib.h>
#include <inttypes.h>
#include <string.h>

struct mpudata_t {
    float data[6];
};

void tarkistus(struct mpudata_t mpu, uint8_t index, float threshold);

void tarkistus(struct mpudata_t mpu, uint8_t index, float threshold) {
    if (mpu.data[index] > threshold) {
        for (int i = 0; i < 5; i++) {
            printf("%.2f,", mpu.data[i]);
        }
        printf("%.2f", mpu.data[5]);
    }
}
