#include <stdio.h>
#include <stdlib.h>
#include <stdio.h>
#include <stdlib.h>

double laske_liike_energia(double nopeus, double massa);

double massa = 30;
double nopeus = 20;

int main()
{
    double tulos = laske_liike_energia(nopeus, massa);
    printf("%f", tulos);
    return 0;
}

double laske_liike_energia(double nopeus, double massa) {
    double liike_energia = 0.5 * massa * nopeus * nopeus;
    return liike_energia;
}
