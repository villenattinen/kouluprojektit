#include <time.h>
#include <inttypes.h>
#include <stdio.h>
#include <limits.h>
#include <stdlib.h>

int main() {

    int n = 5;
    int x = 0;
    for(int i = 1; i <=n; i++) {
        int y = 1;
        for(int j = 1; j <= i; j++) {
            y = y*j;
        }
        x += y;
}
    printf("%d",x);
    return 0;
}
