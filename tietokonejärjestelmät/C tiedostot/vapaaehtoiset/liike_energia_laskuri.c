#include <stdio.h>
#include <stdlib.h>
#include <stdio.h>
#include <stdlib.h>

double laske_liike_energia(double nopeus, double massa);

double laske_liike_energia(double nopeus, double massa) {
    double liike_energia = 0.5*massa*nopeus*nopeus;
    return liike_energia;
}
