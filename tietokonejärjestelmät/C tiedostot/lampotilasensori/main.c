#include <stdio.h>
#include <stdlib.h>
#include <inttypes.h>
#include <math.h>

//float kerroin = 0.03125;
//uint16_t rekisteri = 0b0110110000110101;
uint8_t xlsb=0b11011110;
uint8_t lsb=0b10111100;
uint8_t msb=0b00011000;

//float lampotila(uint16_t rekisteri, float kerroin);
//float kosteus(uint16_t rekisteri);
//float valoisuus(uint16_t rekisteri);
uint32_t ilmanpaine(uint8_t xlsb, uint8_t lsb, uint8_t msb);

int main() {

    float tulos = ilmanpaine(xlsb, lsb, msb);
    printf("%f\n", tulos);

    return 0;
}

uint32_t ilmanpaine(uint8_t xlsb, uint8_t lsb, uint8_t msb) {

    uint32_t bitit = msb << 8;
    bitit = (bitit | lsb) << 8;
    bitit = (bitit | xlsb) >> 4;
    return bitit;
}

/*
float valoisuus(uint16_t rekisteri) {

    //uint16_t e_maski = 0xF000;
    //uint16_t r_maski = 0x0FFF;
    uint16_t e_bitit = (rekisteri & 0xF000) >> 12;
    uint16_t r_bitit = rekisteri & 0x0FFF;
    float luksi = (double)0.01 * pow(2, e_bitit) * r_bitit;

    return luksi;
}

float lampotila(uint16_t rekisteri, float kerroin) {

    uint16_t korjattu_binaari = rekisteri >> 2;
    float celsius_lampotila = korjattu_binaari * kerroin;

    return celsius_lampotila;
}


float kosteus(uint16_t rekisteri) {

    float ilmankosteus = (rekisteri / (double)65536) * (double)100;

    return ilmankosteus;
}
*/
