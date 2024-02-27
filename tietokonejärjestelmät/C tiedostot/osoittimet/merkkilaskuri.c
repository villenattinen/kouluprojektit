#include <stdio.h>
#include <stdlib.h>
#include <inttypes.h>
#include <string.h>

void merkkilaskuri(char *str, uint8_t *tulos);

void merkkilaskuri(char *str, uint8_t *tulos) {

    int i, j, k;
    int v = 0;
    int c = 0;
    printf("%d, %d\n", v, c);
    char vokaalit[] = "aeiouAEIOU";
    char konsonantit[] = "bcdfghjklmnpqrstvwxyzBCDFGHJKLMNPQRSTVWXYZ";

    for (i = 0; i < strlen(str); i++) {
        for (j = 0; j < strlen(vokaalit); j++) {
            if (str[i] == vokaalit[j]) {
                v++;
                break;
            }
        }
        for (k = 0; k < strlen(konsonantit); k++) {
            if (str[i] == konsonantit[k]) {
                c++;
                break;
            }
        }
    }
    tulos[0] = v;
    tulos[1] = c;
}
