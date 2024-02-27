#include <stdio.h>
#include <stdlib.h>
#include <inttypes.h>

int8_t gameboard[100] = {
    0,1,2,1,1,1,0,0,0,0,
    0,1,1,0,0,0,0,0,0,0,
    0,1,1,0,0,0,0,0,0,0,
    0,2,1,0,2,2,0,2,0,2,
    0,1,0,0,0,2,0,2,0,2,
    0,1,1,2,1,1,2,1,0,2,
    0,0,2,0,2,2,2,2,2,2,
    0,1,2,0,2,0,2,0,2,2,
    0,2,0,2,0,0,0,0,0,2,
    0,1,0,0,2,2,2,2,0,2
    };
int win_len = 7;
int8_t tictactoe_check(int8_t * gameboard, int win_len);

int main()
{
    printf("Tulos: %d\n", tictactoe_check(gameboard, win_len));
    return 0;
}

int8_t tictactoe_check(int8_t * gameboard, int win_len) {

    int i, j;
    int k = 0;
    int risti = 0;
    int nolla = 0;

    for (i = 0; i < 100; i++) {
        if (gameboard[i] != 0) {
            k = 0;
            if (((10 - (i % 10)) >= win_len) && ((10 - (i / 10)) >= win_len)) {
                k = 0;
                for (j=i; j < (i + (11 * win_len)); j+=11) {
                    if (gameboard[j] == gameboard[i]) {
                        k+=1;
                    }
                    else {
                        break;
                    }
                }
                printf("Ov: %d, %d\n", i, k);
            }
            if ((k < win_len) && ((1 + (i % 10)) >= win_len) && ((10 - (i / 10)) >= win_len)) {
                k = 0;
                for (j=i; j < (i + (9 * win_len)); j+=9) {
                    if (gameboard[j] == gameboard[i]) {
                        k+=1;
                    }
                    else {
                        break;
                    }
                }
                printf("Vv: %d, %d\n", i, k);
            }
            if ((k < win_len) && ((10 - (i % 10)) >= win_len)) {
                k = 0;
                for (j=i; j < (i + win_len); j++) {
                    if (gameboard[j] == gameboard[i]) {
                        k+=1;
                    }
                    else {
                        break;
                    }
                }
                printf("O: %d, %d\n", i, k);
            }
            if ((k < win_len) && ((10 - (i / 10)) >= win_len)) {
                k = 0;
                for (j=i; j < (i + (10 * win_len)); j+=10) {
                    if (gameboard[j] == gameboard[i]) {
                        k+=1;
                    }
                    else {
                        break;
                    }
                }
                printf("A: %d, %d\n", i, k);
            }
            if (k >= win_len) {
                if (gameboard[i] == 1) {
                    risti = 1;
                }
                else if (gameboard[i] == 2) {
                    nolla = 1;
                }
            }
        }
    }
    printf("%d\n", 11 % 10);
    if ((risti == 1) && (nolla != 1)) {
        return 1;
    }
    else if ((nolla == 1) && (risti != 1)) {
        return 2;
    }
    else {
        return 0;
    }
}
