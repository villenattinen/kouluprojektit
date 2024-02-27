#include <stdio.h>
#include <stdlib.h>
#include <inttypes.h>
#include <string.h>

float keskiarvo(char *lista);

float keskiarvo(char *lista) {

    char* token;
    float vali_arvo = 0;
    float k_arvo = 0;
    float nimittaja = 0;

    for (token = strtok(lista, ","); token; token = strtok(NULL, ",")) {
        vali_arvo+=atof(token);
        nimittaja++;
    }
    k_arvo = vali_arvo / nimittaja;
    return k_arvo;
}
