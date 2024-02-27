#include <stdio.h>
#include <stdlib.h>
#include <inttypes.h>
#include <string.h>

char lista[] = "201,53,12,31,5";

float keskiarvo(char *lista);

int main() {

    printf("%f\n", keskiarvo(lista));

return 0;
}

float keskiarvo(char *lista) {

    char *token;
    float vali_arvo = 0;
    float k_arvo = 0;
    float nimittaja = 0;

    for (token = strtok(lista, ","); token; token = strtok(NULL, ",")) {
        vali_arvo+=atof(token);
        printf("%f\n", vali_arvo);
        nimittaja++;
    }
    printf("%f\n", nimittaja);
    k_arvo = vali_arvo / nimittaja;
    return k_arvo;
}
