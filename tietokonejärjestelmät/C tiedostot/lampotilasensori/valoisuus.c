#include <inttypes.h>
#include <math.h>

float valoisuus(uint16_t rekisteri);

float valoisuus(uint16_t rekisteri) {

    uint16_t e_bitit = (rekisteri & 0xF000) >> 12;
    uint16_t r_bitit = rekisteri & 0x0FFF;
    float luksi = (double)0.01 * pow(2, e_bitit) * r_bitit;
    return luksi;
}
