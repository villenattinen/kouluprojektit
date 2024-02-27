#include <stdio.h>
#include <stdlib.h>
#include <inttypes.h>
#include <string.h>
#include <math.h>

void laske_kuljettu_matka(struct polku *polku);

struct piste {
  int koordinaatit[3];
  struct piste *seuraava;
};

struct polku {
  double matka;
  struct piste *pisteet;
};

int main() {
    struct piste piste1  = {1,2,3};
    struct piste piste2 = {3,4,6};
    struct piste piste3  = {4,4,4};
    piste1.seuraava = &piste2;
    piste2.seuraava = &piste3;
    piste3.seuraava = NULL;
    struct polku *reitti = {0, &piste1};
    laske_kuljettu_matka(reitti);
    return 0;
}

void laske_kuljettu_matka(struct polku *polku) {
    while (polku->pisteet == NULL) {
    }

}


