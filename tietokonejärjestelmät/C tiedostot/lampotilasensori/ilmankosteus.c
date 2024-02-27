#include <inttypes.h>
#include <math.h>

float kosteus(uint16_t rekisteri);

float kosteus(uint16_t rekisteri) {

    float ilmankosteus = (rekisteri / (double)65536) * (double)100;

    return ilmankosteus;
}
