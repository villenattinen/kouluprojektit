#include <inttypes.h>

int64_t laske_kertoma(int8_t n);

int64_t laske_kertoma(int8_t n) {

    uint64_t kertoma = n;

    if (n > 20) {
        kertoma = -1;
    }
    else if (n <= 20) {
        uint8_t i;
        for(i=1; i<n; i++){
            kertoma = kertoma*i;
        }
    }
    return kertoma;
}
