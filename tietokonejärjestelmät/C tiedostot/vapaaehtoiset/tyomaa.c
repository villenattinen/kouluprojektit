#include <stdio.h>
#include <math.h>

const double PI = 3.1416;

double laske_nelion_ala(double sivu);
double laske_kolmion_ala(double sivu);
double laske_sektorin_ala(double sade, double kulma);
double laske_kateetin_pituus(double hypotenuusa);

double laske_nelion_ala(double sivu) {
    return pow(sivu, 2);
}

double laske_kolmion_ala(double sivu) {
    return pow(sivu, 2) / 2;
}

double laske_sektorin_ala(double sade, double kulma) {
    return PI * pow(sade, 2) * kulma / 360;
}

double laske_kateetin_pituus(double hypotenuusa) {
    return hypotenuusa / sqrt(2);
}

double laske_kuvion_ala(double x) {
    double nelio_1_ala, nelio_2_ala, kolmio_ala, sektori_1_ala, sektori_2_ala, kolmion_sivu;
    nelio_1_ala = laske_nelion_ala(x);
    kolmion_sivu = laske_kateetin_pituus(x);
    kolmio_ala = laske_kolmion_ala(kolmion_sivu);
    nelio_2_ala = laske_nelion_ala(kolmion_sivu * 2);
    sektori_1_ala = laske_sektorin_ala(kolmion_sivu, 45);
    sektori_2_ala = laske_sektorin_ala(kolmion_sivu * 2, 45);
    return nelio_1_ala + nelio_2_ala + kolmio_ala + sektori_1_ala + sektori_2_ala;
}

int main() {
    double x = 32.495;
    printf("Kuvion ala x:n arvolla %f on: %f\n", x, laske_kuvion_ala(x));
    return 0;
}
