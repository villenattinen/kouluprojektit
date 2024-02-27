#include <inttypes.h>

float lampotila(uint16_t rekisteri, float kerroin);

float lampotila(uint16_t rekisteri, float kerroin) {

    uint16_t korjattu_binaari = rekisteri >> 2;
    float celsius_lampotila = korjattu_binaari * kerroin;

    return celsius_lampotila;
}
