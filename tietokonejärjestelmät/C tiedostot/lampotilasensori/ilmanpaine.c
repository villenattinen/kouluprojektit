#include <inttypes.h>

uint32_t ilmanpaine(uint8_t xlsb, uint8_t lsb, uint8_t msb);

uint32_t ilmanpaine(uint8_t xlsb, uint8_t lsb, uint8_t msb) {

    uint32_t bitit = msb << 8;
    bitit = (bitit | lsb) << 8;
    bitit = (bitit | xlsb) >> 4;
    return bitit;
}
